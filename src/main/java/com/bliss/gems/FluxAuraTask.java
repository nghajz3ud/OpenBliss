package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.entity.MotifSpeechListener;
import com.bliss.goldgem.GoldGem;
import com.bliss.honored.AssessmentWindowBarTask;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class FluxAuraTask {
    private static final long PERIOD_TICKS = 5L;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final FluxGemListener flux;
    private BukkitTask task;

    public FluxAuraTask(BlissPlugin var1, PlayerDataStore var2, FluxGemListener var3, int var4) {
        this.plugin = var1;
        this.players = var2;
        this.flux = var3;
    }

    public void start(int var1) {
        BukkitScheduler var3 = Bukkit.getScheduler();
        BlissPlugin var11 = this.plugin;
        Runnable var12 = this::tick;
        BukkitTask var10 = var3.runTaskTimer(var11, var12, 5L, 5L);
        this.task = var10;
    }

    public void stop() {
        if (this.task != null) {
            this.task.cancel();
        }
    }

    private void tick() {
        for (Player var8 : Bukkit.getOnlinePlayers()) {
            UUID var9 = var8.getUniqueId();
            ItemStack var10 = var8.getInventory().getItemInMainHand();
            ItemStack var11 = var8.getInventory().getItemInOffHand();
            int var12 = BlissCoreItems.gemIdOf(var10, 382441079);
            int var13 = BlissCoreItems.gemIdOf(var11, 382441079);
            int var147 = GemType.FLUX.id();
            boolean var46;
            if (var12 == var147) {
                var46 = true;
            } else {
                var46 = false;
            }

            int var149 = GemType.FLUX.id();
            boolean var146;
            if (var13 == var149) {
                var146 = true;
            } else {
                var146 = false;
            }

            GemType var150 = GemType.FLUX;
            boolean var16 = GoldGem.hasAbsorbedSoul(var8, var150, 1603837876);
            if (var16) {
                PlayerDataStore var52 = this.players;
                PlayerData var17 = var52.get(var9, 1264010596);
                if (var17.canUsePassives(2051432579) && var17.energy(1160853318) > 5) {
                    String var58 = "flux";
                    String var153 = "tireless";
                    if (GemTuning.passive(var58, var153, true, 14309468)) {
                        PotionEffectType var154 = PotionEffectType.SLOWNESS;
                        var8.removePotionEffect(var154);
                        PotionEffectType var155 = PotionEffectType.HUNGER;
                        var8.removePotionEffect(var155);
                        PotionEffectType var156 = PotionEffectType.WEAKNESS;
                        var8.removePotionEffect(var156);
                    }
                }
            }

            GemType var157 = GemType.FLUX;
            if (GoldGem.isWieldingSoul(var8, var157, 100010596)) {
                Map<UUID, Double> var123 = this.flux.storedDamage;
                Double var198 = 0.0;
                String var212 = fmt2(var123.getOrDefault(var9, var198), 1272421294);
                Map<UUID, Double> var131 = this.flux.fluxKinetic;
                Double var201 = 0.0;
                String var213 = formatKinetic(var131.getOrDefault(var9, var201), 1402756222);
                FluxGemListener var138 = this.flux;
                Map var204 = this.flux.cd2Expiry;
                String var139 = var138.formatCd(var9, var204, 540738700);
                FluxGemListener var207 = this.flux;
                Map var210 = this.flux.cd3Expiry;
                String var208 = var207.formatCd(var9, var210, 540738700);
                String var214 = "&7☄ &b"
                    + var139
                    + "  <##5ED7FF>\ud83d\udd2e &b"
                    + var212
                    + " <##5ED7FF>\ud83d\udd2e &b"
                    + var213
                    + " &7\ud83c\udf00 &b"
                    + var208;
                if (!AssessmentWindowBarTask.isShowing(1134156722) && !MotifSpeechListener.isSpeakingTo(var8.getUniqueId(), 1524431478)) {
                    Component var180 = Text.legacy(var214, 373192400);
                    var8.sendActionBar(var180);
                }
            } else if (!var16 && (var46 || var146)) {
                ItemStack var120;
                if (var46) {
                    var120 = var10;
                } else {
                    var120 = var11;
                }

                int var18 = BlissCoreItems.tierOf(var120, 1633828214);
                PlayerDataStore var72 = this.players;
                PlayerData var19 = var72.get(var9, 1264010596);
                if (!var19.canUsePassives(2051432579)) {
                    String var117 = "flux";
                    String var173 = "flow-state-mining";
                    if (GemTuning.passive(var117, var173, true, 14309468)) {
                        PotionEffectType var174 = PotionEffectType.HASTE;
                        GrantedEffect.strip(var8, var174, 553187889);
                    }
                } else {
                    if (var19.energy(1160853318) > 5) {
                        String var78 = "flux";
                        String var160 = "tireless";
                        if (GemTuning.passive(var78, var160, true, 14309468)) {
                            PotionEffectType var161 = PotionEffectType.SLOWNESS;
                            var8.removePotionEffect(var161);
                            PotionEffectType var162 = PotionEffectType.HUNGER;
                            var8.removePotionEffect(var162);
                            PotionEffectType var163 = PotionEffectType.WEAKNESS;
                            var8.removePotionEffect(var163);
                        }
                    }

                    Map<UUID, Double> var85 = this.flux.storedDamage;
                    Double var184 = 0.0;
                    String var20 = fmt2(var85.getOrDefault(var9, var184), 1272421294);
                    Map<UUID, Double> var93 = this.flux.fluxKinetic;
                    Double var187 = 0.0;
                    String var23 = formatKinetic(var93.getOrDefault(var9, var187), 1402756222);
                    FluxGemListener var102 = this.flux;
                    Map var190 = this.flux.cd2Expiry;
                    String var24 = var102.formatCd(var9, var190, 540738700);
                    FluxGemListener var105 = this.flux;
                    Map var193 = this.flux.cd3Expiry;
                    String var25 = var105.formatCd(var9, var193, 540738700);
                    String var26;
                    if (var18 == 2) {
                        var26 = "&7☄ &b" + var24 + "  <##5ED7FF>\ud83d\udd2e &b" + var20 + " <##5ED7FF>\ud83d\udd2e &b" + var23 + " &7\ud83c\udf00 &b" + var25;
                    } else {
                        var26 = "<##009ac9>\ud83d\udd3a &b" + var20;
                    }

                    if (var19.energy(1160853318) >= 2 && !var19.hudSuppressed(859110487)) {
                        Component var172 = Text.legacy(var26, 373192400);
                        var8.sendActionBar(var172);
                    }
                }
            }
        }
    }

    private static String formatKinetic(double var0, int var2) {
        return fmt2(var0, 1272421294);
    }

    private static String fmt2(double var0, int var2) {
        if (var0 <= 0.0) {
            return "0";
        } else {
            String var12 = "%.2f";
            Object[] var15 = new Object[1];
            Double var16 = var0;
            var15[0] = var16;
            return String.format(var12, var15);
        }
    }
}
