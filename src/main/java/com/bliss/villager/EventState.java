package com.bliss.villager;

import com.bliss.BlissPlugin;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Villager;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

public final class EventState {
    private final BlissPlugin plugin;
    private final File file;
    private boolean active;
    private boolean finalActive;
    private int villagersReturned;
    private final Set<Integer> returnedVillagers;
    private double ringRot;
    private double auraRot;
    private int raidWave;
    private boolean raidBetween;
    private final Map<UUID, Double> raidDamage;
    private Location compassLoc;
    private final Map<Integer, UUID> villagerUuids;
    private final Map<Integer, Location> villagerLocs;
    private final Map<Integer, Boolean> villagerAlive;
    private final Set<Integer> villagerEverKilled;
    private final Map<Integer, String> soulHolder;
    private final Map<Integer, Location> soulLastPos;
    private final Set<UUID> soulDroppedLock;
    private final Map<Integer, Set<String>> tradeBought;
    private final Map<UUID, Integer> tradingWith;

    public EventState(BlissPlugin var1, int var2) {
        HashSet var4 = new HashSet();
        this.returnedVillagers = var4;
        HashMap var20 = new HashMap();
        this.raidDamage = var20;
        HashMap var21 = new HashMap();
        this.villagerUuids = var21;
        HashMap var22 = new HashMap();
        this.villagerLocs = var22;
        HashMap var23 = new HashMap();
        this.villagerAlive = var23;
        HashSet var24 = new HashSet();
        this.villagerEverKilled = var24;
        HashMap var25 = new HashMap();
        this.soulHolder = var25;
        HashMap var26 = new HashMap();
        this.soulLastPos = var26;
        HashSet var27 = new HashSet();
        this.soulDroppedLock = var27;
        HashMap var28 = new HashMap();
        this.tradeBought = var28;
        HashMap var29 = new HashMap();
        this.tradingWith = var29;
        this.plugin = var1;
        File var32 = var1.getDataFolder();
        File var31 = new File(var32, "villager-event.yml");
        this.file = var31;
    }

    public boolean isActive(int var1) {
        return this.active;
    }

    public void setActive(boolean var1, int var2) {
        this.active = var1;
    }

    public boolean isFinalActive(int var1) {
        return this.finalActive;
    }

    public void setFinalActive(boolean var1, int var2) {
        this.finalActive = var1;
    }

    public boolean isEventRunning(int var1) {
        boolean var6;
        if (!this.active && !this.finalActive) {
            var6 = false;
        } else {
            var6 = true;
        }

        return var6;
    }

    public int villagersReturned(int var1) {
        return this.villagersReturned;
    }

    public void setVillagersReturned(int var1, int var2) {
        this.villagersReturned = var1;
    }

    public void incrementVillagersReturned(int var1) {
        int var5 = this.villagersReturned + 1;
        this.villagersReturned = var5;
    }

    public boolean isVillagerReturned(int var1, int var2) {
        Set var5 = this.returnedVillagers;
        Integer var7 = var1;
        return var5.contains(var7);
    }

    public void markVillagerReturned(int var1, int var2) {
        Set var5 = this.returnedVillagers;
        Integer var8 = var1;
        var5.add(var8);
        this.save(1112893273);
    }

    public void clearReturnedVillagers(int var1) {
        this.returnedVillagers.clear();
        this.save(1112893273);
    }

    public int raidWave(int var1) {
        return this.raidWave;
    }

    public void setRaidWave(int var1, int var2) {
        this.raidWave = var1;
    }

    public boolean raidBetween(int var1) {
        return this.raidBetween;
    }

    public void setRaidBetween(boolean var1, int var2) {
        this.raidBetween = var1;
    }

    public void addRaidDamage(UUID var1, double var2, int var4) {
        if (var1 != null && !(var2 <= 0.0)) {
            this.raidDamage.merge(var1, var2, Double::sum);
        }
    }

    public UUID topRaidDamager(int var1) {
        UUID var5 = null;
        double var6 = 0.0;

        for (Entry var9 : this.raidDamage.entrySet()) {
            double var25 = (Double)var9.getValue();
            if (var25 > var6) {
                var6 = (Double)var9.getValue();
                var5 = (UUID)var9.getKey();
            }
        }

        return var5;
    }

