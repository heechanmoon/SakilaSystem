package Controller;

import Model.StaffDTO;
import Model.AddressDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StaffController {
    private Connection connection;

    public StaffController(Connection connection){
        this.connection = connection;
    }

    public boolean getCountry(String country){
        String query = "select * from `country` where `country` = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1,country);
            ResultSet resultSet = pstmt.executeQuery();

            if(resultSet.next()){
                return true;
            }

            pstmt.close();
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public boolean getCity(String city){
        String query = "select * from `city` where `city` = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1,city);
            ResultSet resultSet = pstmt.executeQuery();

            if(resultSet.next()){
                return true;
            }

            pstmt.close();
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public ArrayList<AddressDTO> getCityList(String country){
        ArrayList<AddressDTO> cityList = new ArrayList<>();
        String query = "select * from `city` " +
                "inner join `country` on `city`.`country_id`= `country`.`country_id` " +
                "where `country`.`country` = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1,country);
            ResultSet resultSet = pstmt.executeQuery();

            while(resultSet.next()){
                AddressDTO a = new AddressDTO();
                a.setCity_id(resultSet.getInt("city_id"));
                a.setCity(resultSet.getString("city"));
                cityList.add(a);
            }

            pstmt.close();
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return cityList;
    }

    public ArrayList<AddressDTO> getAddressList(String city){
        ArrayList<AddressDTO> addressList = new ArrayList<>();
        String query = "select * from `address` " +
                "inner join `city` on `address`.`city_id` = `city`.`city_id` " +
                "where `city`.`city` = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1,city);
            ResultSet resultSet = pstmt.executeQuery();

            while(resultSet.next()){
                AddressDTO a = new AddressDTO();
                a.setAddress_id(resultSet.getInt("address_id"));
                a.setAddress(resultSet.getString("address"));
                addressList.add(a);
            }

            pstmt.close();
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return addressList;
    }

    public String getAddress(int address_id){
        String query = "select * from `address` where `address_id` = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1,address_id);
            ResultSet resultSet = pstmt.executeQuery();

            if(resultSet.next()){
                return resultSet.getString("address");
            }

            pstmt.close();
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public ArrayList<AddressDTO> getAddressId(String address){
        ArrayList<AddressDTO> list = new ArrayList<>();
        int address_id = 0;
        String addressQuery = "select * from `address` where `address` = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(addressQuery);
            pstmt.setString(1,address);
            ResultSet resultSet = pstmt.executeQuery();

            while(resultSet.next()){
                AddressDTO addressDTO = new AddressDTO();
                addressDTO.setAddress_id(resultSet.getInt("address_id"));
                addressDTO.setAddress(resultSet.getString("address"));
                list.add(addressDTO);
            }

            pstmt.close();
            resultSet.close();

            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean insert(StaffDTO staffDTO, int address_id){
        String query = "insert into `staff` (`first_name`, `last_name`, `address_id`, `email`, `store_id`, `username`, `password`)" +
                " values (?, ?, ?, ?, 1, ?, ?)";

        try {

            PreparedStatement pstmt = connection.prepareStatement(query);

            pstmt.setString(1,staffDTO.getFirst_name());
            pstmt.setString(2,staffDTO.getLast_name());
            pstmt.setInt(3,address_id);
            pstmt.setString(4,staffDTO.getEmail());
            pstmt.setString(5,staffDTO.getUsername());
            pstmt.setString(6,staffDTO.getPassword());

            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            return false;
        }

        return true;
    }


    public ArrayList<StaffDTO> selectList(){
        ArrayList<StaffDTO> list = new ArrayList<>();
        String query = "select * from `staff`";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);

            ResultSet resultSet = pstmt.executeQuery();

            while(resultSet.next()){
                StaffDTO a = new StaffDTO();

                a.setStore_id(resultSet.getInt("store_id"));
                a.setFirst_name(resultSet.getString("first_name"));
                a.setLast_name(resultSet.getString("last_name"));
                a.setEmail(resultSet.getString("email"));
                a.setAddress_id(resultSet.getInt("address_id"));
                a.setActive(resultSet.getInt("active"));
                a.setUsername(resultSet.getString("username"));
                a.setPassword(resultSet.getString("password"));

                list.add(a);
            }

            resultSet.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public StaffDTO selectOne(int id){
        StaffDTO a = null;
        String query = "select * from `staff` where `staff_id` = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1,id);

            ResultSet resultSet = pstmt.executeQuery();

            if(resultSet.next()){
                a = new StaffDTO();
                a.setStore_id(resultSet.getInt("store_id"));
                a.setFirst_name(resultSet.getString("first_name"));
                a.setLast_name(resultSet.getString("last_name"));
                a.setEmail(resultSet.getString("email"));
                a.setAddress_id(resultSet.getInt("address_id"));
                a.setActive(resultSet.getInt("active"));
                a.setUsername(resultSet.getString("username"));
                a.setPassword(resultSet.getString("password"));
            }

            resultSet.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return a;
    }

    public void updateAddress(int staff_id, int address_id) {
        String query = "UPDATE `staff` SET `address_id` = ? " +
                "WHERE `staff_id` = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);

            pstmt.setInt(1,address_id);
            pstmt.setInt(2,staff_id);

            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePassword(int staff_id, String password){
        String query = "UPDATE `staff` SET `password` = ?" +
                "WHERE `staff_id` = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);

            pstmt.setString(1,password);
            pstmt.setInt(2,staff_id);

            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int staff_id){
        String query = "update `staff` set `active` = 3 WHERE `staff_id` = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, staff_id);

            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public StaffDTO auth(String username, String password) {
        String query = "SELECT * FROM `staff` WHERE `username` = ? AND `password` = ? AND `active` <= 2";

        try {
            StaffDTO staffDTO = new StaffDTO();

            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                staffDTO.setStaff_id(resultSet.getInt("staff_id"));
                staffDTO.setStore_id(resultSet.getInt("store_id"));
                staffDTO.setFirst_name(resultSet.getString("first_name"));
                staffDTO.setLast_name(resultSet.getString("last_name"));
                staffDTO.setEmail(resultSet.getString("email"));
                staffDTO.setAddress_id(resultSet.getInt("address_id"));
                staffDTO.setActive(resultSet.getInt("active"));
                staffDTO.setUsername(resultSet.getString("username"));
                staffDTO.setPassword(resultSet.getString("password"));

                staffDTO = activeOn(staffDTO);

                return staffDTO;
            }

            resultSet.close();
            pstmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public StaffDTO activeOn(StaffDTO staffDTO){
        if(staffDTO.getActive() == 0) {
            String queryActive = "update `staff` set `active` = 1 where `username` = ?";

            System.out.println("휴면상태가 해제되었습니다.");
            PreparedStatement pstmt = null;
            try {
                pstmt = connection.prepareStatement(queryActive);
                pstmt.setString(1, staffDTO.getUsername());
                pstmt.executeUpdate();
                staffDTO.setActive(1);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return staffDTO;
    }

    public boolean duplicateEmail(String email){
        String query = "SELECT * FROM `staff` WHERE `email` = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1,email);

            ResultSet resultSet = pstmt.executeQuery();

            if(resultSet.next()){
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }
}
