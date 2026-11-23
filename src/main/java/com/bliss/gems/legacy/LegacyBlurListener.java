package com.bliss.gems.legacy;

import com.bliss.BlissPlugin;
import com.bliss.core.EnergyScale;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.core.TrustStore;
import com.bliss.gems.ConsistentDamage;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;
import com.bliss.gems.legacy.LegacySpeedState;
import java.util.ArrayList;
import java.util.function.Function;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.MetadataStore;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.trait.LookClose;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

public final class LegacyBlurListener implements Listener {
    private static final int STRIKES = 14;
    private static final int STRIKE_GAP_TICKS = 10;
    private static final int POSITIONS = 7;
    private static final long CLEANUP_TICKS = 145L;
    private static final double STRIKE_DAMAGE = 3.5;
    private static final double KNOCKBACK = 0.3;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final LegacySpeedState state;
    private final GlobalState globalState;
    private final TrustStore trust;
    private final Map<UUID, UUID> victimToCaster;
    private final Map<UUID, List<NPC>> clones;

    public LegacyBlurListener(BlissPlugin var1, PlayerDataStore var2, LegacySpeedState var3, GlobalState var4, TrustStore var5, int var6) {
        ConcurrentHashMap var8 = new ConcurrentHashMap();
        this.victimToCaster = var8;
        ConcurrentHashMap var16 = new ConcurrentHashMap();
        this.clones = var16;
        this.plugin = var1;
        this.players = var2;
        this.state = var3;
        this.globalState = var4;
        this.trust = var5;
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onDamageTrigger(EntityDamageByEntityEvent var1) {
        if (var1.getDamager() instanceof Player var8) {
            GlobalState var23 = GlobalState.forPlayer(var8, 1433087673);
            if (var23.legacySpeedFor(var8, 1914133154)) {
                if (var1.getEntity() instanceof LivingEntity var121) {
                    if (!var121.equals(var8)) {
                        DamageCause var34 = var1.getCause();
                        DamageCause var87 = DamageCause.ENTITY_ATTACK;
                        if (var34 != var87) {
                            DamageCause var36 = var1.getCause();
                            DamageCause var88 = DamageCause.ENTITY_SWEEP_ATTACK;
                            if (var36 != var88) {
                                return;
                            }
                        }

                        if (!ConsistentDamage.deliveringAbilityTo(var1.getEntity(), 450947555)) {
                            ItemStack var122 = var8.getInventory().getItemInMainHand();
                            int var44 = BlissCoreItems.gemIdOf(var122, 382441079);
                            int var90 = GemType.SPEED.id();
                            if (var44 == var90) {
                                if (BlissCoreItems.tierOf(var122, 1633828214) == 2) {
                                    UUID var11 = var8.getUniqueId();
                                    PlayerDataStore var50 = this.players;
                                    if (var50.get(var11, 1264010596).canUsePowers(1616892701)) {
                                        long var13 = System.currentTimeMillis();
                                        Map var58 = this.state.blurCd;
                                        Long var15 = (Long)var58.get(var11);
                                        if (var15 != null) {
                                            long var63 = var15;
                                            if (var63 > var13) {
                                                long var95 = var15;
                                                Component var101 = Text.legacy(
                                                    "&e\ud83d\udd2e &cYour <##FEFD17>Blur <##B8FFFB> is on cooldown for &e"
                                                        + readable(var95 - var13, 1860426398),
                                                    373192400
                                                );
                                                var8.sendMessage(var101);
                                                return;
                                            }
                                        }

                                        if (var121 instanceof Player var16) {
                                            TrustStore var72 = this.trust;
                                            UUID var118 = var16.getUniqueId();
                                            if (var72.isTrusted(var11, var118, 1736364606)) {
                                                Component var116 = Text.legacy("<##FEFD17>\ud83d\udd2e &fYou cannot cast negative powers on allies!", 373192400);
                                                var8.sendMessage(var116);
                                                return;
                                            }
                                        }

                                        Component var106 = Text.legacy(
                                            "<##FEFD17>\ud83d\udd2e <##B8FFFB>You have used <##FEFD17>Blur <##B8FFFB>on <##FEFD17>"
                                                + nameOf(var121, 2046636124),
                                            373192400
                                        );
                                        var8.sendMessage(var106);
                                        if (var121 instanceof Player var79) {
                                            Component var110 = Text.legacy(
                                                "<##FEFD17>\ud83d\udd2e <##B8FFFB>You have been affected by <##FEFD17>Blur <##B8FFFB>by <##FEFD17>"
                                                    + var8.getName(),
                                                373192400
                                            );
                                            var79.sendMessage(var110);
                                        }

                                        this.applyCooldown(var8, 747824258);
                                        Map var82 = this.victimToCaster;
                                        UUID var113 = var121.getUniqueId();
                                        var82.put(var113, var11);
                                        this.runFlurry(var8, var121, 1184255356);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void applyCooldown(Player var1, int var2) {
        PlayerDataStore var15 = this.players;
        UUID var45 = var1.getUniqueId();
        PlayerData var10 = var15.get(var45, 1264010596);
        GlobalState var18 = this.globalState;
        boolean var11 = var18.holdsDragonEgg(var1, 715974514);
        int var12 = var10.energy(1160853318);
        long var13;
        if (var12 > 5) {
            long var43;
            if (var11) {
                var43 = 40000L;
            } else {
                var43 = 75000L;
            }

            var13 = var43;
        } else if (var12 == 5) {
            long var41;
            if (var11) {
                var41 = 45000L;
            } else {
                var41 = 90000L;
            }

            var13 = var41;
        } else if (var12 == 4) {
            long var31;
            if (var11) {
                var31 = 50000L;
            } else {
                var31 = 105000L;
            }

            var13 = var31;
        } else if (var12 == 3) {
            long var35;
            if (var11) {
                var35 = 60000L;
            } else {
                var35 = 120000L;
            }

            var13 = var35;
        } else {
            long var38;
            if (var11) {
                var38 = 75000L;
            } else {
                var38 = 150000L;
            }

            var13 = var38;
        }

        Map var27 = this.state.blurCd;
        UUID var50 = var1.getUniqueId();
        long var5 = System.currentTimeMillis();
        long var56 = EnergyScale.cd(var13, var12, 907518618);
        Long var55 = var5 + var56;
        var27.put(var50, var55);
    }

    private void runFlurry(Player var1, LivingEntity var2, int var3) {
        for (int var11 = 0; var11 < 14; var11++) {
            int var12 = var11 % 7;
            long var13 = (long)var11 * 10L;
            BukkitScheduler var23 = Bukkit.getScheduler();
            BlissPlugin var29 = this.plugin;
            Runnable var33 = () -> {
                if (!var2.isDead() && var2.isValid()) {
                    Location var8 = standPoint(var2, var12, 885501168);
                    this.strike(var1, var2, var8, 2108987875);
                } else {
                    UUID var16 = var1.getUniqueId();
                    this.despawnClones(var16, 1697768296);
                }
            };
            var23.runTaskLater(var29, var33, var13);
        }

        BukkitScheduler var25 = Bukkit.getScheduler();
        BlissPlugin var31 = this.plugin;
        Runnable var35 = () -> {
            UUID var8 = var1.getUniqueId();
            this.despawnClones(var8, 1697768296);
            Map var6 = this.victimToCaster;
            UUID var10 = var2.getUniqueId();
            var6.remove(var10);
        };
        var25.runTaskLater(var31, var35, 145L);
    }

    private void strike(Player var1, LivingEntity var2, Location var3, int var4) {
        UUID var32 = var1.getUniqueId();
        this.despawnClones(var32, 1697768296);
        Location var11 = var3.clone();
        Location var34 = var2.getLocation();
        face(var11, var34, 1183828746);
        this.spawnClone(var1, var11, var2, 1620417656);
        String var43 = "speed";
        String var50 = "legacy-blur-strike";
        double var44 = GemTuning.damage(var43, var50, 3.5, 1172948035);
        ConsistentDamage.dealFullArmorAware(var1, var2, var44, 1323155734);
        if (var3.getWorld() != null) {
            World var21 = var3.getWorld();
            var21.strikeLightningEffect(var3);
        }

        Vector var25 = var2.getLocation().toVector();
        Vector var39 = var3.toVector();
        Vector var12 = var25.subtract(var39);
        if (var12.lengthSquared() > 0.0) {
            Vector var42 = var12.normalize().multiply(0.3);
            var2.setVelocity(var42);
        }
    }

    private static Location standPoint(LivingEntity var0, int var1, int var2) {
        Location var10 = var0.getLocation().clone();
        Vector var11 = var10.getDirection().setY(0).normalize();
        double var82 = -var11.getZ();
        double var106 = var11.getX();
        Vector var21 = new Vector(var82, 0.0, var106);
        Vector var22 = new Vector(0, 1, 0);
        Location var14 = var10.clone();
        switch (var1) {
            case 0:
                Vector var68 = var21.clone().multiply(-2.5);
                var14.add(var68);
                break;
            case 1:
                Vector var74 = var21.clone().multiply(2.5);
                Location var42 = var14.add(var74);
                var42.add(var22);
                break;
            case 2:
                Vector var56 = var11.clone().multiply(2.5);
                Location var32 = var14.add(var56);
                Vector var59 = var21.clone().multiply(1.0);
                var32.add(var59);
                break;
            case 3:
                Vector var49 = var11.clone().multiply(-2.0);
                Location var27 = var14.add(var49);
                Vector var52 = var21.clone().multiply(-1.0);
                Location var28 = var27.add(var52);
                var28.add(var22);
                break;
            case 4:
                Vector var71 = var21.clone().multiply(2.5);
                var14.add(var71);
                break;
            case 5:
                Vector var62 = var11.clone().multiply(2.0);
                Location var35 = var14.add(var62);
                Vector var65 = var21.clone().multiply(-1.5);
                var35.add(var65);
                break;
            case 6:
                Vector var78 = var21.clone().multiply(-2.5);
                Location var45 = var14.add(var78);
                var45.add(var22);
        }

        return var14;
    }

    private static void face(Location var0, Location var1, int var2) {
        double var19 = var1.getX();
        double var59 = var0.getX();
        double var11 = var19 - var59;
        double var24 = var1.getY();
        double var62 = var0.getY();
        double var13 = var24 - var62;
        double var29 = var1.getZ();
        double var65 = var0.getZ();
        double var15 = var29 - var65;
        double var35 = var11 * var11;
        double var71 = var15 * var15;
        double var17 = Math.sqrt(var35 + var71);
        double var43 = -var11;
        float var49 = (float)Math.toDegrees(Math.atan2(var43, var15));
        var0.setYaw(var49);
        float var58 = (float)Math.toDegrees(-Math.atan2(var13, var17));
        var0.setPitch(var58);
    }

    private void spawnClone(Player player, Location location, LivingEntity livingEntity, int n) {
        try {
            Material material;
            Material material2;
            NPCRegistry nPCRegistry = CitizensAPI.getNPCRegistry();
            EntityType entityType = EntityType.PLAYER;
            String string = player.getName();
            NPC nPC = nPCRegistry.createNPC(entityType, string);
            MetadataStore metadataStore = nPC.data();
            NPC.Metadata metadata = NPC.Metadata.SHOULD_SAVE;
            Boolean bl = false;
            metadataStore.setPersistent(metadata, (Object)bl);
            NPC nPC2 = nPC;
            Location location2 = location;
            nPC2.spawn(location2);
            Map<UUID, List<NPC>> map = this.clones;
            UUID uUID2 = player.getUniqueId();
            Function<UUID, List<NPC>> function = uUID -> new ArrayList<>();
            List<NPC> list = map.computeIfAbsent(uUID2, function);
            NPC nPC3 = nPC;
            list.add(nPC3);
            Equipment equipment = (Equipment)nPC.getOrAddTrait(Equipment.class);
            PlayerInventory playerInventory = player.getInventory();
            Equipment equipment2 = equipment;
            Equipment.EquipmentSlot equipmentSlot = Equipment.EquipmentSlot.HELMET;
            ItemStack itemStack = LegacyBlurListener.copy(playerInventory.getHelmet(), 1035441548);
            equipment2.set(equipmentSlot, itemStack);
            Equipment equipment3 = equipment;
            Equipment.EquipmentSlot equipmentSlot2 = Equipment.EquipmentSlot.CHESTPLATE;
            ItemStack itemStack2 = LegacyBlurListener.copy(playerInventory.getChestplate(), 1035441548);
            equipment3.set(equipmentSlot2, itemStack2);
            Equipment equipment4 = equipment;
            Equipment.EquipmentSlot equipmentSlot3 = Equipment.EquipmentSlot.LEGGINGS;
            ItemStack itemStack3 = LegacyBlurListener.copy(playerInventory.getLeggings(), 1035441548);
            equipment4.set(equipmentSlot3, itemStack3);
            Equipment equipment5 = equipment;
            Equipment.EquipmentSlot equipmentSlot4 = Equipment.EquipmentSlot.BOOTS;
            ItemStack itemStack4 = LegacyBlurListener.copy(playerInventory.getBoots(), 1035441548);
            equipment5.set(equipmentSlot4, itemStack4);
            ItemStack itemStack5 = LegacyBlurListener.copy(playerInventory.getItemInMainHand(), 1035441548);
            if (itemStack5 == null || (material2 = itemStack5.getType()) == (material = Material.AIR)) {
                ItemStack itemStack6;
                Material material3 = Material.DIAMOND_SWORD;
                itemStack5 = itemStack6 = new ItemStack(material3);
            }
            Equipment equipment6 = equipment;
            Equipment.EquipmentSlot equipmentSlot5 = Equipment.EquipmentSlot.HAND;
            ItemStack itemStack7 = itemStack5;
            equipment6.set(equipmentSlot5, itemStack7);
            Equipment equipment7 = equipment;
            Equipment.EquipmentSlot equipmentSlot6 = Equipment.EquipmentSlot.OFF_HAND;
            ItemStack itemStack8 = LegacyBlurListener.copy(playerInventory.getItemInOffHand(), 1035441548);
            equipment7.set(equipmentSlot6, itemStack8);
            NPC nPC4 = nPC;
            Location location3 = livingEntity.getLocation();
            nPC4.faceLocation(location3);
            ((LookClose)nPC.getOrAddTrait(LookClose.class)).lookClose(true);
            ConsoleCommandSender consoleCommandSender = Bukkit.getConsoleSender();
            String string2 = "npc select " + nPC.getId();
            Bukkit.dispatchCommand((CommandSender)consoleCommandSender, (String)string2);
            ConsoleCommandSender consoleCommandSender2 = Bukkit.getConsoleSender();
            String string3 = "npc mirror --" + player.getName() + " true";
            Bukkit.dispatchCommand((CommandSender)consoleCommandSender2, (String)string3);
            if (livingEntity instanceof Player) {
                Player player2 = (Player)livingEntity;
                ConsoleCommandSender consoleCommandSender3 = Bukkit.getConsoleSender();
                String string4 = "npc target " + player2.getName() + " -a";
                Bukkit.dispatchCommand((CommandSender)consoleCommandSender3, (String)string4);
            }
        }
        catch (Throwable throwable) {}
    }

    private static ItemStack copy(ItemStack var0, int var1) {
        if (var0 != null) {
            Material var5 = var0.getType();
            Material var3 = Material.AIR;
            if (var5 != var3) {
                return var0.clone();
            }
        }

        return null;
    }

    private void despawnClones(UUID var1, int var2) {
        Map var9 = this.clones;
        List var5 = (List)var9.remove(var1);
        if (var5 != null) {
            for (NPC var7 : (List<NPC>)var5) {
                try {
                    var7.destroy();
                } catch (Throwable var24) {
                }
            }
        }
    }

    @EventHandler
    public void onBlurredVictimDeath(PlayerDeathEvent var1) {
        Player var5 = var1.getEntity();
        Map var11 = this.victimToCaster;
        UUID var25 = var5.getUniqueId();
        UUID var6 = (UUID)var11.get(var25);
        if (var6 != null) {
            Player var7 = Bukkit.getPlayer(var6);
            if (var7 != null) {
                Player var8 = var5.getKiller();
                if (var8 != null) {
                    UUID var22 = var8.getUniqueId();
                    if (var22.equals(var6)) {
                        String var28 = var5.getName();
                        String var31 = var7.getName();
                        Component var30 = Text.legacy(var28 + " couldn't keep up with " + var31, 373192400);
                        var1.deathMessage(var30);
                        return;
                    }
                }
            }
        }
    }

    private static String nameOf(LivingEntity var0, int var1) {
        if (var0 instanceof Player) {
            return ((Player)var0).getName();
        } else {
            String var3 = var0.getCustomName();
            String var11;
            if (var3 != null) {
                var11 = var3;
            } else {
                var11 = var0.getType().name().toLowerCase();
            }

            return var11;
        }
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
}
