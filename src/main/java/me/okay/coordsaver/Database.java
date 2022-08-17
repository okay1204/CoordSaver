package me.okay.coordsaver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
            PreparedStatement statement = conn.prepareStatement("INSERT INTO OR REPLACE INTO PrivateCoordinates VALUES (?, ?, ?, ?, ?, ?);");
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

    public void deletePrivateCoordinates(UUID uuid, String name) {
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM PrivateCoordinates WHERE uuid = ? AND name = ?;");
            statement.setString(1, uuid.toString());
            statement.setString(2, name);
            statement.execute();
        }
        catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }

    public void saveGlobalCoordinates(String name, int x, int y, int z, String world) {
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO OR REPLACE INTO GlobalCoordinates VALUES (?, ?, ?, ?, ?);");
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

    public void deleteGlobalCoordinates(String name) {
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM GlobalCoordinates WHERE name = ?;");
            statement.setString(1, name);
            statement.execute();
        }
        catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }
}