package me.okay.coordsaver.command.subcommand;

import org.bukkit.command.CommandSender;

import me.okay.coordsaver.CustomSubcommand;

public class GloballCoordsList extends CustomSubcommand {
    public GloballCoordsList() {
        super(
            "list",
            "Lists all global coordinate",
            "coordsaver.globalcoords.list"
        );
    }

    @Override
    public boolean onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        sender.sendMessage("GlobalCoordsList");
        return true;
    }
}
