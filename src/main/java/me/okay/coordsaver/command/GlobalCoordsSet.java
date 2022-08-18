package me.okay.coordsaver.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.okay.coordsaver.CoordSaver;
import me.okay.coordsaver.CustomSubcommand;
import me.okay.coordsaver.utils.ColorFormat;

public class GlobalCoordsSet extends CustomSubcommand {
    private final CoordSaver plugin;

    public GlobalCoordsSet(CoordSaver plugin) {
        super(
            "set",
            "Set a global coordinate",
            "coordsaver.globalcoords.set",
            "set <name> [<x> <y> <z>] [<world>]"
        );

        this.plugin = plugin;
    }

    @Override
    public boolean onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        int x, y, z;
        String world;

        if (!(sender instanceof Player) && args.length < 5) {
            sender.sendMessage(ColorFormat.colorize("&cx, y, z and world are required arguments if run from console."));
            return false;
        }

        if (args.length < 1) {
            return false;
        }

        String name = args[0];
        if (name.strip().isEmpty()) {
            sender.sendMessage(ColorFormat.colorize("&cName cannot be empty."));
            return true;
        }

        if (args.length > 1) {
            if (args.length < 4) {
                return false;
            }
            
            try {
                x = Integer.parseInt(args[1]);
                y = Integer.parseInt(args[2]);
                z = Integer.parseInt(args[3]);
            }
            catch (NumberFormatException e) {
                sender.sendMessage(ColorFormat.colorize("&cx, y, and z must be integers."));
                return true;
            }

            if (args.length > 4) {
                world = args[4];
            }
            else {
                world = ((Player) sender).getWorld().getName();
            }
        }
        else {
            Location playerLocation = ((Player) sender).getLocation();
            x = playerLocation.getBlockX();
            y = playerLocation.getBlockY();
            z = playerLocation.getBlockZ();
            world = playerLocation.getWorld().getName();
        }
        
        plugin.getDatabase().saveGlobalCoordinates(name, x, y, z, world);
        sender.sendMessage(ColorFormat.colorize("&aGlobal coordinate &6" + name + "&a set to &6" + x + " " + y + " " + z + " " + world));

        return true;
    }

    private List<String> suggestCoordinate(int actualCoord, String typedArg) {
        if (String.valueOf(actualCoord).startsWith(typedArg)) {
            return List.of(String.valueOf(actualCoord));
        }
        else {
            return List.of();
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return List.of();
        }

        Player player = (Player) sender;
        Location playerLocation = player.getLocation();

        if (args.length == 2) {
            return suggestCoordinate(playerLocation.getBlockX(), args[1]);
        }
        else if (args.length == 3) {
            return suggestCoordinate(playerLocation.getBlockY(), args[2]);
        }
        else if (args.length == 4) {
            return suggestCoordinate(playerLocation.getBlockZ(), args[3]);
        }
        else if (args.length == 5) {
            List<World> worlds = Bukkit.getWorlds();
            List<String> worldNames = new ArrayList<>();

            for (World world : worlds) {
                if (world.getName().startsWith(args[4])) {
                    worldNames.add(world.getName());
                }
            }

            return worldNames;
        }

        return List.of();
    }
}
