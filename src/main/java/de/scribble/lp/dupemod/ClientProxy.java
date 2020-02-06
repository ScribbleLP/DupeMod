package de.scribble.lp.dupemod;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.settings.KeyBinding;

public class ClientProxy extends CommonProxy {
	
	public static KeyBinding DupeKey = new KeyBinding("Load Chests/Items", Keyboard.KEY_I, "DupeMod");

	public void preInit(FMLPreInitializationEvent ev) {
		ClientRegistry.registerKeyBinding(DupeKey);
		super.preInit(ev);
	}
	public void Init(FMLInitializationEvent ev) {
		super.Init(ev);
	}
}
