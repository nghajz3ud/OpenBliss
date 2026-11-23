package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.AchievementStore;
import com.bliss.core.EnergyScale;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.Particles;
import com.bliss.core.PlayerDataStore;
import com.bliss.goldgem.GoldGem;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public final class BountyHunterListener implements Listener {
    private static final long DOUBLE_CLICK_WINDOW_MS = 500L;
    private static final long BOUNTY_COOLDOWN_MS = 300000L;
    private static final int ITEM_BOUNTY_VALUE = 60;
    private static final int HEAD_BOUNTY_VALUE = 360;
    private static final int TRAIL_MAX_STEPS = 35;
    private static final double TRAIL_STEP = 0.2;
    private static final int ACTION_BAR_TICKS = 200;
    private static final long BOUNTY_LOOP_PERIOD_TICKS = 5L;
    private static final Component SELECT_TARGET_TITLE;
    private static final Component PLACE_ITEMS_TITLE;
    private static final NamespacedKey ITEMTAG_ORIGINAL_OWNER = NamespacedKey.fromString("itemtag:original_owner");
    private static final DustOptions DUST_BOUNTY;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final StrengthGemListener strengthListener;
    private final Map<UUID, Long> firstClick;
    private final Map<UUID, Integer> lastInteractTick;
    private final Map<UUID, UUID> target;

    public BountyHunterListener(BlissPlugin var1, PlayerDataStore var2, StrengthGemListener var3, int var4) {
        HashMap var6 = new HashMap();
        this.firstClick = var6;
        HashMap var13 = new HashMap();
        this.lastInteractTick = var13;
        HashMap var14 = new HashMap();
        this.target = var14;
        this.plugin = var1;
        this.players = var2;
        this.strengthListener = var3;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent var1) {
        Action var5 = var1.getAction();
        Action var3 = Action.RIGHT_CLICK_AIR;
        if (var5 != var3) {
            Action var97 = Action.RIGHT_CLICK_BLOCK;
            if (var5 != var97) {
                return;
            }
        }

        Player var6 = var1.getPlayer();
        int var7 = Bukkit.getCurrentTick();
        Map var27 = this.lastInteractTick;
        UUID var99 = var6.getUniqueId();
        Integer var8 = (Integer)var27.get(var99);
        if (var8 != null) {
            int var32 = var8;
            if (var32 == var7) {
                return;
            }
        }

        Map var34 = this.lastInteractTick;
        UUID var102 = var6.getUniqueId();
        Integer var119 = var7;
        var34.put(var102, var119);
        PlayerInventory var9 = var6.getInventory();
        ItemStack var10 = var9.getItemInOffHand();
        GemType var103 = GemType.STRENGTH;
        boolean var11 = GoldGem.isWieldingSoulOff(var6, var103, 1989261555);
        if (BlissCoreItems.isStrengthGem(var10, 1818434225) || var11) {
            Material var13 = var9.getItemInMainHand().getType();
            Material var104 = Material.AIR;
            if (var13 == var104 || isSwordOrAxe(var13, 451956933)) {
                PlayerDataStore var53 = this.players;
                UUID var106 = var6.getUniqueId();
                if (var53.get(var106, 1264010596).canUseAbilities(325243089)) {
                    UUID var14 = var6.getUniqueId();
                    Map var60 = this.strengthListener.cd4;
                    Long var15 = (Long)var60.get(var14);
                    long var16 = System.currentTimeMillis();
                    if (var15 != null) {
                        long var67 = var15;
                        if (var67 > var16) {
                            Component var113 = Text.legacy(
                                "&4\ud83d\udd2e &cYour §eBounty Hunter &cskill is on cooldown for &e" + formatRemaining(var15, 386643211), 373192400
                            );
                            var6.sendMessage(var113);
                            return;
                        }
                    }

                    label53: {
                        if (GlobalState.forPlayer(var6, 1433087673).sneakCast(704684993)) {
                            if (!var6.isSneaking()) {
                                return;
                            }

                            Map var95 = this.firstClick;
                            var95.remove(var14);
                        } else {
                            Map var76 = this.firstClick;
                            Long var19 = (Long)var76.get(var14);
                            if (var19 == null) {
                                break label53;
                            }

                            long var123 = var19;
                            if (var16 - var123 > 500L) {
                                break label53;
                            }

                            Map var86 = this.firstClick;
                            var86.remove(var14);
                        }

                        this.openSelectTargetGui(var6, 473995026);
                        return;
                    }

                    Map var90 = this.firstClick;
                    Long var129 = var16;
                    var90.put(var14, var129);
                }
            }
        }
    }

    private void openSelectTargetGui(Player var1, int var2) {
        UUID var51 = var1.getUniqueId();
        BountyHunterListener.SelectTargetHolder var3 = new BountyHunterListener.SelectTargetHolder(var51, 892298202);
        Component var52 = SELECT_TARGET_TITLE;
        Inventory var6 = Bukkit.createInventory(var3, 54, var52);
        int var7 = 0;

        for (Player var9 : Bukkit.getOnlinePlayers()) {
            if (!var9.equals(var1)) {
                if (var7 >= 54) {
                    break;
                }

                Material var53 = Material.PLAYER_HEAD;
                ItemStack var25 = new ItemStack(var53);
                if (var25.getItemMeta() instanceof SkullMeta var11) {
                    var11.setOwningPlayer(var9);
                    TextComponent var46 = Component.text(var9.getName());
                    TextDecoration var54 = TextDecoration.ITALIC;
                    Component var47 = var46.decoration(var54, false);
                    var11.displayName(var47);
                    var25.setItemMeta(var11);
                }

                int var49 = var7++;
                var6.setItem(var49, var25);
            }
        }

        var1.openInventory(var6);
    }

    @EventHandler
    public void onClick(InventoryClickEvent var1) {
        if (var1.getInventory().getHolder() instanceof BountyHunterListener.SelectTargetHolder var7) {
            var1.setCancelled(true);
            if (!(var1.getWhoClicked() instanceof Player var8)) {
                return;
            }

            UUID var27 = var8.getUniqueId();
            UUID var53 = var7.owner;
            if (!var27.equals(var53)) {
                return;
            }

            ItemStack var67 = var1.getCurrentItem();
            if (var67 == null) {
                return;
            }

            Material var33 = var67.getType();
            Material var54 = Material.PLAYER_HEAD;
            if (var33 != var54) {
                return;
            }

            if (!(var67.getItemMeta() instanceof SkullMeta var10)) {
                return;
            }

            if (var10.getOwningPlayer() == null) {
                return;
            }

            UUID var68 = var10.getOwningPlayer().getUniqueId();
            Map var46 = this.target;
            UUID var56 = var8.getUniqueId();
            var46.put(var56, var68);
            UUID var61 = var8.getUniqueId();
            BountyHunterListener.PlaceItemsHolder var48 = new BountyHunterListener.PlaceItemsHolder(var61, 1213309770);
            InventoryType var57 = InventoryType.HOPPER;
            Component var62 = PLACE_ITEMS_TITLE;
            Inventory var12 = Bukkit.createInventory(var48, var57, var62);
            BukkitScheduler var50 = Bukkit.getScheduler();
            BlissPlugin var59 = this.plugin;
            Runnable var64 = () -> var8.openInventory(var12);
            var50.runTaskLater(var59, var64, 1L);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent var1) {
        if (var1.getInventory().getHolder() instanceof BountyHunterListener.PlaceItemsHolder var9) {
            if (var1.getPlayer() instanceof Player var220) {
                UUID var35 = var220.getUniqueId();
                UUID var130 = var9.owner;
                if (var35.equals(var130)) {
                    UUID var221 = var220.getUniqueId();
                    Map var40 = this.target;
                    UUID var12 = (UUID)var40.remove(var221);
                    if (var12 != null) {
                        Player var13 = Bukkit.getPlayer(var12);
                        String var47;
                        if (var13 != null) {
                            var47 = var13.getName();
                        } else {
                            var47 = null;
                        }

                        String var14 = var47;
                        short var15 = 0;
                        if (var47 != null) {
                            for (ItemStack var19 : var1.getInventory().getContents()) {
                                if (var19 != null && (matchesOriginalOwner(var19, var14, 347875866) || headOf(var19, var12, 1295855690))) {
                                    Material var137 = var19.getType();
                                    Material var4 = Material.PLAYER_HEAD;
                                    short var169;
                                    if (var137 == var4) {
                                        var169 = 360;
                                    } else {
                                        var169 = 60;
                                    }

                                    var15 += var169;
                                }
                            }
                        }

                        if (var15 <= 0) {
                            Component var139 = Text.legacy("<##F10303>\ud83d\udd2e &cInsufficient amount of items.", 373192400);
                            var220.sendMessage(var139);
                            Inventory var171 = var1.getInventory();
                            this.returnHopperItems(var220, var171, 1256704963);
                        } else if (var13 == null) {
                            Component var167 = Text.legacy("<##F10303>\ud83d\udd2e &cTarget is offline.", 373192400);
                            var220.sendMessage(var167);
                            Inventory var202 = var1.getInventory();
                            this.returnHopperItems(var220, var202, 1256704963);
                        } else {
                            World var71 = var13.getWorld();
                            World var142 = var220.getWorld();
                            if (!var71.equals(var142)) {
                                Component var144 = Text.legacy("<##F10303>\ud83d\udd2e &cTarget is in another dimension.", 373192400);
                                var220.sendMessage(var144);
                                Inventory var173 = var1.getInventory();
                                this.returnHopperItems(var220, var173, 1256704963);
                            } else {
                                ItemStack[] var222 = var1.getInventory().getContents();
                                int var227 = var222.length;

                                for (byte var228 = 0; var228 < var227; var228 += 1) {
                                    ItemStack var231 = var222[var228];
                                    if (var231 != null) {
                                        Material var119 = var231.getType();
                                        Material var162 = Material.AIR;
                                        if (var119 != var162 && !matchesOriginalOwner(var231, var14, 347875866) && !headOf(var231, var12, 1295855690)) {
                                            giveBack(var220, var231, 364342012);
                                        }
                                    }
                                }

                                Component var148 = Text.legacy("<##F10303>\ud83d\udd2e &cActivated <##F10303>Bounty Hunter", 373192400);
                                var220.sendMessage(var148);
                                Location var150 = var13.getLocation();
                                Sound var174 = Sound.ENTITY_WARDEN_HEARTBEAT;
                                var13.playSound(var150, var174, 4.0F, 1.0F);
                                Location var152 = var13.getLocation();
                                Sound var175 = Sound.BLOCK_BELL_USE;
                                var13.playSound(var152, var175, 10.0F, 0.8F);
                                Location var154 = var13.getLocation();
                                Sound var176 = Sound.AMBIENT_CAVE;
                                var13.playSound(var154, var176, 8.0F, 0.8F);
                                this.startTargetActionBar(var13, 988383056);
                                long var128;
                                if (GlobalState.isHoldingDragonEgg(var220, 2044938455)) {
                                    var128 = 150000L;
                                } else {
                                    var128 = 300000L;
                                }

                                String var89 = "strength";
                                String var156 = "bounty-hunter";
                                long var90 = GemTuning.cd(var89, var156, 300.0, var128, 1621983041);
                                PlayerDataStore var180 = this.players;
                                int var182 = var180.get(var221, 1264010596).energy(1160853318);
                                long var225 = EnergyScale.cd(var90, var182, 907518618);
                                long var229 = System.currentTimeMillis();
                                long var20 = (long)var15 * 5L * 50L;
                                Map var105 = this.strengthListener.bountyActiveUntil;
                                Long var191 = var229 + var20;
                                var105.put(var221, var191);
                                Map var109 = this.strengthListener.cd4;
                                long var194 = var229 + var20;
                                Long var198 = var194 + var225;
                                var109.put(var221, var198);
                                AchievementStore var113 = this.plugin.advancements(1975441698);
                                AchievementStore.Advancement var199 = AchievementStore.Advancement.RABBIT_SEASON;
                                var113.targetProgress(var220, var199, var12, 1, 794164211);
                                this.startBountyTrail(var220, var13, var15, var221, var225, 120449203);
                            }
                        }
                    }
                }
            }
        }
    }

    public void shutdown(int var1) {
        for (Player var6 : Bukkit.getOnlinePlayers()) {
            Inventory var7 = var6.getOpenInventory().getTopInventory();
            if (var7.getHolder() instanceof BountyHunterListener.PlaceItemsHolder) {
                this.returnHopperItems(var6, var7, 1256704963);
                Map var22 = this.target;
                UUID var26 = var6.getUniqueId();
                var22.remove(var26);
                var6.closeInventory();
            }
        }
    }

    private void returnHopperItems(Player var1, Inventory var2, int var3) {
        for (ItemStack var9 : var2.getContents()) {
            if (var9 != null) {
                Material var19 = var9.getType();
                Material var23 = Material.AIR;
                if (var19 != var23) {
                    giveBack(var1, var9, 364342012);
                }
            }
        }

        var2.clear();
    }

    private static void giveBack(Player var0, ItemStack var1, int var2) {
        if (var0.isDead()) {
            World var12 = var0.getWorld();
            Location var27 = var0.getLocation();
            var12.dropItemNaturally(var27, var1);
        } else {
            PlayerInventory var15 = var0.getInventory();
            ItemStack[] var29 = new ItemStack[]{var1};

            for (ItemStack var9 : var15.addItem(var29).values()) {
                World var25 = var0.getWorld();
                Location var31 = var0.getLocation();
                var25.dropItemNaturally(var31, var9);
            }
        }
    }

    private void startTargetActionBar(final Player var1, int var2) {
        BukkitRunnable var3 = new BukkitRunnable() {
            int ticksLeft = 200;

            public void run() {
                int var10 = this.ticksLeft - 1;
                this.ticksLeft = var10;
                if (var10 >= 0 && var1.isOnline()) {
                    Player var7 = var1;
                    Component var12 = Text.legacy("<##F10303>You feel like you're being watched...", 373192400);
                    var7.sendActionBar(var12);
                } else {
                    this.cancel();
                }
            }
        };
        BlissPlugin var10 = this.plugin;
        var3.runTaskTimer(var10, 0L, 1L);
    }

    private void startBountyTrail(final Player var1, final Player var2, final int var3, final UUID var4, final long var5, int var7) {
        BukkitRunnable var8 = new BukkitRunnable() {
            int remaining;

            {
                int var25 = var3;
                this.remaining = var25;
            }

            public void run() {
                int var28 = this.remaining - 1;
                this.remaining = var28;
                if (var28 >= 0 && var1.isOnline() && var2.isOnline()) {
                    World var12 = var1.getWorld();
                    World var31 = var2.getWorld();
                    if (var12.equals(var31)) {
                        Player var15 = var1;
                        Player var33 = var2;
                        BountyHunterListener.drawBountyLine(var15, var33, 1554612729);
                        return;
                    }
                }

                Map var19 = BountyHunterListener.this.strengthListener.cd4;
                UUID var35 = var4;
                long var38 = System.currentTimeMillis();
                long var43 = var5;
                Long var42 = var38 + var43;
                var19.put(var35, var42);
                Map var24 = BountyHunterListener.this.strengthListener.bountyActiveUntil;
                UUID var37 = var4;
                var24.remove(var37);
                this.cancel();
            }
        };
        BlissPlugin var18 = this.plugin;
        var8.runTaskTimer(var18, 0L, 5L);
    }

    private static void drawBountyLine(Player var0, Player var1, int var2) {
        Location var22 = var0.getEyeLocation().add(0.0, -0.7, 0.0);
        Vector var30 = var1.getEyeLocation().toVector();
        Vector var48 = var22.toVector();
        Vector var23 = var30.subtract(var48);
        if (var23.lengthSquared() != 0.0) {
            var23.normalize().multiply(0.2);
            Location var24 = var22.clone();

            for (int var25 = 0; var25 < 35; var25++) {
                World var44 = var0.getWorld();
                Particle var52 = Particle.DUST;
                DustOptions var20 = DUST_BOUNTY;
                Particles.spawn(var44, var52, var24, 1, 0.0, 0.0, 0.0, 0.0, var20, true, 1748031581);
                var24.add(var23);
            }
        }
    }

    private static boolean headOf(ItemStack var0, UUID var1, int var2) {
        Material var7 = var0.getType();
        Material var4 = Material.PLAYER_HEAD;
        if (var7 != var4) {
            return false;
        } else {
            if (var0.getItemMeta() instanceof SkullMeta var5 && var5.getOwningPlayer() != null) {
                UUID var22 = var5.getOwningPlayer().getUniqueId();
                return var1.equals(var22);
            }

            return false;
        }
    }

    private static boolean matchesOriginalOwner(ItemStack var0, String var1, int var2) {
        ItemMeta var6 = var0.getItemMeta();
        if (var6 == null) {
            return false;
        } else {
            PersistentDataContainer var11 = var6.getPersistentDataContainer();
            NamespacedKey var4 = ITEMTAG_ORIGINAL_OWNER;
            PersistentDataType var5 = PersistentDataType.STRING;
            String var7 = (String)var11.get(var4, var5);
            boolean var17;
            if (var7 != null && var7.equals(var1)) {
                var17 = true;
            } else {
                var17 = false;
            }

            return var17;
        }
    }

    private static boolean isSwordOrAxe(Material var0, int var1) {
        return switch (var0) {
            case WOODEN_SWORD, STONE_SWORD, IRON_SWORD, GOLDEN_SWORD, DIAMOND_SWORD, NETHERITE_SWORD, WOODEN_AXE, STONE_AXE, IRON_AXE, GOLDEN_AXE, DIAMOND_AXE, NETHERITE_AXE-> true;
            default -> false;
        };
    }

    private static String formatRemaining(long var0, int var2) {
        long var8 = System.currentTimeMillis();
        if (var0 <= var8) {
            return "Ready!";
        } else {
            int var10 = (int)((var0 - var8 + 999L) / 1000L);
            int var11 = var10 / 60;
            int var12 = var10 % 60;
            if (var11 == 0) {
                return var12 + "s";
            } else {
                return var12 == 0 ? var11 + "m" : var11 + "m " + var12 + "s";
            }
        }
    }

    static {
        TextComponent var5 = Component.text("Select Target");
        NamedTextColor var1 = NamedTextColor.DARK_GRAY;
        SELECT_TARGET_TITLE = var5.color(var1);
        TextComponent var8 = Component.text("Place Items Belonging To Target");
        NamedTextColor var13 = NamedTextColor.DARK_GRAY;
        PLACE_ITEMS_TITLE = var8.color(var13);
        Color var14 = Color.fromRGB(241, 3, 3);
        DustOptions var12 = new DustOptions(var14, 1.0F);
        DUST_BOUNTY = var12;
    }

    private static final class PlaceItemsHolder implements InventoryHolder {
        final UUID owner;

        PlaceItemsHolder(UUID var1, int var2) {
            this.owner = var1;
        }

        public Inventory getInventory() {
            return null;
        }
    }

    private static final class SelectTargetHolder implements InventoryHolder {
        final UUID owner;

        SelectTargetHolder(UUID var1, int var2) {
            this.owner = var1;
        }

        public Inventory getInventory() {
            return null;
        }
    }
}
