package com.silvager.testPlugin;

import io.papermc.paper.event.entity.EntityPushedByEntityAttackEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;

public final class VeganHorror extends JavaPlugin implements Listener {
    private static VeganHorror instance;
    public static BukkitScheduler scheduler;
    public static Random random = new Random();
    public void onEnable() {
        // Plugin startup logic
        VeganHorror.instance = this;
        scheduler = this.getServer().getScheduler();
        VeganHorror.registerListener(this);
        PluginCommands.registerCommands();
    }
    public static void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, VeganHorror.getInstance());
    }
    @EventHandler
    public void onKnockback(EntityPushedByEntityAttackEvent event) {
        event.setKnockback(event.getKnockback().multiply(-1));
    }
    @EventHandler 
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().displayName(Component.text("Mr. Sussy"));

    }
//    @EventHandler
//    public void onPlayerMove(PlayerMoveEvent event) {
//    if (event.hasChangedBlock()) {
//        if (event.getFrom().getBlockX() != event.getTo().getBlockX()) {
//            event.getPlayer().playSound(event.getPlayer(), Sound.ENTITY_GHAST_HURT, 1f, 1f);
//            event.getPlayer().damage(1f);
//            if (event.getPlayer().getHealth() < 1f) {
//                event.getFrom().createExplosion(2f);
//            }
//            event.setCancelled(true);
//        }
//    }
//    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public static VeganHorror getInstance() {
        return VeganHorror.instance;
    }
}
