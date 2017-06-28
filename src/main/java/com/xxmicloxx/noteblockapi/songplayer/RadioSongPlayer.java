package com.xxmicloxx.noteblockapi.songplayer;

import com.xxmicloxx.noteblockapi.NoteBlockPlugin;
import com.xxmicloxx.noteblockapi.SongPlayer;
import com.xxmicloxx.noteblockapi.model.Instrument;
import com.xxmicloxx.noteblockapi.model.Layer;
import com.xxmicloxx.noteblockapi.model.Note;
import com.xxmicloxx.noteblockapi.model.NotePitch;
import com.xxmicloxx.noteblockapi.model.Song;

import org.bukkit.entity.Player;

public class RadioSongPlayer extends SongPlayer {
    public RadioSongPlayer(NoteBlockPlugin plugin, Song song) {
        super(plugin, song);
    }

    @Override
    public void playTick(Player p, int tick) {
        byte playerVolume = plugin.getPlayerVolume(p);

        for (Layer l : getSong().getLayerHashMap().values()) {
            Note note = l.getNote(tick);
            if (note == null) {
                continue;
            }

            p.playSound(p.getEyeLocation(),
                    Instrument.getInstrument(note.getInstrument()),
                    (l.getVolume() * (int) getVolume() * (int) playerVolume) / 1000000f,
                    NotePitch.getPitch(note.getKey() - 33)
            );
        }
    }
}
