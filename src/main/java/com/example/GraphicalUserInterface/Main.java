package com.example.GraphicalUserInterface;
import Configuration.Config;
import Database.*;

import MovieManager.Movie;
import SearchEngine.SearchEngine;
import UserManager.User;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Filter;

public class Main extends Application {
    private static Main main;
    private static Stage stage;
    private User signedInUser;
    private boolean nowShowingMoviesTabActive, comingSoonMoviesTabActive;
    private Movie movieOnDetail;
    private Movie movieOnBooking;

    private String queryOnSearching;
    private BookingProcessor bookingProcessor;
    private UserCategoryManagementProcessor userCategoryManagementProcessor;
    private FiltererProcessor filtererProcessor;
    private SearchEngine searchEngine;
    private PaymentManagementProcessor paymentManagementProcessor;
    private MovieManagementProcessor movieManagementProcessor;
    private TheaterManagementProcessor theaterManagementProcessor;
    private ShowTimeManagementProcessor showTimeManagementProcessor;
    private ScreenRoomManagementProcessor screenRoomManagementProcessor;
    private ScheduleManagementProcessor scheduleManagementProcessor;
    private ReviewManagementProcessor reviewManagementProcessor;
    private AccountManagementProcessor accountManagementProcessor;
    private PromotionManagementProcessor promotionManagementProcessor;
    private ItemManagementProcessor itemManagementProcessor;
    private AuthenticationManagementProcessor authenticationManagementProcessor;
    private Config config;
    public Main() throws Exception {
        super();
        main = this;
        this.config = new Config();
        this.promotionManagementProcessor = new PromotionManagementProcessor();
        this.authenticationManagementProcessor = new AuthenticationManagementProcessor();
        this.userCategoryManagementProcessor = new UserCategoryManagementProcessor();
        this.paymentManagementProcessor = new PaymentManagementProcessor();
        this.accountManagementProcessor = new AccountManagementProcessor();
        this.reviewManagementProcessor = new ReviewManagementProcessor();
        this.scheduleManagementProcessor = new ScheduleManagementProcessor();
        this.screenRoomManagementProcessor = new ScreenRoomManagementProcessor();
        this.theaterManagementProcessor = new TheaterManagementProcessor();
        this.showTimeManagementProcessor = new ShowTimeManagementProcessor();
        this.movieManagementProcessor = new MovieManagementProcessor();
        this.bookingProcessor = new BookingProcessor();
        this.filtererProcessor = new FiltererProcessor();
        this.itemManagementProcessor = new ItemManagementProcessor();
        this.queryOnSearching = "";
        nowShowingMoviesTabActive = false;
        comingSoonMoviesTabActive = false;
        searchEngine = new SearchEngine();
    }
    public ItemManagementProcessor getItemManagementProcessor() {
        return this.itemManagementProcessor;
    }
    public PromotionManagementProcessor getPromotionManagementProcessor() {
        return this.promotionManagementProcessor;
    }
    public AuthenticationManagementProcessor getAuthenticationManagementProcessor() {
        return this.authenticationManagementProcessor;
    }
    public UserCategoryManagementProcessor getUserCategoryManagementProcessor() {
        return this.userCategoryManagementProcessor;
    }
    public PaymentManagementProcessor getPaymentManagementProcessor() {
        return this.paymentManagementProcessor;
    }
    public ReviewManagementProcessor getReviewManagementProcessor() {
        return this.reviewManagementProcessor;
    }
    public AccountManagementProcessor getAccountManagementProcessor() {
        return this.accountManagementProcessor;
    }
    public Stage getStage() {
        return this.stage;
    }
    public ScheduleManagementProcessor getScheduleManagementProcessor() {
        return this.scheduleManagementProcessor;
    }
    public ScreenRoomManagementProcessor getScreenRoomManagementProcessor() {
        return this.screenRoomManagementProcessor;
    }
    public Node getNodeById(String id) {
        return stage.getScene().lookup(id);
    }
    public TheaterManagementProcessor getTheaterManagementProcessor() {
        return theaterManagementProcessor;
    }
    public ShowTimeManagementProcessor getShowTimeManagementProcessor() {
        return showTimeManagementProcessor;
    }

    public Config getConfig() {
        return this.config;
    }
    public void setMovieOnBooking(Movie movie){
        movieOnBooking = movie;
    }
    public Movie getMovieOnBooking(){
        return movieOnBooking;
    }
    public BookingProcessor getBookingProcessor(){return this.bookingProcessor;}
    public FiltererProcessor getFiltererProcessor() {
        return this.filtererProcessor;
    }
    public void setQueryOnSearching(String queryOnSearching) {
        this.queryOnSearching = queryOnSearching;
    }
    public SearchEngine getSearchEngine() {
        return this.searchEngine;
    }
    public String getQueryOnSearching() {
        return this.queryOnSearching;
    }
    public void setNowShowingMoviesTabActive(boolean isActive) {
        this.nowShowingMoviesTabActive = isActive;}
    public void setComingSoonMoviesTabActive(boolean isActive) {
        this.comingSoonMoviesTabActive = isActive;
    }
    public boolean getNowShowingMoviesTabActive() {
        return this.nowShowingMoviesTabActive;
    }
    public boolean getComingSoonMoviesTabActive() {
        return this.comingSoonMoviesTabActive;
    }
    public void setMovieOnDetail(Movie movie) {
        movieOnDetail = movie;
    }
    public MovieManagementProcessor getMovieManagementProcessor() {
        return this.movieManagementProcessor;
    }
    public void setSignedInUser(User user) {
        this.signedInUser = user;
    }
    public User getSignedInUser() {
        return this.signedInUser;
    }
    public  Movie getMovieOnDetail() {
        return movieOnDetail;
    }
    public static synchronized Main getInstance() {
        if (main == null) {
            try {
                main = new Main();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return main;
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        this.movieManagementProcessor.getMovies();
        stage = primaryStage;
        setMovieOnBooking(new Movie());
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-outline.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 920, 600);
        scene.getStylesheets().add(getClass().getResource("assets/css/index-style.css").toExternalForm());
        stage.setTitle("4HB Cinema Management");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    public void changeView(String fxml) throws IOException {
        ((ScrollPane) getNodeById("#mainOutlineScrollPane")).setVvalue(0.0);
        ((AnchorPane) getNodeById("#mainOutlineContainer")).getChildren().add(FXMLLoader.load(getClass().getResource(fxml)));
        ((AnchorPane) getNodeById("#mainOutlineContainer")).getChildren().remove(0);
    }
    public void changeScene(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml));
        stage.getScene().setRoot(fxmlLoader.load());
    }
    public Popup popup(String fxml) throws IOException {
        Popup popup = new Popup();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
        popup.getContent().add(fxmlLoader.load());
        if (!popup.isShowing())
            popup.show(stage);
        return popup;
    }
    public void hide(Popup popup) {
        popup.hide();
    }
    public static void main(String[] args) {

        launch();

    }
}