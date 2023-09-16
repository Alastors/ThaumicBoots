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

public class PacketSpeedToggle implements IMessage, IMessageHandler<PacketSpeedToggle, IMessage> {

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
    public IMessage onMessage(PacketSpeedToggle message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        final ItemStack boots = ItemBoots.getBoots(player);
        if (boots != null) {
            double jumpState = ItemBoots.changeSpeed(ItemBoots.isSpeedEnabled(boots));
            ItemBoots.setModeSpeed(boots, jumpState);
            PacketSpeedToggleAck ackMessage = new PacketSpeedToggleAck();
            ackMessage.state = jumpState;
            PacketHandler.INSTANCE.sendTo(ackMessage, player);
        }
        return null;
    }
}
