package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.GemAutoEnchants;
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
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class PuffAuraTask {
    private static final long PERIOD_TICKS = 5L;
    private static final long AUTO_ENCHANT_COOLDOWN_MS = 5000L;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final PuffGemListener puff;
    private BukkitTask task;

    public PuffAuraTask(BlissPlugin var1, PlayerDataStore var2, PuffGemListener var3, int var4) {
        this.plugin = var1;
        this.players = var2;
        this.puff = var3;
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
        long var9 = System.currentTimeMillis();

        for (Player var12 : Bukkit.getOnlinePlayers()) {
            UUID var13 = var12.getUniqueId();
            PlayerInventory var14 = var12.getInventory();
            ItemStack var15 = var14.getItemInMainHand();
            ItemStack var16 = var14.getItemInOffHand();
            int var39 = BlissCoreItems.gemIdOf(var15, 382441079);
            int var111 = GemType.PUFF.id();
            boolean var110;
            if (var39 == var111) {
                var110 = true;
            } else {
                var110 = false;
            }

            int var41 = BlissCoreItems.gemIdOf(var16, 382441079);
            int var113 = GemType.PUFF.id();
            boolean var109;
            if (var41 == var113) {
                var109 = true;
            } else {
                var109 = false;
            }

            GemType var114 = GemType.PUFF;
            boolean var19 = GoldGem.hasAbsorbedSoul(var12, var114, 1603837876);
            if (var19) {
                PlayerDataStore var46 = this.players;
                PlayerData var20 = var46.get(var13, 1264010596);
                label78:
                if (var20.canUsePassives(2051432579)) {
                    Map var52 = this.puff.cd1;
                    Long var21 = (Long)var52.get(var13);
                    if (var21 != null) {
                        long var57 = var21;
                        if (var57 > var9) {
                            break label78;
                        }
                    }

                    PlayerInventory var61 = var12.getInventory();
                    int var136 = var20.energy(1160853318);
                    autoEnchant(var61, 2, var136, 1504077162);
                    Map var64 = this.puff.cd1;
                    Long var141 = var9 + 5000L;
                    var64.put(var13, var141);
                }
            }

            GemType var119 = GemType.PUFF;
            if (GoldGem.isWieldingSoul(var12, var119, 100010596)) {
                if (!AssessmentWindowBarTask.isShowing(1134156722) && !MotifSpeechListener.isSpeakingTo(var12.getUniqueId(), 1524431478)) {
                    Component var134 = Text.legacy(this.buildActionBar(2, var13, 600134909), 373192400);
                    var12.sendActionBar(var134);
                }
            } else if (!var19 && (var110 || var109)) {
                PlayerDataStore var72 = this.players;
                PlayerData var156 = var72.get(var13, 1264010596);
                if (var156.canUsePassives(2051432579)) {
                    ItemStack var77;
                    if (var110) {
                        var77 = var15;
                    } else {
                        var77 = var16;
                    }

                    int var157 = BlissCoreItems.tierOf(var77, 1633828214);
                    PlayerDataStore var80 = this.players;
                    UUID var122 = var12.getUniqueId();
                    if (var80.get(var122, 1264010596).energy(1160853318) >= 2) {
                        PlayerDataStore var84 = this.players;
                        UUID var125 = var12.getUniqueId();
                        if (!var84.get(var125, 1264010596).hudSuppressed(859110487)) {
                            Component var128 = Text.legacy(this.buildActionBar(var157, var13, 600134909), 373192400);
                            var12.sendActionBar(var128);
                        }
                    }

                    if (var110) {
                        Map var91 = this.puff.cd1;
                        Long var22 = (Long)var91.get(var13);
                        if (var22 != null) {
                            long var96 = var22;
                            if (var96 > var9) {
                                continue;
                            }
                        }

                        int var146 = var156.energy(1160853318);
                        autoEnchant(var14, var157, var146, 1504077162);
                        Map var102 = this.puff.cd1;
                        Long var151 = var9 + 5000L;
                        var102.put(var13, var151);
                    }
                }
            }
        }
    }

    private String buildActionBar(int var1, UUID var2, int var3) {
        if (var1 == 2) {
            Map var10 = this.puff.cd3;
            String var11 = timer(var10, var2, 1049995098);
            Map var21 = this.puff.cd4;
            String var22 = timer(var21, var2, 1049995098);
            Map var26 = this.puff.cd2;
            String var27 = timer(var26, var2, 1049995098);
            return "&f☁ &b" + var11 + "  &f\ud83d\udd2e &b" + var22 + " &f⏫ &b" + var27;
        } else {
            Map var15 = this.puff.cd4;
            return "&f\ud83d\udd3a &b" + timer(var15, var2, 1049995098);
        }
    }

    private static String timer(Map<UUID, Long> var0, UUID var1, int var2) {
        Long var7 = (Long)var0.get(var1);
        if (var7 == null) {
            return "&aReady!";
        } else {
            long var8 = System.currentTimeMillis();
            long var20 = var7;
            if (var20 <= var8) {
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

    private static void autoEnchant(PlayerInventory var0, int var1, int var2, int var3) {
        Enchantment var5 = Enchantment.POWER;
        int var52 = powerLevel(var1, var2, 1251568823);
        int var8 = GemAutoEnchants.allow(var0, var5, var52, 987370355);
        Enchantment var42 = Enchantment.PUNCH;
        int var54 = punchLevel(var1, var2, 2140896643);
        int var9 = GemAutoEnchants.allow(var0, var42, var54, 987370355);
        String var17 = "puff";
        String var43 = "auto-power";
        if (!GemTuning.passive(var17, var43, true, 14309468)) {
            var8 = 0;
        }

        String var20 = "puff";
        String var44 = "auto-punch";
        if (!GemTuning.passive(var20, var44, true, 14309468)) {
            var9 = 0;
        }

        for (ItemStack var13 : var0.getContents()) {
            if (var13 != null) {
                Material var33 = var13.getType();
                Material var47 = Material.BOW;
                if (var33 == var47) {
                    Enchantment var48 = Enchantment.POWER;
                    if (var13.getEnchantmentLevel(var48) == 0 && var8 > 0) {
                        Enchantment var49 = Enchantment.POWER;
                        var13.addUnsafeEnchantment(var49, var8);
                    }

                    Enchantment var50 = Enchantment.PUNCH;
                    if (var13.getEnchantmentLevel(var50) == 0 && var9 > 0) {
                        Enchantment var51 = Enchantment.PUNCH;
                        var13.addUnsafeEnchantment(var51, var9);
                    }
                }
            }
        }
    }

    private static int powerLevel(int var0, int var1, int var2) {
        if (var0 == 2) {
            if (var1 > 5) {
                return 5;
            } else {
                return var1 > 4 ? 4 : 0;
            }
        } else if (var1 > 5) {
            return 3;
        } else if (var1 > 4) {
            return 2;
        } else {
            return var1 > 3 ? 1 : 0;
        }
    }

    private static int punchLevel(int var0, int var1, int var2) {
        if (var0 == 2) {
            if (var1 > 4) {
                return 2;
            } else {
                return var1 > 3 ? 1 : 0;
            }
        } else {
            return 1;
        }
    }
}
