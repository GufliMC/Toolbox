package com.gufli.hytale.toolbox;

import com.gufli.hytale.toolbox.modules.commands.CommandsConfig;
import com.gufli.hytale.toolbox.modules.movement.MovementConfig;
import io.github.wasabithumb.jtoml.serial.TomlSerializable;

public class ToolboxConfig implements TomlSerializable {

    public MovementConfig movement = new MovementConfig();
    public CommandsConfig commands = new CommandsConfig();

}