    public double ringRot(int var1) {
        return this.ringRot;
    }

    public void addRingRot(double var1, int var3) {
        double var8 = this.ringRot;
        double var12 = (var8 + var1) % 360.0;
        this.ringRot = var12;
    }

    public double auraRot() {
        return this.auraRot;
    }

    public void addAuraRot(double var1, int var3) {
        double var8 = this.auraRot;
        double var12 = (var8 + var1) % 360.0;
        this.auraRot = var12;
    }

    public Location compassLoc(int var1) {
        return this.compassLoc;
    }

    public void setCompassLoc(Location var1, int var2) {
        Location var6;
        if (var1 == null) {
            var6 = null;
        } else {
            var6 = var1.clone();
        }

        this.compassLoc = var6;
    }

    public UUID villagerUuid(int var1, int var2) {
        Map var5 = this.villagerUuids;
        Integer var8 = var1;
        return (UUID)var5.get(var8);
    }

    public void setVillager(int var1, Villager var2, int var3) {
        Map var7 = this.villagerUuids;
        Integer var15 = var1;
        UUID var20 = var2.getUniqueId();
        var7.put(var15, var20);
        Map var10 = this.villagerLocs;
        Integer var17 = var1;
        Location var23 = var2.getLocation().clone();
        var10.put(var17, var23);
        Map var13 = this.villagerAlive;
        Integer var19 = var1;
        Boolean var25 = true;
        var13.put(var19, var25);
    }

    public void clearVillager(int var1, int var2) {
        Map var5 = this.villagerUuids;
        Integer var7 = var1;
        var5.remove(var7);
    }

    public Villager getVillager(int var1, int var2) {
        UUID var5 = this.villagerUuids.get(var1);
        if (var5 == null) {
            return null;
        }
        return Bukkit.getEntity(var5) instanceof Villager var19 ? var19 : null;
    }

    public String soulHolder(int var1, int var2) {
        return this.soulHolder.get(var1);
    }

    public Location villagerLoc(int var1, int var2) {
        Map var5 = this.villagerLocs;
        Integer var8 = var1;
        return (Location)var5.get(var8);
    }

    public void setVillagerLoc(int var1, Location var2, int var3) {
        Map var7 = this.villagerLocs;
        Integer var9 = var1;
        Location var11;
        if (var2 == null) {
            var11 = null;
        } else {
            var11 = var2.clone();
        }

        var7.put(var9, var11);
    }

    public boolean isVillagerAlive(int var1, int var2) {
        return this.villagerAlive.getOrDefault(var1, false);
    }

    public void setVillagerAlive(int var1, boolean var2, int var3) {
        Map var7 = this.villagerAlive;
        Integer var9 = var1;
        Boolean var10 = var2;
        var7.put(var9, var10);
    }

    public boolean wasVillagerEverKilled(int var1, int var2) {
        Set var5 = this.villagerEverKilled;
        Integer var7 = var1;
        return var5.contains(var7);
    }

    public void markVillagerKilled(int var1, int var2) {
        Set var5 = this.villagerEverKilled;
        Integer var7 = var1;
        var5.add(var7);
    }

    public void setSoulHolder(int var1, String var2, int var3) {
        Map var7 = this.soulHolder;
        Integer var9 = var1;
        var7.put(var9, var2);
    }

    public void clearSoulHolder(int var1, int var2) {
        Map var5 = this.soulHolder;
        Integer var7 = var1;
        var5.remove(var7);
    }

    public Location soulLastPos(int var1, int var2) {
        Map var5 = this.soulLastPos;
        Integer var8 = var1;
        return (Location)var5.get(var8);
    }

    public void setSoulLastPos(int var1, Location var2, int var3) {
        Map var7 = this.soulLastPos;
        Integer var9 = var1;
        Location var10;
        if (var2 == null) {
            var10 = null;
        } else {
            var10 = var2.clone();
        }

        var7.put(var9, var10);
    }

    public void clearSoulLastPos(int var1, int var2) {
        Map var5 = this.soulLastPos;
        Integer var7 = var1;
        var5.remove(var7);
    }

    public boolean isSoulDropped(UUID var1, int var2) {
        Set var5 = this.soulDroppedLock;
        return var5.contains(var1);
    }

