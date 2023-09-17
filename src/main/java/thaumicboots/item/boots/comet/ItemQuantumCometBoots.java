package thaumicboots.item.boots.comet;

import net.minecraft.item.EnumRarity;

public class ItemQuantumCometBoots extends ItemElectricCometBoots {

    public ItemQuantumCometBoots(ArmorMaterial par2EnumArmorMaterial, int par3, int par4) {
        super(par2EnumArmorMaterial, par3, par4);
    }

    @Override
    protected void setBootsData() {
        maxCharge = 10_000_000;
        energyPerDamage = 2_500; // 4k hits, 2x prev
        visDiscount = 5;
        transferLimit = 12_000;
        rarity = EnumRarity.rare;
        runBonus = getEMTQuantumSpeed() + 0.220F; // quantum + comet * 4
        jumpBonus = 0.9075D; // 9.5 blocks
        minimumDistance = 100.0F;
        minimumHeight = 10F;
        damageAbsorptionRatio = 2D;
        tier = 4;
        unlocalisedName = "ItemQuantumComet";
        iconResPath = "thaumicboots:quantumComet_16x";
        armorResPath = "thaumicboots:model/quantumbootsComet.png";
    }
}
