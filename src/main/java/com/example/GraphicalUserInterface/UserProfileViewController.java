package com.example.GraphicalUserInterface;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserProfileViewController implements Initializable {
    private Main main;
    private Button tabPanelOnClick;
    @FXML
    private ImageView logoImageView;
    @FXML
    private VBox userProfileViewTabPanelContainer;
    @FXML
    private TextField inputField;
    @FXML
    private Button dashboardTabPanel = new Button("Dashboard"), profileTabPanel = new Button("Profile"), pointTabPanel = new Button("Point"), paymentHistoryTabPanel = new Button("Payment History");
    public UserProfileViewController() {
        main = Main.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logoImageViewInit();
        userProfileViewTabPanelContainerInit();
        userProfileViewTabPanelsInit();
    }
    public void userProfileViewTabPanelContainerInit() {
        userProfileViewTabPanelContainer.getChildren().add(dashboardTabPanel);
        userProfileViewTabPanelContainer.getChildren().add(profileTabPanel);
        userProfileViewTabPanelContainer.getChildren().add(pointTabPanel);
        userProfileViewTabPanelContainer.getChildren().add(paymentHistoryTabPanel);
    }
    public void userProfileViewTabPanelsInit() {
        for (Node tabPanel : userProfileViewTabPanelContainer.getChildren()) {
            tabPanel.setStyle("-fx-pref-width: 110; -fx-pref-height: 48; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: transparent;");
            tabPanelMouseEventListener((Button) tabPanel);
        }
    }
    public void logoImageViewInit() {
        String imageSource = "https://docs.google.com/uc?id=1F2pXOLfvuynr9JcURTR5Syg7N1YdPJXK";
        Image logo = new Image(imageSource);
        if (logo.isError()) {
            System.out.println("Error loading image from " + imageSource);
        } else {
            logoImageView.setImage(logo);
        }
    }
    public void tabPanelMouseEventListener(Button tabPanel) {
        tabPanel.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (tabPanel != tabPanelOnClick) tabPanel.setStyle("-fx-pref-width: 110; -fx-pref-height: 48; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: #293263;");
            }
        });
        tabPanel.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (tabPanel != tabPanelOnClick) tabPanel.setStyle("-fx-pref-width: 110; -fx-pref-height: 48; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: transparent;");
            }
        });
        tabPanel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (tabPanelOnClick != null) {
                    tabPanelOnClick.setStyle("-fx-pref-width: 110; -fx-pref-height: 48; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: transparent;");
                }
                tabPanel.setStyle("-fx-pref-width: 110; -fx-pref-height: 48; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: rgba(0, 0, 255, 0.3);");
                tabPanelOnClick = tabPanel;
            }
        });
    }
    @FXML
    public void logoImageViewOnClick() throws IOException {
        main.changeScene("index-view.fxml");
    }
    @FXML
    public void onSearchFieldEnterKeyPress() throws IOException {
        main.setQueryOnSearching(inputField.getText());
        main.changeScene("search-results-view.fxml");
    }
}