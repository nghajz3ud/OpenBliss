package com.bliss.honored;

import com.bliss.BlissPlugin;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import org.bukkit.configuration.file.YamlConfiguration;

public final class AssessmentDay {
    public static final int LAST_DAY = 11;
    private final BlissPlugin plugin;
    private final File file;
    private int day = 1;
    private int windowOpenedDay = -1;

    public AssessmentDay(BlissPlugin var1, int var2) {
        this.plugin = var1;
        File var14 = var1.getDataFolder();
        File var13 = new File(var14, "assessment.yml");
        this.file = var13;
    }

    public int day(int var1) {
        return this.day;
    }

    public boolean isLastDay(int var1) {
        boolean var5;
        if (this.day >= 11) {
            var5 = true;
        } else {
            var5 = false;
        }

        return var5;
    }

    public void setDay(int var1, int var2) {
        int var11 = Math.min(11, var1);
        int var9 = Math.max(1, var11);
        this.day = var9;
        this.windowOpenedDay = -1;
        this.save(1277286185);
    }

    public void nextDay(int var1) {
        int var8 = this.day + 1;
        int var6 = Math.min(11, var8);
        this.setDay(var6, 1945321378);
    }

    public boolean windowOpenedToday(int var1) {
        int var4 = this.windowOpenedDay;
        int var6 = this.day;
        boolean var5;
        if (var4 == var6) {
            var5 = true;
        } else {
            var5 = false;
        }

        return var5;
    }

    public void markWindowOpened(int var1) {
        int var5 = this.day;
        this.windowOpenedDay = var5;
        this.save(1277286185);
    }

    public void load(int var1) {
        if (this.file.exists()) {
            YamlConfiguration var8 = YamlConfiguration.loadConfiguration(this.file);
            String var6 = "day";
            int var21 = var8.getInt(var6, 1);
            int var19 = Math.min(11, var21);
            int var16 = Math.max(1, var19);
            this.day = var16;
            String var20 = "windowOpenedDay";
            int var18 = var8.getInt(var20, -1);
            this.windowOpenedDay = var18;
        }
    }

    // $VF: Handled exception range with multiple entry points by splitting it
    // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
    public void save(int var1) {
        YamlConfiguration var2 = new YamlConfiguration();
        YamlConfiguration var4 = var2;
        String var21 = "day";
        Integer var29 = this.day;
        var2.set(var21, var29);
        String var22 = "windowOpenedDay";
        Integer var32 = this.windowOpenedDay;
        var2.set(var22, var32);

        try {
            if (!this.file.getParentFile().exists()) {
                this.file.getParentFile().mkdirs();
            }
            File var24 = this.file;
            var4.save(var24);
        } catch (IOException var5) {
            Logger var20 = this.plugin.getLogger();
            String var27 = "Failed to save assessment.yml: " + var5.getMessage();
            var20.warning(var27);
        }
    }
}
