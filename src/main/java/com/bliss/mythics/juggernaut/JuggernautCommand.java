package com.bliss.mythics.juggernaut;

import com.bliss.admin.CommandHelp;
import com.bliss.mythics.MythicItems;
import com.bliss.mythics.util.Text;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public final class JuggernautCommand implements CommandExecutor, TabCompleter {
    private static final String ADMIN = "bliss.admin";
    private final JuggernautManager mgr;
    private final JuggernautKitEditor kits;

    public JuggernautCommand(JuggernautManager var1, JuggernautKitEditor var2, int var3) {
        this.mgr = var1;
        this.kits = var2;
    }

    public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
        if (var4.length == 0) {
            this.usage(var1, 1214296942);
            return true;
        } else {
            String var15 = var4[0];
            Locale var61 = Locale.ROOT;
            String var9 = var15.toLowerCase(var61);
            switch (var9) {
                case "set":
                    this.doSet(var1, var4, 1794305189);
                    break;
                case "remove":
                    this.doRemove(var1, var4, 253648755);
                    break;
                case "revive":
                    this.doRevive(var1, var4, 870669346);
                    break;
                case "cinematic":
                    this.doCinematic(var1, var4, 540533633);
                    break;
                case "spot":
                    this.doSpot(var1, var4, 833764222);
                    break;
                case "ritual":
                    this.doRitual(var1, var4, 764317819);
                    break;
                case "unban":
                    this.doUnban(var1, var4, 1588345351);
                    break;
                case "kit":
                    this.doKit(var1, var4, 1660326060);
                    break;
                case "list":
                    this.doList(var1, 265467649);
                    break;
                default:
                    this.usage(var1, 1214296942);
            }

            return true;
        }
    }

    private void doSet(CommandSender var1, String[] var2, int var3) {
        if (!var1.hasPermission("bliss.admin")) {
            this.deny(var1, 1476531329);
        } else if (var2.length < 3) {
            String var34 = "/juggernaut set <player> <heretic|auratus>";
            CommandHelp.hint(var1, var34, 80456809);
        } else {
            Player var8 = Bukkit.getPlayerExact(var2[1]);
            if (var8 == null) {
                String var6 = "&cPlayer not found / offline.";
                this.send(var1, var6, 931332202);
            } else {
                JuggernautManager.Bond var9 = JuggernautManager.Bond.parse(var2[2]);
                if (var9 == null) {
                    String var55 = "&cBond must be heretic or auratus.";
                    this.send(var1, var55, 931332202);
                } else {
                    JuggernautKitEditor var24 = this.kits;
                    if (var24.isEditing(var8, 1844728332)) {
                        String var50 = "&c" + var8.getName() + " is editing the Juggernaut kit — they must save or cancel first.";
                        this.send(var1, var50, 931332202);
                    } else {
                        JuggernautManager var28 = this.mgr;
                        var28.designate(var8, var9, 183802259);
                        String var53 = var8.getName();
                        String var56 = label(var9, 35461625);
                        String var54 = "&a" + var53 + " is now the " + var56 + " Juggernaut.";
                        this.send(var1, var54, 931332202);
                        Component var45 = Text.legacy("<##FFD773>&lYou are bound to the " + label(var9, 35461625) + " gem.", 883008065);
                        var8.sendActionBar(var45);
                    }
                }
            }
        }
    }

    private static JuggernautManager.Bond bondArg(String[] var0, int var1, int var2) {
        int var6 = var0.length;
        if (var6 <= var1) {
            return null;
        } else {
            String var9 = var0[var1];
            Locale var19 = Locale.ROOT;
            String var5 = var9.toLowerCase(var19);
            if (var5.equals("heretic")) {
                return JuggernautManager.Bond.HERETIC;
            } else {
                return var5.equals("auratus") ? JuggernautManager.Bond.AURATUS : null;
            }
        }
    }

    private static String bondName(JuggernautManager.Bond var0, int var1) {
        if (var0 == null) {
            return "unknown";
        } else {
            JuggernautManager.Bond var3 = JuggernautManager.Bond.AURATUS;
            String var5;
            if (var0 == var3) {
                var5 = "Auratus";
            } else {
                var5 = "Heretic";
            }

            return var5;
        }
    }

    private void doKit(CommandSender var1, String[] var2, int var3) {
        if (!var1.hasPermission("bliss.admin")) {
            this.deny(var1, 1476531329);
        } else {
            String var122;
            if (var2.length >= 2) {
                String var119 = var2[1];
                Locale var164 = Locale.ROOT;
                var122 = var119.toLowerCase(var164);
            } else {
                var122 = "status";
            }

            if (var122.equals("status")) {
                JuggernautManager var203 = this.mgr;
                JuggernautManager.Bond var215 = JuggernautManager.Bond.HERETIC;
                String var212;
                if (var203.hasServerKit(var215, 85248624)) {
                    var212 = "&aHeretic&7: this server's own kit &7(juggernaut_kit_heretic.yml)";
                } else {
                    var212 = "&7Heretic: the kit shipped with the plugin.";
                }

                this.send(var1, var212, 931332202);
                JuggernautManager var206 = this.mgr;
                JuggernautManager.Bond var216 = JuggernautManager.Bond.AURATUS;
                String var211;
                if (var206.hasServerKit(var216, 85248624)) {
                    var211 = "&6Auratus&7: this server's own kit &7(juggernaut_kit_auratus.yml)";
                } else {
                    var211 = "&7Auratus: derived from the Heretic kit, re-trimmed gold.";
                }

                this.send(var1, var211, 931332202);
                Player var121;
                if (this.kits.editorId(1800728692) == null) {
                    var121 = null;
                } else {
                    var121 = Bukkit.getPlayer(this.kits.editorId(1800728692));
                }

                if (var121 != null) {
                    String var209 = var121.getName();
                    String var220 = bondName(this.kits.editingBond(1877993327), 2117143592);
                    String var210 = "&e" + var209 + " is editing the " + var220 + " kit right now.";
                    this.send(var1, var210, 931332202);
                }

                String var162 = "/juggernaut kit <edit|reset> <heretic|auratus>";
                CommandHelp.hint(var1, var162, 80456809);
            } else if (var122.equals("reset")) {
                JuggernautManager.Bond var222 = bondArg(var2, 2, 1987002369);
                if (var222 == null) {
                    String var201 = "&eWhich one? &f/juggernaut kit reset <heretic|auratus>";
                    this.send(var1, var201, 931332202);
                } else if (this.kits.editorId(1800728692) != null) {
                    Player var224 = Bukkit.getPlayer(this.kits.editorId(1800728692));
                    String var213;
                    if (var224 == null) {
                        var213 = "";
                    } else {
                        var213 = " by &f" + var224.getName();
                    }

                    String var198 = "&cThe kit is being edited" + var213 + "&c — reset would be undone by their save. Wait for them to finish.";
                    this.send(var1, var198, 931332202);
                } else {
                    JuggernautManager var190 = this.mgr;
                    String var196;
                    if (var190.resetKitTemplate(var222, 1880150375)) {
                        String var195 = bondName(var222, 2117143592);
                        JuggernautManager.Bond var8 = JuggernautManager.Bond.AURATUS;
                        String var221;
                        if (var222 == var8) {
                            var221 = "back to deriving from the Heretic kit in gold.";
                        } else {
                            var221 = "back to the one shipped with the plugin.";
                        }

                        var196 = "&eThe " + var195 + " kit is deleted — " + var221;
                    } else {
                        var196 = "&cThere is no " + bondName(var222, 2117143592) + " kit of its own to reset.";
                    }

                    this.send(var1, var196, 931332202);
                }
            } else if (var1 instanceof Player var10) {
                switch (var122) {
                    case "edit":
                        JuggernautManager.Bond var225 = bondArg(var2, 2, 1987002369);
                        if (var225 == null) {
                            String var187 = "&eWhich one? &f/juggernaut kit edit <heretic|auratus>";
                            this.send(var1, var187, 931332202);
                            return;
                        }

                        JuggernautKitEditor var59 = this.kits;
                        if (var59.isEditing(var10, 1844728332)) {
                            String var186 = "&cYou are already editing a kit.";
                            this.send(var1, var186, 931332202);
                            return;
                        }

                        JuggernautManager var62 = this.mgr;
                        if (var62.isJuggernaut(var10, 1070464021)) {
                            String var172 = "&cA Juggernaut cannot edit the kit — remove the role first.";
                            this.send(var1, var172, 931332202);
                            return;
                        }

                        JuggernautKitEditor var66 = this.kits;
                        if (!var66.begin(var10, var225, 76298422)) {
                            Player var80;
                            if (this.kits.editorId(1800728692) == null) {
                                var80 = null;
                            } else {
                                var80 = Bukkit.getPlayer(this.kits.editorId(1800728692));
                            }

                            String var185;
                            if (var80 == null) {
                                var185 = "";
                            } else {
                                var185 = " (&f" + var80.getName() + "&c)";
                            }

                            String var184 = "&cSomeone else is editing a kit" + var185 + ".";
                            this.send(var1, var184, 931332202);
                            return;
                        }

                        String var176 = "&aYou are wearing the " + bondName(var225, 2117143592) + " kit. Your own things are held safely.";
                        this.send(var1, var176, 931332202);
                        JuggernautManager.Bond var143 = JuggernautManager.Bond.AURATUS;
                        if (var225 == var143) {
                            JuggernautManager var71 = this.mgr;
                            JuggernautManager.Bond var144 = JuggernautManager.Bond.AURATUS;
                            if (!var71.hasServerKit(var144, 85248624)) {
                                String var177 = "&7Auratus had no kit of its own — this starts from the Heretic one in gold. Saving makes it independent, and the gold re-trim stops being applied.";
                                this.send(var1, var177, 931332202);
                            }
                        }

                        String var178 = "&7Arrange your armour, inventory, off hand and ender chest, then:";
                        this.send(var1, var178, 931332202);
                        String var179 = "&f/juggernaut kit save &7to keep it, &f/juggernaut kit cancel &7to walk away.";
                        this.send(var1, var179, 931332202);
                        String var180 = "&8The off hand is replaced by the bound gem when a Juggernaut is made.";
                        this.send(var1, var180, 931332202);
                        break;
                    case "save":
                        JuggernautKitEditor var35 = this.kits;
                        if (!var35.isEditing(var10, 1844728332)) {
                            String var169 = "&cYou are not editing the kit.";
                            this.send(var1, var169, 931332202);
                            return;
                        }

                        String var13 = bondName(this.kits.editingBond(1877993327), 2117143592);
                        JuggernautKitEditor var42 = this.kits;
                        if (var42.save(var10, 834517568)) {
                            String var166 = "&a" + var13 + " kit saved for this server. Your own things are back.";
                            this.send(var1, var166, 931332202);
                        } else {
                            String var168 = "&cThe "
                                + var13
                                + " kit could NOT be written — see the console. Your own things are back, but the kit is unchanged.";
                            this.send(var1, var168, 931332202);
                        }
                        break;
                    case "cancel":
                        JuggernautKitEditor var48 = this.kits;
                        if (!var48.isEditing(var10, 1844728332)) {
                            String var170 = "&cYou are not editing the kit.";
                            this.send(var1, var170, 931332202);
                            return;
                        }

                        JuggernautKitEditor var52 = this.kits;
                        var52.cancel(var10, 1623180345);
                        String var171 = "&eKit edit cancelled — nothing changed. Your own things are back.";
                        this.send(var1, var171, 931332202);
                        break;
                    default:
                        String var136 = "/juggernaut kit <edit|save|cancel|reset>";
                        CommandHelp.hint(var1, var136, 80456809);
                }
            } else {
                String var188 = "&cRun this in game.";
                this.send(var1, var188, 931332202);
            }
        }
    }

    private void doRemove(CommandSender var1, String[] var2, int var3) {
        if (!var1.hasPermission("bliss.admin")) {
            this.deny(var1, 1476531329);
        } else if (var2.length < 2) {
            String var27 = "/juggernaut remove <player>";
            CommandHelp.hint(var1, var27, 80456809);
        } else {
            Player var7 = Bukkit.getPlayerExact(var2[1]);
            if (var7 == null) {
                String var39 = "&cPlayer not found / offline.";
                this.send(var1, var39, 931332202);
            } else {
                JuggernautManager var18 = this.mgr;
                if (!var18.isJuggernaut(var7, 1070464021)) {
                    String var35 = "&c" + var7.getName() + " is not a Juggernaut.";
                    this.send(var1, var35, 931332202);
                } else {
                    JuggernautManager var22 = this.mgr;
                    var22.undesignate(var7, 1064325827);
                    String var38 = "&e" + var7.getName() + " is no longer a Juggernaut.";
                    this.send(var1, var38, 931332202);
                }
            }
        }
    }

    private void doRevive(CommandSender var1, String[] var2, int var3) {
        if (var2.length < 2) {
            String var79 = "/juggernaut revive <player>";
            CommandHelp.hint(var1, var79, 80456809);
        } else {
            boolean var21;
            label73: {
                label60:
                if (!var1.hasPermission("bliss.admin")) {
                    if (var1 instanceof Player var9 && this.carriesBothGems(var9, 1189416997)) {
                        break label60;
                    }

                    var21 = false;
                    break label73;
                }

                var21 = true;
            }

            if (!var21) {
                String var94 = "&cYou must hold BOTH mythic gems to invoke a revival.";
                this.send(var1, var94, 931332202);
            } else {
                UUID var98 = resolveUuid(var2[1], 1491166557);
                if (var98 == null) {
                    String var6 = "&cUnknown player.";
                    this.send(var1, var6, 931332202);
                } else {
                    UUID var66 = this.mgr.revivalInFlight(425699207);
                    if (var98.equals(var66)) {
                        String var93 = "&cA pedestal revival for " + var2[1] + " is already in progress — wait for it to finish.";
                        this.send(var1, var93, 931332202);
                    } else {
                        JuggernautManager var31 = this.mgr;
                        JuggernautManager.Bond var10 = var31.downedBondOf(var98, 336481994);
                        if (var1 instanceof Player var11
                            && !var1.hasPermission("bliss.admin")
                            && (var10 == null || !this.consumeBondGem(var11, var10, 875592208))) {
                            String var83 = "&cYou must be carrying " + var2[1] + "'s own gem to tether them back.";
                            this.send(var1, var83, 931332202);
                            return;
                        }

                        JuggernautManager var44 = this.mgr;
                        if (!var44.revive(var98, 823653800)) {
                            String var90 = "&c" + var2[1] + " is not a fallen Juggernaut.";
                            this.send(var1, var90, 931332202);
                        } else {
                            if (var10 != null && var1.hasPermission("bliss.admin")) {
                                String var84 = "&7Note: the gem they dropped on death is still out there — the revive kit hands them a new one.";
                                this.send(var1, var84, 931332202);
                            }

                            String var87 = "&a" + var2[1] + " has been revived — they may rejoin and will be re-kitted.";
                            this.send(var1, var87, 931332202);
                            Bukkit.broadcast(Text.legacy("<##FFD773>&l" + var2[1] + " &7has been revived by the soul-tether.", 883008065));
                        }
                    }
                }
            }
        }
    }

    private void doUnban(CommandSender var1, String[] var2, int var3) {
        if (!var1.hasPermission("bliss.admin")) {
            this.deny(var1, 1476531329);
        } else if (var2.length < 2) {
            String var28 = "/juggernaut unban <player>";
            CommandHelp.hint(var1, var28, 80456809);
        } else {
            UUID var8 = resolveUuid(var2[1], 1491166557);
            if (var8 == null) {
                String var47 = "&cUnknown player.";
                this.send(var1, var47, 931332202);
            } else {
                UUID var32 = this.mgr.revivalInFlight(425699207);
                if (var8.equals(var32)) {
                    String var40 = "&cA pedestal revival for " + var2[1] + " is already in progress — wait for it to finish.";
                    this.send(var1, var40, 931332202);
                } else {
                    JuggernautManager var21 = this.mgr;
                    if (!var21.unban(var8, 1175012567)) {
                        String var46 = "&c" + var2[1] + " is not a fallen Juggernaut.";
                        this.send(var1, var46, 931332202);
                    } else {
                        String var43 = "&a" + var2[1] + " is no longer locked out — they may rejoin.";
                        this.send(var1, var43, 931332202);
                    }
                }
            }
        }
    }

    private static UUID resolveUuid(String var0, int var1) {
        Player var3 = Bukkit.getPlayerExact(var0);
        if (var3 != null) {
            return var3.getUniqueId();
        } else {
            OfflinePlayer var4 = Bukkit.getOfflinePlayer(var0);
            UUID var17;
            if (var4 == null || !var4.hasPlayedBefore() && !var4.isOnline()) {
                var17 = null;
            } else {
                var17 = var4.getUniqueId();
            }

            return var17;
        }
    }

    private void doCinematic(CommandSender var1, String[] var2, int var3) {
        if (!var1.hasPermission("bliss.admin")) {
            this.deny(var1, 1476531329);
        } else if (var2.length < 2) {
            String var47 = "/juggernaut cinematic <heretic|auratus> [player]";
            CommandHelp.hint(var1, var47, 80456809);
        } else {
            JuggernautManager.Bond var8 = JuggernautManager.Bond.parse(var2[1]);
            if (var8 == null) {
                String var53 = "&cBond must be heretic or auratus.";
                this.send(var1, var53, 931332202);
            } else {
                Player var9;
                if (var2.length >= 3) {
                    var9 = Bukkit.getPlayerExact(var2[2]);
                    if (var9 == null) {
                        String var52 = "&cPlayer not found / offline.";
                        this.send(var1, var52, 931332202);
                        return;
                    }
                } else if (var1 instanceof Player p) {
                    var9 = p;
                } else {
                    String var51 = "&cSpecify a player when running from console.";
                    this.send(var1, var51, 931332202);
                    return;
                }

                JuggernautManager var27 = this.mgr;
                JuggernautManager.Bond var7 = JuggernautManager.Bond.AURATUS;
                boolean var54;
                if (var8 == var7) {
                    var54 = true;
                } else {
                    var54 = false;
                }

                var27.playSummonCinematic(var9, var54, 640918837);
                String var49 = label(var8, 35461625);
                String var56 = var9.getName();
                String var50 = "&aPlaying the " + var49 + " summoning cinematic on " + var56 + ".";
                this.send(var1, var50, 931332202);
            }
        }
    }

    private void doSpot(CommandSender var1, String[] var2, int var3) {
        if (!var1.hasPermission("bliss.admin")) {
            this.deny(var1, 1476531329);
        } else if (var2.length < 2) {
            String var67 = "/juggernaut spot <set|clear|status> <heretic|auratus>";
            CommandHelp.hint(var1, var67, 80456809);
        } else {
            String var19 = var2[1];
            Locale var69 = Locale.ROOT;
            String var8 = var19.toLowerCase(var69);
            if (var8.equals("status")) {
                for (JuggernautManager.Bond var107 : JuggernautManager.Bond.values()) {
                    JuggernautManager var62 = this.mgr;
                    Location var13 = var62.getSpot(var107, 1757205301);
                    String var99 = label(var107, 35461625);
                    String var103;
                    if (var13 == null) {
                        var103 = "&cunset";
                    } else {
                        var103 = "&a" + fmt(var13, 1266224921);
                    }

                    String var100 = "&7" + var99 + " spot: " + var103;
                    this.send(var1, var100, 931332202);
                }
            } else if (var2.length < 3) {
                String var73 = "/juggernaut spot " + var8 + " <heretic|auratus>";
                CommandHelp.hint(var1, var73, 80456809);
            } else {
                JuggernautManager.Bond var9 = JuggernautManager.Bond.parse(var2[2]);
                if (var9 == null) {
                    String var97 = "&cBond must be heretic or auratus.";
                    this.send(var1, var97, 931332202);
                } else {
                    switch (var8) {
                        case "set":
                            if (!(var1 instanceof Player var12)) {
                                String var93 = "&cRun this in-game, standing on the spot.";
                                this.send(var1, var93, 931332202);
                                return;
                            }

                            JuggernautManager var43 = this.mgr;
                            Location var89 = var12.getLocation();
                            var43.setSpot(var9, var89, 851501813);
                            String var92 = "&a" + label(var9, 35461625) + " summoning spot set to your location.";
                            this.send(var1, var92, 931332202);
                            break;
                        case "clear":
                            JuggernautManager var48 = this.mgr;
                            var48.clearSpot(var9, 65308527);
                            String var96 = "&e" + label(var9, 35461625) + " spot cleared.";
                            this.send(var1, var96, 931332202);
                            break;
                        default:
                            String var79 = "/juggernaut spot <set|clear|status> <heretic|auratus>";
                            CommandHelp.hint(var1, var79, 80456809);
                    }
                }
            }
        }
    }

    private void doRitual(CommandSender var1, String[] var2, int var3) {
        if (!var1.hasPermission("bliss.admin")) {
            this.deny(var1, 1476531329);
        } else {
            if (var2.length >= 2) {
                Player var59 = Bukkit.getPlayerExact(var2[1]);
                if (var59 == null) {
                    String var57 = "&cPlayer not found / offline.";
                    this.send(var1, var57, 931332202);
                    return;
                }

                JuggernautManager var24 = this.mgr;
                JuggernautManager.Bond var9 = var24.bondOf(var59, 1322009532);
                if (var9 == null) {
                    String var52 = "&c" + var59.getName() + " is not a Juggernaut.";
                    this.send(var1, var52, 931332202);
                    return;
                }

                JuggernautManager var29 = this.mgr;
                Location var10 = var29.getSpot(var9, 1757205301);
                if (var10 != null) {
                    var59.teleport(var10);
                }

                JuggernautManager var35 = this.mgr;
                JuggernautManager.Bond var7 = JuggernautManager.Bond.AURATUS;
                boolean var58;
                if (var9 == var7) {
                    var58 = true;
                } else {
                    var58 = false;
                }

                var35.playSummonCinematic(var59, var58, 640918837);
                String var56 = "&aRan the ritual for " + var59.getName() + ".";
                this.send(var1, var56, 931332202);
            } else {
                int var8 = this.mgr.summonToSpots(1139648124);
                String var49 = "&aSummoned &f" + var8 + " &aJuggernaut(s) to their spots with their rituals.";
                this.send(var1, var49, 931332202);
            }
        }
    }

    private static String fmt(Location var0, int var1) {
        String var7 = var0.getWorld().getName();
        int var11 = (int)var0.getX();
        int var14 = (int)var0.getY();
        int var17 = (int)var0.getZ();
        return var7 + " " + var11 + "," + var14 + "," + var17;
    }

    private void doList(CommandSender var1, int var2) {
        if (!var1.hasPermission("bliss.admin")) {
            this.deny(var1, 1476531329);
        } else {
            ArrayList var13 = new ArrayList();
            ArrayList var8 = var13;

            for (Player var56 : Bukkit.getOnlinePlayers()) {
                JuggernautManager var34 = this.mgr;
                JuggernautManager.Bond var11 = var34.bondOf(var56, 1322009532);
                if (var11 != null) {
                    String var45 = var56.getName();
                    String var51 = label(var11, 35461625);
                    JuggernautManager var52 = this.mgr;
                    String var54;
                    if (var52.isDowned(var56, 584775972)) {
                        var54 = "&c downed";
                    } else {
                        var54 = "&a alive";
                    }

                    String var46 = "&7- &f" + var45 + " &8[" + var51 + var54 + "&8]";
                    var8.add(var46);
                }
            }

            if (var8.isEmpty()) {
                String var5 = "&7No online Juggernauts.";
                this.send(var1, var5, 931332202);
            } else {
                String var48 = "&8Juggernauts:";
                this.send(var1, var48, 931332202);

                for (String var10 : (List<String>)var8) {
                    this.send(var1, var10, 931332202);
                }
            }
        }
    }

    private boolean consumeBondGem(Player var1, JuggernautManager.Bond var2, int var3) {
        ItemStack[] var8 = var1.getInventory().getStorageContents();
        int var9 = 0;

        while (true) {
            int var29 = var8.length;
            if (var9 >= var29) {
                ItemStack var39 = var1.getInventory().getItemInOffHand();
                if (matchesBond(var39, var2, 2003466471)) {
                    PlayerInventory var20 = var1.getInventory();
                    takeOne(var20, var39, -1, true, 2070706757);
                    return true;
                }

                return false;
            }

            ItemStack var24 = var8[var9];
            if (matchesBond(var24, var2, 2003466471)) {
                PlayerInventory var27 = var1.getInventory();
                ItemStack var35 = var8[var9];
                takeOne(var27, var35, var9, false, 2070706757);
                return true;
            }

            var9++;
        }
    }

    private static boolean matchesBond(ItemStack var0, JuggernautManager.Bond var1, int var2) {
        JuggernautManager.Bond var4 = JuggernautManager.Bond.HERETIC;
        boolean var7;
        if (var1 == var4) {
            var7 = MythicItems.isMythicBloodGem(var0, 824514243);
        } else {
            var7 = MythicItems.isMythicAegisGem(var0, 73386651);
        }

        return var7;
    }

    private static void takeOne(PlayerInventory var0, ItemStack var1, int var2, boolean var3, int var4) {
        if (var1.getAmount() > 1) {
            int var17 = var1.getAmount() - 1;
            var1.setAmount(var17);
        } else {
            if (var3) {
                var0.setItemInOffHand(null);
            } else {
                var0.setItem(var2, null);
            }
        }
    }

    private boolean carriesBothGems(Player var1, int var2) {
        boolean var5 = false;
        boolean var6 = false;

        for (ItemStack var10 : var1.getInventory().getStorageContents()) {
            if (MythicItems.isMythicBloodGem(var10, 824514243)) {
                var5 = true;
            }

            if (MythicItems.isMythicAegisGem(var10, 73386651)) {
                var6 = true;
            }
        }

        ItemStack var40 = var1.getInventory().getItemInOffHand();
        if (MythicItems.isMythicBloodGem(var40, 824514243)) {
            var5 = true;
        }

        if (MythicItems.isMythicAegisGem(var40, 73386651)) {
            var6 = true;
        }

        boolean var38;
        if (var5 && var6) {
            var38 = true;
        } else {
            var38 = false;
        }

        return var38;
    }

    private static String label(JuggernautManager.Bond var0, int var1) {
        JuggernautManager.Bond var3 = JuggernautManager.Bond.HERETIC;
        String var4;
        if (var0 == var3) {
            var4 = "Heretic";
        } else {
            var4 = "Auratus";
        }

        return var4;
    }

    private void usage(CommandSender var1, int var2) {
        String var4 = "juggernaut";
        String var5 = "Manage the Juggernaut bindings (Heretic / Auratus).";
        String var6 = "set <heretic|auratus> <player>";
        String var7 = "Bind a player as a Juggernaut";
        CommandHelp.Entry var16 = CommandHelp.of(var6, var7, 1959232616);
        String var18 = "remove <player>";
        String var8 = "Drop a player's binding";
        CommandHelp.Entry var19 = CommandHelp.of(var18, var8, 1959232616);
        String var20 = "revive <player>";
        String var9 = "Revive a fallen Juggernaut";
        CommandHelp.Entry var21 = CommandHelp.of(var20, var9, 1959232616);
        String var22 = "unban <player>";
        String var10 = "Lift a Juggernaut death ban";
        CommandHelp.Entry var23 = CommandHelp.of(var22, var10, 1959232616);
        String var24 = "cinematic <heretic|auratus>";
        String var11 = "Replay the summon cinematic";
        CommandHelp.Entry var25 = CommandHelp.of(var24, var11, 1959232616);
        String var26 = "spot";
        String var12 = "Set the fountain summon spot";
        CommandHelp.Entry var27 = CommandHelp.of(var26, var12, 1959232616);
        String var28 = "ritual";
        String var13 = "Start the summon ritual";
        CommandHelp.Entry var29 = CommandHelp.of(var28, var13, 1959232616);
        String var30 = "kit <edit|reset> <heretic|auratus>";
        String var14 = "Change a kit — this server only";
        CommandHelp.Entry var31 = CommandHelp.of(var30, var14, 1959232616);
        String var32 = "list";
        String var15 = "List the current Juggernauts";
        CommandHelp.Entry var33 = CommandHelp.of(var32, var15, 1959232616);
        List var17 = List.of(var16, var19, var21, var23, var25, var27, var29, var31, var33);
        CommandHelp.show(var1, var4, var5, var17, 696249826);
    }

    private void deny(CommandSender var1, int var2) {
        String var5 = "&cNo permission.";
        this.send(var1, var5, 931332202);
    }

    private void send(CommandSender var1, String var2, int var3) {
        Component var6 = Text.legacy(var2, 883008065);
        var1.sendMessage(var6);
    }

    public List<String> onTabComplete(CommandSender var1, Command var2, String var3, String[] var4) {
        ArrayList var5 = new ArrayList();
        ArrayList var8 = var5;
        String var15 = var4[0];
        Locale var109 = Locale.ROOT;
        String var9 = var15.toLowerCase(var109);
        if (var4.length == 1) {
            String[] var20 = new String[9];
            String var7 = "set";
            var20[0] = var7;
            String var157 = "remove";
            var20[1] = var157;
            String var158 = "revive";
            var20[2] = var158;
            String var159 = "unban";
            var20[3] = var159;
            String var160 = "cinematic";
            var20[4] = var160;
            String var161 = "spot";
            var20[5] = var161;
            String var162 = "ritual";
            var20[6] = var162;
            String var163 = "kit";
            var20[7] = var163;
            String var164 = "list";
            var20[8] = var164;

            for (String var13 : var20) {
                if (var13.startsWith(var9)) {
                    var8.add(var13);
                }
            }
        } else if (var4.length == 2) {
            if (var9.equals("cinematic")) {
                var5.add("heretic");
                var5.add("auratus");
            } else if (var9.equals("spot")) {
                var5.add("set");
                var5.add("clear");
                var5.add("status");
            } else if (var9.equals("kit")) {
                var5.add("edit");
                var5.add("save");
                var5.add("cancel");
                var5.add("reset");
                var5.add("status");
            } else if (!var9.equals("list")) {
                for (Player var167 : Bukkit.getOnlinePlayers()) {
                    String var124 = var167.getName();
                    var8.add(var124);
                }
            }
        } else if (var4.length == 3) {
            if (var9.equals("set")) {
                var5.add("heretic");
                var5.add("auratus");
            } else if (var9.equals("spot")) {
                var5.add("heretic");
                var5.add("auratus");
            } else if (!var9.equals("kit") || !var4[1].equalsIgnoreCase("edit") && !var4[1].equalsIgnoreCase("reset")) {
                if (var9.equals("cinematic")) {
                    for (Player var168 : Bukkit.getOnlinePlayers()) {
                        String var146 = var168.getName();
                        var8.add(var146);
                    }
                }
            } else {
                var5.add("heretic");
                var5.add("auratus");
            }
        }

        return var8;
    }
}
