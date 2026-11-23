package com.bliss;

import com.bliss.admin.AssessmentCommand;
import com.bliss.admin.BlissCommand;
import com.bliss.admin.BossDropsListener;
import com.bliss.admin.ConfigMenuListener;
import com.bliss.admin.MenuListener;
import com.bliss.admin.PvpCommand;
import com.bliss.admin.RecipeBookListener;
import com.bliss.admin.TasksCommand;
import com.bliss.admin.TrustCommand;
import com.bliss.admin.WithdrawCommand;
import com.bliss.core.AchievementStore;
import com.bliss.core.AstraSoulStore;
import com.bliss.core.AtomicYml;
import com.bliss.core.BlissAdvancements;
import com.bliss.core.BottledEnergyExpiryTask;
import com.bliss.core.BottledEnergyListener;
import com.bliss.core.BrokenActionBarTask;
import com.bliss.core.BrokenStatePenaltyListener;
import com.bliss.core.DragonGamesHook;
import com.bliss.core.DreamContextListener;
import com.bliss.core.EnderpearlAndSweepListener;
import com.bliss.core.GemAutoEnchants;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.MaceRestrictionsListener;
import com.bliss.core.MegaMaceListener;
import com.bliss.core.NetheriteSmithingListener;
import com.bliss.core.PlayerDataStore;
import com.bliss.core.PlayerDeathListener;
import com.bliss.core.PlayerJoinListener;
import com.bliss.core.PovOverlay;
import com.bliss.core.Progression2RestrictionListener;
import com.bliss.core.ProgressionCraftGateListener;
import com.bliss.core.ProgressionDamageListener;
import com.bliss.core.TagStore;
import com.bliss.core.TeamManager;
import com.bliss.core.TrustStore;
import com.bliss.core.UpgraderGlowTask;
import com.bliss.core.VillagerSpawnGateListener;
import com.bliss.core.VillagerTradeGateListener;
import com.bliss.core.WorldGuardHook;
import com.bliss.entity.EntityMotifs;
import com.bliss.entity.MotifSpeechListener;
import com.bliss.environment.EnchantedObsidianTask;
import com.bliss.environment.EnvironmentListener;
import com.bliss.environment.ReinforcedPortalListener;
import com.bliss.gems.AstraAuraTask;
import com.bliss.gems.AstraDaggersListener;
import com.bliss.gems.AstraDimensionalDriftListener;
import com.bliss.gems.AstraGemListener;
import com.bliss.gems.AstraLifecycleListener;
import com.bliss.gems.AstraPassivesListener;
import com.bliss.gems.AstraProjectionListener;
import com.bliss.gems.AstraVoidListener;
import com.bliss.gems.BountyHunterListener;
import com.bliss.gems.ConsistentDamage;
import com.bliss.gems.DragonEggBuffTask;
import com.bliss.gems.EntityClickGuardListener;
import com.bliss.gems.FireAuraTask;
import com.bliss.gems.FireCozyHealTask;
import com.bliss.gems.FireCozyListener;
import com.bliss.gems.FireCrispBurnTask;
import com.bliss.gems.FireCrispListener;
import com.bliss.gems.FireFireballListener;
import com.bliss.gems.FireGemListener;
import com.bliss.gems.FirePassivesListener;
import com.bliss.gems.FluxAuraTask;
import com.bliss.gems.FluxChargingCommand;
import com.bliss.gems.FluxCopperTeleportListener;
import com.bliss.gems.FluxFreezeDeathListener;
import com.bliss.gems.FluxGemListener;
import com.bliss.gems.FluxGroundAbilityListener;
import com.bliss.gems.FluxKineticBurstListener;
import com.bliss.gems.FluxKineticOverdriveListener;
import com.bliss.gems.FluxRechargeListener;
import com.bliss.gems.GemAuraTask;
import com.bliss.gems.GemProtectionListener;
import com.bliss.gems.LifeAnimalListener;
import com.bliss.gems.LifeAuraTask;
import com.bliss.gems.LifeConsumeListener;
import com.bliss.gems.LifeGemListener;
import com.bliss.gems.LifePowersListener;
import com.bliss.gems.LifeVitalityVortexListener;
import com.bliss.gems.ProjectileListener;
import com.bliss.gems.PuffAbilitiesListener;
import com.bliss.gems.PuffAuraTask;
import com.bliss.gems.PuffFlightTask;
import com.bliss.gems.PuffGemListener;
import com.bliss.gems.SpeedAuraTask;
import com.bliss.gems.SpeedBlurListener;
import com.bliss.gems.SpeedGemListener;
import com.bliss.gems.SpeedStormListener;
import com.bliss.gems.SpeedThunderStepListener;
import com.bliss.gems.StrengthGemListener;
import com.bliss.gems.WattDepositListener;
import com.bliss.gems.WattsCommand;
import com.bliss.gems.WealthAuraTask;
import com.bliss.gems.WealthGemListener;
import com.bliss.gems.WealthPassivesListener;
import com.bliss.gems.WealthPocketsListener;
import com.bliss.gems.legacy.LegacyBlurListener;
import com.bliss.gems.legacy.LegacySoulSpeedTask;
import com.bliss.gems.legacy.LegacySpeedAuraTask;
import com.bliss.gems.legacy.LegacySpeedState;
import com.bliss.gems.legacy.LegacySpeedStormListener;
import com.bliss.gems.legacy.LegacyTerminalVelocityListener;
import com.bliss.goldgem.DreamChannels;
import com.bliss.goldgem.DreamTabList;
import com.bliss.goldgem.FragmentCoreRitual;
import com.bliss.goldgem.GoldGem;
import com.bliss.goldgem.GoldGemCommand;
import com.bliss.goldgem.GoldGemHarvestListener;
import com.bliss.goldgem.GoldGemItemGuardListener;
import com.bliss.goldgem.GoldGemListener;
import com.bliss.goldgem.GoldGemManager;
import com.bliss.goldgem.GoldGemTrimTask;
import com.bliss.goldgem.GoldenDreamCommand;
import com.bliss.goldgem.GoldenDreamMenu;
import com.bliss.goldgem.GoldenDreamRitual;
import com.bliss.goldgem.GoldenDreamWorld;
import com.bliss.goldgem.VictimCommand;
import com.bliss.goldgem.WakeFloat;
import com.bliss.honored.ArmorLimitHologram;
import com.bliss.honored.ArmorLimits;
import com.bliss.honored.AssessmentDay;
import com.bliss.honored.AssessmentWindow;
import com.bliss.honored.AssessmentWindowBarTask;
import com.bliss.honored.CageManager;
import com.bliss.honored.CageTask;
import com.bliss.honored.ControlRoomDeparturePrompt;
import com.bliss.honored.ControlRoomHologramKeeper;
import com.bliss.honored.ControlRoomManager;
import com.bliss.honored.ControlRoomVeil;
import com.bliss.honored.DepartureFlight;
import com.bliss.honored.EnchantLimitHologram;
import com.bliss.honored.EnchantLimits;
import com.bliss.honored.GemAbilityRules;
import com.bliss.honored.HonoredCompass;
import com.bliss.honored.HonoredCompassTask;
import com.bliss.honored.HonoredNameplateTask;
import com.bliss.honored.HonoredOneAuraTask;
import com.bliss.honored.HonoredOneListener;
import com.bliss.honored.HonoredOneManager;
import com.bliss.honored.HonoredOneState;
import com.bliss.honored.HonoredSummonRitual;
import com.bliss.honored.JuggernautGemGuardListener;
import com.bliss.honored.KitRestrictionListener;
import com.bliss.honored.KitRules;
import com.bliss.honored.PedestalFinale;
import com.bliss.honored.PotionLimitHologram;
import com.bliss.honored.PotionLimits;
import com.bliss.honored.WorldBorderHologram;
import com.bliss.items.BlissCoreItems;
import com.bliss.items.BlissRecipes;
import com.bliss.items.PendingGemStore;
import com.bliss.mythics.MythicMainGemTask;
import com.bliss.mythics.MythicsModule;
import com.bliss.nick.AlternateCommand;
import com.bliss.nick.MemoryNpcDeathListener;
import com.bliss.nick.MemoryNpcManager;
import com.bliss.nick.MemoryRoster;
import com.bliss.nick.NickCommand;
import com.bliss.nick.NickManager;
import com.bliss.pedestal.CrushedPedestalOrb;
import com.bliss.pedestal.PedestalGlowTask;
import com.bliss.pedestal.PedestalListener;
import com.bliss.pedestal.PedestalReviveRitual;
import com.bliss.pedestal.PedestalReviveTriggerListener;
import com.bliss.pedestal.PedestalRitualRunner;
import com.bliss.pedestal.PedestalState;
import com.bliss.pedestal.PedestalTickTask;
import com.bliss.pedestal.PedestalState.Ritual;
import com.bliss.trader.GemSelectorListener;
import com.bliss.trader.GemSwapListener;
import com.bliss.trader.TraderListener;
import com.bliss.trader.UpgraderListener;
import com.bliss.villager.BlissItems;
import com.bliss.villager.EnergyTokenListener;
import com.bliss.villager.EventState;
import com.bliss.villager.ParticleTask;
import com.bliss.villager.SoulListener;
import com.bliss.villager.Text;
import com.bliss.villager.TradeInterceptListener;
import com.bliss.villager.VillageDiscJukeboxListener;
import com.bliss.villager.VillagerEventCommand;
import com.bliss.villager.VillagerListener;
import com.bliss.villager.VillagerRaid;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public final class BlissPlugin extends JavaPlugin {
    private EventState eventState;
    private BlissItems villagerItems;
    private ParticleTask particleTask;
    private PlayerDataStore players;
    private BlissCoreItems coreItems;
    private PendingGemStore pendingGems;
    private DragonGamesHook dragonGames;
    private BlissRecipes recipes;
    private PedestalState pedestalState;
    private HonoredOneState honoredState;
    private HonoredOneManager honoredManager;
    private HonoredOneAuraTask honoredAura;
    private AssessmentWindow assessmentWindow;
    private AssessmentDay assessmentDay;
    private HonoredCompassTask honoredCompassTask;
    private AssessmentWindowBarTask windowBarTask;
    private HonoredNameplateTask honoredNameplate;
    private CageManager cageManager;
    private CageTask cageTask;
    private HonoredSummonRitual honoredRitual;
    private ControlRoomManager controlRoom;
    private EnchantLimitHologram enchantLimit;
    private EnchantLimits enchantLimits;
    private ControlRoomVeil controlRoomVeil;
    private ArmorLimitHologram armorLimit;
    private ArmorLimits armorLimits;
    private WorldBorderHologram worldBorderHologram;
    private PotionLimitHologram potionLimit;
    private PotionLimits potionLimits;
    private ControlRoomDeparturePrompt departurePrompt;
    private DepartureFlight departureFlight;
    private PedestalFinale pedestalFinale;
    private FluxGemListener fluxGemListener;
    private LifeVitalityVortexListener vortexListener;
    private FireCrispListener crispListener;
    private AstraDaggersListener astraDaggersListener;
    private NickCommand nickCommand;
    private GlobalState dreamState;
    private KitRules kitRules;
    private CrushedPedestalOrb crushedPedestalOrb;
    private GemAbilityRules gemAbilityRules;
    private KitRestrictionListener kitRestrictions;
    private Progression2RestrictionListener progression2Rules;
    private MegaMaceListener megaMaceListener;
    private EntityMotifs entityMotifs;
    private MotifSpeechListener motifSpeech;
    private PedestalTickTask pedestalTickTask;
    private PedestalGlowTask pedestalGlowTask;
    private GemAuraTask gemAuraTask;
    private GlobalState globalState;
    private GoldGemManager goldGemManager;
    private FragmentCoreRitual fragmentCoreRitual;
    private GoldenDreamRitual goldenDream;
    private DreamTabList dreamTabList;
    private NickManager nick;
    private MemoryRoster memoryRoster;
    private MemoryNpcManager memoryNpcs;
    private GoldGemTrimTask goldGemTrimTask;
    private GoldGemHarvestListener goldGemHarvest;
    private EnchantedObsidianTask enchantedObsidianTask;
    private BrokenActionBarTask brokenActionBarTask;
    private FireAuraTask fireAuraTask;
    private FireCrispBurnTask fireCrispBurnTask;
    private FireCozyHealTask fireCozyHealTask;
    private FireCozyListener fireCozyListener;
    private FireFireballListener fireballListener;
    private BountyHunterListener bountyHunterListener;
    private WealthPocketsListener wealthPocketsListener;
    private WealthGemListener wealthGemListener;
    private WealthAuraTask wealthAuraTask;
    private SpeedAuraTask speedAuraTask;
    private LegacySpeedState legacySpeedState;
    private LegacySpeedAuraTask legacySpeedAura;
    private LegacySoulSpeedTask legacySoulSpeed;
    private PuffAuraTask puffAuraTask;
    private PuffFlightTask puffFlightTask;
    private AstraAuraTask astraAuraTask;
    private AstraLifecycleListener astraLifecycle;
    private AstraDimensionalDriftListener astraDrift;
    private AstraPassivesListener astraPassives;
    private PedestalReviveRitual reviveRitual;
    private TagStore tags;
    private LifeAuraTask lifeAuraTask;
    private LifeAnimalListener lifeAnimal;
    private DragonEggBuffTask dragonEggBuffTask;
    private TrustStore trust;
    private MythicsModule mythics;
    private AchievementStore advancements;
    private BlissAdvancements advancementTab;
    private AstraSoulStore astraSouls;
    private WattDepositListener wattDeposit;
    private final List<Consumer<UUID>> cooldownClearers;
    private MenuListener menuListener;
    private ConfigMenuListener configMenuListener;
    private static final String[] TUNING_ROOTS;
    private static final int GEMS_DESIGN_VERSION = 4;
    private static final String GEMS_DESIGN_FILE = "gems-design.version";
    private boolean audienceGathered;
    private static final double PEDESTAL_SCATTER_R = 15.0;
    private final Map<UUID, GameMode> audienceModes;

    public BlissPlugin() {
        ArrayList var2 = new ArrayList();
        this.cooldownClearers = var2;
        HashMap var5 = new HashMap();
        this.audienceModes = var5;
    }

    public MotifSpeechListener motifSpeech(int var1) {
        return this.motifSpeech;
    }

    public void clearAllGemCooldowns(UUID var1, int var2) {
        for (Consumer var14 : this.cooldownClearers) {
            var14.accept(var1);
        }
    }

    public TrustStore trust(int var1) {
        return this.trust;
    }

    public TagStore tags(int var1) {
        return this.tags;
    }

    public GlobalState globalState(int var1) {
        return this.globalState;
    }

    public GlobalState dreamState(int var1) {
        return this.dreamState;
    }

    public boolean gemsDisabledInSpawn(Player var1, int var2) {
        if (var1 != null && this.globalState != null && GlobalState.forPlayer(var1, 1433087673).spawnGemDisable(898885534)) {
            Location var11 = var1.getLocation();
            String var4 = "spawn";
            if (WorldGuardHook.isInRegion(var11, var4, 32157142)) {
                return true;
            }
        }

        return false;
    }

    public AchievementStore advancements(int var1) {
        return this.advancements;
    }

    public GoldGemManager goldGemManager(int var1) {
        return this.goldGemManager;
    }

    public MemoryRoster memoryRoster(int var1) {
        return this.memoryRoster;
    }

    public MemoryNpcManager memoryNpcs(int var1) {
        return this.memoryNpcs;
    }

    public DreamTabList dreamTabList(int var1) {
        return this.dreamTabList;
    }

    public EntityMotifs entityMotifs(int var1) {
        return this.entityMotifs;
    }

    public MenuListener menu(int var1) {
        return this.menuListener;
    }

    public ConfigMenuListener configMenu(int var1) {
        return this.configMenuListener;
    }

    public void onEnable() {
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }

        int var10001 = 58026091;
        if (false) {
            PluginManager var748 = this.getServer().getPluginManager();
            var748.disablePlugin(this);
        } else {
            this.saveDefaultConfig();
            PlayerDataStore var2 = new PlayerDataStore(this, 2040626935);
            this.players = var2;
            this.players.load(117976764);
            BlissCoreItems var749 = new BlissCoreItems();
            this.coreItems = var749;
            PlayerDataStore var1523 = this.players;
            BlissCoreItems var1704 = this.coreItems;
            PendingGemStore var750 = new PendingGemStore(this, var1523, var1704, 608619008);
            this.pendingGems = var750;
            BlissCoreItems.initPendingStore(this.pendingGems, 873241755);
            this.pendingGems.load(1998253192);
            this.pendingGems.start(66847142);
            BlissCoreItems var1525 = this.coreItems;
            BlissRecipes var751 = new BlissRecipes(this, var1525, 344704508);
            this.recipes = var751;
            PedestalState var752 = new PedestalState(this, 314573563);
            this.pedestalState = var752;
            this.pedestalState.load(594637027);
            this.ensureFixedPedestalConfig(1579716929);

            for (String var13 : TUNING_ROOTS) {
                this.ensureGemTuningConfig(var13, 1475050931);
                this.applyGemTuningResets(var13, 1542510586);
            }

            GemTuning.init(this.getConfig(), 252270605);
            String[] var1898 = TUNING_ROOTS;
            int var1900 = var1898.length;

            for (byte var1903 = 0; var1903 < var1900; var1903 += 1) {
                String var1906 = var1898[var1903];
                FileConfiguration var92 = this.getConfig();
                ConfigurationSection var14 = var92.getConfigurationSection(var1906);
                Logger var95 = this.getLogger();
                int var764;
                if (var14 == null) {
                    var764 = 0;
                } else {
                    var764 = var14.getKeys(true).size();
                }

                String var763 = "Gem ability tuning: " + var764 + " keys read from config.yml [" + var1906 + "].";
                var95.info(var763);
            }

            PedestalState var97 = this.pedestalState;
            FileConfiguration var766 = this.getConfig();
            var97.applyFixedConfig(var766, 1031001108);
            Ritual var101 = this.pedestalState.ritual(1751702735);
            Ritual var767 = Ritual.REPAIR;
            if (var101 == var767 && this.pedestalState.count(629418704) >= 5) {
                PedestalState var770 = this.pedestalState;
                PlayerDataStore var1161 = this.players;
                BlissCoreItems var1323 = this.coreItems;
                PedestalRitualRunner.complete(this, var770, var1161, var1323, 27651565);
            }

            GlobalState var771 = new GlobalState(this, 1125963882);
            this.globalState = var771;
            this.globalState.load(994660777);
            this.migrateTrueDamageOverrides(1439624204);
            String var1526 = "goldendreamstate.yml";
            GlobalState var772 = new GlobalState(this, var1526, true, 1828049222);
            this.dreamState = var772;
            boolean var745;
            if (!this.dreamState.hasFile(511018000)) {
                var745 = true;
            } else {
                var745 = false;
            }

            this.dreamState.load(994660777);
            if (var745) {
                GlobalState var118 = this.dreamState;
                GlobalState var774 = this.globalState;
                var118.copyGameplayFrom(var774, 27036875);
                this.dreamState.save(343464104);
            }

            GlobalState var122 = this.globalState;
            GlobalState var776 = this.dreamState;
            Predicate<World> var1162 = GoldenDreamWorld::isDreamWorld;
            GlobalState.install(var122, var776, var1162, 1663740415);
            GemAutoEnchants.init(this.globalState, 1063422925);
            BlissCoreItems.setLegacySpeedSupplier(() -> this.globalState.legacySpeed(2067387107), 1124291848);
            PlayerDataStore var1528 = this.players;
            GoldGemManager var777 = new GoldGemManager(this, var1528, 1791323215);
            this.goldGemManager = var777;
            if (this.globalState.traderRecipe(285314044)) {
                this.recipes.registerTrader(475248342);
            }

            if (this.globalState.upgraderRecipe(459107113)) {
                this.recipes.registerUpgrader(406940308);
            }

            this.recipes.registerFragment(996864941);
            this.recipes.registerRepairKit(1204073266);
            if (this.globalState.restorationCoreRecipe(1172908205)) {
                this.recipes.registerRestorationCore(1174323205);
            }

            this.recipes.registerRestorationItem(1195127439);
            this.recipes.loadRecipes(1608392953);
            this.recipes.applyCustomRecipes(646303057);
            PlayerDataStore var1328 = this.players;
            TagStore var778 = new TagStore(var1328, 1373038233);
            this.tags = var778;
            ConsistentDamage.init(this.players, 554891206);
            ConsistentDamage.setTrueDamage(this.globalState.trueDamage(755209314), 1147290473);
            Logger var160 = this.getLogger();
            String var1157;
            if (this.globalState.trueDamage(755209314)) {
                var1157 = "True Damage is ON: every gems.*.damage.* and mythic-gems.*.damage.* HP value lands as written, on everyone. The holder's energy scale, the Honored One dial, per-ability caps, shields, Resistance and absorption still apply.";
            } else {
                var1157 = "True Damage is OFF: damage numbers are dealt before armour, like a sword's, and the victim's armour, toughness and Protection reduce them.";
            }

            var160.info(var1157);
            TrustStore var782 = new TrustStore(this, 785340826);
            this.trust = var782;
            this.trust.load(154224824);
            AchievementStore var783 = new AchievementStore(this, 249990973);
            this.advancements = var783;
            this.advancements.load(1043067642);
            if (this.getServer().getPluginManager().getPlugin("UltimateAdvancementAPI") != null) {
                try {
                    BlissCoreItems var1703 = this.coreItems;
                    BlissAdvancements var1147 = new BlissAdvancements(this, var1703, 1902926375);
                    this.advancementTab = var1147;
                    this.advancementTab.register(715078154);
                    AchievementStore var737 = this.advancements;
                    BlissAdvancements var1149 = this.advancementTab;
                    var737.setTab(var1149, 1047838210);
                    PluginManager var740 = this.getServer().getPluginManager();
                    BlissAdvancements var1151 = this.advancementTab;
                    var740.registerEvents(var1151, this);
                } catch (Throwable var1916) {
                    this.advancementTab = null;
                    Logger var744 = this.getLogger();
                    String var1155 = "Could not start the Bliss SMP advancement tab (" + var1916 + ") — achievements still track in chat.";
                    var744.warning(var1155);
                }
            } else {
                this.getLogger().info("UltimateAdvancementAPI not installed — the Bliss SMP advancement tab is disabled; achievements still track in chat.");
            }

            HonoredOneState var786 = new HonoredOneState(this, 747355916);
            this.honoredState = var786;
            this.honoredState.load(1304199831);
            HonoredOneState var1530 = this.honoredState;
            HonoredOneManager var787 = new HonoredOneManager(this, var1530, 880054376);
            this.honoredManager = var787;
            this.honoredManager.refreshAll(48721253);
            HonoredOneManager var1532 = this.honoredManager;
            HonoredOneAuraTask var788 = new HonoredOneAuraTask(this, var1532, 1908018551);
            this.honoredAura = var788;
            this.honoredAura.start(493834857);
            AssessmentWindow var789 = new AssessmentWindow(this, 1252300436);
            this.assessmentWindow = var789;
            AssessmentDay var790 = new AssessmentDay(this, 1170092853);
            this.assessmentDay = var790;
            this.assessmentDay.load(926702936);
            HonoredOneManager var1534 = this.honoredManager;
            AssessmentWindow var1707 = this.assessmentWindow;
            GlobalState var1822 = this.globalState;
            HonoredCompassTask var791 = new HonoredCompassTask(this, var1534, var1707, var1822, 1440581003);
            this.honoredCompassTask = var791;
            this.honoredCompassTask.start(1759447560);
            HonoredNameplateTask.despawnAll(430454330);
            HonoredOneManager var1536 = this.honoredManager;
            AssessmentWindow var1709 = this.assessmentWindow;
            HonoredNameplateTask var792 = new HonoredNameplateTask(this, var1536, var1709, 2116632529);
            this.honoredNameplate = var792;
            this.honoredNameplate.start(1502113334);
            AssessmentWindow var1538 = this.assessmentWindow;
            AssessmentDay var1711 = this.assessmentDay;
            AssessmentWindowBarTask var793 = new AssessmentWindowBarTask(this, var1538, var1711, 929615172);
            this.windowBarTask = var793;
            this.windowBarTask.start(1446739991);
            ControlRoomManager var794 = new ControlRoomManager(this, 330707103);
            this.controlRoom = var794;
            EnchantLimits var795 = new EnchantLimits(this, 1279795433);
            this.enchantLimits = var795;
            this.enchantLimits.load(1889329886);
            EnchantLimits var1540 = this.enchantLimits;
            EnchantLimitHologram var796 = new EnchantLimitHologram(this, var1540, 663850299);
            this.enchantLimit = var796;
            PluginManager var202 = this.getServer().getPluginManager();
            EnchantLimitHologram var798 = this.enchantLimit;
            var202.registerEvents(var798, this);
            ArmorLimits var799 = new ArmorLimits(this, 377644211);
            this.armorLimits = var799;
            this.armorLimits.load(1220568349);
            ArmorLimits var1542 = this.armorLimits;
            ArmorLimitHologram var800 = new ArmorLimitHologram(this, var1542, 1425786101);
            this.armorLimit = var800;
            PluginManager var209 = this.getServer().getPluginManager();
            ArmorLimitHologram var802 = this.armorLimit;
            var209.registerEvents(var802, this);
            WorldBorderHologram var803 = new WorldBorderHologram(this, 1935659137);
            this.worldBorderHologram = var803;
            PluginManager var213 = this.getServer().getPluginManager();
            WorldBorderHologram var805 = this.worldBorderHologram;
            var213.registerEvents(var805, this);
            PotionLimits var806 = new PotionLimits(this, 1174121121);
            this.potionLimits = var806;
            this.potionLimits.load(1618902761);
            PotionLimits var1544 = this.potionLimits;
            PotionLimitHologram var807 = new PotionLimitHologram(this, var1544, 633184091);
            this.potionLimit = var807;
            PluginManager var220 = this.getServer().getPluginManager();
            PotionLimitHologram var809 = this.potionLimit;
            var220.registerEvents(var809, this);
            this.controlRoom.load(1385908546);
            this.loadAudience(1680937754);
            ControlRoomVeil var810 = new ControlRoomVeil(this, 43639358);
            this.controlRoomVeil = var810;
            PluginManager var227 = this.getServer().getPluginManager();
            ControlRoomVeil var812 = this.controlRoomVeil;
            var227.registerEvents(var812, this);
            this.controlRoomVeil.start(1106458133);
            ControlRoomDeparturePrompt var813 = new ControlRoomDeparturePrompt(this, 4282814);
            this.departurePrompt = var813;
            this.departurePrompt.start(569804431);
            DepartureFlight var814 = new DepartureFlight(this, 1085008570);
            this.departureFlight = var814;
            PluginManager var236 = this.getServer().getPluginManager();
            DepartureFlight var816 = this.departureFlight;
            var236.registerEvents(var816, this);
            PedestalFinale var817 = new PedestalFinale(this, 888882262);
            this.pedestalFinale = var817;
            BukkitScheduler var240 = this.getServer().getScheduler();
            Runnable var1170 = () -> {
                this.enchantLimit.reconcile(1408290875);
                this.armorLimit.reconcile(1764668093);
                this.worldBorderHologram.reconcile(380967189);
                this.potionLimit.reconcile(1560808647);
            };
            var240.runTask(this, var1170);
            PluginManager var244 = this.getServer().getPluginManager();
            ControlRoomHologramKeeper var819 = new ControlRoomHologramKeeper(this, 546277833);
            var244.registerEvents(var819, this);
            Location var1901 = this.controlRoom.shrineBlock(1056167168);
            if (var1901 != null) {
                try {
                    Block var250 = var1901.getBlock();
                    BlockData var823 = Bukkit.createBlockData(this.controlRoom.shrineData(366566408));
                    var250.setBlockData(var823, false);
                } catch (IllegalArgumentException var1915) {
                }

                this.controlRoom.clearShrineBlock(2036730787);
            }

            AssessmentWindow var254 = this.assessmentWindow;
            Runnable var825 = this::onAssessmentWindowClosed;
            var254.setOnClose(var825, 1325015815);
            this.assessmentWindow.restore(784164652);
            CageManager var826 = new CageManager(this, 26321764);
            this.cageManager = var826;
            this.cageManager.load(785571764);
            this.cageManager.recoverPendingEngage(544234325);
            CageManager var1546 = this.cageManager;
            HonoredOneManager var1713 = this.honoredManager;
            CageTask var827 = new CageTask(this, var1546, var1713, 2052649741);
            this.cageTask = var827;
            this.cageTask.start(1187966202);
            PluginManager var267 = this.getServer().getPluginManager();
            CageTask var829 = this.cageTask;
            var267.registerEvents(var829, this);
            CageManager var1548 = this.cageManager;
            HonoredSummonRitual var830 = new HonoredSummonRitual(this, var1548, 2036688590);
            this.honoredRitual = var830;
            KitRules var831 = new KitRules(this, 1792746341);
            this.kitRules = var831;
            this.kitRules.load(1182261932);
            GemAbilityRules var832 = new GemAbilityRules(this, 1095722593);
            this.gemAbilityRules = var832;
            this.gemAbilityRules.load(570470248);
            CrushedPedestalOrb var833 = new CrushedPedestalOrb(this, 605051063);
            this.crushedPedestalOrb = var833;
            PluginManager var278 = this.getServer().getPluginManager();
            CrushedPedestalOrb var835 = this.crushedPedestalOrb;
            var278.registerEvents(var835, this);
            this.crushedPedestalOrb.reconcile(1357222658);
            AstraSoulStore var836 = new AstraSoulStore(this, 1631261447);
            this.astraSouls = var836;
            this.astraSouls.load(909715245);
            TeamManager.install(this, 1377306259);
            BlissItems var837 = new BlissItems(this, 1227580713);
            this.villagerItems = var837;
            EventState var838 = new EventState(this, 876550266);
            this.eventState = var838;
            this.eventState.load(1920032886);
            if (this.eventState.isFinalActive(245445441)) {
                BukkitScheduler var294 = this.getServer().getScheduler();
                Runnable var1176 = () -> {
                    if (this.eventState.isFinalActive(245445441)) {
                        if (VillagerRaid.current(137107344) == null) {
                            Location var5 = this.eventState.compassLoc(561536293);
                            if (var5 != null && var5.getWorld() != null) {
                                EventState var25x = this.eventState;
                                VillagerRaid var15 = new VillagerRaid(this, var25x, var5, 205368423);
                                int var20 = this.eventState.raidWave(575879065);
                                boolean var24 = this.eventState.raidBetween(243195253);
                                var15.resume(var20, var24, 2047702377);
                            } else {
                                this.getLogger().warning("Villager raid was active but no compass location is set — cannot resume; use /blissevent end.");
                            }
                        }
                    }
                };
                var294.runTaskLater(this, var1176, 40L);
            }

            EventState var1364 = this.eventState;
            BlissItems var1550 = this.villagerItems;
            VillagerEventCommand var296 = new VillagerEventCommand(this, var1364, var1550, 1180121959);
            PluginCommand var298 = this.getCommand("blissevent");
            var298.setExecutor(var296);
            PluginCommand var300 = this.getCommand("blissevent");
            var300.setTabCompleter(var296);
            PlayerDataStore var1366 = this.players;
            BlissCoreItems var1552 = this.coreItems;
            TraderListener var301 = new TraderListener(this, var1366, var1552, 1391813319);
            PluginManager var304 = this.getServer().getPluginManager();
            var304.registerEvents(var301, this);
            PluginManager var307 = this.getServer().getPluginManager();
            PlayerDataStore var1554 = this.players;
            BlissCoreItems var1715 = this.coreItems;
            GemSelectorListener var845 = new GemSelectorListener(this, var1554, var1715, var301, 2092670997);
            var307.registerEvents(var845, this);
            PlayerDataStore var1369 = this.players;
            BlissCoreItems var1556 = this.coreItems;
            GemSwapListener var308 = new GemSwapListener(this, var1369, var1556, var301, 2013551050);
            var301.setSwaps(var308, 1702194091);
            PluginManager var312 = this.getServer().getPluginManager();
            var312.registerEvents(var308, this);
            if (this.getCommand("gemswap") != null) {
                PluginCommand var316 = this.getCommand("gemswap");
                var316.setExecutor(var308);
            }

            BlissCoreItems var1371 = this.coreItems;
            PedestalState var1558 = this.pedestalState;
            PlayerDataStore var1718 = this.players;
            TagStore var1825 = this.tags;
            BlissCommand var317 = new BlissCommand(this, var1371, var1558, var1718, var1825, var301, 1891653229);
            PluginCommand var319 = this.getCommand("bliss");
            var319.setExecutor(var317);
            PluginCommand var321 = this.getCommand("bliss");
            var321.setTabCompleter(var317);
            BlissCoreItems var1560 = this.coreItems;
            PlayerDataStore var1720 = this.players;
            MenuListener var855 = new MenuListener(this, var1560, var1720, 281946364);
            this.menuListener = var855;
            if (this.getCommand("menu") != null) {
                PluginCommand var326 = this.getCommand("menu");
                MenuListener var859 = this.menuListener;
                var326.setExecutor(var859);
            }

            ConfigMenuListener var860 = new ConfigMenuListener(this, 1402634157);
            this.configMenuListener = var860;
            PluginManager var330 = this.getServer().getPluginManager();
            ConfigMenuListener var862 = this.configMenuListener;
            var330.registerEvents(var862, this);
            String var331 = "give_energy_token";
            ItemStack var865 = this.villagerItems.energyToken(624817347);
            registerMenuGive(var331, var865, 1008766206);
            String var332 = "give_soul_1";
            ItemStack var868 = this.villagerItems.soul(1, 888486940);
            registerMenuGive(var332, var868, 1008766206);
            String var333 = "give_soul_2";
            ItemStack var871 = this.villagerItems.soul(2, 888486940);
            registerMenuGive(var333, var871, 1008766206);
            String var334 = "give_soul_3";
            ItemStack var874 = this.villagerItems.soul(3, 888486940);
            registerMenuGive(var334, var874, 1008766206);
            String var335 = "give_compass_jugg";
            ItemStack var875 = HonoredCompass.juggernaut(267343802);
            registerMenuGive(var335, var875, 1008766206);
            String var336 = "give_obsidian";
            ItemStack var878 = this.coreItems.enchantedObsidian(1193659648);
            registerMenuGive(var336, var878, 1008766206);
            String var337 = "give_fragment";
            ItemStack var881 = this.coreItems.fragment(452582255);
            registerMenuGive(var337, var881, 1008766206);
            String var338 = "gold_gem_awakened";
            ItemStack var882 = awakenedGoldGemPrototype(1891786337);
            Material var1188 = Material.ORANGE_STAINED_GLASS_PANE;
            Consumer<Player> var1375 = var1 -> {
                ItemStack var5 = GoldGem.create(485535734);
                GoldGemManager var7 = this.goldGemManager;
                var7.becomeHolder(var1, var5, 2022822655);
                GoldGem.setMask(var5, 255, 1710446259);
                GoldGem.setState(var5, 1, 953378395);
                GemType var18 = GoldGem.absorbedGems(var5, 874118425).get(0);
                GoldGem.setSelected(var5, var18, 958539197);
                BlissCoreItems.giveOrDrop(var1, var5, 41085121);
                Component var21 = Text.legacy("&6The Gold Gem awakens in your hands.", 373192400);
                var1.sendMessage(var21);
            };
            MenuListener.registerExternal(var338, var882, var1188, var1375, 153575556);
            String var339 = "give_gold_gem_empty";
            ItemStack var883 = GoldGem.create(485535734);
            Consumer<Player> var1377 = var1 -> {
                ItemStack var5 = GoldGem.create(485535734);
                GoldGemManager var7 = this.goldGemManager;
                var7.becomeHolder(var1, var5, 2022822655);
                BlissCoreItems.giveOrDrop(var1, var5, 41085121);
                Component var12 = Text.legacy("&6The Gold Gem chooses you.", 373192400);
                var1.sendMessage(var12);
            };
            MenuListener.registerExternal(var339, var883, null, var1377, 153575556);
            TrustStore var1191 = this.trust;
            TrustCommand var340 = new TrustCommand(var1191, 1947556424);
            if (this.getCommand("trust") != null) {
                PluginCommand var344 = this.getCommand("trust");
                var344.setExecutor(var340);
                PluginCommand var346 = this.getCommand("trust");
                var346.setTabCompleter(var340);
            }

            PvpCommand var347 = new PvpCommand();
            if (this.getCommand("pvp") != null) {
                PluginCommand var351 = this.getCommand("pvp");
                var351.setExecutor(var347);
                PluginCommand var353 = this.getCommand("pvp");
                var353.setTabCompleter(var347);
            }

            TasksCommand var354 = new TasksCommand(this, 1836221706);
            if (this.getCommand("tasks") != null) {
                PluginCommand var358 = this.getCommand("tasks");
                var358.setExecutor(var354);
                PluginCommand var360 = this.getCommand("tasks");
                var360.setTabCompleter(var354);
            }

            try {
                Class var19 = Class.forName("com.bliss.dragongames.DragonGamesModule");
                String var1193 = "install";
                Class[] var1379 = new Class[]{BlissPlugin.class};
                Method var900 = var19.getMethod(var1193, var1379);
                Object[] var1381 = new Object[]{this};
                DragonGamesHook var902 = (DragonGamesHook)var900.invoke(null, var1381);
                this.dragonGames = var902;
                this.getLogger().info("Dragon Games: enabled (/shop, /dragon games start, /bliss dragongames).");
            } catch (ClassNotFoundException var1913) {
                this.getLogger().info("Dragon Games: not included in this build.");
            } catch (ReflectiveOperationException var1914) {
                Logger var728 = this.getLogger();
                String var1145 = "Dragon Games: failed to install — " + var1914.getCause();
                var728.warning(var1145);
            }

            PlayerDataStore var1196 = this.players;
            BlissCoreItems var1383 = this.coreItems;
            WithdrawCommand var366 = new WithdrawCommand(var1196, var1383, 518709547);
            PluginCommand var368 = this.getCommand("withdraw");
            var368.setExecutor(var366);
            PluginCommand var370 = this.getCommand("withdraw");
            var370.setTabCompleter(var366);
            BlissCoreItems var1385 = this.coreItems;
            RecipeBookListener var371 = new RecipeBookListener(this, var1385, 831946430);
            PluginCommand var373 = this.getCommand("recipe");
            var373.setExecutor(var371);
            PluginManager var376 = this.getServer().getPluginManager();
            var376.registerEvents(var371, this);
            HonoredOneManager var1200 = this.honoredManager;
            AssessmentWindow var1387 = this.assessmentWindow;
            CageManager var1562 = this.cageManager;
            PedestalState var1724 = this.pedestalState;
            HonoredSummonRitual var1829 = this.honoredRitual;
            AssessmentDay var1877 = this.assessmentDay;
            ControlRoomManager var1894 = this.controlRoom;
            AssessmentCommand var377 = new AssessmentCommand(var1200, var1387, var1562, var1724, var1829, var1877, var1894, 428345220);
            if (this.getCommand("assessment") != null) {
                PluginCommand var381 = this.getCommand("assessment");
                var381.setExecutor(var377);
                PluginCommand var383 = this.getCommand("assessment");
                var383.setTabCompleter(var377);
            }

            GoldGemManager var1202 = this.goldGemManager;
            PlayerDataStore var1389 = this.players;
            BlissCoreItems var1564 = this.coreItems;
            GoldGemCommand var384 = new GoldGemCommand(var1202, var1389, var1564, 1628560042);
            if (this.getCommand("goldgem") != null) {
                PluginCommand var388 = this.getCommand("goldgem");
                var388.setExecutor(var384);
                PluginCommand var390 = this.getCommand("goldgem");
                var390.setTabCompleter(var384);
            }

            GoldGemManager var1204 = this.goldGemManager;
            VictimCommand var391 = new VictimCommand(var1204, 450899700);
            if (this.getCommand("victim") != null) {
                PluginCommand var395 = this.getCommand("victim");
                var395.setExecutor(var391);
                PluginCommand var397 = this.getCommand("victim");
                var397.setTabCompleter(var391);
            }

            NickManager var926 = new NickManager(this, 569355552);
            this.nick = var926;
            NickManager var1566 = this.nick;
            MemoryRoster var927 = new MemoryRoster(this, var1566, 2021026860);
            this.memoryRoster = var927;
            NickManager var1568 = this.nick;
            MemoryRoster var1726 = this.memoryRoster;
            BlissCoreItems var1831 = this.coreItems;
            MemoryNpcManager var928 = new MemoryNpcManager(this, var1568, var1726, var1831, 1934316535);
            this.memoryNpcs = var928;
            if (this.getCommand("alternate") != null) {
                PluginCommand var404 = this.getCommand("alternate");
                MemoryRoster var1394 = this.memoryRoster;
                AlternateCommand var931 = new AlternateCommand(var1394, 291334302);
                var404.setExecutor(var931);
            }

            NickManager var1570 = this.nick;
            MemoryRoster var1728 = this.memoryRoster;
            MemoryNpcManager var1833 = this.memoryNpcs;
            NickCommand var932 = new NickCommand(this, var1570, var1728, var1833, 127113930);
            this.nickCommand = var932;
            if (this.getCommand("nick") != null) {
                PluginCommand var409 = this.getCommand("nick");
                var409.setExecutor(var932);
                PluginCommand var411 = this.getCommand("nick");
                var411.setTabCompleter(var932);
            }

            PluginManager var25 = this.getServer().getPluginManager();
            NickManager var939 = this.nick;
            var25.registerEvents(var939, this);
            MemoryRoster var941 = this.memoryRoster;
            var25.registerEvents(var941, this);
            MemoryNpcManager var943 = this.memoryNpcs;
            var25.registerEvents(var943, this);
            if (this.memoryNpcs.available(1626139367)) {
                MemoryNpcManager var1397 = this.memoryNpcs;
                MemoryNpcDeathListener var944 = new MemoryNpcDeathListener(var1397, 1753584113);
                var25.registerEvents(var944, this);
            }

            BukkitScheduler var424 = this.getServer().getScheduler();
            Runnable var1210 = () -> this.memoryNpcs.reload(1472509832);
            var424.runTaskLater(this, var1210, 40L);
            MenuListener var947 = this.menuListener;
            var25.registerEvents(var947, this);
            BlissCoreItems var1572 = this.coreItems;
            GlobalState var1730 = this.globalState;
            BossDropsListener var948 = new BossDropsListener(this, var1572, var1730, 976392846);
            var25.registerEvents(var948, this);
            HonoredOneManager var1402 = this.honoredManager;
            AssessmentWindow var1574 = this.assessmentWindow;
            HonoredOneListener var949 = new HonoredOneListener(var1402, var1574, this, 1637460154);
            var25.registerEvents(var949, this);
            JuggernautGemGuardListener var950 = new JuggernautGemGuardListener();
            var25.registerEvents(var950, this);
            KitRules var1576 = this.kitRules;
            GlobalState var1733 = this.globalState;
            KitRestrictionListener var951 = new KitRestrictionListener(this, var1576, var1733, 522022389);
            this.kitRestrictions = var951;
            KitRestrictionListener var953 = this.kitRestrictions;
            var25.registerEvents(var953, this);
            this.kitRestrictions.start(281039508);
            PlayerDataStore var1405 = this.players;
            GlobalState var1578 = this.globalState;
            BrokenStatePenaltyListener var954 = new BrokenStatePenaltyListener(var1405, var1578, 5558517);
            var25.registerEvents(var954, this);
            GlobalState var1407 = this.globalState;
            EventState var1580 = this.eventState;
            VillagerTradeGateListener var955 = new VillagerTradeGateListener(var1407, var1580, 1382417312);
            var25.registerEvents(var955, this);
            GlobalState var1409 = this.globalState;
            VillagerSpawnGateListener var956 = new VillagerSpawnGateListener(var1409, 1395460685);
            var25.registerEvents(var956, this);
            GlobalState var1411 = this.globalState;
            NetheriteSmithingListener var957 = new NetheriteSmithingListener(var1411, 1796291778);
            var25.registerEvents(var957, this);
            BlissCoreItems var1413 = this.coreItems;
            GlobalState var1582 = this.globalState;
            BlissRecipes var1735 = this.recipes;
            ProgressionCraftGateListener var958 = new ProgressionCraftGateListener(var1413, var1582, var1735, 1652984800);
            var25.registerEvents(var958, this);
            MaceRestrictionsListener var959 = new MaceRestrictionsListener();
            var25.registerEvents(var959, this);
            MegaMaceListener var960 = new MegaMaceListener(this, 220034986);
            this.megaMaceListener = var960;
            MegaMaceListener var962 = this.megaMaceListener;
            var25.registerEvents(var962, this);
            this.megaMaceListener.start(402919600);
            EntityMotifs var963 = new EntityMotifs(this, 86670193);
            this.entityMotifs = var963;
            this.entityMotifs.load(499786237);
            this.entityMotifs.start(865462193);
            MotifSpeechListener var964 = new MotifSpeechListener(this, 442608857);
            this.motifSpeech = var964;
            MotifSpeechListener var966 = this.motifSpeech;
            var25.registerEvents(var966, this);
            GlobalState var1418 = this.globalState;
            Progression2RestrictionListener var451 = new Progression2RestrictionListener(this, var1418, 562964473);
            var25.registerEvents(var451, this);
            var451.start(826831075);
            this.progression2Rules = var451;
            Server var456 = this.getServer();
            NamespacedKey var970 = NamespacedKey.minecraft("mace");
            var456.removeRecipe(var970);
            EventState var1584 = this.eventState;
            BlissItems var1737 = this.villagerItems;
            EnergyTokenListener var971 = new EnergyTokenListener(this, var1584, var1737, 2120079378);
            var25.registerEvents(var971, this);
            EventState var1586 = this.eventState;
            BlissItems var1739 = this.villagerItems;
            SoulListener var972 = new SoulListener(this, var1586, var1739, 1443453418);
            var25.registerEvents(var972, this);
            EventState var1588 = this.eventState;
            BlissItems var1741 = this.villagerItems;
            VillagerListener var973 = new VillagerListener(this, var1588, var1741, 314777875);
            var25.registerEvents(var973, this);
            BlissItems var1590 = this.villagerItems;
            VillageDiscJukeboxListener var974 = new VillageDiscJukeboxListener(this, var1590, 2105936976);
            var25.registerEvents(var974, this);
            EventState var1592 = this.eventState;
            BlissItems var1743 = this.villagerItems;
            TradeInterceptListener var975 = new TradeInterceptListener(this, var1592, var1743, 423884663);
            var25.registerEvents(var975, this);
            PlayerDataStore var1594 = this.players;
            BlissCoreItems var1745 = this.coreItems;
            UpgraderListener var976 = new UpgraderListener(this, var1594, var1745, var301, 1006841703);
            var25.registerEvents(var976, this);
            PedestalState var1596 = this.pedestalState;
            BlissCoreItems var1747 = this.coreItems;
            PlayerDataStore var1836 = this.players;
            PedestalListener var977 = new PedestalListener(this, var1596, var1747, var1836, 1762521869);
            var25.registerEvents(var977, this);
            PedestalState var1598 = this.pedestalState;
            PlayerDataStore var1749 = this.players;
            BlissCoreItems var1838 = this.coreItems;
            PedestalReviveRitual var978 = new PedestalReviveRitual(this, var1598, var1749, var1838, 1266020679);
            this.reviveRitual = var978;
            PedestalState var1234 = this.pedestalState;
            PedestalReviveRitual var1428 = this.reviveRitual;
            PedestalReviveTriggerListener var466 = new PedestalReviveTriggerListener(var1234, var1428, 1408758623);
            var25.registerEvents(var466, this);
            var466.startPolling(this, 1874226255);
            GlobalState var1430 = this.globalState;
            GemProtectionListener var981 = new GemProtectionListener(var1430, 1903317798);
            var25.registerEvents(var981, this);
            PlayerDataStore var1600 = this.players;
            BlissCoreItems var1751 = this.coreItems;
            BottledEnergyListener var982 = new BottledEnergyListener(this, var1600, var1751, 2069971770);
            var25.registerEvents(var982, this);
            PlayerDataStore var1602 = this.players;
            BlissCoreItems var1753 = this.coreItems;
            PlayerJoinListener var983 = new PlayerJoinListener(this, var1602, var1753, 1748191718);
            var25.registerEvents(var983, this);
            PlayerDataStore var1604 = this.players;
            BlissCoreItems var1755 = this.coreItems;
            PlayerDeathListener var984 = new PlayerDeathListener(this, var1604, var1755, 1689046748);
            var25.registerEvents(var984, this);
            GlobalState var1435 = this.globalState;
            EnvironmentListener var473 = new EnvironmentListener(this, var1435, 251964444);
            var25.registerEvents(var473, this);
            var473.startGhostSweep(1914562370);
            ReinforcedPortalListener var986 = new ReinforcedPortalListener();
            var25.registerEvents(var986, this);
            PlayerDataStore var1437 = this.players;
            TrustStore var1606 = this.trust;
            StrengthGemListener var477 = new StrengthGemListener(this, var1437, var1606, 1553410713);
            var25.registerEvents(var477, this);
            var477.startChadAuraTask(2128849332);
            List var481 = this.cooldownClearers;
            Consumer var989 = var1 -> {
                Map var4 = var477.cd2;
                var4.remove(var1);
                Map var7 = var477.cd3;
                var7.remove(var1);
                Map var10 = var477.cd4;
                var10.remove(var1);
            };
            var481.add(var989);
            PlayerDataStore var1608 = this.players;
            BountyHunterListener var990 = new BountyHunterListener(this, var1608, var477, 1775157156);
            this.bountyHunterListener = var990;
            BountyHunterListener var992 = this.bountyHunterListener;
            var25.registerEvents(var992, this);
            FireGemListener var485 = new FireGemListener();
            var25.registerEvents(var485, this);
            List var488 = this.cooldownClearers;
            Consumer var995 = var1 -> {
                Map var4 = var485.cd1;
                var4.remove(var1);
                Map var7 = var485.cd2;
                var7.remove(var1);
                Map var10 = var485.cd3;
                var10.remove(var1);
                Map var13x = var485.cd4;
                var13x.remove(var1);
            };
            var488.add(var995);
            PlayerDataStore var1440 = this.players;
            FireCrispListener var490 = new FireCrispListener(this, var1440, var485, 1893595009);
            this.crispListener = var490;
            var25.registerEvents(var490, this);
            PlayerDataStore var1611 = this.players;
            GlobalState var1840 = this.globalState;
            TrustStore var1879 = this.trust;
            FireFireballListener var998 = new FireFireballListener(this, var1611, var485, var1840, var1879, 674964892);
            this.fireballListener = var998;
            FireFireballListener var1000 = this.fireballListener;
            var25.registerEvents(var1000, this);
            PlayerDataStore var1443 = this.players;
            GlobalState var1759 = this.globalState;
            FireCozyListener var495 = new FireCozyListener(this, var1443, var485, var1759, 1385016232);
            this.fireCozyListener = var495;
            var25.registerEvents(var495, this);
            PlayerDataStore var1445 = this.players;
            TrustStore var1614 = this.trust;
            FirePassivesListener var1003 = new FirePassivesListener(var1445, var1614, 786518403);
            var25.registerEvents(var1003, this);
            PlayerDataStore var1616 = this.players;
            WealthPocketsListener var1004 = new WealthPocketsListener(this, var1616, 551401748);
            this.wealthPocketsListener = var1004;
            this.wealthPocketsListener.load(955840959);
            WealthPocketsListener var1006 = this.wealthPocketsListener;
            var25.registerEvents(var1006, this);
            PlayerDataStore var1618 = this.players;
            GlobalState var1761 = this.globalState;
            TrustStore var1842 = this.trust;
            WealthGemListener var1007 = new WealthGemListener(this, var1618, var1761, var1842, 1324700847);
            this.wealthGemListener = var1007;
            WealthGemListener var1009 = this.wealthGemListener;
            var25.registerEvents(var1009, this);
            this.wealthGemListener.start(349911831);
            GlobalState var1449 = this.globalState;
            WealthGemListener var1620 = this.wealthGemListener;
            ProgressionDamageListener var1010 = new ProgressionDamageListener(var1449, var1620, 1058892559);
            var25.registerEvents(var1010, this);
            GlobalState var1451 = this.globalState;
            WealthGemListener var1622 = this.wealthGemListener;
            EnderpearlAndSweepListener var508 = new EnderpearlAndSweepListener(this, var1451, var1622, 848876514);
            var25.registerEvents(var508, this);
            var508.start(66654179);
            PlayerDataStore var1624 = this.players;
            WealthGemListener var1763 = this.wealthGemListener;
            WealthPassivesListener var1012 = new WealthPassivesListener(this, var1624, var1763, 2130488424);
            var25.registerEvents(var1012, this);
            List var513 = this.cooldownClearers;
            Consumer var1014 = var1 -> {
                Map var5 = this.wealthGemListener.cd1;
                var5.remove(var1);
                Map var9 = this.wealthGemListener.cd2;
                var9.remove(var1);
                Map var13x = this.wealthGemListener.cd3;
                var13x.remove(var1);
                Map var17 = this.wealthGemListener.richRushExpiry;
                var17.remove(var1);
                Map var21 = this.wealthGemListener.amplifExpiry;
                var21.remove(var1);
            };
            var513.add(var1014);
            EntityClickGuardListener var1015 = new EntityClickGuardListener();
            var25.registerEvents(var1015, this);
            SpeedGemListener var516 = new SpeedGemListener();
            var25.registerEvents(var516, this);
            List<Consumer<UUID>> var519 = this.cooldownClearers;
            Consumer<UUID> var1018 = var516::clear;
            var519.add(var1018);
            PlayerDataStore var1626 = this.players;
            SpeedThunderStepListener var1019 = new SpeedThunderStepListener(this, var1626, var516, 1647351025);
            var25.registerEvents(var1019, this);
            PlayerDataStore var1628 = this.players;
            TrustStore var1844 = this.trust;
            SpeedStormListener var1020 = new SpeedStormListener(this, var1628, var516, var1844, 2141913221);
            var25.registerEvents(var1020, this);
            PlayerDataStore var1630 = this.players;
            TrustStore var1846 = this.trust;
            SpeedBlurListener var1021 = new SpeedBlurListener(this, var1630, var516, var1846, 362188771);
            var25.registerEvents(var1021, this);
            LegacySpeedState var1022 = new LegacySpeedState();
            this.legacySpeedState = var1022;
            List<Consumer<UUID>> var526 = this.cooldownClearers;
            LegacySpeedState var1024 = this.legacySpeedState;
            Consumer<UUID> var1025 = var1024::clear;
            var526.add(var1025);
            PlayerDataStore var1632 = this.players;
            LegacySpeedState var1768 = this.legacySpeedState;
            GlobalState var1848 = this.globalState;
            LegacyTerminalVelocityListener var1026 = new LegacyTerminalVelocityListener(this, var1632, var1768, var1848, 826231323);
            var25.registerEvents(var1026, this);
            PlayerDataStore var1634 = this.players;
            LegacySpeedState var1770 = this.legacySpeedState;
            GlobalState var1850 = this.globalState;
            TrustStore var1881 = this.trust;
            LegacySpeedStormListener var1027 = new LegacySpeedStormListener(this, var1634, var1770, var1850, var1881, 811020775);
            var25.registerEvents(var1027, this);
            PlayerDataStore var1636 = this.players;
            LegacySpeedState var1772 = this.legacySpeedState;
            GlobalState var1852 = this.globalState;
            TrustStore var1883 = this.trust;
            LegacyBlurListener var1028 = new LegacyBlurListener(this, var1636, var1772, var1852, var1883, 1424960435);
            var25.registerEvents(var1028, this);
            PlayerDataStore var1460 = this.players;
            PuffGemListener var531 = new PuffGemListener(this, var1460, 2093558402);
            var25.registerEvents(var531, this);
            List<Consumer<UUID>> var534 = this.cooldownClearers;
            Consumer<UUID> var1031 = var1 -> {
                Map var4 = var531.cd1;
                var4.remove(var1);
                Map var7 = var531.cd2;
                var7.remove(var1);
                Map var10 = var531.cd3;
                var10.remove(var1);
                Map var13x = var531.cd4;
                var13x.remove(var1);
            };
            var534.add(var1031);
            PlayerDataStore var1638 = this.players;
            GlobalState var1854 = this.globalState;
            PuffAbilitiesListener var1032 = new PuffAbilitiesListener(this, var1638, var531, var1854, 958163697);
            var25.registerEvents(var1032, this);
            AstraGemListener var537 = new AstraGemListener();
            var25.registerEvents(var537, this);
            List<Consumer<UUID>> var540 = this.cooldownClearers;
            Consumer<UUID> var1035 = var1 -> {
                Map var4 = var537.cd1;
                var4.remove(var1);
                Map var7 = var537.cd2;
                var7.remove(var1);
                Map var10 = var537.cd3;
                var10.remove(var1);
            };
            var540.add(var1035);
            PlayerDataStore var1463 = this.players;
            GlobalState var1775 = this.globalState;
            AstraProjectionListener var542 = new AstraProjectionListener(this, var1463, var537, var1775, 1508769478);
            var25.registerEvents(var542, this);
            var542.loadPending(719311172);
            PlayerDataStore var1641 = this.players;
            AstraDimensionalDriftListener var1037 = new AstraDimensionalDriftListener(this, var1641, var537, 1988113158);
            this.astraDrift = var1037;
            AstraDimensionalDriftListener var1039 = this.astraDrift;
            var25.registerEvents(var1039, this);
            TagStore var1856 = this.tags;
            PlayerDataStore var1885 = this.players;
            GlobalState var1896 = this.globalState;
            TrustStore var1897 = this.trust;
            AstraLifecycleListener var1040 = new AstraLifecycleListener(this, var537, var542, var1856, var1885, var1896, var1897, 1016868767);
            this.astraLifecycle = var1040;
            AstraLifecycleListener var1042 = this.astraLifecycle;
            var25.registerEvents(var1042, this);
            PlayerDataStore var1467 = this.players;
            GlobalState var1779 = this.globalState;
            TagStore var1858 = this.tags;
            AstraDaggersListener var549 = new AstraDaggersListener(this, var1467, var537, var1779, var1858, 603135252);
            this.astraDaggersListener = var549;
            var25.registerEvents(var549, this);
            PlayerDataStore var1645 = this.players;
            AstraSoulStore var1781 = this.astraSouls;
            AstraPassivesListener var1045 = new AstraPassivesListener(this, var1645, var1781, 618995487);
            this.astraPassives = var1045;
            AstraPassivesListener var1047 = this.astraPassives;
            var25.registerEvents(var1047, this);
            PlayerDataStore var1647 = this.players;
            TrustStore var1860 = this.trust;
            TagStore var1887 = this.tags;
            AstraVoidListener var1048 = new AstraVoidListener(this, var1647, var537, var1860, var1887, 1195722415);
            var25.registerEvents(var1048, this);
            LifeGemListener var555 = new LifeGemListener();
            var25.registerEvents(var555, this);
            List<Consumer<UUID>> var558 = this.cooldownClearers;
            Consumer<UUID> var1051 = var1 -> {
                Map var5 = var555.cd1;
                var5.remove(var1);
                Map var8 = var555.cd2;
                var8.remove(var1);
                Map var11 = var555.cd3;
                var11.remove(var1);
                Map var14x = var555.cd4;
                var14x.remove(var1);
                Map var17 = var555.nextHealMs;
                var17.remove(var1);
                Map var20 = var555.drainedGroupExpiry;
                var20.remove(var1);
                Map var23 = var555.drainedSoloExpiry;
                var23.remove(var1);
                Map var26 = var555.colGroupExpiry;
                var26.remove(var1);
                Map var29 = var555.colCasterExpiry;
                var29.remove(var1);
                Map var32 = var555.heartLocked;
                var32.remove(var1);
                Player var4 = Bukkit.getPlayer(var1);
                if (var4 != null) {
                    LifeAuraTask.restoreBaseline(var555, var4, 464898981);
                }

                Map var39 = var555.baselineMaxHealth;
                var39.remove(var1);
            };
            var558.add(var1051);
            PlayerDataStore var1471 = this.players;
            LifeVitalityVortexListener var560 = new LifeVitalityVortexListener(this, var1471, var555, 188648408);
            this.vortexListener = var560;
            var25.registerEvents(var560, this);
            PlayerDataStore var1473 = this.players;
            LifeConsumeListener var1054 = new LifeConsumeListener(var1473, 628127836);
            var25.registerEvents(var1054, this);
            PlayerDataStore var1650 = this.players;
            GlobalState var1862 = this.globalState;
            TrustStore var1889 = this.trust;
            LifePowersListener var1055 = new LifePowersListener(this, var1650, var555, var1862, var1889, 1031779725);
            var25.registerEvents(var1055, this);
            PlayerDataStore var1476 = this.players;
            TagStore var1652 = this.tags;
            FluxGemListener var565 = new FluxGemListener(this, var1476, var1652, 833386351);
            this.fluxGemListener = var565;
            GlobalState var1058 = this.globalState;
            var565.setGlobalState(var1058, 1366025830);
            var565.loadWatts(120552376);
            var25.registerEvents(var565, this);
            var565.start(1453002937);
            PlayerDataStore var1478 = this.players;
            FluxAuraTask var571 = new FluxAuraTask(this, var1478, var565, 505758433);
            var571.start(1540425308);
            BlissCoreItems var1480 = this.coreItems;
            BottledEnergyExpiryTask var573 = new BottledEnergyExpiryTask(this, var1480, 1191760765);
            var573.start(1177133704);
            BlissCoreItems var1482 = this.coreItems;
            PlayerDataStore var1655 = this.players;
            GlobalState var1785 = this.globalState;
            UpgraderGlowTask var574 = new UpgraderGlowTask(this, var1482, var1655, var1785, 1104065251);
            var574.start(1698205511);
            List var576 = this.cooldownClearers;
            Consumer var1061 = var1 -> {
                Map var4 = var565.cd2Expiry;
                var4.remove(var1);
                Map var7 = var565.cd3Expiry;
                var7.remove(var1);
                Map var10 = var565.kineticBurstExpiry;
                var10.remove(var1);
                Map var13x = var565.storedDamage;
                var13x.remove(var1);
                Map var16 = var565.doubleDamageFlag;
                var16.remove(var1);
                Map var19x = var565.kinOvAlt;
                var19x.remove(var1);
                Map var22 = var565.kinOvMain;
                var22.remove(var1);
                Map var25x = var565.kinOvMainCounter;
                var25x.remove(var1);
                Map var28 = var565.kinOvMining;
                var28.remove(var1);
                Map var31 = var565.groundFlag;
                var31.remove(var1);
                Map var34 = var565.damagedFlag;
                var34.remove(var1);
            };
            var576.add(var1061);
            WattsCommand var578 = new WattsCommand(var565, 1750388664);
            PluginCommand var580 = this.getCommand("watts");
            var580.setExecutor(var578);
            PluginCommand var582 = this.getCommand("hiddenwatts");
            var582.setExecutor(var578);
            PluginCommand var584 = this.getCommand("maxwatts");
            var584.setExecutor(var578);
            PluginCommand var586 = this.getCommand("resetwatts");
            var586.setExecutor(var578);
            PluginCommand var588 = this.getCommand("resetcharge");
            var588.setExecutor(var578);
            PluginCommand var590 = this.getCommand("setcharge");
            var590.setExecutor(var578);
            BlissCoreItems var1657 = this.coreItems;
            FluxRechargeListener var1074 = new FluxRechargeListener(var565, var1657, 1351676455);
            var25.registerEvents(var1074, this);
            BlissCoreItems var1787 = this.coreItems;
            WattDepositListener var1075 = new WattDepositListener(this, var565, var1787, 736717218);
            this.wattDeposit = var1075;
            WattDepositListener var1077 = this.wattDeposit;
            var25.registerEvents(var1077, this);
            PlayerDataStore var1789 = this.players;
            TagStore var1864 = this.tags;
            TrustStore var1891 = this.trust;
            FluxKineticBurstListener var1078 = new FluxKineticBurstListener(this, var565, var1789, var1864, var1891, 1675713860);
            var25.registerEvents(var1078, this);
            PlayerDataStore var1661 = this.players;
            TagStore var1791 = this.tags;
            FluxCopperTeleportListener var1079 = new FluxCopperTeleportListener(this, var1661, var1791, 1648606644);
            var25.registerEvents(var1079, this);
            PlayerDataStore var1793 = this.players;
            TagStore var1866 = this.tags;
            FluxKineticOverdriveListener var1080 = new FluxKineticOverdriveListener(this, var565, var1793, var1866, 758526295);
            var25.registerEvents(var1080, this);
            FluxFreezeDeathListener var1081 = new FluxFreezeDeathListener(var565, 1993402250);
            var25.registerEvents(var1081, this);
            PlayerDataStore var1795 = this.players;
            TagStore var1868 = this.tags;
            TrustStore var1893 = this.trust;
            FluxGroundAbilityListener var1082 = new FluxGroundAbilityListener(this, var565, var1795, var1868, var1893, 1576705324);
            var25.registerEvents(var1082, this);
            FluxChargingCommand var599 = new FluxChargingCommand(var565, 1524333405);
            PluginCommand var601 = this.getCommand("startcharging");
            var601.setExecutor(var599);
            PluginCommand var603 = this.getCommand("stopcharging");
            var603.setExecutor(var599);
            List var605 = this.cooldownClearers;
            Consumer var1088 = var1 -> {
                Map var4 = var565.fluxMoved;
                var4.remove(var1);
                Map var7 = var565.fluxMovedExpiry;
                var7.remove(var1);
                Map var10 = var565.fluxSpeedFlags;
                var10.remove(var1);
                Map var13x = var565.fluxDamaged;
                var13x.remove(var1);
                Map var16 = var565.fluxDamagedExpiry;
                var16.remove(var1);
                Map var19x = var565.fluxBroken;
                var19x.remove(var1);
                Map var22 = var565.fluxBrokenExpiry;
                var22.remove(var1);
                Map var25x = var565.fluxPlayerShotExpiry;
                var25x.remove(var1);
                Map var28 = var565.freezeExpiry;
                var28.remove(var1);
            };
            var605.add(var1088);
            PlayerDataStore var1665 = this.players;
            WealthGemListener var1797 = this.wealthGemListener;
            ProjectileListener var1089 = new ProjectileListener(this, var1665, var1797, var565, 183177559);
            var25.registerEvents(var1089, this);
            PlayerDataStore var1667 = this.players;
            LifeAnimalListener var1090 = new LifeAnimalListener(this, var1667, 318199988);
            this.lifeAnimal = var1090;
            LifeAnimalListener var1092 = this.lifeAnimal;
            var25.registerEvents(var1092, this);
            EventState var1669 = this.eventState;
            ParticleTask var1093 = new ParticleTask(this, var1669, 227245614);
            this.particleTask = var1093;
            this.particleTask.start(852463693);
            PedestalState var1671 = this.pedestalState;
            PedestalTickTask var1094 = new PedestalTickTask(this, var1671, 420100293);
            this.pedestalTickTask = var1094;
            this.pedestalTickTask.start(653992028);
            PedestalState var1673 = this.pedestalState;
            PlayerDataStore var1799 = this.players;
            PedestalGlowTask var1095 = new PedestalGlowTask(this, var1673, var1799, 112126878);
            this.pedestalGlowTask = var1095;
            this.pedestalGlowTask.start(1091172933);
            PlayerDataStore var1675 = this.players;
            GemAuraTask var1096 = new GemAuraTask(this, var1675, var477, 2101250654);
            this.gemAuraTask = var1096;
            this.gemAuraTask.start(750436014);
            EnchantedObsidianTask var1097 = new EnchantedObsidianTask(this, 534288335);
            this.enchantedObsidianTask = var1097;
            this.enchantedObsidianTask.start(1041087414);
            PlayerDataStore var1677 = this.players;
            BrokenActionBarTask var1098 = new BrokenActionBarTask(this, var1677, 1281101942);
            this.brokenActionBarTask = var1098;
            this.brokenActionBarTask.start(1892990968);
            PlayerDataStore var1679 = this.players;
            GlobalState var1871 = this.globalState;
            FireAuraTask var1099 = new FireAuraTask(this, var1679, var485, var1871, 659076870);
            this.fireAuraTask = var1099;
            this.fireAuraTask.start(2134503211);
            TrustStore var1803 = this.trust;
            FireCrispBurnTask var1100 = new FireCrispBurnTask(this, var490, var1803, 1156781060);
            this.fireCrispBurnTask = var1100;
            this.fireCrispBurnTask.start(320528266);
            TrustStore var1805 = this.trust;
            FireCozyHealTask var1101 = new FireCozyHealTask(this, var495, var1805, 1960257496);
            this.fireCozyHealTask = var1101;
            this.fireCozyHealTask.start(708917917);
            PlayerDataStore var1683 = this.players;
            WealthGemListener var1807 = this.wealthGemListener;
            WealthAuraTask var1102 = new WealthAuraTask(this, var1683, var1807, 24649259);
            this.wealthAuraTask = var1102;
            this.wealthAuraTask.start(2018538528);
            PlayerDataStore var1685 = this.players;
            SpeedAuraTask var1103 = new SpeedAuraTask(this, var1685, var516, 1672683508);
            this.speedAuraTask = var1103;
            this.speedAuraTask.start(1316396072);
            PlayerDataStore var1687 = this.players;
            LegacySpeedState var1810 = this.legacySpeedState;
            GlobalState var1873 = this.globalState;
            LegacySpeedAuraTask var1104 = new LegacySpeedAuraTask(this, var1687, var1810, var1873, 767723262);
            this.legacySpeedAura = var1104;
            this.legacySpeedAura.start(683025693);
            PlayerDataStore var1689 = this.players;
            GlobalState var1812 = this.globalState;
            LegacySoulSpeedTask var1105 = new LegacySoulSpeedTask(this, var1689, var1812, 140036459);
            this.legacySoulSpeed = var1105;
            this.legacySoulSpeed.start(57110305);
            PlayerDataStore var1691 = this.players;
            PuffAuraTask var1106 = new PuffAuraTask(this, var1691, var531, 849492813);
            this.puffAuraTask = var1106;
            this.puffAuraTask.start(80421338);
            PuffFlightTask var1107 = new PuffFlightTask(this, var531, 590098752);
            this.puffFlightTask = var1107;
            this.puffFlightTask.start(234232743);
            PlayerDataStore var1694 = this.players;
            AstraSoulStore var1875 = this.astraSouls;
            AstraAuraTask var1108 = new AstraAuraTask(this, var1694, var537, var1875, 1541381039);
            this.astraAuraTask = var1108;
            this.astraAuraTask.start(1819875971);
            this.astraLifecycle.start(703627738);
            PlayerDataStore var1696 = this.players;
            LifeAuraTask var1109 = new LifeAuraTask(this, var1696, var555, 170740927);
            this.lifeAuraTask = var1109;
            this.lifeAuraTask.start(1451213434);
            GoldGemListener var1110 = new GoldGemListener();
            var25.registerEvents(var1110, this);
            GoldGemTrimTask var1111 = new GoldGemTrimTask(this, 1199379847);
            this.goldGemTrimTask = var1111;
            this.goldGemTrimTask.start(173181376);
            PlayerDataStore var1698 = this.players;
            GoldGemManager var1817 = this.goldGemManager;
            GoldGemHarvestListener var1112 = new GoldGemHarvestListener(this, var1698, var1817, 625042604);
            this.goldGemHarvest = var1112;
            GoldGemHarvestListener var1114 = this.goldGemHarvest;
            var25.registerEvents(var1114, this);
            GoldGemItemGuardListener var1115 = new GoldGemItemGuardListener();
            var25.registerEvents(var1115, this);
            FragmentCoreRitual var1116 = new FragmentCoreRitual(this, 1577966718);
            this.fragmentCoreRitual = var1116;
            FragmentCoreRitual var1118 = this.fragmentCoreRitual;
            var25.registerEvents(var1118, this);
            this.fragmentCoreRitual.start(1954804811);
            GoldenDreamRitual var1119 = new GoldenDreamRitual(this, 285854054);
            this.goldenDream = var1119;
            GoldenDreamRitual var1121 = this.goldenDream;
            var25.registerEvents(var1121, this);
            DreamTabList var1122 = new DreamTabList(this, 1379486507);
            this.dreamTabList = var1122;
            DreamTabList var1124 = this.dreamTabList;
            var25.registerEvents(var1124, this);
            this.dreamTabList.refreshAll(1606045733);
            DreamChannels var1125 = new DreamChannels();
            var25.registerEvents(var1125, this);
            GoldenDreamWorld var1516 = this.goldenDream.dream(2117018474);
            GoldenDreamMenu var681 = new GoldenDreamMenu(this, var1516, 2135303502);
            var25.registerEvents(var681, this);
            if (this.getCommand("goldendream") != null) {
                GoldenDreamWorld var1701 = this.goldenDream.dream(2117018474);
                NickCommand var1819 = this.nickCommand;
                GoldenDreamCommand var685 = new GoldenDreamCommand(this, var681, var1701, var1819, 2134113703);
                PluginCommand var687 = this.getCommand("goldendream");
                var687.setExecutor(var685);
                PluginCommand var689 = this.getCommand("goldendream");
                var689.setTabCompleter(var685);
            }

            this.lifeAnimal.start(1093554598);
            DragonEggBuffTask var1132 = new DragonEggBuffTask(this, 580435099);
            this.dragonEggBuffTask = var1132;
            this.dragonEggBuffTask.start(1471681627);
            BukkitScheduler var697 = this.getServer().getScheduler();
            Runnable var1313 = () -> {
                this.players.save(1141447725);
                this.trust.save(751174642);
                this.advancements.save(491871852);
                this.astraSouls.save(1986509);
                if (this.wealthPocketsListener != null) {
                    this.wealthPocketsListener.save(1917997178);
                }

                if (this.eventState != null) {
                    this.eventState.save(1112893273);
                }

                if (this.globalState != null) {
                    this.globalState.save(343464104);
                }

                if (this.dreamState != null) {
                    this.dreamState.save(343464104);
                }

                if (this.fluxGemListener != null) {
                    this.fluxGemListener.saveWatts(1607488947);
                }

                if (this.kitRules != null) {
                    this.kitRules.save(231690688);
                }

                if (this.cageManager != null) {
                    this.cageManager.save(1684552950);
                }

                if (this.pedestalState != null) {
                    this.pedestalState.save(973788971);
                }
            };
            var697.runTaskTimer(this, var1313, 6000L, 6000L);
            if (this.globalState != null && !this.globalState.mythicGems(289981390)) {
                this.getLogger().info("Mythic gems are switched off in /bliss config — not loading them.");
            } else {
                MythicsModule var1134 = new MythicsModule(this, 1504515270);
                this.mythics = var1134;
                this.mythics.enable(1010186128);
            }

            MythicMainGemTask var707 = new MythicMainGemTask(this, 1258902739);
            var707.start(497920518);
            PlayerDataStore var1316 = this.players;
            DreamContextListener var708 = new DreamContextListener(var1316, 1417597300);
            DreamContextListener var1912 = var708;
            PluginManager var711 = this.getServer().getPluginManager();
            var711.registerEvents(var708, this);

            for (Player var48 : this.getServer().getOnlinePlayers()) {
                var1912.refresh(var48, 2041142238);
            }

            Logger var723 = this.getLogger();
            String var1140 = "BlissPlugin v" + this.getDescription().getVersion() + " enabled.";
            var723.info(var1140);
        }
    }

    private void safe(String var1, Runnable var2, int var3) {
        try {
            var2.run();
        } catch (Throwable var13) {
            Logger var11 = this.getLogger();
            Level var5 = Level.WARNING;
            String var12 = "onDisable: " + var1 + " failed";
            var11.log(var5, var12, var13);
        }
    }

    public void onDisable() {
        String var2 = "juggernaut kit edit";
        Runnable var83 = () -> {
            if (this.mythics != null && this.mythics.kitEditor(1009541039) != null) {
                this.mythics.kitEditor(1009541039).shutdown(1447759135);
            }
        };
        this.safe(var2, var83, 1793957215);
        String var44 = "memory npcs";
        Runnable var85 = () -> {
            if (this.memoryNpcs != null) {
                this.memoryNpcs.shutdown(1005774354);
            }
        };
        this.safe(var44, var85, 1793957215);
        String var45 = "golden dream abort";
        Runnable var87 = () -> {
            if (this.goldenDream != null) {
                this.goldenDream.abortAll(358701842);
            }
        };
        this.safe(var45, var87, 1793957215);
        String var46 = "golden dream walkers";
        Runnable var89 = () -> {
            if (this.goldenDream != null) {
                this.goldenDream.dream(2117018474).shutdown(361587781);
            }
        };
        this.safe(var46, var89, 1793957215);
        String var47 = "wake float";
        Runnable var90 = WakeFloat::shutdown;
        this.safe(var47, var90, 1793957215);
        String var48 = "dream tab list";
        Runnable var92 = () -> {
            if (this.dreamTabList != null) {
                this.dreamTabList.listAll(1571936409);
            }
        };
        this.safe(var48, var92, 1793957215);
        String var49 = "gem pov";
        Runnable var93 = PovOverlay::shutdown;
        this.safe(var49, var93, 1793957215);
        String var50 = "watt deposit";
        Runnable var95 = () -> {
            if (this.wattDeposit != null) {
                this.wattDeposit.shutdown(1490762250);
            }
        };
        this.safe(var50, var95, 1793957215);
        String var51 = "pending gems";
        Runnable var97 = () -> {
            if (this.pendingGems != null) {
                this.pendingGems.stop(1375628618);
            }
        };
        this.safe(var51, var97, 1793957215);
        String var52 = "dragon games";
        Runnable var99 = () -> {
            if (this.dragonGames != null) {
                this.dragonGames.stop(967234841);
            }
        };
        this.safe(var52, var99, 1793957215);
        String var53 = "mythics";
        Runnable var101 = () -> {
            if (this.mythics != null) {
                this.mythics.disable(1490663378);
            }
        };
        this.safe(var53, var101, 1793957215);
        String var54 = "advancement tab";
        Runnable var103 = () -> {
            if (this.advancementTab != null) {
                this.advancementTab.stop(177338613);
            }
        };
        this.safe(var54, var103, 1793957215);
        String var55 = "honored + assessment";
        Runnable var105 = () -> {
            if (this.honoredCompassTask != null) {
                this.honoredCompassTask.cancel();
            }

            if (this.windowBarTask != null) {
                this.windowBarTask.cancel();
            }

            AssessmentWindowBarTask.stopShowing(1546521786);
            if (this.honoredNameplate != null) {
                this.honoredNameplate.cancel();
                this.honoredNameplate.remove(1165561497);
            }

            if (this.cageTask != null) {
                this.cageTask.cancel();
            }

            if (this.kitRestrictions != null) {
                this.kitRestrictions.stop(167607009);
            }

            if (this.progression2Rules != null) {
                this.progression2Rules.stop(632442920);
            }

            if (this.megaMaceListener != null) {
                this.megaMaceListener.stop(1780509133);
            }

            if (this.wealthGemListener != null) {
                this.wealthGemListener.stop(43576081);
            }

            if (this.entityMotifs != null) {
                this.entityMotifs.stop(1415094238);
            }

            if (this.motifSpeech != null) {
                this.motifSpeech.stop(2129140659);
            }

            if (this.crushedPedestalOrb != null) {
                this.crushedPedestalOrb.shutdown(432509810);
            }

            if (this.enchantLimit != null) {
                this.enchantLimit.shutdown(1172298366);
            }

            if (this.armorLimit != null) {
                this.armorLimit.shutdown(2090079936);
            }

            if (this.worldBorderHologram != null) {
                this.worldBorderHologram.shutdown(377119537);
            }

            if (this.potionLimit != null) {
                this.potionLimit.shutdown(1135467720);
            }

            if (this.controlRoomVeil != null) {
                this.controlRoomVeil.shutdown(1255902568);
            }

            if (this.departurePrompt != null) {
                this.departurePrompt.shutdown(1109315045);
            }

            if (this.departureFlight != null) {
                this.departureFlight.shutdown(704761773);
            }

            if (this.pedestalFinale != null) {
                this.pedestalFinale.shutdown(1227814927);
            }
        };
        this.safe(var55, var105, 1793957215);
        String var56 = "villager raid";
        Runnable var106 = () -> {
            if (VillagerRaid.current(137107344) != null) {
                VillagerRaid.current(137107344).stop(false, 917832850);
            }
        };
        this.safe(var56, var106, 1793957215);
        String var57 = "tasks";
        Runnable var108 = () -> {
            if (this.particleTask != null) {
                this.particleTask.stop(1475210454);
            }

            if (this.pedestalTickTask != null) {
                this.pedestalTickTask.stop(1743774796);
            }

            if (this.pedestalGlowTask != null) {
                this.pedestalGlowTask.stop(427933787);
            }

            if (this.gemAuraTask != null) {
                this.gemAuraTask.stop(3309059);
            }

            if (this.fragmentCoreRitual != null) {
                this.fragmentCoreRitual.stop(971606136);
            }

            if (this.goldGemTrimTask != null) {
                this.goldGemTrimTask.stop(59934328);
            }

            if (this.goldGemHarvest != null) {
                this.goldGemHarvest.shutdown(912231482);
            }

            if (this.enchantedObsidianTask != null) {
                this.enchantedObsidianTask.stop(555262373);
            }

            if (this.brokenActionBarTask != null) {
                this.brokenActionBarTask.stop(2141601525);
            }

            if (this.fireAuraTask != null) {
                this.fireAuraTask.stop(1078759288);
            }

            if (this.fireCrispBurnTask != null) {
                this.fireCrispBurnTask.stop(1418758845);
            }

            if (this.fireCozyHealTask != null) {
                this.fireCozyHealTask.stop(1194712152);
            }

            if (this.wealthAuraTask != null) {
                this.wealthAuraTask.stop(55428941);
            }

            if (this.speedAuraTask != null) {
                this.speedAuraTask.stop(409266179);
            }

            if (this.legacySpeedAura != null) {
                this.legacySpeedAura.stop(535056058);
            }

            if (this.legacySoulSpeed != null) {
                this.legacySoulSpeed.stop(1656674528);
            }

            if (this.puffAuraTask != null) {
                this.puffAuraTask.stop(111512974);
            }

            if (this.puffFlightTask != null) {
                this.puffFlightTask.stop(1761147991);
            }

            if (this.astraAuraTask != null) {
                this.astraAuraTask.stop(1837381209);
            }

            if (this.astraLifecycle != null) {
                this.astraLifecycle.stop(241501152);
            }
        };
        this.safe(var57, var108, 1793957215);
        String var58 = "astra drift armour";
        Runnable var110 = () -> {
            if (this.astraDrift != null) {
                this.astraDrift.restoreAll(1425834);
            }
        };
        this.safe(var58, var110, 1793957215);
        String var59 = "astra phase armour";
        Runnable var112 = () -> {
            if (this.astraPassives != null) {
                this.astraPassives.restoreAll(1987462127);
            }
        };
        this.safe(var59, var112, 1793957215);
        String var60 = "revive ritual restore";
        Runnable var114 = () -> {
            if (this.reviveRitual != null) {
                this.reviveRitual.shutdownRestore(1765851402);
            }
        };
        this.safe(var60, var114, 1793957215);
        String var61 = "vortex terrain";
        Runnable var116 = () -> {
            if (this.vortexListener != null) {
                this.vortexListener.revertAll(2008385805);
            }
        };
        this.safe(var61, var116, 1793957215);
        String var62 = "crisp terrain";
        Runnable var118 = () -> {
            if (this.crispListener != null) {
                this.crispListener.revertAll(247202005);
            }
        };
        this.safe(var62, var118, 1793957215);
        String var63 = "astra daggers";
        Runnable var120 = () -> {
            if (this.astraDaggersListener != null) {
                this.astraDaggersListener.stopAll(1746599903);
            }
        };
        this.safe(var63, var120, 1793957215);
        String var64 = "life";
        Runnable var122 = () -> {
            if (this.lifeAuraTask != null) {
                this.lifeAuraTask.stop(1432565703);
            }

            if (this.lifeAnimal != null) {
                this.lifeAnimal.stop(874980914);
            }

            if (this.dragonEggBuffTask != null) {
                this.dragonEggBuffTask.stop(807043176);
            }
        };
        this.safe(var64, var122, 1793957215);
        String var65 = "wealth pockets";
        Runnable var124 = () -> {
            if (this.wealthPocketsListener != null) {
                this.wealthPocketsListener.shutdown(5446788);
            }
        };
        this.safe(var65, var124, 1793957215);
        String var66 = "fireball bars";
        Runnable var126 = () -> {
            if (this.fireballListener != null) {
                this.fireballListener.stopAll(938950552);
            }
        };
        this.safe(var66, var126, 1793957215);
        String var67 = "cozy campfires";
        Runnable var128 = () -> {
            if (this.fireCozyListener != null) {
                this.fireCozyListener.shutdown(23279355);
            }
        };
        this.safe(var67, var128, 1793957215);
        String var68 = "bounty hoppers";
        Runnable var130 = () -> {
            if (this.bountyHunterListener != null) {
                this.bountyHunterListener.shutdown(499690671);
            }
        };
        this.safe(var68, var130, 1793957215);
        String var69 = "task claims";
        Runnable var131 = TasksCommand::releaseAll;
        this.safe(var69, var131, 1793957215);
        String var70 = "save eventState";
        Runnable var133 = () -> {
            if (this.eventState != null) {
                this.eventState.save(1112893273);
            }
        };
        this.safe(var70, var133, 1793957215);
        String var71 = "save pedestalState";
        Runnable var135 = () -> {
            if (this.pedestalState != null) {
                this.pedestalState.save(973788971);
            }
        };
        this.safe(var71, var135, 1793957215);
        String var72 = "save globalState";
        Runnable var137 = () -> {
            if (this.globalState != null) {
                this.globalState.save(343464104);
            }
        };
        this.safe(var72, var137, 1793957215);
        String var73 = "save dreamState";
        Runnable var139 = () -> {
            if (this.dreamState != null) {
                this.dreamState.save(343464104);
            }
        };
        this.safe(var73, var139, 1793957215);
        String var74 = "save pockets";
        Runnable var141 = () -> {
            if (this.wealthPocketsListener != null) {
                this.wealthPocketsListener.save(1917997178);
            }
        };
        this.safe(var74, var141, 1793957215);
        String var75 = "save trust";
        Runnable var143 = () -> {
            if (this.trust != null) {
                this.trust.save(751174642);
            }
        };
        this.safe(var75, var143, 1793957215);
        String var76 = "save advancements";
        Runnable var145 = () -> {
            if (this.advancements != null) {
                this.advancements.save(491871852);
            }
        };
        this.safe(var76, var145, 1793957215);
        String var77 = "save souls";
        Runnable var147 = () -> {
            if (this.astraSouls != null) {
                this.astraSouls.save(1986509);
            }
        };
        this.safe(var77, var147, 1793957215);
        String var78 = "save players";
        Runnable var149 = () -> {
            if (this.players != null) {
                this.players.save(1141447725);
            }
        };
        this.safe(var78, var149, 1793957215);
        String var79 = "save watts";
        Runnable var151 = () -> {
            if (this.fluxGemListener != null) {
                this.fluxGemListener.saveWatts(1607488947);
            }
        };
        this.safe(var79, var151, 1793957215);
        String var80 = "save cage";
        Runnable var153 = () -> {
            if (this.cageManager != null) {
                this.cageManager.save(1684552950);
            }
        };
        this.safe(var80, var153, 1793957215);
        String var81 = "save kitRules";
        Runnable var155 = () -> {
            if (this.kitRules != null) {
                this.kitRules.save(231690688);
            }
        };
        this.safe(var81, var155, 1793957215);
        this.getLogger().info("BlissPlugin disabled.");
    }

    public EventState eventState(int var1) {
        return this.eventState;
    }

    public ControlRoomManager controlRoom(int var1) {
        return this.controlRoom;
    }

    public EnchantLimitHologram enchantLimit(int var1) {
        return this.enchantLimit;
    }

    public EnchantLimits enchantLimits(int var1) {
        return this.enchantLimits;
    }

    public WealthGemListener wealthGemListener(int var1) {
        return this.wealthGemListener;
    }

    public ArmorLimitHologram armorLimit(int var1) {
        return this.armorLimit;
    }

    public ArmorLimits armorLimits(int var1) {
        return this.armorLimits;
    }

    public WorldBorderHologram worldBorderHologram(int var1) {
        return this.worldBorderHologram;
    }

    public PotionLimitHologram potionLimit(int var1) {
        return this.potionLimit;
    }

    public PotionLimits potionLimits(int var1) {
        return this.potionLimits;
    }

    public ControlRoomDeparturePrompt departurePrompt(int var1) {
        return this.departurePrompt;
    }

    public DepartureFlight departureFlight(int var1) {
        return this.departureFlight;
    }

    public PedestalFinale pedestalFinale(int var1) {
        return this.pedestalFinale;
    }

    public ControlRoomVeil controlRoomVeil(int var1) {
        return this.controlRoomVeil;
    }

    private void ensureFixedPedestalConfig(int var1) {
        FileConfiguration config = this.getConfig();
        if (config.isSet("fixed-pedestal")) {
            return;
        }
        try (InputStream res = this.getResource("config.yml")) {
            if (res == null) {
                return;
            }
            ConfigurationSection section = YamlConfiguration.loadConfiguration(new InputStreamReader(res, StandardCharsets.UTF_8))
                .getConfigurationSection("fixed-pedestal");
            if (section == null) {
                return;
            }
            config.createSection("fixed-pedestal", section.getValues(true));
            this.saveConfig();
            this.getLogger().info("Merged the bundled fixed-pedestal block into config.yml (was missing).");
        } catch (Exception ex) {
            this.getLogger().warning("Could not merge fixed-pedestal config: " + ex.getMessage());
        }
    }

    private YamlConfiguration bundledConfig(int var1) {
        try (InputStream res = this.getResource("config.yml")) {
            if (res == null) {
                return null;
            }
            return YamlConfiguration.loadConfiguration(new InputStreamReader(res, StandardCharsets.UTF_8));
        } catch (Exception ex) {
            return null;
        }
    }

    private void migrateTrueDamageOverrides(int var1) {
        FileConfiguration var7 = this.getConfig();
        boolean var8 = this.globalState.trueDamage(755209314);
        boolean var9 = var7.isSet("gems.true-damage-exact");
        String var101 = "gems.true-damage-exact";
        boolean var10 = var7.getBoolean(var101, false);
        int var11 = 0;
        int var12 = 0;
        ArrayList var37 = new ArrayList();
        ArrayList var13 = var37;
        YamlConfiguration var14 = this.bundledConfig(1639104999);

        for (String var18 : TUNING_ROOTS) {
            ConfigurationSection var19 = var7.getConfigurationSection(var18);
            if (var19 != null) {
                for (String var21 : var19.getKeys(false)) {
                    String var22 = var18 + "." + var21 + ".true-damage-override";
                    ConfigurationSection var23 = var7.getConfigurationSection(var22);
                    if (var23 != null) {
                        for (String var25 : var23.getKeys(false)) {
                            if (!var23.isConfigurationSection(var25)) {
                                String var26 = var18 + "." + var21 + ".damage." + var25;
                                if (var8) {
                                    Object var133 = var23.get(var25);
                                    var7.set(var26, var133);
                                    if (var14 != null && var14.isSet(var26)) {
                                        List var135 = var14.getComments(var26);
                                        var7.setComments(var26, var135);
                                        List var137 = var14.getInlineComments(var26);
                                        var7.setInlineComments(var26, var137);
                                    }

                                    var11++;
                                } else {
                                    String var142 = String.valueOf(var23.get(var25));
                                    String var119 = var22 + "." + var25 + "=" + var142;
                                    var13.add(var119);
                                }
                            }
                        }

                        var7.set(var22, null);
                        var12++;
                    }
                }
            }
        }

        if (var9) {
            String var103 = "gems.true-damage-exact";
            var7.set(var103, null);
        }

        if (var12 != 0 || var9) {
            this.saveConfig();
            Logger var51 = this.getLogger();
            String var127;
            if (var11 > 0) {
                var127 = ", " + var11 + " value(s) folded into damage: (they were the numbers being dealt, nothing changes in play)";
            } else {
                var127 = "";
            }

            String var139;
            if (var9) {
                var139 = "; gems.true-damage-exact removed, True Damage always lands the config number as written now";
            } else {
                var139 = "";
            }

            String var105 = "Gem ability tuning: " + var12 + " true-damage-override block(s) removed" + var127 + var139 + ".";
            var51.info(var105);
            if (!var13.isEmpty()) {
                Logger var55 = this.getLogger();
                String var106 = ", ";
                String var108 = "Gem ability tuning: True Damage is OFF here, so these override values were never in use and are dropped: "
                    + String.join(var106, var13);
                var55.info(var108);
            }

            if (var8 && var9 && !var10) {
                this.getLogger()
                    .warning(
                        "This server ran True Damage with true-damage-exact OFF, so the folded numbers were reduced by the reference kit before landing. They land as written now, which is higher: check gems.*.damage.* and retune where it matters."
                    );
            }
        }
    }

    private void ensureGemTuningConfig(String root, int var2) {
        FileConfiguration config = this.getConfig();
        ConfigurationSection existing = config.isSet(root) ? config.getConfigurationSection(root) : null;
        if (existing != null && !existing.getKeys(true).isEmpty()) {
            this.getLogger().info("Gem ability tuning: config.yml already has the " + root + " block.");
            this.refreshGemTuningComments(config, root, 2122088287);
            return;
        }
        try (InputStream res = this.getResource("config.yml")) {
            if (res == null) {
                this.getLogger().warning("Gem ability tuning: the bundled config.yml is missing from the jar; nothing merged.");
                return;
            }
            YamlConfiguration bundled = YamlConfiguration.loadConfiguration(new InputStreamReader(res, StandardCharsets.UTF_8));
            ConfigurationSection bundledSec = bundled.getConfigurationSection(root);
            if (bundledSec == null) {
                this.getLogger().warning("Gem ability tuning: the bundled config.yml has no " + root + " block; nothing merged.");
                return;
            }
            config.set(root, null);
            copySection(bundledSec, config.createSection(root), 479608782);
            config.setComments(root, bundled.getComments(root));
            for (String key : bundledSec.getKeys(true)) {
                String path = root + "." + key;
                config.setComments(path, bundled.getComments(path));
                config.setInlineComments(path, bundled.getInlineComments(path));
            }
            reorderToBundledOrder(config, bundled, 2096523366);
            this.saveConfig();
            boolean allSet = true;
            for (String r : TUNING_ROOTS) {
                if (!config.isSet(r)) {
                    allSet = false;
                }
            }
            if (allSet) {
                this.gemsDesignApplied(4, 2030381265);
            }
            this.getLogger().info("Merged the bundled " + root + " tuning block into config.yml (was missing).");
        } catch (Exception ex) {
            this.getLogger().warning("Could not merge " + root + " tuning config: " + ex.getMessage());
        }
    }

    private int gemsDesignApplied(int var1) {
        File var17 = this.getDataFolder();
        String var4 = "gems-design.version";
        File var2 = new File(var17, var4);
        File var5 = var2;
        if (!var2.isFile()) {
            return 0;
        } else {
            try {
                return Integer.parseInt(Files.readString(var5.toPath()).trim());
            } catch (Exception var18) {
                return 0;
            }
        }
    }

    private void gemsDesignApplied(int var1, int var2) {
        try {
            File var17 = this.getDataFolder();
            String var5 = "gems-design.version";
            File var3 = new File(var17, var5);
            Path var7 = var3.toPath();
            String var13 = Integer.toString(var1);
            OpenOption[] var19 = new OpenOption[0];
            Files.writeString(var7, var13, var19);
        } catch (Exception var20) {
            Logger var11 = this.getLogger();
            String var16 = "Could not record the gems design version: " + var20.getMessage();
            var11.warning(var16);
        }
    }

    private void refreshGemTuningComments(FileConfiguration config, String root, int var3) {
        try (InputStream res = this.getResource("config.yml")) {
            if (res == null) {
                return;
            }
            YamlConfiguration bundled = YamlConfiguration.loadConfiguration(new InputStreamReader(res, StandardCharsets.UTF_8));
            ConfigurationSection bundledSec = bundled.getConfigurationSection(root);
            if (bundledSec == null) {
                return;
            }
            boolean bannerUpdated = false;
            boolean notesUpdated = false;
            if (4 > this.gemsDesignApplied(1642071068)) {
                config.setComments(root, bundled.getComments(root));
                for (String topKey : bundledSec.getKeys(false)) {
                    String path = root + "." + topKey;
                    if (config.isSet(path)) {
                        config.setComments(path, bundled.getComments(path));
                    }
                }
                bannerUpdated = true;
            }
            int addedCount = 0;
            for (String key : bundledSec.getKeys(true)) {
                if (!bundledSec.isConfigurationSection(key)) {
                    String path = root + "." + key;
                    if (!config.isSet(path)) {
                        config.set(path, bundledSec.get(key));
                        config.setComments(path, bundled.getComments(path));
                        config.setInlineComments(path, bundled.getInlineComments(path));
                        addedCount++;
                    }
                }
            }
            if (addedCount > 0) {
                notesUpdated = true;
            }
            for (String key : bundledSec.getKeys(true)) {
                if (key.contains(".")) {
                    String path = root + "." + key;
                    if (config.isSet(path)) {
                        if (config.getComments(path).isEmpty() && !bundled.getComments(path).isEmpty()) {
                            config.setComments(path, bundled.getComments(path));
                            notesUpdated = true;
                        }
                        if (config.getInlineComments(path).isEmpty() && !bundled.getInlineComments(path).isEmpty()) {
                            config.setInlineComments(path, bundled.getInlineComments(path));
                            notesUpdated = true;
                        }
                    }
                }
            }
            if (!bannerUpdated && !notesUpdated) {
                return;
            }
            this.saveConfig();
            if (bannerUpdated && root.equals(TUNING_ROOTS[TUNING_ROOTS.length - 1])) {
                this.gemsDesignApplied(4, 2030381265);
            }
            String part1 = bannerUpdated ? "banner updated to design v4" : "";
            String sep = (bannerUpdated && notesUpdated) ? ", " : "";
            String part2 = notesUpdated ? ((addedCount > 0 ? addedCount + " missing key(s) added, " : "") + "missing key notes filled in") : "";
            this.getLogger().info("Gem ability tuning [" + root + "]: " + part1 + sep + part2 + ".");
        } catch (Exception ex) {
            this.getLogger().warning("Could not refresh " + root + " tuning comments: " + ex.getMessage());
        }
    }

    private static void reorderToBundledOrder(FileConfiguration var0, YamlConfiguration var1, int var2) {
        Set var131 = var1.getKeys(false);
        ArrayList<String> var3 = new ArrayList<>(var131);
        ArrayList<String> var7 = var3;

        for (String var153 : var0.getKeys(false)) {
            if (!var7.contains(var153)) {
                var7.add(var153);
            }
        }

        Predicate<String> var109 = var1x -> {
            boolean var5;
            if (!var0.isSet(var1x)) {
                var5 = true;
            } else {
                var5 = false;
            }

            return var5;
        };
        var7.removeIf(var109);
        Set<String> var147 = var0.getKeys(false);
        ArrayList<String> var110 = new ArrayList<>(var147);
        if (!var7.equals(var110)) {
            LinkedHashMap<String, Object> var24 = new LinkedHashMap<>();
            LinkedHashMap<String, Object> var152 = var24;
            HashMap<String, List<String>> var25 = new HashMap<>();
            HashMap<String, List<String>> var9 = var25;
            HashMap<String, List<String>> var26 = new HashMap<>();
            HashMap<String, List<String>> var10 = var26;

            for (String var161 : var0.getKeys(true)) {
                List<String> var143 = var0.getComments(var161);
                var9.put(var161, var143);
                List<String> var145 = var0.getInlineComments(var161);
                var10.put(var161, var145);
            }

            for (String var160 : var7) {
                if (var0.isConfigurationSection(var160)) {
                    MemoryConfiguration var88 = new MemoryConfiguration();
                    ConfigurationSection var90 = var0.getConfigurationSection(var160);
                    copySection(var90, var88, 479608782);
                    var152.put(var160, var88);
                } else {
                    Object var140 = var0.get(var160);
                    var152.put(var160, var140);
                }
            }

            Set<String> var133 = var0.getKeys(false);

            for (String var12 : new ArrayList<>(var133)) {
                var0.set(var12, null);
            }

            for (String var158 : var7) {
                Object var13 = var152.get(var158);
                if (var13 instanceof ConfigurationSection var58) {
                    ConfigurationSection var116 = var0.createSection(var158);
                    copySection(var58, var116, 479608782);
                } else {
                    var0.set(var158, var13);
                }
            }

            for (String var159 : var0.getKeys(true)) {
                List var162 = (List)var9.get(var159);
                List var164 = (List)var10.get(var159);
                if (var162 != null && !var162.isEmpty()) {
                    var0.setComments(var159, var162);
                }

                if (var164 != null && !var164.isEmpty()) {
                    var0.setInlineComments(var159, var164);
                }
            }
        }
    }

    private static void copySection(ConfigurationSection var0, ConfigurationSection var1, int var2) {
        for (String var8 : var0.getKeys(false)) {
            if (var0.isConfigurationSection(var8)) {
                ConfigurationSection var19 = var0.getConfigurationSection(var8);
                ConfigurationSection var24 = var1.createSection(var8);
                copySection(var19, var24, 479608782);
            } else {
                Object var27 = var0.get(var8);
                var1.set(var8, var27);
            }
        }
    }

    private void applyGemTuningResets(String root, int var2) {
        FileConfiguration config = this.getConfig();
        try (InputStream res = this.getResource("config.yml")) {
            if (res == null) {
                return;
            }
            ConfigurationSection bundledSec = YamlConfiguration.loadConfiguration(new InputStreamReader(res, StandardCharsets.UTF_8))
                .getConfigurationSection(root);
            if (bundledSec == null) {
                return;
            }
            boolean resetAll = config.getBoolean(root + ".reset-all-to-default", false);
            List<String> resetGems = new ArrayList<>();
            for (String gemKey : bundledSec.getKeys(false)) {
                if (!bundledSec.isConfigurationSection(gemKey)) {
                    continue;
                }
                if (!resetAll && !config.getBoolean(root + "." + gemKey + ".reset-to-default", false)) {
                    continue;
                }
                ConfigurationSection gemSec = bundledSec.getConfigurationSection(gemKey);
                for (String subKey : gemSec.getKeys(true)) {
                    if (!gemSec.isConfigurationSection(subKey)) {
                        config.set(root + "." + gemKey + "." + subKey, gemSec.get(subKey));
                    }
                }
                resetGems.add(gemKey);
            }
            if (resetAll) {
                config.set(root + ".reset-all-to-default", false);
            }
            if (resetGems.isEmpty()) {
                return;
            }
            this.saveConfig();
            this.getLogger().info("Gem ability tuning [" + root + "]: reset to defaults — " + (resetAll ? "ALL gems" : String.join(", ", resetGems)) + ".");
        } catch (Exception ex) {
            this.getLogger().warning("Could not apply the " + root + " reset switches: " + ex.getMessage());
        }
    }

    private static ItemStack awakenedGoldGemPrototype(int var0) {
        ItemStack var3 = GoldGem.create(485535734);
        GoldGem.setMask(var3, 255, 1710446259);
        GoldGem.setState(var3, 1, 953378395);
        GoldGem.applyVisuals(var3, 632368650);
        return var3;
    }

    private static void registerMenuGive(String var0, ItemStack var1, int var2) {
        ItemStack var7 = var1.clone();
        Consumer<Player> var10 = var1x -> {
            PlayerInventory var8 = var1x.getInventory();
            ItemStack[] var20 = new ItemStack[1];
            ItemStack var24 = var7.clone();
            var20[0] = var24;

            for (ItemStack var7x : var8.addItem(var20).values()) {
                World var18 = var1x.getWorld();
                Location var22 = var1x.getLocation();
                var18.dropItemNaturally(var22, var7x);
            }
        };
        MenuListener.registerExternal(var0, var1, null, var10, 153575556);
    }

    public BlissItems villagerItems(int var1) {
        return this.villagerItems;
    }

    public PlayerDataStore players(int var1) {
        return this.players;
    }

    public BlissCoreItems coreItems(int var1) {
        return this.coreItems;
    }

    public PendingGemStore pendingGems(int var1) {
        return this.pendingGems;
    }

    public DragonGamesHook dragonGames(int var1) {
        return this.dragonGames;
    }

    public FragmentCoreRitual fragmentCoreRitual(int var1) {
        return this.fragmentCoreRitual;
    }

    public GoldenDreamRitual goldenDream(int var1) {
        return this.goldenDream;
    }

    public BlissRecipes recipes(int var1) {
        return this.recipes;
    }

    public AstraPassivesListener astraPassives(int var1) {
        return this.astraPassives;
    }

    public PedestalState pedestalState(int var1) {
        return this.pedestalState;
    }

    public KitRules kitRules(int var1) {
        return this.kitRules;
    }

    public CrushedPedestalOrb crushedPedestalOrb(int var1) {
        return this.crushedPedestalOrb;
    }

    public HonoredOneManager honoredOne(int var1) {
        return this.honoredManager;
    }

    public GemAbilityRules gemAbilityRules() {
        return this.gemAbilityRules;
    }

    private void onAssessmentWindowClosed() {
        boolean var58;
        if (this.assessmentDay != null && this.assessmentDay.isLastDay(1298882900)) {
            var58 = true;
        } else {
            var58 = false;
        }

        float var57;
        if (var58) {
            var57 = 0.5F;
        } else {
            var57 = 1.0F;
        }

        float var6 = var57;
        float var59;
        if (var58) {
            var59 = 4.0F;
        } else {
            var59 = 1.5F;
        }

        float var7 = var59;

        for (Player var79 : Bukkit.getOnlinePlayers()) {
            Location var66 = var79.getLocation();
            Sound var70 = Sound.BLOCK_BELL_USE;
            var79.playSound(var66, var70, var7, var6);
        }

        if (this.assessmentDay != null && this.assessmentDay.isLastDay(1298882900)) {
            if (this.controlRoom != null && this.honoredManager != null) {
                UUID var78 = this.honoredManager.current(1257858083);
                if (var78 != null) {
                    String var34 = "The Honored One Has Been Chosen";
                    int[] var60 = new int[]{16771504, 16174706};
                    Component var35 = Text.gradient(var34, var60, 2056282608);
                    TextDecoration var61 = TextDecoration.BOLD;
                    Bukkit.broadcast(var35.decoration(var61, true));
                    Player var9 = Bukkit.getPlayer(var78);
                    if (var9 == null || !var9.isOnline()) {
                        ControlRoomManager var52 = this.controlRoom;
                        var52.setPending(var78, 662296968);
                    } else if (!this.controlRoom.isSet(413123864)) {
                        ControlRoomManager var47 = this.controlRoom;
                        var47.setPending(var78, 662296968);
                    } else {
                        BukkitScheduler var49 = Bukkit.getScheduler();
                        Runnable var69 = () -> {
                            Player var4 = Bukkit.getPlayer(var78);
                            if (var4 != null && var4.isOnline() && this.controlRoom.isSet(413123864)) {
                                this.runAscension(var4, 620037489);
                            } else if (this.controlRoom != null) {
                                ControlRoomManager var15 = this.controlRoom;
                                var15.setPending(var78, 662296968);
                            }
                        };
                        var49.runTaskLater(this, var69, 140L);
                    }
                }
            }
        }
    }

    private void runAscension(Player var1, int var2) {
        Location var6 = this.controlRoom.location(1988022717);
        if (var6 == null) {
            ControlRoomManager var25 = this.controlRoom;
            UUID var32 = var1.getUniqueId();
            var25.setPending(var32, 662296968);
        } else {
            if (this.honoredRitual != null && !this.honoredRitual.isRunning(596214312)) {
                HonoredSummonRitual var16 = this.honoredRitual;
                var16.beginAscension(var1, var6, 1769073525);
            } else if (!var1.teleport(var6)) {
                ControlRoomManager var20 = this.controlRoom;
                UUID var28 = var1.getUniqueId();
                var20.setPending(var28, 662296968);
            } else {
                this.controlRoom.beginSession(1185796157);
                UUID var30 = var1.getUniqueId();
                this.gatherControlRoomAudience(var30, 658540308);
            }
        }
    }

    public boolean audienceGathered(int var1) {
        return this.audienceGathered;
    }

    public void spendAudienceLatch(int var1) {
        this.audienceGathered = false;
        this.saveAudience(931327898);
    }

    private File audienceFile(int var1) {
        File var5 = this.getDataFolder();
        String var4 = "audience.yml";
        return new File(var5, var4);
    }

    private void saveAudience(int var1) {
        YamlConfiguration var2 = new YamlConfiguration();
        YamlConfiguration var4 = var2;
        String var22 = "gathered";
        Boolean var33 = this.audienceGathered;
        var2.set(var22, var33);

        for (Entry var6 : this.audienceModes.entrySet()) {
            String var31 = "modes." + var6.getKey();
            String var37 = ((GameMode)var6.getValue()).name();
            var4.set(var31, var37);
        }

        try {
            File var24 = this.audienceFile(2085492915);
            AtomicYml.save(var4, var24, 1347473142);
        } catch (Exception var39) {
            Logger var17 = this.getLogger();
            String var27 = "Failed to save audience.yml: " + var39.getMessage();
            var17.warning(var27);
        }
    }

    private void loadAudience(int var1) {
        File var6 = this.audienceFile(2085492915);
        if (var6.exists()) {
            YamlConfiguration var7 = YamlConfiguration.loadConfiguration(var6);
            this.audienceModes.clear();
            ConfigurationSection var8 = var7.getConfigurationSection("modes");
            if (var8 != null) {
                for (String var10 : var8.getKeys(false)) {
                    try {
                        Map var31 = this.audienceModes;
                        UUID var43 = UUID.fromString(var10);
                        GameMode var49 = GameMode.valueOf(var8.getString(var10));
                        var31.put(var43, var49);
                    } catch (RuntimeException var53) {
                    }
                }
            }

            boolean var39;
            if (this.controlRoom != null && this.controlRoom.inSession(1710238328)) {
                var39 = true;
            } else {
                var39 = false;
            }

            boolean var47;
            label36: {
                if (var39) {
                    String var50 = "gathered";
                    if (var7.getBoolean(var50, false)) {
                        var47 = true;
                        break label36;
                    }
                }

                var47 = false;
            }

            this.audienceGathered = var47;
        }
    }

    public void tryRestoreAudienceMode(Player var1, int var2) {
        if (this.controlRoom == null || !this.controlRoom.inSession(1710238328)) {
            Map var11 = this.audienceModes;
            UUID var20 = var1.getUniqueId();
            GameMode var5 = (GameMode)var11.remove(var20);
            if (var5 != null) {
                GameMode var21 = GameMode.SPECTATOR;
                if (var5 != var21) {
                    GameMode var17 = var1.getGameMode();
                    if (var17 != var5) {
                        var1.setGameMode(var5);
                    }
                }

                this.saveAudience(931327898);
            }
        }
    }

    public void gatherControlRoomAudience(UUID var1, int var2) {
        if (this.controlRoom != null) {
            this.audienceGathered = false;
            if (this.globalState != null && !this.globalState.controlRoomAudience(1678943978)) {
                this.saveAudience(931327898);
            } else {
                this.audienceGathered = true;
                Location var6 = this.controlRoom.location(1988022717);
                if (var6 != null) {
                    int var7 = 0;
                    ArrayList var25 = new ArrayList();
                    ArrayList var8 = var25;
                    Collection<? extends Player> var5 = Bukkit.getOnlinePlayers();

                    for (Player var10 : new ArrayList<>(var5)) {
                        if (var1 != null) {
                            UUID var42 = var10.getUniqueId();
                            if (var42.equals(var1)) {
                                continue;
                            }
                        }

                        GameMode var45 = var10.getGameMode();
                        GameMode var61 = GameMode.SPECTATOR;
                        if (var45 != var61) {
                            Map var47 = this.audienceModes;
                            UUID var63 = var10.getUniqueId();
                            GameMode var70 = var10.getGameMode();
                            var47.put(var63, var70);
                        }

                        GameMode var64 = GameMode.SPECTATOR;
                        var10.setGameMode(var64);
                        if (var10.teleport(var6)) {
                            var7++;
                        } else {
                            String var67 = var10.getName();
                            var8.add(var67);
                        }
                    }

                    this.saveAudience(931327898);
                    Logger var32 = this.getLogger();
                    String var56 = "Control Room: gathered " + var7 + " player(s) as spectators for the decree.";
                    var32.info(var56);
                    if (!var8.isEmpty()) {
                        Logger var36 = this.getLogger();
                        String var57 = ", ";
                        String var59 = "Control Room: could NOT bring in "
                            + String.join(var57, var8)
                            + " — they are spectators where they stood; /tp them in if you want them at the decree.";
                        var36.warning(var59);
                    }
                }
            }
        }
    }

    public GameMode audiencePeekMode(UUID var1, int var2) {
        Map var5 = this.audienceModes;
        return (GameMode)var5.get(var1);
    }

    public void audienceModeRestored(UUID var1, int var2) {
        Map var5 = this.audienceModes;
        if (var5.remove(var1) != null) {
            this.saveAudience(931327898);
        }
    }

    public void scatterAudienceAroundPedestal(UUID var1, Location var2, int var3) {
        if (var2 != null) {
            if (this.audienceGathered) {
                World var13 = var2.getWorld();
                if (var13 != null) {
                    ThreadLocalRandom var14 = ThreadLocalRandom.current();
                    int var15 = 0;
                    ArrayList var36 = new ArrayList();
                    ArrayList var16 = var36;
                    Collection<? extends Player> var5 = Bukkit.getOnlinePlayers();

                    for (Player var18 : new ArrayList<>(var5)) {
                        if (var1 != null) {
                            UUID var46 = var18.getUniqueId();
                            if (var46.equals(var1)) {
                                continue;
                            }
                        }

                        double var19 = var14.nextDouble() * Math.PI * 2.0;
                        double var110 = Math.sqrt(var14.nextDouble());
                        double var21 = 15.0 * var110;
                        double var61 = Math.cos(var19);
                        double var23 = var61 * var21;
                        double var67 = Math.sin(var19);
                        double var25 = var67 * var21;
                        double var118 = -var25;
                        float var27 = (float)Math.toDegrees(Math.atan2(var23, var118));
                        double var122 = var2.getX();
                        double var124 = var122 + var23;
                        double var127 = var2.getY();
                        double var129 = var2.getZ();
                        double var131 = var129 + var25;
                        Location var78 = new Location(var13, var124, var127, var131, var27, 0.0F);
                        if (var18.teleport(var78)) {
                            var15++;
                            if (this.crushedPedestalOrb != null) {
                                CrushedPedestalOrb var86 = this.crushedPedestalOrb;
                                var86.ignoreUntilClear(var18, 1069596687);
                            }
                        } else {
                            String var96 = var18.getName();
                            var16.add(var96);
                        }
                    }

                    Logger var88 = this.getLogger();
                    String var99 = "Pedestal: set down " + var15 + " player(s) around the Honored One's return.";
                    var88.info(var99);
                    if (!var16.isEmpty()) {
                        Logger var92 = this.getLogger();
                        String var100 = ", ";
                        String var102 = "Pedestal: could NOT bring down " + String.join(var100, var16) + ".";
                        var92.warning(var102);
                    }
                }
            }
        }
    }

    public void completeAssessmentAtPedestal(UUID var1, int var2) {
        if (this.kitRules != null) {
            this.kitRules.applyDecree(false, 1681962085);
        }

        if (this.honoredManager != null && var1 != null) {
            HonoredOneManager var15 = this.honoredManager;
            if (var15.isHonoredOne(var1, 1908342269)) {
                this.honoredManager.clearHonoredOne(1926729969);
                Bukkit.broadcast(Text.legacy("&6✦ &eThe Honored One has returned to the pedestal — the &l&6Assessment&e has ended.", 373192400));
            }
        }

        if (this.controlRoom != null) {
            this.controlRoom.endSession(1338322625);
        }

        BukkitScheduler var28 = this.getServer().getScheduler();
        Runnable var32 = () -> {
            if (this.globalState != null && this.globalState.eventProgression(169255316)) {
                BlissCommand.applyProgression5(this, true, 1214874032);
            }
        };
        var28.runTaskLater(this, var32, 20L);
    }

    public Location pedestalRitualCentre(int var1) {
        PedestalState var9 = this.pedestalState(590646643);
        Location var21;
        if (var9 == null) {
            var21 = null;
        } else {
            var21 = var9.pedestalLoc1(1941968134);
        }

        return var21 != null && var21.getWorld() != null ? var21.clone().add(0.5, 0.05, 0.5) : null;
    }

    public HonoredSummonRitual honoredRitual(int var1) {
        return this.honoredRitual;
    }

    public void tryPendingAscension(Player var1, int var2) {
        if (this.controlRoom != null) {
            UUID var7 = this.controlRoom.pending(558468143);
            if (var7 != null) {
                UUID var27 = var1.getUniqueId();
                if (var7.equals(var27)) {
                    if (this.honoredManager != null) {
                        HonoredOneManager var18 = this.honoredManager;
                        UUID var29 = var1.getUniqueId();
                        if (var18.isHonoredOne(var29, 1908342269)) {
                            if (!this.controlRoom.isSet(413123864)) {
                                return;
                            }

                            BukkitScheduler var25 = Bukkit.getScheduler();
                            Runnable var31 = () -> {
                                UUID var4 = this.controlRoom.pending(558468143);
                                if (var4 != null && var4.equals(var7)) {
                                    Player var5 = Bukkit.getPlayer(var7);
                                    if (var5 != null && var5.isOnline()) {
                                        HonoredOneManager var17 = this.honoredManager;
                                        if (var17.isHonoredOne(var7, 1908342269)) {
                                            this.controlRoom.clearPending(21664137);
                                            this.runAscension(var5, 620037489);
                                            return;
                                        }
                                    }

                                    HonoredOneManager var23 = this.honoredManager;
                                    if (!var23.isHonoredOne(var7, 1908342269)) {
                                        this.controlRoom.clearPending(21664137);
                                    }
                                }
                            };
                            var25.runTaskLater(this, var31, 60L);
                            return;
                        }
                    }

                    this.controlRoom.clearPending(21664137);
                    return;
                }
            }
        }
    }

    static {
        String[] var3 = new String[2];
        String var2 = "gems";
        var3[0] = var2;
        String var5 = "mythic-gems";
        var3[1] = var5;
        TUNING_ROOTS = var3;
    }
}
