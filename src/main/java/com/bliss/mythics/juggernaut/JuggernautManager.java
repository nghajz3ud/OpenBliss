package com.bliss.mythics.juggernaut;

import com.bliss.BlissPlugin;
import com.bliss.core.AtomicYml;
import com.bliss.honored.HonoredCompass;
import com.bliss.mythics.MythicItems;
import com.bliss.pedestal.PedestalState;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.ShulkerBox;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitScheduler;
import com.bliss.mythics.juggernaut.SummoningCinematic;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import org.bukkit.block.BlockState;

public final class JuggernautManager {
    public static final double JUGGERNAUT_MAX_HEALTH = 40.0;
    public static final double NORMAL_MAX_HEALTH = 20.0;
    private static final int INVIS_TICKS = 19200;
    private static final long KIT_WINDOW_MS = 5000L;
    private final Plugin plugin;
    private final NamespacedKey keyBond;
    private final NamespacedKey keyDowned;
    private final NamespacedKey keyKitItem;
    private final Map<UUID, Long> kitWindowUntil;
    private final Map<JuggernautManager.Bond, Location> spots;
    private final File spotsFile;
    private final Set<UUID> downed;
    private final Map<UUID, JuggernautManager.Bond> downedBond;
    private final Set<UUID> pendingRevive;
    private volatile UUID revivalInFlight;
    private final File downedFile;
    private final File legacyKitFile;
    private final Map<JuggernautManager.Bond, JuggernautManager.Kit> kits;
    private final EnumSet<JuggernautManager.Bond> serverOwned;
    public static final String FALLEN_MESSAGE = "Fallen Juggernaut. Wait till you are revived";

    private File kitFile(JuggernautManager.Bond var1, int var2) {
        File var8 = this.plugin.getDataFolder();
        String var9 = var1.name();
        Locale var6 = Locale.ROOT;
        String var11 = "juggernaut_kit_" + var9.toLowerCase(var6) + ".yml";
        return new File(var8, var11);
    }

    public JuggernautManager(Plugin var1, int var2) {
        ConcurrentHashMap var4 = new ConcurrentHashMap();
        this.kitWindowUntil = var4;
        EnumMap var24 = new EnumMap<>(JuggernautManager.Bond.class);
        this.spots = var24;
        KeySetView var25 = ConcurrentHashMap.newKeySet();
        this.downed = var25;
        ConcurrentHashMap var26 = new ConcurrentHashMap();
        this.downedBond = var26;
        KeySetView var27 = ConcurrentHashMap.newKeySet();
        this.pendingRevive = var27;
        EnumMap var28 = new EnumMap<>(JuggernautManager.Bond.class);
        this.kits = var28;
        EnumSet var30 = EnumSet.noneOf(JuggernautManager.Bond.class);
        this.serverOwned = var30;
        this.plugin = var1;
        NamespacedKey var33 = NamespacedKey.fromString("blissmythics:juggernaut_bond");
        this.keyBond = var33;
        NamespacedKey var35 = NamespacedKey.fromString("blissmythics:juggernaut_downed");
        this.keyDowned = var35;
        NamespacedKey var37 = NamespacedKey.fromString("blissmythics:juggernaut_kit_item");
        this.keyKitItem = var37;
        File var43 = var1.getDataFolder();
        File var38 = new File(var43, "juggernaut_spots.yml");
        this.spotsFile = var38;
        File var45 = var1.getDataFolder();
        File var39 = new File(var45, "juggernaut_downed.yml");
        this.downedFile = var39;
        File var47 = var1.getDataFolder();
        File var40 = new File(var47, "juggernaut_kit.yml");
        this.legacyKitFile = var40;
        this.loadSpots(663507309);
        this.loadDowned(567978668);
        this.loadTemplates(2059151459);
    }

    public UUID revivalInFlight(int var1) {
        return this.revivalInFlight;
    }

    public void setRevivalInFlight(UUID var1, int var2) {
        this.revivalInFlight = var1;
        this.saveDowned(622359629);
    }

    public void clearRevivalInFlight(int var1) {
        this.revivalInFlight = null;
        this.saveDowned(622359629);
    }

    public void setSpot(JuggernautManager.Bond var1, Location var2, int var3) {
        Map var7 = this.spots;
        Location var10 = var2.clone();
        var7.put(var1, var10);
        this.saveSpots(463964841);
    }

    public Location getSpot(JuggernautManager.Bond var1, int var2) {
        Map var5 = this.spots;
        return (Location)var5.get(var1);
    }

    public void clearSpot(JuggernautManager.Bond var1, int var2) {
        Map var5 = this.spots;
        var5.remove(var1);
        this.saveSpots(463964841);
    }

    public int summonToSpots(int var1) {
        int var6 = 0;

        for (Player var8 : Bukkit.getOnlinePlayers()) {
            JuggernautManager.Bond var9 = this.bondOf(var8, 1322009532);
            if (var9 != null) {
                Map var22 = this.spots;
                Location var10 = (Location)var22.get(var9);
                if (var10 != null) {
                    var8.teleport(var10);
                }

                JuggernautManager.Bond var5 = JuggernautManager.Bond.AURATUS;
                boolean var33;
                if (var9 == var5) {
                    var33 = true;
                } else {
                    var33 = false;
                }

                this.playSummonCinematic(var8, var33, 640918837);
                var6++;
            }
        }

        return var6;
    }

    private void loadSpots(int var1) {
        if (this.spotsFile.exists()) {
            YamlConfiguration var5 = YamlConfiguration.loadConfiguration(this.spotsFile);

            for (JuggernautManager.Bond var9 : JuggernautManager.Bond.values()) {
                String var33 = var9.name();
                if (var5.contains(var33)) {
                    String var35 = var9.name();
                    Location var10 = var5.getLocation(var35);
                    if (var10 != null) {
                        Map var29 = this.spots;
                        var29.put(var9, var10);
                    }
                }
            }
        }
    }

