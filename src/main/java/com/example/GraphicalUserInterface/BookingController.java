package com.example.GraphicalUserInterface;
import Utils.StatusCode;
import Utils.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.TriangleMesh;
//import Database.BookingProcessor;
import java.sql.Time;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.time.format.DateTimeFormatter;
import java.util.*;


public class BookingController {
    //page1
    @FXML
    private  AnchorPane pane1;
    @FXML
    private AnchorPane AnchorDateButton = new AnchorPane();
    private ArrayList<Button> DateButton = new ArrayList<Button>();
    private ArrayList<Boolean> isDateActive = new ArrayList<Boolean>();
    private ArrayList<Button> CinemaButton  = new ArrayList<Button>();
    private ArrayList<Boolean> isCinemaActive = new ArrayList<Boolean>();
    private ArrayList<Button> TimeButton = new ArrayList<Button>();
    private ArrayList<Boolean> isTimeActive = new ArrayList<Boolean>();
    @FXML
    private Button DateBtn1;
    @FXML
    private Button DateBtn2;
    @FXML
    private Button DateBtn3;
    @FXML
    private Button DateBtn4;
    @FXML
    private Button DateBtn5;
    @FXML
    private Button DateBtn6;
    @FXML
    private Button DateBtn7;
    @FXML
    private Button Cinema1;
    @FXML
    private Button Cinema2;
    @FXML
    private Button Cinema3;
    @FXML
    private Button Cinema4;
    @FXML
    private Button Time1;
    @FXML
    private Button Time2;
    @FXML
    private Button Time3;
    @FXML
    private Button Time4;
    @FXML
    private Button Time5;
    @FXML
    private Button Time6;
    private LocalDateTime now = LocalDateTime.now();
    // page2
    @FXML
    private StackPane stackpane;
    @FXML
    private  AnchorPane pane2;
    @FXML
    private GridPane SeatGrid;
    private ArrayList<ArrayList<Boolean>> isSeatActive = new ArrayList<ArrayList<Boolean>>();


    @FXML
     public void initialize(){
        ConstructDateButton();
        FormartDateButton();
        ConstructCinemaButton();
        ConstructTimeButton();
        ConstructSeatGrid();
    }
     public void ConstructDateButton(){
         DateButton.add(DateBtn1);
         DateButton.add(DateBtn2);
         DateButton.add(DateBtn3);
         DateButton.add(DateBtn4);
         DateButton.add(DateBtn5);
         DateButton.add(DateBtn6);
         DateButton.add(DateBtn7);
         ConstructActiveList(DateButton, isDateActive);
     }
     public void ConstructCinemaButton(){
        CinemaButton.add(Cinema1);
        CinemaButton.add(Cinema2);
        CinemaButton.add(Cinema3);
        CinemaButton.add(Cinema4);
         ConstructActiveList(CinemaButton, isCinemaActive);
     }
     public void ConstructTimeButton(){
        TimeButton.add(Time1);
        TimeButton.add(Time2);
        TimeButton.add(Time3);
        TimeButton.add(Time4);
        TimeButton.add(Time5);
        TimeButton.add(Time6);
         ConstructActiveList(TimeButton, isTimeActive);
     }
     public void ConstructSeatGrid(){
         int k = 1;
         for(int i = 0; i < 7; i++){
             ArrayList<Boolean> isActive = new ArrayList<Boolean>();
             for(int j = 0; j < 12; j++){
                 if(i == 0 && (j == 0 || j == 1 || j == 10 || j == 11) ||  i == 1 && (j == 0 || j == 11))
                     continue;
                 Button b = new Button();
                 b.setStyle("-fx-background-color: #A4A4A4");
                 b.setOnMouseClicked((MouseEvent event) -> {
                    b.setStyle("-fx-background-color: #8D090D");
                 });
                 SeatGrid.add(b , j, i);
                 isActive.add(false);
             }
             k += 11;
             isSeatActive.add(isActive);
         }
     }
     public void ConstructActiveList(ArrayList<Button> ListButton, ArrayList<Boolean> ListActive){
         for (int i = 0; i < ListButton.size(); i++){
             ListActive.add(false);
         }
     }
    public void handleDateButtonAction(ActionEvent event) {
        Button button = (Button) event.getSource();
        if(DateButton.contains(button)) {
            setClick(DateButton, button, isDateActive);
        }
        else if (CinemaButton.contains(button)){
            setClick(CinemaButton, button, isCinemaActive);
        }
        else
            setClick(TimeButton, button, isTimeActive);
    }
    public void setClick(ArrayList<Button> ListButton, Button button, ArrayList<Boolean> ListActive){
        int i;
        for(i = 0; i < ListButton.size(); i++){
            if(ListButton.get(i) == button)
                break;
        }
        if(!ListActive.get(i)){
            ListButton.get(i).setStyle("-fx-background-color: #760404");
            setDefautlColor(i, ListButton);
            ListActive.set(i, true);
            setActiveButton(i, ListActive);
        }else{
            ListActive.set(i, false);
            ListButton.get(i).setStyle("-fx-background-color: #2B2B2B");
        }
    }
    public void setActiveButton(int i, ArrayList<Boolean> ListActive){
        for(int j = 0; j < ListActive.size(); j++){
            if(i == j)
                continue;
            ListActive.set(j, false);
        }
    }
    public void setDefautlColor(int i, ArrayList<Button> ListButton){
        for(int j = 0; j < ListButton.size(); j++){
            if(j == i)
                continue;
            ListButton.get(j).setStyle("-fx-background-color: #2B2B2B");
        }
    }
    public void setTime(LocalDateTime now, Button b){
        DateTimeFormatter dateFormater = DateTimeFormatter.ofPattern("dd/MM");
        DateTimeFormatter dayOfWeekformater = DateTimeFormatter.ofPattern("EEEE");
        String Formateddate = now.format(dateFormater);
        String Formateddyaofweek  =now.format(dayOfWeekformater);
        b.setText(Formateddyaofweek + " " + Formateddate);
    }
    public void FormartDateButton(){
            LocalDateTime subnow;
            for (int i = 0; i < DateButton.size(); i++) {
                subnow = now.plusDays(i);
                setTime(subnow, DateButton.get(i));
            }
    }
    public void handleswitchtoPagebefore(ActionEvent event){
        stackpane.getChildren().remove(pane2);
        if(!stackpane.getChildren().contains(pane1))
            stackpane.getChildren().addAll(pane1);
    }
    public  void handleswitchPageafter(ActionEvent event){
        stackpane.getChildren().remove(pane1);
        if(!stackpane.getChildren().contains(pane2))
        stackpane.getChildren().addAll(pane2);
    }
    public void onButtonClicked(){

    }




}
