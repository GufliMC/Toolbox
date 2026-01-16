package com.gufli.hytale.toolbox.modules.commands;

import io.github.wasabithumb.jtoml.serial.TomlSerializable;
import io.github.wasabithumb.jtoml.serial.reflect.Key;

import java.util.HashMap;
import java.util.Map;

public class CommandsConfig implements TomlSerializable {

    @Key("custom-commands")
    public Map<String, CustomCommand> custom = new HashMap<>();

    public CommandsConfig() {
        var infoCommand = new CustomCommand();
        infoCommand.output = "<bold><#008000>Toolbox developed by Gufli.</#008000></bold>\nCheck our discord for help: <#bcbcbc>https://discord.gg/UFEcurxWsV</#bcbcbc>";
        custom.put("info", infoCommand);

        var rulesCommand = new CustomCommand();
        rulesCommand.output = "Server Rules:\n1. Be respectful to others.\n2. No griefing or stealing.\n3. Follow the instructions of the staff.\n4. No cheating or exploiting bugs.\n5. Keep chat appropriate for all ages.";
        custom.put("rules", rulesCommand);
    }

    public static class CustomCommand implements TomlSerializable {

        public String output;

    }

}
