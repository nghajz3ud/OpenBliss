package com.bliss.nick;

import com.bliss.BlissPlugin;
import com.bliss.core.AtomicYml;
import com.bliss.goldgem.DreamTabList;
import com.bliss.villager.Text;
import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.function.Function;
import java.util.logging.Logger;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitScheduler;

public final class NickManager implements Listener {
    private final BlissPlugin plugin;
    private final File file;
    private final YamlConfiguration cfg;

    public NickManager(BlissPlugin var1, int var2) {
        this.plugin = var1;
        File var14 = var1.getDataFolder();
        File var10 = new File(var14, "nicks.yml");
        this.file = var10;
        YamlConfiguration var13 = YamlConfiguration.loadConfiguration(this.file);
        this.cfg = var13;
    }

    public void setName(Player var1, String var2, int var3) {
        String var7 = var1.getUniqueId().toString();
        YamlConfiguration var11 = this.cfg;
        String var14 = var7 + ".name";
        var11.set(var14, var2);
        this.save(1521596237);
        this.apply(var1, 535454495);
    }

    public boolean setSkinFrom(Player var1, Player var2, int var3) {
        ProfileProperty var7 = textures(var2.getPlayerProfile(), 1619210204);
        if (var7 == null) {
            return false;
        } else {
            this.captureReal(var1, 1936023417);
            String var8 = var1.getUniqueId().toString();
            YamlConfiguration var18 = this.cfg;
            String var25 = var8 + ".skin.value";
            String var29 = var7.getValue();
            var18.set(var25, var29);
            YamlConfiguration var20 = this.cfg;
            String var27 = var8 + ".skin.signature";
            String var31 = var7.getSignature();
            var20.set(var27, var31);
            this.save(1521596237);
            this.apply(var1, 535454495);
            return true;
        }
    }

    public void clearSkin(Player var1, int var2) {
        String var6 = var1.getUniqueId().toString();
        YamlConfiguration var10 = this.cfg;
        String var13 = var6 + ".skin";
        var10.set(var13, null);
        this.save(1521596237);
        this.apply(var1, 535454495);
    }

    public void reset(Player var1, int var2) {
        String var6 = var1.getUniqueId().toString();
        UUID var19 = var1.getUniqueId();
        boolean var7 = this.isNicked(var19, 1441442241);
        YamlConfiguration var13 = this.cfg;
        String var21 = var6 + ".name";
        var13.set(var21, null);
        YamlConfiguration var15 = this.cfg;
        String var23 = var6 + ".skin";
        var15.set(var23, null);
        this.save(1521596237);
        if (var7) {
            this.apply(var1, 535454495);
        }
    }

    public void forget(UUID var1, int var2) {
        String var6 = var1.toString();
        YamlConfiguration var9 = this.cfg;
        String var19 = var6 + ".name";
        if (var9.getString(var19) == null) {
            YamlConfiguration var12 = this.cfg;
            String var21 = var6 + ".skin";
            if (!var12.contains(var21)) {
                return;
            }
        }

        YamlConfiguration var15 = this.cfg;
        String var23 = var6 + ".name";
        var15.set(var23, null);
        YamlConfiguration var17 = this.cfg;
        String var25 = var6 + ".skin";
        var17.set(var25, null);
        this.save(1521596237);
    }

    public boolean isNicked(UUID var1, int var2) {
        String var5 = var1.toString();
        YamlConfiguration var8 = this.cfg;
        String var14 = var5 + ".name";
        if (var8.getString(var14) == null) {
            YamlConfiguration var11 = this.cfg;
            String var16 = var5 + ".skin.value";
            if (!var11.contains(var16)) {
                return false;
            }
        }

        return true;
    }

    public void apply(Player var1, int var2) {
        String var9 = var1.getUniqueId().toString();
        this.captureReal(var1, 1936023417);
        YamlConfiguration var21 = this.cfg;
        String var59 = var9 + ".real.name";
        String var74 = var1.getName();
        String var10 = var21.getString(var59, var74);
        YamlConfiguration var24 = this.cfg;
        String var61 = var9 + ".name";
        String var11 = var24.getString(var61);
        String var57;
        if (var11 == null) {
            var57 = var10;
        } else {
            var57 = sanitize(var11, 1261961191);
        }

        String var12 = var57;
        if (var57.isEmpty()) {
            var12 = var10;
        }

        UUID var31 = var1.getUniqueId();
        PlayerProfile var13 = Bukkit.createProfileExact(var31, var12);
        YamlConfiguration var34 = this.cfg;
        String var64 = var9 + ".skin.value";
        YamlConfiguration var76 = this.cfg;
        String var82 = var9 + ".real.value";
        String var77 = var76.getString(var82);
        String var14 = var34.getString(var64, var77);
        YamlConfiguration var37 = this.cfg;
        String var66 = var9 + ".skin.signature";
        YamlConfiguration var79 = this.cfg;
        String var84 = var9 + ".real.signature";
        String var80 = var79.getString(var84);
        String var15 = var37.getString(var66, var80);
        if (var14 != null) {
            String var85 = "textures";
            ProfileProperty var67 = new ProfileProperty(var85, var14, var15);
            var13.setProperty(var67);
        }

        var1.setPlayerProfile(var13);
        Object var55;
        if (var11 == null) {
            var55 = Component.text(var10);
        } else {
            var55 = Text.legacy(var11, 373192400);
        }

        var1.displayName((Component)var55);
        var1.playerListName((Component)var55);
        BlissPlugin var47 = this.plugin;
        Function<Player, String> var72 = this::realName;
        RealNameTable.reconcile(var47, var72, 78405898);
        if (this.plugin.dreamTabList(1532541723) != null) {
            DreamTabList var53 = this.plugin.dreamTabList(1532541723);
            var53.refresh(var1, true, 141712163);
        }
    }

