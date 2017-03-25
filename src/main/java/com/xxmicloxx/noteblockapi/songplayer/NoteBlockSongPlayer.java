package com.xxmicloxx.noteblockapi.songplayer;

import lombok.Getter;
import lombok.Setter;

import com.xxmicloxx.noteblockapi.model.Instrument;
import com.xxmicloxx.noteblockapi.model.Layer;
import com.xxmicloxx.noteblockapi.model.Note;
import com.xxmicloxx.noteblockapi.NoteBlockPlugin;
import com.xxmicloxx.noteblockapi.model.NotePitch;
import com.xxmicloxx.noteblockapi.model.Song;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

@Getter
@Setter
public class NoteBlockSongPlayer extends RangedSongPlayer {

    private Block noteBlock;
    private int distance = 16;

    public NoteBlockSongPlayer(NoteBlockPlugin plugin, Song song, Block noteBlock) {
        super(plugin, song);
        this.noteBlock = noteBlock;
    }

    @Override
    public void playTick(Player p, int tick) {
        if (noteBlock.getType() != Material.NOTE_BLOCK) {
            return;
        }

        if (!p.getWorld().getName().equals(noteBlock.getWorld().getName())) {
            // not in same world
            return;
        }

        byte playerVolume = plugin.getPlayerVolume(p);

        for (Layer l : getSong().getLayerHashMap().values()) {
            Note note = l.getNote(tick);
            if (note == null) {
                continue;
            }
            p.playNote(noteBlock.getLocation(), Instrument.getBukkitInstrument(note.getInstrument()), new org.bukkit.Note(note.getKey() - 33));
            p.playSound(noteBlock.getLocation(),
                    Instrument.getInstrument(note.getInstrument()),
                    ((l.getVolume() * (int) getVolume() * (int) playerVolume) / 1000000f) * ((1f/16f) * distance),
                    NotePitch.getPitch(note.getKey() - 33)
            );

            recalculateRange(p);
        }
    }

    @Override
    public boolean isPlayerInRange(Player p){
        return !(p.getLocation().distance(noteBlock.getLocation()) > distance);
    }
}
