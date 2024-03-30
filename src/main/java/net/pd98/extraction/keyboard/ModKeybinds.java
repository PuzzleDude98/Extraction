package net.pd98.extraction.keyboard;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.pd98.extraction.custom.ModEvents;
import org.lwjgl.glfw.GLFW;

public class ModKeybinds {
    public static KeyBinding keyBinding;

    public static void registerKeyBindings() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.extraction.debug",
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_LEFT_SHIFT, // The keycode of the key
                "category.extraction.test" // The translation key of the keybinding's category.
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                ModEvents.debug();
            }
        });
    }
}
