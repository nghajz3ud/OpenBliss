package com.bliss.honored;

import com.bliss.BlissPlugin;
import com.bliss.villager.Text;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public final class HonoredOneListener implements Listener {
    private final HonoredOneManager manager;
    private final AssessmentWindow window;
    private final BlissPlugin plugin;

    public HonoredOneListener(HonoredOneManager var1, AssessmentWindow var2, BlissPlugin var3, int var4) {
        this.manager = var1;
        this.window = var2;
        this.plugin = var3;
    }

    @EventHandler(
        priority = EventPriority.MONITOR,
        ignoreCancelled = true
    )
    public void onDeath(PlayerDeathEvent var1) {
        Player var5 = var1.getEntity();
        HonoredOneManager var9 = this.manager;
        UUID var22 = var5.getUniqueId();
        if (var9.isHonoredOne(var22, 1908342269)) {
            if (!this.window.isActive(2020345102)) {
                Player var6 = var5.getKiller();
                if (var6 != null && !var6.equals(var5)) {
                    Component var25 = Text.legacy("&7The Honored One title can only be claimed while the Assessment window is open.", 373192400);
                    var6.sendMessage(var25);
                }
            } else {
                HonoredOneManager var21 = this.manager;
                Player var27 = var5.getKiller();
                var21.onHonoredOneKilled(var5, var27, 1207509925);
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent var1) {
        HonoredOneManager var4 = this.manager;
        Player var9 = var1.getPlayer();
        var4.refresh(var9, 1695293367);
        BlissPlugin var6 = this.plugin;
        Player var11 = var1.getPlayer();
        var6.tryPendingAscension(var11, 1229253917);
        BlissPlugin var8 = this.plugin;
        Player var13 = var1.getPlayer();
        var8.tryRestoreAudienceMode(var13, 886063543);
    }
}
