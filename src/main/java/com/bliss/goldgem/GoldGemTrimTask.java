package com.bliss.goldgem;

import com.bliss.BlissPlugin;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class GoldGemTrimTask {
    private static final NamespacedKey KEY_ORIG_TRIM = NamespacedKey.fromString("bliss:goldgem_trim_orig");
    private static final long PERIOD_TICKS = 20L;
    private final BlissPlugin plugin;
    private BukkitTask task;

    public GoldGemTrimTask(BlissPlugin var1, int var2) {
        this.plugin = var1;
    }

    public void start(int var1) {
        BukkitScheduler var3 = Bukkit.getScheduler();
        BlissPlugin var11 = this.plugin;
        Runnable var12 = this::tick;
        BukkitTask var10 = var3.runTaskTimer(var11, var12, 20L, 20L);
        this.task = var10;
    }

    public void stop(int var1) {
        if (this.task != null) {
            this.task.cancel();
        }
    }

    private static Registry<TrimMaterial> trimRegistry(int var0) {
        RegistryAccess var1 = RegistryAccess.registryAccess();
        RegistryKey var2 = RegistryKey.TRIM_MATERIAL;
        return var1.getRegistry(var2);
    }

    private void tick() {
        for (Player var4 : Bukkit.getOnlinePlayers()) {
            if (GoldGem.findInInventory(var4, 1361603346) != null) {
                this.golden(var4, 870697062);
            } else {
                this.revert(var4, 585113020);
            }
        }
    }

    private void golden(Player var1, int var2) {
        PlayerInventory var8 = var1.getInventory();
        ItemStack[] var9 = var8.getArmorContents();
        boolean var10 = false;

        for (ItemStack var14 : var9) {
            if (var14 != null) {
                ItemMeta var16 = var14.getItemMeta();
                if (var16 instanceof ArmorMeta) {
                    ArmorMeta var15 = (ArmorMeta)var16;
                    ArmorTrim var69 = var15.getTrim();
                    if (var69 != null) {
                        TrimMaterial var41 = TrimMaterial.GOLD;
                        TrimMaterial var58 = var69.getMaterial();
                        if (!var41.equals(var58)) {
                            PersistentDataContainer var44 = var15.getPersistentDataContainer();
                            NamespacedKey var59 = KEY_ORIG_TRIM;
                            PersistentDataType var5 = PersistentDataType.STRING;
                            if (!var44.has(var59, var5)) {
                                Registry var46 = trimRegistry(457973640);
                                TrimMaterial var61 = var69.getMaterial();
                                NamespacedKey var17 = var46.getKey(var61);
                                if (var17 == null) {
                                    continue;
                                }

                                PersistentDataContainer var50 = var15.getPersistentDataContainer();
                                NamespacedKey var62 = KEY_ORIG_TRIM;
                                PersistentDataType var65 = PersistentDataType.STRING;
                                String var66 = var17.toString();
                                var50.set(var62, var65, var66);
                            }

                            TrimMaterial var67 = TrimMaterial.GOLD;
                            TrimPattern var68 = var69.getPattern();
                            ArmorTrim var63 = new ArmorTrim(var67, var68);
                            var15.setTrim(var63);
                            var14.setItemMeta(var15);
                            var10 = true;
                        }
                    }
                }
            }
        }

        if (var10) {
            var8.setArmorContents(var9);
        }
    }

    private void revert(Player var1, int var2) {
        PlayerInventory var8 = var1.getInventory();
        ItemStack[] var9 = var8.getContents();
        int var10 = 0;

        while (true) {
            int var52 = var9.length;
            if (var10 >= var52) {
                return;
            }

            ItemStack var11 = var9[var10];
            if (var11 != null && var11.getItemMeta() instanceof ArmorMeta var12) {
                PersistentDataContainer var32 = var12.getPersistentDataContainer();
                NamespacedKey var54 = KEY_ORIG_TRIM;
                PersistentDataType var5 = PersistentDataType.STRING;
                String var62 = (String)var32.get(var54, var5);
                if (var62 != null) {
                    PersistentDataContainer var37 = var12.getPersistentDataContainer();
                    NamespacedKey var55 = KEY_ORIG_TRIM;
                    var37.remove(var55);
                    ArmorTrim var14 = var12.getTrim();
                    NamespacedKey var15 = NamespacedKey.fromString(var62);
                    TrimMaterial var51;
                    if (var15 == null) {
                        var51 = null;
                    } else {
                        Registry var49 = trimRegistry(457973640);
                        var51 = (TrimMaterial)var49.get(var15);
                    }

                    if (var14 != null && var51 != null) {
                        TrimPattern var61 = var14.getPattern();
                        ArmorTrim var56 = new ArmorTrim(var51, var61);
                        var12.setTrim(var56);
                    }

                    var11.setItemMeta(var12);
                    var8.setItem(var10, var11);
                }
            }

            var10++;
        }
    }
}
