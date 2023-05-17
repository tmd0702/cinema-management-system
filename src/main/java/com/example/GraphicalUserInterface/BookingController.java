package com.example.GraphicalUserInterface;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import Database.BookingProcessor;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.animation.*;

import javafx.util.Duration;

import java.util.*;


public class BookingController {
    //page1
    @FXML
    private Button home;
    @FXML
    private  AnchorPane pane1;
    @FXML
    private ImageView image1;
    @FXML
    private AnchorPane AnchorDateButton = new AnchorPane();
    private ArrayList<Button> DateButton = new ArrayList<Button>();
    private ArrayList<Boolean> isDateActive = new ArrayList<Boolean>();
    private ArrayList<Button> CinemaButton  = new ArrayList<Button>();
    private ArrayList<Boolean> isCinemaActive = new ArrayList<Boolean>();
    private ArrayList<Button> TimeButton = new ArrayList<Button>();
    private ArrayList<Boolean> isTimeActive = new ArrayList<Boolean>();
    @FXML
    private Button DateBtn1, DateBtn2, DateBtn3, DateBtn4, DateBtn5, DateBtn6, DateBtn7;
    @FXML
    private Button Cinema1, Cinema2, Cinema3, Cinema4;
    @FXML
    private Button Time1, Time2, Time3, Time4, Time5, Time6;
    private LocalDateTime now = LocalDateTime.now();
    // page2
    @FXML
    private StackPane stackpane;
    @FXML
    private  AnchorPane pane2;
    @FXML
    private ImageView image2;
    @FXML
    private GridPane SeatGrid = new GridPane();
    private ArrayList<String> SeatId = new ArrayList<String>();
    @FXML
    private Label nameMovieBooking, nameCinema, showTime, showDate, seatID, screenName, price, combo, total;
    @FXML
    private AnchorPane ticketInfor;
    // page3
    @FXML
    private AnchorPane pane3;
    @FXML
    private ImageView image3;
    @FXML
    private Button caraPlus, caraSub, cheesePlus, cheeseSub, cokePlus, cokeSub, comboPlus, comboSub;
    @FXML
    private Label numberOfCara, numberOfCheese, numberOfCoke, numberOfCombo, countdownLabel;

    private ArrayList<Button> CaraButton = new ArrayList<Button>();
    private ArrayList<Button> CheeseButton = new ArrayList<Button>();
    private ArrayList<Button> CokeButton = new ArrayList<Button>();
    private ArrayList<Button> ComboButton = new ArrayList<Button>();
    private ArrayList<AnchorPane> listPane = new ArrayList<AnchorPane>();

    private ArrayList<Integer> numberOfitems= new ArrayList<Integer>();
    //page4
    @FXML
    private AnchorPane pane4;
    @FXML
    private ImageView image4;
    private BookingProcessor bookingProcessor;

