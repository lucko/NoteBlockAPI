package com.xxmicloxx.noteblockapi.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xxmicloxx.noteblockapi.SongPlayer;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@AllArgsConstructor
public class SongStoppedEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private SongPlayer songPlayer;

    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
