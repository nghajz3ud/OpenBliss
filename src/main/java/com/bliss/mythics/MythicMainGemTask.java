package com.bliss.mythics;

import com.bliss.BlissPlugin;
import com.bliss.core.AtomicYml;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.goldgem.GoldGem;
import com.bliss.goldgem.GoldenDreamRitual;
import com.bliss.goldgem.GoldenDreamWorld;
import com.bliss.honored.JuggernautGemGuardListener;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class MythicMainGemTask {
    private static final long PERIOD_TICKS = 20L;
    private final BlissPlugin plugin;
    private final File file;
    private final YamlConfiguration store;
    private BukkitTask task;

    public MythicMainGemTask(BlissPlugin var1, int var2) {
        this.plugin = var1;
        File var14 = var1.getDataFolder();
        File var10 = new File(var14, "mythicmaingems.yml");
        this.file = var10;
        YamlConfiguration var13 = YamlConfiguration.loadConfiguration(this.file);
        this.store = var13;
    }

    public void start(int var1) {
        BukkitScheduler var3 = Bukkit.getScheduler();
        BlissPlugin var11 = this.plugin;
        Runnable var12 = this::tick;
        BukkitTask var10 = var3.runTaskTimer(var11, var12, 20L, 20L);
        this.task = var10;
    }

    public void stop() {
        if (this.task != null) {
            this.task.cancel();
        }
    }

    private void tick() {
        GlobalState var4 = this.plugin.globalState(724248306);
        boolean var76;
        if (var4 != null && var4.mythicGems(289981390)) {
            var76 = true;
        } else {
            var76 = false;
        }

        boolean var77;
        if (var76 && var4.mythicGemStripsMain(635850224)) {
            var77 = true;
        } else {
            var77 = false;
        }

        boolean var6 = var77;
        if (var77 || !this.store.getKeys(false).isEmpty()) {
            for (Player var8 : Bukkit.getOnlinePlayers()) {
                UUID var9 = var8.getUniqueId();
                if (!var8.isDead()) {
                    GoldenDreamRitual var10 = this.plugin.goldenDream(2108340810);
                    if (var10 != null) {
                        if (var10.isRunning(var8, 1689309365)) {
                            continue;
                        }

                        GoldenDreamWorld var45 = var10.dream(2117018474);
                        if (var45.inDream(var9, 2004945976)) {
                            continue;
                        }
                    }

                    if (!JuggernautGemGuardListener.isJuggernaut(var8, 859003337) && !holdsGoldGem(var8, 1991661139)) {
                        boolean var12 = MythicItems.hasMythicGemInInventory(var8, 2063095181);
                        YamlConfiguration var55 = this.store;
                        String var81 = var9.toString();
                        if (var55.contains(var81)) {
                            if (var6 && var12) {
                                PlayerDataStore var70 = this.plugin.players(1969902410);
                                PlayerData var90 = var70.get(var9, 1264010596);
                                GemType var73 = var90.gem(1413186337);
                                GemType var86 = GemType.NONE;
                                if (var73 != var86) {
                                    this.take(var8, var90, 2004368860);
                                }
                            } else {
                                this.giveBack(var8, 222714185);
                            }
                        } else if (var6 && var12) {
                            PlayerDataStore var61 = this.plugin.players(1969902410);
                            PlayerData var13 = var61.get(var9, 1264010596);
                            GemType var64 = var13.gem(1413186337);
                            GemType var83 = GemType.NONE;
                            if (var64 != var83) {
                                this.take(var8, var13, 2004368860);
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean holdsGoldGem(Player var0, int var1) {
        ItemStack[] var4 = var0.getInventory().getContents();
        int var5 = var4.length;

        for (int var6 = 0; var6 < var5; var6++) {
            if (GoldGem.isGoldGem(var4[var6])) {
                return true;
            }
        }

        return false;
    }

    private void take(Player var1, PlayerData var2, int var3) {
        String var8 = var1.getUniqueId().toString();
        GemType var9 = var2.gem(1413186337);
        YamlConfiguration var16 = this.store;
        String var43 = var8 + ".gem";
        int var10 = var16.getInt(var43, 0);
        if (var10 != 0) {
            int var45 = var9.id();
            if (var10 != var45) {
                Logger var22 = this.plugin.getLogger();
                String var47 = var1.getName();
                String var67 = String.valueOf(var9);
                String var78 = String.valueOf(GemType.byId(var10));
                String var48 = "Mythic main gem: " + var47 + " came by " + var67 + " while " + var78 + " was remembered - the new gem is what is kept";
                var22.info(var48);
            }
        }

        YamlConfiguration var24 = this.store;
        String var50 = var8 + ".gem";
        Integer var70 = var9.id();
        var24.set(var50, var70);
        if (!this.save(1519909272)) {
            if (var10 != 0) {
                YamlConfiguration var37 = this.store;
                String var61 = var8 + ".gem";
                Integer var75 = var10;
                var37.set(var61, var75);
            } else {
                YamlConfiguration var42 = this.store;
                var42.set(var8, null);
            }

            Logger var40 = this.plugin.getLogger();
            String var64 = "Mythic main gem: could not write the record for " + var1.getName() + " - not taking the gem";
            var40.warning(var64);
        } else {
            GemType var51 = GemType.NONE;
            var2.setGem(var51, 2137390487);
            BlissCoreItems var30 = this.plugin.coreItems(401009135);
            var30.refreshGemItem(var1, var2, 1088402172);
            Component var56 = Text.legacy(
                "<##E3B94F>\ud83d\udd2e <##B8FFFB>The mythic gem takes your " + var9.coloredName() + " <##B8FFFB>gem. It returns once you let go of it.",
                373192400
            );
            var1.sendMessage(var56);
            Logger var34 = this.plugin.getLogger();
            String var58 = String.valueOf(var9);
            String var73 = var1.getName();
            String var59 = "Mythic main gem: took " + var58 + " from " + var73;
            var34.info(var59);
        }
    }

    private void giveBack(Player var1, int var2) {
        String var7;
        var7 = var1.getUniqueId().toString();
        PlayerDataStore var14 = this.plugin.players(1969902410);
        UUID var47 = var1.getUniqueId();
        PlayerData var8 = var14.get(var47, 1264010596);
        YamlConfiguration var17 = this.store;
        String var49 = var7 + ".gem";
        GemType var9 = GemType.byId(var17.getInt(var49, 0));
        GemType var50 = GemType.NONE;
        label21:
        if (var9 != var50) {
            GemType var22 = var8.gem(1413186337);
            GemType var51 = GemType.NONE;
            if (var22 != var51) {
                GemType var24 = var8.gem(1413186337);
                if (var24 != var9) {
                    Logger var27 = this.plugin.getLogger();
                    String var54 = var1.getName();
                    String var71 = String.valueOf(var8.gem(1413186337));
                    String var76 = String.valueOf(var9);
                    String var55 = "Mythic main gem: " + var54 + " already holds " + var71 + ", the remembered " + var76 + " is dropped";
                    var27.info(var55);
                    break label21;
                }
            }

            var8.setGem(var9, 2137390487);
            BlissCoreItems var35 = this.plugin.coreItems(401009135);
            var35.refreshGemItem(var1, var8, 1088402172);
            if (!this.plugin.players(1969902410).save(1141447725)) {
                Logger var42 = this.plugin.getLogger();
                String var61 = "Mythic main gem: could not save players.yml for " + var1.getName() + " - keeping the record, retrying next tick";
                var42.warning(var61);
                return;
            }

            Component var65 = Text.legacy("<##E3B94F>\ud83d\udd2e <##B8FFFB>Your " + var9.coloredName() + " <##B8FFFB>gem returns to you.", 373192400);
            var1.sendMessage(var65);
            Logger var46 = this.plugin.getLogger();
            String var67 = String.valueOf(var9);
            String var75 = var1.getName();
            String var68 = "Mythic main gem: gave " + var67 + " back to " + var75;
            var46.info(var68);
        }

        YamlConfiguration var29 = this.store;
        var29.set(var7, null);
        this.save(1519909272);
    }

    private boolean save(int var1) {
        try {
            YamlConfiguration var5 = this.store;
            File var12 = this.file;
            AtomicYml.save(var5, var12, 1347473142);
            return true;
        } catch (IOException var16) {
            Logger var10 = this.plugin.getLogger();
            String var15 = "mythicmaingems.yml: " + var16.getMessage();
            var10.warning(var15);
            return false;
        }
    }
}
