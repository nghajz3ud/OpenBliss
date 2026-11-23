package com.bliss.dragongames;

import com.bliss.BlissPlugin;
import com.bliss.core.AtomicYml;
import com.bliss.core.Particles;
import com.bliss.core.TeamManager;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.logging.Logger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.Particle.DustOptions;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

public final class DragonGamesManager {
    public static final double BORDER_RADIUS = 1000.0;
    private static final int BORDER_WARNING = 5;
    private static final double BORDER_BUFFER = 10.0;
    public static final int POINTS_KILL_BASE = 25;
    public static final int POINTS_KILL_BONUS = 35;
    private static final double HP_PER_HEART = 2.0;
    public static final long TRACKER_DURATION_MS = 3600000L;
    private static final long TRACKER_TICK_PERIOD = 5L;
    private static final int TRAIL_STEPS = 16;
    private static final double TRAIL_STEP = 0.2;
    private static final DustOptions TRAIL_DUST;
    static final int START_POINTS = 50;
    private static final long HEALTH_TICK_PERIOD = 20L;
    private final BlissPlugin plugin;
    private final NamespacedKey heartsModifierKey;
    private boolean running;
    private boolean trophyTaken;
    private final Set<UUID> participants;
    private final Map<UUID, Integer> points;
    private final Map<UUID, Integer> hearts;
    private final Map<UUID, DragonGamesManager.Tracking> trackers;
    private long eggHeldSince;
    private long nextAskAt;
    private boolean holderStartUsed;
    private boolean promptDeclined;
    private long bloodMoonSavedTime;
    private boolean bloodMoonSavedCycle;
    private long bloodMoonBarEnd;
    private BloodMoon bloodMoon;
    private DragonGamesLeaderboard leaderboard;
    private BukkitTask healthTask;
    private BukkitTask trackerTask;
    private boolean warnedNoPedestal;
    public static final String TEAM_NAME = "DragonGames";
    public static final String TEAM_OUT_NAME = "DragonGamesOut";

    public DragonGamesManager(BlissPlugin var1, int var2) {
        HashSet var4 = new HashSet();
        this.participants = var4;
        HashMap var18 = new HashMap();
        this.points = var18;
        HashMap var19 = new HashMap();
        this.hearts = var19;
        HashMap var20 = new HashMap();
        this.trackers = var20;
        this.eggHeldSince = -1L;
        this.nextAskAt = -1L;
        this.bloodMoonSavedTime = -1L;
        this.bloodMoonSavedCycle = true;
        this.bloodMoonBarEnd = 0L;
        this.plugin = var1;
        NamespacedKey var31 = new NamespacedKey(var1, "dragon_games_hearts");
        this.heartsModifierKey = var31;
    }

    private File file(int var1) {
        File var6 = this.plugin.getDataFolder();
        String var4 = "dragon_games.yml";
        return new File(var6, var4);
    }

    public void load(int var1) {
        File var9 = this.file(975107659);
        if (var9.exists()) {
            YamlConfiguration var10 = YamlConfiguration.loadConfiguration(var9);
            String var4 = "running";
            boolean var73 = var10.getBoolean(var4, false);
            this.running = var73;
            Iterator var11 = var10.getStringList("participants").iterator();

            while (var11.hasNext()) {
                UUID var13 = parse((String)var11.next(), 1833016282);
                if (var13 != null) {
                    Set var36 = this.participants;
                    var36.add(var13);
                }
            }

            ConfigurationSection var39 = var10.getConfigurationSection("points");
            Map var78 = this.points;
            readIntMap(var39, var78, 643083590);
            ConfigurationSection var41 = var10.getConfigurationSection("hearts");
            Map var81 = this.hearts;
            readIntMap(var41, var81, 643083590);
            ConfigurationSection var128 = var10.getConfigurationSection("trackers");
            if (var128 != null) {
                for (String var130 : var128.getKeys(false)) {
                    UUID var14 = parse(var130, 1833016282);
                    String var85 = var130 + ".target";
                    UUID var15 = parse(var128.getString(var85), 1833016282);
                    String var87 = var130 + ".expiry";
                    long var16 = var128.getLong(var87, 0L);
                    if (var14 != null && var15 != null) {
                        Map<UUID, DragonGamesManager.Tracking> var64 = this.trackers;
                        DragonGamesManager.Tracking var109 = new DragonGamesManager.Tracking(var15, var16);
                        var64.put(var14, var109);
                    }
                }
            }

            String var110 = "egg-held-since";
            long var90 = var10.getLong(var110, -1L);
            this.eggHeldSince = var90;
            String var111 = "next-ask-at";
            long var93 = var10.getLong(var111, -1L);
            this.nextAskAt = var93;
            String var112 = "holder-start-used";
            boolean var96 = var10.getBoolean(var112, false);
            this.holderStartUsed = var96;
            String var113 = "prompt-declined";
            boolean var98 = var10.getBoolean(var113, false);
            this.promptDeclined = var98;
            String var114 = "blood-moon-saved-time";
            long var100 = var10.getLong(var114, -1L);
            this.bloodMoonSavedTime = var100;
            String var115 = "blood-moon-saved-cycle";
            boolean var103 = var10.getBoolean(var115, true);
            this.bloodMoonSavedCycle = var103;
            String var116 = "blood-moon-bar-end";
            long var105 = var10.getLong(var116, 0L);
            this.bloodMoonBarEnd = var105;
        }
    }

