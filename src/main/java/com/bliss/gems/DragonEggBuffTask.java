package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.GlobalState;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class DragonEggBuffTask {
    private static final long PERIOD_TICKS = 20L;
    private static final double BONUS_HP = 20.0;
    private final BlissPlugin plugin;
    private final NamespacedKey modifierKey;
    private BukkitTask task;

    public DragonEggBuffTask(BlissPlugin var1, int var2) {
        this.plugin = var1;
        NamespacedKey var9 = new NamespacedKey(var1, "dragon_egg_health");
        this.modifierKey = var9;
    }

    public void start(int var1) {
        BukkitScheduler var3 = Bukkit.getScheduler();
        BlissPlugin var11 = this.plugin;
        Runnable var12 = this::tick;
        BukkitTask var10 = var3.runTaskTimer(var11, var12, 20L, 20L);
        this.task = var10;
    }

    public void stop(int var1) {
        if (this.task != null) {
            this.task.cancel();
        }

        for (Player var5 : Bukkit.getOnlinePlayers()) {
            this.removeBuff(var5, 1553368);
        }
    }

    private void tick() {
        for (Player var4 : Bukkit.getOnlinePlayers()) {
            GlobalState var13 = this.plugin.globalState(724248306);
            if (var13.holdsDragonEgg(var4, 715974514)) {
                this.applyBuff(var4, 1711425310);
            } else {
                this.removeBuff(var4, 1553368);
            }
        }
    }

    private void applyBuff(Player var1, int var2) {
        Attribute var4 = Attribute.MAX_HEALTH;
        AttributeInstance var9 = var1.getAttribute(var4);
        if (var9 != null && !this.hasBuff(var9, 579533731)) {
            NamespacedKey var17 = this.modifierKey;
            Operation var8 = Operation.ADD_NUMBER;
            AttributeModifier var16 = new AttributeModifier(var17, 20.0, var8);
            var9.addModifier(var16);
        }
    }

    private void removeBuff(Player var1, int var2) {
        Attribute var4 = Attribute.MAX_HEALTH;
        AttributeInstance var6 = var1.getAttribute(var4);
        if (var6 != null) {
            boolean var7 = false;

            for (AttributeModifier var9 : var6.getModifiers()) {
                NamespacedKey var28 = var9.getKey();
                NamespacedKey var36 = this.modifierKey;
                if (var28.equals(var36)) {
                    var6.removeModifier(var9);
                    var7 = true;
                }
            }

            if (var7) {
                double var20 = var1.getHealth();
                double var38 = var6.getValue();
                if (var20 > var38) {
                    double var33 = var6.getValue();
                    var1.setHealth(var33);
                }
            }
        }
    }

    private boolean hasBuff(AttributeInstance var1, int var2) {
        Iterator var5 = var1.getModifiers().iterator();

        while (var5.hasNext()) {
            NamespacedKey var15 = ((AttributeModifier)var5.next()).getKey();
            NamespacedKey var19 = this.modifierKey;
            if (var15.equals(var19)) {
                return true;
            }
        }

        return false;
    }
}
