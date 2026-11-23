package com.bliss.honored;

import com.bliss.BlissPlugin;
import com.bliss.pedestal.CrushedPedestalOrb;
import com.bliss.villager.Text;
import java.time.Duration;
import java.util.UUID;
import net.kyori.adventure.text.BuildableComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.ClickCallback.Options;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class ControlRoomDeparturePrompt {
    private static final long PERIOD = 20L;
    private static final long ASK_AFTER = 18000L;
    private static final long RETRY_AFTER = 6000L;
    private static final long DEPART_AFTER = 100L;
    private static final long CHOICE_GAP = 200L;
    private static final long REPLY_AFTER = 100L;
    private final BlissPlugin plugin;
    private BukkitTask task;
    private long ticks;
    private long askAt = 18000L;
    private boolean awaiting;
    private boolean done;
    private static final double ROOM_RADIUS = 150.0;
    private int questionGen;
    private static final Duration BUTTON_LIFETIME = Duration.ofHours(2L);

    public ControlRoomDeparturePrompt(BlissPlugin var1, int var2) {
        this.plugin = var1;
    }

    public void start(int var1) {
        if (this.task == null) {
            BukkitRunnable var3 = new BukkitRunnable() {
                public void run() {
                    ControlRoomDeparturePrompt.this.tick(737123603);
                }
            };
            BlissPlugin var11 = this.plugin;
            BukkitTask var9 = var3.runTaskTimer(var11, 20L, 20L);
            this.task = var9;
        }
    }

    public void shutdown(int var1) {
        if (this.task != null) {
            this.task.cancel();
            this.task = null;
        }

        this.reset(2142732117);
    }

    private void tick(int var1) {
        ControlRoomManager var6 = this.plugin.controlRoom(1275615420);
        if (var6 == null || !var6.inSession(1710238328)) {
            this.reset(2142732117);
        } else if (!this.done) {
            Player var7 = this.honoredOne(578417295);
            if (this.awaiting) {
                if (var7 == null) {
                    this.awaiting = false;
                }
            } else {
                long var33 = this.ticks + 20L;
                this.ticks = var33;
                long var22 = this.ticks;
                long var38 = this.askAt;
                if (var22 >= var38) {
                    if (var7 != null) {
                        if (this.inControlRoom(var7, 2109225569)) {
                            this.ask(var7, 1092666706);
                        }
                    }
                }
            }
        }
    }

    private boolean inControlRoom(Player var1, int var2) {
        ControlRoomManager var7 = this.plugin.controlRoom(1275615420);
        if (var7 == null) {
            return false;
        } else {
            Location var8 = var7.location(1988022717);
            if (var8 != null && var8.getWorld() != null) {
                World var18 = var1.getWorld();
                World var29 = var8.getWorld();
                if (!var18.equals(var29)) {
                    return false;
                } else {
                    Location var22 = var1.getLocation();
                    boolean var26;
                    if (var22.distanceSquared(var8) <= 22500.0) {
                        var26 = true;
                    } else {
                        var26 = false;
                    }

                    return var26;
                }
            } else {
                return false;
            }
        }
    }

    private void reset(int var1) {
        this.ticks = 0L;
        this.askAt = 18000L;
        this.awaiting = false;
        this.done = false;
    }

    private void ask(Player var1, int var2) {
        this.awaiting = true;
        int var15 = this.questionGen + 1;
        this.questionGen = var15;
        Component var17 = Text.legacy("&eAre you ready to leave o' Honored One?", 373192400);
        var1.sendMessage(var17);
        UUID var7 = var1.getUniqueId();
        BukkitScheduler var12 = Bukkit.getScheduler();
        BlissPlugin var19 = this.plugin;
        Runnable var21 = () -> {
            Player var10 = Bukkit.getPlayer(var7);
            if (var10 != null && var10.isOnline() && this.awaiting && !this.done) {
                if (!this.inControlRoom(var10, 2109225569)) {
                    this.awaiting = false;
                } else {
                    Builder var23 = Component.text();
                    String var5 = "[Yes]";
                    NamedTextColor var8 = NamedTextColor.GREEN;
                    String var9 = "Say yes, Honored One";
                    Component var32 = this.button(var5, true, var7, var8, var9, 871276252);
                    Builder var25 = (Builder)var23.append(var32);
                    TextComponent var34 = Component.text("   ");
                    Builder var27 = (Builder)var25.append(var34);
                    String var37 = "[No]";
                    NamedTextColor var40 = NamedTextColor.RED;
                    String var41 = "Not yet — ask again in five minutes";
                    Component var36 = this.button(var37, false, var7, var40, var41, 871276252);
                    BuildableComponent var30 = ((Builder)var27.append(var36)).build();
                    var10.sendMessage(var30);
                }
            }
        };
        var12.runTaskLater(var19, var21, 200L);
    }

    private Component button(String var1, boolean var2, UUID var3, NamedTextColor var4, String var5, int var6) {
        int var11 = this.questionGen;
        ClickCallback var12 = var4x -> {
            if (var4x instanceof Player var8) {
                UUID var13 = var8.getUniqueId();
                if (var13.equals(var3)) {
                    int var18 = this.questionGen;
                    if (var11 != var18) {
                        return;
                    }

                    this.answer(var8, var2, 361354049);
                    return;
                }
            }
        };
        TextComponent var17 = Component.text(var1);
        TextComponent var19 = (TextComponent)var17.color(var4);
        TextDecoration var28 = TextDecoration.BOLD;
        TextComponent var21 = (TextComponent)var19.decoration(var28, true);
        TextDecoration var29 = TextDecoration.ITALIC;
        TextComponent var23 = (TextComponent)var21.decoration(var29, false);
        net.kyori.adventure.text.event.ClickCallback.Options.Builder var39 = Options.builder().uses(1);
        Duration var45 = BUTTON_LIFETIME;
        Options var42 = (Options)var39.lifetime(var45).build();
        ClickEvent var31 = ClickEvent.callback(var12, var42);
        TextComponent var25 = (TextComponent)var23.clickEvent(var31);
        TextComponent var33 = Component.text(var5);
        NamedTextColor var43 = NamedTextColor.GRAY;
        HoverEvent var35 = HoverEvent.showText(var33.color(var43));
        return var25.hoverEvent(var35);
    }

    public void answer(Player var1, boolean var2, int var3) {
        if (!this.awaiting) {
            Component var17 = Text.legacy("&7There is no question waiting.", 373192400);
            var1.sendMessage(var17);
        } else {
            this.awaiting = false;
            if (!var2) {
                long var22 = this.ticks + 6000L;
                this.askAt = var22;
                BukkitScheduler var14 = Bukkit.getScheduler();
                BlissPlugin var25 = this.plugin;
                Runnable var6 = () -> Bukkit.broadcast(Text.legacy("&eVery well.. Take your time", 373192400));
                var14.runTaskLater(var25, var6, 100L);
            } else {
                this.answerYes(1885885871);
            }
        }
    }

    private void answerYes(int var1) {
        this.done = true;
        BukkitScheduler var7 = Bukkit.getScheduler();
        BlissPlugin var10 = this.plugin;
        Runnable var11 = this::sayYes;
        var7.runTaskLater(var10, var11, 100L);
    }

    private void sayYes() {
        this.done = true;
        Bukkit.broadcast(Text.legacy("&eYour requets will be fulfilled soon", 373192400));
        Bukkit.broadcast(Text.legacy("&eHonored One... Enjoy.", 373192400));
        BukkitScheduler var12 = Bukkit.getScheduler();
        BlissPlugin var15 = this.plugin;
        Runnable var16 = this::depart;
        var12.runTaskLater(var15, var16, 100L);
    }

    public boolean askNow(int var1) {
        Player var5 = this.honoredOne(578417295);
        if (var5 == null) {
            return false;
        } else {
            this.done = false;
            long var14 = this.ticks;
            long var19 = this.askAt;
            long var16 = Math.max(var14, var19);
            this.ticks = var16;
            this.ask(var5, 1092666706);
            return true;
        }
    }

    public boolean leaveNow(int var1) {
        if (this.honoredOne(578417295) == null) {
            return false;
        } else {
            this.awaiting = false;
            this.answerYes(1885885871);
            return true;
        }
    }

    private void depart() {
        Player var5 = this.honoredOne(578417295);
        if (var5 == null) {
            this.plugin.getLogger().warning("Control Room: the Honored One went offline before the departure ritual.");
            this.rearm(91693602);
        } else {
            Location var6 = this.plugin.pedestalRitualCentre(1445661832);
            if (var6 == null) {
                this.plugin
                    .getLogger()
                    .warning("Control Room: the Honored One is ready to leave, but no pedestal is configured (/bliss pedestal) — no departure ritual.");
                this.rearm(91693602);
            } else {
                HonoredSummonRitual var7 = this.plugin.honoredRitual(1346825692);
                if (var7 != null && !var7.isRunning(596214312)) {
                    var7.beginDeparture(var5, var6, 204427762);
                } else if (!var5.teleport(var6)) {
                    this.plugin.getLogger().warning("Control Room: the departure teleport was refused; asking the Honored One again.");
                    this.rearm(91693602);
                } else {
                    if (this.plugin.crushedPedestalOrb(952223969) != null) {
                        CrushedPedestalOrb var41 = this.plugin.crushedPedestalOrb(952223969);
                        var41.ignoreUntilClear(var5, 1069596687);
                    }

                    BlissPlugin var43 = this.plugin;
                    UUID var61 = var5.getUniqueId();
                    var43.completeAssessmentAtPedestal(var61, 1401753802);
                    BlissPlugin var45 = this.plugin;
                    UUID var63 = var5.getUniqueId();
                    var45.scatterAudienceAroundPedestal(var63, var6, 1666630238);
                    if (this.plugin.departureFlight(1434080140) != null) {
                        UUID var8 = var5.getUniqueId();
                        BukkitScheduler var51 = Bukkit.getScheduler();
                        BlissPlugin var65 = this.plugin;
                        Runnable var70 = () -> {
                            DepartureFlight var5x = this.plugin.departureFlight(1434080140);
                            var5x.begin(var8, 949513185);
                        };
                        var51.runTaskLater(var65, var70, 10L);
                    }

                    BukkitScheduler var53 = Bukkit.getScheduler();
                    BlissPlugin var67 = this.plugin;
                    Runnable var72 = () -> {
                        if (this.plugin.pedestalFinale(1936651090) != null) {
                            PedestalFinale var8x = this.plugin.pedestalFinale(1936651090);
                            var8x.begin(var6, 1122725959);
                        }
                    };
                    var53.runTaskLater(var67, var72, 100L);
                }
            }
        }
    }

    public void rearm(int var1) {
        this.done = false;
        long var10 = this.ticks + 6000L;
        this.askAt = var10;
    }

    private Player honoredOne(int var1) {
        HonoredOneManager var3 = this.plugin.honoredOne(783425701);
        if (var3 == null) {
            return null;
        } else {
            UUID var4 = var3.current(1257858083);
            Player var11;
            if (var4 == null) {
                var11 = null;
            } else {
                var11 = Bukkit.getPlayer(var4);
            }

            return var11;
        }
    }
}