    private static void readIntMap(ConfigurationSection var0, Map<UUID, Integer> var1, int var2) {
        if (var0 != null) {
            for (String var8 : var0.getKeys(false)) {
                UUID var9 = parse(var8, 1833016282);
                if (var9 != null) {
                    Integer var25 = var0.getInt(var8);
                    var1.put(var9, var25);
                }
            }
        }
    }

    private static UUID parse(String var0, int var1) {
        if (var0 == null) {
            return null;
        } else {
            try {
                return UUID.fromString(var0);
            } catch (IllegalArgumentException var6) {
                return null;
            }
        }
    }

    public void save(int var1) {
        YamlConfiguration var2 = new YamlConfiguration();
        YamlConfiguration var4 = var2;
        String var64 = "running";
        Boolean var97 = this.running;
        var2.set(var64, var97);
        ArrayList var9 = new ArrayList();
        ArrayList var5 = var9;

        for (UUID var7 : this.participants) {
            String var66 = var7.toString();
            var5.add(var66);
        }

        String var67 = "participants";
        var2.set(var67, var5);

        for (Entry var143 : this.points.entrySet()) {
            String var71 = "points." + var143.getKey();
            Object var100 = var143.getValue();
            var4.set(var71, var100);
        }

        for (Entry var144 : this.hearts.entrySet()) {
            String var75 = "hearts." + var144.getKey();
            Object var102 = var144.getValue();
            var4.set(var75, var102);
        }

        for (Entry var145 : this.trackers.entrySet()) {
            String var91 = "trackers." + var145.getKey() + ".target";
            String var132 = ((DragonGamesManager.Tracking)var145.getValue()).target().toString();
            var4.set(var91, var132);
            String var95 = "trackers." + var145.getKey() + ".expiry";
            Long var138 = ((DragonGamesManager.Tracking)var145.getValue()).expiry();
            var4.set(var95, var138);
        }

        String var76 = "egg-held-since";
        Long var106 = this.eggHeldSince;
        var4.set(var76, var106);
        String var77 = "next-ask-at";
        Long var110 = this.nextAskAt;
        var4.set(var77, var110);
        String var78 = "holder-start-used";
        Boolean var113 = this.holderStartUsed;
        var4.set(var78, var113);
        String var79 = "prompt-declined";
        Boolean var116 = this.promptDeclined;
        var4.set(var79, var116);
        String var80 = "blood-moon-saved-time";
        Long var120 = this.bloodMoonSavedTime;
        var4.set(var80, var120);
        String var81 = "blood-moon-saved-cycle";
        Boolean var123 = this.bloodMoonSavedCycle;
        var4.set(var81, var123);
        String var82 = "blood-moon-bar-end";
        Long var127 = this.bloodMoonBarEnd;
        var4.set(var82, var127);

        try {
            File var84 = this.file(975107659);
            AtomicYml.save(var4, var84, 1347473142);
        } catch (IOException var146) {
            Logger var58 = this.plugin.getLogger();
            String var87 = "Could not save dragon_games.yml: " + var146.getMessage();
            var58.warning(var87);
        }
    }

