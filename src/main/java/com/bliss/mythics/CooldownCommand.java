package com.bliss.mythics;

import com.bliss.admin.CommandHelp;
import com.bliss.mythics.aegis.MythicAegisState;
import com.bliss.mythics.blood.MythicBloodState;
import com.bliss.mythics.util.Text;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class CooldownCommand implements CommandExecutor {
    private static final String ADMIN = "bliss.admin";
    private final MythicBloodState blood;
    private final MythicAegisState aegis;

    public CooldownCommand(MythicBloodState var1, MythicAegisState var2, int var3) {
        this.blood = var1;
        this.aegis = var2;
    }

    public boolean onCommand(@NotNull CommandSender var1, @NotNull Command var2, @NotNull String var3, @NotNull String[] var4) {
        if (!var1.hasPermission("bliss.admin")) {
            Component var40 = Text.legacy("&cYou don't have permission to clear cooldowns.", 883008065);
            var1.sendMessage(var40);
            return true;
        } else {
            Player var10;
            if (var4.length >= 1) {
                var10 = Bukkit.getPlayerExact(var4[0]);
                if (var10 == null) {
                    Component var46 = Text.legacy("&cPlayer not found: &f" + var4[0], 883008065);
                    var1.sendMessage(var46);
                    return true;
                }
            } else if (var1 instanceof Player p) {
                var10 = p;
            } else {
                String var60 = "cooldown";
                String var61 = "Clear a player's Mythic gem cooldowns.";
                String var8 = "<player>";
                String var9 = "Whose Heretic / Auratus cooldowns to clear";
                CommandHelp.Entry var62 = CommandHelp.of(var8, var9, 1959232616);
                CommandHelp.show(var1, var60, var61, var62, 1122560938);
                return true;
            }

            MythicBloodState var24 = this.blood;
            UUID var48 = var10.getUniqueId();
            var24.resetCooldowns(var48, 1654610910);
            MythicAegisState var26 = this.aegis;
            UUID var50 = var10.getUniqueId();
            var26.resetCooldowns(var50, 1614869209);
            Component var54 = Text.legacy("<##FFD773>&lᴍʏᴛʜɪᴄ&7 cooldowns cleared for &f" + var10.getName() + "&7.", 883008065);
            var1.sendMessage(var54);
            if (!var10.equals(var1)) {
                Component var59 = Text.legacy("<##FFD773>&lᴍʏᴛʜɪᴄ&7 cooldowns cleared by &f" + var1.getName() + "&7.", 883008065);
                var10.sendMessage(var59);
            }

            return true;
        }
    }
}
