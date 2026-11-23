package com.bliss.gems;

import com.bliss.villager.Text;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class FluxChargingCommand implements CommandExecutor {
    private final FluxGemListener flux;

    public FluxChargingCommand(FluxGemListener var1, int var2) {
        this.flux = var1;
    }

    public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
        if (var1 instanceof Player var8) {
            UUID var9 = var8.getUniqueId();
            String var10 = var3.toLowerCase();
            switch (var10) {
                case "startcharging":
                    FluxGemListener var39 = this.flux;
                    if (var39.chargePercent(var9, 1875673459) > 0.0) {
                        Component var64 = Text.legacy("&aCharging started!", 373192400);
                        var8.sendMessage(var64);
                        Map var46 = this.flux.fluxCharging;
                        Boolean var76 = true;
                        var46.put(var9, var76);
                        Map var50 = this.flux.fluxOvercharging;
                        Boolean var78 = false;
                        var50.put(var9, var78);
                    } else {
                        Component var68 = Text.legacy("&cYou don't have enough energy to charge!", 373192400);
                        var8.sendMessage(var68);
                    }
                    break;
                case "stopcharging":
                    Component var59 = Text.legacy("&cCharging Stopped!", 373192400);
                    var8.sendMessage(var59);
                    Map var31 = this.flux.fluxCharging;
                    Boolean var70 = false;
                    var31.put(var9, var70);
                    Map var35 = this.flux.fluxOvercharging;
                    Boolean var72 = false;
                    var35.put(var9, var72);
            }

            return true;
        } else {
            Component var56 = Text.legacy("&cPlayer only.", 373192400);
            var1.sendMessage(var56);
            return true;
        }
    }
}