    public void start(int var1) {
        Scoreboard var10 = Bukkit.getScoreboardManager().getMainScoreboard();
        String var3 = "DragonGames";
        NamedTextColor var4 = NamedTextColor.GREEN;
        ChatColor var5 = ChatColor.GREEN;
        freshTeam(var10, var3, var4, var5, 1464135475);
        String var28 = "DragonGamesOut";
        NamedTextColor var34 = NamedTextColor.RED;
        ChatColor var39 = ChatColor.RED;
        freshTeam(var10, var28, var34, var39, 1464135475);
        BukkitScheduler var29 = Bukkit.getScheduler();
        BlissPlugin var36 = this.plugin;
        Runnable var41 = this::healthTick;
        BukkitTask var30 = var29.runTaskTimer(var36, var41, 20L, 20L);
        this.healthTask = var30;
        BukkitScheduler var31 = Bukkit.getScheduler();
        BlissPlugin var38 = this.plugin;
        Runnable var43 = this::trackerTick;
        BukkitTask var32 = var31.runTaskTimer(var38, var43, 5L, 5L);
        this.trackerTask = var32;

        for (Player var12 : Bukkit.getOnlinePlayers()) {
            this.onJoin(var12, 1034925991);
        }
    }

    public void stop(int var1) {
        if (this.healthTask != null) {
            this.healthTask.cancel();
        }

        if (this.trackerTask != null) {
            this.trackerTask.cancel();
        }

        for (Player var5 : Bukkit.getOnlinePlayers()) {
            this.removeHeartsModifier(var5, 643390433);
        }

        if (this.leaderboard != null) {
            this.leaderboard.remove(329989465);
        }

        this.save(2020232656);
    }

    public boolean isRunning(int var1) {
        return this.running;
    }

    public boolean isParticipant(UUID var1, int var2) {
        if (var1 != null) {
            Set var6 = this.participants;
            if (var6.contains(var1)) {
                return true;
            }
        }

        return false;
    }

    public int points(UUID var1, int var2) {
        Map<UUID, Integer> var6 = this.points;
        Integer var10 = 0;
        return var6.getOrDefault(var1, var10);
    }

    Set<UUID> participantIds(int var1) {
        return Collections.unmodifiableSet(this.participants);
    }

    public int hearts(UUID var1) {
        Map<UUID, Integer> var5 = this.hearts;
        Integer var9 = 0;
        return var5.getOrDefault(var1, var9);
    }

    public DragonGamesManager.Tracking tracking(UUID var1) {
        Map var4 = this.trackers;
        return (DragonGamesManager.Tracking)var4.get(var1);
    }

    public boolean isTracking(UUID var1, int var2) {
        Map var5 = this.trackers;
        return var5.containsKey(var1);
    }

    public long eggHeldSince(int var1) {
        return this.eggHeldSince;
    }

    public long nextAskAt(int var1) {
        return this.nextAskAt;
    }

    public boolean holderStartUsed(int var1) {
        return this.holderStartUsed;
    }

    public boolean promptDeclined(int var1) {
        return this.promptDeclined;
    }

    public void setEggHeldSince(long var1, int var3) {
        this.eggHeldSince = var1;
        this.save(2020232656);
    }

    public void setNextAskAt(long var1, int var3) {
        this.nextAskAt = var1;
        this.save(2020232656);
    }

    public void setHolderStartUsed(boolean var1, int var2) {
        this.holderStartUsed = var1;
        this.save(2020232656);
    }

    public void setPromptDeclined(boolean var1, int var2) {
        this.promptDeclined = var1;
        this.save(2020232656);
    }

    public long bloodMoonSavedTime(int var1) {
        return this.bloodMoonSavedTime;
    }

    public boolean bloodMoonSavedCycle(int var1) {
        return this.bloodMoonSavedCycle;
    }

    public long bloodMoonBarEnd(int var1) {
        return this.bloodMoonBarEnd;
    }

    public void setBloodMoonState(long var1, boolean var3, long var4, int var6) {
        this.bloodMoonSavedTime = var1;
        this.bloodMoonSavedCycle = var3;
        this.bloodMoonBarEnd = var4;
        this.save(2020232656);
    }

    void setBloodMoon(BloodMoon var1, int var2) {
        this.bloodMoon = var1;
    }

    BloodMoon bloodMoon(int var1) {
        return this.bloodMoon;
    }

    void setLeaderboard(DragonGamesLeaderboard var1, int var2) {
        this.leaderboard = var1;
    }

    public void setPoints(UUID var1, int var2, int var3) {
        Map var7 = this.points;
        Integer var10 = var2;
        var7.put(var1, var10);
        this.save(2020232656);
    }

