package com.gufli.hytale.toolbox.modules.movement;

import io.github.wasabithumb.jtoml.serial.TomlSerializable;
import io.github.wasabithumb.jtoml.serial.reflect.Key;

public class MovementConfig implements TomlSerializable {

    @Key("teleport-random")
    public TeleportRandomConfig teleportRandom = new TeleportRandomConfig();

    public static class TeleportRandomConfig implements TomlSerializable {

        @Key("lower-bound-x")
        public int lowerBoundX = -10000;

        @Key("upper-bound-x")
        public int upperBoundX = 10000;

        @Key("lower-bound-z")
        public int lowerBoundZ = -10000;

        @Key("upper-bound-z")
        public int upperBoundZ = 10000;

    }

}
