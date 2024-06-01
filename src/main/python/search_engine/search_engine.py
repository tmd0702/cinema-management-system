from flask import Flask, request, jsonify
from flask_cors import CORS, cross_origin
import utils
from mysql_connector import Database
import numpy as np
import asyncio
import json
import mysql.connector
import pandas as pd
from sklearn.metrics.pairwise import cosine_similarity
from sentence_transformers import SentenceTransformer
import tensorflow as tf
from tensorflow.keras.models import Model, load_model
from tensorflow.keras.layers import Input, Embedding, Flatten, Dot, Dense, Concatenate

genre_model = load_model('cb_model.keras')
model = SentenceTransformer('paraphrase-MiniLM-L6-v2')
cf_model = load_model('cf_model.keras')

app = Flask(__name__)

CORS(app, resources={r"/*": {"origins": "*"}})
app.config['CORS_HEADERS'] = 'Content-Type'

db = Database()
keywords_id_map = db.get_keywords_id_map()
mydb = None
cursor = None
try:
    mydb = mysql.connector.connect(
        host="103.42.57.83",
        user="movie_admin",
        password="sa123456"
    )
    cursor = mydb.cursor()
    cursor.execute("USE MOVIE;")
    print("Connected")
except Exception as e:
    print('Error occurred', e)
def fetch_movie_ids():
    try:
        cursor.execute("SELECT ID FROM MOVIES;")
        movie_ids = cursor.fetchall()
        return [movie_id[0] for movie_id in movie_ids]  # Extract the IDs from the fetched data
    except Exception as e:
        print('Error occurred while fetching movie IDs:', e)
        return []
def fetch_communities():
    try:
        cursor.execute("SELECT GROUP_CONCAT(USER_ID) AS USERS FROM REVIEW GROUP BY MOVIE_ID;")
        communities = cursor.fetchall()
        return communities
        # return [community[0] for community in communities]  # Extract the IDs from the fetched data
    except Exception as e:
        print('Error occurred while fetching movie IDs:', e)
        return []
def fetch_ratings():
    try:
        cursor.execute("SELECT USER_ID, MOVIE_ID, RATING FROM REVIEW;")
        ratings = cursor.fetchall()
        return ratings # Extract the IDs from the fetched data
    except Exception as e:
        print('Error occurred while fetching movie IDs:', e)
        return []
movies_data = db.get_movies_metadata_cleaned_df()
embedding_dict = dict()
keywords_dict = dict()

def keywords_dict_init():
    global keywords_dict
    for id in movies_data.id.values:
        try:
            keywords = list(
                set(np.append(keywords_id_map[int(id)], np.array(
                    [utils.preprocessing(movies_data.loc[movies_data.id == int(id), 'title'].iloc[0])]))))
            keywords_dict[int(id)] = keywords
        except:
            print(id, 'not found')
            continue
def embedding_dict_init():
    global embedding_dict
    for id in movies_data.id.values:
        movie_overview = movies_data.loc[movies_data.id == int(id), 'overview'].values[0]
        overview_embedding = model.encode(movie_overview)
        embedding_dict[int(id)] = overview_embedding

embedding_dict_init()
keywords_dict_init()
@app.route("/keyword_searching")
def keyword_searching():
    input = request.args.get("input")
    norm_input = utils.preprocessing(input)

    scores = dict()
    for id in movies_data.id.values:
        keywords = keywords_dict.get(int(id))
        n = len(keywords)
        if n == 0:
            continue
        count = 0
        for keyword in keywords:
            count += int(keyword in norm_input)
        scores[int(id)] = count / n
    scores = {k: v for k, v in sorted(scores.items(), key=lambda item: item[1], reverse=True) if v > 0}
    scores = json.dumps(scores)
    print(scores)
    return scores
@app.route("/semantic_searching")
def semantic_searching():
    global embedding_dict
    print(embedding_dict)
    scores = {}
    input = request.args.get("input")
    input_embedding = model.encode(input)

    for id in movies_data.id.values:
        overview_embedding = embedding_dict.get(int(id))
        scores[int(id)] = (np.float64(utils.cosine_similarity(input_embedding, overview_embedding)) - 0.4) / 0.6
    scores = {k: v for k, v in sorted(scores.items(), key=lambda item: item[1], reverse=True) if v > 0}
    print(scores)
    return json.dumps(scores)

@app.route("/recommend")
def recommend():
    # print(request.args)
    user_id = request.args.get('user_id')
    movies = fetch_movie_ids()
    communities = fetch_communities()
    ratings = fetch_ratings()

    def recommend_movies(user_id, communities, ratings, movies, top_n=5):
        parsed_communities = [set(community[0].split(',')) for community in communities]

        user_community = None
        for community in parsed_communities:
            if user_id in community:
                user_community = community
                break

        if user_community is None:
            return pd.DataFrame(columns=['movie_id'])

        ratings_df = pd.DataFrame(ratings, columns=['user_id', 'movie_id', 'rating'])

        community_ratings = ratings_df[ratings_df['user_id'].isin(user_community)]

        user_ratings = ratings_df[ratings_df['user_id'] == user_id]
        seen_movies = set(user_ratings['movie_id'])

        community_ratings = community_ratings[~community_ratings['movie_id'].isin(seen_movies)]

        movie_scores = community_ratings.groupby('movie_id')['rating'].mean().sort_values(ascending=False)

        top_movies = movie_scores.head(top_n).index

        return {'recommend_movies': top_movies.tolist()}
    recommend_movies = recommend_movies(user_id, communities, ratings, movies, 5)
    print(recommend_movies)
    return json.dumps(recommend_movies)


