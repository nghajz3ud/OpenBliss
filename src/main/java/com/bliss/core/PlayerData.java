package com.bliss.core;

import com.bliss.entity.MotifSpeechListener;
import com.bliss.honored.AssessmentWindowBarTask;
import java.util.UUID;

public final class PlayerData {
    public static final int MAX_ENERGY = 11;
    public static final int SHATTERED_ENERGY = 1;
    private GemType gem;
    private boolean tier2;
    private int energy;
    private boolean joined;
    private UUID owner;
    private static boolean energyRestrictionsEnabled = true;
    private static boolean dreamEnergyRestrictionsEnabled = true;
    private static boolean dreamNoEnergyMode = false;
    private boolean inDream;
    private static boolean noEnergyMode = false;
    private boolean restorationLock;
    private boolean tradeLock;
    private boolean inSpawnRegion;
    private long tagExpiryMs;
    private static final long TAG_MAX_MS = 40000L;
    private boolean adminTag;

    public PlayerData() {
        GemType var2 = GemType.NONE;
        this.gem = var2;
        this.tier2 = false;
        this.energy = 1;
        this.joined = false;
        this.inDream = false;
        this.restorationLock = false;
        this.tradeLock = false;
        this.inSpawnRegion = false;
        this.tagExpiryMs = 0L;
    }

    public void setOwner(UUID var1, int var2) {
        this.owner = var1;
    }

    public GemType gem(int var1) {
        return this.gem;
    }

    public void setGem(GemType var1, int var2) {
        GemType var7;
        if (var1 == null) {
            var7 = GemType.NONE;
        } else {
            var7 = var1;
        }

        this.gem = var7;
        GemAutoEnchants.forget(this.owner, 578466762);
    }

    public boolean tier2(int var1) {
        return this.tier2;
    }

    public void setTier2(boolean var1, int var2) {
        this.tier2 = var1;
    }

    private int effEnergy(int var1) {
        int var5;
        if (this.noEnergy(328579270)) {
            var5 = 11;
        } else {
            var5 = this.energy;
        }

        return var5;
    }

    public int energy(int var1) {
        return this.effEnergy(978902702);
    }

    public int rawEnergy(int var1) {
        return this.energy;
    }

    public void setEnergy(int var1, int var2) {
        int var8 = Math.min(11, var1);
        int var7 = Math.max(0, var8);
        this.energy = var7;
    }

    public void addEnergy(int var1, int var2) {
        int var6 = this.energy;
        int var7 = var6 + var1;
        this.setEnergy(var7, 319922676);
    }

    public boolean isShattered() {
        boolean var4;
        if (this.effEnergy(978902702) <= 1) {
            var4 = true;
        } else {
            var4 = false;
        }

        return var4;
    }

    public static void setEnergyRestrictionsEnabled(boolean var0, int var1) {
        energyRestrictionsEnabled = var0;
    }

    public static boolean energyRestrictionsEnabled(int var0) {
        return energyRestrictionsEnabled;
    }

    public static void setDreamEnergyRestrictionsEnabled(boolean var0, int var1) {
        dreamEnergyRestrictionsEnabled = var0;
    }

    public static void setDreamNoEnergyMode(boolean var0, int var1) {
        dreamNoEnergyMode = var0;
    }

    public void setInDream(boolean var1, int var2) {
        this.inDream = var1;
    }

    public boolean inDream() {
        return this.inDream;
    }

    boolean restrictions(int var1) {
        boolean var4;
        if (this.inDream) {
            var4 = dreamEnergyRestrictionsEnabled;
        } else {
            var4 = energyRestrictionsEnabled;
        }

        return var4;
    }

    public boolean noEnergy(int var1) {
        boolean var4;
        if (this.inDream) {
            var4 = dreamNoEnergyMode;
        } else {
            var4 = noEnergyMode;
        }

        return var4;
    }

    public static void setNoEnergyMode(boolean var0, int var1) {
        noEnergyMode = var0;
    }

    public static boolean noEnergyMode() {
        return noEnergyMode;
    }

    public boolean restorationLock(int var1) {
        return this.restorationLock;
    }

    public void setRestorationLock(boolean var1, int var2) {
        this.restorationLock = var1;
    }

