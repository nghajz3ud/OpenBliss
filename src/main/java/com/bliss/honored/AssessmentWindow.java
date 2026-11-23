package com.bliss.honored;

import com.bliss.BlissPlugin;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class AssessmentWindow {
    private static final long WINDOW_TICKS = 144000L;
    private final BlissPlugin plugin;
    private final File file;
    private boolean active;
    private BukkitTask autoEnd;
    private long endsAtMillis;
    private Runnable onClose;

    public AssessmentWindow(BlissPlugin var1, int var2) {
        this.plugin = var1;
        File var10 = var1.getDataFolder();
        File var9 = new File(var10, "assessmentwindow.yml");
        this.file = var9;
    }

    public boolean isActive(int var1) {
        return this.active;
    }

    public long endsAtMillis(int var1) {
        return this.endsAtMillis;
    }

    public long durationMillis() {
        return 7200000L;
    }

    public void setOnClose(Runnable var1, int var2) {
        this.onClose = var1;
    }

    public void start(int var1) {
        this.active = true;
        if (this.autoEnd != null) {
            this.autoEnd.cancel();
        }

        BukkitScheduler var15 = Bukkit.getScheduler();
        BlissPlugin var21 = this.plugin;
        Runnable var22 = this::expire;
        BukkitTask var16 = var15.runTaskLater(var21, var22, 144000L);
        this.autoEnd = var16;
        long var19 = System.currentTimeMillis() + 7200000L;
        this.endsAtMillis = var19;
        this.save(649430672);
    }

    public void stop(int var1) {
        if (this.active) {
            this.active = false;
            if (this.autoEnd != null) {
                this.autoEnd.cancel();
                this.autoEnd = null;
            }

            this.save(649430672);
            this.fireClose(220043712);
        }
    }

    private void expire() {
        this.autoEnd = null;
        if (this.active) {
            this.active = false;
            this.save(649430672);
            this.fireClose(220043712);
        }
    }

    public void restore(int var1) {
        if (this.file.exists()) {
            YamlConfiguration var8 = YamlConfiguration.loadConfiguration(this.file);
            String var3 = "active";
            if (var8.getBoolean(var3, false)) {
                String var43 = "endsAtMillis";
                long var9 = var8.getLong(var43, 0L);
                long var11 = System.currentTimeMillis();
                if (var9 > var11) {
                    this.active = true;
                    this.endsAtMillis = var9;
                    long var65 = (var9 - var11) / 50L;
                    long var13 = Math.max(1L, var65);
                    BukkitScheduler var48 = Bukkit.getScheduler();
                    BlissPlugin var68 = this.plugin;
                    Runnable var69 = this::expire;
                    BukkitTask var49 = var48.runTaskLater(var68, var69, var13);
                    this.autoEnd = var49;
                    Logger var42 = this.plugin.getLogger();
                    String var56 = "Assessment window restored — ~" + (var9 - var11) / 60000L + " min left.";
                    var42.info(var56);
                } else {
                    this.active = false;
                    this.save(649430672);
                    this.fireClose(220043712);
                }
            }
        }
    }

    // $VF: Handled exception range with multiple entry points by splitting it
    // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
    private void save(int var1) {
        YamlConfiguration var2 = new YamlConfiguration();
        YamlConfiguration var4 = var2;
        String var21 = "active";
        Boolean var29 = this.active;
        var2.set(var21, var29);
        String var22 = "endsAtMillis";
        Long var33 = this.endsAtMillis;
        var2.set(var22, var33);

        try {
            if (!this.file.getParentFile().exists()) {
                this.file.getParentFile().mkdirs();
            }
            File var24 = this.file;
            var4.save(var24);
        } catch (IOException var5) {
            Logger var20 = this.plugin.getLogger();
            String var27 = "Failed to save assessmentwindow.yml: " + var5.getMessage();
            var20.warning(var27);
        }
    }

    private void fireClose(int var1) {
        if (this.onClose != null) {
            try {
                this.onClose.run();
            } catch (Throwable var2) {
            }
        }
    }
}
