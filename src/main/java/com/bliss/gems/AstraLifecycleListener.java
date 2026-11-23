/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.destroystokyo.paper.event.player.PlayerStartSpectatingEntityEvent
 *  net.kyori.adventure.text.Component
 *  org.bukkit.Bukkit
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.entity.EntityPickupItemEvent
 *  org.bukkit.event.player.PlayerInteractEntityEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerToggleSneakEvent
 *  org.bukkit.inventory.EquipmentSlot
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitScheduler
 *  org.bukkit.scheduler.BukkitTask
 */
package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.EnergyScale;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.core.TagStore;
import com.bliss.core.TrustStore;
import com.bliss.gems.AstraGemListener;
import com.bliss.gems.AstraProjectionListener;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import com.destroystokyo.paper.event.player.PlayerStartSpectatingEntityEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class AstraLifecycleListener
implements Listener {
    private static final int HAUNT_SECONDS = 15;
    private static final double MAX_PROJECTION_DISTANCE_SQ = 22500.0;
    private static final long DISTANCE_CHECK_PERIOD = 20L;
    private final BlissPlugin plugin;
    private final AstraGemListener astra;
    private final AstraProjectionListener projection;
    private final TagStore tags;
    private final PlayerDataStore players;
    private final GlobalState globalState;
    private final TrustStore trust;
    private BukkitTask distanceTask;
    private static final int UNBOUNDED_HAUNT_SECONDS = 15;
    private static final long UNBOUNDED_TIMEOUT_TICKS = 200L;
    private final Map<UUID, Unbounded> unbounded;
    private final Set<UUID> cameraAttaching;
    private final File unboundedFile;
    private final Map<UUID, GameMode> pendingRideRecovery;

    public AstraLifecycleListener(BlissPlugin blissPlugin, AstraGemListener astraGemListener, AstraProjectionListener astraProjectionListener, TagStore tagStore, PlayerDataStore playerDataStore, GlobalState globalState, TrustStore trustStore, int n) {
        File file;
        TrustStore trustStore2;
        GlobalState globalState2;
        PlayerDataStore playerDataStore2;
        TagStore tagStore2;
        AstraProjectionListener astraProjectionListener2;
        AstraGemListener astraGemListener2;
        BlissPlugin blissPlugin2;
        this.unbounded = new ConcurrentHashMap<>();
        this.cameraAttaching = ConcurrentHashMap.newKeySet();
        this.pendingRideRecovery = new ConcurrentHashMap<>();
        this.plugin = blissPlugin;
        this.astra = astraGemListener;
        this.projection = astraProjectionListener;
        this.tags = tagStore;
        this.players = playerDataStore;
        this.globalState = globalState;
        this.trust = trustStore;
        this.unboundedFile = new File(blissPlugin.getDataFolder(), "unbounded_rides.yml");
        this.loadRides(1299946126);
    }

    public void start(int n) {
        BukkitTask bukkitTask;
        AstraLifecycleListener astraLifecycleListener = this;
        BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
        BlissPlugin blissPlugin = this.plugin;
        Runnable runnable = this::distanceCheck;
        astraLifecycleListener.distanceTask = bukkitTask = bukkitScheduler.runTaskTimer((Plugin)blissPlugin, runnable, 20L, 20L);
    }

    public void stop(int n) {
        if (this.distanceTask != null) {
            this.distanceTask.cancel();
        }
        Set<UUID> set = this.astra.projections.keySet();
        ArrayList<UUID> arrayList = new ArrayList<UUID>(set);
        Iterator<UUID> iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            Player player = Bukkit.getPlayer((UUID)iterator.next());
            if (player == null) continue;
            AstraProjectionListener astraProjectionListener = this.projection;
            Player player2 = player;
            astraProjectionListener.disableProjection(player2, 1333627516);
        }
        Set<UUID> set2 = this.unbounded.keySet();
        ArrayList<UUID> arrayList2 = new ArrayList<UUID>(set2);
        for (UUID uUID : arrayList2) {
            AstraLifecycleListener astraLifecycleListener = this;
            UUID uUID2 = uUID;
            astraLifecycleListener.restoreUnbounded(uUID2, 1078091064);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        AstraProjectionListener astraProjectionListener = this.projection;
        Player player2 = player;
        astraProjectionListener.disableProjection(player2, 1333627516);
        Iterator<Map.Entry<UUID, AstraGemListener.ProjectionState>> iterator = this.astra.projections.entrySet().iterator();
        while (iterator.hasNext()) {
            Entity entity;
            Player player3;
            Player player4 = Bukkit.getPlayer((UUID)iterator.next().getKey());
            if (player4 == null || !(player3 = player).equals((Object)(entity = player4.getSpectatorTarget()))) continue;
            AstraProjectionListener astraProjectionListener2 = this.projection;
            Player player5 = player4;
            astraProjectionListener2.disableProjection(player5, 1333627516);
        }
        AstraLifecycleListener astraLifecycleListener = this;
        UUID uUID = player.getUniqueId();
        astraLifecycleListener.restoreUnbounded(uUID, 1078091064);
        for (Map.Entry<UUID, Unbounded> entry : this.unbounded.entrySet()) {
            UUID uUID2 = entry.getValue().victim;
            UUID uUID3 = player.getUniqueId();
            if (!uUID2.equals(uUID3)) continue;
            AstraLifecycleListener astraLifecycleListener2 = this;
            UUID uUID4 = entry.getKey();
            astraLifecycleListener2.restoreUnbounded(uUID4, 1078091064);
        }
    }

    @EventHandler
    public void onProjectorTeleport(PlayerTeleportEvent playerTeleportEvent) {
        GameMode gameMode;
        Player player = playerTeleportEvent.getPlayer();
        GameMode gameMode2 = player.getGameMode();
        if (gameMode2 != (gameMode = GameMode.SPECTATOR)) {
            return;
        }
        Map<UUID, AstraGemListener.ProjectionState> map = this.astra.projections;
        UUID uUID = player.getUniqueId();
        UUID uUID2 = uUID;
        if (map.containsKey(uUID2)) {
            playerTeleportEvent.setCancelled(true);
            AstraLifecycleListener astraLifecycleListener = this;
            Player player2 = player;
            astraLifecycleListener.hauntNearbyAndEnd(player2, 915794039);
            return;
        }
        Map<UUID, Unbounded> map2 = this.unbounded;
        UUID uUID3 = uUID;
        if (map2.containsKey(uUID3)) {
            Set<UUID> set = this.cameraAttaching;
            UUID uUID4 = uUID;
            if (set.contains(uUID4)) {
                return;
            }
            playerTeleportEvent.setCancelled(true);
            BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
            BlissPlugin blissPlugin = this.plugin;
            AstraLifecycleListener astraLifecycleListener = this;
            UUID uUID5 = uUID;
            Runnable runnable = () -> this.restoreUnbounded(uUID5, 1078091064);
            bukkitScheduler.runTask((Plugin)blissPlugin, runnable);
        }
    }

    @EventHandler
    public void onProjectorSneak(PlayerToggleSneakEvent playerToggleSneakEvent) {
        if (!playerToggleSneakEvent.isSneaking()) {
            return;
        }
        Map<UUID, AstraGemListener.ProjectionState> map = this.astra.projections;
        Player player = playerToggleSneakEvent.getPlayer();
        UUID uUID = player.getUniqueId();
        if (!map.containsKey(uUID)) {
            return;
        }
        if (player.getSpectatorTarget() == null) {
            return;
        }
        AstraLifecycleListener astraLifecycleListener = this;
        Player player2 = player;
        astraLifecycleListener.hauntNearbyAndEnd(player2, 915794039);
    }

    private static int hauntSeconds(int n) {
        String string = "astra";
        String string2 = "haunt-tag";
        return (int)GemTuning.duration(string, string2, 15.0, 812730832);
    }

    private void hauntNearbyAndEnd(Player player, int n) {
        for (Player player2 : player.getWorld().getPlayers()) {
            UUID uUID;
            UUID uUID2;
            TrustStore trustStore;
            Location location;
            Location location2;
            Player player3;
            Player player4 = player2;
            if (player4.equals((Object)(player3 = player)) || (location2 = player2.getLocation()).distanceSquared(location = player.getLocation()) > 1.0 || (trustStore = this.trust).isTrusted(uUID2 = player.getUniqueId(), uUID = player2.getUniqueId(), 1736364606)) continue;
            TagStore tagStore = this.tags;
            UUID uUID3 = player2.getUniqueId();
            String string = "astra";
            String string2 = "haunt-tag";
            int n2 = (int)GemTuning.duration(string, string2, 15.0, 812730832);
            tagStore.addTagSeconds(uUID3, n2, 1101624808);
            Player player5 = player;
            String string3 = player2.getName();
            int n3 = AstraLifecycleListener.hauntSeconds(1247669959);
            Component component = Text.legacy("&5\ud83d\udd2e &bYou haunted and disabled \u00a7e" + string3 + " &bgem for \u00a7e" + n3 + "s", 373192400);
            player5.sendMessage(component);
        }
        AstraProjectionListener astraProjectionListener = this.projection;
        Player player6 = player;
        astraProjectionListener.disableProjection(player6, 1333627516);
    }

    private void distanceCheck() {
        double d;
        String string = "astra";
        String string2 = "projection-leash";
        double d2 = d = GemTuning.radius(string, string2, 50.0, 1058601812);
        double d3 = d;
        double d4 = d2 * d3;
        for (Map.Entry<UUID, AstraGemListener.ProjectionState> entry : this.astra.projections.entrySet()) {
            double d5;
            Location location;
            Location location2;
            double d6;
            World world;
            World world2;
            Entity entity;
            Player player = Bukkit.getPlayer((UUID)entry.getKey());
            if (player == null || (entity = Bukkit.getEntity((UUID)entry.getValue().zombieUid)) == null || !entity.isValid() || !(world2 = entity.getWorld()).equals((Object)(world = player.getWorld())) || !((d6 = (location2 = player.getLocation()).distanceSquared(location = entity.getLocation())) > (d5 = d4))) continue;
            AstraProjectionListener astraProjectionListener = this.projection;
            Player player2 = player;
            astraProjectionListener.disableProjection(player2, 1333627516);
            Player player3 = player;
            Location location3 = entity.getLocation();
            player3.teleport(location3);
        }
    }

    @EventHandler
    public void onGhostPickup(EntityPickupItemEvent entityPickupItemEvent) {
        LivingEntity livingEntity = entityPickupItemEvent.getEntity();
        if (livingEntity instanceof Player) {
            Map<UUID, AstraGemListener.ProjectionState> map = this.astra.projections;
            Player player = (Player)livingEntity;
            UUID uUID = player.getUniqueId();
            if (map.containsKey(uUID)) {
                entityPickupItemEvent.setCancelled(true);
            }
            return;
        }
    }

    @EventHandler
    public void onBodyDamage(EntityDamageEvent entityDamageEvent) {
        Map.Entry<UUID, AstraGemListener.ProjectionState> matched = null;
        UUID uUID = entityDamageEvent.getEntity().getUniqueId();
        for (Map.Entry<UUID, AstraGemListener.ProjectionState> candidate : this.astra.projections.entrySet()) {
            if (uUID.equals(candidate.getValue().zombieUid)) {
                matched = candidate;
                break;
            }
        }
        if (matched == null) {
            return;
        }
        entityDamageEvent.setCancelled(true);
        Player player = Bukkit.getPlayer(matched.getKey());
        if (player != null) {
            AstraProjectionListener astraProjectionListener = this.projection;
            Player player2 = player;
            astraProjectionListener.disableProjection(player2, 1333627516);
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onUnboundedHit(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        Entity entity = entityDamageByEntityEvent.getDamager();
        if (entity instanceof Player) {
            Player player = (Player)entity;
            Entity entity2 = entityDamageByEntityEvent.getEntity();
            if (entity2 instanceof Player) {
                long l;
                long l2;
                int n;
                UUID uUID;
                Map<UUID, Unbounded> map;
                EntityDamageEvent.DamageCause damageCause;
                EntityDamageEvent.DamageCause damageCause2;
                EntityDamageEvent.DamageCause damageCause3;
                Player player2 = player;
                Player player3 = (Player)entity2;
                Player player4 = player3;
                if (player2.equals((Object)player4)) {
                    return;
                }
                EntityDamageEvent.DamageCause damageCause4 = entityDamageByEntityEvent.getCause();
                if (damageCause4 == (damageCause3 = EntityDamageEvent.DamageCause.SONIC_BOOM) || (damageCause2 = entityDamageByEntityEvent.getCause()) == (damageCause = EntityDamageEvent.DamageCause.THORNS)) {
                    return;
                }
                Map<UUID, AstraGemListener.ProjectionState> map2 = this.astra.projections;
                UUID uUID2 = player.getUniqueId();
                UUID uUID3 = uUID2;
                if (map2.containsKey(uUID3) || (map = this.unbounded).containsKey(uUID = uUID2)) {
                    return;
                }
                if (player.isSneaking()) {
                    return;
                }
                TagStore tagStore = this.tags;
                UUID uUID4 = uUID2;
                if (tagStore.isTagged(uUID4, 995676415)) {
                    return;
                }
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                int n2 = BlissCoreItems.gemIdOf(itemStack, 382441079);
                if (n2 != (n = GemType.ASTRA.id()) || BlissCoreItems.tierOf(itemStack, 1633828214) != 2) {
                    return;
                }
                PlayerDataStore playerDataStore = this.players;
                UUID uUID5 = uUID2;
                PlayerData playerData = playerDataStore.get(uUID5, 1264010596);
                if (playerData.energy(1160853318) <= 1) {
                    return;
                }
                if (!playerData.canUsePowers(1616892701)) {
                    return;
                }
                Map<UUID, Long> map3 = this.astra.cd2;
                UUID uUID6 = uUID2;
                Long l3 = map3.get(uUID6);
                if (l3 != null && (l2 = l3.longValue()) > (l = System.currentTimeMillis())) {
                    return;
                }
                TrustStore trustStore = this.trust;
                UUID uUID7 = uUID2;
                UUID uUID8 = player3.getUniqueId();
                if (trustStore.isTrusted(uUID7, uUID8, 1736364606)) {
                    Player player5 = player;
                    Component component = Text.legacy("&5\ud83d\udd2e &fYou cannot cast negative powers on allies!", 373192400);
                    player5.sendMessage(component);
                    return;
                }
                Player player6 = player;
                Component component = Text.legacy("&5\ud83d\udd2e &bYou have used \u00a77Unbounded &bskill on &a" + player3.getName(), 373192400);
                player6.sendMessage(component);
                AstraLifecycleListener astraLifecycleListener = this;
                Player player7 = player;
                PlayerData playerData2 = playerData;
                astraLifecycleListener.applyUnboundedCooldown(player7, playerData2, 981892487);
                Map<UUID, Unbounded> map4 = this.unbounded;
                UUID uUID9 = uUID2;
                UUID uUID10 = player3.getUniqueId();
                GameMode gameMode = player.getGameMode();
                EntityDamageByEntityEvent entityDamageByEntityEvent2 = entityDamageByEntityEvent;
                Unbounded unbounded = new Unbounded(uUID10, gameMode, (EntityDamageEvent)entityDamageByEntityEvent2, 1612939859);
                map4.put(uUID9, unbounded);
                this.persistRides(835037645);
                UUID uUID11 = player3.getUniqueId();
                BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
                BlissPlugin blissPlugin = this.plugin;
                AstraLifecycleListener astraLifecycleListener2 = this;
                UUID uUID12 = uUID2;
                UUID uUID13 = uUID11;
                Runnable runnable = () -> {
                    Player pVictim = Bukkit.getPlayer(uUID12);
                    Player pTarget = Bukkit.getPlayer(uUID13);
                    if (pVictim == null) return;
                    if (!this.unbounded.containsKey(uUID12)) {
                        return;
                    }
                    pVictim.setGameMode(GameMode.SPECTATOR);
                    if (pTarget == null || !pTarget.isOnline()) return;
                    this.cameraAttaching.add(uUID12);
                    try {
                        pVictim.setSpectatorTarget(pTarget);
                    } finally {
                        this.cameraAttaching.remove(uUID12);
                    }
                };
                bukkitScheduler.runTask((Plugin)blissPlugin, runnable);
                BukkitScheduler bukkitScheduler2 = Bukkit.getScheduler();
                BlissPlugin blissPlugin2 = this.plugin;
                UUID uUID14 = uUID2;
                Runnable runnable2 = () -> this.restoreUnbounded(uUID14, 1078091064);
                String string = "astra";
                String string2 = "unbounded-ride";
                long l4 = (long)(GemTuning.duration(string, string2, 10.0, 812730832) * 20.0);
                bukkitScheduler2.runTaskLater((Plugin)blissPlugin2, runnable2, l4);
                return;
            }
            return;
        }
    }

    private void applyUnboundedCooldown(Player player, PlayerData playerData, int n) {
        long l;
        boolean bl = this.globalState.holdsDragonEgg(player, 715974514);
        int n2 = playerData.energy(1160853318);
        if (n2 > 5) {
            l = bl ? 45000L : 90000L;
        } else if (n2 == 5) {
            l = bl ? 50000L : 105000L;
        } else if (n2 == 4) {
            l = bl ? 60000L : 120000L;
        } else if (n2 == 3) {
            l = bl ? 75000L : 150000L;
        } else {
            l = bl ? 90000L : 180000L;
        }
        Map<UUID, Long> map = this.astra.cd2;
        UUID uUID = player.getUniqueId();
        long l7 = System.currentTimeMillis();
        String string = "astra";
        String string2 = "unbounded";
        long l8 = l;
        long l9 = GemTuning.cd(string, string2, 90.0, l8, 1621983041);
        int n3 = n2;
        long l10 = EnergyScale.cd(l9, n3, 907518618);
        Long l11 = l7 + l10;
        map.put(uUID, l11);
    }

    private void restoreUnbounded(UUID uUID, int n) {
        Map<UUID, Unbounded> map = this.unbounded;
        UUID uUID2 = uUID;
        Unbounded unbounded = map.remove(uUID2);
        if (unbounded == null) {
            return;
        }
        Player player = Bukkit.getPlayer((UUID)uUID);
        if (player != null) {
            GameMode gameMode;
            GameMode gameMode2 = player.getGameMode();
            if (gameMode2 == (gameMode = GameMode.SPECTATOR)) {
                player.setSpectatorTarget(null);
            }
            Player player2 = player;
            GameMode gameMode3 = unbounded.origMode;
            GameMode gameMode4 = GameMode.SPECTATOR;
            GameMode gameMode5 = gameMode3 == gameMode4 ? GameMode.SURVIVAL : unbounded.origMode;
            player2.setGameMode(gameMode5);
        }
        this.persistRides(835037645);
    }

    private void persistRides(int n) {
        YamlConfiguration yamlConfiguration;
        YamlConfiguration yamlConfiguration2 = yamlConfiguration = new YamlConfiguration();
        for (Map.Entry<UUID, Unbounded> entry : this.unbounded.entrySet()) {
            YamlConfiguration yamlConfiguration3 = yamlConfiguration2;
            String string = entry.getKey().toString();
            String string2 = entry.getValue().origMode.name();
            yamlConfiguration3.set(string, (Object)string2);
        }
        for (Map.Entry<UUID, GameMode> entry : this.pendingRideRecovery.entrySet()) {
            YamlConfiguration yamlConfiguration4 = yamlConfiguration2;
            String string = entry.getKey().toString();
            String string3 = entry.getValue().name();
            yamlConfiguration4.set(string, (Object)string3);
        }
        try {
            if (!this.unboundedFile.getParentFile().exists()) {
                this.unboundedFile.getParentFile().mkdirs();
            }
            YamlConfiguration yamlConfiguration5 = yamlConfiguration2;
            File file = this.unboundedFile;
            yamlConfiguration5.save(file);
        }
        catch (IOException iOException) {
            Logger logger = this.plugin.getLogger();
            String string = "Failed to save unbounded_rides.yml: " + iOException.getMessage();
            logger.warning(string);
        }
    }

    private void loadRides(int n) {
        if (!this.unboundedFile.exists()) {
            return;
        }
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)this.unboundedFile);
        for (String string : yamlConfiguration.getKeys(false)) {
            try {
                YamlConfiguration yamlConfiguration2 = yamlConfiguration;
                String string2 = string;
                String string3 = "SURVIVAL";
                GameMode gameMode = GameMode.valueOf((String)yamlConfiguration2.getString(string2, string3));
                Map<UUID, GameMode> map = this.pendingRideRecovery;
                UUID uUID = UUID.fromString(string);
                GameMode gameMode2 = gameMode;
                GameMode gameMode3 = GameMode.SPECTATOR;
                GameMode gameMode4 = gameMode2 == gameMode3 ? GameMode.SURVIVAL : gameMode;
                map.put(uUID, gameMode4);
            }
            catch (IllegalArgumentException illegalArgumentException) {}
        }
    }

    @EventHandler
    public void onJoinRideRecovery(PlayerJoinEvent playerJoinEvent) {
        GameMode gameMode;
        Map<UUID, GameMode> map = this.pendingRideRecovery;
        Player player = playerJoinEvent.getPlayer();
        UUID uUID = player.getUniqueId();
        GameMode gameMode2 = map.remove(uUID);
        if (gameMode2 == null) {
            return;
        }
        GameMode gameMode3 = player.getGameMode();
        if (gameMode3 == (gameMode = GameMode.SPECTATOR)) {
            player.setSpectatorTarget(null);
            Player player2 = player;
            GameMode gameMode4 = gameMode2;
            player2.setGameMode(gameMode4);
        }
        this.persistRides(835037645);
    }

    @EventHandler(ignoreCancelled=true)
    public void onProjectionPersonDamage(EntityDamageEvent entityDamageEvent) {
        Entity entity = entityDamageEvent.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }
        Player player = (Player)entity;
        UUID uUID = player.getUniqueId();
        for (Map.Entry<UUID, Unbounded> entry : this.unbounded.entrySet()) {
            EntityDamageEvent entityDamageEvent2;
            EntityDamageEvent entityDamageEvent3;
            UUID uUID2 = entry.getValue().victim;
            UUID uUID3 = uUID;
            if (!uUID2.equals(uUID3) || (entityDamageEvent3 = entry.getValue().creator) == (entityDamageEvent2 = entityDamageEvent)) continue;
            Player player2 = Bukkit.getPlayer((UUID)entry.getKey());
            AstraLifecycleListener astraLifecycleListener = this;
            UUID uUID4 = entry.getKey();
            astraLifecycleListener.restoreUnbounded(uUID4, 1078091064);
            TagStore tagStore = this.tags;
            UUID uUID5 = uUID;
            String string = "astra";
            String string2 = "haunt-tag";
            int n = (int)GemTuning.duration(string, string2, 15.0, 812730832);
            tagStore.addTagSeconds(uUID5, n, 1101624808);
            if (player2 == null) break;
            Player player3 = player2;
            String string3 = player.getName();
            int n2 = AstraLifecycleListener.hauntSeconds(1247669959);
            Component component = Text.legacy("&5\ud83d\udd2e &bYou haunted and disabled \u00a7e" + string3 + " &bgem for \u00a7e" + n2 + "s", 373192400);
            player3.sendMessage(component);
            break;
        }
    }

    @EventHandler
    public void onStartSpectating(PlayerStartSpectatingEntityEvent playerStartSpectatingEntityEvent) {
        Map<UUID, AstraGemListener.ProjectionState> map = this.astra.projections;
        Player player = playerStartSpectatingEntityEvent.getPlayer();
        UUID uUID = player.getUniqueId();
        if (!map.containsKey(uUID)) {
            Player player2;
            UUID uUID2;
            Map<UUID, AstraGemListener.ProjectionState> map2;
            Map<UUID, Unbounded> map3 = this.unbounded;
            UUID uUID3 = player.getUniqueId();
            Unbounded unbounded = map3.get(uUID3);
            if (unbounded != null) {
                UUID uUID4;
                UUID uUID5;
                Entity entity = playerStartSpectatingEntityEvent.getNewSpectatorTarget();
                if (entity == null || !(uUID5 = unbounded.victim).equals(uUID4 = entity.getUniqueId())) {
                    playerStartSpectatingEntityEvent.setCancelled(true);
                }
                return;
            }
            Entity entity = playerStartSpectatingEntityEvent.getNewSpectatorTarget();
            if (entity instanceof Player && (map2 = this.astra.projections).containsKey(uUID2 = (player2 = (Player)entity).getUniqueId())) {
                playerStartSpectatingEntityEvent.setCancelled(true);
            }
            return;
        }
        playerStartSpectatingEntityEvent.setCancelled(true);
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        int n = BlissCoreItems.gemIdOf(itemStack, 382441079);
        int n2 = GemType.ASTRA.id();
        if (n == n2 && BlissCoreItems.tierOf(itemStack, 1633828214) == 2) {
            AstraProjectionListener astraProjectionListener = this.projection;
            Player player3 = player;
            astraProjectionListener.disableProjection(player3, 1333627516);
            TagStore tagStore = this.tags;
            UUID uUID6 = player.getUniqueId();
            tagStore.addTagSeconds(uUID6, 1, 1101624808);
            Entity entity = playerStartSpectatingEntityEvent.getNewSpectatorTarget();
            if (entity instanceof Player) {
                UUID uUID7;
                Player player4 = (Player)entity;
                TrustStore trustStore = this.trust;
                UUID uUID8 = player.getUniqueId();
                if (!trustStore.isTrusted(uUID8, uUID7 = player4.getUniqueId(), 1736364606)) {
                    TagStore tagStore2 = this.tags;
                    UUID uUID9 = player4.getUniqueId();
                    String string = "astra";
                    String string2 = "haunt-tag";
                    int n3 = (int)GemTuning.duration(string, string2, 15.0, 812730832);
                    tagStore2.addTagSeconds(uUID9, n3, 1101624808);
                    Player player5 = player;
                    String string3 = player4.getName();
                    int n4 = AstraLifecycleListener.hauntSeconds(1247669959);
                    Component component = Text.legacy("&5\ud83d\udd2e &bSpooked &e" + string3 + " &bfor &e" + n4 + "s", 373192400);
                    player5.sendMessage(component);
                }
            }
        }
    }

    @EventHandler
    public void onGhostRightClickEntity(PlayerInteractEntityEvent playerInteractEntityEvent) {
        int n;
        EquipmentSlot equipmentSlot;
        EquipmentSlot equipmentSlot2 = playerInteractEntityEvent.getHand();
        if (equipmentSlot2 != (equipmentSlot = EquipmentSlot.HAND)) {
            return;
        }
        Map<UUID, AstraGemListener.ProjectionState> map = this.astra.projections;
        Player player = playerInteractEntityEvent.getPlayer();
        UUID uUID = player.getUniqueId();
        if (!map.containsKey(uUID)) {
            return;
        }
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        int n2 = BlissCoreItems.gemIdOf(itemStack, 382441079);
        if (n2 != (n = GemType.ASTRA.id())) {
            return;
        }
        if (BlissCoreItems.tierOf(itemStack, 1633828214) != 2) {
            return;
        }
        Entity entity = playerInteractEntityEvent.getRightClicked();
        Player player2 = player;
        Component component = Text.legacy("<##A01FFF>\ud83d\udd2e &bTagged " + entity.getName(), 373192400);
        player2.sendMessage(component);
        if (entity instanceof Player) {
            UUID uUID2;
            Player player3 = (Player)entity;
            TrustStore trustStore = this.trust;
            UUID uUID3 = player.getUniqueId();
            if (trustStore.isTrusted(uUID3, uUID2 = player3.getUniqueId(), 1736364606)) {
                return;
            }
        }
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            PotionEffectType potionEffectType = PotionEffectType.GLOWING;
            PotionEffect potionEffect = new PotionEffect(potionEffectType, 40, 0, true, false, false);
            livingEntity.addPotionEffect(potionEffect);
        }
    }

    private static final class Unbounded {
        final UUID victim;
        final GameMode origMode;
        final EntityDamageEvent creator;

        Unbounded(UUID uUID, GameMode gameMode, EntityDamageEvent entityDamageEvent, int n) {
            EntityDamageEvent entityDamageEvent2;
            GameMode gameMode2;
            UUID uUID2;
            this.victim = uUID;
            this.origMode = gameMode;
            this.creator = entityDamageEvent;
        }
    }
}
