package thaumicboots.api.serverfiles;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import thaumicboots.api.ItemBoots;
import thaumicboots.main.utils.compat.GTNHLibHelper;

public class PacketJumpToggleAck implements IMessage, IMessageHandler<PacketJumpToggleAck, IMessage> {

    public double state;

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        state = byteBuf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeDouble(state);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(PacketJumpToggleAck message, MessageContext ctx) {
        Minecraft mc = Minecraft.getMinecraft();
        final ItemStack boots = ItemBoots.getBoots(mc.thePlayer);
        if (boots != null) {
            ItemBoots.setModeJump(boots, message.state);
            if (GTNHLibHelper.isActive()) {
                ItemBoots.renderHUDJumpNotification();
            }
        }

        return null;
    }
}
