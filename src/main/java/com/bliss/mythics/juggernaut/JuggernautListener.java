package com.bliss.mythics.juggernaut;

import com.bliss.mythics.MythicItems;
import com.bliss.mythics.util.Text;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class JuggernautListener implements Listener {
    private final Plugin plugin;
    private final JuggernautManager mgr;
    private static final NamespacedKey NETHERITE_ARMOUR_ADV = NamespacedKey.minecraft("story/netherite_armor");

    public JuggernautListener(Plugin var1, JuggernautManager var2, int var3) {
        this.plugin = var1;
        this.mgr = var2;
    }

    private static boolean isMythicGem(ItemStack var0) {
        boolean var5;
        if (!MythicItems.isMythicBloodGem(var0, 824514243) && !MythicItems.isMythicAegisGem(var0, 73386651)) {
            var5 = false;
        } else {
            var5 = true;
        }

        return var5;
    }

    @EventHandler(
        priority = EventPriority.HIGH,
        ignoreCancelled = true
    )
    public void onDeath(PlayerDeathEvent var1) {
        Player var9 = var1.getEntity();
        List<ItemStack> var10 = this.stripMythicGems(var9, 987853302);
        if (!var10.isEmpty()) {
            List<ItemStack> var21 = var1.getDrops();
            Predicate<ItemStack> var58 = JuggernautListener::isMythicGem;
            var21.removeIf(var58);
            Location var11 = var9.getLocation();
            double var26 = var11.getY();
            double var74 = (double)var11.getWorld().getMinHeight();
            if (var26 < var74) {
                World var30 = var11.getWorld();
                var11 = var30.getHighestBlockAt(var11).getLocation().add(0.5, 1.0, 0.5);
            }

            for (ItemStack var13 : var10) {
                World var42 = var11.getWorld();
                Item var14 = var42.dropItemNaturally(var11, var13);
                var14.setInvulnerable(true);
                var14.setWillAge(false);
            }
        }

        JuggernautManager var47 = this.mgr;
        if (var47.isJuggernaut(var9, 1070464021)) {
            JuggernautManager var50 = this.mgr;
            if (!var50.isDowned(var9, 584775972)) {
                JuggernautManager var53 = this.mgr;
                var53.markDowned(var9, 1769105396);
                Server var56 = this.plugin.getServer();
                Component var71 = Text.legacy("<##C8102E>&l" + var9.getName() + " &7(Juggernaut) has fallen — their gem dropped.", 883008065);
                var56.broadcast(var71);
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent var1) {
        Player var6 = var1.getPlayer();
        JuggernautManager var9 = this.mgr;
        if (var9.isJuggernaut(var6, 1070464021)) {
            BukkitScheduler var14 = this.plugin.getServer().getScheduler();
            Plugin var17 = this.plugin;
            Runnable var18 = () -> {
                JuggernautManager var4 = this.mgr;
                if (var4.isDowned(var6, 584775972)) {
                    Component var11 = Text.legacy("<##C8102E>&lFallen Juggernaut. Wait till you are revived", 883008065);
                    var6.kick(var11);
                } else {
                    JuggernautManager var7 = this.mgr;
                    var7.reapply(var6, 723897296);
                }
            };
            var14.runTask(var17, var18);
        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent var1) {
        JuggernautManager var5 = this.mgr;
        UUID var9 = var1.getPlayer().getUniqueId();
        if (var5.isDowned(var9, 1258641962)) {
            Result var10 = Result.KICK_OTHER;
            Component var11 = Text.legacy("<##C8102E>&lFallen Juggernaut. Wait till you are revived", 883008065);
            var1.disallow(var10, var11);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent var1) {
        JuggernautManager var4 = this.mgr;
        Player var5 = var1.getPlayer();
        var4.reapply(var5, 723897296);
    }

    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent var1) {
        Player var5 = var1.getPlayer();
        JuggernautManager var12 = this.mgr;
        if (var12.isJuggernaut(var5, 1070464021)) {
            boolean var34;
            label31: {
                JuggernautManager var15 = this.mgr;
                if (!var15.inKitWindow(var5, 1622100168)) {
                    NamespacedKey var19 = var1.getAdvancement().getKey();
                    NamespacedKey var36 = NETHERITE_ARMOUR_ADV;
                    if (!var19.equals(var36)) {
                        var34 = false;
                        break label31;
                    }
                }

                var34 = true;
            }

            if (var34) {
                var1.message(null);
                Advancement var39 = var1.getAdvancement();
                AdvancementProgress var7 = var5.getAdvancementProgress(var39);
                Collection<String> var41 = var7.getAwardedCriteria();

                for (String var9 : new ArrayList<>(var41)) {
                    var7.revokeCriteria(var9);
                }
            }
        }
    }

    private List<ItemStack> stripMythicGems(Player var1, int var2) {
        ArrayList var3 = new ArrayList();
        ArrayList var5 = var3;
        PlayerInventory var6 = var1.getInventory();
        ItemStack[] var7 = var6.getStorageContents();
        int var8 = 0;

        while (true) {
            int var42 = var7.length;
            if (var8 >= var42) {
                var6.setStorageContents(var7);
                if (isMythicGem(var6.getItemInOffHand())) {
                    ItemStack var49 = var6.getItemInOffHand();
                    var5.add(var49);
                    var6.setItemInOffHand(null);
                }

                ItemStack[] var61 = var6.getArmorContents();
                int var9 = 0;

                while (true) {
                    int var52 = var61.length;
                    if (var9 >= var52) {
                        var6.setArmorContents(var61);
                        return var5;
                    }

                    if (isMythicGem(var61[var9])) {
                        ItemStack var56 = var61[var9];
                        var5.add(var56);
                        var61[var9] = null;
                    }

                    var9++;
                }
            }

            if (isMythicGem(var7[var8])) {
                ItemStack var45 = var7[var8];
                var5.add(var45);
                var7[var8] = null;
            }

            var8++;
        }
    }
}
