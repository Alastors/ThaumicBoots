package thaumicboots.api.serverfiles;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import thaumicboots.api.ItemBoots;

public class PacketElementModeToggle implements IMessage, IMessageHandler<PacketElementModeToggle, IMessage> {

    public void fromBytes(ByteBuf byteBuf) {
        // not needed
    }

    public void toBytes(ByteBuf byteBuf) {
        // not needed
    }

    @Override
    public IMessage onMessage(PacketElementModeToggle message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        final ItemStack boots = ItemBoots.getBoots(player);
        if (boots != null) {
            boolean mode = !ItemBoots.getModeElement(boots);
            ItemBoots.setModeElement(boots, mode);
            PacketElementModeToggleAck ackMessage = new PacketElementModeToggleAck();
            ackMessage.state = mode;
            PacketHandler.INSTANCE.sendTo(ackMessage, player);
        }
        return null;
    }
}
