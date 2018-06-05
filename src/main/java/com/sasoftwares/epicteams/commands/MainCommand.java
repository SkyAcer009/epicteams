package com.sasoftwares.epicteams.commands;

import com.sasoftwares.epicteams.Team;
import com.sasoftwares.epicteams.io.TeamSerializer;
import com.sasoftwares.epicteams.managers.ChatManager;
import com.sasoftwares.epicteams.managers.FileManager;
import com.sasoftwares.epicteams.managers.TeamFactory;
import com.sasoftwares.epicteams.utils.CharacterFilter;
import com.sasoftwares.epicteams.utils.Page;
import com.sasoftwares.epicteams.utils.Paginator;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
public class MainCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length == 0) {
                if (player.hasPermission("epicteams.admin")) {
                    ChatManager.i.sendStringListMessage(player, "help-list.admin");
                    return false;
                }
                ChatManager.i.sendStringListMessage(player, "help-list.member");
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("help")) {
                    if (player.hasPermission("epicteams.admin")) {
                        ChatManager.i.sendStringListMessage(player, "help-list.admin");
                        return false;
                    }
                    ChatManager.i.sendStringListMessage(player, "help-list.member");
                }
                if (args[0].equalsIgnoreCase("disband")) {
                    if (TeamFactory.i.isOwner(player.getName())) {
                        Team team = TeamFactory.i.getTeamByOwner(player.getName());
                        ChatManager.i.sendLanguageMessage(player, "messages.owner-disband-team");
                        ChatManager.i.broadcastTeamExceptOwner(team, "messages.owner-disband-team-broadcast");
                        TeamFactory.i.deleteTeam(team);
                        return false;
                    }
                    if (TeamFactory.i.isMember(player.getName())) {
                        ChatManager.i.sendLanguageMessage(player, "messages.member-disband-team-deny");
                        return false;
                    }
                    ChatManager.i.sendLanguageMessage(player, "messages.no-team-to-disband");
                }
                if (args[0].equalsIgnoreCase("leave") || args[0].equalsIgnoreCase("quit")) {
                    if (TeamFactory.i.isOwner(player.getName())) {
                        ChatManager.i.sendLanguageMessage(player, "messages.owner-leave-team-deny");
                        return false;
                    }
                    if (TeamFactory.i.isMember(player.getName())) {
                        Team team = TeamFactory.i.getTeamByPlayer(player.getName());
                        team.removePlayer(player.getName());
                        ChatManager.i.broadcastTeam(team, "messages.member-leave-team-broadcast", "%player%", player.getName());
                        ChatManager.i.sendLanguageMessage(player, "messages.member-leave-team", "%name%", team.getName());
                        return false;
                    }
                    ChatManager.i.sendLanguageMessage(player, "messages.no-team-to-leave");
                }
                if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rel")
                        || args[0].equalsIgnoreCase("rl")
                        || args[0].equalsIgnoreCase("r")) {
                    FileManager.i.loadConfig();
                    TeamSerializer.i.serializeTeams();
                    FileManager.i.loadLanguage();
                    ChatManager.i.sendLanguageMessage(player, "messages.reload-resources");
                }
                if (args[0].equalsIgnoreCase("info")) {
                    if (TeamFactory.i.isMember(player.getName()) || TeamFactory.i.isOwner(player.getName())) {
                        int finalCapacity = FileManager.i.getConfig().getInt("max-team-size");
                        Team team = TeamFactory.i.getTeamByPlayer(player.getName());
                        for (String s : FileManager.i.getLanguage().getStringList("messages.team-information-list")) {
                            player.sendMessage(FileManager.i.colorize(s
                                    .replaceAll("%name%", team.getName())
                                    .replaceAll("%owner%", team.getOwner().getName())
                                    .replaceAll("%capacity%", String.valueOf(team.getCurrentSize()) + "/" + finalCapacity)));
                        }
                        for (String s : team.getPlayers()) {
                            ChatManager.i.sendLanguageMessage(player, "messages.team-information-players-format", "%player%", s);
                        }
                        return false;
                    }
                    ChatManager.i.sendLanguageMessage(player, "messages.must-be-in-team-for-info");
                    return false;
                }
                if (args[0].equalsIgnoreCase("list")) {
                    if (TeamFactory.i.getTotalTeams() == 0) {
                        ChatManager.i.sendLanguageMessage(player, "messages.team-list-no-teams");
                        return false;
                    }
                    Paginator pages = new Paginator(TeamFactory.i.getTeams(), FileManager.i.getConfig().getInt("team-list-per-page"));
                    Page page = pages.getPage(1);
                    player.sendMessage(page.getHeader());
                    player.sendMessage(page.getContents());

                }
                if (args[0].equalsIgnoreCase("chat")
                        || args[0].equalsIgnoreCase("c")) {
                    if (FileManager.i.getConfig().getBoolean("team-chat-enabled")) {
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
                        ChatManager.i.sendLanguageMessage(player, "messages.team-chat-disabled");
                    }
                }
                if (args[0].equalsIgnoreCase("create")
                        || args[0].equalsIgnoreCase("kick")
                        || args[0].equalsIgnoreCase("join")
                        || args[0].equalsIgnoreCase("invite")
                        || args[0].equalsIgnoreCase("delete")
                        || args[0].equalsIgnoreCase("mute")
                        || args[0].equalsIgnoreCase("unmute")) {
                    ChatManager.i.sendLanguageMessage(player, "messages.missing-arguments", "%args%", args[0]);
                    return false;
                }
                if (args[0].equalsIgnoreCase("help")
                        || args[0].equalsIgnoreCase("info")
                        || args[0].equalsIgnoreCase("list")
                        || args[0].equalsIgnoreCase("leave")
                        || args[0].equalsIgnoreCase("disband")
                        || args[0].equalsIgnoreCase("quit")
                        || args[0].equalsIgnoreCase("reload")
                        || args[0].equalsIgnoreCase("rel")
                        || args[0].equalsIgnoreCase("rl")
                        || args[0].equalsIgnoreCase("r")
                        || args[0].equalsIgnoreCase("chat")
                        || args[0].equalsIgnoreCase("c")) {
                    return false;
                } else {
                    ChatManager.i.sendLanguageMessage(player, "messages.invalid-argument");
                }
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("mute")) {
                    if (FileManager.i.getConfig().getBoolean("team-chat-enabled")) {
                        if (TeamFactory.i.isMember(player.getName())) {
                            ChatManager.i.sendLanguageMessage(player, "messages.team-mute-member-deny");
                            return false;
                        }
                        if (TeamFactory.i.isOwner(player.getName())) {
                            Team team = TeamFactory.i.getTeamByOwner(player.getName());
                            Player target = Bukkit.getServer().getPlayer(args[1]);
                            if (target != null) {
                                if (args[1].equalsIgnoreCase(player.getName())) {
                                    ChatManager.i.sendLanguageMessage(player, "messages.owner-mute-self-deny");
                                    return false;
                                }
                                if (TeamFactory.i.isSameTeam(player.getName(), target.getName())) {
                                    if (team.isMuted(target.getName())) {
                                        ChatManager.i.sendLanguageMessage(player, "messages.team-mute-already-muted", "%player%", target.getName());
                                        return false;
                                    }
                                    team.setMuted(target.getName(), true);
                                    ChatManager.i.sendLanguageMessage(player, "messages.owner-mute-success", "%player%", target.getName());
                                    ChatManager.i.sendLanguageMessage(target, "messages.owner-mute-message");
                                    return false;
                                }
                                ChatManager.i.sendLanguageMessage(player, "messages.can-only-mute-same-team");
                                return false;
                            }
                            OfflinePlayer offlineTarget = Bukkit.getServer().getOfflinePlayer(args[1]);
                            if (offlineTarget.hasPlayedBefore()) {
                                if (TeamFactory.i.isSameTeam(offlineTarget.getName(), player.getName())) {
                                    if (team.isMuted(offlineTarget.getName())) {
                                        ChatManager.i.sendLanguageMessage(player, "messages.team-mute-already-muted", "%player%", offlineTarget.getName());
                                        return false;
                                    }
                                    team.setMuted(offlineTarget.getName(), true);
                                    ChatManager.i.sendLanguageMessage(player, "messages.owner-mute-success", "%player%", offlineTarget.getName());
                                    return false;
                                }
                                ChatManager.i.sendLanguageMessage(player, "messages.can-only-mute-same-team");
                                return false;
                            }
                            ChatManager.i.sendLanguageMessage(player, "messages.team-mute-never-joined-before", "%args%", args[1]);
                            return false;
                        }
                        ChatManager.i.sendLanguageMessage(player, "messages.team-mute-member-deny");
                        return false;
                    }
                    ChatManager.i.sendLanguageMessage(player, "messages.team-chat-disabled");
                }
                if (args[0].equalsIgnoreCase("unmute")) {
                    if (FileManager.i.getConfig().getBoolean("team-chat-enabled")) {
                        if (TeamFactory.i.isMember(player.getName())) {
                            ChatManager.i.sendLanguageMessage(player, "messages.team-unmute-member-deny");
                            return false;
                        }
                        if (TeamFactory.i.isOwner(player.getName())) {
                            Team team = TeamFactory.i.getTeamByOwner(player.getName());
                            Player target = Bukkit.getServer().getPlayer(args[1]);
                            if (target != null) {
                                if (args[1].equalsIgnoreCase(player.getName())) {
                                    ChatManager.i.sendLanguageMessage(player, "messages.owner-unmute-self-deny");
                                    return false;
                                }
                                if (TeamFactory.i.isSameTeam(player.getName(), target.getName())) {
                                    if (!team.isMuted(target.getName())) {
                                        ChatManager.i.sendLanguageMessage(player, "messages.team-unmute-not-muted", "%player%", target.getName());
                                        return false;
                                    }
                                    team.setMuted(target.getName(), false);
                                    ChatManager.i.sendLanguageMessage(player, "messages.owner-unmute-success", "%player%", target.getName());
                                    ChatManager.i.sendLanguageMessage(target, "messages.owner-unmute-message");
                                    return false;
                                }
                                ChatManager.i.sendLanguageMessage(player, "messages.can-only-unmute-same-team");
                                return false;
                            }
                            OfflinePlayer offlineTarget = Bukkit.getServer().getOfflinePlayer(args[1]);
                            if (offlineTarget.hasPlayedBefore()) {
                                if (TeamFactory.i.isSameTeam(offlineTarget.getName(), player.getName())) {
                                    if (!team.isMuted(offlineTarget.getName())) {
                                        ChatManager.i.sendLanguageMessage(player, "messages.team-unmute-not-muted", "%player%", offlineTarget.getName());
                                        return false;
                                    }
                                    team.setMuted(offlineTarget.getName(), true);
                                    ChatManager.i.sendLanguageMessage(player, "messages.owner-unmute-success", "%player%", offlineTarget.getName());
                                    return false;
                                }
                                ChatManager.i.sendLanguageMessage(player, "messages.can-only-unmute-same-team");
                                return false;
                            }
                            ChatManager.i.sendLanguageMessage(player, "messages.team-unmute-never-joined-before", "%args%", args[1]);
                            return false;
                        }
                        ChatManager.i.sendLanguageMessage(player, "messages.team-unmute-member-deny");
                        return false;
                    }
                    ChatManager.i.sendLanguageMessage(player, "messages.team-chat-disabled");
                }
                if (args[0].equalsIgnoreCase("list")) {
                    try {
                        int pageNum = Integer.parseInt(args[1]);
                        Paginator pages = new Paginator(TeamFactory.i.getTeams(), FileManager.i.getConfig().getInt("team-list-per-page"));
                        if (TeamFactory.i.getTotalTeams() == 0) {
                            ChatManager.i.sendLanguageMessage(player, "messages.team-list-no-teams");
                            return false;
                        }
                        if (pageNum <= 0) {
                            ChatManager.i.sendLanguageMessage(player, "messages.invalid-number");
                            return false;
                        }
                        if (pageNum > (pages.getMaxPages() + 1)) {
                            ChatManager.i.sendLanguageMessage(player, "messages.max-pages-exceed");
                            return false;
                        }
                        Page page = pages.getPage(pageNum);
                        player.sendMessage(page.getHeader());
                        player.sendMessage(page.getContents());
                    } catch (NumberFormatException exception) {
                        ChatManager.i.sendLanguageMessage(player, "messages.invalid-number");
                    }
                }
                if (args[0].equalsIgnoreCase("kick")) {
                    if (TeamFactory.i.isMember(player.getName())) {
                        ChatManager.i.sendLanguageMessage(player, "messages.member-kick-team-player-deny");
                        return false;
                    }
                    if (TeamFactory.i.isOwner(player.getName())) {
                        Team team = TeamFactory.i.getTeamByOwner(player.getName());
                        Player target = Bukkit.getServer().getPlayer(args[1]);
                        if (args[1].equalsIgnoreCase(player.getName())) {
                            ChatManager.i.sendLanguageMessage(player, "messages.owner-kick-self-deny");
                            return false;
                        }
                        if (target != null) {
                            if (TeamFactory.i.isSameTeam(player.getName(), target.getName())) {
                                ChatManager.i.sendLanguageMessage(player, "messages.owner-kick-player", "%player%", target.getName());
                                ChatManager.i.sendLanguageMessage(target, "messages.kicked-player-message");
                                team.removePlayer(target.getName());
                                ChatManager.i.broadcastTeamExceptOwner(team, "messages.owner-kick-player-broadcast", "%player%", target.getName());
                                return false;
                            }
                            ChatManager.i.sendLanguageMessage(player, "messages.owner-kick-not-same-team", "%player%", target.getName());
                            return false;
                        }
                        OfflinePlayer offlineTarget = Bukkit.getServer().getOfflinePlayer(args[1]);
                        if (offlineTarget.hasPlayedBefore()) {
                            if (TeamFactory.i.isSameTeam(player.getName(), offlineTarget.getName())) {
                                team.removePlayer(offlineTarget.getName());
                                ChatManager.i.sendLanguageMessage(player, "messages.owner-kick-player", "%player%", offlineTarget.getName());
                                ChatManager.i.broadcastTeamExceptOwner(team, "messages.owner-kick-player-broadcast", "%player%", offlineTarget.getName());
                                return false;
                            }
                            ChatManager.i.sendLanguageMessage(player, "messages.owner-kick-not-same-team", "%player%", offlineTarget.getName());
                            return false;
                        }
                        ChatManager.i.sendLanguageMessage(player, "messages.owner-kick-never-joined-before", "%args%", args[1]);
                        return false;
                    }
                    ChatManager.i.sendLanguageMessage(player, "messages.no-team-to-kick-from");
                    return false;
                }
                if (args[0].equalsIgnoreCase("create")) {
                    if (TeamFactory.i.isMember(player.getName())) {
                        ChatManager.i.sendLanguageMessage(player, "messages.member-create-team-deny");
                        return false;
                    }
                    if (TeamFactory.i.isOwner(player.getName())) {
                        ChatManager.i.sendLanguageMessage(player, "messages.owner-create-team-deny");
                        return false;
                    }
                    if (TeamFactory.i.alreadyExists(args[1])) {
                        ChatManager.i.sendLanguageMessage(player, "messages.team-create-already-exists", "%args%", args[1]);
                        return false;
                    }
                    if (TeamFactory.i.isExceedingMaxChars(args[1])) {
                        ChatManager.i.sendLanguageMessage(player, "messages.max-team-name-chars-exceed",
                                "%maxchars%", String.valueOf(FileManager.i.getConfig().getInt("max-team-name-characters")));
                        return false;
                    }
                    if (TeamFactory.i.isLessMinChars(args[1])) {
                        ChatManager.i.sendLanguageMessage(player, "messages.min-team-name-less",
                                "%minchars%", String.valueOf(FileManager.i.getConfig().getInt("min-team-name-characters")));
                        return false;
                    }
                    if (CharacterFilter.hasSpecialCharacter(args[1])) {
                        ChatManager.i.sendLanguageMessage(player, "messages.team-create-no-special-characters");
                        return false;
                    }
                    if (CharacterFilter.hasInteger(args[1])) {
                        ChatManager.i.sendLanguageMessage(player, "messages.team-create-no-integers");
                        return false;
                    }
                    if (!TeamFactory.i.isSwearingEnabled()) {
                        if (TeamFactory.i.isSwear(args[1], FileManager.i.getRestricted().getStringList("restricted-words"))) {
                            ChatManager.i.sendLanguageMessage(player, "messages.team-create-no-swear");
                            return false;
                        }
                    }
                    TeamFactory.i.createTeam(player.getName(), args[1]);
                    ChatManager.i.sendLanguageMessage(player, "messages.create-team-success", "%name%", args[1]);
                    return false;
                }
                if (args[0].equalsIgnoreCase("delete")) {
                    if (player.hasPermission("epicteams.admin")) {
                        Team team = TeamFactory.i.getTeamByName(args[1]);
                        if (team != null) {
                            ChatManager.i.sendLanguageMessage(player, "messages.admin-delete-team", "%name%", team.getName());
                            ChatManager.i.sendTeamOwnerMessage(team, "messages.admin-delete-team-owner-message");
                            ChatManager.i.broadcastTeamExceptOwner(team, "messages.admin-delete-team-broadcast");
                            TeamFactory.i.deleteTeam(team);
                            return false;
                        }
                        ChatManager.i.sendLanguageMessage(player, "messages.admin-delete-team-not-exist", "%args%", args[1]);
                        return false;
                    }
                    ChatManager.i.sendLanguageMessage(player, "messages.no-permission");
                    return false;
                }
                if (args[0].equalsIgnoreCase("info")) {
                    Team team = TeamFactory.i.getTeamByName(args[1]);
                    if (team != null) {
                        int finalCapacity = FileManager.i.getConfig().getInt("max-team-size");
                        for (String s : FileManager.i.getLanguage().getStringList("messages.team-information-list")) {
                            player.sendMessage(FileManager.i.colorize(s
                                    .replaceAll("%name%", team.getName())
                                    .replaceAll("%owner%", team.getOwner().getName())
                                    .replaceAll("%capacity%", String.valueOf(team.getCurrentSize()) + "/" + finalCapacity)));
                        }
                        for (String s : team.getPlayers()) {
                            ChatManager.i.sendLanguageMessage(player, "messages.team-information-players-format", "%player%", s);
                        }
                        return false;
                    }
                    ChatManager.i.sendLanguageMessage(player, "messages.team-info-not-exist", "%args%", args[1]);
                    return false;
                }
                if (args[0].equalsIgnoreCase("invite")) {
                    if (TeamFactory.i.isMember(player.getName())) {
                        ChatManager.i.sendLanguageMessage(player, "messages.member-invite-player-deny");
                        return false;
                    }
                    if (TeamFactory.i.isOwner(player.getName())) {
                        if (args[1].equalsIgnoreCase(player.getName())) {
                            ChatManager.i.sendLanguageMessage(player, "messages.owner-invite-self-deny");
                            return false;
                        }
                        Player target = Bukkit.getServer().getPlayer(args[1]);
                        Team team = TeamFactory.i.getTeamByOwner(player.getName());
                        if (target != null) {
                            if (team.isFull()) {
                                ChatManager.i.sendLanguageMessage(player, "messages.owner-invite-team-full");
                                return false;
                            }
                            if (TeamFactory.i.isMember(target.getName())) {
                                ChatManager.i.sendLanguageMessage(player, "messages.owner-invite-player-already-in-team",
                                        "%player%", target.getName());
                                return false;
                            }
                            if (TeamFactory.i.isOwner(target.getName())) {
                                ChatManager.i.sendLanguageMessage(player, "messages.owner-invite-player-already-own-team",
                                        "%player%", target.getName());
                                return false;
                            }
                            if (team.isInvited(target.getName())) {
                                ChatManager.i.sendLanguageMessage(player, "messages.owner-invite-already-has-invite",
                                        "%player%", target.getName());
                                return false;
                            }
                            team.invite(target.getName());
                            ChatManager.i.sendLanguageMessage(player, "messages.owner-invite-success", "%player%", target.getName());
                            ChatManager.i.sendStringListMessage(target, "messages.invite-message",
                                    "%inviter%", "%name%", "%timeout%",
                                    player.getName(), team.getName(), String.valueOf(FileManager.i.getConfig().getInt("invite-timeout")));
                            return false;
                        }
                        ChatManager.i.sendLanguageMessage(player, "messages.owner-invite-player-not-online", "%args%", args[1]);
                        return false;
                    }
                    ChatManager.i.sendLanguageMessage(player, "messages.no-team-to-invite");
                    return false;
                }
                if (args[0].equalsIgnoreCase("join")) {
                    if (TeamFactory.i.isMember(player.getName())) {
                        ChatManager.i.sendLanguageMessage(player, "messages.team-join-already-in-team");
                        return false;
                    }
                    if (TeamFactory.i.isOwner(player.getName())) {
                        ChatManager.i.sendLanguageMessage(player, "messages.team-join-already-own-team");
                        return false;
                    }
                    Team team = TeamFactory.i.getTeamByName(args[1]);
                    if (team != null) {
                        if (team.isInvited(player.getName())) {
                            if (team.isFull()) {
                                ChatManager.i.sendLanguageMessage(player, "messages.team-join-full-deny");
                                return false;
                            }
                            ChatManager.i.broadcastTeam(team, "messages.player-join-broadcast", "%player%", player.getName());
                            team.addPlayer(player.getName());
                            ChatManager.i.sendLanguageMessage(player, "messages.team-join-success", "%name%", team.getName());
                            return false;
                        }
                        ChatManager.i.sendLanguageMessage(player, "messages.team-join-no-invite-deny");
                        return false;
                    }
                    ChatManager.i.sendLanguageMessage(player, "messages.team-join-does-not-exist", "%args%", args[1]);
                    return false;
                }
                if (args[0].equalsIgnoreCase("leave")
                        || args[0].equalsIgnoreCase("quit")
                        || args[0].equalsIgnoreCase("disband")
                        || args[0].equalsIgnoreCase("reload")
                        || args[0].equalsIgnoreCase("rel")
                        || args[0].equalsIgnoreCase("rl")
                        || args[0].equalsIgnoreCase("r")
                        || args[0].equalsIgnoreCase("help")
                        || args[0].equalsIgnoreCase("chat")
                        || args[0].equalsIgnoreCase("c")) {
                    ChatManager.i.sendLanguageMessage(player, "messages.exceeding-arguments", "%args%", args[0]);
                } else {
                    ChatManager.i.sendLanguageMessage(player, "messages.invalid-argument");
                }
            }
            if (args.length > 2) {
                if (args[0].equalsIgnoreCase("help")
                        || args[0].equalsIgnoreCase("list")
                        || args[0].equalsIgnoreCase("info")
                        || args[0].equalsIgnoreCase("create")
                        || args[0].equalsIgnoreCase("disband")
                        || args[0].equalsIgnoreCase("delete")
                        || args[0].equalsIgnoreCase("quit")
                        || args[0].equalsIgnoreCase("leave")
                        || args[0].equalsIgnoreCase("reload")
                        || args[0].equalsIgnoreCase("rel")
                        || args[0].equalsIgnoreCase("rl")
                        || args[0].equalsIgnoreCase("r")
                        || args[0].equalsIgnoreCase("kick")
                        || args[0].equalsIgnoreCase("invite")
                        || args[0].equalsIgnoreCase("join")
                        || args[0].equalsIgnoreCase("chat")
                        || args[0].equalsIgnoreCase("c")
                        || args[0].equalsIgnoreCase("mute")
                        || args[0].equalsIgnoreCase("unmute")) {
                    ChatManager.i.sendLanguageMessage(player, "messages.exceeding-arguments", "%args%", args[0]);
                } else {
                    ChatManager.i.sendLanguageMessage(player, "messages.invalid-argument");
                }
            }
        }
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")
                        || args[0].equalsIgnoreCase("rel")
                        || args[0].equalsIgnoreCase("rl")
                        || args[0].equalsIgnoreCase("r")) {
                    FileManager.i.loadConfig();
                    TeamSerializer.i.serializeTeams();
                    FileManager.i.loadLanguage();
                    sender.sendMessage(FileManager.i.colorize(FileManager.i.getLanguage().getString("messages.reload-resources-console")));
                    return false;
                }
            }
            sender.sendMessage("This command cannot be executed by console!");
        }
        return false;
    }

}
