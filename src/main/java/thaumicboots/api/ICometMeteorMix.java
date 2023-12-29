package thaumicboots.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface ICometMeteorMix extends IMeteor, IComet {

    public default void specialEffect(Item item, EntityPlayer player) {
        ItemStack itemStack = player.inventory.armorItemInSlot(0);
        if (!itemStack.hasTagCompound()) {
            NBTTagCompound par1NBTTagCompound = new NBTTagCompound();
            itemStack.setTagCompound(par1NBTTagCompound);
            itemStack.stackTagCompound.setBoolean("IsSmashingMix", false);
            itemStack.stackTagCompound.setInteger("smashTicksMix", 0);
            itemStack.stackTagCompound.setInteger("airTicksMix", 0);
            itemStack.stackTagCompound.setInteger("runTicksMix", 0);
            itemStack.stackTagCompound.setBoolean("element", false);
        }
        boolean isEnabledGrief = itemStack.stackTagCompound.getBoolean("element");
        grief(player, isEnabledGrief);

        boolean smashing = itemStack.stackTagCompound.getBoolean("IsSmashingMix");
        int ticksSmash = itemStack.stackTagCompound.getInteger("smashTicksMix");
        int ticksAir = itemStack.stackTagCompound.getInteger("airTicksMix");
        int ticksRun = itemStack.stackTagCompound.getInteger("runTicksMix");
        double motionRun = Math.abs(player.motionX) + Math.abs(player.motionZ) + Math.abs(player.motionY);
        if (motionRun > 0.1F || !player.onGround || player.isOnLadder()) {
            if (ticksRun < 100) ticksRun++;
        } else {
            ticksRun = 0;
        }
        if (player.onGround || player.isOnLadder()) {
            int size = 0;
            if (ticksSmash > 5) size = 1;
            if (ticksSmash > 10) size = 2;
            if (ticksSmash > 15) size = 3;
            smashing = false;
            ticksSmash = 0;
            ticksAir = 0;
            if (size > 0) {
                player.worldObj.createExplosion(player, player.posX, player.posY, player.posZ, size, false);
            }
        }

        // COME ON AND SLAM
        if (!player.onGround && !player.isOnLadder() && !player.isInWater()) {
            if (!player.isSneaking()) {
                ticksAir++;
            }

            if (player.isSneaking() && ticksAir > 5) {
                smashing = true;
            }
        }

        if (player.capabilities.isFlying) {
            smashing = false;
            ticksSmash = 0;
            ticksAir = 0;
        }
        if (smashing) {

            for (String particleName : new String[] { "flame", "smoke", "flame" }) {
                player.worldObj.spawnParticle(
                        particleName,
                        player.posX + Math.random() - 0.5F,
                        player.posY + Math.random() - 0.5F,
                        player.posZ + Math.random() - 0.5F,
                        0.0D,
                        0.0D,
                        0.0D);
            }

            player.motionY -= 0.1F;
            ticksSmash++;
        }

        if (!player.isWet() && motionRun > 0.1F) {
            for (String particleName : new String[] { "fireworksSpark", "flame", "flame" }) {
                player.worldObj.spawnParticle(
                        particleName,
                        player.posX + Math.random() - 0.5F,
                        player.boundingBox.minY + 0.25F + ((Math.random() - 0.5) * 0.25F),
                        player.posZ + Math.random() - 0.5F,
                        0.0D,
                        0.025D,
                        0.0D);
            }
        }

        itemStack.stackTagCompound.setInteger("runTicksMix", ticksRun);
        itemStack.stackTagCompound.setBoolean("IsSmashingMix", smashing);
        itemStack.stackTagCompound.setInteger("smashTicksMix", ticksSmash);
        itemStack.stackTagCompound.setInteger("airTicksMix", ticksAir);
    }
}
