package me.okay.coordsaver;

import java.util.UUID;

public class PersonalCoordinate extends Coordinate {
    private final UUID playerUuid;

    public PersonalCoordinate(String name, int x, int y, int z, String world, UUID playerUuid) {
        super(name, x, y, z, world);
        this.playerUuid = playerUuid;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }
}
