package com.bliss.admin;

import com.bliss.villager.Text;
import java.util.ArrayList;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;

public final class HelpBook {
    private static final String[] PAGES;

    private HelpBook() {
    }

    public static ItemStack build(int var0) {
        Material var2 = Material.WRITTEN_BOOK;
        ItemStack var1 = new ItemStack(var2);
        BookMeta var4 = (BookMeta)var1.getItemMeta();
        if (var4 != null) {
            Component var34 = Text.legacy("&1Bliss Guide", 373192400);
            var4.title(var34);
            Component var36 = Text.legacy("&5Blood", 373192400);
            var4.author(var36);
            ArrayList var18 = new ArrayList();
            ArrayList var5 = var18;

            for (String var9 : PAGES) {
                Component var40 = Text.legacy(var9, 373192400);
                var5.add(var40);
            }

            var4.pages(var5);
            var1.setItemMeta(var4);
        }

        return var1;
    }

    public static void give(Player var0, int var1) {
        PlayerInventory var8 = var0.getInventory();
        ItemStack[] var20 = new ItemStack[1];
        ItemStack var5 = build(299168599);
        var20[0] = var5;

        for (ItemStack var7 : var8.addItem(var20).values()) {
            World var18 = var0.getWorld();
            Location var22 = var0.getLocation();
            var18.dropItemNaturally(var22, var7);
        }
    }

    static {
        String[] var3 = new String[20];
        String var2 = "&0&lBLISS\n&8Player Guide\n\n&7Everything you need to\n&7know: every event and\n&7every mechanic of the\n&7Bliss SMP.\n\n&8by Blood\n\n&7Turn the page ➡";
        var3[0] = var2;
        String var23 = "&1&lGEMS & ENERGY\n\n&0You hold a &1gem&0 with its own powers.\n\n&0Each gem has &1Energy&0 (0-11). Using powers spends it, and it drains as you take damage.\n\n&0Keep your energy up!";
        var3[1] = var23;
        String var24 = "&1&lBROKEN & TIERS\n\n&0Gems have &1Tier I&0 and an upgraded &1Tier II&0.\n\n&0Low energy turns it &4Broken&0: powers lock, Tier II drops to I, and you take more damage.\n\n&0Restore with &1Bottled Energy&0 or the Pedestal.";
        var3[2] = var24;
        String var25 = "&1&lTHE GEMS  1/2\n\n&4Strength&0 - raw melee power & auras.\n&8Speed&0 - blinks, storms & flurries.\n&6Flux&0 - kinetic charge & a beam.\n&3Puff&0 - wind: dashes, jumps, soft falls.";
        var3[3] = var25;
        String var26 = "&1&lTHE GEMS  2/2\n\n&2Life&0 - healing, animals & regrowth.\n&6Wealth&0 - riches & hidden pockets.\n&5Astra&0 - souls, daggers & projection.\n&cFire&0 - meteors, embers & warmth.\n\n&8Add-ons add more (Heretic, Auratus).";
        var3[4] = var26;
        String var27 = "&1&lTRADER & UPGRADER\n\n&0Craft the &1Trader&0 head to swap your gem for a new random one.\n\n&0Craft the &1Upgrader&0 head to raise a gem to &1Tier II&0.\n\n&0See recipes with &1/recipe&0.";
        var3[5] = var27;
        String var28 = "&1&lGEM SWAP\n\n&0Right-click a player while holding a &1Trader&0 to offer a swap.\n&0They get &1[ACCEPT]&0 and &130 seconds&0.\n\n&0Gems swap blind; tier and energy stay with you.\n&0The Trader is spent only on accept.";
        var3[6] = var28;
        String var29 = "&1&lTRUST\n\n&0Your powers hurt allies unless you mark them &1trusted&0.\n\n&1/trust add <p>\n&1/trust remove <p>\n&1/trust list\n&1/trust clear\n\n&0Trusted players are safe from your auras and blasts.";
        var3[7] = var29;
        String var30 = "&1&lFLUX WATTS\n\n&0Tier II Flux: press &1Q&0 on the gem for the &1Watt Deposit&0.\n\n&0Put fuel in and close it - one a second burns, then charging starts itself.\n\n&0Non-fuel comes back.\n&1/watts&0 your total.";
        var3[8] = var30;
        String var31 = "&1&lFLUX FUEL\n\n&0Watts each:\n&1Netherite block&0 421,378\n&1Nether star&0 336,141\n&1Wither skull&0 112,047\n&1Diamond block&0 46,819\n&1Netherite ingot&0 46,819\n&1Copper block&0 17,352\n&1Diamond&0 5,202\n&1Copper&0 1,928\n&1Iron block&0 1,067";
        var3[9] = var31;
        String var32 = "&1&lPROGRESSION\n\n&0The world advances through &15 stages&0. Each one unlocks new content and moves the story forward.\n\n&0The next pages cover each stage.";
        var3[10] = var32;
        String var33 = "&1&lSTAGE  I - II\n\n&1I  Awakening&0\nThe gems come alive and the world opens up.\n\n&1II  Into the Fire&0\nDefeat the &1Wither&0. The world border expands outward.";
        var3[11] = var33;
        String var34 = "&1&lSTAGE  III - IV\n\n&1III&0\nDefeat the &1Ender Dragon&0.\n\n&1IV  The Last Raid&0\nFind the hidden village and survive its final raid.";
        var3[12] = var34;
        String var35 = "&1&lSTAGE  V\n\n&1V  The Honored One&0\n\n&0The Assessment. It ends when the Honored One &1departs&0 the Control Room - the final trial of the world.";
        var3[13] = var35;
        String var36 = "&1&lTHE VILLAGE\n\n&0A hidden village is found with a musical &1clue disc&0.\n\n&0Three villagers trade there:\n&1Duke Dennis\n&1KaiCenat\n&1Darren Watkins";
        var3[14] = var36;
        String var37 = "&1&lVILLAGER SOULS\n\n&0Defeat a villager for its &1Soul&0.\n\n&0Right-click a soul to &1return&0 that villager.\n\n&0Return all three to trigger the &1Last Raid&0 - win it to reach Stage IV.";
        var3[15] = var37;
        String var38 = "&1&lTHE HONORED ONE\n\n&0One player carries the &1Honored One&0 title, marked by a gold name.\n\n&0Kill them in &1PvP&0 to claim it - it drifts to the killer. Lava & void deaths keep it.\n\n&0A &1compass&0 points to them.";
        var3[16] = var38;
        String var39 = "&1&lTHE ASSESSMENT\n\n&0Stage V's final trial. The Honored One is &1caged&0 and hunted during timed windows.\n\n&0Survive to the last day to reach the &1Control Room&0 and make your &1decree&0 - ending the story.";
        var3[17] = var39;
        String var40 = "&1&lTHE PEDESTAL\n\n&0A restoration structure. Bring the right items and perform its &1ritual&0 to restore what was lost.\n\n&0Guard it well - tampering with it ends the ritual.";
        var3[18] = var40;
        String var41 = "&1&lCOMMANDS\n\n&1/bliss helpbook&0 guide\n&1/recipe&0 craftables\n&1/withdraw <1-9>&0 bottles\n&1/trust add <p>&0 allies\n&1/watts&0 Flux charge\n&1/startcharging&0 fire Flux\n\n&8Good luck out there!";
        var3[19] = var41;
        PAGES = var3;
    }
}
