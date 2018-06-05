package com.sasoftwares.epicteams.listeners;

import com.sasoftwares.epicteams.managers.ChatManager;
import com.sasoftwares.epicteams.managers.TeamFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class FishingRodListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getCaught() instanceof Player) {
            Player target = (Player) event.getCaught();
            if (TeamFactory.i.isSameTeam(event.getPlayer().getName(), target.getName())) {
                event.setCancelled(true);
                ChatManager.i.sendLanguageMessage(event.getPlayer(), "messages.team-damage-message");
            }
        }
    }

}
