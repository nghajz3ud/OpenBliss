package com.bliss.nick;

import com.bliss.BlissPlugin;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.entity.Player;

final class RealNameTable {
    private static boolean broken;
    private static Field tableField;

    private RealNameTable() {
    }

    // $VF: Handled exception range with multiple entry points by splitting it
    // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
    static void reconcile(BlissPlugin var0, Function<Player, String> var1, int var2) {
        if (!broken) {
            Exception var10000;
            label58: {
                Object var9;
                try {
                    Server var8 = var0.getServer();
                    Class var16 = var8.getClass();
                    String var4 = "getHandle";
                    Class[] var55 = new Class[0];
                    Method var17 = var16.getMethod(var4, var55);
                    Object[] var57 = new Object[0];
                    var9 = var17.invoke(var8, var57);
                    if (tableField == null) {
                        Class var21 = var9.getClass();
                        String var45 = "playersByName";
                        tableField = find(var21, var45, 535350816);
                    }
                } catch (Exception var72) {
                    var10000 = var72;
                    boolean var10001 = false;
                    break label58;
                }

                Map var10;
                Iterator var11;
                try {
                    Field var23 = tableField;
                    var10 = (Map)var23.get(var9);
                    var10.clear();
                    var11 = var0.getServer().getOnlinePlayers().iterator();
                } catch (Exception var71) {
                    var10000 = var71;
                    boolean var73 = false;
                    break label58;
                }

                while (true) {
                    Player var12;
                    try {
                        if (!var11.hasNext()) {
                            return;
                        }

                        var12 = (Player)var11.next();
                        if (var12.hasMetadata("NPC")) {
                            continue;
                        }
                    } catch (Exception var70) {
                        var10000 = var70;
                        boolean var74 = false;
                        break;
                    }

                    try {
                        String var53 = (String)var1.apply(var12);
                        Locale var59 = Locale.ROOT;
                        String var54 = var53.toLowerCase(var59);
                        Class var61 = var12.getClass();
                        String var6 = "getHandle";
                        Class[] var65 = new Class[0];
                        Method var62 = var61.getMethod(var6, var65);
                        Object[] var67 = new Object[0];
                        Object var63 = var62.invoke(var12, var67);
                        var10.put(var54, var63);
                    } catch (Exception var69) {
                        var10000 = var69;
                        boolean var75 = false;
                        break;
                    }
                }
            }

            Exception var68 = var10000;
            broken = true;
            Logger var41 = var0.getLogger();
            String var50 = "nick: can't reach the server's name table (" + var68 + ") - commands will keep seeing nicks as names";
            var41.warning(var50);
        }
    }

    private static Field find(Class<?> var0, String var1, int var2) throws NoSuchFieldException {
        for (Class var6 = var0; var6 != null; var6 = var6.getSuperclass()) {
            try {
                Field var7 = var6.getDeclaredField(var1);
                var7.setAccessible(true);
                return var7;
            } catch (NoSuchFieldException var21) {
            }
        }

        String var20 = var0.getName();
        String var19 = var1 + " in " + var20;
        NoSuchFieldException var9 = new NoSuchFieldException(var19);
        throw var9;
    }
}
