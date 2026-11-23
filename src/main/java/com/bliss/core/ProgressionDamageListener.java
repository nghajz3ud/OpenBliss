package com.bliss.core;

import com.bliss.gems.WealthGemListener;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public final class ProgressionDamageListener implements Listener {
    private final GlobalState state;
    private final WealthGemListener wealth;

    public ProgressionDamageListener(GlobalState var1, WealthGemListener var2, int var3) {
        this.state = var1;
        this.wealth = var2;
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onDamage(EntityDamageByEntityEvent var1) {
        if (var1.getEntity() instanceof Player var4) {
            this.applyProgressionEnchantCap(var4, 808089179);
        }

        if (var1.getDamager() instanceof Player var20) {
            this.applyProgressionEnchantCap(var20, 808089179);
        }
    }

    private void applyProgressionEnchantCap(Player var1, int var2) {
        boolean var83;
        label100: {
            if (this.wealth != null) {
                Map<UUID, Long> var20 = this.wealth.amplifExpiry;
                UUID var85 = var1.getUniqueId();
                Long var100 = 0L;
                long var23 = var20.getOrDefault(var85, var100);
                long var101 = System.currentTimeMillis();
                if (var23 > var101) {
                    var83 = true;
                    break label100;
                }
            }

            var83 = false;
        }

        boolean var7 = var83;
        GlobalState var8 = GlobalState.forPlayer(var1, 1433087673);
        if (var8.progEnchantLimit(1344216646)) {
            ItemStack[] var9 = var1.getInventory().getContents();
            int var10 = 0;

            while (true) {
                int var87 = var9.length;
                if (var10 >= var87) {
                    return;
                }

                ItemStack var11 = var9[var10];
                if (var11 != null && var11.getItemMeta().hasEnchants()) {
                    if (!var8.prog1(494064183)) {
                        if (var8.prog2(1337683067) && !var7) {
                            UUID var99 = var1.getUniqueId();
                            Progression2Rules.applyTo(var11, var99, 736519659);
                        } else if (var8.prog3(1589905580) && !var7) {
                            downgradeEnchants(var11, 890206341);
                        }
                    } else {
                        Material var12 = var11.getType();
                        Material var89 = Material.PRISMARINE_SHARD;
                        if (var12 != var89) {
                            Material var90 = Material.AMETHYST_SHARD;
                            if (var12 != var90) {
                                Material var91 = Material.OBSIDIAN;
                                if (var12 != var91) {
                                    boolean var13 = BlissCoreItems.isMegaMace(var11, 2030001584);

                                    for (Enchantment var15 : var11.getEnchantments().keySet()) {
                                        if (var13) {
                                            Enchantment var92 = Enchantment.BREACH;
                                            if (var15.equals(var92) || MaceRestrictionsListener.isWindBurst(var15, 283952539)) {
                                                continue;
                                            }
                                        }

                                        int var84;
                                        if (GemAutoEnchants.allStages(var1.getUniqueId(), 1630297028)) {
                                            UUID var74 = var1.getUniqueId();
                                            var84 = GemAutoEnchants.granted(var74, var15, 837616120);
                                        } else {
                                            var84 = 0;
                                        }

                                        if (var84 > 0) {
                                            int var69 = var11.getEnchantmentLevel(var15);
                                            if (var69 > var84) {
                                                var11.addUnsafeEnchantment(var15, var84);
                                            }
                                        } else {
                                            var11.removeEnchantment(var15);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                var10++;
            }
        }
    }

    private static void downgradeEnchants(ItemStack var0, int var1) {
        Material var5 = var0.getType();
        if (isHelmet(var5, 150455006) || isChestplate(var5, 953148528) || isLeggings(var5, 1963631968) || isBoots(var5, 1825330898)) {
            Enchantment var3 = Enchantment.PROTECTION;
            if (var0.getEnchantmentLevel(var3) == 4) {
                Enchantment var38 = Enchantment.PROTECTION;
                var0.addUnsafeEnchantment(var38, 3);
            }

            if (isBoots(var5, 1825330898)) {
                Enchantment var39 = Enchantment.FEATHER_FALLING;
                if (var0.getEnchantmentLevel(var39) == 4) {
                    Enchantment var41 = Enchantment.FEATHER_FALLING;
                    var0.addUnsafeEnchantment(var41, 3);
                }
            }
        }

        Material var42 = Material.BOW;
        if (var5 == var42) {
            Enchantment var43 = Enchantment.POWER;
            int var6 = var0.getEnchantmentLevel(var43);
            if (var6 == 4 || var6 == 5) {
                Enchantment var46 = Enchantment.POWER;
                var0.addUnsafeEnchantment(var46, 3);
            }
        }

        if (isSword(var5, 1023724777) || isAxe(var5, 130827015)) {
            Enchantment var47 = Enchantment.SHARPNESS;
            if (var0.getEnchantmentLevel(var47) == 5) {
                Enchantment var49 = Enchantment.SHARPNESS;
                var0.addUnsafeEnchantment(var49, 4);
            }
        }
    }

    @EventHandler
    public void onInvisibleDeath(PlayerDeathEvent var1) {
        Player var4;
        Player var5;
        boolean var6;
        boolean var24;
        label33: {
            var4 = var1.getEntity();
            var5 = var4.getKiller();
            PotionEffectType var3 = PotionEffectType.INVISIBILITY;
            var6 = var4.hasPotionEffect(var3);
            if (var5 != null) {
                PotionEffectType var25 = PotionEffectType.INVISIBILITY;
                if (var5.hasPotionEffect(var25)) {
                    var24 = true;
                    break label33;
                }
            }

            var24 = false;
        }

        if (var24 && var6) {
            Component var27 = Text.legacy("&k123 &rwas &rslain &rby &k123", 373192400);
            var1.deathMessage(var27);
        } else if (var24) {
            Component var35 = Text.legacy(var4.getName() + " was slain by &k123", 373192400);
            var1.deathMessage(var35);
        } else if (var6 && var5 != null) {
            Component var31 = Text.legacy("&k123 &rwas &rslain &rby &r" + var5.getName(), 373192400);
            var1.deathMessage(var31);
        }
    }

    private static boolean isHelmet(Material var0, int var1) {
        return switch (var0) {
            case LEATHER_HELMET, IRON_HELMET, GOLDEN_HELMET, DIAMOND_HELMET, NETHERITE_HELMET, CHAINMAIL_HELMET, TURTLE_HELMET-> true;
            default -> false;
        };
    }

    private static boolean isChestplate(Material var0, int var1) {
        return switch (var0) {
            case LEATHER_CHESTPLATE, IRON_CHESTPLATE, GOLDEN_CHESTPLATE, DIAMOND_CHESTPLATE, NETHERITE_CHESTPLATE, CHAINMAIL_CHESTPLATE, ELYTRA-> true;
            default -> false;
        };
    }

    private static boolean isLeggings(Material var0, int var1) {
        return switch (var0) {
            case LEATHER_LEGGINGS, IRON_LEGGINGS, GOLDEN_LEGGINGS, DIAMOND_LEGGINGS, NETHERITE_LEGGINGS, CHAINMAIL_LEGGINGS-> true;
            default -> false;
        };
    }

    private static boolean isBoots(Material var0, int var1) {
        return switch (var0) {
            case LEATHER_BOOTS, IRON_BOOTS, GOLDEN_BOOTS, DIAMOND_BOOTS, NETHERITE_BOOTS, CHAINMAIL_BOOTS-> true;
            default -> false;
        };
    }

    private static boolean isSword(Material var0, int var1) {
        return switch (var0) {
            case WOODEN_SWORD, STONE_SWORD, IRON_SWORD, GOLDEN_SWORD, DIAMOND_SWORD, NETHERITE_SWORD-> true;
            default -> false;
        };
    }

    private static boolean isAxe(Material var0, int var1) {
        return switch (var0) {
            case WOODEN_AXE, STONE_AXE, IRON_AXE, GOLDEN_AXE, DIAMOND_AXE, NETHERITE_AXE-> true;
            default -> false;
        };
    }
}
