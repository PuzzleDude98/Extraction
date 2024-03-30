package net.pd98.extraction;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.util.ActionResult;
import net.pd98.extraction.custom.ModEvents;
import net.pd98.extraction.eventhandlers.ClientTick;
import net.pd98.extraction.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Extraction implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "extraction";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Extracting!!");

		ModItems.registerModItems();

		UseBlockCallback.EVENT.register(ModEvents::MoveCamera);

		ClientTick.registerCallback();
	}
}