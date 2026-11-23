package com.bliss.admin;

import com.bliss.BlissPlugin;
import com.bliss.items.BlissCoreItems;
import com.bliss.items.BlissRecipes;
import com.bliss.villager.Text;
import java.util.ArrayList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class RecipeBookListener implements CommandExecutor, Listener {
    private static final String[] KEYS;
    private static final int[] LIST_SLOTS;
    private static final int[] GRID_CELLS;
    private static final int ARROW_SLOT = 24;
    private static final int OUTPUT_SLOT = 26;
    private static final int BACK_SLOT = 49;
    private static final int EDIT_SLOT = 53;
    private static final int SAVE_SLOT = 53;
    private static final int RESET_SLOT = 45;
    private static final int TOP_SIZE = 54;
    private final BlissPlugin plugin;
    private final BlissCoreItems items;

    public RecipeBookListener(BlissPlugin var1, BlissCoreItems var2, int var3) {
        this.plugin = var1;
        this.items = var2;
    }

    private static boolean isAdmin(Player var0, int var1) {
        boolean var7;
        if (!var0.isOp() && !var0.hasPermission("bliss.admin")) {
            var7 = false;
        } else {
            var7 = true;
        }

        return var7;
    }

    public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
        if (var1 instanceof Player var7) {
            this.openList(var7, 1232856692);
            return true;
        } else {
            Component var16 = Text.legacy("&cPlayer only.", 373192400);
            var1.sendMessage(var16);
            return true;
        }
    }

    private void openList(Player var1, int var2) {
        RecipeBookListener.ListHolder var3 = new RecipeBookListener.ListHolder();
        Component var26 = Text.legacy("&8Bliss Recipes", 373192400);
        Inventory var10 = Bukkit.createInventory(var3, 27, var26);
        this.fill(var10, 663069017);
        int var11 = 0;

        while (true) {
            int var22 = KEYS.length;
            if (var11 >= var22) {
                var1.openInventory(var10);
                return;
            }

            int[] var24 = LIST_SLOTS;
            int var25 = var24[var11];
            String[] var6 = KEYS;
            String var31 = var6[var11];
            ItemStack var30 = this.display(var31, 1884501006);
            String[] var33 = new String[1];
            String var9 = "&7Click to view its recipe";
            var33[0] = var9;
            ItemStack var29 = this.withHint(var30, var33, 1516901771);
            var10.setItem(var25, var29);
            var11++;
        }
    }

    private void openDetail(Player var1, String var2, boolean var3, int var4) {
        String var40;
        if (var3) {
            var40 = "&8Recipe &7(editing)";
        } else {
            var40 = "&8Recipe";
        }

        RecipeBookListener.DetailHolder var17 = new RecipeBookListener.DetailHolder(var2, var3, 2020661656);
        Component var59 = Text.legacy(var40, 373192400);
        Inventory var14 = Bukkit.createInventory(var17, 54, var59);
        this.fill(var14, 663069017);
        BlissRecipes var22 = this.plugin.recipes(942436690);
        ItemStack[] var15 = var22.grid(var2, 1219052114);
        int var16 = 0;

        while (true) {
            int var45 = GRID_CELLS.length;
            if (var16 >= var45) {
                break;
            }

            int var47 = var15.length;
            if (var16 >= var47) {
                break;
            }

            int[] var48 = GRID_CELLS;
            int var49 = var48[var16];
            ItemStack var62 = var15[var16];
            var14.setItem(var49, var62);
            var16++;
        }

        Material var78 = Material.ARROW;
        String var8 = "&f➜";
        ItemStack var64 = this.named(var78, var8, 120828915);
        var14.setItem(24, var64);
        ItemStack var66 = this.result(var2, 862604955);
        var14.setItem(26, var66);
        Material var80 = Material.BARRIER;
        String var88 = "&cBack";
        ItemStack var68 = this.named(var80, var88, 120828915);
        var14.setItem(49, var68);
        if (var3) {
            Material var81 = Material.LIME_DYE;
            String var89 = "&a&lSave recipe";
            ItemStack var70 = this.named(var81, var89, 120828915);
            var14.setItem(53, var70);
            Material var90 = Material.RED_DYE;
            String var9 = "&c&lReset to default";
            ItemStack var83 = this.named(var90, var9, 120828915);
            String[] var92 = new String[1];
            String var11 = "&7Discard the edit";
            var92[0] = var11;
            ItemStack var72 = this.withHint(var83, var92, 1516901771);
            var14.setItem(45, var72);
            Material var93 = Material.WRITABLE_BOOK;
            String var99 = "&eEditing";
            ItemStack var85 = this.named(var93, var99, 120828915);
            String[] var95 = new String[3];
            String var105 = "&7Place an item on your cursor, then";
            var95[0] = var105;
            String var106 = "&7click a grid cell to set it.";
            var95[1] = var106;
            String var107 = "&7Click a cell with an empty cursor to clear it.";
            var95[2] = var107;
            ItemStack var74 = this.withHint(var85, var95, 1516901771);
            var14.setItem(48, var74);
        } else if (isAdmin(var1, 852547861)) {
            Material var96 = Material.WRITABLE_BOOK;
            String var100 = "&e&lEdit recipe";
            ItemStack var87 = this.named(var96, var100, 120828915);
            String[] var98 = new String[1];
            BlissRecipes var110 = this.plugin.recipes(942436690);
            String var112;
            if (var110.isEdited(var2, 2034122298)) {
                var112 = "&7(currently customised)";
            } else {
                var112 = "&7(currently default)";
            }

            var98[0] = var112;
            ItemStack var76 = this.withHint(var87, var98, 1516901771);
            var14.setItem(53, var76);
        }

        var1.openInventory(var14);
    }

    @EventHandler
    public void onClick(InventoryClickEvent var1) {
        InventoryHolder var6 = var1.getInventory().getHolder();
        if (var6 instanceof RecipeBookListener.ListHolder) {
            var1.setCancelled(true);
            if (var1.getWhoClicked() instanceof Player var7) {
                byte var160 = 0;

                while (true) {
                    int var94 = LIST_SLOTS.length;
                    if (var160 >= var94) {
                        return;
                    }

                    int var27 = var1.getRawSlot();
                    int[] var95 = LIST_SLOTS;
                    int var96 = var95[var160];
                    if (var27 == var96) {
                        String[] var134 = KEYS;
                        String var135 = var134[var160];
                        this.openDetail(var7, var135, false, 671836066);
                        return;
                    }

                    var160 += 1;
                }
            }
        } else if (var6 instanceof RecipeBookListener.DetailHolder var159) {
            if (var1.getWhoClicked() instanceof Player var161) {
                int var162 = var1.getRawSlot();
                if (!var159.editing) {
                    var1.setCancelled(true);
                    if (var162 == 49) {
                        this.openList(var161, 1232856692);
                    } else if (var162 == 53 && isAdmin(var161, 852547861)) {
                        String var137 = var159.which;
                        this.openDetail(var161, var137, true, 671836066);
                    }
                } else if (var162 >= 54) {
                    if (!var1.isShiftClick()) {
                        ClickType var54 = var1.getClick();
                        ClickType var104 = ClickType.DOUBLE_CLICK;
                        if (var54 != var104) {
                            return;
                        }
                    }

                    var1.setCancelled(true);
                } else {
                    var1.setCancelled(true);
                    if (var162 == 49) {
                        String var152 = var159.which;
                        this.openDetail(var161, var152, false, 671836066);
                    } else if (var162 == 45) {
                        BlissRecipes var61 = this.plugin.recipes(942436690);
                        String var110 = var159.which;
                        var61.resetGrid(var110, 1013188133);
                        Component var114 = Text.legacy("&aReset &f" + var159.which + " &arecipe to default.", 373192400);
                        var161.sendMessage(var114);
                        String var139 = var159.which;
                        this.openDetail(var161, var139, false, 671836066);
                    } else if (var162 == 53) {
                        ItemStack[] var163 = new ItemStack[9];
                        byte var164 = 0;

                        while (true) {
                            int var122 = GRID_CELLS.length;
                            if (var164 >= var122) {
                                BlissRecipes var85 = this.plugin.recipes(942436690);
                                String var124 = var159.which;
                                var85.setCustomGrid(var124, var163, 992235848);
                                Component var128 = Text.legacy("&aSaved &f" + var159.which + " &arecipe.", 373192400);
                                var161.sendMessage(var128);
                                String var144 = var159.which;
                                this.openDetail(var161, var144, false, 671836066);
                                return;
                            }

                            ItemStack var150;
                            label89: {
                                Inventory var89 = var1.getInventory();
                                int[] var130 = GRID_CELLS;
                                int var131 = var130[var164];
                                ItemStack var12 = var89.getItem(var131);
                                if (var12 != null) {
                                    Material var148 = var12.getType();
                                    Material var157 = Material.AIR;
                                    if (var148 != var157) {
                                        var150 = var12.clone();
                                        break label89;
                                    }
                                }

                                var150 = null;
                            }

                            var163[var164] = var150;
                            var164 += 1;
                        }
                    } else {
                        if (isGridCell(var162, 1902917528)) {
                            ItemStack var10 = var1.getCursor();
                            if (var10 != null) {
                                Material var71 = var10.getType();
                                Material var117 = Material.AIR;
                                if (var71 != var117) {
                                    ItemStack var11 = var10.clone();
                                    var11.setAmount(1);
                                    Inventory var76 = var1.getInventory();
                                    var76.setItem(var162, var11);
                                    return;
                                }
                            }

                            Inventory var78 = var1.getInventory();
                            var78.setItem(var162, null);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent var1) {
        if (var1.getInventory().getHolder() instanceof RecipeBookListener.DetailHolder
            || var1.getInventory().getHolder() instanceof RecipeBookListener.ListHolder) {
            var1.setCancelled(true);
        }
    }

    private static boolean isGridCell(int var0, int var1) {
        for (int var14 : GRID_CELLS) {
            if (var14 == var0) {
                return true;
            }
        }

        return false;
    }

    private ItemStack display(String var1, int var2) {
        return switch (var1) {
            case "trader" -> this.items.trader(662298763);
            case "upgrader" -> this.items.upgrader(2125662274);
            case "fragment" -> this.items.fragment(452582255);
            case "repairkit" -> this.items.repairKit(1231295697);
            case "restorationcore" -> this.items.restorationCore(913365449);
            default -> this.items.restoration(1610991893);
        };
    }

    private ItemStack result(String var1, int var2) {
        BlissRecipes var6 = this.plugin.recipes(942436690);
        return var6.result(var1, 1187831354);
    }

    private void fill(Inventory var1, int var2) {
        Material var4 = Material.GRAY_STAINED_GLASS_PANE;
        String var5 = " ";
        ItemStack var6 = this.named(var4, var5, 120828915);
        int var7 = 0;

        while (true) {
            int var13 = var1.getSize();
            if (var7 >= var13) {
                return;
            }

            var1.setItem(var7, var6);
            var7++;
        }
    }

    private ItemStack named(Material var1, String var2, int var3) {
        ItemStack var4 = new ItemStack(var1);
        ItemMeta var8 = var4.getItemMeta();
        if (var8 != null) {
            Component var17 = Text.legacy(var2, 373192400);
            TextDecoration var20 = TextDecoration.ITALIC;
            Component var18 = var17.decoration(var20, false);
            var8.displayName(var18);
            var4.setItemMeta(var8);
        }

        return var4;
    }

    private ItemStack withHint(ItemStack var1, String[] var2, int var3) {
        ItemMeta var7 = var1.getItemMeta();
        if (var7 != null) {
            ArrayList var15 = new ArrayList();
            ArrayList var8 = var15;

            for (String var12 : var2) {
                Component var32 = Text.legacy(var12, 373192400);
                TextDecoration var5 = TextDecoration.ITALIC;
                Component var33 = var32.decoration(var5, false);
                var8.add(var33);
            }

            var7.lore(var8);
            var1.setItemMeta(var7);
        }

        return var1;
    }

    static {
        String[] var3 = new String[6];
        String var2 = "trader";
        var3[0] = var2;
        String var28 = "upgrader";
        var3[1] = var28;
        String var29 = "fragment";
        var3[2] = var29;
        String var30 = "repairkit";
        var3[3] = var30;
        String var31 = "restorationcore";
        var3[4] = var31;
        String var32 = "restoration";
        var3[5] = var32;
        KEYS = var3;
        int[] var5 = new int[]{10, 11, 12, 13, 14, 15};
        LIST_SLOTS = var5;
        int[] var7 = new int[]{10, 11, 12, 19, 20, 21, 28, 29, 30};
        GRID_CELLS = var7;
    }

    private static final class DetailHolder implements InventoryHolder {
        final String which;
        final boolean editing;

        DetailHolder(String var1, boolean var2, int var3) {
            this.which = var1;
            this.editing = var2;
        }

        public Inventory getInventory() {
            return null;
        }
    }

    private static final class ListHolder implements InventoryHolder {
        public Inventory getInventory() {
            return null;
        }
    }
}
