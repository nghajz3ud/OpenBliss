package com.bliss.dragongames;

import com.bliss.core.AtomicYml;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.logging.Logger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionType;

public final class DragonGamesShop implements CommandExecutor, Listener {
    private static final Component TITLE = Text.legacy("&7Shop", 373192400);
    private static final Component EDIT_TITLE = Text.legacy("&cShop Editor", 373192400);
    private static final int ROWS = 6;
    private static final int PRICE_STEP = 5;
    private static final int PRICE_BIG_STEP = 25;
    private static final int NEW_OFFER_PRICE = 5;
    private final Plugin plugin;
    private final DragonGamesManager games;
    private final BlissCoreItems items;
    private final Map<Integer, DragonGamesShop.Offer> offers;
    private final File file;
    private boolean fileBroken;

    public DragonGamesShop(Plugin var1, DragonGamesManager var2, BlissCoreItems var3, int var4) {
        HashMap var6 = new HashMap();
        this.offers = var6;
        this.fileBroken = false;
        this.plugin = var1;
        this.games = var2;
        this.items = var3;
        File var22 = var1.getDataFolder();
        File var21 = new File(var22, "dragongamesshop.yml");
        this.file = var21;
        this.defaults(483400315);
        this.load(1491858686);
    }

    private void defaults(int var1) {
        Map var11 = this.offers;
        Integer var49 = 11;
        Material var7 = Material.TOTEM_OF_UNDYING;
        ItemStack var6 = new ItemStack(var7);
        String var130 = "&6Totem";
        String var176 = "&c75$";
        ItemStack var98 = named(var6, var130, var176, 2117364219);
        Material var8 = Material.TOTEM_OF_UNDYING;
        ItemStack var131 = new ItemStack(var8);
        List var132 = List.of(var131);
        DragonGamesShop.Offer var4 = new DragonGamesShop.Offer(75, var98, var132);
        var11.put(var49, var4);
        Map var14 = this.offers;
        Integer var51 = 12;
        ItemStack var100 = splash(PotionType.TURTLE_MASTER, 24159640);
        String var133 = "&5Turtle Master";
        String var177 = "&c50$";
        ItemStack var101 = named(var100, var133, var177, 2117364219);
        List var136 = List.of(splash(PotionType.TURTLE_MASTER, 24159640));
        DragonGamesShop.Offer var74 = new DragonGamesShop.Offer(50, var101, var136);
        var14.put(var51, var74);
        Map var17 = this.offers;
        Integer var53 = 13;
        Material var178 = Material.COBWEB;
        ItemStack var102 = new ItemStack(var178);
        String var137 = "&7Webs";
        String var179 = "&c30$";
        ItemStack var103 = named(var102, var137, var179, 2117364219);
        Material var192 = Material.COBWEB;
        ItemStack var138 = new ItemStack(var192, 64);
        List var139 = List.of(var138);
        DragonGamesShop.Offer var75 = new DragonGamesShop.Offer(30, var103, var139);
        var17.put(var53, var75);
        Map var20 = this.offers;
        Integer var55 = 14;
        Material var180 = Material.GOLDEN_APPLE;
        ItemStack var104 = new ItemStack(var180);
        String var140 = "&6Gap";
        String var181 = "&c15$";
        ItemStack var105 = named(var104, var140, var181, 2117364219);
        Material var193 = Material.GOLDEN_APPLE;
        ItemStack var141 = new ItemStack(var193);
        List var142 = List.of(var141);
        DragonGamesShop.Offer var76 = new DragonGamesShop.Offer(15, var105, var142);
        var20.put(var55, var76);
        Map var23 = this.offers;
        Integer var57 = 15;
        ItemStack var107 = splash(PotionType.STRENGTH, 24159640);
        String var143 = "&6Strength";
        String var182 = "&c5$";
        ItemStack var108 = named(var107, var143, var182, 2117364219);
        List var146 = List.of(splash(PotionType.STRENGTH, 24159640));
        DragonGamesShop.Offer var77 = new DragonGamesShop.Offer(5, var108, var146);
        var23.put(var57, var77);
        Map var26 = this.offers;
        Integer var59 = 20;
        ItemStack var109 = DragonGamesItems.extraHeart$1999829859(1128735326);
        String var147 = "&4+2 Hearts";
        String var183 = "&c75$";
        ItemStack var110 = named(var109, var147, var183, 2117364219);
        List var150 = List.of(DragonGamesItems.extraHeart(2, 613905086));
        DragonGamesShop.Offer var78 = new DragonGamesShop.Offer(75, var110, var150);
        var26.put(var59, var78);
        Map var29 = this.offers;
        Integer var61 = 22;
        ItemStack var113 = this.items.trader(662298763);
        String var151 = "&c30$";
        ItemStack var114 = lore(var113, var151, 709007938);
        List var155 = List.of(this.items.trader(662298763));
        DragonGamesShop.Offer var79 = new DragonGamesShop.Offer(30, var114, var155);
        var29.put(var61, var79);
        Map var32 = this.offers;
        Integer var63 = 23;
        Material var184 = Material.EXPERIENCE_BOTTLE;
        ItemStack var115 = new ItemStack(var184);
        String var156 = "&aExperience Bottle 128";
        String var185 = "&c15$";
        ItemStack var116 = named(var115, var156, var185, 2117364219);
        Material var194 = Material.EXPERIENCE_BOTTLE;
        ItemStack var157 = new ItemStack(var194, 64);
        Material var198 = Material.EXPERIENCE_BOTTLE;
        ItemStack var186 = new ItemStack(var198, 64);
        List var158 = List.of(var157, var186);
        DragonGamesShop.Offer var80 = new DragonGamesShop.Offer(15, var116, var158);
        var32.put(var63, var80);
        Map var35 = this.offers;
        Integer var65 = 24;
        ItemStack var118 = splash(PotionType.SWIFTNESS, 24159640);
        String var159 = "&bSpeed";
        String var187 = "&c5$";
        ItemStack var119 = named(var118, var159, var187, 2117364219);
        List var162 = List.of(splash(PotionType.SWIFTNESS, 24159640));
        DragonGamesShop.Offer var81 = new DragonGamesShop.Offer(5, var119, var162);
        var35.put(var65, var81);
        Map var38 = this.offers;
        Integer var67 = 29;
        Material var188 = Material.ENCHANTED_GOLDEN_APPLE;
        ItemStack var120 = new ItemStack(var188);
        String var163 = "&c50$";
        ItemStack var122 = hideAll(lore(var120, var163, 709007938), 1893558110);
        Material var195 = Material.ENCHANTED_GOLDEN_APPLE;
        ItemStack var164 = new ItemStack(var195);
        List var165 = List.of(var164);
        DragonGamesShop.Offer var82 = new DragonGamesShop.Offer(50, var122, var165);
        var38.put(var67, var82);
        Map var41 = this.offers;
        Integer var69 = 32;
        ItemStack var123 = DragonGamesItems.trackingCompass(321030622);
        String var166 = "&c75$";
        ItemStack var124 = lore(var123, var166, 709007938);
        List var168 = List.of(DragonGamesItems.trackingCompass(321030622));
        DragonGamesShop.Offer var83 = new DragonGamesShop.Offer(75, var124, var168);
        var41.put(var69, var83);
        Map var44 = this.offers;
        Integer var71 = 33;
        ItemStack var126 = splash(PotionType.FIRE_RESISTANCE, 24159640);
        String var169 = "&6Fire Resistance";
        String var189 = "&c5$";
        ItemStack var127 = named(var126, var169, var189, 2117364219);
        List var172 = List.of(splash(PotionType.FIRE_RESISTANCE, 24159640));
        DragonGamesShop.Offer var84 = new DragonGamesShop.Offer(5, var127, var172);
        var44.put(var71, var84);
        Map var47 = this.offers;
        Integer var73 = 38;
        Material var190 = Material.SHULKER_BOX;
        ItemStack var128 = new ItemStack(var190);
        String var173 = "&7Shulker Box";
        String var191 = "&c75$";
        ItemStack var129 = named(var128, var173, var191, 2117364219);
        Material var196 = Material.SHULKER_BOX;
        ItemStack var174 = new ItemStack(var196);
        List var175 = List.of(var174);
        DragonGamesShop.Offer var85 = new DragonGamesShop.Offer(75, var129, var175);
        var47.put(var73, var85);
    }

