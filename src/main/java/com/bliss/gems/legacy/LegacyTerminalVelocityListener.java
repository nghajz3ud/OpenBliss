package com.bliss.gems.legacy;

import com.bliss.BlissPlugin;
import com.bliss.core.EnergyScale;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.PlayerDataStore;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

public final class LegacyTerminalVelocityListener implements Listener {
    private static final long COOLDOWN_MS = 40000L;
    private static final long COOLDOWN_EGG_MS = 20000L;
    private static final int DURATION_TICKS = 200;
    private static final int SPEED_AMP = 3;
    private static final int HASTE_AMP = 1;
    private static final long DOUBLE_CLICK_TICKS = 20L;
    private static final long EAT_GAP_MS = 800L;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final LegacySpeedState state;
    private final GlobalState globalState;
    private final Set<UUID> active;
    private final Set<UUID> armed;
    private final Map<UUID, Long> lastEat;
    private final Map<UUID, Integer> lastClickTick;

    public LegacyTerminalVelocityListener(BlissPlugin var1, PlayerDataStore var2, LegacySpeedState var3, GlobalState var4, int var5) {
        KeySetView var7 = ConcurrentHashMap.newKeySet();
        this.active = var7;
        KeySetView var16 = ConcurrentHashMap.newKeySet();
        this.armed = var16;
        ConcurrentHashMap var17 = new ConcurrentHashMap();
        this.lastEat = var17;
        ConcurrentHashMap var18 = new ConcurrentHashMap();
        this.lastClickTick = var18;
        this.plugin = var1;
        this.players = var2;
        this.state = var3;
        this.globalState = var4;
    }

    public boolean isActive(UUID var1) {
        Set var4 = this.active;
        return var4.contains(var1);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent var1) {
        Action var18 = var1.getAction();
        Action var3 = Action.RIGHT_CLICK_AIR;
        if (var18 != var3) {
            Action var20 = var1.getAction();
            Action var101 = Action.RIGHT_CLICK_BLOCK;
            if (var20 != var101) {
                return;
            }
        }

        GlobalState var23 = GlobalState.forPlayer(var1.getPlayer(), 1433087673);
        Player var103 = var1.getPlayer();
        if (var23.legacySpeedFor(var103, 1914133154)) {
            Player var7 = var1.getPlayer();
            UUID var8 = var7.getUniqueId();
            int var9 = Bukkit.getCurrentTick();
            Map var31 = this.lastClickTick;
            Integer var10 = (Integer)var31.get(var8);
            if (var10 != null) {
                int var36 = var10;
                if (var36 == var9) {
                    return;
                }
            }

            boolean var100;
            label90: {
                Map var38 = this.lastClickTick;
                Integer var130 = var9;
                var38.put(var8, var130);
                Action var41 = var1.getAction();
                Action var107 = Action.RIGHT_CLICK_AIR;
                if (var41 != var107) {
                    Action var43 = var1.getAction();
                    Action var108 = Action.RIGHT_CLICK_BLOCK;
                    if (var43 != var108 || var1.getClickedBlock() != null && var1.getClickedBlock().getType().isInteractable()) {
                        var100 = false;
                        break label90;
                    }
                }

                var100 = true;
            }

            Set var51 = this.active;
            if (!var51.contains(var8) || !var100 || !this.tryEat(var7, 2104469046)) {
                int var60 = BlissCoreItems.gemIdOf(var7.getInventory().getItemInOffHand(), 382441079);
                int var112 = GemType.SPEED.id();
                if (var60 == var112) {
                    PlayerDataStore var62 = this.players;
                    if (var62.get(var8, 1264010596).canUseAbilities(325243089)) {
                        Material var14 = var7.getInventory().getItemInMainHand().getType();
                        Material var114 = Material.AIR;
                        if (var14 == var114 || isSword(var14, 1466076630) || isAxe(var14, 71674549)) {
                            long var15 = System.currentTimeMillis();
                            Map var79 = this.state.terminalCd;
                            Long var17 = (Long)var79.get(var8);
                            if (var17 != null) {
                                long var84 = var17;
                                if (var84 > var15) {
                                    long var117 = var17;
                                    Component var123 = Text.legacy(
                                        "&e\ud83d\udd2e &cYour §eTerminal Velocity &cskill is on cooldown for &e" + readable(var117 - var15, 70644052),
                                        373192400
                                    );
                                    var7.sendMessage(var123);
                                    return;
                                }
                            }

                            Set var89 = this.armed;
                            if (!var89.contains(var8)) {
                                Set var92 = this.armed;
                                var92.add(var8);
                                BukkitScheduler var94 = Bukkit.getScheduler();
                                BlissPlugin var127 = this.plugin;
                                Runnable var134 = () -> {
                                    Set var4 = this.armed;
                                    var4.remove(var8);
                                };
                                var94.runTaskLater(var127, var134, 20L);
                            } else {
                                Set var97 = this.armed;
                                var97.remove(var8);
                                this.activate(var7, 253219157);
                            }
                        }
                    }
                }
            }
        }
    }

