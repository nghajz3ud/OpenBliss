package com.bliss.honored;

import com.bliss.BlissPlugin;
import com.bliss.villager.Text;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.bukkit.entity.TextDisplay.TextAlignment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

public final class PotionLimitHologram implements Listener {
    private static final TextColor NAME_GOLD = TextColor.color(16770148);
    private static final String TAG = "BlissPotionLimit";
    private static final String TAG_ROOT = "BlissPotionLimitRoot";
    private static final String TAG_PICK = "BlissPotionLimitPick";
    private static final String TAG_UP = "BlissPotionLimitUp";
    private static final String TAG_DOWN = "BlissPotionLimitDown";
    private static final String TAG_SAVE = "BlissPotionLimitSave";
    private static final NamespacedKey KEY_EFFECT = NamespacedKey.fromString("bliss:potion_limit_key");
    private static final TextColor ARROW_GREY = TextColor.color(11184810);
    private static final Color ROOT_COLOUR = Color.fromRGB(13133055);
    private static final int DEFAULT_LEVEL = 2;
    private static final int MAX_LEVEL = 5;
    private static final double RING_R = 6.0;
    private static final int RING_COUNT = 12;
    private static final double RING_BASE_Y = 0.5;
    private static final double RING_GAP_Y = 1.5;
    private static final float BOOK_SCALE = 1.26F;
    private static final float ROOT_SCALE = 1.54F;
    private static final float EDIT_SCALE = 1.68F;
    private static final float BOX_ROOT = 1.96F;
    private static final float BOX_PICK = 1.68F;
    private static final float BOX_ARROW = 1.0F;
    private static final float ARROW_SCALE = 1.35F;
    private static final double ROOT_LIFT = 1.2;
    private static final double EDIT_LIFT = 2.4;
    private static final double NAME_OVER = 0.77;
    private static final double ARROW_OUT = 1.54;
    private static final int SPREAD_PER_TICK = 3;
    private static final int SPREAD_FLY = 3;
    private final BlissPlugin plugin;
    private final PotionLimits limits;
    private Location anchor;
    private final List<Entity> rootParts;
    private final List<Entity> ringParts;
    private final List<Entity> editParts;
    private boolean expanded;
    private PotionEffectType editing;
    private int editLevel;
    private TextDisplay editLabel;
    private int spreadGen;
    private BukkitTask spreadTask;
    private final List<PotionLimitHologram.Slot> ringSlots;
    private final List<PotionLimitHologram.Spin> spinners;
    private BukkitTask spinTask;
    private double spinAngle;
    private double tumbleAngle;
    private double bobAngle;
    private static final double SPIN_STEP = 0.05;
    private static final double TUMBLE_RATIO = 0.6180339887;
    private static final double BOB_AMP = 0.13;
    private static final double BOB_RATE = 0.9;
    private static final Set<String> HIDDEN;

    public PotionLimitHologram(BlissPlugin var1, PotionLimits var2, int var3) {
        ArrayList var5 = new ArrayList();
        this.rootParts = var5;
        ArrayList var13 = new ArrayList();
        this.ringParts = var13;
        ArrayList var14 = new ArrayList();
        this.editParts = var14;
        ArrayList var15 = new ArrayList();
        this.ringSlots = var15;
        ArrayList var16 = new ArrayList();
        this.spinners = var16;
        this.plugin = var1;
        this.limits = var2;
    }

    public void reconcile(int var1) {
        this.stopSpin(646106010);
        int var34 = this.spreadGen + 1;
        this.spreadGen = var34;
        if (this.spreadTask != null) {
            this.spreadTask.cancel();
            this.spreadTask = null;
        }

        despawnAll(1095614141);
        this.rootParts.clear();
        this.ringParts.clear();
        this.ringSlots.clear();
        this.editParts.clear();
        this.expanded = false;
        this.editing = null;
        this.editLabel = null;
        ControlRoomManager var4 = this.plugin.controlRoom(1275615420);
        Location var32;
        if (var4 == null) {
            var32 = null;
        } else {
            var32 = var4.potionSpot(491047341);
        }

        if (var32 == null) {
            this.anchor = null;
        } else {
            Location var41 = var32.clone();
            this.anchor = var41;
            this.spawnRoot(2048643070);
        }
    }

