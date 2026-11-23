package com.bliss.honored;

import com.bliss.BlissPlugin;
import com.bliss.goldgem.RitualItems;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.TextDisplay.TextAlignment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

public final class WorldBorderHologram implements Listener {
    private static final TextColor NAME_GOLD = TextColor.color(16770148);
    private static final TextColor NOTE_GREY = TextColor.color(11184810);
    private static final String TAG = "BlissWorldBorder";
    private static final String TAG_UP = "BlissWorldBorderUp";
    private static final String TAG_DOWN = "BlissWorldBorderDown";
    private static final int[] SIZES;
    private static final int DEFAULT_INDEX = 1;
    private static final double ORB_Y = 1.2;
    private static final float ORB_SCALE = 2.4F;
    private static final double TITLE_Y = 0.95;
    private static final double DOWN_Y = 1.15;
    private static final double TEXT_Y = 1.6;
    private static final double UP_Y = 2.05;
    private static final float BOX = 0.4F;
    private static final float ARROW_SCALE = 1.35F;
    private static final String GLYPH_UP = "▲";
    private static final String GLYPH_DOWN = "▼";
    private final BlissPlugin plugin;
    private Location anchor;
    private final List<Entity> parts;
    private TextDisplay reading;

    public WorldBorderHologram(BlissPlugin var1, int var2) {
        ArrayList var4 = new ArrayList();
        this.parts = var4;
        this.plugin = var1;
    }

    public void reconcile(int var1) {
        despawnAll(533512528);
        this.parts.clear();
        this.reading = null;
        ControlRoomManager var4 = this.plugin.controlRoom(1275615420);
        Location var13;
        if (var4 == null) {
            var13 = null;
        } else {
            var13 = var4.borderSpot(983401541);
        }

        if (var13 == null) {
            this.anchor = null;
        } else {
            Location var20 = var13.clone();
            this.anchor = var20;
            this.build(1725197167);
        }
    }

    public void shutdown(int var1) {
        despawnAll(533512528);
        this.parts.clear();
        this.reading = null;
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

    public static void despawnAll(int var0) {
        Iterator var3 = Bukkit.getWorlds().iterator();

        while (var3.hasNext()) {
            for (Entity var6 : ((World)var3.next()).getEntities()) {
                if (var6.getScoreboardTags().contains("BlissWorldBorder")) {
                    var6.remove();
                }
            }
        }
    }

    private void build(int var1) {
        World var13 = this.anchor.getWorld();
        if (var13 != null) {
            Location var14 = this.anchor.clone().add(0.0, 1.2, 0.0);
            List var26 = this.parts;
            Consumer<ItemDisplay> var8 = var0 -> {
                ItemStack var2 = RitualItems.holoOrb(737342991);
                var0.setItemStack(var2);
                Billboard var19 = Billboard.CENTER;
                var0.setBillboard(var19);
                Brightness var20 = new Brightness(15, 15);
                var0.setBrightness(var20);
                Vector3f var26x = new Vector3f();
                AxisAngle4f var27 = new AxisAngle4f(0.0F, 0.0F, 1.0F, 0.0F);
                Vector3f var28 = new Vector3f(2.4F, 2.4F, 2.4F);
                AxisAngle4f var29x = new AxisAngle4f(0.0F, 0.0F, 0.0F, 1.0F);
                Transformation var21 = new Transformation(var26x, var27, var28, var29x);
                var0.setTransformation(var21);
                var0.setDisplayWidth(3.6000001F);
                var0.setDisplayHeight(3.6000001F);
                var0.setPersistent(false);
                var0.addScoreboardTag("BlissWorldBorder");
            };
            Entity var57 = var13.spawn(var14, ItemDisplay.class, var8);
            var26.add(var57);
            List var29 = this.parts;
            Location var80 = var14.clone().add(0.0, 0.95, 0.0);
            String var89 = "World Border Size";
            TextColor var102 = NAME_GOLD;
            TextDisplay var59 = this.label(var80, var89, var102, 324884383);
            var29.add(var59);
            Location var15 = var14.clone().add(0.0, 1.15, 0.0);
            Location var16 = var14.clone().add(0.0, 1.6, 0.0);
            Location var17 = var14.clone().add(0.0, 2.05, 0.0);
            List var41 = this.parts;
            String var96 = "▼";
            TextColor var103 = NOTE_GREY;
            TextDisplay var67 = this.bigLabel(var15, var96, var103, 1.35F, 588813615);
            var41.add(var67);
            List var44 = this.parts;
            String var104 = "BlissWorldBorderDown";
            Interaction var69 = this.hitbox(var15, 0.4F, var104, 971116183);
            var44.add(var69);
            List var47 = this.parts;
            String var98 = "▲";
            TextColor var105 = NOTE_GREY;
            TextDisplay var71 = this.bigLabel(var17, var98, var105, 1.35F, 588813615);
            var47.add(var71);
            List var50 = this.parts;
            String var106 = "BlissWorldBorderUp";
            Interaction var73 = this.hitbox(var17, 0.4F, var106, 971116183);
            var50.add(var73);
            String var101 = this.readingText(12466163);
            TextColor var107 = NAME_GOLD;
            TextDisplay var75 = this.label(var16, var101, var107, 324884383);
            this.reading = var75;
            List var54 = this.parts;
            TextDisplay var77 = this.reading;
            var54.add(var77);
        }
    }

    private int index(int var1) {
        KitRules var4 = this.plugin.kitRules(1205957746);
        int var17;
        if (var4 == null) {
            var17 = 0;
        } else {
            var17 = var4.borderSize(692900498);
        }

        int var5 = var17;
        int var6 = 0;

        while (true) {
            int var18 = SIZES.length;
            if (var6 >= var18) {
                return 1;
            }

            int[] var12 = SIZES;
            int var13 = var12[var6];
            if (var13 == var5) {
                return var6;
            }

            var6++;
        }
    }

    private String readingText(int var1) {
        int[] var2 = SIZES;
        int var7 = this.index(952503510);
        return "Size: " + var2[var7] / 1000 + "k blocks";
    }

    private boolean step(int var1, int var2) {
        KitRules var7 = this.plugin.kitRules(1205957746);
        if (var7 == null) {
            return false;
        } else {
            int var9;
            boolean var24;
            label18: {
                int var8 = this.index(952503510);
                int var26 = SIZES.length - 1;
                int var34 = var8 + var1;
                int var27 = Math.min(var26, var34);
                var9 = Math.max(0, var27);
                if (var9 == var8) {
                    int var20 = var7.borderSize(692900498);
                    int[] var29 = SIZES;
                    int var30 = var29[var9];
                    if (var20 == var30) {
                        var24 = false;
                        break label18;
                    }
                }

                var24 = true;
            }

            int[] var31 = SIZES;
            int var32 = var31[var9];
            var7.setBorderSize(var32, 496876261);
            return var24;
        }
    }

    private void refresh(int var1) {
        if (this.reading != null && this.reading.isValid()) {
            TextDisplay var11 = this.reading;
            TextComponent var13 = Component.text(this.readingText(12466163));
            TextColor var4 = NAME_GOLD;
            TextComponent var15 = (TextComponent)var13.color(var4);
            TextDecoration var19 = TextDecoration.BOLD;
            TextComponent var17 = (TextComponent)var15.decoration(var19, true);
            TextDecoration var20 = TextDecoration.ITALIC;
            Component var18 = var17.decoration(var20, false);
            var11.text(var18);
        }
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
            var2x.addScoreboardTag("BlissWorldBorder");
        };
        return (TextDisplay)var12.spawn(var1, TextDisplay.class, var15);
    }

