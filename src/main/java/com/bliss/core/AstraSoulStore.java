package com.bliss.core;

import com.bliss.BlissPlugin;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import com.bliss.core.AtomicYml;
import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.runtime.ObjectMethods;
import java.util.logging.Logger;

public final class AstraSoulStore {
    public static final int MAX_SLOTS = 2;
    private static final EnumSet<EntityType> UNCAPTURABLE = EnumSet.of(EntityType.ENDER_DRAGON);
    private final BlissPlugin plugin;
    private final File file;
    private final Map<UUID, List<AstraSoulStore.Soul>> souls;

    public static boolean uncapturable(EntityType var0, int var1) {
        if (var0 != null) {
            EnumSet var4 = UNCAPTURABLE;
            if (var4.contains(var0)) {
                return true;
            }
        }

        return false;
    }

    public AstraSoulStore(BlissPlugin var1, int var2) {
        ConcurrentHashMap var4 = new ConcurrentHashMap();
        this.souls = var4;
        this.plugin = var1;
        File var12 = var1.getDataFolder();
        File var11 = new File(var12, "astra_souls.yml");
        this.file = var11;
    }

    public boolean canCapture(UUID var1, EntityType var2, int var3) {
        if (uncapturable(var2, 1427143893)) {
            return false;
        } else {
            Map<UUID, List<AstraSoulStore.Soul>> var9 = this.souls;
            List<AstraSoulStore.Soul> var6 = var9.get(var1);
            if (var6 == null) {
                return true;
            } else {
                Stream<AstraSoulStore.Soul> var15 = var6.stream();
                Predicate<AstraSoulStore.Soul> var23 = var1x -> {
                    EntityType var4 = var1x.type();
                    boolean var5;
                    if (var4 == var2) {
                        var5 = true;
                    } else {
                        var5 = false;
                    }

                    return var5;
                };
                if (var15.anyMatch(var23)) {
                    return false;
                } else {
                    boolean var21;
                    if (var6.size() < 2) {
                        var21 = true;
                    } else {
                        var21 = false;
                    }

                    return var21;
                }
            }
        }
    }

    public boolean capture(UUID var1, EntityType var2, double var3, int var5) {
        if (!this.canCapture(var1, var2, 1775291803)) {
            return false;
        } else {
            Map<UUID, List<AstraSoulStore.Soul>> var15 = this.souls;
            Function<UUID, List<AstraSoulStore.Soul>> var23 = var0 -> {
                ArrayList<AstraSoulStore.Soul> var1x = new ArrayList<>();
                return Collections.synchronizedList(var1x);
            };
            List<AstraSoulStore.Soul> var17 = var15.computeIfAbsent(var1, var23);
            AstraSoulStore.Soul var22 = new AstraSoulStore.Soul(var2, var3);
            var17.add(var22);
            this.save(1986509);
            return true;
        }
    }

    public AstraSoulStore.Soul popOldest(UUID var1, int var2) {
        Map var7 = this.souls;
        List var5 = (List)var7.get(var1);
        if (var5 != null && !var5.isEmpty()) {
            AstraSoulStore.Soul var6 = (AstraSoulStore.Soul)var5.remove(0);
            this.save(1986509);
            return var6;
        } else {
            return null;
        }
    }

    /*
     * Ignored method signature, as it can't be verified against descriptor
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public List snapshot(UUID uUID, int n) {
        List<Soul> list;
        Map<UUID, List<Soul>> map = this.souls;
        UUID uUID2 = uUID;
        List<Soul> list2 = map.get(uUID2);
        if (list2 == null) {
            return List.of();
        }
        List<Soul> list3 = list = list2;
        synchronized (list) {
            List<Soul> list4 = list2;
            return new ArrayList<Soul>(list4);
        }
    }

    public void load(int var1) {
        if (this.file.exists()) {
            ConfigurationSection var9 = YamlConfiguration.loadConfiguration(this.file).getConfigurationSection("souls");
            if (var9 != null) {
                for (String var11 : var9.getKeys(false)) {
                    UUID var12;
                    try {
                        var12 = UUID.fromString(var11);
                    } catch (IllegalArgumentException var86) {
                        continue;
                    }

                    List<Map<?, ?>> var13 = var9.getMapList(var11);
                    if (!var13.isEmpty()) {
                        ArrayList<AstraSoulStore.Soul> var45 = new ArrayList<>();
                        ArrayList<AstraSoulStore.Soul> var14 = var45;

                        for (Map<?, ?> var16 : var13) {
                            Object var17 = var16.get("type");
                            Object var18 = var16.get("max_health");
                            if (var17 != null) {
                                EntityType var19;
                                try {
                                    var19 = EntityType.valueOf(var17.toString());
                                } catch (IllegalArgumentException var87) {
                                    continue;
                                }

                                if (!uncapturable(var19, 1427143893)) {
                                    double var75;
                                    if (var18 instanceof Number) {
                                        var75 = ((Number)var18).doubleValue();
                                    } else {
                                        var75 = 20.0;
                                    }

                                    AstraSoulStore.Soul var84 = new AstraSoulStore.Soul(var19, var75);
                                    var14.add(var84);
                                }
                            }
                        }

                        if (!var14.isEmpty()) {
                            Map var53 = this.souls;
                            List var85 = Collections.synchronizedList(var14);
                            var53.put(var12, var85);
                        }
                    }
                }
            }
        }
    }
    public void save(int n) {
        YamlConfiguration yamlConfiguration;
        YamlConfiguration yamlConfiguration2 = yamlConfiguration = new YamlConfiguration();
        for (Map.Entry<UUID, List<Soul>> entry : this.souls.entrySet()) {
            List<Soul> list;
            ArrayList<Map<String, Object>> arrayList;
            if (entry.getValue().isEmpty()) continue;
            ArrayList<Map<String, Object>> arrayList2 = arrayList = new ArrayList<>();
            List<Soul> list2 = list = entry.getValue();
            // MONITORENTER : list
            for (Soul soul : entry.getValue()) {
                ArrayList<Map<String, Object>> arrayList3 = arrayList2;
                String string = "type";
                String string2 = soul.type().name();
                String string3 = "max_health";
                Double d = soul.maxHealth();
                Map<String, Object> map = Map.of(string, string2, string3, d);
                arrayList3.add(map);
            }
            // MONITOREXIT : list2
            YamlConfiguration yamlConfiguration3 = yamlConfiguration2;
            String string = "souls." + String.valueOf(entry.getKey());
            ArrayList<Map<String, Object>> arrayList4 = arrayList2;
            yamlConfiguration3.set(string, arrayList4);
        }
        try {
            if (!this.file.getParentFile().exists()) {
                this.file.getParentFile().mkdirs();
            }
            YamlConfiguration yamlConfiguration4 = yamlConfiguration2;
            File file = this.file;
            AtomicYml.save(yamlConfiguration4, file, 1347473142);
            return;
        }
        catch (IOException iOException) {
            IOException iOException2 = iOException;
            Logger logger = this.plugin.getLogger();
            String string = "Failed to save astra_souls.yml: " + iOException2.getMessage();
            logger.warning(string);
        }
    }

    public static record Soul(EntityType type, double maxHealth) {
    }
}
