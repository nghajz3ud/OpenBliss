package com.bliss.nick;

import com.bliss.BlissPlugin;
import com.bliss.core.AtomicYml;
import com.bliss.goldgem.DreamChannels;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.api.trait.trait.Equipment.EquipmentSlot;
import net.citizensnpcs.trait.SkinTrait;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import com.bliss.nick.MemoryRoster;
import com.bliss.nick.NickManager;
import java.util.Iterator;
import net.citizensnpcs.api.npc.MetadataStore;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;

public final class MemoryNpcManager implements Listener {
    private static final int TICK_PERIOD = 10;
    private static final float SPRINT_SPEED = 1.3F;
    private static final double WANDER_MIN = 6.0;
    private static final double WANDER_MAX = 14.0;
    private final BlissPlugin plugin;
    private final NickManager nick;
    private final MemoryRoster roster;
    private final BlissCoreItems coreItems;
    private final File file;
    private final YamlConfiguration cfg;
    private final boolean citizens;
    private final Map<String, MemoryNpcManager.Mem> live;
    private final Map<Integer, MemoryNpcManager.Mem> byNpcId;
    private final List<ItemStack> loot;
    private int taskId;

    public MemoryNpcManager(BlissPlugin var1, NickManager var2, MemoryRoster var3, BlissCoreItems var4, int var5) {
        HashMap var7 = new HashMap();
        this.live = var7;
        HashMap var43 = new HashMap();
        this.byNpcId = var43;
        ArrayList var44 = new ArrayList();
        this.loot = var44;
        this.taskId = -1;
        this.plugin = var1;
        this.nick = var2;
        this.roster = var3;
        this.coreItems = var4;
        File var61 = var1.getDataFolder();
        File var50 = new File(var61, "memory_npcs.yml");
        this.file = var50;
        YamlConfiguration var53 = YamlConfiguration.loadConfiguration(this.file);
        this.cfg = var53;
        this.loadLoot(1839273743);
        boolean var54 = detectCitizens(1289289396);
        this.citizens = var54;
        if (this.cfg.isConfigurationSection("spawned")) {
            for (String var11 : this.cfg.getConfigurationSection("spawned").getKeys(false)) {
                YamlConfiguration var39 = this.cfg;
                String var59 = "spawned." + var11 + ".name";
                String var12 = var39.getString(var59);
                if (var12 != null) {
                    var3.claimSpecificName(var12, 80432002);
                }
            }
        }
    }

    private static boolean detectCitizens(int var0) {
        try {
            Class.forName("net.citizensnpcs.api.CitizensAPI");
            return true;
        } catch (Throwable var1) {
            return false;
        }
    }

    public boolean available(int var1) {
        return this.citizens;
    }

    public String spawn(Location var1, String var2, int var3) {
        if (this.citizens && var1 != null && var1.getWorld() != null) {
            String var10 = this.roster.claimFreeName(1102006638);
            if (var10 == null) {
                return null;
            } else {
                String var11 = UUID.randomUUID().toString().substring(0, 8);
                Location var44 = var1.clone();
                MemoryNpcManager.Mem var25 = new MemoryNpcManager.Mem(var11, var10, var2, var44, 1239108536);
                if (!this.spawnEntity(var25, 1679595430)) {
                    MemoryRoster var35 = this.roster;
                    var35.releaseName(var10, 451165245);
                    return null;
                } else {
                    Map var29 = this.live;
                    var29.put(var11, var25);
                    this.persist(var25, 628403900);
                    this.start(522880496);
                    return var10;
                }
            }
        } else {
            return null;
        }
    }

