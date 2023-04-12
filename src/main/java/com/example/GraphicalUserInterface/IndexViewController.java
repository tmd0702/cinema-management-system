package com.example.GraphicalUserInterface;
import javafx.fxml.FXML;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import java.io.IOException;
import javafx.scene.shape.*;

public class IndexViewController {
    private Main main = new Main();
    @FXML
    private Label welcomeText;
    @FXML
    private HBox moviePreviewSection;
    @FXML
    private Rectangle addMovieBtn;

    public IndexViewController() {

    }
    @FXML
    public void onAddMovieBtnClick() {
        welcomeText.setText("OKKKKKKKKKKKKKK");
        addMovieBtn.setHeight(addMovieBtn.getHeight() * 1.5);
        addMovieBtn.setWidth(addMovieBtn.getWidth() * 1.5);
        System.out.println(moviePreviewSection.getChildren());
    }
    public void onMouseEnterMovieElement() {
//        changeSize(addMovieBtn, addMovieBtn.getWidth() * 1.5, addMovieBtn.getHeight() * 1.5)
//        ScaleTransition transition = new ScaleTransition();
//        transition.setDuration(Duration.seconds(1));
//        transition.setNode(addMovieBtn);
//        transition.setToX(1.5);
//        transition.setToY(1.5);
//        transition.play();
    }
    public void onSignInBtnClick() throws IOException {
        this.main.popup("login-form.fxml");
    }
    public void onMouseExitMovieElement() {
//        ScaleTransition transition = new ScaleTransition();
//        transition.setDuration(Duration.seconds(1));
//        transition.setNode(addMovieBtn);
//        transition.setToX(1);
//        transition.setToY(1);
//        transition.play();
    }
    public void onSignUpBtnClick() throws IOException {
        this.main.popup("signup-form.fxml");
    }
}
