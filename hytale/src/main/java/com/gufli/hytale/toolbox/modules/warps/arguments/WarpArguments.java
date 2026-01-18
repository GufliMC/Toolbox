package com.gufli.hytale.toolbox.modules.warps.arguments;

import com.gufli.colonel.annotation.annotations.Completer;
import com.gufli.colonel.annotation.annotations.Parser;
import com.gufli.colonel.annotation.annotations.parameter.Input;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.common.build.FailureHandler;
import com.gufli.hytale.toolbox.database.entity.EWarp;
import com.gufli.hytale.toolbox.modules.warps.WarpsModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WarpArguments {

    private final WarpsModule module;

    public WarpArguments(@NotNull WarpsModule module) {
        this.module = module;
    }

    // WARP

    @Parser(type = EWarp.class)
    public EWarp profile(@Source PlayerRef sender, @Input String input) {
        return module.warp(input)
                .orElseThrow(() -> FailureHandler.of(() -> module.plugin().localizer().send(sender, "cmd.warp.error.warp-not-exist", input)));
    }

    @Completer(type = EWarp.class)
    public List<String> profile() {
        return module.warps().stream()
                .map(EWarp::name)
                .toList();
    }

}
