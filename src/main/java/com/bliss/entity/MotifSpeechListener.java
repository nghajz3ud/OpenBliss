package com.bliss.entity;

import com.bliss.BlissPlugin;
import io.papermc.paper.event.player.AsyncChatEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.logging.Logger;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

public final class MotifSpeechListener implements Listener {
    private static final long TYPE_TICKS = 2L;
    private static final long HOLD_TICKS = 60L;
    private static final int MAX_CHARS = 120;
    private static final TextColor GOLD = TextColor.color(16772800);
    private static final List<String> WHISPERS;
    private static final Map<UUID, Integer> LISTENING;
    private final BlissPlugin plugin;
    private final Map<UUID, BukkitRunnable> typing;
    private final Map<UUID, List<UUID>> audiences;
    private static final UUID ANNOUNCER;

    public static boolean isSpeakingTo(UUID var0, int var1) {
        if (var0 != null) {
            Map var4 = LISTENING;
            if (var4.containsKey(var0)) {
                return true;
            }
        }

        return false;
    }

    private static void addListeners(List<UUID> var0, int var1) {
        for (UUID var7 : var0) {
            Map<UUID, Integer> var14 = LISTENING;
            Integer var16 = 1;
            BiFunction<Integer, Integer, Integer> var5 = Integer::sum;
            var14.merge(var7, var16, var5);
        }
    }

    private static void dropListeners(List<UUID> var0, int var1) {
        for (UUID var6 : var0) {
            Map<UUID, Integer> var13 = LISTENING;
            BiFunction<UUID, Integer, Integer> var4 = (var0x, var1x) -> {
                Integer var5;
                if (var1x <= 1) {
                    var5 = null;
                } else {
                    var5 = var1x - 1;
                }

                return var5;
            };
            var13.computeIfPresent(var6, var4);
        }
    }

    public MotifSpeechListener(BlissPlugin var1, int var2) {
        HashMap var4 = new HashMap();
        this.typing = var4;
        HashMap var8 = new HashMap();
        this.audiences = var8;
        this.plugin = var1;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST,
        ignoreCancelled = true
    )
    public void onChat(AsyncChatEvent var1) {
        Player var8 = var1.getPlayer();
        if (this.motifOf(var8, 1929372979) != null) {
            PlainTextComponentSerializer var17 = PlainTextComponentSerializer.plainText();
            Component var38 = var1.message();
            String var9 = var17.serialize(var38);
            ArrayList var19 = new ArrayList();
            ArrayList var10 = var19;

            for (Audience var12 : var1.viewers()) {
                if (var12 instanceof Player var13) {
                    UUID var43 = var13.getUniqueId();
                    var10.add(var43);
                }
            }

            var1.setCancelled(true);
            BukkitScheduler var26 = Bukkit.getScheduler();
            BlissPlugin var41 = this.plugin;
            Runnable var44 = () -> this.speak(var8, var9, var10, 1624937474);
            var26.runTask(var41, var44);
        }
    }

