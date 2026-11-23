package com.bliss.pedestal;

import com.bliss.BlissPlugin;
import com.bliss.villager.Text;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class PedestalTickTask {
    private final BlissPlugin plugin;
    private final PedestalState state;
    private BukkitTask task;

    public PedestalTickTask(BlissPlugin var1, PedestalState var2, int var3) {
        this.plugin = var1;
        this.state = var2;
    }

    public void start(int var1) {
        BukkitScheduler var3 = Bukkit.getScheduler();
        BlissPlugin var11 = this.plugin;
        Runnable var12 = this::tick;
        BukkitTask var10 = var3.runTaskTimer(var11, var12, 20L, 20L);
        this.task = var10;
    }

    public void stop(int var1) {
        if (this.task != null) {
            this.task.cancel();
        }
    }

    private void tick() {
        if (this.state.depositCooldown(51099791) > 1) {
            PedestalState var9 = this.state;
            int var49 = this.state.depositCooldown(51099791) - 1;
            var9.setDepositCooldown(var49, 174029610);
        } else if (this.state.depositCooldown(51099791) == 1) {
            this.state.setDepositCooldown(0, 174029610);
            if (this.state.mainLoc(15453813) != null) {
                PedestalState.Ritual var25 = this.state.ritual(1751702735);
                PedestalState.Ritual var56 = PedestalState.Ritual.NONE;
                if (var25 != var56 && this.state.count(629418704) < 5) {
                    for (Player var5 : this.state.mainLoc(15453813).getWorld().getPlayers()) {
                        Location var41 = var5.getLocation();
                        Location var60 = this.state.mainLoc(15453813);
                        if (var41.distanceSquared(var60) <= 64.0) {
                            Component var62 = Text.legacy("&6Pedestal cooldown is over. <##96FFD9>Please deposit 1 &lᴇɴᴇʀɢʏ!", 373192400);
                            var5.sendMessage(var62);
                        }
                    }
                }
            }
        }

        if (this.state.breakCooldown(566728259) > 0) {
            PedestalState var14 = this.state;
            int var53 = this.state.breakCooldown(566728259) - 1;
            var14.setBreakCooldown(var53, 548095927);
        }
    }
}