    public void lockSoulDrop(UUID var1, int var2) {
        Set var5 = this.soulDroppedLock;
        var5.add(var1);
    }

    public void unlockSoulDrop(UUID var1, int var2) {
        Set var5 = this.soulDroppedLock;
        var5.remove(var1);
    }

    public boolean isTradeBought(int var1, String var2, int var3) {
        Map var7 = this.tradeBought;
        Integer var14 = var1;
        Set var6 = (Set)var7.get(var14);
        boolean var13;
        if (var6 != null && var6.contains(var2)) {
            var13 = true;
        } else {
            var13 = false;
        }

        return var13;
    }

    public void setTradeBought(int var1, String var2, int var3) {
        Map var7 = this.tradeBought;
        Integer var11 = var1;
        Function var6 = var0 -> new HashSet();
        Set var9 = (Set)var7.computeIfAbsent(var11, var6);
        var9.add(var2);
    }

    public void clearTradeBought(int var1, int var2) {
        Map var5 = this.tradeBought;
        Integer var7 = var1;
        var5.remove(var7);
    }

    public boolean isTradeBoughtAny(int var1, int var2) {
        Map var6 = this.tradeBought;
        Integer var13 = var1;
        Set var5 = (Set)var6.get(var13);
        boolean var12;
        if (var5 != null && !var5.isEmpty()) {
            var12 = true;
        } else {
            var12 = false;
        }

        return var12;
    }

    public Integer tradingWith(UUID var1, int var2) {
        Map var5 = this.tradingWith;
        return (Integer)var5.get(var1);
    }

    public void setTradingWith(UUID var1, int var2, int var3) {
        Map var7 = this.tradingWith;
        Integer var9 = var2;
        var7.put(var1, var9);
    }

    public void clearTradingWith(UUID var1, int var2) {
        Map var5 = this.tradingWith;
        var5.remove(var1);
    }

