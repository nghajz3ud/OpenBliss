package com.bliss.goldgem;

import com.bliss.BlissPlugin;
import com.bliss.core.AtomicYml;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.Particles;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.core.SignatureClock;
import com.bliss.entity.EntityMotifs;
import com.bliss.items.BlissCoreItems;
import com.bliss.nick.MemoryNpcManager;
import com.bliss.nick.MemoryRoster;
import com.bliss.villager.Text;
import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import com.bliss.goldgem.RitualItems;
import java.util.Objects;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

public final class GoldenDreamWorld implements Listener {
    static final String POCKET = "goldendream";
    static final long DREAM_DAY = 80036L;
    static final long DREAM_TOD = 6016L;
    static final int WALK_Y = 64;
    static final int STRIP_WIDTH = 5;
    static final int STRIP_THICK = 5;
    static final int WALK_DELAY_TICKS = 1200;
    static final int LEAVE_TICKS = 6000;
    static final double WALK_SPEED = 0.15;
    private static final double STRIP_MID = 0.5;
    private static final double STRIP_EDGE = 2.2;
    private static final int SPECKS = 4;
    private static final double SPECK_SPREAD = 12.0;
    static final String WORLD = "goldenworld";
    static final String NETHER = "goldenworldnether";
    static final String END = "goldenworldend";
    static final long MEMORY_DAY = 80037L;
    static final long STORM_DAY = 80038L;
    private int stormUntil = -1;
    private static final Particle LIGHT_MOTE;
    private static final int MOTES_PER_TICK = 3;
    private static final double MOTE_SPREAD_H = 22.0;
    private static final double MOTE_SPREAD_V = 9.0;
    private static final int END_X = 100;
    private static final int END_Y = 48;
    private static final int END_Z = 0;
    private static final NamespacedKey LOCKED = NamespacedKey.fromString("bliss:dream_locked");
    private final BlissPlugin plugin;
    private World pocket;
    private World overworld;
    private World nether;
    private World end;
    private final Map<UUID, GoldenDreamWorld.Session> sessions;
    private final Map<UUID, GoldenDreamWorld.Visit> visits;
    private static final String VISIT_KEY = "pocketVisits.";
    private final Set<UUID> graded;
    private BukkitTask gradeTask;
    private final File dreamFile;
    private final YamlConfiguration dream;
    private final Map<UUID, Long> gradedDay;
    public static final String CUE_SOUND = "bliss:alternate";
    private static final int PROXIMITY_PERIOD_TICKS = 20;
    private static final double CUE_RADIUS = 12.0;
    private static final long CUE_COOLDOWN = 20000L;
    private final Map<UUID, Long> cueNext;
    private static final int INTRUDER_ENERGY = 6;
    public static final String IMPORT_FOLDER = "goldendream_import";

    public void storm(int var1, int var2) {
        int var7 = this.stormUntil;
        int var11 = this.plugin.getServer().getCurrentTick();
        int var12 = var11 + var1;
        int var8 = Math.max(var7, var12);
        this.stormUntil = var8;
    }

    private boolean storming(int var1) {
        int var6 = this.plugin.getServer().getCurrentTick();
        int var8 = this.stormUntil;
        boolean var7;
        if (var6 < var8) {
            var7 = true;
        } else {
            var7 = false;
        }

        return var7;
    }

    private static Particle resolveParticle(String var0, Particle var1, int var2) {
        try {
            return Particle.valueOf(var0);
        } catch (IllegalArgumentException var6) {
            return var1;
        }
    }

    public boolean inDream(UUID var1, int var2) {
        YamlConfiguration var5 = this.dream;
        String var7 = var1.toString();
        return var5.contains(var7);
    }

    private boolean intruded(UUID var1, int var2) {
        YamlConfiguration var6 = this.dream;
        String var9 = var1 + ".intruded";
        return var6.getBoolean(var9, false);
    }

    public boolean isIntruder(UUID var1, int var2) {
        boolean var7;
        if (var1 != null && this.intruded(var1, 1723018507)) {
            var7 = true;
        } else {
            var7 = false;
        }

        return var7;
    }

    private void saveDream(int var1) {
        try {
            YamlConfiguration var5 = this.dream;
            File var10 = this.dreamFile;
            AtomicYml.save(var5, var10, 1347473142);
        } catch (IOException var14) {
            Logger var9 = this.plugin.getLogger();
            String var13 = "golden_dream.yml: " + var14.getMessage();
            var9.warning(var13);
        }
    }

    private void capture(Player var1, boolean var2, int var3) {
        String var12 = var1.getUniqueId().toString();
        YamlConfiguration var19 = this.dream;
        if (!var19.contains(var12)) {
            PlayerInventory var13 = var1.getInventory();
            Location var14 = var1.getLocation();
            YamlConfiguration var26 = this.dream;
            String var51 = var12 + ".inv";
            Encoder var6 = Base64.getEncoder();
            byte[] var102 = ItemStack.serializeItemsAsBytes(var13.getContents());
            String var72 = var6.encodeToString(var102);
            var26.set(var51, var72);
            YamlConfiguration var28 = this.dream;
            String var53 = var12 + ".ender";
            Encoder var73 = Base64.getEncoder();
            byte[] var106 = ItemStack.serializeItemsAsBytes(var1.getEnderChest().getContents());
            String var74 = var73.encodeToString(var106);
            var28.set(var53, var74);
            YamlConfiguration var30 = this.dream;
            String var55 = var12 + ".lvl";
            Integer var77 = var1.getLevel();
            var30.set(var55, var77);
            YamlConfiguration var32 = this.dream;
            String var57 = var12 + ".exp";
            Double var82 = (double)var1.getExp();
            var32.set(var57, var82);
            YamlConfiguration var34 = this.dream;
            String var59 = var12 + ".loc";
            String var85 = var14.getWorld().getName();
            double var108 = var14.getX();
            double var110 = var14.getY();
            double var112 = var14.getZ();
            float var114 = var14.getYaw();
            float var115 = var14.getPitch();
            String var86 = var85 + ";" + var108 + ";" + var110 + ";" + var112 + ";" + var114 + ";" + var115;
            var34.set(var59, var86);
            YamlConfiguration var36 = this.dream;
            String var61 = var12 + ".intruded";
            Boolean var88 = false;
            var36.set(var61, var88);
            YamlConfiguration var38 = this.dream;
            String var63 = var12 + ".starter";
            Boolean var90 = var2;
            var38.set(var63, var90);
            PlayerDataStore var41 = this.plugin.players(1969902410);
            UUID var65 = var1.getUniqueId();
            PlayerData var15 = var41.get(var65, 1264010596);
            YamlConfiguration var44 = this.dream;
            String var67 = var12 + ".realGem";
            Integer var94 = var15.gem(1413186337).id();
            var44.set(var67, var94);
            YamlConfiguration var46 = this.dream;
            String var69 = var12 + ".realTier2";
            Boolean var97 = var15.tier2(629984660);
            var46.set(var69, var97);
            YamlConfiguration var48 = this.dream;
            String var71 = var12 + ".realEnergy";
            Integer var100 = var15.rawEnergy(1655845820);
            var48.set(var71, var100);
            this.saveDream(605217750);
        }
    }

    GoldenDreamWorld(BlissPlugin var1, int var2) {
        HashMap var21 = new HashMap();
        this.sessions = var21;
        HashMap var22 = new HashMap();
        this.visits = var22;
        HashSet var23 = new HashSet();
        this.graded = var23;
        HashMap var24 = new HashMap();
        this.gradedDay = var24;
        HashMap var25 = new HashMap();
        this.cueNext = var25;
        this.plugin = var1;
        File var34 = var1.getDataFolder();
        File var27 = new File(var34, "golden_dream.yml");
        this.dreamFile = var27;
        YamlConfiguration var30 = YamlConfiguration.loadConfiguration(this.dreamFile);
        this.dream = var30;
        this.reloadWorlds(153530246);
        BukkitScheduler var19 = var1.getServer().getScheduler();
        Runnable var33 = () -> {
            for (Player var7 : var1.getServer().getOnlinePlayers()) {
                if (this.pocket != null) {
                    World var20 = var7.getWorld();
                    World var51 = this.pocket;
                    if (var20 == var51) {
                        UUID var53 = var7.getUniqueId();
                        if (this.inDream(var53, 2004945976)) {
                            Map var24x = this.sessions;
                            UUID var55 = var7.getUniqueId();
                            if (!var24x.containsKey(var55)) {
                                UUID var57 = var7.getUniqueId();
                                if (this.intruded(var57, 1723018507)) {
                                    Location var8 = this.arrival(1026150274);
                                    if (var8 != null) {
                                        var7.teleport(var8);
                                    }
                                } else {
                                    long var65 = var7.getPlayerTimeOffset();
                                    boolean var67 = var7.isPlayerTimeRelative();
                                    this.enter(var7, var65, var67, 1738178342);
                                }
                            }
                        }
                    }
                }
            }

            for (Player var69 : var1.getServer().getOnlinePlayers()) {
                if (this.pocket != null) {
                    World var46 = var69.getWorld();
                    World var61 = this.pocket;
                    if (var46 == var61) {
                        UUID var63 = var69.getUniqueId();
                        GoldenDreamWorld.Visit var70 = this.loadVisit(var63, 1695920324);
                        if (var70 != null) {
                            this.arm(var70, 692179808);
                        }
                    }
                }
            }
        };
        var19.runTaskLater(var1, var33, 20L);
    }

