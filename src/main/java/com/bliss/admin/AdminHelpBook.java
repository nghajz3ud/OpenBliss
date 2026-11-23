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

public final class AdminHelpBook {
    private static final String[] PAGES;

    private AdminHelpBook() {
    }

    public static ItemStack build(int var0) {
        Material var2 = Material.WRITTEN_BOOK;
        ItemStack var1 = new ItemStack(var2);
        BookMeta var4 = (BookMeta)var1.getItemMeta();
        if (var4 != null) {
            Component var34 = Text.legacy("&4Bliss Admin Guide", 373192400);
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
        ItemStack var5 = build(927939054);
        var20[0] = var5;

        for (ItemStack var7 : var8.addItem(var20).values()) {
            World var18 = var0.getWorld();
            Location var22 = var0.getLocation();
            var18.dropItemNaturally(var22, var7);
        }
    }

    static {
        String[] var3 = new String[33];
        String var2 = "&0&lBLISS\n&8Admin Guide\n\n&7How to set the server up, run every event and use every admin command.\n\n&8by Blood\n\n&7Turn the page ➡";
        var3[0] = var2;
        String var36 = "&1&lFIRST SETUP\n\n&01. Your key goes in &1plugins/BlissPlugin/license.yml&0, then restart.\n2. Set the resource pack in &1server.properties&0 or items show wrong.\n3. Optional: Citizens (NPC memories), UAAPI, WorldGuard.";
        var3[1] = var36;
        String var37 = "&1&lCLIENT LOOKS\n\n&0The Control Room storm, the Blood Moon, the Golden Dream and the memory world are &1Iris shader&0 effects. Players who want them install Iris + the Bliss shaderpack. Without it the game plays the same; only the looks differ.";
        var3[2] = var37;
        String var38 = "&1&lCONFIG MENU\n\n&1/bliss config&0 - the toggles, in tabs: Energy, Gems, Progression, Villager Event, Assessment, Rules and Crafting.\n\n&0Click to flip; it saves at once. Mythic Gems + Juggernaut Role wait for a restart.";
        var3[3] = var38;
        String var39 = "&1&lKEY TOGGLES 1/2\n\n&0Event Progression: master switch for every auto stage.\nBoss Kills → Progression: Wither → 2, Dragon → 3.\nRaid Ends → Progression 4.\nThe HO's decree → 5 needs the master too.";
        var3[4] = var39;
        String var40 = "&1&lKEY TOGGLES 2/2\n\n&0Dragon Games at Prog 3: the egg holder may start them.\nNo Energy Mode: energy never drops.\nPlayer Head Drops: PvP heads, any stage.\n\n&0Ability numbers live in &1config.yml&0; restart after editing.";
        var3[5] = var40;
        String var41 = "&1&lTRUE DAMAGE\n\n&0Config → Gems. On: every damage number in &1config.yml&0 lands as written - same on everyone.\n&0Off: for most abilities the number is dealt before armour, like a sword's, and the victim's kit reduces it.";
        var3[6] = var41;
        String var42 = "&1&lTHE PEDESTAL\n\n&0Stand where its centre goes, then:\n&1/bliss pedestal set\n&1/bliss pedestal activate&0 - builds and opens it\n&1/bliss pedestal deactivate&0 - closes it\n&1/bliss pedestal status\n\n&0The design is built in.";
        var3[7] = var42;
        String var43 = "&1&lPROGRESSION  1/2\n\n&1/bliss progression <0-5|reset>\n\n&10&0 season reset\n&11&0 title, border 2000, the gem roulette\n&12&0 Into the Fire, border 5000\n&13&0 trader + upgrader recipes unlock";
        var3[8] = var43;
        String var44 = "&1&lPROGRESSION  2/2\n\n&14&0 Village and Pillage, after the raid\n&15&0 the Honored One's wishlist: border 5000, netherite on, pearls off, the pedestal shatters.\n\n&0Stages 2-4 can also fire on their own (see toggles).";
        var3[9] = var44;
        String var45 = "&1&lITEMS\n\n&1/bliss give <item> [n]\n&0trader, upgrader, energy, repair, restoration, restorationcore, fragment, obsidian, prismatic, gem [t1|t2]\n\n&1/menu&0 - gems, give page, Gold Gem arc\n&1/recipe&0 - recipes";
        var3[10] = var45;
        String var46 = "&1&lPLAYER STATE\n\n&1/bliss setenergy <p> <n>\n&1/bliss reroll <p>\n&1/bliss reset <p>&0 back to Strength T1\n&1/bliss gemgem <p>&0 gem item back\n&1/bliss cooldown <p>&0 clear\n&1/bliss gemdisable&0 all online / &1gemenable&0 all";
        var3[11] = var46;
        String var47 = "&1&lMISC\n\n&1/pvp on|off\n&1/bliss wisp set <p> <motif>&0 hide behind a sigil\n&1/bliss ghostclear [r]&0 ghost glow\n&1/bliss orb set|remove&0 Prog 5 orb\n&1/setcharge <p> <n>&0 Flux charge\n&1/bliss pov <gem|off>&0 try a POV";
        var3[12] = var47;
        String var48 = "&1&lVILLAGER EVENT 1/3\n\n&0At the village:\n&1/blissevent compass set&0 - the spot; compass, ring and beams read it\n&0By a villager:\n&1villager set 1&0 (2,3) - becomes that trader\n&1villager restock 1&0 - trade again";
        var3[13] = var48;
        String var49 = "&1&lVILLAGER EVENT 2/3\n\n&1/blissevent phase <1|2|3>&0 - 1 disc, 2 hunt, 3 raid\n&0Run 1 then 2 to begin; 2 needs the compass set.\n&1/blissevent give token|soul <1-3>&0 - to yourself";
        var3[14] = var49;
        String var50 = "&1&lVILLAGER EVENT 3/3\n\n&0Three souls returned = the Last Raid (phase 3, on its own). Win it and stage 4 opens (master + Raid Ends on); lost = back to the hunt.\n&1/blissevent end&0 - call it off; stage 4 only once the raid began.";
        var3[15] = var50;
        String var51 = "&1&lDRAGON GAMES 1/2\n\n&0The Dragon Egg holder starts them once, at Progression 3: &1/dragon games start&0. Or you:\n&1/bliss dragongames start&0 - enrols everyone online, pins a 1000-block border\n&1/bliss dragongames end";
        var3[16] = var51;
        String var52 = "&1&lDRAGON GAMES 2/2\n\n&1/bliss dragongames points check|set|add|remove <p> [n]\n&1/shop&0 - participants spend kill points\n&1/bliss dragongames shop edit&0 - item on cursor → slot sells it; L/R ±5, shift ±25, Q removes";
        var3[17] = var52;
        String var53 = "&1&lBLOOD MOON\n\n&1/bliss dragongames bloodmoon start|end&0 - the games' last day: real night, red sky, a one-hour bar, powered creepers and armed mobs.\nEnding it by hand also ends running games; else it holds until one player stands.";
        var3[18] = var53;
        String var54 = "&1&lASSESSMENT 1/3\n\n&0Setup, in order:\n&1/assessment honoredone set <p>\n&1/assessment cage set [r]&0 - on the spire\n&1/assessment controlroom set&0 - where the HO arrives\n&1controlroom <station> set&0: enchants, armor, border, potions";
        var3[19] = var54;
        String var55 = "&1&lASSESSMENT 2/3\n\n&1/assessment ritual&0 - pedestal summon, ends with the HO caged\n&1/assessment day <1-11|next>\n&1/assessment window start|stop&0 - the 2h hunt, once a day\n&1/assessment compass all&0 or &1juggernaut / player / clear <p>";
        var3[20] = var55;
        String var56 = "&1&lASSESSMENT 3/3\n\n&0The title only moves on a PvP kill inside a window.\nDay 11: when the window closes the HO ascends to the Control Room and decrees stage 5.\n\n&1controlroom ask|leave&0 are tests.";
        var3[21] = var56;
        String var57 = "&1&lMYTHIC GEMS\n\n&0Turn on &1Mythic Gems&0 (config) + restart.\n&1/mythics&0 - Heretic / Auratus chest\n&1/juggernaut set <p> <heretic|auratus>&0, remove, revive, unban, spot, ritual, cinematic, list\n&1/cooldown [p]&0 clear their cooldowns";
        var3[22] = var57;
        String var58 = "&1&lJUGGERNAUT KIT\n\n&0A kit each, so they can differ.\n&1/juggernaut kit edit <heretic|auratus>&0 - you wear it, then\n&1kit save&0 or &1kit cancel\n&1kit reset <h|a>&0 - Heretic to the shipped kit, Auratus to deriving in gold\n&1kit&0 - status";
        var3[23] = var58;
        String var59 = "&1&lGOLD GEM\n\n&1/goldgem give [awakened] [p]\n&1/goldgem fragment <1-7|all|core> [p]\n&1/goldgem victim add|remove|list\n&1/goldgem restore <p> [gem]&0 undo an absorb\n&1/tasks wirefragment <1-7> claim <p>&0 award a task";
        var3[24] = var59;
        String var60 = "&1&lGOLDEN DREAM 1/2\n\n&0A player who drops all 7 wire fragments together is pulled into the dream: the pocket walk, then the grey memory world.\n&1/goldendream menu&0 - import a world (folder goldendream_import) or reset it all";
        var3[25] = var60;
        String var61 = "&1&lGOLDEN DREAM 2/2\n\n&1/goldendream walk start|stop|send [p]&0 - the pocket walk by hand (no name = all)\n&1/goldendream wake [p]&0 - leave for good, inventory and place back\n&1/goldendream config&0 and &1progression&0 - the dream's own rules";
        var3[26] = var61;
        String var62 = "&1&lDREAM CHANNELS\n\n&0The dream is a second server:\n&0Chat, deaths and advancements reach only the side they happened on.\n&0Join and leave never reach the dream.\n&0The console sees everything.";
        var3[27] = var62;
        String var63 = "&1&lMEMORIES\n\n&1/goldendream memory add <p> [name]&0, remove, list\n&1memory name|skin <p> <v>\n&1memory npc <n|clear> [skin]&0 - bots at you\n&1memory npc name|skin <bot> <v>\n&0A dead one is purged to the real server.";
        var3[28] = var63;
        String var64 = "&1&lTHE GREAT THUNDER\n\n&1/goldendream thunder&0 - a bolt where you look (storm night: memory world only)\n&1thunder float&0 - every non-memory in the dream floats up 20 blocks, is woken home (staff: set down), then kicked \"Wake Up.\"";
        var3[29] = var64;
        String var65 = "&1&lNICK\n\n&1/nick name <p> <text|off>\n&1/nick skin <p> <player|off>\n&1/nick reset <p>\n\n&0Also under &1/goldendream nick&0. Name and skin are set apart.";
        var3[30] = var65;
        String var66 = "&1&lFILES\n\n&0All of it lives in &1plugins/BlissPlugin/&0: config, state, players, pedestal, memories, golden_dream, the dragon files, honored, cage, controlroom, license.\n\n&0Back that folder up to keep event state.";
        var3[31] = var66;
        String var67 = "&1&lTROUBLE\n\n&0\"No license key set\": fill license.yml, restart.\nPurple/black items: old client pack.\nNo storm / red moon: no Iris + shaderpack.\n\n&0Each root (&1/bliss&0, &1/assessment&0...) lists its own help.";
        var3[32] = var67;
        PAGES = var3;
    }
}
