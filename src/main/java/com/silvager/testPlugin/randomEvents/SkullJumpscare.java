package com.silvager.testPlugin.randomEvents;

import com.silvager.testPlugin.VeganHorror;
import com.silvager.testPlugin.longCurses.CraftingCreeperCurse;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;

public class SkullJumpscare implements Listener {
    static HashMap<Player, BlockDisplay> scarySkulls = new HashMap<>();
    private static SkullJumpscare instance;
    private static int numPlayersCurrentlyRunning = 0;
    public static SkullJumpscare getInstance() {
        if (SkullJumpscare.instance == null) instance = new SkullJumpscare();
        return SkullJumpscare.instance;
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!scarySkulls.containsKey(event.getPlayer())) return;
        if (event.getTo().getPitch() > -40f) return;
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_DEATH, 3f, 0.7f);
        BlockDisplay skull = scarySkulls.get(event.getPlayer());
        scarySkulls.remove(event.getPlayer());
        event.getPlayer().showTitle(Title.title(
                Component.text("Die, "+event.getPlayer().getName()+"!").color(NamedTextColor.RED),
                Component.text(""),
                Title.DEFAULT_TIMES
        ));
        VeganHorror.scheduler.runTaskLater(VeganHorror.getInstance(), () -> {
            event.getPlayer().getWorld().spawnEntity(event.getPlayer().getLocation(), EntityType.LIGHTNING_BOLT);
            skull.remove();
            numPlayersCurrentlyRunning--;
            // If there is nobody else seeing the skull, unregister the handlers
            if (numPlayersCurrentlyRunning <= 0) {
                HandlerList handlerList = event.getHandlers();
                handlerList.unregister(SkullJumpscare.getInstance());
            }
        }, 20);
    }
    public static void showScary(Player player) {
        CraftingCreeperCurse.addPlayerToCurse(player);

        numPlayersCurrentlyRunning++;
        // If this is the start of it running, register the handlers
        if (numPlayersCurrentlyRunning == 1) VeganHorror.registerListener(SkullJumpscare.getInstance());
        float headSize = 70;
        Location spawnLocation = player.getLocation().add(-headSize/2, 70, -headSize/4).setRotation(0, 0);
        BlockDisplay display = player.getWorld().spawn(spawnLocation, BlockDisplay.class);
        display.setVisibleByDefault(false);
        BlockData skullBlockData = Bukkit.createBlockData(Material.SKELETON_SKULL ) ;
        display.setBlock(skullBlockData);
        Quaternionf lookDownRotation = new Quaternionf().rotateX((float) Math.toRadians(90));
        display.setTransformation(new Transformation(
                new Vector3f(0, 0, 0),
                lookDownRotation,
                new Vector3f(headSize, headSize, headSize),
                new Quaternionf()
        ));
        display.setViewRange(100f);
        player.showEntity(VeganHorror.getInstance(), display);
        scarySkulls.put(player, display);
        for (int i=0; i<3; i++) {
            player.playSound(player.getLocation(), Sound.AMBIENT_CAVE, 3f, 1f);
        }
    }
}