    private void reloadWorlds(int n) {
        try {
            File file;
            File file2 = file = Bukkit.getWorldContainer();
            String string = POCKET;
            File file3 = new File(file2, string);
            if (file3.isDirectory()) {
                this.pocket(698565255);
            }
            File file4 = file;
            String string2 = WORLD;
            File file5 = new File(file4, string2);
            if (file5.isDirectory()) {
                this.worldSet(1705953131);
            }
        }
        catch (Exception exception) {
            Logger logger = this.plugin.getLogger();
            String string = "Golden Dream: could not reload its worlds on enable (" + exception.getMessage() + ")";
            logger.warning(string);
        }
    }

    World pocket(int var1) {
        if (this.pocket != null) {
            return this.pocket;
        } else {
            World var36 = Bukkit.getWorld("goldendream");
            this.pocket = var36;
            if (this.pocket == null) {
                String var4 = "goldendream";
                WorldCreator var37 = new WorldCreator(var4);
                Environment var50 = Environment.NORMAL;
                WorldCreator var38 = var37.environment(var50);
                GoldenDreamWorld.Walkway var51 = new GoldenDreamWorld.Walkway();
                World var41 = var38.generator(var51).generateStructures(false).createWorld();
                this.pocket = var41;
            }

            if (this.pocket != null) {
                World var15 = this.pocket;
                GameRule var42 = GameRule.DO_DAYLIGHT_CYCLE;
                Boolean var54 = false;
                var15.setGameRule(var42, var54);
                World var18 = this.pocket;
                GameRule var43 = GameRule.DO_WEATHER_CYCLE;
                Boolean var56 = false;
                var18.setGameRule(var43, var56);
                World var21 = this.pocket;
                GameRule var44 = GameRule.DO_MOB_SPAWNING;
                Boolean var58 = false;
                var21.setGameRule(var44, var58);
                World var24 = this.pocket;
                GameRule var45 = GameRule.DO_IMMEDIATE_RESPAWN;
                Boolean var60 = true;
                var24.setGameRule(var45, var60);
                this.pocket.setFullTime(1920870016L);
                this.pocket.setSpawnLocation(0, 65, 0);
            } else {
                this.plugin.getLogger().warning("Golden Dream: could not create world 'goldendream'");
            }

            return this.pocket;
        }
    }

    public boolean memorySetBuilt(int var1) {
        boolean var8;
        if (this.overworld != null && this.nether != null && this.end != null) {
            var8 = true;
        } else {
            var8 = false;
        }

        return var8;
    }

    World worldSet(int var1) {
        if (this.overworld == null) {
            String var4 = "goldenworld";
            Environment var5 = Environment.NORMAL;
            World var25 = this.load(var4, var5, 636167358);
            this.overworld = var25;
        }

        if (this.nether == null) {
            String var35 = "goldenworldnether";
            Environment var39 = Environment.NETHER;
            World var27 = this.load(var35, var39, 636167358);
            this.nether = var27;
        }

        if (this.end == null) {
            String var36 = "goldenworldend";
            Environment var40 = Environment.THE_END;
            World var29 = this.load(var36, var40, 636167358);
            this.end = var29;
        }

        if (this.gradeTask == null && this.overworld != null) {
            BukkitScheduler var33 = this.plugin.getServer().getScheduler();
            BlissPlugin var38 = this.plugin;
            Runnable var42 = this::grade;
            BukkitTask var34 = var33.runTaskTimer(var38, var42, 1L, 1L);
            this.gradeTask = var34;
        }

        return this.overworld;
    }

    public void forgetGrade(UUID var1, int var2) {
        Set var5 = this.graded;
        var5.remove(var1);
        Map var8 = this.gradedDay;
        var8.remove(var1);
    }

    private void grade() {
        if (this.plugin.getServer().getCurrentTick() % 20 == 0) {
            this.proximityCue(177809989);
        }

        for (Player var22 : this.plugin.getServer().getOnlinePlayers()) {
            World var101 = var22.getWorld();
            if (this.ours(var101, 1901240484)) {
                long var23 = var22.getWorld().getFullTime() / 24000L;
                long var65;
                if (this.storming(1244107095)) {
                    var65 = 80038L;
                } else {
                    var65 = 80037L;
                }

                long var69 = var23 * 2L;
                long var135;
                if (var65 == 80038L) {
                    var135 = 1L;
                } else {
                    var135 = 0L;
                }

                label55: {
                    long var27 = var69 + var135;
                    Set var74 = this.graded;
                    UUID var107 = var22.getUniqueId();
                    boolean var29 = var74.add(var107);
                    Map var77 = this.gradedDay;
                    UUID var109 = var22.getUniqueId();
                    Long var30 = (Long)var77.get(var109);
                    if (!var29 && var30 != null) {
                        long var83 = var30;
                        if (var83 == var27) {
                            break label55;
                        }
                    }

                    long var114 = (var65 - var23) * 24000L;
                    var22.setPlayerTime(var114, true);
                    Map var88 = this.gradedDay;
                    UUID var117 = var22.getUniqueId();
                    Long var131 = var27;
                    var88.put(var117, var131);
                }

                int var31 = this.plugin.getServer().getCurrentTick() % 5;
                byte var99;
                if (var31 != 1 && var31 != 3) {
                    var99 = 4;
                } else {
                    var99 = 3;
                }

                World var98 = var22.getWorld();
                Particle var121 = LIGHT_MOTE;
                Location var134 = var22.getLocation().add(0.0, 2.0, 0.0);
                Particles.spawn(var98, var121, var134, var99, 22.0, 9.0, 22.0, 0.0, null, true, 1748031581);
            } else {
                Set var51 = this.graded;
                UUID var103 = var22.getUniqueId();
                if (var51.remove(var103)) {
                    Map var54 = this.gradedDay;
                    UUID var105 = var22.getUniqueId();
                    var54.remove(var105);
                    var22.resetPlayerTime();
                }
            }
        }
    }

    private void proximityCue(int var1) {
        MemoryRoster var9 = this.plugin.memoryRoster(133420166);
        if (var9 != null) {
            long var10 = System.currentTimeMillis();
            ArrayList<Player> var12 = null;
            List<Location> var13 = null;
            Iterator var14 = this.plugin.getServer().getOnlinePlayers().iterator();

            while (true) {
                Player var15;
                World var16;
                while (true) {
                    if (!var14.hasNext()) {
                        return;
                    }

                    var15 = (Player)var14.next();
                    var16 = var15.getWorld();
                    if (this.ours(var16, 1901240484)) {
                        UUID var133 = var15.getUniqueId();
                        if (this.intruded(var133, 1723018507) && !var15.isDead() && GlobalState.forWorld(var16, 728926677).memoryProximitySound(604772563)) {
                            Map var55 = this.cueNext;
                            UUID var135 = var15.getUniqueId();
                            Long var17 = (Long)var55.get(var135);
                            if (var17 == null) {
                                break;
                            }

                            long var60 = var17;
                            if (var60 <= var10) {
                                break;
                            }
                        }
                    }
                }

                if (var12 == null) {
                    ArrayList<Player> var64 = new ArrayList<>();
                    var12 = var64;

                    for (UUID var160 : var9.activeIds(24745596)) {
                        Server var123 = this.plugin.getServer();
                        Player var21 = var123.getPlayer(var160);
                        if (var21 != null && var21.isOnline() && !var21.isDead()) {
                            var12.add(var21);
                        }
                    }

                    MemoryNpcManager var158 = this.plugin.memoryNpcs(1468333301);
                    List<Location> var117;
                    if (var158 == null) {
                        var117 = List.of();
                    } else {
                        var117 = var158.liveLocations(1461492665);
                    }

                    var13 = var117;
                }

                Location var159 = var15.getLocation();
                boolean var22 = false;

                for (Player var24 : var12) {
                    if (var24 != var15) {
                        Location var25 = var24.getLocation();
                        World var91 = var25.getWorld();
                        if (var91 == var16 && var25.distanceSquared(var159) <= 144.0) {
                            var22 = true;
                            break;
                        }
                    }
                }

                if (!var22) {
                    for (Location var162 : var13) {
                        World var106 = var162.getWorld();
                        if (var106 == var16 && var162.distanceSquared(var159) <= 144.0) {
                            var22 = true;
                            break;
                        }
                    }
                }

                if (var22) {
                    String var150 = "bliss:alternate";
                    SoundCategory var6 = SoundCategory.MASTER;
                    var16.playSound(var159, var150, var6, 1.0F, 1.0F);
                    Map var115 = this.cueNext;
                    UUID var143 = var15.getUniqueId();
                    Long var155 = var10 + 20000L;
                    var115.put(var143, var155);
                }
            }
        }
    }

