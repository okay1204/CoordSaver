package me.okay.coordsaver.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.okay.coordsaver.CoordSaver;
import me.okay.coordsaver.CustomSubcommand;
import me.okay.coordsaver.utils.ColorFormat;

public class CoordsRemove extends CustomSubcommand {
    private final CoordSaver plugin;

    public CoordsRemove(CoordSaver plugin) {
        super(
            "remove",
            "Removes a saved coordinate",
            "coordsaver.coords.remove",
            "remove <name>"
        );

        this.plugin = plugin;
    }

    @Override
    public boolean onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorFormat.colorize("&cThis command can only be run by a player."));
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            return false;
        }

        String name = args[0];

        boolean removed = plugin.getDatabase().deletePrivateCoordinates(player.getUniqueId(), name);
        if (removed) {
            player.sendMessage(ColorFormat.colorize("&aCoordinate &6") + name + ColorFormat.colorize(" &aremoved."));
        }
        else {
            player.sendMessage(ColorFormat.colorize("&cCoordinate &6") + name + ColorFormat.colorize(" &cnot found."));
        }

        return true;
    }
}
