package com.bliss.nick;

import com.bliss.BlissPlugin;
import com.bliss.villager.Text;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public final class NickCommand implements CommandExecutor, TabCompleter {
    private final BlissPlugin plugin;
    private final NickManager nick;
    private final MemoryRoster memories;
    private final MemoryNpcManager npcs;

    public NickCommand(BlissPlugin var1, NickManager var2, MemoryRoster var3, MemoryNpcManager var4, int var5) {
        this.plugin = var1;
        this.nick = var2;
        this.memories = var3;
        this.npcs = var4;
    }

    public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
        String var8 = "/nick";
        return this.run(var1, var4, var8, 349418347);
    }

    public boolean run(CommandSender var1, String[] var2, String var3, int var4) {
        if (!var1.hasPermission("bliss.admin")) {
            Component var106 = Text.legacy("&cYou don't have permission.", 373192400);
            var1.sendMessage(var106);
            return true;
        } else if (var2.length < 1) {
            this.usage(var1, var3, 919658827);
            return true;
        } else if (var2[0].equalsIgnoreCase("memory")) {
            this.memory(var1, var2, var3, 1656280980);
            return true;
        } else if (var2.length < 2) {
            this.usage(var1, var3, 919658827);
            return true;
        } else {
            String var9 = var2[0].toLowerCase();
            Player var10 = Bukkit.getPlayerExact(var2[1]);
            if (var10 == null) {
                Component var176 = Text.legacy("&cPlayer must be online: &f" + var2[1], 373192400);
                var1.sendMessage(var176);
                return true;
            } else if (var10.isDead()) {
                Component var117 = Text.legacy("&cWait for the respawn: &f" + var10.getName(), 373192400);
                var1.sendMessage(var117);
                return true;
            } else {
                switch (var9) {
                    case "name":
                        if (var2.length < 3) {
                            Component var170 = Text.legacy("&eUsage: " + var3 + " name <player> <text|off>", 373192400);
                            var1.sendMessage(var170);
                            return true;
                        }

                        if (var2[2].equalsIgnoreCase("off")) {
                            NickManager var89 = this.nick;
                            var89.setName(var10, null, 1837963399);
                            Component var167 = Text.legacy("&7Cleared &f" + var10.getName() + "&7's nick name.", 373192400);
                            var1.sendMessage(var167);
                            break;
                        } else {
                            String var185 = var2[2];
                            StringBuilder var77 = new StringBuilder(var185);
                            StringBuilder var194 = var77;
                            int var14 = 3;

                            while (true) {
                                int var154 = var2.length;
                                if (var14 >= var154) {
                                    String var195 = var194.toString();
                                    NickManager var83 = this.nick;
                                    var83.setName(var10, var195, 1837963399);
                                    String var157 = var10.getName();
                                    Component var159 = Text.legacy("&6" + var157 + " &7is now &r" + var195, 373192400);
                                    var1.sendMessage(var159);
                                    return true;
                                }

                                StringBuilder var86 = var194.append(' ');
                                String var162 = var2[var14];
                                var86.append(var162);
                                var14++;
                            }
                        }
                    case "skin":
                        if (var2.length < 3) {
                            Component var128 = Text.legacy("&eUsage: " + var3 + " skin <player> <sourcePlayer|off>", 373192400);
                            var1.sendMessage(var128);
                            return true;
                        }

                        if (var2[2].equalsIgnoreCase("off")) {
                            NickManager var59 = this.nick;
                            var59.clearSkin(var10, 1699994696);
                            Component var135 = Text.legacy("&7Restored &f" + var10.getName() + "&7's own skin.", 373192400);
                            var1.sendMessage(var135);
                        } else {
                            Player var13 = Bukkit.getPlayerExact(var2[2]);
                            if (var13 == null) {
                                Component var149 = Text.legacy("&cSkin source must be online: &f" + var2[2], 373192400);
                                var1.sendMessage(var149);
                                return true;
                            }

                            NickManager var66 = this.nick;
                            if (var66.setSkinFrom(var10, var13, 1053928318)) {
                                String var143 = var10.getName();
                                String var182 = var13.getName();
                                Component var145 = Text.legacy("&6" + var143 + " &7now wears &f" + var182 + "&7's skin.", 373192400);
                                var1.sendMessage(var145);
                            } else {
                                Component var141 = Text.legacy("&c" + var13.getName() + " has no readable skin.", 373192400);
                                var1.sendMessage(var141);
                            }
                        }
                        break;
                    case "reset":
                        NickManager var49 = this.nick;
                        var49.reset(var10, 1016238986);
                        Component var124 = Text.legacy("&7Reset &f" + var10.getName() + "&7 to their real name and skin.", 373192400);
                        var1.sendMessage(var124);
                        break;
                    default:
                        this.usage(var1, var3, 919658827);
                }

                return true;
            }
        }
    }

    private void memory(CommandSender var1, String[] var2, String var3, int var4) {
        String var23;
        if (var2.length >= 2) {
            var23 = var2[1].toLowerCase();
        } else {
            var23 = "";
        }

        switch (var23) {
            case "add":
                if (var2.length < 3) {
                    Component var223 = Text.legacy("&eUsage: " + var3 + " memory add <player> [name]", 373192400);
                    var1.sendMessage(var223);
                    return;
                }

                Player var366 = Bukkit.getPlayerExact(var2[2]);
                if (var366 == null) {
                    Component var228 = Text.legacy("&cPlayer must be online: &f" + var2[2], 373192400);
                    var1.sendMessage(var228);
                    return;
                }

                String var71;
                if (var2.length >= 4) {
                    var71 = var2[3];
                } else {
                    var71 = null;
                }

                if (this.plugin.goldenDream(2108340810) != null && !this.plugin.goldenDream(2108340810).dream(2117018474).memorySetBuilt(1911583189)) {
                    Component var232 = Text.legacy("&7Building the memory worlds — the server will hang for a moment.", 373192400);
                    var1.sendMessage(var232);
                }

                MemoryRoster var82 = this.memories;
                String var14 = var82.assign(var366, var71, 1093398507);
                Component var327;
                if (var14 != null) {
                    String var236 = var366.getName();
                    var327 = Text.legacy("&6" + var236 + " &7is now the memory &f" + var14, 373192400);
                } else {
                    var327 = Text.legacy(
                        "&cNo free memory name — that name is taken or not in the pool, or the pool is exhausted (see memories.yml).", 373192400
                    );
                }

                var1.sendMessage(var327);
                break;
            case "remove":
                if (var2.length < 3) {
                    Component var264 = Text.legacy("&eUsage: " + var3 + " memory remove <player>", 373192400);
                    var1.sendMessage(var264);
                    return;
                }

                Player var368 = Bukkit.getPlayerExact(var2[2]);
                if (var368 == null) {
                    Component var269 = Text.legacy("&cPlayer must be online: &f" + var2[2], 373192400);
                    var1.sendMessage(var269);
                    return;
                }

                MemoryRoster var111 = this.memories;
                UUID var271 = var368.getUniqueId();
                if (var111.isMemory(var271, 1860168534)) {
                    MemoryRoster var114 = this.memories;
                    var114.remove(var368, 759182725);
                    Component var276 = Text.legacy("&7" + var368.getName() + " is no longer a memory.", 373192400);
                    var1.sendMessage(var276);
                } else {
                    Component var280 = Text.legacy("&c" + var368.getName() + " is not a memory.", 373192400);
                    var1.sendMessage(var280);
                }
                break;
            case "list":
                int var287 = this.memories.names(1326778483).size();
                String var345 = ", ";
                List var362 = this.memories.names(1326778483);
                String var346 = String.join(var345, var362);
                Component var289 = Text.legacy("&7Memory pool (&f" + var287 + "&7): &f" + var346, 373192400);
                var1.sendMessage(var289);
                List var369 = this.npcs.liveNames(970856627);
                int var291 = var369.size();
                String var349;
                if (var369.isEmpty()) {
                    var349 = "";
                } else {
                    String var350 = ", ";
                    var349 = " &7(&f" + String.join(var350, var369) + "&7)";
                }

                Component var293 = Text.legacy("&7NPC memories live: &f" + var291 + var349, 373192400);
                var1.sendMessage(var293);
                break;
            case "name":
                if (var2.length < 4) {
                    Component var242 = Text.legacy("&eUsage: " + var3 + " memory name <player> <newname>", 373192400);
                    var1.sendMessage(var242);
                    return;
                }

                Player var367 = Bukkit.getPlayerExact(var2[2]);
                if (var367 == null) {
                    Component var260 = Text.legacy("&cPlayer must be online: &f" + var2[2], 373192400);
                    var1.sendMessage(var260);
                    return;
                }

                MemoryRoster var93 = this.memories;
                UUID var245 = var367.getUniqueId();
                if (!var93.isMemory(var245, 1860168534)) {
                    Component var256 = Text.legacy("&c" + var367.getName() + " is not a memory (memory add first).", 373192400);
                    var1.sendMessage(var256);
                    return;
                }

                MemoryRoster var96 = this.memories;
                String var340 = var2[3];
                if (var96.rename(var367, var340, 1754029687)) {
                    String var248 = var367.getName();
                    String var342 = var2[3];
                    Component var250 = Text.legacy("&6" + var248 + " &7is now the memory &f" + var342, 373192400);
                    var1.sendMessage(var250);
                } else {
                    Component var252 = Text.legacy("&cThat name is worn by another memory or a bot.", 373192400);
                    var1.sendMessage(var252);
                }
                break;
            case "skin":
                if (var2.length < 4) {
                    Component var219 = Text.legacy("&eUsage: " + var3 + " memory skin <player> <sourcePlayer|off>", 373192400);
                    var1.sendMessage(var219);
                    return;
                }

                Player var12 = Bukkit.getPlayerExact(var2[2]);
                if (var12 == null) {
                    Component var187 = Text.legacy("&cPlayer must be online: &f" + var2[2], 373192400);
                    var1.sendMessage(var187);
                    return;
                }

                MemoryRoster var40 = this.memories;
                UUID var189 = var12.getUniqueId();
                if (!var40.isMemory(var189, 1860168534)) {
                    Component var216 = Text.legacy("&c" + var12.getName() + " is not a memory (memory add first).", 373192400);
                    var1.sendMessage(var216);
                    return;
                }

                if (var2[3].equalsIgnoreCase("off")) {
                    NickManager var56 = this.nick;
                    var56.clearSkin(var12, 1699994696);
                    MemoryRoster var209 = this.memories;
                    UUID var335 = var12.getUniqueId();
                    Component var212 = Text.legacy("&7The memory &f" + var209.nameOf(var335, 420288045) + " &7wears their own skin again.", 373192400);
                    var1.sendMessage(var212);
                } else {
                    Player var13 = Bukkit.getPlayerExact(var2[3]);
                    if (var13 == null) {
                        Component var206 = Text.legacy("&cSkin source must be online: &f" + var2[3], 373192400);
                        var1.sendMessage(var206);
                        return;
                    }

                    NickManager var50 = this.nick;
                    if (var50.setSkinFrom(var12, var13, 1053928318)) {
                        MemoryRoster var195 = this.memories;
                        UUID var330 = var12.getUniqueId();
                        String var196 = var195.nameOf(var330, 420288045);
                        String var332 = var13.getName();
                        Component var198 = Text.legacy("&7The memory &f" + var196 + " &7now wears &f" + var332 + "&7's skin.", 373192400);
                        var1.sendMessage(var198);
                    } else {
                        Component var202 = Text.legacy("&c" + var13.getName() + " has no readable skin.", 373192400);
                        var1.sendMessage(var202);
                    }
                }
                break;
            case "npc":
                if (!this.npcs.available(1626139367)) {
                    Component var295 = Text.legacy("&cCitizens isn't installed — NPC memories need it.", 373192400);
                    var1.sendMessage(var295);
                    return;
                }

                String var164;
                if (var2.length >= 3) {
                    var164 = var2[2];
                } else {
                    var164 = "1";
                }

                String var370 = var164;
                if (var164.equalsIgnoreCase("name") || var164.equalsIgnoreCase("skin")) {
                    this.npcNameOrSkin(var1, var2, var3, 1037002731);
                    return;
                }

                if (var164.equalsIgnoreCase("clear")) {
                    int var372 = this.npcs.clearAll(897194561);
                    Component var303 = Text.legacy("&7Cleared &f" + var372 + "&7 NPC memories.", 373192400);
                    var1.sendMessage(var303);
                    return;
                }

                if (!(var1 instanceof Player var373)) {
                    Component var305 = Text.legacy("&cRun this in-game — they spawn at you.", 373192400);
                    var1.sendMessage(var305);
                    return;
                }

                int var374;
                try {
                    int var353 = Integer.parseInt(var370);
                    int var307 = Math.min(20, var353);
                    var374 = Math.max(1, var307);
                } catch (NumberFormatException var375) {
                    Component var320 = Text.legacy("&eUsage: " + var3 + " memory npc <count|clear> [skin]", 373192400);
                    var1.sendMessage(var320);
                    return;
                }

                String var160;
                if (var2.length >= 4) {
                    var160 = var2[3];
                } else {
                    var160 = null;
                }

                String var15 = var160;
                int var16 = 0;
                String var17 = null;

                for (int var18 = 0; var18 < var374; var18++) {
                    MemoryNpcManager var155 = this.npcs;
                    Location var312 = var373.getLocation();
                    String var19 = var155.spawn(var312, var15, 1274566050);
                    if (var19 == null) {
                        break;
                    }

                    var16++;
                    var17 = var19;
                }

                Component var317;
                if (var16 == 0) {
                    var317 = Text.legacy("&cCouldn't spawn — the memory pool is exhausted or the spawn failed.", 373192400);
                } else {
                    String var357;
                    if (var16 == 1) {
                        var357 = "y &7(" + var17 + ")";
                    } else {
                        var357 = "ies";
                    }

                    var317 = Text.legacy("&6Spawned &f" + var16 + "&6 NPC memor" + var357, 373192400);
                }

                var1.sendMessage(var317);
                break;
            default:
                Component var283 = Text.legacy("&eUsage: " + var3 + " memory <add|remove|list|name|skin|npc> [player|count] [name]", 373192400);
                var1.sendMessage(var283);
        }
    }

    private void npcNameOrSkin(CommandSender var1, String[] var2, String var3, int var4) {
        boolean var9 = var2[2].equalsIgnoreCase("name");
        if (var2.length < 5) {
            String var87;
            if (var9) {
                var87 = "name <bot> <newname>";
            } else {
                var87 = "skin <bot> <sourcePlayer|off>";
            }

            Component var56 = Text.legacy("&eUsage: " + var3 + " memory npc " + var87, 373192400);
            var1.sendMessage(var56);
        } else {
            String var10 = var2[3];
            boolean var11 = false;

            for (String var31 : this.npcs.liveNames(970856627)) {
                if (var31.equalsIgnoreCase(var10)) {
                    var11 = true;
                    break;
                }
            }

            if (!var11) {
                Component var79 = Text.legacy("&cNo NPC memory called &f" + var10 + " &c- see " + var3 + " memory list.", 373192400);
                var1.sendMessage(var79);
            } else {
                if (var9) {
                    MemoryNpcManager var37 = this.npcs;
                    String var81 = var2[4];
                    if (var37.rename(var10, var81, 469880051)) {
                        String var83 = var2[4];
                        Component var64 = Text.legacy("&6" + var10 + " &7is now &f" + var83, 373192400);
                        var1.sendMessage(var64);
                    } else {
                        Component var61 = Text.legacy("&cThat name is worn by a memory or another bot.", 373192400);
                        var1.sendMessage(var61);
                    }
                } else {
                    String var50;
                    if (var2[4].equalsIgnoreCase("off")) {
                        var50 = null;
                    } else {
                        var50 = var2[4];
                    }

                    MemoryNpcManager var46 = this.npcs;
                    if (var46.reskin(var10, var50, 635373887)) {
                        Component var74;
                        if (var50 == null) {
                            var74 = Text.legacy("&6" + var10 + " &7wears a plain skin again.", 373192400);
                        } else {
                            var74 = Text.legacy("&6" + var10 + " &7now wears &f" + var50 + "&7's skin (fetched by name - may take a moment).", 373192400);
                        }

                        var1.sendMessage(var74);
                    } else {
                        Component var70 = Text.legacy("&cCouldn't change that bot's skin.", 373192400);
                        var1.sendMessage(var70);
                    }
                }
            }
        }
    }

    private void usage(CommandSender var1, String var2, int var3) {
        Component var7 = Text.legacy("&eUsage: " + var2 + " <name|skin|reset|memory> <player> [value]", 373192400);
        var1.sendMessage(var7);
    }

    public List<String> onTabComplete(CommandSender var1, Command var2, String var3, String[] var4) {
        return this.complete(var1, var4, 341247110);
    }

    public List<String> complete(CommandSender var1, String[] var2, int var3) {
        ArrayList var4 = new ArrayList();
        ArrayList var9 = var4;
        if (!var1.hasPermission("bliss.admin")) {
            return var4;
        } else {
            if (var2.length == 1) {
                String var17 = "name";
                String var206 = "skin";
                String var5 = "reset";
                String var6 = "memory";

                for (String var11 : List.of(var17, var206, var5, var6)) {
                    String var209 = var2[0].toLowerCase();
                    if (var11.startsWith(var209)) {
                        var9.add(var11);
                    }
                }
            } else if (var2.length == 2 && var2[0].equalsIgnoreCase("memory")) {
                String var192 = "add";
                String var288 = "remove";
                String var302 = "list";
                String var305 = "name";
                String var306 = "skin";
                String var307 = "npc";

                for (String var323 : List.of(var192, var288, var302, var305, var306, var307)) {
                    String var291 = var2[1].toLowerCase();
                    if (var323.startsWith(var291)) {
                        var9.add(var323);
                    }
                }
            } else if (var2.length == 3 && var2[0].equalsIgnoreCase("memory") && var2[1].equalsIgnoreCase("npc")) {
                String var43 = "1";
                String var219 = "5";
                String var294 = "10";
                String var304 = "clear";
                String var7 = "name";
                String var8 = "skin";

                for (String var316 : List.of(var43, var219, var294, var304, var7, var8)) {
                    String var222 = var2[2].toLowerCase();
                    if (var316.startsWith(var222)) {
                        var9.add(var316);
                    }
                }
            } else if (var2.length == 4
                && var2[0].equalsIgnoreCase("memory")
                && var2[1].equalsIgnoreCase("npc")
                && (var2[2].equalsIgnoreCase("name") || var2[2].equalsIgnoreCase("skin"))) {
                for (String var317 : this.npcs.liveNames(970856627)) {
                    String var79 = var317.toLowerCase();
                    String var235 = var2[3].toLowerCase();
                    if (var79.startsWith(var235)) {
                        var9.add(var317);
                    }
                }
            } else if (var2.length == 5 && var2[0].equalsIgnoreCase("memory") && var2[1].equalsIgnoreCase("npc") && var2[2].equalsIgnoreCase("skin")) {
                var4.add("off");

                for (Player var318 : Bukkit.getOnlinePlayers()) {
                    String var105 = var318.getName().toLowerCase();
                    String var247 = var2[4].toLowerCase();
                    if (var105.startsWith(var247)) {
                        String var249 = var318.getName();
                        var9.add(var249);
                    }
                }
            } else if (var2.length == 4 && var2[0].equalsIgnoreCase("memory") && var2[1].equalsIgnoreCase("skin")) {
                var4.add("off");

                for (Player var319 : Bukkit.getOnlinePlayers()) {
                    String var128 = var319.getName().toLowerCase();
                    String var258 = var2[3].toLowerCase();
                    if (var128.startsWith(var258)) {
                        String var260 = var319.getName();
                        var9.add(var260);
                    }
                }
            } else if (var2.length == 3 && var2[0].equalsIgnoreCase("memory")) {
                for (Player var320 : Bukkit.getOnlinePlayers()) {
                    String var146 = var320.getName().toLowerCase();
                    String var266 = var2[2].toLowerCase();
                    if (var146.startsWith(var266)) {
                        String var268 = var320.getName();
                        var9.add(var268);
                    }
                }
            } else if (var2.length == 2) {
                for (Player var322 : Bukkit.getOnlinePlayers()) {
                    String var188 = var322.getName().toLowerCase();
                    String var285 = var2[1].toLowerCase();
                    if (var188.startsWith(var285)) {
                        String var287 = var322.getName();
                        var9.add(var287);
                    }
                }
            } else if (var2.length == 3 && var2[0].equalsIgnoreCase("skin")) {
                var4.add("off");

                for (Player var321 : Bukkit.getOnlinePlayers()) {
                    String var175 = var321.getName().toLowerCase();
                    String var280 = var2[2].toLowerCase();
                    if (var175.startsWith(var280)) {
                        String var282 = var321.getName();
                        var9.add(var282);
                    }
                }
            } else if (var2.length == 3 && var2[0].equalsIgnoreCase("name")) {
                var4.add("off");
            }

            return var9;
        }
    }
}
