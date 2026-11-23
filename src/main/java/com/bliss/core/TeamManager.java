package com.bliss.core;

import com.bliss.BlissPlugin;
import com.bliss.gems.GrantedEffect;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Logger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

public final class TeamManager {
    public static final String[] GEM_TEAMS;
    public static final String ENCH_OBI_TEAM = "EnchObi";
    public static final String PED_ROLL_PREFIX = "PedRoll_";

    private TeamManager() {
    }

    public static void safeRemoveEntry(Team var0, String var1, int var2) {
        if (var0 != null && var1 != null && var0.hasEntry(var1)) {
            Scoreboard var7 = var0.getScoreboard();
            if (var7 == null) {
                var7 = Bukkit.getScoreboardManager().getMainScoreboard();
            }

            String var8 = var0.getName();
            ChatColor var9 = ChatColor.RESET;

            try {
                var9 = var0.getColor();
            } catch (Throwable var129) {
            }

            Component var10 = null;
            Component var11 = null;

            try {
                var10 = var0.prefix();
            } catch (Throwable var128) {
            }

            try {
                var11 = var0.suffix();
            } catch (Throwable var127) {
            }

            boolean var12 = var0.allowFriendlyFire();
            boolean var13 = var0.canSeeFriendlyInvisibles();
            EnumMap var43 = new EnumMap(Option.class);
            EnumMap var14 = var43;

            for (Option var18 : Option.values()) {
                try {
                    OptionStatus var108 = var0.getOption(var18);
                    var14.put((Enum)var18, var108);
                } catch (Throwable var126) {
                }
            }

            Set var110 = var0.getEntries();
            HashSet var54 = new HashSet(var110);
            var54.remove(var1);
            var0.unregister();
            Team var118 = var7.getTeam(var8);
            if (var118 == null) {
                var118 = var7.registerNewTeam(var8);
            }

            try {
                var118.setColor(var9);
            } catch (Throwable var125) {
            }

            if (var10 != null) {
                var118.prefix(var10);
            }

            if (var11 != null) {
                var118.suffix(var11);
            }

            var118.setAllowFriendlyFire(var12);
            var118.setCanSeeFriendlyInvisibles(var13);

            for (java.util.Map.Entry<Option, OptionStatus> var122 : ((java.util.Map<Option, OptionStatus>)var14).entrySet()) {
                try {
                    Option var105 = var122.getKey();
                    OptionStatus var113 = var122.getValue();
                    var118.setOption(var105, var113);
                } catch (Throwable var124) {
                }
            }

            for (String var123 : (Set<String>)var54) {
                var118.addEntry(var123);
            }
        }
    }

    public static void install(BlissPlugin var0, int var1) {
        Scoreboard var5 = Bukkit.getScoreboardManager().getMainScoreboard();
        String var3 = "EnchObi";
        Team var17 = ensureTeam(var5, var3, 448401175);
        ChatColor var50 = ChatColor.YELLOW;
        var17.setColor(var50);

        for (String var9 : GEM_TEAMS) {
            ensureTeam(var5, var9, 448401175);
        }

        for (GemType var68 : GemType.values()) {
            GemType var56 = GemType.NONE;
            if (var68 != var56) {
                String var10 = "PedRoll_" + var68.name();
                Team var11 = var5.getTeam(var10);
                if (var11 != null) {
                    var11.unregister();
                }

                Team var12 = var5.registerNewTeam(var10);
                NamedTextColor var13 = pedRollColor(var68, 794544213);
                var12.color(var13);

                try {
                    ChatColor var61 = pedRollChatColor(var68, 609911204);
                    var12.setColor(var61);
                } catch (Throwable var69) {
                }

                TextComponent var63 = Component.text("");
                Component var64 = var63.color(var13);
                var12.prefix(var64);
            }
        }
    }

