package com.bliss.core;

import com.bliss.BlissPlugin;
import com.bliss.goldgem.GoldGemManager;
import com.bliss.honored.JuggernautGemGuardListener;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public final class PlayerDeathListener implements Listener {
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final BlissCoreItems coreItems;

    public PlayerDeathListener(BlissPlugin var1, PlayerDataStore var2, BlissCoreItems var3, int var4) {
        this.plugin = var1;
        this.players = var2;
        this.coreItems = var3;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent var1) {
        Player var7 = var1.getEntity();
        Player var8 = var7.getKiller();
        if (var8 != null) {
            UUID var25 = var8.getUniqueId();
            UUID var147 = var7.getUniqueId();
            if (var25.equals(var147)) {
                var8 = null;
            }
        }

        PlayerDataStore var29 = this.players;
        UUID var149 = var7.getUniqueId();
        PlayerData var9 = var29.get(var149, 1264010596);
        PlayerData var146;
        if (var8 == null) {
            var146 = null;
        } else {
            PlayerDataStore var33 = this.players;
            UUID var151 = var8.getUniqueId();
            var146 = var33.get(var151, 1264010596);
        }

        var9.clearTagOnDeath(227654567);
        boolean var11 = GlobalState.forPlayer(var7, 1433087673).noEnergyMode(340448998);
        List<ItemStack> var39 = var1.getDrops();
        Predicate<ItemStack> var152 = var0 -> {
            boolean var3;
            if (BlissCoreItems.gemIdOf(var0, 382441079) > 0) {
                var3 = true;
            } else {
                var3 = false;
            }

            return var3;
        };
        var39.removeIf(var152);
        GlobalState var12 = GlobalState.forPlayer(var7, 1433087673);
        boolean var46;
        if (var8 != null && !var7.hasMetadata("NPC")) {
            var46 = true;
        } else {
            var46 = false;
        }

        boolean var145;
        if (!var46 || var12.progression(790021995) < 5 && !var12.playerHeadDrops(666090489)) {
            var145 = false;
        } else {
            var145 = true;
        }

        if (var145) {
            this.dropTrophyHead(var7, var8, 1375615016);
        }

        if (var46 && var12.trophyThunder(833687581) && (var145 || var12.trophyThunderAllStages(250785423))) {
            World var61 = var7.getWorld();
            Location var157 = var7.getLocation();
            var61.strikeLightningEffect(var157);
            World var64 = var7.getWorld();
            Location var159 = var7.getLocation();
            Sound var197 = Sound.ENTITY_WITHER_SPAWN;
            var64.playSound(var159, var197, 2.0F, 1.0F);
        }

        if (var9.tier2(629984660) && !var12.keepTier2OnDeath(922876535)) {
            if (!var12.tier2OnJoin(2023108022)) {
                World var72 = var7.getWorld();
                Location var161 = var7.getLocation();
                ItemStack var200 = this.coreItems.upgrader(2125662274);
                var72.dropItemNaturally(var161, var200);
            }

            var9.setTier2(false, 1757992594);
        }

        boolean var144;
        label157: {
            if (!JuggernautGemGuardListener.isJuggernaut(var7, 859003337)) {
                GoldGemManager var79 = this.plugin.goldGemManager(256518991);
                UUID var164 = var7.getUniqueId();
                if (!var79.isRepurposed(var164, 1313846040)) {
                    var144 = false;
                    break label157;
                }
            }

            var144 = true;
        }

        boolean var88;
        label152: {
            label151: {
                if (var8 != null) {
                    if (JuggernautGemGuardListener.isJuggernaut(var8, 859003337)) {
                        break label151;
                    }

                    GoldGemManager var86 = this.plugin.goldGemManager(256518991);
                    UUID var166 = var8.getUniqueId();
                    if (var86.isRepurposed(var166, 1313846040)) {
                        break label151;
                    }
                }

                var88 = false;
                break label152;
            }

            var88 = true;
        }

        if (var9.energy(1160853318) > 1 && !var144 && !var11) {
            var9.addEnergy(-1, 514979476);
            int var17 = var9.energy(1160853318);
            if (var8 != null && !var88) {
                int var18 = var146.energy(1160853318);
                boolean var19 = false;
                if (var17 > 2) {
                    if (var18 > 10) {
                        World var138 = var7.getWorld();
                        Location var193 = var7.getLocation();
                        ItemStack var218 = this.coreItems.bottledEnergy(1532783840);
                        var138.dropItemNaturally(var193, var218);
                    }

                    if (var18 < 11 && var18 > 1) {
                        var146.addEnergy(1, 514979476);
                        var19 = true;
                    }
                } else {
                    if (var18 > 10) {
                        World var124 = var7.getWorld();
                        Location var184 = var7.getLocation();
                        ItemStack var211 = this.coreItems.bottledEnergy(1532783840);
                        var124.dropItemNaturally(var184, var211);
                    }

                    if (var18 < 11 && var18 > 1) {
                        var146.addEnergy(1, 514979476);
                        var19 = true;
                        World var131 = var7.getWorld();
                        Location var189 = var7.getLocation();
                        ItemStack var214 = this.coreItems.bottledEnergy(1532783840);
                        var131.dropItemNaturally(var189, var214);
                    }
                }

                if (var19) {
                    BlissCoreItems var135 = this.coreItems;
                    var135.refreshHeldGemItem(var8, var146, 835235475);
                }
            } else {
                World var99 = var7.getWorld();
                Location var170 = var7.getLocation();
                ItemStack var203 = this.coreItems.bottledEnergy(1532783840);
                var99.dropItemNaturally(var170, var203);
            }
        }

        if (!var11) {
            GemType var103 = var9.gem(1413186337);
            GemType var171 = GemType.NONE;
            if (var103 != var171) {
                GemType var173 = var9.gem(1413186337);
                int var205 = var9.energy(1160853318);
                sendBucketMessage(var7, var173, var205, false, 2020658763);
                if (var9.energy(1160853318) == 1) {
                    this.maybeBroadcastShattered(var7, 1284053280);
                }
            }

            if (var8 != null) {
                GemType var110 = var146.gem(1413186337);
                GemType var176 = GemType.NONE;
                if (var110 != var176) {
                    GemType var178 = var146.gem(1413186337);
                    int var207 = var146.energy(1160853318);
                    sendBucketMessage(var8, var178, var207, true, 2020658763);
                }
            }

            if (var8 != null && var146.energy(1160853318) >= 11) {
                AchievementStore var117 = this.plugin.advancements(1975441698);
                AchievementStore.Advancement var208 = AchievementStore.Advancement.OVERFLOWING;
                var117.unlock(var8, var208, 1132982324);
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent var1) {
        final Player var6 = var1.getPlayer();
        BlissPlugin var9 = this.plugin;
        UUID var12 = var6.getUniqueId();
        var9.clearAllGemCooldowns(var12, 1294563415);
        BukkitRunnable var10 = new BukkitRunnable() {
            public void run() {
                PlayerDataStore var12 = PlayerDeathListener.this.players;
                UUID var39 = var6.getUniqueId();
                PlayerData var7 = var12.get(var39, 1264010596);
                int var8 = -1;
                ItemStack[] var9 = var6.getInventory().getContents();
                int var10 = 0;

                while (true) {
                    int var41 = var9.length;
                    if (var10 >= var41) {
                        GemType var22 = var7.gem(1413186337);
                        GemType var42 = GemType.NONE;
                        if (var22 != var42 && !JuggernautGemGuardListener.isJuggernaut(var6, 859003337)) {
                            Player var27 = var6;
                            BlissCoreItems var48 = PlayerDeathListener.this.coreItems;
                            GemType var51 = var7.gem(1413186337);
                            boolean var52 = var7.tier2(629984660);
                            int var53 = var7.energy(1160853318);
                            ItemStack var49 = var48.gemItem(var51, var52, var53, 256965471);
                            BlissCoreItems.giveGemOrHold(var27, var8, var49, 2133739666);
                        }

                        return;
                    }

                    if (var9[var10] != null && BlissCoreItems.gemIdOf(var9[var10], 382441079) > 0) {
                        if (var8 < 0) {
                            var8 = var10;
                        }

                        PlayerInventory var37 = var6.getInventory();
                        var37.setItem(var10, null);
                    }

                    var10++;
                }
            }
        };
        BlissPlugin var14 = this.plugin;
        var10.runTaskLater(var14, 1L);
    }

    private void maybeBroadcastShattered(Player var1, int var2) {
        AchievementStore var7 = this.plugin.advancements(1975441698);
        AchievementStore.Advancement var5 = AchievementStore.Advancement.SHATTERED;
        var7.unlock(var1, var5, 1132982324);
    }

    private void dropTrophyHead(Player var1, Player var2, int var3) {
        Material var5 = Material.PLAYER_HEAD;
        ItemStack var4 = new ItemStack(var5);
        if (var4.getItemMeta() instanceof SkullMeta var8) {
            var8.setOwningPlayer(var1);
            Component var34 = Text.legacy("&e" + var1.getName() + "'s Head", 373192400);
            TextDecoration var46 = TextDecoration.ITALIC;
            Component var35 = var34.decoration(var46, false);
            var8.displayName(var35);
            Component var39 = Text.legacy("&7Killed by: &c" + var2.getName(), 373192400);
            TextDecoration var47 = TextDecoration.ITALIC;
            List var41 = List.of(var39.decoration(var47, false));
            var8.lore(var41);
            NamespacedKey var53 = NamespacedKey.fromString("itemtag:original_owner");
            if (var53 != null) {
                PersistentDataContainer var24 = var8.getPersistentDataContainer();
                PersistentDataType var48 = PersistentDataType.STRING;
                String var52 = var1.getName();
                var24.set(var53, var48, var52);
            }

            var4.setItemMeta(var8);
        }

        World var28 = var1.getWorld();
        Location var45 = var1.getLocation();
        var28.dropItemNaturally(var45, var4);
    }

    private static void sendBucketMessage(Player var0, GemType var1, int var2, boolean var3, int var4) {
        String var14;
        if (var3) {
            var14 = "&bYou &agained &benergy. &byour " + gemColored(var1, 1562961205) + " &bgem is now ";
        } else {
            var14 = "&bYou lost &cenergy. &byour " + gemColored(var1, 1562961205) + " &bgem is now at ";
        }

        String var17 = bucketText(var2, 446941057);
        Component var16 = Text.legacy(var14 + var17, 373192400);
        var0.sendMessage(var16);
    }

    private static String gemColored(GemType var0, int var1) {
        return switch (var0) {
            case STRENGTH-> "&4&lsᴛʀᴇɴɢᴛʜ";
            case FIRE-> "§6§lꜰɪʀᴇ";
            case WEALTH-> "§2§lᴡᴇᴀʟᴛʜ";
            case SPEED-> "&e&lsᴘᴇᴇᴅ";
            case PUFF-> "&f§lᴘᴜꜰꜰ";
            case ASTRA-> "&5&lᴀsᴛʀᴀ";
            case LIFE-> "§d§lʟɪꜰᴇ";
            case FLUX-> "§3§lꜰʟᴜx";
            default -> "&7ɢᴇᴍ";
        };
    }

    private static String bucketText(int var0, int var1) {
        int var7 = Math.min(11, var0);

        return switch (Math.max(0, var7)) {
            case 0 -> "&fN/A";
            case 1 -> "&7Broken";
            case 2 -> "&4Ruined";
            case 3 -> "&eDamaged";
            case 4 -> "&aCracked";
            case 5 -> "&aScratched";
            case 6 -> "&bPristine";
            case 7 -> "&bPristine + 1";
            case 8 -> "&bPristine + 2";
            case 9 -> "&bPristine + 3";
            case 10 -> "&bPristine + 4";
            default -> "&bPristine + 5";
        };
    }
}
