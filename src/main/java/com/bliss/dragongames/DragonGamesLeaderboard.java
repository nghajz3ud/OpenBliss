package com.bliss.dragongames;

import com.bliss.BlissPlugin;
import com.bliss.villager.Text;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.TextDisplay.TextAlignment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

public final class DragonGamesLeaderboard implements Listener {
    static final String TAG = "BlissDGLeaderboard";
    static final int PAGE_SIZE = 5;
    static final double LIFT = 2.0;
    private static final Color BACKGROUND = Color.fromARGB(64, 0, 0, 0);
    private final BlissPlugin plugin;
    private final DragonGamesManager games;
    private TextDisplay display;
    private Interaction hitbox;
    private int page;
    private boolean warnedNoPedestal;

    DragonGamesLeaderboard(BlissPlugin var1, DragonGamesManager var2, int var3) {
        this.plugin = var1;
        this.games = var2;
    }

    private Location anchor(int var1) {
        Location var22;
        if (this.plugin.pedestalState(590646643) == null) {
            var22 = null;
        } else {
            var22 = this.plugin.pedestalState(590646643).mainLoc(15453813);
        }

        if (var22 != null && var22.getWorld() != null) {
            World var23 = var22.getWorld();
            double var27 = (double)var22.getBlockX() + 0.5;
            double var33 = (double)var22.getBlockY() + 2.0;
            double var39 = (double)var22.getBlockZ() + 0.5;
            return new Location(var23, var27, var33, var39);
        } else {
            return null;
        }
    }

    void tick(int var1) {
        if (!this.games.isRunning(320515049)) {
            this.remove(329989465);
        } else {
            if (this.display == null || !this.display.isValid() || this.hitbox == null || !this.hitbox.isValid()) {
                this.build(371214199);
            }

            this.refresh(1836364559);
        }
    }

    private void build(int var1) {
        this.remove(329989465);
        Location var11 = this.anchor(1239580682);
        if (var11 == null) {
            if (!this.warnedNoPedestal) {
                this.warnedNoPedestal = true;
                this.plugin.getLogger().warning("Dragon Games: no pedestal is set - no leaderboard hologram. Use /bliss pedestal set.");
            }
        } else {
            World var12 = var11.getWorld();
            int var46 = var11.getBlockX() >> 4;
            int var56 = var11.getBlockZ() >> 4;
            if (var12.isChunkLoaded(var46, var56)) {
                for (Entity var14 : var12.getEntities()) {
                    if (var14.getScoreboardTags().contains("BlissDGLeaderboard")) {
                        var14.remove();
                    }
                }

                Consumer<TextDisplay> var6 = var0 -> {
                    var0.setPersistent(false);
                    var0.addScoreboardTag("BlissDGLeaderboard");
                    Billboard var15 = Billboard.CENTER;
                    var0.setBillboard(var15);
                    var0.setShadowed(true);
                    var0.setSeeThrough(false);
                    var0.setDefaultBackground(false);
                    Color var19 = BACKGROUND;
                    var0.setBackgroundColor(var19);
                    TextAlignment var20 = TextAlignment.CENTER;
                    var0.setAlignment(var20);
                    Brightness var21 = new Brightness(15, 15);
                    var0.setBrightness(var21);
                };
                TextDisplay var49 = (TextDisplay)var12.spawn(var11, TextDisplay.class, var6);
                this.display = var49;
                Location var60 = var11.clone().add(0.0, -0.05, 0.0);
                Consumer<Interaction> var65 = var0 -> {
                    var0.setPersistent(false);
                    var0.addScoreboardTag("BlissDGLeaderboard");
                    var0.setInteractionWidth(1.9F);
                    var0.setInteractionHeight(1.8F);
                    var0.setResponsive(true);
                };
                Interaction var52 = (Interaction)var12.spawn(var60, Interaction.class, var65);
                this.hitbox = var52;
                this.refresh(1836364559);
            }
        }
    }

