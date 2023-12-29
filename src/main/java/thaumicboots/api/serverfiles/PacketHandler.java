package thaumicboots.api.serverfiles;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import thaumicboots.main.utils.VersionInfo;

public class PacketHandler {

    private PacketHandler() {}

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE
            .newSimpleChannel(VersionInfo.ModID.toLowerCase());

    public static void initPackets() {
        INSTANCE.registerMessage(PacketJumpToggle.class, PacketJumpToggle.class, 1, Side.SERVER);
        INSTANCE.registerMessage(PacketJumpToggleAck.class, PacketJumpToggleAck.class, 2, Side.CLIENT);
        INSTANCE.registerMessage(PacketSpeedToggle.class, PacketSpeedToggle.class, 3, Side.SERVER);
        INSTANCE.registerMessage(PacketSpeedToggleAck.class, PacketSpeedToggleAck.class, 4, Side.CLIENT);
        INSTANCE.registerMessage(PacketElementModeToggle.class, PacketElementModeToggle.class, 5, Side.SERVER);
        INSTANCE.registerMessage(PacketElementModeToggleAck.class, PacketElementModeToggleAck.class, 6, Side.CLIENT);
    }
}
