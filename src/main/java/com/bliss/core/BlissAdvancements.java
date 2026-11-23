package com.bliss.core;

import com.bliss.BlissPlugin;
import com.bliss.items.BlissCoreItems;
import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.UltimateAdvancementAPI;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.database.TeamProgression;
import com.fren_gor.ultimateAdvancementAPI.visibilities.IVisibility;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

public final class BlissAdvancements implements Listener {
    private static final String TAB = "smp";
    private static final String BACKGROUND = "textures/block/stone.png";
    private static final String[][] ENTRIES;
    private final BlissPlugin plugin;
    private final BlissCoreItems coreItems;
    private UltimateAdvancementAPI api;
    private AdvancementTab tab;
    private final Map<String, Advancement> byId;
    private boolean ready;

    public BlissAdvancements(BlissPlugin var1, BlissCoreItems var2, int var3) {
        HashMap var5 = new HashMap();
        this.byId = var5;
        this.plugin = var1;
        this.coreItems = var2;
    }

    // $VF: Handled exception range with multiple entry points by splitting it
    // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
    public void register(int var1) {
        try {
            UltimateAdvancementAPI var84 = UltimateAdvancementAPI.getInstance(this.plugin);
            this.api = var84;
        } catch (Throwable var155) {
            Logger var82 = this.plugin.getLogger();
            String var109 = "UltimateAdvancementAPI not available — Bliss SMP advancement tab disabled. Install UltimateAdvancementAPI 2.8.0 to enable it. ("
                + var155.getMessage()
                + ")";
            var82.warning(var109);
            return;
        }

        Throwable var10000;
        label79: {
            try {
                if (this.api.isAdvancementTabRegistered("smp")) {
                    this.api.unregisterAdvancementTab("smp");
                }
            } catch (Throwable var161) {
                var10000 = var161;
                boolean var10001 = false;
                break label79;
            }

            RootAdvancement var14;
            ArrayList var15;
            String[][] var16;
            int var17;
            int var18;
            try {
                AdvancementTab var89 = this.api.createAdvancementTab("smp");
                this.tab = var89;
                String var5 = "__root__";
                ItemStack var111 = this.icon(var5, 653376273);
                String var123 = "Bliss SMP";
                AdvancementFrameType var6 = AdvancementFrameType.TASK;
                String var11 = "The Bliss SMP achievements.";
                List var145 = List.of(var11);
                AdvancementDisplay var31 = new AdvancementDisplay(var111, var123, var6, false, false, 0.0F, 4.0F, var145);
                AdvancementTab var113 = this.tab;
                String var124 = "root";
                String var139 = "textures/block/stone.png";
                RootAdvancement var32 = new RootAdvancement(var113, var124, var31, var139);
                var14 = var32;
                Map var34 = this.byId;
                String var90 = "root";
                var34.put(var90, var32);
                ArrayList var36 = new ArrayList();
                var15 = var36;
                var16 = ENTRIES;
                var17 = var16.length;
                var18 = 0;
            } catch (Throwable var159) {
                var10000 = var159;
                boolean var164 = false;
                break label79;
            }

            while (true) {
                String[] var19;
                Object var20;
                try {
                    if (var18 >= var17) {
                        break;
                    }

                    var19 = var16[var18];
                    Map var61 = this.byId;
                    String var101 = var19[4];
                    var20 = (Advancement)var61.get(var101);
                    if (var20 == null) {
                        var20 = var14;
                    }
                } catch (Throwable var160) {
                    var10000 = var160;
                    boolean var165 = false;
                    break label79;
                }

                try {
                    float var21 = Float.parseFloat(var19[5]);
                    float var22 = Float.parseFloat(var19[6]);
                    String var126 = var19[0];
                    ItemStack var118 = this.icon(var126, 653376273);
                    String var128 = var19[1];
                    AdvancementFrameType var137 = frame(var19[3], 1729435701);
                    List var148 = List.of(var19[2]);
                    AdvancementDisplay var72 = new AdvancementDisplay(var118, var128, var137, true, false, var21, var22, var148);
                    String var120 = var19[0];
                    BlissAdvancements.ShownAdvancement var73 = new BlissAdvancements.ShownAdvancement(var120, var72, (Advancement)var20);
                    Map var75 = this.byId;
                    String var105 = var19[0];
                    var75.put(var105, var73);
                    var15.add(var73);
                    var18++;
                } catch (Throwable var158) {
                    var10000 = var158;
                    boolean var166 = false;
                    break label79;
                }
            }

            Iterator var151;
            try {
                AdvancementTab var43 = this.tab;
                HashSet var115 = new HashSet(var15);
                var43.registerAdvancements(var14, var115);
                this.ready = true;
                var151 = Bukkit.getOnlinePlayers().iterator();
            } catch (Throwable var157) {
                var10000 = var157;
                boolean var167 = false;
                break label79;
            }

            while (true) {
                try {
                    if (!var151.hasNext()) {
                        return;
                    }

                    Player var152 = (Player)var151.next();
                    this.showFor(var152, 372636757);
                } catch (Throwable var156) {
                    var10000 = var156;
                    boolean var168 = false;
                    break;
                }
            }
        }

        Throwable var149 = var10000;
        Logger var56 = this.plugin.getLogger();
        String var97 = "Failed to build the Bliss SMP advancement tab: " + var149.getMessage();
        var56.warning(var97);
        this.ready = false;
    }

