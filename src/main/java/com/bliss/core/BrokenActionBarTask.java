package com.bliss.core;

import com.bliss.BlissPlugin;
import com.bliss.entity.MotifSpeechListener;
import com.bliss.honored.JuggernautGemGuardListener;
import com.bliss.villager.Text;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class BrokenActionBarTask {
    private static final long PERIOD_TICKS = 5L;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private BukkitTask task;

    public BrokenActionBarTask(BlissPlugin var1, PlayerDataStore var2, int var3) {
        this.plugin = var1;
        this.players = var2;
    }

    public void start(int var1) {
        BukkitScheduler var3 = Bukkit.getScheduler();
        BlissPlugin var11 = this.plugin;
        Runnable var12 = this::tick;
        BukkitTask var10 = var3.runTaskTimer(var11, var12, 5L, 5L);
        this.task = var10;
    }

    public void stop(int var1) {
        if (this.task != null) {
            this.task.cancel();
        }
    }

    private void tick() {
        for (Player var4 : Bukkit.getOnlinePlayers()) {
            if (!JuggernautGemGuardListener.isJuggernaut(var4, 859003337)) {
                PlayerData var5;
                boolean var36;
                label38: {
                    PlayerDataStore var16 = this.players;
                    UUID var37 = var4.getUniqueId();
                    var5 = var16.get(var37, 1264010596);
                    if (GlobalState.forPlayer(var4, 1433087673).spawnGemDisable(898885534)) {
                        Location var22 = var4.getLocation();
                        String var38 = "spawn";
                        if (WorldGuardHook.isInRegion(var22, var38, 32157142)) {
                            var36 = true;
                            break label38;
                        }
                    }

                    var36 = false;
                }

                var5.setInSpawnRegion(var36, 1214845791);
                if (!MotifSpeechListener.isSpeakingTo(var4.getUniqueId(), 1524431478)) {
                    if (var5.isTagged(555875923)) {
                        Component var43 = Text.legacy("\ud83d\udd12 &8&lᴅɪꜱᴀʙʟᴇᴅ: &7" + var5.tagSecondsLeft(487151397) + "s", 373192400);
                        var4.sendActionBar(var43);
                    } else if (var36) {
                        Component var48 = Text.legacy("\ud83d\udd12 &8&lᴅɪꜱᴀʙʟᴇᴅ &7(spawn)", 373192400);
                        var4.sendActionBar(var48);
                    } else if (var5.energy(1160853318) < 2) {
                        Component var46 = Text.legacy("&cBROKEN", 373192400);
                        var4.sendActionBar(var46);
                    }
                }
            }
        }
    }
}
