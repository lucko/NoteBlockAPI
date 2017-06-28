package com.xxmicloxx.noteblockapi.utils;

import org.bukkit.Bukkit;

public enum NoteblockCompatibility {
    PRE_1_9, PRE_1_12, POST_1_12;

    private static NoteblockCompatibility cache = null;
    public static synchronized NoteblockCompatibility detect() {
        if (cache != null) {
            return cache;
        }

        String ver = getServerVersion();

        if (ver == null || ver.startsWith("1_7_") || ver.startsWith("1_8_")) {
            cache = PRE_1_9;
            return PRE_1_9;
        }

        if (ver.startsWith("1_9_") || ver.startsWith("1_10_") || ver.startsWith("1_11_")) {
            cache = PRE_1_12;
            return PRE_1_12;
        }

        cache = POST_1_12;
        return POST_1_12;
    }

    private static String getServerVersion() {
        Class<?> server = Bukkit.getServer().getClass();
        if (!server.getSimpleName().equals("CraftServer")) {
            return null;
        }
        if (server.getName().equals("org.bukkit.craftbukkit.CraftServer")) {
            // Non versioned class
            return null;
        } else {
            String version = server.getName().substring("org.bukkit.craftbukkit.".length());
            return version.substring(0, version.length() - ".CraftServer".length());
        }
    }
}
