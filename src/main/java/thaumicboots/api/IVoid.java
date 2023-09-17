package thaumicboots.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;

import baubles.common.lib.PlayerHandler;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.api.IWarpingGear;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXWispEG;

public interface IVoid extends IWarpingGear, ISpecialArmor {

    default int getWarp(final ItemStack stack, final EntityPlayer player) {
        return 5;
    }

    // TODO: Extract this into it's own method
    default float sashEquiped(final EntityPlayer player) {
        final ItemStack sash = PlayerHandler.getPlayerBaubles(player).getStackInSlot(3);
        if (sash != null && sash.getItem() == ItemRegistry.ItemVoidwalkerSash) {
            return 3.0F;
        }
        return 1.0F;
    }

    // particle effect from Tainted Magic
    default void particles(final World world, final EntityPlayer player) {
        final FXWispEG fx = new FXWispEG(
                world,
                player.posX + (Math.random() - Math.random()) * 0.5D,
                player.boundingBox.minY + 0.05D + (Math.random() - Math.random()) * 0.1D,
                player.posZ + (Math.random() - Math.random()) * 0.5D,
                player);
        ParticleEngine.instance.addEffect(world, fx);
    }
}
