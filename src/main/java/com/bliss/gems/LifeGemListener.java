package com.bliss.gems;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class LifeGemListener implements Listener {
    public final Map<UUID, Long> cd1;
    public final Map<UUID, Long> cd2;
    public final Map<UUID, Long> cd3;
    public final Map<UUID, Long> cd4;
    public final Map<UUID, Long> vortexActiveUntil;
    public final Map<UUID, Long> nextHealMs;
    public final Map<UUID, Long> drainedGroupExpiry;
    public final Map<UUID, Long> drainedSoloExpiry;
    public final Map<UUID, Long> colGroupExpiry;
    public final Map<UUID, Long> colCasterExpiry;
    public final Map<UUID, Long> colCasterUntil;
    public final Map<UUID, LifeGemListener.HeartLockState> heartLocked;
    public final Map<UUID, Double> baselineMaxHealth;

    public LifeGemListener() {
        ConcurrentHashMap var2 = new ConcurrentHashMap();
        this.cd1 = var2;
        ConcurrentHashMap var16 = new ConcurrentHashMap();
        this.cd2 = var16;
        ConcurrentHashMap var17 = new ConcurrentHashMap();
        this.cd3 = var17;
        ConcurrentHashMap var18 = new ConcurrentHashMap();
        this.cd4 = var18;
        ConcurrentHashMap var19 = new ConcurrentHashMap();
        this.vortexActiveUntil = var19;
        ConcurrentHashMap var20 = new ConcurrentHashMap();
        this.nextHealMs = var20;
        ConcurrentHashMap var21 = new ConcurrentHashMap();
        this.drainedGroupExpiry = var21;
        ConcurrentHashMap var22 = new ConcurrentHashMap();
        this.drainedSoloExpiry = var22;
        ConcurrentHashMap var23 = new ConcurrentHashMap();
        this.colGroupExpiry = var23;
        ConcurrentHashMap var24 = new ConcurrentHashMap();
        this.colCasterExpiry = var24;
        ConcurrentHashMap var25 = new ConcurrentHashMap();
        this.colCasterUntil = var25;
        ConcurrentHashMap var26 = new ConcurrentHashMap();
        this.heartLocked = var26;
        ConcurrentHashMap var27 = new ConcurrentHashMap();
        this.baselineMaxHealth = var27;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent var1) {
        Player var4 = var1.getPlayer();
        LifeAuraTask.restoreBaseline(this, var4, 464898981);
    }

    public static final class HeartLockState {
        public final double maxHp;
        public final long expiry;

        public HeartLockState(double var1, long var3, int var5) {
            this.maxHp = var1;
            this.expiry = var3;
        }
    }
}
