package me.okay.coordsaver.command;

import org.bukkit.command.CommandSender;

import me.okay.coordsaver.CoordSaver;
import me.okay.coordsaver.CustomSubcommand;
import me.okay.coordsaver.utils.ColorFormat;

public class GlobalCoordsRemove extends CustomSubcommand {
    private final CoordSaver plugin;

    public GlobalCoordsRemove(CoordSaver plugin) {
        super(
            "remove",
            "Removes a global coordinate",
            "coordsaver.globalcoords.remove",
            "remove <name>"
        );

        this.plugin = plugin;
    }

    @Override
    public boolean onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }

        String name = args[0];

        boolean removed = plugin.getDatabase().deleteGlobalCoordinates(name);
        if (removed) {
            sender.sendMessage(ColorFormat.colorize("&aGlobal coordinate &6") + name + ColorFormat.colorize(" &aremoved."));
        }
        else {
            sender.sendMessage(ColorFormat.colorize("&cGlobal coordinate &6") + name + ColorFormat.colorize(" &cnot found."));
        }

        return true;
    }
}