    // $VF: Handled exception range with multiple entry points by splitting it
    // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
    private void saveSpots(int var1) {
        YamlConfiguration var2 = new YamlConfiguration();
        YamlConfiguration var4 = var2;

        for (Entry var6 : this.spots.entrySet()) {
            String var38 = ((JuggernautManager.Bond)var6.getKey()).name();
            Object var39 = var6.getValue();
            var4.set(var38, var39);
        }

        try {
            if (!this.spotsFile.getParentFile().exists()) {
                this.spotsFile.getParentFile().mkdirs();
            }
            File var31 = this.spotsFile;
            var4.save(var31);
        } catch (IOException var40) {
            Logger var25 = this.plugin.getLogger();
            String var34 = "Failed to save juggernaut_spots.yml: " + var40.getMessage();
            var25.warning(var34);
        }
    }

    private void loadDowned(int var1) {
        if (this.downedFile.exists()) {
            YamlConfiguration var5 = YamlConfiguration.loadConfiguration(this.downedFile);

            for (String var7 : var5.getStringList("downed")) {
                try {
                    Set var24 = this.downed;
                    UUID var66 = UUID.fromString(var7);
                    var24.add(var66);
                } catch (Exception var90) {
                }
            }

            for (String var81 : var5.getStringList("pendingRevive")) {
                try {
                    Set var36 = this.pendingRevive;
                    UUID var69 = UUID.fromString(var81);
                    var36.add(var69);
                } catch (Exception var89) {
                }
            }

            ConfigurationSection var80 = var5.getConfigurationSection("downedBond");
            if (var80 != null) {
                for (String var86 : var80.getKeys(false)) {
                    try {
                        JuggernautManager.Bond var9 = JuggernautManager.Bond.parse(var80.getString(var86));
                        if (var9 != null) {
                            Map var62 = this.downedBond;
                            UUID var77 = UUID.fromString(var86);
                            var62.put(var77, var9);
                        }
                    } catch (Exception var88) {
                    }
                }
            }

            String var72 = "revivalInFlight";
            String var4 = "";
            String var83 = var5.getString(var72, var4);
            if (var83 != null && !var83.isEmpty()) {
                try {
                    UUID var74 = UUID.fromString(var83);
                    this.revivalInFlight = var74;
                } catch (IllegalArgumentException var87) {
                }
            }
        }
    }

    // $VF: Handled exception range with multiple entry points by splitting it
    // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
    private void saveDowned(int var1) {
        YamlConfiguration var2 = new YamlConfiguration();
        YamlConfiguration var4 = var2;
        ArrayList var9 = new ArrayList();
        ArrayList var5 = var9;

        for (UUID var7 : this.downed) {
            String var58 = var7.toString();
            var5.add(var58);
        }

        ArrayList var20 = new ArrayList();
        ArrayList var83 = var20;

        for (UUID var87 : this.pendingRevive) {
            String var72 = var87.toString();
            var83.add(var72);
        }

        String var59 = "downed";
        var2.set(var59, var5);
        String var60 = "pendingRevive";
        var2.set(var60, var83);
        String var61 = "revivalInFlight";
        String var82;
        if (this.revivalInFlight == null) {
            var82 = null;
        } else {
            var82 = this.revivalInFlight.toString();
        }

        var2.set(var61, var82);

        for (Entry var8 : this.downedBond.entrySet()) {
            String var65 = "downedBond." + var8.getKey();
            String var81 = ((JuggernautManager.Bond)var8.getValue()).name();
            var4.set(var65, var81);
        }

        try {
            if (!this.downedFile.getParentFile().exists()) {
                this.downedFile.getParentFile().mkdirs();
            }
            File var67 = this.downedFile;
            var4.save(var67);
        } catch (IOException var86) {
            Logger var51 = this.plugin.getLogger();
            String var70 = "Failed to save juggernaut_downed.yml: " + var86.getMessage();
            var51.warning(var70);
        }
    }

    public void markKitWindow(Player var1, int var2) {
        Map var9 = this.kitWindowUntil;
        UUID var11 = var1.getUniqueId();
        Long var14 = System.currentTimeMillis() + 5000L;
        var9.put(var11, var14);
    }

    public boolean inKitWindow(Player var1, int var2) {
        Map var8 = this.kitWindowUntil;
        UUID var17 = var1.getUniqueId();
        Long var7 = (Long)var8.get(var17);
        if (var7 != null) {
            long var13 = var7;
            long var5 = System.currentTimeMillis();
            if (var13 > var5) {
                return true;
            }
        }

        return false;
    }

    public boolean isJuggernaut(Player var1, int var2) {
        boolean var6;
        if (this.bondOf(var1, 1322009532) != null) {
            var6 = true;
        } else {
            var6 = false;
        }

        return var6;
    }

    public JuggernautManager.Bond bondOf(Player var1, int var2) {
        PersistentDataContainer var7 = var1.getPersistentDataContainer();
        NamespacedKey var13 = this.keyBond;
        PersistentDataType var5 = PersistentDataType.STRING;
        String var6 = (String)var7.get(var13, var5);
        JuggernautManager.Bond var12;
        if (var6 == null) {
            var12 = null;
        } else {
            var12 = JuggernautManager.Bond.parse(var6);
        }

        return var12;
    }

    public boolean isDowned(Player var1, int var2) {
        Set var5 = this.downed;
        UUID var7 = var1.getUniqueId();
        return var5.contains(var7);
    }

    public boolean isDowned(UUID var1, int var2) {
        Set var5 = this.downed;
        return var5.contains(var1);
    }

    public void designate(Player var1, JuggernautManager.Bond var2, int var3) {
        PersistentDataContainer var8 = var1.getPersistentDataContainer();
        NamespacedKey var22 = this.keyBond;
        PersistentDataType var6 = PersistentDataType.STRING;
        String var42 = var2.name();
        var8.set(var22, var6, var42);
        PersistentDataContainer var10 = var1.getPersistentDataContainer();
        NamespacedKey var24 = this.keyDowned;
        var10.remove(var24);
        GameMode var12 = var1.getGameMode();
        GameMode var25 = GameMode.SPECTATOR;
        if (var12 == var25) {
            GameMode var26 = GameMode.SURVIVAL;
            var1.setGameMode(var26);
        }

        this.setMaxHealth(var1, 40.0, 273960762);
        var1.setHealth(40.0);
        this.applyInvisibility(var1, 591488408);
        this.markKitWindow(var1, 716918370);
        this.applyKit(var1, var2, true, 1742946347);
        PlayerInventory var20 = var1.getInventory();
        ItemStack var34 = this.boundGemItem(var2, 1591294875);
        var20.setItemInOffHand(var34);
        JuggernautManager.Bond var44 = JuggernautManager.Bond.AURATUS;
        boolean var41;
        if (var2 == var44) {
            var41 = true;
        } else {
            var41 = false;
        }

        this.playSummonCinematic(var1, var41, 640918837);
    }

