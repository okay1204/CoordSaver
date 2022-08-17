package me.okay.coordsaver.command;

import me.okay.coordsaver.CoordSaver;
import me.okay.coordsaver.CustomCommand;
import me.okay.coordsaver.command.subcommand.GlobalCoordsSet;
import me.okay.coordsaver.command.subcommand.GloballCoordsList;

public class GlobalCoords extends CustomCommand {
    public GlobalCoords(CoordSaver plugin) {
        super(plugin, "globalcoords");

        addSubcommand(new GlobalCoordsSet());
        addSubcommand(new GloballCoordsList());
    }
}