@app.route("/hybrid_recommend")
def hybrid_recommend():
    user_id = request.args.get('user_id')
    movies = fetch_movie_ids()
    communities = fetch_communities()
    ratings = fetch_ratings()

    def recommend_movies(user_id, communities, ratings, movies, top_n=10):
        parsed_communities = [set(community[0].split(',')) for community in communities]

        user_community = None
        for community in parsed_communities:
            if user_id in community:
                user_community = community
                break

        if user_community is None:
            return pd.DataFrame(columns=['movie_id'])

        ratings_df = pd.DataFrame(ratings, columns=['user_id', 'movie_id', 'rating'])

        community_ratings = ratings_df[ratings_df['user_id'].isin(user_community)]

        user_ratings = ratings_df[ratings_df['user_id'] == user_id]
        seen_movies = set(user_ratings['movie_id'])

        community_ratings = community_ratings[~community_ratings['movie_id'].isin(seen_movies)]

        movie_scores = community_ratings.groupby('movie_id')['rating'].mean().sort_values(ascending=False)

        top_movies = movie_scores.head(top_n).index

        return top_movies.tolist()

    def hybrid_recommendation(user_id, movie_ids, top_n=5):
        hybrid_scores = []

        for movie_id in movie_ids:
            cf_prediction = cf_model.predict([np.array([user_id]), np.array([movie_id])])[0]
            print(f'CF prediction for movie {movie_id}: {cf_prediction}')

            movie_genre_vector = get_genre_vector(movie_id)
            if movie_genre_vector is not None:
                cb_prediction = genre_model.predict(np.array([movie_genre_vector]))[0]
                print(f'CB prediction for movie {movie_id}: {cb_prediction}')
                weight_cf = 0.7
                weight_cb = 0.3
            else:
                cb_prediction = 0
                weight_cf = 1
                weight_cb = 0

            final_prediction = weight_cf * cf_prediction + weight_cb * cb_prediction
            hybrid_scores.append((movie_id, final_prediction))

        hybrid_scores.sort(key=lambda x: x[1], reverse=True)
        top_hybrid_movies = [movie_id for movie_id, _ in hybrid_scores[:top_n]]

        return top_hybrid_movies

    recommended_movies = recommend_movies(user_id, communities, ratings, movies, top_n=10)

    top_hybrid_movies = hybrid_recommendation(user_id, recommended_movies, top_n=5)

    return {'recommended_movies': top_hybrid_movies}


def get_genre_vector(movies, movie_id):
    if not len(movies[movies['movie_id'] == movie_id]['genre_vec'].values):
        return None
    return np.stack(movies[movies['movie_id'] == movie_id]['genre_vec'].values)[0]
@app.route("/search_engine")
def search_engine():
    global embedding_dict, keywords_dict
    scores = {}
    input = request.args.get("input")
    norm_input = utils.preprocessing(input)
    input_embedding = model.encode(input)

    # def get_score_map(id):
    #     overview_embedding = embedding_dict.get(int(id))
    #     semantic_score = (np.float64(utils.cosine_similarity(input_embedding, overview_embedding)) - 0.4) / 0.6
    #
    #     keywords = keywords_dict.get(int(id))
    #     n = len(keywords)
    #     if n == 0:
    #         keyword_score = 0
    #     else:
    #         count = 0
    #         for keyword in keywords:
    #             count += int(keyword in norm_input)
    #         keyword_score = count / n
    #     score = max(semantic_score, keyword_score)
    #     if score > 0:
    #         scores[int(id)] = score
    # await asyncio.gather(*map(get_score_map, movies_data.id.values))
    for id in movies_data.id.values:
        overview_embedding = embedding_dict.get(int(id))
        semantic_score = (np.float64(utils.cosine_similarity(input_embedding, overview_embedding)) - 0.4) / 0.6

        keywords = keywords_dict.get(int(id))
        n = len(keywords)
        if n == 0:
            keyword_score = 0
        else:
            count = 0
            for keyword in keywords:
                count += int(keyword in norm_input)
            keyword_score = (count / n - 0.0) / 1
        score = max(semantic_score, keyword_score)
        if score > 0:
            scores[int(id)] = score
    sorted_scores = {k: v for k, v in sorted(scores.items(), key=lambda item: item[1], reverse=True)}
    print(sorted_scores)
    return json.dumps(sorted_scores)


app.run(debug=False, port=8086, host="0.0.0.0")


