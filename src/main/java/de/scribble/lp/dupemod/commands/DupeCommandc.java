package de.scribble.lp.dupemod.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.scribble.lp.dupemod.RefillingDupe;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class DupeCommandc extends CommandBase{
	private Minecraft mc= Minecraft.getMinecraft();
	private	 List<String> tab = new ArrayList<String>();
	
	public List<String> emptyList(List<String> full){
		while(full.size()!=0){
			full.remove(0);
		}
		return full;
	}
	
	@Override
	public String getCommandName() {
		
		return "dupe";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/dupe";
	}
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if(sender instanceof EntityPlayer && mc.thePlayer.getEntityWorld().isRemote){
			if(args.length==0||(args[0].equalsIgnoreCase("chest")&&args.length==1)){
				File file= new File(mc.mcDataDir, "saves" + File.separator +mc.getIntegratedServer().getFolderName()+File.separator+"latest_dupe.txt");
				if (file.exists())new RefillingDupe().refill(file, (EntityPlayer)sender);
			}
		}
	}
}
