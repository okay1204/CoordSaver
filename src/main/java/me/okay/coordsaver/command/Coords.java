package me.okay.coordsaver.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import me.okay.coordsaver.CoordSaver;
import me.okay.coordsaver.CustomCommand;
import me.okay.coordsaver.CustomSubcommand;

public class Coords extends CustomCommand {
    public Coords(CoordSaver plugin) {
        super(plugin, "coords");
        
        addSubcommand(new CoordsSet(plugin));
        addSubcommand(new CoordsList(plugin));
        addSubcommand(new CoordsClear(plugin));
        addSubcommand(new CoordsRemove(plugin));
    }

    @Override
    public boolean onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (args.length == 0) {
            return Bukkit.dispatchCommand(sender, "coordsaver:coords list");
        }

        return false;
    }
}
