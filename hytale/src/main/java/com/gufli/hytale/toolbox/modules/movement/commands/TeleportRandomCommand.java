package com.gufli.hytale.toolbox.modules.movement.commands;

import com.gufli.colonel.annotation.annotations.Command;
import com.gufli.colonel.annotation.annotations.parameter.Parameter;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.hytale.toolbox.modules.movement.MovementModule;
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
    private final MovementModule module;

    public TeleportRandomCommand(@NotNull MovementModule module) {
        this.module = module;
    }

    @Command("tprandom")
    @Command("tpr")
    public void tprandom(@Source PlayerRef sender) {
        teleport(sender, sender);
    }

    @Command("tprandom")
    @Command("tpr")
    public void tprandom(@Source PlayerRef sender, @Parameter PlayerRef target) {
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

        module.teleport(player, world, new Transform(x, height, z));
        module.plugin().localizer().send(player, "cmd.tprandom.teleported");
    }

}