    public void playSummonCinematic(Player var1, boolean var2, int var3) {
        Plugin var8 = this.plugin;
        SummoningCinematic var4 = new SummoningCinematic(var8, var1, var2, 1097061430);
        var4.start(898909888);
    }

    public void undesignate(Player var1, int var2) {
        PersistentDataContainer var7 = var1.getPersistentDataContainer();
        NamespacedKey var30 = this.keyBond;
        var7.remove(var30);
        PersistentDataContainer var9 = var1.getPersistentDataContainer();
        NamespacedKey var32 = this.keyDowned;
        var9.remove(var32);
        this.stripKitItems(var1, 951725465);
        Set var12 = this.downed;
        UUID var35 = var1.getUniqueId();
        var12.remove(var35);
        Map var15 = this.downedBond;
        UUID var37 = var1.getUniqueId();
        var15.remove(var37);
        Set var18 = this.pendingRevive;
        UUID var39 = var1.getUniqueId();
        var18.remove(var39);
        this.saveDowned(622359629);
        this.setMaxHealth(var1, 20.0, 273960762);
        if (var1.getHealth() > 20.0) {
            var1.setHealth(20.0);
        }

        GameMode var28 = var1.getGameMode();
        GameMode var43 = GameMode.SPECTATOR;
        if (var28 == var43) {
            GameMode var44 = GameMode.SURVIVAL;
            var1.setGameMode(var44);
        }
    }

    public void reapply(Player var1, int var2) {
        if (this.isJuggernaut(var1, 1070464021)) {
            Set var9 = this.pendingRevive;
            UUID var17 = var1.getUniqueId();
            if (var9.remove(var17)) {
                this.applyReviveKit(var1, 931208490);
                this.saveDowned(622359629);
            } else {
                if (!this.isDowned(var1, 584775972)) {
                    this.setMaxHealth(var1, 40.0, 273960762);
                }
            }
        }
    }

    public void markDowned(Player var1, int var2) {
        Set var7 = this.downed;
        UUID var16 = var1.getUniqueId();
        var7.add(var16);
        JuggernautManager.Bond var6 = this.bondOf(var1, 1322009532);
        if (var6 != null) {
            Map var13 = this.downedBond;
            UUID var19 = var1.getUniqueId();
            var13.put(var19, var6);
        }

        this.saveDowned(622359629);
    }

    public JuggernautManager.Bond downedBondOf(UUID var1, int var2) {
        Map var5 = this.downedBond;
        return (JuggernautManager.Bond)var5.get(var1);
    }

    public UUID downedOfBond(JuggernautManager.Bond var1, int var2) {
        for (UUID var6 : this.downed) {
            Map var16 = this.downedBond;
            Object var17 = var16.get(var6);
            if (var17 == var1) {
                return var6;
            }
        }

        return null;
    }

    public boolean revive(UUID var1, int var2) {
        Set var6 = this.downed;
        if (!var6.remove(var1)) {
            return false;
        } else {
            Map var10 = this.downedBond;
            var10.remove(var1);
            Set var13 = this.pendingRevive;
            var13.add(var1);
            this.saveDowned(622359629);
            Player var5 = Bukkit.getPlayer(var1);
            if (var5 != null && var5.isOnline() && !var5.isDead()) {
                Set var24 = this.pendingRevive;
                var24.remove(var1);
                this.applyReviveKit(var5, 931208490);
                this.saveDowned(622359629);
            }

            return true;
        }
    }

    public boolean unban(UUID var1, int var2) {
        Set var6 = this.downed;
        boolean var5 = var6.remove(var1);
        Map var9 = this.downedBond;
        var9.remove(var1);
        Set var12 = this.pendingRevive;
        var12.remove(var1);
        this.saveDowned(622359629);
        return var5;
    }

    private void applyReviveKit(Player var1, int var2) {
        GameMode var4 = GameMode.SURVIVAL;
        var1.setGameMode(var4);
        this.setMaxHealth(var1, 40.0, 273960762);
        var1.setHealth(40.0);
        this.applyInvisibility(var1, 591488408);
        this.markKitWindow(var1, 716918370);
        JuggernautManager.Bond var26 = this.bondOf(var1, 1322009532);
        this.applyKit(var1, var26, true, 1742946347);
        PlayerInventory var14 = var1.getInventory();
        JuggernautManager.Bond var28 = this.bondOf(var1, 1322009532);
        ItemStack var23 = this.boundGemItem(var28, 1591294875);
        var14.setItemInOffHand(var23);
        this.returnToPedestal(var1, 343104761);
    }

    private void returnToPedestal(Player var1, int var2) {
        PedestalState var11 = pedestalState(1106481066);
        if (var11 != null) {
            Location var12 = var11.pedestalLoc1(1941968134);
            if (var12 != null && var12.getWorld() != null) {
                Location var13 = var12.clone().add(0.5, 0.05, 0.5);
                float var34 = var1.getLocation().getYaw();
                var13.setYaw(var34);
                float var37 = var1.getLocation().getPitch();
                var13.setPitch(var37);
                if (!this.plugin.isEnabled()) {
                    var1.teleport(var13);
                } else {
                    BukkitScheduler var28 = Bukkit.getScheduler();
                    Plugin var39 = this.plugin;
                    Runnable var41 = () -> {
                        if (var1.isOnline()) {
                            var1.teleport(var13);
                        }
                    };
                    var28.runTask(var39, var41);
                }
            }
        }
    }

    private static PedestalState pedestalState(int var0) {
        Plugin var3 = Bukkit.getPluginManager().getPlugin("BlissPlugin");
        PedestalState var11;
        if (var3 instanceof BlissPlugin) {
            var11 = ((BlissPlugin)var3).pedestalState(590646643);
        } else {
            var11 = null;
        }

        return var11;
    }

