package MovieManager;
import java.time.Duration;
import java.util.*;
import Utils.Utils;
public class MovieManager {
    private ArrayList<Movie> movieList;
    public MovieManager() {
        this.movieList = new ArrayList<Movie>();
    }
    public ArrayList<Movie> getMovieList() {
        return this.movieList;
    }
    public ArrayList<Movie> getCurrentlyPlayingMovieList() {
        ArrayList<Movie> currentlyPlayingMovieList = new ArrayList<Movie>();
        for (Movie movie : this.movieList) {
            long diff = Utils.getDiffBetweenDates(movie.getProductDate(), new Date());
            if (diff < 7 && diff >= 0) {
                currentlyPlayingMovieList.add(movie);
            }
        }
        return currentlyPlayingMovieList;
    }
    public ArrayList<Movie> getComingSoonMovieList() {
        ArrayList<Movie> comingSoonMovieList = new ArrayList<Movie>();
        for (Movie movie: this.movieList) {
            long diff = Utils.getDiffBetweenDates(movie.getProductDate(), new Date());
            if (diff < 0) {
                comingSoonMovieList.add(movie);
            }
        }
        return comingSoonMovieList;
    }
    public MovieManager(ArrayList<Movie> movieList) {
        this.movieList = movieList;
    }
    public Movie getMovieById(String id) {
        Movie movie = new Movie();
        for (Movie m : this.movieList) {
            if (m.getId() == id) {
                movie = m;
                break;
            }
        }
        return movie;
    }
    public void addMovie(Movie movie) {
        try {
            this.movieList.add(movie);
        } catch (Exception e) {
            System.out.println(e);
            throw(e);
        }
    }

}
