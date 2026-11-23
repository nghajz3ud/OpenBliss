package com.bliss.core;

import java.util.Random;

public enum GemType {
    NONE(0, "ɴᴏɴᴇ", "&7", 0),
    STRENGTH(1, "ꜱᴛʀᴇɴɢᴛʜ", "&4&l", 10),
    FIRE(2, "ꜰɪʀᴇ", "&6&l", 2),
    WEALTH(3, "ᴡᴇᴀʟᴛʜ", "&2&l", 12),
    SPEED(4, "ꜱᴘᴇᴇᴅ", "&e&l", 8),
    PUFF(5, "ᴘᴜꜰꜰ", "&f&l", 6),
    ASTRA(6, "ᴀꜱᴛʀᴀ", "&5&l", 14),
    LIFE(7, "ʟɪꜰᴇ", "&d&l", 4),
    FLUX(8, "ꜰʟᴜx", "&b&l", 98);

    private final int id;
    private final String label;
    private final String colorPrefix;
    private final int displayModelData;

    private GemType(int nullxx, String nullxxx, String nullxxxx, int nullxxxxx) {
        this.id = nullxx;
        this.label = nullxxx;
        this.colorPrefix = nullxxxx;
        this.displayModelData = nullxxxxx;
    }

    public int id() {
        return this.id;
    }

    public String label() {
        return this.label;
    }

    public String colorPrefix() {
        return this.colorPrefix;
    }

    public int displayModelData() {
        return this.displayModelData;
    }

    public String coloredName() {
        String var3 = this.colorPrefix;
        String var5 = this.label;
        return var3 + var5;
    }

    public static GemType byId(int var0) {
        for (GemType var6 : values()) {
            int var15 = var6.id;
            if (var15 == var0) {
                return var6;
            }
        }

        return NONE;
    }

    public static GemType random(Random var0) {
        int var6 = var0.nextInt(8);
        return byId(1 + var6);
    }

    public static GemType randomDifferent(GemType var0, Random var1) {
        GemType var4 = random(var1);

        while (var4 == var0) {
            var4 = random(var1);
        }

        return var4;
    }
}
