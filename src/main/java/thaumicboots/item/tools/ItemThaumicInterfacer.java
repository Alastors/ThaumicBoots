package thaumicboots.item.tools;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import thaumicboots.main.utils.TabThaumicBoots;

public class ItemThaumicInterfacer extends Item {

    IIcon itemIcon;
    public TileEntity matrix;

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        itemIcon = ir.registerIcon("thaumicboots:arcanium_filter");
    }

    @SideOnly(Side.CLIENT)
    public IIcon func_77617_a(int par1) {
        return itemIcon;
    }

    public ItemThaumicInterfacer() {
        setCreativeTab(TabThaumicBoots.tabThaumicBoots);
        setUnlocalizedName("ItemArcaniumInterfacer");
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int targetX, int targetY,
            int targetZ, int side, float hitX, float hitY, float hitZ) {
        // ItemStack stack1 = player.getCurrentArmor(0);
        // player.addChatMessage(new ChatComponentText("seethe: " + stack1.getDisplayName()));
        return false;
    }
}
