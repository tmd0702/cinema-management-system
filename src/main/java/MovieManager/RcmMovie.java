package MovieManager;

import java.util.ArrayList;
import java.util.Map;

public class RcmMovie {
    private String id;
    private Double rcm_score;

    public RcmMovie() {
        this.id = "";
        this.rcm_score = 0.0;
    }

    public RcmMovie(String id, Double rcm_score) {
        this.id = id;
        this.rcm_score = rcm_score;
    }

    public String getRcmMovieId() {
        return id;
    }
    public Double getRcmMovieScore() {
        return rcm_score;
    }
    public void setRcmMovieId(String id) {
        this.id = id;
    }
    public void setRcmMovieScore(Double rcm_score) {
        this.rcm_score = rcm_score;
    }
}
