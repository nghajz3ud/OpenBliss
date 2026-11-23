package com.bliss.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.bukkit.configuration.file.YamlConfiguration;

public final class AtomicYml {
    private AtomicYml() {
    }

    public static void save(YamlConfiguration var0, File var1, int var2) throws IOException {
        File var8 = var1.getParentFile();
        if (var8 != null && !var8.exists()) {
            var8.mkdirs();
        }

        String var36 = var1.getName() + ".tmp";
        File var17 = new File(var8, var36);
        File var9 = var17;
        var0.save(var17);

        try {
            Path var20 = var9.toPath();
            Path var28 = var1.toPath();
            CopyOption[] var32 = new CopyOption[2];
            StandardCopyOption var7 = StandardCopyOption.REPLACE_EXISTING;
            var32[0] = var7;
            StandardCopyOption var39 = StandardCopyOption.ATOMIC_MOVE;
            var32[1] = var39;
            Files.move(var20, var28, var32);
        } catch (AtomicMoveNotSupportedException var41) {
            Path var24 = var17.toPath();
            Path var30 = var1.toPath();
            CopyOption[] var34 = new CopyOption[1];
            StandardCopyOption var40 = StandardCopyOption.REPLACE_EXISTING;
            var34[0] = var40;
            Files.move(var24, var30, var34);
        }
    }
}
