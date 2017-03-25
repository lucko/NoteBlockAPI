package com.xxmicloxx.noteblockapi.songplayer;

import com.xxmicloxx.noteblockapi.NoteBlockPlugin;
import com.xxmicloxx.noteblockapi.SongPlayer;
import com.xxmicloxx.noteblockapi.event.PlayerRangeStateChangeEvent;
import com.xxmicloxx.noteblockapi.model.Song;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class RangedSongPlayer extends SongPlayer {
    public RangedSongPlayer(NoteBlockPlugin plugin, Song song) {
        super(plugin, song);
    }

    protected void recalculateRange(Player p) {
        if (isPlayerInRange(p)){
            if (!this.getPlayerList().get(p.getUniqueId())){
                getPlayerList().put(p.getUniqueId(), true);
                PlayerRangeStateChangeEvent event = new PlayerRangeStateChangeEvent(this, p, true);
                Bukkit.getPluginManager().callEvent(event);
            }
        } else {
            if (this.getPlayerList().get(p.getUniqueId())){
                getPlayerList().put(p.getUniqueId(), false);
                PlayerRangeStateChangeEvent event = new PlayerRangeStateChangeEvent(this, p, false);
                Bukkit.getPluginManager().callEvent(event);
            }
        }
    }

    protected abstract boolean isPlayerInRange(Player p);
}
