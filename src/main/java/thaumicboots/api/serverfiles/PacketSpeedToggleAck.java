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

public class PacketSpeedToggleAck implements IMessage, IMessageHandler<PacketSpeedToggleAck, IMessage> {

    public double state;

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        state = byteBuf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeDouble(state);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(PacketSpeedToggleAck message, MessageContext ctx) {
        Minecraft mc = Minecraft.getMinecraft();
        final ItemStack boots = ItemBoots.getBoots(mc.thePlayer);
        if (boots != null) {
            ItemBoots.setModeSpeed(boots, message.state);
            if (GTNHLibHelper.isActive()) {
                ItemBoots.renderHUDSpeedNotification();
            }
        }

        return null;
    }
}
