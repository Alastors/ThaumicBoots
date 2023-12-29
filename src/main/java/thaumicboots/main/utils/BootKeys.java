package thaumicboots.main.utils;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import thaumicboots.api.serverfiles.PacketElementModeToggle;
import thaumicboots.api.serverfiles.PacketHandler;
import thaumicboots.api.serverfiles.PacketJumpToggle;
import thaumicboots.api.serverfiles.PacketSpeedToggle;

public class BootKeys {

    private static final KeyBinding keyJumpToggle = new KeyBinding(
            "keybinding.jumptoggle",
            Keyboard.KEY_NONE,
            "Thaumic Boots");
    private final KeyBinding keySpeedToggle = new KeyBinding(
            "keybinding.speedtoggle",
            Keyboard.KEY_NONE,
            "Thaumic Boots");

    private final KeyBinding keyElementToggle = new KeyBinding(
            "keybinding.elementtoggle",
            Keyboard.KEY_NONE,
            "Thaumic Boots");

    public BootKeys() {
        FMLCommonHandler.instance().bus().register(this);
        ClientRegistry.registerKeyBinding(keyJumpToggle);
        ClientRegistry.registerKeyBinding(keySpeedToggle);
        ClientRegistry.registerKeyBinding(keyElementToggle);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void keyPressed(TickEvent.ClientTickEvent event) {
        checkKeys();
    }

    private void checkKeys() {
        if (keyJumpToggle.isPressed()) {
            toggleJump();
        }else if (keySpeedToggle.isPressed()) {
            toggleSpeed();
        }else if (keyElementToggle.isPressed()) {
            toggleElementMode();
        }
    }

    private static void toggleJump() {
        PacketHandler.INSTANCE.sendToServer(new PacketJumpToggle());
    }

    private static void toggleSpeed() {
        PacketHandler.INSTANCE.sendToServer(new PacketSpeedToggle());
    }

    private static void toggleElementMode() {
        PacketHandler.INSTANCE.sendToServer(new PacketElementModeToggle());
    }
}