    private void load(int var1) {
        if (this.file.exists()) {
            YamlConfiguration var22 = new YamlConfiguration();
            YamlConfiguration var8 = var22;

            try {
                File var87 = this.file;
                var8.load(var87);
            } catch (Exception var116) {
                this.fileBroken = true;
                Logger var85 = this.plugin.getLogger();
                String var110 = "dragongamesshop.yml could not be read ("
                    + var116.getMessage()
                    + ") - the shipped shop is used and the editor is locked until the file is fixed or deleted";
                var85.severe(var110);
                return;
            }

            if (var22.isConfigurationSection("slots")) {
                HashMap var26 = new HashMap();
                HashMap var9 = var26;

                for (String var11 : var22.getConfigurationSection("slots").getKeys(false)) {
                    int var12;
                    try {
                        var12 = Integer.parseInt(var11);
                    } catch (NumberFormatException var117) {
                        continue;
                    }

                    if (var12 >= 0 && var12 < 54) {
                        String var114 = "slots." + var11;
                        String var111 = var114 + ".cost";
                        int var93 = var8.getInt(var111, 0);
                        int var14 = Math.max(0, var93);
                        ArrayList var45 = new ArrayList();
                        ArrayList var15 = var45;
                        String var95 = var114 + ".goods";
                        List var16 = var8.getList(var95);
                        if (var16 != null) {
                            for (Object var19 : var16) {
                                if (var19 instanceof ItemStack) {
                                    ItemStack var17 = (ItemStack)var19;
                                    Material var60 = var17.getType();
                                    Material var96 = Material.AIR;
                                    if (var60 != var96) {
                                        var15.add(var17);
                                    }
                                }
                            }
                        }

                        if (var15.isEmpty()) {
                            Logger var67 = this.plugin.getLogger();
                            String var99 = "dragongamesshop.yml: slot " + var11 + " sells nothing readable - skipped";
                            var67.warning(var99);
                        } else {
                            String var101 = var114 + ".display";
                            ItemStack var115 = var8.getItemStack(var101);
                            if (var115 == null) {
                                ItemStack var73 = (ItemStack)var15.get(0);
                                var115 = displayFor(var73, var14, 265276360);
                            }

                            Integer var105 = var12;
                            DragonGamesShop.Offer var112 = new DragonGamesShop.Offer(var14, var115, var15);
                            var9.put(var105, var112);
                        }
                    }
                }

                this.offers.clear();
                Map var80 = this.offers;
                var80.putAll(var9);
            }
        }
    }

