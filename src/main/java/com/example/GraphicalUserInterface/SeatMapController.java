package com.example.GraphicalUserInterface;

import UserManager.Customer;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.skin.ScrollBarSkin;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.text.Font;
import Database.BookingProcessor;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.animation.*;

import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.controlsfx.glyphfont.FontAwesome;
import UserManager.User;
import java.util.*;
import Utils.*;


public class SeatMapController {
    @FXML
    private ScrollPane cinemaSectionScrollPane = new ScrollPane();
    @FXML
    private FontAwesomeIconView cineScrollLeftBtn, cineScrollRightBtn, roomScrollLeftBtn, roomScrollRightBtn;
    @FXML
    private ScrollPane roomSecionScrollPane = new ScrollPane();
    @FXML
    private HBox cinemaSection;
    @FXML
    private HBox roomSection;
    @FXML
    private GridPane seatGrid;
    private ArrayList<Button> CinemaButton;
    private  ArrayList<ArrayList<String>> cinemaInfor = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> roomInfor = new ArrayList<ArrayList<String>>();
    private ArrayList<Boolean> isCinemaActive = new ArrayList<Boolean>();
    private ArrayList<Boolean> isRoomActive = new ArrayList<Boolean>();
    private ArrayList<String> cinemaName;
    private ArrayList<String> roomName;
    private ArrayList<Button> RoomButton = new ArrayList<Button>();
    private Main main;
    @FXML
    private Label announceRoom;
    @FXML
    private AnchorPane seatMapContainer;
    private  BookingProcessor bookingProcessor;
    private String cinemaId = new String();
    private String roomId = new String();
    private Button nearbyButton = new Button();
    private ArrayList<String> seatIdSelected = new ArrayList<String>();
    private ArrayList<Button> seatBtnSelected = new ArrayList<Button>();
    private int numberSubBtn;

