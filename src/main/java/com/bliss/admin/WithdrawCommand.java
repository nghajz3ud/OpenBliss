package com.bliss.admin;

import com.bliss.core.GlobalState;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public final class WithdrawCommand implements CommandExecutor, TabCompleter {
    private final PlayerDataStore players;
    private final BlissCoreItems items;

    public WithdrawCommand(PlayerDataStore var1, BlissCoreItems var2, int var3) {
        this.players = var1;
        this.items = var2;
    }

    public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
        if (var1 instanceof Player var10) {
            if (GlobalState.forPlayer(var10, 1433087673).noEnergyMode(340448998)) {
                Component var117 = Text.legacy("&cThe energy system is disabled on this server.", 373192400);
                var10.sendMessage(var117);
                return true;
            } else if (var4.length < 1) {
                String var115 = "withdraw";
                String var122 = "Withdraw energy from your gem as Bottled Energy.";
                String var124 = "<1-9>";
                String var129 = "How many energy to bottle";
                CommandHelp.Entry var125 = CommandHelp.of(var124, var129, 1959232616);
                CommandHelp.show(var10, var115, var122, var125, 1122560938);
                return true;
            } else {
                int var11;
                try {
                    var11 = Integer.parseInt(var4[0]);
                } catch (NumberFormatException var134) {
                    Component var89 = Text.legacy("&cAmount must be a number.", 373192400);
                    var10.sendMessage(var89);
                    return true;
                }

                if (var11 < 1) {
                    Component var114 = Text.legacy("&cAmount must be at least 1.", 373192400);
                    var10.sendMessage(var114);
                    return true;
                } else {
                    PlayerDataStore var33 = this.players;
                    UUID var92 = var10.getUniqueId();
                    PlayerData var130 = var33.get(var92, 1264010596);
                    int var36 = var130.energy(1160853318);
                    int var13 = var36 - var11;
                    if (var13 < 2) {
                        Component var96 = Text.legacy("&c You Are Not &b&lOverflowing", 373192400);
                        var10.sendMessage(var96);
                        return true;
                    } else {
                        var130.setEnergy(var13, 319922676);
                        BlissCoreItems var43 = this.items;
                        var43.refreshHeldGemItem(var10, var130, 835235475);
                        if (var11 > 2) {
                            ItemStack var131 = this.items.bottledEnergy(1532783840);
                            var131.setAmount(var11);
                            PlayerInventory var67 = var10.getInventory();
                            ItemStack[] var110 = new ItemStack[]{var131};

                            for (ItemStack var133 : var67.addItem(var110).values()) {
                                World var77 = var10.getWorld();
                                Location var112 = var10.getLocation();
                                var77.dropItemNaturally(var112, var133);
                            }
                        } else {
                            for (int var14 = 0; var14 < var11; var14++) {
                                PlayerInventory var48 = var10.getInventory();
                                ItemStack[] var102 = new ItemStack[1];
                                ItemStack var127 = this.items.bottledEnergyUnique(481803223);
                                var102[0] = var127;

                                for (ItemStack var16 : var48.addItem(var102).values()) {
                                    World var58 = var10.getWorld();
                                    Location var104 = var10.getLocation();
                                    var58.dropItemNaturally(var104, var16);
                                }
                            }
                        }

                        Component var107 = Text.legacy("<##FFD773>\ud83d\udd2e Bottled &l&f" + var11 + " <##96FFD9>&lᴇɴᴇʀɢʏ", 373192400);
                        var10.sendMessage(var107);
                        return true;
                    }
                }
            }
        } else {
            Component var119 = Text.legacy("&cPlayer only.", 373192400);
            var1.sendMessage(var119);
            return true;
        }
    }

    public List<String> onTabComplete(CommandSender var1, Command var2, String var3, String[] var4) {
        ArrayList var5 = new ArrayList();
        ArrayList var7 = var5;
        if (var4.length == 1) {
            for (int var8 = 1; var8 <= 5; var8++) {
                String var9 = Integer.toString(var8);
                String var24 = var4[0];
                if (var9.startsWith(var24)) {
                    var7.add(var9);
                }
            }
        }

        return var7;
    }
}
