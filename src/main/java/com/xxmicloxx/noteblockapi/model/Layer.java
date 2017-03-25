package com.xxmicloxx.noteblockapi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Layer {

    private Map<Integer, Note> hashMap = Collections.synchronizedMap(new HashMap<>());
    private byte volume = 100;
    private String name = "";

    public Note getNote(int tick) {
        return hashMap.get(tick);
    }

    public void setNote(int tick, Note note) {
        hashMap.put(tick, note);
    }
}
