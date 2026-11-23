package com.bliss.villager;

import com.bliss.BlissPlugin;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.PlayerInventory;

public final class TradeInterceptListener implements Listener {
    private static final int RESULT_SLOT = 2;
    private final BlissPlugin plugin;
    private final EventState state;
    private final BlissItems items;

    public TradeInterceptListener(BlissPlugin var1, EventState var2, BlissItems var3, int var4) {
        this.plugin = var1;
        this.state = var2;
        this.items = var3;
    }

    @EventHandler
    public void onLeakedVillagerInteract(PlayerInteractEntityEvent var1) {
        if (!this.state.isEventRunning(527098672)) {
            if (var1.getRightClicked() instanceof Villager var5) {
                if (isEventVillager(var5, 1597773303)) {
                    for (int var28 = 1; var28 <= 3; var28++) {
                        UUID var22 = var5.getUniqueId();
                        EventState var26 = this.state;
                        UUID var27 = var26.villagerUuid(var28, 1186869288);
                        if (var22.equals(var27)) {
                            return;
                        }
                    }

                    var1.setCancelled(true);
                    var5.remove();
                }
            }
        }
    }

    private static boolean isEventVillager(Villager var0, int var1) {
        return var0.getScoreboardTags().contains("BlissEventVillager");
    }

    @EventHandler
    public void onClick(InventoryClickEvent var1) {
        if (this.state.isEventRunning(527098672)) {
            if (var1.getWhoClicked() instanceof Player var7) {
                Inventory var142 = var1.getView().getTopInventory();
                if (var142 instanceof MerchantInventory) {
                    label70: {
                        ItemStack var9 = var1.getCurrentItem();
                        if (var9 != null) {
                            Material var34 = var9.getType();
                            Material var3 = Material.AMETHYST_SHARD;
                            if (var34 == var3) {
                                break label70;
                            }

                            Material var36 = var9.getType();
                            Material var86 = Material.PRISMARINE_SHARD;
                            if (var36 == var86) {
                                break label70;
                            }
                        }

                        EventState var39 = this.state;
                        UUID var89 = var7.getUniqueId();
                        Integer var10 = var39.tradingWith(var89, 1785384040);
                        if (var10 == null) {
                            return;
                        }

                        if (var1.getRawSlot() != 2) {
                            return;
                        }

                        if (var9 != null && !var9.getType().isAir()) {
                            TradeInterceptListener.TokenTrade var11 = TradeInterceptListener.TokenTrade.match(var9);
                            if (var11 == null) {
                                return;
                            }

                            var1.setCancelled(true);
                            EventState var53 = this.state;
                            int var93 = var10;
                            String var122 = var11.key;
                            if (var53.isTradeBought(var93, var122, 1796161451)) {
                                Component var95 = Text.legacy("&cThis trade has already been claimed!", 373192400);
                                var7.sendMessage(var95);
                                Location var97 = var7.getLocation();
                                Sound var123 = Sound.ENTITY_VILLAGER_NO;
                                var7.playSound(var97, var123, 1.0F, 1.0F);
                                return;
                            }

                            MerchantInventory var12 = (MerchantInventory)var142;
                            int var13 = this.countEnergyTokens(var7, var12, 2037976148);
                            int var100 = var11.cost;
                            if (var13 < var100) {
                                int var102 = var11.cost;
                                Component var104 = Text.legacy("&cYou need " + var102 + " Energy Tokens for this! You only have " + var13 + ".", 373192400);
                                var7.sendMessage(var104);
                                Location var106 = var7.getLocation();
                                Sound var126 = Sound.ENTITY_VILLAGER_NO;
                                var7.playSound(var106, var126, 1.0F, 1.0F);
                                return;
                            }

                            int var136 = var11.cost;
                            this.removeEnergyTokens(var7, var12, var136, 756690706);
                            int var109 = var10;
                            ItemStack var14 = var11.build(var109);
                            PlayerInventory var68 = var7.getInventory();
                            ItemStack[] var111 = new ItemStack[]{var14};

                            for (ItemStack var16 : var68.addItem(var111).values()) {
                                World var84 = var7.getWorld();
                                Location var121 = var7.getLocation();
                                var84.dropItemNaturally(var121, var16);
                            }

                            EventState var75 = this.state;
                            int var113 = var10;
                            String var129 = var11.key;
                            var75.setTradeBought(var113, var129, 2130007188);
                            this.state.save(1112893273);
                            Location var115 = var7.getLocation();
                            Sound var130 = Sound.ENTITY_PLAYER_LEVELUP;
                            var7.playSound(var115, var130, 1.0F, 1.0F);
                            String var117 = var11.displayName;
                            int var132 = var11.cost;
                            Component var119 = Text.legacy("&aYou bought &f" + var117 + "&a for &6" + var132 + " Energy Tokens&a!", 373192400);
                            var7.sendMessage(var119);
                            return;
                        }

                        return;
                    }

                    var1.setCancelled(true);
                }
            }
        }
    }

