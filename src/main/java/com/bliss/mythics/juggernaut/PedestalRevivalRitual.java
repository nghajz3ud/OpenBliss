package com.bliss.mythics.juggernaut;

import com.bliss.core.Particles;
import com.bliss.mythics.util.Text;
import java.util.UUID;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class PedestalRevivalRitual extends BukkitRunnable {
    private static final int RISE_TICKS = 70;
    private static final int CINEMATIC_TICKS = 400;
    private static final int RITUAL_TICKS = 470;
    private static final double FLOAT_HEIGHT = 8.0;
    private static final double GEM_BASE_LIFT = 1.0;
    private static final float SPIN_PER_TICK = 3.0F;
    private final Plugin plugin;
    private final JuggernautManager mgr;
    private final Location anchor;
    private final Item gem;
    private final boolean auratus;
    private final UUID reviveTarget;
    private ItemDisplay floater;
    private ItemStack gemStack;
    private int t = 0;
    private boolean cinematicStarted = false;
    private static PedestalRevivalRitual ACTIVE;

    public PedestalRevivalRitual(Plugin var1, JuggernautManager var2, Location var3, Item var4, boolean var5, UUID var6, int var7) {
        this.plugin = var1;
        this.mgr = var2;
        Location var22 = var3.clone();
        this.anchor = var22;
        this.gem = var4;
        this.auratus = var5;
        this.reviveTarget = var6;
    }

    public void start(int var1) {
        World var7 = this.anchor.getWorld();
        if (var7 != null) {
            ACTIVE = this;
            JuggernautManager var13 = this.mgr;
            UUID var18 = this.reviveTarget;
            var13.setRevivalInFlight(var18, 956471003);
            this.spawnFloater(283130369);
            Location var22 = this.anchor;
            this.bigBurst(var7, var22, 40, false, 674960765);
            Plugin var21 = this.plugin;
            this.runTaskTimer(var21, 1L, 1L);
        }
    }

    public static void finishActive(int var0) {
        PedestalRevivalRitual var3 = ACTIVE;
        if (var3 != null) {
            try {
                var3.cancel();
            } catch (IllegalStateException var4) {
            }

            var3.completeNow(false, 2122486343);
        }
    }

    public void run() {
        if (this.t >= 470) {
            this.endRitual(1139186343);
            this.cancel();
        } else {
            int var58 = this.t + 1;
            this.t = var58;
            if (this.floater != null && this.floater.isValid()) {
                double var76 = (double)this.t / 70.0;
                double var93 = Math.min(1.0, var76);
                double var80 = 8.0 * var93;
                double var13 = 1.0 + var80;
                Location var33 = this.anchor.clone();
                Location var15 = var33.add(0.0, var13, 0.0);
                float var84 = (float)this.t;
                float var63 = 3.0F * var84 % 360.0F;
                var15.setYaw(var63);
                var15.setPitch(0.0F);
                ItemDisplay var38 = this.floater;
                var38.teleport(var15);
            }

            if (!this.cinematicStarted && this.t >= 70) {
                this.cinematicStarted = true;
                World var101 = this.anchor.getWorld();
                if (var101 != null) {
                    Location var69 = this.anchor;
                    Sound var86 = Sound.BLOCK_BEACON_ACTIVATE;
                    var101.playSound(var69, var86, 6.0F, 1.2F);
                }

                Location var12 = this.anchor.clone().add(0.0, 9.0, 0.0);
                Plugin var88 = this.plugin;
                boolean var100 = this.auratus;
                SummoningCinematic var54 = new SummoningCinematic(var88, var12, true, var100, 92858397);
                var54.start(898909888);
            }
        }
    }

    private void spawnFloater(int var1) {
        if (this.gem != null) {
            World var10 = this.anchor.getWorld();
            if (var10 != null) {
                ItemStack var11 = this.gem.getItemStack().clone();
                this.gemStack = var11;
                this.gem.remove();
                Location var12 = this.anchor.clone().add(0.0, 1.0, 0.0);
                var12.setYaw(0.0F);
                var12.setPitch(0.0F);
                Consumer<ItemDisplay> var40 = var1x -> {
                    var1x.setPersistent(false);
                    var1x.setItemStack(var11);
                    ItemDisplayTransform var8 = ItemDisplayTransform.GROUND;
                    var1x.setItemDisplayTransform(var8);
                    var1x.setTeleportDuration(1);
                };
                ItemDisplay var38 = (ItemDisplay)var10.spawn(var12, ItemDisplay.class, var40);
                this.floater = var38;
            }
        }
    }

    private void endRitual(int var1) {
        this.completeNow(true, 2122486343);
    }

    private void completeNow(boolean var1, int var2) {
        World var10 = this.anchor.getWorld();
        if (this.gem != null && this.gem.isValid()) {
            this.gem.remove();
        }

        if (this.floater != null && this.floater.isValid()) {
            this.floater.remove();
        }

        if (var1 && var10 != null) {
            Location var62 = this.anchor;
            this.bigBurst(var10, var62, 120, true, 674960765);
            Location var54 = this.anchor;
            Sound var63 = Sound.ITEM_TOTEM_USE;
            var10.playSound(var54, var63, 8.0F, 1.0F);
        }

        label40: {
            if (this.reviveTarget != null) {
                JuggernautManager var35 = this.mgr;
                UUID var56 = this.reviveTarget;
                if (var35.revive(var56, 823653800)) {
                    Bukkit.broadcast(Text.legacy("<##FFD773>&l" + nameOf(this.reviveTarget, 2121824601) + " &7has been revived at the pedestal.", 883008065));
                    break label40;
                }
            }

            if (this.gemStack != null && var10 != null) {
                Location var60 = this.anchor.clone().add(0.0, 1.0, 0.0);
                ItemStack var67 = this.gemStack;
                var10.dropItemNaturally(var60, var67);
            }
        }

        this.mgr.clearRevivalInFlight(1745092150);
        PedestalRevivalRitual var44 = ACTIVE;
        if (var44 == this) {
            ACTIVE = null;
        }
    }

    private static String nameOf(UUID var0, int var1) {
        OfflinePlayer var3 = Bukkit.getOfflinePlayer(var0);
        String var8;
        if (var3.getName() != null) {
            var8 = var3.getName();
        } else {
            var8 = "A Juggernaut";
        }

        return var8;
    }

    private void bigBurst(final World var1, final Location var2, final int var3, final boolean var4, int var5) {
        final Location var19 = var2.clone().add(0.0, 2.0, 0.0);
        Sound var9 = Sound.ITEM_TOTEM_USE;
        var1.playSound(var2, var9, 8.0F, 0.8F);
        Sound var37 = Sound.BLOCK_BEACON_POWER_SELECT;
        var1.playSound(var2, var37, 10.0F, 0.7F);
        Sound var38 = Sound.ENTITY_ILLUSIONER_CAST_SPELL;
        var1.playSound(var2, var38, 8.0F, 0.6F);
        final double var30;
        if (var4) {
            var30 = 0.5;
        } else {
            var30 = 1.5;
        }

        BukkitRunnable var28 = new BukkitRunnable() {
            int b = 0;

            public void run() {
                if (this.b >= 60) {
                    this.cancel();
                } else {
                    if (this.b % 4 == 0) {
                        World var26 = var1;
                        Location var48 = var19;
                        byte var70;
                        if (var4) {
                            var70 = 2;
                        } else {
                            var70 = 1;
                        }

                        PedestalRevivalRitual.flash(var26, var48, var70, 828283707);
                    }

                    World var28 = var1;
                    Particle var49 = Particle.FIREWORK;
                    Location var58 = var19;
                    int var71 = var3;
                    Particles.spawn(var28, var49, var58, var71, 8.0, 6.0, 8.0, 0.5, null, true, 1748031581);
                    double var19x = 0.0;

                    while (var19x <= 10.0) {
                        World var40 = var1;
                        Particle var55 = Particle.END_ROD;
                        Location var65 = var2.clone();
                        Location var66 = var65.add(0.0, var19x, 0.0);
                        Particles.spawn(var40, var55, var66, 1, 0.05, 0.05, 0.05, 0.0, null, true, 1748031581);
                        double var68 = var30;
                        var19x += var68;
                    }

                    if (this.b == 30) {
                        World var37 = var1;
                        Location var52 = var2;
                        Sound var61 = Sound.ENTITY_GENERIC_EXPLODE;
                        var37.playSound(var52, var61, 8.0F, 0.6F);
                    }

                    int var54 = this.b + 1;
                    this.b = var54;
                }
            }
        };
        Plugin var36 = this.plugin;
        var28.runTaskTimer(var36, 0L, 1L);
    }

    private static void flash(World var0, Location var1, int var2, int var3) {
        try {
            Particle var5 = Particle.FLASH;
            Color var16 = Color.WHITE;
            Particles.spawn(var0, var5, var1, var2, 5.0, 5.0, 5.0, 0.0, var16, true, 1748031581);
        } catch (Throwable var27) {
            try {
                Particle var23 = Particle.FLASH;
                Particles.spawn(var0, var23, var1, var2, 5.0, 5.0, 5.0, 0.0, null, true, 1748031581);
            } catch (Throwable var26) {
            }
        }
    }
}
