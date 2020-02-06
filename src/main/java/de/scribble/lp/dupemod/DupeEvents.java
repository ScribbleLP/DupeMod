package de.scribble.lp.dupemod;

import java.io.File;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class DupeEvents {
	private Minecraft mc= Minecraft.getMinecraft();
	protected static EntityPlayer playa;
	

	@SubscribeEvent
	public void onOpenServer(PlayerLoggedInEvent ev){
		if (!mc.getIntegratedServer().getPublic()) {
			File file= new File(mc.mcDataDir, "saves" + File.separator +mc.getIntegratedServer().getFolderName()+File.separator+"latest_dupe.txt");
			if (file.exists()){
				DupeMod.logger.info("Start refilling...");
				new RefillingDupe().refill(file, ev.player);
			}
		}
	}
	@SubscribeEvent
	public void pressKeybinding(InputEvent.KeyInputEvent ev){
		if(ClientProxy.DupeKey.isPressed()){
			File file= new File(mc.mcDataDir, "saves" + File.separator +mc.getIntegratedServer().getFolderName()+File.separator+"latest_dupe.txt");
			DupeMod.NETWORK.sendToServer(new DupePacket());
		}
	}	
	public void startStopping(EntityPlayer player) {
		
		playa= player;
		Minecraft.getMinecraft().thePlayer.motionX=0;
		Minecraft.getMinecraft().thePlayer.motionY=0;
		Minecraft.getMinecraft().thePlayer.motionZ=0;
		Minecraft.getMinecraft().thePlayer.velocityChanged=true;
	}
}