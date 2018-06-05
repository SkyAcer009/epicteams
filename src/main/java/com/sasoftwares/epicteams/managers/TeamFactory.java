package com.sasoftwares.epicteams.managers;

import com.sasoftwares.epicteams.Team;
import com.sasoftwares.epicteams.io.TeamSerializer;
import com.sasoftwares.epicteams.timers.InviteTimer;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TeamFactory {

    public static TeamFactory i = new TeamFactory();
    private ArrayList<Team> teams = new ArrayList<>();

    private TeamFactory() {
    }

    public Team getTeamByName(String name) {
        return this.teams.stream().filter(t -> t.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Team getTeamByOwner(String owner) {
        return this.teams.stream().filter(t -> t.getOwner().getName().equalsIgnoreCase(owner)).findFirst().orElse(null);
    }

    public Team getTeamByPlayer(String name) {
        return this.teams.stream().filter(t -> t.hasPlayer(name)).findFirst().orElse(null);
    }

    public void createTeam(String owner, String name) {
        if (InviteTimer.doesEntityExist(owner)) {
            InviteTimer.invitedPlayers.remove(InviteTimer.returnPData(owner));
        }
        Team team = new Team(owner, name);
        this.teams.add(team);
        TeamSerializer.i.serializeTeam(team);
    }

    public void deleteTeam(Team team) {
        String removePlayer = "";
        for (String pData : InviteTimer.invitedPlayers) {
            String[] args = pData.split(":");
            if (args[2].equals(team.getName())) {
                removePlayer = pData;
            }
        }
        if (!removePlayer.equals("")) {
            InviteTimer.invitedPlayers.remove(removePlayer);
            if (InviteTimer.invitedPlayers.isEmpty()) {
                InviteTimer.inviteTimer.terminate();
            }
        }
        team.clearPlayers();
        this.teams.remove(team);
        TeamSerializer.i.deleteSerializedFile(team.getName());
    }

    public boolean isSameTeam(String player1, String player2) {
        return getTeamByPlayer(player1) == getTeamByPlayer(player2);
    }

    public boolean alreadyExists(String name) {
        return this.teams.stream().anyMatch(t -> t.getName().equalsIgnoreCase(name));
    }

    public boolean isOwner(String name) {
        return this.teams.stream().anyMatch(t -> t.getOwner().getName().equalsIgnoreCase(name) && t.hasPlayer(name));
    }

    public boolean isMember(String name) {
        return this.teams.stream().anyMatch(t -> t.hasPlayer(name) && !t.getOwner().getName().equalsIgnoreCase(name));
    }

    public boolean isExceedingMaxChars(String name) {
        return name.length() > FileManager.i.getConfig().getInt("max-team-name-characters");
    }

    public boolean isLessMinChars(String name) {
        return name.length() < FileManager.i.getConfig().getInt("min-team-name-characters");
    }

    public boolean isSwear(String name, List<String> list) {
        return list.stream().anyMatch(x -> x.equalsIgnoreCase(name));
    }

    public boolean isSwearingEnabled() {
        return FileManager.i.getConfig().getBoolean("enable-team-name-swear");
    }

    public int getTotalTeams() {
        return this.teams.size();
    }

}
