package com.bliss.dragongames;

import com.bliss.admin.CommandHelp;
import com.bliss.villager.Text;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class DragonGamesCommand {
    public static final String SUB = "dragongames";
    private static final String ADMIN = "bliss.admin";
    private final DragonGamesManager games;
    private final DragonGamesShop shop;

    public DragonGamesCommand(DragonGamesManager var1, DragonGamesShop var2, int var3) {
        this.games = var1;
        this.shop = var2;
    }

    public static boolean isSelfCheck(String[] var0, int var1) {
        boolean var11;
        if (var0.length == 2 && var0[0].equalsIgnoreCase("points") && var0[1].equalsIgnoreCase("check")) {
            var11 = true;
        } else {
            var11 = false;
        }

        return var11;
    }

    public void handle(CommandSender var1, String[] var2, int var3) {
        if (var2.length < 1) {
            usage(var1, 1701653884);
        } else {
            String var9 = var2[0].toLowerCase();
            switch (var9) {
                case "start":
                    this.games.startGame(277176602);
                    break;
                case "end":
                    if (!this.games.isRunning(320515049)) {
                        Component var52 = Text.legacy("&cThe Dragon Games are not running.", 373192400);
                        var1.sendMessage(var52);
                        return;
                    }

                    this.games.endGame(1530310747);
                    break;
                case "points":
                    int var72 = var2.length;
                    String[] var65 = Arrays.copyOfRange(var2, 1, var72);
                    this.points(var1, var65, 579599192);
                    break;
                case "bloodmoon":
                    int var70 = var2.length;
                    String[] var62 = Arrays.copyOfRange(var2, 1, var70);
                    this.bloodMoon(var1, var62, 686505404);
                    break;
                case "shop":
                    int var68 = var2.length;
                    String[] var59 = Arrays.copyOfRange(var2, 1, var68);
                    this.shop(var1, var59, 318443571);
                    break;
                default:
                    usage(var1, 1701653884);
            }
        }
    }

    private void shop(CommandSender var1, String[] var2, int var3) {
        if (!var1.hasPermission("bliss.admin")) {
            Component var23 = Text.legacy("&cNo permission.", 373192400);
            var1.sendMessage(var23);
        } else if (var2.length < 1 || !var2[0].equalsIgnoreCase("edit")) {
            String var30 = "/bliss dragongames shop edit";
            CommandHelp.hint(var1, var30, 80456809);
        } else if (var1 instanceof Player var6) {
            DragonGamesShop var19 = this.shop;
            var19.openEditor(var6, 1544009571);
        } else {
            Component var29 = Text.legacy("&cPlayer only.", 373192400);
            var1.sendMessage(var29);
        }
    }

    private void bloodMoon(CommandSender var1, String[] var2, int var3) {
        BloodMoon var6 = this.games.bloodMoon(92689670);
        if (var6 == null) {
            Component var45 = Text.legacy("&cBlood Moon unavailable.", 373192400);
            var1.sendMessage(var45);
        } else if (var2.length < 1) {
            String var47 = "/bliss dragongames bloodmoon <start|end>";
            CommandHelp.hint(var1, var47, 80456809);
        } else {
            String var7 = var2[0].toLowerCase();
            switch (var7) {
                case "start":
                    if (var6.active(687437596)) {
                        Component var52 = Text.legacy("&cThe Blood Moon is already up.", 373192400);
                        var1.sendMessage(var52);
                        return;
                    }

                    var6.begin(327297774);
                    Component var54 = Text.legacy("&aThe Blood Moon has begun - clock parked, bar up, mobs buffed.", 373192400);
                    var1.sendMessage(var54);
                    break;
                case "end":
                    if (!var6.active(687437596)) {
                        Component var56 = Text.legacy("&cNo Blood Moon is up.", 373192400);
                        var1.sendMessage(var56);
                        return;
                    }

                    if (this.games.isRunning(320515049)) {
                        this.games.endGame(1530310747);
                    } else {
                        var6.end(1481613845);
                    }

                    Component var58 = Text.legacy("&aThe Blood Moon is over - clock and sky restored.", 373192400);
                    var1.sendMessage(var58);
                    break;
                default:
                    String var50 = "/bliss dragongames bloodmoon <start|end>";
                    CommandHelp.hint(var1, var50, 80456809);
            }
        }
    }

    private void points(CommandSender var1, String[] var2, int var3) {
        if (var2.length < 1) {
            usage(var1, 1701653884);
        } else if (!this.games.isRunning(320515049)) {
            Component var145 = Text.legacy("&cThe Dragon Games are not running.", 373192400);
            var1.sendMessage(var145);
        } else {
            String var8 = var2[0].toLowerCase();
            switch (var8) {
                case "check":
                    if (var2.length < 2) {
                        if (var1 instanceof Player var160) {
                            DragonGamesManager var112 = this.games;
                            UUID var149 = var160.getUniqueId();
                            Component var115 = Text.legacy("&a Your Current Balance is &2" + var112.points(var149, 77304185) + "$", 373192400);
                            var1.sendMessage(var115);
                            return;
                        }

                        Component var110 = Text.legacy("&cPlayer only.", 373192400);
                        var1.sendMessage(var110);
                        return;
                    }

                    if (!var1.hasPermission("bliss.admin")) {
                        Component var101 = Text.legacy("&cNo permission.", 373192400);
                        var1.sendMessage(var101);
                        return;
                    }

                    Player var11 = Bukkit.getPlayer(var2[1]);
                    if (var11 == null) {
                        Component var104 = Text.legacy("&cPlayer not online.", 373192400);
                        var1.sendMessage(var104);
                        return;
                    }

                    String var106 = var11.getName();
                    DragonGamesManager var146 = this.games;
                    UUID var157 = var11.getUniqueId();
                    int var147 = var146.points(var157, 77304185);
                    Component var108 = Text.legacy("&a" + var106 + "'s Current Balance is &2" + var147 + "$", 373192400);
                    var1.sendMessage(var108);
                    break;
                case "set":
                case "add":
                case "remove":
                    if (!var1.hasPermission("bliss.admin")) {
                        Component var118 = Text.legacy("&cNo permission.", 373192400);
                        var1.sendMessage(var118);
                        return;
                    }

                    if (var2.length < 3) {
                        String var121 = "/bliss dragongames points " + var8 + " <player> <number>";
                        CommandHelp.hint(var1, var121, 80456809);
                        return;
                    }

                    Player var161 = Bukkit.getPlayer(var2[1]);
                    if (var161 == null) {
                        Component var124 = Text.legacy("&cPlayer not online.", 373192400);
                        var1.sendMessage(var124);
                        return;
                    }

                    int var12;
                    try {
                        var12 = Integer.parseInt(var2[2]);
                    } catch (NumberFormatException var163) {
                        Component var127 = Text.legacy("&cAmount must be a number.", 373192400);
                        var1.sendMessage(var127);
                        return;
                    }

                    switch (var8) {
                        case "set":
                            DragonGamesManager var79 = this.games;
                            UUID var138 = var161.getUniqueId();
                            var79.setPoints(var138, var12, 287966591);
                            break;
                        case "add":
                            DragonGamesManager var74 = this.games;
                            UUID var130 = var161.getUniqueId();
                            var74.addPoints(var130, var12, 1465786999);
                            break;
                        case "remove":
                            DragonGamesManager var77 = this.games;
                            UUID var136 = var161.getUniqueId();
                            int var155 = -var12;
                            var77.addPoints(var136, var155, 1465786999);
                    }

                    String var132 = var161.getName();
                    DragonGamesManager var152 = this.games;
                    UUID var159 = var161.getUniqueId();
                    int var153 = var152.points(var159, 77304185);
                    Component var134 = Text.legacy("&a" + var132 + "'s balance is now &2" + var153 + "$", 373192400);
                    var1.sendMessage(var134);
                    break;
                default:
                    usage(var1, 1701653884);
            }
        }
    }

    public static void usage(CommandSender var0, int var1) {
        if (!var0.hasPermission("bliss.admin")) {
            String var18 = "bliss";
            String var19 = "Dragon Games.";
            String var22 = "dragongames points check";
            String var26 = "Show your points balance";
            CommandHelp.Entry var23 = CommandHelp.of(var22, var26, 1959232616);
            CommandHelp.show(var0, var18, var19, var23, 1122560938);
        } else {
            String var17 = "bliss";
            String var4 = "Run the Dragon Games event.";
            String var5 = "dragongames start";
            String var6 = "Enrol everyone online, pin the 1000-block border";
            CommandHelp.Entry var20 = CommandHelp.of(var5, var6, 1959232616);
            String var24 = "dragongames end";
            String var7 = "Lift the borders and restore tab names";
            CommandHelp.Entry var25 = CommandHelp.of(var24, var7, 1959232616);
            String var27 = "dragongames bloodmoon <start|end>";
            String var8 = "Bring the Blood Moon up by hand (ending it ends the games)";
            CommandHelp.Entry var28 = CommandHelp.of(var27, var8, 1959232616);
            String var29 = "dragongames points check [player]";
            String var9 = "Show a balance";
            CommandHelp.Entry var30 = CommandHelp.of(var29, var9, 1959232616);
            String var31 = "dragongames points set <player> <number>";
            String var10 = "Set a player's points";
            CommandHelp.Entry var32 = CommandHelp.of(var31, var10, 1959232616);
            String var33 = "dragongames points add <player> <number>";
            String var11 = "Give a player points";
            CommandHelp.Entry var34 = CommandHelp.of(var33, var11, 1959232616);
            String var35 = "dragongames points remove <player> <number>";
            String var12 = "Take points from a player";
            CommandHelp.Entry var36 = CommandHelp.of(var35, var12, 1959232616);
            String var37 = "dragongames shop edit";
            String var13 = "Edit the shop: what each slot sells (a filled shulker sells its contents), its price";
            CommandHelp.Entry var38 = CommandHelp.of(var37, var13, 1959232616);
            List var21 = List.of(var20, var25, var28, var30, var32, var34, var36, var38);
            CommandHelp.show(var0, var17, var4, var21, 696249826);
        }
    }

    public List<String> complete(CommandSender var1, String[] var2, int var3) {
        ArrayList var4 = new ArrayList();
        ArrayList var8 = var4;
        boolean var9 = var1.hasPermission("bliss.admin");
        if (var2.length == 1) {
            List var32;
            if (var9) {
                String var18 = "start";
                String var107 = "end";
                String var5 = "bloodmoon";
                String var6 = "points";
                String var7 = "shop";
                var32 = List.of(var18, var107, var5, var6, var7);
            } else {
                var32 = List.of("points");
            }

            for (String var12 : (List<String>)var32) {
                String var110 = var2[0].toLowerCase();
                if (var12.startsWith(var110)) {
                    var8.add(var12);
                }
            }
        } else if (var2[0].equalsIgnoreCase("shop")) {
            if (var2.length == 2 && var9) {
                String var39 = "edit";
                String var117 = var2[1].toLowerCase();
                if (var39.startsWith(var117)) {
                    var4.add("edit");
                }
            }
        } else if (var2[0].equalsIgnoreCase("bloodmoon")) {
            if (var2.length == 2 && var9) {
                for (String var155 : List.of("start", "end")) {
                    String var125 = var2[1].toLowerCase();
                    if (var155.startsWith(var125)) {
                        var8.add(var155);
                    }
                }
            }
        } else if (var2[0].equalsIgnoreCase("points")) {
            if (var2.length == 2) {
                List<String> var68;
                if (var9) {
                    String var67 = "check";
                    String var130 = "set";
                    String var148 = "add";
                    String var151 = "remove";
                    var68 = List.of(var67, var130, var148, var151);
                } else {
                    var68 = List.of("check");
                }

                for (String var158 : var68) {
                    String var133 = var2[1].toLowerCase();
                    if (var158.startsWith(var133)) {
                        var8.add(var158);
                    }
                }
            } else if (var2.length == 3 && var9) {
                for (Player var157 : Bukkit.getOnlinePlayers()) {
                    String var93 = var157.getName().toLowerCase();
                    String var138 = var2[2].toLowerCase();
                    if (var93.startsWith(var138)) {
                        String var140 = var157.getName();
                        var8.add(var140);
                    }
                }
            } else if (var2.length == 4 && var9 && !var2[1].equalsIgnoreCase("check")) {
                var4.add("<number>");
            }
        }

        return var8;
    }
}
