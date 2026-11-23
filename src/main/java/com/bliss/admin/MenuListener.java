package com.bliss.admin;

import com.bliss.BlissPlugin;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.goldgem.RitualItems;
import com.bliss.honored.JuggernautGemGuardListener;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.BlissItems;
import com.bliss.villager.Text;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitScheduler;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.invoke.MethodHandle;
import java.lang.runtime.ObjectMethods;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.logging.Logger;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.Plugin;

public final class MenuListener implements Listener, CommandExecutor {
    public static final String TITLE = "Bliss Menu";
    private static final int SIZE = 54;
    private static final int PAGES = 3;
    private static final int GIVE_ENERGY_SLOT = 38;
    private static final int GIVE_TRADER_SLOT = 40;
    private static final int GIVE_UPGRADER_SLOT = 42;
    private static final int GIVE_MEGA_MACE_SLOT = 30;
    private static final int GIVE_PRISMATIC_EDGE_SLOT = 49;
    private static final int GOLD_GEM_PAGE1_SLOT = 40;
    private final BlissPlugin plugin;
    private final BlissCoreItems items;
    private final PlayerDataStore players;
    private final Random random;
    private final NamespacedKey fnKey;
    private final File layoutFile;
    private static final Map<String, MenuListener.ExternalButton> EXTERNAL;

    public static void registerExternal(String var0, ItemStack var1, Material var2, Consumer<Player> var3, int var4) {
        Map var5 = EXTERNAL;
        MenuListener.ExternalButton var7 = new MenuListener.ExternalButton(var1, var2, var3);
        var5.put(var0, var7);
    }

    public static void unregisterExternal(String var0) {
        Map var1 = EXTERNAL;
        var1.remove(var0);
    }

    public MenuListener(BlissPlugin var1, BlissCoreItems var2, PlayerDataStore var3, int var4) {
        Random var6 = new Random();
        this.random = var6;
        this.plugin = var1;
        this.items = var2;
        this.players = var3;
        NamespacedKey var18 = new NamespacedKey(var1, "menu_fn");
        this.fnKey = var18;
        File var21 = var1.getDataFolder();
        File var19 = new File(var21, "menu.yml");
        this.layoutFile = var19;
    }

