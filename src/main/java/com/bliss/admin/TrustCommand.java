package com.bliss.admin;

import com.bliss.core.TrustStore;
import com.bliss.villager.Text;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public final class TrustCommand implements CommandExecutor, TabCompleter {
    private final TrustStore trust;

    public TrustCommand(TrustStore var1, int var2) {
        this.trust = var1;
    }

    public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
        if (var1 instanceof Player var9) {
            if (var4.length >= 1 && var4[0].equalsIgnoreCase("list")) {
                TrustStore var24 = this.trust;
                UUID var121 = var9.getUniqueId();
                Set<UUID> var10 = var24.trusted(var121, 778401367);
                if (var10.isEmpty()) {
                    Component var123 = Text.legacy("\ud83d\udd2e &7You haven't trusted anyone.", 373192400);
                    var9.sendMessage(var123);
                    return true;
                } else {
                    ArrayList var30 = new ArrayList();
                    ArrayList var11 = var30;

                    for (UUID var13 : var10) {
                        String var14 = Bukkit.getOfflinePlayer(var13).getName();
                        String var130;
                        if (var14 == null) {
                            var130 = var13.toString();
                        } else {
                            var130 = var14;
                        }

                        var11.add(var130);
                    }

                    Comparator var124 = String.CASE_INSENSITIVE_ORDER;
                    var11.sort(var124);
                    int var126 = var11.size();
                    String var7 = "&7, &b";
                    String var187 = String.join(var7, var11);
                    Component var128 = Text.legacy("\ud83d\udd2e &aYou trust &b" + var126 + "&a: &b" + var187, 373192400);
                    var9.sendMessage(var128);
                    return true;
                }
            } else if (var4.length >= 1 && var4[0].equalsIgnoreCase("clear")) {
                TrustStore var52 = this.trust;
                UUID var136 = var9.getUniqueId();
                int var203 = var52.clearTrust(var136, 430903439);
                if (var203 > 0) {
                    this.trust.save(751174642);
                }

                Component var141;
                if (var203 == 0) {
                    var141 = Text.legacy("\ud83d\udd2e &7You haven't trusted anyone.", 373192400);
                } else {
                    String var189;
                    if (var203 == 1) {
                        var189 = "";
                    } else {
                        var189 = "s";
                    }

                    var141 = Text.legacy(
                        "\ud83d\udd2e &aUn-trusted everyone — &b" + var203 + " &aplayer" + var189 + ". Your abilities can hurt them again.", 373192400
                    );
                }

                var9.sendMessage(var141);
                return true;
            } else if (var4.length < 2) {
                trustUsage(var1, 1127608232);
                return true;
            } else {
                org.bukkit.OfflinePlayer var204 = Bukkit.getPlayerExact(var4[1]);
                if (var204 == null) {
                    var204 = Bukkit.getOfflinePlayerIfCached(var4[1]);
                }

                if (var204 == null) {
                    Component var146 = Text.legacy("&cUnknown player.", 373192400);
                    var1.sendMessage(var146);
                    return true;
                } else if (!var204.hasPlayedBefore() && !var204.isOnline()) {
                    Component var184 = Text.legacy("&cUnknown player.", 373192400);
                    var1.sendMessage(var184);
                    return true;
                } else if (var4[0].equalsIgnoreCase("add")) {
                    TrustStore var81 = this.trust;
                    UUID var150 = var9.getUniqueId();
                    UUID var191 = var204.getUniqueId();
                    if (var81.isTrusted(var150, var191, 1736364606)) {
                        Component var154 = Text.legacy("\ud83d\udd2e &cYou've already trusted &b" + var4[1] + "&c!", 373192400);
                        var9.sendMessage(var154);
                        return true;
                    } else {
                        TrustStore var86 = this.trust;
                        UUID var156 = var9.getUniqueId();
                        UUID var194 = var204.getUniqueId();
                        var86.addTrust(var156, var194, 96125792);
                        Component var160 = Text.legacy("\ud83d\udd2e &aTrusted &b" + var4[1], 373192400);
                        var9.sendMessage(var160);
                        if (var204.isOnline()) {
                            Player var92 = var204.getPlayer();
                            Component var164 = Text.legacy("\ud83d\udd2e &b" + var9.getName() + " &aTrusted You", 373192400);
                            var92.sendMessage(var164);
                        }

                        return true;
                    }
                } else if (var4[0].equalsIgnoreCase("remove")) {
                    UUID var98 = var204.getUniqueId();
                    UUID var168 = var9.getUniqueId();
                    if (var98.equals(var168)) {
                        Component var170 = Text.legacy("&cYou can't un-trust yourself!", 373192400);
                        var9.sendMessage(var170);
                        return true;
                    } else {
                        TrustStore var103 = this.trust;
                        UUID var172 = var9.getUniqueId();
                        UUID var197 = var204.getUniqueId();
                        if (!var103.isTrusted(var172, var197, 1736364606)) {
                            Component var176 = Text.legacy("\ud83d\udd2e &b" + var4[1] + " &cis not trusted!", 373192400);
                            var9.sendMessage(var176);
                            return true;
                        } else {
                            TrustStore var108 = this.trust;
                            UUID var178 = var9.getUniqueId();
                            UUID var200 = var204.getUniqueId();
                            var108.removeTrust(var178, var200, 1892668838);
                            Component var182 = Text.legacy("\ud83d\udd2e &aUntrusted &b" + var4[1], 373192400);
                            var9.sendMessage(var182);
                            return true;
                        }
                    }
                } else {
                    trustUsage(var1, 1127608232);
                    return true;
                }
            }
        } else {
            Component var186 = Text.legacy("&cPlayer only.", 373192400);
            var1.sendMessage(var186);
            return true;
        }
    }

    private static void trustUsage(CommandSender var0, int var1) {
        String var3 = "trust";
        String var4 = "Stop your gem abilities harming an ally.";
        String var5 = "add <player>";
        String var6 = "Trust a player — your abilities spare them";
        CommandHelp.Entry var10 = CommandHelp.of(var5, var6, 1959232616);
        String var12 = "remove <player>";
        String var7 = "Un-trust a player";
        CommandHelp.Entry var13 = CommandHelp.of(var12, var7, 1959232616);
        String var14 = "list";
        String var8 = "Everyone you trust";
        CommandHelp.Entry var15 = CommandHelp.of(var14, var8, 1959232616);
        String var16 = "clear";
        String var9 = "Un-trust everyone at once";
        CommandHelp.Entry var17 = CommandHelp.of(var16, var9, 1959232616);
        List var11 = List.of(var10, var13, var15, var17);
        CommandHelp.show(var0, var3, var4, var11, 696249826);
    }

    public List<String> onTabComplete(CommandSender var1, Command var2, String var3, String[] var4) {
        ArrayList var5 = new ArrayList();
        ArrayList var8 = var5;
        if (var4.length == 1) {
            String var13 = "add";
            String var48 = "remove";
            String var6 = "list";
            String var7 = "clear";

            for (String var10 : List.of(var13, var48, var6, var7)) {
                String var51 = var4[0].toLowerCase();
                if (var10.startsWith(var51)) {
                    var8.add(var10);
                }
            }
        } else if (var4.length == 2 && !var4[0].equalsIgnoreCase("list") && !var4[0].equalsIgnoreCase("clear")) {
            for (Player var66 : Bukkit.getOnlinePlayers()) {
                String var43 = var66.getName().toLowerCase();
                String var60 = var4[1].toLowerCase();
                if (var43.startsWith(var60)) {
                    String var62 = var66.getName();
                    var8.add(var62);
                }
            }
        }

        return var8;
    }
}
