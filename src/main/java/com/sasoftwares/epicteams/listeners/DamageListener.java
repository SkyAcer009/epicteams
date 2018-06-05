package com.sasoftwares.epicteams.listeners;

import com.sasoftwares.epicteams.managers.ChatManager;
import com.sasoftwares.epicteams.managers.TeamFactory;
import org.bukkit.entity.*;
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
        if ((event.getDamager() instanceof Snowball) && (event.getEntity() instanceof Player)) {
            Snowball snowball = (Snowball) event.getDamager();
            if (snowball.getShooter() instanceof Player) {
                Player player = (Player) snowball.getShooter();
                if (TeamFactory.i.isSameTeam(player.getName(), event.getEntity().getName())) {
                    event.setCancelled(true);
                    ChatManager.i.sendLanguageMessage(player, "messages.team-damage-message");
                }
            }
        }
        if ((event.getDamager() instanceof Egg) && (event.getEntity() instanceof Player)) {
            Egg egg = (Egg) event.getDamager();
            if (egg.getShooter() instanceof Player) {
                Player player = (Player) egg.getShooter();
                if (TeamFactory.i.isSameTeam(player.getName(), event.getEntity().getName())) {
                    event.setCancelled(true);
                    ChatManager.i.sendLanguageMessage(player, "messages.team-damage-message");
                }
            }
        }
        if ((event.getDamager() instanceof Fireball) && (event.getEntity() instanceof Player)) {
            Fireball fireball = (Fireball) event.getDamager();
            if (fireball.getShooter() instanceof Player) {
                Player player = (Player) fireball.getShooter();
                if (TeamFactory.i.isSameTeam(player.getName(), event.getEntity().getName())) {
                    event.setCancelled(true);
                    ChatManager.i.sendLanguageMessage(player, "messages.team-damage-message");
                }
            }
        }
        if ((event.getDamager() instanceof SmallFireball) && (event.getEntity() instanceof Player)) {
            SmallFireball smallFireball = (SmallFireball) event.getDamager();
            if (smallFireball.getShooter() instanceof Player) {
                Player player = (Player) smallFireball.getShooter();
                if (TeamFactory.i.isSameTeam(player.getName(), event.getEntity().getName())) {
                    event.setCancelled(true);
                    ChatManager.i.sendLanguageMessage(player, "messages.team-damage-message");
                }
            }
        }
    }
}
