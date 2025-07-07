package com.silvager.testPlugin;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.NoiseGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class CustomGen extends ChunkGenerator {

    @Override
    public void generateNoise(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                if (random.nextBoolean()) {
                    chunkData.setBlock(x, 0, z, Material.BARRIER);
                } else {
                    chunkData.setBlock(x, 0, z, Material.BONE_BLOCK);
                }
                if (!isPosInsideCircle((chunkX*16) + x, (chunkZ*16) + z)) {
                    for (int y=1; y<=20; y++) {
                        chunkData.setBlock(x, y, z, Material.BARRIER);
                    }
                }

            }
        }
    }
    private boolean isPosInsideCircle(int realX, int realZ) {
        int distance = (int) Math.sqrt(realX^2 + realZ^2);
        return distance < 10;
    }

}
