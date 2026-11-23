package com.bliss.admin;

import com.bliss.BlissPlugin;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.goldgem.GoldGem;
import com.bliss.items.BlissCoreItems;
import com.bliss.mythics.MythicItems;
import com.bliss.mythics.aegis.MythicAegisState;
import com.bliss.mythics.blood.MythicBloodState;
import com.bliss.villager.Text;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;

public final class ConfigMenuListener implements Listener {
    private static final int DESC_WRAP = 48;
    private static final int MAIN_SIZE = 27;
    private static final int MAIN_TITLE_SLOT = 4;
    private static final int PAGE_SIZE = 36;
    private static final int PAGE_HEADER_SLOT = 4;
    private static final int PAGE_BACK_SLOT = 31;
    private static final int[] PAGE_SLOTS;
    private final BlissPlugin plugin;
    private static final Set<ConfigMenuListener.Toggle> DREAM_SCOPED;

    public ConfigMenuListener(BlissPlugin var1, int var2) {
        this.plugin = var1;
    }

    public void open(Player var1, int var2) {
        this.open(var1, false, 1367868276);
    }

    public void open(Player var1, boolean var2, int var3) {
        ConfigMenuListener.MainHolder var4 = new ConfigMenuListener.MainHolder(var2, 460160076);
        String var37;
        if (var2) {
            var37 = "&8&lGolden Dream &7&lConfig";
        } else {
            var37 = "&8&lBliss &7&lConfig";
        }

        Component var49 = Text.legacy(var37, 373192400);
        Inventory var12 = Bukkit.createInventory(var4, 27, var49);
        var4.inv = var12;
        ItemStack var13 = pane(369116110);

        for (int var14 = 0; var14 < 27; var14++) {
            var12.setItem(var14, var13);
        }

        Material var51 = Material.ENCHANTED_BOOK;
        String[] var59 = new String[1];
        String var65;
        if (var2) {
            var65 = "&7The dream's own settings — its worlds only.";
        } else {
            var65 = "&7Click a category to see its settings.";
        }

        var59[0] = var65;
        ItemStack var52 = label(var51, var37, var59, 1352837194);
        var12.setItem(4, var52);

        for (ConfigMenuListener.Category var17 : ConfigMenuListener.Category.values()) {
            int var47 = var17.slot;
            ItemStack var54 = this.categoryButton(var17, 1848526452);
            String var58 = var17.name;
            String[] var61 = new String[3];
            String var67 = var17.blurb;
            var61[0] = var67;
            String var68 = "";
            var61[1] = var68;
            String var69 = "&eClick to open.";
            var61[2] = var69;
            ItemStack var55 = label(var54, var58, var61, 1718725543);
            var12.setItem(var47, var55);
        }

        var1.openInventory(var12);
    }

    private void openPage(Player var1, ConfigMenuListener.Category var2, boolean var3, int var4) {
        ConfigMenuListener.PageHolder var5 = new ConfigMenuListener.PageHolder(var2, var3, 1052802185);
        ConfigMenuListener.PageHolder var11 = var5;
        String var102;
        if (var3) {
            var102 = "&8&lDream &7» ";
        } else {
            var102 = "&8&lConfig &7» ";
        }

        String var104 = var2.name;
        Component var88 = Text.legacy(var102 + var104, 373192400);
        Inventory var12 = Bukkit.createInventory(var5, 36, var88);
        var5.inv = var12;
        ItemStack var13 = pane(369116110);

        for (int var14 = 0; var14 < 36; var14++) {
            var12.setItem(var14, var13);
        }

        Material var91 = var2.header;
        String var106 = var2.name;
        String[] var114 = new String[1];
        String var125 = var2.blurb;
        var114[0] = var125;
        ItemStack var92 = label(var91, var106, var114, 1352837194);
        var12.setItem(4, var92);
        Material var93 = Material.BARRIER;
        String var107 = "&cBack";
        String[] var116 = new String[1];
        String var126 = "&7To the categories.";
        var116[0] = var126;
        ItemStack var94 = label(var93, var107, var116, 1352837194);
        var12.setItem(31, var94);
        GlobalState var63;
        if (var3) {
            var63 = this.plugin.dreamState(503017964);
        } else {
            var63 = this.plugin.globalState(724248306);
        }

        GlobalState var128 = var63;
        int var15 = 0;

        for (ConfigMenuListener.Toggle var19 : ConfigMenuListener.Toggle.values()) {
            ConfigMenuListener.Category var42 = var19.category;
            if (var42 == var2 && !this.hidden(var19, 2120792978)) {
                int var75 = PAGE_SLOTS.length;
                if (var15 >= var75) {
                    Logger var48 = this.plugin.getLogger();
                    String var77 = var2.name();
                    int var96 = PAGE_SLOTS.length;
                    String var109 = var19.name;
                    String var78 = "/bliss config: the "
                        + var77
                        + " page is full at "
                        + var96
                        + " — '"
                        + var109
                        + "' and anything after it cannot be shown. Add a row to PAGE_SLOTS.";
                    var48.warning(var78);
                    break;
                }

                int[] var51 = PAGE_SLOTS;
                int var80 = var15++;
                int var20 = var51[var80];
                if (var3) {
                    Set var54 = DREAM_SCOPED;
                    if (!var54.contains(var19)) {
                        Material var97 = Material.GRAY_STAINED_GLASS_PANE;
                        String var112 = "&8" + var19.name;
                        String[] var118 = new String[1];
                        String var127 = "&7Server-wide — set it in &f/bliss config&7.";
                        var118[0] = var127;
                        ItemStack var98 = label(var97, var112, var118, 1352837194);
                        var12.setItem(var20, var98);
                        continue;
                    }
                }

                Map var58 = var11.slots;
                Integer var84 = var20;
                var58.put(var84, var19);
                boolean var120 = this.get(var128, var19, 1471167249);
                ItemStack var101 = this.icon(var19, var120, 1798567197);
                var12.setItem(var20, var101);
            }
        }

        var1.openInventory(var12);
    }

    private static Material chainMaterial(int var0) {
        Material var2 = Material.matchMaterial("IRON_CHAIN");
        if (var2 == null) {
            var2 = Material.matchMaterial("CHAIN");
        }

        Material var8;
        if (var2 == null) {
            var8 = Material.IRON_BARS;
        } else {
            var8 = var2;
        }

        return var8;
    }

    private boolean hidden(ConfigMenuListener.Toggle var1, int var2) {
        ConfigMenuListener.Toggle var4 = ConfigMenuListener.Toggle.DRAGON_GAMES_HOLDER;
        boolean var8;
        if (var1 == var4 && this.plugin.dragonGames(1198797739) == null) {
            var8 = true;
        } else {
            var8 = false;
        }

        return var8;
    }

