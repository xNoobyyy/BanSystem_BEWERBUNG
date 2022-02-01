package mindcreation.bansystem.mysql;

import lombok.SneakyThrows;
import mindcreation.bansystem.Main;
import mindcreation.bansystem.ban.BanReason;

import java.sql.*;

public class MySQLManager {

    private String HOST, PORT, DATABASE, USER, PASSWORD;
    private Connection con;

    public MySQLManager(String host, String port, String database, String user, String password) {

        HOST = host;
        PORT = port;
        DATABASE = database;
        USER = user;
        PASSWORD = password;

        connect();

        try {
            getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS BanSystem(reason INT, uuid VARCHAR(255) NOT NULL, date DATE)");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void connect() {

        try {
            con = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?autoReconnect=true", USER, PASSWORD);
            System.out.println("[BanSystem] MySQL Verbindung wurde aufegebaut!");
        } catch (SQLException e) {
            System.out.println("[BanSystem] MySQL Verbindung konnte nicht aufgebaut werden!");
            e.printStackTrace();
        }

    }

    public void disconnect() {

        try {
            if(con != null) {
                con.close();
                System.out.println("[BanSystem] MySQL Disconnected!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void update(String query) {
        PreparedStatement state;
        try {
            state = con.prepareStatement(query);
            state.executeUpdate();
            state.close();
        } catch (SQLException e) {
            connect();
            e.printStackTrace();
        }
    }

    public ResultSet query(String query) {
        ResultSet res = null;
        try {
            PreparedStatement state = con.prepareStatement(query);
            res = state.executeQuery();
            state.close();
        } catch (SQLException e) {
            connect();
            e.printStackTrace();
        }

        return res;
    }

    public Connection getConnection() {
        return con;
    }

    @SneakyThrows
    public void insertBan(String uuid, BanReason reason) {
        Date currentDate = new Date(new java.util.Date().getTime());
        update("DELETE FROM BanSystem WHERE uuid='" + uuid + "'");
        PreparedStatement statement = getConnection().prepareStatement("INSERT INTO BanSystem(reason, uuid, date) VALUES (?, ?, ?)");
        statement.setInt(1, reason.getId());
        statement.setString(2, uuid);
        statement.setDate(3, currentDate);
        statement.executeUpdate();
    }

    public void removeBan(String uuid) {
        update("DELETE FROM BanSystem WHERE uuid='" + uuid + "'");
    }

    @SneakyThrows
    public boolean isBanned(String uuid) {
        ResultSet res = Main.getMySQLManager().query("SELECT * FROM BanSystem WHERE uuid='" + uuid + "'");
        return res.first();
    }

}
