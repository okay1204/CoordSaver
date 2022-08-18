package me.okay.coordsaver.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import me.okay.coordsaver.CoordSaver;
import me.okay.coordsaver.CustomCommand;
import me.okay.coordsaver.CustomSubcommand;

public class GlobalCoords extends CustomCommand {
    public GlobalCoords(CoordSaver plugin) {
        super(plugin, "globalcoords");

        addSubcommand(new GlobalCoordsSet(plugin));
        addSubcommand(new GloballCoordsList(plugin));
        addSubcommand(new GlobalCoordsRemove(plugin));
        addSubcommand(new GlobalCoordsClear(plugin));
    }

    @Override
    public boolean onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (args.length == 0) {
            return Bukkit.dispatchCommand(sender, "coordsaver:globalcoords list");
        }

        return false;
    }
}
