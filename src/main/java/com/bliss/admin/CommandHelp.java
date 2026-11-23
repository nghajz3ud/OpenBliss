package com.bliss.admin;

import com.bliss.villager.Text;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class CommandHelp {
    private static final String GOLD = "<##FFD773>";
    private static final String RULE = "&8&m                                                            ";

    private CommandHelp() {
    }

    public static CommandHelp.Entry of(String var0, String var1, int var2) {
        return new CommandHelp.Entry(var0, var1);
    }

    public static void show(CommandSender var0, String var1, String var2, List<CommandHelp.Entry> var3, int var4) {
        Component var55 = Text.legacy("&8&m                                                            ", 373192400);
        var0.sendMessage(var55);
        Component var58 = Text.legacy(" <##FFD773>\ud83d\udd2e &lBLISS &r&8· &f/" + var1, 373192400);
        var0.sendMessage(var58);
        if (var2 != null && !var2.isBlank()) {
            Component var61 = Text.legacy(" &8" + var2, 373192400);
            var0.sendMessage(var61);
        }

        TextComponent var62 = Component.empty();
        var0.sendMessage(var62);
        int var10 = 0;

        for (CommandHelp.Entry var12 : var3) {
            int var65 = var12.args().length();
            var10 = Math.max(var10, var65);
        }

        for (CommandHelp.Entry var104 : var3) {
            String var38 = " ";
            int var95 = var104.args().length();
            int var86 = var10 - var95 + 1;
            int var67 = Math.max(1, var86);
            String var13 = var38.repeat(var67);
            String var69 = var104.args();
            String var98 = var104.description();
            Component var42 = Text.legacy(" &8┃ <##FFD773>/" + var1 + " " + var69 + var13 + "&8— &7" + var98, 373192400);
            String var89 = var104.args();
            String var100 = var104.description();
            HoverEvent var73 = HoverEvent.showText(Text.legacy("<##FFD773>/" + var1 + " " + var89 + "\n&7" + var100 + "\n&8click to type it", 373192400));
            Component var43 = var42.hoverEvent(var73);
            String var92 = firstToken(var104.args(), 496957128);
            ClickEvent var76 = ClickEvent.suggestCommand("/" + var1 + " " + var92);
            Component var14 = var43.clickEvent(var76);
            var0.sendMessage(var14);
        }

        if (var3.size() > 1) {
            Component var80 = Text.legacy(" &8▸ hover a line for detail, click it to type it", 373192400);
            var0.sendMessage(var80);
        }

        Component var82 = Text.legacy("&8&m                                                            ", 373192400);
        var0.sendMessage(var82);
        if (var0 instanceof Player var103) {
            Location var84 = var103.getLocation();
            Sound var93 = Sound.ITEM_BOOK_PAGE_TURN;
            var103.playSound(var84, var93, 0.7F, 1.4F);
        }
    }

    public static void show(CommandSender var0, String var1, String var2, CommandHelp.Entry var3, int var4) {
        List var9 = List.of(var3);
        show(var0, var1, var2, var9, 696249826);
    }

    public static void hint(CommandSender var0, String var1, int var2) {
        String var13;
        if (var1.startsWith("/")) {
            var13 = var1.substring(1);
        } else {
            var13 = var1;
        }

        int var7 = var13.length();
        int var8 = var13.indexOf(60);
        int var9 = var13.indexOf(91);
        if (var8 >= 0) {
            var7 = var8;
        }

        if (var9 >= 0 && var9 < var7) {
            var7 = var9;
        }

        String var27 = var13.substring(0, var7).stripTrailing();
        int var43 = var13.length();
        String var41;
        if (var7 < var43) {
            var41 = " ";
        } else {
            var41 = "";
        }

        String var10 = "/" + var27 + var41;
        Component var38 = Text.legacy(" &8┃ <##FFD773>/" + var13, 373192400);
        HoverEvent var46 = HoverEvent.showText(Text.legacy("&7click to type it", 373192400));
        Component var39 = var38.hoverEvent(var46);
        ClickEvent var48 = ClickEvent.suggestCommand(var10);
        Component var40 = var39.clickEvent(var48);
        var0.sendMessage(var40);
    }

    private static String firstToken(String var0, int var1) {
        String var3 = " ";
        String var5 = var0.split(var3, 2)[0];
        String var12;
        if (!var5.startsWith("<") && !var5.startsWith("[")) {
            var12 = var5;
        } else {
            var12 = "";
        }

        return var12;
    }

    public static record Entry(String args, String description) {
    }
}
