package com.bliss.admin;

import com.bliss.BlissPlugin;
import com.bliss.admin.CommandHelp.Entry;
import com.bliss.honored.AssessmentDay;
import com.bliss.honored.AssessmentWindow;
import com.bliss.honored.CageManager;
import com.bliss.honored.ControlRoomDeparturePrompt;
import com.bliss.honored.ControlRoomManager;
import com.bliss.honored.HonoredCompass;
import com.bliss.honored.HonoredOneManager;
import com.bliss.honored.HonoredSummonRitual;
import com.bliss.items.BlissCoreItems;
import com.bliss.pedestal.PedestalState;
import com.bliss.villager.Text;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public final class AssessmentCommand implements CommandExecutor, TabCompleter {
    private final HonoredOneManager ho;
    private final AssessmentWindow window;
    private final CageManager cage;
    private final PedestalState pedestal;
    private final HonoredSummonRitual ritual;
    private final AssessmentDay day;
    private final ControlRoomManager controlRoom;

    public AssessmentCommand(
        HonoredOneManager var1,
        AssessmentWindow var2,
        CageManager var3,
        PedestalState var4,
        HonoredSummonRitual var5,
        AssessmentDay var6,
        ControlRoomManager var7,
        int var8
    ) {
        this.ho = var1;
        this.window = var2;
        this.cage = var3;
        this.pedestal = var4;
        this.ritual = var5;
        this.day = var6;
        this.controlRoom = var7;
    }

    public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
        if (var4.length < 1) {
            this.usage(var1, 1296710835);
            return true;
        } else {
            String var12 = var4[0];
            Locale var57 = Locale.ROOT;
            String var8 = var12.toLowerCase(var57);
            switch (var8) {
                case "honoredone":
                case "ho":
                    this.handleHonoredOne(var1, var4, 608160520);
                    break;
                case "window":
                    this.handleWindow(var1, var4, 1317214292);
                    break;
                case "compass":
                    this.handleCompass(var1, var4, 584302074);
                    break;
                case "cage":
                    this.handleCage(var1, var4, 207630899);
                    break;
                case "ritual":
                    this.handleRitual(var1, 153092509);
                    break;
                case "day":
                    this.handleDay(var1, var4, 1837588859);
                    break;
                case "controlroom":
                case "cr":
                    this.handleControlRoom(var1, var4, 343563061);
                    break;
                default:
                    this.usage(var1, 1296710835);
            }

            return true;
        }
    }

    private void handleHonoredOne(CommandSender var1, String[] var2, int var3) {
        if (var2.length < 2) {
            this.usage(var1, 1296710835);
        } else {
            String var14 = var2[1];
            Locale var54 = Locale.ROOT;
            String var7 = var14.toLowerCase(var54);
            switch (var7) {
                case "set":
                    if (var2.length < 3) {
                        String var78 = "/assessment honoredone set <player>";
                        CommandHelp.hint(var1, var78, 80456809);
                        return;
                    }

                    Player var83 = Bukkit.getPlayerExact(var2[2]);
                    if (var83 == null) {
                        Component var77 = Text.legacy("&cPlayer not found / offline.", 373192400);
                        var1.sendMessage(var77);
                        return;
                    }

                    HonoredOneManager var42 = this.ho;
                    UUID var71 = var83.getUniqueId();
                    String var82 = "&6✦ &e" + var83.getName() + " &6is now the &l&eHonored One&6.";
                    var42.assign(var71, var82, 209174169);
                    Component var75 = Text.legacy("&aSet " + var83.getName() + " as the Honored One.", 373192400);
                    var1.sendMessage(var75);
                    break;
                case "clear":
                    this.ho.clearHonoredOne(1926729969);
                    Component var58 = Text.legacy("&eHonored One cleared.", 373192400);
                    var1.sendMessage(var58);
                    break;
                case "status":
                    UUID var9 = this.ho.current(1257858083);
                    if (var9 == null) {
                        Component var67 = Text.legacy("&7There is no Honored One.", 373192400);
                        var1.sendMessage(var67);
                        return;
                    }

                    OfflinePlayer var10 = Bukkit.getOfflinePlayer(var9);
                    String var62;
                    if (var10.getName() == null) {
                        var62 = var9.toString();
                    } else {
                        var62 = var10.getName();
                    }

                    Component var64 = Text.legacy("&7Honored One: &6" + var62, 373192400);
                    var1.sendMessage(var64);
                    break;
                default:
                    this.usage(var1, 1296710835);
            }
        }
    }

    private void handleWindow(CommandSender var1, String[] var2, int var3) {
        String var74;
        if (var2.length >= 2) {
            String var73 = var2[1];
            Locale var106 = Locale.ROOT;
            var74 = var73.toLowerCase(var106);
        } else {
            var74 = "status";
        }

        switch (var74) {
            case "start":
            case "open":
                if (this.window.isActive(2020345102)) {
                    Component var92 = Text.legacy("&cThe window is already open.", 373192400);
                    var1.sendMessage(var92);
                    return;
                }

                if (this.day.windowOpenedToday(1532280241)) {
                    Component var90 = Text.legacy(
                        "&cThe window has already been opened on day " + this.day.day(967780557) + " &7— advance the day first (&f/assessment day next&7).",
                        373192400
                    );
                    var1.sendMessage(var90);
                    return;
                }

                this.window.start(321649434);
                this.day.markWindowOpened(2097953902);
                float var75;
                if (this.day.isLastDay(1298882900)) {
                    var75 = 0.5F;
                } else {
                    var75 = 1.0F;
                }

                float var12 = var75;
                float var76;
                if (this.day.isLastDay(1298882900)) {
                    var76 = 4.0F;
                } else {
                    var76 = 1.5F;
                }

                float var13 = var76;

                for (Player var15 : Bukkit.getOnlinePlayers()) {
                    Location var85 = var15.getLocation();
                    Sound var110 = Sound.BLOCK_BELL_USE;
                    var15.playSound(var85, var110, var13, var12);
                }

                int var81 = this.day.day(967780557);
                String var109;
                if (this.day.isLastDay(1298882900)) {
                    var109 = " &6— FINAL DAY, action bar live.";
                } else {
                    var109 = ".";
                }

                Component var83 = Text.legacy("&aAssessment window opened on day &f" + var81 + "/11 &a(2h, auto-closes)" + var109, 373192400);
                var1.sendMessage(var83);
                break;
            case "stop":
            case "close":
            case "end":
                this.window.stop(479346272);
                Component var99 = Text.legacy("&eAssessment window closed.", 373192400);
                var1.sendMessage(var99);
                break;
            case "status":
                String var97;
                if (this.window.isActive(2020345102)) {
                    var97 = "&aThe Assessment window is OPEN.";
                } else {
                    var97 = "&7The Assessment window is closed.";
                }

                Component var96 = Text.legacy(var97, 373192400);
                var1.sendMessage(var96);
                break;
            default:
                String var78 = "/assessment window <start|stop|status>";
                CommandHelp.hint(var1, var78, 80456809);
        }
    }

    private void handleDay(CommandSender var1, String[] var2, int var3) {
        if (var2.length < 2) {
            int var53 = this.day.day(967780557);
            String var113;
            if (this.day.isLastDay(1298882900)) {
                var113 = " &6(final day)";
            } else {
                var113 = "";
            }

            String var116;
            if (this.day.windowOpenedToday(1532280241)) {
                var116 = "&ayes";
            } else {
                var116 = "&cno";
            }

            Component var55 = Text.legacy("&7Day &f" + var53 + "/11" + var113 + " &7| window opened today: " + var116, 373192400);
            var1.sendMessage(var55);
            String var56 = "/assessment day <1-11>|next|status";
            CommandHelp.hint(var1, var56, 80456809);
        } else if (var2[1].matches("-?\\d+")) {
            try {
                AssessmentDay var18 = this.day;
                int var61 = Integer.parseInt(var2[1]);
                var18.setDay(var61, 1945321378);
            } catch (NumberFormatException var124) {
                Component var63 = Text.legacy("&cDay must be a number.", 373192400);
                var1.sendMessage(var63);
                return;
            }

            int var66 = this.day.day(967780557);
            String var103;
            if (this.day.isLastDay(1298882900)) {
                var103 = " &6(final day)";
            } else {
                var103 = "";
            }

            Component var68 = Text.legacy("&aAssessment day set to &f" + var66 + "/11" + var103 + "&a.", 373192400);
            var1.sendMessage(var68);
        } else {
            String var23 = var2[1];
            Locale var70 = Locale.ROOT;
            String var121 = var23.toLowerCase(var70);
            switch (var121) {
                case "set":
                    if (var2.length < 3) {
                        String var88 = "/assessment day <1-11>";
                        CommandHelp.hint(var1, var88, 80456809);
                        return;
                    }

                    try {
                        AssessmentDay var36 = this.day;
                        int var80 = Integer.parseInt(var2[2]);
                        var36.setDay(var80, 1945321378);
                        Component var85 = Text.legacy("&aAssessment day set to &f" + this.day.day(967780557) + "/11&a.", 373192400);
                        var1.sendMessage(var85);
                    } catch (NumberFormatException var125) {
                        Component var87 = Text.legacy("&cDay must be a number.", 373192400);
                        var1.sendMessage(var87);
                    }
                    break;
                case "next":
                    this.day.nextDay(647946230);
                    int var91 = this.day.day(967780557);
                    String var112;
                    if (this.day.isLastDay(1298882900)) {
                        var112 = " &6(final day)";
                    } else {
                        var112 = "";
                    }

                    Component var93 = Text.legacy("&aAdvanced to day &f" + var91 + "/11" + var112 + "&a.", 373192400);
                    var1.sendMessage(var93);
                    break;
                case "status":
                    int var74 = this.day.day(967780557);
                    String var107;
                    if (this.day.isLastDay(1298882900)) {
                        var107 = " &6(final day)";
                    } else {
                        var107 = "";
                    }

                    String var120;
                    if (this.day.windowOpenedToday(1532280241)) {
                        var120 = "&ayes";
                    } else {
                        var120 = "&cno";
                    }

                    Component var76 = Text.legacy("&7Day &f" + var74 + "/11" + var107 + " &7| window opened today: " + var120, 373192400);
                    var1.sendMessage(var76);
                    break;
                default:
                    String var94 = "/assessment day <1-11>|next|status";
                    CommandHelp.hint(var1, var94, 80456809);
            }
        }
    }

    private void handleControlRoom(CommandSender var1, String[] var2, int var3) {
        String var129;
        if (var2.length >= 2) {
            String var128 = var2[1];
            Locale var197 = Locale.ROOT;
            var129 = var128.toLowerCase(var197);
        } else {
            var129 = "status";
        }

        switch (var129) {
            case "set":
                if (!(var1 instanceof Player var12)) {
                    Component var142 = Text.legacy("&cRun this in-game, standing where the Honored One should arrive.", 373192400);
                    var1.sendMessage(var142);
                    return;
                }

                ControlRoomManager var31 = this.controlRoom;
                Location var135 = var12.getLocation();
                var31.set(var135, 843643350);
                ((BlissPlugin)BlissPlugin.getPlugin(BlissPlugin.class)).enchantLimit(1301378516).reconcile(1408290875);
                Component var140 = Text.legacy(
                    "&aControl Room arrival point set to your location &7(&f"
                        + var12.getWorld().getName()
                        + "&7). &7The Honored One is summoned here if they survive the final day.",
                    373192400
                );
                var1.sendMessage(var140);
                break;
            case "clear":
                this.controlRoom.clear(799715260);
                ((BlissPlugin)BlissPlugin.getPlugin(BlissPlugin.class)).enchantLimit(1301378516).reconcile(1408290875);
                Component var174 = Text.legacy("&eControl Room location cleared.", 373192400);
                var1.sendMessage(var174);
                break;
            case "tp":
            case "teleport":
                if (!(var1 instanceof Player var222)) {
                    Component var172 = Text.legacy("&cRun this in-game.", 373192400);
                    var1.sendMessage(var172);
                    return;
                }

                Location var13 = this.controlRoom.location(1988022717);
                if (var13 == null) {
                    String var198;
                    if (this.controlRoom.isConfigured(553762660)) {
                        var198 = "&cThe Control Room is set in world &f" + this.controlRoom.worldName(1739755989) + "&c, but that dimension isn't loaded.";
                    } else {
                        var198 = "&cThe Control Room location isn't set.";
                    }

                    Component var156 = Text.legacy(var198, 373192400);
                    var1.sendMessage(var156);
                    return;
                }

                if (!var222.teleport(var13)) {
                    Component var170 = Text.legacy(
                        "&cCouldn't teleport you to the Control Room &7(&f"
                            + var13.getWorld().getName()
                            + "&7)&c — dismount or leave your vehicle and try again.",
                        373192400
                    );
                    var1.sendMessage(var170);
                    return;
                }

                Component var165 = Text.legacy("&aTeleported to the Control Room &7(&f" + var13.getWorld().getName() + "&7).", 373192400);
                var1.sendMessage(var165);
                break;
            case "status":
                Location var221 = this.controlRoom.location(1988022717);
                if (var221 != null) {
                    String var143 = "&7Control Room: &f%s &7@ &f%.1f, %.1f, %.1f";
                    Object[] var201 = new Object[4];
                    String var208 = var221.getWorld().getName();
                    var201[0] = var208;
                    Double var212 = var221.getX();
                    var201[1] = var212;
                    Double var216 = var221.getY();
                    var201[2] = var216;
                    Double var220 = var221.getZ();
                    var201[3] = var220;
                    Component var145 = Text.legacy(String.format(var143, var201), 373192400);
                    var1.sendMessage(var145);
                } else if (this.controlRoom.isConfigured(553762660)) {
                    Component var150 = Text.legacy(
                        "&7Control Room is set in world &f"
                            + this.controlRoom.worldName(1739755989)
                            + " &7but that dimension is &cnot loaded&7 — the ascension will fire once it loads.",
                        373192400
                    );
                    var1.sendMessage(var150);
                } else {
                    Component var152 = Text.legacy("&7The Control Room location is not set &7(&f/assessment controlroom set&7).", 373192400);
                    var1.sendMessage(var152);
                }
                break;
            case "enchants":
            case "enchant":
                this.handleEnchantSpot(var1, var2, 888408738);
                break;
            case "armor":
            case "armour":
                this.handleArmorSpot(var1, var2, 419481617);
                break;
            case "border":
                this.handleBorderSpot(var1, var2, 795750502);
                break;
            case "potions":
            case "potion":
                this.handlePotionSpot(var1, var2, 1689977838);
                break;
            case "ask":
                ControlRoomDeparturePrompt var224 = ((BlissPlugin)BlissPlugin.getPlugin(BlissPlugin.class)).departurePrompt(1001037186);
                if (var224 == null || !var224.askNow(134874570)) {
                    Component var182 = Text.legacy("&cNo Honored One is online to ask.", 373192400);
                    var1.sendMessage(var182);
                }
                break;
            case "leave":
                ControlRoomDeparturePrompt var223 = ((BlissPlugin)BlissPlugin.getPlugin(BlissPlugin.class)).departurePrompt(1001037186);
                if (var223 != null && var223.leaveNow(2032461709)) {
                    Component var178 = Text.legacy(
                        "&aLeaving as if the Honored One said Yes: the closing lines in 5 s, the departure ritual 5 s after those, then the pedestal finale 5 s after the landing.",
                        373192400
                    );
                    var1.sendMessage(var178);
                } else {
                    Component var180 = Text.legacy("&cNo Honored One is online to send off.", 373192400);
                    var1.sendMessage(var180);
                }
                break;
            default:
                String var131 = "/assessment controlroom <set|clear|tp|status|enchants|armor|border|potions|ask|leave>";
                CommandHelp.hint(var1, var131, 80456809);
        }
    }

    private void handlePotionSpot(CommandSender var1, String[] var2, int var3) {
        BlissPlugin var9 = (BlissPlugin)BlissPlugin.getPlugin(BlissPlugin.class);
        String var64;
        if (var2.length >= 3) {
            String var63 = var2[2];
            Locale var91 = Locale.ROOT;
            var64 = var63.toLowerCase(var91);
        } else {
            var64 = "status";
        }

        switch (var64) {
            case "set":
                if (!(var1 instanceof Player var13)) {
                    Component var67 = Text.legacy("&cRun this in-game, standing where the Potion Limit bottle should float.", 373192400);
                    var1.sendMessage(var67);
                    return;
                }

                ControlRoomManager var33 = this.controlRoom;
                Location var69 = var13.getLocation();
                var33.setPotionSpot(var69, 1894742204);
                var9.potionLimit(621168953).reconcile(1560808647);
                Location var14 = var13.getLocation();
                String var70 = "&aPotion Limit holograms placed here &7(&f%s &7@ &f%.1f, %.1f, %.1f&7).";
                Object[] var92 = new Object[4];
                String var103 = var14.getWorld().getName();
                var92[0] = var103;
                Double var107 = var14.getX();
                var92[1] = var107;
                Double var111 = var14.getY();
                var92[2] = var111;
                Double var115 = var14.getZ();
                var92[3] = var115;
                Component var72 = Text.legacy(String.format(var70, var92), 373192400);
                var1.sendMessage(var72);
                Component var74 = Text.legacy(
                    "&7The bottle floats &f1.6 &7blocks up; the spread is a &f6&7-block-wide stack of rings climbing &f2.2 &7blocks at a time.", 373192400
                );
                var1.sendMessage(var74);
                break;
            case "clear":
                this.controlRoom.clearPotionSpot(235525916);
                var9.potionLimit(621168953).reconcile(1560808647);
                Component var87 = Text.legacy("&ePotion Limit spot cleared.", 373192400);
                var1.sendMessage(var87);
                break;
            case "status":
                Location var131 = this.controlRoom.potionSpot(491047341);
                if (var131 != null) {
                    String var83 = "&7Potion holograms: &f%s &7@ &f%.1f, %.1f, %.1f";
                    Object[] var94 = new Object[4];
                    String var118 = var131.getWorld().getName();
                    var94[0] = var118;
                    Double var122 = var131.getX();
                    var94[1] = var122;
                    Double var126 = var131.getY();
                    var94[2] = var126;
                    Double var130 = var131.getZ();
                    var94[3] = var130;
                    Component var85 = Text.legacy(String.format(var83, var94), 373192400);
                    var1.sendMessage(var85);
                } else if (this.controlRoom.potionConfigured(886107921)) {
                    Component var80 = Text.legacy(
                        "&7Potion holograms are set in world &f" + this.controlRoom.potionWorldName(1573517215) + " &7but that dimension is &cnot loaded&7.",
                        373192400
                    );
                    var1.sendMessage(var80);
                } else {
                    Component var82 = Text.legacy("&7No Potion Limit spot set &7(&f/assessment controlroom potions set&7).", 373192400);
                    var1.sendMessage(var82);
                }
                break;
            default:
                String var75 = "/assessment controlroom potions <set|clear|status>";
                CommandHelp.hint(var1, var75, 80456809);
        }
    }

    private void handleBorderSpot(CommandSender var1, String[] var2, int var3) {
        BlissPlugin var9 = (BlissPlugin)BlissPlugin.getPlugin(BlissPlugin.class);
        String var63;
        if (var2.length >= 3) {
            String var62 = var2[2];
            Locale var88 = Locale.ROOT;
            var63 = var62.toLowerCase(var88);
        } else {
            var63 = "status";
        }

        switch (var63) {
            case "set":
                if (!(var1 instanceof Player var13)) {
                    Component var68 = Text.legacy("&cRun this in-game, standing where the World Border orb should float.", 373192400);
                    var1.sendMessage(var68);
                    return;
                }

                ControlRoomManager var38 = this.controlRoom;
                Location var70 = var13.getLocation();
                var38.setBorderSpot(var70, 60073447);
                var9.worldBorderHologram(1866199369).reconcile(380967189);
                Location var14 = var13.getLocation();
                String var71 = "&aWorld Border dial placed here &7(&f%s &7@ &f%.1f, %.1f, %.1f&7).";
                Object[] var89 = new Object[4];
                String var100 = var14.getWorld().getName();
                var89[0] = var100;
                Double var104 = var14.getX();
                var89[1] = var104;
                Double var108 = var14.getY();
                var89[2] = var108;
                Double var112 = var14.getZ();
                var89[3] = var112;
                Component var73 = Text.legacy(String.format(var71, var89), 373192400);
                var1.sendMessage(var73);
                break;
            case "clear":
                this.controlRoom.clearBorderSpot(1700832502);
                var9.worldBorderHologram(1866199369).reconcile(380967189);
                Component var66 = Text.legacy("&eWorld Border dial cleared.", 373192400);
                var1.sendMessage(var66);
                break;
            case "status":
                Location var128 = this.controlRoom.borderSpot(983401541);
                if (var128 != null) {
                    String var74 = "&7World Border dial: &f%s &7@ &f%.1f, %.1f, %.1f";
                    Object[] var91 = new Object[4];
                    String var115 = var128.getWorld().getName();
                    var91[0] = var115;
                    Double var119 = var128.getX();
                    var91[1] = var119;
                    Double var123 = var128.getY();
                    var91[2] = var123;
                    Double var127 = var128.getZ();
                    var91[3] = var127;
                    Component var76 = Text.legacy(String.format(var74, var91), 373192400);
                    var1.sendMessage(var76);
                } else if (this.controlRoom.borderConfigured(201221402)) {
                    Component var83 = Text.legacy(
                        "&7World Border dial is set in world &f" + this.controlRoom.borderWorldName(731350430) + " &7but that dimension is &cnot loaded&7.",
                        373192400
                    );
                    var1.sendMessage(var83);
                } else {
                    Component var78 = Text.legacy("&7No World Border dial spot set &7(&f/assessment controlroom border set&7).", 373192400);
                    var1.sendMessage(var78);
                }
                break;
            default:
                String var84 = "/assessment controlroom border <set|clear|status>";
                CommandHelp.hint(var1, var84, 80456809);
        }
    }

    private void handleArmorSpot(CommandSender var1, String[] var2, int var3) {
        BlissPlugin var9 = (BlissPlugin)BlissPlugin.getPlugin(BlissPlugin.class);
        String var21;
        if (var2.length >= 3) {
            String var20 = var2[2];
            Locale var66 = Locale.ROOT;
            var21 = var20.toLowerCase(var66);
        } else {
            var21 = "status";
        }

        switch (var21) {
            case "set":
                if (!(var1 instanceof Player var13)) {
                    Component var78 = Text.legacy("&cRun this in-game, standing where the quartz pillar should go.", 373192400);
                    var1.sendMessage(var78);
                    return;
                }

                ControlRoomManager var40 = this.controlRoom;
                Location var71 = var13.getLocation();
                var40.setArmorSpot(var71, 80678334);
                var9.armorLimit(630906960).reconcile(1764668093);
                Location var14 = var13.getLocation();
                String var72 = "&aArmor Limit centrepiece placed here &7(&f%s &7@ &f%.1f, %.1f, %.1f&7).";
                Object[] var92 = new Object[4];
                String var103 = var14.getWorld().getName();
                var92[0] = var103;
                Double var107 = var14.getX();
                var92[1] = var107;
                Double var111 = var14.getY();
                var92[2] = var111;
                Double var115 = var14.getZ();
                var92[3] = var115;
                Component var74 = Text.legacy(String.format(var72, var92), 373192400);
                var1.sendMessage(var74);
                Component var76 = Text.legacy(
                    "&7Prismatic Edge on this spot; the ring is &f6 &7blocks out and the &fentrance &7is the way you were facing.", 373192400
                );
                var1.sendMessage(var76);
                break;
            case "clear":
                this.controlRoom.clearArmorSpot(273714842);
                var9.armorLimit(630906960).reconcile(1764668093);
                Component var69 = Text.legacy("&eArmor Limit centrepiece cleared.", 373192400);
                var1.sendMessage(var69);
                break;
            case "status":
                Location var131 = this.controlRoom.armorSpot(1529295521);
                if (var131 != null) {
                    String var87 = "&7Armor Limit: &f%s &7@ &f%.1f, %.1f, %.1f";
                    Object[] var94 = new Object[4];
                    String var118 = var131.getWorld().getName();
                    var94[0] = var118;
                    Double var122 = var131.getX();
                    var94[1] = var122;
                    Double var126 = var131.getY();
                    var94[2] = var126;
                    Double var130 = var131.getZ();
                    var94[3] = var130;
                    Component var89 = Text.legacy(String.format(var87, var94), 373192400);
                    var1.sendMessage(var89);
                } else if (this.controlRoom.armorConfigured(1763373199)) {
                    Component var86 = Text.legacy(
                        "&7Armor Limit is set in world &f" + this.controlRoom.armorWorldName(708021833) + " &7but that dimension is &cnot loaded&7.", 373192400
                    );
                    var1.sendMessage(var86);
                } else {
                    Component var81 = Text.legacy("&7No Armor Limit spot set &7(&f/assessment controlroom armor set&7).", 373192400);
                    var1.sendMessage(var81);
                }
                break;
            default:
                String var79 = "/assessment controlroom armor <set|clear|status>";
                CommandHelp.hint(var1, var79, 80456809);
        }
    }

    private void handleEnchantSpot(CommandSender var1, String[] var2, int var3) {
        BlissPlugin var9 = (BlissPlugin)BlissPlugin.getPlugin(BlissPlugin.class);
        String var64;
        if (var2.length >= 3) {
            String var63 = var2[2];
            Locale var91 = Locale.ROOT;
            var64 = var63.toLowerCase(var91);
        } else {
            var64 = "status";
        }

        switch (var64) {
            case "set":
                if (!(var1 instanceof Player var131)) {
                    Component var85 = Text.legacy("&cRun this in-game, standing where the Enchantment Limit book should float.", 373192400);
                    var1.sendMessage(var85);
                    return;
                }

                ControlRoomManager var43 = this.controlRoom;
                Location var78 = var131.getLocation();
                var43.setEnchantSpot(var78, 207247187);
                var9.enchantLimit(1301378516).reconcile(1408290875);
                Location var14 = var131.getLocation();
                String var79 = "&aEnchantment Limit holograms moved here &7(&f%s &7@ &f%.1f, %.1f, %.1f&7).";
                Object[] var94 = new Object[4];
                String var118 = var14.getWorld().getName();
                var94[0] = var118;
                Double var122 = var14.getX();
                var94[1] = var122;
                Double var126 = var14.getY();
                var94[2] = var126;
                Double var130 = var14.getZ();
                var94[3] = var130;
                Component var81 = Text.legacy(String.format(var79, var94), 373192400);
                var1.sendMessage(var81);
                Component var83 = Text.legacy(
                    "&7The book floats &f1.2 &7blocks up; the spread is a &f6&7-block-wide stack of rings starting &f0.5 &7up and climbing &f1.5 &7blocks at a time.",
                    373192400
                );
                var1.sendMessage(var83);
                break;
            case "clear":
                this.controlRoom.clearEnchantSpot(1521374685);
                var9.enchantLimit(1301378516).reconcile(1408290875);
                Component var87 = Text.legacy("&eEnchantment Limit spot cleared &7— the books are gone until a new spot is set.", 373192400);
                var1.sendMessage(var87);
                break;
            case "status":
                Location var13 = this.controlRoom.enchantSpot(944846913);
                if (var13 != null) {
                    String var74 = "&7Enchantment holograms: &f%s &7@ &f%.1f, %.1f, %.1f";
                    Object[] var92 = new Object[4];
                    String var103 = var13.getWorld().getName();
                    var92[0] = var103;
                    Double var107 = var13.getX();
                    var92[1] = var107;
                    Double var111 = var13.getY();
                    var92[2] = var111;
                    Double var115 = var13.getZ();
                    var92[3] = var115;
                    Component var76 = Text.legacy(String.format(var74, var92), 373192400);
                    var1.sendMessage(var76);
                } else if (this.controlRoom.enchantConfigured(2144921445)) {
                    Component var73 = Text.legacy(
                        "&7Enchantment holograms are set in world &f"
                            + this.controlRoom.enchantWorldName(406902642)
                            + " &7but that dimension is &cnot loaded&7.",
                        373192400
                    );
                    var1.sendMessage(var73);
                } else {
                    Component var68 = Text.legacy(
                        "&7No enchantment-hologram spot set &7(&f/assessment controlroom enchants set&7) &7— no books stand until one is.", 373192400
                    );
                    var1.sendMessage(var68);
                }
                break;
            default:
                String var66 = "/assessment controlroom enchants <set|clear|status>";
                CommandHelp.hint(var1, var66, 80456809);
        }
    }

    private void handleCompass(CommandSender var1, String[] var2, int var3) {
        if (var2.length < 2) {
            String var62 = "/assessment compass <juggernaut|player|all|clear> [player]";
            CommandHelp.hint(var1, var62, 80456809);
        } else {
            String var17 = var2[1];
            Locale var64 = Locale.ROOT;
            String var9 = var17.toLowerCase(var64);
            switch (var9) {
                case "juggernaut":
                case "jug":
                    ItemStack var89 = HonoredCompass.juggernaut(267343802);
                    String var90 = "Juggernaut's Compass";
                    this.giveCompass(var1, var2, var89, var90, 1170753831);
                    break;
                case "player":
                    ItemStack var7 = HonoredCompass.player(2029116677);
                    String var8 = "Honored One Compass";
                    this.giveCompass(var1, var2, var7, var8, 1170753831);
                    break;
                case "all":
                    int var91 = 0;

                    for (Player var49 : Bukkit.getOnlinePlayers()) {
                        ItemStack var81 = HonoredCompass.player(2029116677);
                        BlissCoreItems.giveOrDrop(var49, var81, 41085121);
                        var91++;
                    }

                    Component var80 = Text.legacy("&aHanded an Honored One Compass to &e" + var91 + " &aplayer(s).", 373192400);
                    var1.sendMessage(var80);
                    break;
                case "clear":
                    if (var2.length < 3) {
                        String var70 = "/assessment compass clear <player>";
                        CommandHelp.hint(var1, var70, 80456809);
                        return;
                    }

                    Player var11 = Bukkit.getPlayerExact(var2[2]);
                    if (var11 == null) {
                        Component var77 = Text.legacy("&cPlayer not found / offline.", 373192400);
                        var1.sendMessage(var77);
                        return;
                    }

                    int var12 = this.clearCompasses(var11, 940378104);
                    String var88 = var11.getName();
                    Component var75 = Text.legacy("&eRemoved &c" + var12 + " &eHO compass(es) from " + var88 + ".", 373192400);
                    var1.sendMessage(var75);
                    break;
                default:
                    String var67 = "/assessment compass <juggernaut|player|all|clear> [player]";
                    CommandHelp.hint(var1, var67, 80456809);
            }
        }
    }

    private void handleCage(CommandSender var1, String[] var2, int var3) {
        String var75;
        if (var2.length >= 2) {
            String var74 = var2[1];
            Locale var111 = Locale.ROOT;
            var75 = var74.toLowerCase(var111);
        } else {
            var75 = "status";
        }

        switch (var75) {
            case "set":
                if (!(var1 instanceof Player var12)) {
                    Component var96 = Text.legacy("&cRun this in-game, standing on the spire.", 373192400);
                    var1.sendMessage(var96);
                    return;
                }

                UUID var13 = this.ho.current(1257858083);
                if (var13 == null) {
                    Component var84 = Text.legacy("&cSet an Honored One first.", 373192400);
                    var1.sendMessage(var84);
                    return;
                }

                double var14 = 5.0;
                if (var2.length >= 3) {
                    try {
                        double var114 = Double.parseDouble(var2[2]);
                        var14 = Math.max(1.0, var114);
                    } catch (NumberFormatException var130) {
                        Component var94 = Text.legacy("&cRadius must be a number.", 373192400);
                        var1.sendMessage(var94);
                        return;
                    }
                }

                CageManager var51 = this.cage;
                Location var117 = var12.getLocation();
                var51.set(var13, var117, var14, 2061408733);
                OfflinePlayer var16 = Bukkit.getOfflinePlayer(var13);
                String var112;
                if (var16.getName() == null) {
                    var112 = var13.toString();
                } else {
                    var112 = var16.getName();
                }

                Component var91 = Text.legacy(
                    "&aSpire cage armed around &6"
                        + var112
                        + " &a(radius "
                        + var14
                        + "). &7Engages at the ritual's end; dissolves when the title first transfers.",
                    373192400
                );
                var1.sendMessage(var91);
                break;
            case "clear":
                this.cage.clear(1660358252);
                Component var78 = Text.legacy("&eSpire cage cleared.", 373192400);
                var1.sendMessage(var78);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "juggernaut ritual");
                Component var81 = Text.legacy("&7Juggernaut rituals triggered.", 373192400);
                var1.sendMessage(var81);
                break;
            case "status":
                if (!this.cage.isActive(128579983)) {
                    Component var98 = Text.legacy("&7No spire cage is active.", 373192400);
                    var1.sendMessage(var98);
                    return;
                }

                OfflinePlayer var129 = Bukkit.getOfflinePlayer(this.cage.caged(702898857));
                String var107;
                if (var129.getName() == null) {
                    var107 = String.valueOf(this.cage.caged(702898857));
                } else {
                    var107 = var129.getName();
                }

                double var122 = this.cage.radius(1148470054);
                String var128;
                if (this.cage.isEngaged(756907884)) {
                    var128 = "&aengaged (confining)";
                } else {
                    var128 = "&earmed (engages at ritual end)";
                }

                Component var105 = Text.legacy("&7Spire cage: &6" + var107 + " &7(radius " + var122 + ") — " + var128 + "&7.", 373192400);
                var1.sendMessage(var105);
                break;
            default:
                String var82 = "/assessment cage <set [radius]|clear|status>";
                CommandHelp.hint(var1, var82, 80456809);
        }
    }

    private void handleRitual(CommandSender var1, int var2) {
        if (this.ritual.isRunning(596214312)) {
            Component var36 = Text.legacy("&eThe ritual is already running.", 373192400);
            var1.sendMessage(var36);
        } else {
            Location var10 = this.pedestal.pedestalLoc1(1941968134);
            if (var10 != null && var10.getWorld() != null) {
                Location var11 = var10.clone().add(0.5, 0.05, 0.5);
                UUID var12 = this.ho.current(1257858083);
                Player var31;
                if (var12 == null) {
                    var31 = null;
                } else {
                    var31 = Bukkit.getPlayer(var12);
                }

                HonoredSummonRitual var33 = this.ritual;
                var33.begin(var11, var31, 1631529935);
                String var43;
                if (var31 == null) {
                    var43 = " &7(no Honored One online — visuals only).";
                } else {
                    var43 = ".";
                }

                Component var42 = Text.legacy("&aHonored One summoning ritual started at the pedestal" + var43, 373192400);
                var1.sendMessage(var42);
            } else {
                Component var45 = Text.legacy("&cThe pedestal isn't configured (set it with /bliss pedestal).", 373192400);
                var1.sendMessage(var45);
            }
        }
    }

    private void giveCompass(CommandSender var1, String[] var2, ItemStack var3, String var4, int var5) {
        if (var2.length < 3) {
            String var20 = var2[1];
            Locale var31 = Locale.ROOT;
            String var22 = "/assessment compass " + var20.toLowerCase(var31) + " <player>";
            CommandHelp.hint(var1, var22, 80456809);
        } else {
            Player var9 = Bukkit.getPlayerExact(var2[2]);
            if (var9 == null) {
                Component var30 = Text.legacy("&cPlayer not found / offline.", 373192400);
                var1.sendMessage(var30);
            } else {
                BlissCoreItems.giveOrDrop(var9, var3, 41085121);
                String var26 = var9.getName();
                Component var28 = Text.legacy("&aGave " + var26 + " a &f" + var4 + "&a.", 373192400);
                var1.sendMessage(var28);
            }
        }
    }

    private int clearCompasses(Player var1, int var2) {
        int var6 = 0;
        ItemStack[] var7 = var1.getInventory().getContents();
        int var8 = 0;

        while (true) {
            int var27 = var7.length;
            if (var8 >= var27) {
                if (HonoredCompass.isHoCompass(var1.getInventory().getItemInOffHand(), 736733534)) {
                    var1.getInventory().setItemInOffHand(null);
                    var6++;
                }

                return var6;
            }

            if (HonoredCompass.isHoCompass(var7[var8], 736733534)) {
                PlayerInventory var26 = var1.getInventory();
                var26.setItem(var8, null);
                var6++;
            }

            var8++;
        }
    }

    private void usage(CommandSender var1, int var2) {
        String var4 = "assessment";
        String var5 = "Run the Assessment (Honored One) event.";
        Entry[] var10 = new Entry[13];
        String var8 = "honoredone <set <player>|clear|status>";
        String var9 = "Who carries the title";
        Entry var24 = CommandHelp.of(var8, var9, 1959232616);
        var10[0] = var24;
        String var25 = "window <start|stop|status>";
        String var49 = "The daily 2-hour tracking window";
        Entry var26 = CommandHelp.of(var25, var49, 1959232616);
        var10[1] = var26;
        String var27 = "compass <juggernaut|player|all|clear> [player]";
        String var50 = "Hand out tracking compasses";
        Entry var28 = CommandHelp.of(var27, var50, 1959232616);
        var10[2] = var28;
        String var29 = "cage <set [radius]|clear|status>";
        String var51 = "The cage around the pedestal";
        Entry var30 = CommandHelp.of(var29, var51, 1959232616);
        var10[3] = var30;
        String var31 = "ritual";
        String var52 = "Start the Honored One summon ritual";
        Entry var32 = CommandHelp.of(var31, var52, 1959232616);
        var10[4] = var32;
        String var33 = "day <1-11|next|status>";
        String var53 = "Advance or read the day counter";
        Entry var34 = CommandHelp.of(var33, var53, 1959232616);
        var10[5] = var34;
        String var35 = "controlroom <set|clear|tp|status>";
        String var54 = "The Control Room location";
        Entry var36 = CommandHelp.of(var35, var54, 1959232616);
        var10[6] = var36;
        String var37 = "controlroom enchants <set|clear|status>";
        String var55 = "Where the Enchantment Limit books float";
        Entry var38 = CommandHelp.of(var37, var55, 1959232616);
        var10[7] = var38;
        String var39 = "controlroom armor <set|clear|status>";
        String var56 = "Where the Armor Limit centrepiece stands";
        Entry var40 = CommandHelp.of(var39, var56, 1959232616);
        var10[8] = var40;
        String var41 = "controlroom border <set|clear|status>";
        String var57 = "Where the World Border dial floats";
        Entry var42 = CommandHelp.of(var41, var57, 1959232616);
        var10[9] = var42;
        String var43 = "controlroom potions <set|clear|status>";
        String var58 = "Where the Potion Limit bottles float";
        Entry var44 = CommandHelp.of(var43, var58, 1959232616);
        var10[10] = var44;
        String var45 = "controlroom ask";
        String var59 = "TEST: put the 'ready to leave?' question now";
        Entry var46 = CommandHelp.of(var45, var59, 1959232616);
        var10[11] = var46;
        String var47 = "controlroom leave";
        String var60 = "TEST: leave now — departure ritual, flight, pedestal finale";
        Entry var48 = CommandHelp.of(var47, var60, 1959232616);
        var10[12] = var48;
        List var11 = List.of(var10);
        CommandHelp.show(var1, var4, var5, var11, 696249826);
    }

    public List<String> onTabComplete(CommandSender var1, Command var2, String var3, String[] var4) {
        ArrayList var5 = new ArrayList();
        ArrayList var8 = var5;
        if (var4.length == 1) {
            String[] var180 = new String[7];
            String var351 = "honoredone";
            var180[0] = var351;
            String var352 = "window";
            var180[1] = var352;
            String var353 = "compass";
            var180[2] = var353;
            String var354 = "cage";
            var180[3] = var354;
            String var355 = "ritual";
            var180[4] = var355;
            String var356 = "day";
            var180[5] = var356;
            String var357 = "controlroom";
            var180[6] = var357;
            String[] var359 = var180;
            int var362 = var180.length;

            for (byte var370 = 0; var370 < var362; var370 += 1) {
                String var377 = var359[var370];
                String var263 = var4[0];
                Locale var312 = Locale.ROOT;
                String var264 = var263.toLowerCase(var312);
                if (var377.startsWith(var264)) {
                    var8.add(var377);
                }
            }
        } else if (var4.length == 2) {
            String var20 = var4[0];
            Locale var195 = Locale.ROOT;
            String var9 = var20.toLowerCase(var195);
            switch (var9) {
                case "honoredone":
                case "ho":
                    String[] var61 = new String[3];
                    String var328 = "set";
                    var61[0] = var328;
                    String var329 = "clear";
                    var61[1] = var329;
                    String var330 = "status";
                    var61[2] = var330;
                    String[] var365 = var61;
                    int var373 = var61.length;

                    for (byte var380 = 0; var380 < var373; var380 += 1) {
                        String var386 = var365[var380];
                        var8.add(var386);
                    }
                    break;
                case "window":
                    String[] var51 = new String[3];
                    String var325 = "start";
                    var51[0] = var325;
                    String var326 = "stop";
                    var51[1] = var326;
                    String var327 = "status";
                    var51[2] = var327;
                    String[] var364 = var51;
                    int var372 = var51.length;

                    for (byte var379 = 0; var379 < var372; var379 += 1) {
                        String var385 = var364[var379];
                        var8.add(var385);
                    }
                    break;
                case "compass":
                    String[] var71 = new String[4];
                    String var331 = "juggernaut";
                    var71[0] = var331;
                    String var332 = "player";
                    var71[1] = var332;
                    String var333 = "all";
                    var71[2] = var333;
                    String var334 = "clear";
                    var71[3] = var334;
                    String[] var366 = var71;
                    int var374 = var71.length;

                    for (byte var381 = 0; var381 < var374; var381 += 1) {
                        String var387 = var366[var381];
                        var8.add(var387);
                    }
                    break;
                case "cage":
                    String[] var41 = new String[3];
                    String var322 = "set";
                    var41[0] = var322;
                    String var323 = "clear";
                    var41[1] = var323;
                    String var324 = "status";
                    var41[2] = var324;
                    String[] var363 = var41;
                    int var371 = var41.length;

                    for (byte var378 = 0; var378 < var371; var378 += 1) {
                        String var384 = var363[var378];
                        var8.add(var384);
                    }
                    break;
                case "day":
                    String[] var81 = new String[13];
                    String var335 = "next";
                    var81[0] = var335;
                    String var336 = "status";
                    var81[1] = var336;
                    String var337 = "1";
                    var81[2] = var337;
                    String var338 = "2";
                    var81[3] = var338;
                    String var339 = "3";
                    var81[4] = var339;
                    String var340 = "4";
                    var81[5] = var340;
                    String var341 = "5";
                    var81[6] = var341;
                    String var342 = "6";
                    var81[7] = var342;
                    String var343 = "7";
                    var81[8] = var343;
                    String var344 = "8";
                    var81[9] = var344;
                    String var345 = "9";
                    var81[10] = var345;
                    String var346 = "10";
                    var81[11] = var346;
                    String var347 = "11";
                    var81[12] = var347;
                    String[] var367 = var81;
                    int var375 = var81.length;

                    for (byte var382 = 0; var382 < var375; var382 += 1) {
                        String var388 = var367[var382];
                        var8.add(var388);
                    }
                    break;
                case "controlroom":
                case "cr":
                    String[] var30 = new String[10];
                    String var7 = "set";
                    var30[0] = var7;
                    String var313 = "clear";
                    var30[1] = var313;
                    String var314 = "tp";
                    var30[2] = var314;
                    String var315 = "status";
                    var30[3] = var315;
                    String var316 = "enchants";
                    var30[4] = var316;
                    String var317 = "armor";
                    var30[5] = var317;
                    String var318 = "border";
                    var30[6] = var318;
                    String var319 = "potions";
                    var30[7] = var319;
                    String var320 = "ask";
                    var30[8] = var320;
                    String var321 = "leave";
                    var30[9] = var321;

                    for (String var14 : var30) {
                        var8.add(var14);
                    }
            }
        } else if (var4.length == 3) {
            boolean var191;
            if ((!var4[0].equalsIgnoreCase("honoredone") && !var4[0].equalsIgnoreCase("ho") || !var4[1].equalsIgnoreCase("set"))
                && (
                    !var4[0].equalsIgnoreCase("compass")
                        || !var4[1].equalsIgnoreCase("juggernaut")
                            && !var4[1].equalsIgnoreCase("jug")
                            && !var4[1].equalsIgnoreCase("player")
                            && !var4[1].equalsIgnoreCase("clear")
                )) {
                var191 = false;
            } else {
                var191 = true;
            }

            if (var191) {
                for (Player var368 : Bukkit.getOnlinePlayers()) {
                    String var240 = var368.getName();
                    var8.add(var240);
                }
            }

            if (var4[0].equalsIgnoreCase("controlroom") || var4[0].equalsIgnoreCase("cr")) {
                String var154 = var4[1];
                Locale var246 = Locale.ROOT;
                if (!var154.toLowerCase(var246).startsWith("ench")) {
                    String var158 = var4[1];
                    Locale var249 = Locale.ROOT;
                    if (!var158.toLowerCase(var249).startsWith("armo")) {
                        String var162 = var4[1];
                        Locale var252 = Locale.ROOT;
                        if (!var162.toLowerCase(var252).startsWith("bord")) {
                            String var166 = var4[1];
                            Locale var255 = Locale.ROOT;
                            if (!var166.toLowerCase(var255).startsWith("poti")) {
                                return var8;
                            }
                        }
                    }
                }

                String[] var170 = new String[3];
                String var348 = "set";
                var170[0] = var348;
                String var349 = "clear";
                var170[1] = var349;
                String var350 = "status";
                var170[2] = var350;
                String[] var361 = var170;
                int var369 = var170.length;

                for (byte var376 = 0; var376 < var369; var376 += 1) {
                    String var383 = var361[var376];
                    var8.add(var383);
                }
            }
        }

        return var8;
    }

    public static String yzdndzxisf(byte[] var0, byte[] var1, int var2) {
        byte[] var7 = Integer.toString(var2).getBytes();
        int var8 = 0;

        while (true) {
            int var16 = var0.length;
            if (var8 >= var16) {
                Charset var30 = StandardCharsets.UTF_16;
                return new String(var0, var30);
            }

            byte var19 = var0[var8];
            int var34 = var7.length;
            int var31 = var8 % var34;
            byte var27 = var7[var31];
            byte var21 = (byte)(var19 ^ var27);
            var0[var8] = var21;
            byte var22 = var0[var8];
            int var36 = var1.length;
            int var33 = var8 % var36;
            byte var29 = var1[var33];
            byte var24 = (byte)(var22 ^ var29);
            var0[var8] = var24;
            var8++;
        }
    }

    private static byte[] hvhbkjkutahluvo() {
        return new byte[]{
            -118,
            -78,
            5,
            104,
            8,
            82,
            67,
            121,
            26,
            115,
            86,
            64,
            70,
            114,
            120,
            48,
            38,
            8,
            117,
            92,
            20,
            113,
            43,
            123,
            31,
            116,
            55,
            74,
            45,
            45,
            122,
            38,
            59,
            30,
            25,
            59,
            76,
            124,
            16,
            76,
            11,
            21,
            68,
            82,
            60,
            9,
            38,
            94
        };
    }
}
