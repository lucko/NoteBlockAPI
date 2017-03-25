package com.xxmicloxx.noteblockapi.songplayer;

import lombok.Getter;
import lombok.Setter;

import com.xxmicloxx.noteblockapi.model.Instrument;
import com.xxmicloxx.noteblockapi.model.Layer;
import com.xxmicloxx.noteblockapi.model.Note;
import com.xxmicloxx.noteblockapi.NoteBlockPlugin;
import com.xxmicloxx.noteblockapi.model.NotePitch;
import com.xxmicloxx.noteblockapi.model.Song;

import org.bukkit.Location;
import org.bukkit.entity.Player;

@Getter
@Setter
public class PositionSongPlayer extends RangedSongPlayer {

    private Location targetLocation;
    private int distance = 16;

    public PositionSongPlayer(NoteBlockPlugin plugin, Song song, Location targetLocation) {
        super(plugin, song);
        this.targetLocation = targetLocation;
    }

    @Override
    public void playTick(Player p, int tick) {
        if (!p.getWorld().getName().equals(targetLocation.getWorld().getName())) {
            // not in same world
            return;
        }

        byte playerVolume = plugin.getPlayerVolume(p);

        for (Layer l : getSong().getLayerHashMap().values()) {
            Note note = l.getNote(tick);
            if (note == null) {
                continue;
            }
            p.playSound(targetLocation,
                    Instrument.getInstrument(note.getInstrument()),
                    ((l.getVolume() * (int) getVolume() * (int) playerVolume) / 1000000f) * ((1f/16f) * distance),
                    NotePitch.getPitch(note.getKey() - 33)
            );

            recalculateRange(p);
        }
    }
    
    public boolean isPlayerInRange(Player p){
        return !(p.getLocation().distance(targetLocation) > distance);
    }
}