    void remove(int var1) {
        if (this.display != null) {
            this.display.remove();
            this.display = null;
        }

        if (this.hitbox != null) {
            this.hitbox.remove();
            this.hitbox = null;
        }

        this.page = 0;
    }

    private List<UUID> standings(int var1) {
        Set<UUID> var10 = this.games.participantIds(1453258644);
        ArrayList<UUID> var2 = new ArrayList<>(var10);
        Comparator<UUID> var8 = (var1x, var2x) -> {
            DragonGamesManager var6 = this.games;
            int var7 = var6.points(var2x, 77304185);
            DragonGamesManager var10x = this.games;
            int var11 = var10x.points(var1x, 77304185);
            return Integer.compare(var7, var11);
        };
        var2.sort(var8);
        return var2;
    }

    private void refresh(int var1) {
        if (this.display != null && this.display.isValid()) {
            List var7 = this.standings(714165426);
            int var51 = (var7.size() + 5 - 1) / 5;
            int var8 = Math.max(1, var51);
            int var21 = this.page;
            if (var21 >= var8) {
                int var54 = var8 - 1;
                this.page = var54;
            }

            if (this.page < 0) {
                this.page = 0;
            }

            Component var9 = Text.legacy("<##FFD773>&lᴅʀᴀɢᴏɴ ɢᴀᴍᴇꜱ ʟᴇᴀᴅᴇʀʙᴏᴀʀᴅ", 373192400);
            int var10 = this.page * 5;

            while (true) {
                int var58 = var7.size();
                int var82 = (this.page + 1) * 5;
                int var59 = Math.min(var58, var82);
                if (var10 >= var59) {
                    if (var7.isEmpty()) {
                        TextComponent var60 = Component.newline();
                        Component var35 = var9.append(var60);
                        Component var62 = Text.legacy("&7nobody is standing", 373192400);
                        var9 = var35.append(var62);
                    }

                    TextComponent var63 = Component.newline();
                    Component var38 = var9.append(var63);
                    int var66 = this.page + 1;
                    String var90;
                    if (var8 > 1) {
                        var90 = " &8- right-click to flip";
                    } else {
                        var90 = "";
                    }

                    Component var68 = Text.legacy("&7Page " + var66 + "/" + var8 + var90, 373192400);
                    Component var95 = var38.append(var68);
                    TextDisplay var41 = this.display;
                    var41.text(var95);
                    return;
                }

                UUID var11 = (UUID)var7.get(var10);
                TextComponent var71 = Component.newline();
                Component var46 = var9.append(var71);
                int var73 = var10 + 1;
                String var87 = name(var11, 1590695612);
                DragonGamesManager var92 = this.games;
                int var93 = var92.points(var11, 77304185);
                Component var75 = Text.legacy("&e#" + var73 + " &f" + var87 + " &7- &6$" + var93, 373192400);
                var9 = var46.append(var75);
                var10++;
            }
        }
    }

    private static String name(UUID var0, int var1) {
        Player var3 = Bukkit.getPlayer(var0);
        if (var3 != null) {
            return var3.getName();
        } else {
            String var4 = Bukkit.getOfflinePlayer(var0).getName();
            String var11;
            if (var4 == null) {
                var11 = "???";
            } else {
                var11 = var4;
            }

            return var11;
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent var1) {
        EquipmentSlot var8 = var1.getHand();
        EquipmentSlot var3 = EquipmentSlot.HAND;
        if (var8 == var3) {
            if (var1.getRightClicked().getScoreboardTags().contains("BlissDGLeaderboard")) {
                var1.setCancelled(true);
                int var27 = (this.standings(714165426).size() + 5 - 1) / 5;
                int var7 = Math.max(1, var27);
                int var30 = this.page + 1;
                int var31 = var30 % var7;
                this.page = var31;
                this.refresh(1836364559);
            }
        }
    }
}
