package com.gufli.hytale.toolbox.modules.teleport.commands;

import com.gufli.colonel.annotation.annotations.Command;
import com.gufli.colonel.annotation.annotations.parameter.Parameter;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.hytale.annotations.command.CommandHelp;
import com.gufli.colonel.hytale.annotations.command.Permission;
import com.gufli.colonel.hytale.annotations.parameter.ParameterHelp;
import com.gufli.hytale.toolbox.modules.teleport.TeleportModule;
import com.gufli.hytale.toolbox.modules.warmup.WarmupModule;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.util.MathUtil;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class TeleportRandomCommand {

    private final Random random = new Random();
    private final TeleportModule module;

    public TeleportRandomCommand(@NotNull TeleportModule module) {
        this.module = module;
    }

    @Command("tprandom")
    @Permission("gufli.toolbox.command.tprandom")
    @CommandHelp(description = "cmd.tprandom.help.description")
    public void tprandom(@Source PlayerRef sender) {
        teleport(sender, sender);
    }

    @Command("tprandom")
    @Permission("gufli.toolbox.command.tprandom")
    @CommandHelp(description = "cmd.tprandom.help.description")
    public void tprandom(@Source PlayerRef sender,
                         @Parameter
                         @ParameterHelp(description = "cmd.tprandom.help.param.target.description", type = "cmd.tprandom.help.param.target.type")
                         PlayerRef target) {
        teleport(sender, target);
    }

    private void teleport(@NotNull PlayerRef sender, @NotNull PlayerRef player) {
        if ( sender.getWorldUuid() == null ) {
            module.plugin().localizer().send(sender, "cmd.tprandom.error.no-teleport-valid");
            return;
        }

        World world = Universe.get().getWorld(sender.getWorldUuid());
        if ( world == null ) {
            module.plugin().localizer().send(sender, "cmd.tprandom.error.no-teleport-valid");
            return;
        }

        var x = random.nextInt(module.config().teleportRandom.lowerBoundX, module.config().teleportRandom.upperBoundX + 1);
        var z = random.nextInt(module.config().teleportRandom.lowerBoundZ, module.config().teleportRandom.upperBoundZ + 1);

        WorldChunk worldChunk = world.getChunk(ChunkUtil.indexChunkFromBlock(x, z));
        if ( worldChunk == null ) {
            module.plugin().localizer().send(sender, "cmd.tprandom.error.no-teleport-valid");
            return;
        }
        int height = worldChunk.getHeight(MathUtil.floor(x), MathUtil.floor(z));

        WarmupModule warmup = this.module.plugin().module(WarmupModule.class);
        warmup.teleport(player, () -> {
            module.teleport(player, world, new Transform(x, height, z));
            module.plugin().localizer().send(player, "cmd.tprandom.teleported");
        });

    }

}
