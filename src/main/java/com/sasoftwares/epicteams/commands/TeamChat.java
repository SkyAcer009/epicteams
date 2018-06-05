package com.sasoftwares.epicteams.commands;

import com.sasoftwares.epicteams.Team;
import com.sasoftwares.epicteams.managers.ChatManager;
import com.sasoftwares.epicteams.managers.FileManager;
import com.sasoftwares.epicteams.managers.TeamFactory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamChat implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command cannot be executed by console!");
            return false;
        }
        final Player player = (Player) sender;
        if (FileManager.i.getConfig().getBoolean("team-chat-enabled")) {
            if (args.length == 0) {
                if (TeamFactory.i.isMember(player.getName()) || TeamFactory.i.isOwner(player.getName())) {
                    Team team = TeamFactory.i.getTeamByPlayer(player.getName());
                    if (team.isTeamChat(player.getName())) {
                        team.setTeamChat(player.getName(), false);
                        ChatManager.i.sendLanguageMessage(player, "messages.team-chat-toggle-off");
                    } else {
                        team.setTeamChat(player.getName(), true);
                        ChatManager.i.sendLanguageMessage(player, "messages.team-chat-toggle-on");
                    }
                } else {
                    ChatManager.i.sendLanguageMessage(player, "messages.must-be-in-team-for-chat");
                }
            } else {
                ChatManager.i.sendLanguageMessage(player, "messages.team-chat-arguments-exceed", "%args%", "/" + command.getName());
            }
        } else {
            ChatManager.i.sendLanguageMessage(player, "messages.team-chat-disabled");
        }
        return false;
    }
}