    private World load(String var1, Environment var2, int var3) {
        World var6 = Bukkit.getWorld(var1);
        if (var6 == null) {
            WorldCreator var9 = new WorldCreator(var1);
            var6 = var9.environment(var2).createWorld();
            if (var6 == null) {
                Logger var19 = this.plugin.getLogger();
                String var24 = "Golden Dream: could not create world '" + var1 + "'";
                var19.warning(var24);
            } else {
                Logger var15 = this.plugin.getLogger();
                String var22 = "Golden Dream: world '" + var1 + "' ready";
                var15.info(var22);
            }
        }

        return var6;
    }

    private boolean ours(World var1, int var2) {
        if (var1 != null) {
            World var9 = this.overworld;
            if (var1 == var9) {
                return true;
            }

            World var11 = this.nether;
            if (var1 == var11) {
                return true;
            }

            World var13 = this.end;
            if (var1 == var13) {
                return true;
            }
        }

        return false;
    }

    public boolean isMemoryWorld(World var1, int var2) {
        return this.ours(var1, 1901240484);
    }

    public static boolean isPocketWorld(World var0) {
        boolean var6;
        if (var0 != null && var0.getName().equals("goldendream")) {
            var6 = true;
        } else {
            var6 = false;
        }

        return var6;
    }

    public static boolean isDreamWorld(World var0) {
        if (var0 == null) {
            return false;
        } else {
            String var3 = var0.getName();
            boolean var15;
            if (!var3.equals("goldendream") && !var3.equals("goldenworld") && !var3.equals("goldenworldnether") && !var3.equals("goldenworldend")) {
                var15 = false;
            } else {
                var15 = true;
            }

            return var15;
        }
    }

    public Location memoryArrival(int var1) {
        return this.arrival(1026150274);
    }

    private Location arrival(int var1) {
        World var11 = this.worldSet(1705953131);
        if (var11 == null) {
            return null;
        } else {
            Location var12 = var11.getSpawnLocation();
            int var23 = var12.getBlockX();
            int var25 = var12.getBlockZ();
            int var13 = var11.getHighestBlockYAt(var23, var25) + 1;
            double var30 = (double)var12.getBlockX() + 0.5;
            double var33 = (double)var13;
            double var38 = (double)var12.getBlockZ() + 0.5;
            return new Location(var11, var30, var33, var38);
        }
    }

    void enter(Player var1, long var2, boolean var4, int var5) {
        this.enter(var1, var2, var4, false, 1808728489);
    }

    void enter(Player var1, long var2, boolean var4, boolean var5, int var6) {
        final World var19 = this.pocket(698565255);
        if (var19 == null) {
            var1.setPlayerTime(var2, var4);
        } else {
            UUID var71 = var1.getUniqueId();
            if (this.intruded(var71, 1723018507)) {
                var1.setPlayerTime(var2, var4);
                Location var20 = this.arrival(1026150274);
                if (var20 != null) {
                    Vector var74 = new Vector();
                    var1.setVelocity(var74);
                    var1.teleport(var20);
                    var1.setFallDistance(0.0F);
                }
            } else {
                this.capture(var1, var5, 1447915196);
                PlayerInventory var117 = var1.getInventory();
                var117.clear();
                ItemStack[] var79 = new ItemStack[4];
                var117.setArmorContents(var79);
                var117.setItemInOffHand(null);
                PlayerDataStore var43 = this.plugin.players(1969902410);
                UUID var82 = var1.getUniqueId();
                PlayerData var21 = var43.get(var82, 1264010596);
                GemType var83 = GemType.NONE;
                var21.setGem(var83, 2137390487);
                BlissCoreItems var48 = this.plugin.coreItems(401009135);
                var48.refreshGemItem(var1, var21, 1088402172);
                this.worldSet(1705953131);
                UUID var86 = var1.getUniqueId();
                this.dropVisit(var86, 509312564);
                Map var53 = this.sessions;
                UUID var88 = var1.getUniqueId();
                GoldenDreamWorld.Session var118 = (GoldenDreamWorld.Session)var53.remove(var88);
                if (var118 != null) {
                    if (var118.task != null) {
                        var118.task.cancel();
                    }

                    this.unlock(var1, var118, 738583955);
                }

                UUID var105 = var1.getUniqueId();
                final GoldenDreamWorld.Session var62 = new GoldenDreamWorld.Session(var105, var2, var4, 2129620640);
                Map var64 = this.sessions;
                UUID var91 = var62.pid;
                var64.put(var91, var62);
                Location var92 = new Location(var19, 0.5, 65.0, 0.5, -90.0F, 0.0F);
                var1.teleport(var92);
                Vector var93 = new Vector();
                var1.setVelocity(var93);
                BukkitRunnable var94 = new BukkitRunnable() {
                    public void run() {
                        Server var27 = GoldenDreamWorld.this.plugin.getServer();
                        UUID var105 = var62.pid;
                        Player var20 = var27.getPlayer(var105);
                        if (var20 != null && var20.isOnline() && !var20.isDead()) {
                            World var35 = var20.getWorld();
                            World var107 = var19;
                            if (var35 == var107) {
                                int var21 = var62.t;
                                if (var21 <= 30) {
                                    long var173 = (long)(30 - var21);
                                    long var159 = 6016L + var173;
                                    SignatureClock.send(var20, 80036L, var159, 399305443);
                                } else if (!var62.clockBack) {
                                    var20.resetPlayerTime();
                                    var62.clockBack = true;
                                }

                                World var47 = var19;
                                Particle var110 = Particle.ASH;
                                Location var136 = var20.getLocation().add(0.0, 1.5, 0.0);
                                Particles.spawn(var47, var110, var136, 4, 12.0, 7.199999999999999, 12.0, 0.0, null, true, 1748031581);
                                Location var22 = var20.getLocation();
                                double var165 = var22.getZ();
                                double var139 = Math.min(2.7, var165);
                                double var23 = Math.max(-1.7000000000000002, var139);
                                double var55 = var22.getZ();
                                if (Math.abs(var55 - var23) > 1.0E-6 || var22.getY() < 64.98) {
                                    var22.setZ(var23);
                                    var22.setY(65.0);
                                    var20.teleport(var22);
                                    Vector var116 = new Vector();
                                    var20.setVelocity(var116);
                                    var20.setFallDistance(0.0F);
                                }

                                if (var62.auto && var21 == 1200) {
                                    GoldenDreamWorld var77 = GoldenDreamWorld.this;
                                    GoldenDreamWorld.Session var148 = var62;
                                    var77.lock(var20, var148, 848108787);
                                }

                                if (var62.locked) {
                                    double var178 = var20.getVelocity().getY();
                                    double var169 = Math.min(0.0, var178);
                                    Vector var120 = new Vector(0.15, var169, 0.0);
                                    var20.setVelocity(var120);
                                }

                                if (var21 < 6000 && !var62.skip) {
                                    GoldenDreamWorld.Session var87 = var62;
                                    int var123 = var87.t + 1;
                                    var87.t = var123;
                                    return;
                                }

                                GoldenDreamWorld var89 = GoldenDreamWorld.this;
                                GoldenDreamWorld.Session var151 = var62;
                                var89.leave(var20, var151, 56228573);
                                this.cancel();
                                return;
                            }
                        }

                        if (var20 != null) {
                            GoldenDreamWorld var94 = GoldenDreamWorld.this;
                            GoldenDreamWorld.Session var153 = var62;
                            var94.unlock(var20, var153, 738583955);
                            if (!var62.clockBack) {
                                long var130 = var62.prevOffset;
                                boolean var163 = var62.prevRel;
                                var20.setPlayerTime(var130, var163);
                            }
                        }

                        Map var101 = GoldenDreamWorld.this.sessions;
                        UUID var134 = var62.pid;
                        var101.remove(var134);
                        this.cancel();
                    }
                };
                BlissPlugin var102 = this.plugin;
                BukkitTask var95 = var94.runTaskTimer(var102, 1L, 1L);
                var62.task = var95;
            }
        }
    }

    public boolean skip(UUID var1, int var2) {
        Map var6 = this.sessions;
        GoldenDreamWorld.Session var5 = (GoldenDreamWorld.Session)var6.get(var1);
        if (var5 == null) {
            return false;
        } else {
            var5.skip = true;
            return true;
        }
    }

