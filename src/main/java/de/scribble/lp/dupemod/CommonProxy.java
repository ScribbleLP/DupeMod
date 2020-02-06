package de.scribble.lp.dupemod;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent ev) {
		DupeMod.NETWORK= NetworkRegistry.INSTANCE.newSimpleChannel("dupemod");
		DupeMod.NETWORK.registerMessage(DupePacketHandler.class, DupePacket.class, 0, Side.SERVER);
	}

	public void Init(FMLInitializationEvent ev) {
		MinecraftForge.EVENT_BUS.register(new DupeEvents2());
		FMLCommonHandler.instance().bus().register(new DupeEvents());
	}
}
