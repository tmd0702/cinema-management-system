package MovieManager;
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
//        ArrayList<Movie> currentlyPlayingMovieList = new ArrayList<Movie>();
//        for (Movie movie : this.movieList) {
//            long diff = Utils.getDiffBetweenDates(movie.getReleaseDate(), new Date());
//            if (diff < 7 && diff >= 0) {
//                currentlyPlayingMovieList.add(movie);
//            }
//        }
//        return currentlyPlayingMovieList;
        ArrayList<Movie> currentlyPlayingMovieList = new ArrayList<Movie>();
        for (int i=0; i<15;++i) {
            currentlyPlayingMovieList.add(movieList.get(i));
        }
        return currentlyPlayingMovieList;
    }
    public ArrayList<Movie> getComingSoonMovieList() {
//        ArrayList<Movie> comingSoonMovieList = new ArrayList<Movie>();
//        for (Movie movie: this.movieList) {
//            long diff = Utils.getDiffBetweenDates(movie.getReleaseDate(), new Date());
//            if (diff < 0) {
//                comingSoonMovieList.add(movie);
//            }
//        }
//        return comingSoonMovieList;
        ArrayList<Movie> comingSoonMovieList = new ArrayList<Movie>();
        for (int i=15; i < 29; ++i) {
            comingSoonMovieList.add(movieList.get(i));
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
