package me.okay.coordsaver.command;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
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
            "list [<page>] [<player>]"
        );

        this.plugin = plugin;
    }

    @Override
    public boolean onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (args.length > 1 && !sender.hasPermission("coordsaver.coords.list.others")) {
            sender.sendMessage(ColorFormat.colorize("&cYou do not have permission to view other players' coordinates. &7(coordsaver.coords.list.others)"));
            return true;
        }

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

        Player targetPlayer;
        if (args.length < 2) {
            if (sender instanceof Player) {
                targetPlayer = (Player) sender;
            }
            else {
                sender.sendMessage(ColorFormat.colorize("&cYou must specify a player if using this command from console."));
                return false;
            }
        }
        else {
            targetPlayer = plugin.getServer().getPlayer(args[1]);
            if (targetPlayer == null) {
                sender.sendMessage(ColorFormat.colorize("&cPlayer not found."));
                return true;
            }
        }

        int maxPages = (int) Math.ceil(plugin.getDatabase().getPrivateCoordinatesCount(targetPlayer.getUniqueId()) / (double) CoordSaver.COORDS_PER_PAGE);
        if (maxPages == 0) {
            sender.sendMessage(ColorFormat.colorize("&c" + (sender == targetPlayer ? "You do" : targetPlayer.getName() + " does") + " not have any coordinates saved."));
            return true;
        }

        if (page > maxPages) {
            page = maxPages;
        }

        List<Coordinate> coordinates = plugin.getDatabase().paginatePrivateCoordinates(targetPlayer.getUniqueId(), page);

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

        TextComponent centerText = new TextComponent(ColorFormat.colorize("&6" + targetPlayer.getName() + "'s Coordinate List: &e(Page " + page + " of " + maxPages + ")"));

        sender.sendMessage(CoordSaver.BORDER_LINE);
        sender.spigot().sendMessage(backArrow, centerText, forwardArrow);
        for (Coordinate coordinate : coordinates) {
            sender.sendMessage(ColorFormat.colorize("&e- " + coordinate.toString()));
        }
        sender.sendMessage(CoordSaver.BORDER_LINE);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (args.length == 2 && sender.hasPermission("coordsaver.coords.list.others")) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(name -> name.startsWith(args[1])).collect(Collectors.toList());
        }

        return List.of();
    }
}
