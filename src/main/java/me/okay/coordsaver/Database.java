package me.okay.coordsaver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;


public class Database {
    
    private final String CONNECT_URL;
    private final Logger logger;
    private Connection conn;

    public Database(CoordSaver plugin) {
        CONNECT_URL = "jdbc:sqlite:" + plugin.getDataFolder() + "/storage.db";

        logger = plugin.getLogger();

        try {
            // Create a connection to the database
            conn = DriverManager.getConnection(CONNECT_URL);

            logger.info("Connected to database.");
            
            conn.prepareStatement("""
                CREATE TABLE IF NOT EXISTS "PrivateCoordinates" (
                    "uuid" TEXT NOT NULL,
                    "name"	TEXT NOT NULL,
                    "x" INTEGER NOT NULL,
                    "y" INTEGER NOT NULL,
                    "z" INTEGER NOT NULL,
                    "world" TEXT NOT NULL,
                    PRIMARY KEY("uuid", "name")
                );
            """).execute();

            conn.prepareStatement("""
               CREATE TABLE IF NOT EXISTS "GlobalCoordinates" (
                    "name" TEXT NOT NULL,
                    "x" INTEGER NOT NULL,
                    "y" INTEGER NOT NULL,
                    "z" INTEGER NOT NULL,
                    "world" TEXT NOT NULL,
                    PRIMARY KEY("name")
                );        
            """).execute();

        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }

    }

    public void safeDisconnect() {
        if (conn != null) {
            try {
                conn.close();
                logger.info("Disconnected from database");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isClosed() {
        try {
            return conn.isClosed();
        }
        catch (SQLException e) {
            return true;
        }
    }
    
    public void savePrivateCoordinates(UUID uuid, String name, int x, int y, int z, String world) {
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT OR REPLACE INTO PrivateCoordinates VALUES (?, ?, ?, ?, ?, ?);");
            statement.setString(1, uuid.toString());
            statement.setString(2, name);
            statement.setInt(3, x);
            statement.setInt(4, y);
            statement.setInt(5, z);
            statement.setString(6, world);
            statement.execute();
        }
        catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }

    public boolean deletePrivateCoordinates(UUID uuid, String name) {
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM PrivateCoordinates WHERE uuid = ? AND name = ?;");
            statement.setString(1, uuid.toString());
            statement.setString(2, name);

            int deleted = statement.executeUpdate();

            if (deleted == 0) {
                return false;
            }
            
            return true;
        }
        catch (SQLException e) {
            logger.severe(e.getMessage());
        }

        return false;
    }

    public void clearPrivateCoordinates(UUID uuid) {
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM PrivateCoordinates WHERE uuid = ?;");
            statement.setString(1, uuid.toString());
            statement.execute();
        }
        catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }

    public int getPrivateCoordinatesCount(UUID uuid) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT COUNT(*) FROM PrivateCoordinates WHERE uuid = ?;");
            statement.setString(1, uuid.toString());
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return result.getInt(1);
            }
        }
        catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        return -1;
    }

    public List<Coordinate> paginatePrivateCoordinates(UUID uuid, int page) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM PrivateCoordinates WHERE uuid = ? ORDER BY name LIMIT ?, ?;");
            statement.setString(1, uuid.toString());
            statement.setInt(2, (page - 1) * CoordSaver.COORDS_PER_PAGE);
            statement.setInt(3, CoordSaver.COORDS_PER_PAGE);
            ResultSet result = statement.executeQuery();
            
            List<Coordinate> coordinates = new ArrayList<>();
            while (result.next()) {
                coordinates.add(new Coordinate(result.getString("name"), result.getInt("x"), result.getInt("y"), result.getInt("z"), result.getString("world")));
            }

            return coordinates;
        }
        catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        return null;
    }

    public void saveGlobalCoordinates(String name, int x, int y, int z, String world) {
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT OR REPLACE INTO GlobalCoordinates VALUES (?, ?, ?, ?, ?);");
            statement.setString(1, name);
            statement.setInt(2, x);
            statement.setInt(3, y);
            statement.setInt(4, z);
            statement.setString(5, world);
            statement.execute();
        }
        catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }

    public boolean deleteGlobalCoordinates(String name) {
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM GlobalCoordinates WHERE name = ?;");
            statement.setString(1, name);
            int deleted = statement.executeUpdate();

            if (deleted == 0) {
                return false;
            }
            
            return true;
        }
        catch (SQLException e) {
            logger.severe(e.getMessage());
        }

        return false;
    }

    public void clearGlobalCoordinates() {
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM GlobalCoordinates;");
            statement.execute();
        }
        catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }

    public int getGlobalCoordinatesCount() {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT COUNT(*) FROM GlobalCoordinates;");
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return result.getInt(1);
            }
        }
        catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        return -1;
    }

    public List<Coordinate> paginateGlobalCoordinates(int page) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM GlobalCoordinates ORDER BY name LIMIT ?, ?;");
            statement.setInt(1, (page - 1) * CoordSaver.COORDS_PER_PAGE);
            statement.setInt(2, CoordSaver.COORDS_PER_PAGE);
            ResultSet result = statement.executeQuery();
            
            List<Coordinate> coordinates = new ArrayList<>();
            while (result.next()) {
                coordinates.add(new Coordinate(result.getString("name"), result.getInt("x"), result.getInt("y"), result.getInt("z"), result.getString("world")));
            }

            return coordinates;
        }
        catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        return null;
    }
}