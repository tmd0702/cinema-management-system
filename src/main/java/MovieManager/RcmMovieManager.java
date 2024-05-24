package MovieManager;

import java.util.ArrayList;

public class RcmMovieManager {
    private ArrayList<RcmMovie> rcm_movie_list;
    
    public RcmMovieManager() {
        this.rcm_movie_list = new ArrayList<>();
    }
    public RcmMovieManager(ArrayList<RcmMovie> rcm_movie_list) {
        this.rcm_movie_list = rcm_movie_list;
    }

    public ArrayList<RcmMovie> getRcmMovieList() {
        return rcm_movie_list;
    }
    public void setRcmMovieList(ArrayList<RcmMovie> rcm_movie_list) {
        this.rcm_movie_list = rcm_movie_list;
    }
}
