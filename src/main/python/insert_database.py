import pandas as pd
import numpy as np
import mysql.connector
import requests
import os
import json
from datetime import date

class Database:
    def __init__(self):
        try:
            self.mydb = mysql.connector.connect(
                            host="127.0.0.1",
                            user="4hb_admin",
                            password="sa123456"
                          )
            self.cursor = self.mydb.cursor()
            self.cursor.execute("USE MOVIE;")
            print("Connected")
        except:
            print('Error occurred')
    def insert_movies(self, vals):

        sql = "INSERT INTO MOVIES (ID, TITLE, OVERVIEW, RELEASE_DATE, LANGUAGE, DURATION, STATUS, VIEW_COUNT, POSTER_PATH, BACKDROP_PATH, REVENUE, TAGLINE, VOTE_COUNT, VOTE_AVERAGE) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s);"
        try:
            self.cursor.execute(sql, vals)
            self.mydb.commit()
            print(self.cursor.rowcount, "record inserted.")
        except:
            print("error, skip")

    def update_releasedate_movie(self):
        today = date.today()
        sql = "UPDATE MOVIES SET RELEASE_DATE = DATE_FORMAT(RELEASE_DATE ,'{today}');".format(today=today)
        try:
            self.cursor.execute(sql)
            self.mydb.commit()
            print("the row is updated.")
        except:
            print("error skip.")
    def insert_genre(self, genre_vals, movie_genre_vals):
        genre_sql = "INSERT INTO GENRES (ID, NAME) VALUES (%s, %s);"
        try:
            self.cursor.execute(genre_sql, genre_vals)
            self.mydb.commit()
            print(self.cursor.rowcount, "genre inserted.")
        except:
            print("id exists, skip")

        movie_genre_sql = "INSERT INTO MOVIE_GENRES (MOVIE_ID, GENRE_ID) VALUES (%s, %s);"
        try:
            self.cursor.execute(movie_genre_sql, movie_genre_vals)
            self.mydb.commit()
            print(self.cursor.rowcount, "movie genre inserted.")
        except:
            print(movie_genre_vals)
            print("movie or genre id not exists, skip")

    def insert_keywords(self, vals):
        self.cursor.execute("")


def themoviedb_url_checker(urlsf):
    url = f"https://image.tmdb.org/t/p/original{urlsf}"
    try:
        return requests.head(url).status_code == 200
    except:
        print("error")
        return False

if __name__ == "__main__":
    db = Database()
    data_path = os.getcwd().replace('\\', '/').replace('/python', '/resources/4hb-data/movies_metadata_cleaned.csv')
    metadata = pd.read_csv(data_path)
    metadata.drop("belongs_to_collection", axis=1, inplace=True)
    metadata = metadata.replace(np.nan, None)
    # # metadata = metadata.loc[metadata['release_date'] > '2010']
    for index, row in metadata.iterrows():
#         if themoviedb_url_checker(row.backdrop_path) == True and themoviedb_url_checker(row.poster_path) == True:
        print(row.backdrop_path, row.poster_path)
        vals = (row.id, row.title, row.overview, row.release_date, row.original_language, row.runtime, row.status, 0, row.poster_path, row.backdrop_path, row.revenue, row.tagline, row.vote_count, row.vote_average)
        db.insert_movies(vals)
#         else:
#             print(index, 'error')
    db.update_releasedate_movie()
    for index, row in metadata.iterrows():
        genres = json.loads(row['genres'].replace("'", '"'))
        movie_id = str(row['id'])

        for genre in genres:
            genre_vals = (str(genre['id']), genre['name'])
            movie_genre_vals = (movie_id, str(genre['id']))
            db.insert_genre(genre_vals, movie_genre_vals)



