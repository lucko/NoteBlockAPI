package com.xxmicloxx.noteblockapi;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class NoteBlockPlugin extends JavaPlugin {
    public static boolean isPre1_9() {
        return Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.7");
    }

    @Getter
    private Map<UUID, List<SongPlayer>> playingSongs = Collections.synchronizedMap(new HashMap<>());

    @Getter
    private Map<UUID, Byte> playerVolume = Collections.synchronizedMap(new HashMap<>());

    @Getter
    private boolean disabling = false;

    @Override
    public void onEnable() {
        getServer().getServicesManager().register(NoteBlockPlugin.class, this, this, ServicePriority.Normal);
    }

    @Override
    public void onDisable() {
        getServer().getServicesManager().unregisterAll(this);

        disabling = true;
        Bukkit.getScheduler().cancelTasks(this);
    }
    
    public boolean isReceivingSong(Player p) {
        List<SongPlayer> songs = getPlayingSongs().get(p.getUniqueId());
        return songs != null && !songs.isEmpty();
    }

    public void stopPlaying(Player p) {
        List<SongPlayer> songs = getPlayingSongs().get(p.getUniqueId());
        if (songs == null) {
            return;
        }

        for (SongPlayer s : songs) {
            s.removePlayer(p);
        }
    }

    public void setPlayerVolume(Player p, byte volume) {
        getPlayerVolume().put(p.getUniqueId(), volume);
    }

    public byte getPlayerVolume(Player p) {
        return getPlayerVolume().getOrDefault(p.getUniqueId(), (byte) 100);
    }

    public void removeSongFromPlayer(Player p, SongPlayer songPlayer) {
        List<SongPlayer> songs = getPlayingSongs().get(p.getUniqueId());
        if (songs == null || songs.isEmpty()) {
            return;
        }

        songs.remove(songPlayer);
    }

    public void addSongToPlayer(Player p, SongPlayer songPlayer) {
        List<SongPlayer> songs = getPlayingSongs().computeIfAbsent(p.getUniqueId(), u -> new ArrayList<>());
        songs.add(songPlayer);
    }

    public void doSync(Runnable r) {
        getServer().getScheduler().runTask(this, r);
    }

    public void doAsync(Runnable r) {
        getServer().getScheduler().runTaskAsynchronously(this, r);
    }
}
