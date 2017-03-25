package com.xxmicloxx.noteblockapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public class Song {

    private float speed;
    private Map<Integer, Layer> layerHashMap = Collections.synchronizedMap(new HashMap<>());
    private short songHeight;
    private short length;
    private String title;

    private String author;
    private String description;
    private File path;

    private float delay;

    public Song(float speed, HashMap<Integer, Layer> layerHashMap, short songHeight, final short length, String title, String author, String description, File path) {
        this(speed, layerHashMap, songHeight, length, title, author, description, path, 20 / speed);
    }

    public Song(Song other) {
        this(other.getSpeed(), other.getLayerHashMap(), other.getSongHeight(), other.getLength(), other.getTitle(), other.getAuthor(), other.getDescription(), other.getPath(), other.getDelay());
    }
}
