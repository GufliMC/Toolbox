package com.gufli.hytale.toolbox.modules.movement;

public class MovementConfig {

    public TeleportRandomConfig teleportRandom = new TeleportRandomConfig();


    public class TeleportRandomConfig {

        public int lowerBoundX = -10000;
        public int upperBoundX = 10000;
        public int lowerBoundZ = -10000;
        public int upperBoundZ = 10000;

    }

}