    public boolean startWalk(UUID var1, int var2) {
        Map var8 = this.sessions;
        GoldenDreamWorld.Session var6 = (GoldenDreamWorld.Session)var8.get(var1);
        Player var20;
        if (var6 == null) {
            var20 = null;
        } else {
            Server var14 = this.plugin.getServer();
            var20 = var14.getPlayer(var1);
        }

        if (var20 == null) {
            return false;
        } else {
            var6.auto = false;
            this.lock(var20, var6, 848108787);
            return true;
        }
    }

    public boolean stopWalk(UUID var1, int var2) {
        Map var8 = this.sessions;
        GoldenDreamWorld.Session var6 = (GoldenDreamWorld.Session)var8.get(var1);
        Player var20;
        if (var6 == null) {
            var20 = null;
        } else {
            Server var19 = this.plugin.getServer();
            var20 = var19.getPlayer(var1);
        }

        if (var20 == null) {
            return false;
        } else {
            var6.auto = false;
            this.unlock(var20, var6, 738583955);
            return true;
        }
    }

    public List<UUID> walking(int var1) {
        Set var5 = this.sessions.keySet();
        return new ArrayList<>(var5);
    }

    public boolean isVisiting(UUID var1, int var2) {
        Map var5 = this.visits;
        return var5.containsKey(var1);
    }

    public String pocketTp(Player var1, int var2) {
        UUID var14 = var1.getUniqueId();
        Map var19 = this.sessions;
        if (!var19.containsKey(var14) && !this.inDream(var14, 2004945976)) {
            Map var24 = this.visits;
            if (var24.containsKey(var14)) {
                return var1.getName() + " is already in the pocket.";
            } else {
                World var15 = this.pocket(698565255);
                if (var15 == null) {
                    return "The pocket world could not be created.";
                } else {
                    Location var70 = var1.getLocation().clone();
                    GoldenDreamWorld.Visit var33 = new GoldenDreamWorld.Visit(var14, var70, 1005208761);
                    Map var35 = this.visits;
                    var35.put(var14, var33);
                    YamlConfiguration var38 = this.dream;
                    String var58 = "pocketVisits." + var14 + ".back";
                    String var67 = packLoc(var33.back, 1932921038);
                    var38.set(var58, var67);
                    this.saveDream(605217750);
                    Vector var59 = new Vector();
                    var1.setVelocity(var59);
                    Location var60 = new Location(var15, 0.5, 65.0, 0.5, -90.0F, 0.0F);
                    if (!var1.teleport(var60)) {
                        this.dropVisit(var14, 509312564);
                        return var1.getName() + " could not be moved right now.";
                    } else {
                        var1.setFallDistance(0.0F);
                        this.arm(var33, 692179808);
                        return null;
                    }
                }
            }
        } else {
            return var1.getName() + " is in the dream - the ritual owns them.";
        }
    }

    public boolean pocketKick(Player var1, int var2) {
        UUID var5 = var1.getUniqueId();
        Map var11 = this.visits;
        GoldenDreamWorld.Visit var6 = (GoldenDreamWorld.Visit)var11.get(var5);
        if (var6 == null) {
            var6 = this.loadVisit(var5, 1695920324);
        }

        if (var6 == null) {
            return false;
        } else {
            Location var7 = var6.back;
            if (var7 == null || !var7.isWorldLoaded()) {
                World var45;
                if (this.plugin.getServer().getWorlds().isEmpty()) {
                    var45 = null;
                } else {
                    var45 = (World)this.plugin.getServer().getWorlds().get(0);
                }

                Location var39;
                if (var45 == null) {
                    var39 = null;
                } else {
                    var39 = var45.getSpawnLocation();
                }

                var7 = var39;
            }

            if (var7 == null) {
                return false;
            } else {
                Vector var48 = new Vector();
                var1.setVelocity(var48);
                if (!var1.teleport(var7)) {
                    return false;
                } else {
                    this.dropVisit(var5, 509312564);
                    var1.setFallDistance(0.0F);
                    return true;
                }
            }
        }
    }

    private void arm(final GoldenDreamWorld.Visit var1, int var2) {
        final World var8 = this.pocket(698565255);
        if (var8 != null) {
            if (var1.task != null) {
                var1.task.cancel();
            }

            BukkitRunnable var4 = new BukkitRunnable() {
                public void run() {
                    Server var12 = GoldenDreamWorld.this.plugin.getServer();
                    UUID var61 = var1.pid;
                    Player var6 = var12.getPlayer(var61);
                    if (var6 != null && var6.isOnline()) {
                        World var21 = var6.getWorld();
                        World var64 = var8;
                        if (var21 != var64) {
                            GoldenDreamWorld var23 = GoldenDreamWorld.this;
                            UUID var67 = var1.pid;
                            var23.dropVisit(var67, 509312564);
                            this.cancel();
                        } else if (!var6.isDead()) {
                            if (GoldenDreamWorld.this.plugin.entityMotifs(949215902) != null) {
                                EntityMotifs var34 = GoldenDreamWorld.this.plugin.entityMotifs(949215902);
                                if (var34.motifOf(var6, 213258127) != null) {
                                    return;
                                }
                            }

                            Location var7 = var6.getLocation();
                            double var84 = var7.getZ();
                            double var76 = Math.min(2.7, var84);
                            double var8x = Math.max(-1.7000000000000002, var76);
                            double var43 = var7.getZ();
                            if (Math.abs(var43 - var8x) > 1.0E-6 || var7.getY() < 64.98) {
                                var7.setZ(var8x);
                                var7.setY(65.0);
                                var6.teleport(var7);
                                Vector var74 = new Vector();
                                var6.setVelocity(var74);
                                var6.setFallDistance(0.0F);
                            }
                        }
                    } else {
                        var1.task = null;
                        this.cancel();
                    }
                }
            };
            BlissPlugin var18 = this.plugin;
            BukkitTask var16 = var4.runTaskTimer(var18, 1L, 1L);
            var1.task = var16;
        }
    }

    private GoldenDreamWorld.Visit loadVisit(UUID var1, int var2) {
        Map var11 = this.visits;
        GoldenDreamWorld.Visit var10 = (GoldenDreamWorld.Visit)var11.get(var1);
        if (var10 != null) {
            return var10;
        } else {
            YamlConfiguration var17 = this.dream;
            String var27 = "pocketVisits." + var1;
            if (!var17.contains(var27)) {
                return null;
            } else {
                YamlConfiguration var31 = this.dream;
                String var34 = "pocketVisits." + var1 + ".back";
                String var32 = var31.getString(var34);
                Location var30 = this.unpackLoc(var32, 1164707431);
                GoldenDreamWorld.Visit var20 = new GoldenDreamWorld.Visit(var1, var30, 1005208761);
                Map var22 = this.visits;
                var22.put(var1, var20);
                return var20;
            }
        }
    }

    private void dropVisit(UUID var1, int var2) {
        Map var7 = this.visits;
        GoldenDreamWorld.Visit var6 = (GoldenDreamWorld.Visit)var7.remove(var1);
        if (var6 != null && var6.task != null) {
            var6.task.cancel();
        }

        YamlConfiguration var16 = this.dream;
        String var23 = "pocketVisits." + var1;
        if (var16.contains(var23)) {
            YamlConfiguration var19 = this.dream;
            String var26 = "pocketVisits." + var1;
            var19.set(var26, null);
            this.saveDream(605217750);
        }
    }

    private static String packLoc(Location var0, int var1) {
        String var9 = var0.getWorld().getName();
        double var11 = var0.getX();
        double var13 = var0.getY();
        double var15 = var0.getZ();
        float var17 = var0.getYaw();
        float var18 = var0.getPitch();
        return var9 + ";" + var11 + ";" + var13 + ";" + var15 + ";" + var17 + ";" + var18;
    }

    private Location unpackLoc(String var1, int var2) {
        if (var1 == null) {
            return null;
        } else {
            String[] var15 = var1.split(";");
            if (var15.length < 6) {
                return null;
            } else {
                Server var25 = this.plugin.getServer();
                String var35 = var15[0];
                World var16 = var25.getWorld(var35);
                if (var16 == null) {
                    return null;
                } else {
                    try {
                        double var38 = Double.parseDouble(var15[1]);
                        double var41 = Double.parseDouble(var15[2]);
                        double var44 = Double.parseDouble(var15[3]);
                        float var47 = Float.parseFloat(var15[4]);
                        float var50 = Float.parseFloat(var15[5]);
                        return new Location(var16, var38, var41, var44, var47, var50);
                    } catch (NumberFormatException var51) {
                        return null;
                    }
                }
            }
        }
    }

