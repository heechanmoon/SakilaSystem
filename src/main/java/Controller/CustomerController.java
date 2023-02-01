package Controller;

import Model.AddressDTO;
import Model.CustomerDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerController {
    private Connection connection;

    public CustomerController(Connection connection){
        this.connection = connection;
    }

    public void insert(CustomerDTO CustomerDTO){
        String query = "insert into `customer`(`store_id`, `first_name`, `last_name`, `email`, `address_id`, `create_date`, `last_update`) values"+
                "(1,?,?,?,?,now(),now())";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1,CustomerDTO.getFirst_name());
            pstmt.setString(2,CustomerDTO.getLast_name());
            pstmt.setString(3,CustomerDTO.getEmail());
            pstmt.setInt(4,CustomerDTO.getAddress_id());

            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<CustomerDTO> selectList(String first_name, String last_name){
        ArrayList<CustomerDTO> list = new ArrayList<>();
        String query = "select * from `customer`"+
                "inner join `address` on `customer`.`address_id` = `address`.`address_id`"+
                "where `first_name` = ? and `last_name` = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1,first_name);
            pstmt.setString(2,last_name);

            ResultSet resultSet = pstmt.executeQuery();

            while(resultSet.next()){
                CustomerDTO a = new CustomerDTO();
                a.setCustomer_id(resultSet.getInt("customer_id"));
                a.setStore_id(resultSet.getInt("store_id"));
                a.setFirst_name(resultSet.getString("first_name"));
                a.setLast_name(resultSet.getString("last_name"));
                a.setEmail(resultSet.getString("email"));
                a.setAddress_id(resultSet.getInt("address_id"));

                list.add(a);
            }

            resultSet.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public CustomerDTO selectOne(int id){
        CustomerDTO CustomerDTO = null;
        String query = "select * from `customer` where `customer_id` = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1,id);

            ResultSet resultSet = pstmt.executeQuery();

            if(resultSet.next()){
                CustomerDTO = new CustomerDTO();
                CustomerDTO.setCustomer_id(resultSet.getInt("customer_id"));
                CustomerDTO.setStore_id(resultSet.getInt("store_id"));
                CustomerDTO.setFirst_name(resultSet.getString("first_name"));
                CustomerDTO.setLast_name(resultSet.getString("last_name"));
                CustomerDTO.setEmail(resultSet.getString("email"));
                CustomerDTO.setAddress_id(resultSet.getInt("address_id"));
            }

            resultSet.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return CustomerDTO;
    }

    public void updateAddress(CustomerDTO CustomerDTO) {
        String query = "UPDATE `customer` SET `address_id` = ?, `last_update` = now()" +
                "WHERE `customer_id` = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1,CustomerDTO.getAddress_id());
            pstmt.setInt(2,CustomerDTO.getCustomer_id());

            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(CustomerDTO CustomerDTO){
        String query = "DELETE FROM `customer` WHERE `customer_id` = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, CustomerDTO.getCustomer_id());

            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean duplicateEmail(String email){
        String query = "SELECT * FROM `customer` WHERE `email` = ?";
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

    public ArrayList<AddressDTO> getAddressId(String address){
        ArrayList<AddressDTO> list = new ArrayList<>();
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
}
