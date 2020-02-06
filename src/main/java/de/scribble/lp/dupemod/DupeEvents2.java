package de.scribble.lp.dupemod;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.WorldEvent;

public class DupeEvents2 {
	private Minecraft mc=Minecraft.getMinecraft();
	
	@SubscribeEvent
	public void onCloseServer(WorldEvent.Unload event){
		if(mc.currentScreen instanceof GuiIngameMenu){
			if (!mc.getIntegratedServer().getPublic()) {
				if (event.world.playerEntities.size() != 0) {
					DupeMod.logger.info("Start saving...");
					new RecordingDupe().saveFile((EntityPlayer) event.world.playerEntities.get(0));
				}
			}
		}
	}
}