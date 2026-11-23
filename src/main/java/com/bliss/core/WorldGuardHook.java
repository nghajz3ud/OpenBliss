package com.bliss.core;

import java.lang.reflect.Method;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public final class WorldGuardHook {
    private static boolean checked = false;
    private static boolean available = false;
    private static Object container;
    private static Method mContainerGet;
    private static Method mAdaptWorld;
    private static Method mAsBlockVector;
    private static Method mGetApplicableRegions;
    private static Method mGetId;

    private WorldGuardHook() {
    }

    private static synchronized void init(int var0) {
        if (!checked) {
            checked = true;

            int var97;
            label44: {
                try {
                    if (Bukkit.getPluginManager().getPlugin("WorldGuard") == null) {
                        return;
                    }
                } catch (Throwable var99) {
                    var97 = 1412981699;
                    break label44;
                }

                Class var7;
                Class var8;
                Class var9;
                Class var10;
                Class var11;
                Class var12;
                try {
                    Class var6 = Class.forName("com.sk89q.worldguard.WorldGuard");
                    var7 = Class.forName("com.sk89q.worldedit.world.World");
                    var8 = Class.forName("com.sk89q.worldedit.math.BlockVector3");
                    var9 = Class.forName("com.sk89q.worldedit.bukkit.BukkitAdapter");
                    var10 = Class.forName("com.sk89q.worldguard.protection.regions.RegionContainer");
                    var11 = Class.forName("com.sk89q.worldguard.protection.managers.RegionManager");
                    var12 = Class.forName("com.sk89q.worldguard.protection.regions.ProtectedRegion");
                    String var57 = "getInstance";
                    Class[] var68 = new Class[0];
                    Method var33 = var6.getMethod(var57, var68);
                    Object[] var70 = new Object[0];
                    Object var13 = var33.invoke(null, var70);
                    Class var36 = var13.getClass();
                    String var59 = "getPlatform";
                    Class[] var72 = new Class[0];
                    Method var37 = var36.getMethod(var59, var72);
                    Object[] var74 = new Object[0];
                    Object var14 = var37.invoke(var13, var74);
                    Class var40 = var14.getClass();
                    String var61 = "getRegionContainer";
                    Class[] var76 = new Class[0];
                    Method var41 = var40.getMethod(var61, var76);
                    Object[] var78 = new Object[0];
                    container = var41.invoke(var14, var78);
                    if (container == null) {
                        return;
                    }
                } catch (Throwable var100) {
                    var97 = 1412981699;
                    break label44;
                }

                try {
                    String var63 = "get";
                    Class[] var80 = new Class[]{var7};
                    mContainerGet = var10.getMethod(var63, var80);
                    String var64 = "adapt";
                    Class[] var82 = new Class[]{World.class};
                    mAdaptWorld = var9.getMethod(var64, var82);
                    String var65 = "asBlockVector";
                    Class[] var84 = new Class[]{Location.class};
                    mAsBlockVector = var9.getMethod(var65, var84);
                    String var66 = "getApplicableRegions";
                    Class[] var86 = new Class[]{var8};
                    mGetApplicableRegions = var11.getMethod(var66, var86);
                    String var67 = "getId";
                    Class[] var88 = new Class[0];
                    mGetId = var12.getMethod(var67, var88);
                    available = true;
                    return;
                } catch (Throwable var98) {
                    var97 = 1412981699;
                }
            }

            available = false;
        }
    }

    public static boolean isAvailable() {
        init(711885077);
        return available;
    }

    // $VF: Handled exception range with multiple entry points by splitting it
    // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
    public static boolean isInRegion(Location var0, String var1, int var2) {
        if (var0 != null && var0.getWorld() != null && var1 != null) {
            init(711885077);
            if (!available) {
                return false;
            } else {
                label72: {
                    int var73;
                    label66: {
                        Object var9;
                        try {
                            Method var21 = mAdaptWorld;
                            Object[] var55 = new Object[1];
                            World var67 = var0.getWorld();
                            var55[0] = var67;
                            Object var8 = var21.invoke(null, var55);
                            Method var23 = mContainerGet;
                            Object var49 = container;
                            Object[] var57 = new Object[]{var8};
                            var9 = var23.invoke(var49, var57);
                            if (var9 == null) {
                                return false;
                            }
                        } catch (Throwable var76) {
                            var73 = 1978746913;
                            break label66;
                        }

                        label51: {
                            Iterator var13;
                            try {
                                Method var26 = mAsBlockVector;
                                Object[] var59 = new Object[]{var0};
                                Object var10 = var26.invoke(null, var59);
                                Method var28 = mGetApplicableRegions;
                                Object[] var61 = new Object[]{var10};
                                if (!(var28.invoke(var9, var61) instanceof Iterable var12)) {
                                    break label72;
                                }

                                var13 = var12.iterator();
                            } catch (Throwable var75) {
                                boolean var10001 = false;
                                break label51;
                            }

                            while (true) {
                                try {
                                    if (!var13.hasNext()) {
                                        break label72;
                                    }

                                    Object var14 = var13.next();
                                    Method var41 = mGetId;
                                    Object[] var63 = new Object[0];
                                    Object var15 = var41.invoke(var14, var63);
                                    String var54 = String.valueOf(var15);
                                    if (var1.equalsIgnoreCase(var54)) {
                                        return true;
                                    }
                                } catch (Throwable var74) {
                                    boolean var78 = false;
                                    break;
                                }
                            }
                        }

                        var73 = 1978746913;
                    }

                    return false;
                }

                return false;
            }
        } else {
            return false;
        }
    }
}
