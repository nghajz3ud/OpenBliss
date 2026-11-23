package com.bliss.gems;

import com.bliss.admin.CommandHelp;
import com.bliss.villager.Text;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class WattsCommand implements CommandExecutor {
    private final FluxGemListener flux;

    public WattsCommand(FluxGemListener var1, int var2) {
        this.flux = var1;
    }

    public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
        Locale var6 = Locale.ROOT;
        String var13 = var3.toLowerCase(var6);
        String var21 = "resetcharge";
        if (var21.equals(var13)) {
            if (!var1.isOp() && !var1.hasPermission("bliss.admin")) {
                Component var234 = Text.legacy("&cOp only.", 373192400);
                var1.sendMessage(var234);
                return true;
            } else {
                this.flux.fluxKinetic.clear();
                this.flux.saveWatts(1607488947);
                this.flux.fluxOvercharging.clear();
                this.flux.fluxOverChargeTime.clear();
                Component var236 = Text.legacy("&fEvery player's Flux beam charge has been reset to &e0%&f.", 373192400);
                var1.sendMessage(var236);
                return true;
            }
        } else {
            String var23 = "setcharge";
            if (var23.equals(var13)) {
                if (!var1.isOp() && !var1.hasPermission("bliss.admin")) {
                    Component var208 = Text.legacy("&cOp only.", 373192400);
                    var1.sendMessage(var208);
                    return true;
                } else if (var4.length < 2) {
                    String var231 = "setcharge";
                    String var271 = "Set a player's Flux beam charge.";
                    String var285 = "<player> <amount>";
                    String var290 = "Charge percent to set";
                    CommandHelp.Entry var286 = CommandHelp.of(var285, var290, 1959232616);
                    CommandHelp.show(var1, var231, var271, var286, 1122560938);
                    return true;
                } else {
                    Player var291 = Bukkit.getPlayerExact(var4[0]);
                    if (var291 == null) {
                        Component var230 = Text.legacy("&cPlayer not found / offline.", 373192400);
                        var1.sendMessage(var230);
                        return true;
                    } else {
                        double var292;
                        try {
                            var292 = Double.parseDouble(var4[1]);
                        } catch (NumberFormatException var298) {
                            Component var213 = Text.legacy("&cAmount must be a number (the beam charge %).", 373192400);
                            var1.sendMessage(var213);
                            return true;
                        }

                        if (var292 < 0.0) {
                            var292 = 0.0;
                        }

                        UUID var295 = var291.getUniqueId();
                        Map var121 = this.flux.fluxKinetic;
                        Double var251 = var292;
                        var121.put(var295, var251);
                        Map var125 = this.flux.fluxCharging;
                        Boolean var253 = false;
                        var125.put(var295, var253);
                        if (var292 > 100.0) {
                            Map var143 = this.flux.fluxOvercharging;
                            Boolean var268 = true;
                            var143.put(var295, var268);
                            Map var147 = this.flux.fluxOverChargeTime;
                            Integer var270 = 3;
                            var147.put(var295, var270);
                        } else {
                            Map var132 = this.flux.fluxOvercharging;
                            Boolean var257 = false;
                            var132.put(var295, var257);
                            Map var136 = this.flux.fluxOverChargeTime;
                            Integer var259 = 0;
                            var136.put(var295, var259);
                        }

                        String var219 = var291.getName();
                        String var262 = formatPercent(var292, 1436639006);
                        String var284;
                        if (var292 > 100.0) {
                            var284 = " &6(overcharged)";
                        } else {
                            var284 = "";
                        }

                        Component var221 = Text.legacy("&aSet &f" + var219 + "&a's Flux beam charge to &f" + var262 + "%&a" + var284 + ".", 373192400);
                        var1.sendMessage(var221);
                        String var224 = formatPercent(var292, 1436639006);
                        String var266;
                        if (var292 > 100.0) {
                            var266 = " &6(overcharged)";
                        } else {
                            var266 = "";
                        }

                        Component var226 = Text.legacy("&f\ud83d\udd2e Your Flux gem is charged at " + var224 + "%" + var266, 373192400);
                        var291.sendMessage(var226);
                        return true;
                    }
                }
            } else if (var1 instanceof Player var14) {
                UUID var15 = var14.getUniqueId();
                switch (var13) {
                    case "watts":
                        Map<UUID, Long> var68 = this.flux.fluxCharge;
                        Long var243 = 0L;
                        long var18 = var68.getOrDefault(var15, var243);
                        FluxGemListener var272 = this.flux;
                        String var275 = formatPercent(var272.chargePercent(var15, 1875673459), 1436639006);
                        Component var195 = Text.legacy("&f\ud83d\udd2e You now have " + var18 + " watts, up to " + var275 + "% charge.", 373192400);
                        var14.sendMessage(var195);
                        break;
                    case "hiddenwatts":
                        Map<UUID, Long> var76 = this.flux.fluxChargeHidden;
                        Long var246 = 0L;
                        long var296 = var76.getOrDefault(var15, var246);
                        FluxGemListener var277 = this.flux;
                        String var280 = formatPercent(var277.chargePercent(var15, 1875673459), 1436639006);
                        Component var200 = Text.legacy("&b\ud83d\udd2e You now have " + var296 + " hidden watts, up to " + var280 + "% charge.", 373192400);
                        var14.sendMessage(var200);
                        break;
                    case "maxwatts":
                        if (!var1.isOp() && !var1.hasPermission("bliss.admin")) {
                            Component var177 = Text.legacy("&cOp only.", 373192400);
                            var1.sendMessage(var177);
                            return true;
                        }

                        Map var47 = this.flux.fluxCharge;
                        Long var237 = 2000000L;
                        var47.put(var15, var237);
                        FluxGemListener var50 = this.flux;
                        var50.recomputeChargePercent(var15, 1834179235);
                        Component var183 = Text.legacy(
                            "&aWatts set to the maximum (&f2000000&a) — 100% charge. &7Use &f/startcharging&7 to drain it into the beam, or &f/setcharge "
                                + var14.getName()
                                + " <amount>&7 to charge the beam directly.",
                            373192400
                        );
                        var14.sendMessage(var183);
                        break;
                    case "resetwatts":
                        if (!var1.isOp() && !var1.hasPermission("bliss.admin")) {
                            Component var186 = Text.legacy("&cOp only.", 373192400);
                            var1.sendMessage(var186);
                            return true;
                        }

                        Map var61 = this.flux.fluxCharge;
                        Long var240 = 0L;
                        var61.put(var15, var240);
                        FluxGemListener var64 = this.flux;
                        var64.recomputeChargePercent(var15, 1834179235);
                        Component var190 = Text.legacy("&eYour Flux watts have been reset to &f0&e.", 373192400);
                        var14.sendMessage(var190);
                }

                return true;
            } else {
                Component var205 = Text.legacy("&cPlayer only.", 373192400);
                var1.sendMessage(var205);
                return true;
            }
        }
    }

    private static String formatPercent(double var0, int var2) {
        double var17 = Math.floor(var0);
        String var15;
        if (var0 == var17) {
            String var12 = "%.1f";
            Object[] var16 = new Object[1];
            Double var19 = var0;
            var16[0] = var19;
            var15 = String.format(var12, var16);
        } else {
            var15 = String.valueOf(var0);
        }

        return var15;
    }
}