    // $VF: Handled exception range with multiple entry points by splitting it
    // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
    public static void forceBroadcastTeam(Team var0, NamedTextColor var1, int var2) {
        if (var0 != null) {
            Throwable var10000;
            label95: {
                Object var8;
                try {
                    var8 = extractNmsTeam(var0, 1403973175);
                    if (var8 == null) {
                        Logger var25 = Bukkit.getLogger();
                        String var70 = "[Bliss] forceBroadcastTeam: no PlayerTeam field on " + var0.getClass().getName();
                        var25.warning(var70);
                        return;
                    }
                } catch (Throwable var110) {
                    var10000 = var110;
                    break label95;
                }

                try {
                    Class var9 = Class.forName("net.minecraft.ChatFormatting");
                    Object var10 = chatFormattingFor(var9, var1, 484813140);
                    Class var33 = var8.getClass();
                    Method var11 = findMethodByParam(var33, var9, 2128720532);
                    if (var11 != null) {
                        Object[] var81 = new Object[]{var10};
                        var11.invoke(var8, var81);
                    }
                } catch (Throwable var109) {
                    var10000 = var109;
                    boolean var10001 = false;
                    break label95;
                }

                Object var15;
                Class var16;
                Method var17;
                Iterator var18;
                try {
                    Class var12 = var8.getClass();
                    Class var42 = Class.forName("net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket");
                    String var77 = "createAddOrModifyPacket";
                    Class[] var83 = new Class[]{var12, null};
                    Class var97 = boolean.class;
                    var83[1] = var97;
                    Method var44 = var42.getMethod(var77, var83);
                    Object[] var85 = new Object[]{var8, null};
                    Boolean var100 = false;
                    var85[1] = var100;
                    var15 = var44.invoke(null, var85);
                    var16 = Class.forName("net.minecraft.network.protocol.Packet");
                    var17 = null;
                    var18 = Bukkit.getOnlinePlayers().iterator();
                } catch (Throwable var108) {
                    var10000 = var108;
                    boolean var111 = false;
                    break label95;
                }

                while (true) {
                    Object var20;
                    try {
                        if (!var18.hasNext()) {
                            return;
                        }

                        var20 = extractServerPlayer((Player)var18.next(), 469592280);
                        if (var20 == null) {
                            continue;
                        }
                    } catch (Throwable var107) {
                        var10000 = var107;
                        boolean var112 = false;
                        break;
                    }

                    Object var21;
                    try {
                        var21 = extractConnection(var20, 1573350724);
                        if (var21 == null) {
                            continue;
                        }
                    } catch (Throwable var106) {
                        var10000 = var106;
                        boolean var113 = false;
                        break;
                    }

                    try {
                        if (var17 == null) {
                            Class var64 = var21.getClass();
                            String var79 = "send";
                            Class[] var87 = new Class[]{var16};
                            var17 = var64.getMethod(var79, var87);
                        }
                    } catch (Throwable var105) {
                        var10000 = var105;
                        boolean var114 = false;
                        break;
                    }

                    try {
                        Object[] var89 = new Object[]{var15};
                        var17.invoke(var21, var89);
                    } catch (Throwable var104) {
                        var10000 = var104;
                        boolean var115 = false;
                        break;
                    }
                }
            }

            Throwable var103 = var10000;
            Logger var27 = Bukkit.getLogger();
            String var73 = "[Bliss] forceBroadcastTeam failed: " + var103;
            var27.warning(var73);
        }
    }

    private static Object chatFormattingFor(Class<?> var0, NamedTextColor var1, int var2) {
        String var9;
        if (var1 == null) {
            var9 = "WHITE";
        } else {
            var9 = var1.toString().toUpperCase();
        }

        return Enum.valueOf((Class)var0, var9);
    }

    private static Method findMethodByParam(Class<?> var0, Class<?> var1, int var2) {
        for (Method var8 : var0.getMethods()) {
            Class[] var9 = var8.getParameterTypes();
            if (var9.length == 1) {
                Class var24 = var9[0];
                if (var24.isAssignableFrom(var1)) {
                    return var8;
                }
            }
        }

        for (Class var60 = var0; var60 != null; var60 = var60.getSuperclass()) {
            for (Method var64 : var60.getDeclaredMethods()) {
                Class[] var10 = var64.getParameterTypes();
                if (var10.length == 1) {
                    Class var42 = var10[0];
                    if (var42.isAssignableFrom(var1)) {
                        try {
                            var64.setAccessible(true);
                            return var64;
                        } catch (Throwable var65) {
                        }
                    }
                }
            }
        }

        return null;
    }

    private static Object extractNmsTeam(Team var0, int var1) {
        for (Class var4 = var0.getClass(); var4 != null; var4 = var4.getSuperclass()) {
            for (Field var8 : var4.getDeclaredFields()) {
                if (var8.getType().getName().endsWith(".PlayerTeam") || var8.getType().getSimpleName().equals("PlayerTeam")) {
                    try {
                        var8.setAccessible(true);
                        Object var9 = var8.get(var0);
                        if (var9 != null) {
                            return var9;
                        }
                    } catch (Throwable var42) {
                    }
                }
            }
        }

        return null;
    }

    private static Object extractServerPlayer(Player var0, int var1) {
        try {
            Class var11 = var0.getClass();
            String var3 = "getHandle";
            Class[] var44 = new Class[0];
            Method var13 = var11.getMethod(var3, var44);
            Object[] var46 = new Object[0];
            return var13.invoke(var0, var46);
        } catch (Throwable var50) {
            for (Class var48 = var0.getClass(); var48 != null; var48 = var48.getSuperclass()) {
                for (Field var9 : var48.getDeclaredFields()) {
                    if (var9.getType().getName().endsWith(".ServerPlayer")) {
                        try {
                            var9.setAccessible(true);
                            return var9.get(var0);
                        } catch (Throwable var49) {
                        }
                    }
                }
            }

            return null;
        }
    }

