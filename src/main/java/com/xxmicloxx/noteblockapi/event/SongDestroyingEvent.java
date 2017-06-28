package com.xxmicloxx.noteblockapi.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.xxmicloxx.noteblockapi.SongPlayer;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@RequiredArgsConstructor
public class SongDestroyingEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final SongPlayer songPlayer;

    @Setter
    private boolean cancelled = false;

    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
