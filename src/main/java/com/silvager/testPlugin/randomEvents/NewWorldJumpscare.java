package com.silvager.testPlugin.randomEvents;

import com.silvager.testPlugin.CustomGen;
import com.silvager.testPlugin.VeganHorror;
import io.papermc.paper.entity.LookAnchor;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class NewWorldJumpscare {
    public static void triggerNewWorldJumpscare(Player player) {
        goToScaryWorld(player);
    }
    private static void goToScaryWorld(Player player) {
        Location origonalLocation = player.getLocation();
        World sigmaland = Bukkit.getWorld("sigmaland");
        if (sigmaland == null) {
            WorldCreator wc = new WorldCreator("sigmaland");
            wc.generator(new CustomGen());
            sigmaland = Bukkit.createWorld(wc);
        }
        sigmaland.setAutoSave(false);
        sigmaland.setViewDistance(2);
        sigmaland.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        for (int i=0; i<5; i++) {
            player.playSound(player.getLocation(), Sound.AMBIENT_CAVE, 2f, 1f);
        }
        PotionEffect darknessEffect = new PotionEffect(
                PotionEffectType.DARKNESS,
                450,
                4
        );
        ArrayList<LivingEntity> scaryEntities = new ArrayList<>();
        player.addPotionEffect(darknessEffect);
        World finalSigmaland = sigmaland;
        GameMode previousGameMode = player.getGameMode();
        VeganHorror.scheduler.runTaskLater(VeganHorror.getInstance(), () -> {
            player.teleport(finalSigmaland.getSpawnLocation());
            player.setGameMode(GameMode.ADVENTURE);
            player.playSound(player.getLocation(), Sound.MUSIC_DISC_11, 2f, 0.8f);
            for (int y=1; y<15; y+= 3) {
                for (double i = 0.0; i < 360.0; i += 20.0) {
                    Location spawnLocation = player.getLocation();
                    spawnLocation.setX(spawnLocation.getX() + (Math.cos(Math.toRadians(i)) * (y + 10)));
                    spawnLocation.setY(y);
                    spawnLocation.setZ(spawnLocation.getZ() + (Math.sin(Math.toRadians(i)) * (y + 10)));
                    LivingEntity newEntity = NewWorldJumpscare.getScaryAnimal(finalSigmaland, spawnLocation);
                    newEntity.lookAt(player.getLocation(), LookAnchor.EYES);
                    newEntity.setInvulnerable(true);
                    newEntity.setSilent(true);
                    newEntity.setAI(false);
                    scaryEntities.add(newEntity);
                }
            }


        }, 60);
        VeganHorror.scheduler.runTaskLater(VeganHorror.getInstance(), () -> {
            for (int i=0; i<5; i++) {
                player.playSound(player.getLocation(), Sound.AMBIENT_CAVE, 2f, 1f);
            }
        }, 300);
        VeganHorror.scheduler.runTaskLater(VeganHorror.getInstance(), () -> {
            player.teleport(origonalLocation);
            for (LivingEntity entity : scaryEntities) {
                entity.remove();
            }
            scaryEntities.clear();
            player.playSound(player.getLocation(),Sound.ENTITY_BREEZE_JUMP, 2f, 1f);
            player.setGameMode(previousGameMode);
        }, 360);
    }
    private static LivingEntity getScaryAnimal(World world, Location location) {
        int randomNum = VeganHorror.random.nextInt(1, 6);
        return switch (randomNum) {
            case 1 -> world.spawn(location, Chicken.class);
            case 2 -> world.spawn(location, Cow.class);
            case 3 -> world.spawn(location, Sheep.class);
            case 4 -> world.spawn(location, Pig.class);
            case 5 -> world.spawn(location, Horse.class);
            default -> world.spawn(location, Llama.class);
        };
    }
}
