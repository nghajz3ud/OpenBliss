package com.bliss.goldgem;

import com.bliss.BlissPlugin;
import com.bliss.core.Particles;
import com.bliss.core.SignatureClock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

public final class GoldenDreamRitual implements Listener {
    private static final int FIRST_ID = 1;
    private static final int LAST_ID = 7;
    private static final int NEEDED = 7;
    private static final double PILE_R = 2.5;
    private static final double SCAN_R = 6.0;
    private static final long SETTLE_TICKS = 12L;
    private final BlissPlugin plugin;
    private final Map<UUID, Boolean> running;
    private final Set<GoldenDreamMass> masses;
    private final Map<UUID, Runnable> entryAborts;
    private final GoldenDreamWorld dream;
    private static final int SPIRAL_TICKS = 200;
    private static final double SPIRAL_SPEED = 0.1;
    private static final float FRAG_SCALE = 0.7F;
    private static final int O_RAMP = 30;
    static final long POV_DAY = 80000L;
    static final long POV_CODE = 16001L;
    private static final double BRAID_R = 4.0;
    private static final double BRAID_AMP = 0.7;
    private static final int BRAID_WAVES = 4;
    private static final double BRAID_PHASE = Math.PI;
    private static final double BRAID_SPIN = 0.0;
    private static final double BRAID_LIFT = 2.0;
    private static final double BRAID_STEP = 0.09;
    private static final int BRAID_EVERY = 2;
    private static final float BRAID_DUST_SIZE = 0.5F;
    private static final double SHOWER_UP = 1.0;
    private static final Particle[] SHOWER_MIX;
    private static final String SHOWER_SOUND = "bliss:goldgem_dream_shower";
    private static final double SHOWER_SOUND_RANGE = 64.0;
    private static final int[] CLIP_BEATS;
    private static final boolean[] CLIP_BIG;
    private static final int PUFF_RING_POINTS = 40;
    private static final double PUFF_RING_SPEED = 2.4;
    private static final double PUFF_RING_SMALL = 0.6;
    private static final double ENDROD_SPEED = 0.06;

    public GoldenDreamRitual(BlissPlugin var1, int var2) {
        HashMap var4 = new HashMap();
        this.running = var4;
        HashSet var14 = new HashSet();
        this.masses = var14;
        HashMap var15 = new HashMap();
        this.entryAborts = var15;
        this.plugin = var1;
        GoldenDreamWorld var17 = new GoldenDreamWorld(var1, 2037087221);
        this.dream = var17;
        PluginManager var13 = var1.getServer().getPluginManager();
        GoldenDreamWorld var19 = this.dream;
        var13.registerEvents(var19, var1);
    }

