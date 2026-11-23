package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.AstraSoulStore;
import com.bliss.core.GemType;
import com.bliss.core.PlayerDataStore;
import com.bliss.entity.MotifSpeechListener;
import com.bliss.goldgem.GoldGem;
import com.bliss.honored.AssessmentWindowBarTask;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class AstraAuraTask {
    private static final long PERIOD_TICKS = 5L;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final AstraGemListener astra;
    private final AstraSoulStore souls;
    private BukkitTask task;

    public AstraAuraTask(BlissPlugin var1, PlayerDataStore var2, AstraGemListener var3, AstraSoulStore var4, int var5) {
        this.plugin = var1;
        this.players = var2;
        this.astra = var3;
        this.souls = var4;
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
        for (Player var6 : Bukkit.getOnlinePlayers()) {
            UUID var7 = var6.getUniqueId();
            ItemStack var8 = var6.getInventory().getItemInMainHand();
            ItemStack var9 = var6.getInventory().getItemInOffHand();
            int var29 = BlissCoreItems.gemIdOf(var8, 382441079);
            int var60 = GemType.ASTRA.id();
            boolean var59;
            if (var29 == var60) {
                var59 = true;
            } else {
                var59 = false;
            }

            int var31 = BlissCoreItems.gemIdOf(var9, 382441079);
            int var62 = GemType.ASTRA.id();
            boolean var58;
            if (var31 == var62) {
                var58 = true;
            } else {
                var58 = false;
            }

            GemType var63 = GemType.ASTRA;
            if (GoldGem.isWieldingSoul(var6, var63, 100010596)) {
                if (!AssessmentWindowBarTask.isShowing(1134156722) && !MotifSpeechListener.isSpeakingTo(var6.getUniqueId(), 1524431478)) {
                    Component var76 = Text.legacy(this.buildActionBar(2, var7, 849068905), 373192400);
                    var6.sendActionBar(var76);
                }
            } else if (var59 || var58) {
                PlayerDataStore var37 = this.players;
                if (var37.get(var7, 1264010596).energy(1160853318) > 1) {
                    ItemStack var42;
                    if (var59) {
                        var42 = var8;
                    } else {
                        var42 = var9;
                    }

                    int var13 = BlissCoreItems.tierOf(var42, 1633828214);
                    PlayerDataStore var45 = this.players;
                    UUID var67 = var6.getUniqueId();
                    if (var45.get(var67, 1264010596).energy(1160853318) >= 2) {
                        PlayerDataStore var49 = this.players;
                        UUID var70 = var6.getUniqueId();
                        if (!var49.get(var70, 1264010596).hudSuppressed(859110487)) {
                            Component var73 = Text.legacy(this.buildActionBar(var13, var7, 849068905), 373192400);
                            var6.sendActionBar(var73);
                        }
                    }
                }
            }
        }
    }

    private String buildActionBar(int var1, UUID var2, int var3) {
        String var9 = this.soulNames(var2, 1697377695);
        if (var1 == 2) {
            Map var14 = this.astra.projections;
            if (var14.containsKey(var2)) {
                return "\ud83e\uddca &bSPOOK  &5\ud83d\udd2e" + var9 + "   &r\ud83c\udf1f &bTAG";
            } else {
                Map var18 = this.astra.cd2;
                String var19 = timer(var18, var2, 266321803);
                Map var33 = this.astra.driftActiveUntil;
                Map var40 = this.astra.cd1;
                String var34 = activeOrTimer(var33, var40, var2, 442214003);
                Map var45 = this.astra.cd3;
                String var46 = timer(var45, var2, 266321803);
                return "\ud83d\udd2a &b" + var19 + "  &5\ud83d\udd2e &b" + var34 + var9 + "  &f\ud83d\udc7b &b" + var46;
            }
        } else {
            Map var25 = this.astra.driftActiveUntil;
            Map var37 = this.astra.cd1;
            String var26 = activeOrTimer(var25, var37, var2, 442214003);
            return "&5\ud83d\udd3a &b" + var26 + var9;
        }
    }

    private static String activeOrTimer(Map<UUID, Long> var0, Map<UUID, Long> var1, UUID var2, int var3) {
        Long var8 = (Long)var0.get(var2);
        if (var8 != null) {
            long var13 = var8;
            long var6 = System.currentTimeMillis();
            if (var13 > var6) {
                return "&cActive..";
            }
        }

        return timer(var1, var2, 266321803);
    }

    private String soulNames(UUID var1, int var2) {
        AstraSoulStore var10 = this.souls;
        List<AstraSoulStore.Soul> var6 = var10.snapshot(var1, 445866519);
        if (var6.isEmpty()) {
            return "";
        } else {
            String var5 = ", ";
            StringJoiner var14 = new StringJoiner(var5);
            StringJoiner var7 = var14;

            for (AstraSoulStore.Soul var9 : var6) {
                String var30 = prettyEntityName(var9.type(), 995107730);
                var7.add(var30);
            }

            return " <##A01FFF>" + var7;
        }
    }

    private static String prettyEntityName(EntityType var0, int var1) {
        String var5 = var0.name().toLowerCase().replace('_', ' ');
        int var38 = var5.length();
        StringBuilder var15 = new StringBuilder(var38);
        StringBuilder var6 = var15;
        boolean var7 = true;

        for (char var11 : var5.toCharArray()) {
            char var36;
            if (var7) {
                var36 = Character.toUpperCase(var11);
            } else {
                var36 = var11;
            }

            var6.append(var36);
            boolean var28;
            if (var11 == ' ') {
                var28 = true;
            } else {
                var28 = false;
            }

            var7 = var28;
        }

        return var6.toString();
    }

    private static String timer(Map<UUID, Long> var0, UUID var1, int var2) {
        Long var7 = (Long)var0.get(var1);
        if (var7 == null) {
            return "&aReady!";
        } else {
            long var8 = System.currentTimeMillis();
            long var19 = var7;
            if (var19 <= var8) {
                return "&aReady!";
            } else {
                long var24 = var7;
                int var10 = (int)((var24 - var8 + 999L) / 1000L);
                int var11 = var10 / 60;
                int var12 = var10 % 60;
                if (var11 == 0) {
                    return var12 + "s";
                } else {
                    return var12 == 0 ? var11 + "m" : var11 + "m " + var12 + "s";
                }
            }
        }
    }
}