    private static Object extractConnection(Object var0, int var1) {
        for (Class var4 = var0.getClass(); var4 != null; var4 = var4.getSuperclass()) {
            for (Field var8 : var4.getDeclaredFields()) {
                String var9 = var8.getType().getName();
                if (var9.endsWith(".ServerGamePacketListenerImpl") || var9.endsWith(".ServerCommonPacketListenerImpl")) {
                    try {
                        var8.setAccessible(true);
                        Object var10 = var8.get(var0);
                        if (var10 != null) {
                            return var10;
                        }
                    } catch (Throwable var42) {
                    }
                }
            }
        }

        return null;
    }

    public static String pedRollTeamName(GemType var0, int var1) {
        return "PedRoll_" + var0.name();
    }

    public static void applyGemGlow(Player var0, GemType var1, int var2, int var3) {
        Scoreboard var12 = Bukkit.getScoreboardManager().getMainScoreboard();

        for (GemType var16 : GemType.values()) {
            GemType var39 = GemType.NONE;
            if (var16 != var39) {
                String var41 = pedRollTeamName(var16, 965817203);
                Team var27 = var12.getTeam(var41);
                String var43 = var0.getName();
                safeRemoveEntry(var27, var43, 1412413626);
            }
        }

        GemType var44 = GemType.NONE;
        if (var1 != var44) {
            String var46 = pedRollTeamName(var1, 965817203);
            Team var54 = var12.getTeam(var46);
            if (var54 != null) {
                String var48 = var0.getName();
                var54.addEntry(var48);
                NamedTextColor var50 = pedRollColor(var1, 794544213);
                forceBroadcastTeam(var54, var50, 612238929);
            }

            PotionEffectType var6 = PotionEffectType.GLOWING;
            PotionEffect var34 = new PotionEffect(var6, var2, 0, false, false, false);
            var0.addPotionEffect(var34, true);
            GrantedEffect.grant(var0, var34, 491118892);
        }
    }

    public static void clearGemGlow(Player var0, int var1) {
        Scoreboard var4 = Bukkit.getScoreboardManager().getMainScoreboard();

        for (GemType var8 : GemType.values()) {
            GemType var23 = GemType.NONE;
            if (var8 != var23) {
                String var25 = pedRollTeamName(var8, 965817203);
                Team var20 = var4.getTeam(var25);
                String var27 = var0.getName();
                safeRemoveEntry(var20, var27, 1412413626);
            }
        }

        PotionEffectType var21 = PotionEffectType.GLOWING;
        GrantedEffect.strip(var0, var21, 553187889);
    }

    private static NamedTextColor pedRollColor(GemType var0, int var1) {
        return switch (var0) {
            case STRENGTH-> NamedTextColor.DARK_RED;
            case FIRE-> NamedTextColor.GOLD;
            case WEALTH-> NamedTextColor.DARK_GREEN;
            case SPEED-> NamedTextColor.YELLOW;
            case PUFF-> NamedTextColor.WHITE;
            case ASTRA-> NamedTextColor.DARK_PURPLE;
            case LIFE-> NamedTextColor.LIGHT_PURPLE;
            case FLUX-> NamedTextColor.AQUA;
            default -> NamedTextColor.WHITE;
        };
    }

    private static ChatColor pedRollChatColor(GemType var0, int var1) {
        return switch (var0) {
            case STRENGTH-> ChatColor.DARK_RED;
            case FIRE-> ChatColor.GOLD;
            case WEALTH-> ChatColor.DARK_GREEN;
            case SPEED-> ChatColor.YELLOW;
            case PUFF-> ChatColor.WHITE;
            case ASTRA-> ChatColor.DARK_PURPLE;
            case LIFE-> ChatColor.LIGHT_PURPLE;
            case FLUX-> ChatColor.AQUA;
            default -> ChatColor.WHITE;
        };
    }

    private static Team ensureTeam(Scoreboard var0, String var1, int var2) {
        Team var5 = var0.getTeam(var1);
        if (var5 == null) {
            var5 = var0.registerNewTeam(var1);
        }

        return var5;
    }

    static {
        String[] var3 = new String[8];
        String var2 = "Strength";
        var3[0] = var2;
        String var11 = "Fire";
        var3[1] = var11;
        String var12 = "Wealth";
        var3[2] = var12;
        String var13 = "Speed";
        var3[3] = var13;
        String var14 = "Puff";
        var3[4] = var14;
        String var15 = "Astra";
        var3[5] = var15;
        String var16 = "Life";
        var3[6] = var16;
        String var17 = "Flux";
        var3[7] = var17;
        GEM_TEAMS = var3;
    }
}
