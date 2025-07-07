package com.silvager.testPlugin.longCurses;

import com.silvager.testPlugin.VeganHorror;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.silvager.testPlugin.VeganHorror.random;

public class CraftingCreeperCurse implements Listener {
    private static ArrayList<Player> cursedPlayers = new ArrayList<>();
    private static CraftingCreeperCurse instance;
    public static CraftingCreeperCurse getInstance() {
        if (instance == null) instance = new CraftingCreeperCurse();
        return instance;
    }
    public static void addPlayerToCurse(Player player) {
        if (cursedPlayers.isEmpty()) {
    VeganHorror.registerListener(CraftingCreeperCurse.getInstance());
        }
        if (!cursedPlayers.contains(player)) cursedPlayers.add(player);
    }


    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!cursedPlayers.contains((Player) event.getPlayer())) return;
        if (event.getInventory().getType() != InventoryType.WORKBENCH) return;
        Location pos = event.getPlayer().getLocation();
        Player player = (Player) event.getPlayer();
        @NotNull Vector vec = pos.getDirection();
        pos = pos.subtract(vec.multiply(2));
        if (random.nextInt(6) == 0) {
            player.getWorld().spawn(pos, Creeper.class);

        } else {
            player.playSound(pos, Sound.ENTITY_CREEPER_PRIMED, 1f, 0.5f);
        }

    }
}
