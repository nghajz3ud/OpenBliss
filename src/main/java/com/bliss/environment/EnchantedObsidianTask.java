package com.bliss.environment;

import com.bliss.BlissPlugin;
import com.bliss.core.TeamManager;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Team;

public final class EnchantedObsidianTask {
    private static final long PERIOD_TICKS = 100L;
    private static final String ENCH_OBSIDIAN_NAME = "ᴇɴᴄʜᴀɴᴛᴇᴅ ᴏʙꜱɪᴅɪᴀɴ";
    private final BlissPlugin plugin;
    private BukkitTask task;
    private final Set<UUID> glowing;

    public EnchantedObsidianTask(BlissPlugin var1, int var2) {
        HashSet var4 = new HashSet();
        this.glowing = var4;
        this.plugin = var1;
    }

    public void start(int var1) {
        BukkitScheduler var3 = Bukkit.getScheduler();
        BlissPlugin var11 = this.plugin;
        Runnable var12 = this::tick;
        BukkitTask var10 = var3.runTaskTimer(var11, var12, 100L, 100L);
        this.task = var10;
    }

    public void stop(int var1) {
        if (this.task != null) {
            this.task.cancel();
        }

        Iterator var4 = this.glowing.iterator();

        while (var4.hasNext()) {
            Player var6 = Bukkit.getPlayer((UUID)var4.next());
            if (var6 != null) {
                PotionEffectType var3 = PotionEffectType.GLOWING;
                var6.removePotionEffect(var3);
            }
        }

        this.glowing.clear();
    }

    private void tick() {
        Team var10 = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("EnchObi");
        if (var10 != null) {
            for (Player var12 : Bukkit.getOnlinePlayers()) {
                if (hasEnchantedObsidian(var12, 1997518805)) {
                    String var60 = var12.getName();
                    var10.addEntry(var60);
                    PotionEffectType var3 = PotionEffectType.GLOWING;
                    PotionEffect var61 = new PotionEffect(var3, -1, 0, true, false, false);
                    var12.addPotionEffect(var61);
                    Set var50 = this.glowing;
                    UUID var63 = var12.getUniqueId();
                    var50.add(var63);
                } else {
                    String var53 = var12.getName();
                    TeamManager.safeRemoveEntry(var10, var53, 1412413626);
                    Set var29 = this.glowing;
                    UUID var55 = var12.getUniqueId();
                    if (var29.remove(var55)) {
                        PotionEffectType var58 = PotionEffectType.GLOWING;
                        var12.removePotionEffect(var58);
                    } else {
                        PotionEffectType var56 = PotionEffectType.GLOWING;
                        PotionEffect var13 = var12.getPotionEffect(var56);
                        if (var13 != null && var13.isInfinite() && var13.getAmplifier() == 0 && var13.isAmbient() && !var13.hasParticles() && !var13.hasIcon()) {
                            PotionEffectType var57 = PotionEffectType.GLOWING;
                            var12.removePotionEffect(var57);
                        }
                    }
                }
            }
        }
    }

    private static boolean hasEnchantedObsidian(Player var0, int var1) {
        for (ItemStack var7 : var0.getInventory().getContents()) {
            if (var7 != null) {
                Material var22 = var7.getType();
                Material var35 = Material.OBSIDIAN;
                if (var22 == var35) {
                    ItemMeta var8 = var7.getItemMeta();
                    if (var8 != null) {
                        Component var9 = var8.displayName();
                        if (var9 != null) {
                            PlainTextComponentSerializer var29 = PlainTextComponentSerializer.plainText();
                            if (var29.serialize(var9).contains("ᴇɴᴄʜᴀɴᴛᴇᴅ ᴏʙꜱɪᴅɪᴀɴ")) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }
}
