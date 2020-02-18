package sample;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;

import java.sql.*;
import java.util.ArrayList;

public class Bookings {
    AnchorPane bookingpane = new AnchorPane();
    Stage bookingstage = new Stage();
    private Label pesetasLabel;
    private Label couponLabel;
    private TextArea ticketArea;
    private Button buyPesetas;
    private Button buyCoupon;
    private Button goBack;
    private Button showRecipt;
    private TextField chooseTicket;
    private Statement loggedIn;
    private LoginController loginController;
    private InloggedCus inloggedCus;
    private int customerPesetas;

    private static InloggedCus currentCustomer;

    public Bookings() throws SQLException {
        bookingstage.setTitle("MyBookings");
        initiazlizeButtons();
        bookingstage.setScene(new Scene(bookingpane,600,500));
        bookingstage.show();
    }


    public javafx.event.EventHandler<MouseEvent> recipt=new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            ticketArea.clear();
            try {
                loggedIn = loginController.getLoginController().getLoginConnection().createStatement();
                String recipt = "SELECT ticketid,boughttype FROM cd.bookings WHERE customerid ='"+ loginController.getLoginController().getUserNameForInlog() +"'";
                ArrayList <String> printRecipt = new ArrayList<>();
                ResultSet getTickets = loggedIn.executeQuery(recipt);
                while (getTickets.next()) {
                    printRecipt.add(getTickets.getString("ticketid"));
                    printRecipt.add(getTickets.getString("boughttype"));
                }
                for (int i = 0; i < printRecipt.size(); i++) {
                    if (i%2==0) {
                        ticketArea.setText(ticketArea.getText()+ "\n");
                    }
                    ticketArea.setText(ticketArea.getText()+printRecipt.get(i) + " ");
                }
                loggedIn.close();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
    };


