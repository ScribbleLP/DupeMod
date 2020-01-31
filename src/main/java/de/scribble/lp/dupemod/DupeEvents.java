package de.scribble.lp.dupemod;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class DupeEvents {
	private Minecraft mc= Minecraft.getMinecraft();
	protected static EntityPlayer playa;
	
	@SubscribeEvent
	public void onCloseServer(PlayerEvent.PlayerLoggedOutEvent ev){
		if(mc.currentScreen instanceof GuiIngameMenu){
			DupeMod.logger.info("Start saving...");
			new RecordingDupe().saveFile(ev.player);
		}
	}
	
	@SubscribeEvent
	public void onOpenServer(PlayerEvent.PlayerLoggedInEvent ev){
		File file= new File(mc.mcDataDir, "saves" + File.separator +mc.getIntegratedServer().getFolderName()+File.separator+"latest_dupe.txt");
		if (file.exists()){
			DupeMod.logger.info("Start refilling...");
			new RefillingDupe().refill(file, ev.player);
		}
	}
	
	@SubscribeEvent
	public void pressKeybinding(InputEvent.KeyInputEvent ev){
			if(ClientProxy.DupeKey.isPressed()){
				DupeMod.NETWORK.sendToServer(new DupePacket());
			}
	}
	public void startStopping(EntityPlayer player) {
		StopMoving stopit = new StopMoving();
		playa= player;
		Minecraft.getMinecraft().thePlayer.motionX=0;
		Minecraft.getMinecraft().thePlayer.motionY=0;
		Minecraft.getMinecraft().thePlayer.motionZ=0;
		Minecraft.getMinecraft().thePlayer.velocityChanged=true;
		playa.setEntityInvulnerable(true);
		MinecraftForge.EVENT_BUS.register(stopit);
	}
}
class StopMoving extends DupeEvents{
	Minecraft mc = Minecraft.getMinecraft();
	int length=0;
	@SubscribeEvent
	public void stopMoving(TickEvent.ClientTickEvent ev) {
		if(ev.phase==Phase.START) {
			if (length < 5) {
				mc.gameSettings.keyBindForward.pressed = false;
				mc.gameSettings.keyBindBack.pressed = false;
				mc.gameSettings.keyBindRight.pressed = false;
				mc.gameSettings.keyBindLeft.pressed = false;
				mc.gameSettings.keyBindJump.pressed = false;
				mc.gameSettings.keyBindSprint.pressed = false;
				mc.gameSettings.keyBindSneak.pressed = false;
				mc.gameSettings.keyBindDrop.pressed = false;
				mc.gameSettings.keyBindAttack.pressed = false;
				mc.gameSettings.keyBindUseItem.pressed = false;
			}
			if(length==60) {
				if(!playa.isCreative()&&!playa.isSpectator()) {
					playa.setEntityInvulnerable(false);
				}
				MinecraftForge.EVENT_BUS.unregister(this);
			}
			length++;
		}
	}
}
