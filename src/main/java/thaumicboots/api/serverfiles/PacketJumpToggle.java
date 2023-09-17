package thaumicboots.api.serverfiles;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import thaumicboots.api.ItemBoots;

public class PacketJumpToggle implements IMessage, IMessageHandler<PacketJumpToggle, IMessage> {

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        // not needed
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        // not needed
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(PacketJumpToggle message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        final ItemStack boots = ItemBoots.getBoots(player);
        if (boots != null) {
            double jumpState = ItemBoots.changeJump(ItemBoots.isJumpEnabled(boots));
            ItemBoots.setModeJump(boots, jumpState);
            PacketJumpToggleAck ackMessage = new PacketJumpToggleAck();
            ackMessage.state = jumpState;
            PacketHandler.INSTANCE.sendTo(ackMessage, player);
        }
        return null;
    }
}
