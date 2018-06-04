package com.sasoftwares.epicteams.io;

import com.sasoftwares.epicteams.Team;
import com.sasoftwares.epicteams.managers.FileManager;
import com.sasoftwares.epicteams.managers.TeamFactory;

import java.io.*;

public class TeamSerializer {

    public static TeamSerializer i = new TeamSerializer();

    private TeamSerializer() {
    }

    public void serializeTeams() {
        TeamFactory.i.getTeams().forEach(t -> {
            try {
                File[] files = new File(FileManager.i.getDatabaseFolder().getPath()).listFiles();
                if (files != null) {
                    FileOutputStream fos = new FileOutputStream(new File(FileManager.i.getDatabaseFolder(), t.getName() + ".homo"));
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(t);
                    oos.close();
                    fos.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void deserializeTeams() {
        File[] files = new File(FileManager.i.getDatabaseFolder().getPath()).listFiles();
        try {
            if (files != null) {
                for (File file : files) {
                    FileInputStream fis = new FileInputStream(file);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    Team team = (Team) ois.readObject();
                    TeamFactory.i.getTeams().add(team);
                    ois.close();
                    fis.close();
                }
            }
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    public void serializeTeam(Team team) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(FileManager.i.getDatabaseFolder(), team.getName() + ".homo"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(team);
            oos.close();
            fos.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void deleteSerializedFile(String name) {
        File[] files = new File(FileManager.i.getDatabaseFolder().getPath()).listFiles();
        try {
            if (files != null) {
                for (File file : files) {
                    if (file.getName().equalsIgnoreCase(name + ".homo")) {
                        file.delete();
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    public boolean databaseEnabled() {
        return FileManager.i.getConfig().getBoolean("use-plugin-database");
    }

    public boolean isDataUpdaterEnabled() {
        return FileManager.i.getConfig().getBoolean("auto-database-sync");
    }
}
