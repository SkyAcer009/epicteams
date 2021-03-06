package com.sasoftwares.epicteams.listeners;

import com.sasoftwares.epicteams.Team;
import com.sasoftwares.epicteams.io.TeamSerializer;
import com.sasoftwares.epicteams.managers.ChatManager;
import com.sasoftwares.epicteams.managers.TeamFactory;
import com.sasoftwares.epicteams.timers.InviteTimer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        if (!TeamSerializer.i.databaseEnabled()) {
            if (InviteTimer.doesEntityExist(event.getPlayer().getName())) {
                InviteTimer.invitedPlayers.remove(InviteTimer.returnPData(event.getPlayer().getName()));
            }
            if (TeamFactory.i.isMember(event.getPlayer().getName())) {
                Team team = TeamFactory.i.getTeamByPlayer(event.getPlayer().getName());
                team.removePlayer(event.getPlayer().getName());
                return;
            }
            if (TeamFactory.i.isOwner(event.getPlayer().getName())) {
                Team team = TeamFactory.i.getTeamByOwner(event.getPlayer().getName());
                ChatManager.i.broadcastTeamExceptOwner(team, "messages.owner-leave-game-broadcast");
                TeamFactory.i.deleteTeam(team);
            }
        } else {
            if (InviteTimer.doesEntityExist(event.getPlayer().getName())) {
                InviteTimer.invitedPlayers.remove(InviteTimer.returnPData(event.getPlayer().getName()));
            }
        }
    }
}
