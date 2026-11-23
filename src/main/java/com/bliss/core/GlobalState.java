package com.bliss.core;

import com.bliss.BlissPlugin;
import com.bliss.gems.ConsistentDamage;
import com.bliss.goldgem.GoldGem;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class GlobalState {
    private final BlissPlugin plugin;
    private final File file;
    private boolean prog1 = true;
    private boolean prog1Started = true;
    private boolean prog2 = false;
    private boolean prog3 = false;
    private boolean prog4 = false;
    private boolean villagers = false;
    private boolean villagersLocked = false;
    private boolean crushedOrb = true;
    private boolean trueDamage = true;
    private Particles.Level particles;
    private boolean enderpearls;
    private boolean memoryProximitySound;
    private boolean traderRecipe;
    private boolean upgraderRecipe;
    private boolean netheriteAllowed;
    private boolean soulMessages;
    private boolean brokenDamage;
    private boolean energyRestrictions;
    private boolean noEnergyMode;
    private boolean clueAudio;
    private boolean lostSoulsAudio;
    private boolean spawnGemDisable;
    private boolean gemAutoEnchantAllStages;
    private boolean mythicGemStripsMain;
    private boolean tier2Glow;
    private boolean playerHeadDrops;
    private boolean keepTier2OnDeath;
    private boolean tier2OnJoin;
    private boolean trophyThunder;
    private boolean trophyThunderAllStages;
    private boolean prog0Gems;
    private boolean bundleGemProtection;
    private boolean sneakCast;
    private boolean legacySpeed;
    private boolean legacyPuff;
    private boolean allowOvercharge;
    private boolean wattDeposit;
    private boolean progressionGems;
    private boolean bottledEnergyExpiry;
    private boolean enchObsidianGlow;
    private boolean mythicDragonEgg;
    private boolean progArmorLimit;
    private boolean progKitLimit;
    private boolean kitEnchantLimits;
    private boolean progEnchantLimit;
    private boolean trackingCompasses;
    private boolean raidAdvancesProg4;
    private boolean eventProgression;
    private boolean bossProgression;
    private boolean restorationCoreRecipe;
    private boolean strengthProg1FullPassive;
    private boolean strengthTwoPassive;
    private boolean witherDropsDone;
    private boolean goldLegacySpeed;
    private boolean dragonGamesHolderStart;
    private boolean mythicGems;
    private boolean juggernautRole;
    private boolean controlRoomAudience;
    private boolean shaderEffects;
    private boolean finaleKnockback;
    private boolean goldGemSoulbound;
    private int progression;
    private UUID dragonEggOwner;
    private final boolean dream;
    private static GlobalState serverState;
    private static GlobalState dreamState;
    private static Predicate<World> dreamWorlds;
    private static volatile boolean mythicDragonEggFlag = false;

    public GlobalState(BlissPlugin var1, int var2) {
        this(var1, "state.yml", false, 1828049222);
    }

    public GlobalState(BlissPlugin var1, String var2, boolean var3, int var4) {
        Particles.Level var82 = Particles.Level.ON;
        this.particles = var82;
        this.enderpearls = false;
        this.memoryProximitySound = true;
        this.traderRecipe = false;
        this.upgraderRecipe = false;
        this.netheriteAllowed = false;
        this.soulMessages = true;
        this.brokenDamage = true;
        this.energyRestrictions = true;
        this.noEnergyMode = false;
        this.clueAudio = true;
        this.lostSoulsAudio = true;
        this.spawnGemDisable = false;
        this.gemAutoEnchantAllStages = false;
        this.mythicGemStripsMain = false;
        this.tier2Glow = true;
        this.playerHeadDrops = false;
        this.keepTier2OnDeath = false;
        this.tier2OnJoin = false;
        this.trophyThunder = true;
        this.trophyThunderAllStages = false;
        this.prog0Gems = false;
        this.bundleGemProtection = true;
        this.sneakCast = false;
        this.legacySpeed = false;
        this.legacyPuff = false;
        this.allowOvercharge = true;
        this.wattDeposit = true;
        this.progressionGems = false;
        this.bottledEnergyExpiry = true;
        this.enchObsidianGlow = true;
        this.mythicDragonEgg = false;
        this.progArmorLimit = true;
        this.progKitLimit = true;
        this.kitEnchantLimits = true;
        this.progEnchantLimit = true;
        this.trackingCompasses = true;
        this.raidAdvancesProg4 = true;
        this.eventProgression = true;
        this.bossProgression = true;
        this.restorationCoreRecipe = true;
        this.strengthProg1FullPassive = false;
        this.strengthTwoPassive = false;
        this.witherDropsDone = false;
        this.goldLegacySpeed = false;
        this.dragonGamesHolderStart = true;
        this.mythicGems = true;
        this.juggernautRole = true;
        this.controlRoomAudience = true;
        this.shaderEffects = true;
        this.finaleKnockback = true;
        this.goldGemSoulbound = false;
        this.progression = 0;
        this.plugin = var1;
        File var138 = var1.getDataFolder();
        File var136 = new File(var138, var2);
        this.file = var136;
        this.dream = var3;
    }

    public boolean isDream(int var1) {
        return this.dream;
    }

    public boolean hasFile(int var1) {
        return this.file.exists();
    }

    public void copyGameplayFrom(GlobalState var1, int var2) {
        boolean var39 = var1.prog1Started(1011566878);
        this.setProg1Started(var39, 1672226358);
        boolean var41 = var1.prog1(494064183);
        this.setProg1(var41, 1805549590);
        boolean var43 = var1.prog2(1337683067);
        this.setProg2(var43, 1105469454);
        boolean var45 = var1.prog3(1589905580);
        this.setProg3(var45, 1348581053);
        boolean var47 = var1.prog4(286518450);
        this.setProg4(var47, 946676425);
        int var49 = var1.progression(790021995);
        this.setProgression(var49, 1556136516);
        boolean var51 = var1.energyRestrictions(860109657);
        this.setEnergyRestrictions(var51, 50405250);
        boolean var53 = var1.noEnergyMode(340448998);
        this.setNoEnergyMode(var53, 1881985181);
        boolean var55 = var1.brokenDamage(1031219045);
        this.setBrokenDamage(var55, 106720013);
        boolean var57 = var1.spawnGemDisable(898885534);
        this.setSpawnGemDisable(var57, 1350062420);
        boolean var59 = var1.bundleGemProtection(468873788);
        this.setBundleGemProtection(var59, 1548605255);
        boolean var61 = var1.sneakCast(704684993);
        this.setSneakCast(var61, 439536169);
        boolean var63 = var1.allowOvercharge(201427360);
        this.setAllowOvercharge(var63, 1722320299);
        boolean var65 = var1.wattDeposit(552206927);
        this.setWattDeposit(var65, 7904853);
        boolean var67 = var1.strengthTwoPassive(1352452848);
        this.setStrengthTwoPassive(var67, 1781545790);
        boolean var69 = var1.legacySpeedRaw(445408285);
        this.setLegacySpeed(var69, 1201045649);
        boolean var71 = var1.legacyPuffRaw(1871243026);
        this.setLegacyPuff(var71, 1817028959);
        boolean var73 = var1.goldLegacySpeed(1059261642);
        this.setGoldLegacySpeed(var73, 669406894);
        boolean var75 = var1.progressionGems(853797119);
        this.setProgressionGems(var75, 1171497813);
        boolean var77 = var1.mythicGemStripsMain(635850224);
        this.setMythicGemStripsMain(var77, 1467653463);
        boolean var79 = var1.strengthProg1FullPassive(2079279880);
        this.setStrengthProg1FullPassive(var79, 2104191768);
        boolean var81 = var1.tier2Glow(1825034216);
        this.setTier2Glow(var81, 1561526866);
        boolean var83 = var1.playerHeadDrops(666090489);
        this.setPlayerHeadDrops(var83, 1774439989);
        boolean var85 = var1.keepTier2OnDeath(922876535);
        this.setKeepTier2OnDeath(var85, 1609445366);
        boolean var87 = var1.tier2OnJoin(2023108022);
        this.setTier2OnJoin(var87, 191934853);
        boolean var89 = var1.prog0Gems(1794280694);
        this.setProg0Gems(var89, 1984571060);
        boolean var91 = var1.trophyThunder(833687581);
        this.setTrophyThunder(var91, 573547549);
        boolean var93 = var1.trophyThunderAllStages(250785423);
        this.setTrophyThunderAllStages(var93, 1599776896);
        boolean var95 = var1.gemAutoEnchantAllStages(182748902);
        this.setGemAutoEnchantAllStages(var95, 1487150081);
        boolean var97 = var1.netheriteAllowed(613865620);
        this.setNetheriteAllowed(var97, 891609125);
        boolean var99 = var1.enderpearls(155177658);
        this.setEnderpearls(var99, 567146737);
        boolean var101 = var1.progArmorLimit(1353714115);
        this.setProgArmorLimit(var101, 2016363559);
        boolean var103 = var1.progKitLimit(1635426977);
        this.setProgKitLimit(var103, 68195459);
        boolean var105 = var1.progEnchantLimit(1344216646);
        this.setProgEnchantLimit(var105, 999545990);
        boolean var107 = var1.memoryProximitySound(604772563);
        this.setMemoryProximitySound(var107, 1594822047);
    }

    public static void install(GlobalState var0, GlobalState var1, Predicate<World> var2, int var3) {
        serverState = var0;
        dreamState = var1;
        dreamWorlds = var2;
    }

    public static GlobalState forWorld(World var0, int var1) {
        if (dreamState != null && var0 != null && dreamWorlds != null) {
            Predicate var6 = dreamWorlds;
            if (var6.test(var0)) {
                return dreamState;
            }
        }

        return serverState;
    }

    public static GlobalState forPlayer(Player var0, int var1) {
        GlobalState var5;
        if (var0 == null) {
            var5 = serverState;
        } else {
            var5 = forWorld(var0.getWorld(), 728926677);
        }

        return var5;
    }

    public static GlobalState forEntity(Entity var0, int var1) {
        GlobalState var3;
        if (var0 == null) {
            var3 = serverState;
        } else {
            var3 = forWorld(var0.getWorld(), 728926677);
        }

        return var3;
    }

    public static boolean isDreamWorld(World var0, int var1) {
        if (var0 != null && dreamWorlds != null) {
            Predicate var5 = dreamWorlds;
            if (var5.test(var0)) {
                return true;
            }
        }

        return false;
    }

    public boolean prog1(int var1) {
        return this.prog1;
    }

    public void setProg1(boolean var1, int var2) {
        this.prog1 = var1;
    }

    public boolean prog1Started(int var1) {
        return this.prog1Started;
    }

    public void setProg1Started(boolean var1, int var2) {
        this.prog1Started = var1;
    }

    public boolean prog2(int var1) {
        return this.prog2;
    }

    public void setProg2(boolean var1, int var2) {
        this.prog2 = var1;
    }

    public boolean prog3(int var1) {
        return this.prog3;
    }

    public void setProg3(boolean var1, int var2) {
        this.prog3 = var1;
    }

    public boolean prog4(int var1) {
        return this.prog4;
    }

    public void setProg4(boolean var1, int var2) {
        this.prog4 = var1;
    }

    public boolean villagers(int var1) {
        return this.villagers;
    }

    public void setVillagers(boolean var1, int var2) {
        this.villagers = var1;
    }

    public void setVillagersAuto(boolean var1, int var2) {
        if (!this.villagersLocked) {
            this.villagers = var1;
        }
    }

    public boolean villagersLocked() {
        return this.villagersLocked;
    }

    public void setVillagersLocked(boolean var1, int var2) {
        this.villagersLocked = var1;
    }

    public boolean crushedOrb(int var1) {
        return this.crushedOrb;
    }

    public void setCrushedOrb(boolean var1, int var2) {
        this.crushedOrb = var1;
    }

    public boolean trueDamage(int var1) {
        return this.trueDamage;
    }

    public void setTrueDamage(boolean var1, int var2) {
        this.trueDamage = var1;
        if (!this.dream) {
            ConsistentDamage.setTrueDamage(var1, 1147290473);
        }
    }

    public Particles.Level particles(int var1) {
        return this.particles;
    }

    public void setParticles(Particles.Level var1, int var2) {
        Particles.Level var9;
        if (var1 == null) {
            var9 = Particles.Level.ON;
        } else {
            var9 = var1;
        }

        this.particles = var9;
        if (!this.dream) {
            Particles.setLevel(this.particles, 1754858271);
        }
    }

    public boolean enderpearls(int var1) {
        return this.enderpearls;
    }

    public void setEnderpearls(boolean var1, int var2) {
        this.enderpearls = var1;
    }

    public boolean memoryProximitySound(int var1) {
        return this.memoryProximitySound;
    }

    public void setMemoryProximitySound(boolean var1, int var2) {
        this.memoryProximitySound = var1;
    }

    public boolean traderRecipe(int var1) {
        return this.traderRecipe;
    }

    public void setTraderRecipe(boolean var1, int var2) {
        this.traderRecipe = var1;
    }

    public boolean upgraderRecipe(int var1) {
        return this.upgraderRecipe;
    }

    public void setUpgraderRecipe(boolean var1, int var2) {
        this.upgraderRecipe = var1;
    }

    public boolean netheriteAllowed(int var1) {
        return this.netheriteAllowed;
    }

    public void setNetheriteAllowed(boolean var1, int var2) {
        this.netheriteAllowed = var1;
    }

    public boolean soulMessages() {
        return this.soulMessages;
    }

    public void setSoulMessages(boolean var1) {
        this.soulMessages = var1;
    }

    public boolean brokenDamage(int var1) {
        return this.brokenDamage;
    }

    public void setBrokenDamage(boolean var1, int var2) {
        this.brokenDamage = var1;
    }

    public boolean clueAudio(int var1) {
        return this.clueAudio;
    }

    public void setClueAudio(boolean var1, int var2) {
        this.clueAudio = var1;
    }

    public boolean lostSoulsAudio(int var1) {
        return this.lostSoulsAudio;
    }

    public void setLostSoulsAudio(boolean var1, int var2) {
        this.lostSoulsAudio = var1;
    }

    public boolean spawnGemDisable(int var1) {
        return this.spawnGemDisable;
    }

    public void setSpawnGemDisable(boolean var1, int var2) {
        this.spawnGemDisable = var1;
    }

    public boolean gemAutoEnchantAllStages(int var1) {
        return this.gemAutoEnchantAllStages;
    }

    public void setGemAutoEnchantAllStages(boolean var1, int var2) {
        this.gemAutoEnchantAllStages = var1;
    }

    public boolean mythicGemStripsMain(int var1) {
        return this.mythicGemStripsMain;
    }

    public void setMythicGemStripsMain(boolean var1, int var2) {
        this.mythicGemStripsMain = var1;
    }

    public boolean tier2Glow(int var1) {
        return this.tier2Glow;
    }

    public void setTier2Glow(boolean var1, int var2) {
        this.tier2Glow = var1;
    }

    public boolean playerHeadDrops(int var1) {
        return this.playerHeadDrops;
    }

    public void setPlayerHeadDrops(boolean var1, int var2) {
        this.playerHeadDrops = var1;
    }

    public boolean keepTier2OnDeath(int var1) {
        return this.keepTier2OnDeath;
    }

    public void setKeepTier2OnDeath(boolean var1, int var2) {
        this.keepTier2OnDeath = var1;
    }

    public boolean tier2OnJoin(int var1) {
        return this.tier2OnJoin;
    }

    public void setTier2OnJoin(boolean var1, int var2) {
        this.tier2OnJoin = var1;
    }

    public boolean trophyThunder(int var1) {
        return this.trophyThunder;
    }

    public void setTrophyThunder(boolean var1, int var2) {
        this.trophyThunder = var1;
    }

    public boolean trophyThunderAllStages(int var1) {
        return this.trophyThunderAllStages;
    }

    public void setTrophyThunderAllStages(boolean var1, int var2) {
        this.trophyThunderAllStages = var1;
    }

    public boolean prog0Gems(int var1) {
        return this.prog0Gems;
    }

    public void setProg0Gems(boolean var1, int var2) {
        this.prog0Gems = var1;
    }

    public boolean wattDeposit(int var1) {
        return this.wattDeposit;
    }

    public void setWattDeposit(boolean var1, int var2) {
        this.wattDeposit = var1;
    }

    public boolean bundleGemProtection(int var1) {
        return this.bundleGemProtection;
    }

    public void setBundleGemProtection(boolean var1, int var2) {
        this.bundleGemProtection = var1;
    }

    public boolean sneakCast(int var1) {
        return this.sneakCast;
    }

    public void setSneakCast(boolean var1, int var2) {
        this.sneakCast = var1;
    }

    private boolean progressionPicksGems(int var1) {
        boolean var6;
        if (this.progressionGems && this.stage(1483650410) > 0) {
            var6 = true;
        } else {
            var6 = false;
        }

        return var6;
    }

    public boolean legacySpeed(int var1) {
        boolean var7;
        if (this.progressionPicksGems(1283766126)) {
            if (this.stage(1483650410) <= 3) {
                var7 = true;
            } else {
                var7 = false;
            }
        } else {
            var7 = this.legacySpeed;
        }

        return var7;
    }

    public void setLegacySpeed(boolean var1, int var2) {
        this.legacySpeed = var1;
    }

    public boolean legacySpeedRaw(int var1) {
        return this.legacySpeed;
    }

    public boolean goldLegacySpeed(int var1) {
        return this.goldLegacySpeed;
    }

    public void setGoldLegacySpeed(boolean var1, int var2) {
        this.goldLegacySpeed = var1;
    }

    public boolean legacySpeedFor(Player var1, int var2) {
        if (this.goldLegacySpeed && var1 != null) {
            GemType var4 = GemType.SPEED;
            if (GoldGem.isWieldingSoul(var1, var4, 100010596)) {
                return true;
            }
        }

        return this.legacySpeed(2067387107);
    }

    public boolean legacyPuff(int var1) {
        boolean var6;
        if (this.progressionPicksGems(1283766126)) {
            if (this.stage(1483650410) <= 3) {
                var6 = true;
            } else {
                var6 = false;
            }
        } else {
            var6 = this.legacyPuff;
        }

        return var6;
    }

    public void setLegacyPuff(boolean var1, int var2) {
        this.legacyPuff = var1;
    }

    public boolean legacyPuffRaw(int var1) {
        return this.legacyPuff;
    }

    public boolean allowOvercharge(int var1) {
        return this.allowOvercharge;
    }

    public void setAllowOvercharge(boolean var1, int var2) {
        this.allowOvercharge = var1;
    }

    public boolean progressionGems(int var1) {
        return this.progressionGems;
    }

    public void setProgressionGems(boolean var1, int var2) {
        this.progressionGems = var1;
    }

    public boolean bottledEnergyExpiry(int var1) {
        return this.bottledEnergyExpiry;
    }

    public void setBottledEnergyExpiry(boolean var1, int var2) {
        this.bottledEnergyExpiry = var1;
    }

    public boolean enchObsidianGlow(int var1) {
        return this.enchObsidianGlow;
    }

    public void setEnchObsidianGlow(boolean var1, int var2) {
        this.enchObsidianGlow = var1;
    }

    public boolean mythicDragonEgg(int var1) {
        return this.mythicDragonEgg;
    }

    public void setMythicDragonEgg(boolean var1, int var2) {
        this.mythicDragonEgg = var1;
        if (!this.dream) {
            mythicDragonEggFlag = var1;
        }
    }

    public static boolean mythicDragonEggEnabled(int var0) {
        return mythicDragonEggFlag;
    }

    public boolean progArmorLimit(int var1) {
        return this.progArmorLimit;
    }

    public void setProgArmorLimit(boolean var1, int var2) {
        this.progArmorLimit = var1;
    }

    public boolean progKitLimit(int var1) {
        return this.progKitLimit;
    }

    public void setProgKitLimit(boolean var1, int var2) {
        this.progKitLimit = var1;
    }

    public boolean kitEnchantLimits(int var1) {
        return this.kitEnchantLimits;
    }

    public void setKitEnchantLimits(boolean var1, int var2) {
        this.kitEnchantLimits = var1;
    }

    public boolean progEnchantLimit(int var1) {
        return this.progEnchantLimit;
    }

    public void setProgEnchantLimit(boolean var1, int var2) {
        this.progEnchantLimit = var1;
    }

    public boolean trackingCompasses(int var1) {
        return this.trackingCompasses;
    }

    public void setTrackingCompasses(boolean var1, int var2) {
        this.trackingCompasses = var1;
    }

    public boolean raidAdvancesProg4(int var1) {
        return this.raidAdvancesProg4;
    }

    public void setRaidAdvancesProg4(boolean var1, int var2) {
        this.raidAdvancesProg4 = var1;
    }

    public boolean eventProgression(int var1) {
        return this.eventProgression;
    }

    public void setEventProgression(boolean var1, int var2) {
        this.eventProgression = var1;
    }

    public boolean bossProgression(int var1) {
        return this.bossProgression;
    }

    public void setBossProgression(boolean var1, int var2) {
        this.bossProgression = var1;
    }

    public boolean restorationCoreRecipe(int var1) {
        return this.restorationCoreRecipe;
    }

    public void setRestorationCoreRecipe(boolean var1, int var2) {
        this.restorationCoreRecipe = var1;
    }

    public boolean strengthProg1FullPassive(int var1) {
        return this.strengthProg1FullPassive;
    }

    public void setStrengthProg1FullPassive(boolean var1, int var2) {
        this.strengthProg1FullPassive = var1;
    }

    public boolean strengthTwoPassive(int var1) {
        return this.strengthTwoPassive;
    }

    public void setStrengthTwoPassive(boolean var1, int var2) {
        this.strengthTwoPassive = var1;
    }

    public boolean witherDropsDone(int var1) {
        return this.witherDropsDone;
    }

    public void setWitherDropsDone(boolean var1, int var2) {
        this.witherDropsDone = var1;
    }

    public boolean dragonGamesHolderStart(int var1) {
        return this.dragonGamesHolderStart;
    }

    public void setDragonGamesHolderStart(boolean var1, int var2) {
        this.dragonGamesHolderStart = var1;
    }

    public boolean mythicGems(int var1) {
        return this.mythicGems;
    }

    public void setMythicGems(boolean var1, int var2) {
        this.mythicGems = var1;
    }

    public boolean juggernautRole(int var1) {
        return this.juggernautRole;
    }

    public void setJuggernautRole(boolean var1, int var2) {
        this.juggernautRole = var1;
    }

    public boolean controlRoomAudience(int var1) {
        return this.controlRoomAudience;
    }

    public void setControlRoomAudience(boolean var1, int var2) {
        this.controlRoomAudience = var1;
    }

    public boolean shaderEffects(int var1) {
        return this.shaderEffects;
    }

    public void setShaderEffects(boolean var1, int var2) {
        this.shaderEffects = var1;
    }

    public boolean finaleKnockback(int var1) {
        return this.finaleKnockback;
    }

    public void setFinaleKnockback(boolean var1, int var2) {
        this.finaleKnockback = var1;
    }

    public boolean goldGemSoulbound(int var1) {
        return this.goldGemSoulbound;
    }

    public void setGoldGemSoulbound(boolean var1, int var2) {
        this.goldGemSoulbound = var1;
    }

    public boolean energyRestrictions(int var1) {
        return this.energyRestrictions;
    }

    public void setEnergyRestrictions(boolean var1, int var2) {
        this.energyRestrictions = var1;
        if (this.dream) {
            PlayerData.setDreamEnergyRestrictionsEnabled(var1, 1554166647);
        } else {
            PlayerData.setEnergyRestrictionsEnabled(var1, 321679586);
        }
    }

    public boolean noEnergyMode(int var1) {
        return this.noEnergyMode;
    }

    public void setNoEnergyMode(boolean var1, int var2) {
        this.noEnergyMode = var1;
        if (this.dream) {
            PlayerData.setDreamNoEnergyMode(var1, 1482638029);
        } else {
            PlayerData.setNoEnergyMode(var1, 1272059663);
        }
    }

    public int progression(int var1) {
        return this.progression;
    }

    public int stage(int var1) {
        if (this.progression > 0) {
            return this.progression;
        } else if (this.prog4) {
            return 4;
        } else if (this.prog3) {
            return 3;
        } else if (this.prog2) {
            return 2;
        } else {
            return this.prog1 && this.prog1Started ? 1 : 0;
        }
    }

    public void setProgression(int var1, int var2) {
        this.progression = var1;
    }

    public UUID dragonEggOwner(int var1) {
        return this.dragonEggOwner;
    }

    public void setDragonEggOwner(UUID var1, int var2) {
        this.dragonEggOwner = var1;
    }

    public boolean holdsDragonEgg(Player var1, int var2) {
        return isHoldingDragonEgg(var1, 2044938455);
    }

    public boolean holdsDragonEgg(UUID var1, int var2) {
        boolean var7;
        if (var1 != null && isHoldingDragonEgg(Bukkit.getPlayer(var1), 2044938455)) {
            var7 = true;
        } else {
            var7 = false;
        }

        return var7;
    }

    public static boolean isHoldingDragonEgg(Player var0, int var1) {
        if (var0 == null) {
            return false;
        } else {
            for (ItemStack var7 : var0.getInventory().getContents()) {
                if (var7 != null) {
                    Material var20 = var7.getType();
                    Material var24 = Material.DRAGON_EGG;
                    if (var20 == var24) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public void load(int var1) {
        if (this.file.exists()) {
            YamlConfiguration var6 = YamlConfiguration.loadConfiguration(this.file);
            String var4 = "prog1";
            boolean var103 = var6.getBoolean(var4, true);
            this.prog1 = var103;
            String var230 = "prog1Started";
            boolean var105 = var6.getBoolean(var230, true);
            this.prog1Started = var105;
            String var231 = "prog2";
            boolean var107 = var6.getBoolean(var231, false);
            this.prog2 = var107;
            String var232 = "prog3";
            boolean var109 = var6.getBoolean(var232, false);
            this.prog3 = var109;
            String var233 = "prog4";
            boolean var111 = var6.getBoolean(var233, false);
            this.prog4 = var111;
            String var234 = "villagers";
            boolean var113 = var6.getBoolean(var234, false);
            this.villagers = var113;
            String var235 = "villagersLocked";
            boolean var115 = var6.getBoolean(var235, false);
            this.villagersLocked = var115;
            String var236 = "crushedOrb";
            boolean var117 = var6.getBoolean(var236, true);
            this.crushedOrb = var117;
            String var237 = "trueDamage";
            boolean var119 = var6.getBoolean(var237, true);
            this.trueDamage = var119;
            String var120 = "particles";
            String var238 = "on";
            Particles.Level var7 = Particles.parse(var6.getString(var120, var238), 1207930812);
            Particles.Level var229;
            if (var7 == null) {
                var229 = Particles.Level.ON;
            } else {
                var229 = var7;
            }

            this.particles = var229;
            if (!this.dream) {
                Particles.setLevel(this.particles, 1754858271);
            }

            String var239 = "enderpearls";
            boolean var123 = var6.getBoolean(var239, false);
            this.enderpearls = var123;
            String var240 = "memoryProximitySound";
            boolean var125 = var6.getBoolean(var240, true);
            this.memoryProximitySound = var125;
            String var241 = "traderRecipe";
            boolean var127 = var6.getBoolean(var241, false);
            this.traderRecipe = var127;
            String var242 = "upgraderRecipe";
            boolean var129 = var6.getBoolean(var242, false);
            this.upgraderRecipe = var129;
            String var243 = "netheriteAllowed";
            boolean var131 = var6.getBoolean(var243, false);
            this.netheriteAllowed = var131;
            String var244 = "soulMessages";
            boolean var133 = var6.getBoolean(var244, true);
            this.soulMessages = var133;
            String var245 = "brokenDamage";
            boolean var135 = var6.getBoolean(var245, true);
            this.brokenDamage = var135;
            String var246 = "energyRestrictions";
            boolean var137 = var6.getBoolean(var246, true);
            this.energyRestrictions = var137;
            String var247 = "noEnergyMode";
            boolean var139 = var6.getBoolean(var247, false);
            this.noEnergyMode = var139;
            String var248 = "clueAudio";
            boolean var141 = var6.getBoolean(var248, true);
            this.clueAudio = var141;
            String var249 = "lostSoulsAudio";
            boolean var143 = var6.getBoolean(var249, true);
            this.lostSoulsAudio = var143;
            String var250 = "spawnGemDisable";
            boolean var145 = var6.getBoolean(var250, false);
            this.spawnGemDisable = var145;
            String var251 = "gemAutoEnchantAllStages";
            boolean var147 = var6.getBoolean(var251, false);
            this.gemAutoEnchantAllStages = var147;
            String var252 = "mythicGemStripsMain";
            boolean var149 = var6.getBoolean(var252, false);
            this.mythicGemStripsMain = var149;
            String var253 = "tier2Glow";
            boolean var151 = var6.getBoolean(var253, true);
            this.tier2Glow = var151;
            String var254 = "playerHeadDrops";
            boolean var153 = var6.getBoolean(var254, false);
            this.playerHeadDrops = var153;
            String var255 = "keepTier2OnDeath";
            boolean var155 = var6.getBoolean(var255, false);
            this.keepTier2OnDeath = var155;
            String var256 = "tier2OnJoin";
            boolean var157 = var6.getBoolean(var256, false);
            this.tier2OnJoin = var157;
            String var257 = "prog0Gems";
            boolean var159 = var6.getBoolean(var257, false);
            this.prog0Gems = var159;
            String var258 = "trophyThunder";
            boolean var161 = var6.getBoolean(var258, true);
            this.trophyThunder = var161;
            String var259 = "trophyThunderAllStages";
            boolean var163 = var6.getBoolean(var259, false);
            this.trophyThunderAllStages = var163;
            String var260 = "bundleGemProtection";
            boolean var165 = var6.getBoolean(var260, true);
            this.bundleGemProtection = var165;
            String var261 = "sneakCast";
            boolean var167 = var6.getBoolean(var261, false);
            this.sneakCast = var167;
            String var262 = "legacySpeed";
            boolean var169 = var6.getBoolean(var262, false);
            this.legacySpeed = var169;
            String var263 = "legacyPuff";
            boolean var171 = var6.getBoolean(var263, false);
            this.legacyPuff = var171;
            String var264 = "allowOvercharge";
            boolean var173 = var6.getBoolean(var264, true);
            this.allowOvercharge = var173;
            String var265 = "wattDeposit";
            boolean var175 = var6.getBoolean(var265, true);
            this.wattDeposit = var175;
            String var266 = "progressionGems";
            boolean var177 = var6.getBoolean(var266, false);
            this.progressionGems = var177;
            String var267 = "bottledEnergyExpiry";
            boolean var179 = var6.getBoolean(var267, true);
            this.bottledEnergyExpiry = var179;
            String var268 = "enchObsidianGlow";
            boolean var181 = var6.getBoolean(var268, true);
            this.enchObsidianGlow = var181;
            String var269 = "mythicDragonEgg";
            boolean var183 = var6.getBoolean(var269, false);
            this.mythicDragonEgg = var183;
            if (!this.dream) {
                mythicDragonEggFlag = this.mythicDragonEgg;
            }

            String var270 = "progArmorLimit";
            boolean var185 = var6.getBoolean(var270, true);
            this.progArmorLimit = var185;
            String var271 = "progKitLimit";
            boolean var187 = var6.getBoolean(var271, true);
            this.progKitLimit = var187;
            String var272 = "progEnchantLimit";
            boolean var189 = var6.getBoolean(var272, true);
            this.progEnchantLimit = var189;
            String var273 = "kitEnchantLimits";
            boolean var191 = var6.getBoolean(var273, true);
            this.kitEnchantLimits = var191;
            String var274 = "trackingCompasses";
            boolean var193 = var6.getBoolean(var274, true);
            this.trackingCompasses = var193;
            String var275 = "raidAdvancesProg4";
            boolean var195 = var6.getBoolean(var275, true);
            this.raidAdvancesProg4 = var195;
            String var276 = "eventProgression";
            boolean var197 = var6.getBoolean(var276, true);
            this.eventProgression = var197;
            String var277 = "bossProgression";
            boolean var199 = var6.getBoolean(var277, true);
            this.bossProgression = var199;
            String var278 = "restorationCoreRecipe";
            boolean var201 = var6.getBoolean(var278, true);
            this.restorationCoreRecipe = var201;
            String var279 = "strengthProg1FullPassive";
            boolean var203 = var6.getBoolean(var279, false);
            this.strengthProg1FullPassive = var203;
            String var280 = "strengthTwoPassive";
            boolean var205 = var6.getBoolean(var280, false);
            this.strengthTwoPassive = var205;
            String var281 = "witherDropsDone";
            boolean var207 = var6.getBoolean(var281, false);
            this.witherDropsDone = var207;
            String var282 = "goldLegacySpeed";
            boolean var209 = var6.getBoolean(var282, false);
            this.goldLegacySpeed = var209;
            String var283 = "dragonGamesHolderStart";
            boolean var211 = var6.getBoolean(var283, true);
            this.dragonGamesHolderStart = var211;
            String var284 = "mythicGems";
            boolean var213 = var6.getBoolean(var284, true);
            this.mythicGems = var213;
            String var285 = "juggernautRole";
            boolean var215 = var6.getBoolean(var285, true);
            this.juggernautRole = var215;
            String var286 = "controlRoomAudience";
            boolean var217 = var6.getBoolean(var286, true);
            this.controlRoomAudience = var217;
            String var287 = "shaderEffects";
            boolean var219 = var6.getBoolean(var287, true);
            this.shaderEffects = var219;
            String var288 = "finaleKnockback";
            boolean var221 = var6.getBoolean(var288, true);
            this.finaleKnockback = var221;
            String var289 = "goldGemSoulbound";
            boolean var223 = var6.getBoolean(var289, false);
            this.goldGemSoulbound = var223;
            String var290 = "progression";
            int var225 = var6.getInt(var290, 0);
            this.progression = var225;
            if (this.dream) {
                PlayerData.setDreamEnergyRestrictionsEnabled(this.energyRestrictions, 1554166647);
                PlayerData.setDreamNoEnergyMode(this.noEnergyMode, 1482638029);
            } else {
                PlayerData.setEnergyRestrictionsEnabled(this.energyRestrictions, 321679586);
                PlayerData.setNoEnergyMode(this.noEnergyMode, 1272059663);
            }

            String var8 = var6.getString("dragonEggOwner");
            if (var8 != null) {
                try {
                    UUID var228 = UUID.fromString(var8);
                    this.dragonEggOwner = var228;
                } catch (IllegalArgumentException var291) {
                }
            }
        }
    }

    // $VF: Handled exception range with multiple entry points by splitting it
    // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
    public void save(int var1) {
        YamlConfiguration var2 = new YamlConfiguration();
        YamlConfiguration var4 = var2;
        String var82 = "prog1";
        Boolean var152 = this.prog1;
        var2.set(var82, var152);
        String var83 = "prog1Started";
        Boolean var155 = this.prog1Started;
        var2.set(var83, var155);
        String var84 = "prog2";
        Boolean var158 = this.prog2;
        var2.set(var84, var158);
        String var85 = "prog3";
        Boolean var161 = this.prog3;
        var2.set(var85, var161);
        String var86 = "prog4";
        Boolean var164 = this.prog4;
        var2.set(var86, var164);
        String var87 = "villagers";
        Boolean var167 = this.villagers;
        var2.set(var87, var167);
        String var88 = "villagersLocked";
        Boolean var170 = this.villagersLocked;
        var2.set(var88, var170);
        String var89 = "crushedOrb";
        Boolean var173 = this.crushedOrb;
        var2.set(var89, var173);
        String var90 = "trueDamage";
        Boolean var176 = this.trueDamage;
        var2.set(var90, var176);
        String var91 = "particles";
        String var179 = this.particles.label();
        var2.set(var91, var179);
        String var92 = "enderpearls";
        Boolean var182 = this.enderpearls;
        var2.set(var92, var182);
        String var93 = "memoryProximitySound";
        Boolean var185 = this.memoryProximitySound;
        var2.set(var93, var185);
        String var94 = "traderRecipe";
        Boolean var188 = this.traderRecipe;
        var2.set(var94, var188);
        String var95 = "upgraderRecipe";
        Boolean var191 = this.upgraderRecipe;
        var2.set(var95, var191);
        String var96 = "netheriteAllowed";
        Boolean var194 = this.netheriteAllowed;
        var2.set(var96, var194);
        String var97 = "soulMessages";
        Boolean var197 = this.soulMessages;
        var2.set(var97, var197);
        String var98 = "brokenDamage";
        Boolean var200 = this.brokenDamage;
        var2.set(var98, var200);
        String var99 = "energyRestrictions";
        Boolean var203 = this.energyRestrictions;
        var2.set(var99, var203);
        String var100 = "noEnergyMode";
        Boolean var206 = this.noEnergyMode;
        var2.set(var100, var206);
        String var101 = "clueAudio";
        Boolean var209 = this.clueAudio;
        var2.set(var101, var209);
        String var102 = "lostSoulsAudio";
        Boolean var212 = this.lostSoulsAudio;
        var2.set(var102, var212);
        String var103 = "spawnGemDisable";
        Boolean var215 = this.spawnGemDisable;
        var2.set(var103, var215);
        String var104 = "gemAutoEnchantAllStages";
        Boolean var218 = this.gemAutoEnchantAllStages;
        var2.set(var104, var218);
        String var105 = "mythicGemStripsMain";
        Boolean var221 = this.mythicGemStripsMain;
        var2.set(var105, var221);
        String var106 = "tier2Glow";
        Boolean var224 = this.tier2Glow;
        var2.set(var106, var224);
        String var107 = "playerHeadDrops";
        Boolean var227 = this.playerHeadDrops;
        var2.set(var107, var227);
        String var108 = "keepTier2OnDeath";
        Boolean var230 = this.keepTier2OnDeath;
        var2.set(var108, var230);
        String var109 = "tier2OnJoin";
        Boolean var233 = this.tier2OnJoin;
        var2.set(var109, var233);
        String var110 = "prog0Gems";
        Boolean var236 = this.prog0Gems;
        var2.set(var110, var236);
        String var111 = "trophyThunder";
        Boolean var239 = this.trophyThunder;
        var2.set(var111, var239);
        String var112 = "trophyThunderAllStages";
        Boolean var242 = this.trophyThunderAllStages;
        var2.set(var112, var242);
        String var113 = "bundleGemProtection";
        Boolean var245 = this.bundleGemProtection;
        var2.set(var113, var245);
        String var114 = "sneakCast";
        Boolean var248 = this.sneakCast;
        var2.set(var114, var248);
        String var115 = "legacySpeed";
        Boolean var251 = this.legacySpeed;
        var2.set(var115, var251);
        String var116 = "legacyPuff";
        Boolean var254 = this.legacyPuff;
        var2.set(var116, var254);
        String var117 = "allowOvercharge";
        Boolean var257 = this.allowOvercharge;
        var2.set(var117, var257);
        String var118 = "wattDeposit";
        Boolean var260 = this.wattDeposit;
        var2.set(var118, var260);
        String var119 = "progressionGems";
        Boolean var263 = this.progressionGems;
        var2.set(var119, var263);
        String var120 = "bottledEnergyExpiry";
        Boolean var266 = this.bottledEnergyExpiry;
        var2.set(var120, var266);
        String var121 = "enchObsidianGlow";
        Boolean var269 = this.enchObsidianGlow;
        var2.set(var121, var269);
        String var122 = "mythicDragonEgg";
        Boolean var272 = this.mythicDragonEgg;
        var2.set(var122, var272);
        String var123 = "progArmorLimit";
        Boolean var275 = this.progArmorLimit;
        var2.set(var123, var275);
        String var124 = "progKitLimit";
        Boolean var278 = this.progKitLimit;
        var2.set(var124, var278);
        String var125 = "progEnchantLimit";
        Boolean var281 = this.progEnchantLimit;
        var2.set(var125, var281);
        String var126 = "kitEnchantLimits";
        Boolean var284 = this.kitEnchantLimits;
        var2.set(var126, var284);
        String var127 = "trackingCompasses";
        Boolean var287 = this.trackingCompasses;
        var2.set(var127, var287);
        String var128 = "raidAdvancesProg4";
        Boolean var290 = this.raidAdvancesProg4;
        var2.set(var128, var290);
        String var129 = "eventProgression";
        Boolean var293 = this.eventProgression;
        var2.set(var129, var293);
        String var130 = "bossProgression";
        Boolean var296 = this.bossProgression;
        var2.set(var130, var296);
        String var131 = "restorationCoreRecipe";
        Boolean var299 = this.restorationCoreRecipe;
        var2.set(var131, var299);
        String var132 = "strengthProg1FullPassive";
        Boolean var302 = this.strengthProg1FullPassive;
        var2.set(var132, var302);
        String var133 = "strengthTwoPassive";
        Boolean var305 = this.strengthTwoPassive;
        var2.set(var133, var305);
        String var134 = "witherDropsDone";
        Boolean var308 = this.witherDropsDone;
        var2.set(var134, var308);
        String var135 = "goldLegacySpeed";
        Boolean var311 = this.goldLegacySpeed;
        var2.set(var135, var311);
        String var136 = "dragonGamesHolderStart";
        Boolean var314 = this.dragonGamesHolderStart;
        var2.set(var136, var314);
        String var137 = "mythicGems";
        Boolean var317 = this.mythicGems;
        var2.set(var137, var317);
        String var138 = "juggernautRole";
        Boolean var320 = this.juggernautRole;
        var2.set(var138, var320);
        String var139 = "controlRoomAudience";
        Boolean var323 = this.controlRoomAudience;
        var2.set(var139, var323);
        String var140 = "shaderEffects";
        Boolean var326 = this.shaderEffects;
        var2.set(var140, var326);
        String var141 = "finaleKnockback";
        Boolean var329 = this.finaleKnockback;
        var2.set(var141, var329);
        String var142 = "goldGemSoulbound";
        Boolean var332 = this.goldGemSoulbound;
        var2.set(var142, var332);
        String var143 = "progression";
        Integer var335 = this.progression;
        var2.set(var143, var335);
        String var144 = "dragonEggOwner";
        String var342;
        if (this.dragonEggOwner == null) {
            var342 = null;
        } else {
            var342 = this.dragonEggOwner.toString();
        }

        var2.set(var144, var342);

        try {
            if (!this.file.getParentFile().exists()) {
                this.file.getParentFile().mkdirs();
            }
            File var146 = this.file;
            AtomicYml.save(var4, var146, 1347473142);
        } catch (IOException var5) {
            Logger var81 = this.plugin.getLogger();
            String var149 = this.file.getName();
            String var341 = var5.getMessage();
            String var150 = "Failed to save " + var149 + ": " + var341;
            var81.warning(var150);
        }
    }
}
