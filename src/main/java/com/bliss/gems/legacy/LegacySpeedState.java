package com.bliss.gems.legacy;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class LegacySpeedState {
    public final Map<UUID, Long> blurCd;
    public final Map<UUID, Long> stormCd;
    public final Map<UUID, Long> terminalCd;
    public final Map<UUID, Long> hasteUntil;
    public final Map<UUID, Long> surgeUntil;
    public final Map<UUID, Long> frozenUntil;

    public LegacySpeedState() {
        ConcurrentHashMap var2 = new ConcurrentHashMap();
        this.blurCd = var2;
        ConcurrentHashMap var9 = new ConcurrentHashMap();
        this.stormCd = var9;
        ConcurrentHashMap var10 = new ConcurrentHashMap();
        this.terminalCd = var10;
        ConcurrentHashMap var11 = new ConcurrentHashMap();
        this.hasteUntil = var11;
        ConcurrentHashMap var12 = new ConcurrentHashMap();
        this.surgeUntil = var12;
        ConcurrentHashMap var13 = new ConcurrentHashMap();
        this.frozenUntil = var13;
    }

    public void clear(UUID var1) {
        Map var4 = this.blurCd;
        var4.remove(var1);
        Map var7 = this.stormCd;
        var7.remove(var1);
        Map var10 = this.terminalCd;
        var10.remove(var1);
        Map var13 = this.hasteUntil;
        var13.remove(var1);
        Map var16 = this.surgeUntil;
        var16.remove(var1);
        Map var19 = this.frozenUntil;
        var19.remove(var1);
    }
}
