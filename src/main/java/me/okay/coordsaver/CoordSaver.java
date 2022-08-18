package me.okay.coordsaver;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import me.okay.coordsaver.command.Coords;
import me.okay.coordsaver.command.GlobalCoords;

public class CoordSaver extends JavaPlugin {
    public static final String BORDER_LINE = ChatColor.DARK_BLUE + "" + ChatColor.STRIKETHROUGH + "----------------------------------------------------";
    public static final int COORDS_PER_PAGE = 10;

    private Database database;

    @Override
    public void onEnable() {
        getDataFolder().mkdir();
        database = new Database(this);

        // Commands
        new GlobalCoords(this);
        new Coords(this);
    }

    @Override
    public void onDisable() {
        database.safeDisconnect();
    }

    public Database getDatabase() {
        return database;
    }
}