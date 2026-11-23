package com.bliss.honored;

import java.util.Iterator;
import java.util.UUID;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class HonoredNameplateTask extends BukkitRunnable {
    private static final TextColor COUNTER_AMBER = TextColor.color(16754468);
    private static final double LIFT = 2.35;
    private static final String NAMEPLATE_TAG = "BlissHONameplate";
    private final Plugin plugin;
    private final HonoredOneManager manager;
    private final AssessmentWindow window;
    private UUID displayId;
    private UUID trackedHo;

    public HonoredNameplateTask(Plugin var1, HonoredOneManager var2, AssessmentWindow var3, int var4) {
        this.plugin = var1;
        this.manager = var2;
        this.window = var3;
    }

    public void start(int var1) {
        Plugin var9 = this.plugin;
        this.runTaskTimer(var9, 20L, 1L);
    }

    public void run() {
        UUID var9 = this.manager.current(1257858083);
        Player var15;
        if (var9 == null) {
            var15 = null;
        } else {
            var15 = Bukkit.getPlayer(var9);
        }

        if (var15 != null && var15.isOnline()) {
            TextDisplay var11;
            label33: {
                var11 = this.display(1045800998);
                if (var11 != null) {
                    UUID var41 = this.trackedHo;
                    if (var9.equals(var41)) {
                        World var25 = var11.getWorld();
                        World var43 = var15.getWorld();
                        if (var25.equals(var43)) {
                            break label33;
                        }
                    }
                }

                this.remove(1165561497);
                var11 = this.spawn(var15, 2141368009);
                this.trackedHo = var9;
            }

            if (var11 != null) {
                Location var48 = var15.getLocation().add(0.0, 2.35, 0.0);
                var11.teleport(var48);
                Component var50 = this.buildText(var15, 1504426563);
                var11.text(var50);
            }
        } else {
            if (this.displayId != null || this.trackedHo != null) {
                this.remove(1165561497);
            }
        }
    }

    private Component buildText(Player var1, int var2) {
        TextComponent var11 = Component.text(var1.getName());
        NamedTextColor var4 = NamedTextColor.WHITE;
        Component var7 = var11.color(var4);
        long var8 = this.manager.trialMillisLeft(384882759);
        if (var8 <= 0L) {
            return var7;
        } else if (this.window.isActive(2020345102)) {
            long var25 = this.window.endsAtMillis(312074411);
            long var44 = System.currentTimeMillis();
            Component var29 = counter(var25 - var44, 1525114914);
            TextComponent var39 = Component.text("\n");
            Component var30 = var29.append(var39);
            return var30.append(var7);
        } else {
            Component var34 = counter(var8, 1525114914);
            TextComponent var42 = Component.text("\n");
            Component var35 = var34.append(var42);
            return var35.append(var7);
        }
    }

    private static Component counter(long var0, int var2) {
        TextComponent var7 = Component.text(format(var0, 929699358));
        TextColor var5 = COUNTER_AMBER;
        return var7.color(var5);
    }

    private static String format(long var0, int var2) {
        if (var0 < 0L) {
            var0 = 0L;
        }

        long var11 = var0 / 1000L;
        long var13 = var11 / 3600L;
        long var15 = var11 % 3600L / 60L;
        long var17 = var11 % 60L;
        if (var13 > 0L) {
            String var45 = "%dh %02dm %02ds";
            Object[] var49 = new Object[3];
            Long var72 = var13;
            var49[0] = var72;
            Long var75 = var15;
            var49[1] = var75;
            Long var78 = var17;
            var49[2] = var78;
            return String.format(var45, var49);
        } else {
            String var43 = "%dm %02ds";
            Object[] var47 = new Object[2];
            Long var66 = var15;
            var47[0] = var66;
            Long var69 = var17;
            var47[1] = var69;
            return String.format(var43, var47);
        }
    }

    private TextDisplay spawn(Player var1, int var2) {
        Location var11 = var1.getLocation().add(0.0, 2.35, 0.0);
        World var16 = var1.getWorld();
        Consumer<TextDisplay> var24 = var0 -> {
            Billboard var2x = Billboard.CENTER;
            var0.setBillboard(var2x);
            var0.setSeeThrough(true);
            var0.setShadowed(false);
            Color var16x = Color.fromARGB(0, 0, 0, 0);
            var0.setBackgroundColor(var16x);
            var0.setPersistent(false);
            var0.setViewRange(1.0F);
            var0.addScoreboardTag("BlissHONameplate");
        };
        TextDisplay var12 = (TextDisplay)var16.spawn(var11, TextDisplay.class, var24);
        UUID var23 = var12.getUniqueId();
        this.displayId = var23;
        return var12;
    }

    private TextDisplay display(int var1) {
        if (this.displayId == null) {
            return null;
        } else {
            if (Bukkit.getEntity(this.displayId) instanceof TextDisplay var4 && var4.isValid()) {
                return var4;
            }

            this.displayId = null;
            return null;
        }
    }

    public void remove(int var1) {
        despawnAll(430454330);
        this.displayId = null;
        this.trackedHo = null;
    }

    public static void despawnAll(int var0) {
        Iterator var3 = Bukkit.getWorlds().iterator();

        while (var3.hasNext()) {
            for (TextDisplay var6 : ((World)var3.next()).getEntitiesByClass(TextDisplay.class)) {
                if (var6.getScoreboardTags().contains("BlissHONameplate")) {
                    var6.remove();
                }
            }
        }
    }
}
