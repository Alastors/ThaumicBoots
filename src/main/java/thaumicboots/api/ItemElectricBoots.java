package thaumicboots.api;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emt.util.EMTConfigHandler;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import thaumicboots.main.utils.TabThaumicBoots;

public class ItemElectricBoots extends ItemBoots implements IElectricItem, ISpecialArmor {

    public int maxCharge;
    public int energyPerDamage;
    public double transferLimit;
    public boolean provideEnergy;

    public double damageAbsorptionRatio;

    public ItemElectricBoots(ArmorMaterial par2EnumArmorMaterial, int par3, int par4) {
        super(par2EnumArmorMaterial, par3, par4);
        setCreativeTab(TabThaumicBoots.tabThaumicBoots);
        setUnlocalizedName(unlocalisedName);
    }

    protected void setBootsData() {
        maxCharge = 0;
        energyPerDamage = 0;
        provideEnergy = false; // doesn't need to exist, but someone could make boots that function like a battery
        transferLimit = 0;
        damageAbsorptionRatio = 0.5D;
    }

    public int getEnergyPerDamage() {
        return energyPerDamage;
    }

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return provideEnergy;
    }

    @Override
    public double getMaxCharge(ItemStack itemStack) {
        return maxCharge;
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return transferLimit;
    }

    // For some reason, sometimes the EMTConfigHandler returns this as 0,
    // but only when it's called outside of EMT,
    // this ensures this never happens internally.
    public float getEMTNanoSpeed() {
        if ((float) EMTConfigHandler.nanoBootsSpeed == 0.0F) {
            return 0.275F;
        }
        return (float) EMTConfigHandler.nanoBootsSpeed;
    }

    public float getEMTQuantumSpeed() {
        if ((float) EMTConfigHandler.quantumBootsSpeed == 0.0F) {
            return 0.51F;
        }
        return (float) EMTConfigHandler.quantumBootsSpeed;
    }

    public double getDamageAbsorptionRatio() {
        return damageAbsorptionRatio;
    }

    public double getBaseAbsorptionRatio() {
        return 0.15D;
    }

    // TODO: non-variable related methods

    @Override
    protected float computeBonus(ItemStack itemStack, EntityPlayer player) {
        int ticks = player.inventory.armorItemInSlot(0).stackTagCompound.getInteger("runTicks");

        float bonus = runBonus + ((ticks / 5) * 0.003F);
        if (ElectricItem.manager.getCharge(itemStack) == 0) {
            bonus = 0;
        } else if (player.isInWater()) {
            bonus /= 4.0F;
        }

        return bonus;
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List itemList) {
        ItemStack itemStack = new ItemStack(this, 1);
        if (getChargedItem(itemStack) == this) {
            ItemStack charged = new ItemStack(this, 1);
            ElectricItem.manager.charge(charged, 2147483647, 2147483647, true, false);
            itemList.add(charged);
        }
        if (getEmptyItem(itemStack) == this) {
            itemList.add(new ItemStack(this, 1, getMaxDamage()));
        }
    }

    @Override
    public ISpecialArmor.ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source,
            double damage, int slot) {
        if (source.isUnblockable()) {
            return new net.minecraftforge.common.ISpecialArmor.ArmorProperties(0, 0.0D, 0);
        } else {
            double absorptionRatio = getBaseAbsorptionRatio() * getDamageAbsorptionRatio();
            int energyPerDamage = getEnergyPerDamage();
            double damageLimit = energyPerDamage <= 0 ? 0
                    : (25 * ElectricItem.manager.getCharge(armor)) / energyPerDamage;
            return new net.minecraftforge.common.ISpecialArmor.ArmorProperties(0, absorptionRatio, (int) damageLimit);
        }
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        if (ElectricItem.manager.getCharge(armor) >= getEnergyPerDamage()) {
            return (int) Math.round(20D * getBaseAbsorptionRatio() * getDamageAbsorptionRatio());
        } else {
            return 0;
        }
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
        ElectricItem.manager.discharge(stack, damage * getEnergyPerDamage(), 0x7fffffff, true, false, false);
    }

    public Item getChargedItem(ItemStack itemStack) {
        return this;
    }

    public Item getEmptyItem(ItemStack itemStack) {
        return this;
    }

    // necessary for the elctric functionality
    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (player.moveForward <= 0F) {
            return;
        }

        float bonus = getSpeedModifier();
        stepHeight(player);
        if (steadyBonus) {
            runningTicks(player);
            bonus = computeBonus(itemStack, player);
        }
        if (ElectricItem.manager.getCharge(itemStack) == 0) {
            bonus *= 0;
        }
        bonus *= itemStack.stackTagCompound.getDouble(TAG_MODE_SPEED);
        applyBonus(player, bonus);

        if (negateFall) {
            if (player.fallDistance > 0.0F) {
                player.fallDistance = 0.0F;
            }
        }
    }
}