    public void shutdown(int var1) {
        this.stopSpin(646106010);
        int var22 = this.spreadGen + 1;
        this.spreadGen = var22;
        if (this.spreadTask != null) {
            this.spreadTask.cancel();
            this.spreadTask = null;
        }

        despawnAll(1095614141);
        this.rootParts.clear();
        this.ringParts.clear();
        this.ringSlots.clear();
        this.editParts.clear();
        this.expanded = false;
        this.editing = null;
        this.editLabel = null;
    }

    public boolean standing(int var1) {
        if (this.anchor != null && !this.rootParts.isEmpty()) {
            for (Entity var4 : this.rootParts) {
                if (var4 == null || !var4.isValid()) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public static void despawnAll(int var0) {
        Iterator var3 = Bukkit.getWorlds().iterator();

        while (var3.hasNext()) {
            for (Entity var6 : ((World)var3.next()).getEntities()) {
                if (var6.getScoreboardTags().contains("BlissPotionLimit")) {
                    var6.remove();
                }
            }
        }
    }

    private void spawnRoot(int var1) {
        if (this.anchor.getWorld() != null) {
            Location var15 = this.anchor.clone().add(0.0, 1.2, 0.0);
            List var24 = this.rootParts;
            ItemDisplay var39 = this.bottle(var15, 1.54F, null, false, 621900673);
            ItemDisplay var34 = this.spinning(var39, var15, 263441187);
            var24.add(var34);
            List var27 = this.rootParts;
            Location var42 = var15.clone().add(0.0, 0.77, 0.0);
            String var48 = "Potion Limit";
            TextColor var50 = NAME_GOLD;
            TextDisplay var36 = this.label(var42, var48, var50, true, 1803394520);
            var27.add(var36);
            List var30 = this.rootParts;
            String var51 = "BlissPotionLimitRoot";
            Interaction var38 = this.hitbox(var15, 1.96F, var51, null, 624443881);
            var30.add(var38);
            this.startSpin(916467305);
        }
    }

    private void toggleSpread(int var1) {
        if (this.expanded) {
            this.collapseRings(48723493);
        } else {
            this.expandRings(775596937);
        }
    }

    private void expandRings(int var1) {
        final List var9 = allEffects(1628432219);
        if (!var9.isEmpty()) {
            List var24 = this.ringParts;
            this.removeAll(var24, 2121951745);
            this.ringSlots.clear();
            this.expanded = true;
            final int var27 = this.spreadGen + 1;
            this.spreadGen = var27;
            final Location var11 = this.anchor.clone().add(0.0, 1.2, 0.0);
            BukkitRunnable var30 = new BukkitRunnable() {
                int i = 0;

                public void run() {
                    int var7 = var27;
                    int var20 = PotionLimitHologram.this.spreadGen;
                    if (var7 != var20) {
                        this.cancel();
                    } else {
                        int var6 = 0;

                        while (var6 < 3) {
                            int var12 = this.i;
                            int var24 = var9.size();
                            if (var12 >= var24) {
                                break;
                            }

                            PotionLimitHologram var14 = PotionLimitHologram.this;
                            List var26 = var9;
                            int var34 = this.i;
                            PotionEffectType var28 = (PotionEffectType)var26.get(var34);
                            PotionLimitHologram var36 = PotionLimitHologram.this;
                            int var39 = this.i;
                            Location var37 = var36.slotPos(var39, 1218732670);
                            Location var41 = var11;
                            int var42 = var27;
                            var14.emit(var28, var37, var41, var42, 78711078);
                            var6++;
                            int var30 = this.i + 1;
                            this.i = var30;
                        }

                        int var17 = this.i;
                        int var33 = var9.size();
                        if (var17 >= var33) {
                            this.cancel();
                        }
                    }
                }
            };
            BlissPlugin var33 = this.plugin;
            BukkitTask var31 = var30.runTaskTimer(var33, 0L, 1L);
            this.spreadTask = var31;
        }
    }

    private Location slotPos(int var1, int var2) {
        int var12 = var1 / 12;
        double var38 = (double)(var1 % 12);
        double var23 = (Math.PI * 2) * var38 / 12.0;
        double var46 = (double)(var12 % 2) * (Math.PI / 12);
        double var14 = var23 + var46;
        Location var29 = this.anchor.clone();
        double var36 = Math.cos(var14) * 6.0;
        double var56 = (double)var12 * 1.5;
        double var52 = 0.5 + var56;
        double var62 = Math.sin(var14) * 6.0;
        return var29.add(var36, var52, var62);
    }

    private void emit(PotionEffectType var1, Location var2, Location var3, int var4, int var5) {
        PotionLimitHologram.Slot var6 = new PotionLimitHologram.Slot();
        Color var46 = var1.getColor();
        ItemDisplay var27 = this.bottle(var3, 1.26F, var46, false, 621900673);
        var6.bottle = var27;
        var6.bottle.setTeleportDuration(3);
        List var17 = this.ringParts;
        ItemDisplay var30 = var6.bottle;
        var17.add(var30);
        List var20 = this.ringSlots;
        var20.add(var6);
        BukkitScheduler var22 = Bukkit.getScheduler();
        BlissPlugin var33 = this.plugin;
        Runnable var37 = () -> {
            int var12 = this.spreadGen;
            if (var4 == var12 && var6.bottle.isValid()) {
                ItemDisplay var10 = var6.bottle;
                var10.teleport(var2);
            }
        };
        var22.runTaskLater(var33, var37, 1L);
        BukkitScheduler var24 = Bukkit.getScheduler();
        BlissPlugin var35 = this.plugin;
        Runnable var39 = () -> {
            int var23 = this.spreadGen;
            if (var4 == var23) {
                Location var33x = var2.clone().add(0.0, 0.77, 0.0);
                String var36 = this.pickerLabel(var1, 1626840005);
                TextColor var38 = NAME_GOLD;
                TextDisplay var25 = this.label(var33x, var36, var38, true, 1803394520);
                var6.name = var25;
                String var39x = "BlissPotionLimitPick";
                Interaction var27x = this.hitbox(var2, 1.68F, var39x, var1, 624443881);
                var6.box = var27x;
                List var18 = this.ringParts;
                TextDisplay var29 = var6.name;
                var18.add(var29);
                List var21 = this.ringParts;
                Interaction var31 = var6.box;
                var21.add(var31);
            }
        };
        var24.runTaskLater(var35, var39, 4L);
    }

    private void collapseRings(int var1) {
        if (this.spreadTask != null) {
            this.spreadTask.cancel();
            this.spreadTask = null;
        }

        this.expanded = false;
        final int var32 = this.spreadGen + 1;
        this.spreadGen = var32;
        if (this.ringSlots.isEmpty()) {
            List var38 = this.ringParts;
            this.removeAll(var38, 2121951745);
        } else {
            final Location var10 = this.anchor.clone().add(0.0, 1.2, 0.0);
            List var40 = this.ringSlots;
            final ArrayList var25 = new ArrayList(var40);
            this.ringSlots.clear();
            BukkitRunnable var35 = new BukkitRunnable() {
                int i = 0;

                public void run() {
                    int var9 = var32;
                    int var50 = PotionLimitHologram.this.spreadGen;
                    if (var9 != var50) {
                        this.cancel();
                    } else {
                        int var6 = 0;

                        while (var6 < 3) {
                            int var13 = this.i;
                            int var54 = var25.size();
                            if (var13 >= var54) {
                                break;
                            }

                            List var15 = var25;
                            int var56 = this.i;
                            PotionLimitHologram.Slot var7 = (PotionLimitHologram.Slot)var15.get(var56);
                            if (var7.name != null && var7.name.isValid()) {
                                var7.name.remove();
                            }

                            if (var7.box != null && var7.box.isValid()) {
                                var7.box.remove();
                            }

                            ItemDisplay var8 = var7.bottle;
                            if (var8 != null && var8.isValid()) {
                                var8.setTeleportDuration(3);
                                Location var59 = var10;
                                var8.teleport(var59);
                                BukkitScheduler var40 = Bukkit.getScheduler();
                                BlissPlugin var62 = PotionLimitHologram.this.plugin;
                                Runnable var69 = () -> {
                                    if (var8.isValid()) {
                                        var8.remove();
                                    }
                                };
                                var40.runTaskLater(var62, var69, 4L);
                            }

                            var6++;
                            int var64 = this.i + 1;
                            this.i = var64;
                        }

                        int var44 = this.i;
                        int var67 = var25.size();
                        if (var44 >= var67) {
                            PotionLimitHologram.this.spreadTask = null;
                            this.cancel();
                        }
                    }
                }
            };
            BlissPlugin var42 = this.plugin;
            BukkitTask var36 = var35.runTaskTimer(var42, 0L, 1L);
            this.spreadTask = var36;
        }
    }

    private String pickerLabel(PotionEffectType var1, int var2) {
        PotionLimits var6 = this.limits;
        int var5 = var6.cap(var1, 1961378735);
        String var11;
        if (var5 < 0) {
            var11 = prettyName(var1, 1303041911);
        } else {
            String var10 = prettyName(var1, 1303041911);
            var11 = var10 + " [" + var5 + "]";
        }

        return var11;
    }

    private void beginEdit(PotionEffectType var1, int var2) {
        this.collapseRings(48723493);
        List var67 = this.editParts;
        this.removeAll(var67, 2121951745);
        this.editing = var1;
        PotionLimits var23 = this.limits;
        int var14 = var23.cap(var1, 1961378735);
        int var97;
        if (var14 < 0) {
            var97 = 2;
        } else {
            var97 = var14;
        }

        this.editLevel = var97;
        if (this.anchor.getWorld() != null) {
            Location var16 = this.anchor.clone().add(0.0, 2.4, 0.0);
            List var35 = this.editParts;
            Color var137 = var1.getColor();
            ItemDisplay var98 = this.bottle(var16, 1.68F, var137, false, 621900673);
            ItemDisplay var74 = this.spinning(var98, var16, 263441187);
            var35.add(var74);
            List var38 = this.editParts;
            Location var101 = var16.clone().add(0.0, 0.77, 0.0);
            String var119 = prettyName(var1, 1303041911);
            TextColor var129 = NAME_GOLD;
            TextDisplay var76 = this.label(var101, var119, var129, true, 1803394520);
            var38.add(var76);
            Location var104 = var16.clone().add(0.0, 1.12, 0.0);
            String var123 = this.capText(2102545165);
            TextColor var130 = NAME_GOLD;
            TextDisplay var78 = this.label(var104, var123, var130, true, 1803394520);
            this.editLabel = var78;
            List var42 = this.editParts;
            TextDisplay var80 = this.editLabel;
            var42.add(var80);
            List var45 = this.editParts;
            String var131 = "BlissPotionLimitSave";
            Interaction var82 = this.hitbox(var16, 1.96F, var131, null, 624443881);
            var45.add(var82);
            Vector var17 = this.rightOfRoom(787616610);
            Location var50 = var16.clone();
            Vector var85 = var17.clone().multiply(1.54);
            Location var18 = var50.add(var85);
            Location var53 = var16.clone();
            Vector var88 = var17.clone().multiply(1.54);
            Location var19 = var53.subtract(var88);
            List var56 = this.editParts;
            String var125 = "▲";
            TextColor var132 = ARROW_GREY;
            TextDisplay var90 = this.bigLabel(var18, var125, var132, 1.35F, 456344305);
            var56.add(var90);
            List var59 = this.editParts;
            String var133 = "BlissPotionLimitUp";
            Interaction var92 = this.hitbox(var18, 1.0F, var133, null, 624443881);
            var59.add(var92);
            List var62 = this.editParts;
            String var127 = "▼";
            TextColor var134 = ARROW_GREY;
            TextDisplay var94 = this.bigLabel(var19, var127, var134, 1.35F, 456344305);
            var62.add(var94);
            List var65 = this.editParts;
            String var135 = "BlissPotionLimitDown";
            Interaction var96 = this.hitbox(var19, 1.0F, var135, null, 624443881);
            var65.add(var96);
        }
    }

    private void adjust(int var1, int var2) {
        if (this.editing != null) {
            int var34 = this.editLevel;
            int var35 = var34 + var1;
            int var28 = Math.min(5, var35);
            int var18 = Math.max(0, var28);
            this.editLevel = var18;
            if (this.editLabel != null && this.editLabel.isValid()) {
                TextDisplay var16 = this.editLabel;
                TextComponent var21 = Component.text(this.capText(2102545165));
                TextColor var29 = NAME_GOLD;
                TextComponent var23 = (TextComponent)var21.color(var29);
                TextDecoration var30 = TextDecoration.BOLD;
                TextComponent var25 = (TextComponent)var23.decoration(var30, true);
                TextDecoration var31 = TextDecoration.ITALIC;
                Component var26 = var25.decoration(var31, false);
                var16.text(var26);
            }

            Sound var27 = Sound.BLOCK_NOTE_BLOCK_HAT;
            float var33;
            if (var1 > 0) {
                var33 = 1.6F;
            } else {
                var33 = 0.8F;
            }

            this.sound(var27, var33, 1601292076);
        }
    }

    private void saveEdit(int var1) {
        if (this.editing != null) {
            PotionLimits var8 = this.limits;
            PotionEffectType var19 = this.editing;
            int var29 = this.editLevel;
            var8.setCap(var19, var29, 1301205225);
            PotionEffectType var5 = this.editing;
            this.editing = null;
            this.editLabel = null;
            List var23 = this.editParts;
            this.removeAll(var23, 2121951745);
            String var15 = prettyName(var5, 1303041911);
            String var28;
            if (this.editLevel == 0) {
                var28 = "banned";
            } else {
                var28 = "max " + this.editLevel;
            }

            Bukkit.broadcast(Text.legacy("&e✦ &6Potion Limit set: &e" + var15 + " &7→ &f" + var28, 373192400));
        }
    }

    private String capText(int var1) {
        String var6;
        if (this.editLevel == 0) {
            var6 = "Banned";
        } else {
            var6 = "Level " + this.editLevel;
        }

        return var6;
    }

    private Vector rightOfRoom(int var1) {
        double var9 = Math.toRadians((double)this.anchor.getYaw());
        double var21 = -Math.cos(var9);
        double var25 = -Math.sin(var9);
        Vector var17 = new Vector(var21, 0.0, var25);
        return var17.normalize();
    }

    private ItemDisplay bottle(Location var1, float var2, Color var3, boolean var4, int var5) {
        World var10 = var1.getWorld();
        Material var7 = Material.POTION;
        ItemStack var15 = new ItemStack(var7);
        if (var15.getItemMeta() instanceof PotionMeta var12) {
            if (var3 == null) {
                PotionType var38 = PotionType.WATER;
                var12.setBasePotionType(var38);
            } else {
                var12.setColor(var3);
            }

            if (var4 && var3 != null) {
                Boolean var35 = true;
                var12.setEnchantmentGlintOverride(var35);
            }

            var15.setItemMeta(var12);
        }

        Consumer<ItemDisplay> var40 = var2x -> {
            var2x.setItemStack(var15);
            ItemDisplayTransform var20 = ItemDisplayTransform.GROUND;
            var2x.setItemDisplayTransform(var20);
            Billboard var21 = Billboard.CENTER;
            var2x.setBillboard(var21);
            Brightness var22 = new Brightness(15, 15);
            var2x.setBrightness(var22);
            Vector3f var26 = new Vector3f();
            AxisAngle4f var27 = new AxisAngle4f(0.0F, 0.0F, 1.0F, 0.0F);
            Vector3f var28 = new Vector3f(var2, var2, var2);
            AxisAngle4f var29 = new AxisAngle4f(0.0F, 0.0F, 0.0F, 1.0F);
            Transformation var23 = new Transformation(var26, var27, var28, var29);
            var2x.setTransformation(var23);
            var2x.setPersistent(false);
            var2x.addScoreboardTag("BlissPotionLimit");
        };
        return (ItemDisplay)var10.spawn(var1, ItemDisplay.class, var40);
    }

    private ItemDisplay spinning(ItemDisplay var1, Location var2, int var3) {
        Billboard var5 = Billboard.FIXED;
        var1.setBillboard(var5);
        var1.setTeleportDuration(1);
        List var10 = this.spinners;
        Location var15 = var2.clone();
        PotionLimitHologram.Spin var14 = new PotionLimitHologram.Spin(var1, var15, 283731334);
        var10.add(var14);
        return var1;
    }

    private void startSpin(int var1) {
        if (this.spinTask == null) {
            BukkitRunnable var3 = new BukkitRunnable() {
                public void run() {
                    PotionLimitHologram var16 = PotionLimitHologram.this;
                    double var70 = var16.spinAngle + 0.05;
                    var16.spinAngle = var70;
                    if (PotionLimitHologram.this.spinAngle > Math.PI * 2) {
                        PotionLimitHologram var23 = PotionLimitHologram.this;
                        double var74 = var23.spinAngle - (Math.PI * 2);
                        var23.spinAngle = var74;
                    }

                    PotionLimitHologram var25 = PotionLimitHologram.this;
                    double var78 = var25.tumbleAngle + 0.030901699435000003;
                    var25.tumbleAngle = var78;
                    if (PotionLimitHologram.this.tumbleAngle > Math.PI * 2) {
                        PotionLimitHologram var32 = PotionLimitHologram.this;
                        double var82 = var32.tumbleAngle - (Math.PI * 2);
                        var32.tumbleAngle = var82;
                    }

                    PotionLimitHologram var34 = PotionLimitHologram.this;
                    double var86 = var34.bobAngle + 0.045000000000000005;
                    var34.bobAngle = var86;
                    if (PotionLimitHologram.this.bobAngle > Math.PI * 2) {
                        PotionLimitHologram var41 = PotionLimitHologram.this;
                        double var90 = var41.bobAngle - (Math.PI * 2);
                        var41.bobAngle = var90;
                    }

                    List<PotionLimitHologram.Spin> var44 = PotionLimitHologram.this.spinners;
                    Predicate<PotionLimitHologram.Spin> var92 = var0 -> {
                        boolean var4;
                        if (!var0.d.isValid()) {
                            var4 = true;
                        } else {
                            var4 = false;
                        }

                        return var4;
                    };
                    var44.removeIf(var92);

                    for (PotionLimitHologram.Spin var13 : PotionLimitHologram.this.spinners) {
                        Vector3f var14 = var13.d.getTransformation().getScale();
                        ItemDisplay var60 = var13.d;
                        Vector3f var115 = new Vector3f();
                        float var131 = (float)PotionLimitHologram.this.spinAngle;
                        AxisAngle4f var124 = new AxisAngle4f(var131, 0.0F, 1.0F, 0.0F);
                        float var137 = (float)PotionLimitHologram.this.tumbleAngle;
                        AxisAngle4f var132 = new AxisAngle4f(var137, 1.0F, 0.0F, 0.0F);
                        Transformation var93 = new Transformation(var115, var124, var14, var132);
                        var60.setTransformation(var93);
                        Location var15 = var13.home.clone();
                        double var96 = var13.home.getY();
                        double var122 = Math.sin(PotionLimitHologram.this.bobAngle) * 0.13;
                        double var98 = var96 + var122;
                        var15.setY(var98);
                        ItemDisplay var66 = var13.d;
                        var66.teleport(var15);
                    }
                }
            };
            BlissPlugin var11 = this.plugin;
            BukkitTask var9 = var3.runTaskTimer(var11, 1L, 1L);
            this.spinTask = var9;
        }
    }

    private void stopSpin(int var1) {
        if (this.spinTask != null) {
            this.spinTask.cancel();
            this.spinTask = null;
        }

        this.spinners.clear();
        this.bobAngle = 0.0;
        this.tumbleAngle = 0.0;
        this.spinAngle = 0.0;
    }

    private TextDisplay label(Location var1, String var2, TextColor var3, boolean var4, int var5) {
        World var14 = var1.getWorld();
        Consumer<TextDisplay> var17 = var3x -> {
            TextComponent var19 = Component.text(var2);
            TextComponent var21 = (TextComponent)var19.color(var3);
            TextDecoration var35 = TextDecoration.BOLD;
            TextComponent var23 = (TextComponent)var21.decoration(var35, var4);
            TextDecoration var36 = TextDecoration.ITALIC;
            Component var24 = var23.decoration(var36, false);
            var3x.text(var24);
            Billboard var25 = Billboard.CENTER;
            var3x.setBillboard(var25);
            TextAlignment var26 = TextAlignment.CENTER;
            var3x.setAlignment(var26);
            Color var28 = Color.fromARGB(0, 0, 0, 0);
            var3x.setBackgroundColor(var28);
            var3x.setShadowed(true);
            var3x.setSeeThrough(false);
            var3x.setDefaultBackground(false);
            Brightness var32 = new Brightness(15, 15);
            var3x.setBrightness(var32);
            var3x.setPersistent(false);
            var3x.addScoreboardTag("BlissPotionLimit");
        };
        return (TextDisplay)var14.spawn(var1, TextDisplay.class, var17);
    }

    private TextDisplay bigLabel(Location var1, String var2, TextColor var3, float var4, int var5) {
        TextDisplay var17 = this.label(var1, var2, var3, true, 1803394520);
        Vector3f var22 = new Vector3f();
        AxisAngle4f var23 = new AxisAngle4f(0.0F, 0.0F, 1.0F, 0.0F);
        Vector3f var24 = new Vector3f(var4, var4, var4);
        AxisAngle4f var25 = new AxisAngle4f(0.0F, 0.0F, 0.0F, 1.0F);
        Transformation var21 = new Transformation(var22, var23, var24, var25);
        var17.setTransformation(var21);
        return var17;
    }

    private Interaction hitbox(Location var1, float var2, String var3, PotionEffectType var4, int var5) {
        World var17 = var1.getWorld();
        Location var20 = var1.clone();
        double var26 = (double)var2 / 2.0;
        Location var21 = var20.subtract(0.0, var26, 0.0);
        Consumer<Interaction> var23 = var3x -> {
            var3x.setInteractionWidth(var2);
            var3x.setInteractionHeight(var2);
            var3x.setResponsive(true);
            var3x.setPersistent(false);
            var3x.addScoreboardTag("BlissPotionLimit");
            var3x.addScoreboardTag(var3);
            if (var4 != null && KEY_EFFECT != null) {
                PersistentDataContainer var19 = var3x.getPersistentDataContainer();
                NamespacedKey var25 = KEY_EFFECT;
                PersistentDataType var6 = PersistentDataType.STRING;
                String var27 = var4.getKey().getNamespace();
                String var30 = var4.getKey().getKey();
                String var28 = var27 + ":" + var30;
                var19.set(var25, var6, var28);
            }
        };
        return (Interaction)var17.spawn(var21, Interaction.class, var23);
    }

    private void removeAll(List<Entity> var1, int var2) {
        for (Entity var5 : var1) {
            if (var5.isValid()) {
                var5.remove();
            }
        }

        var1.clear();
    }

    private void sound(Sound var1, float var2, int var3) {
        World var15;
        if (this.anchor == null) {
            var15 = null;
        } else {
            var15 = this.anchor.getWorld();
        }

        if (var15 != null) {
            Location var16 = this.anchor;
            var15.playSound(var16, var1, 1.4F, var2);
        }
    }

    private static boolean hidden(PotionEffectType var0, int var1) {
        String var4 = var0.getKey().getKey();
        Set var7 = HIDDEN;
        boolean var11;
        if (!var7.contains(var4) && !var4.contains("nautilus")) {
            var11 = false;
        } else {
            var11 = true;
        }

        return var11;
    }

    private static List<PotionEffectType> allEffects(int var0) {
        ArrayList<PotionEffectType> var1 = new ArrayList<>();
        ArrayList<PotionEffectType> var2 = var1;

        for (PotionEffectType var4 : Registry.EFFECT) {
            if (!hidden(var4, 706700137)) {
                var2.add(var4);
            }
        }

        Comparator<PotionEffectType> var19 = (var0x, var1x) -> {
            String var5 = var0x.getKey().getKey();
            String var8 = var1x.getKey().getKey();
            return var5.compareTo(var8);
        };
        var2.sort(var19);
        return var2;
    }

    static String prettyName(PotionEffectType var0, int var1) {
        String[] var5 = var0.getKey().getKey().split("_");
        StringBuilder var12 = new StringBuilder();
        StringBuilder var6 = var12;
        int var7 = 0;

        while (true) {
            int var36 = var5.length;
            if (var7 >= var36) {
                return var6.toString();
            }

            String var8 = var5[var7];
            if (!var8.isEmpty()) {
                if (var6.length() > 0) {
                    var6.append(' ');
                }

                if (var7 <= 0 || !var8.equals("of") && !var8.equals("the")) {
                    char var44 = Character.toUpperCase(var8.charAt(0));
                    StringBuilder var31 = var6.append(var44);
                    String var46 = var8.substring(1);
                    Locale var49 = Locale.ROOT;
                    String var47 = var46.toLowerCase(var49);
                    var31.append(var47);
                } else {
                    var6.append(var8);
                }
            }

            var7++;
        }
    }

    private static PotionEffectType effectOf(Interaction var0, int var1) {
        if (KEY_EFFECT == null) {
            return null;
        } else {
            PersistentDataContainer var9 = var0.getPersistentDataContainer();
            NamespacedKey var3 = KEY_EFFECT;
            PersistentDataType var4 = PersistentDataType.STRING;
            String var5 = (String)var9.get(var3, var4);
            if (var5 == null) {
                return null;
            } else {
                NamespacedKey var6 = NamespacedKey.fromString(var5);
                PotionEffectType var19;
                if (var6 == null) {
                    var19 = null;
                } else {
                    Registry var17 = Registry.EFFECT;
                    var19 = (PotionEffectType)var17.get(var6);
                }

                return var19;
            }
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent var1) {
        EquipmentSlot var10 = var1.getHand();
        EquipmentSlot var3 = EquipmentSlot.HAND;
        if (var10 == var3) {
            if (var1.getRightClicked() instanceof Interaction var7) {
                if (var7.getScoreboardTags().contains("BlissPotionLimit")) {
                    var1.setCancelled(true);
                    Player var78 = var1.getPlayer();
                    BlissPlugin var24 = this.plugin;
                    if (WorldBorderHologram.decreeAllowed(var24, var78, 979011771)) {
                        if (!var7.getScoreboardTags().contains("BlissPotionLimitUp") && !var7.getScoreboardTags().contains("BlissPotionLimitDown")) {
                            World var34 = var7.getWorld();
                            Location var67 = var7.getLocation();
                            Sound var4 = Sound.BLOCK_BREWING_STAND_BREW;
                            var34.playSound(var67, var4, 1.4F, 1.0F);
                        }

                        if (var7.getScoreboardTags().contains("BlissPotionLimitRoot")) {
                            if (this.editing == null) {
                                this.toggleSpread(1197330851);
                            }
                        } else if (var7.getScoreboardTags().contains("BlissPotionLimitPick")) {
                            PotionEffectType var79 = effectOf(var7, 1755964223);
                            if (var79 != null) {
                                this.beginEdit(var79, 180554481);
                            }
                        } else if (var7.getScoreboardTags().contains("BlissPotionLimitUp")) {
                            this.adjust(1, 1922070458);
                        } else if (var7.getScoreboardTags().contains("BlissPotionLimitDown")) {
                            this.adjust(-1, 1922070458);
                        } else {
                            if (var7.getScoreboardTags().contains("BlissPotionLimitSave")) {
                                Component var77 = Text.legacy("&7Potion limit saved.", 373192400);
                                var78.sendMessage(var77);
                                this.saveEdit(1707923725);
                            }
                        }
                    }
                }
            }
        }
    }

    static {
        String[] var11 = new String[17];
        String var2 = "oozing";
        var11[0] = var2;
        String var29 = "hero_of_the_village";
        var11[1] = var29;
        String var30 = "absorption";
        var11[2] = var30;
        String var31 = "bad_omen";
        var11[3] = var31;
        String var32 = "conduit_power";
        var11[4] = var32;
        String var33 = "jump_boost";
        var11[5] = var33;
        String var34 = "levitation";
        var11[6] = var34;
        String var35 = "luck";
        var11[7] = var35;
        String var36 = "glowing";
        var11[8] = var36;
        String var37 = "unluck";
        var11[9] = var37;
        String var38 = "wither";
        var11[10] = var38;
        String var39 = "weakness";
        var11[11] = var39;
        String var40 = "water_breathing";
        var11[12] = var40;
        String var41 = "wind_charged";
        var11[13] = var41;
        String var42 = "nausea";
        var11[14] = var42;
        String var43 = "raid_omen";
        var11[15] = var43;
        String var44 = "hunger";
        var11[16] = var44;
        HIDDEN = Set.of(var11);
    }

    private static final class Slot {
        ItemDisplay bottle;
        TextDisplay name;
        Interaction box;
    }

    private static final class Spin {
        final ItemDisplay d;
        final Location home;

        Spin(ItemDisplay var1, Location var2, int var3) {
            this.d = var1;
            this.home = var2;
        }
    }
}
