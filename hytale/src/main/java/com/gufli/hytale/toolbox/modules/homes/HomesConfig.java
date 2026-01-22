package com.gufli.hytale.toolbox.modules.homes;

import io.github.wasabithumb.jtoml.comment.Comment;
import io.github.wasabithumb.jtoml.serial.TomlSerializable;
import io.github.wasabithumb.jtoml.serial.reflect.Key;

import java.util.HashMap;
import java.util.Map;

public class HomesConfig implements TomlSerializable {

    @Comment.Inline("Assign a group with the following permission: gufli.toolbox.homes.group.<groupname>")
    @Key("groups")
    public Map<String, HomeLimitGroup> groups = new HashMap<>();

    public HomesConfig() {
        var defaultGroup = new HomeLimitGroup();
        defaultGroup.limit = 3;
        groups.put("default", defaultGroup);

        var adminGroup = new HomeLimitGroup();
        adminGroup.limit = 9999;
        groups.put("admin", adminGroup);
    }

    public static class HomeLimitGroup implements TomlSerializable {
        public int limit;
    }
}