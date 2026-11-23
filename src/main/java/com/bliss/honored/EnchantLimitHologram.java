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
import org.bukkit.enchantments.Enchantment;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

public final class EnchantLimitHologram implements Listener {
    private static final TextColor NAME_GOLD = TextColor.color(16770148);
    private static final String TAG = "BlissEnchantLimit";
    private static final String TAG_ROOT = "BlissEnchantLimitRoot";
    private static final String TAG_PICK = "BlissEnchantLimitPick";
    private static final String TAG_UP = "BlissEnchantLimitUp";
    private static final String TAG_DOWN = "BlissEnchantLimitDown";
    private static final String TAG_SAVE = "BlissEnchantLimitSave";
    private static final NamespacedKey KEY_ENCH = NamespacedKey.fromString("bliss:ench_limit_key");
    private static final TextColor ARROW_GREY = TextColor.color(11184810);
    private static final double RING_R = 6.0;
    private static final int RING_COUNT = 12;
    private static final double RING_BASE_Y = 0.5;
    private static final double RING_GAP_Y = 1.5;
    private static final float BOOK_SCALE = 1.26F;
    private static final float ROOT_SCALE = 1.54F;
    private static final float EDIT_SCALE = 2.1F;
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
    private final EnchantLimits limits;
    private Location anchor;
    private final List<Entity> rootParts;
    private final List<Entity> ringParts;
    private final List<Entity> editParts;
    private boolean expanded;
    private Enchantment editing;
    private int editLevel;
    private TextDisplay editLabel;
    private int spreadGen;
    private BukkitTask spreadTask;
    private final List<EnchantLimitHologram.Slot> ringSlots;
    private final List<EnchantLimitHologram.Spin> spinners;
    private BukkitTask spinTask;
    private double spinAngle;
    private double tumbleAngle;
    private double bobAngle;
    private static final double SPIN_STEP = 0.05;
    private static final double TUMBLE_RATIO = 0.6180339887;
    private static final double BOB_AMP = 0.13;
    private static final double BOB_RATE = 0.9;
    private static final Set<String> HIDDEN;

    public EnchantLimitHologram(BlissPlugin var1, EnchantLimits var2, int var3) {
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
        this.stopSpin(963450405);
        int var35 = this.spreadGen + 1;
        this.spreadGen = var35;
        if (this.spreadTask != null) {
            this.spreadTask.cancel();
            this.spreadTask = null;
        }

        despawnAll(3264090);
        this.rootParts.clear();
        this.ringParts.clear();
        this.ringSlots.clear();
        this.editParts.clear();
        this.expanded = false;
        this.editing = null;
        this.editLabel = null;
        ControlRoomManager var4 = this.plugin.controlRoom(1275615420);
        if (var4 == null) {
            this.anchor = null;
        } else {
            Location var5 = var4.enchantSpot(944846913);
            if (var5 == null) {
                this.anchor = null;
            } else {
                Location var42 = var5.clone();
                this.anchor = var42;
                this.spawnRoot(722283077);
            }
        }
    }

