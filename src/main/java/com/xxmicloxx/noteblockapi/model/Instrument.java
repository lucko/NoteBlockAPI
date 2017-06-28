package com.xxmicloxx.noteblockapi.model;

import com.xxmicloxx.noteblockapi.utils.NoteblockCompatibility;

import org.bukkit.Sound;

public class Instrument {
    private static final Sound[] LOOKUP = getLookup();

    private static Sound[] getLookup() {
        if (NoteblockCompatibility.detect() == NoteblockCompatibility.PRE_1_9) {
            return new Sound[]{
                    Sound.valueOf("NOTE_PIANO"),
                    Sound.valueOf("NOTE_BASS_GUITAR"),
                    Sound.valueOf("NOTE_BASS_DRUM"),
                    Sound.valueOf("NOTE_SNARE_DRUM"),
                    Sound.valueOf("NOTE_STICKS")
            };
        } else if (NoteblockCompatibility.detect() == NoteblockCompatibility.PRE_1_12) {
            return new Sound[]{
                    Sound.valueOf("BLOCK_NOTE_HARP"),
                    Sound.valueOf("BLOCK_NOTE_BASS"),
                    Sound.valueOf("BLOCK_NOTE_BASEDRUM"),
                    Sound.valueOf("BLOCK_NOTE_SNARE"),
                    Sound.valueOf("BLOCK_NOTE_HAT")
            };
        } else {
            return new Sound[]{
                    Sound.valueOf("BLOCK_NOTE_HARP"),
                    Sound.valueOf("BLOCK_NOTE_BASS"),
                    Sound.valueOf("BLOCK_NOTE_BASEDRUM"),
                    Sound.valueOf("BLOCK_NOTE_SNARE"),
                    Sound.valueOf("BLOCK_NOTE_HAT"),
                    Sound.valueOf("BLOCK_NOTE_FLUTE"),
                    Sound.valueOf("BLOCK_NOTE_BELL"),
                    Sound.valueOf("BLOCK_NOTE_GUITAR"),
                    Sound.valueOf("BLOCK_NOTE_CHIME"),
                    Sound.valueOf("BLOCK_NOTE_XYLOPHONE")
            };
        }
    }

    public static Sound getInstrument(byte instrument) {
        if (instrument < 0 || instrument >= LOOKUP.length) {
            return LOOKUP[0];
        }
        return LOOKUP[instrument];
    }

    public static org.bukkit.Instrument getBukkitInstrument(byte instrument) {
        switch (instrument) {
            case 0:
                return org.bukkit.Instrument.PIANO;
            case 1:
                return org.bukkit.Instrument.BASS_GUITAR;
            case 2:
                return org.bukkit.Instrument.BASS_DRUM;
            case 3:
                return org.bukkit.Instrument.SNARE_DRUM;
            case 4:
                return org.bukkit.Instrument.STICKS;
            default:
                return org.bukkit.Instrument.PIANO;
        }
    }
}
