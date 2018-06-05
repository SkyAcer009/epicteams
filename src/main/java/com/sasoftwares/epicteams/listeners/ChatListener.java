package com.sasoftwares.epicteams.listeners;

import com.sasoftwares.epicteams.Team;
import com.sasoftwares.epicteams.managers.ChatManager;
import com.sasoftwares.epicteams.managers.FileManager;
import com.sasoftwares.epicteams.managers.TeamFactory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event) {
        try {
            if (TeamFactory.i.isMember(event.getPlayer().getName())) {
                Team team = TeamFactory.i.getTeamByPlayer(event.getPlayer().getName());
                if (team.isTeamChat(event.getPlayer().getName())) {
                    if (team.isMuted(event.getPlayer().getName())) {
                        ChatManager.i.sendLanguageMessage(event.getPlayer(), "messages.muted-message");
                        event.setCancelled(true);
                        return;
                    }
                    event.setFormat(FileManager.i.colorize(FileManager.i.getLanguage().getString("messages.team-chat-member-format")
                            .replaceAll("%name%", event.getPlayer().getName())
                            .replaceAll("%message%", event.getMessage())));
                    event.setMessage(event.getFormat());
                    event.setCancelled(true);
                    team.broadcast(event.getMessage());
                }
            }
            if (TeamFactory.i.isOwner(event.getPlayer().getName())) {
                Team team = TeamFactory.i.getTeamByOwner(event.getPlayer().getName());
                if (team.isTeamChat(event.getPlayer().getName())) {
                    event.setFormat(FileManager.i.colorize(FileManager.i.getLanguage().getString("messages.team-chat-owner-format")
                            .replaceAll("%name%", event.getPlayer().getName())
                            .replaceAll("%message%", event.getMessage())));
                    event.setMessage(event.getFormat());
                    event.setCancelled(true);
                    team.broadcast(event.getMessage());
                }
            }
        } catch (Exception exception) {
            //TODO - FIX CONVERSION EXCEPTION WHEN $ % \ SYMBOLS ARE PARSED
            event.setCancelled(true);
        }
    }

}