    public void addPoints(UUID var1, int var2, int var3) {
        Map<UUID, Integer> var8 = this.points;
        Integer var11 = var2;
        BiFunction<Integer, Integer, Integer> var7 = Integer::sum;
        var8.merge(var1, var11, var7);
        this.save(2020232656);
    }

    public boolean spend(UUID var1, int var2, int var3) {
        int var8 = this.points(var1, 77304185);
        if (var8 < var2) {
            return false;
        } else {
            Map var12 = this.points;
            Integer var20 = var8 - var2;
            var12.put(var1, var20);
            this.save(2020232656);
            return true;
        }
    }

    public void startGame(int var1) {
        boolean var76;
        if (!this.running) {
            var76 = true;
        } else {
            var76 = false;
        }

        if (var76) {
            this.participants.clear();
            this.points.clear();
            this.hearts.clear();
            this.trackers.clear();
            this.trophyTaken = false;

            for (Player var110 : Bukkit.getOnlinePlayers()) {
                ItemStack[] var8 = var110.getInventory().getContents();
                int var9 = 0;

                while (true) {
                    int var96 = var8.length;
                    if (var9 >= var96) {
                        break;
                    }

                    if (var8[var9] != null) {
                        Material var72 = var8[var9].getType();
                        Material var99 = Material.DRAGON_EGG;
                        if (var72 == var99) {
                            PlayerInventory var74 = var110.getInventory();
                            var74.setItem(var9, null);
                            this.trophyTaken = true;
                        }
                    }

                    var9++;
                }
            }

            if (!this.trophyTaken) {
                Bukkit.getLogger()
                    .warning(
                        "[BlissPlugin] Dragon Games: no Dragon Egg was found on any player, so none was confiscated. The winner will NOT be given one - find the existing egg and hand it over, or the server would end up with two."
                    );
            }
        }

        this.running = true;
        Bukkit.broadcast(Text.legacy("&aDragonGames Started", 373192400));
        Bukkit.broadcast(Text.legacy("&cBorder is 1000 Blocks Within Pedestal", 373192400));

        for (Player var7 : Bukkit.getOnlinePlayers()) {
            Set var44 = this.participants;
            UUID var80 = var7.getUniqueId();
            var44.add(var80);
            Map var47 = this.points;
            UUID var82 = var7.getUniqueId();
            Integer var102 = 50;
            var47.put(var82, var102);
            Map var50 = this.hearts;
            UUID var84 = var7.getUniqueId();
            Integer var104 = 0;
            var50.put(var84, var104);
            Component var88 = Text.legacy("&a" + var7.getName(), 373192400);
            var7.playerListName(var88);
            Team var106 = this.team(1286635874);
            this.teamJoin(var7, var106, 1706888627);
            this.applyBorder(var7, 2029419438);
            this.removeHeartsModifier(var7, 643390433);
            Map var57 = this.trackers;
            UUID var93 = var7.getUniqueId();
            var57.remove(var93);
            Material var94 = Material.COMPASS;
            var7.setCooldown(var94, 0);
        }

        this.save(2020232656);
    }

    public void endGame(int var1) {
        this.running = false;
        Bukkit.broadcast(Text.legacy("&cThe dragon games have ended.", 373192400));
        Player var5 = null;
        int var6 = Integer.MIN_VALUE;

        for (UUID var95 : this.participants) {
            Player var96 = Bukkit.getPlayer(var95);
            if (var96 != null) {
                Map<UUID, Integer> var67 = this.points;
                Integer var91 = 0;
                int var10 = var67.getOrDefault(var95, var91);
                if (var5 == null || var10 > var6) {
                    var5 = var96;
                    var6 = var10;
                }
            }
        }

        if (var5 != null && this.trophyTaken) {
            Material var4 = Material.DRAGON_EGG;
            ItemStack var75 = new ItemStack(var4);
            BlissCoreItems.giveOrDrop(var5, var75, 41085121);
        } else if (var5 != null) {
            Component var83 = Text.legacy(
                "&6You won the Dragon Games! &eThe egg was never collected at the start, so an admin has to hand you the one already in the world.", 373192400
            );
            var5.sendMessage(var83);
            Logger var59 = Bukkit.getLogger();
            String var86 = "[BlissPlugin] Dragon Games ended with no trophy to award: no egg was confiscated at the start. Winner: " + var5.getName();
            var59.warning(var86);
        }

        for (Player var94 : Bukkit.getOnlinePlayers()) {
            this.resetBorder(var94, 1047999183);
            var94.playerListName(null);
            this.removeHeartsModifier(var94, 643390433);
            Material var81 = Material.COMPASS;
            var94.setCooldown(var81, 0);
        }

        Scoreboard var93 = Bukkit.getScoreboardManager().getMainScoreboard();
        Team var8 = var93.getTeam("DragonGames");
        if (var8 != null) {
            var8.unregister();
        }

        Team var9 = var93.getTeam("DragonGamesOut");
        if (var9 != null) {
            var9.unregister();
        }

        this.participants.clear();
        this.hearts.clear();
        this.trackers.clear();
        if (this.bloodMoon != null) {
            this.bloodMoon.end(1481613845);
        }

        this.save(2020232656);
    }

