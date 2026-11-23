package com.bliss.mythics;

import com.bliss.BlissPlugin;
import com.bliss.admin.MenuListener;
import com.bliss.core.TrustStore;
import com.bliss.honored.JuggernautGemGuardListener;
import com.bliss.mythics.aegis.AngelsGrasp;
import com.bliss.mythics.aegis.AngelsHang;
import com.bliss.mythics.aegis.MythicAegisAuraTask;
import com.bliss.mythics.aegis.MythicAegisListener;
import com.bliss.mythics.aegis.MythicAegisState;
import com.bliss.mythics.blood.BloodRitualTask;
import com.bliss.mythics.blood.MythicBloodAuraTask;
import com.bliss.mythics.blood.MythicBloodListener;
import com.bliss.mythics.blood.MythicBloodState;
import com.bliss.mythics.juggernaut.JuggernautCommand;
import com.bliss.mythics.juggernaut.JuggernautKitEditor;
import com.bliss.mythics.juggernaut.JuggernautListener;
import com.bliss.mythics.juggernaut.JuggernautManager;
import com.bliss.mythics.juggernaut.PedestalGemDropListener;
import com.bliss.mythics.juggernaut.PedestalRevivalRitual;
import java.util.Iterator;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;

public final class MythicsModule {
    private final BlissPlugin plugin;
    private MythicBloodState bloodState;
    private MythicAegisState aegisState;
    private JuggernautManager juggernauts;
    private JuggernautKitEditor kitEditor;

    public MythicsModule(BlissPlugin var1, int var2) {
        this.plugin = var1;
    }

