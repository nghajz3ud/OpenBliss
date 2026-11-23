package com.bliss.dragongames;

import com.bliss.BlissPlugin;
import com.bliss.admin.CommandHelp;
import com.bliss.core.GlobalState;
import com.bliss.villager.Text;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.kyori.adventure.text.BuildableComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.ClickCallback.Options;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class DragonGamesInvite implements CommandExecutor, TabCompleter {
    static final long ASK_AFTER_TICKS = 72000L;
    static final long RETRY_TICKS = 24000L;
    private static final long TICK_PERIOD = 20L;
    private static final Duration BUTTON_LIFETIME = Duration.ofHours(24L);
    private final BlissPlugin plugin;
    private final DragonGamesManager games;
    private BukkitTask task;
    private int questionGen;

    public DragonGamesInvite(BlissPlugin var1, DragonGamesManager var2, int var3) {
        this.plugin = var1;
        this.games = var2;
    }

    public void start(int var1) {
        BukkitScheduler var3 = Bukkit.getScheduler();
        BlissPlugin var11 = this.plugin;
        Runnable var12 = this::tick;
        BukkitTask var10 = var3.runTaskTimer(var11, var12, 20L, 20L);
        this.task = var10;
    }

    public void stop(int var1) {
        if (this.task != null) {
            this.task.cancel();
        }
    }

    private void tick() {
        Player var7 = this.currentHolder(681345032);
        long var8 = ((World)Bukkit.getWorlds().get(0)).getGameTime();
        if (this.games.eggHeldSince(1559404593) < 0L) {
            if (var7 != null) {
                DragonGamesManager var26 = this.games;
                var26.setEggHeldSince(var8, 1744034005);
            }
        } else if (!this.games.holderStartUsed(856197641) && !this.games.isRunning(320515049)) {
            if (!this.games.promptDeclined(875940165) && var7 != null) {
                GlobalState var10 = this.plugin.globalState(724248306);
                if (var10 != null && var10.stage(1483650410) >= 3) {
                    if (var10.dragonGamesHolderStart(437420920)) {
                        long var62;
                        if (this.games.nextAskAt(1526409795) >= 0L) {
                            var62 = this.games.nextAskAt(1526409795);
                        } else {
                            var62 = this.games.eggHeldSince(1559404593) + 72000L;
                        }

                        if (var8 >= var62) {
                            DragonGamesManager var56 = this.games;
                            long var69 = var8 + 24000L;
                            var56.setNextAskAt(var69, 439613958);
                            this.ask(var7, 1034187277);
                        }
                    }
                }
            }
        }
    }

    private Player currentHolder(int var1) {
        GlobalState var3 = this.plugin.globalState(724248306);
        if (var3 != null && var3.dragonEggOwner(396132898) != null) {
            Player var4 = Bukkit.getPlayer(var3.dragonEggOwner(396132898));
            if (var4 != null && GlobalState.isHoldingDragonEgg(var4, 2044938455)) {
                return var4;
            }
        }

        for (Player var5 : Bukkit.getOnlinePlayers()) {
            if (GlobalState.isHoldingDragonEgg(var5, 2044938455)) {
                return var5;
            }
        }

        return null;
    }

    private void ask(Player var1, int var2) {
        int var16 = this.questionGen + 1;
        this.questionGen = var16;
        UUID var10 = var1.getUniqueId();
        Component var18 = Text.legacy("&5Do you wish to start the &d&lDragon Games&5?", 373192400);
        var1.sendMessage(var18);
        Builder var19 = Component.text();
        String var5 = "[Yes]";
        NamedTextColor var7 = NamedTextColor.GREEN;
        String var8 = "Start the Dragon Games now";
        DragonGamesInvite.Answer var9 = DragonGamesInvite.Answer.YES;
        Component var32 = this.button(var5, var10, var7, var8, var9, 1296870139);
        Builder var21 = (Builder)var19.append(var32);
        TextComponent var34 = Component.text("   ");
        Builder var23 = (Builder)var21.append(var34);
        String var41 = "[Not yet]";
        NamedTextColor var45 = NamedTextColor.YELLOW;
        String var47 = "Ask me again tomorrow";
        DragonGamesInvite.Answer var49 = DragonGamesInvite.Answer.NOT_YET;
        Component var36 = this.button(var41, var10, var45, var47, var49, 1296870139);
        Builder var25 = (Builder)var23.append(var36);
        TextComponent var38 = Component.text("   ");
        Builder var27 = (Builder)var25.append(var38);
        String var42 = "[No]";
        NamedTextColor var46 = NamedTextColor.RED;
        String var48 = "Stop asking - /dragon games start still works, once";
        DragonGamesInvite.Answer var50 = DragonGamesInvite.Answer.NO;
        Component var40 = this.button(var42, var10, var46, var48, var50, 1296870139);
        BuildableComponent var30 = ((Builder)var27.append(var40)).build();
        var1.sendMessage(var30);
    }

    private Component button(String var1, UUID var2, NamedTextColor var3, String var4, DragonGamesInvite.Answer var5, int var6) {
        int var11 = this.questionGen;
        ClickCallback var12 = var4x -> {
            if (var4x instanceof Player var8) {
                UUID var13 = var8.getUniqueId();
                if (var13.equals(var2)) {
                    int var18 = this.questionGen;
                    if (var11 != var18) {
                        return;
                    }

                    this.answer(var8, var5, 366997230);
                    return;
                }
            }
        };
        TextComponent var17 = Component.text(var1);
        TextComponent var19 = (TextComponent)var17.color(var3);
        TextDecoration var28 = TextDecoration.BOLD;
        TextComponent var21 = (TextComponent)var19.decoration(var28, true);
        TextDecoration var29 = TextDecoration.ITALIC;
        TextComponent var23 = (TextComponent)var21.decoration(var29, false);
        net.kyori.adventure.text.event.ClickCallback.Options.Builder var39 = Options.builder().uses(1);
        Duration var45 = BUTTON_LIFETIME;
        Options var42 = (Options)var39.lifetime(var45).build();
        ClickEvent var31 = ClickEvent.callback(var12, var42);
        TextComponent var25 = (TextComponent)var23.clickEvent(var31);
        TextComponent var33 = Component.text(var4);
        NamedTextColor var43 = NamedTextColor.GRAY;
        HoverEvent var35 = HoverEvent.showText(var33.color(var43));
        return var25.hoverEvent(var35);
    }

    private void answer(Player var1, DragonGamesInvite.Answer var2, int var3) {
        int var14 = this.questionGen + 1;
        this.questionGen = var14;
        switch (var2) {
            case YES:
                this.tryHolderStart(var1, 584091959);
                break;
            case NOT_YET:
                Component var19 = Text.legacy("&7Alright, I'll ask again tomorrow. You can also start them any time with &f/dragon games start&7.", 373192400);
                var1.sendMessage(var19);
                break;
            case NO:
                this.games.setPromptDeclined(true, 1452060673);
                Component var17 = Text.legacy("&7Understood, I won't ask again. &f/dragon games start &7still works - once.", 373192400);
                var1.sendMessage(var17);
        }
    }

    private void tryHolderStart(Player var1, int var2) {
        if (!GlobalState.isHoldingDragonEgg(var1, 2044938455)) {
            Component var30 = Text.legacy("&cOnly the Dragon Egg holder can start the Dragon Games.", 373192400);
            var1.sendMessage(var30);
        } else {
            GlobalState var5 = this.plugin.globalState(724248306);
            if (var5 == null || var5.stage(1483650410) < 3) {
                Component var40 = Text.legacy("&cThe Dragon Games open at Progression 3.", 373192400);
                var1.sendMessage(var40);
            } else if (!var5.dragonGamesHolderStart(437420920)) {
                Component var33 = Text.legacy("&cOn this server the Dragon Games are started by the admins.", 373192400);
                var1.sendMessage(var33);
            } else if (this.games.holderStartUsed(856197641)) {
                Component var35 = Text.legacy("&cThe Dragon Games have already been held on this server.", 373192400);
                var1.sendMessage(var35);
            } else if (this.games.isRunning(320515049)) {
                Component var37 = Text.legacy("&cThe Dragon Games are already running.", 373192400);
                var1.sendMessage(var37);
            } else {
                this.games.setHolderStartUsed(true, 598817900);
                this.games.startGame(277176602);
            }
        }
    }

    public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
        if (var1 instanceof Player var10) {
            if (var4.length == 2 && var4[0].equalsIgnoreCase("games") && var4[1].equalsIgnoreCase("start")) {
                this.tryHolderStart(var10, 584091959);
                return true;
            } else {
                String var33 = "dragon";
                String var7 = "The Dragon Egg holder's one command.";
                String var8 = "games start";
                String var9 = "Start the Dragon Games - once, ever";
                CommandHelp.Entry var36 = CommandHelp.of(var8, var9, 1959232616);
                CommandHelp.show(var1, var33, var7, var36, 1122560938);
                return true;
            }
        } else {
            Component var35 = Text.legacy("&cPlayer only.", 373192400);
            var1.sendMessage(var35);
            return true;
        }
    }

    public List<String> onTabComplete(CommandSender var1, Command var2, String var3, String[] var4) {
        ArrayList var5 = new ArrayList();
        if (var4.length == 1) {
            String var10 = "games";
            String var27 = var4[0].toLowerCase();
            if (var10.startsWith(var27)) {
                var5.add("games");
                return var5;
            }
        }

        if (var4.length == 2 && var4[0].equalsIgnoreCase("games")) {
            String var20 = "start";
            String var34 = var4[1].toLowerCase();
            if (var20.startsWith(var34)) {
                var5.add("start");
            }
        }

        return var5;
    }

    private static enum Answer {
        YES,
        NOT_YET,
        NO;
    }
}