    public void shutdown(int var1) {
        this.stopSpin(963450405);
        int var22 = this.spreadGen + 1;
        this.spreadGen = var22;
        if (this.spreadTask != null) {
            this.spreadTask.cancel();
            this.spreadTask = null;
        }

        despawnAll(3264090);
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
                if (var6.getScoreboardTags().contains("BlissEnchantLimit")) {
                    var6.remove();
                }
            }
        }
    }

    private void spawnRoot(int var1) {
        if (this.anchor.getWorld() != null) {
            Location var14 = this.anchor.clone().add(0.0, 1.2, 0.0);
            List var23 = this.rootParts;
            ItemDisplay var38 = this.book(var14, 1.54F, true, 2144484849);
            ItemDisplay var33 = this.spinning(var38, var14, 1057458963);
            var23.add(var33);
            List var26 = this.rootParts;
            Location var41 = var14.clone().add(0.0, 0.77, 0.0);
            String var47 = "Enchantment Limit";
            TextColor var49 = NAME_GOLD;
            TextDisplay var35 = this.label(var41, var47, var49, true, 1428612014);
            var26.add(var35);
            List var29 = this.rootParts;
            String var50 = "BlissEnchantLimitRoot";
            Interaction var37 = this.hitbox(var14, 1.96F, var50, null, 1357332373);
            var29.add(var37);
            this.startSpin(1500016762);
        }
    }

    private void toggleSpread(int var1) {
        if (this.expanded) {
            this.collapseRings(251087590);
        } else {
            this.expandRings(778471515);
        }
    }

    private void expandRings(int var1) {
        final List var9 = allEnchantments(2082701584);
        if (!var9.isEmpty()) {
            List var24 = this.ringParts;
            this.removeAll(var24, 170061561);
            this.ringSlots.clear();
            this.expanded = true;
            final int var27 = this.spreadGen + 1;
            this.spreadGen = var27;
            final Location var11 = this.anchor.clone().add(0.0, 1.2, 0.0);
            BukkitRunnable var30 = new BukkitRunnable() {
                int i = 0;

                public void run() {
                    int var7 = var27;
                    int var20 = EnchantLimitHologram.this.spreadGen;
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

                            EnchantLimitHologram var14 = EnchantLimitHologram.this;
                            List var26 = var9;
                            int var34 = this.i;
                            Enchantment var28 = (Enchantment)var26.get(var34);
                            EnchantLimitHologram var36 = EnchantLimitHologram.this;
                            int var39 = this.i;
                            Location var37 = var36.slotPos(var39, 1903980788);
                            Location var41 = var11;
                            int var42 = var27;
                            var14.emit(var28, var37, var41, var42, 585410794);
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

    private void emit(Enchantment var1, Location var2, Location var3, int var4, int var5) {
        EnchantLimitHologram.Slot var6 = new EnchantLimitHologram.Slot();
        ItemDisplay var27 = this.book(var3, 1.26F, 860629592);
        var6.book = var27;
        var6.book.setTeleportDuration(3);
        List var17 = this.ringParts;
        ItemDisplay var30 = var6.book;
        var17.add(var30);
        List var20 = this.ringSlots;
        var20.add(var6);
        BukkitScheduler var22 = Bukkit.getScheduler();
        BlissPlugin var33 = this.plugin;
        Runnable var37 = () -> {
            int var12 = this.spreadGen;
            if (var4 == var12 && var6.book.isValid()) {
                ItemDisplay var10 = var6.book;
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
                String var36 = this.pickerLabel(var1, 2069430724);
                TextColor var38 = NAME_GOLD;
                TextDisplay var25 = this.label(var33x, var36, var38, true, 1428612014);
                var6.name = var25;
                String var39x = "BlissEnchantLimitPick";
                Interaction var27x = this.hitbox(var2, 1.68F, var39x, var1, 1357332373);
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
            List var34 = this.ringParts;
            this.removeAll(var34, 170061561);
        } else {
            final Location var10 = this.anchor.clone().add(0.0, 1.2, 0.0);
            List var40 = this.ringSlots;
            final ArrayList var26 = new ArrayList(var40);
            this.ringSlots.clear();
            BukkitRunnable var37 = new BukkitRunnable() {
                int i = 0;

                public void run() {
                    int var9 = var32;
                    int var50 = EnchantLimitHologram.this.spreadGen;
                    if (var9 != var50) {
                        this.cancel();
                    } else {
                        int var6 = 0;

                        while (var6 < 3) {
                            int var14 = this.i;
                            int var54 = var26.size();
                            if (var14 >= var54) {
                                break;
                            }

                            List var16 = var26;
                            int var56 = this.i;
                            EnchantLimitHologram.Slot var7 = (EnchantLimitHologram.Slot)var16.get(var56);
                            if (var7.name != null && var7.name.isValid()) {
                                var7.name.remove();
                            }

                            if (var7.box != null && var7.box.isValid()) {
                                var7.box.remove();
                            }

                            ItemDisplay var8 = var7.book;
                            if (var8 != null && var8.isValid()) {
                                var8.setTeleportDuration(3);
                                Location var61 = var10;
                                var8.teleport(var61);
                                BukkitScheduler var42 = Bukkit.getScheduler();
                                BlissPlugin var64 = EnchantLimitHologram.this.plugin;
                                Runnable var70 = () -> {
                                    if (var8.isValid()) {
                                        var8.remove();
                                    }
                                };
                                var42.runTaskLater(var64, var70, 4L);
                            }

                            var6++;
                            int var58 = this.i + 1;
                            this.i = var58;
                        }

                        int var45 = this.i;
                        int var67 = var26.size();
                        if (var45 >= var67) {
                            EnchantLimitHologram.this.spreadTask = null;
                            this.cancel();
                        }
                    }
                }
            };
            BlissPlugin var42 = this.plugin;
            BukkitTask var38 = var37.runTaskTimer(var42, 0L, 1L);
            this.spreadTask = var38;
        }
    }

    private String pickerLabel(Enchantment var1, int var2) {
        EnchantLimits var6 = this.limits;
        int var5 = var6.cap(var1, 1602405455);
        String var12;
        if (var5 < 0) {
            var12 = prettyName(var1, 1817563720);
        } else {
            String var10 = prettyName(var1, 1817563720);
            var12 = var10 + " [" + var5 + "]";
        }

        return var12;
    }

    private void beginEdit(Enchantment var1, int var2) {
        this.collapseRings(251087590);
        List var66 = this.editParts;
        this.removeAll(var66, 170061561);
        this.editing = var1;
        EnchantLimits var22 = this.limits;
        int var13 = var22.cap(var1, 1602405455);
        int var97;
        if (var13 < 0) {
            var97 = var1.getMaxLevel();
        } else {
            var97 = var13;
        }

        this.editLevel = var97;
        if (this.anchor.getWorld() != null) {
            Location var15 = this.anchor.clone().add(0.0, 2.4, 0.0);
            List var34 = this.editParts;
            ItemDisplay var98 = this.book(var15, 2.1F, 860629592);
            ItemDisplay var73 = this.spinning(var98, var15, 1057458963);
            var34.add(var73);
            Location var101 = var15.clone().add(0.0, 1.12, 0.0);
            String var119 = this.capText(884627246);
            TextColor var129 = NAME_GOLD;
            TextDisplay var75 = this.label(var101, var119, var129, true, 1428612014);
            this.editLabel = var75;
            List var38 = this.editParts;
            TextDisplay var77 = this.editLabel;
            var38.add(var77);
            List var41 = this.editParts;
            Location var104 = var15.clone().subtract(0.0, 0.7, 0.0);
            String var123 = prettyName(var1, 1817563720);
            TextColor var130 = NAME_GOLD;
            TextDisplay var79 = this.label(var104, var123, var130, true, 1428612014);
            var41.add(var79);
            List var44 = this.editParts;
            String var131 = "BlissEnchantLimitSave";
            Interaction var81 = this.hitbox(var15, 1.96F, var131, null, 1357332373);
            var44.add(var81);
            Vector var16 = this.rightOfRoom(369613262);
            Location var49 = var15.clone();
            Vector var84 = var16.clone().multiply(1.54);
            Location var17 = var49.add(var84);
            Location var52 = var15.clone();
            Vector var87 = var16.clone().multiply(1.54);
            Location var18 = var52.subtract(var87);
            List var55 = this.editParts;
            String var125 = "▲";
            TextColor var132 = ARROW_GREY;
            TextDisplay var89 = this.bigLabel(var17, var125, var132, 1.35F, 88036053);
            var55.add(var89);
            List var58 = this.editParts;
            String var133 = "BlissEnchantLimitUp";
            Interaction var91 = this.hitbox(var17, 1.0F, var133, null, 1357332373);
            var58.add(var91);
            List var61 = this.editParts;
            String var127 = "▼";
            TextColor var134 = ARROW_GREY;
            TextDisplay var93 = this.bigLabel(var18, var127, var134, 1.35F, 88036053);
            var61.add(var93);
            List var64 = this.editParts;
            String var135 = "BlissEnchantLimitDown";
            Interaction var95 = this.hitbox(var18, 1.0F, var135, null, 1357332373);
            var64.add(var95);
        }
    }

    private void adjust(int var1, int var2) {
        if (this.editing != null) {
            int var22 = this.editing.getMaxLevel();
            int var8 = Math.max(1, var22);
            int var40 = this.editLevel;
            int var41 = var40 + var1;
            int var34 = Math.min(var8, var41);
            int var24 = Math.max(0, var34);
            this.editLevel = var24;
            if (this.editLabel != null && this.editLabel.isValid()) {
                TextDisplay var19 = this.editLabel;
                TextComponent var27 = Component.text(this.capText(884627246));
                TextColor var35 = NAME_GOLD;
                TextComponent var29 = (TextComponent)var27.color(var35);
                TextDecoration var36 = TextDecoration.BOLD;
                TextComponent var31 = (TextComponent)var29.decoration(var36, true);
                TextDecoration var37 = TextDecoration.ITALIC;
                Component var32 = var31.decoration(var37, false);
                var19.text(var32);
            }

            Sound var33 = Sound.BLOCK_NOTE_BLOCK_HAT;
            float var39;
            if (var1 > 0) {
                var39 = 1.6F;
            } else {
                var39 = 0.8F;
            }

            this.sound(var33, var39, 1450689667);
        }
    }

    private void saveEdit(int var1) {
        if (this.editing != null) {
            EnchantLimits var8 = this.limits;
            Enchantment var19 = this.editing;
            int var29 = this.editLevel;
            var8.setCap(var19, var29, 991827291);
            Enchantment var5 = this.editing;
            this.editing = null;
            this.editLabel = null;
            List var23 = this.editParts;
            this.removeAll(var23, 170061561);
            String var15 = prettyName(var5, 1817563720);
            String var28;
            if (this.editLevel == 0) {
                var28 = "banned";
            } else {
                var28 = "max " + this.editLevel;
            }

            Bukkit.broadcast(Text.legacy("&e✦ &6Enchantment Limit set: &e" + var15 + " &7→ &f" + var28, 373192400));
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

    private ItemDisplay book(Location var1, float var2, int var3) {
        return this.book(var1, var2, false, 2144484849);
    }

    private ItemDisplay book(Location var1, float var2, boolean var3, int var4) {
        World var9 = var1.getWorld();
        Material var6 = Material.ENCHANTED_BOOK;
        ItemStack var13 = new ItemStack(var6);
        if (var3) {
            ItemMeta var11 = var13.getItemMeta();
            if (var11 != null) {
                Boolean var25 = true;
                var11.setEnchantmentGlintOverride(var25);
                var13.setItemMeta(var11);
            }
        }

        Consumer<ItemDisplay> var29 = var2x -> {
            var2x.setItemStack(var13);
            ItemDisplayTransform var20 = ItemDisplayTransform.GROUND;
            var2x.setItemDisplayTransform(var20);
            Billboard var21 = Billboard.CENTER;
            var2x.setBillboard(var21);
            Brightness var22 = new Brightness(15, 15);
            var2x.setBrightness(var22);
            Vector3f var26 = new Vector3f();
            AxisAngle4f var27 = new AxisAngle4f(0.0F, 0.0F, 1.0F, 0.0F);
            Vector3f var28 = new Vector3f(var2, var2, var2);
            AxisAngle4f var29x = new AxisAngle4f(0.0F, 0.0F, 0.0F, 1.0F);
            Transformation var23 = new Transformation(var26, var27, var28, var29x);
            var2x.setTransformation(var23);
            var2x.setPersistent(false);
            var2x.addScoreboardTag("BlissEnchantLimit");
        };
        return (ItemDisplay)var9.spawn(var1, ItemDisplay.class, var29);
    }

    private ItemDisplay spinning(ItemDisplay var1, Location var2, int var3) {
        Billboard var5 = Billboard.FIXED;
        var1.setBillboard(var5);
        var1.setTeleportDuration(1);
        List var10 = this.spinners;
        Location var15 = var2.clone();
        EnchantLimitHologram.Spin var14 = new EnchantLimitHologram.Spin(var1, var15, 2094312463);
        var10.add(var14);
        return var1;
    }

    private void startSpin(int var1) {
        if (this.spinTask == null) {
            BukkitRunnable var3 = new BukkitRunnable() {
                public void run() {
                    EnchantLimitHologram var16 = EnchantLimitHologram.this;
                    double var70 = var16.spinAngle + 0.05;
                    var16.spinAngle = var70;
                    if (EnchantLimitHologram.this.spinAngle > Math.PI * 2) {
                        EnchantLimitHologram var23 = EnchantLimitHologram.this;
                        double var74 = var23.spinAngle - (Math.PI * 2);
                        var23.spinAngle = var74;
                    }

                    EnchantLimitHologram var25 = EnchantLimitHologram.this;
                    double var78 = var25.tumbleAngle + 0.030901699435000003;
                    var25.tumbleAngle = var78;
                    if (EnchantLimitHologram.this.tumbleAngle > Math.PI * 2) {
                        EnchantLimitHologram var32 = EnchantLimitHologram.this;
                        double var82 = var32.tumbleAngle - (Math.PI * 2);
                        var32.tumbleAngle = var82;
                    }

                    EnchantLimitHologram var34 = EnchantLimitHologram.this;
                    double var86 = var34.bobAngle + 0.045000000000000005;
                    var34.bobAngle = var86;
                    if (EnchantLimitHologram.this.bobAngle > Math.PI * 2) {
                        EnchantLimitHologram var41 = EnchantLimitHologram.this;
                        double var90 = var41.bobAngle - (Math.PI * 2);
                        var41.bobAngle = var90;
                    }

                    List<EnchantLimitHologram.Spin> var44 = EnchantLimitHologram.this.spinners;
                    Predicate<EnchantLimitHologram.Spin> var92 = var0 -> {
                        boolean var4;
                        if (!var0.d.isValid()) {
                            var4 = true;
                        } else {
                            var4 = false;
                        }

                        return var4;
                    };
                    var44.removeIf(var92);

                    for (EnchantLimitHologram.Spin var13 : EnchantLimitHologram.this.spinners) {
                        Vector3f var14 = var13.d.getTransformation().getScale();
                        ItemDisplay var60 = var13.d;
                        Vector3f var115 = new Vector3f();
                        float var131 = (float)EnchantLimitHologram.this.spinAngle;
                        AxisAngle4f var124 = new AxisAngle4f(var131, 0.0F, 1.0F, 0.0F);
                        float var137 = (float)EnchantLimitHologram.this.tumbleAngle;
                        AxisAngle4f var132 = new AxisAngle4f(var137, 1.0F, 0.0F, 0.0F);
                        Transformation var93 = new Transformation(var115, var124, var14, var132);
                        var60.setTransformation(var93);
                        Location var15 = var13.home.clone();
                        double var96 = var13.home.getY();
                        double var122 = Math.sin(EnchantLimitHologram.this.bobAngle) * 0.13;
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
            var3x.addScoreboardTag("BlissEnchantLimit");
        };
        return (TextDisplay)var14.spawn(var1, TextDisplay.class, var17);
    }

    private TextDisplay bigLabel(Location var1, String var2, TextColor var3, float var4, int var5) {
        TextDisplay var17 = this.label(var1, var2, var3, true, 1428612014);
        Vector3f var22 = new Vector3f();
        AxisAngle4f var23 = new AxisAngle4f(0.0F, 0.0F, 1.0F, 0.0F);
        Vector3f var24 = new Vector3f(var4, var4, var4);
        AxisAngle4f var25 = new AxisAngle4f(0.0F, 0.0F, 0.0F, 1.0F);
        Transformation var21 = new Transformation(var22, var23, var24, var25);
        var17.setTransformation(var21);
        return var17;
    }

    private Interaction hitbox(Location var1, float var2, String var3, Enchantment var4, int var5) {
        World var17 = var1.getWorld();
        Location var20 = var1.clone();
        double var26 = (double)var2 / 2.0;
        Location var21 = var20.subtract(0.0, var26, 0.0);
        Consumer<Interaction> var23 = var3x -> {
            var3x.setInteractionWidth(var2);
            var3x.setInteractionHeight(var2);
            var3x.setResponsive(true);
            var3x.setPersistent(false);
            var3x.addScoreboardTag("BlissEnchantLimit");
            var3x.addScoreboardTag(var3);
            if (var4 != null && KEY_ENCH != null) {
                PersistentDataContainer var19 = var3x.getPersistentDataContainer();
                NamespacedKey var25 = KEY_ENCH;
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

    private static List<Enchantment> allEnchantments(int var0) {
        ArrayList<Enchantment> var1 = new ArrayList<>();
        ArrayList<Enchantment> var2 = var1;

        for (Enchantment var4 : Registry.ENCHANTMENT) {
            Set<String> var14 = HIDDEN;
            String var21 = var4.getKey().getKey();
            if (!var14.contains(var21)) {
                var2.add(var4);
            }
        }

        Comparator<Enchantment> var18 = (var0x, var1x) -> {
            String var5 = var0x.getKey().getKey();
            String var8 = var1x.getKey().getKey();
            return var5.compareTo(var8);
        };
        var2.sort(var18);
        return var2;
    }

    static String prettyName(Enchantment var0, int var1) {
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

    private static Enchantment enchantOf(Interaction var0, int var1) {
        if (KEY_ENCH == null) {
            return null;
        } else {
            PersistentDataContainer var8 = var0.getPersistentDataContainer();
            NamespacedKey var3 = KEY_ENCH;
            PersistentDataType var4 = PersistentDataType.STRING;
            String var5 = (String)var8.get(var3, var4);
            if (var5 == null) {
                return null;
            } else {
                NamespacedKey var6 = NamespacedKey.fromString(var5);
                Enchantment var19;
                if (var6 == null) {
                    var19 = null;
                } else {
                    Registry var15 = Registry.ENCHANTMENT;
                    var19 = (Enchantment)var15.get(var6);
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
                if (var7.getScoreboardTags().contains("BlissEnchantLimit")) {
                    var1.setCancelled(true);
                    Player var78 = var1.getPlayer();
                    BlissPlugin var24 = this.plugin;
                    if (WorldBorderHologram.decreeAllowed(var24, var78, 979011771)) {
                        if (!var7.getScoreboardTags().contains("BlissEnchantLimitUp") && !var7.getScoreboardTags().contains("BlissEnchantLimitDown")) {
                            World var34 = var7.getWorld();
                            Location var67 = var7.getLocation();
                            Sound var4 = Sound.BLOCK_ENCHANTMENT_TABLE_USE;
                            var34.playSound(var67, var4, 1.4F, 1.0F);
                        }

                        if (var7.getScoreboardTags().contains("BlissEnchantLimitRoot")) {
                            if (this.editing == null) {
                                this.toggleSpread(730661517);
                            }
                        } else if (var7.getScoreboardTags().contains("BlissEnchantLimitPick")) {
                            Enchantment var79 = enchantOf(var7, 1056426858);
                            if (var79 != null) {
                                this.beginEdit(var79, 358056206);
                            }
                        } else if (var7.getScoreboardTags().contains("BlissEnchantLimitUp")) {
                            this.adjust(1, 1423587625);
                        } else if (var7.getScoreboardTags().contains("BlissEnchantLimitDown")) {
                            this.adjust(-1, 1423587625);
                        } else {
                            if (var7.getScoreboardTags().contains("BlissEnchantLimitSave")) {
                                Component var77 = Text.legacy("&7Enchantment limit saved.", 373192400);
                                var78.sendMessage(var77);
                                this.saveEdit(488625376);
                            }
                        }
                    }
                }
            }
        }
    }

    static {
        String var11 = "lure";
        String var1 = "luck_of_the_sea";
        String var2 = "bane_of_arthropods";
        String var3 = "fire_protection";
        String var4 = "smite";
        String var5 = "projectile_protection";
        HIDDEN = Set.of(var11, var1, var2, var3, var4, var5);
    }

    private static final class Slot {
        ItemDisplay book;
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