    public void reload(int var1) {
        if (this.citizens) {
            this.start(522880496);
            if (this.cfg.isConfigurationSection("spawned")) {
                Set<String> var71 = this.cfg.getConfigurationSection("spawned").getKeys(false);

                for (String var15 : new ArrayList<>(var71)) {
                    String var16 = "spawned." + var15 + ".";
                    YamlConfiguration var38 = this.cfg;
                    String var61 = var16 + "world";
                    String var17 = var38.getString(var61);
                    World var59;
                    if (var17 == null) {
                        var59 = null;
                    } else {
                        var59 = Bukkit.getWorld(var17);
                    }

                    if (var59 != null) {
                        YamlConfiguration var77 = this.cfg;
                        String var81 = var16 + "x";
                        double var78 = var77.getDouble(var81);
                        YamlConfiguration var83 = this.cfg;
                        String var87 = var16 + "y";
                        double var84 = var83.getDouble(var87);
                        YamlConfiguration var88 = this.cfg;
                        String var91 = var16 + "z";
                        double var89 = var88.getDouble(var91);
                        YamlConfiguration var92 = this.cfg;
                        String var96 = var16 + "yaw";
                        float var95 = (float)var92.getDouble(var96);
                        YamlConfiguration var98 = this.cfg;
                        String var102 = var16 + "pitch";
                        float var101 = (float)var98.getDouble(var102);
                        Location var42 = new Location(var59, var78, var84, var89, var95, var101);
                        YamlConfiguration var44 = this.cfg;
                        String var63 = var16 + "name";
                        String var20 = var44.getString(var63);
                        YamlConfiguration var47 = this.cfg;
                        String var65 = var16 + "skin";
                        String var21 = var47.getString(var65);
                        if (var20 != null) {
                            MemoryRoster var51 = this.roster;
                            var51.claimSpecificName(var20, 80432002);
                            MemoryNpcManager.Mem var52 = new MemoryNpcManager.Mem(var15, var20, var21, var42, 1239108536);
                            if (this.spawnEntity(var52, 1679595430)) {
                                Map var56 = this.live;
                                var56.put(var15, var52);
                            }
                        }
                    }
                }
            }
        }
    }

    public int clearAll(int var1) {
        int var4 = 0;
        Collection<MemoryNpcManager.Mem> var24 = this.live.values();

        for (MemoryNpcManager.Mem var6 : new ArrayList<>(var24)) {
            this.despawn(var6, false, 385547572);
            var4++;
        }

        this.live.clear();
        YamlConfiguration var18 = this.cfg;
        String var22 = "spawned";
        var18.set(var22, null);
        this.save(1108236475);
        return var4;
    }

    public int count() {
        return this.live.size();
    }

    public List<String> liveNames(int var1) {
        ArrayList var2 = new ArrayList();
        ArrayList var3 = var2;

        for (MemoryNpcManager.Mem var5 : this.live.values()) {
            String var19 = var5.name;
            var3.add(var19);
        }

        return var3;
    }

    public List<Location> liveLocations(int var1) {
        ArrayList var2 = new ArrayList();
        ArrayList var3 = var2;

        for (MemoryNpcManager.Mem var5 : this.live.values()) {
            if (var5.npc != null && var5.npc.isSpawned() && var5.npc.getEntity() != null) {
                Location var29 = var5.npc.getEntity().getLocation();
                var3.add(var29);
            }
        }

        return var3;
    }

    private MemoryNpcManager.Mem byName(String var1, int var2) {
        for (MemoryNpcManager.Mem var6 : this.live.values()) {
            String var16 = var6.name;
            if (var16.equalsIgnoreCase(var1)) {
                return var6;
            }
        }

        return null;
    }

    public boolean rename(String var1, String var2, int var3) {
        MemoryNpcManager.Mem var6 = this.byName(var1, 1964018668);
        if (var6 != null) {
            MemoryRoster var11 = this.roster;
            if (var11.isNameFree(var2, 1383580576)) {
                MemoryRoster var15 = this.roster;
                String var38 = var6.name;
                var15.releaseName(var38, 451165245);
                MemoryRoster var17 = this.roster;
                var17.claimSpecificName(var2, 80432002);
                var6.name = var2;

                try {
                    if (var6.npc != null) {
                        Entity var7 = var6.npc.getEntity();
                        NPC var25 = var6.npc;
                        var25.setName(var2);
                        Entity var28 = var6.npc.getEntity();
                        if (var28 == var7) {
                            respawnInPlace(var6, 1105160141);
                        }
                    }
                } catch (Throwable var48) {
                    Logger var35 = this.plugin.getLogger();
                    String var46 = "NPC memory rename: " + var48.getMessage();
                    var35.warning(var46);
                }

                this.persist(var6, 628403900);
                return true;
            }
        }

        return false;
    }

