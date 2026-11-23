package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.EnergyScale;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.Particles;
import com.bliss.core.PlayerDataStore;
import com.bliss.core.TagStore;
import com.bliss.core.TrustStore;
import com.bliss.goldgem.GoldGem;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

public final class FluxKineticOverdriveListener implements Listener {
    private static final long PAIR_DEACTIVATE_TICKS = 540L;
    private static final long SOLO_DEACTIVATE_TICKS = 600L;
    private final BlissPlugin plugin;
    private final FluxGemListener flux;
    private final PlayerDataStore players;
    private final TagStore tags;
    private final Map<UUID, Integer> activationGeneration;
    private final TrustStore trust;

    public FluxKineticOverdriveListener(BlissPlugin var1, FluxGemListener var2, PlayerDataStore var3, TagStore var4, int var5) {
        HashMap var7 = new HashMap();
        this.activationGeneration = var7;
        this.plugin = var1;
        this.flux = var2;
        this.players = var3;
        this.tags = var4;
        TrustStore var19 = var1.trust(310732214);
        this.trust = var19;
    }

    @EventHandler
    public void onRightClickEntity(PlayerInteractEntityEvent var1) {
        EquipmentSlot var24 = var1.getHand();
        EquipmentSlot var3 = EquipmentSlot.HAND;
        if (var24 == var3) {
            Player var16 = var1.getPlayer();
            UUID var17 = var16.getUniqueId();
            if (this.sharedGate(var16, var17, 964048216)) {
                EntityClickGuard.mark(var17, 1276880015);
                Entity var18 = var1.getRightClicked();
                UUID var19 = var18.getUniqueId();
                if (var18 instanceof Player var20) {
                    TrustStore var41 = this.trust;
                    UUID var111 = var20.getUniqueId();
                    if (var41.isTrusted(var17, var111, 1736364606)) {
                        Component var82 = Text.legacy("&b\ud83d\udd2e &fYou cannot cast negative powers on allies!", 373192400);
                        var16.sendMessage(var82);
                        return;
                    }
                }

                long var150 = System.currentTimeMillis();
                PlayerDataStore var47 = this.players;
                int var49 = var47.get(var17, 1264010596).energy(1160853318);
                FluxGemListener var85 = this.flux;
                boolean var86 = var85.hasDragonEgg(var17, 1897338785);
                long var21 = pairUpCooldownSeconds(var49, var86, 1475749136);
                Map var54 = this.flux.cd3Expiry;
                String var7 = "flux";
                String var8 = "kinetic-overdrive-pair";
                long var148 = var21 * 1000L;
                long var135 = GemTuning.cd(var7, var8, 300.0, var148, 1621983041);
                PlayerDataStore var145 = this.players;
                int var147 = var145.get(var17, 1264010596).energy(1160853318);
                long var137 = EnergyScale.cd(var135, var147, 907518618);
                Long var117 = var150 + var137;
                var54.put(var17, var117);
                Component var91 = Text.legacy("&b\ud83d\udd2e You used &#5ED7FFKinetic Overdrive &bon " + targetName(var18, 1052862196), 373192400);
                var16.sendMessage(var91);
                Map var59 = this.flux.kinOvMain;
                Boolean var119 = true;
                var59.put(var17, var119);
                Map var63 = this.flux.kinOvAlt;
                Boolean var121 = true;
                var63.put(var19, var121);
                Map var67 = this.flux.kinOvMainCounter;
                Integer var123 = 0;
                var67.put(var17, var123);
                if (var18 instanceof Player var23) {
                    Component var98 = Text.legacy("&b\ud83d\udd2e You have been affected with " + var16.getName() + "'s &#5ED7FFKinetic Overdrive!", 373192400);
                    var23.sendMessage(var98);
                    Map<UUID, Double> var101 = this.flux.storedDamage;
                    Double var127 = 0.0;
                    double var104 = var101.getOrDefault(var17, var127);
                    if (scramblePlayerInventory(var23, var104, 1097964596)) {
                        Component var107 = Text.legacy("&b\ud83d\udd2e &7Your inventory has been &#5ED7FFscrambled&7!", 373192400);
                        var23.sendMessage(var107);
                    }
                }

                this.playPairUpTrail(var16, var18, 463953242);
                String var128 = "flux";
                String var139 = "kinetic-overdrive-pair";
                long var133 = Math.round(GemTuning.duration(var128, var139, 27.0, 812730832) * 20.0);
                this.scheduleDeactivate(var17, var19, var133, 1301399757);
            }
        }
    }

