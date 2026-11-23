package com.bliss.gems;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public final class AstraGemListener implements Listener {
    public final Map<UUID, Long> cd1;
    public final Map<UUID, Long> cd2;
    public final Map<UUID, Long> cd3;
    public final Map<UUID, Long> driftActiveUntil;
    public final Map<UUID, AstraGemListener.ProjectionState> projections;

    public AstraGemListener() {
        ConcurrentHashMap var2 = new ConcurrentHashMap();
        this.cd1 = var2;
        ConcurrentHashMap var8 = new ConcurrentHashMap();
        this.cd2 = var8;
        ConcurrentHashMap var9 = new ConcurrentHashMap();
        this.cd3 = var9;
        ConcurrentHashMap var10 = new ConcurrentHashMap();
        this.driftActiveUntil = var10;
        ConcurrentHashMap var11 = new ConcurrentHashMap();
        this.projections = var11;
    }

    public static final class ProjectionState {
        public final ItemStack[] savedContents;
        public final ItemStack[] savedArmor;
        public final ItemStack savedOffhand;
        public final UUID zombieUid;
        public final int npcId;
        public final Location originalLocation;
        public final GameMode originalGameMode;

        public ProjectionState(ItemStack[] var1, ItemStack[] var2, ItemStack var3, UUID var4, int var5, Location var6, GameMode var7, int var8) {
            this.savedContents = var1;
            this.savedArmor = var2;
            this.savedOffhand = var3;
            this.zombieUid = var4;
            this.npcId = var5;
            this.originalLocation = var6;
            this.originalGameMode = var7;
        }
    }
}
