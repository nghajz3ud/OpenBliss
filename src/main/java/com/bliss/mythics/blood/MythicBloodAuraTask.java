package com.bliss.mythics.blood;

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

public final class MythicBloodAuraTask extends BukkitRunnable {
    private static final double LOW_HP_ON = 0.35;
    private static final double LOW_HP_OFF = 0.4;
    private final Plugin plugin;
    private final MythicBloodState state;
    private int pass = 0;

    public MythicBloodAuraTask(Plugin var1, MythicBloodState var2, int var3) {
        this.plugin = var1;
        this.state = var2;
    }

    public void start(int var1) {
        Plugin var9 = this.plugin;
        this.runTaskTimer(var9, 20L, 5L);
    }

    public void run() {
        boolean var56;
        if (this.pass % 4 == 0) {
            var56 = true;
        } else {
            var56 = false;
        }

        boolean var4 = var56;
        System.currentTimeMillis();

        for (Player var8 : this.plugin.getServer().getOnlinePlayers()) {
            ItemStack var9 = var8.getInventory().getItemInMainHand();
            ItemStack var10 = var8.getInventory().getItemInOffHand();
            boolean var55;
            if (!MythicItems.isMythicBloodGem(var9, 824514243) && !MythicItems.isMythicBloodGem(var10, 824514243)) {
                var55 = false;
            } else {
                var55 = true;
            }

            if (!var55) {
                Set var53 = this.state.strengthII;
                UUID var65 = var8.getUniqueId();
                var53.remove(var65);
            } else if (MythicGate.disabledInSpawn(var8, 821632949)) {
                Set var48 = this.state.strengthII;
                UUID var62 = var8.getUniqueId();
                var48.remove(var62);
                this.showHud(var8, 882965864);
            } else {
                if (var4 && var55) {
                    this.applyEnduringStrength(var8, 1179044012);
                }

                this.showHud(var8, 882965864);
            }
        }

        int var58 = this.pass + 1;
        this.pass = var58;
    }

    private void applyEnduringStrength(Player var1, int var2) {
        Attribute var4 = Attribute.MAX_HEALTH;
        AttributeInstance var12 = var1.getAttribute(var4);
        double var54;
        if (var12 != null) {
            var54 = var12.getValue();
        } else {
            var54 = 20.0;
        }

        double var51;
        if (var54 > 0.0) {
            double var49 = var1.getHealth();
            var51 = var49 / var54;
        } else {
            var51 = 1.0;
        }

        Set var25 = this.state.strengthII;
        UUID var57 = var1.getUniqueId();
        boolean var17 = var25.contains(var57);
        if (var17 && var51 > 0.4) {
            var17 = false;
        } else if (!var17 && var51 <= 0.35) {
            var17 = true;
        }

        if (var17) {
            Set var35 = this.state.strengthII;
            UUID var59 = var1.getUniqueId();
            var35.add(var59);
        } else {
            Set var41 = this.state.strengthII;
            UUID var62 = var1.getUniqueId();
            var41.remove(var62);
        }

        PotionEffectType var7 = PotionEffectType.STRENGTH;
        byte var69;
        if (var17) {
            var69 = 1;
        } else {
            var69 = 0;
        }

        PotionEffect var60 = new PotionEffect(var7, 40, var69, false, false);
        var1.addPotionEffect(var60);
    }

    private void showHud(Player var1, int var2) {
        long var9 = System.currentTimeMillis();
        UUID var11 = var1.getUniqueId();
        MythicBloodState var24 = this.state;
        int var13 = var24.sawsAvailable(var11, 2, var9, 1478640902);
        String var27;
        if (var13 <= 0) {
            var27 = "&c";
        } else if (var13 >= 2) {
            var27 = "&a";
        } else {
            var27 = "&6";
        }

        String var28 = "&c\ud83e\udd1f";
        String var70 = "&f";
        String var15 = Text.iconCount(var28, var13, var70, 1112509315);
        MythicBloodState var31 = this.state;
        long var32 = var31.nextSawReady(var11, 184977075);
        int var16 = secondsUntil(var32, var9, 1681210041);
        Map var37 = this.state.ritualReadyAt;
        Long var39 = (Long)var37.get(var11);
        int var17 = secondsUntil(var39, var9, 2056550935);
        Map var43 = this.state.fountainReadyAt;
        Long var45 = (Long)var43.get(var11);
        int var18 = secondsUntil(var45, var9, 2056550935);
        String var69;
        if (var16 <= 0) {
            var69 = var27 + "Ready";
        } else {
            var69 = var27 + var16 + "s";
        }

        String var75 = cdLabel(var17, 70983366);
        String var77 = cdLabel(var18, 70983366);
        String var19 = "&8❰ " + var15 + " " + var69 + " &8| " + var75 + " &8| &c\ud83e\udd0c " + var77 + " &8❱";
        if (!AssessmentWindowBarTask.isShowing(1134156722) && !MotifSpeechListener.isSpeakingTo(var1.getUniqueId(), 1524431478)) {
            Component var66 = Text.legacy(var19, 883008065);
            var1.sendActionBar(var66);
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
                long var12 = var0;
                return (int)((var12 - var1 + 999L) / 1000L);
            }
        }

        return 0;
    }

    private static int secondsUntil(long var0, long var2, int var4) {
        int var10;
        if (var0 <= var2) {
            var10 = 0;
        } else {
            var10 = (int)((var0 - var2 + 999L) / 1000L);
        }

        return var10;
    }
}
