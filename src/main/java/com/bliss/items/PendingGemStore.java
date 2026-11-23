package com.bliss.items;

import com.bliss.BlissPlugin;
import com.bliss.core.AtomicYml;
import com.bliss.core.GemType;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.honored.JuggernautGemGuardListener;
import com.bliss.villager.Text;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class PendingGemStore {
    private static final long RETRY_TICKS = 20L;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final BlissCoreItems items;
    private final Set<UUID> owed;
    private BukkitTask task;

    public PendingGemStore(BlissPlugin var1, PlayerDataStore var2, BlissCoreItems var3, int var4) {
        HashSet var6 = new HashSet();
        this.owed = var6;
        this.plugin = var1;
        this.players = var2;
        this.items = var3;
    }

    private File file(int var1) {
        File var6 = this.plugin.getDataFolder();
        String var4 = "pending_gems.yml";
        return new File(var6, var4);
    }

    public void load(int var1) {
        File var4 = this.file(98356547);
        if (var4.exists()) {
            for (String var7 : YamlConfiguration.loadConfiguration(var4).getStringList("owed")) {
                try {
                    Set var23 = this.owed;
                    UUID var27 = UUID.fromString(var7);
                    var23.add(var27);
                } catch (IllegalArgumentException var28) {
                }
            }
        }
    }

    private void save(int var1) {
        YamlConfiguration var2 = new YamlConfiguration();
        YamlConfiguration var4 = var2;
        ArrayList var8 = new ArrayList();
        ArrayList var5 = var8;

        for (UUID var7 : this.owed) {
            String var26 = var7.toString();
            var5.add(var26);
        }

        String var27 = "owed";
        var2.set(var27, var5);

        try {
            File var29 = this.file(98356547);
            AtomicYml.save(var4, var29, 1347473142);
        } catch (Exception var34) {
            Logger var24 = this.plugin.getLogger();
            String var32 = "Could not save pending_gems.yml: " + var34.getMessage();
            var24.warning(var32);
        }
    }

    public void start(int var1) {
        BukkitScheduler var3 = Bukkit.getScheduler();
        BlissPlugin var11 = this.plugin;
        Runnable var12 = this::tick;
        BukkitTask var10 = var3.runTaskTimer(var11, var12, 20L, 20L);
        this.task = var10;
    }

    public void stop(int var1) {
        if (this.task != null) {
            this.task.cancel();
        }
    }

    private void tick() {
        if (!this.owed.isEmpty()) {
            Set var20 = this.owed;
            ArrayList var8 = new ArrayList(var20);
            Iterator var3 = var8.iterator();

            while (var3.hasNext()) {
                Player var5 = Bukkit.getPlayer((UUID)var3.next());
                if (var5 != null) {
                    this.tryDeliver(var5, 1966578596);
                }
            }
        }
    }

    public void owe(Player var1, int var2) {
        if (var1 != null) {
            Set var6 = this.owed;
            UUID var10 = var1.getUniqueId();
            if (var6.add(var10)) {
                this.save(712763179);
                Component var12 = Text.legacy(
                    "<##FF6B6B>\ud83d\udd2e &cYour inventory is full — &fyour gem is being held&c. Free a slot and it comes straight back.", 373192400
                );
                var1.sendMessage(var12);
            }
        }
    }

    public void owe(UUID var1, int var2) {
        if (var1 != null) {
            Set var6 = this.owed;
            if (var6.add(var1)) {
                this.save(712763179);
            }
        }
    }

    public boolean isOwed(UUID var1) {
        Set var4 = this.owed;
        return var4.contains(var1);
    }

    private void clear(UUID var1, int var2) {
        Set var5 = this.owed;
        if (var5.remove(var1)) {
            this.save(712763179);
        }
    }

    public void tryDeliver(Player var1, int var2) {
        if (var1 != null && var1.isOnline()) {
            UUID var7 = var1.getUniqueId();
            Set var15 = this.owed;
            if (var15.contains(var7)) {
                PlayerDataStore var18 = this.players;
                PlayerData var8 = var18.get(var7, 1264010596);
                if (var8 != null) {
                    GemType var22 = var8.gem(1413186337);
                    GemType var37 = GemType.NONE;
                    if (var22 != var37 && !JuggernautGemGuardListener.isJuggernaut(var1, 859003337)) {
                        if (BlissCoreItems.findGemSlot(var1, 1702349856) >= 0) {
                            this.clear(var7, 474665422);
                            return;
                        }

                        BlissCoreItems var29 = this.items;
                        GemType var40 = var8.gem(1413186337);
                        boolean var49 = var8.tier2(629984660);
                        int var51 = var8.energy(1160853318);
                        ItemStack var9 = var29.gemItem(var40, var49, var51, 256965471);
                        if (!BlissCoreItems.placeGem(var1, -1, var9, 2132502548)) {
                            return;
                        }

                        this.clear(var7, 474665422);
                        Component var47 = Text.legacy("\ud83d\udd2e &bYour " + var8.gem(1413186337).coloredName() + " &bis back in your inventory.", 373192400);
                        var1.sendMessage(var47);
                        return;
                    }
                }

                this.clear(var7, 474665422);
            }
        }
    }
}
