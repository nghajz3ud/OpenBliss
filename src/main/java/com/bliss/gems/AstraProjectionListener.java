package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.EnergyScale;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.gems.AstraGemListener.ProjectionState;
import com.bliss.goldgem.GoldGem;
import com.bliss.honored.GemAbilityRules;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.io.File;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import com.bliss.gems.AstraDimensionalDriftListener;
import com.bliss.gems.AstraGemListener;
import com.bliss.gems.EntityClickGuard;
import com.bliss.gems.GrantedEffect;
import net.citizensnpcs.api.npc.MetadataStore;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

public final class AstraProjectionListener implements Listener {
    private static final int PASSIVE_INVISIBILITY_TICKS = Integer.MAX_VALUE;
    private static final long INITIAL_COOLDOWN_MS = 300000L;
    private static final double MAX_PROJECTION_SECONDS = 30.0;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final AstraGemListener astra;
    private final GlobalState globalState;
    private final Map<UUID, Integer> lastInteractTick;
    private final File projFile;
    private final Map<UUID, AstraProjectionListener.Snapshot> pendingRecovery;

    private static PotionEffect projectionInvisibility(int var0) {
        PotionEffectType var2 = PotionEffectType.INVISIBILITY;
        return new PotionEffect(var2, Integer.MAX_VALUE, 0, true, false, false);
    }

