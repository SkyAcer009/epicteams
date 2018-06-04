package com.sasoftwares.epicteams.timers;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class InviteTimer {

    public static ArrayList<String> invitedPlayers = new ArrayList<>();
    public static Timer inviteTimer = new Timer();

    public static void start() {
        inviteTimer = new Timer(1, 0, TimeUnit.SECONDS, new TaskTimer() {
            public void run() {
                if (invitedPlayers != null && !invitedPlayers.isEmpty() && inviteTimer != null) {
                    for (String pData : invitedPlayers) {
                        String[] args = pData.split(":");
                        try {
                            int time = Integer.parseInt(args[1]);
                            time--;
                            if (time == 0) {
                                invitedPlayers.remove(pData);
                                if (invitedPlayers.isEmpty()) {
                                    inviteTimer.terminate();
                                }
                                return;
                            }
                            invitedPlayers.remove(pData);
                            invitedPlayers.add(args[0] + ":" + time + ":" + args[2]);
                        } catch (NumberFormatException nfe) {
                            Bukkit.getServer().getLogger().severe(nfe.toString());
                        }
                    }
                }
            }
        }).start();
    }

    public static boolean doesEntityExist(String name) {
        for (String s : InviteTimer.invitedPlayers) {
            String[] args = s.split(":");
            if (args[0].equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static String returnPData(String name) {
        for (String s : InviteTimer.invitedPlayers) {
            String[] args = s.split(":");
            if (args[0].equals(name)) {
                return s;
            }
        }
        return "ERROR";
    }

}