    @FXML
     public void initialize(){
        ConstructHomButton();
        ConstructPane();

        //page1
        ConstructDateButton();
        FormartDateButton();
        ConstructCinemaButton();
        ConstructTimeButton();

        //page2
        ConstructSeatGrid();
        ConstructTicketInfor();
        //ConstructBookingProcessor();
        //page3
        ConstructItemsButton();
        ConstructItem();
        //page4
        setCountDown();
    }
    public BookingController(){

     this.bookingProcessor = Main.getInstance().getBookingProcessor();
    }
    public void ConstructHomButton(){
        home.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Main.getInstance().changeScene("index-view.fxml");
                } catch (IOException e) {
                    System.out.println(e);
                }

            }
        });
    }

    public void ConstructItem(){
        for(int i = 0; i < 4; i++)
        numberOfitems.add(0);
    }

    public void ConstructItemsButton(){
        CaraButton.add(caraPlus);
        CaraButton.add(caraSub);
        CheeseButton.add(cheesePlus);
        CheeseButton.add(cheeseSub);
        CokeButton.add(cokePlus);
        CokeButton.add(cokeSub);
        ComboButton.add(comboPlus);
        ComboButton.add(comboSub);
    }
    public void ConstructPane(){
        listPane.add(pane1);
        listPane.add(pane2);
        listPane.add(pane3);
        listPane.add(pane4);
        image1.setImage(Main.getInstance().getMovieOnBooking().getPosterImage());
        image2.setImage(Main.getInstance().getMovieOnBooking().getPosterImage());
        image3.setImage(Main.getInstance().getMovieOnBooking().getPosterImage());
        image4.setImage(Main.getInstance().getMovieOnBooking().getPosterImage());
    }

     public void ConstructTicketInfor() {

         nameMovieBooking.setText(Main.getInstance().getMovieOnBooking().getTitle());
         nameCinema.setText("");
         setScreenTicketInfor();
         showTime.setText("");
         showDate.setText("");
         price.setText("0");
         combo.setText("0");
         total.setText("0");
         seatID.setText("");
         countdownLabel.setText("15:00");

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
         bookingProcessor.getSeat();
         System.out.println(bookingProcessor.getBookingInfor().getSeats());
         int column = 0;
         int row = 0;
         int k = 1;
         ArrayList<String> SeatList = bookingProcessor.getBookingInfor().getSeats();
         String seat = SeatList.get(SeatList.size()-1);
         getColumnRowOfRoom(SeatList, row, column);
        SeatGrid.setHgap(10);
        SeatGrid.setVgap(10);
        for(String s : SeatList){
            int rowIndex = s.charAt(0) - 'A';
            int columnIndex = Integer.parseInt(s.substring(1));
            Button button = new Button(s);
            button.setPrefSize(25,32);
            button.setStyle("-fx-background-color: #A4A4A4");
            button.setFont(Font.font(7.5));
            button.setWrapText(true);
            SeatGrid.add(button, columnIndex, rowIndex);
            button.setOnAction(event->{
                if(button.getStyle() == "-fx-background-color: #A4A4A4") {
                    button.setStyle("-fx-background-color: #8D090D");
                    SeatId.add(button.getText());
                    displaySeatInfor();
                }
                else {
                    button.setStyle("-fx-background-color: #A4A4A4");
                    SeatId.remove(button.getText());
                    displaySeatInfor();
                }

            });
        }
//         for(int i = 0; i < 7; i++){
//             ArrayList<Boolean> isActive = new ArrayList<Boolean>();
//             for(int j = 0; j < 12; j++) {
//                 if (i == 0 && (j == 0 || j == 1 || j == 10 || j == 11) || i == 1 && (j == 0 || j == 11))
//                     continue;
//
////                if(i == 1 && j == 2){
////                    button.setDisable(true);
////                    button.setStyle("-fx-background-color: #393939");
////                    continue;
////
////                }
//
//             }
//             k += 11;
//
//         }

     }
     public void getColumnRowOfRoom(ArrayList<String> list, int r, int c){
         char maxChar = 'A';
         int maxNum = 1;

         for (String s : list) {
             char t = s.charAt(0);
             int num = Integer.parseInt(s.substring(1));

             if (t >= maxChar && num > maxNum) {
                 maxChar = t;
                 maxNum = num;
             }
         }
         r = maxChar;
         c = maxNum;

     }
     public void handleComboButton(ActionEvent event){
            Button b = (Button) event.getSource();
            if(CaraButton.contains(b))
                changeNumberOfItems(numberOfCara, b, CaraButton);
            else if(CheeseButton.contains(b))
                changeNumberOfItems(numberOfCheese, b, CheeseButton);
            else if(CokeButton.contains(b))
                changeNumberOfItems(numberOfCoke, b, CokeButton);
            else if (ComboButton.contains(b))
                changeNumberOfItems(numberOfCombo, b, ComboButton);
            calculateTotal();

     }
     public void changeNumberOfItems(Label label, Button b, ArrayList<Button> List){
        int i = Integer.parseInt(label.getText());
            if(b.getText().equals("-")){
                if(i > 0)
                    i--;
            }
            else i++;
        label.setText(Integer.toString(i));
        getPriceCombo(List, i);
        calculateCombo();
     }
     public void calculateCombo(){
         int p = 0;
         for(int j = 0; j < 4;j++){
             p += 55000 * numberOfitems.get(j);
         }
         combo.setText(Integer.toString(p));
     }
     public void getPriceCombo(ArrayList<Button> List, int i){
        if(List == CaraButton){
            numberOfitems.set(0, i);
        } else if(List == CheeseButton){
            numberOfitems.set(1, i);
        }else if (List == CokeButton){
            numberOfitems.set(2, i);
        }else numberOfitems.set(3, i);
     }

     public void displaySeatInfor(){
             String listseat = "";
             if(SeatId.size() == 0)
                 listseat = "";
             else {
                 for(int i = 0; i < SeatId.size(); i++){
                     listseat += SeatId.get(i) + ", ";
                 }
             }
             seatID.setText(listseat);
             calculatePrice();
             calculateTotal();
     }
     public void calculatePrice(){
         int Price = SeatId.size() * 70000;
         price.setText(Integer.toString(Price));
     }
     public void calculateTotal(){
         total.setText(Integer.toString(Integer.parseInt(price.getText()) + Integer.parseInt(combo.getText())));
     }

     public void ConstructActiveList(ArrayList<Button> ListButton, ArrayList<Boolean> ListActive){
         for (int i = 0; i < ListButton.size(); i++){
             ListActive.add(false);
         }
     }

    public void handleDateButtonAction(ActionEvent event) {
        Button button = (Button) event.getSource();
        if(DateButton.contains(button)) {
            setClick(DateButton, button, isDateActive, showDate);
        }
        else if (CinemaButton.contains(button)){
            setClick(CinemaButton, button, isCinemaActive, nameCinema);
        }
        else
            setClick(TimeButton, button, isTimeActive, showTime);
    }

    public void setClick(ArrayList<Button> ListButton, Button button, ArrayList<Boolean> ListActive, Label label){
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
            if(checkDate(button)) {
                int j;
                for(j = 0; i < DateButton.size(); j++)
                    if(button == DateButton.get(j))
                        break;
                LocalDateTime now = LocalDateTime.now().plusDays(i);
                DateTimeFormatter ShowDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                label.setText(now.format(ShowDateFormat));
            }
            else
                label.setText(button.getText());
        }else{
            ListActive.set(i, false);
            ListButton.get(i).setStyle("-fx-background-color: #2B2B2B");
            label.setText("");
        }
    }

    public boolean checkDate(Button b){
        if(!DateButton.contains(b))
            return false;
        return true;
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

    public void handleSwitchToPageBefore(ActionEvent event){
        Button b = (Button) event.getSource();
        int i;
        for( i = 0; i < listPane.size(); i++) {
            if (listPane.get(i).getChildren().contains(b))
            {
                stackpane.getChildren().remove(listPane.get(i));
                if(i-1 == 1){
                    pane2.getChildren().addAll(ticketInfor);
                    //setScreenTicketInfor();
                }
                break;
            }

        }
        if(!stackpane.getChildren().contains(listPane.get(i-1))) {
            stackpane.getChildren().addAll(listPane.get(i-1));

        }

    }
    public void setScreenTicketInfor(){
        bookingProcessor.getScreen();
        this.screenName.setText( bookingProcessor.getBookingInfor().getScreen());



    }
    public  void handleSwitchPageAfter(ActionEvent event){
        Button b = (Button) event.getSource();
        int i;
        for( i = 0; i < listPane.size(); i++) {
            if (listPane.get(i).getChildren().contains(b)) {
                stackpane.getChildren().remove(listPane.get(i));
                if (i + 1 == 2) {
                    pane3.getChildren().addAll(ticketInfor);
                    //setScreenTicketInfor();
                }
                break;
            }
        }
        if(!stackpane.getChildren().contains(listPane.get(i+1))) {
            stackpane.getChildren().addAll(listPane.get(i + 1));
        }

    }
    public void setCountDown(){
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE); // Chạy vô hạn
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1), event -> {
                    // Giảm giây đi 1 giây
                    int secondsLeft = Integer.parseInt(countdownLabel.getText().substring(3));
                    secondsLeft--;
                    if (secondsLeft < 0) {
                        // Nếu hết giờ, reset về 59 giây
                        secondsLeft = 59;
                        int minutesLeft = Integer.parseInt(countdownLabel.getText().substring(0, 2));
                        minutesLeft--;
                        if (minutesLeft < 0) {
                            minutesLeft = 0;
                            secondsLeft = 0;
                        }
                        countdownLabel.setText(String.format("%02d:%02d", minutesLeft, secondsLeft));
                    } else {
                        // Cập nhật đồng hồ đếm ngược
                        int minutesLeft = Integer.parseInt(countdownLabel.getText().substring(0, 2));
                        countdownLabel.setText(String.format("%02d:%02d", minutesLeft, secondsLeft));
                    }
                })
        );
        timeline.play();
    }
    public void onButtonClicked(){

    }



}