    private TextDisplay bigLabel(Location var1, String var2, TextColor var3, float var4, int var5) {
        TextDisplay var16 = this.label(var1, var2, var3, 324884383);
        Vector3f var21 = new Vector3f();
        AxisAngle4f var22 = new AxisAngle4f(0.0F, 0.0F, 1.0F, 0.0F);
        Vector3f var23 = new Vector3f(var4, var4, var4);
        AxisAngle4f var24 = new AxisAngle4f(0.0F, 0.0F, 0.0F, 1.0F);
        Transformation var20 = new Transformation(var21, var22, var23, var24);
        var16.setTransformation(var20);
        return var16;
    }

    private Interaction hitbox(Location var1, float var2, String var3, int var4) {
        World var15 = var1.getWorld();
        Location var18 = var1.clone();
        double var24 = (double)var2 / 2.0;
        Location var19 = var18.subtract(0.0, var24, 0.0);
        Consumer<Interaction> var21 = var2x -> {
            var2x.setInteractionWidth(var2);
            var2x.setInteractionHeight(var2);
            var2x.setResponsive(true);
            var2x.setPersistent(false);
            var2x.addScoreboardTag("BlissWorldBorder");
            var2x.addScoreboardTag(var3);
        };
        return (Interaction)var15.spawn(var19, Interaction.class, var21);
    }

    static boolean decreeAllowed(BlissPlugin var0, Player var1, int var2) {
        if (var1.hasPermission("bliss.admin")) {
            return true;
        } else {
            if (var0.honoredOne(783425701) != null) {
                HonoredOneManager var9 = var0.honoredOne(783425701);
                UUID var14 = var1.getUniqueId();
                if (var9.isHonoredOne(var14, 1908342269)) {
                    return true;
                }
            }

            return false;
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent var1) {
        EquipmentSlot var12 = var1.getHand();
        EquipmentSlot var3 = EquipmentSlot.HAND;
        if (var12 == var3) {
            Entity var7 = var1.getRightClicked();
            if (var7.getScoreboardTags().contains("BlissWorldBorder")) {
                var1.setCancelled(true);
                BlissPlugin var20 = this.plugin;
                Player var42 = var1.getPlayer();
                if (decreeAllowed(var20, var42, 979011771)) {
                    if (var7 instanceof Interaction var8) {
                        boolean var9 = var8.getScoreboardTags().contains("BlissWorldBorderUp");
                        boolean var10 = var8.getScoreboardTags().contains("BlissWorldBorderDown");
                        if (var9 || var10) {
                            byte var48;
                            if (var9) {
                                var48 = 1;
                            } else {
                                var48 = -1;
                            }

                            boolean var11 = this.step(var48, 1299782954);
                            this.refresh(1580947728);
                            World var38 = var8.getWorld();
                            Location var47 = var8.getLocation();
                            Sound var4 = Sound.BLOCK_NOTE_BLOCK_HAT;
                            float var50;
                            if (!var11) {
                                var50 = 0.6F;
                            } else if (var9) {
                                var50 = 1.6F;
                            } else {
                                var50 = 0.8F;
                            }

                            var38.playSound(var47, var4, 1.2F, var50);
                        }
                    }
                }
            }
        }
    }

    static {
        int[] var7 = new int[]{3000, 5000, 10000, 15000};
        SIZES = var7;
    }
}
