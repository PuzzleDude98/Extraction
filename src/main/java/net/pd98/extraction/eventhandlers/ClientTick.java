package net.pd98.extraction.eventhandlers;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.pd98.extraction.custom.ModEvents;

public class ClientTick {
    public static void registerCallback() {
        ClientTickEvents.START_CLIENT_TICK.register(ClientTick::onClientTick);
    }

    private static void onClientTick(MinecraftClient client) {
        ModEvents.moveCamera();
    }

}