    public void enable(int var1) {
        TrustStore var7 = this.plugin.trust(310732214);
        PluginManager var8 = this.plugin.getServer().getPluginManager();
        AngelsGrasp.init(this.plugin, 1776446919);
        MythicBloodState var3 = new MythicBloodState();
        this.bloodState = var3;
        BlissPlugin var128 = this.plugin;
        MythicBloodState var149 = this.bloodState;
        MythicBloodListener var82 = new MythicBloodListener(var128, var149, var7, 697004919);
        BlissPlugin var107 = this.plugin;
        var8.registerEvents(var82, var107);
        BlissPlugin var109 = this.plugin;
        MythicBloodState var130 = this.bloodState;
        MythicBloodAuraTask var26 = new MythicBloodAuraTask(var109, var130, 1380485597);
        var26.start(370164792);
        MythicAegisState var83 = new MythicAegisState();
        this.aegisState = var83;
        BlissPlugin var132 = this.plugin;
        MythicAegisState var151 = this.aegisState;
        MythicAegisListener var84 = new MythicAegisListener(var132, var151, var7, 201320304);
        BlissPlugin var111 = this.plugin;
        var8.registerEvents(var84, var111);
        BlissPlugin var113 = this.plugin;
        MythicAegisState var134 = this.aegisState;
        MythicAegisAuraTask var29 = new MythicAegisAuraTask(var113, var134, 15911125);
        var29.start(394423730);
        MythicItemGuardListener var85 = new MythicItemGuardListener();
        BlissPlugin var115 = this.plugin;
        var8.registerEvents(var85, var115);
        String var31 = "mythic_heretic";
        ItemStack var86 = MythicItems.bloodGem(1434502583);
        Material var116 = Material.RED_STAINED_GLASS_PANE;
        Consumer<Player> var135 = var0 -> {
            ItemStack var2 = MythicItems.bloodGem(1434502583);
            give(var0, var2, 1976099698);
        };
        MenuListener.registerExternal(var31, var86, var116, var135, 153575556);
        String var32 = "mythic_auratus";
        ItemStack var87 = MythicItems.aegisGem(1323939072);
        Material var117 = Material.YELLOW_STAINED_GLASS_PANE;
        Consumer<Player> var136 = var0 -> {
            ItemStack var2 = MythicItems.aegisGem(1323939072);
            give(var0, var2, 1976099698);
        };
        MenuListener.registerExternal(var32, var87, var117, var136, 153575556);
        MythicsCommand var33 = new MythicsCommand();
        BlissPlugin var119 = this.plugin;
        var8.registerEvents(var33, var119);
        PluginCommand var10 = this.plugin.getCommand("mythics");
        if (var10 != null) {
            var10.setExecutor(var33);
        }

        PluginCommand var11 = this.plugin.getCommand("cooldown");
        if (var11 != null) {
            MythicBloodState var138 = this.bloodState;
            MythicAegisState var153 = this.aegisState;
            CooldownCommand var92 = new CooldownCommand(var138, var153, 710529122);
            var11.setExecutor(var92);
        }

        boolean var81;
        if (this.plugin.globalState(724248306) != null && !this.plugin.globalState(724248306).juggernautRole(1687899048)) {
            var81 = false;
        } else {
            var81 = true;
        }

        if (var81) {
            BlissPlugin var140 = this.plugin;
            JuggernautManager var93 = new JuggernautManager(var140, 599973714);
            this.juggernauts = var93;
            UUID var13 = this.juggernauts.revivalInFlight(425699207);
            if (var13 != null) {
                this.juggernauts.clearRevivalInFlight(1745092150);
                JuggernautManager var61 = this.juggernauts;
                if (var61.revive(var13, 823653800)) {
                    this.plugin.getLogger().info("Recovered a crash-interrupted pedestal revival.");
                }
            }

            BlissPlugin var142 = this.plugin;
            JuggernautManager var155 = this.juggernauts;
            JuggernautListener var96 = new JuggernautListener(var142, var155, 1966293873);
            BlissPlugin var121 = this.plugin;
            var8.registerEvents(var96, var121);
            BlissPlugin var144 = this.plugin;
            BlissPlugin var157 = this.plugin;
            JuggernautManager var162 = this.juggernauts;
            PedestalGemDropListener var97 = new PedestalGemDropListener(var144, var157, var162, 1360594496);
            BlissPlugin var123 = this.plugin;
            var8.registerEvents(var97, var123);
            BlissPlugin var146 = this.plugin;
            JuggernautManager var159 = this.juggernauts;
            JuggernautKitEditor var98 = new JuggernautKitEditor(var146, var159, 1492976587);
            this.kitEditor = var98;
            JuggernautKitEditor var100 = this.kitEditor;
            BlissPlugin var125 = this.plugin;
            var8.registerEvents(var100, var125);
            JuggernautManager var127 = this.juggernauts;
            JuggernautKitEditor var148 = this.kitEditor;
            JuggernautCommand var70 = new JuggernautCommand(var127, var148, 1811029011);
            PluginCommand var15 = this.plugin.getCommand("juggernaut");
            if (var15 != null) {
                var15.setExecutor(var70);
                var15.setTabCompleter(var70);
            }
        }

        JuggernautGemGuardListener.setRoleActive(var81, 480896347);
        Logger var80 = this.plugin.getLogger();
        String var105;
        if (var81) {
            var105 = "Mythic gems enabled (Heretic + Auratus + Juggernaut role).";
        } else {
            var105 = "Mythic gems enabled (Heretic + Auratus; the Juggernaut role is switched off).";
        }

        var80.info(var105);
    }

    private static void give(Player var0, ItemStack var1, int var2) {
        PlayerInventory var9 = var0.getInventory();
        ItemStack[] var21 = new ItemStack[]{var1};

        for (ItemStack var8 : var9.addItem(var21).values()) {
            World var19 = var0.getWorld();
            Location var23 = var0.getLocation();
            var19.dropItemNaturally(var23, var8);
        }
    }

    public void disable(int var1) {
        if (this.kitEditor != null) {
            this.kitEditor.shutdown(1447759135);
        }

        BloodRitualTask.restoreAll(908550784);
        AngelsGrasp.cleanupAll(348793898);
        AngelsHang.releaseAll(1074205534);
        PedestalRevivalRitual.finishActive(689106599);
        Iterator var3 = Bukkit.getOnlinePlayers().iterator();

        while (var3.hasNext()) {
            MythicAegisAuraTask.resetReach((Player)var3.next(), 1923315323);
        }
    }

    public JuggernautManager juggernauts() {
        return this.juggernauts;
    }

    public JuggernautKitEditor kitEditor(int var1) {
        return this.kitEditor;
    }
}