    @EventHandler
    public void onClick(InventoryClickEvent var1) {
        InventoryHolder var8 = var1.getView().getTopInventory().getHolder();
        if (var8 instanceof ConfigMenuListener.MainHolder || var8 instanceof ConfigMenuListener.PageHolder) {
            var1.setCancelled(true);
            if (var1.getWhoClicked() instanceof Player var9) {
                if (var1.getClickedInventory() != null) {
                    InventoryHolder var33 = var1.getClickedInventory().getHolder();
                    if (var33 == var8) {
                        int var116 = var1.getRawSlot();
                        if (var8 instanceof ConfigMenuListener.MainHolder var117) {
                            ConfigMenuListener.Category var118 = ConfigMenuListener.Category.bySlot(var116);
                            if (var118 != null) {
                                BukkitScheduler var74 = Bukkit.getScheduler();
                                BlissPlugin var96 = this.plugin;
                                Runnable var107 = () -> {
                                    boolean var8x = var117.dream;
                                    this.openPage(var9, var118, var8x, 1758787118);
                                };
                                var74.runTask(var96, var107);
                            }

                            return;
                        }

                        ConfigMenuListener.PageHolder var11 = (ConfigMenuListener.PageHolder)var8;
                        if (var116 == 31) {
                            BukkitScheduler var67 = Bukkit.getScheduler();
                            BlissPlugin var94 = this.plugin;
                            Runnable var105 = () -> {
                                boolean var6 = var11.dream;
                                this.open(var9, var6, 1367868276);
                            };
                            var67.runTask(var94, var105);
                            return;
                        }

                        Map var42 = var11.slots;
                        Integer var79 = var116;
                        ConfigMenuListener.Toggle var12 = (ConfigMenuListener.Toggle)var42.get(var79);
                        if (var12 == null) {
                            return;
                        }

                        GlobalState var50;
                        if (var11.dream) {
                            var50 = this.plugin.dreamState(503017964);
                        } else {
                            var50 = this.plugin.globalState(724248306);
                        }

                        if (var50 == null) {
                            Component var92 = Text.legacy("&cGlobalState unavailable.", 373192400);
                            var9.sendMessage(var92);
                            return;
                        }

                        boolean var54;
                        if (!this.get(var50, var12, 1471167249)) {
                            var54 = true;
                        } else {
                            var54 = false;
                        }

                        this.set(var50, var12, var54, 1204401261);
                        var50.save(343464104);
                        Inventory var58 = var1.getInventory();
                        ItemStack var99 = this.icon(var12, var54, 1798567197);
                        var58.setItem(var116, var99);
                        Location var84 = var9.getLocation();
                        Sound var101;
                        if (var54) {
                            var101 = Sound.BLOCK_NOTE_BLOCK_PLING;
                        } else {
                            var101 = Sound.BLOCK_NOTE_BLOCK_BASS;
                        }

                        float var113;
                        if (var54) {
                            var113 = 1.4F;
                        } else {
                            var113 = 0.8F;
                        }

                        var9.playSound(var84, var101, 0.6F, var113);
                        String var86 = var12.name;
                        String var103;
                        if (var54) {
                            var103 = "&l&aenabled";
                        } else {
                            var103 = "&l&cdisabled";
                        }

                        Component var88 = Text.legacy("&7" + var86 + " is now " + var103, 373192400);
                        var9.sendMessage(var88);
                        ConfigMenuListener.Toggle var89 = ConfigMenuListener.Toggle.TRUE_DAMAGE;
                        if (var12 == var89 && var54) {
                            this.warnTrueDamage(var9, 1143071793);
                        }

                        return;
                    }
                }
            }
        }
    }

    private void warnTrueDamage(Player var1, int var2) {
        Component var8 = Text.legacy("&6&l! &eEvery gem and mythic ability now deals the same to everyone.", 373192400);
        var1.sendMessage(var8);
        Component var10 = Text.legacy("&e  Each &fgems.*.damage.* &enumber in config.yml lands as written, on anyone.", 373192400);
        var1.sendMessage(var10);
        Component var12 = Text.legacy("&8  Energy scale, the Honored One dial, per-ability caps, shields and", 373192400);
        var1.sendMessage(var12);
        Component var14 = Text.legacy("&8  Resistance still apply. See the notes at the top of config.yml.", 373192400);
        var1.sendMessage(var14);
    }

    private boolean get(GlobalState var1, ConfigMenuListener.Toggle var2, int var3) {
        return switch (var2) {
            case ENERGY_RESTRICTIONS -> var1.energyRestrictions(860109657);
            case NO_ENERGY -> var1.noEnergyMode(340448998);
            case BROKEN_DAMAGE -> var1.brokenDamage(1031219045);
            case BOTTLED_ENERGY_EXPIRY -> var1.bottledEnergyExpiry(261106370);
            case WATT_DEPOSIT -> var1.wattDeposit(552206927);
            case SPAWN_GEM_DISABLE -> var1.spawnGemDisable(898885534);
            case BUNDLE_GEM_PROTECTION -> var1.bundleGemProtection(468873788);
            case ALLOW_OVERCHARGE -> var1.allowOvercharge(201427360);
            case STRENGTH_TWO -> var1.strengthTwoPassive(1352452848);
            case LEGACY_SPEED -> var1.legacySpeedRaw(445408285);
            case LEGACY_PUFF -> var1.legacyPuffRaw(1871243026);
            case LEGACY_AURATUS -> MythicAegisState.legacyAuratus(180703355);
            case GOLD_LEGACY_SPEED -> var1.goldLegacySpeed(1059261642);
            case AURATUS_CHAIN_LOCK -> MythicAegisState.chainRefundLock(1519417400);
            case SNEAK_CAST -> var1.sneakCast(704684993);
            case HERETIC_RICOCHET_HALF -> MythicBloodState.ricochetHalfDamage(858730909);
            case PROGRESSION_GEMS -> var1.progressionGems(853797119);
            case TRUE_DAMAGE -> var1.trueDamage(755209314);
            case MYTHIC_GEM_STRIPS_MAIN -> var1.mythicGemStripsMain(635850224);
            case MYTHIC_GEMS -> var1.mythicGems(289981390);
            case JUGGERNAUT_ROLE -> var1.juggernautRole(1687899048);
            case EVENT_PROGRESSION -> var1.eventProgression(169255316);
            case STRENGTH_PROG1_FULL -> var1.strengthProg1FullPassive(2079279880);
            case TIER2_GLOW -> var1.tier2Glow(1825034216);
            case GEM_AUTOENCHANT_ALL -> var1.gemAutoEnchantAllStages(182748902);
            case BOSS_PROGRESSION -> var1.bossProgression(1009383364);
            case PROG0_GEMS -> var1.prog0Gems(1794280694);
            case RAID_ADVANCES_PROG -> var1.raidAdvancesProg4(598254309);
            case DRAGON_GAMES_HOLDER -> var1.dragonGamesHolderStart(437420920);
            case VILLAGERS -> var1.villagers(570366672);
            case CLUE_AUDIO -> var1.clueAudio(2027958034);
            case LOST_SOULS_AUDIO -> var1.lostSoulsAudio(951200401);
            case TRACKING_COMPASSES -> var1.trackingCompasses(758328200);
            case CONTROL_ROOM_AUDIENCE -> var1.controlRoomAudience(1678943978);
            case SHADER_EFFECTS -> var1.shaderEffects(1378906903);
            case FINALE_KNOCKBACK -> var1.finaleKnockback(1560972786);
            case CRUSHED_ORB -> var1.crushedOrb(1885715107);
            case GOLD_GEM_SOULBOUND -> var1.goldGemSoulbound(269044761);
            case TRADER -> var1.traderRecipe(285314044);
            case UPGRADER -> var1.upgraderRecipe(459107113);
            case RESTORATION_CORE_RECIPE -> var1.restorationCoreRecipe(1172908205);
            case NETHERITE -> var1.netheriteAllowed(613865620);
            case ENDERPEARLS -> var1.enderpearls(155177658);
            case TROPHY_THUNDER -> var1.trophyThunder(833687581);
            case TROPHY_THUNDER_ALL -> var1.trophyThunderAllStages(250785423);
            case PLAYER_HEAD_DROPS -> var1.playerHeadDrops(666090489);
            case KEEP_TIER2_ON_DEATH -> var1.keepTier2OnDeath(922876535);
            case TIER2_ON_JOIN -> var1.tier2OnJoin(2023108022);
            case ENCH_OBSIDIAN_GLOW -> var1.enchObsidianGlow(1536924725);
            case MYTHIC_DRAGON_EGG -> var1.mythicDragonEgg(329233392);
            case PROG_ARMOR_LIMIT -> var1.progArmorLimit(1353714115);
            case PROG_KIT_LIMIT -> var1.progKitLimit(1635426977);
            case KIT_ENCHANT_LIMITS -> var1.kitEnchantLimits(1706590402);
            case PROG_ENCHANT_LIMIT -> var1.progEnchantLimit(1344216646);
            case MEMORY_PROXIMITY_SOUND -> var1.memoryProximitySound(604772563);
            default -> {
                MatchException var15 = new MatchException(null, null);
                throw var15;
            }
        };
    }

