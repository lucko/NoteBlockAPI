package com.xxmicloxx.noteblockapi.model;

import lombok.AllArgsConstructor;

import com.xxmicloxx.noteblockapi.utils.NoteblockCompatibility;

@AllArgsConstructor
public enum NotePitch {

    NOTE_0(0.5F, 0.50000F),
    NOTE_1(0.53F, 0.52973F),
    NOTE_2(0.56F, 0.56123F),
    NOTE_3(0.6F, 0.59461F),
    NOTE_4(0.63F, 0.62995F),
    NOTE_5(0.67F, 0.66741F),
    NOTE_6(0.7F, 0.70711F),
    NOTE_7(0.76F, 0.74916F),
    NOTE_8(0.8F, 0.79370F),
    NOTE_9(0.84F, 0.84089F),
    NOTE_10(0.9F, 0.89091F),
    NOTE_11(0.94F, 0.94386F),
    NOTE_12(1.0F, 1.00000F),
    NOTE_13(1.06F, 1.05945F),
    NOTE_14(1.12F, 1.12245F),
    NOTE_15(1.18F, 1.18920F),
    NOTE_16(1.26F, 1.25993F),
    NOTE_17(1.34F, 1.33484F),
    NOTE_18(1.42F, 1.41420F),
    NOTE_19(1.5F, 1.49832F),
    NOTE_20(1.6F, 1.58741F),
    NOTE_21(1.68F, 1.68180F),
    NOTE_22(1.78F, 1.78180F),
    NOTE_23(1.88F, 1.88775F),
    NOTE_24(2.0F, 2.00000F);

    public float pitchPre1_9;
    public float pitchPost1_9;

    public static NotePitch getNotePitch(int note) {
        if (note < 0 || note >= values().length) {
            return null;
        }

        return values()[note];
    }

    public static float getPitch(int note) {
        NotePitch notePitch = getNotePitch(note);
        if (notePitch == null) {
            return 0.0F;
        }

        return NoteblockCompatibility.detect() == NoteblockCompatibility.PRE_1_9 ? notePitch.pitchPre1_9 : notePitch.pitchPost1_9;
    }
}