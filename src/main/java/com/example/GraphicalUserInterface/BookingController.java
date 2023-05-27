package com.example.GraphicalUserInterface;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.skin.ScrollBarSkin;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import Database.BookingProcessor;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.animation.*;

import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.controlsfx.glyphfont.FontAwesome;

import java.util.*;
import Utils.Utils;


public class BookingController {
    //page1
    @FXML
    private Button home;
    @FXML
    private  AnchorPane pane1;
    @FXML
    private ImageView image1;
    private ArrayList<Button> DateButton = new ArrayList<Button>();
    private ArrayList<Boolean> isDateActive = new ArrayList<Boolean>();
    private ArrayList<Button> CinemaButton  = new ArrayList<Button>();
    private ArrayList<Boolean> isCinemaActive = new ArrayList<Boolean>();
    private ArrayList<Button> TimeButton = new ArrayList<Button>();
    private ArrayList<Boolean> isTimeActive = new ArrayList<Boolean>();
    @FXML
    private Button DateBtn1, DateBtn2, DateBtn3, DateBtn4, DateBtn5, DateBtn6, DateBtn7;
    @FXML
    private HBox cinemaSection = new HBox();
    @FXML
    private ScrollPane cinemaSectionScrollPane = new ScrollPane();
    @FXML
    private HBox timeSection = new HBox();
    @FXML
    private ScrollPane timeSlotSecionScrollPane = new ScrollPane();
    @FXML
    private FontAwesomeIconView cineScrollLeftBtn, cineScrollRightBtn, timeSlotScrollLeftBtn, timeSlotScrollRightBtn;
    @FXML
    private AnchorPane cinemaAnchorPane = new AnchorPane();
    @FXML
    private Label announceTime, announceCinema;
    private ArrayList<ArrayList<String>> cinemaInfor = new ArrayList<ArrayList<String>>();
    private ArrayList<String> cinemaName;
    private ArrayList<ArrayList<String>> timeInfor = new ArrayList<ArrayList<String>>();
    private ArrayList<String> timeSlotList;
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
        scrollBtnInit();
        //page3
        ConstructItemsButton();
        ConstructItem();
        //page4

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
        bookingProcessor.getBookingInfor().setIdMovie(Main.getInstance().getMovieOnBooking().getId());
        bookingProcessor.getBookingInfor().setNameMovie(Main.getInstance().getMovieOnBooking().getTitle());
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
         setScreenTicketInfor();
         price.setText("0");
         combo.setText("0");
         total.setText("0");
         seatID.setText("");
         countdownLabel.setText("15:00");
         setCountDown();

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
        cinemaSection.setSpacing(5);
        cinemaSectionScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        ArrayList<String> nameTypeCoulumn = new ArrayList<String>();
        ArrayList<String> nameColumn = new ArrayList<String>();
        nameColumn.add("ID");
        nameColumn.add("NAME");
        nameTypeCoulumn.add("Varchar");
        nameTypeCoulumn.add("Varchar");
        cinemaInfor.add(nameColumn);
        cinemaInfor.add(nameTypeCoulumn);
        cinemaInfor.addAll( bookingProcessor.getTheaterList());
        System.out.println(cinemaInfor);
        cinemaName = Utils.getDataValuesByColumnName(cinemaInfor, "NAME");
        for(String cinema : cinemaName){
            Button button = new Button();
            button.setWrapText(true);
            button.setTextAlignment(TextAlignment.CENTER);
            //button.setPrefSize(134,48);
            button.setMinSize(134, 48);
            button.setStyle("-fx-background-color: #2B2B2B");
            button.setTextFill(Color.WHITE);
            button.setText(cinema);
            button.setOnAction(e->handleDateButtonAction(e));
            CinemaButton.add(button);
            cinemaSection.getChildren().add(button);
        }
        ConstructActiveList(CinemaButton, isCinemaActive);
     }
    public String getCinemaIdFromName(ArrayList<ArrayList<String>> infor, ArrayList<String> names, String name ) {
        String id = null;
        for (int i=0; i<names.size();++i) {
            if (names.get(i).equals(name)) {
                id = infor.get(2 + i).get(0);
                break;
            }
        }
        return id;
    }
     public void ConstructTimeButton(){
        timeSection.setSpacing(5);
        timeSlotSecionScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
         ArrayList<String> nameTypeCoulumn = new ArrayList<String>();
         ArrayList<String> nameColumn = new ArrayList<String>();
         nameColumn.add("ID");
         nameColumn.add("TIME_SLOT");
         nameTypeCoulumn.add("Varchar");
         nameTypeCoulumn.add("Varchar");
         timeInfor.add(nameColumn);
         timeInfor.add(nameTypeCoulumn);
         timeInfor.addAll(bookingProcessor.getTimeSlotList());
         System.out.println(timeInfor);
         timeSlotList = Utils.getDataValuesByColumnName(timeInfor, "TIME_SLOT");
        for(String timeSlot : timeSlotList){
            Button button = new Button();
            button.setWrapText(true);
            button.setTextAlignment(TextAlignment.CENTER);
            //button.setPrefSize(134,48);
            button.setMinSize(75, 48);
            button.setStyle("-fx-background-color: #2B2B2B");
            button.setTextFill(Color.WHITE);
            button.setText(timeSlot);
            button.setOnAction(e->handleDateButtonAction(e));
            TimeButton.add(button);
            timeSection.getChildren().add(button);
        }
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
            button.setPrefSize(25,34);
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
                SeatGrid.requestFocus();
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
     public void displayAnnounce(){
        if(showDate.getText() == ""){
            InvisibleScrollpane(cinemaSection, cineScrollLeftBtn, cineScrollRightBtn);
            InvisibleScrollpane(timeSection, timeSlotScrollLeftBtn, timeSlotScrollRightBtn);
            announceCinema.setVisible(true);
            announceTime.setVisible(true);
        }else {
            VisibleScrollpane(cinemaSection, cineScrollLeftBtn, cineScrollRightBtn);
            System.out.println(cinemaSection.isVisible());
            if (nameCinema.getText() == "") {
                System.out.println(nameCinema.getText() + "Aaaaaaaaaa");
                InvisibleScrollpane(timeSection, timeSlotScrollLeftBtn, timeSlotScrollRightBtn);
                announceCinema.setVisible(false);
                announceTime.setVisible(true);
            } else {
                VisibleScrollpane(timeSection, timeSlotScrollLeftBtn, timeSlotScrollRightBtn);
                announceCinema.setVisible(false);
                announceTime.setVisible(false);

            }
        }
     }
     public void InvisibleScrollpane(HBox hbox, FontAwesomeIconView left, FontAwesomeIconView right){
        hbox.setVisible(false);
        left.setVisible(false);
        right.setVisible(false);
     }
    public void VisibleScrollpane(HBox hbox, FontAwesomeIconView left, FontAwesomeIconView right){
        hbox.setVisible(true);
        left.setVisible(true);
        right.setVisible(true);
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
                for(j = 0; j < DateButton.size(); j++)
                    if(button == DateButton.get(j))
                        break;
                LocalDateTime now = LocalDateTime.now().plusDays(i);
                DateTimeFormatter ShowDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                label.setText(now.format(ShowDateFormat));
                DateTimeFormatter showDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                System.out.println(now.format(showDateFormat));
                bookingProcessor.getBookingInfor().setDate(now.format(showDateFormat));
                System.out.println(bookingProcessor.getBookingInfor().getDate());
                ConstructCinemaButton();
            }
            else
                label.setText(button.getText());
        }else{
            ListActive.set(i, false);
            ListButton.get(i).setStyle("-fx-background-color: #2B2B2B");
            label.setText("");
        }
        if(CinemaButton.contains(button)){
            System.out.println(getCinemaIdFromName(cinemaInfor, cinemaName, nameCinema.getText()));
            bookingProcessor.getBookingInfor().setNameCinema(getCinemaIdFromName(cinemaInfor, cinemaName, nameCinema.getText()));
            ConstructTimeButton();
        }
        if(TimeButton.contains(button)){
            System.out.println(getCinemaIdFromName(timeInfor, timeSlotList, showTime.getText()));
            bookingProcessor.getBookingInfor().setTime(getCinemaIdFromName(timeInfor, timeSlotList, showTime.getText()));
            ConstructTicketInfor();
            System.out.println(bookingProcessor.getBookingInfor().getScreen());
            ConstructSeatGrid();
        }
        displayAnnounce();
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

        this.screenName.setText(bookingProcessor.getScreen());

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
    public void scrollBtnInit() {
        scrollBtnChangeStyleOnHover(cineScrollLeftBtn);
        scrollBtnChangeStyleOnHover(cineScrollRightBtn);
        scrollBtnChangeStyleOnHover(timeSlotScrollLeftBtn);
        scrollBtnChangeStyleOnHover(timeSlotScrollRightBtn);
    }
    public void scrollAnimation(DoubleProperty property, double seconds, double targetHvalue) {
        Animation animation = new Timeline(
                new KeyFrame(Duration.seconds(seconds),
                        new KeyValue(property, targetHvalue)));
        animation.play();
    }
    public ScrollPane getScrollPane(FontAwesomeIconView button){
        if(button.getId().contains("cine")){
            return cinemaSectionScrollPane;
        }
        if(button.getId().contains("time")){
            return timeSlotSecionScrollPane;
        }
        return null;
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
    public void onButtonClicked(){

    }



}