    public void onJoin(Player var1, int var2) {
        UUID var10 = var1.getUniqueId();
        if (this.running) {
            Set var18 = this.participants;
            if (var18.contains(var10)) {
                this.applyBorder(var1, 2029419438);
                Component var65 = Text.legacy("&a" + var1.getName(), 373192400);
                var1.playerListName(var65);
                Team var76 = this.team(1286635874);
                this.teamJoin(var1, var76, 1706888627);
                Map var31 = this.trackers;
                DragonGamesManager.Tracking var11 = (DragonGamesManager.Tracking)var31.get(var10);
                if (var11 != null) {
                    long var36 = var11.expiry();
                    long var77 = System.currentTimeMillis();
                    long var12 = var36 - var77;
                    if (var12 > 0L) {
                        Material var68 = Material.COMPASS;
                        long var86 = var12 / 50L;
                        int var85 = (int)Math.min(2147483647L, var86);
                        var1.setCooldown(var68, var85);
                    }
                }
            } else {
                Component var56 = Text.legacy("&c" + var1.getName(), 373192400);
                var1.playerListName(var56);
                Team var74 = this.outTeam(187830077);
                this.teamJoin(var1, var74, 1706888627);
                Map var23 = this.hearts;
                var23.remove(var10);
                this.resetBorder(var1, 1047999183);
            }
        } else {
            Set var45 = this.participants;
            var45.remove(var10);
            Map var48 = this.hearts;
            var48.remove(var10);
            var1.playerListName(null);
            this.teamClear(var1, 2016617065);
            this.resetBorder(var1, 1047999183);
        }

        this.applyHeartsModifier(var1, 2081068606);
    }

    public void onDeath(Player var1, Player var2, int var3) {
        if (this.running) {
            UUID var8 = var1.getUniqueId();
            Set var18 = this.participants;
            boolean var9 = var18.remove(var8);
            Map<UUID, Integer> var21 = this.hearts;
            Integer var85 = 0;
            int var10 = var21.getOrDefault(var8, var85);
            if (var10 > 0) {
                World var11 = var1.getWorld();
                Location var12 = var1.getLocation();

                for (int var13 = var10; var13 > 0; var13 += -64) {
                    ItemStack var88 = DragonGamesItems.extraHeart(Math.min(64, var13), 613905086);
                    var11.dropItemNaturally(var12, var88);
                }
            }

            Map var35 = this.hearts;
            var35.remove(var8);
            Map var38 = this.points;
            var38.remove(var8);
            Map var41 = this.trackers;
            var41.remove(var8);
            Component var72 = Text.legacy("&c" + var1.getName(), 373192400);
            var1.playerListName(var72);
            Team var90 = this.outTeam(187830077);
            this.teamJoin(var1, var90, 1706888627);
            this.resetBorder(var1, 1047999183);
            this.removeHeartsModifier(var1, 643390433);
            if (var2 != null) {
                byte var95 = 25;
                if (var9) {
                    Set var51 = this.participants;
                    UUID var77 = var2.getUniqueId();
                    if (var51.contains(var77)) {
                        var95 = 60;
                    }
                }

                Map<UUID, Integer> var54 = this.points;
                UUID var79 = var2.getUniqueId();
                Integer var92 = Integer.valueOf(var95);
                BiFunction<Integer, Integer, Integer> var94 = Integer::sum;
                var54.merge(var79, var92, var94);
                if (var9) {
                    String var81 = var1.getName();
                    Component var83 = Text.legacy("&c⚔ You killed " + var81 + " and gained &e$" + var95 + "!", 373192400);
                    var2.sendMessage(var83);
                }
            }

            if (var9 && this.participants.size() == 1) {
                this.endGame(1530310747);
            } else {
                this.save(2020232656);
            }
        }
    }

