package com.gufli.hytale.toolbox.modules.admin.commands;

import com.gufli.brick.i18n.hytale.localization.HytaleLocalizer;
import com.gufli.colonel.annotation.annotations.Command;
import com.gufli.colonel.annotation.annotations.parameter.Parameter;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.common.dispatch.definition.ReadMode;
import com.gufli.colonel.hytale.annotations.command.CommandHelp;
import com.gufli.colonel.hytale.annotations.command.Permission;
import com.gufli.colonel.hytale.annotations.parameter.ParameterHelp;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.modules.entity.component.Invulnerable;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;

public class GodCommand {

    private final HytaleLocalizer localizer;

    public GodCommand(HytaleLocalizer localizer) {
        this.localizer = localizer;
    }

    @Command("god")
    @CommandHelp(description = "cmd.god.help.description")
    @Permission("gufli.toolbox.command.god")
    public void god(@Source PlayerRef sender) {
        var ref = sender.getReference();
        if ( ref == null ) {
            return;
        }

        var store = ref.getStore();

        Invulnerable invulnerable = store.getComponent(ref, Invulnerable.getComponentType());
        if ( invulnerable == null ) {
            store.putComponent(ref, Invulnerable.getComponentType(), Invulnerable.INSTANCE);
            localizer.send(sender, "cmd.god.enabled");
        } else {
            store.tryRemoveComponent(ref, Invulnerable.getComponentType());
            localizer.send(sender, "cmd.god.disabled");
        }
    }

}
