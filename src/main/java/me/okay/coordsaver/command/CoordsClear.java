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

public class CoordsClear extends CustomSubcommand {
    private final CoordSaver plugin;

    public CoordsClear(CoordSaver plugin) {
        super(
            "clear",
            "Clears all your saved coordinates",
            "coordsaver.coords.clear"
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

        if (args.length < 1 || !args[0].equals("confirm")) {
            TextComponent message = new TextComponent(ColorFormat.colorize("&6Are you sure you want to clear all your saved coordinates? This cannot be undone. "));

            TextComponent confirmButton = new TextComponent(ColorFormat.colorize("&a&l[Click to confirm]"));
            confirmButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ColorFormat.colorize("&aClick to confirm. &cThis cannot be undone."))));
            confirmButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/coordsaver:coords clear confirm"));
            player.spigot().sendMessage(message, confirmButton);
            return true;
        }
        else {
            plugin.getDatabase().clearPrivateCoordinates(player.getUniqueId());
            sender.sendMessage(ColorFormat.colorize("&aAll your saved coordinates have been cleared."));
            return true;
        }
    }
}
