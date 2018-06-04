package com.sasoftwares.epicteams.timers;

import com.sasoftwares.epicteams.EpicTeams;
import com.sasoftwares.epicteams.io.TeamSerializer;
import com.sasoftwares.epicteams.managers.FileManager;
import org.bukkit.scheduler.BukkitRunnable;

public class DataUpdater {

    private DataUpdater() {
    }

    public static void run() {
        if (TeamSerializer.i.databaseEnabled() && TeamSerializer.i.isDataUpdaterEnabled()) {
            new BukkitRunnable() {
                public void run() {
                    if (FileManager.i.getConfig().getInt("data-sync-interval") == 0
                            || FileManager.i.getConfig().getInt("data-sync-interval") == -1) {
                        cancel();
                        return;
                    }
                    TeamSerializer.i.serializeTeams();
                }
            }.runTaskTimerAsynchronously(EpicTeams.getPlugin(EpicTeams.class), 0, FileManager.i.getConfig().getInt("data-sync-interval") * 20);
        }
    }

}
