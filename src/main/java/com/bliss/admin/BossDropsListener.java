package com.bliss.admin;

import com.bliss.BlissPlugin;
import com.bliss.core.GlobalState;
import com.bliss.items.BlissCoreItems;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public final class BossDropsListener implements Listener {
    private final BlissPlugin plugin;
    private final BlissCoreItems items;
    private final GlobalState state;
    private final Random random;

    public BossDropsListener(BlissPlugin var1, BlissCoreItems var2, GlobalState var3, int var4) {
        Random var6 = new Random();
        this.random = var6;
        this.plugin = var1;
        this.items = var2;
        this.state = var3;
    }

    @EventHandler
    public void onDeath(EntityDeathEvent var1) {
        LivingEntity var4 = var1.getEntity();
        int var5 = this.state.stage(1483650410);
        if (var4 instanceof Wither && var5 == 1) {
            if (!this.state.witherDropsDone(1566547913)) {
                this.state.setWitherDropsDone(true, 958591876);
                this.state.save(343464104);
                this.triggerWitherDrops(var4, 1898202684);
            }

            if (this.state.eventProgression(169255316) && this.state.bossProgression(1009383364)) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bliss progression 2");
            }
        } else {
            if (var4 instanceof EnderDragon && var5 == 2 && this.state.eventProgression(169255316) && this.state.bossProgression(1009383364)) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bliss progression 3");
            }
        }
    }

    private void triggerWitherDrops(LivingEntity var1, int var2) {
        Location var13 = var1.getLocation().clone();
        Vector var14 = var13.getDirection().setY(0);
        Vector var40;
        if (var14.lengthSquared() < 1.0E-8) {
            var40 = new Vector(1, 0, 0);
        } else {
            var40 = var14.normalize();
        }

        Vector var15 = var40;
        double var51 = -var15.getZ();
        double var80 = var15.getX();
        Vector var28 = new Vector(var51, 0.0, var80);
        Vector var16 = var28;

        for (int var17 = 0; var17 < 5; var17++) {
            BukkitScheduler var37 = Bukkit.getScheduler();
            BlissPlugin var46 = this.plugin;
            Runnable var56 = () -> {
                ItemStack var10 = this.items.upgrader(2125662274);
                this.dropAt(var13, var15, var16, var10, 1407665641);
            };
            long var66 = (long)var17 * 2L;
            var37.runTaskLater(var46, var56, var66);
        }

        for (int var82 = 0; var82 < 8; var82++) {
            BukkitScheduler var34 = Bukkit.getScheduler();
            BlissPlugin var44 = this.plugin;
            Runnable var54 = () -> {
                ItemStack var10 = this.items.trader(662298763);
                this.dropAt(var13, var15, var16, var10, 1407665641);
            };
            long var75 = (long)var82 * 2L;
            long var60 = 10L + var75;
            var34.runTaskLater(var44, var54, var60);
        }
    }

    private void dropAt(Location var1, Vector var2, Vector var3, ItemStack var4, int var5) {
        double var10 = this.random.nextDouble() * 10.0 - 5.0;
        double var12 = this.random.nextDouble() * 10.0 - 5.0;
        Location var31 = var1.clone();
        Vector var37 = var3.clone();
        Vector var38 = var37.multiply(var10);
        Location var32 = var31.add(var38);
        Vector var40 = var2.clone();
        Vector var41 = var40.multiply(var12);
        Location var14 = var32.add(var41);
        World var35 = var14.getWorld();
        var35.dropItemNaturally(var14, var4);
    }
}