    public ItemStack boundGemItem(JuggernautManager.Bond var1, int var2) {
        JuggernautManager.Bond var4 = JuggernautManager.Bond.HERETIC;
        ItemStack var5;
        if (var1 == var4) {
            var5 = MythicItems.bloodGem(1434502583);
        } else {
            var5 = MythicItems.aegisGem(1323939072);
        }

        return var5;
    }

    public boolean isBoundGem(ItemStack var1, JuggernautManager.Bond var2, int var3) {
        JuggernautManager.Bond var5 = JuggernautManager.Bond.HERETIC;
        boolean var8;
        if (var2 == var5) {
            var8 = MythicItems.isMythicBloodGem(var1, 824514243);
        } else {
            var8 = MythicItems.isMythicAegisGem(var1, 73386651);
        }

        return var8;
    }

    public boolean carriesBond(Player var1, JuggernautManager.Bond var2) {
        for (ItemStack var9 : var1.getInventory().getStorageContents()) {
            if (this.isBoundGem(var9, var2, 2048720761)) {
                return true;
            }
        }

        ItemStack var27 = var1.getInventory().getItemInOffHand();
        return this.isBoundGem(var27, var2, 2048720761);
    }

    private void applyKit(Player var1, JuggernautManager.Bond var2, boolean var3, int var4) {
        Map var17 = this.kits;
        boolean var8 = var17.containsKey(var2);
        JuggernautManager.Kit var71;
        if (var8) {
            Map var21 = this.kits;
            var71 = (JuggernautManager.Kit)var21.get(var2);
        } else {
            Map var69 = this.kits;
            JuggernautManager.Bond var95 = JuggernautManager.Bond.HERETIC;
            var71 = (JuggernautManager.Kit)var69.get(var95);
        }

        if (var71 == null) {
            TrimMaterial var96 = trimFor(var2, 1423294723);
            this.equipCursedArmour(var1, var96, 611856153);
            this.giveWeaponsAndConsumables(var1, 1110621658);
            if (var3) {
                this.stockEnderChest(var1, 1985253319);
            }
        } else {
            PlayerInventory var10 = var1.getInventory();
            ItemStack[] var11 = cloneArr(var71.armor, 146077270);
            if (!var8) {
                JuggernautManager.Bond var76 = JuggernautManager.Bond.AURATUS;
                if (var2 == var76) {
                    ItemStack[] var12 = var11;
                    int var13 = var11.length;

                    for (int var14 = 0; var14 < var13; var14++) {
                        retrimGold(var12[var14], 1358333122);
                    }
                }
            }

            for (ItemStack var103 : var11) {
                this.markKitItem(var103, 1656563390);
            }

            var10.setArmorContents(var11);
            ItemStack[] var82 = cloneArr(var71.inv, 146077270);
            var10.setStorageContents(var82);
            ItemStack var91;
            if (var71.offhand == null) {
                var91 = null;
            } else {
                var91 = var71.offhand.clone();
            }

            var10.setItemInOffHand(var91);
            if (var3) {
                ItemStack[] var98 = cloneArr(var71.ender, 146077270);

                for (ItemStack var16 : var98) {
                    this.markKitItem(var16, 1656563390);
                }

                Inventory var61 = var1.getEnderChest();
                var61.setContents(var98);
            }
        }
    }

    private void markKitItem(ItemStack var1, int var2) {
        if (var1 != null) {
            ItemMeta var7 = var1.getItemMeta();
            if (var7 != null) {
                PersistentDataContainer var12 = var7.getPersistentDataContainer();
                NamespacedKey var15 = this.keyKitItem;
                PersistentDataType var5 = PersistentDataType.BYTE;
                Byte var17 = (byte)1;
                var12.set(var15, var5, var17);
                var1.setItemMeta(var7);
            }
        }
    }

    private ItemStack marked(ItemStack var1, int var2) {
        this.markKitItem(var1, 1656563390);
        return var1;
    }

    private boolean isKitItem(ItemStack var1, int var2) {
        if (var1 == null) {
            return false;
        } else {
            ItemMeta var6 = var1.getItemMeta();
            if (var6 != null) {
                PersistentDataContainer var11 = var6.getPersistentDataContainer();
                NamespacedKey var15 = this.keyKitItem;
                PersistentDataType var5 = PersistentDataType.BYTE;
                if (var11.has(var15, var5)) {
                    return true;
                }
            }

            return false;
        }
    }

    private void stripKitItems(Player var1, int var2) {
        PlayerInventory var6 = var1.getInventory();
        ItemStack[] var7 = var6.getArmorContents();
        boolean var8 = false;
        int var9 = 0;

        while (true) {
            int var38 = var7.length;
            if (var9 >= var38) {
                if (var8) {
                    var6.setArmorContents(var7);
                }

                Inventory var52 = var1.getEnderChest();
                ItemStack[] var10 = var52.getContents();
                boolean var11 = false;
                int var12 = 0;

                while (true) {
                    int var41 = var10.length;
                    if (var12 >= var41) {
                        if (var11) {
                            var52.setContents(var10);
                        }

                        return;
                    }

                    ItemStack var44 = var10[var12];
                    if (this.isKitItem(var44, 8390448)) {
                        var10[var12] = null;
                        var11 = true;
                    }

                    var12++;
                }
            }

            ItemStack var47 = var7[var9];
            if (this.isKitItem(var47, 8390448)) {
                var7[var9] = null;
                var8 = true;
            }

            var9++;
        }
    }

    private static void retrimGold(ItemStack var0, int var1) {
        if (var0 != null) {
            ItemMeta var6 = var0.getItemMeta();
            if (var6 instanceof ArmorMeta var7) {
                ArmorTrim var8 = var7.getTrim();
                TrimPattern var23;
                if (var8 != null) {
                    var23 = var8.getPattern();
                } else {
                    var23 = TrimPattern.SILENCE;
                }

                TrimMaterial var4 = TrimMaterial.GOLD;
                ArmorTrim var3 = new ArmorTrim(var4, var23);
                var7.setTrim(var3);
                var0.setItemMeta(var6);
            }
        }
    }

    private static ItemStack[] cloneArr(ItemStack[] var0, int var1) {
        ItemStack[] var6 = new ItemStack[var0.length];
        int var7 = 0;

        while (true) {
            int var14 = var0.length;
            if (var7 >= var14) {
                return var6;
            }

            ItemStack var19;
            if (var0[var7] == null) {
                var19 = null;
            } else {
                var19 = var0[var7].clone();
            }

            var6[var7] = var19;
            var7++;
        }
    }

