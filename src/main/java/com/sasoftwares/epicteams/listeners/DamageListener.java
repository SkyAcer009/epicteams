package com.sasoftwares.epicteams.listeners;

import com.sasoftwares.epicteams.managers.ChatManager;
import com.sasoftwares.epicteams.managers.TeamFactory;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageByEntityEvent event) {
        if ((event.getDamager() instanceof Player) && (event.getEntity() instanceof Player)) {
            if (TeamFactory.i.isSameTeam(event.getDamager().getName(), event.getEntity().getName())) {
                event.setCancelled(true);
                ChatManager.i.sendLanguageMessage((Player) event.getDamager(), "messages.team-damage-message");
            }
            return;
        }
        if ((event.getDamager() instanceof Arrow) && (event.getEntity() instanceof Player)) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Player) {
                Player player = (Player) arrow.getShooter();
                if (TeamFactory.i.isSameTeam(player.getName(), event.getEntity().getName())) {
                    event.setCancelled(true);
                    ChatManager.i.sendLanguageMessage(player, "messages.team-damage-message");
                }
            }
        }
    }
}
