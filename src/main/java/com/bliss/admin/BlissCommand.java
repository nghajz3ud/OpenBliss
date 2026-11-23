package com.bliss.admin;

import com.bliss.BlissPlugin;
import com.bliss.core.DragonGamesHook;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.Particles;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.core.PovOverlay;
import com.bliss.core.TagStore;
import com.bliss.entity.EntityMotifs;
import com.bliss.goldgem.GoldenDreamWorld;
import com.bliss.honored.JuggernautGemGuardListener;
import com.bliss.items.BlissCoreItems;
import com.bliss.pedestal.CrushedPedestalOrb;
import com.bliss.pedestal.PedestalReviveRitual;
import com.bliss.pedestal.PedestalState;
import com.bliss.trader.TraderListener;
import com.bliss.villager.Text;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitScheduler;

public final class BlissCommand implements CommandExecutor, TabCompleter {
    private final BlissPlugin plugin;
    private final BlissCoreItems items;
    private final PedestalState pedestal;
    private final PlayerDataStore players;
    private final TagStore tags;
    private final TraderListener trader;
    private static final String DRAGON_GAMES = "dragongames";
    private static final long[] PROG1_STAGE_DELAYS;
    private final Random progRng;

    public BlissCommand(BlissPlugin var1, BlissCoreItems var2, PedestalState var3, PlayerDataStore var4, TagStore var5, TraderListener var6, int var7) {
        Random var9 = new Random();
        this.progRng = var9;
        this.plugin = var1;
        this.items = var2;
        this.pedestal = var3;
        this.players = var4;
        this.tags = var5;
        this.trader = var6;
    }

