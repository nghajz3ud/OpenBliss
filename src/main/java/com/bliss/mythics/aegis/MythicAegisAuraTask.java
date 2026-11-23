package com.bliss.mythics.aegis;

import com.bliss.entity.MotifSpeechListener;
import com.bliss.honored.AssessmentWindowBarTask;
import com.bliss.mythics.MythicItems;
import com.bliss.mythics.util.MythicGate;
import com.bliss.mythics.util.Text;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public final class MythicAegisAuraTask extends BukkitRunnable {
    private static final int PERIOD = 5;
    private static final double ENTITY_REACH_DEFAULT = 3.0;
    private static final double ENTITY_REACH_BOOST = 5.0;
    private static final double BLOCK_REACH_DEFAULT = 4.5;
    private static final double BLOCK_REACH_BOOST = 8.0;
    private static final PotionEffectType[] NEGATIVE;
    private final Plugin plugin;
    private final MythicAegisState state;

    public MythicAegisAuraTask(Plugin var1, MythicAegisState var2, int var3) {
        this.plugin = var1;
        this.state = var2;
    }

    public void start(int var1) {
        Plugin var9 = this.plugin;
        this.runTaskTimer(var9, 20L, 5L);
    }

    public void run() {
        long var6 = System.currentTimeMillis();

        for (Player var9 : this.plugin.getServer().getOnlinePlayers()) {
            var9.getUniqueId();
            ItemStack var11 = var9.getInventory().getItemInMainHand();
            ItemStack var12 = var9.getInventory().getItemInOffHand();
            boolean var46;
            if (!MythicItems.isMythicAegisGem(var11, 73386651) && !MythicItems.isMythicAegisGem(var12, 73386651)) {
                var46 = false;
            } else {
                var46 = true;
            }

            if (!var46) {
                this.clearReach(var9, 365505338);
            } else if (MythicGate.disabledInSpawn(var9, 821632949)) {
                this.clearReach(var9, 365505338);
                if (var46) {
                    this.showHud(var9, var6, 1880712899);
                }
            } else {
                this.divinePurity(var9, 230284104);
                this.haulingStrikeAura(var9, 114959662);
                this.showHud(var9, var6, 1880712899);
            }
        }
    }

    private void divinePurity(Player var1, int var2) {
        for (PotionEffectType var8 : NEGATIVE) {
            if (var1.hasPotionEffect(var8)) {
                var1.removePotionEffect(var8);
            }
        }
    }

    private void haulingStrikeAura(Player var1, int var2) {
        if (var1.isSneaking()) {
            PotionEffectType var5 = PotionEffectType.HASTE;
            PotionEffect var15 = new PotionEffect(var5, 20, 1, false, false);
            var1.addPotionEffect(var15);
            this.setReach(var1, 5.0, 8.0, 1597161196);
        } else {
            this.clearReach(var1, 365505338);
        }
    }

    private void setReach(Player var1, double var2, double var4, int var6) {
        Attribute var8 = Attribute.ENTITY_INTERACTION_RANGE;
        AttributeInstance var11 = var1.getAttribute(var8);
        if (var11 != null) {
            double var16 = var11.getBaseValue();
            if (var16 != var2) {
                var11.setBaseValue(var2);
            }
        }

        Attribute var34 = Attribute.BLOCK_INTERACTION_RANGE;
        AttributeInstance var12 = var1.getAttribute(var34);
        if (var12 != null) {
            double var24 = var12.getBaseValue();
            if (var24 != var4) {
                var12.setBaseValue(var4);
            }
        }

        Set var30 = this.state.reachBoosted;
        UUID var38 = var1.getUniqueId();
        var30.add(var38);
    }

    private void clearReach(Player var1, int var2) {
        Set var6 = this.state.reachBoosted;
        UUID var9 = var1.getUniqueId();
        if (var6.remove(var9)) {
            resetReach(var1, 1923315323);
        }
    }

    public static void resetReach(Player var0, int var1) {
        Attribute var3 = Attribute.ENTITY_INTERACTION_RANGE;
        AttributeInstance var6 = var0.getAttribute(var3);
        if (var6 != null && var6.getBaseValue() != 3.0) {
            var6.setBaseValue(3.0);
        }

        Attribute var25 = Attribute.BLOCK_INTERACTION_RANGE;
        AttributeInstance var7 = var0.getAttribute(var25);
        if (var7 != null && var7.getBaseValue() != 4.5) {
            var7.setBaseValue(4.5);
        }
    }

    private void showHud(Player var1, long var2, int var4) {
        UUID var10 = var1.getUniqueId();
        MythicAegisState var22 = this.state;
        int var12 = var22.chainsAvailable(var10, 2, var2, 1870480623);
        String var25;
        if (var12 <= 0) {
            var25 = "&c";
        } else if (var12 >= 2) {
            var25 = "&a";
        } else {
            var25 = "&6";
        }

        String var26 = "<##FFE9A8>⊵";
        String var68 = "&f";
        String var14 = Text.iconCount(var26, var12, var68, 1112509315);
        MythicAegisState var29 = this.state;
        long var30 = var29.nextChainReady(var10, 343884876);
        int var15 = secondsUntil(var30, var2, 1125371665);
        Map var35 = this.state.dashReadyAt;
        Long var37 = (Long)var35.get(var10);
        int var16 = secondsUntil(var37, var2, 1381523371);
        Map var41 = this.state.angelsReadyAt;
        Long var43 = (Long)var41.get(var10);
        int var17 = secondsUntil(var43, var2, 1381523371);
        String var67;
        if (var15 <= 0) {
            var67 = var25 + "Ready";
        } else {
            var67 = var25 + var15 + "s";
        }

        String var73 = cdLabel(var17, 833731200);
        String var75 = cdLabel(var16, 833731200);
        String var18 = "&8❰ " + var14 + " " + var67 + " &8| " + var73 + " &8| <##FFE9A8>⊴ " + var75 + " &8❱";
        if (!AssessmentWindowBarTask.isShowing(1134156722) && !MotifSpeechListener.isSpeakingTo(var1.getUniqueId(), 1524431478)) {
            Component var64 = Text.legacy(var18, 883008065);
            var1.sendActionBar(var64);
        }
    }

    private static String cdLabel(int var0, int var1) {
        String var4;
        if (var0 <= 0) {
            var4 = "&aReady";
        } else {
            var4 = "&e" + var0 + "s";
        }

        return var4;
    }

    private static int secondsUntil(Long var0, long var1, int var3) {
        if (var0 != null) {
            long var8 = var0;
            if (var8 > var1) {
                long var13 = var0;
                return (int)((var13 - var1 + 999L) / 1000L);
            }
        }

        return 0;
    }

    private static int secondsUntil(long var0, long var2, int var4) {
        int var18;
        if (var0 <= var2) {
            var18 = 0;
        } else {
            var18 = (int)((var0 - var2 + 999L) / 1000L);
        }

        return var18;
    }

    static {
        PotionEffectType[] var3 = new PotionEffectType[15];
        PotionEffectType var2 = PotionEffectType.SLOWNESS;
        var3[0] = var2;
        PotionEffectType var18 = PotionEffectType.MINING_FATIGUE;
        var3[1] = var18;
        PotionEffectType var19 = PotionEffectType.NAUSEA;
        var3[2] = var19;
        PotionEffectType var20 = PotionEffectType.BLINDNESS;
        var3[3] = var20;
        PotionEffectType var21 = PotionEffectType.HUNGER;
        var3[4] = var21;
        PotionEffectType var22 = PotionEffectType.WEAKNESS;
        var3[5] = var22;
        PotionEffectType var23 = PotionEffectType.POISON;
        var3[6] = var23;
        PotionEffectType var24 = PotionEffectType.WITHER;
        var3[7] = var24;
        PotionEffectType var25 = PotionEffectType.DARKNESS;
        var3[8] = var25;
        PotionEffectType var26 = PotionEffectType.UNLUCK;
        var3[9] = var26;
        PotionEffectType var27 = PotionEffectType.BAD_OMEN;
        var3[10] = var27;
        PotionEffectType var28 = PotionEffectType.LEVITATION;
        var3[11] = var28;
        PotionEffectType var29 = PotionEffectType.WIND_CHARGED;
        var3[12] = var29;
        PotionEffectType var30 = PotionEffectType.OOZING;
        var3[13] = var30;
        PotionEffectType var31 = PotionEffectType.INFESTED;
        var3[14] = var31;
        NEGATIVE = var3;
    }
}
