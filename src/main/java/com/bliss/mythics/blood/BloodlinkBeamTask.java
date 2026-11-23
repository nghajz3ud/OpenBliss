package com.bliss.mythics.blood;

import com.bliss.core.Particles;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public final class BloodlinkBeamTask extends BukkitRunnable {
    private static final double STEP = 0.3;
    private static final float DUST = 1.6F;
    private static final double MAX_LEN = 24.0;
    private final Plugin plugin;
    private final MythicBloodState state;
    private final MythicBloodState.LinkGroup group;

    public BloodlinkBeamTask(Plugin var1, MythicBloodState var2, MythicBloodState.LinkGroup var3, int var4) {
        this.plugin = var1;
        this.state = var2;
        this.group = var3;
    }

    public void start(int var1) {
        Plugin var9 = this.plugin;
        this.runTaskTimer(var9, 0L, 1L);
    }

    public void run() {
        long var6 = System.currentTimeMillis();
        long var14 = this.group.untilMs;
        if (var14 <= var6) {
            this.teardown(1603815710);
            this.cancel();
        } else {
            ArrayList var19 = new ArrayList();
            ArrayList var8 = var19;

            for (UUID var64 : this.group.members) {
                Server var39 = this.plugin.getServer();
                Player var11 = var39.getPlayer(var64);
                if (var11 != null && var11.isOnline() && !var11.isDead()) {
                    var8.add(var11);
                }
            }

            if (var8.size() >= 2) {
                int var63 = 0;

                while (true) {
                    int var50 = var8.size();
                    if (var63 >= var50) {
                        return;
                    }

                    int var10 = var63 + 1;

                    while (true) {
                        int var53 = var8.size();
                        if (var10 >= var53) {
                            var63++;
                            break;
                        }

                        Player var56 = (Player)var8.get(var63);
                        Player var62 = (Player)var8.get(var10);
                        this.drawBeam(var56, var62, 523762264);
                        var10++;
                    }
                }
            }
        }
    }

    private void drawBeam(Player var1, Player var2, int var3) {
        World var42 = var1.getWorld();
        World var94 = var2.getWorld();
        if (var42.equals(var94)) {
            World var28 = var1.getWorld();
            Location var29 = var1.getLocation().add(0.0, 1.0, 0.0);
            Vector var53 = var2.getLocation().add(0.0, 1.0, 0.0).toVector();
            Vector var100 = var29.toVector();
            Vector var31 = var53.subtract(var100);
            double var32 = var31.length();
            if (!(var32 < 0.001)) {
                if (var32 > 24.0) {
                    var32 = 24.0;
                }

                double var135 = var31.length();
                double var103 = 1.0 / var135;
                var31.multiply(var103);

                for (double var34 = 0.0; var34 < var32; var34 += 0.3) {
                    double var74 = var29.getX();
                    double var112 = var31.getX();
                    double var114 = var112 * var34;
                    double var36 = var74 + var114;
                    double var79 = var29.getY();
                    double var117 = var31.getY();
                    double var119 = var117 * var34;
                    double var38 = var79 + var119;
                    double var84 = var29.getZ();
                    double var122 = var31.getZ();
                    double var124 = var122 * var34;
                    double var40 = var84 + var124;
                    Particle var105 = Particle.DUST;
                    Color var26 = Color.RED;
                    DustOptions var25 = new DustOptions(var26, 1.6F);
                    Particles.spawn(var28, var105, var36, var38, var40, 1, 0.0, 0.0, 0.0, 0.0, var25, 2077675459);
                    Particle var106 = Particle.BLOCK;
                    BlockData var159 = Material.REDSTONE_BLOCK.createBlockData();
                    Particles.spawn(var28, var106, var36, var38, var40, 1, 0.0, 0.0, 0.0, 0.0, var159, 2077675459);
                }
            }
        }
    }

    private void teardown(int var1) {
        for (UUID var5 : this.group.members) {
            Map var16 = this.state.linkOf;
            Object var17 = var16.get(var5);
            MythicBloodState.LinkGroup var23 = this.group;
            if (var17 == var23) {
                Map var20 = this.state.linkOf;
                var20.remove(var5);
            }
        }
    }
}
