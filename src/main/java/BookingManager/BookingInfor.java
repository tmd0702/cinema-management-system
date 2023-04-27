package BookingManager;

import com.example.GraphicalUserInterface.BookingController;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
public class BookingInfor {
    private String idMovie, nameMovie, nameCinema, date, time, screen;
    private ArrayList<String> seats;
    private ArrayList<Integer> items;
    private int ticketPrice, comboPrice, total;

    public BookingInfor(){
        this.idMovie = "";
        this.nameMovie = "";
        this.nameCinema = "";
        this.date = "";
        this.time = "";
        this.screen = "";
        this.seats = new ArrayList<>();
        this.items = new ArrayList<>();
        this.ticketPrice = 0;
        this.comboPrice = 0;
        this.total = 0;
    }
    public BookingInfor(String id, String movie, String cinema, String date, String time, String screen, ArrayList<String> seats, ArrayList<Integer> items, int ticketp, int combop, int totalp){
        this.idMovie = id;
        this.nameMovie = movie;
        this.nameCinema = cinema;
        this.date = date;
        this.time = time;
        this.screen = screen;
        this.seats = seats;
        this.items = items;
        this.ticketPrice = ticketp;
        this.comboPrice = combop;
        this.total = totalp;
    }
    public void setIdMovie(String id){this.idMovie = id;}
    public void addItems(Integer item){
        this.items.add(item);
    }
    public void addSeats(String seat){
        this.seats.add(seat);
    }
    public void setNameMovie(String movie){this.nameMovie = movie;}
    public void setNameCinema(String cinema){this.nameCinema = cinema;}
    public void setDate(String date){this.date = date;}
    public void setTime(String time){this.time = time;}
    public void setScreen(String screen){this.screen = screen;}
    public void setSeats(ArrayList<String> seats){this.seats = seats;}
    public void setItems(ArrayList<Integer> items){this.items = items;}
    public void setTicketPrice(int price){this.ticketPrice = price;}
    public void setComboPrice(int price){this.comboPrice = price;}
    public void setTotal(int price){this.total = price;}
    public String getNameMovie(){ return this.nameMovie;}
    public String getNameCinema(){return this.nameCinema;}
    public String getDate(){return this.date;}
    public String getTime(){return this.time;}
    public String getScreen(){return this.screen;}
    public ArrayList<String> getSeats(){return this.seats;}
    public ArrayList<Integer> getItems(){return this.items;}
    public int getTicketPrice(){return this.ticketPrice;}
    public int getComboPrice(){return this.comboPrice;}
    public int getTotal(){return this.total;}
    public String getIdMovie(){return this.idMovie;}


}
