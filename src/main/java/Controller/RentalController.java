package Controller;

import Model.RentalDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RentalController {
    private Connection connection;

    public RentalController(Connection connection){
        this.connection = connection;
    }

    public void insert(RentalDTO rentalDTO){
        String query = "insert into `rental` (`rental_date`, `inventory_id`, `customer_id`, `staff_id`)" +
                " values (now(), ?, ?, ?)";

        String secondQuery = "update `inventory` set `isRent` = true where `inventory_id` = ? ";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);

            pstmt.setInt(1,rentalDTO.getInventory_id());
            pstmt.setInt(2,rentalDTO.getCustomer_id());
            pstmt.setInt(3,rentalDTO.getStaff_id());

            pstmt.executeUpdate();

            pstmt = connection.prepareStatement(secondQuery);
            pstmt.setInt(1,rentalDTO.getInventory_id());

            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<RentalDTO> selectList(int customer_id, int staff_id){
        ArrayList<RentalDTO> list = new ArrayList<>();
        String query = "select * from `rental` "+
                "where `customer_id` = ? and `staff_id` = ? and `return_date` is null "+
                "order by `rental_id`";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1,customer_id);
            pstmt.setInt(2,staff_id);

            ResultSet resultSet = pstmt.executeQuery();

            while(resultSet.next()){
                RentalDTO a = new RentalDTO();
                a.setRental_id(resultSet.getInt("rental_id"));
                a.setRental_date(resultSet.getDate("rental_date"));
                a.setInventory_id(resultSet.getInt("inventory_id"));
                a.setCustomer_id(resultSet.getInt("customer_id"));
                a.setStaff_id(resultSet.getInt("staff_id"));

                list.add(a);
            }

            resultSet.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public RentalDTO selectOne(int id){
        RentalDTO a = null;
        String query = "select * from `rental` where `rental_id` = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1,id);

            ResultSet resultSet = pstmt.executeQuery();

            if(resultSet.next()){
                a = new RentalDTO();
                a.setRental_id(resultSet.getInt("rental_id"));
                a.setRental_date(resultSet.getDate("rental_date"));
                a.setInventory_id(resultSet.getInt("inventory_id"));
                a.setCustomer_id(resultSet.getInt("customer_id"));
                a.setStaff_id(resultSet.getInt("staff_id"));
            }

            resultSet.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return a;
    }

    public void returnFilm(int rental_id) {
        String query = "UPDATE `rental` SET `return_date` = now() " +
                "WHERE `rental_id` = ?";

        String secondQuery = "UPDATE `inventory` set `isRent` = false " +
                "where `inventory_id` = ?";

        String thirdQuery = "select * from `rental` where `rental_id` = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1,rental_id);
            pstmt.executeUpdate();

            pstmt = connection.prepareStatement(thirdQuery);
            pstmt.setInt(1,rental_id);

            ResultSet resultSet = pstmt.executeQuery();

            if(resultSet.next()){
                pstmt = connection.prepareStatement(secondQuery);
                pstmt.setInt(1, resultSet.getInt("inventory_id"));

                pstmt.executeUpdate();
            }

            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
