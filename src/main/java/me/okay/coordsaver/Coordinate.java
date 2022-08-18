package me.okay.coordsaver;

import me.okay.coordsaver.utils.ColorFormat;

public class Coordinate {
    private final String name;
    private final int x;
    private final int y;
    private final int z;
    private final String world;

    public Coordinate(String name, int x, int y, int z, String world) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public String getWorld() {
        return world;
    }

    public String toString() {
        return ColorFormat.colorize("&b") + name + ColorFormat.colorize(": &3" + x + " " + y + " " + z + " &9" + world);
    }
}
