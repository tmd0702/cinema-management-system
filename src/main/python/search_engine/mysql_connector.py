import pandas as pd
import numpy as np
import mysql.connector
import requests
import os
import json
class Database:
    def __init__(self):
        self.movies_metadata_cleaned_df = pd.read_csv("../../resources/4hb-data/movies_metadata_cleaned.csv")
        # self.movies_data = pd.read_csv("../resources/4hb-data/movies_metadata.csv")
        self.keywords_df = pd.read_csv("../../resources/4hb-data/keywords.csv")
        for index, row in self.keywords_df.iterrows():
            try:
                self.keywords_df.loc[index, 'keywords'] = row.iloc[1].replace("{'", '{"').replace("':", '":').replace(
                    " '", ' "').replace("'}", '"}').replace('\\', '/')
            except:
                print(index, 'error!')
        # self.keywords_df.to_csv("D:/4hb-db/cleaned_keywords.csv", index=None)
        # print('ok')
        self.id_list = self.keywords_df.id.values
        self.keyword_list = self.keywords_df.keywords.values
        self.keywords_id_map = dict()

        def mapfunc_get_keyword(x):
            return x['name']

        for keywords, id in zip(self.keyword_list, self.id_list):
            try:
                self.keywords_id_map[id] = list(map(mapfunc_get_keyword, json.loads(keywords)))
            except:
                print(id, 'error')
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
    # def get_movies_data(self):
    #     return self.movies_data
    def get_movies_metadata_cleaned_df(self):
        return self.movies_metadata_cleaned_df