    @FXML
    public void initialize() throws Exception {
        this.main = Main.getInstance();
        this.bookingProcessor = new BookingProcessor();
        scrollBtnInit();
        ConstructCinemaButton();
        numberSubBtn = 0;
    }
    public ScrollPane getScrollPane(FontAwesomeIconView button){
        if(button.getId().contains("cine")){
            return cinemaSectionScrollPane;
        }
        if(button.getId().contains("room")){
            return roomSecionScrollPane;
        }
        return null;
    }
    public void scrollBtnInit() {
        scrollBtnChangeStyleOnHover(cineScrollLeftBtn);
        scrollBtnChangeStyleOnHover(cineScrollRightBtn);
        scrollBtnChangeStyleOnHover(roomScrollLeftBtn);
        scrollBtnChangeStyleOnHover(roomScrollRightBtn);
    }
    @FXML
    public void scrollLeftBtnOnClick(MouseEvent event) {
        Button button = (Button)(cinemaSection.getChildren().get(0));
        ScrollPane scrollPane = getScrollPane((FontAwesomeIconView) event.getSource());
        int n = 0;
        if(scrollPane == cinemaSectionScrollPane){
            n = 4;
        }else{
            n = 7;
        }
        HBox hbox = (HBox)scrollPane.getContent();
        scrollAnimation(scrollPane.hvalueProperty(), 0.5, scrollPane.getHvalue() - (((button.getWidth() + hbox.getSpacing()) * n )/ hbox.getWidth() + 0.007)) ;
    }
    @FXML
    public void scrollRightBtnOnClick(MouseEvent event) {
        Button button = (Button)(cinemaSection.getChildren().get(0));
        ScrollPane scrollPane = getScrollPane((FontAwesomeIconView) event.getSource());
        int n = 0;
        if(scrollPane == cinemaSectionScrollPane){
            n = 4;
        }else{
            n = 7;
        }
        HBox hbox = (HBox)scrollPane.getContent();
        scrollAnimation(scrollPane.hvalueProperty(), 0.5, scrollPane.getHvalue() + (((button.getWidth() + hbox.getSpacing()) * n )/ hbox.getWidth() + 0.007));
    }
    public void scrollBtnChangeStyleOnHover(FontAwesomeIconView btn) {
        Animation fadeInAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0.3),
                        new KeyValue(btn.opacityProperty(), 0.8)));
        Animation fadeOutAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0.3),
                        new KeyValue(btn.opacityProperty(), 0.3)));
        btn.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                fadeInAnimation.play();
                btn.setOpacity(0.5);
            }
        });
        btn.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                fadeOutAnimation.play();
                btn.setOpacity(0.2);
            }
        });
    }
    public void scrollAnimation(DoubleProperty property, double seconds, double targetHvalue) {
        Animation animation = new Timeline(
                new KeyFrame(Duration.seconds(seconds),
                        new KeyValue(property, targetHvalue)));
        animation.play();
    }
    public void ConstructCinemaButton(){
        CinemaButton = new ArrayList<Button>();
        cinemaInfor = new ArrayList<ArrayList<String>>();
        cinemaSection = new HBox();
        cinemaSection.setSpacing(3);
        cinemaSectionScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        cinemaInfor = main.getProcessorManager().getCinemaManagementProcessor().getData(0, -1, "", "").getData();
        System.out.println(cinemaInfor);
        cinemaName = Utils.getDataValuesByColumnName(cinemaInfor, "CINEMAS.NAME");
        System.out.println(cinemaName);
        for(String cinema : cinemaName){
            Button button = new Button();
            button.setWrapText(true);
            button.setTextAlignment(TextAlignment.CENTER);
            //button.setPrefSize(134,48);
            button.setMinSize(134, 48);
            button.setStyle("-fx-background-color: #2B2B2B");
            button.setFont(Font.font("Georgia"));
            button.setTextFill(Color.WHITE);
            button.setText(cinema);
            button.setOnAction(e->handleDateButtonAction(e));
            CinemaButton.add(button);
            cinemaSection.getChildren().add(button);
        }
        cinemaSectionScrollPane.setContent(cinemaSection);
        ConstructActiveList(CinemaButton, isCinemaActive);

    }
    public void setDefautlColor(int i, ArrayList<Button> ListButton){
        for(int j = 0; j < ListButton.size(); j++){
            if(j == i)
                continue;
            ListButton.get(j).setStyle("-fx-background-color: #2B2B2B");
        }
    }
    public void setActiveButton(int i, ArrayList<Boolean> ListActive){
        for(int j = 0; j < ListActive.size(); j++){
            if(i == j)
                continue;
            ListActive.set(j, false);
        }
    }
    public String getIdFromIndex(ArrayList<ArrayList<String>> infor, int index ) {
        String id = infor.get(2 + index).get(0);
        return id;
    }
    public void setClick(ArrayList<Button> ListButton, Button button, ArrayList<Boolean> ListActive) {
        int i;
        for (i = 0; i < ListButton.size(); i++) {
            if (ListButton.get(i) == button)
                break;
        }
        if (!ListActive.get(i)) {
            ListButton.get(i).setStyle("-fx-background-color: #760404");
            setDefautlColor(i, ListButton);
            ListActive.set(i, true);
            setActiveButton(i, ListActive);
        } else {
            ListActive.set(i, false);
            ListButton.get(i).setStyle("-fx-background-color: #2B2B2B");
        }
        if (CinemaButton.contains(button)) {
            System.out.println(getIdFromIndex(cinemaInfor, CinemaButton.indexOf(button)));
            cinemaId = getIdFromIndex(cinemaInfor, CinemaButton.indexOf(button));
            ConstructRoomButton();

        }
        if(RoomButton.contains(button)){
            System.out.println(getIdFromIndex(roomInfor, RoomButton.indexOf(button)));
            roomId = getIdFromIndex(roomInfor, RoomButton.indexOf(button));
            if(button.getStyle() != "-fx-background-color: #8D090D") {
                seatMapContainer.getChildren().remove(seatGrid);
                seatGrid = new GridPane();
//                SeatName = new ArrayList<String>();
            }
            ConstructSeatGrid();
        }
        displayAnnounce();
    }
    public void handleDateButtonAction(ActionEvent event) {
        Button button = (Button) event.getSource();
        if (RoomButton.contains(button)){
            setClick(RoomButton, button, isRoomActive);
            if (roomSection.getChildren().isEmpty()) {
                announceRoom.setText("Please choose one cinema you would you like to watch in listed above");
            }
        }
        else {
            setClick(CinemaButton, button, isCinemaActive);
        }
    }
    public void ConstructRoomButton(){
        RoomButton = new ArrayList<Button>();
        roomInfor = new ArrayList<ArrayList<String>>();
        roomSection = new HBox();
        roomSection.setSpacing(3);
        roomSecionScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        roomInfor = main.getProcessorManager().getScreenRoomManagementProcessor().getData(0, -1, String.format("CINEMAS.ID = '%s'", cinemaId),"").getData();
        System.out.println(roomInfor);
        roomName = Utils.getDataValuesByColumnName(roomInfor, "SCREEN_ROOMS.NAME");
        int i = 0;
        for(String room : roomName){
            Button button = new Button();
            button.setWrapText(true);
            button.setTextAlignment(TextAlignment.CENTER);
            //button.setPrefSize(134,48);
            button.setMinSize(75, 48);
            button.setStyle("-fx-background-color: #2B2B2B");
            button.setFont(Font.font("Georgia"));
            button.setTextFill(Color.WHITE);
            button.setText(room);
            button.setOnAction(e->handleDateButtonAction(e));
            RoomButton.add(button);
            i++;
            roomSection.getChildren().add(button);

        }
        roomSecionScrollPane.setContent(roomSection);
        ConstructActiveList(RoomButton, isRoomActive);
    }
    public void displayAnnounce(){

    }
    public void ConstructActiveList(ArrayList<Button> ListButton, ArrayList<Boolean> ListActive){
        for (int i = 0; i < ListButton.size(); i++){
            ListActive.add(false);
        }
    }

    public void ConstructSeatGrid(){
        seatGrid.setPrefSize(507, 298);
        seatGrid.setLayoutX(296);
        seatGrid.setLayoutY(316);
        seatGrid.setHgap(10);
        seatGrid.setVgap(10);
        if(!seatMapContainer.getChildren().contains(seatGrid))
            seatMapContainer.getChildren().add(seatGrid);
        ArrayList<ArrayList<String>> SeatInfor = main.getProcessorManager().getSeatManagementProcessor().getData(0, -1, String.format("SCREEN_ROOM_ID = '%s'", roomId), "").getData();
        ArrayList<String> SeatList = Utils.getDataValuesByColumnName(SeatInfor, "NAME");
        System.out.println(SeatList);
        int r = 0;
        for(int i = 0; i < SeatList.size(); i++){
            String s = SeatList.get(i);
            int rowIndex = s.charAt(0) - 'A';
            int columnIndex = Integer.parseInt(s.substring(1))-1;
            Button button = new Button(s);
            button.setTextFill(Color.BLACK);
            button.setPrefSize(25,34);
            button.setFont(Font.font(7.5));
            button.setWrapText(true);
            String category = Utils.getDataValuesByColumnName(SeatInfor, "SEAT_CATEGORY_ID").get(i);
            if(category.equals("SC_00001"))
                button.setStyle("-fx-background-color: #A4A4A4");
            else if (category.equals("SC_00002"))
                button.setStyle("-fx-background-color: #A4A4A4;-fx-border-color: yellow");
            else button.setStyle("-fx-background-color:  #FF00D6");
            if(!bookingProcessor.checkActive(Utils.getDataValuesByColumnName(SeatInfor, "STATUS").get(i))){
                button.setStyle("-fx-background-color: #ffffff;");
                button.setText("X");
            }
            seatGrid.add(button, columnIndex, rowIndex);
            button.setOnAction(event->{
                    if (button.getStyle() != "-fx-background-color: #8D090D") {
                        if(category.equals("SC_00003")) {
                            int numberSeat = Integer.parseInt(button.getText().substring(1));

                            if (numberSeat % 2 == 0) {
                                nearbyButton = (Button) seatGrid.getChildren().get(seatGrid.getChildren().indexOf(button) - 1);
                            } else {
                                nearbyButton = (Button) seatGrid.getChildren().get(seatGrid.getChildren().indexOf(button) + 1);
                            }
                            nearbyButton.setStyle("-fx-background-color: #8D090D");
                            seatIdSelected.add(getIdFromIndex(SeatInfor, SeatList.indexOf(nearbyButton.getText())));
                            seatBtnSelected.add(nearbyButton);
                        }
                        button.setStyle("-fx-background-color: #8D090D");
                        seatIdSelected.add(getIdFromIndex(SeatInfor, SeatList.indexOf(s)));
                        seatBtnSelected.add(button);
                    } else {
                        if (category.equals("SC_00001"))
                            button.setStyle("-fx-background-color: #A4A4A4");
                        else if (category.equals("SC_00002"))
                            button.setStyle("-fx-background-color: #A4A4A4;-fx-border-color: yellow");
                        else {
                            button.setStyle("-fx-background-color:  #FF00D6");
                            nearbyButton.setStyle("-fx-background-color:  #FF00D6");
                            seatIdSelected.remove(getIdFromIndex(SeatInfor, SeatList.indexOf(nearbyButton.getText())));
                            seatBtnSelected.remove(nearbyButton);
                        }
                        seatIdSelected.remove(getIdFromIndex(SeatInfor, SeatList.indexOf(s)));
                        seatBtnSelected.remove(button);
                    }
                System.out.println(seatIdSelected);
                System.out.println(seatBtnSelected);
                    seatGrid.requestFocus();
            });
            r++;
            if(r == 12)
            {
                Button subButton = new Button();
                subButton.setId("subButton" + numberSubBtn);
                numberSubBtn++;
                subButton.setStyle("-fx-border-color: green; -fx-background-color: transparent;");
                seatGrid.add(subButton, r, rowIndex);
                r = 0;
                subButton.setOnAction(e->{
                    if(subButton.getStyle().equals("-fx-border-color: green; -fx-background-color: transparent;")){
                        subButton.setStyle("-fx-border-color: red; -fx-background-color: transparent;");
                        seatIdSelected.add(subButton.getId());
                        seatBtnSelected.add(subButton);
                    }
                    else {
                        subButton.setStyle("-fx-border-color: green; -fx-background-color: transparent;");
                        seatIdSelected.remove(subButton.getId());
                        seatBtnSelected.remove(subButton);
                    }
                    System.out.println(seatIdSelected);
                    System.out.println(seatBtnSelected);
                    seatGrid.requestFocus();
                });
            }
        }
    }
    @FXML
    public void handleDeleteSeatList(){
        for(Button button : seatBtnSelected )
            seatGrid.getChildren().remove(button);
    }
    @FXML
    public void handleInsertSeat(){

    }
    @FXML
    public void handleUpdateSeat(){

    }
    @FXML
    public void handleBlockSeatList(){
        for (Button button : seatBtnSelected){
            button.setStyle("-fx-background-color: #ffffff;");
            button.setText("X");
        }
    }

}
