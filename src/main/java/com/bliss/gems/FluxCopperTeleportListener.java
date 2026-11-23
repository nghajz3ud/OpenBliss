package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.AchievementStore;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.core.TagStore;
import com.bliss.goldgem.GoldGem;
import com.bliss.items.BlissCoreItems;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public final class FluxCopperTeleportListener implements Listener {
    private static final double MAX_DISTANCE_SQ = 2601.0;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final TagStore tags;

    public FluxCopperTeleportListener(BlissPlugin var1, PlayerDataStore var2, TagStore var3, int var4) {
        this.plugin = var1;
        this.players = var2;
        this.tags = var3;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent var1) {
        EquipmentSlot var22 = var1.getHand();
        EquipmentSlot var3 = EquipmentSlot.HAND;
        if (var22 == var3) {
            Action var24 = var1.getAction();
            Action var97 = Action.RIGHT_CLICK_AIR;
            if (var24 != var97) {
                Action var26 = var1.getAction();
                Action var98 = Action.RIGHT_CLICK_BLOCK;
                if (var26 != var98) {
                    return;
                }
            }

            Player var10 = var1.getPlayer();
            TagStore var30 = this.tags;
            UUID var100 = var10.getUniqueId();
            if (!var30.isTagged(var100, 995676415)) {
                PlayerDataStore var33 = this.players;
                UUID var102 = var10.getUniqueId();
                PlayerData var11 = var33.get(var102, 1264010596);
                if (var11.energy(1160853318) > 4) {
                    if (var11.canUseAbilities(325243089)) {
                        if (var10.isSneaking()) {
                            GemType var104 = GemType.FLUX;
                            boolean var12 = GoldGem.isWieldingSoulMain(var10, var104, 244096655);
                            ItemStack var13 = var10.getInventory().getItemInMainHand();
                            int var47 = BlissCoreItems.gemIdOf(var13, 382441079);
                            int var106 = GemType.FLUX.id();
                            if (var47 == var106 || var12) {
                                if (!var12) {
                                    int var14 = BlissCoreItems.tierOf(var13, 1633828214);
                                    if (var14 != 1 && var14 != 2) {
                                        return;
                                    }
                                }

                                Block var137 = var10.getTargetBlockExact(50);
                                if (var137 != null) {
                                    if (isCopper(var137.getType(), 988387741)) {
                                        if (isCopper(var10.getLocation().getBlock().getRelative(0, -1, 0).getType(), 988387741)) {
                                            Location var16 = var137.getLocation().add(0.5, 1.0, 0.5);
                                            String var70 = "flux";
                                            String var113 = "copper-teleport";
                                            double var124 = Math.sqrt(2601.0);
                                            double var17 = GemTuning.radius(var70, var113, var124, 1058601812);
                                            Location var74 = var10.getLocation();
                                            double var75 = var74.distanceSquared(var16);
                                            double var128 = var17 * var17;
                                            if (!(var75 >= var128)) {
                                                Location var19 = var10.getLocation();
                                                long var20 = (long)Math.ceil(var19.distance(var16));
                                                float var117 = var19.getYaw();
                                                var16.setYaw(var117);
                                                float var119 = var19.getPitch();
                                                var16.setPitch(var119);
                                                var10.teleport(var16);
                                                if (var20 > 0L) {
                                                    AchievementStore var96 = this.plugin.advancements(1975441698);
                                                    AchievementStore.Advancement var132 = AchievementStore.Advancement.ZIP_AWAY;
                                                    var96.progress(var10, var132, var20, 223202830);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean isCopper(Material var0, int var1) {
        return var0.name().toLowerCase().contains("copper");
    }
}