    private void leave(Player var1, GoldenDreamWorld.Session var2, int var3) {
        this.unlock(var1, var2, 738583955);
        long var25 = var2.prevOffset;
        boolean var36 = var2.prevRel;
        var1.setPlayerTime(var25, var36);
        Map var11 = this.sessions;
        UUID var28 = var2.pid;
        var11.remove(var28);
        Location var8 = this.arrival(1026150274);
        if (var8 != null) {
            Vector var29 = new Vector();
            var1.setVelocity(var29);
            var1.teleport(var8);
            var1.setFallDistance(0.0F);
            this.intruder(var1, 1550934488);
        } else {
            Logger var23 = this.plugin.getLogger();
            String var35 = "Golden Dream: no world to send " + var1.getName() + " to";
            var23.warning(var35);
        }
    }

    private void intruder(Player var1, int var2) {
        UUID var62 = var1.getUniqueId();
        if (!this.intruded(var62, 1723018507)) {
            YamlConfiguration var12 = this.dream;
            String var66 = var1.getUniqueId() + ".intruded";
            Boolean var105 = true;
            var12.set(var66, var105);
            this.saveDream(605217750);
            PlayerInventory var6 = var1.getInventory();
            var6.clear();
            ItemStack[] var68 = new ItemStack[4];
            var6.setArmorContents(var68);
            var6.setItemInOffHand(null);
            var1.getEnderChest().clear();
            var1.setLevel(0);
            var1.setExp(0.0F);
            var1.setTotalExperience(0);

            for (PotionEffect var115 : var1.getActivePotionEffects()) {
                PotionEffectType var104 = var115.getType();
                var1.removePotionEffect(var104);
            }

            var1.setFireTicks(0);
            var1.setFallDistance(0.0F);
            var1.setAbsorptionAmount(0.0);
            var1.setArrowsInBody(0);
            var1.setFreezeTicks(0);
            int var80 = var1.getMaximumAir();
            var1.setRemainingAir(var80);
            Attribute var81 = Attribute.MAX_HEALTH;
            AttributeInstance var114 = var1.getAttribute(var81);
            double var84;
            if (var114 != null) {
                var84 = var114.getValue();
            } else {
                var84 = 20.0;
            }

            var1.setHealth(var84);
            var1.setFoodLevel(20);
            var1.setSaturation(5.0F);
            var1.setExhaustion(0.0F);
            PlayerDataStore var43 = this.plugin.players(1969902410);
            UUID var90 = var1.getUniqueId();
            PlayerData var8 = var43.get(var90, 1264010596);
            YamlConfiguration var46 = this.dream;
            String var94 = var1.getUniqueId() + ".realGem";
            int var108 = var8.gem(1413186337).id();
            GemType var9 = GemType.byId(var46.getInt(var94, var108));
            Random var109 = new Random();
            GemType var96 = GemType.randomDifferent(var9, var109);
            var8.setGem(var96, 2137390487);
            var8.setTier2(false, 1757992594);
            var8.setEnergy(6, 319922676);
            BlissCoreItems var54 = this.plugin.coreItems(401009135);
            var54.refreshGemItem(var1, var8, 1088402172);
            Logger var57 = this.plugin.getLogger();
            String var101 = var1.getName();
            String var113 = String.valueOf(var8.gem(1413186337));
            String var102 = "Golden Dream: " + var101 + " enters the memory as an intruder with " + var113;
            var57.info(var102);
        }
    }

    private static boolean airBelow(Location var0, int var1, int var2) {
        if (!var0.isWorldLoaded()) {
            return false;
        } else {
            for (int var8 = 1; var8 <= var1; var8++) {
                Location var13 = var0.clone();
                double var22 = (double)var8;
                if (!var13.subtract(0.0, var22, 0.0).getBlock().isPassable()) {
                    return false;
                }
            }

            return true;
        }
    }

    // $VF: Handled exception range with multiple entry points by splitting it
    // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
    public boolean wake(Player var1, int var2) {
        String var12 = var1.getUniqueId().toString();
        YamlConfiguration var20 = this.dream;
        if (!var20.contains(var12)) {
            return false;
        } else if (var1.isDead()) {
            return false;
        } else {
            Map var27 = this.sessions;
            UUID var95 = var1.getUniqueId();
            GoldenDreamWorld.Session var13 = (GoldenDreamWorld.Session)var27.remove(var95);
            if (var13 != null) {
                if (var13.task != null) {
                    var13.task.cancel();
                }

                this.unlock(var1, var13, 738583955);
            }

            UUID var98 = var1.getUniqueId();
            this.forgetGrade(var98, 1777841881);
            var1.resetPlayerTime();

            label83: {
                Exception var10000;
                label93: {
                    try {
                        YamlConfiguration var39 = this.dream;
                        String var100 = var12 + ".inv";
                        String var14 = var39.getString(var100);
                        if (var14 != null) {
                            PlayerInventory var43 = var1.getInventory();
                            Decoder var101 = Base64.getDecoder();
                            ItemStack[] var103 = ItemStack.deserializeItemsFromBytes(var101.decode(var14));
                            var43.setContents(var103);
                        }
                    } catch (Exception var192) {
                        var10000 = var192;
                        boolean var10001 = false;
                        break label93;
                    }

                    try {
                        YamlConfiguration var45 = this.dream;
                        String var105 = var12 + ".ender";
                        String var15 = var45.getString(var105);
                        if (var15 != null) {
                            Inventory var49 = var1.getEnderChest();
                            Decoder var106 = Base64.getDecoder();
                            ItemStack[] var108 = ItemStack.deserializeItemsFromBytes(var106.decode(var15));
                            var49.setContents(var108);
                        }
                    } catch (Exception var191) {
                        var10000 = var191;
                        boolean var193 = false;
                        break label93;
                    }

                    try {
                        YamlConfiguration var110 = this.dream;
                        String var153 = var12 + ".lvl";
                        int var111 = var110.getInt(var153, 0);
                        var1.setLevel(var111);
                        YamlConfiguration var113 = this.dream;
                        String var155 = var12 + ".exp";
                        float var116 = (float)var113.getDouble(var155, 0.0);
                        var1.setExp(var116);
                        break label83;
                    } catch (Exception var190) {
                        var10000 = var190;
                        boolean var194 = false;
                    }
                }

                Exception var187 = var10000;
                Logger var93 = this.plugin.getLogger();
                String var148 = var1.getName();
                String var173 = var187.getMessage();
                String var149 = "Golden Dream: could not restore " + var148 + "'s items (" + var173 + ")";
                var93.warning(var149);
            }

            YamlConfiguration var53 = this.dream;
            String var118 = var12 + ".realGem";
            boolean var186 = var53.contains(var118);
            if (var186) {
                PlayerDataStore var58 = this.plugin.players(1969902410);
                UUID var120 = var1.getUniqueId();
                PlayerData var188 = var58.get(var120, 1264010596);
                YamlConfiguration var122 = this.dream;
                String var157 = var12 + ".realGem";
                int var178 = var188.gem(1413186337).id();
                GemType var124 = GemType.byId(var122.getInt(var157, var178));
                var188.setGem(var124, 2137390487);
                YamlConfiguration var126 = this.dream;
                String var159 = var12 + ".realTier2";
                boolean var180 = var188.tier2(629984660);
                boolean var127 = var126.getBoolean(var159, var180);
                var188.setTier2(var127, 1757992594);
                YamlConfiguration var129 = this.dream;
                String var161 = var12 + ".realEnergy";
                int var182 = var188.energy(1160853318);
                int var130 = var129.getInt(var161, var182);
                var188.setEnergy(var130, 319922676);
            }

            YamlConfiguration var132 = this.dream;
            String var163 = var12 + ".loc";
            String var133 = var132.getString(var163);
            Location var189 = this.parseLoc(var133, 1321721311);
            YamlConfiguration var66 = this.dream;
            String var135 = var12 + ".starter";
            boolean var16 = var66.getBoolean(var135, false);
            YamlConfiguration var69 = this.dream;
            var69.set(var12, null);
            this.saveDream(605217750);
            if (var189 != null) {
                Vector var137 = new Vector();
                var1.setVelocity(var137);
                var1.teleport(var189);
                var1.setFallDistance(0.0F);
                if (airBelow(var189, 4, 474234204)) {
                    PotionEffectType var183 = PotionEffectType.SLOW_FALLING;
                    PotionEffect var141 = new PotionEffect(var183, 400, 0, true, false, false);
                    var1.addPotionEffect(var141);
                }
            }

            if (var186) {
                BlissCoreItems var83 = this.plugin.coreItems(401009135);
                PlayerDataStore var168 = this.plugin.players(1969902410);
                UUID var185 = var1.getUniqueId();
                PlayerData var169 = var168.get(var185, 1264010596);
                var83.refreshGemItem(var1, var169, 1088402172);
            }

            if (var16) {
                ItemStack var143 = RitualItems.fragmentCore(1868423911);
                BlissCoreItems.giveOrDrop(var1, var143, 41085121);
            }

            Logger var88 = this.plugin.getLogger();
            String var145 = var1.getName();
            String var171;
            if (var16) {
                var171 = " with the Fragment Core";
            } else {
                var171 = "";
            }

            String var146 = "Golden Dream: " + var145 + " wakes from the dream" + var171;
            var88.info(var146);
            return true;
        }
    }

