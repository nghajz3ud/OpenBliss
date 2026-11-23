package com.bliss.core;

import java.util.List;
import org.bukkit.command.CommandSender;

public interface DragonGamesHook {
    void handle(CommandSender var1, String[] var2, int var3);

    List<String> complete(CommandSender var1, String[] var2, int var3);

    boolean isSelfCheck(String[] var1, int var2);

    void stop(int var1);
}
