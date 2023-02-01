package Controller;

import Model.InventoryDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InventoryController {
    private Connection connection;

    public InventoryController(Connection connection){
        this.connection = connection;
    }

    public void insert(InventoryDTO inventoryDTO){
        String query = "insert into `inventory` (`film_id`, `store_id`)" +
                " values (?, ?)";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);

            pstmt.setInt(1,inventoryDTO.getFilm_id());
            pstmt.setInt(2,inventoryDTO.getStore_id());

            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<InventoryDTO> selectList(String title, int manager_staff_id){
        ArrayList<InventoryDTO> list = new ArrayList<>();
        String query = "select * from `inventory` "+
                "inner join `film` on `film`.`film_id` = `inventory`.`film_id` "+
                "inner join `store` on `store`.`store_id` = `inventory`.`store_id` "+
                "where `title` = ? and `manager_staff_id` = ? and `isRent` = false";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1,title);
            pstmt.setInt(2,manager_staff_id);

            ResultSet resultSet = pstmt.executeQuery();

            while(resultSet.next()){
                InventoryDTO a = new InventoryDTO();
                a.setInventory_id(resultSet.getInt("inventory_id"));
                a.setFilm_id(resultSet.getInt("film_id"));
                a.setStore_id(resultSet.getInt("store_id"));

                list.add(a);
            }

            resultSet.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public InventoryDTO selectOne(int id){
        InventoryDTO a = null;
        String query = "select * from `inventory` where `inventory_id` = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1,id);

            ResultSet resultSet = pstmt.executeQuery();

            if(resultSet.next()){
                a = new InventoryDTO();
                a.setInventory_id(resultSet.getInt("inventory_id"));
                a.setFilm_id(resultSet.getInt("film_id"));
                a.setStore_id(resultSet.getInt("store_id"));
            }

            resultSet.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return a;
    }

    public void delete(int inventory_id){
        String query = "DELETE FROM `inventory` WHERE `inventory_id` = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, inventory_id);

            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
