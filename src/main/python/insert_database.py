import pandas as pd
import numpy as np
import mysql.connector
import requests
import os
import json
from datetime import date, timedelta
import random

class Database:
    def __init__(self):
        try:
            self.mydb = mysql.connector.connect(
                            host="103.42.57.83",
                            user="movie_admin",
                            password="sa123456"
                          )
            self.cursor = self.mydb.cursor()
            self.cursor.execute("USE MOVIE;")
            print("Connected")
        except Exception as e:
            print('Error occurred', e)
    def insert_movies(self, vals):

        sql = "INSERT INTO MOVIES (ID, TITLE, OVERVIEW, RELEASE_DATE, LANGUAGE, DURATION, STATUS, VIEW_COUNT, POSTER_PATH, BACKDROP_PATH, REVENUE, TAGLINE, VOTE_COUNT, VOTE_AVERAGE) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s);"
        try:
            self.cursor.execute(sql, vals)
            self.mydb.commit()
            print(self.cursor.rowcount, "record inserted.")
        except:
            print("error, skip")

    def update_releasedate_movie(self):
        today = date.today() - timedelta(days=1)
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
        def insert_fake_review(self, movie_id, user_id, rating, comment):
            sql = "INSERT INTO REVIEW (MOVIE_ID, USER_ID, RATING, COMMENT) VALUES (%s, %s, %s, %s);"
        vals = (movie_id, user_id, rating, comment)
        try:
            self.cursor.execute(sql, vals)
            self.mydb.commit()
            print(self.cursor.rowcount, "review inserted.")
        except Exception as e:
            print("Error inserting review:", e)

    def insert_fake_review(self, movie_id, user_id, rating, comment):
        sql = "INSERT INTO REVIEW (MOVIE_ID, USER_ID, RATING, COMMENT) VALUES (%s, %s, %s, %s);"
        vals = (movie_id, user_id, rating, comment)
        try:
            self.cursor.execute(sql, vals)
            self.mydb.commit()
            print(self.cursor.rowcount, "review inserted.")
        except Exception as e:
            print("Error inserting review:", e)
    def generate_fake_reviews(self, num_reviews):
        # Fetch user IDs from the USERS table
        user_ids_query = "SELECT ID FROM USERS;"
        movie_ids_query = "SELECT ID FROM MOVIES;"

        try:
            self.cursor.execute(user_ids_query)
            user_ids = [row[0] for row in self.cursor.fetchall()]
            self.cursor.execute(movie_ids_query)
            movie_ids = [row[0] for row in self.cursor.fetchall()]
        except Exception as e:
            print("Error fetching IDs:", e)
            return

        for _ in range(num_reviews):
            movie_id = random.choice(movie_ids)
            user_id = random.choice(user_ids)
            rating = round(random.uniform(0, 5) * 2) / 2
            comment = f"This is a fake review comment for movie {movie_id} by user {user_id}."

            self.insert_fake_review(movie_id, user_id, rating, comment)


def themoviedb_url_checker(urlsf):
    url = f"https://image.tmdb.org/t/p/original{urlsf}"
    try:
        return requests.head(url).status_code == 200
    except:
        print("error")
        return False

if __name__ == "__main__":
    db = Database()
    db.generate_fake_reviews(5000)
#     data_path = os.getcwd().replace('\\', '/').replace('/python', '/resources/4hb-data/movies_metadata_cleaned.csv')
#     metadata = pd.read_csv(data_path)
#     metadata.drop("belongs_to_collection", axis=1, inplace=True)
#     metadata = metadata.replace(np.nan, None)
#     # # metadata = metadata.loc[metadata['release_date'] > '2010']
#     for index, row in metadata.iterrows():
# #         if themoviedb_url_checker(row.backdrop_path) == True and themoviedb_url_checker(row.poster_path) == True:
#         print(row.backdrop_path, row.poster_path)
#         vals = (row.id, row.title, row.overview, row.release_date, row.original_language, row.runtime, row.status, 0, row.poster_path, row.backdrop_path, row.revenue, row.tagline, row.vote_count, row.vote_average)
#         db.insert_movies(vals)
# #         else:
# #             print(index, 'error')
#     db.update_releasedate_movie()
#     for index, row in metadata.iterrows():
#         genres = json.loads(row['genres'].replace("'", '"'))
#         movie_id = str(row['id'])
#
#         for genre in genres:
#             genre_vals = (str(genre['id']), genre['name'])
#             movie_genre_vals = (movie_id, str(genre['id']))
#             db.insert_genre(genre_vals, movie_genre_vals)



