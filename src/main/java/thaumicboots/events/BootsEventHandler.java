package thaumicboots.events;

import java.util.HashMap;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import flaxbeard.thaumicexploration.ThaumicExploration;
import flaxbeard.thaumicexploration.common.ConfigTX;
import flaxbeard.thaumicexploration.integration.TTIntegration;
import ic2.api.item.ElectricItem;
import thaumicboots.api.*;
import thaumicboots.item.boots.comet.ItemElectricCometBoots;
import thaumicboots.item.boots.meteor.ItemElectricMeteorBoots;
import thaumicboots.item.boots.voidwalker.*;
import thaumicboots.main.utils.compat.EMTHelper;
import thaumicboots.main.utils.compat.ExplorationsHelper;

public class BootsEventHandler {

    public static final PlayerCapabilities genericPlayerCapabilities = new PlayerCapabilities();

    @SubscribeEvent
    public void livingTick(LivingUpdateEvent event) {
        if (!(event.entity instanceof EntityPlayer)) {
            return;
        }

        EntityPlayer player = (EntityPlayer) event.entity;
        checkAir(player);

        if (!(event.entity.worldObj.isRemote)) {
            return;
        }

        ItemStack boots = player.inventory.armorItemInSlot(0);

        if ((this.prevStep.containsKey(event.entity.getEntityId()))
                && ((boots == null) || !((boots.getItem() instanceof ItemElectricMeteorBoots)
                        || (boots.getItem() instanceof ItemElectricCometBoots)))) {
            event.entity.stepHeight = (this.prevStep.get(event.entity.getEntityId()));
            this.prevStep.remove(event.entity.getEntityId());
        }
    }

    @SubscribeEvent
    public void joinWorld(EntityJoinWorldEvent event) {
        if ((event.entity instanceof EntityPlayer)) {
            EntityPlayer player = (EntityPlayer) event.entity;
        }
    }

    HashMap<Integer, Float> prevStep = new HashMap<>();

    @SubscribeEvent
    public void playerJumps(LivingEvent.LivingJumpEvent event) {

        // if not a player
        if (!(event.entity instanceof EntityPlayer)) {
            return;
        }

        EntityPlayer player = (EntityPlayer) event.entity;

        // if no boots equipped
        if (player.inventory.armorItemInSlot(0) == null) {
            return;
        }

        Item item = player.inventory.armorItemInSlot(0).getItem();

        // Is there some way I could make this better?
        if (EMTHelper.isActive() && item instanceof ItemElectricBoots) {
            if (ElectricItem.manager.getCharge(player.inventory.armorItemInSlot(0)) == 0) {
                return;
            }
        }

        if (item instanceof IMeteor meteor && (player.isSneaking())) {
            meteor.specialEffect2(event);
        } else if (item instanceof ItemBoots boots) {
            event.entityLiving.motionY += (boots.getJumpModifier()
                    * ItemBoots.isJumpEnabled(player.inventory.armorItemInSlot(0)));
        }
        // 0.275D is approx 3 blocks, 0.265D will get you to just 3 blocks,
        // 0.55D is approx 5.5 blocks, so 0.275 is around 2.25 additional blocks
    }

    @SubscribeEvent
    public void onLivingFall(LivingFallEvent event) {
        /*
         * if (!EMT.instance.isSimulating()) { return; }
         */

        // if entity isn't a player
        if (!(event.entity instanceof EntityPlayer)) {
            return;
        }

        EntityPlayer entity = (EntityPlayer) event.entity;

        // if no boots equiped
        if (entity.inventory.armorInventory[0] == null) {
            return;
        }
        ItemStack itemStack = entity.inventory.armorInventory[0];
        Item item = itemStack.getItem();

        // if the boots aren't Comet boots or its derivative
        if (!(item instanceof ItemElectricCometBoots)) {
            return;
        }

        ItemElectricCometBoots bootItem = (ItemElectricCometBoots) item;

        // nullifying the fall damages due to jump boost
        if (event.distance <= bootItem.getMinimumHeight()) {
            event.setCanceled(true);
            return;
        }

        if (EMTHelper.isActive()) {
            double energyDemand = bootItem.getPowerConsumption(event.distance);

            // if boots can be discharged nullifying the fall damages
            if (energyDemand <= ElectricItem.manager.getCharge(itemStack)) {
                ElectricItem.manager.discharge(itemStack, energyDemand, Integer.MAX_VALUE, true, false, false);
                event.setCanceled(true);
            }
        }
    }

    // TODO boot effect
    public void checkAir(EntityPlayer player) {

        // no item in boots slot
        if (player.inventory.armorItemInSlot(0) == null) {
            return;
        }

        Item item = player.inventory.armorItemInSlot(0).getItem();


        if (item instanceof ISpecialEffect boot) {
            boot.specialEffect(item, player);
        }
    }

}