    private void set(GlobalState var1, ConfigMenuListener.Toggle var2, boolean var3, int var4) {
        switch (var2) {
            case ENERGY_RESTRICTIONS:
                var1.setEnergyRestrictions(var3, 50405250);
                break;
            case NO_ENERGY:
                var1.setNoEnergyMode(var3, 1881985181);
                break;
            case BROKEN_DAMAGE:
                var1.setBrokenDamage(var3, 106720013);
                break;
            case BOTTLED_ENERGY_EXPIRY:
                var1.setBottledEnergyExpiry(var3, 1903503209);
                break;
            case WATT_DEPOSIT:
                var1.setWattDeposit(var3, 7904853);
                break;
            case SPAWN_GEM_DISABLE:
                var1.setSpawnGemDisable(var3, 1350062420);
                break;
            case BUNDLE_GEM_PROTECTION:
                var1.setBundleGemProtection(var3, 1548605255);
                break;
            case ALLOW_OVERCHARGE:
                var1.setAllowOvercharge(var3, 1722320299);
                break;
            case STRENGTH_TWO:
                var1.setStrengthTwoPassive(var3, 1781545790);
                break;
            case LEGACY_SPEED:
                var1.setLegacySpeed(var3, 1201045649);
                if (!var1.isDream(1952413142)) {
                    this.restampSpeedGems(1832560588);
                }
                break;
            case LEGACY_PUFF:
                var1.setLegacyPuff(var3, 1817028959);
                break;
            case LEGACY_AURATUS:
                String var85 = "auratus";
                String var152 = "legacy";
                GemTuning.setMythicFlag(var85, var152, var3, 376595522);
                this.plugin.saveConfig();
                break;
            case GOLD_LEGACY_SPEED:
                var1.setGoldLegacySpeed(var3, 669406894);
                break;
            case AURATUS_CHAIN_LOCK:
                String var13 = "auratus";
                String var114 = "refund-lock";
                GemTuning.setMythicFlag(var13, var114, var3, 376595522);
                this.plugin.saveConfig();
                break;
            case SNEAK_CAST:
                var1.setSneakCast(var3, 439536169);
                break;
            case HERETIC_RICOCHET_HALF:
                String var49 = "heretic";
                String var131 = "ricochet-half";
                GemTuning.setMythicFlag(var49, var131, var3, 376595522);
                this.plugin.saveConfig();
                break;
            case PROGRESSION_GEMS:
                var1.setProgressionGems(var3, 1171497813);
                if (!var1.isDream(1952413142)) {
                    this.restampSpeedGems(1832560588);
                }
                break;
            case TRUE_DAMAGE:
                var1.setTrueDamage(var3, 1237197351);
                break;
            case MYTHIC_GEM_STRIPS_MAIN:
                var1.setMythicGemStripsMain(var3, 1467653463);
                break;
            case MYTHIC_GEMS:
                var1.setMythicGems(var3, 2102903793);
                break;
            case JUGGERNAUT_ROLE:
                var1.setJuggernautRole(var3, 1248740951);
                break;
            case EVENT_PROGRESSION:
                var1.setEventProgression(var3, 1499003840);
                break;
            case STRENGTH_PROG1_FULL:
                var1.setStrengthProg1FullPassive(var3, 2104191768);
                break;
            case TIER2_GLOW:
                var1.setTier2Glow(var3, 1561526866);
                break;
            case GEM_AUTOENCHANT_ALL:
                var1.setGemAutoEnchantAllStages(var3, 1487150081);
                break;
            case BOSS_PROGRESSION:
                var1.setBossProgression(var3, 1474614967);
                break;
            case PROG0_GEMS:
                var1.setProg0Gems(var3, 1984571060);
                break;
            case RAID_ADVANCES_PROG:
                var1.setRaidAdvancesProg4(var3, 18719168);
                break;
            case DRAGON_GAMES_HOLDER:
                var1.setDragonGamesHolderStart(var3, 1146958599);
                break;
            case VILLAGERS:
                var1.setVillagers(var3, 393264146);
                var1.setVillagersLocked(true, 492545785);
                break;
            case CLUE_AUDIO:
                var1.setClueAudio(var3, 1391397753);
                break;
            case LOST_SOULS_AUDIO:
                var1.setLostSoulsAudio(var3, 603484735);
                break;
            case TRACKING_COMPASSES:
                var1.setTrackingCompasses(var3, 328661338);
                break;
            case CONTROL_ROOM_AUDIENCE:
                var1.setControlRoomAudience(var3, 634586814);
                break;
            case SHADER_EFFECTS:
                var1.setShaderEffects(var3, 1705590000);
                if (!var3 && this.plugin.controlRoomVeil(785222279) != null) {
                    this.plugin.controlRoomVeil(785222279).liftAll(167366833);
                }
                break;
            case FINALE_KNOCKBACK:
                var1.setFinaleKnockback(var3, 1659042392);
                break;
            case CRUSHED_ORB:
                var1.setCrushedOrb(var3, 937486534);
                break;
            case GOLD_GEM_SOULBOUND:
                var1.setGoldGemSoulbound(var3, 716024532);
                break;
            case TRADER:
                var1.setTraderRecipe(var3, 196837014);
                if (var3) {
                    this.plugin.recipes(942436690).registerTrader(475248342);
                } else {
                    this.plugin.recipes(942436690).unregisterTrader(1677158148);
                }
                break;
            case UPGRADER:
                var1.setUpgraderRecipe(var3, 477819128);
                if (var3) {
                    this.plugin.recipes(942436690).registerUpgrader(406940308);
                } else {
                    this.plugin.recipes(942436690).unregisterUpgrader(1437158839);
                }
                break;
            case RESTORATION_CORE_RECIPE:
                var1.setRestorationCoreRecipe(var3, 493974721);
                if (var3) {
                    this.plugin.recipes(942436690).registerRestorationCore(1174323205);
                } else {
                    this.plugin.recipes(942436690).unregisterRestorationCore(1384458554);
                }
                break;
            case NETHERITE:
                var1.setNetheriteAllowed(var3, 891609125);
                break;
            case ENDERPEARLS:
                var1.setEnderpearls(var3, 567146737);
                break;
            case TROPHY_THUNDER:
                var1.setTrophyThunder(var3, 573547549);
                break;
            case TROPHY_THUNDER_ALL:
                var1.setTrophyThunderAllStages(var3, 1599776896);
                break;
            case PLAYER_HEAD_DROPS:
                var1.setPlayerHeadDrops(var3, 1774439989);
                break;
            case KEEP_TIER2_ON_DEATH:
                var1.setKeepTier2OnDeath(var3, 1609445366);
                if (!var1.isDream(1952413142)) {
                    this.remintUpgraderRecipe(21230971);
                }
                break;
            case TIER2_ON_JOIN:
                var1.setTier2OnJoin(var3, 191934853);
                if (!var1.isDream(1952413142)) {
                    this.remintUpgraderRecipe(21230971);
                }
                break;
            case ENCH_OBSIDIAN_GLOW:
                var1.setEnchObsidianGlow(var3, 681825737);
                break;
            case MYTHIC_DRAGON_EGG:
                var1.setMythicDragonEgg(var3, 1215149356);
                break;
            case PROG_ARMOR_LIMIT:
                var1.setProgArmorLimit(var3, 2016363559);
                break;
            case PROG_KIT_LIMIT:
                var1.setProgKitLimit(var3, 68195459);
                break;
            case KIT_ENCHANT_LIMITS:
                var1.setKitEnchantLimits(var3, 796716491);
                break;
            case PROG_ENCHANT_LIMIT:
                var1.setProgEnchantLimit(var3, 999545990);
                break;
            case MEMORY_PROXIMITY_SOUND:
                var1.setMemoryProximitySound(var3, 1594822047);
        }
    }

