package com.gufli.hytale.toolbox.modules.warmup;

import io.github.wasabithumb.jtoml.comment.Comment;
import io.github.wasabithumb.jtoml.serial.TomlSerializable;
import io.github.wasabithumb.jtoml.serial.reflect.Key;

import java.util.HashMap;
import java.util.Map;

public class WarmupConfig implements TomlSerializable {

    @Comment.Inline("Assign a group with the following permission: gufli.toolbox.warmup.teleport.group.<groupname>")
    @Key("groups")
    public Map<String, TeleportWarmupGroup> groups = new HashMap<>();

    public WarmupConfig() {
        var defaultGroup = new TeleportWarmupGroup();
        defaultGroup.delay = 5;
        groups.put("default", defaultGroup);

        var adminGroup = new TeleportWarmupGroup();
        adminGroup.delay = 0;
        groups.put("admin", adminGroup);
    }

    public static class TeleportWarmupGroup implements TomlSerializable {

        public int delay;

    }
}