    public boolean hasKitTemplate(JuggernautManager.Bond var1) {
        Map var4 = this.kits;
        if (!var4.containsKey(var1)) {
            Map var7 = this.kits;
            JuggernautManager.Bond var10 = JuggernautManager.Bond.HERETIC;
            if (!var7.containsKey(var10)) {
                return false;
            }
        }

        return true;
    }

    public boolean hasServerKit(JuggernautManager.Bond var1, int var2) {
        EnumSet var5 = this.serverOwned;
        return var5.contains(var1);
    }

    public void loadKitForEditing(Player var1, JuggernautManager.Bond var2, int var3) {
        this.applyKit(var1, var2, true, 1742946347);
    }

    public boolean saveKitTemplate(JuggernautManager.Bond var1, ItemStack[] var2, ItemStack[] var3, ItemStack var4, ItemStack[] var5, int var6) {
        YamlConfiguration var7 = new YamlConfiguration();
        YamlConfiguration var10 = var7;
        String var42 = "hasTemplate";
        Boolean var69 = true;
        var7.set(var42, var69);
        String var43 = "armor";
        writeItems(var7, var43, var2, 4, 1427078663);
        String var44 = "inv";
        writeItems(var7, var44, var3, 36, 1427078663);
        String var45 = "ender";
        writeItems(var7, var45, var5, 27, 1427078663);
        if (var4 != null && !var4.getType().isAir()) {
            String var46 = "offhand";
            var7.set(var46, var4);
        }

        File var11 = this.kitFile(var1, 1360845828);

        try {
            AtomicYml.save(var10, var11, 1347473142);
        } catch (IOException var83) {
            Logger var28 = this.plugin.getLogger();
            String var50 = var11.getName();
            String var75 = var83.getMessage();
            String var51 = var50 + ": " + var75;
            var28.warning(var51);
            return false;
        }

        JuggernautManager.Kit var30 = new JuggernautManager.Kit();
        ItemStack[] var54 = cloneArr(fit(var2, 4, 202055608), 146077270);
        var30.armor = var54;
        ItemStack[] var57 = cloneArr(fit(var3, 36, 202055608), 146077270);
        var30.inv = var57;
        ItemStack[] var60 = cloneArr(fit(var5, 27, 202055608), 146077270);
        var30.ender = var60;
        ItemStack var65;
        if (var4 != null && !var4.getType().isAir()) {
            var65 = var4.clone();
        } else {
            var65 = null;
        }

        var30.offhand = var65;
        Map var36 = this.kits;
        var36.put(var1, var30);
        EnumSet var39 = this.serverOwned;
        var39.add(var1);
        return true;
    }

    public boolean resetKitTemplate(JuggernautManager.Bond var1, int var2) {
        File var5 = this.kitFile(var1, 1360845828);
        if (!var5.exists()) {
            return false;
        } else if (!var5.delete()) {
            Logger var19 = this.plugin.getLogger();
            String var24 = var5.getName() + ": could not delete the server's kit";
            var19.warning(var24);
            return false;
        } else {
            Map var13 = this.kits;
            var13.remove(var1);
            this.loadTemplates(2059151459);
            return true;
        }
    }

    private static ItemStack[] fit(ItemStack[] var0, int var1, int var2) {
        ItemStack[] var7 = new ItemStack[var1];
        if (var0 != null) {
            int var8 = 0;

            while (true) {
                int var17 = var0.length;
                int var15 = Math.min(var1, var17);
                if (var8 >= var15) {
                    break;
                }

                ItemStack var19 = var0[var8];
                var7[var8] = var19;
                var8++;
            }
        }

        return var7;
    }