    private int countEnergyTokens(Player var1, MerchantInventory var2, int var3) {
        int var6 = 0;

        for (ItemStack var10 : var1.getInventory().getContents()) {
            BlissItems var31 = this.items;
            if (var31.isEnergyToken(var10, 838390083)) {
                int var43 = var10.getAmount();
                var6 += var43;
            }
        }

        for (int var44 = 0; var44 < 2; var44++) {
            ItemStack var45 = var2.getItem(var44);
            BlissItems var24 = this.items;
            if (var24.isEnergyToken(var45, 838390083)) {
                int var39 = var45.getAmount();
                var6 += var39;
            }
        }

        return var6;
    }

    private void removeEnergyTokens(Player var1, MerchantInventory var2, int var3, int var4) {
        for (int var8 = 0; var8 < 2 && var3 > 0; var8++) {
            ItemStack var9 = var2.getItem(var8);
            BlissItems var17 = this.items;
            if (var17.isEnergyToken(var9, 838390083)) {
                int var50 = var9.getAmount();
                int var10 = Math.min(var3, var50);
                int var52 = var9.getAmount();
                int var53 = var52 - var10;
                var9.setAmount(var53);
                ItemStack var70;
                if (var9.getAmount() <= 0) {
                    var70 = null;
                } else {
                    var70 = var9;
                }

                var2.setItem(var8, var70);
                var3 -= var10;
            }
        }

        if (var3 > 0) {
            ItemStack[] var73 = var1.getInventory().getContents();
            int var74 = 0;

            while (true) {
                int var57 = var73.length;
                if (var74 >= var57 || var3 <= 0) {
                    PlayerInventory var33 = var1.getInventory();
                    var33.setContents(var73);
                    return;
                }

                ItemStack var75 = var73[var74];
                BlissItems var37 = this.items;
                if (var37.isEnergyToken(var75, 838390083)) {
                    int var62 = var75.getAmount();
                    int var11 = Math.min(var3, var62);
                    int var64 = var75.getAmount();
                    int var65 = var64 - var11;
                    var75.setAmount(var65);
                    if (var75.getAmount() <= 0) {
                        var73[var74] = null;
                    }

                    var3 -= var11;
                }

                var74++;
            }
        }
    }

    private static enum TokenTrade {
        MACE("mace", 8, "Mace") {
            @Override
            ItemStack build(int var1x) {
                return new ItemStack(Material.MACE);
            }
        },
        HELM("helm", 4, "Netherite Helm") {
            @Override
            ItemStack build(int var1) {
                return VillagerSetup.namedNetherite(Material.NETHERITE_HELMET, "ɴᴇᴛʜᴇʀɪᴛᴇ ʜᴇʟᴍ", 1484712041);
            }
        },
        LEGS("legs", 4, "Netherite Leggings") {
            @Override
            ItemStack build(int var1) {
                return VillagerSetup.namedNetherite(Material.NETHERITE_LEGGINGS, "ɴᴇᴛʜᴇʀɪᴛᴇ ʟᴇɢɢɪɴɢꜱ", 1484712041);
            }
        },
        CHEST("chest", 4, "Netherite Chestplate") {
            @Override
            ItemStack build(int var1) {
                return VillagerSetup.namedNetherite(Material.NETHERITE_CHESTPLATE, "ɴᴇᴛʜᴇʀɪᴛᴇ ᴄʜᴇꜱᴛᴘʟᴀᴛᴇ", 1484712041);
            }
        },
        BOOTS("boots", 4, "Netherite Boots") {
            @Override
            ItemStack build(int var1) {
                return VillagerSetup.namedNetherite(Material.NETHERITE_BOOTS, "ɴᴇᴛʜᴇʀɪᴛᴇ ʙᴏᴏᴛꜱ", 1484712041);
            }
        },
        SWORD("sword", 4, "Netherite Sword") {
            @Override
            ItemStack build(int var1) {
                return VillagerSetup.namedNetherite(Material.NETHERITE_SWORD, "ɴᴇᴛʜᴇʀɪᴛᴇ ꜱᴡᴏʀᴅ", 1484712041);
            }
        },
        AXE("axe", 4, "Netherite Axe") {
            @Override
            ItemStack build(int var1) {
                return VillagerSetup.namedNetherite(Material.NETHERITE_AXE, "ɴᴇᴛʜᴇʀɪᴛᴇ ᴀxᴇ", 1484712041);
            }
        };

        final String key;
        final int cost;
        final String displayName;

        private TokenTrade(String nullxx, int nullxxx, String nullxxxx) {
            this.key = nullxx;
            this.cost = nullxxx;
            this.displayName = nullxxxx;
        }

        abstract ItemStack build(int var1);

        static TradeInterceptListener.TokenTrade match(ItemStack var0) {
            Material var3 = var0.getType();
            return switch (var3) {
                case MACE-> MACE;
                case NETHERITE_HELMET-> HELM;
                case NETHERITE_LEGGINGS-> LEGS;
                case NETHERITE_CHESTPLATE-> CHEST;
                case NETHERITE_BOOTS-> BOOTS;
                case NETHERITE_SWORD-> SWORD;
                case NETHERITE_AXE-> AXE;
                default -> null;
            };
        }
    }
}
