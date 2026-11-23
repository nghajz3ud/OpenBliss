package com.bliss.goldgem;

import com.bliss.admin.CommandHelp;
import com.bliss.villager.Text;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public final class VictimCommand implements CommandExecutor, TabCompleter {
    private final GoldGemManager manager;

    public VictimCommand(GoldGemManager var1, int var2) {
        this.manager = var1;
    }

    public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
        if (var1 instanceof Player var11) {
            if (GoldGem.findInInventory(var11, 1361603346) == null) {
                String var92 = "&cOnly the Gold Gem's holder may command souls.";
                this.msg(var1, var92, 372431639);
                return true;
            } else if (var4.length >= 2 && (var4[0].equalsIgnoreCase("add") || var4[0].equalsIgnoreCase("remove"))) {
                OfflinePlayer var12 = Bukkit.getPlayerExact(var4[1]);
                if (var12 == null) {
                    OfflinePlayer var13 = Bukkit.getOfflinePlayer(var4[1]);
                    if (!var13.hasPlayedBefore()) {
                        String var95 = "&cUnknown player: &f" + var4[1];
                        this.msg(var1, var95, 372431639);
                        return true;
                    }

                    var12 = var13;
                }

                UUID var46 = var12.getUniqueId();
                UUID var81 = var11.getUniqueId();
                if (var46.equals(var81)) {
                    String var102 = "&cYou cannot mark your own soul.";
                    this.msg(var1, var102, 372431639);
                    return true;
                } else {
                    String var70;
                    if (var12.getName() == null) {
                        var70 = var12.getUniqueId().toString();
                    } else {
                        var70 = var12.getName();
                    }

                    boolean var14 = var4[0].equalsIgnoreCase("add");
                    boolean var57;
                    if (var14) {
                        GoldGemManager var56 = this.manager;
                        UUID var85 = var12.getUniqueId();
                        var57 = var56.addVictim(var85, 1646529536);
                    } else {
                        GoldGemManager var63 = this.manager;
                        UUID var89 = var12.getUniqueId();
                        var57 = var63.removeVictim(var89, 211332633);
                    }

                    if (var14) {
                        String var105;
                        if (var57) {
                            var105 = "<##FFD773>Marked &f" + var70 + "<##FFD773>'s soul for harvest.";
                        } else {
                            var105 = "&7" + var70 + "'s soul is already marked.";
                        }

                        this.msg(var1, var105, 372431639);
                    } else {
                        String var104;
                        if (var57) {
                            var104 = "<##FFD773>Unmarked &f" + var70 + "<##FFD773>'s soul.";
                        } else {
                            var104 = "&7" + var70 + "'s soul was not marked.";
                        }

                        this.msg(var1, var104, 372431639);
                    }

                    return true;
                }
            } else {
                String var91 = "victim";
                String var103 = "Mark a soul for the Gold Gem to harvest.";
                String var106 = "add <player>";
                String var9 = "Mark that player's soul";
                CommandHelp.Entry var107 = CommandHelp.of(var106, var9, 1959232616);
                String var109 = "remove <player>";
                String var10 = "Unmark that player's soul";
                CommandHelp.Entry var110 = CommandHelp.of(var109, var10, 1959232616);
                List var108 = List.of(var107, var110);
                CommandHelp.show(var1, var91, var103, var108, 696249826);
                return true;
            }
        } else {
            String var7 = "&cOnly the Gold Gem's holder may command souls.";
            this.msg(var1, var7, 372431639);
            return true;
        }
    }

    private void msg(CommandSender var1, String var2, int var3) {
        Component var6 = Text.legacy(var2, 373192400);
        var1.sendMessage(var6);
    }

    public List<String> onTabComplete(CommandSender var1, Command var2, String var3, String[] var4) {
        if (var4.length == 1) {
            String[] var36 = new String[2];
            String var9 = "add";
            var36[0] = var9;
            String var44 = "remove";
            var36[1] = var44;
            List var37 = Arrays.asList(var36);
            String var40 = var4[0];
            return this.filter(var37, var40, 567133740);
        } else if (var4.length != 2) {
            return List.of();
        } else {
            ArrayList var17 = new ArrayList();
            ArrayList var10 = var17;

            for (Player var12 : Bukkit.getOnlinePlayers()) {
                String var33 = var12.getName();
                var10.add(var33);
            }

            String var38 = var4[1];
            return this.filter(var10, var38, 567133740);
        }
    }

    private List<String> filter(List<String> var1, String var2, int var3) {
        ArrayList var4 = new ArrayList();
        ArrayList var5 = var4;

        for (String var7 : var1) {
            String var17 = var7.toLowerCase();
            String var22 = var2.toLowerCase();
            if (var17.startsWith(var22)) {
                var5.add(var7);
            }
        }

        return var5;
    }
}
