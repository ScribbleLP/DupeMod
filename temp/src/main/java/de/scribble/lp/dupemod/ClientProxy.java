package de.scribble.lp.dupemod;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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
