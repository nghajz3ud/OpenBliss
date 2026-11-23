package com.bliss.honored;

import com.bliss.core.GlobalState;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class HonoredCompassTask extends BukkitRunnable {
    private static final long PERIOD = 10L;
    private final Plugin plugin;
    private final HonoredOneManager ho;
    private final AssessmentWindow window;
    private final GlobalState globalState;

    public HonoredCompassTask(Plugin var1, HonoredOneManager var2, AssessmentWindow var3, GlobalState var4, int var5) {
        this.plugin = var1;
        this.ho = var2;
        this.window = var3;
        this.globalState = var4;
    }

    public void start(int var1) {
        Plugin var9 = this.plugin;
        this.runTaskTimer(var9, 10L, 10L);
    }

    public void run() {
        UUID var4 = this.ho.current(1257858083);
        Player var58;
        if (var4 == null) {
            var58 = null;
        } else {
            var58 = Bukkit.getPlayer(var4);
        }

        Player var5 = var58;
        boolean var59;
        if (this.globalState.trackingCompasses(758328200) && this.window.isActive(2020345102) && var58 != null && var58.isOnline()) {
            var59 = true;
        } else {
            var59 = false;
        }

        boolean var6 = var59;

        for (Player var8 : Bukkit.getOnlinePlayers()) {
            PlayerInventory var9;
            Location var60;
            label54: {
                var9 = var8.getInventory();
                if (var6) {
                    World var38 = var5.getWorld();
                    World var61 = var8.getWorld();
                    if (var38 == var61) {
                        var60 = var5.getLocation();
                        break label54;
                    }
                }

                var60 = null;
            }

            Location var10 = var60;
            ItemStack[] var11 = var9.getContents();
            int var12 = 0;

            while (true) {
                int var63 = var11.length;
                if (var12 >= var63) {
                    ItemStack var69 = var9.getItemInOffHand();
                    if (HonoredCompass.isHoCompass(var69, 736733534) && HonoredCompass.aim(var69, var10, 1675779828)) {
                        var9.setItemInOffHand(var69);
                    }
                    break;
                }

                ItemStack var13 = var11[var12];
                if (HonoredCompass.isHoCompass(var13, 736733534) && HonoredCompass.aim(var13, var10, 1675779828)) {
                    var9.setItem(var12, var13);
                }

                var12++;
            }
        }
    }
}
