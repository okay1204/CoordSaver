package me.okay.coordsaver;

import org.bukkit.plugin.java.JavaPlugin;

import me.okay.coordsaver.command.GlobalCoords;

public class CoordSaver extends JavaPlugin {
    private Database database;

    @Override
    public void onEnable() {
        getDataFolder().mkdir();
        database = new Database(this);

        // Commands
        new GlobalCoords(this);
    }

    @Override
    public void onDisable() {
        database.safeDisconnect();
    }

    public Database getDatabase() {
        return database;
    }
}