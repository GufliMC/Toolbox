package com.gufli.hytale.toolbox.arguments;

import com.gufli.brick.i18n.common.localization.I18nLocalizer;
import com.gufli.brick.i18n.hytale.localization.HytaleLocalizer;
import com.gufli.colonel.annotation.annotations.Completer;
import com.gufli.colonel.annotation.annotations.Parser;
import com.gufli.colonel.annotation.annotations.parameter.Input;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.common.build.FailureHandler;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import java.util.List;

public class HytaleArguments {

    private final HytaleLocalizer localizer;

    public HytaleArguments(HytaleLocalizer localizer) {
        this.localizer = localizer;
    }

    // PLAYERS

//    @Parser(type = Player.class)
//    public Player player(@Source Player sender, @Input String input) {
//        return regionManager
//                .region(worldId, input)
//                .orElseThrow(() -> FailureHandler.of(() -> localizer.send(sender, "cmderr.args.player-not-online", input)));
//    }
//
//    @Completer(type = Player.class)
//    public List<String> player(@Source String input) {
//        //
//    }

}
