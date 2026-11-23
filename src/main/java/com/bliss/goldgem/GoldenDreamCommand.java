package com.bliss.goldgem;

import com.bliss.BlissPlugin;
import com.bliss.admin.ConfigMenuListener;
import com.bliss.core.GlobalState;
import com.bliss.nick.NickCommand;
import com.bliss.villager.Text;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public final class GoldenDreamCommand implements CommandExecutor, TabCompleter {
    private final BlissPlugin plugin;
    private final GoldenDreamMenu menu;
    private final GoldenDreamWorld world;
    private final NickCommand nick;

    public GoldenDreamCommand(BlissPlugin var1, GoldenDreamMenu var2, GoldenDreamWorld var3, NickCommand var4, int var5) {
        this.plugin = var1;
        this.menu = var2;
        this.world = var3;
        this.nick = var4;
    }

    public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
        if (!var1.hasPermission("bliss.admin")) {
            Component var130 = Text.legacy("&cYou don't have permission.", 373192400);
            var1.sendMessage(var130);
            return true;
        } else if (var4.length == 0) {
            this.help(var1, 91981405);
            return true;
        } else {
            String var10 = var4[0].toLowerCase();
            switch (var10) {
                case "config":
                    if (!(var1 instanceof Player var12)) {
                        Component var144 = Text.legacy("&cThe config menu is in-game only.", 373192400);
                        var1.sendMessage(var144);
                        return true;
                    }

                    ConfigMenuListener var48 = this.plugin.configMenu(835099904);
                    var48.open(var12, true, 1367868276);
                    break;
                case "progression":
                    this.progression(var1, var4, 46255714);
                    break;
                case "menu":
                    if (!(var1 instanceof Player var198)) {
                        Component var174 = Text.legacy("&cThe Golden Dream menu is in-game only.", 373192400);
                        var1.sendMessage(var174);
                        return true;
                    }

                    GoldenDreamMenu var98 = this.menu;
                    var98.open(var198, 621099676);
                    break;
                case "import":
                    if (var4.length < 2 || !var4[1].equalsIgnoreCase("confirm")) {
                        Component var150 = Text.legacy(
                            "&eThis copies the world folder at &f/goldendream_import &ein over the memory overworld (nether and end are left as-is); everyone in the dream is moved out first.",
                            373192400
                        );
                        var1.sendMessage(var150);
                        Component var152 = Text.legacy("&eType &f/goldendream import confirm &eto do it.", 373192400);
                        var1.sendMessage(var152);
                        return true;
                    }

                    GoldenDreamWorld var57 = this.world;
                    var57.importOverworld(var1, 189351067);
                    break;
                case "reset":
                    if (var4.length < 2 || !var4[1].equalsIgnoreCase("confirm")) {
                        Component var139 = Text.legacy(
                            "&cThis deletes and regenerates the pocket, overworld, nether and end, and drops every dream record — a brand-new dream.",
                            373192400
                        );
                        var1.sendMessage(var139);
                        Component var141 = Text.legacy("&eType &f/goldendream reset confirm &eto do it.", 373192400);
                        var1.sendMessage(var141);
                        return true;
                    }

                    GoldenDreamWorld var36 = this.world;
                    var36.resetAll(var1, 83591313);
                    break;
                case "skip":
                case "wake":
                    this.skipOrWake(var1, var4, 1874881315);
                    break;
                case "walk":
                    this.walk(var1, var4, 2019717153);
                    break;
                case "thunder":
                    if (var4.length >= 2 && var4[1].equalsIgnoreCase("float")) {
                        int var197 = WakeFloat.start(this.plugin, 734022666);
                        Component var169;
                        if (var197 == 0) {
                            var169 = Text.legacy("&cNobody in the dream to wake.", 373192400);
                        } else {
                            var169 = Text.legacy("&6" + var197 + " &7in the dream drift up - kicked awake 20 blocks up.", 373192400);
                        }

                        var1.sendMessage(var169);
                        return true;
                    }

                    if (!(var1 instanceof Player var196)) {
                        Component var164 = Text.legacy("&cIn-game only.", 373192400);
                        var1.sendMessage(var164);
                        return true;
                    }

                    BlissPlugin var77 = this.plugin;
                    Location var160 = var196.getLocation();
                    PurgeThunder.strike(var77, var160, 1428767857);
                    Component var162 = Text.legacy("&6The great thunder &7is drawn past you - look up.", 373192400);
                    var196.sendMessage(var162);
                    break;
                case "memory":
                    NickCommand var89 = this.nick;
                    String var194 = "/goldendream";
                    var89.run(var1, var4, var194, 349418347);
                    break;
                case "nick":
                    NickCommand var65 = this.nick;
                    int var195 = var4.length;
                    String[] var189 = Arrays.copyOfRange(var4, 1, var195);
                    String var193 = "/goldendream nick";
                    var65.run(var1, var189, var193, 349418347);
                    break;
                default:
                    this.help(var1, 91981405);
            }

            return true;
        }
    }

    private void progression(CommandSender var1, String[] var2, int var3) {
        GlobalState var12 = this.plugin.dreamState(503017964);
        if (var12 == null) {
            Component var117 = Text.legacy("&cThe dream's state is unavailable.", 373192400);
            var1.sendMessage(var117);
        } else {
            String var25;
            if (var2.length >= 2) {
                var25 = var2[1].toLowerCase();
            } else {
                var25 = "status";
            }

            switch (var25) {
                case "status":
                    int var146 = var12.stage(1483650410);
                    boolean var179 = var12.prog1(494064183);
                    boolean var180 = var12.prog2(1337683067);
                    boolean var181 = var12.prog3(1589905580);
                    boolean var182 = var12.prog4(286518450);
                    boolean var183 = var12.netheriteAllowed(613865620);
                    boolean var184 = var12.enderpearls(155177658);
                    Component var148 = Text.legacy(
                        "&6Golden Dream &7is stuck on &fProgression "
                            + var146
                            + " &7(prog1 "
                            + var179
                            + ", prog2 "
                            + var180
                            + ", prog3 "
                            + var181
                            + ", prog4 "
                            + var182
                            + "; netherite "
                            + var183
                            + ", pearls "
                            + var184
                            + ")",
                        373192400
                    );
                    var1.sendMessage(var148);
                    return;
                case "0":
                    var12.setProg1Started(false, 1672226358);
                    var12.setProg1(true, 1805549590);
                    var12.setProg2(false, 1105469454);
                    var12.setProg3(false, 1348581053);
                    var12.setProg4(false, 946676425);
                    var12.setEnderpearls(false, 567146737);
                    var12.setProgression(0, 1556136516);
                    break;
                case "1":
                    var12.setProg1Started(true, 1672226358);
                    var12.setProg1(true, 1805549590);
                    var12.setProg2(false, 1105469454);
                    var12.setProg3(false, 1348581053);
                    var12.setProg4(false, 946676425);
                    var12.setEnderpearls(false, 567146737);
                    var12.setProgression(1, 1556136516);
                    break;
                case "2":
                    var12.setProg1(false, 1805549590);
                    var12.setProg2(true, 1105469454);
                    var12.setProg3(false, 1348581053);
                    var12.setProg4(false, 946676425);
                    var12.setProgression(2, 1556136516);
                    break;
                case "3":
                    var12.setProg1(false, 1805549590);
                    var12.setProg2(false, 1105469454);
                    var12.setProg3(true, 1348581053);
                    var12.setProg4(false, 946676425);
                    var12.setProgression(3, 1556136516);
                    break;
                case "4":
                    var12.setProg1(false, 1805549590);
                    var12.setProg2(false, 1105469454);
                    var12.setProg3(false, 1348581053);
                    var12.setProg4(true, 946676425);
                    var12.setProgression(4, 1556136516);
                    break;
                case "5":
                    var12.setProg1(false, 1805549590);
                    var12.setProg2(false, 1105469454);
                    var12.setProg3(false, 1348581053);
                    var12.setProg4(true, 946676425);
                    var12.setEnderpearls(false, 567146737);
                    var12.setNetheriteAllowed(true, 891609125);
                    var12.setProgression(5, 1556136516);
                    break;
                case "reset":
                    var12.setProg1Started(true, 1672226358);
                    var12.setProg1(false, 1805549590);
                    var12.setProg2(false, 1105469454);
                    var12.setProg3(false, 1348581053);
                    var12.setProg4(false, 946676425);
                    var12.setProgression(0, 1556136516);
                    break;
                default:
                    Component var137 = Text.legacy("&eUsage: /goldendream progression <0|1|2|3|4|5|reset|status>", 373192400);
                    var1.sendMessage(var137);
                    return;
            }

            var12.save(343464104);
            Component var185 = Text.legacy("&6Now on progression " + var12.stage(1483650410) + " on the golden dream", 373192400);

            for (Player var16 : Bukkit.getOnlinePlayers()) {
                if (var16.hasPermission("bliss.admin")) {
                    var16.sendMessage(var185);
                }
            }

            if (!(var1 instanceof Player)) {
                var1.sendMessage(var185);
            }
        }
    }

    private void skipOrWake(CommandSender var1, String[] var2, int var3) {
        boolean var7 = var2[0].equalsIgnoreCase("wake");
        Player var61;
        if (var2.length >= 2) {
            var61 = Bukkit.getPlayer(var2[1]);
            if (var61 == null) {
                Component var55 = Text.legacy("&cPlayer not found: " + var2[1], 373192400);
                var1.sendMessage(var55);
                return;
            }
        } else if (var1 instanceof Player p) {
            var61 = p;
        } else {
            String var57;
            if (var7) {
                var57 = "wake";
            } else {
                var57 = "skip";
            }

            Component var50 = Text.legacy("&cName a player: /goldendream " + var57 + " <player>", 373192400);
            var1.sendMessage(var50);
            return;
        }

        if (var7) {
            GoldenDreamWorld var40 = this.world;
            Component var56;
            if (var40.wake(var61, 1928909343)) {
                var56 = Text.legacy("&6" + var61.getName() + " &7wakes from the dream.", 373192400);
            } else {
                var56 = Text.legacy("&c" + var61.getName() + " is not in the dream.", 373192400);
            }

            var1.sendMessage(var56);
        } else {
            GoldenDreamWorld var30 = this.world;
            UUID var58 = var61.getUniqueId();
            Component var35;
            if (var30.skip(var58, 1580032486)) {
                var35 = Text.legacy("&6" + var61.getName() + " &7leaves the pocket now.", 373192400);
            } else {
                var35 = Text.legacy("&c" + var61.getName() + " is not walking the pocket.", 373192400);
            }

            var1.sendMessage(var35);
        }
    }

    private void walk(CommandSender var1, String[] var2, int var3) {
        String var19;
        if (var2.length >= 2) {
            var19 = var2[1].toLowerCase();
        } else {
            var19 = "";
        }

        String var7 = var19;
        if (!var19.equals("start") && !var19.equals("stop") && !var19.equals("send")) {
            Component var116 = Text.legacy("&eUsage: /goldendream walk <start|stop|send> [player]", 373192400);
            var1.sendMessage(var116);
        } else {
            ArrayList<UUID> var26 = new ArrayList<>();
            String var9 = null;
            if (var2.length >= 3) {
                Player var10 = Bukkit.getPlayer(var2[2]);
                if (var10 == null) {
                    Component var84 = Text.legacy("&cPlayer not found: " + var2[2], 373192400);
                    var1.sendMessage(var84);
                    return;
                }

                UUID var86 = var10.getUniqueId();
                var26.add(var86);
                var9 = var10.getName();
            } else {
                List var114 = this.world.walking(1918167828);
                var26.addAll(var114);
            }

            int var119 = 0;

            for (UUID var12 : var26) {
                boolean var74;
                switch (var7) {
                    case "start":
                        GoldenDreamWorld var61 = this.world;
                        var74 = var61.startWalk(var12, 398901065);
                        break;
                    case "stop":
                        GoldenDreamWorld var56 = this.world;
                        var74 = var56.stopWalk(var12, 1117990740);
                        break;
                    default:
                        GoldenDreamWorld var59 = this.world;
                        var74 = var59.skip(var12, 1580032486);
                }

                if (var74) {
                    var119++;
                }
            }

            if (var119 == 0) {
                String var95;
                if (var9 == null) {
                    var95 = "&cNobody is walking the pocket.";
                } else {
                    var95 = "&c" + var9 + " is not walking the pocket.";
                }

                Component var94 = Text.legacy(var95, 373192400);
                var1.sendMessage(var94);
            } else {
                String var69;
                if (var9 != null) {
                    var69 = var9;
                } else {
                    var69 = var119 + " in the pocket";
                }
                Component var105 = Text.legacy(switch (var7) {
                    case "start" -> "&7The walk starts: &6" + var69;
                    case "stop" -> "&7The walk stops, free to move: &6" + var69;
                    default -> "&7Into the memory now: &6" + var69;
                }, 373192400);
                var1.sendMessage(var105);
            }
        }
    }

    private void help(CommandSender var1, int var2) {
        Component var28 = Text.legacy("&6&lGolden Dream &7— commands", 373192400);
        var1.sendMessage(var28);
        Component var30 = Text.legacy("&e/goldendream config &7— the dream's own config: the same toggles as /bliss config, kept apart", 373192400);
        var1.sendMessage(var30);
        Component var32 = Text.legacy(
            "&e/goldendream progression <0-5|reset|status> &7— the stage the dream is stuck on; its worlds follow those rules", 373192400
        );
        var1.sendMessage(var32);
        Component var34 = Text.legacy("&e/goldendream menu &7— the admin menu: import world / reset (click to arm, click again to confirm)", 373192400);
        var1.sendMessage(var34);
        Component var36 = Text.legacy("&e/goldendream import confirm &7— copy &f/goldendream_import &7in as the memory overworld", 373192400);
        var1.sendMessage(var36);
        Component var38 = Text.legacy("&e/goldendream reset confirm &7— a brand-new dream: fresh worlds, every record dropped", 373192400);
        var1.sendMessage(var38);
        Component var40 = Text.legacy(
            "&e/goldendream walk start [player] &7— start them walking the golden sky now (no name: everyone in the pocket)", 373192400
        );
        var1.sendMessage(var40);
        Component var42 = Text.legacy("&e/goldendream walk stop [player] &7— stop the walk, free to move again", 373192400);
        var1.sendMessage(var42);
        Component var44 = Text.legacy("&e/goldendream walk send [player] &7— straight into the memory now", 373192400);
        var1.sendMessage(var44);
        Component var46 = Text.legacy("&e/goldendream skip [player] &7— end the pocket walk now, into the memory this tick", 373192400);
        var1.sendMessage(var46);
        Component var48 = Text.legacy("&e/goldendream wake [player] &7— leave the dream for good; inventory and place restored", 373192400);
        var1.sendMessage(var48);
        Component var50 = Text.legacy("&e/goldendream thunder &7— draw the great thunder across the sky past you", 373192400);
        var1.sendMessage(var50);
        Component var52 = Text.legacy(
            "&e/goldendream thunder float &7— everyone in the dream who is not a memory drifts up and is kicked awake, back in their real world", 373192400
        );
        var1.sendMessage(var52);
        Component var54 = Text.legacy("&e/goldendream memory add <player> [name] &7— make a player a memory (a name from the pool)", 373192400);
        var1.sendMessage(var54);
        Component var56 = Text.legacy("&e/goldendream memory remove <player> &7— no longer a memory", 373192400);
        var1.sendMessage(var56);
        Component var58 = Text.legacy("&e/goldendream memory list &7— the name pool, and how many NPC memories are up", 373192400);
        var1.sendMessage(var58);
        Component var60 = Text.legacy("&e/goldendream memory name <player> <newname> &7— a memory's name alone (skin stays)", 373192400);
        var1.sendMessage(var60);
        Component var62 = Text.legacy("&e/goldendream memory skin <player> <source|off> &7— a memory's skin alone (name stays)", 373192400);
        var1.sendMessage(var62);
        Component var64 = Text.legacy("&e/goldendream memory npc <count|clear> [skin] &7— spawn Citizens bot memories at you, or clear them all", 373192400);
        var1.sendMessage(var64);
        Component var66 = Text.legacy("&e/goldendream memory npc name <bot> <newname> &7— a bot's name alone", 373192400);
        var1.sendMessage(var66);
        Component var68 = Text.legacy("&e/goldendream memory npc skin <bot> <player|off> &7— a bot's skin alone (fetched by that player's name)", 373192400);
        var1.sendMessage(var68);
        Component var70 = Text.legacy("&e/goldendream nick name <player> <text|off> &7— a fake name", 373192400);
        var1.sendMessage(var70);
        Component var72 = Text.legacy("&e/goldendream nick skin <player> <source|off> &7— borrow an online player's skin", 373192400);
        var1.sendMessage(var72);
        Component var74 = Text.legacy("&e/goldendream nick reset <player> &7— real name and skin back", 373192400);
        var1.sendMessage(var74);
    }

    public List<String> onTabComplete(CommandSender var1, Command var2, String var3, String[] var4) {
        ArrayList var5 = new ArrayList();
        ArrayList var12 = var5;
        if (!var1.hasPermission("bliss.admin")) {
            return var5;
        } else if (var4.length == 1) {
            String[] var136 = new String[11];
            String var225 = "config";
            var136[0] = var225;
            String var226 = "progression";
            var136[1] = var226;
            String var227 = "menu";
            var136[2] = var227;
            String var228 = "import";
            var136[3] = var228;
            String var229 = "reset";
            var136[4] = var229;
            String var230 = "walk";
            var136[5] = var230;
            String var231 = "skip";
            var136[6] = var231;
            String var232 = "wake";
            var136[7] = var232;
            String var233 = "thunder";
            var136[8] = var233;
            String var234 = "memory";
            var136[9] = var234;
            String var235 = "nick";
            var136[10] = var235;

            for (String var239 : List.of(var136)) {
                String var199 = var4[0].toLowerCase();
                if (var239.startsWith(var199)) {
                    var12.add(var239);
                }
            }

            return var12;
        } else {
            String var14 = var4[0].toLowerCase();
            switch (var14) {
                case "memory":
                    NickCommand var109 = this.nick;
                    return var109.complete(var1, var4, 341247110);
                case "nick":
                    NickCommand var106 = this.nick;
                    int var237 = var4.length;
                    String[] var210 = Arrays.copyOfRange(var4, 1, var237);
                    return var106.complete(var1, var210, 341247110);
                case "progression":
                    if (var4.length == 2) {
                        String var72 = "0";
                        String var171 = "1";
                        String var204 = "2";
                        String var7 = "3";
                        String var8 = "4";
                        String var9 = "5";
                        String var10 = "reset";
                        String var11 = "status";

                        for (String var244 : List.of(var72, var171, var204, var7, var8, var9, var10, var11)) {
                            String var174 = var4[1].toLowerCase();
                            if (var244.startsWith(var174)) {
                                var12.add(var244);
                            }
                        }
                    }
                    break;
                case "skip":
                case "wake":
                    if (var4.length == 2) {
                        for (Player var245 : Bukkit.getOnlinePlayers()) {
                            String var101 = var245.getName().toLowerCase();
                            String var184 = var4[1].toLowerCase();
                            if (var101.startsWith(var184)) {
                                String var186 = var245.getName();
                                var12.add(var186);
                            }
                        }
                    }
                    break;
                case "walk":
                    if (var4.length == 2) {
                        String var58 = "start";
                        String var165 = "stop";
                        String var202 = "send";

                        for (String var243 : List.of(var58, var165, var202)) {
                            String var168 = var4[1].toLowerCase();
                            if (var243.startsWith(var168)) {
                                var12.add(var243);
                            }
                        }
                    } else if (var4.length == 3) {
                        for (Player var17 : Bukkit.getOnlinePlayers()) {
                            String var54 = var17.getName().toLowerCase();
                            String var162 = var4[2].toLowerCase();
                            if (var54.startsWith(var162)) {
                                String var164 = var17.getName();
                                var12.add(var164);
                            }
                        }
                    }
                    break;
                case "import":
                case "reset":
                    if (var4.length == 2) {
                        String var36 = "confirm";
                        String var156 = var4[1].toLowerCase();
                        if (var36.startsWith(var156)) {
                            var5.add("confirm");
                        }
                    }
                    break;
                case "thunder":
                    if (var4.length == 2) {
                        String var86 = "float";
                        String var179 = var4[1].toLowerCase();
                        if (var86.startsWith(var179)) {
                            var5.add("float");
                        }
                    }
            }

            return var12;
        }
    }
}
