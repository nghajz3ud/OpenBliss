package com.bliss.core;

import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public final class DreamContextListener implements Listener {
    private final PlayerDataStore players;

    public DreamContextListener(PlayerDataStore var1, int var2) {
        this.players = var1;
    }

    public void refresh(Player var1, int var2) {
        PlayerDataStore var5 = this.players;
        UUID var7 = var1.getUniqueId();
        PlayerData var6 = var5.get(var7, 1264010596);
        boolean var10 = GlobalState.isDreamWorld(var1.getWorld(), 437560231);
        var6.setInDream(var10, 497796284);
    }

    @EventHandler(
        priority = EventPriority.LOWEST
    )
    public void onJoin(PlayerJoinEvent var1) {
        Player var4 = var1.getPlayer();
        this.refresh(var4, 2041142238);
    }

    @EventHandler(
        priority = EventPriority.LOWEST
    )
    public void onWorldChange(PlayerChangedWorldEvent var1) {
        Player var4 = var1.getPlayer();
        this.refresh(var4, 2041142238);
    }
}