    public void endEvent(int var1) {
        this.active = false;
        this.finalActive = false;
        this.villagersReturned = 0;
        this.returnedVillagers.clear();
        this.ringRot = 0.0;
        this.auraRot = 0.0;
        this.raidWave = 0;
        this.raidBetween = false;
        this.raidDamage.clear();
        this.soulHolder.clear();
        this.soulLastPos.clear();
        this.soulDroppedLock.clear();
        this.tradingWith.clear();

        for (int var4 = 1; var4 <= 3; var4++) {
            Villager var5 = this.getVillager(var4, 1571302102);
            if (var5 != null && this.isVillagerAlive(var4, 2034788216)) {
                var5.remove();
            }

            Map var33 = this.villagerUuids;
            Integer var60 = var4;
            var33.remove(var60);
            Map var36 = this.villagerLocs;
            Integer var62 = var4;
            var36.remove(var62);
            Map var39 = this.villagerAlive;
            Integer var64 = var4;
            var39.remove(var64);
            Set var42 = this.villagerEverKilled;
            Integer var66 = var4;
            var42.remove(var66);
            Map var45 = this.tradeBought;
            Integer var68 = var4;
            var45.remove(var68);
        }

        this.save(1112893273);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void load(int n) {
        ConfigurationSection configurationSection;
        ConfigurationSection configurationSection2;
        ConfigurationSection configurationSection3;
        YamlConfiguration yamlConfiguration;
        block27: {
            if (!this.file.exists()) {
                return;
            }
            try {
                boolean bl;
                int n2;
                int n3;
                boolean bl2;
                boolean bl3;
                yamlConfiguration = YamlConfiguration.loadConfiguration((File)this.file);
                EventState eventState = this;
                YamlConfiguration yamlConfiguration2 = yamlConfiguration;
                String string = "active";
                eventState.active = bl3 = yamlConfiguration2.getBoolean(string, false);
                EventState eventState2 = this;
                YamlConfiguration yamlConfiguration3 = yamlConfiguration;
                String string2 = "finalActive";
                eventState2.finalActive = bl2 = yamlConfiguration3.getBoolean(string2, false);
                EventState eventState3 = this;
                YamlConfiguration yamlConfiguration4 = yamlConfiguration;
                String string3 = "villagersReturned";
                eventState3.villagersReturned = n3 = yamlConfiguration4.getInt(string3, 0);
                Set<Integer> set = this.returnedVillagers;
                List list = yamlConfiguration.getIntegerList("returnedVillagers");
                set.addAll(list);
                EventState eventState4 = this;
                YamlConfiguration yamlConfiguration5 = yamlConfiguration;
                String string4 = "raidWave";
                eventState4.raidWave = n2 = yamlConfiguration5.getInt(string4, 0);
                EventState eventState5 = this;
                YamlConfiguration yamlConfiguration6 = yamlConfiguration;
                String string5 = "raidBetween";
                eventState5.raidBetween = bl = yamlConfiguration6.getBoolean(string5, false);
                ConfigurationSection configurationSection4 = yamlConfiguration.getConfigurationSection("raidDamage");
                if (configurationSection4 == null) break block27;
                for (String string6 : configurationSection4.getKeys(false)) {
                    try {
                        Map<UUID, Double> map = this.raidDamage;
                        UUID uUID = UUID.fromString(string6);
                        ConfigurationSection configurationSection5 = configurationSection4;
                        String string7 = string6;
                        Double d = configurationSection5.getDouble(string7);
                        map.put(uUID, d);
                    }
                    catch (RuntimeException runtimeException) {}
                }
            }
            catch (Exception exception) {
                Logger logger = this.plugin.getLogger();
                String string = "villager-event.yml failed to load cleanly: " + exception.getMessage();
                logger.warning(string);
                return;
            }
        }
        if (yamlConfiguration.contains("compass")) {
            try {
                Location location;
                EventState eventState = this;
                eventState.compassLoc = location = yamlConfiguration.getLocation("compass");
            }
            catch (RuntimeException runtimeException) {}
            this.compassLoc = null;
        }
        ConfigurationSection configurationSection6 = yamlConfiguration.getConfigurationSection("villagers");
        if (configurationSection6 != null) {
            for (String string : configurationSection6.getKeys(false)) {
                try {
                    int n4 = Integer.parseInt(string);
                    ConfigurationSection configurationSection7 = configurationSection6;
                    String string8 = string + ".uuid";
                    String string9 = configurationSection7.getString(string8);
                    if (string9 != null) {
                        Map<Integer, UUID> map = this.villagerUuids;
                        Integer n5 = n4;
                        UUID uUID = UUID.fromString(string9);
                        map.put(n5, uUID);
                    }
                    ConfigurationSection configurationSection8 = configurationSection6;
                    String string10 = string + ".loc";
                    if (configurationSection8.contains(string10)) {
                        Map<Integer, Location> map = this.villagerLocs;
                        Integer n6 = n4;
                        ConfigurationSection configurationSection9 = configurationSection6;
                        String string11 = string + ".loc";
                        Location location = configurationSection9.getLocation(string11);
                        map.put(n6, location);
                    }
                    Map<Integer, Boolean> map = this.villagerAlive;
                    Integer n7 = n4;
                    ConfigurationSection configurationSection10 = configurationSection6;
                    String string12 = string + ".alive";
                    Boolean bl = configurationSection10.getBoolean(string12, false);
                    map.put(n7, bl);
                    ConfigurationSection configurationSection11 = configurationSection6;
                    String string13 = string + ".everKilled";
                    if (!configurationSection11.getBoolean(string13, false)) continue;
                    Set<Integer> set = this.villagerEverKilled;
                    Integer n8 = n4;
                    set.add(n8);
                }
                catch (RuntimeException runtimeException) {}
            }
        }
        if ((configurationSection3 = yamlConfiguration.getConfigurationSection("soulHolder")) != null) {
            for (String string : configurationSection3.getKeys(false)) {
                try {
                    Map<Integer, String> map = this.soulHolder;
                    Integer n9 = Integer.parseInt(string);
                    ConfigurationSection configurationSection12 = configurationSection3;
                    String string14 = string;
                    String string15 = configurationSection12.getString(string14);
                    map.put(n9, string15);
                }
                catch (RuntimeException runtimeException) {}
            }
        }
        if ((configurationSection2 = yamlConfiguration.getConfigurationSection("soulLastPos")) != null) {
            for (String string : configurationSection2.getKeys(false)) {
                try {
                    Map<Integer, Location> map = this.soulLastPos;
                    Integer n10 = Integer.parseInt(string);
                    ConfigurationSection configurationSection13 = configurationSection2;
                    String string16 = string;
                    Location location = configurationSection13.getLocation(string16);
                    map.put(n10, location);
                }
                catch (RuntimeException runtimeException) {}
            }
        }
        if ((configurationSection = yamlConfiguration.getConfigurationSection("tradeBought")) == null) return;
        for (String string : configurationSection.getKeys(false)) {
            try {
                HashSet hashSet;
                int n11 = Integer.parseInt(string);
                ConfigurationSection configurationSection14 = configurationSection;
                String string17 = string;
                List list = configurationSection14.getStringList(string17);
                HashSet hashSet2 = hashSet = new HashSet(list);
                Map<Integer, Set<String>> map = this.tradeBought;
                Integer n12 = n11;
                HashSet hashSet3 = hashSet2;
                map.put(n12, hashSet3);
            }
            catch (RuntimeException runtimeException) {}
        }
    }

    // $VF: Handled exception range with multiple entry points by splitting it
    // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
    public void save(int var1) {
        YamlConfiguration var2 = new YamlConfiguration();
        YamlConfiguration var5 = var2;
        String var109 = "active";
        Boolean var152 = this.active;
        var2.set(var109, var152);
        String var110 = "finalActive";
        Boolean var155 = this.finalActive;
        var2.set(var110, var155);
        String var111 = "villagersReturned";
        Integer var158 = this.villagersReturned;
        var2.set(var111, var158);
        String var112 = "returnedVillagers";
        Set var185 = this.returnedVillagers;
        ArrayList var159 = new ArrayList(var185);
        var2.set(var112, var159);
        String var113 = "raidWave";
        Integer var162 = this.raidWave;
        var2.set(var113, var162);
        String var114 = "raidBetween";
        Boolean var165 = this.raidBetween;
        var2.set(var114, var165);

        for (Entry var203 : this.raidDamage.entrySet()) {
            String var150 = "raidDamage." + var203.getKey();
            Object var184 = var203.getValue();
            var5.set(var150, var184);
        }

        if (this.compassLoc != null) {
            String var115 = "compass";
            Location var167 = this.compassLoc;
            var5.set(var115, var167);
        }

        for (Entry var202 : this.villagerUuids.entrySet()) {
            String var146 = "villagers." + var202.getKey() + ".uuid";
            String var182 = ((UUID)var202.getValue()).toString();
            var5.set(var146, var182);
        }

        for (Entry var7 : this.villagerLocs.entrySet()) {
            String var119 = "villagers." + var7.getKey() + ".loc";
            Object var169 = var7.getValue();
            var5.set(var119, var169);
        }

        for (Entry var201 : this.villagerAlive.entrySet()) {
            String var142 = "villagers." + var201.getKey() + ".alive";
            Object var178 = var201.getValue();
            var5.set(var142, var178);
        }

        for (Integer var200 : this.villagerEverKilled) {
            String var138 = "villagers." + var200 + ".everKilled";
            Boolean var176 = true;
            var5.set(var138, var176);
        }

        for (Entry var199 : this.soulHolder.entrySet()) {
            String var136 = "soulHolder." + var199.getKey();
            Object var174 = var199.getValue();
            var5.set(var136, var174);
        }

        for (Entry var198 : this.soulLastPos.entrySet()) {
            String var132 = "soulLastPos." + var198.getKey();
            Object var172 = var198.getValue();
            var5.set(var132, var172);
        }

        for (Entry var197 : this.tradeBought.entrySet()) {
            String var123 = "tradeBought." + var197.getKey();
            Collection var188 = (Collection)var197.getValue();
            ArrayList var170 = new ArrayList(var188);
            var5.set(var123, var170);
        }

        try {
            if (!this.file.getParentFile().exists()) {
                this.file.getParentFile().mkdirs();
            }
            File var125 = this.file;
            var5.save(var125);
        } catch (IOException var196) {
            Logger var84 = this.plugin.getLogger();
            String var128 = "Failed to save villager-event.yml: " + var196.getMessage();
            var84.warning(var128);
        }
    }
}