    public GoldenDreamWorld dream(int var1) {
        return this.dream;
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onDrop(PlayerDropItemEvent var1) {
        if (RitualItems.isWireFragment(var1.getItemDrop().getItemStack(), 130584315)) {
            Player var7 = var1.getPlayer();
            BukkitScheduler var16 = this.plugin.getServer().getScheduler();
            BlissPlugin var18 = this.plugin;
            Runnable var19 = () -> {
                Location var7x = var1.getItemDrop().getLocation();
                this.judgePile(var7, var7x, 263740153);
            };
            var16.runTaskLater(var18, var19, 12L);
        }
    }

    private void judgePile(Player var1, Location var2, int var3) {
        if (var1 != null && var2 != null && var2.getWorld() != null) {
            Boolean var15 = Boolean.TRUE;
            Map var36 = this.running;
            UUID var42 = var1.getUniqueId();
            Object var37 = var36.get(var42);
            if (!var15.equals(var37)) {
                List var8 = this.collect(var1, var2, 105399758);
                if (var8.size() >= 7) {
                    Location var9 = centre(var8, 1514409867);
                    Iterator var10 = var8.iterator();

                    while (var10.hasNext()) {
                        Location var31 = ((Item)var10.next()).getLocation();
                        if (var31.distanceSquared(var9) > 6.25) {
                            return;
                        }
                    }

                    this.begin(var1, var9, var8, 186488396);
                }
            }
        }
    }

    private List<Item> collect(Player var1, Location var2, int var3) {
        HashMap var4 = new HashMap();
        HashMap var11 = var4;
        World var17 = var2.getWorld();

        for (Entity var13 : var17.getNearbyEntities(var2, 6.0, 6.0, 6.0)) {
            if (var13 instanceof Item) {
                Item var14 = (Item)var13;
                if (!var14.isDead()) {
                    UUID var32 = var1.getUniqueId();
                    UUID var49 = var14.getThrower();
                    if (var32.equals(var49) && RitualItems.isWireFragment(var14.getItemStack(), 130584315)) {
                        int var15 = RitualItems.wireFragmentTask(var14.getItemStack(), 1609711760);
                        if (var15 >= 1 && var15 <= 7) {
                            Integer var53 = var15;
                            var11.putIfAbsent(var53, var14);
                        }
                    }
                }
            }
        }

        Object var46;
        if (var11.size() == 7) {
            Collection var57 = var11.values();
            var46 = new ArrayList(var57);
        } else {
            var46 = List.of();
        }

        return (List<Item>)var46;
    }

    private static Location centre(List<Item> var0, int var1) {
        double var9 = 0.0;
        double var11 = 0.0;
        double var13 = 0.0;
        Iterator var15 = var0.iterator();

        while (var15.hasNext()) {
            Location var17 = ((Item)var15.next()).getLocation();
            double var46 = var17.getX();
            var9 += var46;
            double var49 = var17.getY();
            var11 += var49;
            double var52 = var17.getZ();
            var13 += var52;
        }

        int var76 = var0.size();
        World var57 = ((Item)var0.get(0)).getWorld();
        double var62 = (double)var76;
        double var60 = var9 / var62;
        double var68 = (double)var76;
        double var66 = var11 / var68;
        double var74 = (double)var76;
        double var72 = var13 / var74;
        return new Location(var57, var60, var66, var72);
    }

    private void begin(Player var1, final Location var2, List<Item> var3, int var4) {
        Map var27 = this.running;
        UUID var76 = var1.getUniqueId();
        Boolean var94 = true;
        var27.put(var76, var94);
        final ArrayList<ItemStack> var29 = new ArrayList<>();
        Stream<Item> var31 = var3.stream();
        Comparator<Item> var78 = Comparator.comparingInt(var0 -> RitualItems.wireFragmentTask(var0.getItemStack(), 1609711760));
        Stream<Item> var32 = var31.sorted(var78);
        Consumer<Item> var80 = var1x -> {
            ItemStack var4x = var1x.getItemStack().clone();
            var4x.setAmount(1);
            var29.add(var4x);
        };
        var32.forEach(var80);

        for (Item var139 : var3) {
            if (!var139.isDead()) {
                var139.remove();
            }
        }

        final World var138 = var2.getWorld();
        Sound var95 = Sound.BLOCK_BEACON_ACTIVATE;
        SoundCategory var8 = SoundCategory.MASTER;
        var138.playSound(var2, var95, var8, 2.0F, 0.7F);
        Logger var42 = this.plugin.getLogger();
        String var83 = var1.getName();
        String var97 = var138.getName();
        long var106 = Math.round(var2.getX());
        long var121 = Math.round(var2.getY());
        long var132 = Math.round(var2.getZ());
        String var84 = "Golden Dream: entry ritual triggered by " + var83 + " at " + var97 + " " + var106 + "," + var121 + "," + var132;
        var42.info(var84);
        final UUID var20 = var1.getUniqueId();
        final FragmentCoreRitual var21 = this.plugin.fragmentCoreRitual(234148762);
        final Location var22 = var2.clone().add(0.0, 1.0, 0.0);
        final Location var23 = var2.clone().add(0.0, 2.0, 0.0);
        if (this.plugin.controlRoomVeil(785222279) != null) {
            this.plugin.controlRoomVeil(785222279).pause(272L, 1861141377);
        }

        final long var24 = var1.getPlayerTimeOffset();
        final boolean var26 = var1.isPlayerTimeRelative();
        Map var66 = this.entryAborts;
        Runnable var99 = () -> {
            for (ItemStack var13 : var29) {
                var138.dropItem(var2, var13);
            }

            Server var19 = this.plugin.getServer();
            Player var33 = var19.getPlayer(var20);
            if (var33 != null && var33.isOnline()) {
                var33.setPlayerTime(var24, var26);
            }
        };
        var66.put(var20, var99);
        BukkitRunnable var68 = new BukkitRunnable() {
            FragmentCoreRitual.CamGroup cam;
            int t = 0;
            double spin = 0.0;
            int timer = 0;
            int beat = 0;
            int mix = 0;
            final List<ItemDisplay> frags;

            {
                ArrayList var47 = new ArrayList();
                this.frags = var47;
            }

            public void run() {
                Server var44 = GoldenDreamRitual.this.plugin.getServer();
                UUID var223 = var20;
                Player var30 = var44.getPlayer(var223);
                if (var30 != null && var30.isOnline() && !var30.isDead()) {
                    if (this.t >= 200) {
                        if (this.t == 200) {
                            for (Player var523 : var138.getPlayers()) {
                                Location var182 = var523.getLocation();
                                Location var279 = var22;
                                if (var182.distanceSquared(var279) < 4096.0) {
                                    String var280 = "bliss:goldgem_dream_shower";
                                    SoundCategory var352 = SoundCategory.MASTER;
                                    var523.stopSound(var280, var352);
                                }
                            }
                        }

                        int var355 = this.t - 200;
                        if (this.cover(var30, var355, 1426147298)) {
                            this.release(var30, false, 1640348751);
                            this.cancel();
                            BlissPlugin var359 = GoldenDreamRitual.this.plugin;
                            GoldenDreamRitual var391 = GoldenDreamRitual.this;
                            Location var431 = var23.clone();
                            UUID var438 = var20;
                            GoldenDreamMass var191 = new GoldenDreamMass(var359, var391, var431, var438, 1697825634);
                            var191.schedule(200L, 992733272);
                        }

                        int var286 = this.t + 1;
                        this.t = var286;
                    } else {
                        SignatureClock.send(var30, 80000L, 16001L, 399305443);
                        if (this.t == 0) {
                            List var3 = List.of();
                            FragmentCoreRitual.CamGroup var228 = FragmentCoreRitual.CamGroup.start(var30, var3, 1794988348);
                            this.cam = var228;
                        }

                        if (this.cam != null) {
                            double var297 = (double)this.t / 30.0;
                            double var31 = Math.min(1.0, var297);
                            FragmentCoreRitual.CamGroup var64 = this.cam;
                            double var368 = 11.2 * var31;
                            double var231 = 7.0 + var368;
                            double var434 = 0.18 * var31;
                            double var372 = 0.04 + var434;
                            var64.publish(var231, var372, 877774164);
                        }

                        if (this.t % 2 == 0) {
                            GoldenDreamRitual var69 = GoldenDreamRitual.this;
                            World var235 = var138;
                            Location var300 = var23;
                            double var375 = this.spin;
                            var69.drawBraid(var235, var300, var375, 1785724786);
                        }

                        double var238 = this.spin + 0.0;
                        this.spin = var238;
                        if (var21 != null) {
                            int var241 = this.timer - 1;
                            this.timer = var241;
                            if (var241 <= 0) {
                                int var243 = FragmentCoreRitual.slow(4, 1516685867);
                                this.timer = var243;
                                FragmentCoreRitual var76 = var21;
                                World var245 = var138;
                                Location var303 = var22;
                                var76.flash(var245, var303, 1, 0.0, 0.0, 0.0, 2059459588);
                                int[] var77 = FragmentCoreRitual.AFTERMATH_BAR_RINGS;
                                int var304 = this.beat;
                                int var380 = var304 + 1;
                                this.beat = var380;
                                int var306 = FragmentCoreRitual.AFTERMATH_BAR_RINGS.length;
                                int var247 = var304 % var306;
                                int var511 = var77[var247];
                                ThreadLocalRandom var33 = ThreadLocalRandom.current();

                                for (int var34 = 0; var34 < var511; var34++) {
                                    Particle[] var82 = GoldenDreamRitual.SHOWER_MIX;
                                    int var307 = this.mix;
                                    int var381 = var307 + 1;
                                    this.mix = var381;
                                    int var309 = GoldenDreamRitual.SHOWER_MIX.length;
                                    int var250 = var307 % var309;
                                    Particle var35 = var82[var250];
                                    double var36 = var33.nextDouble();
                                    double var314 = var36 * var36 * 1.2000000000000002;
                                    double var39 = 1.0 + var314;
                                    Particle var251 = Particle.SCULK_CHARGE;
                                    byte var94;
                                    if (var35 == var251) {
                                        var94 = 45;
                                    } else {
                                        var94 = 30;
                                    }

                                    FragmentCoreRitual var93 = var21;
                                    World var253 = var138;
                                    Location var317 = var22;
                                    double var440 = (double)var94;
                                    double var490 = 0.4 * var39;
                                    double var468 = 0.6 + var490;
                                    int var444 = (int)(var440 * var468);
                                    double var472 = 0.55 * var39;
                                    var93.angledBurst(var253, var317, false, 45.0, var444, var35, var472, 1595198679);
                                }
                            }
                        }

                        if (this.t == 0) {
                            for (Player var520 : var138.getPlayers()) {
                                Location var107 = var520.getLocation();
                                Location var255 = var22;
                                if (var107.distanceSquared(var255) < 4096.0) {
                                    Location var257 = var22;
                                    String var320 = "bliss:goldgem_dream_shower";
                                    SoundCategory var383 = SoundCategory.MASTER;
                                    var520.playSound(var257, var320, var383, 1.4F, 1.0F);
                                }
                            }
                        }

                        byte var513 = 0;

                        label141:
                        while (true) {
                            int var259 = GoldenDreamRitual.CLIP_BEATS.length;
                            if (var513 >= var259) {
                                break;
                            }

                            int[] var114 = GoldenDreamRitual.CLIP_BEATS;
                            int var115 = var114[var513];
                            int var262 = this.t;
                            if (var115 == var262) {
                                boolean[] var116 = GoldenDreamRitual.CLIP_BIG;
                                boolean var521 = var116[var513];
                                double var221;
                                if (var521) {
                                    var221 = 2.4;
                                } else {
                                    var221 = 1.44;
                                }

                                double var526 = var221;
                                byte var169;
                                if (var521) {
                                    var169 = 40;
                                } else {
                                    var169 = 28;
                                }

                                byte var530 = var169;
                                World var121 = var138;
                                Particle var264 = Particle.END_ROD;
                                Location var322 = var22;
                                byte var385;
                                if (var521) {
                                    var385 = 26;
                                } else {
                                    var385 = 14;
                                }

                                Particles.spawn(var121, var264, var322, var385, 0.4, 0.4, 0.4, 0.06, 1986132876);
                                int var38 = 0;

                                while (true) {
                                    if (var38 >= var530) {
                                        break label141;
                                    }

                                    double var324 = (double)var38;
                                    double var126 = (Math.PI * 2) * var324;
                                    double var327 = (double)var530;
                                    double var533 = var126 / var327;
                                    World var131 = var138;
                                    Particle var266 = Particle.POOF;
                                    double var331 = var22.getX();
                                    double var407 = var22.getY();
                                    double var449 = var22.getZ();
                                    double var488 = Math.cos(var533);
                                    double var509 = Math.sin(var533);
                                    Particles.spawn(var131, var266, var331, var407, var449, 0, var488, 0.0, var509, var526, null, true, 954482185);
                                    var38++;
                                }
                            }

                            var513 += 1;
                        }

                        if (this.t == 0 && var21 != null) {
                            for (ItemStack var522 : var29) {
                                List var145 = this.frags;
                                FragmentCoreRitual var268 = var21;
                                Location var334 = var23;
                                ItemDisplay var269 = var268.spawnDisplay(var334, var522, 0.7F, 1550711431);
                                var145.add(var269);
                            }
                        }

                        double var414 = (double)this.t / 200.0;
                        double var337 = 1.0 - var414;
                        double var515 = 4.0 * var337;
                        byte var528 = 0;

                        while (true) {
                            int var272 = this.frags.size();
                            if (var528 >= var272) {
                                int var274 = this.t + 1;
                                this.t = var274;
                                return;
                            }

                            List var155 = this.frags;
                            ItemDisplay var529 = (ItemDisplay)var155.get(var528);
                            if (var529.isValid()) {
                                double var161 = (double)var528;
                                double var419 = (double)this.frags.size();
                                double var342 = (Math.PI * 2) / var419;
                                double var163 = var161 * var342;
                                double var348 = (double)this.t * 0.1;
                                double var531 = var163 + var348;
                                World var388 = var138;
                                double var425 = var23.getX();
                                double var478 = this.spin;
                                double var457 = Math.cos(var531 + var478);
                                double var459 = var457 * var515;
                                double var427 = var425 + var459;
                                double var463 = var23.getY();
                                double var484 = var23.getZ();
                                double var505 = this.spin;
                                double var500 = Math.sin(var531 + var505);
                                double var502 = var500 * var515;
                                double var486 = var484 + var502;
                                Location var276 = new Location(var388, var427, var463, var486);
                                var529.teleport(var276);
                            }

                            var528 += 1;
                        }
                    }
                } else {
                    for (Player var524 : var138.getPlayers()) {
                        Location var203 = var524.getLocation();
                        Location var288 = var22;
                        if (var203.distanceSquared(var288) < 4096.0) {
                            String var289 = "bliss:goldgem_dream_shower";
                            SoundCategory var363 = SoundCategory.MASTER;
                            var524.stopSound(var289, var363);
                        }
                    }

                    for (ItemStack var525 : var29) {
                        World var219 = var138;
                        Location var292 = var2;
                        var219.dropItem(var292, var525);
                    }

                    this.release(var30, true, 1640348751);
                    this.cancel();
                }
            }

            private boolean cover(Player var1, int var2x, int var3) {
                if (var2x == 0) {
                    for (ItemDisplay var68 : this.frags) {
                        if (var68.isValid()) {
                            var68.remove();
                        }
                    }

                    this.frags.clear();
                    if (this.cam != null) {
                        this.cam.stop(373212778);
                        this.cam = null;
                    }
                }

                if (var2x < 20) {
                    SignatureClock.send(var1, 80000L, 16121L, 399305443);
                    return false;
                } else {
                    double var60 = (double)(var2x - 20);
                    double var65 = (double)Math.max(1, 12);
                    double var62 = var60 / var65;
                    double var56 = 1.0 - var62;
                    int var15 = (int)Math.round(15.0 * var56);
                    if (var15 > 0 && var21 != null) {
                        FragmentCoreRitual var43 = var21;
                        var43.applyCoverDay(var1, var15, 5838947);
                        return false;
                    } else {
                        return true;
                    }
                }
            }

            private void release(Player var1, boolean var2x, int var3) {
                if (var2x) {
                    Map var11 = GoldenDreamRitual.this.entryAborts;
                    UUID var38 = var20;
                    var11.remove(var38);
                }

                if (this.cam != null) {
                    this.cam.stop(373212778);
                    this.cam = null;
                }

                for (ItemDisplay var8 : this.frags) {
                    if (var8.isValid()) {
                        var8.remove();
                    }
                }

                this.frags.clear();
                if (var1 != null && var1.isOnline()) {
                    long var41 = var24;
                    boolean var45 = var26;
                    var1.setPlayerTime(var41, var45);
                }

                if (var2x) {
                    GoldenDreamRitual var37 = GoldenDreamRitual.this;
                    UUID var44 = var20;
                    var37.finish(var44, 1863011177);
                }
            }
        };
        BlissPlugin var93 = this.plugin;
        var68.runTaskTimer(var93, 1L, 1L);
    }

    private void drawBraid(World var1, Location var2, double var3, int var5) {
        Color var7 = FragmentCoreRitual.HOLDER_GOLD;
        DustOptions var6 = new DustOptions(var7, 0.5F);
        DustOptions var26 = var6;
        double var80 = Math.sqrt(23.84);
        int var29 = (int)Math.ceil((Math.PI * 2) * var80 / 0.09);

        for (int var30 = 0; var30 < 2; var30++) {
            double var31 = (double)var30 * Math.PI;

            for (int var33 = 0; var33 < var29; var33++) {
                double var63 = (double)var33;
                double var101 = (double)var29;
                double var88 = (Math.PI * 2) / var101;
                double var34 = var63 * var88;
                double var68 = var2.getY();
                double var105 = 4.0 * var34;
                double var109 = Math.sin(var105 + var31);
                double var92 = 0.7 * var109;
                double var36 = var68 + var92;
                double var38 = var34 + var3;
                Particle var79 = Particle.DUST;
                double var97 = var2.getX();
                double var115 = Math.cos(var38) * 4.0;
                double var99 = var97 + var115;
                double var124 = var2.getZ();
                double var130 = Math.sin(var38) * 4.0;
                double var126 = var124 + var130;
                Particles.spawn(var1, var79, var99, var36, var126, 1, 0.0, 0.0, 0.0, 0.0, var26, true, 954482185);
            }
        }
    }

    public void finish(Player var1) {
        if (var1 != null) {
            Map var5 = this.running;
            UUID var7 = var1.getUniqueId();
            var5.remove(var7);
        }
    }

    void finish(UUID var1, int var2) {
        if (var1 != null) {
            Map var6 = this.running;
            var6.remove(var1);
        }
    }

    void register(GoldenDreamMass var1, int var2) {
        Set var5 = this.masses;
        var5.add(var1);
    }

    void unregister(GoldenDreamMass var1, int var2) {
        Set var5 = this.masses;
        var5.remove(var1);
    }

    void entryStarted(UUID var1, int var2) {
        Map var5 = this.entryAborts;
        var5.remove(var1);
    }

    public void abortAll(int var1) {
        Collection<Runnable> var31 = this.entryAborts.values();

        for (Runnable var5 : new ArrayList<>(var31)) {
            try {
                var5.run();
            } catch (Throwable var37) {
            }
        }

        this.entryAborts.clear();
        Set<GoldenDreamMass> var33 = this.masses;

        for (GoldenDreamMass var35 : new ArrayList<>(var33)) {
            try {
                var35.abortNow(172450522);
            } catch (Throwable var36) {
            }
        }

        this.masses.clear();
        this.running.clear();
    }

    public boolean isRunning(Player var1, int var2) {
        if (var1 != null) {
            Boolean var6 = Boolean.TRUE;
            Map var9 = this.running;
            UUID var11 = var1.getUniqueId();
            Object var10 = var9.get(var11);
            if (var6.equals(var10)) {
                return true;
            }
        }

        return false;
    }

    static {
        Particle[] var3 = new Particle[3];
        Particle var2 = Particle.SOUL;
        var3[0] = var2;
        Particle var30 = Particle.END_ROD;
        var3[1] = var30;
        Particle var31 = Particle.SCULK_CHARGE;
        var3[2] = var31;
        SHOWER_MIX = var3;
        int[] var5 = new int[]{6, 20, 37, 58, 79, 82, 98, 137, 158, 178};
        CLIP_BEATS = var5;
        boolean[] var7 = new boolean[]{true, true, true, false, false, false, true, true, true, false};
        CLIP_BIG = var7;
    }
}
