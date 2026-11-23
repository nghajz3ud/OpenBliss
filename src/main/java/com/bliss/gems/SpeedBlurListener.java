package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.EnergyScale;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.Particles;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.core.TrustStore;
import com.bliss.goldgem.GoldGem;
import com.bliss.honored.GemAbilityRules;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.damage.DamageSource.Builder;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;
import com.bliss.gems.ConsistentDamage;
import com.bliss.gems.SpeedGemListener;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.npc.MetadataStore;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

public final class SpeedBlurListener implements Listener {
    private static final long PRIME_WINDOW_MS = 5000L;
    private static final long OLD_COOLDOWN_MS = 90000L;
    private static final int MAX_USES = 4;
    private static final int ITERATIONS = 3;
    private static final int ITERATION_TICKS = 10;
    private static final double NPC_DISTANCE = 2.5;
    private static final double BLUR_BASE_HP = 1.4;
    private static final double BLUR_STACK_HP = 0.7;
    private static final double PULL = 0.4;
    private static final long LINK_TICKS = 100L;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final SpeedGemListener speed;
    private final TrustStore trust;
    private final Set<UUID> blurCasterDied;

    public SpeedBlurListener(BlissPlugin var1, PlayerDataStore var2, SpeedGemListener var3, TrustStore var4, int var5) {
        KeySetView var7 = ConcurrentHashMap.newKeySet();
        this.blurCasterDied = var7;
        this.plugin = var1;
        this.players = var2;
        this.speed = var3;
        this.trust = var4;
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onHit(EntityDamageByEntityEvent var1) {
        DamageCause var28 = var1.getCause();
        DamageCause var3 = DamageCause.ENTITY_ATTACK;
        if (var28 != var3) {
            DamageCause var30 = var1.getCause();
            DamageCause var170 = DamageCause.ENTITY_SWEEP_ATTACK;
            if (var30 != var170) {
                return;
            }
        }

        if (!ConsistentDamage.deliveringAbilityTo(var1.getEntity(), 450947555)) {
            if (var1.getDamager() instanceof Player var14) {
                GlobalState var41 = GlobalState.forPlayer(var14, 1433087673);
                if (var41.legacySpeedFor(var14, 1914133154)) {
                    return;
                }
            }

            if (var1.getDamager() instanceof Player var268) {
                if (var1.getEntity() instanceof LivingEntity var270) {
                    if (!var270.equals(var268)) {
                        UUID var271 = var268.getUniqueId();
                        PlayerDataStore var60 = this.players;
                        PlayerData var17 = var60.get(var271, 1264010596);
                        if (var17.canUsePowers(1616892701)) {
                            if (GemAbilityRules.on(GemAbilityRules.GemAbility.SPEED_BLUR, 425337338)) {
                                Set var68 = this.speed.blurActive;
                                if (!var68.contains(var271)) {
                                    long var18 = System.currentTimeMillis();
                                    String var4 = "speed";
                                    String var5 = "blur-charges";
                                    double var203 = GemTuning.power(var4, var5, 4.0, 1503850832);
                                    int var20 = (int)Math.max(1.0, var203);
                                    Map var79 = this.speed.blurPrimedUntil;
                                    Long var21 = (Long)var79.get(var271);
                                    if (var21 != null) {
                                        long var84 = var21;
                                        if (var84 > var18) {
                                            DamageCause var129 = var1.getCause();
                                            DamageCause var193 = DamageCause.ENTITY_SWEEP_ATTACK;
                                            if (var129 == var193) {
                                                return;
                                            }

                                            if (var270 instanceof Player var272) {
                                                TrustStore var135 = this.trust;
                                                UUID var225 = var272.getUniqueId();
                                                if (var135.isTrusted(var271, var225, 1736364606)) {
                                                    return;
                                                }
                                            }

                                            Map var139 = this.speed.blurPrimedUntil;
                                            var139.remove(var271);
                                            Map<UUID, Integer> var143 = this.speed.blurUses;
                                            Integer var227 = var20;
                                            int var273 = var143.getOrDefault(var271, var227);
                                            int var274 = var20 - var273;
                                            int var25 = var273 - 1;
                                            Map var153 = this.speed.blurUses;
                                            Integer var229 = var25;
                                            var153.put(var271, var229);
                                            if (var25 <= 0) {
                                                long var158;
                                                if (GlobalState.isHoldingDragonEgg(var268, 2044938455)) {
                                                    var158 = 45000L;
                                                } else {
                                                    var158 = 90000L;
                                                }

                                                Map var162 = this.speed.blurCd;
                                                String var254 = "speed";
                                                String var262 = "blur";
                                                long var255 = GemTuning.cd(var254, var262, 90.0, var158, 1621983041);
                                                int var265 = var17.energy(1160853318);
                                                long var257 = EnergyScale.cd(var255, var265, 907518618);
                                                Long var234 = var18 + var257;
                                                var162.put(var271, var234);
                                            }

                                            String var164 = "speed";
                                            String var201 = "blur";
                                            double var165 = GemTuning.damage(var164, var201, 1.4, 1172948035);
                                            double var238 = (double)var274;
                                            String var259 = "speed";
                                            String var263 = "blur-stack";
                                            double var260 = GemTuning.damage(var259, var263, 0.7, 1172948035);
                                            double var240 = var238 * var260;
                                            double var277 = var165 + var240;
                                            this.fireBlur(var268, var270, var277, 1432969830);
                                            return;
                                        }
                                    }

                                    if (holdingSpeedT2(var268, 1097548826)) {
                                        Map var91 = this.speed.blurCd;
                                        Long var22 = (Long)var91.get(var271);
                                        if (var22 != null) {
                                            long var96 = var22;
                                            if (var96 > var18) {
                                                long var178 = var22;
                                                Component var184 = Text.legacy(
                                                    "<##ED8790>\ud83d\udd2e <##ED8790>Blur <##B8FFFB>is on cooldown for <##82F3FF>"
                                                        + readable(var178 - var18, 1695768396),
                                                    373192400
                                                );
                                                var268.sendMessage(var184);
                                                return;
                                            }
                                        }

                                        Map<UUID, Integer> var102 = this.speed.blurUses;
                                        Integer var210 = var20;
                                        if (var102.getOrDefault(var271, var210) <= 0) {
                                            Map var108 = this.speed.blurUses;
                                            Integer var212 = var20;
                                            var108.put(var271, var212);
                                        }

                                        String var110 = "speed";
                                        String var187 = "blur-prime";
                                        long var23 = (long)(GemTuning.duration(var110, var187, 5.0, 812730832) * 1000.0);
                                        long var26 = var18 + var23;
                                        Map var123 = this.speed.blurPrimedUntil;
                                        Long var221 = var26;
                                        var123.put(var271, var221);
                                        Component var190 = Text.legacy("<##FEFD17>\ud83d\udd2e Your next hit will inflict Blur!", 373192400);
                                        var268.sendMessage(var190);
                                        BukkitScheduler var126 = Bukkit.getScheduler();
                                        BlissPlugin var192 = this.plugin;
                                        Runnable var223 = () -> this.expirePrime(var271, var26, 1523171725);
                                        long var248 = var23 / 50L;
                                        var126.runTaskLater(var192, var223, var248);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean holdingSpeedT2(Player var0, int var1) {
        ItemStack var4 = var0.getInventory().getItemInMainHand();
        int var8 = BlissCoreItems.gemIdOf(var4, 382441079);
        int var19 = GemType.SPEED.id();
        if (var8 != var19) {
            GemType var20 = GemType.SPEED;
            if (!GoldGem.isWieldingSoulMain(var0, var20, 244096655)) {
                return false;
            }
        }

        int var13 = BlissCoreItems.gemIdOf(var4, 382441079);
        int var22 = GemType.SPEED.id();
        if (var13 != var22) {
            var4 = GoldGem.heldGoldGem(var0, 780486261);
        }

        boolean var18;
        if (BlissCoreItems.tierOf(var4, 1633828214) == 2) {
            var18 = true;
        } else {
            var18 = false;
        }

        return var18;
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

    private void expirePrime(UUID var1, long var2, int var4) {
        Map var12 = this.speed.blurPrimedUntil;
        Long var9 = (Long)var12.get(var1);
        if (var9 != null) {
            long var17 = var9;
            if (var17 == var2) {
                Map var22 = this.speed.blurPrimedUntil;
                var22.remove(var1);
                Player var10 = Bukkit.getPlayer(var1);
                if (var10 != null) {
                    Component var30 = Text.legacy("<##FEFD17>\ud83d\udd2e Blur unreadied, You took too long", 373192400);
                    var10.sendMessage(var30);
                }

                return;
            }
        }
    }

    private void fireBlur(final Player var1, final LivingEntity var2, final double var3, int var5) {
        final UUID var21 = var1.getUniqueId();
        PlayerDataStore var30 = this.players;
        final double var22 = EnergyScale.power(var30.get(var21, 1264010596), 1425120665);
        PlayerDataStore var35 = this.players;
        final double var24 = EnergyScale.radius(var35.get(var21, 1264010596).energy(1160853318), 1465433230);
        String var8 = "speed";
        String var9 = "blur-clones";
        double var80 = GemTuning.power(var8, var9, 3.0, 1503850832);
        final int var26 = (int)Math.max(1.0, var80);
        Set var47 = this.speed.blurActive;
        var47.add(var21);
        Set var50 = this.blurCasterDied;
        var50.remove(var21);
        Component var71 = Text.legacy("<##ED8790>\ud83d\udd2e You unleashed <##ED8790>Blur <##B8FFFB>on <##ED8790>" + var2.getName(), 373192400);
        var1.sendMessage(var71);
        if (var2 instanceof Player var57) {
            Component var75 = Text.legacy("<##ED8790>\ud83d\udd2e You have been hit by <##ED8790>Blur <##B8FFFB>by <##ED8790>" + var1.getName(), 373192400);
            var57.sendMessage(var75);
        }

        Builder var59 = DamageSource.builder(DamageType.SONIC_BOOM);
        Builder var60 = var59.withCausingEntity(var1);
        final DamageSource var89 = var60.withDirectEntity(var1).build();
        BukkitRunnable var63 = new BukkitRunnable() {
            int i = 0;
            Location prevPos;
            NPC clone;

            public void run() {
                int var12 = this.i;
                int var89x = var26;
                if (var12 < var89x && var1.isOnline() && !var1.isDead()) {
                    Set var21x = SpeedBlurListener.this.blurCasterDied;
                    UUID var91 = var21;
                    if (!var21x.remove(var91) && !var2.isDead() && var2.isValid()) {
                        LivingEntity var30 = var2;
                        int var93 = this.i;
                        Location var8 = SpeedBlurListener.positionAround(var30, var93, 360959931);
                        if (this.prevPos != null) {
                            Location var35 = this.prevPos;
                            SpeedBlurListener.drawStreak(var35, var8, 733847842);
                        }

                        this.prevPos = var8;
                        if (this.clone != null) {
                            SpeedBlurListener.destroy(this.clone, 1676832390);
                        }

                        SpeedBlurListener var97 = SpeedBlurListener.this;
                        Player var128 = var1;
                        LivingEntity var160 = var2;
                        NPC var98 = var97.spawnClone(var128, var8, var160, 1745460017);
                        this.clone = var98;
                        double var43 = var3;
                        double var131 = (double)var26;
                        double var9 = var43 / var131;
                        var2.setNoDamageTicks(0);
                        if (GemAbilityRules.on(GemAbilityRules.GemAbility.SPEED_BLUR, 425337338)) {
                            LivingEntity var52 = var2;
                            GemAbilityRules.GemAbility var100 = GemAbilityRules.GemAbility.SPEED_BLUR;
                            double var101 = GemAbilityRules.scaled(var100, var9, 1684677315);
                            double var149 = var22;
                            double var103 = var101 * var149;
                            DamageSource var152 = var89;
                            var52.damage(var103, var152);
                        }

                        Vector var54 = var8.toVector();
                        Vector var108 = var2.getLocation().toVector();
                        Vector var11 = var54.subtract(var108);
                        if (var11.lengthSquared() > 1.0E-6) {
                            LivingEntity var61 = var2;
                            Vector var111 = var11.normalize().multiply(0.4);
                            var61.setVelocity(var111);
                        }

                        Location var64 = var2.getLocation();
                        double var157 = var2.getHeight() / 2.0;
                        Location var65 = var64.add(0.0, var157, 0.0);
                        double var115 = var24;
                        SpeedBlurListener.appearBurst(var65, var115, 1957268943);
                        World var67 = var8.getWorld();
                        Sound var139 = Sound.ITEM_TRIDENT_THUNDER;
                        var67.playSound(var8, var139, 1.0F, 1.2F);
                        Map var71 = SpeedBlurListener.this.speed.blurVictimToAttacker;
                        UUID var120 = var2.getUniqueId();
                        UUID var141 = var21;
                        var71.put(var120, var141);
                        Map var76 = SpeedBlurListener.this.speed.blurLinkExpiry;
                        UUID var123 = var2.getUniqueId();
                        Long var146 = System.currentTimeMillis() + 5000L;
                        var76.put(var123, var146);
                        int var125 = this.i + 1;
                        this.i = var125;
                        return;
                    }
                }

                if (this.clone != null) {
                    SpeedBlurListener.destroy(this.clone, 1676832390);
                }

                Set var86 = SpeedBlurListener.this.speed.blurActive;
                UUID var127 = var21;
                var86.remove(var127);
                this.cancel();
            }
        };
        BlissPlugin var79 = this.plugin;
        var63.runTaskTimer(var79, 0L, 10L);
    }

    private static Location positionAround(LivingEntity var0, int var1, int var2) {
        Location var11 = var0.getLocation().clone();
        Vector var12 = var11.getDirection().setY(0);
        if (var12.lengthSquared() < 1.0E-6) {
            Vector var52 = new Vector(0, 0, 1);
            var12 = var52;
        } else {
            var12.normalize();
        }

        double var70 = -var12.getZ();
        double var76 = var12.getX();
        Vector var28 = new Vector(var70, 0.0, var76);

        Location var15 = var11.add(switch (var1 % 4) {
            case 0 -> var12.clone().multiply(2.5);
            case 1 -> var28.clone().multiply(2.5);
            case 2 -> var12.clone().multiply(-2.5);
            default -> var28.clone().multiply(-2.5);
        });
        Vector var37 = var0.getLocation().toVector();
        Vector var59 = var15.toVector();
        Vector var16 = var37.subtract(var59);
        if (var16.lengthSquared() > 1.0E-6) {
            var15.setDirection(var16);
        }

        return var15;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private NPC spawnClone(Player player, Location location, LivingEntity livingEntity, int n) {
        try {
            NPCRegistry nPCRegistry = CitizensAPI.getNPCRegistry();
            EntityType entityType = EntityType.PLAYER;
            String string = player.getName();
            NPC nPC = nPCRegistry.createNPC(entityType, string);
            try {
                SkinTrait skinTrait = (SkinTrait)nPC.getOrAddTrait(SkinTrait.class);
                Player player2 = player;
                skinTrait.setSkinPersistent(player2);
            }
            catch (Throwable throwable) {}
            MetadataStore metadataStore = nPC.data();
            NPC.Metadata metadata = NPC.Metadata.SHOULD_SAVE;
            Boolean bl = false;
            metadataStore.setPersistent(metadata, (Object)bl);
            MetadataStore metadataStore2 = nPC.data();
            NPC.Metadata metadata2 = NPC.Metadata.DEFAULT_PROTECTED;
            Boolean bl2 = true;
            metadataStore2.setPersistent(metadata2, (Object)bl2);
            NPC nPC2 = nPC;
            Location location2 = location;
            nPC2.spawn(location2);
            ((LookClose)nPC.getOrAddTrait(LookClose.class)).lookClose(true);
            try {
                Navigator navigator = nPC.getNavigator();
                LivingEntity livingEntity2 = livingEntity;
                navigator.setTarget((Entity)livingEntity2, false);
            }
            catch (Throwable throwable) {}
            BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
            BlissPlugin blissPlugin = this.plugin;
            NPC nPC3 = nPC;
            Player player3 = player;
            Runnable runnable = () -> {
                if (!nPC3.isSpawned()) {
                    return;
                }
                Equipment equipment = (Equipment)nPC3.getOrAddTrait(Equipment.class);
                PlayerInventory playerInventory = player3.getInventory();
                Equipment equipment2 = equipment;
                Equipment.EquipmentSlot equipmentSlot = Equipment.EquipmentSlot.HAND;
                ItemStack itemStack = SpeedBlurListener.clone(playerInventory.getItemInMainHand(), 249544708);
                equipment2.set(equipmentSlot, itemStack);
                Equipment equipment3 = equipment;
                Equipment.EquipmentSlot equipmentSlot2 = Equipment.EquipmentSlot.HELMET;
                ItemStack itemStack2 = SpeedBlurListener.clone(playerInventory.getHelmet(), 249544708);
                equipment3.set(equipmentSlot2, itemStack2);
                Equipment equipment4 = equipment;
                Equipment.EquipmentSlot equipmentSlot3 = Equipment.EquipmentSlot.CHESTPLATE;
                ItemStack itemStack3 = SpeedBlurListener.clone(playerInventory.getChestplate(), 249544708);
                equipment4.set(equipmentSlot3, itemStack3);
                Equipment equipment5 = equipment;
                Equipment.EquipmentSlot equipmentSlot4 = Equipment.EquipmentSlot.LEGGINGS;
                ItemStack itemStack4 = SpeedBlurListener.clone(playerInventory.getLeggings(), 249544708);
                equipment5.set(equipmentSlot4, itemStack4);
                Equipment equipment6 = equipment;
                Equipment.EquipmentSlot equipmentSlot5 = Equipment.EquipmentSlot.BOOTS;
                ItemStack itemStack5 = SpeedBlurListener.clone(playerInventory.getBoots(), 249544708);
                equipment6.set(equipmentSlot5, itemStack5);
            };
            bukkitScheduler.runTaskLater((Plugin)blissPlugin, runnable, 2L);
            return nPC;
        }
        catch (Throwable throwable) {
            return null;
        }
    }

    private static void destroy(NPC var0, int var1) {
        try {
            if (var0 != null) {
                var0.destroy();
            }
        } catch (Throwable var2) {
        }
    }

    private static ItemStack clone(ItemStack var0, int var1) {
        if (var0 != null) {
            Material var5 = var0.getType();
            Material var3 = Material.AIR;
            if (var5 != var3) {
                return var0.clone();
            }
        }

        return null;
    }

    private static void appearBurst(Location var0, double var1, int var3) {
        World var20 = var0.getWorld();
        Particle var5 = Particle.DAMAGE_INDICATOR;
        double var35 = 0.3 * var1;
        double var51 = 0.3 * var1;
        double var73 = 0.3 * var1;
        Particles.spawn(var20, var5, var0, 8, var35, var51, var73, 0.0, 1986132876);
        Particle var26 = Particle.CRIT;
        double var39 = 0.4 * var1;
        double var57 = 0.4 * var1;
        double var79 = 0.4 * var1;
        Particles.spawn(var20, var26, var0, 15, var39, var57, var79, 0.1, 1986132876);
        Particle var27 = Particle.SWEEP_ATTACK;
        double var43 = 0.2 * var1;
        double var63 = 0.2 * var1;
        double var85 = 0.2 * var1;
        Particles.spawn(var20, var27, var0, 2, var43, var63, var85, 0.0, 1986132876);
        Particle var28 = Particle.DUST;
        double var47 = 0.4 * var1;
        double var69 = 0.4 * var1;
        double var91 = 0.4 * var1;
        Color var107 = Color.fromRGB(255, 255, 0);
        DustOptions var16 = new DustOptions(var107, 2.0F);
        Particles.spawn(var20, var28, var0, 20, var47, var69, var91, 0.0, var16, 1141219473);
    }

    private static void drawStreak(Location var0, Location var1, int var2) {
        World var18 = var0.getWorld();
        World var55 = var1.getWorld();
        if (var18.equals(var55)) {
            Vector var30 = var1.toVector();
            Vector var57 = var0.toVector();
            Vector var19 = var30.subtract(var57);
            double var20 = var19.length();
            if (!(var20 < 1.0E-4)) {
                int var22 = (int)Math.ceil(var20 * 8.0);
                double var70 = (double)var22;
                double var60 = 1.0 / var70;
                Vector var23 = var19.multiply(var60);
                Location var24 = var0.clone().add(0.0, 1.0, 0.0);

                for (int var25 = 0; var25 <= var22; var25++) {
                    Particle var65 = Particle.ELECTRIC_SPARK;
                    Particles.spawn(var18, var65, var24, 1, 0.0, 0.0, 0.0, 0.0, 1986132876);
                    var24.add(var23);
                }
            }
        }
    }

    @EventHandler(
        priority = EventPriority.MONITOR
    )
    public void onBlurCasterDeath(PlayerDeathEvent var1) {
        UUID var4 = var1.getEntity().getUniqueId();
        Set var9 = this.speed.blurActive;
        if (var9.contains(var4)) {
            Set var12 = this.blurCasterDied;
            var12.add(var4);
        }
    }

    @EventHandler
    public void onBlurredDeath(PlayerDeathEvent var1) {
        UUID var6 = var1.getEntity().getUniqueId();
        Map var15 = this.speed.blurLinkExpiry;
        Long var7 = (Long)var15.get(var6);
        if (var7 != null) {
            long var20 = var7;
            long var4 = System.currentTimeMillis();
            if (var20 >= var4) {
                Map var25 = this.speed.blurVictimToAttacker;
                UUID var8 = (UUID)var25.get(var6);
                if (var8 == null) {
                    return;
                }

                Player var9 = Bukkit.getPlayer(var8);
                String var42;
                if (var9 != null) {
                    var42 = var9.getName();
                } else {
                    var42 = "their afterimage";
                }

                String var46 = var1.getEntity().getName();
                Component var48 = Text.legacy(var46 + " couldn't keep up with " + var42, 373192400);
                var1.deathMessage(var48);
                Map var36 = this.speed.blurVictimToAttacker;
                var36.remove(var6);
                Map var40 = this.speed.blurLinkExpiry;
                var40.remove(var6);
                return;
            }
        }
    }
}
