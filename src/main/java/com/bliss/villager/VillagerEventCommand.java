package com.bliss.villager;

import com.bliss.BlissPlugin;
import com.bliss.admin.BlissCommand;
import com.bliss.admin.CommandHelp;
import com.bliss.core.GlobalState;
import com.bliss.core.Particles;
import com.bliss.items.BlissCoreItems;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Villager.Type;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class VillagerEventCommand implements CommandExecutor, TabCompleter {
    private final BlissPlugin plugin;
    private final EventState state;
    private final BlissItems items;
    private static final String LOST_SOULS_SOUND = "bliss:lost_souls";
    private static final long LOST_SOULS_AUDIO_TICKS = 547L;
    private static final long LEAD_IN_TICKS = 0L;
    private static final float LOST_SOULS_VOLUME = 1000000.0F;
    private static final double TEXT_PACE = 1.04;
    private static BukkitTask pendingHuntStart;
    private static final double RAID_BURST_Y = 2.0;
    private static final int RAID_BURST_PTS = 72;
    private static final double RAID_BURST_SPEED = 2.4;
    private static final int INTRO_BOLTS = 7;
    private static final int BOLT_STAGGER = 5;
    private static final int STROKE_PER_TICK = 7;
    private static final double INTRO_BOLT_R = 18.0;
    private static final double INTRO_BOLT_H = 14.0;
    private static final int INTRO_BOLT_SEGS = 10;
    private static final int INTRO_BOLT_PTS = 6;
    private static final double INTRO_BOLT_JITTER = 1.6;
    private static final Particle SWARM_PARTICLE = Particle.CRIMSON_SPORE;
    private static final int SWARM_AT = 50;
    private static final int SWARM_TICKS = 36;
    private static final int SWARM_MOTES = 90;
    private static final double SWARM_R = 16.0;
    private static final double SWARM_Y_SPREAD = 8.0;
    private static final double SWARM_CURL = 0.05;
    private static final DustOptions RED_DUST;
    private static final int DUST_AT = 90;
    private static final int DUST_TICKS = 10;
    private static final int DUST_PER_TICK = 70;
    private static final double DUST_R = 14.0;
    private static final int FLAME_AT = 102;
    private static final int FLAME_RING_PTS = 64;
    private static final double FLAME_SPEED = 1.2;
    private static final long INTRO_TICKS = 118L;
    private static boolean raidScheduled = false;

    public VillagerEventCommand(BlissPlugin var1, EventState var2, BlissItems var3, int var4) {
        this.plugin = var1;
        this.state = var2;
        this.items = var3;
    }

    public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
        if (var4.length == 0) {
            this.usage(var1, 12567314);
            return true;
        } else {
            String var8 = var4[0].toLowerCase();
            switch (var8) {
                case "phase":
                    this.handlePhase(var1, var4, 2099870556);
                    break;
                case "end":
                    this.handleEnd(var1, 1312695705);
                    break;
                case "compass":
                    this.handleCompass(var1, var4, 1771138913);
                    break;
                case "villager":
                    this.handleVillager(var1, var4, 800404316);
                    break;
                case "give":
                    this.handleGive(var1, var4, 1032267802);
                    break;
                default:
                    this.usage(var1, 12567314);
            }

            return true;
        }
    }

    private void handlePhase(CommandSender var1, String[] var2, int var3) {
        if (var2.length < 2) {
            String var29 = "/blissevent phase <1|2|3>";
            CommandHelp.hint(var1, var29, 80456809);
        } else {
            String var6 = var2[1];
            switch (var6) {
                case "1":
                    this.handlePhase1(var1, 1704540675);
                    break;
                case "2":
                    this.handlePhase2(var1, 196565015);
                    break;
                case "3":
                    this.handlePhase3(var1, 308749420);
                    break;
                default:
                    String var32 = "/blissevent phase <1|2|3>";
                    CommandHelp.hint(var1, var32, 80456809);
            }
        }
    }

    private void handlePhase1(CommandSender var1, int var2) {
        if (DiscSummonRitual.isRunning(1066722316)) {
            Component var14 = Text.legacy("&eThe disc ritual is already running.", 373192400);
            var1.sendMessage(var14);
        } else {
            BlissPlugin var15 = this.plugin;
            DiscSummonRitual var6 = new DiscSummonRitual(var15, 1140349632);
            if (!var6.summon(2064464609)) {
                Component var12 = Text.legacy("&eNo pedestal location is set — disc ritual skipped. Set one with /bliss pedestal first.", 373192400);
                var1.sendMessage(var12);
            }
        }
    }

    private void handlePhase2(CommandSender var1, int var2) {
        if (this.state.compassLoc(561536293) == null) {
            Component var40 = Text.legacy("&cSet compass first: /blissevent compass set", 373192400);
            var1.sendMessage(var40);
        } else {
            if (VillagerRaid.current(137107344) != null) {
                VillagerRaid.current(137107344).stop(true, 917832850);
            }

            this.state.setRaidWave(0, 415126607);
            this.state.setRaidBetween(false, 2064038361);
            this.state.setVillagersReturned(0, 205753343);
            this.state.clearReturnedVillagers(1392763794);
            this.state.setFinalActive(false, 1061721000);
            this.plugin.globalState(724248306).setVillagersAuto(false, 2036260727);
            this.plugin.globalState(724248306).save(343464104);

            for (Player var9 : Bukkit.getOnlinePlayers()) {
                Location var48 = var9.getLocation();
                Sound var5 = Sound.UI_TOAST_CHALLENGE_COMPLETE;
                var9.playSound(var48, var5, 1.0F, 1.0F);
            }

            this.playLostSoulsIntro(1584166020);
        }
    }

    private void playLostSoulsIntro(int var1) {
        String[] var30 = new String[18];
        String var4 = "we are the last of our kind..";
        var30[0] = var4;
        String var181 = "three souls who yet remain";
        var30[1] = var181;
        String var182 = "find us";
        var30[2] = var182;
        String var183 = "before all is truly lost";
        var30[3] = var183;
        String var184 = "we beg of you";
        var30[4] = var184;
        String var185 = "rise against our captors";
        var30[5] = var185;
        String var186 = "and restore what once was";
        var30[6] = var186;
        String var187 = "do not falter";
        var30[7] = var187;
        String var188 = "do not be swayed by";
        var30[8] = var188;
        String var189 = "the shimmer of emeralds";
        var30[9] = var189;
        String var190 = "or our super duper";
        var30[10] = var190;
        String var191 = "really...";
        var30[11] = var191;
        String var192 = "crazy good trades..";
        var30[12] = var192;
        String var193 = "You";
        var30[13] = var193;
        String var194 = "Hold the hope of our people";
        var30[14] = var194;
        String var195 = "bring us home";
        var30[15] = var195;
        String var196 = "and rekindle";
        var30[16] = var196;
        String var197 = "the spirit of the villages";
        var30[17] = var197;
        boolean var38;
        if (this.plugin.globalState(724248306) != null && !this.plugin.globalState(724248306).lostSoulsAudio(951200401)) {
            var38 = false;
        } else {
            var38 = true;
        }

        if (var38) {
            for (Player var11 : Bukkit.getOnlinePlayers()) {
                String var148 = "bliss:lost_souls";
                SoundCategory var198 = SoundCategory.MASTER;
                var11.playSound(var11, var148, var198, 1000000.0F, 1.0F);
            }
        }

        int var237 = 0;

        for (String var243 : var30) {
            int var130 = var243.length();
            var237 += var130;
        }

        if (var237 == 0) {
            var237 = 1;
        }

        long var239 = 0L;
        boolean var241 = true;

        for (String var17 : var30) {
            int var117 = var17.length();
            int var18 = Math.max(1, var117);
            long var151 = 568L * var239;
            long var211 = (long)var237;
            long var153 = var151 / var211;
            long var19 = 0L + var153;
            long var235 = (long)var18;
            long var215 = var239 + var235;
            long var157 = 568L * var215;
            long var218 = (long)var237;
            long var159 = var157 / var218;
            long var21 = 0L + var159;
            long var77 = (long)var18;
            long var163 = var21 - var19;
            long var23 = Math.max(var77, var163);

            for (int var25 = 1; var25 <= var18; var25++) {
                String var26 = var17.substring(0, var25);
                boolean var27 = var241;
                var241 = false;
                long var224 = (long)(var25 - 1);
                long var171 = var23 * var224;
                long var227 = (long)var18;
                long var173 = var171 / var227;
                long var28 = var19 + var173;
                BukkitScheduler var95 = Bukkit.getScheduler();
                BlissPlugin var121 = this.plugin;
                Runnable var176 = () -> {
                    Component var12 = Text.legacy("&6&lLost Souls:", 373192400);
                    Component var23x = Text.legacy("&f" + var26, 373192400);
                    long var25x;
                    if (var27) {
                        var25x = 8L;
                    } else {
                        var25x = 0L;
                    }

                    Duration var27x = ticks(var25x, 2121410567);
                    Duration var29 = ticks(120L, 2121410567);
                    Duration var30x = ticks(0L, 2121410567);
                    Times var28x = Times.times(var27x, var29, var30x);
                    Title var9 = Title.title(var12, var23x, var28x);

                    for (Player var21x : Bukkit.getOnlinePlayers()) {
                        var21x.showTitle(var9);
                    }
                };
                var95.runTaskLater(var121, var176, var28);
            }

            long var166 = (long)var18;
            var239 += var166;
        }

        BukkitScheduler var97 = Bukkit.getScheduler();
        BlissPlugin var123 = this.plugin;
        Runnable var177 = () -> {
            for (Player var6 : Bukkit.getOnlinePlayers()) {
                Component var16 = Text.legacy("&e(!)Find the lost souls.", 373192400);
                var6.sendMessage(var16);
                Location var18x = var6.getLocation();
                Sound var2 = Sound.BLOCK_AMETHYST_BLOCK_PLACE;
                var6.playSound(var18x, var2, 1.0F, 1.0F);
                Location var20 = var6.getLocation();
                Sound var21x = Sound.BLOCK_BEACON_POWER_SELECT;
                var6.playSound(var20, var21x, 1.0F, 1.0F);
            }
        };
        var97.runTaskLater(var123, var177, 578L);
        BukkitScheduler var99 = Bukkit.getScheduler();
        BlissPlugin var125 = this.plugin;
        Runnable var178 = () -> {
            for (Player var6 : Bukkit.getOnlinePlayers()) {
                Location var14 = var6.getLocation();
                Sound var2 = Sound.BLOCK_BEACON_ACTIVATE;
                var6.playSound(var14, var2, 1.0F, 1.0F);
            }
        };
        var99.runTaskLater(var125, var178, 580L);
        if (pendingHuntStart != null) {
            pendingHuntStart.cancel();
        }

        BukkitTask[] var242 = new BukkitTask[1];
        BukkitScheduler var179 = Bukkit.getScheduler();
        BlissPlugin var207 = this.plugin;
        Runnable var230 = () -> {
            BukkitTask var2 = pendingHuntStart;
            BukkitTask var10 = var242[0];
            if (var2 == var10) {
                pendingHuntStart = null;
            }

            this.state.setActive(true, 389460485);
            this.state.save(1112893273);
        };
        BukkitTask var180 = var179.runTaskLater(var207, var230, 586L);
        var242[0] = var180;
        pendingHuntStart = var242[0];
    }

    private static Duration ticks(long var0, int var2) {
        return Duration.ofMillis(var0 * 50L);
    }

    private void handlePhase3(CommandSender var1, int var2) {
        if (!this.state.isEventRunning(527098672)) {
            this.state.setActive(true, 389460485);
        }

        BlissPlugin var10 = this.plugin;
        EventState var12 = this.state;
        beginRaid(var10, var12, 1543134889);
    }

    public static void beginRaid(BlissPlugin var0, EventState var1, int var2) {
        if (var1.isEventRunning(527098672)) {
            if (pendingHuntStart != null) {
                pendingHuntStart.cancel();
                pendingHuntStart = null;
            }

            var1.setActive(false, 389460485);
            var1.setFinalActive(true, 1061721000);
            var1.save(1112893273);
            raidOpeningShot(var0, var1, 103568635);
            if (VillagerRaid.current(137107344) == null && !raidScheduled) {
                Location var8 = var1.compassLoc(561536293);
                if (var8 == null || var8.getWorld() == null) {
                    var8 = firstOnlinePlayerLoc(2068834975);
                }

                if (var8 != null) {
                    Location var9 = var8;
                    raidScheduled = true;
                    BukkitScheduler var31 = Bukkit.getScheduler();
                    Runnable var37 = () -> {
                        raidScheduled = false;
                        if (var1.isFinalActive(245445441)) {
                            if (VillagerRaid.current(137107344) == null) {
                                VillagerRaid var10 = new VillagerRaid(var0, var1, var9, 205368423);
                                var10.start(104815921);
                            }
                        }
                    };
                    var31.runTaskLater(var0, var37, 118L);
                } else {
                    var0.getLogger().warning("Villager raid: no compass location and no players online — raid not spawned.");
                }
            }
        }
    }

    public static void failRaid(BlissPlugin var0, EventState var1, int var2) {
        if (var1.isFinalActive(245445441)) {
            VillagerRaid var8 = VillagerRaid.current(137107344);
            if (var8 != null) {
                var8.stop(true, 917832850);
            }

            var1.setFinalActive(false, 1061721000);
            var1.setActive(true, 389460485);
            var1.setRaidWave(0, 415126607);
            var1.setRaidBetween(false, 2064038361);
            var1.setVillagersReturned(0, 205753343);
            var1.clearReturnedVillagers(1392763794);
            var1.save(1112893273);

            for (Player var10 : Bukkit.getOnlinePlayers()) {
                Component var38 = Text.legacy("&4&l✖ THE VILLAGE HAS FALLEN &7— the last raid is lost.", 373192400);
                var10.sendMessage(var38);
                Component var40 = Text.legacy("&e(!)Return all three souls to the compass to raise the village again.", 373192400);
                var10.sendMessage(var40);
                Location var42 = var10.getLocation();
                Sound var5 = Sound.ENTITY_WITHER_SPAWN;
                var10.playSound(var42, var5, 1.0F, 0.6F);
            }
        }
    }

    private static void raidOpeningShot(BlissPlugin var0, EventState var1, int var2) {
        Location var21 = var1.compassLoc(561536293);
        if (var21 != null && var21.getWorld() != null) {
            final World var22 = var21.getWorld();
            final Location var23 = var21.clone().add(0.0, 2.0, 0.0);
            Sound var6 = Sound.BLOCK_BEACON_AMBIENT;
            var22.playSound(var23, var6, 50.0F, 1.6F);
            Sound var87 = Sound.ENTITY_WARDEN_SONIC_BOOM;
            var22.playSound(var23, var87, 20.0F, 1.0F);
            Sound var88 = Sound.ENTITY_GENERIC_EXPLODE;
            var22.playSound(var23, var88, 20.0F, 0.75F);

            for (int var24 = 0; var24 < 72; var24++) {
                double var90 = (double)var24;
                double var25 = (Math.PI * 2) * var90 / 72.0;
                Particle var78 = Particle.END_ROD;
                double var125 = Math.cos(var25);
                double var156 = Math.sin(var25);
                Particles.spawn(var22, var78, var23, 0, var125, 0.0, var156, 2.4, null, true, 1748031581);
            }

            ThreadLocalRandom var161 = ThreadLocalRandom.current();
            ArrayList var54 = new ArrayList();
            final ArrayList var162 = var54;
            final int[] var27 = new int[7];

            for (int var28 = 0; var28 < 7; var28++) {
                List var86 = boltPath(var22, var23, var161, 1206382457);
                var162.add(var86);
            }

            final double[] var163 = new double[90];
            final double[] var29 = new double[90];
            final double[] var30 = new double[90];

            for (int var31 = 0; var31 < 90; var31++) {
                double var103 = var161.nextDouble() * Math.PI * 2.0;
                var163[var31] = var103;
                double var150 = var161.nextDouble() * 0.25;
                double var136 = 0.75 + var150;
                double var107 = 16.0 * var136;
                var29[var31] = var107;
                double var110 = var23.getY();
                double var143 = (var161.nextDouble() - 0.35) * 8.0;
                double var112 = var110 + var143;
                var30[var31] = var112;
            }

            BukkitRunnable var67 = new BukkitRunnable() {
                int t = 0;

                public void run() {
                    for (int var21 = 0; var21 < 7; var21++) {
                        int var322 = var21 * 5;
                        int var102 = this.t;
                        if (var102 >= var322) {
                            int[] var104 = var27;
                            int var105 = var104[var21];
                            List var154 = var162;
                            int var157 = ((List)var154.get(var21)).size();
                            if (var105 < var157) {
                                List var107 = var162;
                                List var323 = (List)var107.get(var21);
                                int var111 = var323.size();
                                int[] var160 = var27;
                                int var162x = var160[var21] + 7;
                                int var324 = Math.min(var111, var162x);
                                int[] var114 = var27;

                                for (int var27x = var114[var21]; var27x < var324; var27x++) {
                                    World var125 = var22;
                                    Particle var170 = Particle.WAX_OFF;
                                    Location var224 = (Location)var323.get(var27x);
                                    Particles.spawn(var125, var170, var224, 1, 0.0, 0.0, 0.0, 0.0, 1986132876);
                                }

                                int[] var118 = var27;
                                if (var118[var21] == 0) {
                                    World var121 = var22;
                                    Location var168 = (Location)var323.get(0);
                                    Sound var220 = Sound.ENTITY_LIGHTNING_BOLT_THUNDER;
                                    var121.playSound(var168, var220, 3.0F, 1.4F);
                                }

                                int[] var123 = var27;
                                var123[var21] = var324;
                            }
                        }
                    }

                    if (this.t >= 50 && this.t < 86) {
                        double var317 = (double)(this.t - 50) / 36.0;

                        for (int var24 = 0; var24 < 90; var24++) {
                            double[] var45 = var29;
                            double var46 = var45[var24];
                            double var173 = 1.0 - var317;
                            double var25 = var46 * var173;
                            double[] var51 = var30;
                            double var52 = var51[var24];
                            double var177 = var23.getY();
                            double[] var236 = var30;
                            double var237 = var236[var24];
                            double var179 = var177 - var237;
                            double var181 = var179 * var317;
                            double var28 = var52 + var181;
                            World var57 = var22;
                            Particle var132 = VillagerEventCommand.SWARM_PARTICLE;
                            double var185 = var23.getX();
                            double[] var242 = var163;
                            double var245 = Math.cos(var242[var24]);
                            double var247 = var245 * var25;
                            double var187 = var185 + var247;
                            double var270 = var23.getZ();
                            double[] var284 = var163;
                            double var287 = Math.sin(var284[var24]);
                            double var289 = var287 * var25;
                            double var272 = var270 + var289;
                            Particles.spawn(var57, var132, var187, var28, var272, 1, 0.0, 0.0, 0.0, 0.0, 1717927553);
                            double[] var59 = var163;
                            double var191 = var59[var24] + 0.05;
                            var59[var24] = var191;
                        }

                        if (this.t == 50) {
                            World var63 = var22;
                            Location var136 = var23;
                            Sound var193 = Sound.BLOCK_RESPAWN_ANCHOR_CHARGE;
                            var63.playSound(var136, var193, 6.0F, 0.6F);
                        }
                    }

                    if (this.t >= 90 && this.t < 100) {
                        double var198 = (double)(this.t - 90 + 1);
                        double var319 = 14.0 * var198 / 10.0;
                        World var75 = var22;
                        Particle var139 = Particle.DUST;
                        Location var203 = var23;
                        double var256 = var319 * 0.6;
                        double var278 = var319 * 0.4;
                        double var296 = var319 * 0.6;
                        DustOptions var16 = VillagerEventCommand.RED_DUST;
                        Particles.spawn(var75, var139, var203, 70, var256, var278, var296, 0.0, var16, true, 1748031581);
                        if (this.t == 90) {
                            World var79 = var22;
                            Location var142 = var23;
                            Sound var204 = Sound.ENTITY_GENERIC_EXPLODE;
                            var79.playSound(var142, var204, 8.0F, 1.4F);
                        }
                    }

                    if (this.t == 102) {
                        for (byte var321 = 0; var321 < 64; var321 += 1) {
                            double var206 = (double)var321;
                            double var22x = (Math.PI * 2) * var206 / 64.0;
                            World var91 = var22;
                            Particle var145 = Particle.FLAME;
                            Location var211 = var23;
                            double var261 = Math.cos(var22x);
                            double var300 = Math.sin(var22x);
                            Particles.spawn(var91, var145, var211, 0, var261, 0.05, var300, 1.2, null, true, 1748031581);
                        }

                        World var93 = var22;
                        Location var147 = var23;
                        Sound var212 = Sound.ITEM_FIRECHARGE_USE;
                        var93.playSound(var147, var212, 8.0F, 0.7F);
                    }

                    int var149 = this.t + 1;
                    this.t = var149;
                    if ((long)var149 > 118L) {
                        this.cancel();
                    }
                }
            };
            var67.runTaskTimer(var0, 1L, 1L);
        }
    }

    private static List<Location> boltPath(World var0, Location var1, ThreadLocalRandom var2, int var3) {
        double var21 = var2.nextDouble() * Math.PI * 2.0;
        double var128 = Math.sqrt(var2.nextDouble());
        double var23 = 18.0 * var128;
        double var61 = var1.getX();
        double var132 = Math.cos(var21);
        double var134 = var132 * var23;
        double var25 = var61 + var134;
        double var66 = var1.getZ();
        double var138 = Math.sin(var21);
        double var140 = var138 * var23;
        double var27 = var66 + var140;
        int var118 = (int)Math.floor(var25);
        int var146 = (int)Math.floor(var27);
        double var29 = (double)(var0.getHighestBlockYAt(var118, var146) + 1);
        double var31 = var29 + 14.0;
        ArrayList var79 = new ArrayList();
        ArrayList var33 = var79;
        double var34 = var25;
        double var36 = var27;

        for (int var38 = 1; var38 <= 10; var38++) {
            double var151 = var31 - var29;
            double var186 = (double)var38;
            double var155 = var151 * var186 / 10.0;
            double var39 = var31 - var155;
            double var159 = var31 - var29;
            double var194 = (double)(var38 - 1);
            double var163 = var159 * var194 / 10.0;
            double var41 = var31 - var163;
            double var170 = (var2.nextDouble() - 0.5) * 1.6;
            double var43 = var25 + var170;
            double var177 = (var2.nextDouble() - 0.5) * 1.6;
            double var45 = var27 + var177;

            for (int var47 = 0; var47 < 6; var47++) {
                double var48 = (double)var47 / 6.0;
                double var210 = var43 - var34;
                double var212 = var210 * var48;
                double var208 = var34 + var212;
                double var222 = var39 - var41;
                double var224 = var222 * var48;
                double var216 = var41 + var224;
                double var234 = var45 - var36;
                double var236 = var234 * var48;
                double var228 = var36 + var236;
                Location var122 = new Location(var0, var208, var216, var228);
                var33.add(var122);
            }

            var34 = var43;
            var36 = var45;
        }

        return var33;
    }

    private static Location firstOnlinePlayerLoc(int var0) {
        Iterator var2 = Bukkit.getOnlinePlayers().iterator();
        return var2.hasNext() ? ((Player)var2.next()).getLocation() : null;
    }

    private void handleEnd(CommandSender var1, int var2) {
        boolean var6 = this.state.isFinalActive(245445441);
        boolean var7 = this.state.isActive(2119994266);
        boolean var52;
        if (pendingHuntStart != null) {
            var52 = true;
        } else {
            var52 = false;
        }

        if (!var6 && !var7 && !var52) {
            Component var54 = Text.legacy("&cNo event is running.", 373192400);
            var1.sendMessage(var54);
        } else {
            if (var52) {
                pendingHuntStart.cancel();
                pendingHuntStart = null;
            }

            boolean var53;
            if (!var6 && this.state.topRaidDamager(263540791) == null) {
                var53 = false;
            } else {
                var53 = true;
            }

            if (var53) {
                BlissPlugin var29 = this.plugin;
                EventState var56 = this.state;
                VillagerRaid.crownTopDamager(var29, var56, 1946505142);
            }

            this.state.endEvent(372527669);
            GlobalState var34 = this.plugin.globalState(724248306);
            boolean var62;
            if (this.plugin.globalState(724248306).stage(1483650410) >= 4) {
                var62 = true;
            } else {
                var62 = false;
            }

            var34.setVillagersAuto(var62, 2036260727);
            this.plugin.globalState(724248306).save(343464104);
            if (var53) {
                if (VillagerRaid.current(137107344) != null) {
                    VillagerRaid.current(137107344).stop(true, 917832850);
                }

                if (this.plugin.globalState(724248306).eventProgression(169255316) && this.plugin.globalState(724248306).raidAdvancesProg4(598254309)) {
                    BlissCommand.applyProgression4(this.plugin, 679055365);
                }
            }

            VillagerRaid.sweepRaiders(927052260);
        }
    }

    private void handleCompass(CommandSender var1, String[] var2, int var3) {
        if (!(var1 instanceof Player var10)) {
            Component var47 = Text.legacy("&cPlayer only.", 373192400);
            var1.sendMessage(var47);
        } else {
            if (var2.length >= 2 && var2[1].equalsIgnoreCase("set")) {
                if (!var10.getLocation().subtract(0.0, 1.0, 0.0).getBlock().getType().isSolid()) {
                    Component var45 = Text.legacy("&cStand on a solid block first — the compass can't be set in mid-air.", 373192400);
                    var1.sendMessage(var45);
                    return;
                }

                EventState var29 = this.state;
                Location var41 = var10.getLocation();
                var29.setCompassLoc(var41, 524975724);
                this.state.save(1112893273);
                Component var43 = Text.legacy("&aVillage compass set to your location.", 373192400);
                var1.sendMessage(var43);
            } else {
                this.usage(var1, 12567314);
            }
        }
    }

    private void handleVillager(CommandSender var1, String[] var2, int var3) {
        if (!(var1 instanceof Player var11)) {
            Component var74 = Text.legacy("&cPlayer only.", 373192400);
            var1.sendMessage(var74);
        } else {
            boolean var73;
            if (var2.length >= 2 && var2[1].equalsIgnoreCase("restock")) {
                var73 = true;
            } else {
                var73 = false;
            }

            if (var2.length >= 3 && (var73 || var2[1].equalsIgnoreCase("set"))) {
                String var83 = var2[2];
                Integer var13 = this.parseId(var83, 2099673991);
                if (var13 == null) {
                    String var84 = "/blissevent villager <set|restock> <1|2|3>";
                    CommandHelp.hint(var11, var84, 80456809);
                } else if (var73) {
                    int var106 = var13;
                    this.handleRestock(var11, var106, 1722397492);
                } else {
                    Villager var14 = null;

                    for (Entity var16 : var11.getNearbyEntities(10.0, 10.0, 10.0)) {
                        if (var16 instanceof Villager) {
                            var14 = (Villager)var16;
                            break;
                        }
                    }

                    if (var14 == null) {
                        Component var88 = Text.legacy("&cNo villager found within 10 blocks.", 373192400);
                        var11.sendMessage(var88);
                    } else {
                        var14.setInvulnerable(false);
                        var14.setSilent(true);
                        var14.setPersistent(true);
                        Component var94 = Cosmetics.villagerName(var13, 527184414);
                        var14.customName(var94);
                        var14.setCustomNameVisible(true);
                        var14.setVillagerLevel(5);
                        Type var97 = Type.PLAINS;
                        var14.setVillagerType(var97);
                        Profession var98 = Profession.TOOLSMITH;
                        var14.setProfession(var98);
                        var14.addScoreboardTag("BlissEventVillager");
                        int var67 = var13;
                        VillagerSetup.applyTrades(var67, var14, 645060823);
                        EventState var69 = this.state;
                        int var102 = var13;
                        var69.setVillager(var102, var14, 607405227);
                        this.state.save(1112893273);
                    }
                }
            } else {
                String var81 = "/blissevent villager <set|restock> <1|2|3>";
                CommandHelp.hint(var11, var81, 80456809);
            }
        }
    }

    private void handleRestock(Player var1, int var2, int var3) {
        EventState var8 = this.state;
        if (!var8.isTradeBoughtAny(var2, 807653419)) {
            Component var25 = Text.legacy("&7Villager &f" + var2 + "&7 has nothing to restock — its one-time trades are unclaimed.", 373192400);
            var1.sendMessage(var25);
        } else {
            EventState var12 = this.state;
            var12.clearTradeBought(var2, 1101962048);
            EventState var14 = this.state;
            Villager var7 = var14.getVillager(var2, 1571302102);
            if (var7 != null && var7.isValid()) {
                VillagerSetup.applyTrades(var2, var7, 645060823);
            }

            this.state.save(1112893273);
            String var34;
            if (var7 != null && var7.isValid()) {
                var34 = "";
            } else {
                var34 = " &7(the villager itself isn't loaded; the rows refresh when it is)";
            }

            Component var31 = Text.legacy("&aVillager &f" + var2 + "&a restocked — its one-time trades can be bought again." + var34, 373192400);
            var1.sendMessage(var31);
        }
    }

    private void handleGive(CommandSender var1, String[] var2, int var3) {
        if (var1 instanceof Player var7) {
            if (var2.length < 2) {
                this.usage(var1, 12567314);
            } else {
                String var8 = var2[1].toLowerCase();
                switch (var8) {
                    case "token":
                        ItemStack var48 = this.items.energyToken(624817347);
                        BlissCoreItems.giveOrDrop(var7, var48, 41085121);
                        break;
                    case "soul":
                        if (var2.length < 3) {
                            this.usage(var1, 12567314);
                            return;
                        }

                        String var52 = var2[2];
                        Integer var10 = this.parseId(var52, 2099673991);
                        if (var10 == null) {
                            this.usage(var1, 12567314);
                            return;
                        }

                        BlissItems var55 = this.items;
                        int var60 = var10;
                        ItemStack var56 = var55.soul(var60, 888486940);
                        BlissCoreItems.giveOrDrop(var7, var56, 41085121);
                        break;
                    default:
                        this.usage(var1, 12567314);
                }
            }
        } else {
            Component var41 = Text.legacy("&cPlayer only.", 373192400);
            var1.sendMessage(var41);
        }
    }

    private Integer parseId(String var1, int var2) {
        String var3 = "1";
        if (!var3.equals(var1)) {
            String var6 = "2";
            if (!var6.equals(var1)) {
                String var8 = "3";
                if (!var8.equals(var1)) {
                    return null;
                }
            }
        }

        return Integer.parseInt(var1);
    }

    private void usage(CommandSender var1, int var2) {
        String var4 = "blissevent";
        String var5 = "Run the Villager event.";
        String var6 = "phase <1|2|3>";
        String var7 = "1 the disc ritual, 2 the hunt, 3 the raid";
        CommandHelp.Entry var13 = CommandHelp.of(var6, var7, 1959232616);
        String var15 = "end";
        String var8 = "End the event and open Progression 4";
        CommandHelp.Entry var16 = CommandHelp.of(var15, var8, 1959232616);
        String var17 = "compass set";
        String var9 = "Point the event compass here";
        CommandHelp.Entry var18 = CommandHelp.of(var17, var9, 1959232616);
        String var19 = "villager set <1|2|3>";
        String var10 = "Place a village's villager here";
        CommandHelp.Entry var20 = CommandHelp.of(var19, var10, 1959232616);
        String var21 = "villager restock <1|2|3>";
        String var11 = "Put its one-time trades back on sale";
        CommandHelp.Entry var22 = CommandHelp.of(var21, var11, 1959232616);
        String var23 = "give <token|soul <1|2|3>>";
        String var12 = "Hand out event items";
        CommandHelp.Entry var24 = CommandHelp.of(var23, var12, 1959232616);
        List var14 = List.of(var13, var16, var18, var20, var22, var24);
        CommandHelp.show(var1, var4, var5, var14, 696249826);
    }

    public List<String> onTabComplete(CommandSender var1, Command var2, String var3, String[] var4) {
        ArrayList var5 = new ArrayList();
        ArrayList var9 = var5;
        if (var4.length == 1) {
            String var107 = "phase";
            String var159 = "end";
            String var170 = "compass";
            String var7 = "villager";
            String var8 = "give";

            for (String var175 : List.of(var107, var159, var170, var7, var8)) {
                String var162 = var4[0].toLowerCase();
                if (var175.startsWith(var162)) {
                    var9.add(var175);
                }
            }
        } else if (var4.length == 2) {
            String var10 = var4[0].toLowerCase();
            switch (var10) {
                case "phase":
                    String var28 = "1";
                    String var123 = "2";
                    String var6 = "3";

                    for (String var13 : List.of(var28, var123, var6)) {
                        String var125 = var4[1];
                        if (var13.startsWith(var125)) {
                            var9.add(var13);
                        }
                    }
                    break;
                case "compass":
                    String var53 = "set";
                    String var134 = var4[1].toLowerCase();
                    if (var53.startsWith(var134)) {
                        var5.add("set");
                    }
                    break;
                case "villager":
                    for (String var179 : List.of("set", "restock")) {
                        String var139 = var4[1].toLowerCase();
                        if (var179.startsWith(var139)) {
                            var9.add(var179);
                        }
                    }
                    break;
                case "give":
                    for (String var178 : List.of("token", "soul")) {
                        String var130 = var4[1].toLowerCase();
                        if (var178.startsWith(var130)) {
                            var9.add(var178);
                        }
                    }
            }
        } else if (var4.length == 3
            && (
                var4[0].equalsIgnoreCase("villager") && (var4[1].equalsIgnoreCase("set") || var4[1].equalsIgnoreCase("restock"))
                    || var4[0].equalsIgnoreCase("give") && var4[1].equalsIgnoreCase("soul")
            )) {
            String var95 = "1";
            String var155 = "2";
            String var168 = "3";

            for (String var174 : List.of(var95, var155, var168)) {
                String var157 = var4[2];
                if (var174.startsWith(var157)) {
                    var9.add(var174);
                }
            }
        }

        return var9;
    }

    static {
        Color var6 = Color.fromRGB(190, 20, 20);
        DustOptions var4 = new DustOptions(var6, 3.0F);
        RED_DUST = var4;
    }
}
