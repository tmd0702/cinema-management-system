import pandas as pd
import numpy as np
import mysql.connector
import requests
import os
import json
import pandas as pd
from numpy.linalg import norm
import numpy as np
class Database:
    def __init__(self):
        self.movies_metadata_cleaned_df = pd.read_csv("D:/4hb-db/movies_metadata_cleaned.csv")
        self.movie_data = self.movies_metadata_cleaned_df = pd.read_csv("D:/4hb-db/movies_metadata.csv")
        self.keywords_df = pd.read_csv("D:/4hb-db/keywords.csv")
        self.id_list = self.keywords_df.id.values
        self.keyword_list = self.keywords_df.keywords.values
        self.keywords_id_map = dict()

        def mapfunc_get_keyword(x):
            return x['name']

        for keywords, id in zip(self.keyword_list, self.id_list):
            self.keywords_id_map[id] = list(map(mapfunc_get_keyword, json.loads(keywords)))
        # try:
        #     self.mydb = mysql.connector.connect(
        #                     host="127.0.0.1",
        #                     user="4hb_admin",
        #                     password="sa123456"
        #                   )
        #     self.cursor = self.mydb.cursor()
        #
        #     print("Connected")
        # except:
        #     print('Error occurred')
    def insert_movies(self, vals):
        self.cursor.execute("USE MOVIE;")

        sql = "INSERT INTO MOVIES (ID, TITLE, OVERVIEW, RELEASE_DATE, LANGUAGE, DURATION, STATUS, VIEW_COUNT, POSTER_PATH, BACKDROP_PATH, REVENUE, TAGLINE, VOTE_COUNT, VOTE_AVERAGE) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s);"
        try:
            self.cursor.execute(sql, vals)
            self.mydb.commit()
            print(self.cursor.rowcount, "record inserted.")
        except:
            print("error, skip")

    def insert_keywords(self, vals):
        pass

    def get_keywords_id_map(self):
        return self.keywords_id_map
    def get_movies_data(self):
        return self.movies_data