    // $VF: Handled exception range with multiple entry points by splitting it
    // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
    public boolean reskin(String var1, String var2, int var3) {
        MemoryNpcManager.Mem var6 = this.byName(var1, 1964018668);
        if (var6 == null) {
            return false;
        } else {
            var6.skin = var2;

            label44: {
                Throwable var10000;
                label50: {
                    SkinTrait var7;
                    label52: {
                        try {
                            if (var6.npc == null) {
                                break label44;
                            }

                            var7 = (SkinTrait)var6.npc.getOrAddTrait(SkinTrait.class);
                            if (var2 != null && !var2.isBlank()) {
                                break label52;
                            }
                        } catch (Throwable var40) {
                            var10000 = var40;
                            boolean var10001 = false;
                            break label50;
                        }

                        try {
                            var7.clearTexture();
                            respawnInPlace(var6, 1105160141);
                            break label44;
                        } catch (Throwable var39) {
                            var10000 = var39;
                            boolean var41 = false;
                            break label50;
                        }
                    }

                    try {
                        var7.setSkinName(var2);
                        break label44;
                    } catch (Throwable var38) {
                        var10000 = var38;
                        boolean var42 = false;
                    }
                }

                Throwable var37 = var10000;
                Logger var24 = this.plugin.getLogger();
                String var35 = "NPC memory skin: " + var37.getMessage();
                var24.warning(var35);
            }

            this.persist(var6, 628403900);
            return true;
        }
    }