    public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
        if (var4.length == 0) {
            this.usage(var1, 505980392);
            return true;
        } else {
            String var8 = var4[0].toLowerCase();
            if (var8.equals("helpbook")) {
                this.handleHelpBook(var1, 1266387948);
                return true;
            } else {
                int var187 = var4.length;
                String[] var9 = Arrays.copyOfRange(var4, 1, var187);
                DragonGamesHook var10 = this.plugin.dragonGames(1198797739);
                if (var8.equals("dragongames") && var10 != null && var10.isSelfCheck(var9, 189559697)) {
                    var10.handle(var1, var9, 822440176);
                    return true;
                } else if (!var1.hasPermission("bliss.admin")) {
                    Component var186 = Text.legacy("&cYou don't have permission. Try &f/bliss helpbook&c.", 373192400);
                    var1.sendMessage(var186);
                    return true;
                } else {
                    switch (var8) {
                        case "give":
                            this.handleGive(var1, var4, 833963119);
                            break;
                        case "pedestal":
                            this.handlePedestal(var1, var4, 103580527);
                            break;
                        case "orb":
                            this.handleOrb(var1, var4, 93530789);
                            break;
                        case "reroll":
                            this.handleReroll(var1, var4, 1503804011);
                            break;
                        case "setenergy":
                            this.handleSetEnergy(var1, var4, 1186565041);
                            break;
                        case "gemdisable":
                            this.handleGemDisable(var1, 1129002874);
                            break;
                        case "gemenable":
                            this.handleGemEnable(var1, 485159871);
                            break;
                        case "reset":
                            this.handleReset(var1, var4, 1185598036);
                            break;
                        case "gemgem":
                            this.handleGemGem(var1, var4, 134751538);
                            break;
                        case "cooldown":
                            this.handleCooldown(var1, var4, 1137796058);
                            break;
                        case "progression":
                            this.handleProgression(var1, var4, 440436720);
                            break;
                        case "config":
                            this.handleConfig(var1, var4, 1265929110);
                            break;
                        case "menu":
                            this.handleMenu(var1, var4, 1139164291);
                            break;
                        case "ghostclear":
                            this.handleGhostClear(var1, var4, 629800558);
                            break;
                        case "wisp":
                            this.handleEntity(var1, var4, 1015302321);
                            break;
                        case "dream":
                            this.handleDream(var1, var4, 1219149086);
                            break;
                        case "pocket":
                            this.handlePocket(var1, var4, 601327802);
                            break;
                        case "particles":
                            this.handleParticles(var1, var4, 1762041972);
                            break;
                        case "admin":
                            this.handleAdminHelpBook(var1, var4, 52186227);
                            break;
                        case "pov":
                            this.handlePov(var1, var4, 646924532);
                            break;
                        case "dragongames":
                            if (var10 != null) {
                                var10.handle(var1, var9, 822440176);
                            } else {
                                this.usage(var1, 505980392);
                            }
                            break;
                        default:
                            this.usage(var1, 505980392);
                    }

                    return true;
                }
            }
        }
    }

    private void handlePov(CommandSender var1, String[] var2, int var3) {
        if (var1 instanceof Player var9) {
            if (var2.length < 2) {
                String var75 = "/bliss pov <gem|off> [seconds]";
                CommandHelp.hint(var1, var75, 80456809);
            } else if (var2[1].equalsIgnoreCase("off")) {
                String var78;
                if (PovOverlay.hide(var9, 731260135)) {
                    var78 = "&7POV off.";
                } else {
                    var78 = "&7No POV on you.";
                }

                Component var74 = Text.legacy(var78, 373192400);
                var9.sendMessage(var74);
            } else {
                GemType var10 = null;

                for (GemType var14 : GemType.values()) {
                    GemType var69 = GemType.NONE;
                    if (var14 != var69) {
                        String var47 = var14.name();
                        String var71 = var2[1];
                        if (var47.equalsIgnoreCase(var71)) {
                            var10 = var14;
                        }
                    }
                }

                if (var10 == null) {
                    String var56 = "/bliss pov <strength|fire|wealth|speed|puff|astra|life|flux|off> [seconds]";
                    CommandHelp.hint(var1, var56, 80456809);
                } else {
                    int var86 = 10;
                    if (var2.length >= 3) {
                        try {
                            int var80 = Integer.parseInt(var2[2]);
                            int var59 = Math.min(600, var80);
                            var86 = Math.max(1, var59);
                        } catch (NumberFormatException var87) {
                            String var60 = "/bliss pov <gem|off> [seconds]";
                            CommandHelp.hint(var1, var60, 80456809);
                            return;
                        }
                    }

                    BlissPlugin var39 = this.plugin;
                    int var85 = var86 * 20;
                    if (!PovOverlay.show(var39, var9, var10, var85, 1643598112)) {
                        Component var67 = Text.legacy(
                            "&cAnother sky effect owns your clock right now (dream, memory, ritual, Control Room) â€” the POV stays out of its way.", 373192400
                        );
                        var9.sendMessage(var67);
                    } else {
                        String var63 = var10.coloredName();
                        Component var65 = Text.legacy("&7POV: " + var63 + " &7for &f" + var86 + "s &7(needs the BlissGoldRitual shaderpack).", 373192400);
                        var9.sendMessage(var65);
                    }
                }
            }
        } else {
            Component var77 = Text.legacy("&cPlayer only.", 373192400);
            var1.sendMessage(var77);
        }
    }

    private void handleAdminHelpBook(CommandSender var1, String[] var2, int var3) {
        if (var2.length < 2 || !var2[1].equalsIgnoreCase("helpbook")) {
            String var25 = "/bliss admin helpbook";
            CommandHelp.hint(var1, var25, 80456809);
        } else if (var1 instanceof Player var6) {
            AdminHelpBook.give(var6, 1906050018);
            Component var22 = Text.legacy("&aHere's the admin guide â€” setup, every event and every admin command.", 373192400);
            var6.sendMessage(var22);
        } else {
            Component var24 = Text.legacy("&cPlayer only.", 373192400);
            var1.sendMessage(var24);
        }
    }

    private void handleDream(CommandSender var1, String[] var2, int var3) {
        String var5 = "goldendream";
        StringBuilder var4 = new StringBuilder(var5);
        StringBuilder var6 = var4;
        int var7 = 1;

        while (true) {
            int var16 = var2.length;
            if (var7 >= var16) {
                String var18 = var6.toString();
                Bukkit.dispatchCommand(var1, var18);
                return;
            }

            StringBuilder var13 = var6.append(' ');
            String var21 = var2[var7];
            var13.append(var21);
            var7++;
        }
    }

    private void handleParticles(CommandSender var1, String[] var2, int var3) {
        GlobalState var7 = this.plugin.globalState(724248306);
        if (var2.length < 2) {
            Component var38 = Text.legacy(
                "&eParticles are &f" + var7.particles(1423953150).label() + "&e. Usage: &f/bliss particles <off|decreased|on|extra>", 373192400
            );
            var1.sendMessage(var38);
            Component var40 = Text.legacy("&7off &8- nothing drawn   &7decreased &8- about half", 373192400);
            var1.sendMessage(var40);
            Component var42 = Text.legacy("&7on &8- as designed   &7extra &8- twice as thick", 373192400);
            var1.sendMessage(var42);
        } else {
            Particles.Level var8 = Particles.parse(var2[1], 1207930812);
            if (var8 == null) {
                Component var28 = Text.legacy("&cNo such level: &f" + var2[1] + "&c. Pick &foff&c, &fdecreased&c, &fon&c or &fextra&c.", 373192400);
                var1.sendMessage(var28);
            } else {
                var7.setParticles(var8, 619914488);
                var7.save(343464104);
                Component var33 = Text.legacy("&aEvery gem, ritual and event now draws &f" + var8.label() + "&a.", 373192400);
                var1.sendMessage(var33);
            }
        }
    }

    private void handlePocket(CommandSender var1, String[] var2, int var3) {
        String var44;
        if (var2.length >= 2) {
            var44 = var2[1].toLowerCase();
        } else {
            var44 = "";
        }

        if (var2.length >= 3 && (var44.equals("tp") || var44.equals("kick"))) {
            Player var8 = Bukkit.getPlayerExact(var2[2]);
            if (var8 == null) {
                Component var54 = Text.legacy("&cPlayer not found: &f" + var2[2], 373192400);
                var1.sendMessage(var54);
            } else {
                GoldenDreamWorld var41;
                if (this.plugin.goldenDream(2108340810) == null) {
                    var41 = null;
                } else {
                    var41 = this.plugin.goldenDream(2108340810).dream(2117018474);
                }

                if (var41 == null) {
                    Component var75 = Text.legacy("&cThe Golden Dream is not loaded.", 373192400);
                    var1.sendMessage(var75);
                } else {
                    if (var44.equals("tp")) {
                        String var10 = var41.pocketTp(var8, 647555506);
                        String var77;
                        if (var10 == null) {
                            var77 = "&6" + var8.getName() + " &eis in the pocket.";
                        } else {
                            var77 = "&c" + var10;
                        }

                        Component var71 = Text.legacy(var77, 373192400);
                        var1.sendMessage(var71);
                    } else {
                        if (var8.isDead()) {
                            Component var59 = Text.legacy("&c" + var8.getName() + " is dead - wait for the respawn, then kick.", 373192400);
                            var1.sendMessage(var59);
                            return;
                        }

                        String var64;
                        if (var41.pocketKick(var8, 853854218)) {
                            var64 = "&6" + var8.getName() + " &eis out of the pocket.";
                        } else {
                            var64 = "&c" + var8.getName() + " is not in the pocket.";
                        }

                        Component var65 = Text.legacy(var64, 373192400);
                        var1.sendMessage(var65);
                    }
                }
            }
        } else {
            Component var49 = Text.legacy("&eUsage: /bliss pocket <tp|kick> <player>", 373192400);
            var1.sendMessage(var49);
        }
    }

    private void handleGive(CommandSender var1, String[] var2, int var3) {
        if (!(var1 instanceof Player var9)) {
            Component var177 = Text.legacy("&cPlayer only.", 373192400);
            var1.sendMessage(var177);
        } else if (var2.length < 2) {
            this.usage(var1, 505980392);
        } else {
            String var10 = var2[1].toLowerCase();
            ItemStack var188;
            String var189;
            switch (var10) {
                case "trader":
                    var188 = this.items.trader(662298763);
                    var189 = "Trader";
                    break;
                case "upgrader":
                    var188 = this.items.upgrader(2125662274);
                    var189 = "Upgrader";
                    break;
                case "energy":
                    var188 = this.items.bottledEnergy(1532783840);
                    var189 = "Bottled Energy";
                    break;
                case "repair":
                    var188 = this.items.repairKit(1231295697);
                    var189 = "Repair Kit";
                    break;
                case "gemselector":
                    var188 = this.items.gemSelector(1614312040);
                    var189 = "Gem Selector";
                    break;
                case "restoration":
                    var188 = this.items.restoration(1610991893);
                    var189 = "Restoration";
                    break;
                case "restorationcore":
                    var188 = this.items.restorationCore(913365449);
                    var189 = "Restoration Core";
                    break;
                case "fragment":
                    var188 = this.items.fragment(452582255);
                    var189 = "Fragment";
                    break;
                case "obsidian":
                    var188 = this.items.enchantedObsidian(1193659648);
                    var189 = "Enchanted Obsidian";
                    break;
                case "prismatic":
                    var188 = this.items.prismaticEdge(840003792);
                    var189 = "Prismatic Blade";
                    break;
                case "gem":
                    PlayerDataStore var73 = this.players;
                    UUID var145 = var9.getUniqueId();
                    PlayerData var12 = var73.get(var145, 1264010596);
                    GemType var76 = var12.gem(1413186337);
                    GemType var146 = GemType.NONE;
                    if (var76 == var146) {
                        Component var148 = Text.legacy("&cNo gem assigned.", 373192400);
                        var1.sendMessage(var148);
                        return;
                    }

                    boolean var13 = var12.tier2(629984660);
                    byte var14 = 2;
                    if (var2.length >= 3 && (var2[2].equalsIgnoreCase("t1") || var2[2].equalsIgnoreCase("t2"))) {
                        var13 = var2[2].equalsIgnoreCase("t2");
                        var14 = 3;
                    }

                    if (JuggernautGemGuardListener.isJuggernaut(var9, 859003337)) {
                        Component var164 = Text.legacy("&cThat player is a Juggernaut â€” Juggernauts are gem-exempt.", 373192400);
                        var1.sendMessage(var164);
                        return;
                    }

                    int var15 = parseAmount(var2, var14, 1174105028);
                    BlissCoreItems var158 = this.items;
                    GemType var180 = var12.gem(1413186337);
                    int var187 = var12.energy(1160853318);
                    ItemStack var159 = var158.gemItem(var180, var13, var187, 256965471);
                    giveAmount(var9, var159, var15, 1191603470);
                    String var184 = var12.gem(1413186337).coloredName();
                    String var186;
                    if (var13) {
                        var186 = " &ftÉªá´‡Ê€ ÉªÉª";
                    } else {
                        var186 = " &ftÉªá´‡Ê€ Éª";
                    }

                    Component var162 = Text.legacy("&aGiven &f" + var15 + "x " + var184 + var186 + " &agem item.", 373192400);
                    var1.sendMessage(var162);
                    return;
                default:
                    this.usage(var1, 505980392);
                    return;
            }

            int var190 = parseAmount(var2, 2, 1174105028);
            giveAmount(var9, var188, var190, 1191603470);
            Component var143 = Text.legacy("&aGiven &f" + var190 + "x &a" + var189 + ".", 373192400);
            var1.sendMessage(var143);
        }
    }

    private void handleEntity(CommandSender var1, String[] var2, int var3) {
        EntityMotifs var9 = this.plugin.entityMotifs(949215902);
        if (var9 == null) {
            Component var157 = Text.legacy("&cEntity motifs aren't loaded.", 373192400);
            var1.sendMessage(var157);
        } else if (var2.length < 2) {
            this.usage(var1, 505980392);
        } else {
            String var10 = var2[1].toLowerCase();
            switch (var10) {
                case "set":
                    if (var2.length < 4) {
                        String var150 = "|";
                        List var165 = EntityMotifs.motifs(1082160894);
                        Component var153 = Text.legacy(
                            "&cUsage: &f/bliss wisp set <player> <" + String.join(var150, var165) + "|none> &7[who can see it: names, or &fself&7]", 373192400
                        );
                        var1.sendMessage(var153);
                        return;
                    }

                    Player var172 = Bukkit.getPlayerExact(var2[2]);
                    if (var172 == null) {
                        Component var109 = Text.legacy("&cNo player online called &f" + var2[2] + "&c.", 373192400);
                        var1.sendMessage(var109);
                        return;
                    }

                    String var13 = var2[3];
                    if (!var13.equalsIgnoreCase("none") && !var13.equalsIgnoreCase("clear")) {
                        if (!EntityMotifs.known(var13, 1658721522)) {
                            String var159 = "&c, &f";
                            List var7 = EntityMotifs.motifs(1082160894);
                            String var160 = String.join(var159, var7);
                            Component var115 = Text.legacy("&cNo motif called &f" + var13 + "&c. Try &f" + var160, 373192400);
                            var1.sendMessage(var115);
                            return;
                        }

                        ArrayList var57 = new ArrayList();
                        ArrayList var14 = var57;
                        ArrayList var58 = new ArrayList();
                        ArrayList var15 = var58;
                        int var16 = 4;

                        while (true) {
                            int var117 = var2.length;
                            if (var16 >= var117) {
                                break;
                            }

                            if (var2[var16].equalsIgnoreCase("all") || var2[var16].equalsIgnoreCase("everyone")) {
                                var14.clear();
                                var15.clear();
                                break;
                            }

                            Player var80;
                            if (var2[var16].equalsIgnoreCase("self")) {
                                var80 = var172;
                            } else {
                                var80 = Bukkit.getPlayerExact(var2[var16]);
                            }

                            if (var80 == null) {
                                Component var127 = Text.legacy("&cNo player online called &f" + var2[var16] + "&c.", 373192400);
                                var1.sendMessage(var127);
                                return;
                            }

                            UUID var129 = var80.getUniqueId();
                            if (!var14.contains(var129)) {
                                UUID var131 = var80.getUniqueId();
                                var14.add(var131);
                                String var133 = var80.getName();
                                var15.add(var133);
                            }

                            var16++;
                        }

                        var9.set(var172, var13, var14, 1623379796);
                        String var137 = var172.getName();
                        String var164 = var13.toLowerCase();
                        String var171;
                        if (var14.isEmpty()) {
                            var171 = ", seen by everyone.";
                        } else {
                            String var169 = "&a, &f";
                            var171 = ", seen only by &f" + String.join(var169, var15) + "&a.";
                        }

                        Component var139 = Text.legacy("&a" + var137 + " now stands as <##7FD8FF>" + var164 + "&a" + var171, 373192400);
                        var1.sendMessage(var139);
                        break;
                    }

                    UUID var141 = var172.getUniqueId();
                    if (var9.clear(var141, 649233089)) {
                        Component var145 = Text.legacy("&a" + var172.getName() + " is themselves again.", 373192400);
                        var1.sendMessage(var145);
                    } else {
                        Component var149 = Text.legacy("&7" + var172.getName() + " wasn't behind a motif.", 373192400);
                        var1.sendMessage(var149);
                    }

                    return;
                case "list":
                    Map<UUID, String> var12 = var9.all(401991558);
                    if (var12.isEmpty()) {
                        Component var99 = Text.legacy("&7Nobody is behind a motif.", 373192400);
                        var1.sendMessage(var99);
                        return;
                    }

                    Component var101 = Text.legacy("&7Standing as a motif:", 373192400);
                    var1.sendMessage(var101);
                    BiConsumer<UUID, String> var103 = (var2x, var3x) -> {
                        OfflinePlayer var8 = Bukkit.getOfflinePlayer(var2x);
                        String var41;
                        if (var8.getName() == null) {
                            var41 = var2x.toString();
                        } else {
                            var41 = var8.getName();
                        }

                        List<UUID> var10x = var9.viewersOf(var2x, 35810571);
                        String var51;
                        if (var10x.isEmpty()) {
                            var51 = " &8(everyone)";
                        } else {
                            ArrayList var25 = new ArrayList();
                            ArrayList var11 = var25;

                            for (UUID var13x : var10x) {
                                OfflinePlayer var14x = Bukkit.getOfflinePlayer(var13x);
                                String var50;
                                if (var14x.getName() == null) {
                                    var50 = var13x.toString();
                                } else {
                                    var50 = var14x.getName();
                                }

                                var11.add(var50);
                            }

                            String var37 = ", ";
                            var51 = " &8(only " + String.join(var37, var11) + ")";
                        }

                        Component var44 = Text.legacy("&8- &f" + var41 + " &8: <##7FD8FF>" + var3x + var51, 373192400);
                        var1.sendMessage(var44);
                    };
                    var12.forEach(var103);
                    break;
                default:
                    this.usage(var1, 505980392);
            }
        }
    }

    private static int parseAmount(String[] var0, int var1, int var2) {
        int var8 = var0.length;
        if (var8 > var1) {
            try {
                int var16 = Integer.parseInt(var0[var1]);
                int var14 = Math.min(2304, var16);
                return Math.max(1, var14);
            } catch (NumberFormatException var17) {
            }
        }

        return 1;
    }

    private static void giveAmount(Player var0, ItemStack var1, int var2, int var3) {
        int var38 = var1.getMaxStackSize();
        int var8 = Math.max(1, var38);
        int var9 = Math.max(1, var2);

        while (var9 > 0) {
            int var10 = Math.min(var9, var8);
            ItemStack var11 = var1.clone();
            var11.setAmount(var10);
            PlayerInventory var24 = var0.getInventory();
            ItemStack[] var43 = new ItemStack[]{var11};

            for (ItemStack var13 : var24.addItem(var43).values()) {
                World var34 = var0.getWorld();
                Location var45 = var0.getLocation();
                var34.dropItemNaturally(var45, var13);
            }

            var9 -= var10;
        }
    }

    private void handlePedestal(CommandSender var1, String[] var2, int var3) {
        if (var2.length < 2) {
            this.usage(var1, 505980392);
        } else {
            String var10 = var2[1].toLowerCase();
            switch (var10) {
                case "set":
                    Consumer<Player> var109 = var2x -> {
                        PedestalState var5 = this.pedestal;
                        Location var9 = var2x.getLocation();
                        var5.configureFromCenter(var9, 990554762);
                        this.pedestal.save(973788971);
                        Component var11 = Text.legacy(
                            "&aPedestal locations set (center is one block below you). Run /bliss pedestal activate to build it.", 373192400
                        );
                        var1.sendMessage(var11);
                    };
                    this.requirePlayer(var1, var109, 1112831726);
                    break;
                case "activate":
                    if (this.pedestal.mainLoc(15453813) == null) {
                        Component var88 = Text.legacy("&cPedestal locations not set. Run /bliss pedestal set first.", 373192400);
                        var1.sendMessage(var88);
                        return;
                    }

                    this.pedestal.setActive(true, 1028669057);
                    this.pedestal.setBeacon(false, 116258295);
                    this.pedestal.resetRitual(1772872156);
                    this.pedestal.buildStructure(1721962847);
                    PedestalReviveRitual.restoreWeather(781471445);
                    this.pedestal.save(973788971);
                    Server var42 = Bukkit.getServer();
                    Component var92 = Text.legacy("&6 The Pedestal Is Now &b&lOpen", 373192400);
                    var42.broadcast(var92);
                    break;
                case "deactivate":
                    this.pedestal.setActive(false, 1028669057);
                    this.pedestal.resetRitual(1772872156);
                    this.pedestal.unbuildStructure(280713799);
                    PedestalReviveRitual.restoreWeather(781471445);
                    this.pedestal.save(973788971);
                    Server var53 = Bukkit.getServer();
                    Component var96 = Text.legacy("&6 The Pedestal Has Closed &c&l", 373192400);
                    var53.broadcast(var96);
                    break;
                case "status":
                    boolean var69 = this.pedestal.active(80985509);
                    boolean var101 = this.pedestal.beacon(313684215);
                    String var112 = String.valueOf(this.pedestal.ritual(1751702735));
                    int var115 = this.pedestal.count(629418704);
                    int var117 = this.pedestal.durability(919222756);
                    Component var71 = Text.legacy(
                        "&7Active: &f" + var69 + " &7Beacon: &f" + var101 + " &7Ritual: &f" + var112 + " &7Count: &f" + var115 + "/5 &7Durability: &f" + var117,
                        373192400
                    );
                    var1.sendMessage(var71);
                    String var85;
                    if (this.pedestal.mainLoc(15453813) == null) {
                        var85 = "unset";
                    } else {
                        Location var104 = this.pedestal.mainLoc(15453813);
                        var85 = this.describe(var104, 1712453325);
                    }

                    Component var77 = Text.legacy("&7Main: &f" + var85, 373192400);
                    var1.sendMessage(var77);
                    String var84;
                    if (this.pedestal.subLoc(135424274) == null) {
                        var84 = "unset";
                    } else {
                        Location var107 = this.pedestal.subLoc(135424274);
                        var84 = this.describe(var107, 1712453325);
                    }

                    Component var83 = Text.legacy("&7Sub:  &f" + var84, 373192400);
                    var1.sendMessage(var83);
                    break;
                default:
                    this.usage(var1, 505980392);
            }
        }
    }

    private void handleOrb(CommandSender var1, String[] var2, int var3) {
        CrushedPedestalOrb var9 = this.plugin.crushedPedestalOrb(952223969);
        if (var9 == null) {
            Component var55 = Text.legacy("&cThe orb system isn't loaded.", 373192400);
            var1.sendMessage(var55);
        } else if (var2.length < 2) {
            this.usage(var1, 505980392);
        } else {
            String var10 = var2[1].toLowerCase();
            switch (var10) {
                case "set":
                    Consumer<Player> var77 = var3x -> {
                        Location var14 = var3x.getLocation().clone();
                        double var8 = CrushedPedestalOrb.restingLift(383911191);
                        Location var12x = var14.add(0.0, var8, 0.0);
                        var9.setFreeOrb(var12x, 300946825);
                        Component var22 = Text.legacy(
                            "&aOrb placed at &f" + this.describe(var12x, 1712453325) + "&a. Run &f/bliss orb remove&a to take it away.", 373192400
                        );
                        var1.sendMessage(var22);
                    };
                    this.requirePlayer(var1, var77, 1112831726);
                    break;
                case "remove":
                    if (var9.clearFreeOrb(914066823)) {
                        Component var63 = Text.legacy("&aThe placed orb has been removed.", 373192400);
                        var1.sendMessage(var63);
                    } else {
                        Component var61 = Text.legacy("&7There is no placed orb. (The pedestal's own orb, if any, is the Codex's doing.)", 373192400);
                        var1.sendMessage(var61);
                    }
                    break;
                case "status":
                    Location var12 = var9.freeOrb(1133825782);
                    String var74;
                    if (var12 == null) {
                        var74 = "none";
                    } else {
                        var74 = this.describe(var12, 1712453325);
                    }

                    Component var69 = Text.legacy("&7Placed orb: &f" + var74, 373192400);
                    var1.sendMessage(var69);
                    boolean var48;
                    if (this.plugin.kitRules(1205957746) != null && this.plugin.kitRules(1205957746).pedestalClosed(12031987)) {
                        var48 = true;
                    } else {
                        var48 = false;
                    }

                    String var73;
                    if (var48) {
                        var73 = "standing (pedestal decreed unused)";
                    } else {
                        var73 = "none (rituals kept)";
                    }

                    Component var72 = Text.legacy("&7Pedestal orb: &f" + var73, 373192400);
                    var1.sendMessage(var72);
                    break;
                default:
                    this.usage(var1, 505980392);
            }
        }
    }

    private void handleReroll(CommandSender var1, String[] var2, int var3) {
        if (var2.length < 2) {
            String var48 = "/bliss reroll <player>";
            CommandHelp.hint(var1, var48, 80456809);
        } else {
            Player var7 = Bukkit.getPlayerExact(var2[1]);
            if (var7 == null) {
                Component var31 = Text.legacy("&cPlayer must be online: &f" + var2[1], 373192400);
                var1.sendMessage(var31);
            } else {
                PlayerDataStore var16 = this.players;
                UUID var33 = var7.getUniqueId();
                GemType var19 = var16.get(var33, 1264010596).gem(1413186337);
                GemType var34 = GemType.NONE;
                if (var19 == var34) {
                    Component var38 = Text.legacy("&c" + var7.getName() + " has no gem to reroll.", 373192400);
                    var1.sendMessage(var38);
                } else {
                    TraderListener var22 = this.trader;
                    if (!var22.adminReroll(var7, 912092656)) {
                        Component var47 = Text.legacy("&c" + var7.getName() + " is already mid-reroll.", 373192400);
                        var1.sendMessage(var47);
                    } else {
                        Component var43 = Text.legacy("&aRerolling " + var7.getName() + "'s gemâ€¦", 373192400);
                        var1.sendMessage(var43);
                    }
                }
            }
        }
    }

    private void handleSetEnergy(CommandSender var1, String[] var2, int var3) {
        if (var2.length < 3) {
            String var34 = "/bliss setenergy <player> <n>";
            CommandHelp.hint(var1, var34, 80456809);
        } else {
            OfflinePlayer var7 = Bukkit.getOfflinePlayer(var2[1]);
            if (var7.getUniqueId() == null) {
                Component var49 = Text.legacy("&cUnknown player.", 373192400);
                var1.sendMessage(var49);
            } else {
                int var8;
                try {
                    var8 = Integer.parseInt(var2[2]);
                } catch (NumberFormatException var53) {
                    Component var47 = Text.legacy("&cNumber required.", 373192400);
                    var1.sendMessage(var47);
                    return;
                }

                PlayerDataStore var21 = this.players;
                UUID var38 = var7.getUniqueId();
                PlayerData var9 = var21.get(var38, 1264010596);
                var9.setEnergy(var8, 319922676);
                if (var7.isOnline() && var7.getPlayer() != null) {
                    BlissCoreItems var29 = this.items;
                    Player var41 = var7.getPlayer();
                    var29.refreshHeldGemItem(var41, var9, 835235475);
                }

                String var43 = var2[1];
                int var52 = var9.energy(1160853318);
                Component var45 = Text.legacy("&aSet " + var43 + "'s energy to " + var52, 373192400);
                var1.sendMessage(var45);
            }
        }
    }

    private void handleGemDisable(CommandSender var1, int var2) {
        for (Player var7 : Bukkit.getOnlinePlayers()) {
            TagStore var15 = this.tags;
            UUID var17 = var7.getUniqueId();
            var15.addTagSecondsUncapped(var17, 9999, 176936897);
        }

        Component var19 = Text.legacy("&aGem abilities disabled server-wide.", 373192400);
        var1.sendMessage(var19);
    }

    private void handleGemEnable(CommandSender var1, int var2) {
        this.tags.clearAll(2066568345);
        Component var7 = Text.legacy("&aGem abilities re-enabled.", 373192400);
        var1.sendMessage(var7);
    }

    private void handleReset(CommandSender var1, String[] var2, int var3) {
        if (var2.length < 2) {
            String var30 = "/bliss reset <player>";
            CommandHelp.hint(var1, var30, 80456809);
        } else {
            OfflinePlayer var7 = Bukkit.getOfflinePlayer(var2[1]);
            if (var7.getUniqueId() == null) {
                Component var44 = Text.legacy("&cUnknown player.", 373192400);
                var1.sendMessage(var44);
            } else {
                PlayerDataStore var17 = this.players;
                UUID var33 = var7.getUniqueId();
                PlayerData var8 = var17.get(var33, 1264010596);
                var8.setEnergy(6, 319922676);
                var8.setTier2(false, 1757992594);
                GemType var36 = GemType.STRENGTH;
                var8.setGem(var36, 2137390487);
                if (var7.isOnline() && var7.getPlayer() != null) {
                    BlissCoreItems var27 = this.items;
                    Player var38 = var7.getPlayer();
                    var27.refreshGemItem(var38, var8, 1088402172);
                }

                Component var42 = Text.legacy("&6ðŸ”® &aReset &b" + var2[1] + "&a's gem", 373192400);
                var1.sendMessage(var42);
            }
        }
    }

    private void handleGemGem(CommandSender var1, String[] var2, int var3) {
        if (var2.length < 2) {
            String var55 = "/bliss gemgem <player>";
            CommandHelp.hint(var1, var55, 80456809);
        } else {
            OfflinePlayer var10 = Bukkit.getOfflinePlayer(var2[1]);
            if (var10.getUniqueId() == null) {
                Component var73 = Text.legacy("&cUnknown player.", 373192400);
                var1.sendMessage(var73);
            } else {
                PlayerDataStore var24 = this.players;
                UUID var58 = var10.getUniqueId();
                PlayerData var11 = var24.get(var58, 1264010596);
                if (var10.isOnline() && var10.getPlayer() != null) {
                    Player var12 = var10.getPlayer();
                    int var13 = -1;
                    ItemStack[] var14 = var12.getInventory().getContents();
                    int var15 = 0;

                    while (true) {
                        int var60 = var14.length;
                        if (var15 >= var60) {
                            GemType var48 = var11.gem(1413186337);
                            GemType var64 = GemType.NONE;
                            if (var48 != var64 && !JuggernautGemGuardListener.isJuggernaut(var12, 859003337)) {
                                BlissCoreItems var75 = this.items;
                                GemType var78 = var11.gem(1413186337);
                                boolean var79 = var11.tier2(629984660);
                                int var80 = var11.energy(1160853318);
                                ItemStack var76 = var75.gemItem(var78, var79, var80, 256965471);
                                BlissCoreItems.giveGemOrHold(var12, var13, var76, 2133739666);
                            }

                            Component var69 = Text.legacy("&aReissued " + var2[1] + "'s gem item.", 373192400);
                            var1.sendMessage(var69);
                            return;
                        }

                        if (var14[var15] != null && BlissCoreItems.gemIdOf(var14[var15], 382441079) > 0) {
                            if (var13 < 0) {
                                var13 = var15;
                            }

                            PlayerInventory var46 = var12.getInventory();
                            var46.setItem(var15, null);
                        }

                        var15++;
                    }
                } else {
                    Component var71 = Text.legacy("&cPlayer not online.", 373192400);
                    var1.sendMessage(var71);
                }
            }
        }
    }

    private void handleCooldown(CommandSender var1, String[] var2, int var3) {
        if (var2.length < 2) {
            String var30 = "/bliss cooldown <player>";
            CommandHelp.hint(var1, var30, 80456809);
        } else {
            OfflinePlayer var7 = Bukkit.getOfflinePlayer(var2[1]);
            if (var7.getUniqueId() == null) {
                Component var33 = Text.legacy("&cUnknown player.", 373192400);
                var1.sendMessage(var33);
            } else {
                UUID var8 = var7.getUniqueId();
                BlissPlugin var20 = this.plugin;
                var20.clearAllGemCooldowns(var8, 1294563415);
                TagStore var22 = this.tags;
                var22.clear(var8, 798728773);
                if (var7.isOnline() && var7.getPlayer() != null) {
                    var7.getPlayer().setWalkSpeed(0.2F);
                }

                Component var40 = Text.legacy("&aCleared all cooldowns for " + var2[1] + ".", 373192400);
                var1.sendMessage(var40);
            }
        }
    }

    private void handleProgression(CommandSender var1, String[] var2, int var3) {
        if (var2.length < 2) {
            String var178 = "/bliss progression <0|1|2|3|4|5|reset>";
            CommandHelp.hint(var1, var178, 80456809);
        } else {
            GlobalState var9 = this.plugin.globalState(724248306);
            if (var9 == null) {
                Component var177 = Text.legacy("&cGlobalState unavailable.", 373192400);
                var1.sendMessage(var177);
            } else {
                String var10 = var2[1].toLowerCase();
                switch (var10) {
                    case "0":
                        var9.setProg1Started(false, 1672226358);
                        var9.setProg1(true, 1805549590);
                        var9.setProg2(false, 1105469454);
                        var9.setProg3(false, 1348581053);
                        var9.setVillagersAuto(false, 2036260727);
                        var9.setEnderpearls(false, 567146737);
                        var9.setProgression(0, 1556136516);
                        var9.setWitherDropsDone(false, 958591876);

                        for (Player var13 : Bukkit.getOnlinePlayers()) {
                            PlayerDataStore var83 = this.players;
                            UUID var156 = var13.getUniqueId();
                            var83.get(var156, 1264010596).setJoined(false, 2028976592);
                        }

                        Component var159 = Text.legacy("&aProgression reset to 0.", 373192400);
                        var1.sendMessage(var159);
                        break;
                    case "1":
                        var9.setProg1Started(true, 1672226358);
                        var9.setProg1(true, 1805549590);
                        var9.setProg2(false, 1105469454);
                        var9.setProg3(false, 1348581053);
                        var9.setVillagersAuto(false, 2036260727);
                        var9.setEnderpearls(false, 567146737);
                        var9.setProgression(1, 1556136516);
                        String var128 = "<##cafca4>&lProgression 1";
                        String var179 = "<##f9f4d3>The Creation of Everything";
                        this.broadcastTitle(var128, var179, 211511920);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "worldborder set 2000");
                        BukkitScheduler var41 = Bukkit.getScheduler();
                        BlissPlugin var131 = this.plugin;
                        Runnable var181 = this::runProg1Animation;
                        var41.runTaskLater(var131, var181, 70L);
                        break;
                    case "2":
                        var9.setProg1(false, 1805549590);
                        var9.setProg2(true, 1105469454);
                        var9.setProg3(false, 1348581053);
                        var9.setProgression(2, 1556136516);
                        String var137 = "&c&lProgression 2";
                        String var183 = "<##FC8888>Into the Fire";
                        this.broadcastTitle(var137, var183, 211511920);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "worldborder set 5000");
                        break;
                    case "3":
                        var9.setProg1(false, 1805549590);
                        var9.setProg2(false, 1105469454);
                        var9.setProg3(true, 1348581053);
                        var9.setProgression(3, 1556136516);
                        String var144 = "&5&lProgression 3";
                        String var184 = "<##EB2ED8>The Next Journey";
                        this.broadcastTitle(var144, var184, 211511920);
                        var9.setUpgraderRecipe(true, 477819128);
                        var9.setTraderRecipe(true, 196837014);
                        this.plugin.recipes(942436690).registerUpgrader(406940308);
                        this.plugin.recipes(942436690).registerTrader(475248342);
                        break;
                    case "4":
                        applyProgression4(this.plugin, 679055365);
                        var9.setProgression(4, 1556136516);
                        break;
                    case "5":
                        this.applyProgression5(var1, var9, 518784789);
                        break;
                    case "reset":
                        var9.setProg1Started(true, 1672226358);
                        var9.setProg1(false, 1805549590);
                        var9.setProg2(false, 1105469454);
                        var9.setProg3(false, 1348581053);
                        var9.setProg4(false, 946676425);
                        var9.setVillagersLocked(false, 492545785);
                        var9.setVillagers(true, 393264146);
                        var9.setProgression(0, 1556136516);
                        Component var169 = Text.legacy("&aProgression reset.", 373192400);
                        var1.sendMessage(var169);
                        break;
                    default:
                        Component var120 = Text.legacy("&cUnknown phase: " + var2[1], 373192400);
                        var1.sendMessage(var120);
                }

                var9.save(343464104);
            }
        }
    }

    private void applyProgression5(CommandSender var1, GlobalState var2, int var3) {
        applyProgression5(this.plugin, 1857285266);
        Component var12 = Text.legacy("&6&lProgression 5 &aapplied:", 373192400);
        var1.sendMessage(var12);
        Component var14 = Text.legacy("  &aâœ” &7World border â†’ &f5000 &7(5s shrink)", 373192400);
        var1.sendMessage(var14);
        Component var16 = Text.legacy("  &aâœ” &7Netherite standard kit &7(smithing enabled)", 373192400);
        var1.sendMessage(var16);
        Component var18 = Text.legacy("  &aâœ” &7Ender-pearl running &cbanned", 373192400);
        var1.sendMessage(var18);
        Component var20 = Text.legacy("  &aâœ” &7Pedestal / rituals &cdisabled &7(Bliss Point)", 373192400);
        var1.sendMessage(var20);
    }

    public static void applyProgression5(BlissPlugin var0, int var1) {
        applyProgression5(var0, false, 1214874032);
    }

    public static void applyProgression5(BlissPlugin var0, boolean var1, int var2) {
        GlobalState var9 = var0.globalState(724248306);
        if (var9 != null) {
            var9.setProg1(false, 1805549590);
            var9.setProg2(false, 1105469454);
            var9.setProg3(false, 1348581053);
            var9.setVillagersAuto(true, 2036260727);
            if (!var1) {
                var9.setEnderpearls(false, 567146737);
            }

            var9.setNetheriteAllowed(true, 891609125);
            var9.setProgression(5, 1556136516);
            var9.save(343464104);
            Component var10 = Text.legacy("<##FFD773>&lProgression 5", 373192400);
            Component var11 = Text.legacy("<##E3B94F>The Honored One's Wishlist", 373192400);
            Duration var33 = Duration.ofSeconds(1L);
            Duration var69 = Duration.ofSeconds(3L);
            Duration var78 = Duration.ofSeconds(1L);
            Times var12 = Times.times(var33, var69, var78);
            Title var13 = Title.title(var10, var11, var12);

            for (Player var15 : Bukkit.getOnlinePlayers()) {
                var15.showTitle(var13);
                Location var73 = var15.getLocation();
                Sound var80 = Sound.UI_TOAST_CHALLENGE_COMPLETE;
                var15.playSound(var73, var80, 1.0F, 1.0F);
            }

            if (!var1) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "worldborder set 5000 5");
            }

            PedestalState var81 = var0.pedestalState(590646643);
            if (!var1 && var81 != null && var81.active(80985509)) {
                var81.setActive(false, 1028669057);
                var81.resetRitual(1772872156);
                var81.unbuildStructure(280713799);
                PedestalReviveRitual.restoreWeather(781471445);
                var81.save(973788971);
                Server var59 = Bukkit.getServer();
                Component var77 = Text.legacy("&6 The Pedestal has &c&lshattered &6â€” the rituals are no more.", 373192400);
                var59.broadcast(var77);
            }
        }
    }

    private void handleConfig(CommandSender var1, String[] var2, int var3) {
        if (var1 instanceof Player var6) {
            if (this.plugin.globalState(724248306) == null) {
                Component var22 = Text.legacy("&cGlobalState unavailable.", 373192400);
                var1.sendMessage(var22);
            } else if (this.plugin.configMenu(835099904) == null) {
                Component var24 = Text.legacy("&cConfig menu unavailable.", 373192400);
                var1.sendMessage(var24);
            } else {
                ConfigMenuListener var20 = this.plugin.configMenu(835099904);
                var20.open(var6, 1452201716);
            }
        } else {
            Component var27 = Text.legacy("&cRun /bliss config in-game to open the config menu.", 373192400);
            var1.sendMessage(var27);
        }
    }

    private void runProg1Animation() {
        for (Player var5 : Bukkit.getOnlinePlayers()) {
            PlayerDataStore var14 = this.players;
            UUID var29 = var5.getUniqueId();
            PlayerData var6 = var14.get(var29, 1264010596);
            if (!var6.joined(225029398)) {
                var6.setTier2(false, 1757992594);
                var6.setEnergy(6, 319922676);
                var6.setJoined(true, 2028976592);
                BlissPlugin var22 = this.plugin;
                UUID var34 = var5.getUniqueId();
                var22.clearAllGemCooldowns(var34, 1294563415);
                TagStore var24 = this.tags;
                UUID var36 = var5.getUniqueId();
                var24.clear(var36, 798728773);
                var5.setWalkSpeed(0.2F);
                this.clearGemsInInventory(var5, 1436411873);
                this.rollGemForPlayer(var5, false, 673570569);
            }
        }

        this.scheduleProg1Stage(0, 0L, 6540620);
    }

    private void scheduleProg1Stage(int var1, long var2, int var4) {
        int var24 = PROG1_STAGE_DELAYS.length;
        if (var1 < var24) {
            long[] var17 = PROG1_STAGE_DELAYS;
            long var14 = var17[var1];
            int var28 = PROG1_STAGE_DELAYS.length - 1;
            boolean var21;
            if (var1 == var28) {
                var21 = true;
            } else {
                var21 = false;
            }

            boolean var16 = var21;
            BukkitScheduler var22 = Bukkit.getScheduler();
            BlissPlugin var30 = this.plugin;
            Runnable var32 = () -> {
                for (Player var14x : Bukkit.getOnlinePlayers()) {
                    if (!JuggernautGemGuardListener.isJuggernaut(var14x, 859003337)) {
                        this.clearGemsInInventory(var14x, 1436411873);
                        this.rollGemForPlayer(var14x, var16, 673570569);
                        Location var31 = var14x.getLocation();
                        Sound var37 = Sound.BLOCK_AMETHYST_BLOCK_BREAK;
                        var14x.playSound(var31, var37, 1.0F, 1.0F);
                    }
                }

                int var27 = var1 + 1;
                long var34 = var2 + var14;
                this.scheduleProg1Stage(var27, var34, 6540620);
            };
            var22.runTaskLater(var30, var32, var14);
        }
    }

    private void rollGemForPlayer(Player var1, boolean var2, int var3) {
        PlayerDataStore var9 = this.players;
        UUID var21 = var1.getUniqueId();
        PlayerData var7 = var9.get(var21, 1264010596);
        GemType var12 = var7.gem(1413186337);
        Random var23 = this.progRng;
        GemType var8 = GemType.randomDifferent(var12, var23);
        var7.setGem(var8, 2137390487);
        if (!var2) {
            Component var28 = Text.legacy("ðŸ”® &bYou have traded your current gem to " + tradeLabel(var8, 1906508513), 373192400);
            var1.sendMessage(var28);
        }

        if (this.items != null) {
            BlissCoreItems var20 = this.items;
            var20.refreshGemItem(var1, var7, 1088402172);
        }
    }

    private void clearGemsInInventory(Player var1, int var2) {
        ItemStack[] var6 = var1.getInventory().getContents();
        int var7 = 0;

        while (true) {
            int var20 = var6.length;
            if (var7 >= var20) {
                return;
            }

            ItemStack var8 = var6[var7];
            if (var8 != null && BlissCoreItems.gemIdOf(var8, 382441079) > 0) {
                PlayerInventory var19 = var1.getInventory();
                var19.setItem(var7, null);
            }

            var7++;
        }
    }

    private static String tradeLabel(GemType var0, int var1) {
        return switch (var0) {
            case STRENGTH-> "&4&lêœ±á´›Ê€á´‡É´É¢á´›Êœ";
            case FIRE-> "Â§6Â§lêœ°ÉªÊ€á´‡";
            case WEALTH-> "Â§2Â§lá´¡á´‡á´€ÊŸá´›Êœ";
            case SPEED-> "&e&lsá´˜á´‡á´‡á´…";
            case PUFF-> "&fÂ§lá´˜á´œêœ°êœ°";
            case ASTRA-> "&5&lá´€sá´›Ê€á´€";
            case LIFE-> "Â§dÂ§lÊŸÉªêœ°á´‡";
            case FLUX-> "Â§9Â§lêœ°ÊŸá´œx";
            default -> var0.coloredName();
        };
    }

    private void broadcastTitle(String var1, String var2, int var3) {
        Component var11 = Text.legacy(var1, 373192400);
        Component var12 = Text.legacy(var2, 373192400);
        Duration var22 = Duration.ofSeconds(1L);
        Duration var35 = Duration.ofSeconds(3L);
        Duration var40 = Duration.ofSeconds(1L);
        Times var13 = Times.times(var22, var35, var40);
        Title var14 = Title.title(var11, var12, var13);

        for (Player var16 : Bukkit.getOnlinePlayers()) {
            var16.showTitle(var14);
            Location var39 = var16.getLocation();
            Sound var42 = Sound.UI_TOAST_CHALLENGE_COMPLETE;
            var16.playSound(var39, var42, 1.0F, 1.0F);
        }
    }

    public static void applyProgression4(BlissPlugin var0, int var1) {
        GlobalState var8 = var0.globalState(724248306);
        if (var8 != null) {
            var8.setProg1(false, 1805549590);
            var8.setProg2(false, 1105469454);
            var8.setProg3(false, 1348581053);
            var8.setVillagersAuto(true, 2036260727);
            if (var8.progression(790021995) < 4) {
                var8.setProgression(4, 1556136516);
            }

            var8.save(343464104);
            Component var9 = Text.legacy("&6&lProgression 4", 373192400);
            Component var10 = Text.legacy("<##27D52D>Village and Pillage", 373192400);
            Duration var31 = Duration.ofSeconds(1L);
            Duration var51 = Duration.ofSeconds(3L);
            Duration var56 = Duration.ofSeconds(1L);
            Times var11 = Times.times(var31, var51, var56);
            Title var12 = Title.title(var9, var10, var11);

            for (Player var14 : Bukkit.getOnlinePlayers()) {
                var14.showTitle(var12);
                Location var55 = var14.getLocation();
                Sound var58 = Sound.UI_TOAST_CHALLENGE_COMPLETE;
                var14.playSound(var55, var58, 1.0F, 1.0F);
            }
        }
    }

    private void handleMenu(CommandSender var1, String[] var2, int var3) {
        if (var1 instanceof Player var8) {
            MenuListener var9 = this.plugin.menu(1233327997);
            if (var9 != null) {
                int var21 = MenuListener.pageArg(var2, 1, 580559153);
                var9.openMenu(var8, var21, 2016488517);
            }
        } else {
            Component var20 = Text.legacy("&cPlayer only.", 373192400);
            var1.sendMessage(var20);
        }
    }

    private void requirePlayer(CommandSender var1, Consumer<Player> var2, int var3) {
        if (var1 instanceof Player var6) {
            var2.accept(var6);
        } else {
            Component var13 = Text.legacy("&cPlayer only.", 373192400);
            var1.sendMessage(var13);
        }
    }

    private String describe(Location var1, int var2) {
        String var8 = var1.getWorld().getName();
        int var12 = (int)var1.getX();
        int var15 = (int)var1.getY();
        int var18 = (int)var1.getZ();
        return var8 + " " + var12 + " " + var15 + " " + var18;
    }

    private void handleGhostClear(CommandSender var1, String[] var2, int var3) {
        if (var1 instanceof Player var10) {
            int var11 = 24;
            if (var2.length >= 2) {
                try {
                    int var61 = Integer.parseInt(var2[1]);
                    int var50 = Math.min(96, var61);
                    var11 = Math.max(1, var50);
                } catch (NumberFormatException var70) {
                }
            }

            int var12 = 0;
            World var26 = var10.getWorld();
            Location var52 = var10.getLocation();
            double var63 = (double)var11;
            double var66 = (double)var11;
            double var68 = (double)var11;

            for (Entity var14 : var26.getNearbyEntities(var52, var63, var66, var68)) {
                if (var14 instanceof BlockDisplay) {
                    BlockDisplay var15 = (BlockDisplay)var14;
                    if (var14.getScoreboardTags().contains("BlissEnchObsidianGlow")) {
                        Material var44 = var14.getLocation().getBlock().getType();
                        Material var54 = Material.OBSIDIAN;
                        if (var44 != var54) {
                            var15.remove();
                            var12++;
                        }
                    }
                }
            }

            Component var57 = Text.legacy("&aCleared &f" + var12 + "&a ghost obsidian marker(s) within " + var11 + " blocks.", 373192400);
            var10.sendMessage(var57);
        } else {
            Component var59 = Text.legacy("&cPlayer only.", 373192400);
            var1.sendMessage(var59);
        }
    }

    private void handleHelpBook(CommandSender var1, int var2) {
        if (var1 instanceof Player var5) {
            HelpBook.give(var5, 2114637800);
            Component var14 = Text.legacy("&aHere's your Bliss guide book â€” read every event & mechanic inside.", 373192400);
            var5.sendMessage(var14);
        } else {
            Component var12 = Text.legacy("&cPlayer only.", 373192400);
            var1.sendMessage(var12);
        }
    }

    private void usage(CommandSender var1, int var2) {
        boolean var14;
        if (this.plugin.dragonGames(1198797739) != null) {
            var14 = true;
        } else {
            var14 = false;
        }

        if (!var1.hasPermission("bliss.admin")) {
            ArrayList var17 = new ArrayList();
            String var29 = "helpbook";
            String var5 = "Open the player guide book";
            CommandHelp.Entry var30 = CommandHelp.of(var29, var5, 1959232616);
            var17.add(var30);
            if (var14) {
                String var31 = "dragongames points check";
                String var37 = "Your Dragon Games points";
                CommandHelp.Entry var32 = CommandHelp.of(var31, var37, 1959232616);
                var17.add(var32);
            }

            String var33 = "bliss";
            String var38 = "The Bliss SMP player guide.";
            CommandHelp.show(var1, var33, var38, var17, 696249826);
        } else {
            CommandHelp.Entry[] var40 = new CommandHelp.Entry[21];
            String var8 = "helpbook";
            String var9 = "Open the player guide book";
            CommandHelp.Entry var65 = CommandHelp.of(var8, var9, 1959232616);
            var40[0] = var65;
            String var66 = "admin helpbook";
            String var106 = "The admin guide: setup, events, every command";
            CommandHelp.Entry var67 = CommandHelp.of(var66, var106, 1959232616);
            var40[1] = var67;
            String var68 = "give <item> [amount]";
            String var107 = "Spawn a Bliss item (trader, gem, obsidianâ€¦)";
            CommandHelp.Entry var69 = CommandHelp.of(var68, var107, 1959232616);
            var40[2] = var69;
            String var70 = "pedestal <set|activate|deactivate|status>";
            String var108 = "Build and control the pedestal";
            CommandHelp.Entry var71 = CommandHelp.of(var70, var108, 1959232616);
            var40[3] = var71;
            String var72 = "orb <set|remove|status>";
            String var109 = "Stand the Gold Gem orb where you are";
            CommandHelp.Entry var73 = CommandHelp.of(var72, var109, 1959232616);
            var40[4] = var73;
            String var74 = "setenergy <player> <n>";
            String var110 = "Set a player's gem energy";
            CommandHelp.Entry var75 = CommandHelp.of(var74, var110, 1959232616);
            var40[5] = var75;
            String var76 = "reroll <player>";
            String var111 = "Give a player a fresh random gem";
            CommandHelp.Entry var77 = CommandHelp.of(var76, var111, 1959232616);
            var40[6] = var77;
            String var78 = "gemdisable";
            String var112 = "Tag-disable every player's gem";
            CommandHelp.Entry var79 = CommandHelp.of(var78, var112, 1959232616);
            var40[7] = var79;
            String var80 = "gemenable";
            String var113 = "Clear the tag-disable from everyone";
            CommandHelp.Entry var81 = CommandHelp.of(var80, var113, 1959232616);
            var40[8] = var81;
            String var82 = "reset <player>";
            String var114 = "Wipe a player's gem state";
            CommandHelp.Entry var83 = CommandHelp.of(var82, var114, 1959232616);
            var40[9] = var83;
            String var84 = "gemgem <player>";
            String var115 = "Re-issue a player's gem item";
            CommandHelp.Entry var85 = CommandHelp.of(var84, var115, 1959232616);
            var40[10] = var85;
            String var86 = "cooldown <player>";
            String var116 = "Clear a player's ability cooldowns";
            CommandHelp.Entry var87 = CommandHelp.of(var86, var116, 1959232616);
            var40[11] = var87;
            String var88 = "progression <0-5|reset>";
            String var117 = "Set the server's progression stage";
            CommandHelp.Entry var89 = CommandHelp.of(var88, var117, 1959232616);
            var40[12] = var89;
            String var90 = "config";
            String var118 = "Open the config menu (in-game)";
            CommandHelp.Entry var91 = CommandHelp.of(var90, var118, 1959232616);
            var40[13] = var91;
            String var92 = "menu";
            String var119 = "Open the admin gem menu";
            CommandHelp.Entry var93 = CommandHelp.of(var92, var119, 1959232616);
            var40[14] = var93;
            String var94 = "ghostclear";
            String var120 = "Clear stuck Astra projection ghosts";
            CommandHelp.Entry var95 = CommandHelp.of(var94, var120, 1959232616);
            var40[15] = var95;
            String var96 = "wisp set <player> <motif|none> [viewers]";
            String var121 = "Hide a player behind a sigil; name who may see it";
            CommandHelp.Entry var97 = CommandHelp.of(var96, var121, 1959232616);
            var40[16] = var97;
            String var98 = "wisp list";
            String var122 = "Who is standing as a motif";
            CommandHelp.Entry var99 = CommandHelp.of(var98, var122, 1959232616);
            var40[17] = var99;
            String var100 = "pocket <tp|kick> <player>";
            String var123 = "Put a player on the dream's walkway, or take them off it";
            CommandHelp.Entry var101 = CommandHelp.of(var100, var123, 1959232616);
            var40[18] = var101;
            String var102 = "particles <off|decreased|on|extra>";
            String var124 = "How thickly every gem and ritual draws";
            CommandHelp.Entry var103 = CommandHelp.of(var102, var124, 1959232616);
            var40[19] = var103;
            String var104 = "pov <gem|off> [seconds]";
            String var125 = "Try a gem's POV overlay on yourself";
            CommandHelp.Entry var105 = CommandHelp.of(var104, var125, 1959232616);
            var40[20] = var105;
            List var41 = List.of(var40);
            ArrayList var24 = new ArrayList(var41);
            if (var14) {
                String var34 = "dragongames <start|end|bloodmoon|points|shop â€¦>";
                String var42 = "Run the Dragon Games event";
                CommandHelp.Entry var35 = CommandHelp.of(var34, var42, 1959232616);
                var24.add(var35);
            }

            String var36 = "bliss";
            String var43 = "Player guide plus admin tools.";
            CommandHelp.show(var1, var36, var43, var24, 696249826);
        }
    }

    public List<String> onTabComplete(CommandSender var1, Command var2, String var3, String[] var4) {
        ArrayList var5 = new ArrayList();
        ArrayList var13 = var5;
        if (var4.length >= 5 && var4[0].equalsIgnoreCase("wisp") && var4[1].equalsIgnoreCase("set")) {
            int var391 = var4.length - 1;
            String var14 = var4[var391].toLowerCase();

            for (String var652 : List.of("self", "all")) {
                if (var652.startsWith(var14)) {
                    var13.add(var652);
                }
            }

            for (Player var16 : Bukkit.getOnlinePlayers()) {
                String var46 = var16.getName().toLowerCase();
                if (var46.startsWith(var14)) {
                    String var395 = var16.getName();
                    var13.add(var395);
                }
            }

            return var13;
        } else {
            DragonGamesHook var641 = this.plugin.dragonGames(1198797739);
            if (var4.length >= 2 && var4[0].equalsIgnoreCase("dragongames")) {
                Object var383;
                if (var641 == null) {
                    var383 = var5;
                } else {
                    int var591 = var4.length;
                    String[] var533 = Arrays.copyOfRange(var4, 1, var591);
                    var383 = var641.complete(var1, var533, 525999774);
                }

                return (List<String>)var383;
            } else if (var4.length >= 2 && var4[0].equalsIgnoreCase("dream")) {
                PluginCommand var651 = this.plugin.getCommand("goldendream");
                Object var382;
                if (var651 == null) {
                    var382 = var5;
                } else {
                    String var571 = "goldendream";
                    int var639 = var4.length;
                    String[] var590 = Arrays.copyOfRange(var4, 1, var639);
                    var382 = var651.tabComplete(var1, var571, var590);
                }

                return (List<String>)var382;
            } else if (var4.length == 3 && var4[0].equalsIgnoreCase("pocket")) {
                String var643 = var4[2].toLowerCase();

                for (Player var17 : Bukkit.getOnlinePlayers()) {
                    String var90 = var17.getName().toLowerCase();
                    if (var90.startsWith(var643)) {
                        String var411 = var17.getName();
                        var13.add(var411);
                    }
                }

                return var13;
            } else {
                if (var4.length == 1) {
                    String[] var568 = new String[21];
                    String var617 = "helpbook";
                    var568[0] = var617;
                    String var618 = "give";
                    var568[1] = var618;
                    String var619 = "pedestal";
                    var568[2] = var619;
                    String var620 = "orb";
                    var568[3] = var620;
                    String var621 = "setenergy";
                    var568[4] = var621;
                    String var622 = "reroll";
                    var568[5] = var622;
                    String var623 = "gemdisable";
                    var568[6] = var623;
                    String var624 = "gemenable";
                    var568[7] = var624;
                    String var625 = "reset";
                    var568[8] = var625;
                    String var626 = "gemgem";
                    var568[9] = var626;
                    String var627 = "cooldown";
                    var568[10] = var627;
                    String var628 = "progression";
                    var568[11] = var628;
                    String var629 = "config";
                    var568[12] = var629;
                    String var630 = "menu";
                    var568[13] = var630;
                    String var631 = "ghostclear";
                    var568[14] = var631;
                    String var632 = "wisp";
                    var568[15] = var632;
                    String var633 = "dream";
                    var568[16] = var633;
                    String var634 = "pocket";
                    var568[17] = var634;
                    String var635 = "particles";
                    var568[18] = var635;
                    String var636 = "admin";
                    var568[19] = var636;
                    String var637 = "pov";
                    var568[20] = var637;
                    List<String> var569 = List.of(var568);
                    ArrayList<String> var362 = new ArrayList<>(var569);
                    if (var641 != null) {
                        var362.add("dragongames");
                    }

                    for (String var671 : var362) {
                        String var527 = var4[0].toLowerCase();
                        if (var671.startsWith(var527)) {
                            var13.add(var671);
                        }
                    }
                } else if (var4.length == 2) {
                    String var649 = var4[0].toLowerCase();
                    switch (var649) {
                        case "admin":
                            String var245 = "helpbook";
                            String var478 = var4[1].toLowerCase();
                            if (var245.startsWith(var478)) {
                                var5.add("helpbook");
                            }
                            break;
                        case "pov":
                            String var262 = "strength";
                            String var485 = "fire";
                            String var548 = "wealth";
                            String var575 = "speed";
                            String var593 = "puff";
                            String var616 = "astra";
                            String var640 = "life";
                            String var11 = "flux";
                            String var12 = "off";

                            for (String var675 : List.of(var262, var485, var548, var575, var593, var616, var640, var11, var12)) {
                                String var488 = var4[1].toLowerCase();
                                if (var675.startsWith(var488)) {
                                    var13.add(var675);
                                }
                            }
                            break;
                        case "give":
                            String[] var287 = new String[11];
                            String var576 = "trader";
                            var287[0] = var576;
                            String var577 = "upgrader";
                            var287[1] = var577;
                            String var578 = "energy";
                            var287[2] = var578;
                            String var579 = "repair";
                            var287[3] = var579;
                            String var580 = "gemselector";
                            var287[4] = var580;
                            String var581 = "restoration";
                            var287[5] = var581;
                            String var582 = "restorationcore";
                            var287[6] = var582;
                            String var583 = "fragment";
                            var287[7] = var583;
                            String var584 = "obsidian";
                            var287[8] = var584;
                            String var585 = "prismatic";
                            var287[9] = var585;
                            String var586 = "gem";
                            var287[10] = var586;

                            for (String var677 : List.of(var287)) {
                                String var499 = var4[1].toLowerCase();
                                if (var677.startsWith(var499)) {
                                    var13.add(var677);
                                }
                            }
                            break;
                        case "pedestal":
                            String var311 = "set";
                            String var506 = "activate";
                            String var565 = "deactivate";
                            String var587 = "status";

                            for (String var679 : List.of(var311, var506, var565, var587)) {
                                String var509 = var4[1].toLowerCase();
                                if (var679.startsWith(var509)) {
                                    var13.add(var679);
                                }
                            }
                            break;
                        case "orb":
                            String var299 = "set";
                            String var501 = "remove";
                            String var563 = "status";

                            for (String var678 : List.of(var299, var501, var563)) {
                                String var504 = var4[1].toLowerCase();
                                if (var678.startsWith(var504)) {
                                    var13.add(var678);
                                }
                            }
                            break;
                        case "wisp":
                            for (String var672 : List.of("set", "list")) {
                                String var469 = var4[1].toLowerCase();
                                if (var672.startsWith(var469)) {
                                    var13.add(var672);
                                }
                            }
                            break;
                        case "pocket":
                            for (String var18 : List.of("tp", "kick")) {
                                String var464 = var4[1].toLowerCase();
                                if (var18.startsWith(var464)) {
                                    var13.add(var18);
                                }
                            }
                            break;
                        case "particles":
                            for (Particles.Level var20 : Particles.Level.values()) {
                                String var282 = var20.label();
                                String var494 = var4[1].toLowerCase();
                                if (var282.startsWith(var494)) {
                                    String var496 = var20.label();
                                    var13.add(var496);
                                }
                            }
                            break;
                        case "setenergy":
                        case "reroll":
                        case "reset":
                        case "gemgem":
                        case "cooldown":
                            for (Player var674 : Bukkit.getOnlinePlayers()) {
                                String var258 = var674.getName().toLowerCase();
                                String var482 = var4[1].toLowerCase();
                                if (var258.startsWith(var482)) {
                                    String var484 = var674.getName();
                                    var13.add(var484);
                                }
                            }
                            break;
                        case "progression":
                            String var233 = "0";
                            String var471 = "1";
                            String var544 = "2";
                            String var574 = "3";
                            String var592 = "4";
                            String var9 = "5";
                            String var10 = "reset";

                            for (String var673 : List.of(var233, var471, var544, var574, var592, var9, var10)) {
                                String var474 = var4[1].toLowerCase();
                                if (var673.startsWith(var474)) {
                                    var13.add(var673);
                                }
                            }
                    }
                } else if (var4.length == 3) {
                    if (var4[0].equalsIgnoreCase("wisp") && var4[1].equalsIgnoreCase("set")) {
                        for (Player var658 : Bukkit.getOnlinePlayers()) {
                            String var195 = var658.getName().toLowerCase();
                            String var456 = var4[2].toLowerCase();
                            if (var195.startsWith(var456)) {
                                String var458 = var658.getName();
                                var13.add(var458);
                            }
                        }

                        return var13;
                    }

                    if (var4[0].equalsIgnoreCase("give") && var4[1].equalsIgnoreCase("gem")) {
                        for (String var656 : List.of("t1", "t2")) {
                            String var444 = var4[2].toLowerCase();
                            if (var656.startsWith(var444)) {
                                var13.add(var656);
                            }
                        }
                    }

                    if (var4[0].equalsIgnoreCase("give") && !var4[1].equalsIgnoreCase("gem")) {
                        String var173 = "1";
                        String var450 = "16";
                        String var539 = "32";
                        String var573 = "64";

                        for (String var657 : List.of(var173, var450, var539, var573)) {
                            String var452 = var4[2];
                            if (var657.startsWith(var452)) {
                                var13.add(var657);
                            }
                        }
                    }
                } else if (var4.length == 4) {
                    if (var4[0].equalsIgnoreCase("wisp") && var4[1].equalsIgnoreCase("set")) {
                        List<String> var534 = EntityMotifs.motifs(1082160894);
                        ArrayList<String> var109 = new ArrayList<>(var534);
                        var109.add("none");

                        for (String var661 : var109) {
                            String var423 = var4[3].toLowerCase();
                            if (var661.startsWith(var423)) {
                                var13.add(var661);
                            }
                        }

                        return var13;
                    }

                    if (var4[0].equalsIgnoreCase("give") && var4[1].equalsIgnoreCase("gem")) {
                        String var130 = "1";
                        String var429 = "16";
                        String var536 = "32";
                        String var572 = "64";

                        for (String var655 : List.of(var130, var429, var536, var572)) {
                            String var431 = var4[3];
                            if (var655.startsWith(var431)) {
                                var13.add(var655);
                            }
                        }
                    }
                }

                return var13;
            }
        }
    }

    static {
        long[] var4 = new long[]{0L, 18L, 16L, 14L, 12L, 10L, 8L, 6L, 5L, 5L, 4L, 4L, 3L, 3L, 2L};
        PROG1_STAGE_DELAYS = var4;
    }
}