    public String realName(Player var1) {
        YamlConfiguration var5 = this.cfg;
        String var9 = var1.getUniqueId() + ".real.name";
        String var10 = var1.getName();
        return var5.getString(var9, var10);
    }

    private static String sanitize(String var0, int var1) {
        String var3 = "(?i)<#+[0-9a-f]{6}>";
        String var4 = "";
        String var6 = var0.replaceAll(var3, var4);
        String var14 = "(?i)[&§].";
        String var19 = "";
        String var8 = var6.replaceAll(var14, var19).replace(' ', '_');
        String var16 = "[^A-Za-z0-9_]";
        String var21 = "";
        String var5 = var8.replaceAll(var16, var21);
        String var12;
        if (var5.length() > 16) {
            var12 = var5.substring(0, 16);
        } else {
            var12 = var5;
        }

        return var12;
    }

    private void captureReal(Player var1, int var2) {
        String var6 = var1.getUniqueId().toString();
        YamlConfiguration var11 = this.cfg;
        String var28 = var6 + ".real.name";
        if (!var11.contains(var28)) {
            YamlConfiguration var14 = this.cfg;
            String var30 = var6 + ".real.name";
            String var37 = var1.getName();
            var14.set(var30, var37);
            this.save(1521596237);
        }

        YamlConfiguration var17 = this.cfg;
        String var32 = var6 + ".real.value";
        if (!var17.contains(var32)) {
            ProfileProperty var7 = textures(var1.getPlayerProfile(), 1619210204);
            if (var7 != null) {
                YamlConfiguration var24 = this.cfg;
                String var34 = var6 + ".real.value";
                String var39 = var7.getValue();
                var24.set(var34, var39);
                YamlConfiguration var26 = this.cfg;
                String var36 = var6 + ".real.signature";
                String var41 = var7.getSignature();
                var26.set(var36, var41);
                this.save(1521596237);
            }
        }
    }

    private static ProfileProperty textures(PlayerProfile var0, int var1) {
        for (ProfileProperty var5 : var0.getProperties()) {
            if (var5.getName().equals("textures")) {
                return var5;
            }
        }

        return null;
    }

    @EventHandler(
        priority = EventPriority.LOW
    )
    public void onJoin(PlayerJoinEvent var1) {
        Player var6;
        String var7;
        boolean var35;
        label26: {
            var6 = var1.getPlayer();
            var7 = var6.getUniqueId().toString();
            YamlConfiguration var14 = this.cfg;
            String var36 = var7 + ".name";
            if (var14.getString(var36) == null) {
                YamlConfiguration var17 = this.cfg;
                String var38 = var7 + ".skin.value";
                if (!var17.contains(var38)) {
                    var35 = false;
                    break label26;
                }
            }

            var35 = true;
        }

        label21: {
            if (!var35) {
                YamlConfiguration var21 = this.cfg;
                String var40 = var7 + ".real.name";
                if (!var21.contains(var40)) {
                    break label21;
                }
            }

            String var24 = var6.getName();
            YamlConfiguration var42 = this.cfg;
            String var48 = var7 + ".real.name";
            String var43 = var42.getString(var48);
            if (!var24.equals(var43)) {
                YamlConfiguration var27 = this.cfg;
                String var45 = var7 + ".real.name";
                String var50 = var6.getName();
                var27.set(var45, var50);
                this.save(1521596237);
            }
        }

        if (var35) {
            BukkitScheduler var33 = this.plugin.getServer().getScheduler();
            BlissPlugin var47 = this.plugin;
            Runnable var52 = () -> {
                if (var6.isOnline()) {
                    this.apply(var6, 535454495);
                }
            };
            var33.runTask(var47, var52);
        }
    }

    @EventHandler(
        priority = EventPriority.MONITOR
    )
    public void onPostRespawn(PlayerPostRespawnEvent var1) {
        BlissPlugin var4 = this.plugin;
        Function<Player, String> var5 = this::realName;
        RealNameTable.reconcile(var4, var5, 78405898);
    }

    @EventHandler(
        priority = EventPriority.MONITOR
    )
    public void onQuit(PlayerQuitEvent var1) {
        BukkitScheduler var7 = this.plugin.getServer().getScheduler();
        BlissPlugin var9 = this.plugin;
        Runnable var10 = () -> {
            BlissPlugin var3 = this.plugin;
            Function<Player, String> var4 = this::realName;
            RealNameTable.reconcile(var3, var4, 78405898);
        };
        var7.runTask(var9, var10);
    }

    private void save(int var1) {
        try {
            YamlConfiguration var5 = this.cfg;
            File var10 = this.file;
            AtomicYml.save(var5, var10, 1347473142);
        } catch (IOException var14) {
            Logger var9 = this.plugin.getLogger();
            String var13 = "nicks.yml: " + var14.getMessage();
            var9.warning(var13);
        }
    }
}