    private static void writeItems(YamlConfiguration var0, String var1, ItemStack[] var2, int var3, int var4) {
        if (var2 != null) {
            int var8 = 0;

            while (true) {
                int var23 = var2.length;
                int var19 = Math.min(var3, var23);
                if (var8 >= var19) {
                    return;
                }

                ItemStack var9 = var2[var8];
                if (var9 != null && !var9.getType().isAir()) {
                    String var22 = var1 + "." + var8;
                    var0.set(var22, var9);
                }

                var8++;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void loadTemplates(int n) {
        this.kits.clear();
        this.serverOwned.clear();
        Bond[] bondArray = Bond.values();
        int n2 = bondArray.length;
        int n3 = 0;
        while (true) {
            File file;
            int n4;
            int n5;
            if ((n5 = ++n3) < (n4 = n2)) {
                JuggernautManager juggernautManager = this;
                Bond[] bondArray2 = bondArray;
                int n6 = n3;
                Bond bond = bondArray2[n6];
                Bond bond2 = bond;
                file = juggernautManager.kitFile(bond2, 1360845828);
                if (!file.exists()) continue;
                Kit kit = JuggernautManager.readKit(YamlConfiguration.loadConfiguration((File)file), 227343112);
                if (kit != null) {
                    Map<Bond, Kit> map = this.kits;
                    Bond bond3 = bond;
                    Kit kit2 = kit;
                    map.put(bond3, kit2);
                    EnumSet<Bond> enumSet = this.serverOwned;
                    Bond bond4 = bond;
                    enumSet.add(bond4);
                    continue;
                }
            } else {
                Kit kit;
                Map<Bond, Kit> map = this.kits;
                Bond bond = Bond.HERETIC;
                if (!map.containsKey((Object)bond) && this.legacyKitFile.exists() && (kit = JuggernautManager.readKit(YamlConfiguration.loadConfiguration((File)this.legacyKitFile), 227343112)) != null) {
                    Map<Bond, Kit> map2 = this.kits;
                    Bond bond5 = Bond.HERETIC;
                    Kit kit3 = kit;
                    map2.put(bond5, kit3);
                    EnumSet<Bond> enumSet = this.serverOwned;
                    Bond bond6 = Bond.HERETIC;
                    enumSet.add(bond6);
                    File file2 = this.legacyKitFile;
                    JuggernautManager juggernautManager = this;
                    Bond bond7 = Bond.HERETIC;
                    File file3 = juggernautManager.kitFile(bond7, 1360845828);
                    if (file2.renameTo(file3)) {
                        Logger logger = this.plugin.getLogger();
                        JuggernautManager juggernautManager2 = this;
                        Bond bond8 = Bond.HERETIC;
                        String string = "Juggernaut kit: juggernaut_kit.yml migrated to " + juggernautManager2.kitFile(bond8, 1360845828).getName() + " (Auratus still derives from it in gold until you edit it separately).";
                        logger.info(string);
                    } else {
                        Logger logger = this.plugin.getLogger();
                        JuggernautManager juggernautManager3 = this;
                        Bond bond9 = Bond.HERETIC;
                        String string = "Juggernaut kit: could not rename juggernaut_kit.yml to " + juggernautManager3.kitFile(bond9, 1360845828).getName() + " \u2014 the legacy kit is in force but `kit status` and `kit reset heretic` will not see it. Move or delete the file by hand.";
                        logger.warning(string);
                    }
                }
                Map<Bond, Kit> map3 = this.kits;
                Bond bond10 = Bond.HERETIC;
                if (map3.containsKey((Object)bond10)) return;
                InputStream inputStream = this.plugin.getResource("juggernaut_kit.yml");
                if (inputStream == null) {
                    return;
                }
                try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                    Kit kit4 = JuggernautManager.readKit(YamlConfiguration.loadConfiguration(inputStreamReader), 227343112);
                    if (kit4 != null) {
                        this.kits.put(Bond.HERETIC, kit4);
                    }
                    return;
                }
                catch (IOException iOException) {
                    return;
                }
            }
            Logger logger = this.plugin.getLogger();
            String string = file.getName() + ": no usable kit in it \u2014 ignored.";
            logger.warning(string);
        }
    }

    private static JuggernautManager.Kit readKit(YamlConfiguration var0, int var1) {
        String var3 = "hasTemplate";
        if (!var0.getBoolean(var3, false)) {
            return null;
        } else {
            JuggernautManager.Kit var9 = new JuggernautManager.Kit();
            String var23 = "armor";
            ItemStack[] var16 = readItems(var0, var23, 4, 1319758992);
            var9.armor = var16;
            String var24 = "inv";
            ItemStack[] var18 = readItems(var0, var24, 36, 1319758992);
            var9.inv = var18;
            String var25 = "ender";
            ItemStack[] var20 = readItems(var0, var25, 27, 1319758992);
            var9.ender = var20;
            ItemStack var22 = var0.getItemStack("offhand");
            var9.offhand = var22;
            return var9;
        }
    }

    private static ItemStack[] readItems(YamlConfiguration var0, String var1, int var2, int var3) {
        ItemStack[] var9 = new ItemStack[var2];
        ConfigurationSection var10 = var0.getConfigurationSection(var1);
        if (var10 == null) {
            return var9;
        } else {
            for (String var12 : var10.getKeys(false)) {
                try {
                    int var13 = Integer.parseInt(var12);
                    if (var13 >= 0 && var13 < var2) {
                        String var38 = var1 + "." + var12;
                        ItemStack var37 = var0.getItemStack(var38);
                        var9[var13] = var37;
                    }
                } catch (NumberFormatException var39) {
                }
            }

            return var9;
        }
    }

    private void setMaxHealth(Player var1, double var2, int var4) {
        Attribute var6 = Attribute.MAX_HEALTH;
        AttributeInstance var9 = var1.getAttribute(var6);
        if (var9 != null) {
            double var13 = var9.getBaseValue();
            if (var13 != var2) {
                var9.setBaseValue(var2);
            }
        }
    }

    private void applyInvisibility(Player var1, int var2) {
        PotionEffectType var5 = PotionEffectType.INVISIBILITY;
        PotionEffect var4 = new PotionEffect(var5, 19200, 0, false, false, true);
        var1.addPotionEffect(var4);
    }

    private static TrimMaterial trimFor(JuggernautManager.Bond var0, int var1) {
        JuggernautManager.Bond var3 = JuggernautManager.Bond.AURATUS;
        TrimMaterial var4;
        if (var0 == var3) {
            var4 = TrimMaterial.GOLD;
        } else {
            var4 = TrimMaterial.REDSTONE;
        }

        return var4;
    }

    private void equipCursedArmour(Player var1, TrimMaterial var2, int var3) {
        PlayerInventory var9 = var1.getInventory();
        Material var7 = Material.NETHERITE_HELMET;
        ItemStack var22 = this.cursedArmour(var7, var2, 73960355);
        ItemStack var15 = this.marked(var22, 1536428999);
        var9.setHelmet(var15);
        Material var29 = Material.NETHERITE_CHESTPLATE;
        ItemStack var24 = this.cursedArmour(var29, var2, 73960355);
        ItemStack var17 = this.marked(var24, 1536428999);
        var9.setChestplate(var17);
        Material var30 = Material.NETHERITE_LEGGINGS;
        ItemStack var26 = this.cursedArmour(var30, var2, 73960355);
        ItemStack var19 = this.marked(var26, 1536428999);
        var9.setLeggings(var19);
        Material var31 = Material.NETHERITE_BOOTS;
        ItemStack var28 = this.cursedArmour(var31, var2, 73960355);
        ItemStack var21 = this.marked(var28, 1536428999);
        var9.setBoots(var21);
    }

    private void giveWeaponsAndConsumables(Player var1, int var2) {
        PlayerInventory var7 = var1.getInventory();
        ItemStack[] var26 = new ItemStack[4];
        ItemStack var36 = this.cursedSword(68747380);
        var26[0] = var36;
        ItemStack var38 = this.cursedAxe(1307754006);
        var26[1] = var38;
        ItemStack var40 = this.cursedPickaxe(1413264101);
        var26[2] = var40;
        ItemStack var42 = this.shield(1540530690);
        var26[3] = var42;
        var7.addItem(var26);

        for (ItemStack var9 : this.pvpConsumables(978993585)) {
            ItemStack[] var28 = new ItemStack[]{var9};
            var7.addItem(var28);
        }

        try {
            ItemStack[] var30 = new ItemStack[1];
            ItemStack var44 = HonoredCompass.juggernaut(267343802);
            var30[0] = var44;
            var7.addItem(var30);
        } catch (Throwable var45) {
        }
    }

    private void stockEnderChest(Player var1, int var2) {
        Inventory var11 = var1.getEnderChest();
        Material var7 = Material.RED_SHULKER_BOX;
        Material var9 = Material.GOLDEN_APPLE;
        ItemStack var8 = new ItemStack(var9, 64);
        ItemStack var67 = this.fullShulker(var7, var8, 1593941990);
        ItemStack var54 = this.marked(var67, 1536428999);
        var11.setItem(0, var54);
        Material var88 = Material.ORANGE_SHULKER_BOX;
        PotionType var132 = PotionType.LONG_FIRE_RESISTANCE;
        ItemStack var111 = this.potion(var132, 1, 199736847);
        ItemStack var69 = this.fullShulker(var88, var111, 1593941990);
        ItemStack var56 = this.marked(var69, 1536428999);
        var11.setItem(1, var56);
        Material var89 = Material.BLUE_SHULKER_BOX;
        PotionType var133 = PotionType.LONG_SWIFTNESS;
        ItemStack var113 = this.potion(var133, 1, 199736847);
        ItemStack var71 = this.fullShulker(var89, var113, 1593941990);
        ItemStack var58 = this.marked(var71, 1536428999);
        var11.setItem(2, var58);
        Material var90 = Material.YELLOW_SHULKER_BOX;
        PotionType var134 = PotionType.LONG_STRENGTH;
        ItemStack var115 = this.potion(var134, 1, 199736847);
        ItemStack var73 = this.fullShulker(var90, var115, 1593941990);
        ItemStack var60 = this.marked(var73, 1536428999);
        var11.setItem(3, var60);
        Material var91 = Material.WHITE_SHULKER_BOX;
        Material var143 = Material.BREEZE_ROD;
        ItemStack var116 = new ItemStack(var143, 64);
        ItemStack var75 = this.fullShulker(var91, var116, 1593941990);
        ItemStack var62 = this.marked(var75, 1536428999);
        var11.setItem(4, var62);
        Material var92 = Material.LIME_SHULKER_BOX;
        Material var144 = Material.EXPERIENCE_BOTTLE;
        ItemStack var117 = new ItemStack(var144, 64);
        ItemStack var77 = this.fullShulker(var92, var117, 1593941990);
        ItemStack var64 = this.marked(var77, 1536428999);
        var11.setItem(5, var64);
        Material var93 = Material.PURPLE_SHULKER_BOX;
        PotionType var135 = PotionType.TURTLE_MASTER;
        ItemStack var119 = this.potion(var135, 1, 199736847);
        ItemStack var79 = this.fullShulker(var93, var119, 1593941990);
        ItemStack var66 = this.marked(var79, 1536428999);
        var11.setItem(6, var66);

        for (int var12 = 0; var12 < 4; var12++) {
            ItemStack[] var53 = new ItemStack[1];
            Material var153 = Material.EXPERIENCE_BOTTLE;
            ItemStack var131 = new ItemStack(var153, 64);
            ItemStack var109 = this.marked(var131, 1536428999);
            var53[0] = var109;
            var11.addItem(var53);
        }

        ItemStack[] var43 = new ItemStack[1];
        PotionType var136 = PotionType.TURTLE_MASTER;
        ItemStack var121 = this.potion(var136, 4, 199736847);
        ItemStack var95 = this.marked(var121, 1536428999);
        var43[0] = var95;
        var11.addItem(var43);
        ItemStack[] var45 = new ItemStack[3];
        PotionType var137 = PotionType.LONG_STRENGTH;
        ItemStack var123 = this.potion(var137, 1, 199736847);
        ItemStack var97 = this.marked(var123, 1536428999);
        var45[0] = var97;
        PotionType var138 = PotionType.LONG_SWIFTNESS;
        ItemStack var125 = this.potion(var138, 1, 199736847);
        ItemStack var99 = this.marked(var125, 1536428999);
        var45[1] = var99;
        PotionType var139 = PotionType.LONG_FIRE_RESISTANCE;
        ItemStack var127 = this.potion(var139, 1, 199736847);
        ItemStack var101 = this.marked(var127, 1536428999);
        var45[2] = var101;
        var11.addItem(var45);
        ItemStack[] var47 = new ItemStack[1];
        Material var150 = Material.WIND_CHARGE;
        ItemStack var128 = new ItemStack(var150, 64);
        ItemStack var103 = this.marked(var128, 1536428999);
        var47[0] = var103;
        var11.addItem(var47);
        ItemStack[] var49 = new ItemStack[1];
        Material var151 = Material.GOLDEN_APPLE;
        ItemStack var129 = new ItemStack(var151, 64);
        ItemStack var105 = this.marked(var129, 1536428999);
        var49[0] = var105;
        var11.addItem(var49);
        ItemStack[] var51 = new ItemStack[1];
        Material var152 = Material.ENCHANTED_GOLDEN_APPLE;
        ItemStack var130 = new ItemStack(var152, 24);
        ItemStack var107 = this.marked(var130, 1536428999);
        var51[0] = var107;
        var11.addItem(var51);
    }

    private ItemStack fullShulker(Material var1, ItemStack var2, int var3) {
        ItemStack var4 = new ItemStack(var1);
        BlockStateMeta var7 = (BlockStateMeta)var4.getItemMeta();
        ShulkerBox var8 = (ShulkerBox)var7.getBlockState();
        int var9 = 0;

        while (true) {
            int var26 = var8.getInventory().getSize();
            if (var9 >= var26) {
                var7.setBlockState(var8);
                var4.setItemMeta(var7);
                return var4;
            }

            Inventory var19 = var8.getInventory();
            ItemStack var31 = var2.clone();
            var19.setItem(var9, var31);
            var9++;
        }
    }

    private List<ItemStack> pvpConsumables(int var1) {
        ArrayList var2 = new ArrayList();
        Material var3 = Material.GOLDEN_APPLE;
        ItemStack var37 = new ItemStack(var3, 64);
        var2.add(var37);
        Material var62 = Material.GOLDEN_APPLE;
        ItemStack var38 = new ItemStack(var62, 64);
        var2.add(var38);
        Material var63 = Material.ENCHANTED_GOLDEN_APPLE;
        ItemStack var39 = new ItemStack(var63, 4);
        var2.add(var39);
        Material var64 = Material.ENDER_CHEST;
        ItemStack var40 = new ItemStack(var64, 32);
        var2.add(var40);
        Material var65 = Material.COBWEB;
        ItemStack var41 = new ItemStack(var65, 64);
        var2.add(var41);
        Material var66 = Material.COBWEB;
        ItemStack var42 = new ItemStack(var66, 64);
        var2.add(var42);
        Material var67 = Material.WIND_CHARGE;
        ItemStack var43 = new ItemStack(var67, 64);
        var2.add(var43);
        Material var68 = Material.BUCKET;
        ItemStack var44 = new ItemStack(var68, 16);
        var2.add(var44);
        Material var69 = Material.WATER_BUCKET;
        ItemStack var45 = new ItemStack(var69);
        var2.add(var45);
        Material var70 = Material.WATER_BUCKET;
        ItemStack var46 = new ItemStack(var70);
        var2.add(var46);
        PotionType var57 = PotionType.LONG_STRENGTH;
        ItemStack var48 = this.potion(var57, 5, 199736847);
        var2.add(var48);
        PotionType var58 = PotionType.LONG_SWIFTNESS;
        ItemStack var50 = this.potion(var58, 5, 199736847);
        var2.add(var50);
        PotionType var59 = PotionType.LONG_FIRE_RESISTANCE;
        ItemStack var52 = this.potion(var59, 5, 199736847);
        var2.add(var52);
        PotionType var60 = PotionType.STRONG_STRENGTH;
        ItemStack var54 = this.potion(var60, 2, 199736847);
        var2.add(var54);
        PotionType var61 = PotionType.TURTLE_MASTER;
        ItemStack var56 = this.potion(var61, 3, 199736847);
        var2.add(var56);
        return var2;
    }

    private ItemStack potion(PotionType var1, int var2, int var3) {
        Material var5 = Material.SPLASH_POTION;
        ItemStack var4 = new ItemStack(var5, var2);
        PotionMeta var8 = (PotionMeta)var4.getItemMeta();
        var8.setBasePotionType(var1);
        var4.setItemMeta(var8);
        return var4;
    }

    private ItemStack cursedArmour(Material var1, TrimMaterial var2, int var3) {
        ItemStack var4 = new ItemStack(var1);
        ItemMeta var9 = var4.getItemMeta();
        Enchantment var29 = Enchantment.PROTECTION;
        var9.addEnchant(var29, 4, true);
        Enchantment var30 = Enchantment.MENDING;
        var9.addEnchant(var30, 1, true);
        Enchantment var31 = Enchantment.UNBREAKING;
        var9.addEnchant(var31, 3, true);
        Enchantment var32 = Enchantment.VANISHING_CURSE;
        var9.addEnchant(var32, 1, true);
        if (var9 instanceof ArmorMeta var25) {
            TrimPattern var7 = TrimPattern.SILENCE;
            ArmorTrim var33 = new ArmorTrim(var2, var7);
            var25.setTrim(var33);
        }

        var4.setItemMeta(var9);
        return var4;
    }

    private ItemStack cursedSword(int var1) {
        Material var3 = Material.NETHERITE_SWORD;
        ItemStack var2 = new ItemStack(var3);
        ItemMeta var6 = var2.getItemMeta();
        Enchantment var20 = Enchantment.SHARPNESS;
        var6.addEnchant(var20, 5, true);
        Enchantment var21 = Enchantment.UNBREAKING;
        var6.addEnchant(var21, 3, true);
        Enchantment var22 = Enchantment.MENDING;
        var6.addEnchant(var22, 1, true);
        Enchantment var23 = Enchantment.VANISHING_CURSE;
        var6.addEnchant(var23, 1, true);
        var2.setItemMeta(var6);
        return var2;
    }

    private ItemStack shield(int var1) {
        Material var3 = Material.SHIELD;
        ItemStack var2 = new ItemStack(var3);
        ItemMeta var6 = var2.getItemMeta();
        Enchantment var16 = Enchantment.MENDING;
        var6.addEnchant(var16, 1, true);
        Enchantment var17 = Enchantment.UNBREAKING;
        var6.addEnchant(var17, 3, true);
        var2.setItemMeta(var6);
        return var2;
    }

    private ItemStack cursedAxe(int var1) {
        Material var3 = Material.NETHERITE_AXE;
        ItemStack var2 = new ItemStack(var3);
        ItemMeta var6 = var2.getItemMeta();
        Enchantment var20 = Enchantment.SHARPNESS;
        var6.addEnchant(var20, 5, true);
        Enchantment var21 = Enchantment.UNBREAKING;
        var6.addEnchant(var21, 3, true);
        Enchantment var22 = Enchantment.MENDING;
        var6.addEnchant(var22, 1, true);
        Enchantment var23 = Enchantment.VANISHING_CURSE;
        var6.addEnchant(var23, 1, true);
        var2.setItemMeta(var6);
        return var2;
    }

    private ItemStack cursedPickaxe(int var1) {
        Material var3 = Material.NETHERITE_PICKAXE;
        ItemStack var2 = new ItemStack(var3);
        ItemMeta var6 = var2.getItemMeta();
        Enchantment var22 = Enchantment.EFFICIENCY;
        var6.addEnchant(var22, 5, true);
        Enchantment var23 = Enchantment.SILK_TOUCH;
        var6.addEnchant(var23, 1, true);
        Enchantment var24 = Enchantment.UNBREAKING;
        var6.addEnchant(var24, 3, true);
        Enchantment var25 = Enchantment.MENDING;
        var6.addEnchant(var25, 1, true);
        Enchantment var26 = Enchantment.VANISHING_CURSE;
        var6.addEnchant(var26, 1, true);
        var2.setItemMeta(var6);
        return var2;
    }

    public static enum Bond {
        HERETIC,
        AURATUS;

        public static JuggernautManager.Bond parse(String var0) {
            try {
                Locale var2 = Locale.ROOT;
                return valueOf(var0.toUpperCase(var2));
            } catch (Exception var6) {
                return null;
            }
        }
    }

    private static final class Kit {
        ItemStack[] armor;
        ItemStack[] inv;
        ItemStack[] ender;
        ItemStack offhand;
    }
}