    private void remintUpgraderRecipe(int var1) {
        if (this.plugin.recipes(942436690).isUpgraderRegistered(1232619566)) {
            this.plugin.recipes(942436690).unregisterUpgrader(1437158839);
            this.plugin.recipes(942436690).registerUpgrader(406940308);
        }
    }

    private void restampSpeedGems(int var1) {
        for (Player var9 : Bukkit.getOnlinePlayers()) {
            PlayerDataStore var21 = this.plugin.players(1969902410);
            UUID var37 = var9.getUniqueId();
            PlayerData var10 = var21.get(var37, 1264010596);
            GemType var24 = var10.gem(1413186337);
            GemType var38 = GemType.SPEED;
            if (var24 == var38) {
                ItemStack[] var11 = var9.getInventory().getContents();
                int var12 = 0;

                while (true) {
                    int var40 = var11.length;
                    if (var12 >= var40) {
                        break;
                    }

                    if (var11[var12] != null) {
                        int var34 = BlissCoreItems.gemIdOf(var11[var12], 382441079);
                        int var44 = GemType.SPEED.id();
                        if (var34 == var44) {
                            PlayerInventory var36 = var9.getInventory();
                            BlissCoreItems var47 = this.plugin.coreItems(401009135);
                            GemType var5 = GemType.SPEED;
                            boolean var49 = var10.tier2(629984660);
                            int var50 = var10.energy(1160853318);
                            ItemStack var48 = var47.gemItem(var5, var49, var50, 256965471);
                            var36.setItem(var12, var48);
                        }
                    }

                    var12++;
                }
            }
        }
    }

    private ItemStack baseIcon(ConfigMenuListener.Toggle var1, int var2) {
        BlissCoreItems var7 = this.plugin.coreItems(401009135);

        return switch (var1) {
            case ENERGY_RESTRICTIONS -> var7.bottledEnergy(1532783840);
            case NO_ENERGY -> {
                Material var39 = Material.NETHER_STAR;
                yield new ItemStack(var39);
            }
            case BROKEN_DAMAGE -> {
                GemType var30 = GemType.STRENGTH;
                yield var7.gemItem(var30, true, 1, 256965471);
            }
            case BOTTLED_ENERGY_EXPIRY -> {
                ItemStack var8 = var7.bottledEnergy(1532783840);
                var7.makeUseless(var8, 607150677);
                yield var8;
            }
            case WATT_DEPOSIT -> {
                Material var43 = Material.CHEST;
                yield new ItemStack(var43);
            }
            case SPAWN_GEM_DISABLE -> {
                Material var60 = Material.RESPAWN_ANCHOR;
                yield new ItemStack(var60);
            }
            case BUNDLE_GEM_PROTECTION -> {
                Material var66 = Material.BUNDLE;
                yield new ItemStack(var66);
            }
            case ALLOW_OVERCHARGE -> {
                GemType var29 = GemType.FLUX;
                yield var7.gemItem$1963390319(var29, true, 975160804);
            }
            case STRENGTH_TWO -> {
                GemType var35 = GemType.STRENGTH;
                yield var7.gemItem$1963390319(var35, true, 975160804);
            }
            case LEGACY_SPEED -> {
                GemType var32 = GemType.SPEED;
                yield var7.gemItem$1963390319(var32, true, 975160804);
            }
            case LEGACY_PUFF -> {
                GemType var33 = GemType.PUFF;
                yield var7.gemItem$1963390319(var33, true, 975160804);
            }
            case LEGACY_AURATUS -> MythicItems.aegisGem(1323939072);
            case GOLD_LEGACY_SPEED -> {
                Material var45 = Material.GOLDEN_BOOTS;
                yield new ItemStack(var45);
            }
            case AURATUS_CHAIN_LOCK -> {
                Material var53 = chainMaterial(529836884);
                yield new ItemStack(var53);
            }
            case SNEAK_CAST -> {
                Material var38 = Material.LEATHER_BOOTS;
                yield new ItemStack(var38);
            }
            case HERETIC_RICOCHET_HALF -> {
                Material var80 = Material.NETHERITE_SWORD;
                yield new ItemStack(var80);
            }
            case PROGRESSION_GEMS -> {
                Material var36 = Material.CLOCK;
                yield new ItemStack(var36);
            }
            case TRUE_DAMAGE -> {
                Material var51 = Material.NETHERITE_SWORD;
                yield new ItemStack(var51);
            }
            case MYTHIC_GEM_STRIPS_MAIN -> {
                Material var57 = Material.SHEARS;
                yield new ItemStack(var57);
            }
            case MYTHIC_GEMS -> MythicItems.bloodGem(1434502583);
            case JUGGERNAUT_ROLE -> {
                Material var70 = Material.NETHERITE_CHESTPLATE;
                yield new ItemStack(var70);
            }
            case EVENT_PROGRESSION -> {
                Material var49 = Material.BEACON;
                yield new ItemStack(var49);
            }
            case STRENGTH_PROG1_FULL -> {
                GemType var31 = GemType.STRENGTH;
                yield var7.gemItem$1963390319(var31, false, 975160804);
            }
            case TIER2_GLOW -> {
                Material var46 = Material.GLOW_INK_SAC;
                yield new ItemStack(var46);
            }
            case GEM_AUTOENCHANT_ALL -> {
                Material var40 = Material.ENCHANTING_TABLE;
                yield new ItemStack(var40);
            }
            case BOSS_PROGRESSION -> {
                Material var63 = Material.DRAGON_HEAD;
                yield new ItemStack(var63);
            }
            case PROG0_GEMS -> {
                Material var69 = Material.AMETHYST_CLUSTER;
                yield new ItemStack(var69);
            }
            case RAID_ADVANCES_PROG -> {
                Material var56 = Material.CROSSBOW;
                yield new ItemStack(var56);
            }
            case DRAGON_GAMES_HOLDER -> {
                Material var67 = Material.DRAGON_EGG;
                yield new ItemStack(var67);
            }
            case VILLAGERS -> {
                Material var71 = Material.VILLAGER_SPAWN_EGG;
                yield new ItemStack(var71);
            }
            case CLUE_AUDIO -> {
                Material var76 = Material.MUSIC_DISC_5;
                yield new ItemStack(var76);
            }
            case LOST_SOULS_AUDIO -> {
                Material var61 = Material.GOAT_HORN;
                yield new ItemStack(var61);
            }
            case TRACKING_COMPASSES -> {
                Material var68 = Material.COMPASS;
                yield new ItemStack(var68);
            }
            case CONTROL_ROOM_AUDIENCE -> {
                Material var58 = Material.SPYGLASS;
                yield new ItemStack(var58);
            }
            case SHADER_EFFECTS -> {
                Material var74 = Material.TINTED_GLASS;
                yield new ItemStack(var74);
            }
            case FINALE_KNOCKBACK -> {
                Material var48 = Material.WIND_CHARGE;
                yield new ItemStack(var48);
            }
            case CRUSHED_ORB -> {
                Material var55 = Material.HEART_OF_THE_SEA;
                yield new ItemStack(var55);
            }
            case GOLD_GEM_SOULBOUND -> GoldGem.create(485535734);
            case TRADER -> var7.trader(662298763);
            case UPGRADER -> var7.upgrader(2125662274);
            case RESTORATION_CORE_RECIPE -> var7.restorationCore(913365449);
            case NETHERITE -> {
                Material var77 = Material.NETHERITE_PICKAXE;
                yield new ItemStack(var77);
            }
            case ENDERPEARLS -> {
                Material var59 = Material.ENDER_PEARL;
                yield new ItemStack(var59);
            }
            case TROPHY_THUNDER -> {
                Material var42 = Material.LIGHTNING_ROD;
                yield new ItemStack(var42);
            }
            case TROPHY_THUNDER_ALL -> {
                Material var78 = Material.TRIAL_KEY;
                yield new ItemStack(var78);
            }
            case PLAYER_HEAD_DROPS -> {
                Material var82 = Material.PLAYER_HEAD;
                yield new ItemStack(var82);
            }
            case KEEP_TIER2_ON_DEATH -> {
                Material var47 = Material.TOTEM_OF_UNDYING;
                yield new ItemStack(var47);
            }
            case TIER2_ON_JOIN -> {
                GemType var4 = GemType.LIFE;
                yield var7.gemItem$1963390319(var4, true, 975160804);
            }
            case ENCH_OBSIDIAN_GLOW -> {
                Material var75 = Material.OBSIDIAN;
                yield new ItemStack(var75);
            }
            case MYTHIC_DRAGON_EGG -> {
                Material var54 = Material.DRAGON_EGG;
                yield new ItemStack(var54);
            }
            case PROG_ARMOR_LIMIT -> {
                Material var64 = Material.NETHERITE_CHESTPLATE;
                yield new ItemStack(var64);
            }
            case PROG_KIT_LIMIT -> {
                Material var73 = Material.BUNDLE;
                yield new ItemStack(var73);
            }
            case KIT_ENCHANT_LIMITS -> {
                Material var41 = Material.ENCHANTING_TABLE;
                yield new ItemStack(var41);
            }
            case PROG_ENCHANT_LIMIT -> {
                Material var81 = Material.ENCHANTED_BOOK;
                yield new ItemStack(var81);
            }
            case MEMORY_PROXIMITY_SOUND -> {
                Material var52 = Material.NOTE_BLOCK;
                yield new ItemStack(var52);
            }
            default -> {
                MatchException var19 = new MatchException(null, null);
                throw var19;
            }
        };
    }

