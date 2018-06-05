package com.sasoftwares.epicteams.managers;

import com.sasoftwares.epicteams.Team;
import org.bukkit.entity.Player;

public class ChatManager {

    public static ChatManager i = new ChatManager();

    private ChatManager() {
    }

    public void sendLanguageMessage(Player player, String path) {
        player.sendMessage(FileManager.i.colorize(FileManager.i.getLanguage().getString(path)));
    }

    public void sendLanguageMessage(Player player, String path, String regex, String replacement) {
        player.sendMessage(FileManager.i.colorize(FileManager.i.getLanguage().getString(path)
                .replaceAll(regex, replacement)));
    }

    public void sendStringListMessage(Player player, String path) {
        FileManager.i.getLanguage().getStringList(path).forEach(s -> player.sendMessage(FileManager.i.colorize(s)));
    }

    public void sendStringListMessage(Player player, String path,
                                      String regex1,
                                      String regex2,
                                      String regex3,
                                      String replacement1,
                                      String replacement2,
                                      String replacement3) {
        FileManager.i.getLanguage().getStringList(path).forEach(s -> player.sendMessage(FileManager.i.colorize(s
                .replaceAll(regex1, replacement1)
                .replaceAll(regex2, replacement2)
                .replaceAll(regex3, replacement3))));
    }

    public void broadcastTeam(Team team, String path, String regex, String replacement) {
        team.broadcast(FileManager.i.colorize(FileManager.i.getLanguage().getString(path)
                .replaceAll(regex, replacement)));
    }

    public void broadcastTeamExceptOwner(Team team, String path) {
        team.broadcastExceptOwner(FileManager.i.colorize(FileManager.i.getLanguage().getString(path)));
    }

    public void broadcastTeamExceptOwner(Team team, String path, String regex, String replacement) {
        team.broadcastExceptOwner(FileManager.i.colorize(FileManager.i.getLanguage().getString(path)
                .replace(regex, replacement)));
    }

    public void sendTeamOwnerMessage(Team team, String path) {
        team.getOwner().sendMessage(FileManager.i.colorize(FileManager.i.getLanguage().getString(path)));
    }
}
