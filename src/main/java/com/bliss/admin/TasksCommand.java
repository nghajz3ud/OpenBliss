package com.bliss.admin;

import com.bliss.BlissPlugin;
import com.bliss.core.Particles;
import com.bliss.entity.MotifSpeechListener;
import com.bliss.goldgem.RitualItems;
import com.bliss.villager.Text;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import net.kyori.adventure.text.Component;
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
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public final class TasksCommand implements CommandExecutor, TabCompleter {
    private final BlissPlugin plugin;
    private static final String ADMIN = "bliss.admin";
    private static final double DROP_AHEAD = 3.0;
    private static final double DROP_ABOVE = 5.0;
    private static final int FIRST_TASK = 1;
    private static final int LAST_TASK = 7;
    private static final long HOLD_AFTER_TYPING = 60L;
    private static final long SHOW_MAX_TICKS = 800L;
    private static final int SNAKES = 3;
    private static final float SNAKE_SIZE = 0.9F;
    private static final double SNAKE_RADIUS = 0.85;
    private static final double SNAKE_SPEED = 0.18;
    private static final double SNAKE_TRAIL = 1.0;
    private static final double SNAKE_PTS_RAD = 6.0;
    private static final double SNAKE_TAIL_FADE = 0.15;
    private static final Color GOLD = Color.fromRGB(255, 215, 115);
    private static final double TAU = Math.PI * 2;
    private static final Set<UUID> FLOATING = ConcurrentHashMap.newKeySet();

    public TasksCommand(BlissPlugin var1, int var2) {
        this.plugin = var1;
    }

    public static void releaseAll() {
        Iterator var1 = FLOATING.iterator();

        while (var1.hasNext()) {
            Entity var4 = Bukkit.getEntity((UUID)var1.next());
            if (var4 instanceof Item) {
                Item var3 = (Item)var4;
                if (var3.isValid()) {
                    release(var3, 1690790621);
                }
            }
        }

        FLOATING.clear();
    }

    private static void release(Item var0, int var1) {
        var0.setGravity(true);
        var0.setPickupDelay(10);
        Vector var12 = new Vector(0, 0, 0);
        var0.setVelocity(var12);
        Set var9 = FLOATING;
        UUID var14 = var0.getUniqueId();
        var9.remove(var14);
    }

    public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
        if (!var1.hasPermission("bliss.admin")) {
            Component var22 = Text.legacy("&cYou don't have permission.", 373192400);
            var1.sendMessage(var22);
            return true;
        } else if (var4.length == 0) {
            this.usage(var1, 658644606);
            return true;
        } else {
            if (var4[0].equalsIgnoreCase("wirefragment")) {
                this.claimWireFragment(var1, var4, 1567569105);
            } else {
                this.usage(var1, 658644606);
            }

            return true;
        }
    }

    private void claimWireFragment(CommandSender var1, String[] var2, int var3) {
        if (var2.length >= 4 && var2[2].equalsIgnoreCase("claim")) {
            int var12;
            try {
                var12 = Integer.parseInt(var2[1]);
            } catch (NumberFormatException var117) {
                String var71 = "/tasks wirefragment <1-7> claim <player> [reason...]";
                CommandHelp.hint(var1, var71, 80456809);
                return;
            }

            if (var12 >= 1 && var12 <= 7) {
                Player var116 = Bukkit.getPlayerExact(var2[3]);
                if (var116 == null) {
                    Component var78 = Text.legacy("&cPlayer not found or offline: &f" + var2[3], 373192400);
                    var1.sendMessage(var78);
                } else {
                    String var39;
                    if (var2.length > 4) {
                        String var64 = " ";
                        int var111 = var2.length;
                        CharSequence[] var95 = Arrays.copyOfRange(var2, 4, var111);
                        var39 = String.join(var64, var95).strip();
                    } else {
                        var39 = "";
                    }

                    Location var15 = showSpot(var116, 401438453);
                    World var16 = var15.getWorld();
                    ItemStack var99 = RitualItems.wireFragment(var12, 60990984);
                    Item var17 = var16.dropItem(var15, var99);
                    var17.setGravity(false);
                    Vector var82 = new Vector(0, 0, 0);
                    var17.setVelocity(var82);
                    var17.setPickupDelay(Integer.MAX_VALUE);
                    Set var49 = FLOATING;
                    UUID var85 = var17.getUniqueId();
                    var49.add(var85);
                    appearBurst(var16, var15, 2072032765);
                    TasksCommand.ClaimShow var52 = new TasksCommand.ClaimShow(var17, var15, var1, var116, var12, 426549770);
                    BlissPlugin var88 = this.plugin;
                    var52.runTaskTimer(var88, 1L, 1L);
                    MotifSpeechListener var19 = this.plugin.motifSpeech(2107445616);
                    if (!var39.isEmpty() && var19 != null) {
                        Runnable var106 = var52::armFinish;
                        var19.announce(var39, var106, 718606350);
                    } else {
                        var52.armFinish();
                    }

                    String var104 = var116.getName();
                    Component var91 = Text.legacy("<##FFD773>Wire Fragment <##F6C500>#" + var12 + " <##FFD773>claimed by <##F6C500>" + var104, 373192400);
                    var1.sendMessage(var91);
                }
            } else {
                Component var97 = Text.legacy("&cWire Fragment id must be 1-7.", 373192400);
                var1.sendMessage(var97);
            }
        } else {
            String var69 = "/tasks wirefragment <1-7> claim <player> [reason...]";
            CommandHelp.hint(var1, var69, 80456809);
        }
    }

    private static Location showSpot(Player var0, int var1) {
        Location var10 = var0.getLocation();
        Vector var11 = var10.getDirection().setY(0);
        if (var11.lengthSquared() < 1.0E-6) {
            Vector var20 = new Vector(0, 0, 1);
            var11 = var20;
        }

        Location var22 = var10.clone();
        Vector var27 = var11.normalize().multiply(3.0);
        return var22.add(var27).add(0.0, 5.0, 0.0);
    }

    private static void appearBurst(World var0, Location var1, int var2) {
        Particle var4 = Particle.FIREWORK;
        Particles.spawn(var0, var4, var1, 40, 0.3, 0.3, 0.3, 0.45, null, true, 1748031581);
        Particle var22 = Particle.END_ROD;
        Particles.spawn(var0, var22, var1, 25, 0.2, 0.2, 0.2, 0.3, null, true, 1748031581);
        Sound var27 = Sound.PARTICLE_SOUL_ESCAPE;
        SoundCategory var31 = SoundCategory.MASTER;
        var0.playSound(var1, var27, var31, 4.0F, 0.5F);
        Sound var28 = Sound.PARTICLE_SOUL_ESCAPE;
        SoundCategory var32 = SoundCategory.MASTER;
        var0.playSound(var1, var28, var32, 4.0F, 0.9F);
        Sound var29 = Sound.ENTITY_WARDEN_HEARTBEAT;
        SoundCategory var33 = SoundCategory.MASTER;
        var0.playSound(var1, var29, var33, 2.5F, 0.8F);
    }

    private void usage(CommandSender var1, int var2) {
        String var4 = "tasks";
        String var5 = "Hand out a Gold Gem arc task reward.";
        String var6 = "wirefragment <1-7> claim <player> [reason…]";
        String var7 = "Hang that Wire Fragment in front of them, type the reason, then drop it";
        List var9 = List.of(CommandHelp.of(var6, var7, 1959232616));
        CommandHelp.show(var1, var4, var5, var9, 696249826);
    }

    public List<String> onTabComplete(CommandSender var1, Command var2, String var3, String[] var4) {
        ArrayList var5 = new ArrayList();
        ArrayList var7 = var5;
        if (!var1.hasPermission("bliss.admin")) {
            return var5;
        } else {
            if (var4.length == 1) {
                String var60 = "wirefragment";
                String var90 = var4[0];
                Locale var96 = Locale.ROOT;
                String var91 = var90.toLowerCase(var96);
                if (var60.startsWith(var91)) {
                    var5.add("wirefragment");
                }
            } else if (var4.length == 2 && var4[0].equalsIgnoreCase("wirefragment")) {
                for (int var8 = 1; var8 <= 7; var8++) {
                    String var9 = String.valueOf(var8);
                    String var71 = var4[1];
                    if (var9.startsWith(var71)) {
                        var7.add(var9);
                    }
                }
            } else if (var4.length == 3 && var4[0].equalsIgnoreCase("wirefragment")) {
                String var35 = "claim";
                String var77 = var4[2];
                Locale var94 = Locale.ROOT;
                String var78 = var77.toLowerCase(var94);
                if (var35.startsWith(var78)) {
                    var5.add("claim");
                }
            } else if (var4.length == 4 && var4[0].equalsIgnoreCase("wirefragment")) {
                String var45 = var4[3];
                Locale var84 = Locale.ROOT;
                String var97 = var45.toLowerCase(var84);

                for (Player var10 : Bukkit.getOnlinePlayers()) {
                    String var55 = var10.getName();
                    Locale var85 = Locale.ROOT;
                    String var56 = var55.toLowerCase(var85);
                    if (var56.startsWith(var97)) {
                        String var88 = var10.getName();
                        var7.add(var88);
                    }
                }
            }

            return var7;
        }
    }

    private final class ClaimShow extends BukkitRunnable {
        private final Item item;
        private final Location centre;
        private final CommandSender sender;
        private final Player target;
        private final int id;
        private final Vector[] u;
        private final Vector[] v;
        private final double[] head;
        private double swept;
        private long ticks;
        private long finishAt;

        ClaimShow(Item nullx, Location nullxx, CommandSender nullxxx, Player nullxxxx, int nullxxxxx, int var7) {
            Vector[] var66 = new Vector[3];
            this.u = var66;
            Vector[] var68 = new Vector[3];
            this.v = var68;
            double[] var70 = new double[3];
            this.head = var70;
            this.swept = 0.0;
            this.ticks = 0L;
            this.finishAt = -1L;
            this.item = nullx;
            this.centre = nullxx;
            this.sender = nullxxx;
            this.target = nullxxxx;
            this.id = nullxxxxx;
            ThreadLocalRandom var17 = ThreadLocalRandom.current();

            for (int var18 = 0; var18 < 3; var18++) {
                double var19 = var17.nextDouble() * (Math.PI * 2);
                double var87 = var17.nextDouble();
                double var21 = Math.acos(2.0 * var87 - 1.0);
                double var93 = Math.sin(var21);
                double var119 = Math.cos(var19);
                double var95 = var93 * var119;
                double var123 = Math.sin(var21);
                double var132 = Math.sin(var19);
                double var125 = var123 * var132;
                double var136 = Math.cos(var21);
                Vector var52 = new Vector(var95, var125, var136);
                Vector var65;
                if (Math.abs(var52.getX()) < 0.9) {
                    var65 = new Vector(1, 0, 0);
                } else {
                    var65 = new Vector(0, 1, 0);
                }

                Vector[] var60 = this.u;
                Vector var101 = var52.clone();
                Vector var103 = var101.crossProduct(var65).normalize();
                var60[var18] = var103;
                Vector[] var62 = this.v;
                Vector var105 = var52.clone();
                Vector[] var116 = this.u;
                Vector var117 = var116[var18];
                Vector var107 = var105.crossProduct(var117).normalize();
                var62[var18] = var107;
                double[] var64 = this.head;
                double var111 = var17.nextDouble() * (Math.PI * 2);
                var64[var18] = var111;
            }
        }

        void armFinish() {
            if (this.finishAt < 0L) {
                long var12 = this.ticks;
                long var20 = this.ticks / 2L;
                long var16 = var12 + var20 + 60L;
                this.finishAt = var16;
            }
        }

        public void run() {
            long var123 = this.ticks + 1L;
            this.ticks = var123;
            if (!this.item.isValid()) {
                Set var118 = TasksCommand.FLOATING;
                UUID var147 = this.item.getUniqueId();
                var118.remove(var147);
                this.cancel();
            } else if (this.ticks > 800L) {
                this.finish(124337471);
            } else {
                if (this.finishAt >= 0L) {
                    long var53 = this.ticks;
                    long var151 = this.finishAt;
                    if (var53 >= var151) {
                        this.finish(124337471);
                        return;
                    }
                }

                World var25 = this.centre.getWorld();
                if (this.ticks % 2L == 0L) {
                    Particle var125 = Particle.WHITE_ASH;
                    Location var158 = this.centre;
                    Particles.spawn(var25, var125, var158, 1, 0.18, 0.18, 0.18, 0.01, null, true, 1748031581);
                }

                if (this.swept < 1.0) {
                    double var200 = this.swept + 0.18;
                    double var128 = Math.min(1.0, var200);
                    this.swept = var128;
                }

                Color var161 = TasksCommand.GOLD;
                DustOptions var72 = new DustOptions(var161, 0.9F);
                DustOptions var26 = var72;
                ThreadLocalRandom var27 = ThreadLocalRandom.current();

                for (int var28 = 0; var28 < 3; var28++) {
                    double[] var77 = this.head;
                    double var164 = var77[var28] + 0.18;
                    var77[var28] = var164;
                    double[] var79 = this.head;
                    if (var79[var28] >= Math.PI * 2) {
                        double[] var84 = this.head;
                        double var170 = var84[var28] - (Math.PI * 2);
                        var84[var28] = var170;
                    }

                    double[] var86 = this.head;
                    double var87 = var86[var28];
                    double var173 = this.swept;
                    double var29 = var87 - var173;
                    int var142 = (int)Math.ceil(this.swept * 6.0);
                    int var31 = Math.max(1, var142);

                    for (int var32 = 0; var32 <= var31; var32++) {
                        double var96 = (double)var32;
                        double var176 = (double)var31;
                        double var33 = var96 / var176;
                        double var101 = var27.nextDouble();
                        double var212 = 0.85 * var33;
                        double var180 = 0.15 + var212;
                        if (!(var101 > var180)) {
                            double[] var183 = this.head;
                            double var184 = var183[var28];
                            double var186 = var184 - var29;
                            double var188 = var186 * var33;
                            double var35 = var29 + var188;
                            double var37 = Math.cos(var35);
                            double var39 = Math.sin(var35);
                            Particle var144 = Particle.DUST;
                            double var192 = this.centre.getX();
                            Vector[] var235 = this.u;
                            double var237 = var235[var28].getX();
                            double var220 = var37 * var237;
                            Vector[] var260 = this.v;
                            double var262 = var260[var28].getX();
                            double var241 = var39 * var262;
                            double var224 = (var220 + var241) * 0.85;
                            double var194 = var192 + var224;
                            double var228 = this.centre.getY();
                            Vector[] var265 = this.u;
                            double var267 = var265[var28].getY();
                            double var247 = var37 * var267;
                            Vector[] var288 = this.v;
                            double var290 = var288[var28].getY();
                            double var271 = var39 * var290;
                            double var251 = (var247 + var271) * 0.85;
                            double var230 = var228 + var251;
                            double var255 = this.centre.getZ();
                            Vector[] var293 = this.u;
                            double var295 = var293[var28].getZ();
                            double var277 = var37 * var295;
                            Vector[] var307 = this.v;
                            double var309 = var307[var28].getZ();
                            double var299 = var39 * var309;
                            double var281 = (var277 + var299) * 0.85;
                            double var257 = var255 + var281;
                            Particles.spawn(var25, var144, var194, var230, var257, 1, 0.0, 0.0, 0.0, 0.0, var26, true, 954482185);
                        }
                    }
                }
            }
        }

        private void finish(int var1) {
            this.cancel();
            if (this.item.isValid()) {
                World var7 = this.centre.getWorld();
                Location var25 = this.centre;
                TasksCommand.appearBurst(var7, var25, 2072032765);
                Location var27 = this.centre;
                Sound var4 = Sound.BLOCK_AMETHYST_BLOCK_BREAK;
                var7.playSound(var27, var4, 1.6F, 0.5F);
                TasksCommand.release(this.item, 1690790621);
                if (!this.target.isOnline()) {
                    CommandSender var22 = this.sender;
                    Component var32 = Text.legacy(
                        "&e" + this.target.getName() + " left before the claim landed — the fragment dropped where they stood.", 373192400
                    );
                    var22.sendMessage(var32);
                }
            }

            Set var23 = TasksCommand.FLOATING;
            UUID var35 = this.item.getUniqueId();
            var23.remove(var35);
        }
    }
}
