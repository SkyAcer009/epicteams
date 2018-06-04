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

    public Team(String owner, String name) {
        this.owner = owner;
        this.name = name;
        this.players = new ArrayList<>();
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
        if (TeamSerializer.i.databaseEnabled()) {
            TeamSerializer.i.serializeTeam(this);
        }
        if (InviteTimer.doesEntityExist(name)) {
            InviteTimer.invitedPlayers.remove(InviteTimer.returnPData(name));
        }
    }

    public void removePlayer(String name) {
        this.players.removeIf(t -> t.contains(name));
        if (TeamSerializer.i.databaseEnabled()) {
            TeamSerializer.i.serializeTeam(this);
        }
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
        if (TeamSerializer.i.databaseEnabled()) {
            TeamSerializer.i.serializeTeam(this);
        }
    }

    public boolean isFull() {
        return this.players.size() == FileManager.i.getConfig().getInt("max-team-size");
    }

    public Player getOwner() {
        return Bukkit.getServer().getPlayer(this.owner);
    }

}