    public void stop(int var1) {
        if (this.api != null && this.ready) {
            try {
                this.api.unregisterAdvancementTab("smp");
            } catch (Throwable var2) {
            }
        }

        this.ready = false;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent var1) {
        if (this.ready) {
            Player var6 = var1.getPlayer();
            BukkitScheduler var10 = Bukkit.getScheduler();
            BlissPlugin var12 = this.plugin;
            Runnable var13 = () -> this.showFor(var6, 372636757);
            var10.runTaskLater(var12, var13, 40L);
        }
    }

    private void showFor(Player var1, int var2) {
        if (this.ready && this.tab != null && var1.isOnline()) {
            try {
                AdvancementTab var12 = this.tab;
                var12.showTab(var1);
                AdvancementTab var14 = this.tab;
                var14.grantRootAdvancement(var1);
            } catch (Throwable var17) {
            }
        }
    }

    public Boolean isGranted(Player var1, String var2, int var3) {
        if (!this.ready) {
            return null;
        } else {
            Map var10 = this.byId;
            Advancement var6 = (Advancement)var10.get(var2);
            if (var6 == null) {
                return null;
            } else {
                try {
                    return var6.isGranted(var1);
                } catch (Throwable var22) {
                    return null;
                }
            }
        }
    }

    public void grant(Player var1, String var2, int var3) {
        if (this.ready) {
            Map var10 = this.byId;
            Advancement var6 = (Advancement)var10.get(var2);
            if (var6 != null) {
                try {
                    if (!var6.isGranted(var1)) {
                        var6.grant(var1);
                    }
                } catch (Throwable var20) {
                }
            }
        }
    }

    private ItemStack icon(String var1, int var2) {
        return switch (var1) {
            case "shattered" -> {
                BlissCoreItems var25 = this.coreItems;
                GemType var85 = GemType.STRENGTH;
                yield var25.gemItem(var85, false, 1, 256965471);
            }
            case "zip_away" -> {
                BlissCoreItems var33 = this.coreItems;
                GemType var87 = GemType.FLUX;
                yield var33.gemItem(var87, true, 11, 256965471);
            }
            case "piercing_precision" -> {
                BlissCoreItems var46 = this.coreItems;
                GemType var92 = GemType.ASTRA;
                yield var46.gemItem(var92, true, 11, 256965471);
            }
            case "saved_by_dice" -> {
                BlissCoreItems var38 = this.coreItems;
                GemType var89 = GemType.ASTRA;
                yield var38.gemItem(var89, true, 11, 256965471);
            }
            case "overflowing" -> {
                BlissCoreItems var44 = this.coreItems;
                GemType var91 = GemType.PUFF;
                yield var44.gemItem(var91, false, 11, 256965471);
            }
            case "almost_fell_from_grace" -> {
                BlissCoreItems var21 = this.coreItems;
                GemType var84 = GemType.PUFF;
                yield var21.gemItem(var84, true, 11, 256965471);
            }
            case "good_as_new" -> this.coreItems.repairKit(1231295697);
            case "reawakening" -> this.coreItems.restoration(1610991893);
            case "rabbit_season" -> {
                BlissCoreItems var19 = this.coreItems;
                GemType var83 = GemType.STRENGTH;
                yield var19.gemItem(var83, true, 11, 256965471);
            }
            case "time_for_a_change" -> this.coreItems.trader(662298763);
            case "the_next_level" -> this.coreItems.upgrader(2125662274);
            case "overcharged" -> {
                BlissCoreItems var42 = this.coreItems;
                GemType var90 = GemType.FLUX;
                yield var42.gemItem(var90, true, 11, 256965471);
            }
            case "boundary_break" -> {
                BlissCoreItems var36 = this.coreItems;
                GemType var88 = GemType.WEALTH;
                yield var36.gemItem(var88, true, 11, 256965471);
            }
            default -> {
                BlissCoreItems var27 = this.coreItems;
                GemType var86 = GemType.ASTRA;
                yield var27.gemItem(var86, true, 11, 256965471);
            }
        };
    }

