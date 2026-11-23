package com.bliss.honored;

import com.bliss.BlissPlugin;
import com.bliss.core.GlobalState;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.bukkit.entity.TextDisplay.TextAlignment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

public final class ArmorLimitHologram implements Listener {
    private static final TextColor NAME_GOLD = TextColor.color(16770148);
    private static final TextColor NOTE_GREY = TextColor.color(11184810);
    private static final String TAG = "BlissArmorLimit";
    private static final String TAG_UP = "BlissArmorLimitUp";
    private static final String TAG_DOWN = "BlissArmorLimitDown";
    private static final String TAG_STAND = "BlissArmorLimitStand";
    private static final NamespacedKey KEY_PIECE = NamespacedKey.fromString("bliss:armor_limit_piece");
    private static final int PILLAR_H = 1;
    private static final double EDGE_LIFT = 0.4;
    private static final float EDGE_SCALE = 1.26F;
    private static final Color EDGE_GLOW = Color.fromRGB(255, 228, 100);
    private static final double EDGE_NAME = 0.55;
    private static final int RING_SLOTS = 9;
    private static final double RING_R = 8.0;
    private static final double FLANK_SLOT = 0.7;
    private static final double[] PIECE_Y;
    private static final ArmorLimits.Piece[] PIECES;
    private static final double ARROW_OUT = 0.95;
    private static final float ARROW_BOX = 0.45F;
    private static final double TITLE_OVER = 2.35;
    private static final String GLYPH_RIGHT = "▶";
    private static final String GLYPH_LEFT = "◀";
    private static final String GLYPH_X = "✖";
    private static final String GLYPH_TICK = "✔";
    private static final String GLYPH_UP = "▲";
    private static final String GLYPH_DOWN = "▼";
    private static final TextColor RED = TextColor.color(16730698);
    private static final TextColor GREEN = TextColor.color(6021979);
    private static final String TAG_ON = "BlissArmorLimitOn";
    private static final String TAG_OFF = "BlissArmorLimitOff";
    private static final NamespacedKey KEY_SWITCH = NamespacedKey.fromString("bliss:armor_limit_switch");
    private static final double TOGGLE_Y = 1.2;
    private static final float TOGGLE_SCALE = 1.4F;
    private static final double TOGGLE_NAME = 0.7;
    private static final double TOGGLE_ROW = 1.25;
    private static final double ROW_GAP = 0.85;
    private static final float MARK_SCALE = 1.6F;
    private static final float MARK_BOX = 0.6F;
    private static final double SPIN_STEP = 0.05;
    private static final double TUMBLE_RATIO = 0.6180339887;
    private static final double BOB_AMP = 0.13;
    private static final double BOB_RATE = 0.9;
    private static final float SHIELD_YAW_OFF = -90.0F;
    private static final double VILLAGER_HEAD = 2.05;
    private static final String TAG_MOD_UP = "BlissArmorLimitModUp";
    private static final String TAG_MOD_DOWN = "BlissArmorLimitModDown";
    private static final NamespacedKey KEY_DIAL = NamespacedKey.fromString("bliss:armor_limit_dial");
    private static final double MOD_DOWN_Y = 1.15;
    private static final double MOD_TEXT_Y = 1.6;
    private static final double MOD_UP_Y = 2.05;
    private static final float MOD_BOX = 0.4F;
    private static final float MOD_SCALE = 1.35F;
    private final BlissPlugin plugin;
    private final ArmorLimits limits;
    private Location anchor;
    private final List<Entity> parts;
    private ArmorStand stand;
    private final List<ArmorLimitHologram.Spin> spinners;
    private final EnumMap<ArmorLimitHologram.Switch, TextDisplay> stateLabels;
    private final EnumMap<ArmorLimitHologram.Dial, TextDisplay> dialLabels;
    private BukkitTask spinTask;
    private double spinAngle;
    private double tumbleAngle;
    private double bobAngle;

    public ArmorLimitHologram(BlissPlugin var1, ArmorLimits var2, int var3) {
        ArrayList var5 = new ArrayList();
        this.parts = var5;
        ArrayList var13 = new ArrayList();
        this.spinners = var13;
        EnumMap var14 = new EnumMap<>(ArmorLimitHologram.Switch.class);
        this.stateLabels = var14;
        EnumMap var15 = new EnumMap<>(ArmorLimitHologram.Dial.class);
        this.dialLabels = var15;
        this.plugin = var1;
        this.limits = var2;
    }

    public void reconcile(int var1) {
        this.stopSpin(22866284);
        despawnAll(2140090338);
        this.parts.clear();
        this.stand = null;
        ControlRoomManager var6 = this.plugin.controlRoom(1275615420);
        Location var18;
        if (var6 == null) {
            var18 = null;
        } else {
            var18 = var6.armorSpot(1529295521);
        }

        if (var18 == null) {
            this.anchor = null;
            this.restorePillar(1836782931);
        } else {
            Location var47 = var18.clone();
            this.anchor = var47;
            Block var8 = this.anchor.getBlock();
            Location var27 = this.anchor;
            double var52 = (double)var8.getX() + 0.5;
            var27.setX(var52);
            Location var29 = this.anchor;
            double var56 = (double)var8.getY();
            var29.setY(var56);
            Location var31 = this.anchor;
            double var62 = (double)var8.getZ() + 0.5;
            var31.setZ(var62);
            Location var9 = var6.shrineBlock(1056167168);
            label25:
            if (var9 != null) {
                World var36 = var9.getWorld();
                World var65 = var8.getWorld();
                if (var36 == var65) {
                    int var38 = var9.getBlockX();
                    int var67 = var8.getX();
                    if (var38 == var67) {
                        int var40 = var9.getBlockY();
                        int var69 = var8.getY();
                        if (var40 == var69) {
                            int var42 = var9.getBlockZ();
                            int var71 = var8.getZ();
                            if (var42 == var71) {
                                break label25;
                            }
                        }
                    }
                }

                this.restorePillar(1836782931);
            }

            this.build(1607619143);
        }
    }

