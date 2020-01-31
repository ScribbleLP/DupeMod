package de.scribble.lp.dupemod;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.common.io.Files;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenDoublePlant;
@Deprecated
public class Recording {
	private Minecraft mc= Minecraft.getMinecraft();
	private StringBuilder output = new StringBuilder();
	private int chestcounter=0;
	private int itemcounter=0;
	
	public static void sendMessage(String msg){
		try{
			Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(msg));
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	public void nearbyChest(EntityPlayer player){

		World world = player.getEntityWorld();
		BlockPos playerPos = new BlockPos(player);
		
		output.append("Chest:\n");
		
		for(int x=-5; x<=5; x++){				//x
			for(int y=-5; y<=5; y++){			//y
				for(int z=-5; z<=5; z++){		//z

					if (world.getBlockState(playerPos.add(x, y, z)).getBlock()== Blocks.CHEST||world.getBlockState(playerPos.add(x, y, z)).getBlock()== Blocks.TRAPPED_CHEST){
						TileEntityChest foundchest =(TileEntityChest) world.getTileEntity(playerPos.add(x,y,z));
						chestcounter++;
						//sendMessage(foundchest.getPos().toString().substring(9,foundchest.getPos().toString().length()-1));

						output.append("\t"+foundchest.getPos().toString().substring(9,foundchest.getPos().toString().length()-1)+"\n");
						
						for(int i=0; i<foundchest.getSizeInventory();i++){
							ItemStack item = foundchest.getStackInSlot(i);
							if(item != null){
								if(item.hasDisplayName()){
									//sendMessage("Slot;"+i+";"+Item.getIdFromItem(item.getItem())+";("+item.getUnlocalizedName()+");"+item.getCount()+";"+item.getItemDamage()+";"+item.getDisplayName()+";"+item.getEnchantmentTagList()+"\n");
									output.append("\t\tSlot;"+i+";"+Item.getIdFromItem(item.getItem())+";("+item.getUnlocalizedName()+");"+item.stackSize+";"+item.getItemDamage()+";"+item.getDisplayName()+";"+item.getEnchantmentTagList()+"\n");
								}else{
									//sendMessage("Slot;"+i+";"+Item.getIdFromItem(item.getItem())+";("+item.getUnlocalizedName()+");"+item.getCount()+";"+item.getItemDamage()+";null;"+item.getEnchantmentTagList()+"\n");
									output.append("\t\tSlot;"+i+";"+Item.getIdFromItem(item.getItem())+";("+item.getUnlocalizedName()+");"+item.stackSize+";"+item.getItemDamage()+";null;"+item.getEnchantmentTagList()+"\n");
								}
							}
						}
						output.append("\t\t-\n");
					}
				}
			}
		}
		output.append("\t-\n");
	}
	public void nearbyItems(EntityPlayer player){
		World world = player.getEntityWorld();
		BlockPos playerPos = new BlockPos(player);
		
		output.append("Items:"+playerPos.getX()+":"+playerPos.getY()+":"+playerPos.getZ()+"\n");
		
		List<EntityItem> entitylist= world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(playerPos).expandXyz(10.0));
		if(!entitylist.isEmpty()){
			itemcounter=entitylist.size();
			for(int i=0;i<entitylist.size();i++){
				if(entitylist.get(i).getEntityItem().hasDisplayName()){
					output.append("\tItem;"+i+";"+entitylist.get(i).posX+";"+entitylist.get(i).posY+";"+entitylist.get(i).posZ+";"+Item.getIdFromItem(entitylist.get(i).getEntityItem().getItem())+";("+entitylist.get(i).getEntityItem().getUnlocalizedName()+");"+entitylist.get(i).getEntityItem().stackSize+";"+entitylist.get(i).getEntityItem().getItemDamage()+";"+entitylist.get(i).getEntityItem().getDisplayName()+";"+entitylist.get(i).getEntityItem().getEnchantmentTagList()+";"+entitylist.get(i).getAge()+";"+entitylist.get(i).getThrower()+"\n");
				}else{
					output.append("\tItem;"+i+";"+entitylist.get(i).posX+";"+entitylist.get(i).posY+";"+entitylist.get(i).posZ+";"+Item.getIdFromItem(entitylist.get(i).getEntityItem().getItem())+";("+entitylist.get(i).getEntityItem().getUnlocalizedName()+");"+entitylist.get(i).getEntityItem().stackSize+";"+entitylist.get(i).getEntityItem().getItemDamage()+";null;"+entitylist.get(i).getEntityItem().getEnchantmentTagList()+";"+entitylist.get(i).getAge()+";"+entitylist.get(i).getThrower()+"\n");
				}
			}
		}
		output.append("\t-\n");
		
	}
	public void saveFile(EntityPlayer player){
		File file= new File(mc.mcDataDir, "saves" + File.separator +mc.getIntegratedServer().getFolderName()+File.separator+"latest_dupe.txt");
		output.append("#This file was generated by the DupeMod, the author is ScribbleLP.\n");
	
		nearbyChest(player);
		nearbyItems(player);
		
		output.append("END");
		try {
			DupeMod.logger.info("Saving "+chestcounter+" chest(s) and "+ itemcounter+ " item(s).");
			Files.write(output.toString().getBytes(), file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
