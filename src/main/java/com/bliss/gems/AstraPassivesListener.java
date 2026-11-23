package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.AchievementStore;
import com.bliss.core.AstraSoulStore;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.Particles;
import com.bliss.core.PlayerDataStore;
import com.bliss.goldgem.GoldGem;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Warden;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

public final class AstraPassivesListener implements Listener {
    private static final double PHASE_CHANCE = 0.05;
    private static final double HEAL_TRIGGER_HP = 4.0;
    private static final int ORB_TICKS = 60;
    private static final double ORB_RADIUS = 0.65;
    private static final int ORB_PARTICLES_PER_TICK = 20;
    private static final double PHASE_ORB_RADIUS = 0.4;
    private static final int PHASE_ORB_PARTICLES_PER_TICK = 8;
    private static final double PHASE_ORB_Y = 1.25;
    private static final int PHASE_ORB_TICKS = 4;
    private static final Color PHASE_ORB_COLOR = Color.fromRGB(210, 160, 255);
    private static final Color ASTRA_PURPLE = Color.fromRGB(160, 31, 255);
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final AstraSoulStore souls;
    private final Random random;
    private final NamespacedKey summonedKey;
    private final NamespacedKey summonerKey;
    private static final int PHASE_INVIS_TICKS = 20;
    private final Map<UUID, ItemStack[]> phaseArmor;
    private final Map<UUID, Integer> phased;
    private int phaseSeq;
    private final Map<UUID, Integer> lastSummonTick;

    public AstraPassivesListener(BlissPlugin var1, PlayerDataStore var2, AstraSoulStore var3, int var4) {
        Random var6 = new Random();
        this.random = var6;
        ConcurrentHashMap var19 = new ConcurrentHashMap();
        this.phaseArmor = var19;
        ConcurrentHashMap var20 = new ConcurrentHashMap();
        this.phased = var20;
        this.phaseSeq = 0;
        HashMap var22 = new HashMap();
        this.lastSummonTick = var22;
        this.plugin = var1;
        this.players = var2;
        this.souls = var3;
        NamespacedKey var26 = new NamespacedKey(var1, "astra_soul_summoned");
        this.summonedKey = var26;
        NamespacedKey var27 = new NamespacedKey(var1, "astra_soul_summoner");
        this.summonerKey = var27;
    }

    @EventHandler(
        priority = EventPriority.LOW,
        ignoreCancelled = true
    )
    public void onPhasing(EntityDamageEvent var1) {
        if (var1.getEntity() instanceof Player var12) {
            UUID var94 = var12.getUniqueId();
            Map var23 = this.phased;
            if (var23.containsKey(var94)) {
                DamageCause var26 = var1.getCause();
                DamageCause var58 = DamageCause.KILL;
                if (var26 != var58) {
                    DamageCause var28 = var1.getCause();
                    DamageCause var59 = DamageCause.SUICIDE;
                    if (var28 != var59) {
                        var1.setCancelled(true);
                        return;
                    }
                }
            }

            Map var31 = this.phaseArmor;
            if (var31.containsKey(var94)) {
                this.endPhase(var12, 704428567);
            }

            if (this.holdsAstra(var12, 388454034)) {
                PlayerDataStore var37 = this.players;
                if (var37.get(var94, 1264010596).canUsePassives(2051432579)) {
                    double var42 = this.random.nextDouble();
                    String var4 = "astra";
                    String var5 = "phasing";
                    double var75 = GemTuning.chance(var4, var5, 5.0, 1091538029) / 100.0;
                    if (!(var42 >= var75)) {
                        double var46 = var1.getFinalDamage();
                        double var78 = var12.getHealth();
                        boolean var57;
                        if (var46 >= var78) {
                            var57 = true;
                        } else {
                            var57 = false;
                        }

                        var1.setCancelled(true);
                        if (var57) {
                            AchievementStore var53 = this.plugin.advancements(1975441698);
                            AchievementStore.Advancement var80 = AchievementStore.Advancement.SAVED_BY_DICE;
                            var53.unlock(var12, var80, 1132982324);
                        }

                        this.startPhase(var12, 1297194666);
                        Location var70 = var12.getLocation().add(0.0, 1.25, 0.0);
                        Color var93 = PHASE_ORB_COLOR;
                        this.spawnCaptureOrb(var70, 0.4, 8, 4, var93, 1.0F, 1417817111);
                        Location var72 = var12.getLocation();
                        Sound var85 = Sound.ENTITY_ENDERMAN_TELEPORT;
                        var12.playSound(var72, var85, 0.4F, 1.6F);
                    }
                }
            }
        }
    }

