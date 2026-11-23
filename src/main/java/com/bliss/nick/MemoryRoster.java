package com.bliss.nick;

import com.bliss.BlissPlugin;
import com.bliss.core.AtomicYml;
import com.bliss.goldgem.GoldenDreamWorld;
import com.bliss.villager.Text;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public final class MemoryRoster implements Listener {
    static final int MIN = 10;
    static final int MAX = 20;
    private static final List<String> SEED;
    private final BlissPlugin plugin;
    private final NickManager nick;
    private final File file;
    private final YamlConfiguration cfg;
    private final Set<UUID> returning;
    private final Set<UUID> moveOnRespawn;
    private final Set<UUID> wakeOnRespawn;
    private final Set<String> reserved;

    public MemoryRoster(BlissPlugin var1, NickManager var2, int var3) {
        HashSet var5 = new HashSet();
        this.returning = var5;
        HashSet var26 = new HashSet();
        this.moveOnRespawn = var26;
        HashSet var27 = new HashSet();
        this.wakeOnRespawn = var27;
        HashSet var28 = new HashSet();
        this.reserved = var28;
        this.plugin = var1;
        this.nick = var2;
        File var40 = var1.getDataFolder();
        File var31 = new File(var40, "memories.yml");
        this.file = var31;
        YamlConfiguration var34 = YamlConfiguration.loadConfiguration(this.file);
        this.cfg = var34;
        if (!this.cfg.contains("names") || this.cfg.getStringList("names").size() < 10) {
            YamlConfiguration var24 = this.cfg;
            List var41 = SEED;
            ArrayList var39 = new ArrayList(var41);
            var24.set("names", var39);
            this.save(713226912);
        }
    }

    public List<String> names(int var1) {
        return this.cfg.getStringList("names");
    }

    private List<String> freeNames(int var1) {
        HashSet var2 = new HashSet();
        HashSet var3 = var2;
        if (this.cfg.isConfigurationSection("active")) {
            for (String var5 : this.cfg.getConfigurationSection("active").getKeys(false)) {
                YamlConfiguration var21 = this.cfg;
                String var54 = "active." + var5;
                String var6 = var21.getString(var54);
                if (var6 != null) {
                    String var56 = var6.toLowerCase();
                    var3.add(var56);
                }
            }
        }

        for (String var65 : this.reserved) {
            String var61 = var65.toLowerCase();
            var3.add(var61);
        }

        ArrayList var31 = new ArrayList();
        ArrayList var63 = var31;

        for (String var66 : this.names(1326778483)) {
            String var58 = var66.toLowerCase();
            if (!var3.contains(var58)) {
                var63.add(var66);
            }
        }

        return var63;
    }

    public String claimFreeName(int var1) {
        List var5 = this.freeNames(1483447618);
        if (var5.isEmpty()) {
            return null;
        } else {
            ThreadLocalRandom var3 = ThreadLocalRandom.current();
            int var20 = var5.size();
            int var18 = var3.nextInt(var20);
            String var6 = (String)var5.get(var18);
            Set var15 = this.reserved;
            var15.add(var6);
            return var6;
        }
    }

    public void claimSpecificName(String var1, int var2) {
        if (var1 != null) {
            Set var6 = this.reserved;
            var6.add(var1);
        }
    }

    public void releaseName(String var1, int var2) {
        if (var1 != null) {
            Set var6 = this.reserved;
            var6.remove(var1);
        }
    }

    public boolean isMemory(UUID var1, int var2) {
        YamlConfiguration var5 = this.cfg;
        String var8 = "active." + var1;
        return var5.contains(var8);
    }

    public String nameOf(UUID var1, int var2) {
        YamlConfiguration var5 = this.cfg;
        String var8 = "active." + var1;
        return var5.getString(var8);
    }

    public Set<UUID> activeIds(int var1) {
        HashSet<UUID> var2 = new HashSet<>();
        HashSet<UUID> var3 = var2;
        ConfigurationSection var4 = this.cfg.getConfigurationSection("active");
        if (var4 == null) {
            return var2;
        } else {
            Iterator<String> var5 = var4.getKeys(false).iterator();

            while (var5.hasNext()) {
                String var6 = var5.next();

                try {
                    UUID var28 = UUID.fromString(var6);
                    var3.add(var28);
                } catch (IllegalArgumentException var31) {
                }
            }

            return var3;
        }
    }

    public String assign(Player var1, String var2, int var3) {
        if (var2 == null) {
            List var7 = this.freeNames(1483447618);
            if (var7.isEmpty()) {
                return null;
            }

            ThreadLocalRandom var5 = ThreadLocalRandom.current();
            int var74 = var7.size();
            int var58 = var5.nextInt(var74);
            var2 = (String)var7.get(var58);
        } else {
            List var55 = this.freeNames(1483447618);
            if (!var55.contains(var2)) {
                return null;
            }
        }

        YamlConfiguration var17 = this.cfg;
        String var62 = "active." + var1.getUniqueId();
        var17.set(var62, var2);
        this.save(713226912);
        NickManager var20 = this.nick;
        var20.setName(var1, var2, 1837963399);
        if (this.plugin.goldenDream(2108340810) != null) {
            GoldenDreamWorld var27 = this.plugin.goldenDream(2108340810).dream(2117018474);
            UUID var65 = var1.getUniqueId();
            if (var27.inDream(var65, 2004945976)) {
                if (var1.isDead()) {
                    Set var32 = this.wakeOnRespawn;
                    UUID var67 = var1.getUniqueId();
                    var32.add(var67);
                } else {
                    GoldenDreamWorld var38 = this.plugin.goldenDream(2108340810).dream(2117018474);
                    var38.wake(var1, 1928909343);
                    this.moveToMemory(var1, 1693807961);
                }

                return var2;
            }
        }

        if (!GoldenDreamWorld.isDreamWorld(var1.getWorld()) && this.plugin.goldenDream(2108340810) != null) {
            if (var1.isDead()) {
                Set var52 = this.moveOnRespawn;
                UUID var72 = var1.getUniqueId();
                var52.add(var72);
            } else {
                this.moveToMemory(var1, 1693807961);
            }
        }

        return var2;
    }

    private void moveToMemory(Player var1, int var2) {
        Location var5 = this.plugin.goldenDream(2108340810).dream(2117018474).memoryArrival(421630336);
        if (var5 != null) {
            Vector var4 = new Vector();
            var1.setVelocity(var4);
            var1.teleport(var5);
            var1.setFallDistance(0.0F);
        }
    }

    public boolean isNameFree(String var1, int var2) {
        if (var1 != null && !var1.isBlank()) {
            if (this.cfg.isConfigurationSection("active")) {
                for (String var7 : this.cfg.getConfigurationSection("active").getKeys(false)) {
                    YamlConfiguration var42 = this.cfg;
                    String var45 = "active." + var7;
                    String var43 = var42.getString(var45);
                    if (var1.equalsIgnoreCase(var43)) {
                        return false;
                    }
                }
            }

            for (String var47 : this.reserved) {
                if (var1.equalsIgnoreCase(var47)) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public boolean rename(Player var1, String var2, int var3) {
        UUID var17 = var1.getUniqueId();
        if (this.isMemory(var17, 1860168534) && this.isNameFree(var2, 1383580576)) {
            YamlConfiguration var12 = this.cfg;
            String var22 = "active." + var1.getUniqueId();
            var12.set(var22, var2);
            this.save(713226912);
            NickManager var15 = this.nick;
            var15.setName(var1, var2, 1837963399);
            return true;
        } else {
            return false;
        }
    }

    public void remove(Player var1, int var2) {
        UUID var6 = var1.getUniqueId();
        Set var10 = this.moveOnRespawn;
        var10.remove(var6);
        Set var13 = this.wakeOnRespawn;
        var13.remove(var6);
        Set var16 = this.returning;
        var16.remove(var6);
        YamlConfiguration var19 = this.cfg;
        String var64 = "active." + var6;
        var19.set(var64, null);
        this.save(713226912);
        NickManager var22 = this.nick;
        var22.reset(var1, 1016238986);
        if (this.plugin.goldenDream(2108340810) != null) {
            if (GoldenDreamWorld.isDreamWorld(var1.getWorld())) {
                GoldenDreamWorld var32 = this.plugin.goldenDream(2108340810).dream(2117018474);
                if (!var32.inDream(var6, 2004945976)) {
                    GoldenDreamWorld var37 = this.plugin.goldenDream(2108340810).dream(2117018474);
                    if (!var37.isVisiting(var6, 631003554)) {
                        if (var1.isDead()) {
                            Set var42 = this.returning;
                            var42.add(var6);
                        } else {
                            World var59;
                            if (this.plugin.getServer().getWorlds().isEmpty()) {
                                var59 = null;
                            } else {
                                var59 = (World)this.plugin.getServer().getWorlds().get(0);
                            }

                            if (var59 != null) {
                                Vector var70 = new Vector();
                                var1.setVelocity(var70);
                                Location var72 = var59.getSpawnLocation();
                                var1.teleport(var72);
                                var1.setFallDistance(0.0F);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    public void onDeath(PlayerDeathEvent var1) {
        Player var5 = var1.getEntity();
        UUID var6 = var5.getUniqueId();
        if (this.isMemory(var6, 1860168534)) {
            String var7 = this.nameOf(var6, 420288045);
            if (!this.killedByIntruder(var5, 637677568)) {
                var1.deathMessage(null);
            } else {
                Component var40 = Text.legacy("&e" + var7 + " has been purged.", 373192400);
                var1.deathMessage(var40);
                Location var8 = var5.getLocation();
                World var9 = var8.getWorld();
                if (var9 != null) {
                    var9.strikeLightningEffect(var8);
                }

                YamlConfiguration var28 = this.cfg;
                String var44 = "active." + var6;
                var28.set(var44, null);
                this.save(713226912);
                Set var31 = this.returning;
                var31.add(var6);
                NickManager var34 = this.nick;
                var34.forget(var6, 738960671);
            }
        }
    }

    private boolean killedByIntruder(Player var1, int var2) {
        if (this.plugin.goldenDream(2108340810) == null) {
            return false;
        } else {
            Player var5 = var1.getKiller();
            if (var5 == null) {
                EntityDamageEvent var6 = var1.getLastDamageCause();
                label34:
                if (var6 instanceof EntityDamageByEntityEvent) {
                    Entity var8 = ((EntityDamageByEntityEvent)var6).getDamager();
                    if (var8 instanceof Projectile) {
                        ProjectileSource var12 = ((Projectile)var8).getShooter();
                        if (var12 instanceof Player) {
                            var5 = (Player)var12;
                            break label34;
                        }
                    }

                    if (var8 instanceof Player) {
                        var5 = (Player)var8;
                    }
                }
            }

            if (var5 == null) {
                return false;
            } else {
                UUID var53 = var5.getUniqueId();
                if (this.isMemory(var53, 1860168534)) {
                    return false;
                } else {
                    GoldenDreamWorld var45 = this.plugin.goldenDream(2108340810).dream(2117018474);
                    UUID var55 = var5.getUniqueId();
                    return var45.isIntruder(var55, 1068736112);
                }
            }
        }
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    public void onRespawn(PlayerRespawnEvent var1) {
        Player var6 = var1.getPlayer();
        Set var10 = this.wakeOnRespawn;
        UUID var105 = var6.getUniqueId();
        if (var10.remove(var105) && this.plugin.goldenDream(2108340810) != null) {
            BukkitScheduler var18 = this.plugin.getServer().getScheduler();
            BlissPlugin var107 = this.plugin;
            Runnable var135 = () -> {
                if (var6.isOnline() && !var6.isDead()) {
                    UUID var15 = var6.getUniqueId();
                    if (this.isMemory(var15, 1860168534)) {
                        GoldenDreamWorld var12 = this.plugin.goldenDream(2108340810).dream(2117018474);
                        var12.wake(var6, 1928909343);
                        this.moveToMemory(var6, 1693807961);
                        return;
                    }
                }
            };
            var18.runTask(var107, var135);
        }

        Set var21 = this.moveOnRespawn;
        UUID var109 = var6.getUniqueId();
        if (var21.remove(var109)) {
            UUID var111 = var6.getUniqueId();
            if (this.isMemory(var111, 1860168534) && this.plugin.goldenDream(2108340810) != null) {
                Location var7 = this.plugin.goldenDream(2108340810).dream(2117018474).memoryArrival(421630336);
                if (var7 != null) {
                    var1.setRespawnLocation(var7);
                }
            }
        }

        label94: {
            Set var36 = this.returning;
            UUID var114 = var6.getUniqueId();
            if (!var36.contains(var114)) {
                UUID var116 = var6.getUniqueId();
                if (!this.isMemory(var116, 1860168534)) {
                    Set var41 = this.moveOnRespawn;
                    UUID var118 = var6.getUniqueId();
                    if (!var41.contains(var118)) {
                        Set var44 = this.wakeOnRespawn;
                        UUID var120 = var6.getUniqueId();
                        if (!var44.contains(var120) && GoldenDreamWorld.isDreamWorld(var1.getRespawnLocation().getWorld())) {
                            if (this.plugin.goldenDream(2108340810) == null) {
                                break label94;
                            }

                            GoldenDreamWorld var56 = this.plugin.goldenDream(2108340810).dream(2117018474);
                            UUID var122 = var6.getUniqueId();
                            if (!var56.inDream(var122, 2004945976)) {
                                GoldenDreamWorld var61 = this.plugin.goldenDream(2108340810).dream(2117018474);
                                UUID var124 = var6.getUniqueId();
                                if (!var61.isVisiting(var124, 631003554)) {
                                    break label94;
                                }
                            }
                        }
                    }
                }
            }

            Set var77 = this.returning;
            UUID var129 = var6.getUniqueId();
            if (!var77.remove(var129)) {
                return;
            }

            BukkitScheduler var82 = this.plugin.getServer().getScheduler();
            BlissPlugin var131 = this.plugin;
            Runnable var137 = () -> {
                if (var6.isOnline()) {
                    NickManager var6x = this.nick;
                    var6x.apply(var6, 535454495);
                }
            };
            var82.runTask(var131, var137);
            if ((var1.isBedSpawn() || var1.isAnchorSpawn()) && !GoldenDreamWorld.isDreamWorld(var1.getRespawnLocation().getWorld())) {
                return;
            }

            World var104;
            if (this.plugin.getServer().getWorlds().isEmpty()) {
                var104 = null;
            } else {
                var104 = (World)this.plugin.getServer().getWorlds().get(0);
            }

            if (var104 != null) {
                Location var133 = var104.getSpawnLocation();
                var1.setRespawnLocation(var133);
            }

            return;
        }

        World var75;
        if (this.plugin.getServer().getWorlds().isEmpty()) {
            var75 = null;
        } else {
            var75 = (World)this.plugin.getServer().getWorlds().get(0);
        }

        if (var75 != null) {
            Location var126 = var75.getSpawnLocation();
            var1.setRespawnLocation(var126);
        }
    }

    private void save(int var1) {
        try {
            YamlConfiguration var5 = this.cfg;
            File var10 = this.file;
            AtomicYml.save(var5, var10, 1347473142);
        } catch (IOException var14) {
            Logger var9 = this.plugin.getLogger();
            String var13 = "memories.yml: " + var14.getMessage();
            var9.warning(var13);
        }
    }

    static {
        String[] var3 = new String[15];
        String var2 = "ItsRyanStuff";
        var3[0] = var2;
        String var19 = "EchoOfNoah";
        var3[1] = var19;
        String var20 = "PaleWanderer";
        var3[2] = var20;
        String var21 = "GreyHollow";
        var3[3] = var21;
        String var22 = "FadedMilo";
        var3[4] = var22;
        String var23 = "DustBoundLeo";
        var3[5] = var23;
        String var24 = "AshenKai";
        var3[6] = var24;
        String var25 = "HollowFinn";
        var3[7] = var25;
        String var26 = "MutedTheo";
        var3[8] = var26;
        String var27 = "SilentEli";
        var3[9] = var27;
        String var28 = "WornOutSam";
        var3[10] = var28;
        String var29 = "GhostlyOwen";
        var3[11] = var29;
        String var30 = "BleakToby";
        var3[12] = var30;
        String var31 = "FrayedZane";
        var3[13] = var31;
        String var32 = "QuietedMax";
        var3[14] = var32;
        SEED = List.of(var3);
    }
}
