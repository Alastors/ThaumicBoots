package thaumicboots.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IComet extends ISpecialEffect, IGrief {

    public default void specialEffect(Item item, EntityPlayer player) {
        ItemStack itemStack = player.inventory.armorItemInSlot(0);
        if (!itemStack.hasTagCompound()) {
            NBTTagCompound par1NBTTagCompound = new NBTTagCompound();
            itemStack.setTagCompound(par1NBTTagCompound);
            itemStack.stackTagCompound.setInteger("runTicks", 0);
            itemStack.stackTagCompound.setBoolean("element", false);
        }
        boolean isEnabledGrief = itemStack.stackTagCompound.getBoolean("element");
        grief(player, isEnabledGrief);
        int ticks = itemStack.stackTagCompound.getInteger("runTicks");
        double motion = Math.abs(player.motionX) + Math.abs(player.motionZ) + Math.abs(player.motionY);
        if (motion > 0.1F || !player.onGround || player.isOnLadder()) {
            if (ticks < 100) ticks++;
        } else {
            ticks = 0;
        }
        if (!player.isWet() && motion > 0.1F) {
            player.worldObj.spawnParticle(
                    "fireworksSpark",
                    player.posX + Math.random() - 0.5F,
                    player.boundingBox.minY + 0.25F + ((Math.random() - 0.5) * 0.25F),
                    player.posZ + Math.random() - 0.5F,
                    0.0D,
                    0.025D,
                    0.0D);
        }
        itemStack.stackTagCompound.setInteger("runTicks", ticks);
    }

}
