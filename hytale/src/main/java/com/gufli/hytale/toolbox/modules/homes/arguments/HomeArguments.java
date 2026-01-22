package com.gufli.hytale.toolbox.modules.homes.arguments;

import com.gufli.colonel.annotation.annotations.Completer;
import com.gufli.colonel.annotation.annotations.Parser;
import com.gufli.colonel.annotation.annotations.parameter.Input;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.common.build.FailureHandler;
import com.gufli.hytale.toolbox.database.entity.EHome;
import com.gufli.hytale.toolbox.modules.homes.HomesModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class HomeArguments {

    private final HomesModule module;

    public HomeArguments(@NotNull HomesModule module) {
        this.module = module;
    }

    // HOME

    @Parser(type = EHome.class)
    public EHome home(@Source PlayerRef sender, @Input String input) {
        return module.home(sender.getUuid(), input)
                .orElseThrow(() -> FailureHandler.of(() -> module.plugin().localizer().send(sender, "cmd.home.error.home-not-exist", input)));
    }

    @Completer(type = EHome.class)
    public List<String> home(@Source PlayerRef sender) {
        return module.homes(sender.getUuid()).stream()
                .map(EHome::name)
                .collect(Collectors.toList());
    }

}