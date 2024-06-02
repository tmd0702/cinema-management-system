package MovieManager;
import java.util.*;
import Utils.Utils;
import com.example.GraphicalUserInterface.Main;
import javafx.scene.image.Image;
import org.json.JSONObject;

public class MovieManager {
    private ArrayList<Movie> movieList;
    private ArrayList<Movie> recommendedList;
    private Main main = Main.getInstance();
    private Image imageNotFound;
    public MovieManager() {
        this.movieList = new ArrayList<Movie>();
        this.recommendedList = new ArrayList<Movie>();
        this.imageNotFound = new Image("https://westsiderc.org/wp-content/uploads/2019/08/Image-Not-Available.png");
    }
    public Image getImageNotFound() {
        return this.imageNotFound;
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
        for (int i=0; i<Math.round(movieList.size() / 2);++i) {
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
        for (int i=Math.round(movieList.size() / 2); i < movieList.size(); ++i) {
            comingSoonMovieList.add(movieList.get(i));
        }
        return comingSoonMovieList;
    }

    public ArrayList<Movie> getRecommendMovieList() {
        JSONObject jsonData = new JSONObject();
        ArrayList<Movie> recommendMovieList = new ArrayList<Movie>();
        if (main.getSignedInUser() != null ) {
            jsonData.put("user_id", main.getSignedInUser().getId());
            ArrayList<String> recommendMovieIDs = main.getConnector().HTTPRecommendMoviesRequest(jsonData);
            if (recommendMovieIDs.size() > 0) {
                for (int i = 0; i < recommendMovieIDs.size(); ++i) {
                    for (int j = 0; j < movieList.size(); ++j) {
                        if (recommendMovieIDs.get(i).toString().equals(movieList.get(j).getId().toString())) {
//                        System.out.println(recommendMovieIDs.get(i).toString() + " " + movieList.get(j).getId().toString() + " " + movieList.get(j).getTitle().toString());
                            recommendMovieList.add(movieList.get(j));
                            break;
                        }
                    }

                }
            }
        } else {

        }
        this.recommendedList = recommendMovieList;
        return recommendMovieList;
    }
    public ArrayList<Movie> getRecommendedList() {
        return this.recommendedList;
    }
    public MovieManager(ArrayList<Movie> movieList) {
        this.movieList = movieList;
    }
    public Movie getMovieById(String id) {
        Movie movie = null;

        for (Movie m : this.movieList) {
            if (m.getId().equals(id)) {
                movie = m;
                break;
            }
        }
        return movie;
    }
    public void setMovieList(ArrayList<Movie> movieList) {
        this.movieList = movieList;
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
