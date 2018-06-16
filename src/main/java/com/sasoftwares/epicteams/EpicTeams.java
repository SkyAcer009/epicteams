package com.sasoftwares.epicteams;

import com.sasoftwares.epicteams.commands.MainCommand;
import com.sasoftwares.epicteams.commands.TeamChat;
import com.sasoftwares.epicteams.io.TeamSerializer;
import com.sasoftwares.epicteams.listeners.*;
import com.sasoftwares.epicteams.managers.FileManager;
import com.sasoftwares.epicteams.metrics.Metrics;
import com.sasoftwares.epicteams.timers.DataUpdater;
import com.sasoftwares.epicteams.timers.InviteTimer;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class EpicTeams extends JavaPlugin {
    public void onEnable() {
        FileManager.i.setupConfig(this);
        FileManager.i.setupDatabase(this);
        FileManager.i.setupLanguage(this);
        FileManager.i.setupRestricted(this);
        TeamSerializer.i.deserializeTeams();
        DataUpdater.run();
        getCommand("teams").setExecutor(new MainCommand());
        getCommand("teamchat").setExecutor(new TeamChat());
        getServer().getPluginManager().registerEvents(new DamageListener(), this);
        getServer().getPluginManager().registerEvents(new KickListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new FishingRodListener(), this);
        if (FileManager.i.getConfig().getBoolean("use-metrics")) {
            Metrics metrics = new Metrics(this);
            metrics.getPluginData();
        }
        getLogger().info(ChatColor.YELLOW + "EpicTeams v1.1 made by SkyAcer009 and MasterFox11");
        getLogger().info(ChatColor.YELLOW + "Please do not forget to rate the plugin on spigot if you enjoyed it!");
    }

    public void onDisable() {
        TeamSerializer.i.serializeTeams();
        if (InviteTimer.inviteTimer.isRunning()) {
            InviteTimer.inviteTimer.terminate();
        }
    }
}
