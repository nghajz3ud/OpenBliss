package com.bliss.goldgem;

import io.papermc.paper.event.player.AsyncChatEvent;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class DreamChannels implements Listener {
    private static boolean dreamSide(Player var0, int var1) {
        boolean var6;
        if (var0 != null && GoldenDreamWorld.isDreamWorld(var0.getWorld())) {
            var6 = true;
        } else {
            var6 = false;
        }

        return var6;
    }

    private static boolean silenced(Component var0, int var1) {
        if (var0 != null) {
            TextComponent var4 = Component.empty();
            if (!var4.equals(var0)) {
                return false;
            }
        }

        return true;
    }

    @EventHandler(
        priority = EventPriority.HIGH
    )
    public void onChat(AsyncChatEvent var1) {
        boolean var4 = dreamSide(var1.getPlayer(), 548821471);
        Set var8 = var1.viewers();
        Predicate var10 = var1x -> {
            if (var1x instanceof Player) {
                boolean var9 = dreamSide((Player)var1x, 548821471);
                if (var9 != var4) {
                    return true;
                }
            }

            return false;
        };
        var8.removeIf(var10);
    }

    @EventHandler(
        priority = EventPriority.MONITOR
    )
    public void onDeath(PlayerDeathEvent var1) {
        Component var4 = var1.deathMessage();
        if (!silenced(var4, 984946640)) {
            boolean var5 = dreamSide(var1.getEntity(), 548821471);
            if (splitFrom(var5, 1217102164)) {
                var1.deathMessage(null);
                sendSide(var5, var4, 1024104737);
                ConsoleCommandSender var16 = Bukkit.getConsoleSender();
                var16.sendMessage(var4);
            }
        }
    }

    @EventHandler(
        priority = EventPriority.MONITOR
    )
    public void onAdvancement(PlayerAdvancementDoneEvent var1) {
        Component var4 = var1.message();
        if (!silenced(var4, 984946640)) {
            boolean var5 = dreamSide(var1.getPlayer(), 548821471);
            if (splitFrom(var5, 1217102164)) {
                var1.message(null);
                sendSide(var5, var4, 1024104737);
                ConsoleCommandSender var16 = Bukkit.getConsoleSender();
                var16.sendMessage(var4);
            }
        }
    }

    @EventHandler(
        priority = EventPriority.MONITOR
    )
    public void onJoin(PlayerJoinEvent var1) {
        Component var4 = var1.joinMessage();
        Consumer<Component> var5 = var1::joinMessage;
        hideFromDream(var4, var5, 24584904);
    }

    @EventHandler(
        priority = EventPriority.MONITOR
    )
    public void onQuit(PlayerQuitEvent var1) {
        Component var4 = var1.quitMessage();
        Consumer<Component> var5 = var1::quitMessage;
        hideFromDream(var4, var5, 24584904);
    }

    private static void hideFromDream(Component var0, Consumer<Component> var1, int var2) {
        if (!silenced(var0, 984946640)) {
            if (anyoneDreaming(86656413)) {
                var1.accept(null);
                sendSide(false, var0, 1024104737);
                ConsoleCommandSender var9 = Bukkit.getConsoleSender();
                var9.sendMessage(var0);
            }
        }
    }

    private static boolean anyoneDreaming(int var0) {
        Iterator var2 = Bukkit.getOnlinePlayers().iterator();

        while (var2.hasNext()) {
            if (dreamSide((Player)var2.next(), 548821471)) {
                return true;
            }
        }

        return false;
    }

    private static boolean splitFrom(boolean var0, int var1) {
        Iterator var4 = Bukkit.getOnlinePlayers().iterator();

        while (var4.hasNext()) {
            boolean var14 = dreamSide((Player)var4.next(), 548821471);
            if (var14 != var0) {
                return true;
            }
        }

        return false;
    }

    public static void sendSide(boolean var0, Component var1, int var2) {
        for (Player var6 : Bukkit.getOnlinePlayers()) {
            boolean var14 = dreamSide(var6, 548821471);
            if (var14 == var0) {
                var6.sendMessage(var1);
            }
        }
    }

    public static boolean sideOf(World var0, int var1) {
        return GoldenDreamWorld.isDreamWorld(var0);
    }
}
