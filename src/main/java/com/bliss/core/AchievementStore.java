package com.bliss.core;

import com.bliss.BlissPlugin;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Logger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public final class AchievementStore {
    private final BlissPlugin plugin;
    private final File file;
    private final Map<UUID, Set<String>> unlocked;
    private final Map<UUID, Map<String, Long>> counters;
    private final Map<UUID, Map<String, Map<UUID, Integer>>> targetCounters;
    private BlissAdvancements tab;

    public AchievementStore(BlissPlugin var1, int var2) {
        ConcurrentHashMap var4 = new ConcurrentHashMap();
        this.unlocked = var4;
        ConcurrentHashMap var12 = new ConcurrentHashMap();
        this.counters = var12;
        ConcurrentHashMap var13 = new ConcurrentHashMap();
        this.targetCounters = var13;
        this.plugin = var1;
        File var16 = var1.getDataFolder();
        File var15 = new File(var16, "advancements.yml");
        this.file = var15;
    }

    public void setTab(BlissAdvancements var1, int var2) {
        this.tab = var1;
    }

    public boolean isUnlocked(UUID var1, AchievementStore.Advancement var2, int var3) {
        Map var7 = this.unlocked;
        Set var6 = (Set)var7.get(var1);
        if (var6 != null) {
            String var15 = var2.id;
            if (var6.contains(var15)) {
                return true;
            }
        }

        return false;
    }

    public void unlock(Player var1, AchievementStore.Advancement var2, int var3) {
        if (!this.alreadyHas(var1, var2, 485291765)) {
            Map var10 = this.unlocked;
            UUID var21 = var1.getUniqueId();
            Function var27 = var0 -> ConcurrentHashMap.newKeySet();
            Set var13 = (Set)var10.computeIfAbsent(var21, var27);
            String var23 = var2.id;
            if (var13.add(var23)) {
                String var25 = var1.getName();
                this.broadcast(var25, var2, 365912590);
                if (this.tab != null) {
                    BlissAdvancements var19 = this.tab;
                    String var30 = var2.id;
                    var19.grant(var1, var30, 672121049);
                }
            }
        }
    }

    private boolean alreadyHas(Player var1, AchievementStore.Advancement var2, int var3) {
        UUID var48 = var1.getUniqueId();
        if (!this.isUnlocked(var48, var2, 218116362)) {
            return false;
        } else if (this.tab == null) {
            return true;
        } else {
            BlissAdvancements var16 = this.tab;
            String var60 = var2.id;
            Boolean var7 = var16.isGranted(var1, var60, 1791788570);
            if (var7 != null && !var7) {
                UUID var8 = var1.getUniqueId();
                Map var24 = this.unlocked;
                Set var9 = (Set)var24.get(var8);
                if (var9 != null) {
                    String var52 = var2.id;
                    var9.remove(var52);
                }

                Map var31 = this.counters;
                Map var10 = (Map)var31.get(var8);
                if (var10 != null) {
                    String var55 = var2.id;
                    var10.remove(var55);
                }

                Map var38 = this.targetCounters;
                Map var11 = (Map)var38.get(var8);
                if (var11 != null) {
                    String var58 = var2.id;
                    var11.remove(var58);
                }

                return false;
            } else {
                return true;
            }
        }
    }

    public void progress(Player var1, AchievementStore.Advancement var2, long var3, int var5) {
        UUID var10 = var1.getUniqueId();
        if (!this.alreadyHas(var1, var2, 485291765)) {
            Map<UUID, Map<String, Long>> var17 = this.counters;
            Function<UUID, Map<String, Long>> var32 = var0 -> new ConcurrentHashMap<>();
            Map<String, Long> var19 = var17.computeIfAbsent(var10, var32);
            String var30 = var2.id;
            Long var35 = var3;
            BiFunction<Long, Long, Long> var9 = Long::sum;
            long var24 = var19.merge(var30, var35, var9);
            long var37 = var2.threshold;
            if (var24 >= var37) {
                this.unlock(var1, var2, 1132982324);
            }
        }
    }

    public void targetProgress(Player var1, AchievementStore.Advancement var2, UUID var3, int var4, int var5) {
        UUID var10 = var1.getUniqueId();
        if (!this.alreadyHas(var1, var2, 485291765)) {
            Map<UUID, Map<String, Map<UUID, Integer>>> var17 = this.targetCounters;
            Function<UUID, Map<String, Map<UUID, Integer>>> var36 = var0 -> new ConcurrentHashMap<>();
            Map<String, Map<UUID, Integer>> var19 = var17.computeIfAbsent(var10, var36);
            String var33 = var2.id;
            Function<String, Map<UUID, Integer>> var37 = var0 -> new ConcurrentHashMap<>();
            Map<UUID, Integer> var22 = var19.computeIfAbsent(var33, var37);
            Integer var39 = var4;
            BiFunction<Integer, Integer, Integer> var9 = Integer::sum;
            long var27 = (long)var22.merge(var3, var39, var9).intValue();
            long var41 = var2.threshold;
            if (var27 >= var41) {
                this.unlock(var1, var2, 1132982324);
            }
        }
    }

    public void resetTargetCountersExcept(Player var1, AchievementStore.Advancement var2, UUID var3, int var4) {
        Map var9 = this.targetCounters;
        UUID var20 = var1.getUniqueId();
        Map var7 = (Map)var9.get(var20);
        if (var7 != null) {
            String var22 = var2.id;
            Map<UUID, Integer> var8 = (Map<UUID, Integer>)var7.get(var22);
            if (var8 != null) {
                Set<Entry<UUID, Integer>> var18 = var8.entrySet();
                Predicate<Entry<UUID, Integer>> var24 = var1x -> {
                    UUID var5 = var1x.getKey();
                    boolean var7x;
                    if (!var5.equals(var3)) {
                        var7x = true;
                    } else {
                        var7x = false;
                    }

                    return var7x;
                };
                var18.removeIf(var24);
            }
        }
    }

    private void broadcast(String var1, AchievementStore.Advancement var2, int var3) {
        NamedTextColor var54;
        if (var2.challenge) {
            var54 = NamedTextColor.DARK_PURPLE;
        } else {
            var54 = NamedTextColor.GREEN;
        }

        TextComponent var19 = Component.text(var2.display);
        TextComponent var21 = (TextComponent)var19.color(var54);
        TextDecoration var55 = TextDecoration.BOLD;
        TextComponent var23 = (TextComponent)var21.decorate(var55);
        TextComponent var56 = Component.newline();
        TextComponent var25 = (TextComponent)var23.append(var56);
        TextComponent var59 = Component.text(var2.description);
        NamedTextColor var6 = NamedTextColor.GRAY;
        TextComponent var61 = (TextComponent)var59.color(var6);
        TextDecoration var72 = TextDecoration.BOLD;
        Component var62 = var61.decoration(var72, false);
        Component var10 = var25.append(var62);
        TextComponent var30 = Component.text("[" + var2.display + "]");
        TextComponent var32 = (TextComponent)var30.color(var54);
        HoverEvent var65 = HoverEvent.showText(var10);
        Component var11 = var32.hoverEvent(var65);
        String var36;
        if (var2.challenge) {
            var36 = " has completed the challenge ";
        } else {
            var36 = " has made the advancement ";
        }

        TextComponent var39 = Component.text(var1 + var36);
        NamedTextColor var67 = NamedTextColor.WHITE;
        TextComponent var41 = (TextComponent)var39.color(var67);
        Component var13 = var41.append(var11);

        for (Player var15 : Bukkit.getOnlinePlayers()) {
            var15.sendMessage(var13);
            if (var2.challenge) {
                Location var71 = var15.getLocation();
                Sound var73 = Sound.UI_TOAST_CHALLENGE_COMPLETE;
                var15.playSound(var71, var73, 1.0F, 1.0F);
            }
        }
    }

    public void load(int var1) {
        if (this.file.exists()) {
            ConfigurationSection var7 = YamlConfiguration.loadConfiguration(this.file).getConfigurationSection("advancements");
            if (var7 != null) {
                for (String var9 : var7.getKeys(false)) {
                    UUID var10;
                    try {
                        var10 = UUID.fromString(var9);
                    } catch (IllegalArgumentException var142) {
                        continue;
                    }

                    String var108 = var9 + ".unlocked";
                    List var11 = var7.getStringList(var108);
                    if (!var11.isEmpty()) {
                        KeySetView var12 = ConcurrentHashMap.newKeySet();
                        var12.addAll(var11);
                        Map var48 = this.unlocked;
                        var48.put(var10, var12);
                    }

                    String var112 = var9 + ".counters";
                    ConfigurationSection var137 = var7.getConfigurationSection(var112);
                    if (var137 != null) {
                        ConcurrentHashMap var53 = new ConcurrentHashMap();
                        ConcurrentHashMap var13 = var53;

                        for (String var15 : var137.getKeys(false)) {
                            Long var128 = var137.getLong(var15);
                            var13.put(var15, var128);
                        }

                        if (!var13.isEmpty()) {
                            Map var67 = this.counters;
                            var67.put(var10, var13);
                        }
                    }

                    String var117 = var9 + ".target_counters";
                    ConfigurationSection var138 = var7.getConfigurationSection(var117);
                    if (var138 != null) {
                        ConcurrentHashMap var72 = new ConcurrentHashMap();
                        ConcurrentHashMap var139 = var72;

                        for (String var16 : var138.getKeys(false)) {
                            ConfigurationSection var17 = var138.getConfigurationSection(var16);
                            if (var17 != null) {
                                ConcurrentHashMap var84 = new ConcurrentHashMap();
                                ConcurrentHashMap var18 = var84;

                                for (String var20 : var17.getKeys(false)) {
                                    try {
                                        UUID var122 = UUID.fromString(var20);
                                        Integer var132 = var17.getInt(var20);
                                        var18.put(var122, var132);
                                    } catch (IllegalArgumentException var141) {
                                    }
                                }

                                if (!var18.isEmpty()) {
                                    var139.put(var16, var18);
                                }
                            }
                        }

                        if (!var139.isEmpty()) {
                            Map var103 = this.targetCounters;
                            var103.put(var10, var139);
                        }
                    }
                }
            }
        }
    }

    // $VF: Handled exception range with multiple entry points by splitting it
    // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
    public void save(int var1) {
        YamlConfiguration var2 = new YamlConfiguration();
        YamlConfiguration var5 = var2;
        HashSet<UUID> var17 = new HashSet<>();
        Set<UUID> var95 = this.unlocked.keySet();
        var17.addAll(var95);
        Set var98 = this.counters.keySet();
        var17.addAll(var98);
        Set var101 = this.targetCounters.keySet();
        var17.addAll(var101);

        for (UUID var8 : var17) {
            String var9 = "advancements." + var8;
            Map var48 = this.unlocked;
            Set var10 = (Set)var48.get(var8);
            if (var10 != null && !var10.isEmpty()) {
                String var109 = var9 + ".unlocked";
                ArrayList var3 = new ArrayList(var10);
                var5.set(var109, var3);
            }

            Map<UUID, Map<String, Long>> var56 = this.counters;
            Map<String, Long> var11 = var56.get(var8);
            if (var11 != null) {
                for (Entry<String, Long> var13 : var11.entrySet()) {
                    String var118 = var13.getKey();
                    String var112 = var9 + ".counters." + var118;
                    Object var120 = var13.getValue();
                    var5.set(var112, var120);
                }
            }

            Map<UUID, Map<String, Map<UUID, Integer>>> var70 = this.targetCounters;
            Map<String, Map<UUID, Integer>> var130 = var70.get(var8);
            if (var130 != null) {
                for (Entry<String, Map<UUID, Integer>> var14 : var130.entrySet()) {
                    for (Entry<UUID, Integer> var16 : var14.getValue().entrySet()) {
                        String var123 = (String)var14.getKey();
                        String var128 = String.valueOf(var16.getKey());
                        String var115 = var9 + ".target_counters." + var123 + "." + var128;
                        Object var125 = var16.getValue();
                        var5.set(var115, var125);
                    }
                }
            }
        }

        try {
            if (!this.file.getParentFile().exists()) {
                this.file.getParentFile().mkdirs();
            }
            File var103 = this.file;
            AtomicYml.save(var5, var103, 1347473142);
        } catch (IOException var129) {
            Logger var40 = this.plugin.getLogger();
            String var106 = "Failed to save advancements.yml: " + var129.getMessage();
            var40.warning(var106);
        }
    }

    public static enum Advancement {
        SAVED_BY_DICE("saved_by_dice", "Saved By The Dice", "Avoid a fatal blow with Phasing", 1L, true),
        ALMOST_FELL("almost_fell_from_grace", "Almost Fell From Grace", "Prevent 30,000 points of fall damage with Puff Gem", 30000L, false),
        GOOD_AS_NEW("good_as_new", "Good As New!", "Draw 10 energy from the Energy Pool using a Repair Kit", 10L, false),
        REAWAKENING("reawakening", "Reawakening", "Successfully complete a Restoration Ritual", 1L, true),
        RABBIT_SEASON("rabbit_season", "It's Rabbit Season", "Track the same player with Shadow Stalker 17 times", 17L, true),
        TIME_FOR_A_CHANGE("time_for_a_change", "Time For A Change", "Use a Trader to switch gem types", 1L, false),
        THE_NEXT_LEVEL("the_next_level", "The Next Level", "Use an Upgrader to obtain a Tier 2 gem", 1L, true),
        OVERCHARGED("overcharged", "Overcharged", "Push a Flux Gem past 100% charge", 1L, true),
        BOUNDARY_BREAK("boundary_break", "Boundary Break", "Boost enchantments beyond vanilla levels using Amplification", 1L, false),
        SHATTERED("shattered", "Shattered", "Reach the Broken energy stage", 1L, true),
        ZIP_AWAY("zip_away", "Zip Away", "Travel across 1,914 copper blocks using Conduction", 1914L, false),
        PIERCING_PRECISION("piercing_precision", "Piercing Precision", "Hit a player with 5 consecutive Phantom Daggers", 5L, false),
        OVERFLOWING("overflowing", "Overflowing", "Reach the Pristine +5 energy stage", 1L, true);

        public final String id;
        public final String display;
        public final String description;
        public final long threshold;
        public final boolean challenge;

        private Advancement(String nullxx, String nullxxx, String nullxxxx, long nullxxxxx, boolean nullxxxxxx) {
            this.id = nullxx;
            this.display = nullxxx;
            this.description = nullxxxx;
            this.threshold = nullxxxxx;
            this.challenge = nullxxxxxx;
        }
    }
}
