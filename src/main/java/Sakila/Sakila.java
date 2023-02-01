package Sakila;

import View.StaffView;
import dbConn.ConnectionMaker;
import dbConn.MySqlConnectionMaker;

public class Sakila {
    public static void main(String[] args) {
        ConnectionMaker connectionMaker = new MySqlConnectionMaker();
        StaffView staffView = new StaffView(connectionMaker);
        staffView.showIndex();
    }
}
