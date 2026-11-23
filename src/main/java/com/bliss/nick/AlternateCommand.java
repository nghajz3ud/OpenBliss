package com.bliss.nick;

import com.bliss.goldgem.GoldenDreamWorld;
import com.bliss.villager.Text;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class AlternateCommand implements CommandExecutor {
    private static final long COOLDOWN_MS = 120000L;
    private final MemoryRoster roster;
    private final Map<UUID, Long> nextAt;

    public AlternateCommand(MemoryRoster var1, int var2) {
        ConcurrentHashMap var4 = new ConcurrentHashMap();
        this.nextAt = var4;
        this.roster = var1;
    }

    public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
        if (var1 instanceof Player var13) {
            UUID var14 = var13.getUniqueId();
            if (this.roster != null) {
                MemoryRoster var28 = this.roster;
                if (var28.isMemory(var14, 1860168534)) {
                    if (!GoldenDreamWorld.isDreamWorld(var13.getWorld())) {
                        Component var76 = Text.legacy("&cOnly inside the dream.", 373192400);
                        var13.sendMessage(var76);
                        return true;
                    }

                    long var15 = System.currentTimeMillis();
                    Map var36 = this.nextAt;
                    Long var17 = (Long)var36.get(var14);
                    if (var17 != null) {
                        long var41 = var17;
                        if (var41 > var15) {
                            long var45 = var17;
                            long var18 = (var45 - var15 + 999L) / 1000L;
                            Component var71 = Text.legacy("&cNot yet - &f" + var18 + "s&c.", 373192400);
                            var13.sendMessage(var71);
                            return true;
                        }
                    }

                    Map var56 = this.nextAt;
                    Long var91 = var15 + 120000L;
                    var56.put(var14, var91);
                    World var59 = var13.getWorld();
                    Location var74 = var13.getLocation();
                    String var92 = "bliss:alternate";
                    SoundCategory var9 = SoundCategory.MASTER;
                    var59.playSound(var74, var92, var9, 1.0F, 1.0F);
                    return true;
                }
            }

            Component var78 = Text.legacy("&cOnly a memory can do that.", 373192400);
            var13.sendMessage(var78);
            return true;
        } else {
            Component var80 = Text.legacy("&cPlayer only.", 373192400);
            var1.sendMessage(var80);
            return true;
        }
    }
}