    private Location parseLoc(String var1, int var2) {
        if (var1 == null) {
            return null;
        } else {
            String[] var15 = var1.split(";");
            if (var15.length != 6) {
                return null;
            } else {
                World var16 = Bukkit.getWorld(var15[0]);
                if (var16 == null) {
                    return null;
                } else {
                    try {
                        double var35 = Double.parseDouble(var15[1]);
                        double var38 = Double.parseDouble(var15[2]);
                        double var41 = Double.parseDouble(var15[3]);
                        float var44 = Float.parseFloat(var15[4]);
                        float var47 = Float.parseFloat(var15[5]);
                        return new Location(var16, var35, var38, var41, var44, var47);
                    } catch (NumberFormatException var48) {
                        return null;
                    }
                }
            }
        }
    }

    private void lock(Player var1, GoldenDreamWorld.Session var2, int var3) {
        if (!var2.locked) {
            float var23 = var1.getWalkSpeed();
            var2.prevWalk = var23;
            boolean var25 = var1.getAllowFlight();
            var2.prevFlight = var25;
            Attribute var26 = Attribute.JUMP_STRENGTH;
            AttributeInstance var8 = var1.getAttribute(var26);
            double var29;
            if (var8 != null) {
                var29 = var8.getBaseValue();
            } else {
                var29 = 0.42;
            }

            var2.prevJump = var29;
            var1.setWalkSpeed(0.0F);
            var1.setFlying(false);
            var1.setAllowFlight(false);
            if (var8 != null) {
                var8.setBaseValue(0.0);
            }

            PersistentDataContainer var21 = var1.getPersistentDataContainer();
            NamespacedKey var36 = LOCKED;
            PersistentDataType var6 = PersistentDataType.BYTE;
            Byte var38 = (byte)1;
            var21.set(var36, var6, var38);
            var2.locked = true;
        }
    }

    private void unlock(Player var1, GoldenDreamWorld.Session var2, int var3) {
        if (var2.locked) {
            float var17 = var2.prevWalk;
            var1.setWalkSpeed(var17);
            boolean var19 = var2.prevFlight;
            var1.setAllowFlight(var19);
            Attribute var20 = Attribute.JUMP_STRENGTH;
            AttributeInstance var6 = var1.getAttribute(var20);
            if (var6 != null) {
                double var22 = var2.prevJump;
                var6.setBaseValue(var22);
            }

            PersistentDataContainer var15 = var1.getPersistentDataContainer();
            NamespacedKey var24 = LOCKED;
            var15.remove(var24);
            var2.locked = false;
        }
    }

    private void unlockDefaults(Player var1, int var2) {
        var1.setWalkSpeed(0.2F);
        Attribute var17 = Attribute.JUMP_STRENGTH;
        AttributeInstance var6 = var1.getAttribute(var17);
        if (var6 != null) {
            double var19 = var6.getDefaultValue();
            var6.setBaseValue(var19);
        }

        boolean var24;
        label15: {
            GameMode var7 = var1.getGameMode();
            GameMode var5 = GameMode.CREATIVE;
            if (var7 != var5) {
                GameMode var25 = GameMode.SPECTATOR;
                if (var7 != var25) {
                    var24 = false;
                    break label15;
                }
            }

            var24 = true;
        }

        var1.setAllowFlight(var24);
        PersistentDataContainer var16 = var1.getPersistentDataContainer();
        NamespacedKey var23 = LOCKED;
        var16.remove(var23);
    }

