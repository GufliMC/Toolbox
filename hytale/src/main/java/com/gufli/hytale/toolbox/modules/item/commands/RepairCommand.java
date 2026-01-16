package com.gufli.hytale.toolbox.modules.item.commands;

import com.gufli.colonel.annotation.annotations.Command;
import com.gufli.colonel.annotation.annotations.parameter.Source;
import com.gufli.colonel.hytale.annotations.command.CommandHelp;
import com.gufli.colonel.hytale.annotations.command.Permission;
import com.gufli.hytale.toolbox.modules.item.ItemModule;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jetbrains.annotations.NotNull;

public class RepairCommand {

    private final ItemModule module;

    public RepairCommand(@NotNull ItemModule module) {
        this.module = module;
    }

    @Command("repair")
    @Permission("gufli.toolbox.command.repair")
    @CommandHelp(description = "cmd.repair.help.description")
    public void repair(@Source PlayerRef sender) {
        var ref = sender.getReference();
        if ( ref == null ) {
            return;
        }

        var store = ref.getStore();

        var player = store.getComponent(ref, Player.getComponentType());
        if ( player == null ) {
            return;
        }

        Inventory inventory = player.getInventory();
        ItemStack item = inventory.getActiveHotbarItem();
        if (item == null || item.isEmpty()) {
            return;
        }

        ItemStack replacement = item.withDurability(item.getMaxDurability());
        inventory.getHotbar().replaceItemStackInSlot(inventory.getActiveHotbarSlot(), item, replacement);

        module.plugin().localizer().send(sender, "cmd.repair.repaired");
    }

}
