package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.EnergyScale;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.ItemEscrow;
import com.bliss.core.PlayerDataStore;
import com.bliss.goldgem.GoldGem;
import com.bliss.honored.GemAbilityRules;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.logging.Logger;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Camel;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class AstraDimensionalDriftListener implements Listener {
    private static final long DRIFT_DURATION_TICKS = 100L;
    private static final long POST_DRIFT_AVAILABILITY_TICKS = 60L;
    private static final long FULL_COOLDOWN_MS = 45000L;
    private static final long INTERRUPTED_COOLDOWN_MS = 35000L;
    private static final int INVIS_DURATION_TICKS = 200;
    private static final int CAMEL_MAX_HEALTH = 53;
    private static final double CAMEL_MOVEMENT_SPEED = 0.2;
    private static final double CAMEL_JUMP_STRENGTH = 0.6;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final AstraGemListener astra;
    private final Map<UUID, AstraDimensionalDriftListener.DriftState> drifts;
    private final Set<UUID> doubleClickLatch;
    private final Set<UUID> astraDodgeLockout;
    private final Map<UUID, Integer> lastInteractTick;
    private final ItemEscrow armorEscrow;
    static final NamespacedKey DISGUISE_KEY;

    public AstraDimensionalDriftListener(BlissPlugin var1, PlayerDataStore var2, AstraGemListener var3, int var4) {
        ConcurrentHashMap var6 = new ConcurrentHashMap();
        this.drifts = var6;
        KeySetView var17 = ConcurrentHashMap.newKeySet();
        this.doubleClickLatch = var17;
        KeySetView var18 = ConcurrentHashMap.newKeySet();
        this.astraDodgeLockout = var18;
        ConcurrentHashMap var19 = new ConcurrentHashMap();
        this.lastInteractTick = var19;
        this.plugin = var1;
        this.players = var2;
        this.astra = var3;
        ItemEscrow var23 = new ItemEscrow(var1, "astra_drift_armor.yml", 528395154);
        this.armorEscrow = var23;
    }

    @EventHandler
    public void onJoinRecoverArmor(PlayerJoinEvent var1) {
        Player var6 = var1.getPlayer();
        ItemEscrow var12 = this.armorEscrow;
        UUID var42 = var6.getUniqueId();
        ItemStack[] var7 = var12.release(var42, 1427941333);
        if (var7 != null && var7.length >= 4) {
            ItemStack[] var18 = new ItemStack[4];
            ItemStack var71 = var6.getInventory().getHelmet();
            var18[0] = var71;
            ItemStack var74 = var6.getInventory().getChestplate();
            var18[1] = var74;
            ItemStack var77 = var6.getInventory().getLeggings();
            var18[2] = var77;
            ItemStack var80 = var6.getInventory().getBoots();
            var18[3] = var80;
            ItemStack[] var8 = var18;

            for (int var9 = 0; var9 < 4; var9++) {
                if (isDisguiseButton(var8[var9])) {
                    switch (var9) {
                        case 0:
                            PlayerInventory var30 = var6.getInventory();
                            ItemStack var51 = var7[var9];
                            var30.setHelmet(var51);
                            break;
                        case 1:
                            PlayerInventory var28 = var6.getInventory();
                            ItemStack var49 = var7[var9];
                            var28.setChestplate(var49);
                            break;
                        case 2:
                            PlayerInventory var32 = var6.getInventory();
                            ItemStack var53 = var7[var9];
                            var32.setLeggings(var53);
                            break;
                        default:
                            PlayerInventory var26 = var6.getInventory();
                            ItemStack var47 = var7[var9];
                            var26.setBoots(var47);
                    }
                } else if (var7[var9] != null) {
                    Material var37 = var7[var9].getType();
                    Material var56 = Material.AIR;
                    if (var37 != var56) {
                        ItemStack var58 = var7[var9];
                        BlissCoreItems.giveOrDrop(var6, var58, 41085121);
                    }
                }
            }

            Logger var41 = this.plugin.getLogger();
            String var61 = "Returned armour held by an interrupted Dimensional Drift to " + var6.getName() + ".";
            var41.info(var61);
        }
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

        Player var7 = var1.getPlayer();
        UUID var8 = var7.getUniqueId();
        int var9 = Bukkit.getCurrentTick();
        Map var27 = this.lastInteractTick;
        Integer var10 = (Integer)var27.get(var8);
        if (var10 != null) {
            int var32 = var10;
            if (var32 == var9) {
                return;
            }
        }

        Map var34 = this.lastInteractTick;
        Integer var128 = var9;
        var34.put(var8, var128);
        if (!var7.isSneaking()) {
            if (!BlissCoreItems.isUtilityHead(var7.getInventory().getItemInMainHand(), 1077770594)) {
                ItemStack var11 = var7.getInventory().getItemInOffHand();
                int var46 = BlissCoreItems.gemIdOf(var11, 382441079);
                int var106 = GemType.ASTRA.id();
                if (var46 != var106) {
                    GemType var107 = GemType.ASTRA;
                    if (!GoldGem.isWieldingSoulOff(var7, var107, 1989261555)) {
                        return;
                    }
                }

                int var50 = BlissCoreItems.gemIdOf(var11, 382441079);
                int var109 = GemType.ASTRA.id();
                if (var50 != var109) {
                    ItemStack var12 = var7.getInventory().getItemInOffHand();
                    if (GoldGem.isGoldGem(var12)) {
                        var11 = var12;
                    }
                }

                int var137 = BlissCoreItems.tierOf(var11, 1633828214);
                PlayerDataStore var60 = this.players;
                if (var60.get(var8, 1264010596).canUseAbilities(325243089)) {
                    Set var65 = this.astraDodgeLockout;
                    if (!var65.contains(var8)) {
                        Material var14 = var7.getInventory().getItemInMainHand().getType();
                        Material var112 = Material.AIR;
                        if (var14 == var112 || isSword(var14, 214470704) || isAxe(var14, 1149121082)) {
                            long var15 = System.currentTimeMillis();
                            Map var80 = this.astra.cd1;
                            Long var17 = (Long)var80.get(var8);
                            if (var17 != null) {
                                long var85 = var17;
                                if (var85 > var15) {
                                    long var115 = var17;
                                    Component var121 = Text.legacy(
                                        "<##A01FFF>\ud83d\udd2e <##FF686F>Your <##C7C7C7>Dimensional Drift <##FF686F>skill is on cooldown for <##A01FFF>"
                                            + readable(var115 - var15, 1487332436),
                                        373192400
                                    );
                                    var7.sendMessage(var121);
                                    return;
                                }
                            }

                            Set var90 = this.doubleClickLatch;
                            if (!var90.contains(var8)) {
                                Set var93 = this.doubleClickLatch;
                                var93.add(var8);
                                BukkitScheduler var95 = Bukkit.getScheduler();
                                BlissPlugin var125 = this.plugin;
                                Runnable var132 = () -> {
                                    Set var4 = this.doubleClickLatch;
                                    var4.remove(var8);
                                };
                                var95.runTaskLater(var125, var132, 20L);
                            } else {
                                Set var98 = this.doubleClickLatch;
                                var98.remove(var8);
                                this.activate(var7, var137, 984916167);
                            }
                        }
                    }
                }
            }
        }
    }

    private void activate(Player var1, int var2, int var3) {
        if (GemAbilityRules.on(GemAbilityRules.GemAbility.ASTRA_DRIFT, 425337338)) {
            UUID var14 = var1.getUniqueId();
            Component var138 = Text.legacy("<##A01FFF>\ud83d\udd2e <##FF686F>You used <##C7C7C7>Dimensional Drift", 373192400);
            var1.sendMessage(var138);
            World var38 = var1.getWorld();
            Location var140 = var1.getLocation();
            EntityType var6 = EntityType.CAMEL;
            Camel var15 = (Camel)var38.spawnEntity(var140, var6);
            Attribute var141 = Attribute.MOVEMENT_SPEED;
            AttributeInstance var16 = var15.getAttribute(var141);
            if (var16 != null) {
                var16.setBaseValue(0.2);
            }

            Attribute var144 = Attribute.JUMP_STRENGTH;
            AttributeInstance var17 = var15.getAttribute(var144);
            if (var17 != null) {
                var17.setBaseValue(0.6);
            }

            Attribute var147 = Attribute.MAX_HEALTH;
            AttributeInstance var18 = var15.getAttribute(var147);
            if (var18 != null) {
                var18.setBaseValue(53.0);
            }

            var15.setHealth(53.0);
            var15.setTamed(true);
            var15.setOwner(var1);
            var15.setInvisible(true);
            var15.setInvulnerable(true);
            var15.setRemoveWhenFarAway(false);
            var15.setPersistent(false);
            if (var15.getInventory() != null) {
                AbstractHorseInventory var63 = var15.getInventory();
                Material var7 = Material.SADDLE;
                ItemStack var158 = new ItemStack(var7);
                var63.setSaddle(var158);
            }

            var15.addPassenger(var1);
            AstraPassivesListener var19 = this.plugin.astraPassives(2128076535);
            if (var19 != null) {
                var19.forceEndPhase(var1, 1476884501);
            }

            PotionEffectType var202 = PotionEffectType.INVISIBILITY;
            String var8 = "astra";
            String var9 = "drift-invisibility";
            int var210 = (int)(GemTuning.duration(var8, var9, 10.0, 812730832) * 20.0);
            PotionEffect var161 = new PotionEffect(var202, var210, 0, true, false, false);
            GrantedEffect.grant(var1, var161, 491118892);
            ItemStack var20 = var1.getInventory().getHelmet();
            ItemStack var21 = var1.getInventory().getChestplate();
            ItemStack var22 = var1.getInventory().getLeggings();
            ItemStack var23 = var1.getInventory().getBoots();
            if (isDisguiseButton(var20)) {
                var20 = null;
            }

            if (isDisguiseButton(var21)) {
                var21 = null;
            }

            if (isDisguiseButton(var22)) {
                var22 = null;
            }

            if (isDisguiseButton(var23)) {
                var23 = null;
            }

            PlayerInventory var98 = var1.getInventory();
            ItemStack var163 = disguise(var20, 1950883261);
            var98.setHelmet(var163);
            PlayerInventory var100 = var1.getInventory();
            ItemStack var165 = disguise(var21, 1950883261);
            var100.setChestplate(var165);
            PlayerInventory var102 = var1.getInventory();
            ItemStack var167 = disguise(var22, 1950883261);
            var102.setLeggings(var167);
            PlayerInventory var104 = var1.getInventory();
            ItemStack var169 = disguise(var23, 1950883261);
            var104.setBoots(var169);
            UUID var178 = var15.getUniqueId();
            AstraDimensionalDriftListener.DriftState var105 = new AstraDimensionalDriftListener.DriftState(var178, var20, var21, var22, var23, 91226611);
            Map var107 = this.drifts;
            var107.put(var14, var105);
            ItemEscrow var110 = this.armorEscrow;
            ItemStack[] var181 = new ItemStack[]{var20, var21, var22, var23};
            var110.hold(var14, var181, 1481203625);
            long var136;
            if (GlobalState.isHoldingDragonEgg(var1, 2044938455)) {
                var136 = 22500L;
            } else {
                var136 = 45000L;
            }

            String var114 = "astra";
            String var172 = "drift";
            long var27 = (long)(GemTuning.duration(var114, var172, 5.0, 812730832) * 20.0);
            long var29 = var27 * 50L;
            long var31 = System.currentTimeMillis();
            Map var129 = this.astra.driftActiveUntil;
            Long var192 = var31 + var29;
            var129.put(var14, var192);
            Map var133 = this.astra.cd1;
            long var195 = var31 + var29;
            String var220 = "astra";
            String var234 = "dimensional-drift";
            long var221 = GemTuning.cd(var220, var234, 45.0, var136, 1621983041);
            PlayerDataStore var242 = this.players;
            int var244 = var242.get(var14, 1264010596).energy(1160853318);
            long var223 = EnergyScale.cd(var221, var244, 907518618);
            Long var199 = var195 + var223;
            var133.put(var14, var199);
            BukkitScheduler var175 = Bukkit.getScheduler();
            BlissPlugin var201 = this.plugin;
            Runnable var205 = () -> this.cleanup(var1, false, 1796026085);
            BukkitTask var176 = var175.runTaskLater(var201, var205, var27);
            var105.endTask = var176;
        }
    }

    private void cleanup(Player var1, boolean var2, int var3) {
        this.cleanup(var1, var2, false, 1243405624);
    }

    private void cleanup(Player var1, boolean var2, boolean var3, int var4) {
        UUID var16 = var1.getUniqueId();
        Map var23 = this.drifts;
        AstraDimensionalDriftListener.DriftState var17 = (AstraDimensionalDriftListener.DriftState)var23.remove(var16);
        ItemEscrow var27 = this.armorEscrow;
        var27.release(var16, 1427941333);
        if (var17 != null) {
            Map var32 = this.astra.driftActiveUntil;
            var32.remove(var16);
            Entity var18 = Bukkit.getEntity(var17.camelUid);
            if (var18 != null && var18.isValid()) {
                var18.remove();
            }

            ItemStack var85 = var17.savedHelmet;
            boolean var99;
            if (!var3) {
                var99 = true;
            } else {
                var99 = false;
            }

            restoreArmorSlot(var1, 0, var85, var99, 1559118504);
            ItemStack var87 = var17.savedChestplate;
            boolean var108;
            if (!var3) {
                var108 = true;
            } else {
                var108 = false;
            }

            restoreArmorSlot(var1, 1, var87, var108, 1559118504);
            ItemStack var89 = var17.savedLeggings;
            boolean var102;
            if (!var3) {
                var102 = true;
            } else {
                var102 = false;
            }

            restoreArmorSlot(var1, 2, var89, var102, 1559118504);
            ItemStack var91 = var17.savedBoots;
            boolean var104;
            if (!var3) {
                var104 = true;
            } else {
                var104 = false;
            }

            restoreArmorSlot(var1, 3, var91, var104, 1559118504);
            sweepDisguises(var1, 230949872);
            if (var2 || var3) {
                PotionEffectType var79 = PotionEffectType.INVISIBILITY;
                GrantedEffect.strip(var1, var79, 553187889);
            }

            if (var2) {
                long var71;
                if (GlobalState.isHoldingDragonEgg(var1, 2044938455)) {
                    var71 = 17500L;
                } else {
                    var71 = 35000L;
                }

                Map var54 = this.astra.cd1;
                long var92 = System.currentTimeMillis();
                String var9 = "astra";
                String var10 = "dimensional-drift-interrupted";
                long var109 = GemTuning.cd(var9, var10, 35.0, var71, 1621983041);
                PlayerDataStore var114 = this.players;
                int var116 = var114.get(var16, 1264010596).energy(1160853318);
                long var111 = EnergyScale.cd(var109, var116, 907518618);
                Long var96 = var92 + var111;
                var54.put(var16, var96);
            }

            if (var17.endTask != null) {
                var17.endTask.cancel();
            }

            if (!this.plugin.isEnabled()) {
                Set var64 = this.astraDodgeLockout;
                var64.remove(var16);
            } else {
                Set var67 = this.astraDodgeLockout;
                var67.add(var16);
                BukkitScheduler var69 = Bukkit.getScheduler();
                BlissPlugin var84 = this.plugin;
                Runnable var98 = () -> {
                    Set var4x = this.astraDodgeLockout;
                    var4x.remove(var16);
                };
                var69.runTaskLater(var84, var98, 60L);
            }
        }
    }

    @EventHandler
    public void onQuitMidDrift(PlayerQuitEvent var1) {
        Player var5 = var1.getPlayer();
        Map var8 = this.drifts;
        UUID var11 = var5.getUniqueId();
        if (var8.containsKey(var11)) {
            this.cleanup(var5, false, 1796026085);
        }
    }

    @EventHandler(
        priority = EventPriority.LOWEST
    )
    public void onDeathMidDrift(PlayerDeathEvent var1) {
        Player var6 = var1.getPlayer();
        Map var10 = this.drifts;
        UUID var19 = var6.getUniqueId();
        AstraDimensionalDriftListener.DriftState var7 = (AstraDimensionalDriftListener.DriftState)var10.get(var19);
        if (var7 != null) {
            if (!var1.getKeepInventory()) {
                List var17 = var1.getDrops();
                ItemStack[] var21 = new ItemStack[4];
                ItemStack var28 = var7.savedHelmet;
                var21[0] = var28;
                ItemStack var30 = var7.savedChestplate;
                var21[1] = var30;
                ItemStack var32 = var7.savedLeggings;
                var21[2] = var32;
                ItemStack var34 = var7.savedBoots;
                var21[3] = var34;
                swapDisguiseDrops(var17, var21, 924249232);
            }

            this.cleanup(var6, false, true, 1243405624);
        }
    }

    static void swapDisguiseDrops(List<ItemStack> var0, ItemStack[] var1, int var2) {
        Predicate<ItemStack> var4 = AstraDimensionalDriftListener::isDisguiseButton;
        var0.removeIf(var4);

        for (ItemStack var8 : var1) {
            if (var8 != null) {
                Material var19 = var8.getType();
                Material var24 = Material.AIR;
                if (var19 != var24) {
                    ItemStack var26 = var8.clone();
                    var0.add(var26);
                }
            }
        }
    }

    static void restoreArmorSlot(Player var0, int var1, ItemStack var2) {
        restoreArmorSlot(var0, var1, var2, true, 1559118504);
    }

    static void restoreArmorSlot(Player var0, int var1, ItemStack var2, boolean var3, int var4) {
        PlayerInventory var7 = var0.getInventory();

        if (isDisguiseButton(switch (var1) {
            case 0 -> var7.getHelmet();
            case 1 -> var7.getChestplate();
            case 2 -> var7.getLeggings();
            default -> var7.getBoots();
        })) {
            switch (var1) {
                case 0:
                    var7.setHelmet(var2);
                    break;
                case 1:
                    var7.setChestplate(var2);
                    break;
                case 2:
                    var7.setLeggings(var2);
                    break;
                default:
                    var7.setBoots(var2);
            }
        } else if (var3 && var2 != null) {
            Material var22 = var2.getType();
            Material var31 = Material.AIR;
            if (var22 != var31) {
                BlissCoreItems.giveOrDrop(var0, var2, 41085121);
            }
        }
    }

    static void sweepDisguises(Player var0, int var1) {
        ItemStack[] var5 = var0.getInventory().getContents();
        int var6 = 0;

        while (true) {
            int var16 = var5.length;
            if (var6 >= var16) {
                return;
            }

            if (isDisguiseButton(var5[var6])) {
                PlayerInventory var15 = var0.getInventory();
                var15.setItem(var6, null);
            }

            var6++;
        }
    }

    static boolean wearingDisguise(Player var0, int var1) {
        ItemStack[] var4 = var0.getInventory().getArmorContents();
        int var5 = var4.length;

        for (int var6 = 0; var6 < var5; var6++) {
            if (isDisguiseButton(var4[var6])) {
                return true;
            }
        }

        return false;
    }

    static boolean isDisguiseButton(ItemStack var0) {
        if (var0 != null) {
            Material var6 = var0.getType();
            Material var2 = Material.CRIMSON_BUTTON;
            if (var6 == var2) {
                ItemMeta var4 = var0.getItemMeta();
                if (var4 != null) {
                    PersistentDataContainer var11 = var4.getPersistentDataContainer();
                    NamespacedKey var15 = DISGUISE_KEY;
                    PersistentDataType var3 = PersistentDataType.BYTE;
                    if (var11.has(var15, var3)) {
                        return true;
                    }
                }

                return false;
            }
        }

        return false;
    }

    public void restoreAll(int var1) {
        Set<UUID> var38 = this.drifts.keySet();

        for (UUID var5 : new ArrayList<>(var38)) {
            Player var6 = Bukkit.getPlayer(var5);
            if (var6 != null) {
                this.cleanup(var6, false, 1796026085);
            } else {
                Map var20 = this.drifts;
                AstraDimensionalDriftListener.DriftState var7 = (AstraDimensionalDriftListener.DriftState)var20.remove(var5);
                if (var7 != null) {
                    Entity var8 = Bukkit.getEntity(var7.camelUid);
                    if (var8 != null && var8.isValid()) {
                        var8.remove();
                    }

                    if (var7.endTask != null) {
                        var7.endTask.cancel();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSneakInterrupt(PlayerToggleSneakEvent var1) {
        if (var1.isSneaking()) {
            Player var5 = var1.getPlayer();
            Map var10 = this.drifts;
            UUID var13 = var5.getUniqueId();
            if (var10.containsKey(var13)) {
                this.cleanup(var5, true, 1796026085);
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onDamageInterrupt(EntityDamageEvent var1) {
        if (var1.getEntity() instanceof Player var5) {
            Map var13 = this.drifts;
            UUID var17 = var5.getUniqueId();
            if (var13.containsKey(var17)) {
                var1.setCancelled(true);
                this.cleanup(var5, true, 1796026085);
            }
        }
    }

    private static ItemStack disguise(ItemStack var0, int var1) {
        if (var0 != null) {
            Material var10 = var0.getType();
            Material var3 = Material.AIR;
            if (var10 != var3) {
                Material var4 = Material.CRIMSON_BUTTON;
                ItemStack var12 = new ItemStack(var4);
                ItemMeta var7 = var0.getItemMeta();
                ItemMeta var8 = var12.getItemMeta();
                if (var8 != null) {
                    if (var7 != null) {
                        var7.getEnchants().forEach((var1x, var2) -> {
                            int var8x = var2;
                            var8.addEnchant(var1x, var8x, true);
                        });
                    }

                    PersistentDataContainer var22 = var8.getPersistentDataContainer();
                    NamespacedKey var28 = DISGUISE_KEY;
                    PersistentDataType var30 = PersistentDataType.BYTE;
                    Byte var31 = (byte)1;
                    var22.set(var28, var30, var31);
                    var12.setItemMeta(var8);
                }

                return var12;
            }
        }

        return var0;
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

    static {
        String var1 = "bliss";
        String var2 = "astra_disguise";
        NamespacedKey var0 = new NamespacedKey(var1, var2);
        DISGUISE_KEY = var0;
    }

    private static final class DriftState {
        final UUID camelUid;
        final ItemStack savedHelmet;
        final ItemStack savedChestplate;
        final ItemStack savedLeggings;
        final ItemStack savedBoots;
        BukkitTask endTask;

        DriftState(UUID var1, ItemStack var2, ItemStack var3, ItemStack var4, ItemStack var5, int var6) {
            this.camelUid = var1;
            ItemStack var25;
            if (var2 == null) {
                var25 = null;
            } else {
                var25 = var2.clone();
            }

            this.savedHelmet = var25;
            ItemStack var24;
            if (var3 == null) {
                var24 = null;
            } else {
                var24 = var3.clone();
            }

            this.savedChestplate = var24;
            ItemStack var23;
            if (var4 == null) {
                var23 = null;
            } else {
                var23 = var4.clone();
            }

            this.savedLeggings = var23;
            ItemStack var20;
            if (var5 == null) {
                var20 = null;
            } else {
                var20 = var5.clone();
            }

            this.savedBoots = var20;
        }
    }
}
