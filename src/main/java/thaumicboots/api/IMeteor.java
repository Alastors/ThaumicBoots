package thaumicboots.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;

import cpw.mods.fml.common.Loader;
import flaxbeard.thaumicexploration.integration.TTIntegration;

public interface IMeteor extends ISpecialEffect {

    public default void specialEffect(Item item, EntityPlayer player) {
        ItemStack itemStack = player.inventory.armorItemInSlot(0);
        if (!itemStack.hasTagCompound()) {
            NBTTagCompound par1NBTTagCompound = new NBTTagCompound();
            itemStack.setTagCompound(par1NBTTagCompound);
            itemStack.stackTagCompound.setBoolean("IsSmashing", false);
            itemStack.stackTagCompound.setInteger("smashTicks", 0);
            itemStack.stackTagCompound.setInteger("airTicks", 0);
        }
        boolean smashing = itemStack.stackTagCompound.getBoolean("IsSmashing");
        int ticks = itemStack.stackTagCompound.getInteger("smashTicks");
        int ticksAir = itemStack.stackTagCompound.getInteger("airTicks");

        if (player.onGround || player.isOnLadder()) {
            int size = 0;
            if (ticks > 5) size = 1;
            if (ticks > 10) size = 2;
            if (ticks > 15) size = 3;
            smashing = false;
            ticks = 0;
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
            ticks = 0;
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
            ticks++;
        } else {
            double motion = Math.abs(player.motionX) + Math.abs(player.motionZ) + Math.abs(0.5 * player.motionY);
            if (!player.isWet() && motion > 0.1F) {
                player.worldObj.spawnParticle(
                        "flame",
                        player.posX + Math.random() - 0.5F,
                        player.boundingBox.minY + 0.25F + ((Math.random() - 0.5) * 0.25F),
                        player.posZ + Math.random() - 0.5F,
                        0.0D,
                        0.025D,
                        0.0D);
            }
        }

        itemStack.stackTagCompound.setBoolean("IsSmashing", smashing);
        itemStack.stackTagCompound.setInteger("smashTicks", ticks);
        itemStack.stackTagCompound.setInteger("airTicks", ticksAir);
    }

    public default void specialEffect2(LivingEvent.LivingJumpEvent event) {
        Vec3 vector = event.entityLiving.getLook(0.5F);
        double total = Math.abs(vector.zCoord + vector.xCoord);
        double jump = 0;
        if (Loader.isModLoaded("ThaumicTinkerer")) {
            jump = TTIntegration.getAscentLevel((EntityPlayer) event.entity);
        }
        if (jump >= 1) {
            jump = (jump + 2D) / 4D;
        }

        if (vector.yCoord < total) vector.yCoord = total;

        event.entityLiving.motionY += ((jump + 1) * vector.yCoord) / 1.5F;
        event.entityLiving.motionZ += (jump + 1) * vector.zCoord * 4;
        event.entityLiving.motionX += (jump + 1) * vector.xCoord * 4;
    }
}
