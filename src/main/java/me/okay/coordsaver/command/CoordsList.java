package me.okay.coordsaver.command;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.okay.coordsaver.CoordSaver;
import me.okay.coordsaver.Coordinate;
import me.okay.coordsaver.CustomSubcommand;
import me.okay.coordsaver.utils.ColorFormat;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class CoordsList extends CustomSubcommand {
    private final CoordSaver plugin;

    public CoordsList(CoordSaver plugin) {
        super(
            "list",
            "Lists all your saved coordinates",
            "coordsaver.coords.list",
            "list [<page>]"
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

        int page;
        if (args.length < 1) {
            page = 1;
        }
        else {
            try {
                page = Integer.parseInt(args[0]);
            }
            catch (NumberFormatException e) {
                player.sendMessage(ColorFormat.colorize("&cInvalid page number."));
                return true;
            }

            if (page < 1) {
                page = 1;
            }
        }

        int maxPages = (int) Math.ceil(plugin.getDatabase().getPrivateCoordinatesCount(player.getUniqueId()) / (double) CoordSaver.COORDS_PER_PAGE);
        if (maxPages == 0) {
            player.sendMessage(ColorFormat.colorize("&cYou do not have any coordinates saved."));
            return true;
        }

        if (page > maxPages) {
            page = maxPages;
        }

        List<Coordinate> coordinates = plugin.getDatabase().paginatePrivateCoordinates(player.getUniqueId(), page);

        TextComponent backArrow;
        if (page == 1) {
            backArrow = new TextComponent(ColorFormat.colorize("&7&l<< "));
        }
        else {
            backArrow = new TextComponent(ColorFormat.colorize("&6&l<< "));
            backArrow.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ColorFormat.colorize("&6Go to page " + (page - 1)))));
            backArrow.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/coordsaver:coords list " + (page - 1)));
        }

        TextComponent forwardArrow;
        if (page == maxPages) {
            forwardArrow = new TextComponent(ColorFormat.colorize("&7&l >>"));
        }
        else {
            forwardArrow = new TextComponent(ColorFormat.colorize("&6&l >>"));
            forwardArrow.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ColorFormat.colorize("&6Go to page " + (page + 1)))));
            forwardArrow.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/coordsaver:coords list " + (page + 1)));
        }

        TextComponent centerText = new TextComponent(ColorFormat.colorize("&6" + player.getName() + "'s Coordinate List: &e(Page " + page + " of " + maxPages + ")"));

        player.sendMessage(CoordSaver.BORDER_LINE);
        sender.spigot().sendMessage(backArrow, centerText, forwardArrow);
        for (Coordinate coordinate : coordinates) {
            player.sendMessage(ColorFormat.colorize("&e- " + coordinate.toString()));
        }
        player.sendMessage(CoordSaver.BORDER_LINE);

        return true;
    }
}
