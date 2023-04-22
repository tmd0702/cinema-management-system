from flask import Flask, request, jsonify
from flask_cors import CORS, cross_origin
import utils
from mysql_connector import Database
import numpy as np
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

@app.route("/keyword_searching")
def keyword_searching():
    input = request.args.get("input")
    norm_input = utils.preprocessing(input)

    scores = dict()
    for id in movies_data.id.values:
        try:
            keywords = list(
                set(np.append(keywords_id_map[int(id)], np.array([utils.preprocessing(movies_data.loc[movies_data.id == int(id), 'title'].iloc[0])]))))
        except:
            print(id, 'not found')
            continue
        # print(keywords)
        # print(len(keywords))
        n = len(keywords)
        if n == 0:
            continue
        count = 0
        for keyword in keywords:
            count += int(keyword in norm_input)
        scores[int(id)] = count / n
    scores = {k: v for k, v in sorted(scores.items(), key=lambda item: item[1], reverse=True) if v > 0}
    scores = json.dumps(scores)
    print(type(scores))
    print(scores)
    return scores
@app.route("/semantic_searching")
def semantic_searching():
    scores = {}
    input = request.args.get("input")
    input_embedding = model.encode(input)

    for id in movies_data.id.values:
        movie_overview = movies_data.loc[movies_data.id == int(id), 'overview'].values[0]
        overview_embedding = model.encode(movie_overview)

        scores[int(id)] = np.float64(utils.cosine_similarity(input_embedding, overview_embedding))
    scores = {k: v for k, v in sorted(scores.items(), key=lambda item: item[1], reverse=True) if v > 0}
    print(scores)
    return json.dumps(scores)


app.run(debug=False, port=8085)