    public AstraProjectionListener(BlissPlugin var1, PlayerDataStore var2, AstraGemListener var3, GlobalState var4, int var5) {
        ConcurrentHashMap var7 = new ConcurrentHashMap();
        this.lastInteractTick = var7;
        ConcurrentHashMap var17 = new ConcurrentHashMap();
        this.pendingRecovery = var17;
        this.plugin = var1;
        this.players = var2;
        this.astra = var3;
        this.globalState = var4;
        File var23 = var1.getDataFolder();
        File var22 = new File(var23, "astra_projections.yml");
        this.projFile = var22;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent var1) {
        Action var18 = var1.getAction();
        Action var3 = Action.RIGHT_CLICK_AIR;
        if (var18 != var3) {
            Action var20 = var1.getAction();
            Action var80 = Action.RIGHT_CLICK_BLOCK;
            if (var20 != var80) {
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
        Integer var104 = var9;
        var34.put(var8, var104);
        ItemStack var11 = var7.getInventory().getItemInMainHand();
        GemType var84 = GemType.ASTRA;
        boolean var12 = GoldGem.isWieldingSoulMain(var7, var84, 244096655);
        int var42 = BlissCoreItems.gemIdOf(var11, 382441079);
        int var86 = GemType.ASTRA.id();
        if (var42 == var86 || var12) {
            if (var12 || BlissCoreItems.tierOf(var11, 1633828214) == 2) {
                PlayerDataStore var48 = this.players;
                PlayerData var13 = var48.get(var8, 1264010596);
                long var14 = System.currentTimeMillis();
                Map var54 = this.astra.projections;
                if ((ProjectionState)var54.get(var8) != null) {
                    this.disableProjection(var7, 1333627516);
                } else if (!EntityClickGuard.recentlyClickedEntity(var8, 1923725760)) {
                    if (var13.canUsePowers(1616892701)) {
                        if (!AstraDimensionalDriftListener.wearingDisguise(var7, 287048586) && !this.isDriftLive(var8, 826296371)) {
                            Map var68 = this.astra.cd3;
                            Long var17 = (Long)var68.get(var8);
                            if (var17 != null) {
                                long var73 = var17;
                                if (var73 > var14) {
                                    long var93 = var17;
                                    Component var99 = Text.legacy(
                                        "&5\ud83d\udd2e &cYour &7Astral Projection &cskill is on cooldown for &5" + readable(var93 - var14, 682542562),
                                        373192400
                                    );
                                    var7.sendMessage(var99);
                                    return;
                                }
                            }

                            this.activateProjection(var7, 2130828940);
                        } else {
                            Component var102 = Text.legacy("&5\ud83d\udd2e &cYou cannot project while phased out.", 373192400);
                            var7.sendMessage(var102);
                        }
                    }
                }
            }
        }
    }

    private boolean isDriftLive(UUID var1, int var2) {
        Map var9 = this.astra.driftActiveUntil;
        Long var7 = (Long)var9.get(var1);
        if (var7 != null) {
            long var14 = var7;
            long var5 = System.currentTimeMillis();
            if (var14 > var5) {
                return true;
            }
        }

        return false;
    }

    private void activateProjection(Player var1, int var2) {
        if (GemAbilityRules.on(GemAbilityRules.GemAbility.ASTRA_PROJECTION, 425337338)) {
            UUID var12 = var1.getUniqueId();
            Location var13 = var1.getLocation().clone();
            GameMode var14 = var1.getGameMode();
            ItemStack[] var15 = (ItemStack[])var1.getInventory().getContents().clone();
            ItemStack[] var16 = (ItemStack[])var1.getInventory().getArmorContents().clone();
            ItemStack var17 = var1.getInventory().getItemInOffHand().clone();
            ItemStack var18 = var1.getInventory().getItemInMainHand().clone();
            AstraProjectionListener.ProjectionBody var19 = this.spawnBody(var1, var13, var18, var16, var17, 1543493138);
            ItemStack var20 = var18.clone();
            var1.getInventory().clear();
            PlayerInventory var57 = var1.getInventory();
            var57.setItemInMainHand(var20);
            PotionEffect var80 = projectionInvisibility(414576443);
            var1.addPotionEffect(var80);
            GameMode var81 = GameMode.SPECTATOR;
            var1.setGameMode(var81);
            UUID var120 = var19.uid;
            int var127 = var19.npcId;
            ProjectionState var61 = new ProjectionState(var15, var16, var17, var120, var127, var13, var14, 57718088);
            Map var64 = this.astra.projections;
            var64.put(var12, var61);
            BukkitScheduler var66 = Bukkit.getScheduler();
            BlissPlugin var84 = this.plugin;
            Runnable var93 = () -> {
                Player var5 = Bukkit.getPlayer(var12);
                if (var5 != null) {
                    Map var10 = this.astra.projections;
                    Object var11 = var10.get(var12);
                    if (var11 == var61) {
                        this.disableProjection(var5, 1333627516);
                        Component var17x = Text.legacy("&5\ud83d\udd2e &bYour §7Astral Projection &bran out.", 373192400);
                        var5.sendMessage(var17x);
                    }
                }
            };
            String var102 = "astra";
            String var112 = "astral-projection-max";
            long var107 = (long)(GemTuning.duration(var102, var112, 30.0, 812730832) * 20.0);
            var66.runTaskLater(var84, var93, var107);
            this.persist(var12, var15, var16, var17, var13, var14, 1519901451);
            GlobalState var70 = this.globalState;
            long var77;
            if (var70.holdsDragonEgg(var1, 715974514)) {
                var77 = 150000L;
            } else {
                var77 = 300000L;
            }

            Map var74 = this.astra.cd3;
            long var95 = System.currentTimeMillis();
            String var114 = "astra";
            String var126 = "astral-projection";
            long var115 = GemTuning.cd(var114, var126, 300.0, var77, 1621983041);
            PlayerDataStore var132 = this.players;
            int var134 = var132.get(var12, 1264010596).energy(1160853318);
            long var117 = EnergyScale.cd(var115, var134, 907518618);
            Long var99 = var95 + var117;
            var74.put(var12, var99);
            Component var89 = Text.legacy("&5\ud83d\udd2e &bYou have activated §7Astral Projection", 373192400);
            var1.sendMessage(var89);
        }
    }

    // $VF: Handled exception range with multiple entry points by splitting it
    // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
    public void disableProjection(Player var1, int var2) {
        UUID var14 = var1.getUniqueId();
        Map var22 = this.astra.projections;
        ProjectionState var15 = (ProjectionState)var22.remove(var14);
        if (var15 != null) {
            this.unpersist(var14, 1544232872);
            Map var28 = this.pendingRecovery;
            var28.remove(var14);
            Entity var16 = Bukkit.getEntity(var15.zombieUid);
            Location var37;
            if (var16 != null && var16.isValid()) {
                var37 = var16.getLocation();
            } else {
                var37 = var15.originalLocation;
            }

            var1.teleport(var37);
            GameMode var82 = var15.originalGameMode;
            var1.setGameMode(var82);
            var1.setWalkSpeed(0.2F);
            PotionEffect var84 = projectionInvisibility(414576443);
            GrantedEffect.stripLingering(var1, var84, 827997716);
            var1.getInventory().clear();
            PlayerInventory var46 = var1.getInventory();
            ItemStack[] var86 = var15.savedContents;
            var46.setContents(var86);
            PlayerInventory var48 = var1.getInventory();
            ItemStack[] var88 = var15.savedArmor;
            var48.setArmorContents(var88);
            PlayerInventory var50 = var1.getInventory();
            ItemStack var90 = var15.savedOffhand;
            var50.setItemInOffHand(var90);
            label60:
            if (var15.npcId >= 0) {
                label69: {
                    try {
                        NPCRegistry var53 = CitizensAPI.getNPCRegistry();
                        int var92 = var15.npcId;
                        NPC var18 = var53.getById(var92);
                        if (var18 != null) {
                            var18.destroy();
                            break label60;
                        }
                    } catch (Throwable var109) {
                        boolean var10001 = false;
                        break label69;
                    }

                    try {
                        if (var16 != null && var16.isValid()) {
                            var16.remove();
                        }
                        break label60;
                    } catch (Throwable var108) {
                        boolean var110 = false;
                    }
                }

                if (var16 != null && var16.isValid()) {
                    var16.remove();
                }
            } else if (var16 != null && var16.isValid()) {
                var16.remove();
            }

            long var106 = this.disableCooldownMs(var1, 876028539);
            Map var62 = this.astra.cd3;
            long var5 = System.currentTimeMillis();
            String var7 = "astra";
            String var8 = "astral-projection-return";
            long var98 = GemTuning.cd(var7, var8, 120.0, var106, 1621983041);
            PlayerDataStore var103 = this.players;
            int var105 = var103.get(var14, 1264010596).energy(1160853318);
            long var100 = EnergyScale.cd(var98, var105, 907518618);
            Long var97 = var5 + var100;
            var62.put(var14, var97);
        }
    }

    private long disableCooldownMs(Player var1, int var2) {
        PlayerDataStore var8 = this.players;
        UUID var34 = var1.getUniqueId();
        PlayerData var5 = var8.get(var34, 1264010596);
        GlobalState var11 = this.globalState;
        boolean var6 = var11.holdsDragonEgg(var1, 715974514);
        int var7 = var5.energy(1160853318);
        if (var7 > 5) {
            long var30;
            if (var6) {
                var30 = 60000L;
            } else {
                var30 = 120000L;
            }

            return var30;
        } else if (var7 == 5) {
            long var27;
            if (var6) {
                var27 = 65000L;
            } else {
                var27 = 135000L;
            }

            return var27;
        } else if (var7 == 4) {
            long var24;
            if (var6) {
                var24 = 75000L;
            } else {
                var24 = 150000L;
            }

            return var24;
        } else if (var7 == 3) {
            long var21;
            if (var6) {
                var21 = 90000L;
            } else {
                var21 = 180000L;
            }

            return var21;
        } else {
            long var32;
            if (var6) {
                var32 = 105000L;
            } else {
                var32 = 210000L;
            }

            return var32;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private ProjectionBody spawnBody(Player player, Location location, ItemStack itemStack, ItemStack[] itemStackArray, ItemStack itemStack2, int n) {
        if (Bukkit.getPluginManager().isPluginEnabled("Citizens")) {
            try {
                Equipment equipment;
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
                NPC.Metadata metadata = NPC.Metadata.DEFAULT_PROTECTED;
                Boolean bl = false;
                metadataStore.setPersistent(metadata, (Object)bl);
                MetadataStore metadataStore2 = nPC.data();
                NPC.Metadata metadata2 = NPC.Metadata.SHOULD_SAVE;
                Boolean bl2 = false;
                metadataStore2.setPersistent(metadata2, (Object)bl2);
                Equipment equipment2 = equipment = (Equipment)nPC.getOrAddTrait(Equipment.class);
                Equipment.EquipmentSlot equipmentSlot = Equipment.EquipmentSlot.HAND;
                ItemStack itemStack3 = AstraProjectionListener.clone(itemStack, 1374690825);
                equipment2.set(equipmentSlot, itemStack3);
                Equipment equipment3 = equipment;
                Equipment.EquipmentSlot equipmentSlot2 = Equipment.EquipmentSlot.OFF_HAND;
                ItemStack itemStack4 = AstraProjectionListener.clone(itemStack2, 1374690825);
                equipment3.set(equipmentSlot2, itemStack4);
                Equipment equipment4 = equipment;
                Equipment.EquipmentSlot equipmentSlot3 = Equipment.EquipmentSlot.HELMET;
                ItemStack itemStack5 = AstraProjectionListener.clone(itemStackArray[3], 1374690825);
                equipment4.set(equipmentSlot3, itemStack5);
                Equipment equipment5 = equipment;
                Equipment.EquipmentSlot equipmentSlot4 = Equipment.EquipmentSlot.CHESTPLATE;
                ItemStack itemStack6 = AstraProjectionListener.clone(itemStackArray[2], 1374690825);
                equipment5.set(equipmentSlot4, itemStack6);
                Equipment equipment6 = equipment;
                Equipment.EquipmentSlot equipmentSlot5 = Equipment.EquipmentSlot.LEGGINGS;
                ItemStack itemStack7 = AstraProjectionListener.clone(itemStackArray[1], 1374690825);
                equipment6.set(equipmentSlot5, itemStack7);
                Equipment equipment7 = equipment;
                Equipment.EquipmentSlot equipmentSlot6 = Equipment.EquipmentSlot.BOOTS;
                ItemStack itemStack8 = AstraProjectionListener.clone(itemStackArray[0], 1374690825);
                equipment7.set(equipmentSlot6, itemStack8);
                NPC nPC2 = nPC;
                Location location2 = location;
                nPC2.spawn(location2);
                Entity entity = nPC.getEntity();
                if (entity instanceof LivingEntity) {
                    LivingEntity livingEntity;
                    LivingEntity livingEntity2 = livingEntity = (LivingEntity)entity;
                    Attribute attribute = Attribute.MAX_HEALTH;
                    AttributeInstance attributeInstance = livingEntity2.getAttribute(attribute);
                    Player player3 = player;
                    Attribute attribute2 = Attribute.MAX_HEALTH;
                    AttributeInstance attributeInstance2 = player3.getAttribute(attribute2);
                    if (attributeInstance != null && attributeInstance2 != null) {
                        AttributeInstance attributeInstance3 = attributeInstance;
                        double d = attributeInstance2.getBaseValue();
                        attributeInstance3.setBaseValue(d);
                    }
                    double d = attributeInstance != null ? attributeInstance.getValue() : player.getHealth();
                    double d2 = d;
                    LivingEntity livingEntity3 = livingEntity;
                    double d3 = d2;
                    double d4 = player.getHealth();
                    double d5 = Math.min(d3, d4);
                    double d6 = Math.max(1.0, d5);
                    livingEntity3.setHealth(d6);
                }
                UUID uUID = nPC.getEntity().getUniqueId();
                int n2 = nPC.getId();
                return new ProjectionBody(uUID, n2, 1670378012);
            }
            catch (Throwable throwable) {
                Logger logger = this.plugin.getLogger();
                String string = "Astral Projection: Citizens NPC spawn failed (" + throwable.getMessage() + ") \u2014 falling back to the zombie body.";
                logger.warning(string);
            }
        }
        AstraProjectionListener astraProjectionListener = this;
        Player player4 = player;
        Location location3 = location;
        ItemStack itemStack9 = itemStack;
        ItemStack[] itemStackArray2 = itemStackArray;
        ItemStack itemStack10 = itemStack2;
        UUID uUID = astraProjectionListener.spawnFallbackZombie(player4, location3, itemStack9, itemStackArray2, itemStack10, 782902853);
        return new ProjectionBody(uUID, -1, 1670378012);
    }

    private UUID spawnFallbackZombie(Player var1, Location var2, ItemStack var3, ItemStack[] var4, ItemStack var5, int var6) {
        World var20 = var1.getWorld();
        EntityType var9 = EntityType.ZOMBIE;
        Zombie var16 = (Zombie)var20.spawnEntity(var2, var9);
        TextComponent var75 = Component.text(var1.getName());
        var16.customName(var75);
        var16.setCustomNameVisible(true);
        if (var16.getEquipment() != null) {
            EntityEquipment var28 = var16.getEquipment();
            ItemStack var78 = clone(var3, 1374690825);
            var28.setItemInMainHand(var78);
            EntityEquipment var30 = var16.getEquipment();
            ItemStack var80 = clone(var5, 1374690825);
            var30.setItemInOffHand(var80);
            EntityEquipment var32 = var16.getEquipment();
            ItemStack var82 = var4[3];
            var32.setHelmet(var82);
            EntityEquipment var34 = var16.getEquipment();
            ItemStack var84 = var4[2];
            var34.setChestplate(var84);
            EntityEquipment var36 = var16.getEquipment();
            ItemStack var86 = var4[1];
            var36.setLeggings(var86);
            EntityEquipment var38 = var16.getEquipment();
            ItemStack var88 = var4[0];
            var38.setBoots(var88);
            var16.getEquipment().setHelmetDropChance(0.0F);
            var16.getEquipment().setChestplateDropChance(0.0F);
            var16.getEquipment().setLeggingsDropChance(0.0F);
            var16.getEquipment().setBootsDropChance(0.0F);
            var16.getEquipment().setItemInMainHandDropChance(0.0F);
            var16.getEquipment().setItemInOffHandDropChance(0.0F);
        }

        PotionEffectType var10 = PotionEffectType.SLOWNESS;
        PotionEffect var95 = new PotionEffect(var10, 1198800, 254, true, false, false);
        var16.addPotionEffect(var95);
        PotionEffectType var107 = PotionEffectType.RESISTANCE;
        PotionEffect var96 = new PotionEffect(var107, 1198800, 254, true, false, false);
        var16.addPotionEffect(var96);
        PotionEffectType var108 = PotionEffectType.FIRE_RESISTANCE;
        PotionEffect var97 = new PotionEffect(var108, 1198800, 254, true, false, false);
        var16.addPotionEffect(var97);
        PotionEffectType var109 = PotionEffectType.WATER_BREATHING;
        PotionEffect var98 = new PotionEffect(var109, 1198800, 254, true, false, false);
        var16.addPotionEffect(var98);
        var16.setRemoveWhenFarAway(false);
        var16.setPersistent(false);
        return var16.getUniqueId();
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

    private void persist(UUID uUID, ItemStack[] itemStackArray, ItemStack[] itemStackArray2, ItemStack itemStack, Location location, GameMode gameMode, int n) {
        try {
            ItemStack itemStack2;
            YamlConfiguration yamlConfiguration = this.projFile.isFile() ? YamlConfiguration.loadConfiguration((File)this.projFile) : new YamlConfiguration();
            YamlConfiguration yamlConfiguration2 = yamlConfiguration;
            String string = uUID.toString() + ".";
            YamlConfiguration yamlConfiguration3 = yamlConfiguration2;
            String string2 = string + "contents";
            String string3 = AstraProjectionListener.enc(itemStackArray, 1391173588);
            yamlConfiguration3.set(string2, (Object)string3);
            YamlConfiguration yamlConfiguration4 = yamlConfiguration2;
            String string4 = string + "armor";
            String string5 = AstraProjectionListener.enc(itemStackArray2, 1391173588);
            yamlConfiguration4.set(string4, (Object)string5);
            YamlConfiguration yamlConfiguration5 = yamlConfiguration2;
            String string6 = string + "offhand";
            ItemStack[] itemStackArray3 = new ItemStack[1];
            if (itemStack == null) {
                Material material = Material.AIR;
                itemStack2 = new ItemStack(material);
            } else {
                itemStack2 = itemStack;
            }
            itemStackArray3[0] = itemStack2;
            String string7 = AstraProjectionListener.enc(itemStackArray3, 1391173588);
            yamlConfiguration5.set(string6, (Object)string7);
            if (location != null && location.getWorld() != null) {
                YamlConfiguration yamlConfiguration6 = yamlConfiguration2;
                String string8 = string + "world";
                String string9 = location.getWorld().getName();
                yamlConfiguration6.set(string8, (Object)string9);
                YamlConfiguration yamlConfiguration7 = yamlConfiguration2;
                String string10 = string + "x";
                Double d = location.getX();
                yamlConfiguration7.set(string10, (Object)d);
                YamlConfiguration yamlConfiguration8 = yamlConfiguration2;
                String string11 = string + "y";
                Double d2 = location.getY();
                yamlConfiguration8.set(string11, (Object)d2);
                YamlConfiguration yamlConfiguration9 = yamlConfiguration2;
                String string12 = string + "z";
                Double d3 = location.getZ();
                yamlConfiguration9.set(string12, (Object)d3);
                YamlConfiguration yamlConfiguration10 = yamlConfiguration2;
                String string13 = string + "yaw";
                Double d4 = (double)location.getYaw();
                yamlConfiguration10.set(string13, (Object)d4);
                YamlConfiguration yamlConfiguration11 = yamlConfiguration2;
                String string14 = string + "pitch";
                Double d5 = (double)location.getPitch();
                yamlConfiguration11.set(string14, (Object)d5);
            }
            YamlConfiguration yamlConfiguration12 = yamlConfiguration2;
            String string15 = string + "mode";
            String string16 = gameMode.name();
            yamlConfiguration12.set(string15, (Object)string16);
            YamlConfiguration yamlConfiguration13 = yamlConfiguration2;
            File file = this.projFile;
            yamlConfiguration13.save(file);
        }
        catch (Exception exception) {
            Logger logger = this.plugin.getLogger();
            String string = "Astral Projection: failed to persist crash snapshot (" + exception.getMessage() + ")";
            logger.warning(string);
        }
    }

    private void unpersist(UUID var1, int var2) {
        if (this.projFile.isFile()) {
            Exception var10000;
            label27: {
                YamlConfiguration var6;
                try {
                    var6 = YamlConfiguration.loadConfiguration(this.projFile);
                    String var20 = var1.toString();
                    if (!var6.contains(var20)) {
                        return;
                    }
                } catch (Exception var30) {
                    var10000 = var30;
                    break label27;
                }

                try {
                    String var22 = var1.toString();
                    var6.set(var22, null);
                    File var24 = this.projFile;
                    var6.save(var24);
                    return;
                } catch (Exception var29) {
                    var10000 = var29;
                }
            }

            Exception var28 = var10000;
            Logger var19 = this.plugin.getLogger();
            String var27 = "Astral Projection: failed to clear crash snapshot (" + var28.getMessage() + ")";
            var19.warning(var27);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void loadPending(int n) {
        if (!this.projFile.isFile()) {
            return;
        }
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)this.projFile);
        for (String string : yamlConfiguration.getKeys(false)) {
            try {
                UUID uUID = UUID.fromString(string);
                String string2 = string + ".";
                YamlConfiguration yamlConfiguration2 = yamlConfiguration;
                String string3 = string2 + "contents";
                ItemStack[] itemStackArray2 = AstraProjectionListener.dec(yamlConfiguration2.getString(string3), 1398760493);
                YamlConfiguration yamlConfiguration3 = yamlConfiguration;
                String string4 = string2 + "armor";
                ItemStack[] itemStackArray = AstraProjectionListener.dec(yamlConfiguration3.getString(string4), 1398760493);
                YamlConfiguration yamlConfiguration4 = yamlConfiguration;
                String string5 = string2 + "offhand";
                ItemStack[] itemStackArray3 = AstraProjectionListener.dec(yamlConfiguration4.getString(string5), 1398760493);
                ItemStack itemStack2 = itemStackArray3 != null && itemStackArray3.length > 0 ? itemStackArray3[0] : null;
                ItemStack itemStack = itemStack2;
                Location location = null;
                YamlConfiguration yamlConfiguration5 = yamlConfiguration;
                String string6 = string2 + "world";
                String string7 = yamlConfiguration5.getString(string6);
                if (string7 != null && Bukkit.getWorld((String)string7) != null) {
                    Location location2;
                    World world = Bukkit.getWorld((String)string7);
                    YamlConfiguration yamlConfiguration6 = yamlConfiguration;
                    String string8 = string2 + "x";
                    double d = yamlConfiguration6.getDouble(string8);
                    YamlConfiguration yamlConfiguration7 = yamlConfiguration;
                    String string9 = string2 + "y";
                    double d2 = yamlConfiguration7.getDouble(string9);
                    YamlConfiguration yamlConfiguration8 = yamlConfiguration;
                    String string10 = string2 + "z";
                    double d3 = yamlConfiguration8.getDouble(string10);
                    YamlConfiguration yamlConfiguration9 = yamlConfiguration;
                    String string11 = string2 + "yaw";
                    float f = (float)yamlConfiguration9.getDouble(string11);
                    YamlConfiguration yamlConfiguration10 = yamlConfiguration;
                    String string12 = string2 + "pitch";
                    float f2 = (float)yamlConfiguration10.getDouble(string12);
                    location = location2 = new Location(world, d, d2, d3, f, f2);
                }
                GameMode gameMode = GameMode.SURVIVAL;
                try {
                    YamlConfiguration yamlConfiguration11 = yamlConfiguration;
                    String string13 = string2 + "mode";
                    String string14 = "SURVIVAL";
                    gameMode = GameMode.valueOf((String)yamlConfiguration11.getString(string13, string14));
                }
                catch (Exception exception) {}
                Map<UUID, Snapshot> map = this.pendingRecovery;
                UUID uUID2 = uUID;
                ItemStack[] itemStackArray4 = itemStackArray2;
                ItemStack[] itemStackArray5 = itemStackArray;
                ItemStack itemStack3 = itemStack;
                Location location3 = location;
                GameMode gameMode2 = gameMode;
                Snapshot snapshot = new Snapshot(itemStackArray4, itemStackArray5, itemStack3, location3, gameMode2, 58887734);
                map.put(uUID2, snapshot);
            }
            catch (Exception exception) {
                Logger logger = this.plugin.getLogger();
                String string15 = "Astral Projection: skipping unreadable crash snapshot '" + string + "'";
                logger.warning(string15);
            }
        }
        if (!this.pendingRecovery.isEmpty()) {
            Logger logger = this.plugin.getLogger();
            String string = "Astral Projection: " + this.pendingRecovery.size() + " projection(s) to recover from a prior crash.";
            logger.info(string);
            for (Player player : Bukkit.getOnlinePlayers()) {
                AstraProjectionListener astraProjectionListener = this;
                Player player2 = player;
                astraProjectionListener.recover(player2, 128528847);
            }
        }
    }

    private void recover(Player var1, int var2) {
        Map var7 = this.pendingRecovery;
        UUID var37 = var1.getUniqueId();
        AstraProjectionListener.Snapshot var6 = (AstraProjectionListener.Snapshot)var7.remove(var37);
        if (var6 != null) {
            var1.getInventory().clear();
            if (var6.contents != null) {
                PlayerInventory var16 = var1.getInventory();
                ItemStack[] var39 = var6.contents;
                var16.setContents(var39);
            }

            if (var6.armor != null) {
                PlayerInventory var20 = var1.getInventory();
                ItemStack[] var41 = var6.armor;
                var20.setArmorContents(var41);
            }

            PlayerInventory var22 = var1.getInventory();
            ItemStack var43 = var6.offhand;
            var22.setItemInOffHand(var43);
            GameMode var45 = var6.mode;
            GameMode var5 = GameMode.SPECTATOR;
            GameMode var47;
            if (var45 == var5) {
                var47 = GameMode.SURVIVAL;
            } else {
                var47 = var6.mode;
            }

            var1.setGameMode(var47);
            var1.setWalkSpeed(0.2F);
            PotionEffect var49 = projectionInvisibility(414576443);
            GrantedEffect.stripLingering(var1, var49, 827997716);
            if (var6.loc != null && var6.loc.getWorld() != null) {
                Location var51 = var6.loc;
                var1.teleport(var51);
            }

            UUID var53 = var1.getUniqueId();
            this.unpersist(var53, 1544232872);
            Logger var36 = this.plugin.getLogger();
            String var56 = "Astral Projection: recovered " + var1.getName() + " from a crashed projection.";
            var36.info(var56);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent var1) {
        Player var4 = var1.getPlayer();
        this.recover(var4, 128528847);
    }

    private static String enc(ItemStack[] var0, int var1) {
        Encoder var2 = Base64.getEncoder();
        byte[] var5 = ItemStack.serializeItemsAsBytes(var0);
        return var2.encodeToString(var5);
    }

    private static ItemStack[] dec(String var0, int var1) {
        if (var0 != null && !var0.isEmpty()) {
            Decoder var6 = Base64.getDecoder();
            return ItemStack.deserializeItemsFromBytes(var6.decode(var0));
        } else {
            return null;
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

    private static final class ProjectionBody {
        final UUID uid;
        final int npcId;

        ProjectionBody(UUID var1, int var2, int var3) {
            this.uid = var1;
            this.npcId = var2;
        }
    }

    private static final class Snapshot {
        final ItemStack[] contents;
        final ItemStack[] armor;
        final ItemStack offhand;
        final Location loc;
        final GameMode mode;

        Snapshot(ItemStack[] var1, ItemStack[] var2, ItemStack var3, Location var4, GameMode var5, int var6) {
            this.contents = var1;
            this.armor = var2;
            this.offhand = var3;
            this.loc = var4;
            this.mode = var5;
        }
    }
}