    private void startPhase(Player var1, int var2) {
        UUID var11 = var1.getUniqueId();
        Map var17 = this.phaseArmor;
        if (!var17.containsKey(var11)) {
            int var53 = this.phaseSeq + 1;
            this.phaseSeq = var53;
            Map var21 = this.phased;
            Integer var75 = var53;
            var21.put(var11, var75);
            PlayerInventory var13 = var1.getInventory();
            if (!isDisguise(var13.getHelmet(), 1063701855)
                && !isDisguise(var13.getChestplate(), 1063701855)
                && !isDisguise(var13.getLeggings(), 1063701855)
                && !isDisguise(var13.getBoots(), 1063701855)) {
                ItemStack[] var38 = new ItemStack[4];
                ItemStack var90 = var13.getHelmet();
                var38[0] = var90;
                ItemStack var92 = var13.getChestplate();
                var38[1] = var92;
                ItemStack var94 = var13.getLeggings();
                var38[2] = var94;
                ItemStack var96 = var13.getBoots();
                var38[3] = var96;
                Map var40 = this.phaseArmor;
                var40.put(var11, var38);
                ItemStack var58 = disguise(var38[0], 1541076611);
                var13.setHelmet(var58);
                ItemStack var61 = disguise(var38[1], 1541076611);
                var13.setChestplate(var61);
                ItemStack var64 = disguise(var38[2], 1541076611);
                var13.setLeggings(var64);
                ItemStack var67 = disguise(var38[3], 1541076611);
                var13.setBoots(var67);
                PotionEffectType var97 = PotionEffectType.INVISIBILITY;
                PotionEffect var68 = new PotionEffect(var97, 20, 0, true, false, false);
                GrantedEffect.grant(var1, var68, 491118892);
                BukkitScheduler var47 = Bukkit.getScheduler();
                BlissPlugin var70 = this.plugin;
                Runnable var86 = () -> {
                    Map var7 = this.phased;
                    Integer var6 = (Integer)var7.get(var11);
                    if (var6 != null) {
                        int var12 = var6;
                        if (var12 == var53) {
                            this.endPhase(var1, 704428567);
                        }
                    }
                };
                var47.runTaskLater(var70, var86, 20L);
            } else {
                PotionEffectType var101 = PotionEffectType.INVISIBILITY;
                PotionEffect var71 = new PotionEffect(var101, 20, 0, true, false, false);
                GrantedEffect.grant(var1, var71, 491118892);
                BukkitScheduler var50 = Bukkit.getScheduler();
                BlissPlugin var73 = this.plugin;
                Runnable var88 = () -> {
                    Map var6 = this.phased;
                    Integer var8 = var53;
                    var6.remove(var11, var8);
                };
                var50.runTaskLater(var73, var88, 20L);
            }
        }
    }

    private void endPhase(Player var1, int var2) {
        this.endPhase(var1, true, 751050427);
    }

    private void endPhase(Player var1, boolean var2, int var3) {
        Map var10 = this.phased;
        UUID var22 = var1.getUniqueId();
        var10.remove(var22);
        Map var13 = this.phaseArmor;
        UUID var24 = var1.getUniqueId();
        ItemStack[] var8 = (ItemStack[])var13.remove(var24);
        if (var8 != null) {
            for (int var9 = 0; var9 < 4; var9++) {
                ItemStack var28 = var8[var9];
                AstraDimensionalDriftListener.restoreArmorSlot(var1, var9, var28, var2, 1559118504);
            }

            AstraDimensionalDriftListener.sweepDisguises(var1, 230949872);
            PotionEffectType var26 = PotionEffectType.INVISIBILITY;
            GrantedEffect.strip(var1, var26, 553187889);
        }
    }

