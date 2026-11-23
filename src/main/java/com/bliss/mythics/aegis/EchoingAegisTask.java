package com.bliss.mythics.aegis;

import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class EchoingAegisTask extends BukkitRunnable {
    private static final int CMD_PARRY = 30012;
    private final Plugin plugin;
    private final MythicAegisState state;
    private final Player caster;
    private final long untilMs;
    private ItemDisplay plate;

    public EchoingAegisTask(Plugin var1, MythicAegisState var2, Player var3, long var4) {
        this.plugin = var1;
        this.state = var2;
        this.caster = var3;
        this.untilMs = var4;
    }

    public void start() {
        Location var6 = this.caster.getLocation();
        World var10 = var6.getWorld();
        Sound var3 = Sound.ITEM_SHIELD_BLOCK;
        var10.playSound(var6, var3, 1.0F, 1.4F);
        World var12 = var6.getWorld();
        Sound var21 = Sound.BLOCK_BEACON_ACTIVATE;
        var12.playSound(var6, var21, 0.5F, 1.8F);
        ItemDisplay var18 = this.spawnPlate(2041857564);
        this.plate = var18;
        Plugin var20 = this.plugin;
        this.runTaskTimer(var20, 0L, 1L);
    }

    public void run() {
        if (this.caster.isOnline()) {
            long var5 = System.currentTimeMillis();
            long var10 = this.untilMs;
            if (var5 < var10) {
                this.positionPlate(2000234508);
                return;
            }
        }

        this.close(1431148222);
    }

    private ItemDisplay spawnPlate(int var1) {
        Material var3 = Material.PAPER;
        ItemStack var2 = new ItemStack(var3);
        ItemMeta var6 = var2.getItemMeta();
        if (var6 != null) {
            Integer var22 = 30012;
            var6.setCustomModelData(var22);
            var2.setItemMeta(var6);
        }

        World var16 = this.caster.getWorld();
        Location var26 = this.caster.getEyeLocation();
        Consumer<ItemDisplay> var29 = var1x -> {
            var1x.setPersistent(false);
            var1x.setItemStack(var2);
            ItemDisplayTransform var11 = ItemDisplayTransform.FIXED;
            var1x.setItemDisplayTransform(var11);
            var1x.setTeleportDuration(1);
            Brightness var13 = new Brightness(15, 15);
            var1x.setBrightness(var13);
        };
        ItemDisplay var7 = (ItemDisplay)var16.spawn(var26, ItemDisplay.class, var29);
        this.positionPlate(var7, 1205263012);
        return var7;
    }

    private void positionPlate(int var1) {
        if (this.plate != null && !this.plate.isDead()) {
            ItemDisplay var9 = this.plate;
            this.positionPlate(var9, 1205263012);
        }
    }

    private void positionPlate(ItemDisplay var1, int var2) {
        Location var14 = this.caster.getEyeLocation();
        Vector var15 = var14.getDirection().normalize();
        Location var24 = var14.clone();
        Vector var32 = var15.clone().multiply(1.2);
        Location var16 = var24.add(var32);
        Quaternionf var26 = new Quaternionf();
        float var40 = (float)var15.getX();
        float var44 = (float)var15.getY();
        float var49 = (float)var15.getZ();
        Quaternionf var17 = var26.rotateTo(0.0F, 1.0F, 0.0F, var40, var44, var49);
        var1.teleport(var16);
        Vector3f var37 = new Vector3f(0.0F, 0.0F, 0.0F);
        Vector3f var46 = new Vector3f(0.7F, 0.7F, 0.7F);
        Quaternionf var51 = new Quaternionf();
        Transformation var35 = new Transformation(var37, var17, var46, var51);
        var1.setTransformation(var35);
    }

    private void close(int var1) {
        if (this.plate != null && !this.plate.isDead()) {
            this.plate.remove();
        }

        this.plate = null;
        if (this.caster.isOnline()) {
            Set var19 = this.state.parrySucceeded;
            UUID var31 = this.caster.getUniqueId();
            if (!var19.contains(var31)) {
                World var23 = this.caster.getWorld();
                Location var34 = this.caster.getLocation();
                Sound var4 = Sound.BLOCK_BEACON_DEACTIVATE;
                var23.playSound(var34, var4, 0.5F, 1.6F);
            }
        }

        Set var26 = this.state.parrySucceeded;
        UUID var37 = this.caster.getUniqueId();
        var26.remove(var37);
        this.cancel();
    }
}
