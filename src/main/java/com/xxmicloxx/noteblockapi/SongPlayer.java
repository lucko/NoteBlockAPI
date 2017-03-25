package com.xxmicloxx.noteblockapi;

import lombok.Getter;
import lombok.Setter;

import com.xxmicloxx.noteblockapi.event.SongDestroyingEvent;
import com.xxmicloxx.noteblockapi.event.SongEndEvent;
import com.xxmicloxx.noteblockapi.event.SongStoppedEvent;
import com.xxmicloxx.noteblockapi.model.FadeType;
import com.xxmicloxx.noteblockapi.model.Song;
import com.xxmicloxx.noteblockapi.utils.Interpolator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class SongPlayer {
    protected final NoteBlockPlugin plugin;

    @Getter
    private Song song;

    @Getter
    private boolean playing = false;

    @Getter
    @Setter
    private short tick = -1;

    // player --> if they're in range.
    @Getter
    private Map<UUID, Boolean> playerList = Collections.synchronizedMap(new HashMap<>());

    @Getter
    @Setter
    protected boolean autoDestroy = false;

    private boolean destroyed = false;

    @Getter
    @Setter
    private byte fadeTarget = 100;

    @Getter
    @Setter
    private byte volume = 100;

    @Getter
    @Setter
    private byte fadeStart = volume;

    @Getter
    @Setter
    private int fadeDuration = 60;

    @Getter
    @Setter
    private int fadeDone = 0;

    @Getter
    @Setter
    private FadeType fadeType = FadeType.FADE_LINEAR;

    private final Lock lock = new ReentrantLock();

    public SongPlayer(NoteBlockPlugin plugin, Song song) {
        this.plugin = plugin;
        this.song = song;
        start();
    }

    public abstract void playTick(Player p, int tick);

    private void calculateFade() {
        if (fadeDone == fadeDuration) {
            return; // no fade today
        }

        double targetVolume = Interpolator.interpLinear(new double[]{0, fadeStart, fadeDuration, fadeTarget}, fadeDone);
        setVolume((byte)targetVolume);
        fadeDone++;
    }

    private void start() {
        plugin.doAsync(() -> {
            while (!destroyed) {
                long startTime = System.currentTimeMillis();

                lock.lock();
                try {
                    if (destroyed || plugin.isDisabling()) {
                        break;
                    }

                    if (playing) {
                        calculateFade();
                        tick++;

                        if (tick > song.getLength()) {
                            playing = false;
                            tick = -1;
                            SongEndEvent event = new SongEndEvent(SongPlayer.this);
                            plugin.doSync(() -> Bukkit.getPluginManager().callEvent(event));
                            if (autoDestroy) {
                                destroy();
                            }
                            return;
                        }

                        plugin.doSync(() -> {
                            for (UUID s : playerList.keySet()) {
                                Player p = Bukkit.getPlayer(s);
                                if (p == null) {
                                    // offline...
                                    continue;
                                }
                                playTick(p, tick);
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }

                if (destroyed) {
                    break;
                }

                // wait for the next tick.
                long duration = System.currentTimeMillis() - startTime;
                float delayMillis = song.getDelay() * 50;
                if (duration < delayMillis) {
                    try {
                        Thread.sleep((long) (delayMillis - duration));
                    } catch (InterruptedException e) {
                        // do nothing
                    }
                }
            }
        });
    }

    public void addPlayer(Player p) {
        lock.lock();
        try {
            if (!playerList.containsKey(p.getUniqueId())) {
                playerList.put(p.getUniqueId(), false);
                plugin.addSongToPlayer(p, this);
            }
        } finally {
            lock.unlock();
        }
    }

    public void removePlayer(Player p) {
        lock.lock();
        try {
            playerList.remove(p.getUniqueId());
            plugin.removeSongFromPlayer(p, this);

            if (playerList.isEmpty() && autoDestroy) {
                SongEndEvent event = new SongEndEvent(this);
                Bukkit.getPluginManager().callEvent(event);
                destroy();
            }
        } finally {
            lock.unlock();
        }
    }

    public void destroy() {
        lock.lock();
        try {
            SongDestroyingEvent event = new SongDestroyingEvent(this);
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }

            destroyed = true;
            playing = false;
            setTick((short) -1);
        } finally {
            lock.unlock();
        }
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
        if (!playing) {
            SongStoppedEvent event = new SongStoppedEvent(this);
            Bukkit.getPluginManager().callEvent(event);
        }
    }
}
