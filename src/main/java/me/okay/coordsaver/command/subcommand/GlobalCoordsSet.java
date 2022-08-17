package me.okay.coordsaver.command.subcommand;

import org.bukkit.command.CommandSender;

import me.okay.coordsaver.CustomSubcommand;

public class GlobalCoordsSet extends CustomSubcommand {
    public GlobalCoordsSet() {
        super(
            "set",
            "Set a global coordinate",
            "coordsaver.globalcoords.set",
            "set [<x> <y> <z>] [<world>]"
        );
    }

    @Override
    public boolean onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        sender.sendMessage("GlobalCoordsSet");
        return true;
    }
}