    private static void respawnInPlace(MemoryNpcManager.Mem var0, int var1) {
        if (var0.npc != null && var0.npc.isSpawned() && var0.npc.getEntity() != null) {
            Location var4 = var0.npc.getEntity().getLocation();
            NPC var17 = var0.npc;
            DespawnReason var3 = DespawnReason.PENDING_RESPAWN;
            var17.despawn(var3);
            NPC var20 = var0.npc;
            var20.spawn(var4);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private boolean spawnEntity(Mem mem, int n) {
        int n2;
        NPC nPC;
        NPC nPC2;
        try {
            NPCRegistry nPCRegistry = CitizensAPI.getNPCRegistry();
            EntityType entityType = EntityType.PLAYER;
            String string = mem.name;
            nPC2 = nPCRegistry.createNPC(entityType, string);
            if (mem.skin != null && !mem.skin.isBlank()) {
                try {
                    SkinTrait skinTrait = (SkinTrait)nPC2.getOrAddTrait(SkinTrait.class);
                    String string2 = mem.skin;
                    skinTrait.setSkinName(string2);
                }
                catch (Throwable throwable) {}
            }
        }
        catch (Throwable throwable) {
            Logger logger = this.plugin.getLogger();
            String string = "NPC memory spawn failed: " + throwable.getMessage();
            logger.warning(string);
            return false;
        }
        MetadataStore metadataStore = nPC2.data();
        NPC.Metadata metadata = NPC.Metadata.SHOULD_SAVE;
        Boolean bl = false;
        metadataStore.setPersistent(metadata, (Object)bl);
        MetadataStore metadataStore2 = nPC2.data();
        NPC.Metadata metadata2 = NPC.Metadata.DEFAULT_PROTECTED;
        Boolean bl2 = false;
        metadataStore2.setPersistent(metadata2, (Object)bl2);
        MetadataStore metadataStore3 = nPC2.data();
        NPC.Metadata metadata3 = NPC.Metadata.RESPAWN_DELAY;
        Integer n3 = -1;
        metadataStore3.setPersistent(metadata3, (Object)n3);
        Mem mem2 = mem;
        mem2.npc = nPC = nPC2;
        Mem mem3 = mem;
        mem3.npcId = n2 = nPC2.getId();
        Map<Integer, Mem> map = this.byNpcId;
        Integer n4 = mem.npcId;
        Mem mem4 = mem;
        map.put(n4, mem4);
        mem.geared = false;
        NPC nPC3 = nPC2;
        Location location = mem.home;
        nPC3.spawn(location);
        BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
        BlissPlugin blissPlugin = this.plugin;
        MemoryNpcManager memoryNpcManager = this;
        Mem mem5 = mem;
        Runnable runnable = () -> this.gear(mem5, 96978881);
        bukkitScheduler.runTaskLater((Plugin)blissPlugin, runnable, 2L);
        return true;
    }

    private void gear(MemoryNpcManager.Mem var1, int var2) {
        if (var1.npc != null && var1.npc.isSpawned()) {
            try {
                Equipment var7 = (Equipment)var1.npc.getOrAddTrait(Equipment.class);
                EquipmentSlot var26 = EquipmentSlot.HELMET;
                Material var6 = Material.DIAMOND_HELMET;
                ItemStack var5 = new ItemStack(var6);
                var7.set(var26, var5);
                EquipmentSlot var27 = EquipmentSlot.CHESTPLATE;
                Material var39 = Material.DIAMOND_CHESTPLATE;
                ItemStack var35 = new ItemStack(var39);
                var7.set(var27, var35);
                EquipmentSlot var28 = EquipmentSlot.LEGGINGS;
                Material var40 = Material.DIAMOND_LEGGINGS;
                ItemStack var36 = new ItemStack(var40);
                var7.set(var28, var36);
                EquipmentSlot var29 = EquipmentSlot.BOOTS;
                Material var41 = Material.DIAMOND_BOOTS;
                ItemStack var37 = new ItemStack(var41);
                var7.set(var29, var37);
                EquipmentSlot var30 = EquipmentSlot.HAND;
                Material var42 = Material.DIAMOND_SWORD;
                ItemStack var38 = new ItemStack(var42);
                var7.set(var30, var38);
                var1.geared = true;
            } catch (Throwable var44) {
                Logger var25 = this.plugin.getLogger();
                String var34 = "NPC memory gear failed: " + var44.getMessage();
                var25.warning(var34);
            }
        }
    }

    private void start(int var1) {
        if (this.citizens && this.taskId == -1) {
            BukkitScheduler var14 = Bukkit.getScheduler();
            BlissPlugin var17 = this.plugin;
            Runnable var18 = this::tickAll;
            int var16 = var14.runTaskTimer(var17, var18, 10L, 10L).getTaskId();
            this.taskId = var16;
        }
    }

    private void tickAll() {
        if (!this.live.isEmpty()) {
            long var3 = System.currentTimeMillis();
            Collection<MemoryNpcManager.Mem> var33 = this.live.values();

            for (MemoryNpcManager.Mem var6 : new ArrayList<>(var33)) {
                try {
                    this.tick(var6, var3, 1612632777);
                } catch (Throwable var38) {
                    if (!var6.warned) {
                        var6.warned = true;
                        Logger var26 = this.plugin.getLogger();
                        String var30 = var6.name;
                        String var37 = String.valueOf(var38);
                        String var31 = "NPC memory " + var30 + " tick failed: " + var37;
                        var26.warning(var31);
                    }
                }
            }
        }
    }

    private void tick(MemoryNpcManager.Mem var1, long var2, int var4) {
        NPC var15 = var1.npc;
        if (var15 != null && var15.isSpawned()) {
            if (var1.geared) {
                LivingEntity var16 = (LivingEntity)var15.getEntity();
                if (var16 != null) {
                    try {
                        ((Player)var16).setSprinting(true);
                    } catch (Throwable var131) {
                    }

                    long var98 = var1.nextWander;
                    if (var2 >= var98 || !var15.getNavigator().isNavigating()) {
                        long var78 = var2 + 3000L;
                        long var117 = (long)ThreadLocalRandom.current().nextInt(3000);
                        long var80 = var78 + var117;
                        var1.nextWander = var80;
                        double var17 = ThreadLocalRandom.current().nextDouble() * Math.PI * 2.0;
                        double var107 = ThreadLocalRandom.current().nextDouble() * 8.0;
                        double var19 = 6.0 + var107;
                        World var21 = var1.home.getWorld();
                        if (var21 == null) {
                            return;
                        }

                        Location var60 = var1.home.clone();
                        double var84 = Math.cos(var17);
                        double var86 = var84 * var19;
                        double var126 = Math.sin(var17);
                        double var128 = var126 * var19;
                        Location var22 = var60.add(var86, 0.0, var128);
                        int var90 = var22.getBlockX() >> 4;
                        int var112 = var22.getBlockZ() >> 4;
                        if (!var21.isChunkLoaded(var90, var112)) {
                            var22 = var1.home.clone();
                        }

                        double var94 = (double)(var21.getHighestBlockYAt(var22) + 1);
                        var22.setY(var94);

                        try {
                            Navigator var69 = var15.getNavigator();
                            var69.setTarget(var22);
                            var15.getNavigator().getLocalParameters().speedModifier(1.3F);
                        } catch (Throwable var130) {
                        }
                    }
                }
            }
        }
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    public void onNpcPlayerDeath(PlayerDeathEvent var1) {
        if (this.citizens) {
            NPC var4;
            try {
                NPCRegistry var7 = CitizensAPI.getNPCRegistry();
                Player var15 = var1.getEntity();
                var4 = var7.getNPC(var15);
            } catch (Throwable var19) {
                return;
            }

            if (var4 != null) {
                Map var12 = this.byNpcId;
                Integer var18 = var4.getId();
                if (var12.containsKey(var18)) {
                    var1.deathMessage(null);
                }
            }
        }
    }

    void handleDeath(Object var1, int var2) {
        NPC var9 = (NPC)var1;
        Map var15 = this.byNpcId;
        Integer var62 = var9.getId();
        MemoryNpcManager.Mem var10 = (MemoryNpcManager.Mem)var15.get(var62);
        if (var10 != null && !var10.destroyedByUs) {
            Location var60;
            if (var9.getEntity() != null) {
                var60 = var9.getEntity().getLocation();
            } else {
                var60 = var9.getStoredLocation();
            }

            Location var11 = var60;
            if (var60 != null && var60.getWorld() != null) {
                double var28 = var60.getY();
                double var82 = (double)var60.getWorld().getMinHeight();
                if (var28 < var82) {
                    var11 = var10.home;
                }
            }

            World var35;
            if (var11 == null) {
                var35 = null;
            } else {
                var35 = var11.getWorld();
            }

            boolean var36 = DreamChannels.sideOf(var35, 1949642301);
            Component var66 = Text.legacy("&e" + var10.name + " has been purged.", 373192400);
            DreamChannels.sendSide(var36, var66, 1024104737);
            if (var11 != null && var11.getWorld() != null) {
                World var41 = var11.getWorld();
                var41.strikeLightningEffect(var11);
                World var44 = var11.getWorld();
                Sound var84 = Sound.ENTITY_WITHER_DEATH;
                SoundCategory var6 = SoundCategory.MASTER;
                var44.playSound(var11, var84, var6, 3.0F, 0.5F);
                this.dropLoot(var11, 1746535971);
            }

            var10.destroyedByUs = true;
            Map var48 = this.live;
            String var72 = var10.id;
            var48.remove(var72);
            MemoryRoster var51 = this.roster;
            String var74 = var10.name;
            var51.releaseName(var74, 451165245);
            YamlConfiguration var53 = this.cfg;
            String var77 = "spawned." + var10.id;
            var53.set(var77, null);
            this.save(1108236475);
            BukkitScheduler var56 = Bukkit.getScheduler();
            BlissPlugin var79 = this.plugin;
            Runnable var87 = () -> {
                Map var6x = this.byNpcId;
                Integer var12 = var10.npcId;
                var6x.remove(var12);

                try {
                    ((NPC)var1).destroy();
                } catch (Throwable var13) {
                }
            };
            var56.runTaskLater(var79, var87, 1L);
        }
    }

    private void dropLoot(Location var1, int var2) {
        World var6 = var1.getWorld();
        if (var6 != null) {
            for (ItemStack var8 : this.loot) {
                if (var8 != null) {
                    Material var21 = var8.getType();
                    Material var4 = Material.AIR;
                    if (var21 != var4) {
                        ItemStack var25 = var8.clone();
                        var6.dropItemNaturally(var1, var25);
                    }
                }
            }
        }
    }

    private void despawn(MemoryNpcManager.Mem var1, boolean var2, int var3) {
        var1.destroyedByUs = true;

        try {
            if (var1.npc != null) {
                var1.npc.destroy();
            }
        } catch (Throwable var30) {
        }

        Map var13 = this.byNpcId;
        Integer var24 = var1.npcId;
        var13.remove(var24);
        MemoryRoster var16 = this.roster;
        String var26 = var1.name;
        var16.releaseName(var26, 451165245);
        if (var2) {
            YamlConfiguration var19 = this.cfg;
            String var29 = "spawned." + var1.id;
            var19.set(var29, null);
            this.save(1108236475);
        }
    }

    public void shutdown(int var1) {
        Collection<MemoryNpcManager.Mem> var29 = this.live.values();

        for (MemoryNpcManager.Mem var5 : new ArrayList<>(var29)) {
            var5.destroyedByUs = true;

            try {
                if (var5.npc != null) {
                    var5.npc.destroy();
                }
            } catch (Throwable var30) {
            }
        }

        if (this.taskId != -1) {
            BukkitScheduler var21 = Bukkit.getScheduler();
            int var26 = this.taskId;
            var21.cancelTask(var26);
            this.taskId = -1;
        }
    }

    private void persist(MemoryNpcManager.Mem var1, int var2) {
        String var6 = "spawned." + var1.id + ".";
        YamlConfiguration var10 = this.cfg;
        String var29 = var6 + "name";
        String var44 = var1.name;
        var10.set(var29, var44);
        YamlConfiguration var12 = this.cfg;
        String var31 = var6 + "skin";
        String var46 = var1.skin;
        var12.set(var31, var46);
        if (var1.home.isWorldLoaded()) {
            YamlConfiguration var17 = this.cfg;
            String var33 = var6 + "world";
            String var50 = var1.home.getWorld().getName();
            var17.set(var33, var50);
        }

        YamlConfiguration var19 = this.cfg;
        String var35 = var6 + "x";
        Double var55 = var1.home.getX();
        var19.set(var35, var55);
        YamlConfiguration var21 = this.cfg;
        String var37 = var6 + "y";
        Double var60 = var1.home.getY();
        var21.set(var37, var60);
        YamlConfiguration var23 = this.cfg;
        String var39 = var6 + "z";
        Double var65 = var1.home.getZ();
        var23.set(var39, var65);
        YamlConfiguration var25 = this.cfg;
        String var41 = var6 + "yaw";
        Float var69 = var1.home.getYaw();
        var25.set(var41, var69);
        YamlConfiguration var27 = this.cfg;
        String var43 = var6 + "pitch";
        Float var73 = var1.home.getPitch();
        var27.set(var43, var73);
        this.save(1108236475);
    }

    private void loadLoot(int var1) {
        if (!this.cfg.contains("loot")) {
            YamlConfiguration var18 = this.cfg;
            String var57 = "loot";
            String var5 = "DIAMOND:1";
            String var6 = "EMERALD:2";
            String var7 = "GOLDEN_APPLE:1";
            String var8 = "IRON_INGOT:3";
            List var73 = List.of(var5, var6, var7, var8);
            ArrayList var4 = new ArrayList(var73);
            var18.set(var57, var4);
            this.save(1108236475);
        }

        this.loot.clear();

        for (String var10 : this.cfg.getStringList("loot")) {
            if (var10 != null && !var10.isBlank()) {
                String[] var11 = var10.split(":");
                Material var12 = Material.matchMaterial(var11[0].trim());
                if (var12 == null) {
                    Logger var43 = this.plugin.getLogger();
                    String var63 = "memory_npcs.yml loot: unknown material '" + var11[0] + "'";
                    var43.warning(var63);
                } else {
                    int var13 = 1;
                    if (var11.length > 1) {
                        try {
                            var13 = Integer.parseInt(var11[1].trim());
                        } catch (NumberFormatException var76) {
                        }
                    }

                    int var67 = var12.getMaxStackSize();
                    int var68 = Math.min(var67, var13);
                    int var75 = Math.max(1, var68);
                    List var54 = this.loot;
                    ItemStack var69 = new ItemStack(var12, var75);
                    var54.add(var69);
                }
            }
        }
    }

    private void save(int var1) {
        try {
            YamlConfiguration var5 = this.cfg;
            File var10 = this.file;
            AtomicYml.save(var5, var10, 1347473142);
        } catch (IOException var14) {
            Logger var9 = this.plugin.getLogger();
            String var13 = "memory_npcs.yml: " + var14.getMessage();
            var9.warning(var13);
        }
    }

    private static final class Mem {
        final String id;
        String name;
        String skin;
        final Location home;
        NPC npc;
        int npcId = -1;
        boolean geared = false;
        boolean destroyedByUs = false;
        boolean warned = false;
        long nextWander = 0L;

        Mem(String var1, String var2, String var3, Location var4, int var5) {
            this.id = var1;
            this.name = var2;
            this.skin = var3;
            this.home = var4;
        }
    }
}
