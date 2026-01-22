package com.gufli.hytale.toolbox.database;

import com.gufli.brick.orm.ebean.database.EbeanConfig;
import com.gufli.brick.orm.ebean.database.EbeanDatabaseContext;
import com.gufli.brick.orm.ebean.database.EbeanMigrations;
import com.gufli.hytale.toolbox.database.converters.TransformConverter;
import com.gufli.hytale.toolbox.database.entity.EHome;
import com.gufli.hytale.toolbox.database.entity.EPlayer;
import com.gufli.hytale.toolbox.database.entity.EWarp;
import io.ebean.annotation.Platform;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Arrays;

public class ToolboxDatabaseContext extends EbeanDatabaseContext {

    public final static String DATASOURCE_NAME = "Toolbox";

    public ToolboxDatabaseContext(EbeanConfig config) {
        super(config, DATASOURCE_NAME);
    }

    public ToolboxDatabaseContext(EbeanConfig config, int poolSize) {
        super(config, DATASOURCE_NAME, poolSize);
    }

    @Override
    protected Class<?>[] applicableClasses() {
        return APPLICABLE_CLASSES;
    }

    private final static Class<?>[] APPLICABLE_CLASSES = new Class[]{
            EPlayer.class,
            EWarp.class,
            EHome.class,

            TransformConverter.class
    };

    public static void main(String[] args) throws IOException, SQLException {
        EbeanMigrations generator = new EbeanMigrations(
                DATASOURCE_NAME,
                Path.of("Toolbox/hytale/src/main/resources"),
                Platform.H2, Platform.MYSQL
        );
        Arrays.stream(APPLICABLE_CLASSES).forEach(generator::addClass);
        generator.generate();
    }
}
