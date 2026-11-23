package com.bliss.goldgem;

import com.bliss.admin.CommandHelp;
import com.bliss.core.GemType;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.honored.JuggernautGemGuardListener;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class GoldGemCommand implements CommandExecutor, TabCompleter {
    private final GoldGemManager manager;
    private final PlayerDataStore players;
    private final BlissCoreItems items;

    public GoldGemCommand(GoldGemManager var1, PlayerDataStore var2, BlissCoreItems var3, int var4) {
        this.manager = var1;
        this.players = var2;
        this.items = var3;
    }

    public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
        if (var4.length == 0) {
            this.usage(var1, 1531016127);
            return true;
        } else {
            String var8 = var4[0].toLowerCase();
            switch (var8) {
                case "give":
                    this.give(var1, var4, 199124371);
                    break;
                case "fragment":
                    this.fragment(var1, var4, 1615893465);
                    break;
                case "victim":
                    this.victim(var1, var4, 1621269632);
                    break;
                case "restore":
                    this.restore(var1, var4, 1648867713);
                    break;
                case "status":
                    this.status(var1, 2067662877);
                    break;
                default:
                    this.usage(var1, 1531016127);
            }

            return true;
        }
    }

    private void fragment(CommandSender var1, String[] var2, int var3) {
        if (var2.length < 2) {
            String var58 = "/goldgem fragment <1-7|all|core> [player]";
            CommandHelp.hint(var1, var58, 80456809);
        } else {
            Player var93;
            if (var2.length >= 3) {
                var93 = Bukkit.getPlayerExact(var2[2]);
                if (var93 == null) {
                    String var90 = "&cPlayer not found: &f" + var2[2];
                    this.msg(var1, var90, 1220362783);
                    return;
                }
            } else if (var1 instanceof Player p) {
                var93 = p;
            } else {
                String var87 = "&cConsole must specify a player.";
                this.msg(var1, var87, 1220362783);
                return;
            }

            String var9 = var2[1].toLowerCase();
            switch (var9) {
                case "core":
                    ItemStack var73 = RitualItems.fragmentCore(1868423911);
                    BlissCoreItems.giveOrDrop(var93, var73, 41085121);
                    String var86 = "&6Gave a &lꜰʀᴀɢᴍᴇɴᴛ ᴄᴏʀᴇ&6 to &f" + var93.getName();
                    this.msg(var1, var86, 1220362783);
                    break;
                case "all":
                    for (int var94 = 1; var94 <= 7; var94++) {
                        ItemStack var72 = RitualItems.wireFragment(var94, 60990984);
                        BlissCoreItems.giveOrDrop(var93, var72, 41085121);
                    }

                    String var83 = "&6Gave all &f7&6 Wire Fragments to &f" + var93.getName();
                    this.msg(var1, var83, 1220362783);
                    break;
                default:
                    int var12;
                    try {
                        var12 = Integer.parseInt(var9);
                    } catch (NumberFormatException var95) {
                        String var62 = "/goldgem fragment <1-7|all|core> [player]";
                        CommandHelp.hint(var1, var62, 80456809);
                        return;
                    }

                    if (var12 < 1 || var12 > 7) {
                        String var80 = "&cWire Fragment id must be 1-7.";
                        this.msg(var1, var80, 1220362783);
                        return;
                    }

                    ItemStack var66 = RitualItems.wireFragment(var12, 60990984);
                    BlissCoreItems.giveOrDrop(var93, var66, 41085121);
                    String var91 = var93.getName();
                    String var79 = "&6Gave &lᴡɪʀᴇ ꜰʀᴀɢᴍᴇɴᴛ&6 #" + var12 + " to &f" + var91;
                    this.msg(var1, var79, 1220362783);
            }
        }
    }

    private void give(CommandSender var1, String[] var2, int var3) {
        boolean var9 = false;
        String var10 = null;
        int var11 = 1;

        while (true) {
            int var52 = var2.length;
            if (var11 >= var52) {
                Player var87;
                if (var10 != null) {
                    var87 = Bukkit.getPlayerExact(var10);
                    if (var87 == null) {
                        String var83 = "&cPlayer not found: &f" + var10;
                        this.msg(var1, var83, 1220362783);
                        return;
                    }
                } else if (var1 instanceof Player p) {
                    var87 = p;
                } else {
                    String var6 = "&cConsole must specify a player: &f/goldgem give [awakened] <player>";
                    this.msg(var1, var6, 1220362783);
                    return;
                }

                ItemStack var13 = GoldGem.create(485535734);
                GoldGemManager var33 = this.manager;
                var33.becomeHolder(var87, var13, 2022822655);
                if (var9) {
                    GoldGem.setMask(var13, 255, 1710446259);
                    GoldGem.setState(var13, 1, 953378395);
                    GemType var63 = GoldGem.absorbedGems(var13, 874118425).get(0);
                    GoldGem.setSelected(var13, var63, 958539197);
                }

                BlissCoreItems.giveOrDrop(var87, var13, 41085121);
                if (var9) {
                    String var80 = "&6Gave an &lᴀᴡᴀᴋᴇɴᴇᴅ&6 Gold Gem (all 8 souls) to &f" + var87.getName();
                    this.msg(var1, var80, 1220362783);
                    if (var87 != var1) {
                        String var81 = "&6The Gold Gem awakens in your hands.";
                        this.msg(var87, var81, 1220362783);
                    }
                } else {
                    GemType var14 = GoldGem.getSelected(var13, 128405186);
                    String var75 = var87.getName();
                    GemType var8 = GemType.NONE;
                    String var86;
                    if (var14 == var8) {
                        var86 = " &7(no gem to absorb)";
                    } else {
                        var86 = " &7(absorbed " + var14.label() + ")";
                    }

                    String var76 = "&6Gave the Gold Gem to &f" + var75 + var86;
                    this.msg(var1, var76, 1220362783);
                    if (var87 != var1) {
                        String var77 = "&6The Gold Gem chooses you.";
                        this.msg(var87, var77, 1220362783);
                    }
                }

                return;
            }

            if (var2[var11].equalsIgnoreCase("awakened")) {
                var9 = true;
            } else {
                var10 = var2[var11];
            }

            var11++;
        }
    }

    private void victim(CommandSender var1, String[] var2, int var3) {
        if (var2.length < 2) {
            String var82 = "/goldgem victim <add|remove|list> [player]";
            CommandHelp.hint(var1, var82, 80456809);
        } else {
            String var9 = var2[1].toLowerCase();
            switch (var9) {
                case "add":
                case "remove":
                    OfflinePlayer var117;
                    if (var2.length >= 3) {
                        var117 = Bukkit.getOfflinePlayer(var2[2]);
                    } else {
                        if (!(var1 instanceof Player)) {
                            String var112 = "&cSpecify a player.";
                            this.msg(var1, var112, 1220362783);
                            return;
                        }

                        var117 = (Player)var1;
                    }

                    String var71;
                    if (var117.getName() == null) {
                        var71 = var117.getUniqueId().toString();
                    } else {
                        var71 = var117.getName();
                    }

                    UUID var120 = var117.getUniqueId();
                    boolean var15 = var2[1].equalsIgnoreCase("add");
                    boolean var69;
                    if (var15) {
                        GoldGemManager var68 = this.manager;
                        var69 = var68.addVictim(var120, 1646529536);
                    } else {
                        GoldGemManager var65 = this.manager;
                        var69 = var65.removeVictim(var120, 211332633);
                    }

                    String var111;
                    if (var69) {
                        String var113;
                        if (var15) {
                            var113 = "Added";
                        } else {
                            var113 = "Removed";
                        }

                        String var116;
                        if (var15) {
                            var116 = "to";
                        } else {
                            var116 = "from";
                        }

                        var111 = "&6" + var113 + " &f" + var71 + "&6 " + var116 + " the victim list.";
                    } else {
                        String var115;
                        if (var15) {
                            var115 = "already on";
                        } else {
                            var115 = "not on";
                        }

                        var111 = "&7" + var71 + " was " + var115 + " the victim list.";
                    }

                    this.msg(var1, var111, 1220362783);
                    break;
                case "list":
                    Set<UUID> var11 = this.manager.victims(102476803);
                    if (var11.isEmpty()) {
                        String var6 = "&7The victim list is empty.";
                        this.msg(var1, var6, 1220362783);
                        return;
                    }

                    String var101 = "&6Victim list (&f" + var11.size() + "&6):";
                    this.msg(var1, var101, 1220362783);

                    for (UUID var13 : var11) {
                        OfflinePlayer var14 = Bukkit.getOfflinePlayer(var13);
                        String var105;
                        if (var14.getName() == null) {
                            var105 = var13.toString();
                        } else {
                            var105 = var14.getName();
                        }

                        String var106 = "&7- &f" + var105;
                        this.msg(var1, var106, 1220362783);
                    }
                    break;
                default:
                    String var85 = "/goldgem victim <add|remove|list> [player]";
                    CommandHelp.hint(var1, var85, 80456809);
            }
        }
    }

    private void restore(CommandSender var1, String[] var2, int var3) {
        if (var2.length < 2) {
            String var72 = "/goldgem restore <player> [gem]";
            CommandHelp.hint(var1, var72, 80456809);
        } else {
            Player var10 = Bukkit.getPlayerExact(var2[1]);
            if (var10 == null) {
                String var74 = "&cPlayer not found (they must be online): &f" + var2[1];
                this.msg(var1, var74, 1220362783);
            } else {
                UUID var11 = var10.getUniqueId();
                GoldGemManager var22 = this.manager;
                if (!var22.isRepurposed(var11, 1313846040)) {
                    String var93 = "&c" + var10.getName() + "'s soul has not been repurposed — nothing to restore.";
                    this.msg(var1, var93, 1220362783);
                } else if (JuggernautGemGuardListener.isJuggernaut(var10, 859003337)) {
                    String var90 = "&cThat player is a Juggernaut — Juggernauts are gem-exempt.";
                    this.msg(var1, var90, 1220362783);
                } else {
                    GemType var105;
                    if (var2.length >= 3) {
                        var105 = parseGem(var2[2], 1787926602);
                        GemType var57 = GemType.NONE;
                        if (var105 == var57) {
                            String var77 = "&cUnknown gem: &f" + var2[2];
                            this.msg(var1, var77, 1220362783);
                            return;
                        }
                    } else {
                        GoldGemManager var45 = this.manager;
                        var105 = var45.repurposedGem(var11, 709561743);
                        GemType var68 = GemType.NONE;
                        if (var105 == var68) {
                            String var88 = var10.getName();
                            String var99 = var10.getName();
                            String var89 = "&cThe Gold Gem took "
                                + var88
                                + "'s gem before the type was recorded. Name it: &f/goldgem restore "
                                + var99
                                + " <gem>";
                            this.msg(var1, var89, 1220362783);
                            return;
                        }
                    }

                    GoldGemManager var34 = this.manager;
                    var34.clearRepurposed(var11, 566739106);
                    PlayerDataStore var37 = this.players;
                    PlayerData var12 = var37.get(var11, 1264010596);
                    var12.setGem(var105, 2137390487);
                    int var63 = BlissCoreItems.findGemSlot(var10, 1702349856);
                    BlissCoreItems var79 = this.items;
                    boolean var100 = var12.tier2(629984660);
                    int var104 = var12.energy(1160853318);
                    ItemStack var80 = var79.gemItem(var105, var100, var104, 256965471);
                    BlissCoreItems.giveGemOrHold(var10, var63, var80, 2133739666);
                    String var82 = var105.coloredName();
                    String var97 = var10.getName();
                    String var103;
                    if (var12.tier2(629984660)) {
                        var103 = "II";
                    } else {
                        var103 = "I";
                    }

                    String var83 = "&6Restored " + var82 + "&6 to &f" + var97 + "&7 (Tier " + var103 + "; the Gold Gem keeps its absorbed soul).";
                    this.msg(var1, var83, 1220362783);
                    if (var10 != var1) {
                        String var86 = "&6Your soul is your own again — your " + var105.coloredName() + "&6 gem returns.";
                        this.msg(var10, var86, 1220362783);
                    }
                }
            }
        }
    }

    private static GemType parseGem(String var0, int var1) {
        for (GemType var7 : GemType.values()) {
            GemType var21 = GemType.NONE;
            if (var7 != var21) {
                String var17 = var7.name();
                if (var17.equalsIgnoreCase(var0)) {
                    return var7;
                }
            }
        }

        return GemType.NONE;
    }

    private void status(CommandSender var1, int var2) {
        String var9 = "&6Gold Gem &7| victims: &f" + this.manager.victims(102476803).size();
        this.msg(var1, var9, 1220362783);
    }

    private void usage(CommandSender var1, int var2) {
        String var4 = "goldgem";
        String var5 = "Spawn the Gold Gem and manage the harvest list.";
        String var6 = "give [awakened] [player]";
        String var7 = "Hand out the Gold Gem";
        CommandHelp.Entry var12 = CommandHelp.of(var6, var7, 1959232616);
        String var14 = "fragment <1-7|all|core> [player]";
        String var8 = "Hand out Wire Fragments";
        CommandHelp.Entry var15 = CommandHelp.of(var14, var8, 1959232616);
        String var16 = "victim <add|remove|list> [player]";
        String var9 = "Mark souls (no name = yourself)";
        CommandHelp.Entry var17 = CommandHelp.of(var16, var9, 1959232616);
        String var18 = "restore <player> [gem]";
        String var10 = "Give back an absorbed gem";
        CommandHelp.Entry var19 = CommandHelp.of(var18, var10, 1959232616);
        String var20 = "status";
        String var11 = "Show the Gold Gem's state";
        CommandHelp.Entry var21 = CommandHelp.of(var20, var11, 1959232616);
        List var13 = List.of(var12, var15, var17, var19, var21);
        CommandHelp.show(var1, var4, var5, var13, 696249826);
    }

    private void msg(CommandSender var1, String var2, int var3) {
        Component var6 = Text.legacy(var2, 373192400);
        var1.sendMessage(var6);
    }

    public List<String> onTabComplete(CommandSender var1, Command var2, String var3, String[] var4) {
        if (var4.length == 1) {
            String[] var171 = new String[5];
            String var225 = "give";
            var171[0] = var225;
            String var226 = "fragment";
            var171[1] = var226;
            String var227 = "victim";
            var171[2] = var227;
            String var228 = "restore";
            var171[3] = var228;
            String var229 = "status";
            var171[4] = var229;
            List var172 = Arrays.asList(var171);
            String var187 = var4[0];
            return this.filter(var172, var187, 1234168190);
        } else if (var4.length == 2 && var4[0].equalsIgnoreCase("restore")) {
            ArrayList var21 = new ArrayList();
            ArrayList var10 = var21;

            for (Player var12 : Bukkit.getOnlinePlayers()) {
                String var124 = var12.getName();
                var10.add(var124);
            }

            String var173 = var4[1];
            return this.filter(var10, var173, 1234168190);
        } else if (var4.length == 3 && var4[0].equalsIgnoreCase("restore")) {
            ArrayList var38 = new ArrayList();
            ArrayList var230 = var38;

            for (GemType var14 : GemType.values()) {
                GemType var131 = GemType.NONE;
                if (var14 != var131) {
                    String var134 = var14.name().toLowerCase();
                    var230.add(var134);
                }
            }

            String var175 = var4[2];
            return this.filter(var230, var175, 1234168190);
        } else if (var4.length == 2 && var4[0].equalsIgnoreCase("give")) {
            List var183 = List.of("awakened");
            ArrayList var106 = new ArrayList(var183);
            ArrayList var232 = var106;

            for (Player var238 : Bukkit.getOnlinePlayers()) {
                String var168 = var238.getName();
                var232.add(var168);
            }

            String var185 = var4[1];
            return this.filter(var232, var185, 1234168190);
        } else if (var4.length == 2 && var4[0].equalsIgnoreCase("fragment")) {
            String[] var143 = new String[9];
            String var9 = "1";
            var143[0] = var9;
            String var213 = "2";
            var143[1] = var213;
            String var214 = "3";
            var143[2] = var214;
            String var215 = "4";
            var143[3] = var215;
            String var216 = "5";
            var143[4] = var216;
            String var217 = "6";
            var143[5] = var217;
            String var218 = "7";
            var143[6] = var218;
            String var219 = "all";
            var143[7] = var219;
            String var220 = "core";
            var143[8] = var220;
            List var144 = Arrays.asList(var143);
            String var177 = var4[1];
            return this.filter(var144, var177, 1234168190);
        } else if (var4.length == 2 && var4[0].equalsIgnoreCase("victim")) {
            String[] var165 = new String[3];
            String var222 = "add";
            var165[0] = var222;
            String var223 = "remove";
            var165[1] = var223;
            String var224 = "list";
            var165[2] = var224;
            List var166 = Arrays.asList(var165);
            String var181 = var4[1];
            return this.filter(var166, var181, 1234168190);
        } else {
            boolean var89;
            if ((var4.length != 3 || !var4[0].equalsIgnoreCase("give") || !var4[1].equalsIgnoreCase("awakened"))
                && (var4.length != 3 || !var4[0].equalsIgnoreCase("fragment"))
                && (var4.length != 3 || !var4[0].equalsIgnoreCase("victim") || var4[1].equalsIgnoreCase("list"))) {
                var89 = false;
            } else {
                var89 = true;
            }

            if (!var89) {
                return List.of();
            } else {
                ArrayList var91 = new ArrayList();
                ArrayList var234 = var91;

                for (Player var239 : Bukkit.getOnlinePlayers()) {
                    String var163 = var239.getName();
                    var234.add(var163);
                }

                int var201 = var4.length - 1;
                String var179 = var4[var201];
                return this.filter(var234, var179, 1234168190);
            }
        }
    }

    private List<String> filter(List<String> var1, String var2, int var3) {
        ArrayList var4 = new ArrayList();
        ArrayList var5 = var4;

        for (String var7 : var1) {
            String var17 = var7.toLowerCase();
            String var22 = var2.toLowerCase();
            if (var17.startsWith(var22)) {
                var5.add(var7);
            }
        }

        return var5;
    }
}