    public boolean tradeLock(int var1) {
        return this.tradeLock;
    }

    public void setTradeLock(boolean var1, int var2) {
        this.tradeLock = var1;
    }

    public boolean inSpawnRegion() {
        return this.inSpawnRegion;
    }

    public void setInSpawnRegion(boolean var1, int var2) {
        this.inSpawnRegion = var1;
    }

    public boolean isTagged(int var1) {
        long var5 = this.tagExpiryMs;
        long var3 = System.currentTimeMillis();
        boolean var8;
        if (var5 > var3) {
            var8 = true;
        } else {
            var8 = false;
        }

        return var8;
    }

    public int tagSecondsLeft(int var1) {
        long var7 = this.tagExpiryMs;
        long var3 = System.currentTimeMillis();
        long var5 = var7 - var3;
        int var20;
        if (var5 <= 0L) {
            var20 = 0;
        } else {
            var20 = (int)((var5 + 999L) / 1000L);
        }

        return var20;
    }

    public void addTagSeconds(int var1, int var2) {
        long var14 = System.currentTimeMillis();
        long var23 = this.tagExpiryMs;
        if (var23 <= var14) {
            this.adminTag = false;
        }

        long var28 = this.tagExpiryMs;
        long var41;
        if (var28 > var14) {
            var41 = this.tagExpiryMs;
        } else {
            var41 = var14;
        }

        long var53 = (long)var1 * 1000L;
        long var18 = var41 + var53;
        long var20 = var14 + 40000L;
        long var44 = this.tagExpiryMs;
        long var57 = Math.min(var18, var20);
        long var46 = Math.max(var44, var57);
        this.tagExpiryMs = var46;
    }

    public void addTagSecondsUncapped(int var1, int var2) {
        long var12 = System.currentTimeMillis();
        long var17 = this.tagExpiryMs;
        long var23;
        if (var17 > var12) {
            var23 = this.tagExpiryMs;
        } else {
            var23 = var12;
        }

        long var30 = (long)var1 * 1000L;
        long var25 = var23 + var30;
        this.tagExpiryMs = var25;
        this.adminTag = true;
    }

    public void clearTag(int var1) {
        this.tagExpiryMs = 0L;
        this.adminTag = false;
    }

    public void clearTagOnDeath(int var1) {
        if (!this.adminTag) {
            this.clearTag(1961285331);
        }
    }

    private boolean gateLocked(int var1) {
        boolean var10;
        if (!this.restorationLock && !this.tradeLock && !this.isTagged(555875923) && !this.inSpawnRegion) {
            var10 = false;
        } else {
            var10 = true;
        }

        return var10;
    }

    public boolean hudSuppressed(int var1) {
        boolean var10;
        if (!this.isTagged(555875923)
            && !this.inSpawnRegion
            && !AssessmentWindowBarTask.isShowing(1134156722)
            && !MotifSpeechListener.isSpeakingTo(this.owner, 1524431478)) {
            var10 = false;
        } else {
            var10 = true;
        }

        return var10;
    }

    public boolean canUsePassives(int var1) {
        boolean var11;
        if (this.gateLocked(572656357) || this.isBroken(430675503) || this.restrictions(457321189) && this.effEnergy(978902702) < 3) {
            var11 = false;
        } else {
            var11 = true;
        }

        return var11;
    }

    public boolean canUsePowers(int var1) {
        boolean var9;
        if (this.gateLocked(572656357) || this.restrictions(457321189) && this.effEnergy(978902702) < 4) {
            var9 = false;
        } else {
            var9 = true;
        }

        return var9;
    }

    public boolean canUseAbilities(int var1) {
        boolean var9;
        if (this.gateLocked(572656357) || this.restrictions(457321189) && this.effEnergy(978902702) < 2) {
            var9 = false;
        } else {
            var9 = true;
        }

        return var9;
    }

    public boolean isBroken(int var1) {
        boolean var5;
        if (this.effEnergy(978902702) <= 1) {
            var5 = true;
        } else {
            var5 = false;
        }

        return var5;
    }

    public boolean joined(int var1) {
        return this.joined;
    }

    public void setJoined(boolean var1, int var2) {
        this.joined = var1;
    }
}