    public void consumeHeart(Player var1, int var2) {
        Map<UUID, Integer> var7 = this.hearts;
        UUID var11 = var1.getUniqueId();
        Integer var13 = 1;
        BiFunction<Integer, Integer, Integer> var6 = Integer::sum;
        var7.merge(var11, var13, var6);
        this.applyHeartsModifier(var1, 2081068606);
        this.save(2020232656);
    }

    private void applyHeartsModifier(Player var1, int var2) {
        Attribute var4 = Attribute.MAX_HEALTH;
        AttributeInstance var10 = var1.getAttribute(var4);
        if (var10 != null) {
            int var62;
            label50: {
                if (this.running) {
                    Set var22 = this.participants;
                    UUID var64 = var1.getUniqueId();
                    if (var22.contains(var64)) {
                        Map<UUID, Integer> var25 = this.hearts;
                        UUID var66 = var1.getUniqueId();
                        Integer var74 = 0;
                        var62 = var25.getOrDefault(var66, var74);
                        break label50;
                    }
                }

                var62 = 0;
            }

            double var12 = (double)((byte)var62) * 2.0;
            AttributeModifier var14 = null;

            for (AttributeModifier var16 : var10.getModifiers()) {
                NamespacedKey var43 = var16.getKey();
                NamespacedKey var68 = this.heartsModifierKey;
                if (var43.equals(var68)) {
                    var14 = var16;
                    break;
                }
            }

            if (var14 != null) {
                double var48 = var14.getAmount();
                if (var48 == var12) {
                    return;
                }
            }

            if (var14 != null) {
                var10.removeModifier(var14);
            }

            if (var12 > 0.0) {
                NamespacedKey var84 = this.heartsModifierKey;
                Operation var9 = Operation.ADD_NUMBER;
                AttributeModifier var70 = new AttributeModifier(var84, var12, var9);
                var10.addModifier(var70);
            }

            double var58 = var1.getHealth();
            double var82 = var10.getValue();
            if (var58 > var82) {
                double var72 = var10.getValue();
                var1.setHealth(var72);
            }
        }
    }

    private void removeHeartsModifier(Player var1, int var2) {
        Attribute var4 = Attribute.MAX_HEALTH;
        AttributeInstance var6 = var1.getAttribute(var4);
        if (var6 != null) {
            boolean var7 = false;
            Collection<AttributeModifier> var37 = var6.getModifiers();

            for (AttributeModifier var9 : new ArrayList<>(var37)) {
                NamespacedKey var27 = var9.getKey();
                NamespacedKey var35 = this.heartsModifierKey;
                if (var27.equals(var35)) {
                    var6.removeModifier(var9);
                    var7 = true;
                }
            }

            if (var7) {
                double var19 = var1.getHealth();
                double var39 = var6.getValue();
                if (var19 > var39) {
                    double var32 = var6.getValue();
                    var1.setHealth(var32);
                }
            }
        }
    }

    private void healthTick() {
        if (this.bloodMoon != null) {
            this.bloodMoon.tick(98339132);
        }

        if (this.leaderboard != null) {
            this.leaderboard.tick(403483024);
        }

        for (Player var6 : Bukkit.getOnlinePlayers()) {
            this.applyHeartsModifier(var6, 2081068606);
            if (this.running) {
                Set var28 = this.participants;
                UUID var33 = var6.getUniqueId();
                Team var32;
                if (var28.contains(var33)) {
                    var32 = this.team(1286635874);
                } else {
                    var32 = this.outTeam(187830077);
                }

                this.teamJoin(var6, var32, 1706888627);
            } else {
                this.teamClear(var6, 2016617065);
            }
        }
    }

    private static Team freshTeam(Scoreboard var0, String var1, NamedTextColor var2, ChatColor var3, int var4) {
        Team var8 = var0.getTeam(var1);
        if (var8 != null) {
            var8.unregister();
        }

        Team var13 = var0.registerNewTeam(var1);
        return configureTeam(var13, var2, var3, 76886972);
    }

    private static Team configureTeam(Team var0, NamedTextColor var1, ChatColor var2, int var3) {
        var0.color(var1);

        try {
            var0.setColor(var2);
        } catch (Throwable var13) {
        }

        var0.setAllowFriendlyFire(true);
        var0.setCanSeeFriendlyInvisibles(false);
        return var0;
    }

