package de.scribble.lp.dupemod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent ev) {
		DupeMod.NETWORK= NetworkRegistry.INSTANCE.newSimpleChannel("dupemod");
		DupeMod.NETWORK.registerMessage(DupePacketHandler.class, DupePacket.class, 0, Side.SERVER);
	}

	public void Init(FMLInitializationEvent ev) {
		FMLCommonHandler.instance().bus().register(new DupeEvents());
	}
}
