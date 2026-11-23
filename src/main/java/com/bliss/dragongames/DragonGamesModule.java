package com.bliss.dragongames;

import com.bliss.BlissPlugin;
import com.bliss.core.DragonGamesHook;
import com.bliss.items.BlissCoreItems;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;

public final class DragonGamesModule implements DragonGamesHook {
    private final DragonGamesManager games;
    private final DragonGamesCommand command;
    private final DragonGamesInvite invite;
    private final BloodMoon bloodMoon;
    private final DragonGamesShop shop;

    private DragonGamesModule(DragonGamesManager var1, DragonGamesInvite var2, BloodMoon var3, DragonGamesShop var4, int var5) {
        this.games = var1;
        DragonGamesCommand var15 = new DragonGamesCommand(var1, var4, 789521458);
        this.command = var15;
        this.invite = var2;
        this.bloodMoon = var3;
        this.shop = var4;
    }

    public static DragonGamesHook install(BlissPlugin var0) {
        DragonGamesManager var1 = new DragonGamesManager(var0, 1214704783);
        var1.load(1288166836);
        PluginManager var14 = var0.getServer().getPluginManager();
        DragonGamesListener var44 = new DragonGamesListener(var0, var1, 1008237209);
        var14.registerEvents(var44, var0);
        BlissCoreItems var73 = var0.coreItems(401009135);
        DragonGamesShop var15 = new DragonGamesShop(var0, var1, var73, 5879265);
        PluginManager var18 = var0.getServer().getPluginManager();
        var18.registerEvents(var15, var0);
        if (var0.getCommand("shop") != null) {
            PluginCommand var22 = var0.getCommand("shop");
            var22.setExecutor(var15);
        }

        DragonGamesInvite var23 = new DragonGamesInvite(var0, var1, 1447284294);
        if (var0.getCommand("dragon") != null) {
            PluginCommand var27 = var0.getCommand("dragon");
            var27.setExecutor(var23);
            PluginCommand var29 = var0.getCommand("dragon");
            var29.setTabCompleter(var23);
        }

        BloodMoon var30 = new BloodMoon(var0, var1, 2099252080);
        PluginManager var33 = var0.getServer().getPluginManager();
        var33.registerEvents(var30, var0);
        var1.setBloodMoon(var30, 2008500538);
        DragonGamesLeaderboard var35 = new DragonGamesLeaderboard(var0, var1, 1342261858);
        PluginManager var38 = var0.getServer().getPluginManager();
        var38.registerEvents(var35, var0);
        var1.setLeaderboard(var35, 1894325325);
        var1.start(213468661);
        var23.start(1714147714);
        var30.resume(448780478);
        return new DragonGamesModule(var1, var23, var30, var15, 918361369);
    }

    public DragonGamesManager games() {
        return this.games;
    }

    @Override
    public void handle(CommandSender var1, String[] var2, int var3) {
        DragonGamesCommand var7 = this.command;
        var7.handle(var1, var2, 1088077131);
    }

    @Override
    public List<String> complete(CommandSender var1, String[] var2, int var3) {
        DragonGamesCommand var7 = this.command;
        return var7.complete(var1, var2, 1878006838);
    }

    @Override
    public boolean isSelfCheck(String[] var1, int var2) {
        return DragonGamesCommand.isSelfCheck(var1, 1567124282);
    }

    @Override
    public void stop(int var1) {
        this.shop.closeAll(1516861526);
        this.bloodMoon.stop(1182673421);
        this.invite.stop(1935768671);
        this.games.stop(737529231);
    }
}