    private void save(int var1) {
        if (this.fileBroken) {
            this.plugin.getLogger().warning("dragongamesshop.yml: not saved - the file on disk is unreadable, fix or delete it first");
        } else {
            YamlConfiguration var9 = new YamlConfiguration();
            YamlConfiguration var4 = var9;
            var9.createSection("slots");

            for (Entry var6 : this.offers.entrySet()) {
                String var7 = "slots." + var6.getKey();
                String var43 = var7 + ".cost";
                Integer var52 = ((DragonGamesShop.Offer)var6.getValue()).cost();
                var4.set(var43, var52);
                String var45 = var7 + ".display";
                ItemStack var56 = ((DragonGamesShop.Offer)var6.getValue()).display();
                var4.set(var45, var56);
                String var47 = var7 + ".goods";
                List var60 = ((DragonGamesShop.Offer)var6.getValue()).goods();
                var4.set(var47, var60);
            }

            try {
                File var38 = this.file;
                AtomicYml.save(var4, var38, 1347473142);
            } catch (IOException var62) {
                Logger var22 = this.plugin.getLogger();
                String var41 = "dragongamesshop.yml: " + var62.getMessage();
                var22.warning(var41);
            }
        }
    }

    public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
        if (var1 instanceof Player var7) {
            if (!this.games.isRunning(320515049)) {
                Component var25 = Text.legacy("&cThe Dragon Games are not running.", 373192400);
                var7.sendMessage(var25);
                return true;
            } else {
                DragonGamesManager var17 = this.games;
                UUID var27 = var7.getUniqueId();
                if (!var17.isParticipant(var27, 187258544)) {
                    Component var30 = Text.legacy("&cOnly Dragon Games participants can use the shop.", 373192400);
                    var7.sendMessage(var30);
                    return true;
                } else {
                    this.open(var7, 28185871);
                    return true;
                }
            }
        } else {
            Component var32 = Text.legacy("&cPlayer only.", 373192400);
            var1.sendMessage(var32);
            return true;
        }
    }

    private void open(Player var1, int var2) {
        UUID var37 = var1.getUniqueId();
        DragonGamesShop.ShopHolder var3 = new DragonGamesShop.ShopHolder(var37, 816668978);
        Component var38 = TITLE;
        Inventory var5 = Bukkit.createInventory(var3, 54, var38);
        Material var39 = Material.GRAY_STAINED_GLASS_PANE;
        ItemStack var10 = new ItemStack(var39);
        String var28 = "&7 ";
        ItemStack var6 = named(var10, var28, null, 2117364219);
        int var7 = 0;

        while (true) {
            int var30 = var5.getSize();
            if (var7 >= var30) {
                for (Entry var8 : this.offers.entrySet()) {
                    int var34 = (Integer)var8.getKey();
                    ItemStack var45 = ((DragonGamesShop.Offer)var8.getValue()).display().clone();
                    var5.setItem(var34, var45);
                }

                var1.openInventory(var5);
                return;
            }

            var5.setItem(var7, var6);
            var7++;
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent var1) {
        if (var1.getInventory().getHolder() instanceof DragonGamesShop.ShopHolder var6) {
            var1.setCancelled(true);
            if (var1.getWhoClicked() instanceof Player var85) {
                UUID var25 = var85.getUniqueId();
                UUID var62 = var6.owner;
                if (var25.equals(var62)) {
                    if (var1.getClickedInventory() != null) {
                        Inventory var30 = var1.getClickedInventory();
                        Inventory var64 = var1.getInventory();
                        if (var30.equals(var64)) {
                            ClickType var33 = var1.getClick();
                            ClickType var65 = ClickType.LEFT;
                            if (var33 != var65) {
                                ClickType var35 = var1.getClick();
                                ClickType var66 = ClickType.RIGHT;
                                if (var35 != var66) {
                                    return;
                                }
                            }

                            Map var37 = this.offers;
                            Integer var69 = var1.getSlot();
                            DragonGamesShop.Offer var86 = (DragonGamesShop.Offer)var37.get(var69);
                            if (var86 == null) {
                                return;
                            }

                            if (this.games.isRunning(320515049)) {
                                DragonGamesManager var45 = this.games;
                                UUID var71 = var85.getUniqueId();
                                if (var45.isParticipant(var71, 187258544)) {
                                    DragonGamesManager var49 = this.games;
                                    UUID var73 = var85.getUniqueId();
                                    int var80 = var86.cost();
                                    if (!var49.spend(var73, var80, 145324854)) {
                                        int var77 = var86.cost();
                                        DragonGamesManager var82 = this.games;
                                        UUID var84 = var85.getUniqueId();
                                        int var83 = var82.points(var84, 77304185);
                                        Component var79 = Text.legacy(
                                            "&cNot enough points — that costs &2" + var77 + "$&c, you have &2" + var83 + "$&c.", 373192400
                                        );
                                        var85.sendMessage(var79);
                                        return;
                                    }

                                    for (ItemStack var10 : var86.goods()) {
                                        ItemStack var75 = var10.clone();
                                        BlissCoreItems.giveOrDrop(var85, var75, 41085121);
                                    }

                                    return;
                                }
                            }

                            var85.closeInventory();
                            return;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent var1) {
        if (var1.getInventory().getHolder() instanceof DragonGamesShop.ShopHolder) {
            var1.setCancelled(true);
        }

        if (var1.getInventory().getHolder() instanceof DragonGamesShop.EditHolder) {
            for (int var23 : var1.getRawSlots()) {
                int var27 = var1.getInventory().getSize();
                if (var23 < var27) {
                    var1.setCancelled(true);
                    return;
                }
            }
        }
    }

    public void closeAll(int var1) {
        for (Player var4 : Bukkit.getOnlinePlayers()) {
            InventoryHolder var5 = var4.getOpenInventory().getTopInventory().getHolder();
            if (var5 instanceof DragonGamesShop.ShopHolder || var5 instanceof DragonGamesShop.EditHolder) {
                var4.closeInventory();
            }
        }
    }

    public void openEditor(Player var1, int var2) {
        if (this.fileBroken) {
            Component var19 = Text.legacy(
                "&cdragongamesshop.yml could not be read - fix or delete it (then restart) before editing, or the shipped shop would overwrite it.", 373192400
            );
            var1.sendMessage(var19);
        } else {
            UUID var27 = var1.getUniqueId();
            DragonGamesShop.EditHolder var11 = new DragonGamesShop.EditHolder(var27, 970737072);
            Component var28 = EDIT_TITLE;
            Inventory var7 = Bukkit.createInventory(var11, 54, var28);
            int var8 = 0;

            while (true) {
                int var22 = var7.getSize();
                if (var8 >= var22) {
                    var1.openInventory(var7);
                    Component var25 = Text.legacy(
                        "&cShop Editor &7— put an item on a slot to sell it (a filled shulker sells its contents); left/right click: price ±5 (shift ±25); Q: remove. Saved as you go.",
                        373192400
                    );
                    var1.sendMessage(var25);
                    return;
                }

                ItemStack var30 = this.editorItem(var8, 303385352);
                var7.setItem(var8, var30);
                var8++;
            }
        }
    }

    private ItemStack editorItem(int var1, int var2) {
        Map var9 = this.offers;
        Integer var38 = var1;
        DragonGamesShop.Offer var5 = (DragonGamesShop.Offer)var9.get(var38);
        if (var5 == null) {
            return null;
        } else {
            ItemStack var6 = var5.display().clone();
            ItemMeta var7 = var6.getItemMeta();
            if (var7 == null) {
                return var6;
            } else {
                ArrayList var20 = new ArrayList();
                Component var42 = DragonGamesItems.noItalic("&c" + var5.cost() + "$", 1188798615);
                var20.add(var42);
                Component var47 = DragonGamesItems.noItalic("&8sells: " + describe(var5.goods(), 1207453719), 1188798615);
                var20.add(var47);
                TextComponent var48 = Component.empty();
                var20.add(var48);
                Component var50 = DragonGamesItems.noItalic("&eLeft / Right: &7price +5 / -5 &8(shift: 25)", 1188798615);
                var20.add(var50);
                Component var52 = DragonGamesItems.noItalic("&eItem on the cursor: &7this slot sells it instead", 1188798615);
                var20.add(var52);
                Component var54 = DragonGamesItems.noItalic("&eQ (drop): &7remove this offer", 1188798615);
                var20.add(var54);
                var7.lore(var20);
                var6.setItemMeta(var7);
                return var6;
            }
        }
    }

    private static String describe(List<ItemStack> var0, int var1) {
        StringBuilder var2 = new StringBuilder();
        StringBuilder var5 = var2;

        for (ItemStack var7 : var0) {
            if (var5.length() > 0) {
                var5.append(", ");
            }

            int var36 = var7.getAmount();
            var5.append(var36).append("x ");
            ItemMeta var8 = var7.getItemMeta();
            if (var8 != null && var8.hasDisplayName()) {
                PlainTextComponentSerializer var43 = PlainTextComponentSerializer.plainText();
                Component var46 = var8.displayName();
                String var44 = var43.serialize(var46);
                var5.append(var44);
            } else {
                String var42 = var7.getType().name().toLowerCase().replace('_', ' ');
                var5.append(var42);
            }
        }

        return var5.toString();
    }

    @EventHandler
    public void onEditClick(InventoryClickEvent var1) {
        if (var1.getInventory().getHolder() instanceof DragonGamesShop.EditHolder var9) {
            if (var1.getWhoClicked() instanceof Player var153) {
                UUID var30 = var153.getUniqueId();
                UUID var89 = var9.owner;
                if (var30.equals(var89) && var153.hasPermission("bliss.admin")) {
                    boolean var88;
                    label65: {
                        if (var1.getClickedInventory() != null) {
                            Inventory var37 = var1.getClickedInventory();
                            Inventory var92 = var1.getInventory();
                            if (var37.equals(var92)) {
                                var88 = true;
                                break label65;
                            }
                        }

                        var88 = false;
                    }

                    if (var88) {
                        var1.setCancelled(true);
                        int var12 = var1.getSlot();
                        ItemStack var13 = var1.getCursor();
                        if (var13 != null) {
                            Material var47 = var13.getType();
                            Material var94 = Material.AIR;
                            if (var47 != var94) {
                                ItemStack var155 = var13.clone();
                                Map var69 = this.offers;
                                Integer var111 = var12;
                                DragonGamesShop.Offer var15 = (DragonGamesShop.Offer)var69.get(var111);
                                int var73;
                                if (var15 != null) {
                                    var73 = var15.cost();
                                } else {
                                    var73 = 5;
                                }

                                Map var75 = this.offers;
                                Integer var113 = var12;
                                ItemStack var150 = displayFor(var155, var73, 265276360);
                                List var152 = List.of(var155);
                                DragonGamesShop.Offer var126 = new DragonGamesShop.Offer(var73, var150, var152);
                                var75.put(var113, var126);
                                this.save(290120545);
                                Inventory var79 = var1.getInventory();
                                ItemStack var128 = this.editorItem(var12, 303385352);
                                var79.setItem(var12, var128);
                                String var131 = describe(List.of(var155), 1207453719);
                                Component var117 = Text.legacy("&aSlot " + var12 + " now sells &f" + var131 + " &afor &2" + var73 + "$&a.", 373192400);
                                var153.sendMessage(var117);
                                return;
                            }
                        }

                        Map var49 = this.offers;
                        Integer var96 = var12;
                        DragonGamesShop.Offer var14 = (DragonGamesShop.Offer)var49.get(var96);
                        if (var14 != null) {
                            switch (var1.getClick()) {
                                case LEFT:
                                    int var137 = var14.cost() + 5;
                                    this.reprice(var12, var14, var137, 1916736324);
                                    break;
                                case RIGHT:
                                    int var143 = var14.cost() - 5;
                                    this.reprice(var12, var14, var143, 1916736324);
                                    break;
                                case SHIFT_LEFT:
                                    int var134 = var14.cost() + 25;
                                    this.reprice(var12, var14, var134, 1916736324);
                                    break;
                                case SHIFT_RIGHT:
                                    int var140 = var14.cost() - 25;
                                    this.reprice(var12, var14, var140, 1916736324);
                                    break;
                                case DROP:
                                case CONTROL_DROP:
                                    Map var56 = this.offers;
                                    Integer var101 = var12;
                                    var56.remove(var101);
                                    this.save(290120545);
                                    Component var104 = Text.legacy("&7Slot " + var12 + " removed from the shop.", 373192400);
                                    var153.sendMessage(var104);
                                    break;
                                default:
                                    return;
                            }

                            Inventory var61 = var1.getInventory();
                            ItemStack var121 = this.editorItem(var12, 303385352);
                            var61.setItem(var12, var121);
                        }
                    } else {
                        if (!var1.isShiftClick()) {
                            InventoryAction var85 = var1.getAction();
                            InventoryAction var118 = InventoryAction.COLLECT_TO_CURSOR;
                            if (var85 != var118) {
                                return;
                            }
                        }

                        var1.setCancelled(true);
                    }
                } else {
                    var1.setCancelled(true);
                }
            }
        }
    }

    private void reprice(int var1, DragonGamesShop.Offer var2, int var3, int var4) {
        int var22 = Math.max(0, var3);
        Map var13 = this.offers;
        Integer var17 = var1;
        ItemStack var18 = var2.display();
        ItemStack var19 = withPrice(var18, var22, 82557640);
        List var21 = var2.goods();
        DragonGamesShop.Offer var7 = new DragonGamesShop.Offer(var22, var19, var21);
        var13.put(var17, var7);
        this.save(290120545);
    }

    private static ItemStack displayFor(ItemStack var0, int var1, int var2) {
        return withPrice(var0, var1, 82557640);
    }

    private static ItemStack withPrice(ItemStack var0, int var1, int var2) {
        ItemStack var5 = var0.clone();
        ItemMeta var6 = var5.getItemMeta();
        if (var6 != null) {
            List var17 = List.of(DragonGamesItems.noItalic("&c" + var1 + "$", 1188798615));
            var6.lore(var17);
            var5.setItemMeta(var6);
        }

        return var5;
    }

    private static ItemStack splash(PotionType var0, int var1) {
        Material var3 = Material.SPLASH_POTION;
        ItemStack var2 = new ItemStack(var3);
        PotionMeta var5 = (PotionMeta)var2.getItemMeta();
        var5.setBasePotionType(var0);
        var2.setItemMeta(var5);
        return var2;
    }

    private static ItemStack named(ItemStack var0, String var1, String var2, int var3) {
        ItemMeta var6 = var0.getItemMeta();
        Component var14 = DragonGamesItems.noItalic(var1, 1188798615);
        var6.displayName(var14);
        if (var2 != null) {
            List var17 = List.of(DragonGamesItems.noItalic(var2, 1188798615));
            var6.lore(var17);
        }

        var0.setItemMeta(var6);
        return var0;
    }

    private static ItemStack lore(ItemStack var0, String var1, int var2) {
        ItemMeta var5 = var0.getItemMeta();
        List var12 = List.of(DragonGamesItems.noItalic(var1, 1188798615));
        var5.lore(var12);
        var0.setItemMeta(var5);
        return var0;
    }

    private static ItemStack hideAll(ItemStack var0, int var1) {
        ItemMeta var4 = var0.getItemMeta();
        ItemFlag[] var3 = ItemFlag.values();
        var4.addItemFlags(var3);
        var0.setItemMeta(var4);
        return var0;
    }

    private static final class EditHolder implements InventoryHolder {
        final UUID owner;

        EditHolder(UUID var1, int var2) {
            this.owner = var1;
        }

        public Inventory getInventory() {
            return null;
        }
    }

    private static record Offer(int cost, ItemStack display, List<ItemStack> goods) {
    }

    private static final class ShopHolder implements InventoryHolder {
        final UUID owner;

        ShopHolder(UUID var1, int var2) {
            this.owner = var1;
        }

        public Inventory getInventory() {
            return null;
        }
    }
}
