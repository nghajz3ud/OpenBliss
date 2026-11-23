package com.bliss.goldgem;

import com.bliss.core.AtomicYml;
import com.bliss.core.GemType;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.trader.TraderListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.logging.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class GoldGemManager {
    private final JavaPlugin plugin;
    private final PlayerDataStore players;
    private final Set<UUID> victims;
    private final Map<UUID, GemType> repurposed;
    private final Map<UUID, GemType> soulboundPending;
    private final File file;

    public GoldGemManager(JavaPlugin var1, PlayerDataStore var2, int var3) {
        HashSet var5 = new HashSet();
        this.victims = var5;
        HashMap var15 = new HashMap();
        this.repurposed = var15;
        HashMap var16 = new HashMap();
        this.soulboundPending = var16;
        this.plugin = var1;
        this.players = var2;
        File var20 = var1.getDataFolder();
        File var19 = new File(var20, "goldgem.yml");
        this.file = var19;
        this.load(390161727);
    }

    public boolean isVictim(UUID var1, int var2) {
        Set var5 = this.victims;
        return var5.contains(var1);
    }

    public boolean addVictim(UUID var1, int var2) {
        Set var6 = this.victims;
        boolean var5 = var6.add(var1);
        this.save(566924689);
        return var5;
    }

    public boolean removeVictim(UUID var1, int var2) {
        Set var6 = this.victims;
        boolean var5 = var6.remove(var1);
        this.save(566924689);
        return var5;
    }

    public Set<UUID> victims(int var1) {
        Set var4 = this.victims;
        return new HashSet<>(var4);
    }

    public boolean isRepurposed(UUID var1, int var2) {
        Map var5 = this.repurposed;
        return var5.containsKey(var1);
    }

    public void markRepurposed(UUID var1, GemType var2, int var3) {
        Map var7 = this.repurposed;
        GemType var10;
        if (var2 == null) {
            var10 = GemType.NONE;
        } else {
            var10 = var2;
        }

        var7.put(var1, var10);
        this.save(566924689);
    }

    public GemType repurposedGem(UUID var1, int var2) {
        Map<UUID, GemType> var6 = this.repurposed;
        GemType var5 = GemType.NONE;
        return var6.getOrDefault(var1, var5);
    }

    public boolean clearRepurposed(UUID var1, int var2) {
        Map var5 = this.repurposed;
        if (var5.remove(var1) == null) {
            return false;
        } else {
            this.save(566924689);
            return true;
        }
    }

    public void becomeHolder(Player var1, ItemStack var2, int var3) {
        PlayerDataStore var8 = this.players;
        UUID var24 = var1.getUniqueId();
        PlayerData var6 = var8.get(var24, 1264010596);
        GoldGem.setMask(var2, 0, 1710446259);
        GemType var23;
        if (var6 == null) {
            var23 = GemType.NONE;
        } else {
            var23 = var6.gem(1413186337);
        }

        GemType var26 = GemType.NONE;
        if (var23 != var26) {
            TraderListener.clearGemItems(var1, 579055745);
            GemType var27 = GemType.NONE;
            var6.setGem(var27, 2137390487);
            GoldGem.addGem(var2, var23, 876666177);
            GoldGem.setSelected(var2, var23, 958539197);
        }

        if (var6 != null) {
            var6.setTier2(true, 1757992594);
            var6.setEnergy(8, 319922676);
        }

        GoldGem.setOwn(var2, var23, 818871712);
        UUID var34 = var1.getUniqueId();
        GoldGem.setHolder(var2, var34, 1407575650);
    }

    public GemType ownSoulOf(ItemStack var1, int var2) {
        if (GoldGem.hasOwnRecord(var1, 1526399553)) {
            return GoldGem.getOwn(var1, 445527790);
        } else {
            for (GemType var6 : GoldGem.absorbedGems(var1, 874118425)) {
                Map var20 = this.repurposed;
                if (!var20.containsValue(var6)) {
                    return var6;
                }
            }

            return GemType.NONE;
        }
    }

    public List<UUID> victimsOf(GemType var1, int var2) {
        ArrayList var3 = new ArrayList();
        ArrayList var4 = var3;

        for (Entry var6 : this.repurposed.entrySet()) {
            Object var18 = var6.getValue();
            if (var18 == var1) {
                UUID var24 = (UUID)var6.getKey();
                var4.add(var24);
            }
        }

        return var4;
    }

    public void setSoulboundPending(UUID var1, GemType var2, int var3) {
        Map var7 = this.soulboundPending;
        GemType var10;
        if (var2 == null) {
            var10 = GemType.NONE;
        } else {
            var10 = var2;
        }

        var7.put(var1, var10);
        this.save(566924689);
    }

    public boolean isSoulboundPending(UUID var1, int var2) {
        Map var5 = this.soulboundPending;
        return var5.containsKey(var1);
    }

    public GemType takeSoulboundPending(UUID var1, int var2) {
        Map var6 = this.soulboundPending;
        GemType var5 = (GemType)var6.remove(var1);
        if (var5 != null) {
            this.save(566924689);
        }

        return var5;
    }

    private void load(int var1) {
        if (this.file.exists()) {
            YamlConfiguration var6 = YamlConfiguration.loadConfiguration(this.file);
            this.victims.clear();

            for (String var104 : var6.getStringList("victims")) {
                try {
                    Set var75 = this.victims;
                    UUID var91 = UUID.fromString(var104);
                    var75.add(var91);
                } catch (IllegalArgumentException var110) {
                }
            }

            this.repurposed.clear();
            if (var6.isList("repurposed")) {
                for (String var8 : var6.getStringList("repurposed")) {
                    try {
                        Map var36 = this.repurposed;
                        UUID var81 = UUID.fromString(var8);
                        GemType var4 = GemType.NONE;
                        var36.put(var81, var4);
                    } catch (IllegalArgumentException var109) {
                    }
                }
            } else {
                ConfigurationSection var101 = var6.getConfigurationSection("repurposed");
                if (var101 != null) {
                    for (String var106 : var101.getKeys(false)) {
                        try {
                            Map var68 = this.repurposed;
                            UUID var89 = UUID.fromString(var106);
                            GemType var97 = gemByName(var101.getString(var106), 1861460994);
                            var68.put(var89, var97);
                        } catch (IllegalArgumentException var108) {
                        }
                    }
                }
            }

            this.soulboundPending.clear();
            ConfigurationSection var100 = var6.getConfigurationSection("soulboundpending");
            if (var100 != null) {
                for (String var105 : var100.getKeys(false)) {
                    try {
                        Map var53 = this.soulboundPending;
                        UUID var85 = UUID.fromString(var105);
                        GemType var94 = gemByName(var100.getString(var105), 1861460994);
                        var53.put(var85, var94);
                    } catch (IllegalArgumentException var107) {
                    }
                }
            }
        }
    }

    private static GemType gemByName(String var0, int var1) {
        if (var0 == null) {
            return GemType.NONE;
        } else {
            try {
                return GemType.valueOf(var0);
            } catch (IllegalArgumentException var7) {
                return GemType.NONE;
            }
        }
    }

    // $VF: Handled exception range with multiple entry points by splitting it
    // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
    private void save(int var1) {
        YamlConfiguration var2 = new YamlConfiguration();
        YamlConfiguration var4 = var2;
        ArrayList var8 = new ArrayList();
        ArrayList var5 = var8;

        for (UUID var81 : this.victims) {
            String var68 = var81.toString();
            var5.add(var68);
        }

        String var53 = "victims";
        var2.set(var53, var5);

        for (Entry var7 : this.repurposed.entrySet()) {
            String var57 = "repurposed." + var7.getKey();
            String var72 = ((GemType)var7.getValue()).name();
            var4.set(var57, var72);
        }

        for (Entry var80 : this.soulboundPending.entrySet()) {
            String var66 = "soulboundpending." + var80.getKey();
            String var76 = ((GemType)var80.getValue()).name();
            var4.set(var66, var76);
        }

        try {
            if (!this.plugin.getDataFolder().exists()) {
                this.plugin.getDataFolder().mkdirs();
            }
            File var59 = this.file;
            AtomicYml.save(var4, var59, 1347473142);
        } catch (IOException var79) {
            Logger var43 = this.plugin.getLogger();
            String var62 = "Could not save goldgem.yml: " + var79.getMessage();
            var43.warning(var62);
        }
    }
}
