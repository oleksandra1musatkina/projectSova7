package sk.itsovy.projectSova7;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Database implements CarMethods {

    private final String username = "oleksandra";
    private final String password = "sasa";
    private final String url = "jdbc:mysql://localhost:3308/sova7?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    private Connection getConnection() {
        Connection connection;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded!");
            connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addCar(Car car) {
        Connection connection = getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO cars (brand,color,fuel,spz,price) values(?,?,?,?,?)");
            preparedStatement.setString(1, car.getBrand());
            preparedStatement.setString(2, car.getColor());
            preparedStatement.setString(3, String.valueOf(car.getFuel()));
            preparedStatement.setString(4, car.getSpz());
            preparedStatement.setInt(5, car.getPrice());
            int result = preparedStatement.executeUpdate();
            closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Car> getCarsByPrice(int maxPrice) {
        Connection connection = getConnection();
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM cars where price < ?";
        ResultSet resultSet;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, maxPrice);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String brand = resultSet.getString("brand");
                String color = resultSet.getString("color");
                char fuel = resultSet.getString("fuel").charAt(0);
                String spz = resultSet.getString("spz");
                int price = resultSet.getInt("price");
                Car car = new Car(brand, color, fuel, spz, price);
                cars.add(car);
            }
            closeConnection(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    @Override
    public List<Car> getCarsByBrand(String brand) {
        Connection connection = getConnection();
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM cars where brand like ?";
        ResultSet resultSet;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, brand);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String color = resultSet.getString("color");
                char fuel = resultSet.getString("fuel").charAt(0);
                String spz = resultSet.getString("spz");
                int price = resultSet.getInt("price");
                Car car = new Car(brand, color, fuel, spz, price);
                cars.add(car);
            }
            closeConnection(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    @Override
    public List<Car> getCarsByFuel(char fuel) {
        Connection connection = getConnection();
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM cars where fuel like ?";
        ResultSet resultSet;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(fuel));
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String brand = resultSet.getString("brand");
                String color = resultSet.getString("color");
                String spz = resultSet.getString("spz");
                int price = resultSet.getInt("price");
                Car car = new Car(brand, color, fuel, spz, price);
                cars.add(car);
            }
            closeConnection(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    @Override
    public List<Car> getCarsByRegion(String spz) {
        Connection connection = getConnection();
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM cars where spz like '" + spz + "%' ";
        ResultSet resultSet;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String brand = resultSet.getString("brand");
                String color = resultSet.getString("color");
                char fuel = resultSet.getString("fuel").charAt(0);
                String spzFromDatabase = resultSet.getString("spz");
                int price = resultSet.getInt("price");
                Car car = new Car(brand, color, fuel, spzFromDatabase, price);
                cars.add(car);
            }
            closeConnection(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    @Override
    public void changeSPZ(String oldSPZ, String newSPZ) {
        Connection connection = getConnection();
        String query = "UPDATE cars SET spz=? where spz like ? ";
        int result;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newSPZ);
            preparedStatement.setString(2, oldSPZ);
            result = preparedStatement.executeUpdate();
            closeConnection(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void generateXML() {


        Connection connection = getConnection();
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM cars";
        ResultSet resultSet;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String brand = resultSet.getString("brand");
                String color = resultSet.getString("color");
                char fuel = resultSet.getString("fuel").charAt(0);
                String spz = resultSet.getString("spz");
                int price = resultSet.getInt("price");
                Car car = new Car(brand, color, fuel, spz, price);
                cars.add(car);
            }
            closeConnection(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("subor.xml"));
            writer.write("");
            writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.append("<cars>\n");
            for (Car car : cars) {
                writer.append("\t<car>\n");
                writer.append("\t\t<brand>");
                writer.append(car.getBrand());
                writer.append("</brand>\n");
                writer.append("\t\t<color>");
                writer.append(car.getColor());
                writer.append("</color>\n");
                writer.append("\t\t<fuel>");
                writer.append(car.getFuel());
                writer.append("</fuel>\n");
                writer.append("\t\t<spz>");
                writer.append(car.getSpz());
                writer.append("</spz>\n");
                writer.append("\t\t<price>");
                writer.append(String.valueOf(car.getPrice()));
                writer.append("</price>\n");
                writer.append("\t</car>\n");

            }
            writer.append("</cars>\n");

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
