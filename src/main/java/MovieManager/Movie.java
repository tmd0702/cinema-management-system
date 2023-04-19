package MovieManager;

import javafx.scene.image.Image;

import java.util.Date;

public class Movie {
    private String id, title, overview, movieStatus, posterPath, backdropPath, tagline, language, basePath = "https://www.themoviedb.org/t/p/original";
    private int duration, viewCount, voteCount;
    public float voteAverage;
    private Image backdropImage, posterImage;
    public double revenue;
    private Date releaseDate;
    public Movie() {
        this.tagline = "";
        this.language = "";
        this.voteCount = 0;
        this.voteAverage = 0;
        this.revenue = 0;
        this.posterPath = "";
        this.backdropPath = "";
        this.id = "";
        this.title = "";
        this.overview = "";
        this.movieStatus = "";
        this.duration = 0;
        this.viewCount = 0;
        this.releaseDate = new Date();
    }
    public Image getPosterImage() {
        return this.posterImage;
    }
    public Movie(String id, String title, String overview, String movieStatus, int duration, int viewCount, Date releaseDate, String posterPath, String backdropPath) {
        this.id = id;
        this.backdropPath = backdropPath;
        this.backdropImage = new Image(getBackdropPath());
        this.posterPath = posterPath;
        this.posterImage = new Image(getPosterPath());
        System.out.println(this.getPosterPath() + " " + title);
        this.title = title;
        this.overview = overview;
        this.movieStatus = movieStatus;
        this.duration = duration;
        this.viewCount = viewCount;
        this.releaseDate = releaseDate;
    }
    public String getPosterPath() {
        return this.basePath + this.posterPath;
    }
    public Image getBackdropImage() {
        return this.backdropImage;
    }
    public int getDuration() {
        return this.duration;
    }
    public int getViewCount() {
        return this.viewCount;
    }
    public String getBackdropPath() {
        return this.basePath + this.backdropPath;
    }
    public String getId() {
        return this.id;
    }
    public String getTitle() {
        return this.title;
    }
    public Date getReleaseDate() {
        return this.releaseDate;
    }
    public String getOverview() {
        return this.overview;
    }
}
