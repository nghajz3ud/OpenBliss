package com.bliss.goldgem;

import com.bliss.BlissPlugin;
import com.bliss.villager.Text;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class GoldenDreamMenu implements Listener {
    private static final int IMPORT_SLOT = 11;
    private static final int RESET_SLOT = 15;
    private final BlissPlugin plugin;
    private final GoldenDreamWorld world;
    private final Map<UUID, String> armed;

    public GoldenDreamMenu(BlissPlugin var1, GoldenDreamWorld var2, int var3) {
        HashMap var5 = new HashMap();
        this.armed = var5;
        this.plugin = var1;
        this.world = var2;
    }

    public void open(Player var1, int var2) {
        Map var8 = this.armed;
        UUID var17 = var1.getUniqueId();
        var8.remove(var17);
        GoldenDreamMenu.Holder var10 = new GoldenDreamMenu.Holder();
        Component var22 = Text.legacy("&8Golden Dream", 373192400);
        Inventory var7 = Bukkit.createInventory(var10, 27, var22);
        var10.inv = var7;
        this.render(var7, null, 691223540);
        var1.openInventory(var7);
    }

    private void render(Inventory var1, String var2, int var3) {
        Material var7 = Material.FILLED_MAP;
        String var8 = "&6Import world data";
        String var9 = "import";
        boolean var21 = var9.equals(var2);
        String var24 = "&7Copy the world folder at";
        String var11 = "&f/goldendream_import";
        String var12 = "&7in over the memory overworld.";
        String var13 = "&7Nether and end are left as-is.";
        List var25 = List.of(var24, var11, var12, var13);
        ItemStack var16 = this.button(var7, var8, var21, var25, 748104360);
        var1.setItem(11, var16);
        Material var19 = Material.BARRIER;
        String var20 = "&cReset world + player data";
        String var22 = "reset";
        boolean var23 = var22.equals(var2);
        String var27 = "&7Delete and regenerate the pocket,";
        String var29 = "&7overworld, nether and end,";
        String var30 = "&7and drop every dream record.";
        String var31 = "&7A brand-new dream.";
        List var28 = List.of(var27, var29, var30, var31);
        ItemStack var18 = this.button(var19, var20, var23, var28, 748104360);
        var1.setItem(15, var18);
    }

    private ItemStack button(Material var1, String var2, boolean var3, List<String> var4, int var5) {
        Material var47;
        if (var3) {
            var47 = Material.GLOWSTONE_DUST;
        } else {
            var47 = var1;
        }

        ItemStack var6 = new ItemStack(var47);
        ItemMeta var9 = var6.getItemMeta();
        Component var36 = noItalic(Text.legacy(var2, 373192400), 1315256300);
        var9.displayName(var36);
        ArrayList var16 = new ArrayList();
        ArrayList var10 = var16;

        for (String var12 : var4) {
            Component var39 = noItalic(Text.legacy(var12, 373192400), 1315256300);
            var10.add(var39);
        }

        TextComponent var40 = Component.empty();
        var10.add(var40);
        String var46;
        if (var3) {
            var46 = "&e&lClick again to CONFIRM";
        } else {
            var46 = "&8Click to arm";
        }

        Component var43 = noItalic(Text.legacy(var46, 373192400), 1315256300);
        var10.add(var43);
        var9.lore(var10);
        var6.setItemMeta(var9);
        return var6;
    }

    private static Component noItalic(Component var0, int var1) {
        TextDecoration var3 = TextDecoration.ITALIC;
        return var0.decoration(var3, false);
    }

    @EventHandler
    public void onClick(InventoryClickEvent var1) {
        if (var1.getInventory().getHolder() instanceof GoldenDreamMenu.Holder) {
            var1.setCancelled(true);
            if (var1.getWhoClicked() instanceof Player var5) {
                if (var1.getClickedInventory() != null && !(var1.getClickedInventory().getHolder() instanceof Player)) {
                    int var65 = var1.getRawSlot();
                    String var50;
                    if (var65 == 11) {
                        var50 = "import";
                    } else if (var65 == 15) {
                        var50 = "reset";
                    } else {
                        var50 = null;
                    }

                    if (var50 != null) {
                        UUID var8 = var5.getUniqueId();
                        Map var53 = this.armed;
                        Object var54 = var53.get(var8);
                        if (var50.equals(var54)) {
                            Map var34 = this.armed;
                            var34.remove(var8);
                            var5.closeInventory();
                            if (var50.equals("import")) {
                                GoldenDreamWorld var40 = this.world;
                                var40.importOverworld(var5, 189351067);
                            } else {
                                GoldenDreamWorld var43 = this.world;
                                var43.resetAll(var5, 83591313);
                            }
                        } else {
                            Map var46 = this.armed;
                            var46.put(var8, var50);
                            Inventory var61 = var1.getInventory();
                            this.render(var61, var50, 691223540);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent var1) {
        if (var1.getInventory().getHolder() instanceof GoldenDreamMenu.Holder) {
            var1.setCancelled(true);
        }
    }

    private static final class Holder implements InventoryHolder {
        private Inventory inv;

        public Inventory getInventory() {
            return this.inv;
        }
    }
}
