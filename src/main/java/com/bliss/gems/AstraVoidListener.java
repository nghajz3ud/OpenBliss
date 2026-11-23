package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.EnergyScale;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.Particles;
import com.bliss.core.PlayerDataStore;
import com.bliss.core.TagStore;
import com.bliss.core.TrustStore;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public final class AstraVoidListener implements Listener {
    private static final int ROUNDS = 60;
    private static final long ROUND_TICKS = 40L;
    private static final long STAGGER_TICKS = 10L;
    private static final double REACH_SQ = 49.0;
    private static final int TAG_SECONDS = 2;
    private static final int TAG_REFRESH_BELOW = 2;
    private static final double CENTRE_HEIGHT = 3.73;
    private static final double OUTER_RADIUS = 9.1;
    private static final double INNER_RADIUS = 9.555;
    private static final double OUTER_SPACING = 0.4;
    private static final double INNER_SPACING = 0.45;
    private static final double ARC_STEP = 0.06;
    private static final double OUTER_TAIL = 1.413716694115407;
    private static final double INNER_TAIL = 1.0995574287564276;
    private static final double INNER_SPAN = Math.PI * 11.0 / 10.0;
    private static final DustOptions OUTER_DUST;
    private static final DustOptions INNER_DUST;
    private static final double[] OUTER_TILTS;
    private static final double[] INNER_TILTS;
    private static final int DARKNESS_EVERY = 10;
    private static final int DARKNESS_TICKS = 20;
    private static final double DARKNESS_RANGE_SQ = 82.81;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final AstraGemListener astra;
    private final TrustStore trust;
    private final TagStore tags;

    public AstraVoidListener(BlissPlugin var1, PlayerDataStore var2, AstraGemListener var3, TrustStore var4, TagStore var5, int var6) {
        this.plugin = var1;
        this.players = var2;
        this.astra = var3;
        this.trust = var4;
        this.tags = var5;
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent var1) {
        EquipmentSlot var11 = var1.getHand();
        EquipmentSlot var3 = EquipmentSlot.HAND;
        if (var11 == var3) {
            if (!(var1 instanceof PlayerInteractAtEntityEvent)) {
                Player var6 = var1.getPlayer();
                GameMode var17 = var6.getGameMode();
                GameMode var43 = GameMode.SPECTATOR;
                if (var17 != var43) {
                    ItemStack var7 = var6.getInventory().getItemInMainHand();
                    int var22 = BlissCoreItems.gemIdOf(var7, 382441079);
                    int var45 = GemType.ASTRA.id();
                    if (var22 == var45) {
                        if (BlissCoreItems.tierOf(var7, 1633828214) == 2) {
                            UUID var8 = var6.getUniqueId();
                            PlayerDataStore var28 = this.players;
                            if (var28.get(var8, 1264010596).canUsePowers(1616892701)) {
                                Map var34 = this.astra.cd3;
                                Long var10 = (Long)var34.get(var8);
                                if (var10 != null) {
                                    long var39 = var10;
                                    long var4 = System.currentTimeMillis();
                                    if (var39 > var4) {
                                        return;
                                    }
                                }

                                Location var53 = var1.getRightClicked().getLocation().clone();
                                this.cast(var6, var53, 1789972989);
                            }
                        }
                    }
                }
            }
        }
    }

    private void cast(Player var1, Location var2, int var3) {
        UUID var14 = var1.getUniqueId();
        PlayerDataStore var19 = this.players;
        int var15 = var19.get(var14, 1264010596).energy(1160853318);
        Component var31 = Text.legacy("&5\ud83d\udd2e &bYou have activated §7Astral Void", 373192400);
        var1.sendMessage(var31);
        Map var25 = this.astra.cd3;
        long var6 = System.currentTimeMillis();
        String var9 = "astra";
        String var10 = "astral-void";
        long var46 = cooldownMs(var15, 1771349019);
        long var40 = GemTuning.cd(var9, var10, 150.0, var46, 1621983041);
        long var42 = EnergyScale.cd(var40, var15, 907518618);
        Long var37 = var6 + var42;
        var25.put(var14, var37);
        AstraVoidListener.Ending var27 = new AstraVoidListener.Ending();
        this.drawVoid(var2, var27, 310788167);
        this.runRound(var1, var2, 0, var27, 1494484036);
    }

    private static long cooldownMs(int var0, int var1) {
        if (var0 > 4) {
            return 150000L;
        } else if (var0 == 4) {
            return 155000L;
        } else if (var0 == 3) {
            return 160000L;
        } else {
            return var0 == 2 ? 170000L : 180000L;
        }
    }

    private void runRound(Player var1, Location var2, int var3, AstraVoidListener.Ending var4, int var5) {
        if (var3 >= 60) {
            var4.done = true;
        } else {
            World var14 = var2.getWorld();
            if (var14 == null) {
                var4.done = true;
            } else if (var1.isOnline() && !var1.isDead()) {
                UUID var15 = var1.getUniqueId();
                String var36 = "astra";
                String var76 = "astral-void";
                double var16 = GemTuning.radius(var36, var76, 7.0, 1058601812);
                double var18 = var16 * var16;
                long var20 = 0L;

                for (Player var23 : var14.getPlayers()) {
                    UUID var54 = var23.getUniqueId();
                    if (!var54.equals(var15)) {
                        Location var57 = var23.getLocation();
                        double var58 = var57.distanceSquared(var2);
                        if (!(var58 > var18)) {
                            TrustStore var62 = this.trust;
                            UUID var90 = var23.getUniqueId();
                            if (!var62.isTrusted(var15, var90, 1736364606)) {
                                BukkitScheduler var65 = Bukkit.getScheduler();
                                BlissPlugin var81 = this.plugin;
                                Runnable var92 = () -> this.tag(var23, 663518032);
                                var65.runTaskLater(var81, var92, var20);
                                var20 += 10L;
                            }
                        }
                    }
                }

                BukkitScheduler var71 = Bukkit.getScheduler();
                BlissPlugin var83 = this.plugin;
                Runnable var96 = () -> {
                    int var10 = var3 + 1;
                    this.runRound(var1, var2, var10, var4, 1494484036);
                };
                var71.runTaskLater(var83, var96, 40L);
            } else {
                var4.done = true;
            }
        }
    }

    private void tag(Player var1, int var2) {
        if (var1.isOnline()) {
            PlayerDataStore var8 = this.players;
            UUID var13 = var1.getUniqueId();
            if (var8.get(var13, 1264010596).tagSecondsLeft(487151397) <= 2) {
                TagStore var12 = this.tags;
                UUID var16 = var1.getUniqueId();
                var12.addTagSeconds(var16, 2, 1101624808);
            }
        }
    }

    private void drawVoid(Location var1, final AstraVoidListener.Ending var2, int var3) {
        if (var1.getWorld() != null) {
            final Location var20 = var1.clone().add(0.0, 3.73, 0.0);
            final AstraVoidListener.Arc[] var21 = new AstraVoidListener.Arc[OUTER_TILTS.length];
            final AstraVoidListener.Arc[] var22 = new AstraVoidListener.Arc[INNER_TILTS.length];
            ThreadLocalRandom var23 = ThreadLocalRandom.current();
            int var24 = 1;
            byte var125;
            if (var23.nextBoolean()) {
                var125 = 0;
            } else {
                var125 = 1;
            }

            int var25 = 4 - var125;
            int var26 = 0;

            while (true) {
                int var81 = OUTER_TILTS.length;
                if (var26 >= var81) {
                    double var213 = var23.nextDouble() * 2.0 * Math.PI;
                    double var130 = Math.cos(var213);
                    double var191 = Math.sin(var213);
                    Vector var52 = new Vector(var130, 1.0, var191);
                    Vector var27 = var52;
                    int var28 = 0;

                    while (true) {
                        int var83 = INNER_TILTS.length;
                        if (var28 >= var83) {
                            BukkitRunnable var55 = new BukkitRunnable() {
                                int t = 0;

                                public void run() {
                                    if (var2.done) {
                                        this.cancel();
                                    } else {
                                        int var42 = this.t + 1;
                                        this.t = var42;

                                        for (AstraVoidListener.Arc var62 : var21) {
                                            Location var51 = var20;
                                            DustOptions var58 = AstraVoidListener.OUTER_DUST;
                                            if (var62.draw(var51, 9.1, 0.4, var58, 313917425)) {
                                                var62.restart(412189712);
                                            }
                                        }

                                        for (AstraVoidListener.Arc var9 : var22) {
                                            Location var47 = var20;
                                            DustOptions var5 = AstraVoidListener.INNER_DUST;
                                            if (var9.draw(var47, 9.555, 0.45, var5, 313917425)) {
                                                var9.restart(412189712);
                                            }
                                        }

                                        if (this.t % 10 == 0) {
                                            AstraVoidListener.blind(var20, 666199037);
                                        }
                                    }
                                }
                            };
                            BlissPlugin var85 = this.plugin;
                            var55.runTaskTimer(var85, 0L, 1L);
                            return;
                        }

                        AstraVoidListener.Arc var135 = new AstraVoidListener.Arc(Math.PI * 11.0 / 10.0, 1060583139);
                        var22[var28] = var135;
                        AstraVoidListener.Arc var59 = var22[var28];
                        double[] var136 = INNER_TILTS;
                        double var137 = var136[var28];
                        var59.orient(var27, var137, 32973509);
                        AstraVoidListener.Arc var61 = var22[var28];
                        double var194 = (double)var28;
                        double var180 = 0.6 * var194;
                        double var200 = (double)INNER_TILTS.length - 1.0;
                        double var182 = var180 / var200;
                        double var147 = 1.0 - var182;
                        double var92 = 1.0995574287564276 * var147;
                        var61.tail = var92;
                        AstraVoidListener.Arc var63 = var22[var28];
                        double var98 = (double)var28 * (Math.PI * 11.0 / 10.0);
                        double var153 = (double)INNER_TILTS.length;
                        double var100 = var98 / var153;
                        var63.phase = var100;
                        var28++;
                    }
                }

                AstraVoidListener.Arc var139 = new AstraVoidListener.Arc(Math.PI * 2, 1060583139);
                var21[var26] = var139;
                AstraVoidListener.Arc var66 = var21[var26];
                Vector var104 = new Vector(0, 1, 0);
                double[] var140 = OUTER_TILTS;
                double var141 = var140[var26];
                var66.orient(var104, var141, 32973509);
                AstraVoidListener.Arc var68 = var21[var26];
                double var203 = (double)var26;
                double var187 = 0.6 * var203;
                double var209 = (double)OUTER_TILTS.length - 1.0;
                double var189 = var187 / var209;
                double var159 = 1.0 - var189;
                double var108 = 1.413716694115407 * var159;
                var68.tail = var108;
                AstraVoidListener.Arc var70 = var21[var26];
                var70.direction = var24;
                var25 += -1;
                if (var25 == 0) {
                    var24 = -var24;
                    byte var114;
                    if (var23.nextBoolean()) {
                        var114 = 0;
                    } else {
                        var114 = 1;
                    }

                    var25 = 4 - var114;
                }

                AstraVoidListener.Arc var77 = var21[var26];
                double var121 = (double)var26 * 2.0 * Math.PI;
                double var167 = (double)OUTER_TILTS.length;
                double var123 = var121 / var167;
                var77.phase = var123;
                var26++;
            }
        }
    }

    private static void blind(Location var0, int var1) {
        World var12 = var0.getWorld();
        if (var12 != null) {
            String var21 = "astra";
            String var3 = "astral-void-darkness";
            double var13 = GemTuning.radius(var21, var3, 9.1, 1058601812);
            double var15 = var13 * var13;

            for (Player var18 : var12.getPlayers()) {
                Location var37 = var18.getLocation();
                double var38 = var37.distanceSquared(var0);
                if (!(var38 > var15)) {
                    PotionEffectType var6 = PotionEffectType.DARKNESS;
                    PotionEffect var44 = new PotionEffect(var6, 20, 1, true, false, false);
                    var18.addPotionEffect(var44);
                }
            }
        }
    }

    static {
        Color var9 = Color.fromRGB(160, 31, 255);
        DustOptions var0 = new DustOptions(var9, 1.44F);
        OUTER_DUST = var0;
        Color var11 = Color.fromRGB(255, 255, 255);
        DustOptions var4 = new DustOptions(var11, 0.96F);
        INNER_DUST = var4;
        double[] var6 = new double[]{0.86, 0.78, 0.62, 0.5, 0.34, 0.26, 0.1, -0.06, -0.22, -0.3, -0.48, -0.6, -0.74, -0.84};
        OUTER_TILTS = var6;
        double[] var8 = new double[]{-0.18, -0.09, 0.0, 0.09, 0.18};
        INNER_TILTS = var8;
    }

    private static final class Arc {
        Vector right;
        Vector up;
        Vector axis;
        double tilt;
        double phase;
        double tail;
        int direction = 1;
        final double span;

        Arc(double var1, int var3) {
            this.span = var1;
            this.randomise(1193985964);
        }

        void randomise(int var1) {
            ThreadLocalRandom var14 = ThreadLocalRandom.current();
            double var15 = var14.nextDouble() * 2.0 * Math.PI;
            double var44 = var14.nextDouble();
            double var17 = Math.acos(2.0 * var44 - 1.0);
            double var48 = Math.sin(var17);
            double var52 = Math.cos(var15);
            double var50 = var48 * var52;
            double var56 = Math.cos(var17);
            double var58 = Math.sin(var17);
            double var62 = Math.sin(var15);
            double var60 = var58 * var62;
            Vector var3 = new Vector(var50, var56, var60);
            this.setAxis(var3, 122387394);
            this.tilt = 0.0;
            this.phase = 0.0;
        }

        void orient(Vector var1, double var2, int var4) {
            this.setAxis(var1, 122387394);
            this.tilt = var2;
            this.phase = 0.0;
        }

        void restart(int var1) {
            this.phase = 0.0;
        }

        private void setAxis(Vector var1, int var2) {
            Vector var21 = var1.clone().normalize();
            this.axis = var21;
            Vector var19;
            if (Math.abs(this.axis.getY()) < 0.9) {
                var19 = new Vector(0, 1, 0);
            } else {
                var19 = new Vector(1, 0, 0);
            }

            Vector var23 = this.axis;
            Vector var25 = var23.getCrossProduct(var19).normalize();
            this.right = var25;
            Vector var27 = this.axis;
            Vector var33 = this.right;
            Vector var29 = var27.getCrossProduct(var33).normalize();
            this.up = var29;
        }

        boolean draw(Location var1, double var2, double var4, DustOptions var6, int var7) {
            World var28 = var1.getWorld();
            if (var28 == null) {
                return false;
            } else {
                double var135 = this.phase + 0.06;
                this.phase = var135;
                double var58 = this.phase;
                double var150 = this.tail;
                double var29 = var58 - var150;
                double var153 = this.span;
                if (var29 >= var153) {
                    return true;
                } else {
                    double var31 = Math.max(0.0, var29);
                    double var70 = this.span;
                    double var158 = this.phase;
                    double var33 = Math.min(var70, var158);
                    if (var31 >= var33) {
                        return false;
                    } else {
                        double var244 = this.tilt;
                        double var275 = this.tilt;
                        double var246 = var244 * var275;
                        double var214 = 1.0 - var246;
                        double var166 = Math.sqrt(Math.max(0.0, var214));
                        double var35 = var2 * var166;
                        double var84 = this.axis.getX();
                        double var169 = this.tilt;
                        double var86 = var84 * var169;
                        double var37 = var86 * var2;
                        double var92 = this.axis.getY();
                        double var174 = this.tilt;
                        double var94 = var92 * var174;
                        double var39 = var94 * var2;
                        double var100 = this.axis.getZ();
                        double var179 = this.tilt;
                        double var102 = var100 * var179;
                        double var41 = var102 * var2;
                        double var139 = var33 - var31;
                        double var141 = var139 * var35;
                        int var147 = (int)Math.ceil(var141 / var4);
                        int var43 = Math.max(2, var147);

                        for (int var44 = 0; var44 < var43; var44++) {
                            double var185 = var33 - var31;
                            double var219 = (double)var44;
                            double var187 = var185 * var219;
                            double var223 = (double)(var43 - 1);
                            double var189 = var187 / var223;
                            double var115 = var31 + var189;
                            double var193 = (double)this.direction;
                            double var47 = var115 * var193;
                            double var121 = Math.cos(var47);
                            double var49 = var121 * var35;
                            double var127 = Math.sin(var47);
                            double var51 = var127 * var35;
                            Particle var149 = Particle.DUST;
                            double var200 = var1.getX();
                            double var202 = var200 + var37;
                            double var250 = this.right.getX();
                            double var229 = var49 * var250;
                            double var204 = var202 + var229;
                            double var254 = this.up.getX();
                            double var233 = var51 * var254;
                            double var206 = var204 + var233;
                            double var236 = var1.getY();
                            double var238 = var236 + var39;
                            double var279 = this.right.getY();
                            double var260 = var49 * var279;
                            double var240 = var238 + var260;
                            double var283 = this.up.getY();
                            double var264 = var51 * var283;
                            double var242 = var240 + var264;
                            double var267 = var1.getZ();
                            double var269 = var267 + var41;
                            double var297 = this.right.getZ();
                            double var289 = var49 * var297;
                            double var271 = var269 + var289;
                            double var301 = this.up.getZ();
                            double var293 = var51 * var301;
                            double var273 = var271 + var293;
                            Particles.spawn(var28, var149, var206, var242, var273, 1, 0.0, 0.0, 0.0, 0.0, var6, true, 954482185);
                        }

                        return false;
                    }
                }
            }
        }
    }

    private static final class Ending {
        volatile boolean done;
    }
}