    private static boolean scramblePlayerInventory(Player var0, double var1, int var3) {
        PlayerInventory var8 = var0.getInventory();
        ArrayList var17 = new ArrayList(36);
        ArrayList var9 = var17;

        for (int var10 = 0; var10 < 36; var10++) {
            ItemStack var11 = var8.getItem(var10);
            if (var11 != null) {
                Material var24 = var11.getType();
                Material var69 = Material.AIR;
                if (var24 != var69) {
                    Integer var71 = var10;
                    var9.add(var71);
                }
            }
        }

        if (var9.isEmpty()) {
            return false;
        } else {
            int var100 = (int)Math.round(Math.max(0.0, var1));
            if (var100 <= 0) {
                return false;
            } else {
                int var73 = var9.size();
                int var101 = Math.min(var100, var73);
                Collections.shuffle(var9);
                List var89 = var9.subList(0, var101);
                ArrayList<Integer> var41 = new ArrayList<>((List<Integer>)var89);
                ArrayList<Integer> var102 = var41;
                ArrayList<ItemStack> var42 = new ArrayList<>(var101);
                ArrayList var12 = var42;

                for (int var14 : var41) {
                    ItemStack var76 = var8.getItem(var14).clone();
                    var12.add(var76);
                    var8.setItem(var14, null);
                }

                ArrayList var54 = new ArrayList(var41);
                ArrayList var103 = var54;
                Collections.shuffle(var54);
                boolean var104 = false;

                for (int var15 = 0; var15 < var101; var15++) {
                    int var82 = (Integer)var103.get(var15);
                    ItemStack var97 = (ItemStack)var12.get(var15);
                    var8.setItem(var82, var97);
                    Integer var62 = (Integer)var103.get(var15);
                    Object var85 = var102.get(var15);
                    if (!var62.equals(var85)) {
                        var104 = true;
                    }
                }

                return var104;
            }
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent var1) {
        EquipmentSlot var21 = var1.getHand();
        EquipmentSlot var3 = EquipmentSlot.HAND;
        if (var21 == var3) {
            Action var23 = var1.getAction();
            Action var57 = Action.RIGHT_CLICK_AIR;
            if (var23 != var57) {
                Action var25 = var1.getAction();
                Action var58 = Action.RIGHT_CLICK_BLOCK;
                if (var25 != var58) {
                    return;
                }
            }

            Player var15 = var1.getPlayer();
            UUID var16 = var15.getUniqueId();
            if (!EntityClickGuard.recentlyClickedEntity(var16, 1923725760)) {
                if (this.sharedGate(var15, var16, 964048216)) {
                    long var17 = System.currentTimeMillis();
                    PlayerDataStore var37 = this.players;
                    int var39 = var37.get(var16, 1264010596).energy(1160853318);
                    FluxGemListener var62 = this.flux;
                    boolean var63 = var62.hasDragonEgg(var16, 1897338785);
                    long var19 = soloCooldownSeconds(var39, var63, 925590606);
                    Map var44 = this.flux.cd3Expiry;
                    String var6 = "flux";
                    String var7 = "kinetic-overdrive-solo";
                    long var102 = var19 * 1000L;
                    long var89 = GemTuning.cd(var6, var7, 75.0, var102, 1621983041);
                    PlayerDataStore var99 = this.players;
                    int var101 = var99.get(var16, 1264010596).energy(1160853318);
                    long var91 = EnergyScale.cd(var89, var101, 907518618);
                    Long var77 = var17 + var91;
                    var44.put(var16, var77);
                    Component var66 = Text.legacy("&b\ud83d\udd2e You have activated Kinetic Overdrive!", 373192400);
                    var15.sendMessage(var66);
                    Map var49 = this.flux.kinOvMain;
                    Boolean var79 = true;
                    var49.put(var16, var79);
                    Map var53 = this.flux.kinOvMainCounter;
                    Integer var81 = 0;
                    var53.put(var16, var81);
                    Location var70 = var15.getLocation();
                    this.playSoloCircles(var70, 945744391);
                    String var5 = "flux";
                    String var93 = "kinetic-overdrive-solo";
                    long var87 = Math.round(GemTuning.duration(var5, var93, 30.0, 812730832) * 20.0);
                    this.scheduleDeactivate(var16, null, var87, 1301399757);
                }
            }
        }
    }

    private boolean sharedGate(Player var1, UUID var2, int var3) {
        TagStore var12 = this.tags;
        if (var12.isTagged(var2, 995676415)) {
            return false;
        } else {
            PlayerDataStore var16 = this.players;
            if (!var16.get(var2, 1264010596).canUsePowers(1616892701)) {
                return false;
            } else {
                ItemStack var9 = var1.getInventory().getItemInMainHand();
                GemType var47 = GemType.FLUX;
                boolean var10 = GoldGem.isWieldingSoulMain(var1, var47, 244096655);
                int var27 = BlissCoreItems.gemIdOf(var9, 382441079);
                int var49 = GemType.FLUX.id();
                if (var27 != var49 && !var10) {
                    return false;
                } else if (BlissCoreItems.tierOf(var9, 1633828214) != 2 && !var10) {
                    return false;
                } else {
                    Map var36 = this.flux.cd3Expiry;
                    Long var11 = (Long)var36.get(var2);
                    if (var11 != null) {
                        long var41 = var11;
                        long var6 = System.currentTimeMillis();
                        if (var41 > var6) {
                            return false;
                        }
                    }

                    return true;
                }
            }
        }
    }

    private static String targetName(Entity var0, int var1) {
        if (var0 instanceof Player) {
            return ((Player)var0).getName();
        } else {
            EntityType var20 = var0.getType();
            String var19;
            if (var0.getCustomName() != null) {
                var19 = var0.getCustomName();
            } else {
                var19 = var20.name().toLowerCase().replace('_', ' ');
            }

            return var19;
        }
    }

    private void scheduleDeactivate(UUID var1, UUID var2, long var3, int var5) {
        Map<UUID, Integer> var13 = this.activationGeneration;
        Integer var27 = 0;
        int var12 = var13.getOrDefault(var1, var27) + 1;
        Map var19 = this.activationGeneration;
        Integer var29 = var12;
        var19.put(var1, var29);
        BukkitScheduler var21 = Bukkit.getScheduler();
        BlissPlugin var26 = this.plugin;
        Runnable var31 = () -> {
            Map<UUID, Integer> var8 = this.activationGeneration;
            Integer var44 = 0;
            int var11 = var8.getOrDefault(var1, var44);
            if (var11 == var12) {
                Map var14 = this.flux.kinOvMain;
                Boolean var46 = false;
                var14.put(var1, var46);
                if (var2 != null) {
                    Map var19x = this.flux.kinOvAlt;
                    Boolean var48 = false;
                    var19x.put(var2, var48);
                }

                Map var23 = this.flux.kinOvMainCounter;
                var23.remove(var1);
                Server var27x = this.plugin.getServer();
                Player var7 = var27x.getPlayer(var1);
                if (var7 != null && var7.isOnline()) {
                    PotionEffectType var42 = PotionEffectType.HASTE;
                    GrantedEffect.strip(var7, var42, 553187889);
                }

                Map var35 = this.flux.kinOvMining;
                Integer var50 = 0;
                var35.put(var1, var50);
            }
        };
        var21.runTaskLater(var26, var31, var3);
    }

    private static long pairUpCooldownSeconds(int var0, boolean var1, int var2) {
        if (var0 >= 10) {
            long var15;
            if (var1) {
                var15 = 60L;
            } else {
                var15 = 120L;
            }

            return var15;
        } else {
            return switch (var0) {
                case 2, 3 -> {
                    if (var1) {
                        yield 210L;
                    } else {
                        yield 420L;
                    }
                }
                case 4, 5 -> {
                    if (var1) {
                        yield 170L;
                    } else {
                        yield 345L;
                    }
                }
                case 6, 7 -> {
                    if (var1) {
                        yield 150L;
                    } else {
                        yield 300L;
                    }
                }
                case 8 -> {
                    if (var1) {
                        yield 105L;
                    } else {
                        yield 210L;
                    }
                }
                case 9 -> {
                    if (var1) {
                        yield 80L;
                    } else {
                        yield 165L;
                    }
                }
                default -> {
                    if (var1) {
                        yield 210L;
                    } else {
                        yield 420L;
                    }
                }
            };
        }
    }

    private static long soloCooldownSeconds(int var0, boolean var1, int var2) {
        if (var0 > 5) {
            long var14;
            if (var1) {
                var14 = 40L;
            } else {
                var14 = 75L;
            }

            return var14;
        } else {
            return switch (var0) {
                case 2 -> {
                    if (var1) {
                        yield 75L;
                    } else {
                        yield 150L;
                    }
                }
                case 3 -> {
                    if (var1) {
                        yield 60L;
                    } else {
                        yield 120L;
                    }
                }
                case 4 -> {
                    if (var1) {
                        yield 50L;
                    } else {
                        yield 105L;
                    }
                }
                case 5 -> {
                    if (var1) {
                        yield 45L;
                    } else {
                        yield 90L;
                    }
                }
                default -> {
                    if (var1) {
                        yield 75L;
                    } else {
                        yield 150L;
                    }
                }
            };
        }
    }

    private void playPairUpTrail(final Player var1, final Entity var2, int var3) {
        BukkitRunnable var4 = new BukkitRunnable() {
            int i = 0;

            public void run() {
                if (this.i < 30 && var2.isValid() && var1.isOnline()) {
                    FluxKineticOverdriveListener var18 = FluxKineticOverdriveListener.this;
                    Location var23 = var1.getEyeLocation();
                    Location var28 = var2.getLocation().add(0.0, 1.0, 0.0);
                    var18.drawTrail(var23, var28, 1502479575);
                    int var25 = this.i + 1;
                    this.i = var25;
                } else {
                    this.cancel();
                }
            }
        };
        BlissPlugin var10 = this.plugin;
        var4.runTaskTimer(var10, 0L, 2L);
    }

    private void drawTrail(Location var1, Location var2, int var3) {
        World var36 = var1.getWorld();
        World var68 = var2.getWorld();
        if (var36.equals(var68)) {
            int var74 = (int)(var1.distance(var2) / 0.5);
            int var25 = Math.max(1, var74);
            Color var77 = Color.fromRGB(94, 215, 255);
            DustOptions var43 = new DustOptions(var77, 1.5F);
            DustOptions var26 = var43;

            for (int var27 = 0; var27 <= var25; var27++) {
                double var47 = (double)var27;
                double var79 = (double)var25;
                double var28 = var47 / var79;
                double var52 = var1.getX();
                double var82 = var2.getX();
                double var107 = var1.getX();
                double var84 = var82 - var107;
                double var86 = var84 * var28;
                double var30 = var52 + var86;
                double var57 = var1.getY();
                double var89 = var2.getY();
                double var112 = var1.getY();
                double var91 = var89 - var112;
                double var93 = var91 * var28;
                double var32 = var57 + var93;
                double var62 = var1.getZ();
                double var96 = var2.getZ();
                double var117 = var1.getZ();
                double var98 = var96 - var117;
                double var100 = var98 * var28;
                double var34 = var62 + var100;
                World var67 = var1.getWorld();
                Particle var76 = Particle.DUST;
                Particles.spawn(var67, var76, var30, var32, var34, 1, 0.0, 0.0, 0.0, 0.0, var26, true, 954482185);
            }
        }
    }

    private void playSoloCircles(final Location var1, int var2) {
        BukkitRunnable var3 = new BukkitRunnable() {
            int tick = 0;

            public void run() {
                switch (this.tick) {
                    case 0:
                        FluxKineticOverdriveListener var24 = FluxKineticOverdriveListener.this;
                        Location var52 = var1;
                        var24.drawCircle(var52, 0.25, 1802729759);
                        FluxKineticOverdriveListener var26 = FluxKineticOverdriveListener.this;
                        Location var54 = var1;
                        var26.drawCircle(var54, 0.5, 1802729759);
                        break;
                    case 1:
                        FluxKineticOverdriveListener var31 = FluxKineticOverdriveListener.this;
                        Location var58 = var1;
                        var31.drawCircle(var58, 0.75, 1802729759);
                        FluxKineticOverdriveListener var33 = FluxKineticOverdriveListener.this;
                        Location var60 = var1;
                        var33.drawCircle(var60, 1.0, 1802729759);
                        break;
                    case 2:
                        FluxKineticOverdriveListener var12 = FluxKineticOverdriveListener.this;
                        Location var40 = var1;
                        var12.drawCircle(var40, 1.25, 1802729759);
                        FluxKineticOverdriveListener var14 = FluxKineticOverdriveListener.this;
                        Location var42 = var1;
                        var14.drawCircle(var42, 1.5, 1802729759);
                        break;
                    case 3:
                        FluxKineticOverdriveListener var20 = FluxKineticOverdriveListener.this;
                        Location var48 = var1;
                        var20.drawCircle(var48, 1.75, 1802729759);
                        FluxKineticOverdriveListener var22 = FluxKineticOverdriveListener.this;
                        Location var50 = var1;
                        var22.drawCircle(var50, 2.0, 1802729759);
                        break;
                    case 4:
                        FluxKineticOverdriveListener var16 = FluxKineticOverdriveListener.this;
                        Location var44 = var1;
                        var16.drawCircle(var44, 2.25, 1802729759);
                        FluxKineticOverdriveListener var18 = FluxKineticOverdriveListener.this;
                        Location var46 = var1;
                        var18.drawCircle(var46, 2.5, 1802729759);
                        break;
                    case 5:
                        FluxKineticOverdriveListener var7 = FluxKineticOverdriveListener.this;
                        Location var34 = var1;
                        var7.drawCircle(var34, 2.75, 1802729759);
                        FluxKineticOverdriveListener var9 = FluxKineticOverdriveListener.this;
                        Location var36 = var1;
                        var9.drawCircle(var36, 3.0, 1802729759);
                        break;
                    case 6:
                        FluxKineticOverdriveListener var29 = FluxKineticOverdriveListener.this;
                        Location var56 = var1;
                        var29.drawCircle(var56, 3.0, 1802729759);
                        break;
                    default:
                        this.cancel();
                        return;
                }

                int var38 = this.tick + 1;
                this.tick = var38;
            }
        };
        BlissPlugin var10 = this.plugin;
        var3.runTaskTimer(var10, 0L, 1L);
    }

    private void drawCircle(Location var1, double var2, int var4) {
        int var74 = (int)Math.floor((Math.PI * 2) * var2 / 0.15);
        int var31 = Math.max(1, var74);
        Color var78 = Color.fromRGB(94, 215, 255);
        DustOptions var46 = new DustOptions(var78, 0.9F);
        DustOptions var32 = var46;

        for (int var33 = 0; var33 < var31; var33++) {
            double var52 = (double)var33 * 360.0;
            double var82 = (double)var31;
            double var34 = Math.toRadians(var52 / var82);
            double var59 = var1.getX();
            double var98 = Math.cos(var34);
            double var86 = var2 * var98;
            double var36 = var59 + var86;
            double var64 = var1.getZ();
            double var102 = Math.sin(var34);
            double var90 = var2 * var102;
            double var38 = var64 + var90;
            World var69 = var1.getWorld();
            Particle var76 = Particle.DUST;
            double var105 = var1.getY();
            Particles.spawn(var69, var76, var36, var105, var38, 1, 0.0, 0.0, 0.0, 0.0, var32, true, 954482185);
        }
    }
}
