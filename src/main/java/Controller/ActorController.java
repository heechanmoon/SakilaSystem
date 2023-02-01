package Controller;

import Model.ActorDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ActorController {
    private Connection connection;

    public ActorController(Connection connection){
        this.connection = connection;
    }

    public void insert(ActorDTO actorDTO){
        String query = "insert into `actor`(`first_name`, `last_name`)"+
                "values(?,?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1,actorDTO.getFirst_name());
            pstmt.setString(2, actorDTO.getLast_name());

            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<ActorDTO> selectList(String first_name, String last_name){
        ArrayList<ActorDTO> list = new ArrayList<>();
        String query = "select * from `actor` where `first_name` = ? and `last_name` = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1,first_name);
            pstmt.setString(2,last_name);

            ResultSet resultSet = pstmt.executeQuery();

            while(resultSet.next()){
                ActorDTO a = new ActorDTO();
                a.setActor_id(resultSet.getInt("actor_id"));
                a.setFirst_name(resultSet.getString("first_name"));
                a.setLast_name(resultSet.getString("last_name"));

                list.add(a);
            }

            resultSet.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public ActorDTO selectOne(int id){
        ActorDTO actorDTO = null;
        String query = "select * from `actor` where `actor_id` = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1,id);

            ResultSet resultSet = pstmt.executeQuery();

            if(resultSet.next()){
                actorDTO = new ActorDTO();
                actorDTO.setActor_id(resultSet.getInt("actor_id"));
                actorDTO.setFirst_name(resultSet.getString("first_name"));
                actorDTO.setLast_name(resultSet.getString("last_name"));
            }

            resultSet.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return actorDTO;
    }

    public void update(ActorDTO actorDTO) {
        String query = "UPDATE `actor` SET `first_name` = ?, `last_name` = ?, `modify_date` = NOW() WHERE `actor_id` = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, actorDTO.getFirst_name());
            pstmt.setString(2, actorDTO.getLast_name());
            pstmt.setInt(3, actorDTO.getActor_id());

            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean delete(int actor_id){
        String query = "DELETE FROM `actor` WHERE `actor_id` = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, actor_id);

            pstmt.executeUpdate();

            pstmt.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public ArrayList<String> actorFilmList(int actor_id){
        ArrayList<String> filmList = new ArrayList<>();

        String query = "select `film`.`title` from `film_actor` join `film` on `film_actor`.`film_id` = `film`.`film_id`\n" +
                "where `actor_id` IN(\n" +
                "select `actor_id` from `actor`\n" +
                "where `actor`.`actor_id` = ?\n" +
                ")";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, actor_id);

            ResultSet resultSet = pstmt.executeQuery();

            while(resultSet.next()) {
                filmList.add(resultSet.getString("title"));
            }

            return filmList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean getIsActor(ActorDTO actor) {
        ArrayList<ActorDTO> list = selectList(actor.getFirst_name(), actor.getLast_name());

        return list.isEmpty();
    }
}
