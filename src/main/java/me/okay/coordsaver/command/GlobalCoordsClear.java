package me.okay.coordsaver.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.okay.coordsaver.CoordSaver;
import me.okay.coordsaver.CustomSubcommand;
import me.okay.coordsaver.utils.ColorFormat;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class GlobalCoordsClear extends CustomSubcommand {
    private final CoordSaver plugin;

    public GlobalCoordsClear(CoordSaver plugin) {
        super(
            "clear",
            "Clears all global coordinates",
            "coordsaver.globalcoords.clear"
        );

        this.plugin = plugin;
    }

    @Override
    public boolean onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (args.length < 1 || !args[0].equals("confirm")) {
            if (sender instanceof Player) {
                TextComponent message = new TextComponent(ColorFormat.colorize("&6Are you sure you want to clear all global coordinates? This cannot be undone. "));

                TextComponent confirmButton = new TextComponent(ColorFormat.colorize("&a&l[Click to confirm]"));
                confirmButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ColorFormat.colorize("&aClick to confirm. &cThis cannot be undone."))));
                confirmButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/coordsaver:globalcoords clear confirm"));
                sender.spigot().sendMessage(message, confirmButton);
            }
            else {
                sender.sendMessage(ColorFormat.colorize("&6Are you sure you want to clear all global coordinates? This cannot be undone. &7(/globalcoords clear confirm)"));
            }
            return true;
        }
        else {
            plugin.getDatabase().clearGlobalCoordinates();
            sender.sendMessage(ColorFormat.colorize("&aAll global coordinates have been cleared."));
            return true;
        }
    }
}