    public boolean forceEndPhase(Player var1, int var2) {
        if (var1 != null) {
            Map var6 = this.phaseArmor;
            UUID var11 = var1.getUniqueId();
            if (var6.containsKey(var11)) {
                this.endPhase(var1, 704428567);
                return true;
            }
        }

        return false;
    }

    @EventHandler
    public void onQuitMidPhase(PlayerQuitEvent var1) {
        Map var4 = this.phaseArmor;
        UUID var8 = var1.getPlayer().getUniqueId();
        if (var4.containsKey(var8)) {
            Player var10 = var1.getPlayer();
            this.endPhase(var10, 704428567);
        }
    }

    @EventHandler(
        priority = EventPriority.LOWEST
    )
    public void onDeathMidPhase(PlayerDeathEvent var1) {
        Player var5 = var1.getPlayer();
        Map var9 = this.phaseArmor;
        UUID var18 = var5.getUniqueId();
        ItemStack[] var6 = (ItemStack[])var9.get(var18);
        if (var6 != null) {
            if (!var1.getKeepInventory()) {
                List var16 = var1.getDrops();
                AstraDimensionalDriftListener.swapDisguiseDrops(var16, var6, 924249232);
            }

            this.endPhase(var5, false, 751050427);
        }
    }

    public void restoreAll(int var1) {
        Set<UUID> var23 = this.phaseArmor.keySet();

        for (UUID var5 : new ArrayList<>(var23)) {
            Player var6 = Bukkit.getPlayer(var5);
            if (var6 != null) {
                this.endPhase(var6, 704428567);
            } else {
                Map var17 = this.phaseArmor;
                var17.remove(var5);
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
                    NamespacedKey var28 = AstraDimensionalDriftListener.DISGUISE_KEY;
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

    private static boolean isDisguise(ItemStack var0, int var1) {
        if (var0 != null) {
            Material var5 = var0.getType();
            Material var3 = Material.CRIMSON_BUTTON;
            if (var5 == var3) {
                return true;
            }
        }

        return false;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent var1) {
        LivingEntity var11 = var1.getEntity();
        PersistentDataContainer var23 = var11.getPersistentDataContainer();
        NamespacedKey var94 = this.summonedKey;
        PersistentDataType var4 = PersistentDataType.BYTE;
        Byte var12 = (Byte)var23.get(var94, var4);
        if (var12 != null && var12 == 1) {
            var1.getDrops().clear();
            var1.setDroppedExp(0);
        } else {
            String var32 = "astra";
            String var97 = "soul-capture";
            if (GemTuning.passive(var32, var97, true, 14309468)) {
                if (!(var11 instanceof Player)) {
                    if (!(var11 instanceof ArmorStand)) {
                        Player var13 = var11.getKiller();
                        if (var13 != null) {
                            if (var11.getLastDamageCause() instanceof EntityDamageByEntityEvent var14) {
                                Entity var99 = var14.getDamager();
                                if (var13.equals(var99)) {
                                    DamageCause var50 = var14.getCause();
                                    DamageCause var100 = DamageCause.ENTITY_ATTACK;
                                    if (var50 != var100) {
                                        DamageCause var52 = var14.getCause();
                                        DamageCause var101 = DamageCause.ENTITY_SWEEP_ATTACK;
                                        if (var52 != var101) {
                                            return;
                                        }
                                    }

                                    ItemStack var140 = var13.getInventory().getItemInMainHand();
                                    int var57 = BlissCoreItems.gemIdOf(var140, 382441079);
                                    int var103 = GemType.ASTRA.id();
                                    if (var57 != var103) {
                                        if (!GoldGem.isGoldGem(var140)) {
                                            return;
                                        }

                                        int var61 = GoldGem.getMask(var140, 139794617);
                                        GemType var104 = GemType.ASTRA;
                                        if (!GoldGem.has(var61, var104, 949760421)) {
                                            return;
                                        }
                                    }

                                    PlayerDataStore var64 = this.players;
                                    UUID var106 = var13.getUniqueId();
                                    if (var64.get(var106, 1264010596).canUsePassives(2051432579)) {
                                        EntityType var16 = var11.getType();
                                        double var17 = 20.0;
                                        Attribute var107 = Attribute.MAX_HEALTH;
                                        AttributeInstance var19 = var11.getAttribute(var107);
                                        if (var19 != null) {
                                            var17 = var19.getBaseValue();
                                        }

                                        if (AstraSoulStore.uncapturable(var16, 1427143893)) {
                                            Component var111 = Text.legacy(
                                                "&5\ud83d\udd2e &c" + prettyName(var16, 538667302) + " is too great to bind.", 373192400
                                            );
                                            var13.sendActionBar(var111);
                                        } else {
                                            AstraSoulStore var81 = this.souls;
                                            UUID var113 = var13.getUniqueId();
                                            if (!var81.canCapture(var113, var16, 1775291803)) {
                                                Component var130 = Text.legacy(
                                                    "&5\ud83d\udd2e &cSoul slots full or already storing " + prettyName(var16, 538667302), 373192400
                                                );
                                                var13.sendActionBar(var130);
                                            } else {
                                                AstraSoulStore var84 = this.souls;
                                                UUID var115 = var13.getUniqueId();
                                                var84.capture(var115, var16, var17, 615578881);
                                                Location var118 = var11.getLocation().add(0.0, 0.5, 0.0);
                                                this.spawnCaptureOrb(var118, 123427347);
                                                AstraSoulStore var88 = this.souls;
                                                UUID var120 = var13.getUniqueId();
                                                int var20 = var88.snapshot(var120, 445866519).size();
                                                String var122 = prettyName(var16, 538667302);
                                                Component var124 = Text.legacy("&5\ud83d\udd2e &dCaptured " + var122 + " soul. &7(" + var20 + "/2)", 373192400);
                                                var13.sendMessage(var124);
                                                Location var126 = var13.getLocation();
                                                Sound var137 = Sound.BLOCK_AMETHYST_BLOCK_CHIME;
                                                var13.playSound(var126, var137, 0.7F, 1.4F);
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

    @EventHandler
    public void onSneakRightClick(PlayerInteractEvent var1) {
        Action var32 = var1.getAction();
        Action var3 = Action.RIGHT_CLICK_AIR;
        if (var32 != var3) {
            Action var34 = var1.getAction();
            Action var113 = Action.RIGHT_CLICK_BLOCK;
            if (var34 != var113) {
                return;
            }
        }

        Player var18 = var1.getPlayer();
        if (var18.isSneaking()) {
            int var19 = Bukkit.getCurrentTick();
            Map<UUID, Integer> var41 = this.lastSummonTick;
            UUID var115 = var18.getUniqueId();
            Integer var153 = var19;
            Integer var20 = var41.put(var115, var153);
            if (var20 != null) {
                int var117 = var20;
                if (var19 - var117 < 4) {
                    return;
                }
            }

            ItemStack var21 = var18.getInventory().getItemInMainHand();
            ItemStack var22 = var18.getInventory().getItemInOffHand();
            if (!BlissCoreItems.isUtilityHead(var21, 1077770594)) {
                int var56 = BlissCoreItems.gemIdOf(var21, 382441079);
                int var120 = GemType.ASTRA.id();
                boolean var57;
                if (var56 == var120) {
                    var57 = true;
                } else {
                    var57 = false;
                }

                int var59 = BlissCoreItems.gemIdOf(var22, 382441079);
                int var122 = GemType.ASTRA.id();
                boolean var112;
                if (var59 == var122) {
                    var112 = true;
                } else {
                    var112 = false;
                }

                GemType var123 = GemType.ASTRA;
                boolean var25 = GoldGem.hasAbsorbedSoul(var18, var123, 1603837876);
                if (var57 || var112 || var25) {
                    if (!var57 || BlissCoreItems.tierOf(var21, 1633828214) != 2) {
                        Material var26 = var21.getType();
                        if (!var57 && !GoldGem.isGoldGem(var21)) {
                            Material var125 = Material.AIR;
                            if (var26 != var125 && !isSword(var26, 1641431850) && !isAxe(var26, 246061159)) {
                                return;
                            }
                        }

                        PlayerDataStore var79 = this.players;
                        UUID var127 = var18.getUniqueId();
                        if (var79.get(var127, 1264010596).canUsePassives(2051432579)) {
                            AstraSoulStore var84 = this.souls;
                            UUID var129 = var18.getUniqueId();
                            AstraSoulStore.Soul var28 = var84.popOldest(var129, 1038847886);
                            if (var28 == null) {
                                Component var131 = Text.legacy("&5\ud83d\udd2e &cNo souls stored", 373192400);
                                var18.sendActionBar(var131);
                            } else {
                                Location var29 = var18.getLocation().clone();

                                Entity var30;
                                try {
                                    World var92 = var18.getWorld();
                                    EntityType var155 = var28.type();
                                    var30 = var92.spawnEntity(var29, var155);
                                } catch (IllegalArgumentException var178) {
                                    AstraSoulStore var96 = this.souls;
                                    UUID var134 = var18.getUniqueId();
                                    EntityType var157 = var28.type();
                                    double var165 = var28.maxHealth();
                                    var96.capture(var134, var157, var165, 615578881);
                                    Component var139 = Text.legacy(
                                        "&5\ud83d\udd2e &cCould not summon " + prettyName(var28.type(), 538667302), 373192400
                                    );
                                    var18.sendMessage(var139);
                                    return;
                                }

                                if (var30 instanceof LivingEntity var177) {
                                    PersistentDataContainer var104 = var177.getPersistentDataContainer();
                                    NamespacedKey var141 = this.summonedKey;
                                    PersistentDataType var158 = PersistentDataType.BYTE;
                                    Byte var168 = (byte)1;
                                    var104.set(var141, var158, var168);
                                    PersistentDataContainer var106 = var177.getPersistentDataContainer();
                                    NamespacedKey var143 = this.summonerKey;
                                    PersistentDataType var159 = PersistentDataType.STRING;
                                    String var171 = var18.getUniqueId().toString();
                                    var106.set(var143, var159, var171);
                                    UUID var161 = var18.getUniqueId();
                                    this.startSummonUpkeep(var177, var161, 1825350391);
                                }

                                World var109 = var18.getWorld();
                                Particle var145 = Particle.REVERSE_PORTAL;
                                Location var163 = var29.add(0.0, 0.5, 0.0);
                                Particles.spawn(var109, var145, var163, 40, 0.4, 0.6, 0.4, 0.07, 1986132876);
                                Location var147 = var18.getLocation();
                                Sound var164 = Sound.ENTITY_ENDER_DRAGON_FLAP;
                                var18.playSound(var147, var164, 0.6F, 1.4F);
                                Component var152 = Text.legacy(
                                    "&5\ud83d\udd2e &dSummoned " + prettyName(var28.type(), 538667302) + " &7from stored soul.", 373192400
                                );
                                var18.sendMessage(var152);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(
        priority = EventPriority.MONITOR,
        ignoreCancelled = true
    )
    public void onLowHealthHeal(EntityDamageEvent var1) {
        if (var1.getEntity() instanceof Player var6) {
            if (this.holdsAstra(var6, 388454034)) {
                PlayerDataStore var16 = this.players;
                UUID var24 = var6.getUniqueId();
                if (var16.get(var24, 1264010596).canUsePassives(2051432579)) {
                    String var19 = "astra";
                    String var25 = "soul-healing";
                    if (GemTuning.passive(var19, var25, true, 14309468)) {
                        BukkitScheduler var21 = Bukkit.getScheduler();
                        BlissPlugin var27 = this.plugin;
                        Runnable var29 = () -> this.tryHeal(var6, 663593759);
                        var21.runTaskLater(var27, var29, 1L);
                    }
                }
            }
        }
    }

    private void tryHeal(Player var1, int var2) {
        if (var1.isOnline()) {
            double var20 = var1.getHealth();
            if (!(var20 <= 0.0) && !(var20 >= 4.0)) {
                AstraSoulStore var39 = this.souls;
                UUID var58 = var1.getUniqueId();
                AstraSoulStore.Soul var22 = var39.popOldest(var58, 1038847886);
                if (var22 != null) {
                    double var23 = var22.maxHealth() / 4.0;
                    Attribute var59 = Attribute.MAX_HEALTH;
                    AttributeInstance var25 = var1.getAttribute(var59);
                    double var50;
                    if (var25 != null) {
                        var50 = var25.getBaseValue();
                    } else {
                        var50 = 20.0;
                    }

                    double var80 = var20 + var23;
                    double var62 = Math.min(var50, var80);
                    var1.setHealth(var62);
                    String var64 = "&5\ud83d\udd2e &dSoul absorbed (+%.1f❤). &7%s consumed.";
                    Object[] var75 = new Object[2];
                    Double var94 = var23 / 2.0;
                    var75[0] = var94;
                    String var97 = prettyName(var22.type(), 538667302);
                    var75[1] = var97;
                    Component var66 = Text.legacy(String.format(var64, var75), 373192400);
                    var1.sendMessage(var66);
                    Location var68 = var1.getLocation();
                    Sound var76 = Sound.ENTITY_VEX_AMBIENT;
                    var1.playSound(var68, var76, 0.4F, 1.8F);
                    World var56 = var1.getWorld();
                    Particle var69 = Particle.HEART;
                    Location var79 = var1.getLocation().add(0.0, 1.6, 0.0);
                    Particles.spawn(var56, var69, var79, 4, 0.3, 0.3, 0.3, 0.0, 1986132876);
                }
            }
        }
    }

    @EventHandler
    public void onSummonedTarget(EntityTargetEvent var1) {
        Entity var6 = var1.getEntity();
        if (var6 instanceof LivingEntity) {
            PersistentDataContainer var15 = ((LivingEntity)var6).getPersistentDataContainer();
            NamespacedKey var31 = this.summonerKey;
            PersistentDataType var4 = PersistentDataType.STRING;
            String var35 = (String)var15.get(var31, var4);
            if (var35 != null) {
                Entity var8 = var1.getTarget();
                if (var8 instanceof Player) {
                    String var27 = ((Player)var8).getUniqueId().toString();
                    if (var27.equals(var35)) {
                        var1.setCancelled(true);
                        var1.setTarget(null);
                    }
                }
            }
        }
    }

    @EventHandler(
        priority = EventPriority.LOW,
        ignoreCancelled = true
    )
    public void onSummonedDamage(EntityDamageByEntityEvent var1) {
        Entity var5 = var1.getDamager();
        LivingEntity var6 = null;
        if (var5 instanceof LivingEntity) {
            var6 = (LivingEntity)var5;
        } else if (var5 instanceof Projectile) {
            ProjectileSource var10 = ((Projectile)var5).getShooter();
            if (var10 instanceof LivingEntity) {
                var6 = (LivingEntity)var10;
            }
        }

        if (var6 != null) {
            PersistentDataContainer var20 = var6.getPersistentDataContainer();
            NamespacedKey var46 = this.summonerKey;
            PersistentDataType var4 = PersistentDataType.STRING;
            String var49 = (String)var20.get(var46, var4);
            if (var49 != null) {
                Entity var9 = var1.getEntity();
                if (var9 instanceof Player) {
                    String var32 = ((Player)var9).getUniqueId().toString();
                    if (var32.equals(var49)) {
                        var1.setCancelled(true);
                    }
                }
            }
        }
    }

    private void startSummonUpkeep(final LivingEntity var1, final UUID var2, int var3) {
        Color var5 = ASTRA_PURPLE;
        final DustOptions var4 = new DustOptions(var5, 1.0F);
        BukkitRunnable var10 = new BukkitRunnable() {
            public void run() {
                if (!var1.isDead() && var1.isValid()) {
                    double var19 = var1.getHeight();
                    double var21 = var1.getWidth();
                    World var40 = var1.getWorld();
                    Particle var2x = Particle.DUST;
                    Location var67 = var1.getLocation();
                    double var72 = var19 * 0.6;
                    Location var68 = var67.add(0.0, var72, 0.0);
                    double var70 = var21 * 0.55;
                    double var76 = var19 * 0.45;
                    double var82 = var21 * 0.55;
                    DustOptions var86 = var4;
                    Particles.spawn(var40, var2x, var68, 6, var70, var76, var82, 0.0, var86, 1141219473);
                    Player var23 = Bukkit.getPlayer(var2);
                    if (var23 != null) {
                        if (var1 instanceof Mob var24) {
                            LivingEntity var63 = var24.getTarget();
                            if (var23.equals(var63)) {
                                var24.setTarget(null);
                            }
                        }

                        if (var1 instanceof Warden var60) {
                            var60.clearAnger(var23);
                        }
                    }
                } else {
                    this.cancel();
                }
            }
        };
        BlissPlugin var13 = this.plugin;
        var10.runTaskTimer(var13, 5L, 5L);
    }

    private boolean holdsAstra(Player var1, int var2) {
        int var7 = BlissCoreItems.gemIdOf(var1.getInventory().getItemInMainHand(), 382441079);
        int var15 = GemType.ASTRA.id();
        if (var7 != var15) {
            int var11 = BlissCoreItems.gemIdOf(var1.getInventory().getItemInOffHand(), 382441079);
            int var17 = GemType.ASTRA.id();
            if (var11 != var17) {
                GemType var18 = GemType.ASTRA;
                if (!GoldGem.hasAbsorbedSoul(var1, var18, 1603837876)) {
                    return false;
                }
            }
        }

        return true;
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

    private static String prettyName(EntityType var0, int var1) {
        String var5 = var0.name().toLowerCase().replace('_', ' ');
        int var38 = var5.length();
        StringBuilder var15 = new StringBuilder(var38);
        StringBuilder var6 = var15;
        boolean var7 = true;

        for (char var11 : var5.toCharArray()) {
            char var35;
            if (var7) {
                var35 = Character.toUpperCase(var11);
            } else {
                var35 = var11;
            }

            var6.append(var35);
            boolean var30;
            if (var11 == ' ') {
                var30 = true;
            } else {
                var30 = false;
            }

            var7 = var30;
        }

        return var6.toString();
    }

    private void spawnCaptureOrb(Location var1, int var2) {
        Color var9 = ASTRA_PURPLE;
        this.spawnCaptureOrb(var1, 0.65, 20, 60, var9, 1.5F, 1417817111);
    }

    private void spawnCaptureOrb(Location var1, double var2, int var4) {
        Color var11 = ASTRA_PURPLE;
        this.spawnCaptureOrb(var1, var2, var4, 60, var11, 1.5F, 1417817111);
    }

    private void spawnCaptureOrb(Location var1, final double var2, final int var4, final int var5, Color var6, float var7, int var8) {
        final Location var17 = var1.clone();
        final DustOptions var20 = new DustOptions(var6, var7);
        BukkitRunnable var21 = new BukkitRunnable() {
            int ticks = 0;

            public void run() {
                int var77 = this.ticks + 1;
                this.ticks = var77;
                int var79 = var5;
                if (var77 > var79) {
                    this.cancel();
                } else {
                    int var16 = 0;

                    while (true) {
                        int var81 = var4;
                        if (var16 >= var81) {
                            World var34 = var17.getWorld();
                            Particle var82 = Particle.REVERSE_PORTAL;
                            Location var85 = var17;
                            Particles.spawn(var34, var82, var85, 4, 0.3, 0.3, 0.3, 0.02, 1986132876);
                            return;
                        }

                        double var17x = AstraPassivesListener.this.random.nextDouble() * Math.PI * 2.0;
                        double var93 = AstraPassivesListener.this.random.nextDouble();
                        double var19 = Math.acos(2.0 * var93 - 1.0);
                        double var53 = var2;
                        double var138 = AstraPassivesListener.this.random.nextDouble();
                        double var131 = 0.25 * var138;
                        double var99 = 0.75 + var131;
                        double var21 = var53 * var99;
                        double var103 = Math.sin(var19);
                        double var59 = var21 * var103;
                        double var107 = Math.cos(var17x);
                        double var23 = var59 * var107;
                        double var111 = Math.cos(var19);
                        double var25 = var21 * var111;
                        double var115 = Math.sin(var19);
                        double var69 = var21 * var115;
                        double var119 = Math.sin(var17x);
                        double var27 = var69 * var119;
                        World var75 = var17.getWorld();
                        Particle var83 = Particle.DUST;
                        Location var123 = var17.clone();
                        Location var124 = var123.add(var23, var25, var27);
                        DustOptions var146 = var20;
                        Particles.spawn(var75, var83, var124, 1, 0.0, 0.0, 0.0, 0.0, var146, 1141219473);
                        var16++;
                    }
                }
            }
        };
        BlissPlugin var24 = this.plugin;
        var21.runTaskTimer(var24, 0L, 1L);
    }
}
