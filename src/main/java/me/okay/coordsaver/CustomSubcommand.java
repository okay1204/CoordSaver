package me.okay.coordsaver;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.okay.coordsaver.utils.ColorFormat;

public abstract class CustomSubcommand {
    protected static final String NO_PERMS_MESSAGE = ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. " +
    "Please contact the server administrators if you believe that this is a mistake.";
    private ArrayList<CustomSubcommand> subcommands = new ArrayList<CustomSubcommand>();
    private CustomSubcommand parent = null;

    private String name;
    private String description;
    private String permission;
    private String usage;
    private String permissionMessage;

    public CustomSubcommand(String name, String description, String permission) {
        this(name, description, permission, name);
    }

    public CustomSubcommand(String name, String description, String permission, String usage) {
        this(name, description, permission, usage, NO_PERMS_MESSAGE);
    }

    public CustomSubcommand(String name, String description, String permission, String usage, String permissionMessage) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.usage = usage;
        this.permissionMessage = permissionMessage;
    }

    public CustomSubcommand(CoordSaver plugin, String name) {
        this.name = name;
        Command command = plugin.getCommand(name);
        this.description = command.getDescription();
        this.permission = command.getPermission();
        this.usage = command.getUsage();
        this.permissionMessage = command.getPermissionMessage() != null ? command.getPermissionMessage() : NO_PERMS_MESSAGE;
    }
    
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public String getFullUsage() {
        if (parent != null) {
            return parent.getFullUsage() + " " + usage;
        }
        else {
            return usage;
        }
    }

    public String getPermission() {
        return permission;
    }

    protected void addSubcommand(CustomSubcommand subcommand) {
        subcommands.add(subcommand);
        subcommand.setParent(this);
    }

    public List<CustomSubcommand> getSubcommands() {
        return subcommands;
    }

    public void setParent(CustomSubcommand parent) {
        this.parent = parent;
    }

    public CustomSubcommand getParent() {
        return parent;
    }

    public String getPermissionMessage() {
        return permissionMessage;
    }

    private String[] cutOffFirstArg(String[] args) {
        String[] newArgs = new String[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            newArgs[i - 1] = args[i];
        }
        return newArgs;
    }

    public CommandResult onCommand(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (subcommands.size() > 0) {
            if (args.length > 0) {
                for (CustomSubcommand subcommand : subcommands) {
                    if (subcommand.getName().equals(args[0])) {
                        return subcommand.onCommand(sender, this, label, cutOffFirstArg(args));
                    }
                }
            }

            // form a usage command that combines all subcommands
            String allUsages = ColorFormat.colorize("&cUsages: \n");
            for (CustomSubcommand subcommand : subcommands) {
                allUsages += subcommand.getFullUsage() + "\n";
            }

            sender.sendMessage(allUsages);
            return CommandResult.USAGE_FAILURE;
        }
        else {
            if (!sender.hasPermission(permission)) {
                sender.sendMessage(getPermissionMessage());
                return CommandResult.PERMISSION_FAILURE;
            }
            else {
                boolean isSuccessful = onRun(sender, command, label, args);
                if (isSuccessful) {
                    return CommandResult.SUCCESS;
                }
                else {
                    sender.sendMessage(ColorFormat.colorize("&cUsage: ") + getFullUsage());
                    return CommandResult.USAGE_FAILURE;
                }
            }
        }
    };

    public boolean onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        return true;
    };

    public List<String> onTabComplete(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (getSubcommands().size() > 0) {
            ArrayList<String> subcommandNames = new ArrayList<String>();

            if (args.length == 1) {
                for (CustomSubcommand subcommand : getSubcommands()) {
                    if (subcommand.getName().startsWith(args[0])) {
                        subcommandNames.add(subcommand.getName());
                    }
                }
            }
            else {
                for (CustomSubcommand subcommand : getSubcommands()) {
                    if (subcommand.getName().equals(args[0])) {
                        subcommandNames.addAll(subcommand.onTabComplete(sender, this, label, cutOffFirstArg(args)));
                    }
                }
            }
            
            return subcommandNames;
        }

        return List.of();
    }
}

