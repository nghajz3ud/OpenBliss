package com.bliss.admin;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public final class PvpCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
        if (var4.length < 1) {
            pvpUsage(var1, 1000386521);
            return true;
        } else {
            boolean var64;
            if (var4[0].equalsIgnoreCase("on")) {
                var64 = true;
            } else {
                if (!var4[0].equalsIgnoreCase("off")) {
                    pvpUsage(var1, 1000386521);
                    return true;
                }

                var64 = false;
            }

            for (World var31 : Bukkit.getWorlds()) {
                var31.setPVP(var64);
            }

            String var52;
            if (var64) {
                var52 = "enabled";
            } else {
                var52 = "disabled";
            }

            TextComponent var35 = Component.text("PvP has been " + var52);
            TextComponent var56 = Component.empty();
            Duration var60 = Duration.ofSeconds(1L);
            Duration var62 = Duration.ofSeconds(5L);
            Duration var63 = Duration.ofSeconds(1L);
            Times var61 = Times.times(var60, var62, var63);
            Title var66 = Title.title(var35, var56, var61);

            for (Player var44 : Bukkit.getOnlinePlayers()) {
                var44.showTitle(var66);
            }

            return true;
        }
    }

    private static void pvpUsage(CommandSender var0, int var1) {
        String var3 = "pvp";
        String var4 = "Toggle PvP server-wide.";
        String var5 = "on";
        String var6 = "Allow players to damage each other";
        CommandHelp.Entry var8 = CommandHelp.of(var5, var6, 1959232616);
        String var10 = "off";
        String var7 = "Block all player-versus-player damage";
        CommandHelp.Entry var11 = CommandHelp.of(var10, var7, 1959232616);
        List var9 = List.of(var8, var11);
        CommandHelp.show(var0, var3, var4, var9, 696249826);
    }

    public List<String> onTabComplete(CommandSender var1, Command var2, String var3, String[] var4) {
        ArrayList var5 = new ArrayList();
        ArrayList var7 = var5;
        if (var4.length == 1) {
            for (String var9 : List.of("on", "off")) {
                String var29 = var4[0].toLowerCase();
                if (var9.startsWith(var29)) {
                    var7.add(var9);
                }
            }
        }

        return var7;
    }
}