    private Team team(int var1) {
        Scoreboard var5 = Bukkit.getScoreboardManager().getMainScoreboard();
        Team var6 = var5.getTeam("DragonGames");
        Team var11;
        if (var6 != null) {
            var11 = var6;
        } else {
            Team var13 = var5.registerNewTeam("DragonGames");
            NamedTextColor var15 = NamedTextColor.GREEN;
            ChatColor var4 = ChatColor.GREEN;
            var11 = configureTeam(var13, var15, var4, 76886972);
        }

        return var11;
    }

    private Team outTeam(int var1) {
        Scoreboard var5 = Bukkit.getScoreboardManager().getMainScoreboard();
        Team var6 = var5.getTeam("DragonGamesOut");
        Team var13;
        if (var6 != null) {
            var13 = var6;
        } else {
            Team var12 = var5.registerNewTeam("DragonGamesOut");
            NamedTextColor var15 = NamedTextColor.RED;
            ChatColor var4 = ChatColor.RED;
            var13 = configureTeam(var12, var15, var4, 76886972);
        }

        return var13;
    }

    private void teamJoin(Player var1, Team var2, int var3) {
        String var23 = var1.getName();
        if (!var2.hasEntry(var23)) {
            Scoreboard var12 = Bukkit.getScoreboardManager().getMainScoreboard();
            String var25 = var1.getName();
            Team var7 = var12.getEntryTeam(var25);
            if (var7 != null) {
                String var8 = var7.getName();
                String var17 = "DragonGames";
                if (!var17.equals(var8)) {
                    String var19 = "DragonGamesOut";
                    if (!var19.equals(var8)) {
                        return;
                    }
                }

                String var29 = var1.getName();
                TeamManager.safeRemoveEntry(var7, var29, 1412413626);
            }

            String var31 = var1.getName();
            var2.addEntry(var31);
        }
    }

    private void teamClear(Player var1, int var2) {
        Scoreboard var5 = Bukkit.getScoreboardManager().getMainScoreboard();
        Team var8 = var5.getTeam("DragonGames");
        String var12 = var1.getName();
        TeamManager.safeRemoveEntry(var8, var12, 1412413626);
        Team var10 = var5.getTeam("DragonGamesOut");
        String var15 = var1.getName();
        TeamManager.safeRemoveEntry(var10, var15, 1412413626);
    }

    public void applyBorder(Player var1, int var2) {
        Location var6 = this.plugin.pedestalState(590646643).mainLoc(15453813);
        if (var6 == null) {
            World var7 = (World)Bukkit.getWorlds().get(0);
            var6 = var7.getSpawnLocation();
            if (!this.warnedNoPedestal) {
                this.warnedNoPedestal = true;
                Logger var22 = this.plugin.getLogger();
                String var32 = "Dragon Games: no pedestal is set — the border is centred on " + var7.getName() + "'s spawn instead. Use /bliss pedestal set.";
                var22.warning(var32);
            }
        }

        WorldBorder var44 = Bukkit.createWorldBorder();
        double var34 = var6.getX();
        double var42 = var6.getZ();
        var44.setCenter(var34, var42);
        var44.setSize(2000.0);
        var44.setWarningDistance(5);
        var44.setDamageBuffer(10.0);
        var1.setWorldBorder(var44);
    }

    public void resetBorder(Player var1, int var2) {
        var1.setWorldBorder(null);
    }

    public void startTracking(Player var1, UUID var2, int var3) {
        long var12 = System.currentTimeMillis() + 3600000L;
        Map var17 = this.trackers;
        UUID var21 = var1.getUniqueId();
        DragonGamesManager.Tracking var23 = new DragonGamesManager.Tracking(var2, var12);
        var17.put(var21, var23);
        Material var22 = Material.COMPASS;
        var1.setCooldown(var22, 72000);
        this.save(2020232656);
    }

