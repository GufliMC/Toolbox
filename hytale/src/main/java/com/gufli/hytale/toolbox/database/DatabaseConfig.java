package com.gufli.hytale.toolbox.database;

import com.gufli.brick.orm.ebean.database.EbeanConfig;
import io.github.wasabithumb.jtoml.serial.TomlSerializable;

public class DatabaseConfig extends EbeanConfig implements TomlSerializable {

    public DatabaseConfig() {
        dsn = "jdbc:h2:file:./mods/Gufli_Toolbox/data/database.h2;MODE=MySQL";
        driver = "org.h2.Driver";
        username = "user";
        password = "";
    }

}
