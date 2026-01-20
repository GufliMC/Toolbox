package com.gufli.hytale.toolbox;

import com.gufli.hytale.toolbox.database.DatabaseConfig;
import com.gufli.hytale.toolbox.modules.chat.ChatConfig;
import com.gufli.hytale.toolbox.modules.commands.CommandsConfig;
import com.gufli.hytale.toolbox.modules.information.InformationConfig;
import com.gufli.hytale.toolbox.modules.teleport.TeleportConfig;
import io.github.wasabithumb.jtoml.serial.TomlSerializable;

public class ToolboxConfig implements TomlSerializable {

    public ChatConfig chat = new ChatConfig();
    public InformationConfig information = new InformationConfig();
    public CommandsConfig commands = new CommandsConfig();
    public TeleportConfig teleport = new TeleportConfig();
    public DatabaseConfig database = new DatabaseConfig();

}
