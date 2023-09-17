package thaumicboots;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;

import flaxbeard.thaumicexploration.ThaumicExploration;
import flaxbeard.thaumicexploration.common.ConfigTX;
import thaumicboots.main.utils.compat.ExplorationsHelper;

public interface IGrief {

    public default void grief(EntityPlayer player) {
        // anti-griefing config
        if (ExplorationsHelper.isActive()) {
            if (!ConfigTX.allowBootsIce) {
                return;
            }
            for (int x = -5; x < 6; x++) {
                for (int z = -5; z < 6; z++) {
                    int X = (int) (player.posX + x);
                    int Y = (int) (player.posY - 1);
                    int Z = (int) (player.posZ + z);

                    // if the block isn't water
                    if (player.worldObj.getBlock(X, Y, Z) != Blocks.water) {
                        continue;
                    }

                    // if the block hasn't some water properties
                    if (player.worldObj.getBlock(X, Y, Z).getMaterial() != Material.water) {
                        continue;
                    }

                    // if the metadata of the block isn't 0
                    if (player.worldObj.getBlockMetadata(X, Y, Z) != 0) {
                        continue;
                    }

                    // if the player is in water
                    if (player.isInWater()) {
                        continue;
                    }

                    // ???, someone needs to figure out what this does.
                    if ((Math.abs(x) + Math.abs(z) >= 8)) {
                        continue;
                    }

                    player.worldObj.setBlock(X, Y, Z, ThaumicExploration.meltyIce);
                    player.worldObj.spawnParticle("snowballpoof", X, Y + 1, Z, 0.0D, 0.025D, 0.0D);
                }
            }
        }
    }
}
