package org.example;

import java.sql.*;

import static spark.Spark.*;


public class Main {
    private static final String connectionUrl = "jdbc:mysql://localhost:3306/doll_schema";
    private static final String username = "root";
    private static final String password = "topsecretpassword";
    private static Connection connection;

    public static void main(String[] args) {

        try {
            connection = DriverManager.getConnection(connectionUrl, username, password);
        } catch (SQLException e) {
            System.out.println(e.getStackTrace());
        }

        get("/dolls", (req, res) -> {
            getAllDolls();
            return "Done SELECT";
        });

        get("/dolls/:id", (req, res) -> {
            getDollById(Integer.parseInt(req.params(":id")));
            return "Done SELECT by ID";
        });

        post("/dolls/:name/:price/:stock", (req, res) -> {
            insertDoll(req.params(":name"), Double.parseDouble(req.params(":price")), Integer.parseInt(req.params(":stock")));
            return "Done INSERT";
        });

        delete("/dolls/:id", (req, res) -> {
            deleteDoll(Integer.parseInt(req.params(":id")));
            return "Done DELETE";
        });

        post("/dolls/:id/:name/:price/:stock", (req, res) -> {
            updateDoll(Integer.parseInt(req.params(":id")), req.params(":name"), Double.parseDouble(req.params(":price")), Integer.parseInt(req.params(":stock")));
            return "Done UPDATE";
        });
    }

    public static void getAllDolls() throws SQLException {
        Statement ps = connection.createStatement();
        ResultSet rs = ps.executeQuery("SELECT * FROM `doll`;");
        while (rs.next()) {
            Doll d = new Doll(rs.getString("name"), rs.getDouble("price"), rs.getInt("stock"));
            System.out.println(d);
        }
    }

    private static void getDollById(int id) throws SQLException{
        PreparedStatement ps2 = connection.prepareStatement("SELECT * FROM `doll` WHERE id = ? ;");
        ps2.setInt(1, id);
        ResultSet rs = ps2.executeQuery();
        if (rs.next()) {
            Doll d = new Doll(rs.getString("name"), rs.getDouble("price"), rs.getInt("stock"));
            System.out.println(d);
        }
    }

    public static void insertDoll(String name, double price, int stock) throws SQLException {
        PreparedStatement ps2 = connection.prepareStatement("INSERT INTO `doll` (`name`, `price`, `stock`) VALUES ( ?, ?, ?);");
        ps2.setString(1, name);
        ps2.setDouble(2, price);
        ps2.setInt(3, stock);
        ps2.execute();
    }

    private static void deleteDoll(int id) throws SQLException {
        PreparedStatement ps2 = connection.prepareStatement("DELETE FROM `doll` WHERE id = ? ;");
        ps2.setInt(1, id);
        ps2.execute();
    }

    private static void updateDoll(int id, String name, double price, int stock) throws SQLException {
        PreparedStatement ps2 = connection.prepareStatement("UPDATE `doll` SET `name` = ? , `price` = ? , `stock` = ?   WHERE id = ? ;");
        ps2.setString(1, name);
        ps2.setDouble(2, price);
        ps2.setInt(3, stock);
        ps2.setInt(4, id);
        ps2.execute();

    }







}
