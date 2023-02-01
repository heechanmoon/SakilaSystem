package Controller;

import Model.ActorDTO;
import Model.FilmDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FilmController {
    private Connection connection;

    public FilmController(Connection connection){
        this.connection = connection;
    }

    public void insert(FilmDTO filmDTO, ArrayList<ActorDTO> actors){
        String query = "insert into `film` (`title`,`description`,`release_year`, `language_id`, `rental_duration`, `rental_rate`, `length`, `replacement_cost`, `rating`, `special_features`)" +
                " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String secondQuery = "insert into `film_actor` (`actor_id`, `film_id`)" +
                " values (?,?)";

        String thirdQuery = "insert into `film_category` (`film_id`, `category_id`)" +
                " values (?,?)";

        String fourthQuery = "select * from `film`" +
                " where `title` = ? and `description` = ?" +
                " order by `film_id` desc limit 1";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1,filmDTO.getTitle());
            pstmt.setString(2,filmDTO.getDescription());
            pstmt.setInt(3,filmDTO.getRelease_year());
            pstmt.setInt(4,filmDTO.getLanguage_id());
            pstmt.setInt(5,filmDTO.getRental_duration());
            pstmt.setBigDecimal(6,filmDTO.getRental_rate());
            pstmt.setInt(7,filmDTO.getLength());
            pstmt.setBigDecimal(8,filmDTO.getReplacement_cost());
            pstmt.setString(9, filmDTO.getRating());
            pstmt.setString(10, filmDTO.getSpecial_features());

            pstmt.executeUpdate();

            pstmt = connection.prepareStatement(fourthQuery);
            pstmt.setString(1,filmDTO.getTitle());
            pstmt.setString(2,filmDTO.getDescription());

            ResultSet resultSet = pstmt.executeQuery();

            if(resultSet.next()) {
                for (ActorDTO a : actors) {
                    pstmt = connection.prepareStatement(secondQuery);
                    pstmt.setInt(1, a.getActor_id());
                    pstmt.setInt(2, resultSet.getInt("film_id"));
                    pstmt.executeUpdate();
                }
            }

            pstmt = connection.prepareStatement(fourthQuery);
            pstmt.setString(1,filmDTO.getTitle());
            pstmt.setString(2,filmDTO.getDescription());

            resultSet = pstmt.executeQuery();

            pstmt = connection.prepareStatement(thirdQuery);
            if(resultSet.next()) {
                pstmt.setInt(1, resultSet.getInt("film_id"));
            }
            pstmt.setInt(2, filmDTO.getCategory_id());

            pstmt.executeUpdate();

            resultSet.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<FilmDTO> selectList(String title){
        ArrayList<FilmDTO> list = new ArrayList<>();
        String query = "select * from `film`"+
                "inner join `film_category` on `film`.`film_id` = `film_category`.`film_id`"+
                "inner join `film_actor` on `film`.`film_id` = `film_actor`.`film_id`"+
                "where `title` = ?"+
                "group by `film`.`film_id`";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1,title);

            ResultSet resultSet = pstmt.executeQuery();

            while(resultSet.next()){
                FilmDTO a = new FilmDTO();
                a.setFilm_id(resultSet.getInt("film_id"));
                a.setActor_id(resultSet.getInt("actor_id"));
                a.setCategory_id(resultSet.getInt("category_id"));
                a.setTitle(resultSet.getString("title"));
                a.setDescription(resultSet.getString("description"));
                a.setRelease_year(resultSet.getInt("release_year"));
                a.setLanguage_id(resultSet.getInt("language_id"));
                a.setRental_duration(resultSet.getInt("rental_duration"));
                a.setRental_rate(resultSet.getBigDecimal("rental_rate"));
                a.setLength(resultSet.getInt("length"));
                a.setReplacement_cost(resultSet.getBigDecimal("replacement_cost"));
                a.setRating(resultSet.getString("rating"));
                a.setSpecial_features(resultSet.getString("special_features"));

                list.add(a);
            }

            resultSet.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public FilmDTO selectOne(int id){
        FilmDTO a = null;
        String query = "select * from `film` "+
                "inner join `film_category` on `film`.`film_id` = `film_category`.`film_id` "+
                "inner join `film_actor` on `film`.`film_id` = `film_actor`.`film_id` "+
                "where `film`.`film_id` = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1,id);

            ResultSet resultSet = pstmt.executeQuery();

            if(resultSet.next()){
                a = new FilmDTO();
                a.setFilm_id(resultSet.getInt("film_id"));
                a.setActor_id(resultSet.getInt("actor_id"));
                a.setCategory_id(resultSet.getInt("category_id"));
                a.setTitle(resultSet.getString("title"));
                a.setDescription(resultSet.getString("description"));
                a.setRelease_year(resultSet.getInt("release_year"));
                a.setLanguage_id(resultSet.getInt("language_id"));
                a.setRental_duration(resultSet.getInt("rental_duration"));
                a.setRental_rate(resultSet.getBigDecimal("rental_rate"));
                a.setLength(resultSet.getInt("length"));
                a.setReplacement_cost(resultSet.getBigDecimal("replacement_cost"));
                a.setRating(resultSet.getString("rating"));
                a.setSpecial_features(resultSet.getString("special_features"));
            }

            resultSet.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return a;
    }

    public void update(FilmDTO filmDTO) {
        String query = "UPDATE `film` SET `rental_duration` = ?, `rental_rate` = ?, `replacement_cost` = ? " +
                "WHERE `film_id` = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1,filmDTO.getRental_duration());
            pstmt.setBigDecimal(2,filmDTO.getRental_rate());
            pstmt.setBigDecimal(3,filmDTO.getReplacement_cost());
            pstmt.setInt(4,filmDTO.getFilm_id());

            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int film_id){
        String query = "DELETE FROM `film` WHERE `film_id` = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, film_id);

            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getLanguageString(int language_id){
        String query = "select * from `language` where `language_id` = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1,language_id);

            ResultSet resultSet = pstmt.executeQuery();

            if(resultSet.next()){
                return resultSet.getString("name");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public ArrayList<ActorDTO> actorList(int film_id){
        ArrayList<ActorDTO> actors = new ArrayList<>();
        String query = "select * from `film_actor` " +
                "inner join `actor` on `actor`.`actor_id` = `film_actor`.`actor_id` " +
                "where `film_id` = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1,film_id);

            ResultSet resultSet = pstmt.executeQuery();

            while(resultSet.next()){
                ActorDTO a = new ActorDTO();
                a.setActor_id(resultSet.getInt("actor_id"));
                a.setFirst_name(resultSet.getString("first_name"));
                a.setLast_name(resultSet.getString("last_name"));
                actors.add(a);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return actors;
    }

    public String getCategory(int category_id){
        String query = "select * from `category` where `category_id` = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, category_id);
            ResultSet resultSet = pstmt.executeQuery();

            if(resultSet.next()){
                return resultSet.getString("name");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