    public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
        if (var1 instanceof Player var9) {
            int var19 = pageArg(var4, 0, 580559153);
            this.openMenu(var9, var19, 2016488517);
            return true;
        } else {
            Component var18 = Text.legacy("&cPlayer only.", 373192400);
            var1.sendMessage(var18);
            return true;
        }
    }

    static int pageArg(String[] var0, int var1, int var2) {
        if (var1 >= 0) {
            int var13 = var0.length;
            if (var1 < var13) {
                try {
                    int var18 = Integer.parseInt(var0[var1]) - 1;
                    int var15 = Math.min(2, var18);
                    return Math.max(0, var15);
                } catch (NumberFormatException var19) {
                }
            }
        }

        return 0;
    }

    public void openMenu(Player var1) {
        this.openMenu(var1, 0, 2016488517);
    }

    public void openMenu(Player var1, int var2, int var3) {
        MenuListener.MenuHolder var4 = new MenuListener.MenuHolder();
        var4.page = var2;
        TextComponent var34 = Component.text("§6Bliss Menu");
        Inventory var8 = Bukkit.createInventory(var4, 54, var34);
        var4.inv = var8;
        ItemStack[] var9 = this.loadLayout(var2, 4514424);
        boolean var25;
        if (var9 != null) {
            var25 = true;
        } else {
            var25 = false;
        }

        if (var9 == null) {
            var9 = this.defaultLayout(var2, 2009056910);
        }

        this.decorate(var9, var2, var25, 463434038);
        var8.setContents(var9);
        var1.openInventory(var8);
    }

    private void decorate(ItemStack[] var1, int var2, boolean var3, int var4) {
        for (int var12 = 0; var12 < 54; var12++) {
            ItemStack var226 = var1[var12];
            String var13 = this.fnOf(var226, 913819619);
            String var23 = "minus";
            if (!var23.equals(var13)) {
                String var25 = "plus";
                if (!var25.equals(var13)) {
                    continue;
                }
            }

            ItemMeta var14 = var1[var12].getItemMeta();
            if (var14 == null || !var14.hasCustomModelData()) {
                String var33 = "minus";
                boolean var15 = var33.equals(var13);
                ItemStack var384 = this.items.bottledEnergy(1532783840);
                String var413;
                if (var15) {
                    var413 = "§c§lᴇɴᴇʀɢʏ −1";
                } else {
                    var413 = "§a§lᴇɴᴇʀɢʏ +1";
                }

                String var414;
                if (var15) {
                    var414 = "§7Click to remove 1 energy";
                } else {
                    var414 = "§7Click to add 1 energy";
                }

                ItemStack var385 = reskinEnergy(var384, var413, var414, 2087955748);
                ItemStack var320 = this.fn(var385, var13, 2086486096);
                var1[var12] = var320;
            }
        }

        for (byte var420 = 0; var420 < 54; var420 += 1) {
            ItemStack var234 = var1[var420];
            String var429 = this.fnOf(var234, 913819619);
            if (var429 != null) {
                switch (var429) {
                    case "trader":
                        ItemStack var54 = var1[var420];
                        String var241 = "§b§lTrade Your Gem";
                        renameKeepLore(var54, var241, 1717620871);
                        break;
                    case "upgrader":
                        ItemStack var52 = var1[var420];
                        String var239 = "§b§lUpgrade Your Gem";
                        renameKeepLore(var52, var239, 1717620871);
                        break;
                    case "restore":
                        ItemStack var50 = var1[var420];
                        String var237 = "§a§lSet Energy to Pristine";
                        renameKeepLore(var50, var237, 1717620871);
                }
            }
        }

        for (byte var421 = 0; var421 < 54; var421 += 1) {
            ItemStack var246 = var1[var421];
            String var430 = this.fnOf(var246, 913819619);
            if (var430 != null && var430.startsWith("gem_")) {
                for (int var441 : neighbours(var421, false, 2007602620)) {
                    ItemStack var250 = var1[var441];
                    if (this.replaceable(var250, 2090198810)) {
                        ItemStack var325 = coloredPane(Material.BLACK_STAINED_GLASS_PANE, 1050367015);
                        var1[var441] = var325;
                    }
                }
            }
        }

        if (var2 == 0) {
            for (Entry var431 : EXTERNAL.entrySet()) {
                String var437 = (String)var431.getKey();
                if (!var437.startsWith("give_")) {
                    String var94 = "gold_gem_awakened";
                    if (!var94.equals(var437)) {
                        ItemStack var442 = ((MenuListener.ExternalButton)var431.getValue()).button();
                        int var16 = -1;

                        for (int var17 = 0; var17 < 54; var17++) {
                            ItemStack var327 = var1[var17];
                            String var256 = this.fnOf(var327, 913819619);
                            if (var437.equals(var256)) {
                                var16 = var17;
                                break;
                            }
                        }

                        if (var16 < 0) {
                            for (byte var445 = 0; var445 < 54; var445 += 1) {
                                ItemStack var110 = var1[var445];
                                if (matchesButton(var110, var442, 731707083)) {
                                    var16 = var445;
                                    break;
                                }
                            }
                        }

                        if (var16 < 0) {
                            var16 = this.injectSlot(var1, var437, 824831343);
                        }

                        if (var16 >= 0) {
                            ItemStack var388 = var442.clone();
                            ItemStack var330 = this.fn(var388, var437, 2086486096);
                            var1[var16] = var330;

                            for (byte var446 = 0; var446 < 54; var446 += 1) {
                                if (var446 != var16) {
                                    ItemStack var332 = var1[var446];
                                    String var265 = this.fnOf(var332, 913819619);
                                    if (!var437.equals(var265)) {
                                        ItemStack var124 = var1[var446];
                                        if (!matchesButton(var124, var442, 731707083)) {
                                            continue;
                                        }
                                    }

                                    var1[var446] = null;
                                }
                            }

                            if (((MenuListener.ExternalButton)var431.getValue()).outline() != null) {
                                for (int var18 : neighbours(var16, false, 2007602620)) {
                                    ItemStack var271 = var1[var18];
                                    if (this.replaceable(var271, 2090198810)) {
                                        ItemStack var339 = coloredPane(((MenuListener.ExternalButton)var431.getValue()).outline(), 1050367015);
                                        var1[var18] = var339;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (var2 == 0) {
            for (byte var423 = 0; var423 < 54; var423 += 1) {
                String var146 = "gold_gem_awakened";
                ItemStack var341 = var1[var423];
                String var275 = this.fnOf(var341, 913819619);
                if (var146.equals(var275)) {
                    var1[var423] = null;
                }
            }

            String var343 = "gem_selector";
            ItemStack var406 = this.items.gemSelector(1614312040);
            this.ensureButton(var1, var343, 40, var406, 164122555);
        }

        if (var2 < 2) {
            String var344 = "page_next";
            String var407 = "§e§lNext Page →";
            String var417 = "§7Open page " + (var2 + 2);
            ItemStack var408 = navButton(var407, var417, 1197285987);
            this.ensureButton(var1, var344, 53, var408, 164122555);
        }

        if (var2 > 0) {
            String var345 = "page_prev";
            String var409 = "§e§l← Previous Page";
            String var419 = "§7Back to page " + var2;
            ItemStack var410 = navButton(var409, var419, 1197285987);
            this.ensureButton(var1, var345, 45, var410, 164122555);
        }

        if (var2 == 1) {
            for (byte var424 = 0; var424 < 54; var424 += 1) {
                int var432 = soulIdOf(var1[var424], 41103518);
                if (var432 > 0) {
                    ItemStack var162 = var1[var424];
                    String var287 = "&d&l" + BlissItems.soulOwner(var432, 904057364);
                    renameKeepLore(var162, var287, 1717620871);
                }
            }

            ItemStack var348 = this.items.bottledEnergy(1532783840);
            var1[38] = var348;
            ItemStack var351 = this.items.trader(662298763);
            var1[40] = var351;
            ItemStack var354 = this.items.upgrader(2125662274);
            var1[42] = var354;
            ItemStack var357 = this.items.megaMace(1601970596);
            var1[30] = var357;
            ItemStack var360 = this.items.prismaticEdge(840003792);
            var1[49] = var360;

            for (byte var425 = 0; var425 < 54; var425 += 1) {
                ItemStack var295 = var1[var425];
                if (this.replaceable(var295, 2090198810)) {
                    ItemStack var363 = coloredPane(Material.GREEN_STAINED_GLASS_PANE, 1050367015);
                    var1[var425] = var363;
                }
            }
        }

        if (var2 == 2 && !var3) {
            for (byte var426 = 1; var426 <= 7; var426 += 1) {
                int var300 = 9 + var426;
                ItemStack var366 = RitualItems.wireFragment(var426, 60990984);
                var1[var300] = var366;
            }

            ItemStack var367 = RitualItems.fragmentCore(1868423911);
            var1[22] = var367;
            MenuListener.ExternalButton var427 = EXTERNAL.get("give_gold_gem_empty");
            MenuListener.ExternalButton var433 = EXTERNAL.get("gold_gem_awakened");
            if (var427 != null) {
                ItemStack var396 = var427.button().clone();
                String var411 = "give_gold_gem_empty";
                ItemStack var369 = this.fn(var396, var411, 2086486096);
                var1[30] = var369;
            }

            if (var433 != null) {
                ItemStack var399 = var433.button().clone();
                String var412 = "gold_gem_awakened";
                ItemStack var371 = this.fn(var399, var412, 2086486096);
                var1[32] = var371;
            }

            for (byte var438 = 0; var438 < 54; var438 += 1) {
                ItemStack var308 = var1[var438];
                if (this.replaceable(var308, 2090198810)) {
                    ItemStack var374 = coloredPane(Material.YELLOW_STAINED_GLASS_PANE, 1050367015);
                    var1[var438] = var374;
                }
            }

            int[] var195 = new int[]{30, 32};
            int[] var439 = var195;
            int var443 = var195.length;

            for (byte var444 = 0; var444 < var443; var444 += 1) {
                for (int var19 : neighbours(var439[var444], false, 2007602620)) {
                    ItemStack var314 = var1[var19];
                    if (this.replaceable(var314, 2090198810)) {
                        ItemStack var379 = coloredPane(Material.ORANGE_STAINED_GLASS_PANE, 1050367015);
                        var1[var19] = var379;
                    }
                }
            }
        }

        for (int var434 : borderSlots(487347562)) {
            ItemStack var317 = var1[var434];
            if (this.replaceable(var317, 2090198810)) {
                ItemStack var382 = coloredPane(Material.BLACK_STAINED_GLASS_PANE, 1050367015);
                var1[var434] = var382;
            }
        }
    }

    private static List<Integer> borderSlots(int var0) {
        ArrayList var1 = new ArrayList();
        ArrayList var3 = var1;

        for (int var5 = 0; var5 < 6; var5++) {
            for (int var6 = 0; var6 < 9; var6++) {
                if (var5 == 0 || var5 == 5 || var6 == 0 || var6 == 8) {
                    int var25 = var5 * 9;
                    Integer var27 = var25 + var6;
                    var3.add(var27);
                }
            }
        }

        return var3;
    }

    private int injectSlot(ItemStack[] var1, String var2, int var3) {
        byte var28 = switch (var2) {
            case "mythic_heretic" -> 48;
            case "gold_gem_awakened" -> 40;
            case "mythic_auratus" -> 50;
            default -> -1;
        };
        if (var28 >= 0 && var28 < 54) {
            ItemStack var37 = var1[var28];
            if (this.replaceable(var37, 2090198810)) {
                return var28;
            }
        }

        for (byte var43 = 53; var43 >= 0; var43 += -1) {
            ItemStack var39 = var1[var43];
            if (this.replaceable(var39, 2090198810)) {
                return var43;
            }
        }

        return -1;
    }

    private void ensureButton(ItemStack[] var1, String var2, int var3, ItemStack var4, int var5) {
        for (int var11 = 0; var11 < 54; var11++) {
            ItemStack var43 = var1[var11];
            String var37 = this.fnOf(var43, 913819619);
            if (var2.equals(var37)) {
                ItemStack var45 = this.fn(var4, var2, 2086486096);
                var1[var11] = var45;
                return;
            }
        }

        int var18;
        label46: {
            if (var3 >= 0 && var3 < 54) {
                ItemStack var32 = var1[var3];
                if (this.replaceable(var32, 2090198810)) {
                    var18 = var3;
                    break label46;
                }
            }

            var18 = -1;
        }

        byte var49 = (byte)var18;
        if (var49 < 0) {
            for (byte var12 = 53; var12 >= 0; var12 += -1) {
                ItemStack var34 = var1[var12];
                if (this.replaceable(var34, 2090198810)) {
                    var49 = var12;
                    break;
                }
            }
        }

        if (var49 >= 0) {
            ItemStack var41 = this.fn(var4, var2, 2086486096);
            var1[var49] = var41;
        }
    }

    private static ItemStack navButton(String var0, String var1, int var2) {
        Material var4 = Material.ARROW;
        ItemStack var3 = new ItemStack(var4);
        ItemMeta var7 = var3.getItemMeta();
        if (var7 != null) {
            Component var17 = Text.legacy(var0, 373192400);
            TextDecoration var23 = TextDecoration.ITALIC;
            Component var18 = var17.decoration(var23, false);
            var7.displayName(var18);
            List var21 = List.of(Text.legacy(var1, 373192400));
            var7.lore(var21);
            var3.setItemMeta(var7);
        }

        return var3;
    }

    private static boolean matchesButton(ItemStack var0, ItemStack var1, int var2) {
        if (var0 != null) {
            Material var11 = var0.getType();
            Material var33 = var1.getType();
            if (var11 == var33) {
                ItemMeta var5 = var0.getItemMeta();
                ItemMeta var6 = var1.getItemMeta();
                if (var5 != null && var6 != null && var5.displayName() != null && var6.displayName() != null) {
                    LegacyComponentSerializer var7 = LegacyComponentSerializer.legacySection();
                    Component var35 = var5.displayName();
                    String var8 = ChatColor.stripColor(var7.serialize(var35));
                    Component var37 = var6.displayName();
                    String var9 = ChatColor.stripColor(var7.serialize(var37));
                    return var8.equals(var9);
                }

                return false;
            }
        }

        return false;
    }

    private static List<Integer> neighbours(int var0, boolean var1, int var2) {
        ArrayList var3 = new ArrayList();
        ArrayList var5 = var3;
        int var6 = var0 / 9;
        int var7 = var0 % 9;

        for (int var8 = -1; var8 <= 1; var8++) {
            for (int var9 = -1; var9 <= 1; var9++) {
                if ((var8 != 0 || var9 != 0) && (var1 || var8 == 0 || var9 == 0)) {
                    int var10 = var6 + var8;
                    int var11 = var7 + var9;
                    if (var10 >= 0 && var10 < 6 && var11 >= 0 && var11 < 9) {
                        int var45 = var10 * 9;
                        Integer var47 = var45 + var11;
                        var5.add(var47);
                    }
                }
            }
        }

        return var5;
    }

    private boolean replaceable(ItemStack var1, int var2) {
        if (var1 != null) {
            Material var6 = var1.getType();
            Material var4 = Material.AIR;
            if (var6 != var4) {
                if (this.fnOf(var1, 913819619) != null) {
                    return false;
                }

                return var1.getType().name().endsWith("_STAINED_GLASS_PANE");
            }
        }

        return true;
    }

    private static ItemStack coloredPane(Material var0, int var1) {
        ItemStack var2 = new ItemStack(var0);
        ItemMeta var5 = var2.getItemMeta();
        if (var5 != null) {
            TextComponent var14 = Component.text(" ");
            var5.displayName(var14);
            var2.setItemMeta(var5);
        }

        return var2;
    }

    private static void renameKeepLore(ItemStack var0, String var1, int var2) {
        if (var0 != null) {
            ItemMeta var7 = var0.getItemMeta();
            if (var7 != null) {
                Component var14 = Text.legacy(var1, 373192400);
                TextDecoration var5 = TextDecoration.ITALIC;
                Component var15 = var14.decoration(var5, false);
                var7.displayName(var15);
                var0.setItemMeta(var7);
            }
        }
    }

    private static ItemStack reskinEnergy(ItemStack var0, String var1, String var2, int var3) {
        ItemMeta var8 = var0.getItemMeta();
        if (var8 != null) {
            Component var16 = Text.legacy(var1, 373192400);
            TextDecoration var6 = TextDecoration.ITALIC;
            Component var17 = var16.decoration(var6, false);
            var8.displayName(var17);
            List var20 = List.of(Text.legacy(var2, 373192400));
            var8.lore(var20);
            var0.setItemMeta(var8);
        }

        return var0;
    }

    private ItemStack[] loadLayout(int var1, int var2) {
        ItemStack[] var5 = this.loadEditedLayout(var1, 1866274571);
        return var5 != null ? var5 : this.loadBakedLayout(var1, 1817123688);
    }

    private static String pageKey(int var0, int var1) {
        String var6;
        if (var0 == 0) {
            var6 = "slots";
        } else {
            var6 = "page" + (var0 + 1);
        }

        return var6;
    }

    private static ItemStack[] readSlots(YamlConfiguration var0, int var1, int var2) {
        String var8 = pageKey(var1, 422440104);
        if (!var0.contains(var8)) {
            return null;
        } else {
            ItemStack[] var9 = new ItemStack[54];
            boolean var10 = false;

            for (int var11 = 0; var11 < 54; var11++) {
                String var31 = var8 + "." + var11;
                ItemStack var30 = var0.getItemStack(var31);
                var9[var11] = var30;
                if (var9[var11] != null) {
                    var10 = true;
                }
            }

            ItemStack[] var26;
            if (var10) {
                var26 = var9;
            } else {
                var26 = null;
            }

            return var26;
        }
    }

    private ItemStack[] loadEditedLayout(int var1, int var2) {
        if (!this.layoutFile.exists()) {
            return null;
        } else {
            YamlConfiguration var9 = YamlConfiguration.loadConfiguration(this.layoutFile);
            return readSlots(var9, var1, 1671674442);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private ItemStack[] loadBakedLayout(int n, int n2) {
        try (InputStream inputStream = this.plugin.getResource("menu_layout.yml")) {
            if (inputStream == null) {
                return null;
            }
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(inputStreamReader);
            return MenuListener.readSlots(yamlConfiguration, n, 1671674442);
        } catch (Exception exception) {
            Logger logger = this.plugin.getLogger();
            logger.warning("menu_layout.yml failed to load: " + exception.getMessage());
            return null;
        }
    }

    private ItemStack[] defaultLayout(int var1, int var2) {
        ItemStack[] var8 = new ItemStack[54];
        ItemStack var9 = pane(1571274796);

        for (int var10 = 0; var10 < 54; var10++) {
            ItemStack var46 = var9.clone();
            var8[var10] = var46;
        }

        if (var1 != 0) {
            return var8;
        } else {
            ItemStack var74 = this.items.trader(662298763);
            String var7 = "trader";
            ItemStack var48 = this.fn(var74, var7, 2086486096);
            var8[4] = var48;
            BlissCoreItems var76 = this.items;
            GemType var109 = GemType.STRENGTH;
            ItemStack var77 = var76.gemDisplay$1463922612(var109, 1977498201);
            String var110 = "gem_1";
            ItemStack var50 = this.fn(var77, var110, 2086486096);
            var8[9] = var50;
            BlissCoreItems var79 = this.items;
            GemType var111 = GemType.FIRE;
            ItemStack var80 = var79.gemDisplay$1463922612(var111, 1977498201);
            String var112 = "gem_2";
            ItemStack var52 = this.fn(var80, var112, 2086486096);
            var8[10] = var52;
            BlissCoreItems var82 = this.items;
            GemType var113 = GemType.WEALTH;
            ItemStack var83 = var82.gemDisplay$1463922612(var113, 1977498201);
            String var114 = "gem_3";
            ItemStack var54 = this.fn(var83, var114, 2086486096);
            var8[11] = var54;
            BlissCoreItems var85 = this.items;
            GemType var115 = GemType.SPEED;
            ItemStack var86 = var85.gemDisplay$1463922612(var115, 1977498201);
            String var116 = "gem_4";
            ItemStack var56 = this.fn(var86, var116, 2086486096);
            var8[12] = var56;
            ItemStack var89 = this.items.upgrader(2125662274);
            String var117 = "upgrader";
            ItemStack var58 = this.fn(var89, var117, 2086486096);
            var8[13] = var58;
            BlissCoreItems var91 = this.items;
            GemType var118 = GemType.PUFF;
            ItemStack var92 = var91.gemDisplay$1463922612(var118, 1977498201);
            String var119 = "gem_5";
            ItemStack var60 = this.fn(var92, var119, 2086486096);
            var8[14] = var60;
            BlissCoreItems var94 = this.items;
            GemType var120 = GemType.ASTRA;
            ItemStack var95 = var94.gemDisplay$1463922612(var120, 1977498201);
            String var121 = "gem_6";
            ItemStack var62 = this.fn(var95, var121, 2086486096);
            var8[15] = var62;
            BlissCoreItems var97 = this.items;
            GemType var122 = GemType.LIFE;
            ItemStack var98 = var97.gemDisplay$1463922612(var122, 1977498201);
            String var123 = "gem_7";
            ItemStack var64 = this.fn(var98, var123, 2086486096);
            var8[16] = var64;
            BlissCoreItems var100 = this.items;
            GemType var124 = GemType.FLUX;
            ItemStack var101 = var100.gemDisplay$1463922612(var124, 1977498201);
            String var125 = "gem_8";
            ItemStack var66 = this.fn(var101, var125, 2086486096);
            var8[17] = var66;
            String var102 = "§c§lᴇɴᴇʀɢʏ −1";
            String var126 = "§7Click to remove 1 energy";
            ItemStack var103 = energyButton(var102, var126, 1650471123);
            String var127 = "minus";
            ItemStack var68 = this.fn(var103, var127, 2086486096);
            var8[21] = var68;
            ItemStack var106 = this.items.restoration(1610991893);
            String var128 = "restore";
            ItemStack var70 = this.fn(var106, var128, 2086486096);
            var8[22] = var70;
            String var107 = "§a§lᴇɴᴇʀɢʏ +1";
            String var129 = "§7Click to add 1 energy";
            ItemStack var108 = energyButton(var107, var129, 1650471123);
            String var130 = "plus";
            ItemStack var72 = this.fn(var108, var130, 2086486096);
            var8[23] = var72;
            return var8;
        }
    }

    private ItemStack fn(ItemStack var1, String var2, int var3) {
        ItemMeta var8 = var1.getItemMeta();
        if (var8 != null) {
            PersistentDataContainer var12 = var8.getPersistentDataContainer();
            NamespacedKey var16 = this.fnKey;
            PersistentDataType var6 = PersistentDataType.STRING;
            var12.set(var16, var6, var2);
            var1.setItemMeta(var8);
        }

        return var1;
    }

    private String fnOf(ItemStack var1, int var2) {
        if (var1 != null && var1.getItemMeta() != null) {
            PersistentDataContainer var10 = var1.getItemMeta().getPersistentDataContainer();
            NamespacedKey var14 = this.fnKey;
            PersistentDataType var5 = PersistentDataType.STRING;
            return (String)var10.get(var14, var5);
        } else {
            return null;
        }
    }

    private static ItemStack pane(int var0) {
        Material var2 = Material.GRAY_STAINED_GLASS_PANE;
        ItemStack var1 = new ItemStack(var2);
        ItemMeta var4 = var1.getItemMeta();
        if (var4 != null) {
            TextComponent var13 = Component.text(" ");
            var4.displayName(var13);
            var1.setItemMeta(var4);
        }

        return var1;
    }

    private static ItemStack energyButton(String var0, String var1, int var2) {
        Material var4 = Material.NAUTILUS_SHELL;
        ItemStack var3 = new ItemStack(var4);
        ItemMeta var7 = var3.getItemMeta();
        if (var7 != null) {
            Component var17 = Text.legacy(var0, 373192400);
            TextDecoration var23 = TextDecoration.ITALIC;
            Component var18 = var17.decoration(var23, false);
            var7.displayName(var18);
            List var21 = List.of(Text.legacy(var1, 373192400));
            var7.lore(var21);
            var3.setItemMeta(var7);
        }

        return var3;
    }

    @EventHandler
    public void onClick(InventoryClickEvent var1) {
        if (var1.getView().getTopInventory().getHolder() instanceof MenuListener.MenuHolder var7) {
            if (var1.getWhoClicked() instanceof Player var196) {
                var1.setCancelled(true);
                if (var1.getRawSlot() >= 0 && var1.getRawSlot() < 54) {
                    ItemStack var127 = var1.getCurrentItem();
                    String var197 = this.fnOf(var127, 913819619);
                    if (var197 == null) {
                        ItemStack var198 = var1.getCurrentItem();
                        if (var7.page >= 1 && var198 != null && !this.replaceable(var198, 2090198810)) {
                            this.giveOne(var196, var198, 2047451683);
                        }
                    } else {
                        PlayerDataStore var36 = this.players;
                        UUID var129 = var196.getUniqueId();
                        PlayerData var10 = var36.get(var129, 1264010596);
                        switch (var197) {
                            case "trader":
                                GemType var66 = var10.gem(1413186337);
                                Random var147 = this.random;
                                GemType var200 = GemType.randomDifferent(var66, var147);
                                var10.setGem(var200, 2137390487);
                                this.regrantGem(var196, var10, 1205974125);
                                Component var153 = Text.legacy("\ud83d\udd2e &bYou have traded your current gem to " + var200.coloredName(), 373192400);
                                var196.sendMessage(var153);
                                break;
                            case "upgrader":
                                boolean var179;
                                if (!var10.tier2(629984660)) {
                                    var179 = true;
                                } else {
                                    var179 = false;
                                }

                                var10.setTier2(var179, 1757992594);
                                this.regrantGem(var196, var10, 1205974125);
                                break;
                            case "minus":
                                if (GlobalState.forPlayer(var196, 1433087673).noEnergyMode(340448998)) {
                                    Component var166 = Text.legacy("&cThe energy system is disabled on this server.", 373192400);
                                    var196.sendMessage(var166);
                                } else if (var10.energy(1160853318) > 1) {
                                    var10.addEnergy(-1, 514979476);
                                    this.regrantGem(var196, var10, 1205974125);
                                }
                                break;
                            case "restore":
                                if (GlobalState.forPlayer(var196, 1433087673).noEnergyMode(340448998)) {
                                    Component var137 = Text.legacy("&cThe energy system is disabled on this server.", 373192400);
                                    var196.sendMessage(var137);
                                } else {
                                    var10.setEnergy(6, 319922676);
                                    this.regrantGem(var196, var10, 1205974125);
                                }
                                break;
                            case "plus":
                                if (GlobalState.forPlayer(var196, 1433087673).noEnergyMode(340448998)) {
                                    Component var158 = Text.legacy("&cThe energy system is disabled on this server.", 373192400);
                                    var196.sendMessage(var158);
                                } else if (var10.energy(1160853318) < 11) {
                                    var10.addEnergy(1, 514979476);
                                    this.regrantGem(var196, var10, 1205974125);
                                }
                                break;
                            case "gem_selector":
                                ItemStack var186 = this.items.gemSelector(1614312040);
                                this.giveOne(var196, var186, 2047451683);
                                break;
                            case "page_next":
                                int var202 = var7.page;
                                BukkitScheduler var94 = Bukkit.getScheduler();
                                BlissPlugin var168 = this.plugin;
                                Runnable var192 = () -> {
                                    int var9 = var202 + 1;
                                    int var8 = Math.min(2, var9);
                                    this.openMenu(var196, var8, 2016488517);
                                };
                                var94.runTask(var168, var192);
                                break;
                            case "page_prev":
                                int var201 = var7.page;
                                BukkitScheduler var74 = Bukkit.getScheduler();
                                BlissPlugin var156 = this.plugin;
                                Runnable var188 = () -> {
                                    int var9 = var201 - 1;
                                    int var8 = Math.max(0, var9);
                                    this.openMenu(var196, var8, 2016488517);
                                };
                                var74.runTask(var156, var188);
                                break;
                            default:
                                if (var197.startsWith("gem_")) {
                                    try {
                                        GemType var144 = GemType.byId(Integer.parseInt(var197.substring(4)));
                                        var10.setGem(var144, 2137390487);
                                        this.regrantGem(var196, var10, 1205974125);
                                    } catch (NumberFormatException var203) {
                                    }

                                    return;
                                }

                                Map var56 = EXTERNAL;
                                MenuListener.ExternalButton var13 = (MenuListener.ExternalButton)var56.get(var197);
                                if (var13 != null) {
                                    Consumer<Player> var61 = var13.action();
                                    var61.accept(var196);
                                }
                        }
                    }
                }
            }
        }
    }

    private void regrantGem(Player var1, PlayerData var2, int var3) {
        ItemStack[] var8 = var1.getInventory().getContents();
        int var9 = -1;
        int var10 = 0;

        while (true) {
            int var33 = var8.length;
            if (var10 >= var33) {
                GemType var26 = var2.gem(1413186337);
                GemType var37 = GemType.NONE;
                if (var26 != var37 && !JuggernautGemGuardListener.isJuggernaut(var1, 859003337)) {
                    BlissCoreItems var30 = this.items;
                    GemType var39 = var2.gem(1413186337);
                    boolean var42 = var2.tier2(629984660);
                    int var44 = var2.energy(1160853318);
                    ItemStack var45 = var30.gemItem(var39, var42, var44, 256965471);
                    BlissCoreItems.giveGemOrHold(var1, var9, var45, 2133739666);
                }

                return;
            }

            if (var8[var10] != null && BlissCoreItems.gemIdOf(var8[var10], 382441079) > 0) {
                if (var9 < 0) {
                    var9 = var10;
                }

                PlayerInventory var24 = var1.getInventory();
                var24.setItem(var10, null);
            }

            var10++;
        }
    }

    private static int soulIdOf(ItemStack var0, int var1) {
        if (var0 != null) {
            Material var7 = var0.getType();
            Material var3 = Material.PITCHER_POD;
            if (var7 == var3) {
                ItemMeta var4 = var0.getItemMeta();
                if (var4 != null && var4.hasCustomModelData()) {
                    int var5 = var4.getCustomModelData() - 359;
                    int var20;
                    if (var5 >= 1 && var5 <= 3) {
                        var20 = var5;
                    } else {
                        var20 = -1;
                    }

                    return var20;
                }

                return -1;
            }
        }

        return -1;
    }

    private void giveOne(Player var1, ItemStack var2, int var3) {
        int var8 = soulIdOf(var2, 41103518);
        ItemStack var49;
        if (var8 > 0 && this.plugin.villagerItems(1841893765) != null) {
            BlissItems var40 = this.plugin.villagerItems(1841893765);
            var49 = var40.soul(var8, 888486940);
        } else {
            BlissCoreItems var17 = this.items;
            if (var17.isBottledEnergy(var2, 1793320558)) {
                var49 = this.items.bottledEnergy(1532783840);
            } else {
                var49 = var2.clone();
            }
        }

        var49.setAmount(1);
        PlayerInventory var23 = var1.getInventory();
        ItemStack[] var44 = new ItemStack[]{var49};

        for (ItemStack var10 : var23.addItem(var44).values()) {
            World var33 = var1.getWorld();
            Location var46 = var1.getLocation();
            var33.dropItemNaturally(var46, var10);
        }
    }

    static {
        ConcurrentHashMap var0 = new ConcurrentHashMap();
        EXTERNAL = var0;
    }

    public static record ExternalButton(ItemStack button, Material outline, Consumer<Player> action) {
    }

    private static final class MenuHolder implements InventoryHolder {
        private Inventory inv;
        private int page;

        public Inventory getInventory() {
            return this.inv;
        }
    }
}
