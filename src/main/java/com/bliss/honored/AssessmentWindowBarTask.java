package com.bliss.honored;

import com.bliss.villager.Text;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class AssessmentWindowBarTask extends BukkitRunnable {
    private static volatile boolean showing = false;
    private final Plugin plugin;
    private final AssessmentWindow window;
    private final AssessmentDay day;

    public static boolean isShowing(int var0) {
        return showing;
    }

    public static void stopShowing(int var0) {
        showing = false;
    }

    public AssessmentWindowBarTask(Plugin var1, AssessmentWindow var2, AssessmentDay var3, int var4) {
        this.plugin = var1;
        this.window = var2;
        this.day = var3;
    }

    public void start(int var1) {
        Plugin var9 = this.plugin;
        this.runTaskTimer(var9, 20L, 20L);
    }

    public void run() {
        boolean var13;
        if (this.window.isActive(2020345102) && this.day.isLastDay(1298882900)) {
            var13 = true;
        } else {
            var13 = false;
        }

        showing = var13;
        if (showing) {
            long var17 = this.window.endsAtMillis(312074411);
            long var3 = System.currentTimeMillis();
            Component var5 = Text.legacy("<##ffcc00>⌛ &lAssessment &r<##ffcc00>" + format(var17 - var3, 1260957294), 373192400);

            for (Player var31 : Bukkit.getOnlinePlayers()) {
                var31.sendActionBar(var5);
            }
        }
    }

    private static String format(long var0, int var2) {
        if (var0 < 0L) {
            var0 = 0L;
        }

        long var11 = var0 / 1000L;
        long var13 = var11 / 3600L;
        long var15 = var11 % 3600L / 60L;
        long var17 = var11 % 60L;
        String var45;
        if (var13 > 0L) {
            String var43 = "%d:%02d:%02d";
            Object[] var46 = new Object[3];
            Long var65 = var13;
            var46[0] = var65;
            Long var68 = var15;
            var46[1] = var68;
            Long var71 = var17;
            var46[2] = var71;
            var45 = String.format(var43, var46);
        } else {
            String var44 = "%d:%02d";
            Object[] var48 = new Object[2];
            Long var74 = var15;
            var48[0] = var74;
            Long var77 = var17;
            var48[1] = var77;
            var45 = String.format(var44, var48);
        }

        return var45;
    }
}
