package com.sasoftwares.epicteams;

import com.sasoftwares.epicteams.command.MainCommand;
import com.sasoftwares.epicteams.io.TeamSerializer;
import com.sasoftwares.epicteams.listeners.DamageListener;
import com.sasoftwares.epicteams.listeners.KickListener;
import com.sasoftwares.epicteams.listeners.QuitListener;
import com.sasoftwares.epicteams.managers.FileManager;
import com.sasoftwares.epicteams.metrics.Metrics;
import com.sasoftwares.epicteams.timers.DataUpdater;
import com.sasoftwares.epicteams.timers.InviteTimer;
import org.bukkit.plugin.java.JavaPlugin;

public class EpicTeams extends JavaPlugin {
    public void onEnable() {
        FileManager.i.setupConfig(this);
        FileManager.i.setupDatabase(this);
        FileManager.i.setupLanguage(this);
        if (TeamSerializer.i.databaseEnabled()) {
            TeamSerializer.i.deserializeTeams();
        }
        DataUpdater.run();
        getCommand("teams").setExecutor(new MainCommand());
        getServer().getPluginManager().registerEvents(new DamageListener(), this);
        getServer().getPluginManager().registerEvents(new KickListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
        if (FileManager.i.getConfig().getBoolean("use-metrics")) {
            Metrics metrics = new Metrics(this);
            metrics.getPluginData();
        }
    }

    public void onDisable() {
        if (TeamSerializer.i.databaseEnabled()) {
            TeamSerializer.i.serializeTeams();
        }
        if (InviteTimer.inviteTimer.isRunning()) {
            InviteTimer.inviteTimer.terminate();
        }
    }
}
