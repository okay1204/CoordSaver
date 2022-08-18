package me.okay.coordsaver.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import me.okay.coordsaver.CoordSaver;
import me.okay.coordsaver.CustomCommand;
import me.okay.coordsaver.CustomSubcommand;
import me.okay.coordsaver.command.subcommand.GlobalCoordsClear;
import me.okay.coordsaver.command.subcommand.GlobalCoordsRemove;
import me.okay.coordsaver.command.subcommand.GlobalCoordsSet;
import me.okay.coordsaver.command.subcommand.GloballCoordsList;

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
            return Bukkit.dispatchCommand(sender, "globalcoords list");
        }

        return false;
    }
}
