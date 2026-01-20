package com.gufli.hytale.toolbox.modules.information;

import io.github.wasabithumb.jtoml.serial.TomlSerializable;
import io.github.wasabithumb.jtoml.serial.reflect.Key;

public class InformationConfig implements TomlSerializable {

    @Key("players")
    public JoinQuitConfig joinQuit = new JoinQuitConfig();

    public MotdConfig motd = new MotdConfig();

    public static class JoinQuitConfig implements TomlSerializable {

        public boolean enabled = true;

        @Key("join-first-message")
        public String joinFirstMessage = "<gray>Welcome <gold>{player}<gray> to the server! They deserve a cake.";

        @Key("join-message")
        public String joinMessage = "<green><bold>+</bold> <gold>{player} <gray>has joined the game.";

        @Key("quit-message")
        public String quitMessage = "<red><bold>-</bold> <gold>{player} <gray>has left the game.";

    }

    public static class MotdConfig implements TomlSerializable {

        public boolean enabled = true;

        @Key("motd")
        public String motd =
                "<gold>==============================</gold>\n" +
                        "<gray>Welcome to the server, <gold>{player}<gray>!\n" +
                        "<gray>Have fun and follow the <green>/rules<gray>!\n" +
                        "<gold>==============================</gold>";

    }

}
