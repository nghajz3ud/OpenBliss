package com.bliss.mythics.juggernaut;

import com.bliss.BlissPlugin;
import com.bliss.mythics.MythicItems;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class PedestalGemDropListener implements Listener {
    private static final double PEDESTAL_RADIUS = 5.0;
    private static final long RITUAL_LOCK_TICKS = 490L;
    private final Plugin plugin;
    private final BlissPlugin bliss;
    private final JuggernautManager mgr;
    private boolean ritualActive = false;

    public PedestalGemDropListener(Plugin var1, BlissPlugin var2, JuggernautManager var3, int var4) {
        this.plugin = var1;
        this.bliss = var2;
        this.mgr = var3;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST,
        ignoreCancelled = true
    )
    public void onDrop(PlayerDropItemEvent var1) {
        Player var11 = var1.getPlayer();
        JuggernautManager var23 = this.mgr;
        if (var23.isJuggernaut(var11, 1070464021)) {
            JuggernautManager.Bond var13 = bondOfGem(var1.getItemDrop().getItemStack(), 542931631);
            if (var13 != null) {
                Location var14 = this.bliss.pedestalState(590646643).mainLoc(15453813);
                if (var14 != null && var14.getWorld() != null) {
                    Location var39 = var11.getLocation();
                    if (near(var39, var14, 617208259)) {
                        JuggernautManager var42 = this.mgr;
                        UUID var15 = var42.downedOfBond(var13, 168736649);
                        if (var15 != null) {
                            if (!this.ritualActive) {
                                this.ritualActive = true;
                                BukkitScheduler var51 = this.plugin.getServer().getScheduler();
                                Plugin var82 = this.plugin;
                                Runnable var93 = () -> this.ritualActive = false;
                                var51.runTaskLater(var82, var93, 490L);
                                Item var16 = var1.getItemDrop();
                                ItemStack var17 = var16.getItemStack();
                                if (var17.getAmount() > 1) {
                                    ItemStack var18 = var17.clone();
                                    int var86 = var17.getAmount() - 1;
                                    var18.setAmount(var86);
                                    var17.setAmount(1);
                                    var16.setItemStack(var17);
                                    PlayerInventory var65 = var11.getInventory();
                                    ItemStack[] var90 = new ItemStack[]{var18};

                                    for (ItemStack var20 : var65.addItem(var90).values()) {
                                        World var76 = var11.getWorld();
                                        Location var92 = var11.getLocation();
                                        var76.dropItemNaturally(var92, var20);
                                    }
                                }

                                Plugin var96 = this.plugin;
                                JuggernautManager var100 = this.mgr;
                                JuggernautManager.Bond var10 = JuggernautManager.Bond.AURATUS;
                                boolean var102;
                                if (var13 == var10) {
                                    var102 = true;
                                } else {
                                    var102 = false;
                                }

                                PedestalRevivalRitual var71 = new PedestalRevivalRitual(var96, var100, var14, var16, var102, var15, 1722372739);
                                var71.start(2089428242);
                            }
                        }
                    }
                }
            }
        }
    }

    private static JuggernautManager.Bond bondOfGem(ItemStack var0, int var1) {
        if (MythicItems.isMythicBloodGem(var0, 824514243)) {
            return JuggernautManager.Bond.HERETIC;
        } else {
            return MythicItems.isMythicAegisGem(var0, 73386651) ? JuggernautManager.Bond.AURATUS : null;
        }
    }

    private static boolean near(Location var0, Location var1, int var2) {
        if (var0.getWorld() != null) {
            World var9 = var0.getWorld();
            World var16 = var1.getWorld();
            if (var9.equals(var16) && var0.distanceSquared(var1) <= 25.0) {
                return true;
            }
        }

        return false;
    }
}
