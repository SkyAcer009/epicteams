package com.sasoftwares.epicteams;

import com.sasoftwares.epicteams.io.TeamSerializer;
import com.sasoftwares.epicteams.managers.FileManager;
import com.sasoftwares.epicteams.timers.InviteTimer;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;

@Data
public class Team implements Serializable {

    private final String name;
    private final String owner;
    private final ArrayList<String> players;
    private final ArrayList<String> teamChatPlayers;
    private final ArrayList<String> mutedPlayers;

    public Team(String owner, String name) {
        this.owner = owner;
        this.name = name;
        this.players = new ArrayList<>();
        this.teamChatPlayers = new ArrayList<>();
        this.mutedPlayers = new ArrayList<>();
        this.players.add(owner);
    }

    public void invite(String name) {
        InviteTimer.invitedPlayers.add(name + ":" + FileManager.i.getConfig().getInt("invite-timeout") + ":" + this.name);
        if (!InviteTimer.inviteTimer.isRunning()) {
            InviteTimer.start();
        }
    }

    public boolean isInvited(String name) {
        return InviteTimer.doesEntityExist(name);
    }

    public void addPlayer(String name) {
        if (!this.players.contains(name)) this.players.add(name);
        TeamSerializer.i.serializeTeam(this);
        if (InviteTimer.doesEntityExist(name)) {
            InviteTimer.invitedPlayers.remove(InviteTimer.returnPData(name));
        }
    }

    public void removePlayer(String name) {
        this.players.removeIf(t -> t.contains(name));
        TeamSerializer.i.serializeTeam(this);
    }

    public boolean hasPlayer(String name) {
        return this.players.contains(name);
    }

    public int getCurrentSize() {
        return this.players.size();
    }

    public void broadcast(String msg) {
        this.players.forEach(s -> {
            if (this.players.contains(s)) {
                Player player = Bukkit.getServer().getPlayer(s);
                if (player != null) {
                    player.sendMessage(msg);
                }
            }
        });
    }

    public void broadcastExceptOwner(String msg) {
        this.players.forEach(s -> {
            if (this.players.contains(s)) {
                if (!this.owner.equalsIgnoreCase(s)) {
                    Player player = Bukkit.getServer().getPlayer(s);
                    if (player != null) {
                        player.sendMessage(msg);
                    }
                }
            }
        });
    }

    public void clearPlayers() {
        this.players.clear();
        TeamSerializer.i.serializeTeam(this);
    }

    public boolean isFull() {
        return this.players.size() == FileManager.i.getConfig().getInt("max-team-size");
    }

    public Player getOwner() {
        return Bukkit.getServer().getPlayer(this.owner);
    }

    public boolean isTeamChat(String name) {
        return this.teamChatPlayers.contains(name);
    }

    public void setTeamChat(String name, boolean b) {
        if (b) {
            if (!this.teamChatPlayers.contains(name)) this.teamChatPlayers.add(name);
            TeamSerializer.i.serializeTeam(this);
        } else {
            this.teamChatPlayers.removeIf(t -> t.contains(name));
            TeamSerializer.i.serializeTeam(this);
        }
    }

    public boolean isMuted(String name) {
        return this.mutedPlayers.contains(name);
    }

    public void setMuted(String name, boolean b) {
        if (b) {
            if (!this.mutedPlayers.contains(name)) this.mutedPlayers.add(name);
            TeamSerializer.i.serializeTeam(this);
        } else {
            this.mutedPlayers.removeIf(t -> t.contains(name));
            TeamSerializer.i.serializeTeam(this);
        }
    }
}
