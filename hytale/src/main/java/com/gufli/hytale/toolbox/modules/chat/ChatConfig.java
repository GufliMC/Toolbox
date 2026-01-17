package com.gufli.hytale.toolbox.modules.chat;

import io.github.wasabithumb.jtoml.serial.TomlSerializable;
import io.github.wasabithumb.jtoml.serial.reflect.Key;

public class ChatConfig implements TomlSerializable {

    public boolean enabled = true;

    @Key("chat-format")
    public String chatFormat = "<white>[{player}]: <gray>{message}";

}
