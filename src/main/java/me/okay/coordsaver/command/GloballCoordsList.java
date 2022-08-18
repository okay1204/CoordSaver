package me.okay.coordsaver.command;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import me.okay.coordsaver.CoordSaver;
import me.okay.coordsaver.Coordinate;
import me.okay.coordsaver.CustomSubcommand;
import me.okay.coordsaver.utils.ColorFormat;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class GloballCoordsList extends CustomSubcommand {
    private static final String BORDER_LINE = ChatColor.DARK_BLUE + "" + ChatColor.STRIKETHROUGH + "----------------------------------------------------";
    private static final int COORDS_PER_PAGE = 10;
    private final CoordSaver plugin;

    public GloballCoordsList(CoordSaver plugin) {
        super(
            "list",
            "Lists all global coordinates",
            "coordsaver.globalcoords.list",
            "list [<page>]"
        );

        this.plugin = plugin;
    }

    @Override
    public boolean onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        int page;
        if (args.length < 1) {
            page = 1;
        }
        else {
            try {
                page = Integer.parseInt(args[0]);
            }
            catch (NumberFormatException e) {
                sender.sendMessage(ColorFormat.colorize("&cInvalid page number."));
                return true;
            }

            if (page < 1) {
                page = 1;
            }
        }

        int maxPages = (int) Math.ceil(plugin.getDatabase().getGlobalCoordinatesCount() / (double) COORDS_PER_PAGE);
        if (maxPages == 0) {
            sender.sendMessage(ColorFormat.colorize("&cThere are no global coordinates set."));
            return true;
        }

        if (page > maxPages) {
            page = maxPages;
        }

        List<Coordinate> coordinates = plugin.getDatabase().paginateGlobalCoordinates(page);

        TextComponent backArrow;
        if (page == 1) {
            backArrow = new TextComponent(ColorFormat.colorize("&7&l<< "));
        }
        else {
            backArrow = new TextComponent(ColorFormat.colorize("&6&l<< "));
            backArrow.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ColorFormat.colorize("&6Go to page " + (page - 1)))));
            backArrow.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/coordsaver:globalcoords list " + (page - 1)));
        }

        TextComponent forwardArrow;
        if (page == maxPages) {
            forwardArrow = new TextComponent(ColorFormat.colorize("&7&l >>"));
        }
        else {
            forwardArrow = new TextComponent(ColorFormat.colorize("&6&l >>"));
            forwardArrow.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ColorFormat.colorize("&6Go to page " + (page + 1)))));
            forwardArrow.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/coordsaver:globalcoords list " + (page + 1)));
        }

        TextComponent centerText = new TextComponent(ColorFormat.colorize("&6Global Coordinate List: &e(Page " + page + " of " + maxPages + ")"));

        sender.sendMessage(BORDER_LINE);
        sender.spigot().sendMessage(backArrow, centerText, forwardArrow);
        for (Coordinate coordinate : coordinates) {
            sender.sendMessage(ColorFormat.colorize("&e- " + coordinate.toString()));
        }
        sender.sendMessage(BORDER_LINE);

        return true;
    }
}