    public javafx.event.EventHandler<MouseEvent> goBackOnClick=new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            bookingstage.close();
        }
    };


    public javafx.event.EventHandler<MouseEvent> buywithcoupons=new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            try {
                if (getCouponsForLabel()>=1) {
                    loggedIn = loginController.getLoginController().getLoginConnection().createStatement();
                    String booking = "INSERT INTO cd.bookings (customerid,ticketid,boughttype)" +
                            "VALUES((SELECT customerid FROM cd.customer WHERE customerid =" + "'" + loginController.getLoginController().getUserNameForInlog() + "'" + ")," +
                            "(SELECT ticketid FROM cd.tickets WHERE ticketid = " + "'" + chooseTicket.getText() + "'" + "),'coupon')";
                    loggedIn.executeUpdate(String.format(booking));
                    loggedIn.close();
                    changeBoughtTicketStatus(chooseTicket.getText());
                    changeCouponAmountAfterBuy();
                    getTicketsForTextArea();
                }
                else {
                    chooseTicket.setText("not enought coupons");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    };

    public javafx.event.EventHandler<MouseEvent> buywithpesetas=new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            try {
                customerPesetas = inloggedCus.getCurrentCustomer().calculatePesetas();
                int pricecost = getTicketCost();
                if (customerPesetas >= pricecost) {
                    loggedIn = loginController.getLoginController().getLoginConnection().createStatement();
                    String booking = "INSERT INTO cd.bookings (customerid,ticketid,boughttype)" +
                            "VALUES((SELECT customerid FROM cd.customer WHERE customerid =" + "'" + loginController.getLoginController().getUserNameForInlog() + "'" + ")," +
                            "(SELECT ticketid FROM cd.tickets WHERE ticketid = " + "'" + chooseTicket.getText() + "'" + "),'pesetas')";
                    loggedIn.executeUpdate(String.format(booking));
                    loggedIn.close();
                    changeBoughtTicketStatus(chooseTicket.getText());
                    updatePesetas(customerPesetas,pricecost);
                    getTicketsForTextArea();
                }
                else {
                    chooseTicket.setText("not enought pesetas");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    };

    private void initiazlizeButtons() throws SQLException {
        buyCoupon = new Button("buy with coupon");
        buyCoupon.setLayoutX(450);
        buyCoupon.setLayoutY(100);
        buyCoupon.addEventFilter(MouseEvent.MOUSE_CLICKED,buywithcoupons);
        buyPesetas = new Button("buy with pesetas");
        buyPesetas.setLayoutX(450);
        buyPesetas.setLayoutY(150);
        buyPesetas.addEventFilter(MouseEvent.MOUSE_CLICKED,buywithpesetas);
        showRecipt = new Button("show recipt");
        showRecipt.setLayoutX(450);
        showRecipt.setLayoutY(300);
        showRecipt.addEventFilter(MouseEvent.MOUSE_CLICKED,recipt);
        pesetasLabel = new Label("pesetas: " + getPesetasForLabel());
        pesetasLabel.setLayoutX(350);
        pesetasLabel.setLayoutY(150);
        couponLabel = new Label("coupons: " + getCouponsForLabel());
        couponLabel.setLayoutX(350);
        couponLabel.setLayoutY(100);
        ticketArea = new TextArea();
        ticketArea.setLayoutX(20);
        ticketArea.setLayoutY(50);
        ticketArea.isResizable();
        ticketArea.setPrefSize(300,300);
        getTicketsForTextArea();
        chooseTicket = new TextField("type the ticketID");
        chooseTicket.setLayoutX(360);
        chooseTicket.setLayoutY(200);
        goBack = new Button("close window");
        goBack.setLayoutY(450);
        goBack.setLayoutX(500);
        goBack.addEventFilter(MouseEvent.MOUSE_CLICKED, goBackOnClick);

        bookingpane.getChildren().addAll(buyCoupon,buyPesetas,pesetasLabel,couponLabel,ticketArea,chooseTicket,goBack,showRecipt);
    }
         private int getPesetasForLabel() throws SQLException {
             int pesetas = 0;
             loggedIn = loginController.getLoginController().getLoginConnection().createStatement();
             String login = "SELECT pesetas FROM cd.customer WHERE customerid=" + "'" + loginController.getLoginController().getUserNameForInlog() + "'"
                     + " AND password= " + "'" + loginController.getLoginController().getPasswordForInlog() + "'";
             ResultSet getPesetas = loggedIn.executeQuery(login);
             while (getPesetas.next()) {
                 pesetas = getPesetas.getInt("pesetas");
             }
             return pesetas;
         }

         private int getCouponsForLabel() throws SQLException {
             int pesetas = 0;
             loggedIn = loginController.getLoginController().getLoginConnection().createStatement();
             String coupons = "SELECT coupon FROM cd.customer WHERE customerid=" + "'" + loginController.getLoginController().getUserNameForInlog() + "'";
             ResultSet getcoupons = loggedIn.executeQuery(coupons);
             while (getcoupons.next()) {
                 pesetas = getcoupons.getInt("coupon");
             }
             return pesetas;
         }
         private void getTicketsForTextArea() throws SQLException {
        ticketArea.clear();
             loggedIn = loginController.getLoginController().getLoginConnection().createStatement();
             String tickets = "SELECT ticketid, artistname FROM cd.tickets INNER JOIN cd.konsert ON konsertid ="
                     + "'" + inloggedCus.getCurrentCustomer().getKonsertID() + "'" + " AND ticketdate = konsertdate" +
                     " AND boughtstatus ='1'";
             ArrayList <String> ticketarray = new ArrayList<>();
             ResultSet getTickets = loggedIn.executeQuery(tickets);
             while (getTickets.next()) {
                 ticketarray.add(getTickets.getString("ticketid"));
                 ticketarray.add(getTickets.getString("artistname"));
             }
             for (int i = 0; i < ticketarray.size(); i++) {
                 if (i%2==0) {
                     ticketArea.setText(ticketArea.getText()+ "\n");
                 }
                 ticketArea.setText(ticketArea.getText()+ticketarray.get(i) + " ");

             }
             loggedIn.close();
         }

         private int getTicketCost() throws SQLException {
             loggedIn = loginController.getLoginController().getLoginConnection().createStatement();
             String ticketprice = "SELECT price FROM cd.tickets WHERE ticketid = " + "'" +  chooseTicket.getText() + "'" +"";
             ResultSet priceResult = loggedIn.executeQuery(ticketprice);
             int priceString =0;
             while (priceResult.next()) {
                 priceString = priceResult.getInt("price");
             }
             return priceString;
         }
         private void updatePesetas(int currentPesetas,int costPesetas) throws SQLException {
             loggedIn = loginController.getLoginController().getLoginConnection().createStatement();
             String newPesetas = "UPDATE cd.customer SET pesetas = "+currentPesetas +"-" +costPesetas+ "WHERE customerid ="+"'" + loginController.getLoginController().getUserNameForInlog() + "'";
             loggedIn.executeUpdate(String.format(newPesetas));
             loggedIn.close();
             pesetasLabel.setText("pesetas:" +getPesetasForLabel());
         }
         private void changeBoughtTicketStatus(String ticketid) throws SQLException {
             loggedIn = loginController.getLoginController().getLoginConnection().createStatement();
             String statusTicket = "UPDATE cd.tickets SET boughtstatus = 0 Where ticketid ='"+ticketid +"'";
             loggedIn.executeUpdate(statusTicket);
             loggedIn.close();
         }
         private void changeCouponAmountAfterBuy() throws SQLException {
             loggedIn = loginController.getLoginController().getLoginConnection().createStatement();
             String statusTicket = "UPDATE cd.customer SET coupon = (coupon -1) Where customerid ='"+ loginController.getLoginController().getUserNameForInlog() +"'";
             loggedIn.executeUpdate(statusTicket);
             loggedIn.close();
             couponLabel.setText("coupons: " + getCouponsForLabel());

         }
}