    private ItemStack categoryButton(ConfigMenuListener.Category var1, int var2) {
        return switch (var1) {
            case ENERGY -> this.plugin.coreItems(401009135).bottledEnergy(1532783840);
            case GEMS -> {
                BlissCoreItems var12 = this.plugin.coreItems(401009135);
                GemType var4 = GemType.PUFF;
                yield var12.gemItem$1963390319(var4, true, 975160804);
            }
            default -> {
                Material var15 = var1.button;
                yield new ItemStack(var15);
            }
        };
    }

    private ItemStack icon(ConfigMenuListener.Toggle var1, boolean var2, int var3) {
        ItemStack var8 = this.baseIcon(var1, 856503059);
        ItemMeta var9 = var8.getItemMeta();
        if (var9 != null) {
            String var64;
            if (var2) {
                var64 = "&a";
            } else {
                var64 = "&c";
            }

            String var65 = var1.name;
            Component var43 = Text.legacy(var64 + "&l" + var65, 373192400);
            TextDecoration var66 = TextDecoration.ITALIC;
            Component var44 = var43.decoration(var66, false);
            var9.displayName(var44);
            ArrayList var18 = new ArrayList();
            ArrayList var10 = var18;

            for (String var12 : wrap(var1.desc, 48, 272478660)) {
                Component var62 = Text.legacy("&7" + var12, 373192400);
                TextDecoration var69 = TextDecoration.ITALIC;
                Component var63 = var62.decoration(var69, false);
                var10.add(var63);
            }

            TextComponent var46 = Component.empty();
            var10.add(var46);
            String var48;
            if (var2) {
                var48 = "&aStatus: &l✔ ENABLED";
            } else {
                var48 = "&cStatus: &l✘ DISABLED";
            }

            Component var49 = Text.legacy(var48, 373192400);
            TextDecoration var67 = TextDecoration.ITALIC;
            Component var50 = var49.decoration(var67, false);
            var10.add(var50);
            String var59;
            if (var2) {
                var59 = "disable";
            } else {
                var59 = "enable";
            }

            Component var53 = Text.legacy("&eClick to " + var59 + ".", 373192400);
            TextDecoration var68 = TextDecoration.ITALIC;
            Component var54 = var53.decoration(var68, false);
            var10.add(var54);
            var9.lore(var10);
            Boolean var57 = var2;
            var9.setEnchantmentGlintOverride(var57);
            var8.setItemMeta(var9);
        }

        return var8;
    }

    private static List<String> wrap(String var0, int var1, int var2) {
        ArrayList var3 = new ArrayList();
        ArrayList var4 = var3;
        StringBuilder var10 = new StringBuilder();
        StringBuilder var5 = var10;

        for (String var9 : var0.split(" ")) {
            if (var5.length() > 0) {
                int var28 = var5.length() + 1;
                int var46 = var9.length();
                int var29 = var28 + var46;
                if (var29 > var1) {
                    String var49 = var5.toString();
                    var4.add(var49);
                    var5.setLength(0);
                }
            }

            if (var5.length() > 0) {
                var5.append(' ');
            }

            var5.append(var9);
        }

        if (var5.length() > 0) {
            String var42 = var5.toString();
            var4.add(var42);
        }

        return var4;
    }

    private static ItemStack pane(int var0) {
        Material var2 = Material.GRAY_STAINED_GLASS_PANE;
        ItemStack var1 = new ItemStack(var2);
        ItemMeta var4 = var1.getItemMeta();
        if (var4 != null) {
            TextComponent var13 = Component.text(" ");
            var4.displayName(var13);
            var1.setItemMeta(var4);
        }

        return var1;
    }

    private static ItemStack label(Material var0, String var1, String[] var2, int var3) {
        ItemStack var4 = new ItemStack(var0);
        return label(var4, var1, var2, 1718725543);
    }

    private static ItemStack label(ItemStack var0, String var1, String[] var2, int var3) {
        ItemMeta var8 = var0.getItemMeta();
        if (var8 != null) {
            Component var31 = Text.legacy(var1, 373192400);
            TextDecoration var6 = TextDecoration.ITALIC;
            Component var32 = var31.decoration(var6, false);
            var8.displayName(var32);
            ArrayList var17 = new ArrayList();
            ArrayList var9 = var17;

            for (String var13 : var2) {
                Object var41;
                if (var13.isEmpty()) {
                    var41 = Component.empty();
                } else {
                    Component var38 = Text.legacy(var13, 373192400);
                    TextDecoration var42 = TextDecoration.ITALIC;
                    var41 = var38.decoration(var42, false);
                }

                var9.add(var41);
            }

            var8.lore(var9);
            var0.setItemMeta(var8);
        }

        return var0;
    }

