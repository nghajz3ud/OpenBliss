package com.bliss.dragongames;

import com.bliss.BlissPlugin;
import com.bliss.goldgem.FragmentCoreRitual;
import com.bliss.villager.Text;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.bossbar.BossBar.Color;
import net.kyori.adventure.bossbar.BossBar.Overlay;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractSkeleton;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public final class BloodMoon implements Listener {
    static final long BLOOD_DAY = 80018L;
    static final long BLOOD_TIME = 1920450000L;
    static final long BAR_DURATION_MS = 3600000L;
    private static final String TITLE = "<##FF2A44>&l♠ <##7A0C16>&lTh<##98101F>&le <##B61329>&lBl<##D31434>&loo<##EE1C3E>&ld <##D31434>&lMo<##B61329>&lon <##FF2A44>&l♠";
    private final BlissPlugin plugin;
    private final DragonGamesManager games;
    private BossBar bar;

    BloodMoon(BlissPlugin var1, DragonGamesManager var2, int var3) {
        this.plugin = var1;
        this.games = var2;
    }

    public boolean active(int var1) {
        boolean var9;
        if (this.games.bloodMoonSavedTime(559715134) >= 0L) {
            var9 = true;
        } else {
            var9 = false;
        }

        return var9;
    }

    private static World world(int var0) {
        return (World)Bukkit.getWorlds().get(0);
    }

    void begin(int var1) {
        World var10 = world(401214569);
        FragmentCoreRitual var11 = this.plugin.fragmentCoreRitual(234148762);
        boolean var46;
        if (var11 != null && var11.clockHeld(150384835)) {
            var46 = true;
        } else {
            var46 = false;
        }

        if (!this.active(687437596)) {
            long var59;
            boolean var61;
            if (var46) {
                var59 = var11.heldPrevFull(807109741);
                var61 = var11.heldPrevCycle(246945090);
            } else {
                var59 = var10.getFullTime();
                GameRule var3 = GameRule.DO_DAYLIGHT_CYCLE;
                Boolean var13 = (Boolean)var10.getGameRuleValue(var3);
                boolean var47;
                if (var13 != null && !var13) {
                    var47 = false;
                } else {
                    var47 = true;
                }

                var61 = var47;
            }

            DragonGamesManager var33 = this.games;
            long var57 = System.currentTimeMillis() + 3600000L;
            var33.setBloodMoonState(var59, var61, var57, 1261509556);
        }

        if (var46) {
            var11.overrideSavedClock(1920450000L, false, 803195832);
        } else {
            GameRule var52 = GameRule.DO_DAYLIGHT_CYCLE;
            Boolean var55 = false;
            var10.setGameRule(var52, var55);
            var10.setFullTime(1920450000L);
        }

        clearWeather(var10, 543660288);
        this.showAll(917876477);
    }

    private static void clearWeather(World var0, int var1) {
        if (var0.hasStorm()) {
            var0.setStorm(false);
        }

        if (var0.isThundering()) {
            var0.setThundering(false);
        }

        var0.setClearWeatherDuration(12000);
    }

    void resume(int var1) {
        if (this.active(687437596)) {
            this.begin(327297774);
        }
    }

    void end(int var1) {
        if (this.active(687437596)) {
            World var8 = world(401214569);
            FragmentCoreRitual var9 = this.plugin.fragmentCoreRitual(234148762);
            if (var9 != null && var9.clockHeld(150384835)) {
                long var27 = this.games.bloodMoonSavedTime(559715134);
                boolean var41 = this.games.bloodMoonSavedCycle(1798818573);
                var9.overrideSavedClock(var27, var41, 803195832);
            } else {
                GameRule var32 = GameRule.DO_DAYLIGHT_CYCLE;
                Boolean var39 = this.games.bloodMoonSavedCycle(1798818573);
                var8.setGameRule(var32, var39);
                long var35 = this.games.bloodMoonSavedTime(559715134);
                var8.setFullTime(var35);
            }

            this.games.setBloodMoonState(-1L, true, 0L, 1261509556);
            var8.setClearWeatherDuration(0);
            this.hideAll(1613838872);
        }
    }

    void tick(int var1) {
        if (this.active(687437596)) {
            BossBar var7 = this.bar(1521255547);
            float var17 = this.progressNow(2053781092);
            var7.progress(var17);
            World var4 = world(401214569);
            if (var4.hasStorm() || var4.isThundering() || var4.getClearWeatherDuration() < 1200) {
                clearWeather(var4, 543660288);
            }
        }
    }

    void stop(int var1) {
        this.hideAll(1613838872);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent var1) {
        if (this.active(687437596)) {
            Player var6 = var1.getPlayer();
            BossBar var7 = this.bar(1521255547);
            var6.showBossBar(var7);
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onSpawn(CreatureSpawnEvent var1) {
        if (this.active(687437596)) {
            SpawnReason var11 = var1.getSpawnReason();
            SpawnReason var3 = SpawnReason.CUSTOM;
            if (var11 != var3) {
                World var12 = world(401214569);
                World var46 = var1.getLocation().getWorld();
                if (var12.equals(var46)) {
                    LivingEntity var5 = var1.getEntity();
                    if (var5 instanceof Creeper) {
                        ((Creeper)var5).setPowered(true);
                    } else if (var5 instanceof Zombie) {
                        EntityEquipment var56 = ((Zombie)var5).getEquipment();
                        if (var56 != null) {
                            ItemStack var51 = maxedSword(2063502789);
                            var56.setItemInMainHand(var51);
                            var56.setItemInMainHandDropChance(0.0F);
                        }
                    } else {
                        if (var5 instanceof AbstractSkeleton) {
                            EntityEquipment var7 = ((AbstractSkeleton)var5).getEquipment();
                            if (var7 == null) {
                                return;
                            }

                            ItemStack var8 = var7.getItemInMainHand();
                            Material var30 = var8.getType();
                            Material var47 = Material.BOW;
                            if (var30 != var47) {
                                return;
                            }

                            Enchantment var48 = Enchantment.POWER;
                            var8.addUnsafeEnchantment(var48, 2);
                            var7.setItemInMainHand(var8);
                            var7.setItemInMainHandDropChance(0.0F);
                        }
                    }
                }
            }
        }
    }

    private static ItemStack maxedSword(int var0) {
        Material var2 = Material.DIAMOND_SWORD;
        ItemStack var1 = new ItemStack(var2);
        Enchantment var12 = Enchantment.SHARPNESS;
        var1.addUnsafeEnchantment(var12, 5);
        Enchantment var13 = Enchantment.FIRE_ASPECT;
        var1.addUnsafeEnchantment(var13, 2);
        Enchantment var14 = Enchantment.KNOCKBACK;
        var1.addUnsafeEnchantment(var14, 2);
        Enchantment var15 = Enchantment.SWEEPING_EDGE;
        var1.addUnsafeEnchantment(var15, 3);
        Enchantment var16 = Enchantment.LOOTING;
        var1.addUnsafeEnchantment(var16, 3);
        Enchantment var17 = Enchantment.UNBREAKING;
        var1.addUnsafeEnchantment(var17, 3);
        Enchantment var18 = Enchantment.MENDING;
        var1.addUnsafeEnchantment(var18, 1);
        return var1;
    }

    private float progressNow(int var1) {
        long var10 = this.games.bloodMoonBarEnd(732436942);
        long var3 = System.currentTimeMillis();
        long var7 = var10 - var3;
        if (var7 <= 0L) {
            return 0.0F;
        } else {
            double var29 = (double)var7 / 3600000.0;
            return (float)Math.min(1.0, var29);
        }
    }

    private BossBar bar(int var1) {
        if (this.bar == null) {
            Component var11 = Text.legacy(
                "<##FF2A44>&l♠ <##7A0C16>&lTh<##98101F>&le <##B61329>&lBl<##D31434>&loo<##EE1C3E>&ld <##D31434>&lMo<##B61329>&lon <##FF2A44>&l♠", 373192400
            );
            Color var5 = Color.RED;
            Overlay var6 = Overlay.PROGRESS;
            BossBar var12 = BossBar.bossBar(var11, 1.0F, var5, var6);
            this.bar = var12;
        }

        return this.bar;
    }

    private void showAll(int var1) {
        BossBar var4 = this.bar(1521255547);
        float var18 = this.progressNow(2053781092);
        var4.progress(var18);

        for (Player var17 : Bukkit.getOnlinePlayers()) {
            var17.showBossBar(var4);
        }
    }

    private void hideAll(int var1) {
        if (this.bar != null) {
            for (Player var14 : Bukkit.getOnlinePlayers()) {
                BossBar var15 = this.bar;
                var14.hideBossBar(var15);
            }
        }
    }
}
