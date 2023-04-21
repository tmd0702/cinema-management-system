import pandas as pd
import numpy as np
import mysql.connector
import requests
import os

class Database:
    def __init__(self):
        try:
            self.mydb = mysql.connector.connect(
                            host="127.0.0.1",
                            user="4hb_admin",
                            password="sa123456"
                          )
            self.cursor = self.mydb.cursor()

            print("Connected")
        except:
            print('Error occurred')
    def insert_movies(self, vals):
        self.cursor.execute("USE MOVIE;")

        sql = "INSERT INTO MOVIES (ID, TITLE, OVERVIEW, RELEASE_DATE, LANGUAGE, DURATION, MOVIE_STATUS, VIEW_COUNT, POSTER_PATH, BACKDROP_PATH, REVENUE, TAGLINE, VOTE_COUNT, VOTE_AVERAGE) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s);"
        try:
            self.cursor.execute(sql, vals)
            self.mydb.commit()
            print(self.cursor.rowcount, "record inserted.")
        except:
            print("error, skip")

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
    # metadata = metadata.loc[metadata['release_date'] > '2010']
    for index, row in metadata.iterrows():
        if themoviedb_url_checker(row.backdrop_path) == True and themoviedb_url_checker(row.poster_path) == True:
            print(row.backdrop_path, row.poster_path)
            vals = (row.id, row.title, row.overview, row.release_date, row.original_language, row.runtime, row.status, 0, row.poster_path, row.backdrop_path, row.revenue, row.tagline, row.vote_count, row.vote_average)
            db.insert_movies(vals)

        else:
            print(index, 'error')


