package com.bliss.honored;

import com.bliss.BlissPlugin;
import com.bliss.core.TeamManager;
import com.bliss.villager.Text;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

public final class HonoredOneManager {
    private static final String TEAM = "BlissHO";
    private final BlissPlugin plugin;
    private final HonoredOneState state;
    public static final long TRIAL_MILLIS = 1873800000L;

    public HonoredOneManager(BlissPlugin var1, HonoredOneState var2, int var3) {
        this.plugin = var1;
        this.state = var2;
        this.ensureTeam(1280372592);
    }

    private Team ensureTeam(int var1) {
        Scoreboard var5 = Bukkit.getScoreboardManager().getMainScoreboard();
        Team var6 = var5.getTeam("BlissHO");
        if (var6 == null) {
            var6 = var5.registerNewTeam("BlissHO");
        }

        NamedTextColor var19 = NamedTextColor.WHITE;
        var6.color(var19);
        TextComponent var20 = Component.empty();
        var6.prefix(var20);
        TextComponent var21 = Component.empty();
        var6.suffix(var21);
        Option var22 = Option.NAME_TAG_VISIBILITY;
        OptionStatus var4 = OptionStatus.NEVER;
        var6.setOption(var22, var4);
        return var6;
    }

    public long trialMillisLeft(int var1) {
        long var7 = this.state.trialEndsAt(2132707251);
        long var17;
        if (var7 <= 0L) {
            var17 = 0L;
        } else {
            long var5 = System.currentTimeMillis();
            long var21 = var7 - var5;
            var17 = Math.max(0L, var21);
        }

        return var17;
    }

    public void endTrial(int var1) {
        if (this.state.trialEndsAt(2132707251) > 0L) {
            this.state.setTrialEndsAt(0L, 2086351711);
        }
    }

    public UUID current(int var1) {
        return this.state.current(2044055893);
    }

    public boolean isHonoredOne(UUID var1, int var2) {
        HonoredOneState var5 = this.state;
        return var5.isHonoredOne(var1, 220812424);
    }

    public void assign(UUID var1, String var2, int var3) {
        UUID var9 = this.state.current(2044055893);
        if (var9 != null && !var9.equals(var1)) {
            this.clearDisplay(var9, 1806294670);
        }

        HonoredOneState var18 = this.state;
        var18.set(var1, 1196536715);
        HonoredOneState var20 = this.state;
        long var34 = System.currentTimeMillis() + 1873800000L;
        var20.setTrialEndsAt(var34, 2086351711);
        this.applyDisplay(var1, 1775974378);
        if (var2 != null) {
            Bukkit.broadcast(Text.legacy(var2, 373192400));
        }

        Player var10 = Bukkit.getPlayer(var1);
        if (var10 != null) {
            TextComponent var38 = Component.text("✦ Honored One ✦");
            NamedTextColor var6 = NamedTextColor.GOLD;
            Component var39 = var38.color(var6);
            TextComponent var41 = Component.empty();
            Title var40 = Title.title(var39, var41);
            var10.showTitle(var40);
        }
    }

    public void clearHonoredOne(int var1) {
        UUID var4 = this.state.current(2044055893);
        if (var4 != null) {
            this.clearDisplay(var4, 1806294670);
        }

        this.state.clear(1980890918);
        HonoredNameplateTask.despawnAll(430454330);
    }

    public void onHonoredOneKilled(Player var1, Player var2, int var3) {
        if (var2 != null && !var2.equals(var1)) {
            UUID var12 = var2.getUniqueId();
            String var13 = var2.getName();
            String var15 = var1.getName();
            String var14 = "&6✦ &e" + var13 + " &6has claimed the title of &l&eHonored One&6 from &e" + var15 + "&6!";
            this.assign(var12, var14, 209174169);
        }
    }

    public void refresh(Player var1, int var2) {
        UUID var7 = var1.getUniqueId();
        if (this.isHonoredOne(var7, 1908342269)) {
            UUID var9 = var1.getUniqueId();
            this.applyDisplay(var9, 1775974378);
        }
    }

    public void refreshAll(int var1) {
        for (Player var5 : Bukkit.getOnlinePlayers()) {
            this.refresh(var5, 1695293367);
        }
    }

    private void applyDisplay(UUID var1, int var2) {
        String var5 = this.nameOf(var1, 786130732);
        if (var5 != null) {
            Team var9 = this.ensureTeam(1280372592);
            var9.addEntry(var5);
        }
    }

    private void clearDisplay(UUID var1, int var2) {
        String var5 = this.nameOf(var1, 786130732);
        if (var5 != null) {
            Team var12 = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("BlissHO");
            TeamManager.safeRemoveEntry(var12, var5, 1412413626);
        }
    }

    private String nameOf(UUID var1, int var2) {
        Player var4 = Bukkit.getPlayer(var1);
        return var4 != null ? var4.getName() : Bukkit.getOfflinePlayer(var1).getName();
    }
}