    public void shutdown(int var1) {
        for (GoldenDreamWorld.Session var81 : this.sessions.values()) {
            if (var81.task != null) {
                var81.task.cancel();
            }

            Server var63 = this.plugin.getServer();
            UUID var72 = var81.pid;
            Player var82 = var63.getPlayer(var72);
            if (var82 != null) {
                this.unlock(var82, var81, 738583955);
                long var75 = var81.prevOffset;
                boolean var77 = var81.prevRel;
                var82.setPlayerTime(var75, var77);
            }
        }

        this.sessions.clear();

        for (GoldenDreamWorld.Visit var7 : this.visits.values()) {
            if (var7.task != null) {
                var7.task.cancel();
            }
        }

        this.visits.clear();
        if (this.gradeTask != null) {
            this.gradeTask.cancel();
            this.gradeTask = null;
        }

        this.graded.clear();

        for (Player var80 : this.plugin.getServer().getOnlinePlayers()) {
            World var8 = var80.getWorld();
            World var69 = this.pocket;
            if (var8 == var69 || this.ours(var8, 1901240484)) {
                var80.resetPlayerTime();
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent var1) {
        Player var8 = var1.getPlayer();
        Attribute var3 = Attribute.JUMP_STRENGTH;
        AttributeInstance var9 = var8.getAttribute(var3);
        boolean var88;
        if (var8.getWalkSpeed() != 0.0F && (var9 == null || !(var9.getBaseValue() <= 0.0))) {
            var88 = false;
        } else {
            var88 = true;
        }

        label58: {
            PersistentDataContainer var24 = var8.getPersistentDataContainer();
            NamespacedKey var90 = LOCKED;
            PersistentDataType var129 = PersistentDataType.BYTE;
            if (!var24.has(var90, var129)) {
                if (!var88) {
                    break label58;
                }

                Map var28 = this.sessions;
                UUID var92 = var8.getUniqueId();
                if (var28.containsKey(var92)) {
                    break label58;
                }
            }

            this.unlockDefaults(var8, 804618137);
            var8.resetPlayerTime();
            Logger var34 = this.plugin.getLogger();
            String var96 = "Golden Dream: undid a leftover movement lock on " + var8.getName();
            var34.info(var96);
        }

        if (!var8.hasGravity()) {
            var8.setGravity(true);
            Logger var40 = this.plugin.getLogger();
            String var100 = "Golden Dream: restored gravity for " + var8.getName();
            var40.info(var100);
        }

        YamlConfiguration var42 = this.dream;
        String var104 = var8.getUniqueId() + ".wakeOnJoin";
        if (var42.getBoolean(var104, false)) {
            BukkitScheduler var47 = this.plugin.getServer().getScheduler();
            BlissPlugin var106 = this.plugin;
            Runnable var132 = () -> {
                if (var8.isOnline() && !var8.isDead()) {
                    this.wake(var8, 1928909343);
                }
            };
            var47.runTaskLater(var106, var132, 20L);
        } else {
            UUID var108 = var8.getUniqueId();
            if (this.inDream(var108, 2004945976)) {
                Map var52 = this.sessions;
                UUID var110 = var8.getUniqueId();
                if (!var52.containsKey(var110)) {
                    UUID var112 = var8.getUniqueId();
                    boolean var11 = this.intruded(var112, 1723018507);
                    BukkitScheduler var59 = this.plugin.getServer().getScheduler();
                    BlissPlugin var114 = this.plugin;
                    Runnable var134 = () -> {
                        if (var8.isOnline()) {
                            UUID var24x = var8.getUniqueId();
                            if (this.inDream(var24x, 2004945976)) {
                                Map var12 = this.sessions;
                                UUID var26 = var8.getUniqueId();
                                if (!var12.containsKey(var26)) {
                                    if (!var11) {
                                        long var32 = var8.getPlayerTimeOffset();
                                        boolean var34x = var8.isPlayerTimeRelative();
                                        this.enter(var8, var32, var34x, 1738178342);
                                    } else {
                                        World var29 = var8.getWorld();
                                        if (!this.ours(var29, 1901240484)) {
                                            Location var7 = this.arrival(1026150274);
                                            if (var7 != null) {
                                                var8.teleport(var7);
                                                var8.setFallDistance(0.0F);
                                            }
                                        }
                                    }

                                    return;
                                }
                            }
                        }
                    };
                    var59.runTaskLater(var114, var134, 20L);
                    return;
                }
            }

            YamlConfiguration var62 = this.dream;
            String var118 = "pocketVisits." + var8.getUniqueId();
            if (var62.contains(var118)) {
                if (var8.getWorld().getName().equals("goldendream")) {
                    UUID var126 = var8.getUniqueId();
                    GoldenDreamWorld.Visit var150 = this.loadVisit(var126, 1695920324);
                    BukkitScheduler var86 = this.plugin.getServer().getScheduler();
                    BlissPlugin var128 = this.plugin;
                    Runnable var138 = () -> {
                        if (var8.isOnline() && var150 != null) {
                            Map var8x = this.visits;
                            UUID var11x = var150.pid;
                            if (var8x.containsKey(var11x)) {
                                this.arm(var150, 692179808);
                            }
                        }
                    };
                    var86.runTaskLater(var128, var138, 20L);
                } else {
                    UUID var124 = var8.getUniqueId();
                    this.dropVisit(var124, 509312564);
                }
            } else {
                if (var8.getWorld().getName().equals("goldendream")) {
                    Location var149 = this.arrival(1026150274);
                    if (var149 != null) {
                        BukkitScheduler var74 = this.plugin.getServer().getScheduler();
                        BlissPlugin var121 = this.plugin;
                        Runnable var136 = () -> {
                            if (var8.isOnline()) {
                                var8.teleport(var149);
                                var8.setFallDistance(0.0F);
                            }
                        };
                        var74.runTask(var121, var136);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent var1) {
        Set var7 = this.graded;
        UUID var25 = var1.getPlayer().getUniqueId();
        var7.remove(var25);
        Map var10 = this.gradedDay;
        UUID var28 = var1.getPlayer().getUniqueId();
        var10.remove(var28);
        Map var13 = this.sessions;
        UUID var31 = var1.getPlayer().getUniqueId();
        GoldenDreamWorld.Session var6 = (GoldenDreamWorld.Session)var13.remove(var31);
        if (var6 != null) {
            if (var6.task != null) {
                var6.task.cancel();
            }

            Player var33 = var1.getPlayer();
            this.unlock(var33, var6, 738583955);
            Player var23 = var1.getPlayer();
            long var35 = var6.prevOffset;
            boolean var37 = var6.prevRel;
            var23.setPlayerTime(var35, var37);
        }
    }

    @EventHandler
    public void onPortal(PlayerPortalEvent var1) {
        World var19 = var1.getFrom().getWorld();
        if (this.ours(var19, 1901240484)) {
            Location var20 = var1.getFrom();
            TeleportCause var29 = var1.getCause();
            TeleportCause var57 = TeleportCause.NETHER_PORTAL;
            if (var29 != var57) {
                TeleportCause var31 = var1.getCause();
                TeleportCause var58 = TeleportCause.END_PORTAL;
                if (var31 == var58) {
                    World var60 = this.overworld;
                    if (var19 == var60 && this.end != null) {
                        platform(this.end, 89382035);
                        World var77 = this.end;
                        Location var61 = new Location(var77, 100.5, 49.0, 0.5, 90.0F, 0.0F);
                        var1.setTo(var61);
                    } else {
                        World var63 = this.end;
                        if (var19 == var63 && this.overworld != null) {
                            Location var21 = this.arrival(1026150274);
                            if (var21 != null) {
                                var1.setTo(var21);
                            }
                        }
                    }
                }
            } else {
                World var66 = this.overworld;
                Location var122;
                if (var19 == var66 && this.nether != null) {
                    World var74 = this.nether;
                    double var81 = var20.getX() / 8.0;
                    double var93 = Math.min(Math.max(var20.getY(), 10.0), 120.0);
                    double var105 = var20.getZ() / 8.0;
                    float var113 = var20.getYaw();
                    float var119 = var20.getPitch();
                    Location var48 = new Location(var74, var81, var93, var105, var113, var119);
                    var122 = var48;
                } else {
                    World var72 = this.nether;
                    if (var19 != var72 || this.overworld == null) {
                        return;
                    }

                    World var76 = this.overworld;
                    double var86 = var20.getX() * 8.0;
                    double var98 = var20.getY();
                    double var110 = var20.getZ() * 8.0;
                    float var117 = var20.getYaw();
                    float var121 = var20.getPitch();
                    Location var56 = new Location(var76, var86, var98, var110, var117, var121);
                    var122 = var56;
                }

                var1.setTo(var122);
                var1.setCanCreatePortal(true);
                var1.setSearchRadius(128);
                var1.setCreationRadius(16);
            }
        }
    }

    private static void platform(World var0, int var1) {
        for (int var6 = 98; var6 <= 102; var6++) {
            for (int var7 = -2; var7 <= 2; var7++) {
                Block var13 = var0.getBlockAt(var6, 48, var7);
                Material var20 = Material.OBSIDIAN;
                var13.setType(var20, false);

                for (int var8 = 49; var8 <= 51; var8++) {
                    Block var17 = var0.getBlockAt(var6, var8, var7);
                    Material var23 = Material.AIR;
                    var17.setType(var23, false);
                }
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent var1) {
        Player var13 = var1.getPlayer();
        World var14 = var13.getWorld();
        if (this.pocket != null) {
            World var43 = this.pocket;
            if (var14 == var43) {
                World var54 = this.pocket;
                Location var44 = new Location(var54, 0.5, 65.0, 0.5, -90.0F, 0.0F);
                var1.setRespawnLocation(var44);
                BukkitScheduler var26 = this.plugin.getServer().getScheduler();
                BlissPlugin var46 = this.plugin;
                Runnable var53 = () -> {
                    if (var13.isOnline() && !var13.isDead()) {
                        UUID var15x = var13.getUniqueId();
                        if (this.inDream(var15x, 2004945976)) {
                            Map var12 = this.sessions;
                            UUID var17 = var13.getUniqueId();
                            if (!var12.containsKey(var17)) {
                                long var19 = var13.getPlayerTimeOffset();
                                boolean var21 = var13.isPlayerTimeRelative();
                                this.enter(var13, var19, var21, 1738178342);
                            }
                        }
                    }
                };
                var26.runTask(var46, var53);
                return;
            }
        }

        if (this.ours(var14, 1901240484)) {
            if ((var1.isBedSpawn() || var1.isAnchorSpawn()) && var1.getRespawnLocation().getWorld() != null) {
                World var50 = var1.getRespawnLocation().getWorld();
                if (this.ours(var50, 1901240484)) {
                    return;
                }
            }

            Location var15 = this.arrival(1026150274);
            if (var15 != null) {
                var1.setRespawnLocation(var15);
            }
        }
    }

    private void evacuate(World[] var1, int var2) {
        World var19;
        if (this.plugin.getServer().getWorlds().isEmpty()) {
            var19 = null;
        } else {
            var19 = (World)this.plugin.getServer().getWorlds().get(0);
        }

        Location var21;
        if (var19 == null) {
            var21 = null;
        } else {
            var21 = var19.getSpawnLocation();
        }

        Location var7 = var21;

        for (World var11 : var1) {
            if (var11 != null) {
                List<Player> var72 = var11.getPlayers();

                for (Player var13 : new ArrayList<>(var72)) {
                    Map var38 = this.sessions;
                    UUID var64 = var13.getUniqueId();
                    GoldenDreamWorld.Session var14 = (GoldenDreamWorld.Session)var38.remove(var64);
                    if (var14 != null && var14.task != null) {
                        var14.task.cancel();
                    }

                    if (var13.getWalkSpeed() == 0.0F) {
                        this.unlockDefaults(var13, 804618137);
                    }

                    UUID var68 = var13.getUniqueId();
                    this.forgetGrade(var68, 1777841881);
                    var13.resetPlayerTime();
                    if (var7 != null) {
                        var13.teleport(var7);
                        var13.setFallDistance(0.0F);
                    }
                }
            }
        }
    }

    public boolean resetAll(CommandSender var1, int var2) {
        Collection<GoldenDreamWorld.Visit> var116 = this.visits.values();

        for (GoldenDreamWorld.Visit var142 : new ArrayList<>(var116)) {
            Server var75 = this.plugin.getServer();
            UUID var111 = var142.pid;
            Player var148 = var75.getPlayer(var111);
            if (var148 != null && var148.isOnline() && !var148.isDead()) {
                this.pocketKick(var148, 853854218);
            } else if (var142.task != null) {
                var142.task.cancel();
                var142.task = null;
            }
        }

        this.visits.clear();
        World[] var90 = new World[4];
        World var133 = this.pocket;
        var90[0] = var133;
        World var135 = this.overworld;
        var90[1] = var135;
        World var137 = this.nether;
        var90[2] = var137;
        World var139 = this.end;
        var90[3] = var139;
        this.evacuate(var90, 308737335);
        int var141 = 0;
        int var8 = 0;
        Set<String> var119 = this.dream.getKeys(false);

        for (String var10 : new ArrayList<>(var119)) {
            if (!var10.equals("pocketVisits")) {
                Player var11 = null;

                try {
                    Server var58 = this.plugin.getServer();
                    UUID var106 = UUID.fromString(var10);
                    var11 = var58.getPlayer(var106);
                } catch (IllegalArgumentException var149) {
                }

                if (var11 == null || !var11.isOnline() || var11.isDead()) {
                    YamlConfiguration var68 = this.dream;
                    String var109 = var10 + ".wakeOnJoin";
                    Boolean var127 = true;
                    var68.set(var109, var127);
                    var8++;
                } else if (this.wake(var11, 1928909343)) {
                    var141 += 1;
                }
            }
        }

        this.saveDream(605217750);
        if (this.gradeTask != null) {
            this.gradeTask.cancel();
            this.gradeTask = null;
        }

        this.graded.clear();
        String var120 = "goldendream";
        boolean var93 = this.wipe(var120, 1764694250);
        boolean var36 = true & var93;
        String var121 = "goldenworldend";
        boolean var95 = this.wipe(var121, 1764694250);
        boolean var38 = var36 & var95;
        String var122 = "goldenworldnether";
        boolean var97 = this.wipe(var122, 1764694250);
        boolean var40 = var38 & var97;
        String var123 = "goldenworld";
        boolean var99 = this.wipe(var123, 1764694250);
        boolean var147 = var40 & var99;
        this.end = null;
        this.nether = null;
        this.overworld = null;
        this.pocket = null;
        this.pocket(698565255);
        this.worldSet(1705953131);
        if (var1 != null) {
            String var114;
            if (var147) {
                var114 = "&aGolden Dream reset — fresh worlds. Woke &f" + var141 + "&a; &f" + var8 + "&a offline dreamer(s) will wake on their next login.";
            } else {
                var114 = "&eGolden Dream reset, but a world folder was locked — restart to finish clearing it.";
            }

            Component var102 = Text.legacy(var114, 373192400);
            var1.sendMessage(var102);
        }

        return var147;
    }

    public boolean importOverworld(CommandSender var1, int var2) {
        File var4 = Bukkit.getWorldContainer();
        String var5 = "goldendream_import";
        File var3 = new File(var4, var5);
        File var9 = var3;
        label79:
        if (var3.isDirectory()) {
            String var99 = "level.dat";
            File var14 = new File(var3, var99);
            if (!var14.isFile()) {
                String var100 = "region";
                File var16 = new File(var3, var100);
                if (!var16.isDirectory()) {
                    break label79;
                }
            }

            World[] var59 = new World[1];
            World var111 = this.overworld;
            var59[0] = var111;
            this.evacuate(var59, 308737335);
            String var60 = "goldenworld";
            if (!this.wipe(var60, 1764694250)) {
                String var96 = "goldenworld";
                Environment var108 = Environment.NORMAL;
                World var80 = this.load(var96, var108, 636167358);
                this.overworld = var80;
                if (this.overworld != null && this.gradeTask == null) {
                    BukkitScheduler var84 = this.plugin.getServer().getScheduler();
                    BlissPlugin var98 = this.plugin;
                    Runnable var110 = this::grade;
                    BukkitTask var85 = var84.runTaskTimer(var98, var110, 1L, 1L);
                    this.gradeTask = var85;
                }

                if (var1 != null) {
                    Component var87 = Text.legacy("&eCould not clear the old goldenworld (locked) — restart, then import again.", 373192400);
                    var1.sendMessage(var87);
                }

                return false;
            }

            this.overworld = null;
            File var90 = Bukkit.getWorldContainer();
            String var102 = "goldenworld";
            File var25 = new File(var90, var102);
            File var10 = var25;

            try {
                Path var27 = var9.toPath();
                Path var63 = var10.toPath();
                copyTree(var27, var63, 724235208);
                String var103 = "uid.dat";
                File var28 = new File(var10, var103);
                var28.delete();
                String var104 = "session.lock";
                File var30 = new File(var10, var104);
                var30.delete();
            } catch (IOException var112) {
                if (var1 != null) {
                    Component var67 = Text.legacy("&cImport copy failed: &f" + var112.getMessage(), 373192400);
                    var1.sendMessage(var67);
                }

                return false;
            }

            String var93 = "goldenworld";
            Environment var105 = Environment.NORMAL;
            World var69 = this.load(var93, var105, 636167358);
            this.overworld = var69;
            if (this.overworld != null && this.gradeTask == null) {
                BukkitScheduler var73 = this.plugin.getServer().getScheduler();
                BlissPlugin var95 = this.plugin;
                Runnable var107 = this::grade;
                BukkitTask var74 = var73.runTaskTimer(var95, var107, 1L, 1L);
                this.gradeTask = var74;
            }

            if (var1 != null) {
                String var77;
                if (this.overworld != null) {
                    var77 = "&aImported &fgoldendream_import&a as the memory overworld.";
                } else {
                    var77 = "&cImport copied but the world would not load.";
                }

                Component var78 = Text.legacy(var77, 373192400);
                var1.sendMessage(var78);
            }

            boolean var46;
            if (this.overworld != null) {
                var46 = true;
            } else {
                var46 = false;
            }

            return var46;
        }

        if (var1 != null) {
            Component var57 = Text.legacy("&cNo world found at &fgoldendream_import&c. Put a world folder there first.", 373192400);
            var1.sendMessage(var57);
        }

        return false;
    }

    // $VF: Handled exception range with multiple entry points by splitting it
    // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
    private boolean wipe(String var1, int var2) {
        World var7 = Bukkit.getWorld(var1);
        if (var7 != null && !Bukkit.unloadWorld(var7, false)) {
            Logger var29 = this.plugin.getLogger();
            String var35 = "Golden Dream: could not unload world '" + var1 + "' (players still in it?)";
            var29.warning(var35);
            return false;
        } else {
            File var5 = Bukkit.getWorldContainer();
            File var14 = new File(var5, var1);
            File var8 = var14;
            if (!var14.exists()) {
                return true;
            } else {
                try {
                    deleteTree(var8.toPath(), 757273025);
                    return !var8.exists();
                } catch (IOException var9) {
                    Logger var24 = this.plugin.getLogger();
                    String var37 = var9.getMessage();
                    String var33 = "Golden Dream: could not delete world '" + var1 + "' (" + var37 + ")";
                    var24.warning(var33);
                    return false;
                }
            }
        }
    }

    private static void deleteTree(Path var0, int var1) throws IOException {
        LinkOption[] var20 = new LinkOption[0];
        if (Files.exists(var0, var20)) {
            FileVisitOption[] var22 = new FileVisitOption[0];

            try (Stream<Path> var4 = Files.walk(var0, var22)) {
                Comparator<Path> var23 = Comparator.reverseOrder();
                Stream<Path> var11 = var4.sorted(var23);
                Consumer<Path> var24 = var0x -> {
                    try {
                        Files.deleteIfExists(var0x);
                    } catch (IOException var1x) {
                    }
                };
                var11.forEach(var24);
            }
        }
    }

    // $VF: Handled exception range with multiple entry points by splitting it
    // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
    private static void copyTree(Path var0, Path var1, int var2) throws IOException {
        try (Stream<Path> var8 = Files.walk(var0)) {
            Iterator<Path> var9 = var8.iterator();
            while (var9.hasNext()) {
                Path var10 = var9.next();
                Path var11 = var1.resolve(var0.relativize(var10));
                if (Files.isDirectory(var10)) {
                    Files.createDirectories(var11);
                } else {
                    Files.copy(var10, var11, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                }
            }
        }
    }

    static {
        String var0 = "FIREFLY";
        Particle var1 = Particle.WHITE_ASH;
        LIGHT_MOTE = resolveParticle(var0, var1, 535815664);
    }

    private final class Session {
        final UUID pid;
        final long prevOffset;
        final boolean prevRel;
        float prevWalk = 0.2F;
        boolean prevFlight;
        double prevJump = 0.42;
        boolean locked;
        boolean clockBack;
        boolean skip;
        boolean auto = true;
        int t;
        BukkitTask task;

        Session(UUID nullx, long nullxx, boolean nullxxx, int var6) {
            this.pid = nullx;
            this.prevOffset = nullxx;
            this.prevRel = nullxxx;
        }
    }

    private final class Visit {
        final UUID pid;
        final Location back;
        BukkitTask task;

        Visit(UUID nullx, Location nullxx, int var4) {
            this.pid = nullx;
            this.back = nullxx;
        }
    }

    static final class Walkway extends ChunkGenerator {
        private static final int HALF = 2;

        public void generateSurface(WorldInfo var1, Random var2, int var3, int var4, ChunkData var5) {
            int var11 = var4 << 4;
            int var12 = var11;

            while (true) {
                int var26 = var11 + 16;
                if (var12 >= var26) {
                    return;
                }

                if (var12 >= -2 && var12 <= 2) {
                    for (int var13 = 0; var13 < 16; var13++) {
                        for (int var14 = 60; var14 <= 64; var14++) {
                            int var33 = var12 - var11;
                            Material var34 = Material.BARRIER;
                            var5.setBlock(var13, var14, var33, var34);
                        }
                    }
                }

                var12++;
            }
        }

        public boolean shouldGenerateNoise() {
            return false;
        }

        public boolean shouldGenerateSurface() {
            return false;
        }

        public boolean shouldGenerateCaves() {
            return false;
        }

        public boolean shouldGenerateDecorations() {
            return false;
        }

        public boolean shouldGenerateMobs() {
            return false;
        }

        public boolean shouldGenerateStructures() {
            return false;
        }

        public Location getFixedSpawnLocation(World var1, Random var2) {
            return new Location(var1, 0.5, 65.0, 0.5);
        }
    }
}
