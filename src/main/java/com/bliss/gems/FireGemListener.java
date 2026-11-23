package com.bliss.gems;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.event.Listener;

public final class FireGemListener implements Listener {
    public final Map<UUID, Long> cd1;
    public final Map<UUID, Long> cd2;
    public final Map<UUID, Long> cd3;
    public final Map<UUID, Long> cd4;
    public final Map<UUID, Long> crispActiveUntil;

    public FireGemListener() {
        HashMap var2 = new HashMap();
        this.cd1 = var2;
        HashMap var8 = new HashMap();
        this.cd2 = var8;
        HashMap var9 = new HashMap();
        this.cd3 = var9;
        HashMap var10 = new HashMap();
        this.cd4 = var10;
        HashMap var11 = new HashMap();
        this.crispActiveUntil = var11;
    }
}