    private void restorePillar(int var1) {
        ControlRoomManager var5 = this.plugin.controlRoom(1275615420);
        if (var5 != null) {
            Location var6 = var5.shrineBlock(1056167168);
            if (var6 != null) {
                Block var7 = var6.getBlock();
                Material var18 = var7.getType();
                Material var3 = Material.QUARTZ_PILLAR;
                if (var18 == var3) {
                    try {
                        BlockData var24 = Bukkit.createBlockData(var5.shrineData(366566408));
                        var7.setBlockData(var24, false);
                    } catch (IllegalArgumentException var25) {
                    }
                }

                var5.clearShrineBlock(2036730787);
            }
        }
    }

    public void shutdown(int var1) {
        this.stopSpin(22866284);
        despawnAll(2140090338);
        this.parts.clear();
        this.stand = null;
    }

    public boolean standing(int var1) {
        if (this.anchor != null && !this.parts.isEmpty()) {
            for (Entity var4 : this.parts) {
                if (var4 == null || !var4.isValid()) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    private void stopSpin(int var1) {
        if (this.spinTask != null) {
            this.spinTask.cancel();
            this.spinTask = null;
        }

        this.spinners.clear();
        this.stateLabels.clear();
        this.bobAngle = 0.0;
        this.tumbleAngle = 0.0;
        this.spinAngle = 0.0;
    }

    public static void despawnAll(int var0) {
        Iterator var3 = Bukkit.getWorlds().iterator();

        while (var3.hasNext()) {
            for (Entity var6 : ((World)var3.next()).getEntities()) {
                if (var6.getScoreboardTags().contains("BlissArmorLimit")) {
                    var6.remove();
                }
            }
        }
    }

    private void build(int var1) {
        if (this.anchor.getWorld() != null) {
            Block var13 = this.anchor.getBlock();
            Material var28 = var13.getType();
            Material var3 = Material.QUARTZ_PILLAR;
            if (var28 != var3) {
                ControlRoomManager var14 = this.plugin.controlRoom(1275615420);
                if (var14 != null) {
                    Location var78 = var13.getLocation();
                    String var101 = var13.getBlockData().getAsString();
                    var14.setShrineBlock(var78, var101, 1716700832);
                }

                Material var79 = Material.QUARTZ_PILLAR;
                var13.setType(var79, false);
            }

            Location var187 = this.anchor.clone().add(0.0, 1.4, 0.0);
            ItemStack var106 = this.plugin.coreItems(401009135).prismaticEdge(840003792);
            ItemDisplay var15 = this.item(var187, var106, 1.26F, 820771713);
            Billboard var83 = Billboard.FIXED;
            var15.setBillboard(var83);
            var15.setGlowing(true);
            Color var85 = EDGE_GLOW;
            var15.setGlowColorOverride(var85);
            List var45 = this.spinners;
            Location var167 = var187.clone();
            ArmorLimitHologram.Spin var86 = new ArmorLimitHologram.Spin(var15, var167, true, 23700473);
            var45.add(var86);
            List var48 = this.parts;
            var48.add(var15);
            List var51 = this.parts;
            Location var109 = var187.clone().add(0.0, 0.55, 0.0);
            String var136 = "Prismatic Edge";
            TextColor var168 = NAME_GOLD;
            TextDisplay var89 = this.label(var109, var136, var168, 1552666624);
            var51.add(var89);
            Vector var16 = this.anchor.getDirection().setY(0);
            if (var16.lengthSquared() < 1.0E-6) {
                Vector var61 = new Vector(0, 0, 1);
                var16 = var61;
            }

            var16.normalize();
            ArmorLimitHologram.Dial var91 = ArmorLimitHologram.Dial.PEARL_LIMIT;
            Location var114 = this.ringPos(1.0, var16, 1083117147);
            this.buildDial(var91, var114, 2064629895);
            ArmorLimitHologram.Switch var92 = ArmorLimitHologram.Switch.WEB;
            Location var116 = this.ringPos(2.0, var16, 1083117147);
            Vector var144 = ringRight(2.0, var16, 1919589985);
            this.buildSwitch(var92, var116, var144, 1443629822);
            ArmorLimitHologram.Switch var93 = ArmorLimitHologram.Switch.ELYTRA;
            Location var118 = this.ringPos(3.3, var16, 1083117147);
            Vector var149 = ringRight(3.3, var16, 1919589985);
            this.buildSwitch(var93, var118, var149, 1443629822);
            Location var95 = this.ringPos(4.0, var16, 1083117147);
            Vector var123 = ringRight(4.0, var16, 1919589985);
            this.buildArmorStand(var95, var123, 1975033452);
            ArmorLimitHologram.Switch var96 = ArmorLimitHologram.Switch.GOD_APPLE;
            Location var125 = this.ringPos(4.7, var16, 1083117147);
            Vector var154 = ringRight(4.7, var16, 1919589985);
            this.buildSwitch(var96, var125, var154, 1443629822);
            ArmorLimitHologram.Switch var97 = ArmorLimitHologram.Switch.VILLAGER;
            Location var127 = this.ringPos(6.0, var16, 1083117147);
            Vector var159 = ringRight(6.0, var16, 1919589985);
            this.buildSwitch(var97, var127, var159, 1443629822);
            ArmorLimitHologram.Switch var98 = ArmorLimitHologram.Switch.SHIELD;
            Location var129 = this.ringPos(7.0, var16, 1083117147);
            Vector var164 = ringRight(7.0, var16, 1919589985);
            this.buildSwitch(var98, var129, var164, 1443629822);
            ArmorLimitHologram.Dial var99 = ArmorLimitHologram.Dial.PEARL_COOLDOWN;
            Location var131 = this.ringPos(8.0, var16, 1083117147);
            this.buildDial(var99, var131, 2064629895);
            this.startSpin(915563342);
        }
    }

    private static Vector ringOut(double var0, Vector var2, int var3) {
        double var16 = Math.toRadians(var0 * 40.0);
        double var18 = Math.cos(var16);
        double var20 = Math.sin(var16);
        double var36 = var2.getX();
        double var38 = var36 * var18;
        double var43 = var2.getZ();
        double var45 = var43 * var20;
        double var40 = var38 - var45;
        double var50 = var2.getX();
        double var52 = var50 * var20;
        double var57 = var2.getZ();
        double var59 = var57 * var18;
        double var54 = var52 + var59;
        return new Vector(var40, 0.0, var54);
    }

    private Location ringPos(double var1, Vector var3, int var4) {
        Location var12 = this.anchor.clone();
        Vector var15 = ringOut(var1, var3, 1441892072).multiply(8.0);
        return var12.add(var15);
    }

    private static Vector ringRight(double var0, Vector var2, int var3) {
        Vector var10 = ringOut(var0, var2, 1441892072);
        double var16 = -var10.getZ();
        double var18 = var10.getX();
        return new Vector(var16, 0.0, var18);
    }

    private float faceCentreYaw(Location var1, int var2) {
        double var10 = this.anchor.getX();
        double var29 = var1.getX();
        double var5 = var10 - var29;
        double var16 = this.anchor.getZ();
        double var32 = var1.getZ();
        double var7 = var16 - var32;
        double var22 = -var5;
        return (float)Math.toDegrees(Math.atan2(var22, var7));
    }

    private void buildArmorStand(Location var1, Vector var2, int var3) {
        World var14 = var1.getWorld();
        float var15 = this.faceCentreYaw(var1, 516571848);
        Consumer<ArmorStand> var100 = var1x -> {
            var1x.setGravity(false);
            var1x.setInvulnerable(true);
            var1x.setCanPickupItems(false);
            var1x.setPersistent(false);
            var1x.setSilent(true);
            var1x.setBasePlate(true);
            var1x.setArms(false);
            var1x.setRotation(var15, 0.0F);
            var1x.addScoreboardTag("BlissArmorLimit");
            var1x.addScoreboardTag("BlissArmorLimitStand");
        };
        ArmorStand var56 = (ArmorStand)var14.spawn(var1, ArmorStand.class, var100);
        this.stand = var56;
        List var25 = this.parts;
        ArmorStand var58 = this.stand;
        var25.add(var58);
        this.dressStand(341790506);
        List var29 = this.parts;
        Location var81 = var1.clone().add(0.0, 2.35, 0.0);
        String var92 = "Armor Limit";
        TextColor var101 = NAME_GOLD;
        TextDisplay var60 = this.label(var81, var92, var101, 1552666624);
        var29.add(var60);
        int var16 = 0;

        while (true) {
            int var62 = PIECES.length;
            if (var16 >= var62) {
                return;
            }

            Location var34 = var1.clone();
            double[] var93 = PIECE_Y;
            double var94 = var93[var16];
            Location var17 = var34.add(0.0, var94, 0.0);
            Location var37 = var17.clone();
            Vector var67 = var2.clone().multiply(0.95);
            Location var18 = var37.add(var67);
            Location var40 = var17.clone();
            Vector var70 = var2.clone().multiply(0.95);
            Location var19 = var40.subtract(var70);
            List var43 = this.parts;
            String var96 = "▶";
            TextColor var103 = NOTE_GREY;
            TextDisplay var72 = this.label(var18, var96, var103, 1552666624);
            var43.add(var72);
            List var46 = this.parts;
            String var104 = "BlissArmorLimitUp";
            ArmorLimits.Piece[] var109 = PIECES;
            ArmorLimits.Piece var110 = var109[var16];
            Interaction var74 = this.hitbox(var18, 0.45F, var104, var110, 1267715787);
            var46.add(var74);
            List var49 = this.parts;
            String var98 = "◀";
            TextColor var105 = NOTE_GREY;
            TextDisplay var76 = this.label(var19, var98, var105, 1552666624);
            var49.add(var76);
            List var52 = this.parts;
            String var106 = "BlissArmorLimitDown";
            ArmorLimits.Piece[] var111 = PIECES;
            ArmorLimits.Piece var112 = var111[var16];
            Interaction var78 = this.hitbox(var19, 0.45F, var106, var112, 1267715787);
            var52.add(var78);
            var16++;
        }
    }

    private void buildSwitch(ArmorLimitHologram.Switch var1, Location var2, Vector var3, int var4) {
        Location var20 = var2.clone();
        double var103 = var1.bodyY;
        Location var15 = var20.add(0.0, var103, 0.0);
        if (var1.item != null) {
            List var60 = this.parts;
            Material var102 = var1.item;
            ArmorLimitHologram.Switch var134 = ArmorLimitHologram.Switch.SHIELD;
            boolean var125;
            if (var1 == var134) {
                var125 = true;
            } else {
                var125 = false;
            }

            ItemDisplay var86 = this.floater(var102, var15, var125, 282745387);
            var60.add(var86);
        } else {
            List var25 = this.parts;
            Villager var63 = this.villager(var2, 1797879707);
            var25.add(var63);
        }

        List var28 = this.parts;
        Location var89 = var15.clone().add(0.0, 0.7, 0.0);
        String var108 = var1.title;
        TextColor var10 = NAME_GOLD;
        TextDisplay var65 = this.label(var89, var108, var10, 1552666624);
        var28.add(var65);
        Location var16 = var15.clone().add(0.0, 1.25, 0.0);
        Location var34 = var16.clone();
        Vector var70 = var3.clone().multiply(0.85);
        Location var17 = var34.subtract(var70);
        Location var37 = var16.clone();
        Vector var73 = var3.clone().multiply(0.85);
        Location var18 = var37.add(var73);
        List var40 = this.parts;
        String var111 = "✖";
        TextColor var119 = RED;
        TextDisplay var75 = this.bigLabel(var17, var111, var119, 1.6F, 589796446);
        var40.add(var75);
        List var43 = this.parts;
        String var120 = "BlissArmorLimitOff";
        Interaction var77 = this.hitbox(var17, 0.6F, var120, var1, 1726852388);
        var43.add(var77);
        List var46 = this.parts;
        String var113 = "✔";
        TextColor var121 = GREEN;
        TextDisplay var79 = this.bigLabel(var18, var113, var121, 1.6F, 589796446);
        var46.add(var79);
        List var49 = this.parts;
        String var122 = "BlissArmorLimitOn";
        Interaction var81 = this.hitbox(var18, 0.6F, var122, var1, 1726852388);
        var49.add(var81);
        String var99 = this.stateText(var1, 291646128);
        TextColor var117 = this.stateColour(var1, 1029874911);
        TextDisplay var19 = this.label(var16, var99, var117, 1552666624);
        List var54 = this.parts;
        var54.add(var19);
        EnumMap var57 = this.stateLabels;
        var57.put(var1, var19);
    }

    private void buildDial(ArmorLimitHologram.Dial var1, Location var2, int var3) {
        Location var16 = var2.clone().add(0.0, 1.2, 0.0);
        List var24 = this.parts;
        Material var7 = Material.ENDER_PEARL;
        ItemDisplay var59 = this.floater(var7, var16, false, 282745387);
        var24.add(var59);
        List var27 = this.parts;
        Location var81 = var16.clone().add(0.0, 0.7, 0.0);
        String var93 = var1.title;
        TextColor var106 = NAME_GOLD;
        TextDisplay var61 = this.label(var81, var93, var106, 1552666624);
        var27.add(var61);
        Location var17 = var16.clone().add(0.0, 1.15, 0.0);
        Location var18 = var16.clone().add(0.0, 1.6, 0.0);
        Location var19 = var16.clone().add(0.0, 2.05, 0.0);
        List var39 = this.parts;
        String var100 = "▼";
        TextColor var107 = NOTE_GREY;
        TextDisplay var69 = this.bigLabel(var17, var100, var107, 1.35F, 589796446);
        var39.add(var69);
        List var42 = this.parts;
        String var108 = "BlissArmorLimitModDown";
        NamespacedKey var120 = KEY_DIAL;
        String var123 = var1.name();
        Interaction var71 = this.hitbox(var17, 0.4F, var108, var120, var123, 819828760);
        var42.add(var71);
        List var45 = this.parts;
        String var102 = "▲";
        TextColor var109 = NOTE_GREY;
        TextDisplay var73 = this.bigLabel(var19, var102, var109, 1.35F, 589796446);
        var45.add(var73);
        List var48 = this.parts;
        String var110 = "BlissArmorLimitModUp";
        NamespacedKey var122 = KEY_DIAL;
        String var125 = var1.name();
        Interaction var75 = this.hitbox(var19, 0.4F, var110, var122, var125, 819828760);
        var48.add(var75);
        String var87 = this.dialText(var1, 1910868624);
        TextColor var105 = NAME_GOLD;
        TextDisplay var20 = this.label(var18, var87, var105, 1552666624);
        List var53 = this.parts;
        var53.add(var20);
        EnumMap var56 = this.dialLabels;
        var56.put(var1, var20);
    }

    private String dialText(ArmorLimitHologram.Dial var1, int var2) {
        KitRules var9 = this.plugin.kitRules(1205957746);
        ArmorLimitHologram.Dial var4 = ArmorLimitHologram.Dial.PEARL_COOLDOWN;
        if (var1 == var4) {
            double var21;
            if (var9 == null) {
                var21 = 1.0;
            } else {
                var21 = var9.pearlCooldownMult(1963696175);
            }

            Locale var23 = Locale.ROOT;
            String var27 = "Modifier: x%.2f";
            Object[] var28 = new Object[1];
            Double var29 = var21;
            var28[0] = var29;
            return String.format(var23, var27, var28);
        } else {
            int var18;
            if (var9 == null) {
                var18 = -1;
            } else {
                var18 = var9.pearlLimit(782575185);
            }

            String var25;
            if (var18 == -1) {
                var25 = "Limit: Unlimited";
            } else {
                var25 = "Limit: " + var18;
            }

            return var25;
        }
    }

    private void stepDial(ArmorLimitHologram.Dial var1, int var2, int var3) {
        KitRules var9 = this.plugin.kitRules(1205957746);
        if (var9 != null) {
            ArmorLimitHologram.Dial var5 = ArmorLimitHologram.Dial.PEARL_COOLDOWN;
            if (var1 == var5) {
                double var26 = var9.pearlCooldownMult(1963696175);
                double var34 = (double)var2 * 0.25;
                double var28 = var26 + var34;
                var9.setPearlCooldownMult(var28, 1311392125);
            } else {
                var9.stepPearlLimit(var2, 2136389686);
            }

            GlobalState var10 = this.plugin.globalState(724248306);
            if (var10 != null && !var10.enderpearls(155177658)) {
                var10.setEnderpearls(true, 567146737);
                var10.save(343464104);
            }
        }
    }

    private void refreshDial(ArmorLimitHologram.Dial var1, int var2) {
        EnumMap var8 = this.dialLabels;
        TextDisplay var7 = (TextDisplay)var8.get(var1);
        if (var7 != null && var7.isValid()) {
            TextComponent var17 = Component.text(this.dialText(var1, 1910868624));
            TextColor var23 = NAME_GOLD;
            TextComponent var19 = (TextComponent)var17.color(var23);
            TextDecoration var24 = TextDecoration.BOLD;
            TextComponent var21 = (TextComponent)var19.decoration(var24, true);
            TextDecoration var25 = TextDecoration.ITALIC;
            Component var22 = var21.decoration(var25, false);
            var7.text(var22);
        }
    }

    private static ArmorLimitHologram.Dial dialOf(Interaction var0, int var1) {
        if (KEY_DIAL == null) {
            return null;
        } else {
            PersistentDataContainer var8 = var0.getPersistentDataContainer();
            NamespacedKey var3 = KEY_DIAL;
            PersistentDataType var4 = PersistentDataType.STRING;
            String var5 = (String)var8.get(var3, var4);
            if (var5 == null) {
                return null;
            } else {
                try {
                    return ArmorLimitHologram.Dial.valueOf(var5);
                } catch (IllegalArgumentException var14) {
                    return null;
                }
            }
        }
    }

    private boolean allowed(ArmorLimitHologram.Switch var1, int var2) {
        KitRules var6 = this.plugin.kitRules(1205957746);
        if (var6 == null) {
            return true;
        } else {
            return switch (var1) {
                case GOD_APPLE -> var6.godApplesAllowed(564954237);
                case ELYTRA -> var6.elytraAllowed(913902142);
                case VILLAGER -> var6.villagersAllowed(1843282219);
                case SHIELD -> var6.shieldAllowed(914900028);
                case WEB -> var6.cobwebsAllowed(1748007571);
                default -> {
                    MatchException var13 = new MatchException(null, null);
                    throw var13;
                }
            };
        }
    }

    private String stateText(ArmorLimitHologram.Switch var1, int var2) {
        String var6;
        if (this.allowed(var1, 556796153)) {
            var6 = "Enabled";
        } else {
            var6 = "Disabled";
        }

        return var6;
    }

    private TextColor stateColour(ArmorLimitHologram.Switch var1, int var2) {
        TextColor var6;
        if (this.allowed(var1, 556796153)) {
            var6 = GREEN;
        } else {
            var6 = RED;
        }

        return var6;
    }

    private ItemDisplay floater(Material var1, Location var2, boolean var3, int var4) {
        Location var11 = var2.clone();
        float var26 = this.faceCentreYaw(var2, 516571848);
        float var36;
        if (var3) {
            var36 = -90.0F;
        } else {
            var36 = 0.0F;
        }

        float var27 = var26 + var36;
        var11.setYaw(var27);
        var11.setPitch(0.0F);
        ItemDisplay var12 = this.item(var11, var1, 1.4F, 1077408063);
        Billboard var30 = Billboard.FIXED;
        var12.setBillboard(var30);
        if (var3) {
            ItemDisplayTransform var31 = ItemDisplayTransform.THIRDPERSON_RIGHTHAND;
            var12.setItemDisplayTransform(var31);
            var12.setTeleportDuration(1);
        }

        List var23 = this.spinners;
        Location var38 = var11.clone();
        boolean var39;
        if (!var3) {
            var39 = true;
        } else {
            var39 = false;
        }

        ArmorLimitHologram.Spin var33 = new ArmorLimitHologram.Spin(var12, var38, var39, 23700473);
        var23.add(var33);
        return var12;
    }

    private void startSpin(int var1) {
        if (!this.spinners.isEmpty() && this.spinTask == null) {
            BukkitRunnable var3 = new BukkitRunnable() {
                public void run() {
                    ArmorLimitHologram var17 = ArmorLimitHologram.this;
                    double var72 = var17.spinAngle + 0.05;
                    var17.spinAngle = var72;
                    if (ArmorLimitHologram.this.spinAngle > Math.PI * 2) {
                        ArmorLimitHologram var24 = ArmorLimitHologram.this;
                        double var76 = var24.spinAngle - (Math.PI * 2);
                        var24.spinAngle = var76;
                    }

                    ArmorLimitHologram var26 = ArmorLimitHologram.this;
                    double var80 = var26.tumbleAngle + 0.030901699435000003;
                    var26.tumbleAngle = var80;
                    if (ArmorLimitHologram.this.tumbleAngle > Math.PI * 2) {
                        ArmorLimitHologram var33 = ArmorLimitHologram.this;
                        double var84 = var33.tumbleAngle - (Math.PI * 2);
                        var33.tumbleAngle = var84;
                    }

                    ArmorLimitHologram var35 = ArmorLimitHologram.this;
                    double var88 = var35.bobAngle + 0.045000000000000005;
                    var35.bobAngle = var88;
                    if (ArmorLimitHologram.this.bobAngle > Math.PI * 2) {
                        ArmorLimitHologram var42 = ArmorLimitHologram.this;
                        double var92 = var42.bobAngle - (Math.PI * 2);
                        var42.bobAngle = var92;
                    }

                    for (ArmorLimitHologram.Spin var15 : ArmorLimitHologram.this.spinners) {
                        if (var15.d.isValid()) {
                            if (var15.tumble) {
                                Vector3f var138 = var15.d.getTransformation().getScale();
                                ItemDisplay var69 = var15.d;
                                Vector3f var124 = new Vector3f();
                                float var130 = (float)ArmorLimitHologram.this.spinAngle;
                                AxisAngle4f var125 = new AxisAngle4f(var130, 0.0F, 1.0F, 0.0F);
                                float var136 = (float)ArmorLimitHologram.this.tumbleAngle;
                                AxisAngle4f var131 = new AxisAngle4f(var136, 1.0F, 0.0F, 0.0F);
                                Transformation var101 = new Transformation(var124, var125, var138, var131);
                                var69.setTransformation(var101);
                            } else {
                                Location var16 = var15.home.clone();
                                double var96 = var15.home.getY();
                                double var122 = Math.sin(ArmorLimitHologram.this.bobAngle) * 0.13;
                                double var98 = var96 + var122;
                                var16.setY(var98);
                                ItemDisplay var62 = var15.d;
                                var62.teleport(var16);
                            }
                        }
                    }
                }
            };
            BlissPlugin var14 = this.plugin;
            BukkitTask var12 = var3.runTaskTimer(var14, 1L, 1L);
            this.spinTask = var12;
        }
    }

    private void dressStand(int var1) {
        if (this.stand != null && this.stand.isValid()) {
            EntityEquipment var11 = this.stand.getEquipment();
            ArmorLimits.Piece var4 = ArmorLimits.Piece.HELMET;
            ItemStack var21 = this.piece(var4, 1014211940);
            var11.setHelmet(var21);
            EntityEquipment var14 = this.stand.getEquipment();
            ArmorLimits.Piece var28 = ArmorLimits.Piece.CHESTPLATE;
            ItemStack var23 = this.piece(var28, 1014211940);
            var14.setChestplate(var23);
            EntityEquipment var17 = this.stand.getEquipment();
            ArmorLimits.Piece var29 = ArmorLimits.Piece.LEGGINGS;
            ItemStack var25 = this.piece(var29, 1014211940);
            var17.setLeggings(var25);
            EntityEquipment var20 = this.stand.getEquipment();
            ArmorLimits.Piece var30 = ArmorLimits.Piece.BOOTS;
            ItemStack var27 = this.piece(var30, 1014211940);
            var20.setBoots(var27);
        }
    }

    private ItemStack piece(ArmorLimits.Piece var1, int var2) {
        ArmorLimits var6 = this.limits;
        ArmorLimits.Tier var7 = var6.tier(var1, 1084990497);
        Material var8 = ArmorLimits.material(var7, var1, 1127097336);
        return new ItemStack(var8);
    }

    private ItemDisplay item(Location var1, Material var2, float var3, int var4) {
        ItemStack var7 = new ItemStack(var2);
        return this.item(var1, var7, var3, 820771713);
    }

    private ItemDisplay item(Location var1, ItemStack var2, float var3, int var4) {
        World var12 = var1.getWorld();
        Consumer<ItemDisplay> var15 = var2x -> {
            var2x.setItemStack(var2);
            ItemDisplayTransform var20 = ItemDisplayTransform.GROUND;
            var2x.setItemDisplayTransform(var20);
            Billboard var21 = Billboard.CENTER;
            var2x.setBillboard(var21);
            Brightness var22 = new Brightness(15, 15);
            var2x.setBrightness(var22);
            Vector3f var26 = new Vector3f();
            AxisAngle4f var27 = new AxisAngle4f(0.0F, 0.0F, 1.0F, 0.0F);
            Vector3f var28 = new Vector3f(var3, var3, var3);
            AxisAngle4f var29 = new AxisAngle4f(0.0F, 0.0F, 0.0F, 1.0F);
            Transformation var23 = new Transformation(var26, var27, var28, var29);
            var2x.setTransformation(var23);
            var2x.setPersistent(false);
            var2x.addScoreboardTag("BlissArmorLimit");
        };
        return (ItemDisplay)var12.spawn(var1, ItemDisplay.class, var15);
    }

    private Villager villager(Location var1, int var2) {
        World var7 = var1.getWorld();
        float var8 = this.faceCentreYaw(var1, 516571848);
        Consumer<Villager> var16 = var1x -> {
            var1x.setRotation(var8, 0.0F);
            var1x.setAI(false);
            var1x.setInvulnerable(true);
            var1x.setSilent(true);
            var1x.setCollidable(false);
            var1x.setPersistent(false);
            var1x.setRemoveWhenFarAway(false);
            var1x.addScoreboardTag("BlissArmorLimit");
        };
        return (Villager)var7.spawn(var1, Villager.class, var16);
    }

    private TextDisplay label(Location var1, String var2, TextColor var3, int var4) {
        World var12 = var1.getWorld();
        Consumer<TextDisplay> var15 = var2x -> {
            TextComponent var18 = Component.text(var2);
            TextComponent var20 = (TextComponent)var18.color(var3);
            TextDecoration var34 = TextDecoration.BOLD;
            TextComponent var22 = (TextComponent)var20.decoration(var34, true);
            TextDecoration var35 = TextDecoration.ITALIC;
            Component var23 = var22.decoration(var35, false);
            var2x.text(var23);
            Billboard var24 = Billboard.CENTER;
            var2x.setBillboard(var24);
            TextAlignment var25 = TextAlignment.CENTER;
            var2x.setAlignment(var25);
            Color var27 = Color.fromARGB(0, 0, 0, 0);
            var2x.setBackgroundColor(var27);
            var2x.setShadowed(true);
            var2x.setSeeThrough(false);
            var2x.setDefaultBackground(false);
            Brightness var31 = new Brightness(15, 15);
            var2x.setBrightness(var31);
            var2x.setPersistent(false);
            var2x.addScoreboardTag("BlissArmorLimit");
        };
        return (TextDisplay)var12.spawn(var1, TextDisplay.class, var15);
    }

    private TextDisplay bigLabel(Location var1, String var2, TextColor var3, float var4, int var5) {
        TextDisplay var16 = this.label(var1, var2, var3, 1552666624);
        Vector3f var21 = new Vector3f();
        AxisAngle4f var22 = new AxisAngle4f(0.0F, 0.0F, 1.0F, 0.0F);
        Vector3f var23 = new Vector3f(var4, var4, var4);
        AxisAngle4f var24 = new AxisAngle4f(0.0F, 0.0F, 0.0F, 1.0F);
        Transformation var20 = new Transformation(var21, var22, var23, var24);
        var16.setTransformation(var20);
        return var16;
    }

    private Interaction hitbox(Location var1, float var2, String var3, ArmorLimits.Piece var4, int var5) {
        NamespacedKey var10 = KEY_PIECE;
        String var13 = var4.name();
        return this.hitbox(var1, var2, var3, var10, var13, 819828760);
    }

    private Interaction hitbox(Location var1, float var2, String var3, ArmorLimitHologram.Switch var4, int var5) {
        NamespacedKey var10 = KEY_SWITCH;
        String var13 = var4.name();
        return this.hitbox(var1, var2, var3, var10, var13, 819828760);
    }

    private Interaction hitbox(Location var1, float var2, String var3, NamespacedKey var4, String var5, int var6) {
        World var18 = var1.getWorld();
        Location var21 = var1.clone();
        double var27 = (double)var2 / 2.0;
        Location var22 = var21.subtract(0.0, var27, 0.0);
        Consumer<Interaction> var24 = var4x -> {
            var4x.setInteractionWidth(var2);
            var4x.setInteractionHeight(var2);
            var4x.setResponsive(true);
            var4x.setPersistent(false);
            var4x.addScoreboardTag("BlissArmorLimit");
            var4x.addScoreboardTag(var3);
            if (var4 != null) {
                PersistentDataContainer var18x = var4x.getPersistentDataContainer();
                PersistentDataType var7 = PersistentDataType.STRING;
                var18x.set(var4, var7, var5);
            }
        };
        return (Interaction)var18.spawn(var22, Interaction.class, var24);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent var1) {
        EquipmentSlot var17 = var1.getHand();
        EquipmentSlot var3 = EquipmentSlot.HAND;
        if (var17 == var3) {
            Entity var7 = var1.getRightClicked();
            if (var7.getScoreboardTags().contains("BlissArmorLimit")) {
                var1.setCancelled(true);
                BlissPlugin var25 = this.plugin;
                Player var90 = var1.getPlayer();
                if (WorldBorderHologram.decreeAllowed(var25, var90, 979011771)) {
                    if (var7 instanceof Interaction var8) {
                        World var9 = var8.getWorld();
                        boolean var10 = var8.getScoreboardTags().contains("BlissArmorLimitModUp");
                        boolean var11 = var8.getScoreboardTags().contains("BlissArmorLimitModDown");
                        if (!var10 && !var11) {
                            boolean var12 = var8.getScoreboardTags().contains("BlissArmorLimitOn");
                            boolean var13 = var8.getScoreboardTags().contains("BlissArmorLimitOff");
                            if (!var12 && !var13) {
                                ArmorLimits.Piece var14 = pieceOf(var8, 1393893283);
                                if (var14 != null) {
                                    byte var64;
                                    if (var8.getScoreboardTags().contains("BlissArmorLimitUp")) {
                                        var64 = 1;
                                    } else if (var8.getScoreboardTags().contains("BlissArmorLimitDown")) {
                                        var64 = -1;
                                    } else {
                                        var64 = 0;
                                    }

                                    if (var64 != 0) {
                                        ArmorLimits var57 = this.limits;
                                        ArmorLimits.Tier var16 = var57.step(var14, var64, 2099830012);
                                        this.dressStand(341790506);
                                        Location var98 = var8.getLocation();
                                        Sound var113 = equipSound(var16, 841327657);
                                        var9.playSound(var98, var113, 1.0F, 1.0F);
                                    }
                                }
                            } else {
                                ArmorLimitHologram.Switch var125 = switchOf(var8, 982158009);
                                if (var125 != null) {
                                    KitRules var126 = this.plugin.kitRules(1205957746);
                                    if (var126 != null) {
                                        switch (var125) {
                                            case GOD_APPLE:
                                                var126.setGodApplesAllowed(var12, 755883566);
                                                break;
                                            case ELYTRA:
                                                var126.setElytraAllowed(var12, 1081948569);
                                                break;
                                            case VILLAGER:
                                                var126.setVillagersAllowed(var12, 947550140);
                                                break;
                                            case SHIELD:
                                                var126.setShieldAllowed(var12, 614996347);
                                                break;
                                            case WEB:
                                                var126.setCobwebsAllowed(var12, 450887693);
                                        }
                                    }

                                    this.refreshState(var125, 601083756);
                                    Location var103 = var8.getLocation();
                                    Sound var114 = Sound.BLOCK_NOTE_BLOCK_PLING;
                                    float var122;
                                    if (var12) {
                                        var122 = 1.5F;
                                    } else {
                                        var122 = 0.7F;
                                    }

                                    var9.playSound(var103, var114, 1.2F, var122);
                                }
                            }
                        } else {
                            ArmorLimitHologram.Dial var124 = dialOf(var8, 1350222503);
                            if (var124 != null) {
                                byte var116;
                                if (var10) {
                                    var116 = 1;
                                } else {
                                    var116 = -1;
                                }

                                this.stepDial(var124, var116, 1615924684);
                                this.refreshDial(var124, 1722065374);
                                Location var111 = var8.getLocation();
                                Sound var117 = Sound.BLOCK_NOTE_BLOCK_HAT;
                                float var123;
                                if (var10) {
                                    var123 = 1.6F;
                                } else {
                                    var123 = 0.8F;
                                }

                                var9.playSound(var111, var117, 1.2F, var123);
                            }
                        }
                    }
                }
            }
        }
    }

    private static Sound equipSound(ArmorLimits.Tier var0, int var1) {
        return switch (var0) {
            case LEATHER-> Sound.ITEM_ARMOR_EQUIP_LEATHER;
            case GOLDEN-> Sound.ITEM_ARMOR_EQUIP_GOLD;
            case CHAINMAIL-> Sound.ITEM_ARMOR_EQUIP_CHAIN;
            case IRON-> Sound.ITEM_ARMOR_EQUIP_IRON;
            case DIAMOND-> Sound.ITEM_ARMOR_EQUIP_DIAMOND;
            case NETHERITE-> Sound.ITEM_ARMOR_EQUIP_NETHERITE;
            default -> {
                MatchException var7 = new MatchException(null, null);
                throw var7;
            }
        };
    }

    private void refreshState(ArmorLimitHologram.Switch var1, int var2) {
        EnumMap var8 = this.stateLabels;
        TextDisplay var7 = (TextDisplay)var8.get(var1);
        if (var7 != null && var7.isValid()) {
            TextComponent var17 = Component.text(this.stateText(var1, 291646128));
            TextColor var24 = this.stateColour(var1, 1029874911);
            TextComponent var19 = (TextComponent)var17.color(var24);
            TextDecoration var25 = TextDecoration.BOLD;
            TextComponent var21 = (TextComponent)var19.decoration(var25, true);
            TextDecoration var26 = TextDecoration.ITALIC;
            Component var22 = var21.decoration(var26, false);
            var7.text(var22);
        }
    }

    private static ArmorLimitHologram.Switch switchOf(Interaction var0, int var1) {
        if (KEY_SWITCH == null) {
            return null;
        } else {
            PersistentDataContainer var9 = var0.getPersistentDataContainer();
            NamespacedKey var3 = KEY_SWITCH;
            PersistentDataType var4 = PersistentDataType.STRING;
            String var5 = (String)var9.get(var3, var4);
            if (var5 == null) {
                return null;
            } else {
                try {
                    return ArmorLimitHologram.Switch.valueOf(var5);
                } catch (IllegalArgumentException var16) {
                    return null;
                }
            }
        }
    }

    @EventHandler
    public void onManipulate(PlayerArmorStandManipulateEvent var1) {
        if (var1.getRightClicked().getScoreboardTags().contains("BlissArmorLimit")) {
            var1.setCancelled(true);
        }
    }

    private static ArmorLimits.Piece pieceOf(Interaction var0, int var1) {
        if (KEY_PIECE == null) {
            return null;
        } else {
            PersistentDataContainer var8 = var0.getPersistentDataContainer();
            NamespacedKey var3 = KEY_PIECE;
            PersistentDataType var4 = PersistentDataType.STRING;
            String var5 = (String)var8.get(var3, var4);
            if (var5 == null) {
                return null;
            } else {
                try {
                    return ArmorLimits.Piece.valueOf(var5);
                } catch (IllegalArgumentException var14) {
                    return null;
                }
            }
        }
    }

    static {
        double[] var13 = new double[]{1.8, 1.3, 0.85, 0.35};
        PIECE_Y = var13;
        ArmorLimits.Piece[] var15 = new ArmorLimits.Piece[4];
        ArmorLimits.Piece var38 = ArmorLimits.Piece.HELMET;
        var15[0] = var38;
        ArmorLimits.Piece var39 = ArmorLimits.Piece.CHESTPLATE;
        var15[1] = var39;
        ArmorLimits.Piece var40 = ArmorLimits.Piece.LEGGINGS;
        var15[2] = var40;
        ArmorLimits.Piece var41 = ArmorLimits.Piece.BOOTS;
        var15[3] = var41;
        PIECES = var15;
    }

    private static enum Dial {
        PEARL_COOLDOWN("Pearl Cooldown Modifier"),
        PEARL_LIMIT("Pearl Limit");

        final String title;

        private Dial(String nullxx) {
            this.title = nullxx;
        }
    }

    private static final class Spin {
        final ItemDisplay d;
        final Location home;
        final boolean tumble;

        Spin(ItemDisplay var1, Location var2, boolean var3, int var4) {
            this.d = var1;
            this.home = var2;
            this.tumble = var3;
        }
    }

    private static enum Switch {
        GOD_APPLE("God Apple Toggle", Material.ENCHANTED_GOLDEN_APPLE, 1.2),
        ELYTRA("Elytra Toggle", Material.ELYTRA, 1.2),
        VILLAGER("Villager Toggle", null, 2.05),
        SHIELD("Shield Toggle", Material.SHIELD, 1.2),
        WEB("Web Toggle", Material.COBWEB, 1.2);

        final String title;
        final Material item;
        final double bodyY;

        private Switch(String nullxx, Material nullxxx, double nullxxxx) {
            this.title = nullxx;
            this.item = nullxxx;
            this.bodyY = nullxxxx;
        }
    }
}
