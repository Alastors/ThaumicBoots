package thaumicboots.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ISpecialArmor;

import baubles.common.lib.PlayerHandler;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.api.IWarpingGear;

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
}
