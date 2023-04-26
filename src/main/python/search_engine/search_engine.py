from flask import Flask, request, jsonify
from flask_cors import CORS, cross_origin
import utils
from mysql_connector import Database
import numpy as np
import asyncio
import json
import pandas as pd
from sentence_transformers import SentenceTransformer

model = SentenceTransformer('paraphrase-MiniLM-L6-v2')
app = Flask(__name__)

CORS(app, resources={r"/*": {"origins": "*"}})
app.config['CORS_HEADERS'] = 'Content-Type'

db = Database()
keywords_id_map = db.get_keywords_id_map()

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


app.run(debug=False, port=8085, host="0.0.0.0")