    @EventHandler(
        priority = EventPriority.HIGHEST,
        ignoreCancelled = true
    )
    public void onCommand(PlayerCommandPreprocessEvent var1) {
        Player var6 = var1.getPlayer();
        if (this.motifOf(var6, 1929372979) != null) {
            String var16 = var1.getMessage().substring(1);
            String var37 = " ";
            String[] var7 = var16.split(var37, 3);
            if (var7.length >= 3) {
                String var8 = var7[0].toLowerCase();
                int var9 = var8.indexOf(58);
                if (var9 >= 0) {
                    int var42 = var9 + 1;
                    var8 = var8.substring(var42);
                }

                List var28 = WHISPERS;
                if (var28.contains(var8)) {
                    Player var10 = Bukkit.getPlayerExact(var7[1]);
                    if (var10 != null) {
                        var1.setCancelled(true);
                        String var49 = var7[2];
                        List var52 = List.of(var10.getUniqueId());
                        this.speak(var6, var49, var52, 1624937474);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent var1) {
        UUID var5 = var1.getPlayer().getUniqueId();
        this.stopTyping(var5, 420334821);
    }

    public void stop(int var1) {
        Iterator var3 = this.typing.values().iterator();

        while (var3.hasNext()) {
            ((BukkitRunnable)var3.next()).cancel();
        }

        this.typing.clear();
        this.audiences.clear();
        LISTENING.clear();
    }

    private String motifOf(Player var1, int var2) {
        EntityMotifs var5 = this.plugin.entityMotifs(949215902);
        String var10;
        if (var5 == null) {
            var10 = null;
        } else {
            var10 = var5.motifOf(var1, 213258127);
        }

        return var10;
    }

    private void stopTyping(UUID var1, int var2) {
        Map var7 = this.typing;
        BukkitRunnable var5 = (BukkitRunnable)var7.remove(var1);
        if (var5 != null) {
            var5.cancel();
        }

        Map var13 = this.audiences;
        List var6 = (List)var13.remove(var1);
        if (var6 != null) {
            dropListeners(var6, 238151242);
        }
    }

    private void speak(Player var1, String var2, List<UUID> var3, int var4) {
        UUID var10 = var1.getUniqueId();
        this.type(var10, var2, var3, null, 604699711);
    }

    public void announce(String var1, Runnable var2, int var3) {
        ArrayList var4 = new ArrayList();
        ArrayList var8 = var4;

        for (Player var10 : Bukkit.getOnlinePlayers()) {
            UUID var27 = var10.getUniqueId();
            var8.add(var27);
        }

        if (var8.isEmpty()) {
            if (var2 != null) {
                var2.run();
            }
        } else {
            UUID var25 = ANNOUNCER;
            this.type(var25, var1, var8, var2, 604699711);
        }
    }

    private void type(final UUID var1, String var2, final List<UUID> var3, final Runnable var4, int var5) {
        String var13Trimmed = var2.strip();
        if (!var13Trimmed.isEmpty() && !var3.isEmpty()) {
            if (var13Trimmed.length() > 120) {
                var13Trimmed = var13Trimmed.substring(0, 119) + "…";
            }
            final String var13 = var13Trimmed;

            this.stopTyping(var1, 420334821);
            addListeners(var3, 695645824);
            Map var30 = this.audiences;
            var30.put(var1, var3);
            BukkitRunnable var32 = new BukkitRunnable() {
                int shown = 0;
                long heldFor = 0L;
                boolean done = false;

                public void run() {
                    int var10 = this.shown;
                    int var54 = var13.length();
                    if (var10 < var54) {
                        int var56 = this.shown + 1;
                        this.shown = var56;
                    } else {
                        if (!this.done) {
                            this.done = true;
                            if (var4 != null) {
                                try {
                                    var4.run();
                                } catch (Throwable var77) {
                                    Logger var52 = MotifSpeechListener.this.plugin.getLogger();
                                    String var71 = "action-bar callback failed: " + var77;
                                    var52.warning(var71);
                                }
                            }
                        }

                        long var63 = this.heldFor + 2L;
                        this.heldFor = var63;
                        if (var63 > 60L) {
                            Map var39 = MotifSpeechListener.this.typing;
                            UUID var66 = var1;
                            var39.remove(var66);
                            Map var43 = MotifSpeechListener.this.audiences;
                            UUID var68 = var1;
                            var43.remove(var68);
                            MotifSpeechListener.dropListeners(var3, 238151242);
                            this.cancel();
                            return;
                        }
                    }

                    String var13x = var13;
                    int var73 = this.shown;
                    String var14 = var13x.substring(0, var73);
                    TextColor var58 = MotifSpeechListener.GOLD;
                    TextComponent var6 = Component.text(var14, var58);
                    Iterator var7 = var3.iterator();

                    while (var7.hasNext()) {
                        Player var9 = Bukkit.getPlayer((UUID)var7.next());
                        if (var9 != null) {
                            var9.sendActionBar(var6);
                        }
                    }
                }
            };
            Map var34 = this.typing;
            var34.put(var1, var32);
            BlissPlugin var45 = this.plugin;
            var32.runTaskTimer(var45, 0L, 2L);
        } else {
            if (var4 != null) {
                var4.run();
            }
        }
    }

    static {
        String var7 = "msg";
        String var1 = "tell";
        String var2 = "w";
        String var3 = "whisper";
        WHISPERS = List.of(var7, var1, var2, var3);
        ConcurrentHashMap var9 = new ConcurrentHashMap();
        LISTENING = var9;
        UUID var10 = new UUID(0L, 0L);
        ANNOUNCER = var10;
    }
}
