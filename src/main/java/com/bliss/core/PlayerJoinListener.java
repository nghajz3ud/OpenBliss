package com.bliss.core;

import com.bliss.BlissPlugin;
import com.bliss.items.BlissCoreItems;
import java.util.Random;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public final class PlayerJoinListener implements Listener {
    private static final int FIRST_JOIN_ENERGY = 6;
    private static final float WALK_SPEED = 0.2F;
    private static final double MAX_HEALTH = 20.0;
    private static final double ATTACK_SPEED = 4.0;
    private static final int GAMEPLAY_TAG_MAX_SECONDS = 40;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final BlissCoreItems coreItems;
    private final Random random;

    public PlayerJoinListener(BlissPlugin var1, PlayerDataStore var2, BlissCoreItems var3, int var4) {
        Random var6 = new Random();
        this.random = var6;
        this.plugin = var1;
        this.players = var2;
        this.coreItems = var3;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent var1) {
        Player var8 = var1.getPlayer();
        GameMode var9 = var8.getGameMode();
        if (!var8.isOp()) {
            GameMode var3 = GameMode.SPECTATOR;
            if (var9 != var3) {
                GameMode var88 = GameMode.CREATIVE;
                if (var9 != var88) {
                    var8.setAllowFlight(false);
                    var8.setFlying(false);
                }
            }
        }

        if (!var8.hasGravity()) {
            var8.setGravity(true);
        }

        if (var8.isInvulnerable()) {
            var8.setInvulnerable(false);
        }

        PlayerDataStore var33 = this.players;
        UUID var94 = var8.getUniqueId();
        if (var33.get(var94, 1264010596).tagSecondsLeft(487151397) <= 40) {
            TagStore var38 = this.plugin.tags(1027557380);
            UUID var97 = var8.getUniqueId();
            var38.clear(var97, 798728773);
        }

        this.restampSpeedGem(var8, 1640244533);
        if (var8.isGlowing()) {
            var8.setGlowing(false);
        }

        var8.setWalkSpeed(0.2F);
        Attribute var101 = Attribute.MAX_HEALTH;
        AttributeInstance var10 = var8.getAttribute(var101);
        if (var10 != null) {
            var10.setBaseValue(20.0);
        }

        Attribute var104 = Attribute.ATTACK_SPEED;
        AttributeInstance var11 = var8.getAttribute(var104);
        if (var11 != null) {
            var11.setBaseValue(4.0);
        }

        PlayerDataStore var53 = this.players;
        UUID var108 = var8.getUniqueId();
        PlayerData var12 = var53.get(var108, 1264010596);
        GlobalState var13 = GlobalState.forPlayer(var8, 1433087673);
        boolean var14 = var13.tier2OnJoin(2023108022);
        boolean var87;
        if (!var13.prog1Started(1011566878) && !var13.prog0Gems(1794280694)) {
            var87 = true;
        } else {
            var87 = false;
        }

        if (!var12.joined(225029398)) {
            var12.setTier2(var14, 1757992594);
            var12.setEnergy(6, 319922676);
        }

        if (!var12.joined(225029398) && !var87) {
            GemType var16 = GemType.random(this.random);
            var12.setGem(var16, 2137390487);
            var12.setJoined(true, 2028976592);
            Location var117 = var8.getLocation();
            Sound var119 = Sound.ENTITY_PLAYER_LEVELUP;
            var8.playSound(var117, var119, 1.0F, 1.0F);
            BlissCoreItems var121 = this.coreItems;
            ItemStack var122 = var121.gemItem(var16, var14, 6, 256965471);
            BlissCoreItems.giveGemOrHold(var8, -1, var122, 2133739666);
        } else if (var14 && !var12.tier2(629984660)) {
            GemType var74 = var12.gem(1413186337);
            GemType var111 = GemType.NONE;
            if (var74 != var111 && BlissCoreItems.findGemSlot(var8, 1702349856) >= 0) {
                var12.setTier2(true, 1757992594);
                BlissCoreItems var79 = this.coreItems;
                var79.refreshHeldGemItem(var8, var12, 835235475);
            }
        }
    }

    private void restampSpeedGem(Player var1, int var2) {
        PlayerDataStore var19 = this.players;
        UUID var60 = var1.getUniqueId();
        PlayerData var9 = var19.get(var60, 1264010596);
        GemType var22 = var9.gem(1413186337);
        GemType var61 = GemType.SPEED;
        if (var22 == var61) {
            String var59;
            if (this.plugin.globalState(724248306).legacySpeed(2067387107)) {
                var59 = "Thunder Step";
            } else {
                var59 = "Terminal Velocity";
            }

            String var11 = var59;
            ItemStack[] var12 = var1.getInventory().getContents();
            int var13 = 0;

            while (true) {
                int var63 = var12.length;
                if (var13 >= var63) {
                    return;
                }

                ItemStack var14 = var12[var13];
                if (var14 != null) {
                    int var37 = BlissCoreItems.gemIdOf(var14, 382441079);
                    int var66 = GemType.SPEED.id();
                    if (var37 == var66) {
                        ItemMeta var15 = var14.getItemMeta();
                        if (var15 != null && var15.hasLore()) {
                            boolean var16 = false;

                            for (Component var18 : var15.lore()) {
                                PlainTextComponentSerializer var52 = PlainTextComponentSerializer.plainText();
                                String var53 = var52.serialize(var18);
                                if (var53.contains(var11)) {
                                    var16 = true;
                                    break;
                                }
                            }

                            if (var16) {
                                PlayerInventory var58 = var1.getInventory();
                                BlissCoreItems var70 = this.coreItems;
                                GemType var6 = GemType.SPEED;
                                boolean var72 = var9.tier2(629984660);
                                int var73 = var9.energy(1160853318);
                                ItemStack var71 = var70.gemItem(var6, var72, var73, 256965471);
                                var58.setItem(var13, var71);
                            }
                        }
                    }
                }

                var13++;
            }
        }
    }
}
