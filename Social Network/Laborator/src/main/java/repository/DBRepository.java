package repository;

import domain.Utilizator;
import validator.UtilizatorValidator;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.lang.String;

public class DBRepository implements Repository<Long, Utilizator> {

    private String url;
    private String username;
    private String password;

    private UtilizatorValidator validator;


    public DBRepository(String url, String username, String password, UtilizatorValidator validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator=validator;
    }

    @Override
    public Optional<Utilizator> findOne(Long longID) {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from users " + "where id = ?");)
        {
            statement.setInt(1, Math.toIntExact(longID));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Utilizator u = new Utilizator(firstName,lastName);
                u.setId(longID);
                return Optional.ofNullable(u);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public Iterable<Utilizator> findAll() {
        Set<Utilizator> users = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
             ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next())
            {
                Long id= resultSet.getLong("id");
                String firstName=resultSet.getString("first_name");
                String lastName=resultSet.getString("last_name");
                Utilizator user=new Utilizator(firstName,lastName);
                user.setId(id);
                users.add(user);

            }
            return users;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<Utilizator> save(Utilizator entity) {
        try{
            Connection connection=DriverManager.getConnection(url,username,password);
            PreparedStatement statement=connection.prepareStatement("INSERT INTO Users(first_name,last_name) VALUES (?,?)");
            //statement.setLong(1,entity.getId());
            statement.setString(1,entity.getFirstName());
            statement.setString(2,entity.getLastName());
            statement.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Utilizator> delete(Long aLong) {
        try{
            Connection connection=DriverManager.getConnection(url,username,password);
            PreparedStatement statement=connection.prepareStatement("DELETE FROM Users WHERE id = ?;");
            statement.setLong(1,aLong);
            statement.executeUpdate();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Utilizator> update(Utilizator entity) {
        try{
            Connection connection=DriverManager.getConnection(url,username,password);
            PreparedStatement statement=connection.prepareStatement("UPDATE users SET first_name=?,last_name=? WHERE id=?;");
            statement.setString(1,entity.getFirstName());
            statement.setString(2,entity.getLastName());
            statement.setLong(3,entity.getId());
            statement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }
}

