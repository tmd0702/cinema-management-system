package MovieManager;

import java.util.Date;

public class Movie {
    private String id, title, content, category, movieStatus, posterPath, backdropPath;
    private int duration, viewCount;
    private Date productDate;
    public Movie() {
        this.posterPath = "";
        this.backdropPath = "";
        this.id = "";
        this.title = "";
        this.content = "";
        this.category = "";
        this.movieStatus = "";
        this.duration = 0;
        this.viewCount = 0;
        this.productDate = new Date();
    }
    public Movie(String id, String title, String content, String category, String movieStatus, int duration, int viewCount, Date productDate, String posterPath, String backdropPath) {
        this.id = id;
        this.backdropPath = backdropPath;
        this.posterPath = posterPath;
        this.title = title;
        this.content = content;
        this.category = category;
        this.movieStatus = movieStatus;
        this.duration = duration;
        this.viewCount = viewCount;
        this.productDate = productDate;
    }
    public String getPosterPath() {
        return this.posterPath;
    }
    public int getDuration() {
        return this.duration;
    }
    public int getViewCount() {
        return this.viewCount;
    }
    public String getBackdropPath() {
        return this.backdropPath;
    }
    public String getId() {
        return this.id;
    }
    public String getTitle() {
        return this.title;
    }
    public String getCategory() {
        return this.category;
    }
    public Date getProductDate() {
        return this.productDate;
    }
    public String getContent() {
        return this.content;
    }
}
