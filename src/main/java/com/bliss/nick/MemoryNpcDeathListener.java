package com.bliss.nick;

import net.citizensnpcs.api.event.NPCDeathEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public final class MemoryNpcDeathListener implements Listener {
    private final MemoryNpcManager manager;

    public MemoryNpcDeathListener(MemoryNpcManager var1, int var2) {
        this.manager = var1;
    }

    @EventHandler(
        priority = EventPriority.MONITOR
    )
    public void onNpcDeath(NPCDeathEvent var1) {
        MemoryNpcManager var4 = this.manager;
        NPC var5 = var1.getNPC();
        var4.handleDeath(var5, 2125562792);
    }
}