    private static AdvancementFrameType frame(String var0, int var1) {
        return switch (var0) {
            case "goal" -> AdvancementFrameType.GOAL;
            case "challenge" -> AdvancementFrameType.CHALLENGE;
            default -> AdvancementFrameType.TASK;
        };
    }

    static {
        String[][] var5 = new String[13][];
        String[] var18 = new String[7];
        String var4 = "shattered";
        var18[0] = var4;
        String var133 = "Shattered";
        var18[1] = var133;
        String var134 = "Reach the Broken energy stage";
        var18[2] = var134;
        String var135 = "task";
        var18[3] = var135;
        String var136 = "root";
        var18[4] = var136;
        String var137 = "1";
        var18[5] = var137;
        String var138 = "0";
        var18[6] = var138;
        var5[0] = var18;
        String[] var20 = new String[7];
        String var139 = "piercing_precision";
        var20[0] = var139;
        String var140 = "Piercing Precision";
        var20[1] = var140;
        String var141 = "Hit a player with 5 consecutive Phantom Daggers";
        var20[2] = var141;
        String var142 = "task";
        var20[3] = var142;
        String var143 = "root";
        var20[4] = var143;
        String var144 = "1";
        var20[5] = var144;
        String var145 = "1";
        var20[6] = var145;
        var5[1] = var20;
        String[] var22 = new String[7];
        String var146 = "zip_away";
        var22[0] = var146;
        String var147 = "Zip Away";
        var22[1] = var147;
        String var148 = "Travel across 1,914 copper blocks using Conduction";
        var22[2] = var148;
        String var149 = "task";
        var22[3] = var149;
        String var150 = "root";
        var22[4] = var150;
        String var151 = "1";
        var22[5] = var151;
        String var152 = "2";
        var22[6] = var152;
        var5[2] = var22;
        String[] var24 = new String[7];
        String var153 = "rabbit_season";
        var24[0] = var153;
        String var154 = "It's Rabbit Season";
        var24[1] = var154;
        String var155 = "Track the same player with Shadow Stalker 17 times";
        var24[2] = var155;
        String var156 = "task";
        var24[3] = var156;
        String var157 = "root";
        var24[4] = var157;
        String var158 = "1";
        var24[5] = var158;
        String var159 = "3";
        var24[6] = var159;
        var5[3] = var24;
        String[] var26 = new String[7];
        String var160 = "boundary_break";
        var26[0] = var160;
        String var161 = "Boundary Break";
        var26[1] = var161;
        String var162 = "Boost enchantments beyond vanilla levels using Amplification";
        var26[2] = var162;
        String var163 = "challenge";
        var26[3] = var163;
        String var164 = "root";
        var26[4] = var164;
        String var165 = "1";
        var26[5] = var165;
        String var166 = "4";
        var26[6] = var166;
        var5[4] = var26;
        String[] var28 = new String[7];
        String var167 = "almost_fell_from_grace";
        var28[0] = var167;
        String var168 = "Almost Fell From Grace";
        var28[1] = var168;
        String var169 = "Prevent 30,000 points of fall damage with Puff Gem";
        var28[2] = var169;
        String var170 = "task";
        var28[3] = var170;
        String var171 = "root";
        var28[4] = var171;
        String var172 = "1";
        var28[5] = var172;
        String var173 = "5";
        var28[6] = var173;
        var5[5] = var28;
        String[] var30 = new String[7];
        String var174 = "the_next_level";
        var30[0] = var174;
        String var175 = "The Next Level";
        var30[1] = var175;
        String var176 = "Use an Upgrader to obtain a Tier 2 gem";
        var30[2] = var176;
        String var177 = "task";
        var30[3] = var177;
        String var178 = "root";
        var30[4] = var178;
        String var179 = "1";
        var30[5] = var179;
        String var180 = "6";
        var30[6] = var180;
        var5[6] = var30;
        String[] var32 = new String[7];
        String var181 = "good_as_new";
        var32[0] = var181;
        String var182 = "Good As New!";
        var32[1] = var182;
        String var183 = "Draw 10 energy from the Energy Pool using a Repair Kit";
        var32[2] = var183;
        String var184 = "task";
        var32[3] = var184;
        String var185 = "root";
        var32[4] = var185;
        String var186 = "1";
        var32[5] = var186;
        String var187 = "7";
        var32[6] = var187;
        var5[7] = var32;
        String[] var34 = new String[7];
        String var188 = "overflowing";
        var34[0] = var188;
        String var189 = "Overflowing";
        var34[1] = var189;
        String var190 = "Reach the Pristine +5 energy stage";
        var34[2] = var190;
        String var191 = "goal";
        var34[3] = var191;
        String var192 = "shattered";
        var34[4] = var192;
        String var193 = "2";
        var34[5] = var193;
        String var194 = "0";
        var34[6] = var194;
        var5[8] = var34;
        String[] var36 = new String[7];
        String var195 = "saved_by_dice";
        var36[0] = var195;
        String var196 = "Saved By The Dice";
        var36[1] = var196;
        String var197 = "Avoid a fatal blow with Phasing";
        var36[2] = var197;
        String var198 = "task";
        var36[3] = var198;
        String var199 = "piercing_precision";
        var36[4] = var199;
        String var200 = "2";
        var36[5] = var200;
        String var201 = "1";
        var36[6] = var201;
        var5[9] = var36;
        String[] var38 = new String[7];
        String var202 = "overcharged";
        var38[0] = var202;
        String var203 = "Overcharged";
        var38[1] = var203;
        String var204 = "Push a Flux Gem past 100% charge";
        var38[2] = var204;
        String var205 = "task";
        var38[3] = var205;
        String var206 = "zip_away";
        var38[4] = var206;
        String var207 = "2";
        var38[5] = var207;
        String var208 = "2";
        var38[6] = var208;
        var5[10] = var38;
        String[] var40 = new String[7];
        String var209 = "time_for_a_change";
        var40[0] = var209;
        String var210 = "Time For A Change";
        var40[1] = var210;
        String var211 = "Use a Trader to switch gem types";
        var40[2] = var211;
        String var212 = "task";
        var40[3] = var212;
        String var213 = "the_next_level";
        var40[4] = var213;
        String var214 = "2";
        var40[5] = var214;
        String var215 = "6";
        var40[6] = var215;
        var5[11] = var40;
        String[] var42 = new String[7];
        String var216 = "reawakening";
        var42[0] = var216;
        String var217 = "Reawakening";
        var42[1] = var217;
        String var218 = "Successfully complete a Restoration Ritual";
        var42[2] = var218;
        String var219 = "goal";
        var42[3] = var219;
        String var220 = "good_as_new";
        var42[4] = var220;
        String var221 = "2";
        var42[5] = var221;
        String var222 = "7";
        var42[6] = var222;
        var5[12] = var42;
        ENTRIES = var5;
    }

    public interface AlwaysVisible extends IVisibility {
        default boolean isVisible(Advancement var1, TeamProgression var2) {
            return true;
        }
    }

    private static final class ShownAdvancement extends BaseAdvancement implements BlissAdvancements.AlwaysVisible {
        ShownAdvancement(String var1, AdvancementDisplay var2, Advancement var3) {
            super(var1, var2, var3);
        }
    }
}