    static {
        int[] var4 = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 32, 33, 34};
        PAGE_SLOTS = var4;
        ConfigMenuListener.Toggle var5 = ConfigMenuListener.Toggle.ENERGY_RESTRICTIONS;
        ConfigMenuListener.Toggle[] var8 = new ConfigMenuListener.Toggle[26];
        ConfigMenuListener.Toggle var3 = ConfigMenuListener.Toggle.NO_ENERGY;
        var8[0] = var3;
        ConfigMenuListener.Toggle var73 = ConfigMenuListener.Toggle.BROKEN_DAMAGE;
        var8[1] = var73;
        ConfigMenuListener.Toggle var74 = ConfigMenuListener.Toggle.SPAWN_GEM_DISABLE;
        var8[2] = var74;
        ConfigMenuListener.Toggle var75 = ConfigMenuListener.Toggle.BUNDLE_GEM_PROTECTION;
        var8[3] = var75;
        ConfigMenuListener.Toggle var76 = ConfigMenuListener.Toggle.ALLOW_OVERCHARGE;
        var8[4] = var76;
        ConfigMenuListener.Toggle var77 = ConfigMenuListener.Toggle.WATT_DEPOSIT;
        var8[5] = var77;
        ConfigMenuListener.Toggle var78 = ConfigMenuListener.Toggle.STRENGTH_TWO;
        var8[6] = var78;
        ConfigMenuListener.Toggle var79 = ConfigMenuListener.Toggle.LEGACY_SPEED;
        var8[7] = var79;
        ConfigMenuListener.Toggle var80 = ConfigMenuListener.Toggle.LEGACY_PUFF;
        var8[8] = var80;
        ConfigMenuListener.Toggle var81 = ConfigMenuListener.Toggle.GOLD_LEGACY_SPEED;
        var8[9] = var81;
        ConfigMenuListener.Toggle var82 = ConfigMenuListener.Toggle.PROGRESSION_GEMS;
        var8[10] = var82;
        ConfigMenuListener.Toggle var83 = ConfigMenuListener.Toggle.STRENGTH_PROG1_FULL;
        var8[11] = var83;
        ConfigMenuListener.Toggle var84 = ConfigMenuListener.Toggle.TIER2_GLOW;
        var8[12] = var84;
        ConfigMenuListener.Toggle var85 = ConfigMenuListener.Toggle.GEM_AUTOENCHANT_ALL;
        var8[13] = var85;
        ConfigMenuListener.Toggle var86 = ConfigMenuListener.Toggle.NETHERITE;
        var8[14] = var86;
        ConfigMenuListener.Toggle var87 = ConfigMenuListener.Toggle.ENDERPEARLS;
        var8[15] = var87;
        ConfigMenuListener.Toggle var88 = ConfigMenuListener.Toggle.PLAYER_HEAD_DROPS;
        var8[16] = var88;
        ConfigMenuListener.Toggle var89 = ConfigMenuListener.Toggle.KEEP_TIER2_ON_DEATH;
        var8[17] = var89;
        ConfigMenuListener.Toggle var90 = ConfigMenuListener.Toggle.TIER2_ON_JOIN;
        var8[18] = var90;
        ConfigMenuListener.Toggle var91 = ConfigMenuListener.Toggle.PROG0_GEMS;
        var8[19] = var91;
        ConfigMenuListener.Toggle var92 = ConfigMenuListener.Toggle.TROPHY_THUNDER;
        var8[20] = var92;
        ConfigMenuListener.Toggle var93 = ConfigMenuListener.Toggle.TROPHY_THUNDER_ALL;
        var8[21] = var93;
        ConfigMenuListener.Toggle var94 = ConfigMenuListener.Toggle.PROG_ARMOR_LIMIT;
        var8[22] = var94;
        ConfigMenuListener.Toggle var95 = ConfigMenuListener.Toggle.PROG_KIT_LIMIT;
        var8[23] = var95;
        ConfigMenuListener.Toggle var96 = ConfigMenuListener.Toggle.PROG_ENCHANT_LIMIT;
        var8[24] = var96;
        ConfigMenuListener.Toggle var97 = ConfigMenuListener.Toggle.MEMORY_PROXIMITY_SOUND;
        var8[25] = var97;
        DREAM_SCOPED = EnumSet.of(var5, var8);
    }

    private static enum Category {
        ENERGY(10, Material.EXPERIENCE_BOTTLE, Material.PURPLE_STAINED_GLASS_PANE, "&d&lEnergy", "&7Energy stages, bottles & break rules."),
        GEMS(11, Material.AMETHYST_SHARD, Material.MAGENTA_STAINED_GLASS_PANE, "&5&lGems", "&7Gem rules, which version runs, and the mythic module."),
        PROGRESSION(12, Material.LADDER, Material.YELLOW_STAINED_GLASS_PANE, "&e&lProgression", "&7The stage ladder & what advances it."),
        VILLAGER_EVENT(14, Material.VILLAGER_SPAWN_EGG, Material.LIME_STAINED_GLASS_PANE, "&a&lVillager Event", "&7The hunt, its audio & the Last Raid."),
        ASSESSMENT(15, Material.RECOVERY_COMPASS, Material.ORANGE_STAINED_GLASS_PANE, "&6&lThe Assessment", "&7The Honored One, the Control Room & the finale."),
        RULES(16, Material.CRAFTING_TABLE, Material.BLUE_STAINED_GLASS_PANE, "&9&lRules & Crafting", "&7Custom recipes and general world rules.");

        final int slot;
        final Material button;
        final Material header;
        final String name;
        final String blurb;

        private Category(int nullxx, Material nullxxx, Material nullxxxx, String nullxxxxx, String nullxxxxxx) {
            this.slot = nullxx;
            this.button = nullxxx;
            this.header = nullxxxx;
            this.name = nullxxxxx;
            this.blurb = nullxxxxxx;
        }

        static ConfigMenuListener.Category bySlot(int var0) {
            for (ConfigMenuListener.Category var6 : values()) {
                int var14 = var6.slot;
                if (var14 == var0) {
                    return var6;
                }
            }

            return null;
        }
    }

    private static final class MainHolder implements InventoryHolder {
        final boolean dream;
        private Inventory inv;

        MainHolder(boolean var1, int var2) {
            this.dream = var1;
        }

        public Inventory getInventory() {
            return this.inv;
        }
    }

    private static final class PageHolder implements InventoryHolder {
        final ConfigMenuListener.Category category;
        final boolean dream;
        final Map<Integer, ConfigMenuListener.Toggle> slots;
        private Inventory inv;

        PageHolder(ConfigMenuListener.Category var1, boolean var2, int var3) {
            HashMap var5 = new HashMap();
            this.slots = var5;
            this.category = var1;
            this.dream = var2;
        }

        public Inventory getInventory() {
            return this.inv;
        }
    }

    private static enum Toggle {
        ENERGY_RESTRICTIONS(Category.ENERGY, "Energy Restrictions", "Energy-stage gating of passives, powers & abilities."),
        NO_ENERGY(Category.ENERGY, "No Energy Mode", "Disables the energy system: every gem stays at Pristine + 5 and never drops on death. Perfect for SMPs or FFA servers."),
        BROKEN_DAMAGE(Category.ENERGY, "Broken Double Damage", "2x gem damage while in the Broken energy state."),
        BOTTLED_ENERGY_EXPIRY(Category.ENERGY, "Bottled Energy Expiry", "Whether stored bottled energy expires (turns to grey \"Useless\" juice)."),
        WATT_DEPOSIT(Category.ENERGY, "Watt Deposit", "How the Flux gem is charged. ON = press Q on a tier-2 Flux gem to open the Watt Deposit, fill it with fuel, and it burns one item a second on the action bar once you shut it (anything that isn't fuel comes straight back). OFF = the old way, gem in the off hand and a right-click per item. Never both at once."),
        SPAWN_GEM_DISABLE(Category.GEMS, "Spawn Gem Disable", "Disable all gem passives, powers & abilities inside the WorldGuard \"spawn\" region."),
        BUNDLE_GEM_PROTECTION(Category.GEMS, "Bundle Gem Protection", "Blocks hiding gems inside bundles (a bundled gem slips past every gem-keeping rule). Off = 1:1 with the original server, where the bundle trick was a known bug."),
        ALLOW_OVERCHARGE(Category.GEMS, "Flux Overcharge", "Whether the Flux gem may charge past 100% into the overcharge state (the 100-200% range and the 200% ultra beam). Off = charging tops out at 100% and the gem never overcharges."),
        STRENGTH_TWO(Category.GEMS, "Strength II Passive", "ON = the Strength gem's always-on passive is Strength II instead of Strength I (the Prog 1 rule still decides whether the passive applies at all). OFF = Strength I."),
        LEGACY_SPEED(Category.GEMS, "Legacy Speed Gem", "ON = the original Speed gem (Terminal Velocity, Speedy Storm, old Blur, Soul Speed enchant). OFF = the reworked gem (Thunder Step, Speed Storm, Blur combo). Anything already running finishes; new casts use the selected version. Gem lore follows."),
        LEGACY_PUFF(Category.GEMS, "Legacy Puff Gem", "ON = the Puff gem's older, plainer effects (no launch gust/sound, no dash landing impact or trail, no ground tilt on Breezy Bash). Mechanics are identical either way — this only changes the visuals."),
        LEGACY_AURATUS(Category.GEMS, "Legacy Auratus Gem", "ON = the original Auratus gem (full chain-link textures and hooked tip, slams only from a chain-down while airborne, no chain refunds, the old skyhook on Angels Grasp, the explosion slam). OFF = the reworked gem (chain refunds, forward slam throw, parry heals, pixel chains, the flag pole combo). Anything already running finishes; new casts use the selected version. Same as mythic-gems.auratus.legacy in config.yml."),
        GOLD_LEGACY_SPEED(Category.GEMS, "Gold Gem Uses Legacy Speed", "ON = once the Gold Gem has absorbed the Speed soul, wielding it runs the ORIGINAL Speed gem (Terminal Velocity, Speedy Storm, the NPC-clone Blur, Soul Speed) even on a server whose ordinary Speed gem is the rework. OFF = the Gold Gem's Speed soul follows the server-wide Legacy Speed setting like everything else. Only affects the soul while it is being wielded — a player's own Speed gem is untouched."),
        AURATUS_CHAIN_LOCK(Category.GEMS, "Auratus Chain Lock", "ON = after a chain is handed back (it hooked nothing and its slam connected with nothing) the next Hauling Strike is locked for 1 second, silently — it stops the refund being farmed for the slam's forward throw. OFF = throw again immediately, with no wait between chains. Reworked gem only; the legacy gem never refunds a chain. Same as mythic-gems.auratus.refund-lock in config.yml."),
        SNEAK_CAST(Category.GEMS, "Sneak Cast Abilities", "ON = the middle ability on each gem's action bar arms on ONE right-click while SNEAKING with the gem in the off hand, instead of a double right-click: Thunder Step, Crisp, Vitality Vortex, Wealth Pockets, Kinetic Burst and Bounty Hunter. OFF = the double right-click they shipped with. Astra's Dimensional Drift keeps its double-click either way — a sneaking right-click on that gem is already Soul Capture's re-summon, and the two would fire together. Everything else about the abilities is unchanged: same hand, same cooldowns, same sword/axe/empty-hand rule."),
        HERETIC_RICOCHET_HALF(Category.GEMS, "Half Ricochet Damage", "ON = a Heretic blood saw that has bounced off a block deals HALF its damage for the rest of its flight. A saw ricochets up to 3 times and forgets what it already pierced on every bounce, so one thrown into a corner comes back through the same person again; this makes those return hits cost half. OFF = a bounced saw hits exactly as hard as a direct one. The 30% cut on a second saw striking the same target within 2 seconds applies either way, on top of this. Same as mythic-gems.heretic.ricochet-half in config.yml."),
        PROGRESSION_GEMS(Category.GEMS, "Gem Version by Progression", "ON = the Speed & Puff versions follow the server's Progression: stages 1-3 run the legacy gems, 4 and up the reworked ones. Overrides the two toggles beside this one while it is on (before Progression 1 they still apply)."),
        TRUE_DAMAGE(Category.GEMS, "True Damage", "ON = every gem AND mythic ability deals the SAME to everyone: the damage number in config.yml lands as written, whether the victim is naked or in enchanted netherite. Their own armour, toughness, Protection and Feather Falling stop deciding anything. OFF = the graded behaviour, where the number is the hit before armour, like a sword's, and each ability is reduced by a different part of the victim's real kit. Totems, Resistance and absorption still apply either way, and so do the holder's energy scale, the Honored One dial and the per-ability caps. Ordinary sword hits and the gem passives that ride them are untouched, and so is Puff's Breezy Bash, which lands as real fall damage and is still decided by armour and Feather Falling. The shipped numbers were tuned for ON; turned OFF they will feel weak. A few keys are multipliers or caps rather than HP; each config comment says which."),
        MYTHIC_GEM_STRIPS_MAIN(Category.GEMS, "Mythic Gem Strips Main Gem", "ON = a player who is not a Juggernaut and comes to carry a Heretic or Auratus gem has their own Bliss gem taken away and remembered (the gem type — tier and energy stay theirs meanwhile); it comes back the moment they no longer carry a mythic gem, or when this is switched OFF, online or on their next join. OFF = they keep both, and anything taken comes back."),
        MYTHIC_GEMS(Category.GEMS, "Mythic Gems", "The built-in Heretic & Auratus gems, /mythics, and their menu buttons. Off = the base plugin runs exactly as it did before they were merged in. TAKES EFFECT ON RESTART."),
        JUGGERNAUT_ROLE(Category.GEMS, "Juggernaut Role", "The Heretic/Auratus Juggernauts: bindings, the double-health cursed kit, the death-ban, the soul-tether revive and the pedestal gem revival. The GEMS still work with this off. TAKES EFFECT ON RESTART."),
        EVENT_PROGRESSION(Category.PROGRESSION, "Event Progression", "Master switch: whether ANY in-game event auto-advances the server's Progression (Wither kill → 2, Ender Dragon kill → 3, villager raid or /blissevent end → 4, the Honored One's departure → 5). Off = every stage is advanced manually via /bliss progression. The Dragon Games never advance it either way."),
        STRENGTH_PROG1_FULL(Category.PROGRESSION, "Prog 1 Strength I", "Whether the Strength gem grants full Strength I during Progression 1. Off = a gentler +1.5 melee boost so the diamond kit can't 2-shot."),
        TIER2_GLOW(Category.PROGRESSION, "Tier 2 Wielder Glow", "ON = anyone carrying an upgraded (Tier 2) gem glows in that gem's colour during Progression 1 and 2, so others can spot them. OFF = no glow."),
        GEM_AUTOENCHANT_ALL(Category.PROGRESSION, "Gem Auto-Enchants Every Stage", "ON = the gem passives auto-enchant gear on EVERY Progression, 1 and 2 included, and the stage sweeps leave what a gem granted alone (at the level it granted). OFF = the pre-made rules: nothing on Progression 1, only Fire Aspect / Flame / Punch on Progression 2, everything from 3."),
        BOSS_PROGRESSION(Category.PROGRESSION, "Boss Kills → Progression", "Whether killing the Wither (→ Progression 2) and the Ender Dragon (→ Progression 3) advance the server's progression (first kill only)."),
        PROG0_GEMS(Category.PROGRESSION, "Stage 0 Hands Out Gems", "What a join does while the server sits at Progression 0 (where /bliss progression 0 leaves it). OFF = nobody is handed a gem: stage 0 is the pre-season state and the roulette at Progression 1 is what gives everyone theirs. Players already carrying a gem keep it. ON = the old behaviour, where anyone who had not rolled yet is given a random gem the moment they log in."),
        RAID_ADVANCES_PROG(Category.PROGRESSION, "Raid Ends → Progression 4", "Whether ending the villager raid auto-advances the server to Progression 4."),
        DRAGON_GAMES_HOLDER(Category.PROGRESSION, "Dragon Games at Prog 3", "ON = from Progression 3 the Dragon Egg holder is asked \"Do you wish to start the Dragon Games?\" about 3 days after picking the egg up, and may start them ONCE with /dragon games start. OFF = neither — only an admin starts them (/bliss dragongames start). The games never advance the Progression either way."),
        VILLAGERS(Category.VILLAGER_EVENT, "Villagers", "Villager natural SPAWNING and TRADING, server-wide (the 3 custom event villagers stay tradeable either way). Setting this by hand LOCKS it: the progression and the villager event stop writing it, so reaching a stage can never undo your choice. /bliss progression reset puts it back on automatic."),
        CLUE_AUDIO(Category.VILLAGER_EVENT, "Village Clue Audio", "The whispered clue played from the summon disc."),
        LOST_SOULS_AUDIO(Category.VILLAGER_EVENT, "Lost Souls Audio", "The Lost Souls narration during the villager event."),
        TRACKING_COMPASSES(Category.ASSESSMENT, "Tracking Compasses", "Whether the Honored One compasses track the HO while the Assessment window is open."),
        CONTROL_ROOM_AUDIENCE(Category.ASSESSMENT, "Control Room Audience", "Whether the WHOLE SERVER watches the Honored One's decree: every other player made a spectator and brought to the Control Room on day 11, then set down around the pedestal at the departure. Off = he goes alone and nobody else is moved."),
        SHADER_EFFECTS(Category.ASSESSMENT, "Shader Screen Effects", "The gold pov veils and full-screen covers of the Assessment, the finale and the orb. They are drawn by the BlissGoldRitual Iris pack via a per-player clock; without that pack they show nothing but still freeze each viewer's sky. Off = no player's clock is ever touched."),
        FINALE_KNOCKBACK(Category.ASSESSMENT, "Finale Knockback", "Whether the pedestal finale throws every entity within 40 blocks and holds players in its slow-motion window. The cinematic still plays either way — this is only whether it moves anybody."),
        CRUSHED_ORB(Category.ASSESSMENT, "Crushed Pedestal Orb", "ON = the finale's climax raises the monument: the pedestal buckled under a standing orb, with the gem-coloured dust snakes winding round it. OFF = the decree still seals the pedestal, the orb simply never goes up. One already standing is left where it is, and /bliss orb still places a free one by hand."),
        GOLD_GEM_SOULBOUND(Category.ASSESSMENT, "Gold Gem Soulbound", "ON = the Gold Gem never drops on death: the holder keeps it with only their own base soul, every other absorbed soul goes back to the player it was harvested from, and the awakening starts over. OFF = it drops on death like any item."),
        TRADER(Category.RULES, "Trader Recipe", "Crafting recipe for the Gem Trader head."),
        UPGRADER(Category.RULES, "Upgrader Recipe", "Crafting recipe for the Gem Upgrader head."),
        RESTORATION_CORE_RECIPE(Category.RULES, "Restoration Core Recipe", "Crafting recipe for the Restoration Core item."),
        NETHERITE(Category.RULES, "Netherite Gear", "Crafting netherite tools & armor."),
        ENDERPEARLS(Category.RULES, "Ender Pearls", "Whether players may throw ender pearls."),
        TROPHY_THUNDER(Category.RULES, "Trophy Thunder & Roar", "The lightning bolt and the wither roar that go off where a trophy head drops. ON = both play, as they always have. OFF = the head still drops and still carries its \"Killed by\" line, it just lands in silence - no bolt, no roar."),
        TROPHY_THUNDER_ALL(Category.RULES, "Thunder & Roar Every Stage", "Where the bolt and roar above are allowed to play. OFF = only where a trophy head lands, which means Progression 5 (or any stage, if Player Head Drops is on). ON = on ANY player-vs-player kill at any progression, head or no head. Does nothing while Trophy Thunder & Roar is off."),
        PLAYER_HEAD_DROPS(Category.RULES, "Player Head Drops", "ON = a player killed by another player drops their own head — the Progression 5 trophy (their skin, a \"Killed by\" line, the lightning and the roar) — in ANY progression. OFF = only at Progression 5, as always."),
        KEEP_TIER2_ON_DEATH(Category.RULES, "Keep Tier 2 on Death", "ON = dying with an upgraded (Tier 2) gem costs nothing: no Upgrader drops at the body and the gem is still Tier 2 when you respawn. OFF = the Upgrader drops where you fell and your gem goes back to Tier 1, the way it always has."),
        TIER2_ON_JOIN(Category.RULES, "Tier 2 on Join", "ON = everyone joins with an upgraded (Tier 2) gem: a first-ever join is minted at Tier 2, and anyone still on Tier 1 is upgraded on their next login, gem item and all. While this is on, dying never drops an Upgrader either — the tier comes back at the next login, so the drop would only be a free item. A player whose gem is away (a Juggernaut, a harvested soul, a mythic carrier) is left alone. OFF = everyone starts at Tier 1 and upgrades with an Upgrader."),
        ENCH_OBSIDIAN_GLOW(Category.RULES, "Enchanted Obsidian Glow", "The yellow glow outline on placed enchanted obsidian (the block works either way)."),
        MYTHIC_DRAGON_EGG(Category.RULES, "Dragon Egg Halves Mythics", "A Dragon Egg halves the base gems' cooldowns. ON extends that to the Heretic and Auratus abilities too; OFF (the default) leaves mythic cooldowns alone."),
        PROG_ARMOR_LIMIT(Category.RULES, "Progression Armor Limit", "ON (the default) = the progression's armor limit is enforced: netherite gear cannot be made at the smithing table until the stage allows it. OFF = netherite is craftable at every stage, whatever Netherite Gear says, and no stage change turns this back on."),
        PROG_KIT_LIMIT(Category.RULES, "Progression Kit Limit", "ON (the default) = the progression's kit limit is enforced: Progression 2's banned items and carry limits, and the ender-pearl ban. OFF = none of that applies at any stage, and no stage change turns this back on."),
        KIT_ENCHANT_LIMITS(Category.RULES, "Kit Enchant Limits", "The HONORED ONE'S decreed enchantment caps and bans - a separate thing from the progression's limit below, which is why turning that one off never lifted these. ON (the default) = whatever was decreed at the Control Room's enchant-limit stand is enforced on carried items, at the enchanting table and at the anvil. OFF = every decreed cap is lifted at once and a sword may keep Sharpness V; nothing is forgotten, so turning this back on re-applies the same decree."),
        PROG_ENCHANT_LIMIT(Category.RULES, "Progression Enchant Limit", "ON (the default) = the progression's enchant limit is enforced: the Progression 1 strip, the Progression 2 caps and bans (table, anvil, sweep and on-hit), the Progression 3 downgrade. OFF = enchants are never touched by the stage, and no stage change turns this back on. The Honored One's decree is separate and still applies."),
        MEMORY_PROXIMITY_SOUND(Category.RULES, "Memory Proximity Sound", "The Golden Dream's cue. ON (the default) = when a memory closes to within a few blocks of an intruder, the pack's \"alternate\" sound plays there for everyone nearby, at most once every twenty seconds per intruder. OFF = the dream stays silent. A memory's own /alternate is separate and always works. Dream-scoped: set it in /goldendream config.");

        final ConfigMenuListener.Category category;
        final String name;
        final String desc;

        private Toggle(ConfigMenuListener.Category nullxx, String nullxxx, String nullxxxx) {
            this.category = nullxx;
            this.name = nullxxx;
            this.desc = nullxxxx;
        }

    }
}