    private void activate(Player var1, int var2) {
        final UUID var11 = var1.getUniqueId();
        Component var33 = Text.legacy("&e\ud83d\udd2e &cYou have activated §eTerminal Velocity", 373192400);
        var1.sendMessage(var33);
        PotionEffectType var5 = PotionEffectType.SPEED;
        PotionEffect var34 = new PotionEffect(var5, 200, 3, false, true, true);
        var1.addPotionEffect(var34);
        PotionEffectType var48 = PotionEffectType.HASTE;
        PotionEffect var35 = new PotionEffect(var48, 200, 1, false, true, true);
        var1.addPotionEffect(var35);
        long var31;
        if (GlobalState.isHoldingDragonEgg(var1, 2044938455)) {
            var31 = 20000L;
        } else {
            var31 = 40000L;
        }

        Map var24 = this.state.terminalCd;
        long var40 = System.currentTimeMillis();
        PlayerDataStore var58 = this.players;
        int var60 = var58.get(var11, 1264010596).energy(1160853318);
        long var53 = EnergyScale.cd(var31, var60, 907518618);
        Long var44 = var40 + var53;
        var24.put(var11, var44);
        Set var27 = this.active;
        var27.add(var11);
        BukkitRunnable var29 = new BukkitRunnable() {
            public void run() {
                Set var4 = LegacyTerminalVelocityListener.this.active;
                UUID var6 = var11;
                var4.remove(var6);
            }
        };
        BlissPlugin var39 = this.plugin;
        var29.runTaskLater(var39, 200L);
    }

    private boolean tryEat(Player var1, int var2) {
        UUID var10 = var1.getUniqueId();
        Map var16 = this.lastEat;
        Long var11 = (Long)var16.get(var10);
        if (var11 != null) {
            long var21 = var11;
            long var5 = System.currentTimeMillis();
            if (var21 > var5) {
                return false;
            }
        }

        ItemStack var12 = var1.getInventory().getItemInMainHand();
        if (var12 == null) {
            return false;
        } else {
            LegacyTerminalVelocityListener.Food var31 = switch (var12.getType()) {
                case GOLDEN_APPLE-> LegacyTerminalVelocityListener.Food.GOLDEN_APPLE;
                case GOLDEN_CARROT-> LegacyTerminalVelocityListener.Food.GOLDEN_CARROT;
                case COOKED_BEEF-> LegacyTerminalVelocityListener.Food.COOKED_BEEF;
                case COOKED_PORKCHOP-> LegacyTerminalVelocityListener.Food.COOKED_PORKCHOP;
                default -> null;
            };
            if (var31 == null) {
                return false;
            } else {
                LegacyTerminalVelocityListener.Food var49 = LegacyTerminalVelocityListener.Food.GOLDEN_APPLE;
                if (var31 != var49 && var1.getFoodLevel() >= 20) {
                    return false;
                } else {
                    Map var39 = this.lastEat;
                    Long var59 = System.currentTimeMillis() + 800L;
                    var39.put(var10, var59);
                    BukkitScheduler var41 = Bukkit.getScheduler();
                    BlissPlugin var53 = this.plugin;
                    Runnable var61 = () -> {
                        Map var4 = this.lastEat;
                        var4.remove(var10);
                    };
                    var41.runTaskLater(var53, var61, 16L);
                    eat(var1, var31, var12, 1556172454);
                    return true;
                }
            }
        }
    }

    private static void eat(Player var0, LegacyTerminalVelocityListener.Food var1, ItemStack var2, int var3) {
        Location var26 = var0.getLocation();
        Sound var6 = Sound.ENTITY_PLAYER_BURP;
        var0.playSound(var26, var6, 1.0F, 1.0F);
        int var29 = var2.getAmount() - 1;
        var2.setAmount(var29);
        switch (var1) {
            case GOLDEN_APPLE:
                PotionEffectType var45 = PotionEffectType.ABSORPTION;
                PotionEffect var30 = new PotionEffect(var45, 2400, 0, false, true, true);
                var0.addPotionEffect(var30);
                PotionEffectType var46 = PotionEffectType.REGENERATION;
                PotionEffect var31 = new PotionEffect(var46, 100, 0, false, true, true);
                var0.addPotionEffect(var31);
                saturate(var0, 11.0F, 9.6F, 1830902952);
                feed(var0, 16, 4, 161490615);
                break;
            case GOLDEN_CARROT:
                saturate(var0, 6.0F, 14.4F, 1830902952);
                feed(var0, 14, 6, 161490615);
                break;
            case COOKED_BEEF:
            case COOKED_PORKCHOP:
                saturate(var0, 8.0F, 12.8F, 1830902952);
                feed(var0, 12, 8, 161490615);
        }
    }

    private static void saturate(Player var0, float var1, float var2, int var3) {
        float var7 = var0.getSaturation();
        if (var7 > var1) {
            var0.setSaturation(20.0F);
        } else {
            float var12 = var0.getSaturation();
            float var13 = var12 + var2;
            var0.setSaturation(var13);
        }
    }

    private static void feed(Player var0, int var1, int var2, int var3) {
        int var7 = var0.getFoodLevel();
        if (var7 > var1) {
            var0.setFoodLevel(20);
        } else {
            int var12 = var0.getFoodLevel();
            int var13 = var12 + var2;
            var0.setFoodLevel(var13);
        }
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

    private static String readable(long var0, int var2) {
        int var8 = (int)((var0 + 999L) / 1000L);
        int var9 = var8 / 60;
        int var10 = var8 % 60;
        if (var9 == 0) {
            return var10 + "s";
        } else {
            return var10 == 0 ? var9 + "m" : var9 + "m " + var10 + "s";
        }
    }

    private static enum Food {
        GOLDEN_APPLE,
        GOLDEN_CARROT,
        COOKED_BEEF,
        COOKED_PORKCHOP;
    }
}