    private void trackerTick() {
        if (!this.trackers.isEmpty()) {
            long var3 = System.currentTimeMillis();
            boolean var5 = false;
            Set<Entry<UUID, DragonGamesManager.Tracking>> var68 = this.trackers.entrySet();

            for (Entry<UUID, DragonGamesManager.Tracking> var7 : new ArrayList<>(var68)) {
                Player var8 = Bukkit.getPlayer(var7.getKey());
                DragonGamesManager.Tracking var9 = var7.getValue();
                long var70 = var9.expiry();
                if (var3 >= var70) {
                    if (var8 != null) {
                        consumeOneTracker(var8, 543176200);
                        aimTrackers(var8, null, 1165281861);
                        Map var51 = this.trackers;
                        Object var63 = var7.getKey();
                        var51.remove(var63);
                        var5 = true;
                        Component var65 = Text.legacy("&cYour Tracking Compass has run out.", 373192400);
                        var8.sendMessage(var65);
                    }
                } else if (var8 != null) {
                    Player var10;
                    boolean var55;
                    label48: {
                        var10 = Bukkit.getPlayer(var9.target());
                        if (var10 != null) {
                            World var42 = var10.getWorld();
                            World var57 = var8.getWorld();
                            if (var42.equals(var57)) {
                                var55 = true;
                                break label48;
                            }
                        }

                        var55 = false;
                    }

                    Location var66;
                    if (var55) {
                        var66 = var10.getLocation();
                    } else {
                        var66 = null;
                    }

                    aimTrackers(var8, var66, 1165281861);
                    if (var55) {
                        drawTrail(var8, var10, 1213494034);
                    }
                }
            }

            if (var5) {
                this.save(2020232656);
            }
        }
    }

    private static void aimTrackers(Player var0, Location var1, int var2) {
        PlayerInventory var6 = var0.getInventory();
        ItemStack[] var7 = var6.getContents();
        int var8 = 0;

        while (true) {
            int var22 = var7.length;
            if (var8 >= var22) {
                return;
            }

            ItemStack var9 = var7[var8];
            if (DragonGamesItems.isTracker(var9, 774563610) && aim(var9, var1, 1712174086)) {
                var6.setItem(var8, var9);
            }

            var8++;
        }
    }

    private static boolean aim(ItemStack var0, Location var1, int var2) {
        CompassMeta var7 = (CompassMeta)var0.getItemMeta();
        Location var8 = var7.getLodestone();
        if (var1 == null) {
            if (var8 == null) {
                return false;
            }

            var7.setLodestone(null);
        } else {
            if (var8 != null) {
                World var16 = var8.getWorld();
                World var30 = var1.getWorld();
                if (var16 == var30 && var8.distanceSquared(var1) < 0.01) {
                    return false;
                }
            }

            var7.setLodestoneTracked(false);
            var7.setLodestone(var1);
        }

        var0.setItemMeta(var7);
        return true;
    }

    private static void consumeOneTracker(Player var0, int var1) {
        PlayerInventory var5 = var0.getInventory();
        ItemStack[] var6 = var5.getContents();
        int var7 = 0;

        while (true) {
            int var23 = var6.length;
            if (var7 >= var23) {
                return;
            }

            ItemStack var8 = var6[var7];
            if (DragonGamesItems.isTracker(var8, 774563610)) {
                if (var8.getAmount() <= 1) {
                    var5.setItem(var7, null);
                } else {
                    int var29 = var8.getAmount() - 1;
                    var8.setAmount(var29);
                    var5.setItem(var7, var8);
                }

                return;
            }

            var7++;
        }
    }

    private static void drawTrail(Player var0, Player var1, int var2) {
        Location var21 = var0.getEyeLocation().subtract(0.0, 0.7, 0.0);
        Vector var30 = var1.getEyeLocation().toVector();
        Vector var53 = var0.getEyeLocation().toVector();
        Vector var22 = var30.subtract(var53);
        if (!(var22.lengthSquared() < 1.0E-6)) {
            var22.normalize().multiply(0.2);
            Location var66 = var1.getLocation();
            double var62 = var21.distance(var66) / 0.4;
            int var23 = (int)Math.min(16.0, var62);
            Location var24 = var21.clone();

            for (int var25 = 0; var25 < var23; var25++) {
                Particle var57 = Particle.DUST;
                DustOptions var20 = TRAIL_DUST;
                Particles.spawn(var0, var57, var24, 3, 0.0, 0.0, 0.0, 0.0, var20, 285216392);
                var24.add(var22);
            }
        }
    }

    static {
        Color var4 = Color.fromRGB(241, 3, 3);
        DustOptions var0 = new DustOptions(var4, 2.0F);
        TRAIL_DUST = var0;
    }

    public static record Tracking(UUID target, long expiry) {
    }
}
