package com.bliss.core;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public final class VillagerSpawnGateListener implements Listener {
    private final GlobalState state;

    public VillagerSpawnGateListener(GlobalState var1, int var2) {
        this.state = var1;
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent var1) {
        EntityType var5 = var1.getEntityType();
        EntityType var3 = EntityType.VILLAGER;
        if (var5 == var3) {
            if (!this.state.villagers(570366672)) {
                SpawnReason var4 = var1.getSpawnReason();
                SpawnReason var14 = SpawnReason.CUSTOM;
                if (var4 != var14) {
                    SpawnReason var15 = SpawnReason.COMMAND;
                    if (var4 != var15) {
                        var1.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }
}
