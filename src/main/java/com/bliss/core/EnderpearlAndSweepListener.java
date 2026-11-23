package com.bliss.core;

import com.bliss.BlissPlugin;
import com.bliss.gems.WealthGemListener;
import com.bliss.items.BlissCoreItems;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class EnderpearlAndSweepListener implements Listener {
    private static final long SWEEP_PERIOD_TICKS = 200L;
    private final BlissPlugin plugin;
    private final GlobalState state;
    private final WealthGemListener wealth;
    private BukkitTask sweepTask;

    public EnderpearlAndSweepListener(BlissPlugin var1, GlobalState var2, WealthGemListener var3, int var4) {
        this.plugin = var1;
        this.state = var2;
        this.wealth = var3;
    }

    public void start(int var1) {
        BukkitScheduler var3 = Bukkit.getScheduler();
        BlissPlugin var11 = this.plugin;
        Runnable var12 = this::sweep;
        BukkitTask var10 = var3.runTaskTimer(var11, var12, 200L, 200L);
        this.sweepTask = var10;
    }

    public void stop() {
        if (this.sweepTask != null) {
            this.sweepTask.cancel();
        }
    }

    @EventHandler
    public void onPearlInteract(PlayerInteractEvent var1) {
        GlobalState var4 = GlobalState.forPlayer(var1.getPlayer(), 1433087673);
        if (!var4.enderpearls(155177658) && var4.progKitLimit(1635426977)) {
            if (var1.getAction().isRightClick()) {
                ItemStack var5 = var1.getItem();
                if (var5 != null) {
                    Material var19 = var5.getType();
                    Material var3 = Material.ENDER_PEARL;
                    if (var19 == var3) {
                        Result var21 = Result.DENY;
                        var1.setUseItemInHand(var21);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPearlTeleport(PlayerTeleportEvent var1) {
        GlobalState var4 = GlobalState.forPlayer(var1.getPlayer(), 1433087673);
        if (!var4.enderpearls(155177658) && var4.progKitLimit(1635426977)) {
            TeleportCause var12 = var1.getCause();
            TeleportCause var3 = TeleportCause.ENDER_PEARL;
            if (var12 == var3) {
                var1.setCancelled(true);
            }
        }
    }

    private void sweep() {
        long var6 = System.currentTimeMillis();

        for (Player var9 : Bukkit.getOnlinePlayers()) {
            boolean var38;
            label68: {
                if (this.wealth != null) {
                    Map<UUID, Long> var32 = this.wealth.amplifExpiry;
                    UUID var101 = var9.getUniqueId();
                    Long var115 = 0L;
                    long var35 = var32.getOrDefault(var101, var115);
                    if (var35 > var6) {
                        var38 = true;
                        break label68;
                    }
                }

                var38 = false;
            }

            boolean var10 = var38;
            GlobalState var11 = GlobalState.forPlayer(var9, 1433087673);
            if (var11.progEnchantLimit(1344216646)) {
                for (ItemStack var15 : var9.getInventory().getContents()) {
                    if (var15 != null && var15.getItemMeta() != null && var15.getItemMeta().hasEnchants()) {
                        if (!var11.prog1(494064183)) {
                            if (var11.prog2(1337683067) && !var10) {
                                UUID var114 = var9.getUniqueId();
                                Progression2Rules.applyTo(var15, var114, 736519659);
                            } else if (var11.prog3(1589905580) && !var10) {
                                downgradeEnchants(var15, 408058782);
                            }
                        } else {
                            Material var16 = var15.getType();
                            Material var104 = Material.PRISMARINE_SHARD;
                            if (var16 != var104) {
                                Material var105 = Material.AMETHYST_SHARD;
                                if (var16 != var105) {
                                    Material var106 = Material.OBSIDIAN;
                                    if (var16 != var106) {
                                        boolean var17 = BlissCoreItems.isMegaMace(var15, 2030001584);

                                        for (Enchantment var19 : var15.getEnchantments().keySet()) {
                                            if (var17) {
                                                Enchantment var107 = Enchantment.BREACH;
                                                if (var19.equals(var107) || MaceRestrictionsListener.isWindBurst(var19, 283952539)) {
                                                    continue;
                                                }
                                            }

                                            int var92;
                                            if (GemAutoEnchants.allStages(var9.getUniqueId(), 1630297028)) {
                                                UUID var91 = var9.getUniqueId();
                                                var92 = GemAutoEnchants.granted(var91, var19, 837616120);
                                            } else {
                                                var92 = 0;
                                            }

                                            if (var92 > 0) {
                                                int var86 = var15.getEnchantmentLevel(var19);
                                                if (var86 > var92) {
                                                    var15.addUnsafeEnchantment(var19, var92);
                                                }
                                            } else {
                                                var15.removeEnchantment(var19);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static void downgradeEnchants(ItemStack var0, int var1) {
        Material var5 = var0.getType();
        if (isHelmet(var5, 73041561) || isChestplate(var5, 1555847425) || isLeggings(var5, 453610861) || isBoots(var5, 1396561515)) {
            Enchantment var3 = Enchantment.PROTECTION;
            if (var0.getEnchantmentLevel(var3) == 4) {
                Enchantment var38 = Enchantment.PROTECTION;
                var0.addUnsafeEnchantment(var38, 3);
            }

            if (isBoots(var5, 1396561515)) {
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

        if (isSword(var5, 1082394424) || isAxe(var5, 908329349)) {
            Enchantment var47 = Enchantment.SHARPNESS;
            if (var0.getEnchantmentLevel(var47) == 5) {
                Enchantment var49 = Enchantment.SHARPNESS;
                var0.addUnsafeEnchantment(var49, 4);
            }
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
