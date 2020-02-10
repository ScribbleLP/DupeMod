package de.scribble.lp.dupemod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
@Deprecated
public class Refilling {
	private Minecraft mc= Minecraft.getMinecraft(); 
	private TileEntityChest foundchest;
	private int chestcounter=0;
	private int chestitemcounter=0;
	private int itemcounter=0;
	
	public void killItems(List<EntityItem> list){
		for(int index=0;index<list.size();index++){
			list.get(index).setDead();
		}
	}
	
	/**
	 * Loads/refills chests and item entitys on the ground, essentially replacing them or spawning new Items
	 * @param file
	 * @param player
	 */
	public void refill(File file, EntityPlayer player){
		String[] coords;
		String[] items;
		String[] enchantments;
		World world = player.getEntityWorld();
		BlockPos playerPos = new BlockPos(player);
		try{
			BufferedReader Buff = new BufferedReader(new FileReader(file));
			String s;
			while (true){
				if((s=Buff.readLine()).equalsIgnoreCase("END")){
					break;
				}
				else if(s.startsWith("#")){				//comments
					continue;
				}
				else if(s.startsWith("Chest:")){
					while (true){
						if((s=Buff.readLine()).equalsIgnoreCase("\t-")){
							break;
						}
						else if(s.startsWith("#")){		//comments
							continue;
						}
						else if(s.startsWith("\tx")){
							coords=s.split("(x=)|(,\\ y=)|(,\\ z=)");
							if (world.getBlockState(new BlockPos(Integer.parseInt(coords[1]),Integer.parseInt(coords[2]),Integer.parseInt(coords[3]))).getBlock()== Blocks.CHEST||world.getBlockState(new BlockPos(Integer.parseInt(coords[1]),Integer.parseInt(coords[2]),Integer.parseInt(coords[3]))).getBlock()== Blocks.TRAPPED_CHEST){
									
								foundchest= (TileEntityChest) world.getTileEntity(new BlockPos(Integer.parseInt(coords[1]),Integer.parseInt(coords[2]),Integer.parseInt(coords[3])));
							
								if(playerPos.distanceSq((double)foundchest.getPos().getX(), (double)foundchest.getPos().getY(), (double)foundchest.getPos().getZ())>50.0){
										DupeMod.logger.error("Chest at "+Integer.parseInt(coords[1])+" "+Integer.parseInt(coords[2])+" "+Integer.parseInt(coords[3])+" is too far away! Distance: "+playerPos.distanceSq((double)foundchest.getPos().getX(), (double)foundchest.getPos().getY(), (double)foundchest.getPos().getZ()));
										continue;
								}
								while(true){
									if((s=Buff.readLine()).equalsIgnoreCase("\t\t-")){
										break;
									}
									else if(s.startsWith("#")){		//comments
										continue;
									}
									else if(s.startsWith("\t\tSlot")){
										items=s.split(";");
										
										ItemStack properties= new ItemStack(Item.getItemById(Integer.parseInt(items[2])),
																								Integer.parseInt(items[4]),
																								Integer.parseInt(items[5]));
										if(!items[7].equals("[]")){
											enchantments=items[7].split("(\\[\\{lvl:)|(s,id:)|(s\\},\\{lvl:)|(s\\})");
											for(int index=1;index<=(enchantments.length-2)/2;index++){
												properties.addEnchantment(Enchantment.getEnchantmentByID(Integer.parseInt(enchantments[2*index])), Integer.parseInt(enchantments[2*index-1]));
											}
										}
										if(!items[6].equals("null")){
											properties.setStackDisplayName(items[6]);
										}
										foundchest.setInventorySlotContents(Integer.parseInt(items[1]), properties);
										chestitemcounter++; //for logging
									}
								}chestcounter++; //for logging
							}
							else{
								DupeMod.logger.error("Didn't find a chest at "+Integer.parseInt(coords[1])+" "+Integer.parseInt(coords[2])+" "+Integer.parseInt(coords[3])+".");
								continue;
							}
						}
					}
				}
				else if(s.startsWith("Items:")){
					
					
					String[] position=s.split(":");
					BlockPos dupePos= new BlockPos(Integer.parseInt(position[1]),Integer.parseInt(position[2]),Integer.parseInt(position[3]));
					List<EntityItem> entitylist= world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(dupePos).grow(10.0));
					
					
					if(playerPos.distanceSq((double)dupePos.getX(),(double)dupePos.getY(),(double)dupePos.getZ())>=50.0){
						DupeMod.logger.error("Player moved too far from initial duping position. Aborting EntityDupe! DupePosition: ("+dupePos.getX()+";"+dupePos.getY()+";"+dupePos.getZ()+") Distance: "+playerPos.distanceSq((double)dupePos.getX(),(double)dupePos.getY(),(double)dupePos.getZ()));
						continue;
					}
					if(!entitylist.isEmpty()){
						killItems(entitylist);
					}
					while (true){
						if((s=Buff.readLine()).equalsIgnoreCase("\t-")){
							break;
						}
						else if(s.startsWith("#")){		//comments
							continue;
						}
						else if(s.startsWith("\tItem;")){
							String[] props=s.split(";");
							ItemStack Overflow= new ItemStack(Item.getItemById(Integer.parseInt(props[5])),
									Integer.parseInt(props[7]),
									Integer.parseInt(props[8]));
							
							if(!props[10].equals("[]")){
								enchantments=props[10].split("(\\[\\{lvl:)|(s,id:)|(s\\},\\{lvl:)|(s\\})");
								for(int index=1;index<=(enchantments.length-2)/2;index++){
									Overflow.addEnchantment(Enchantment.getEnchantmentByID(Integer.parseInt(enchantments[2*index])), Integer.parseInt(enchantments[2*index-1]));
								}
							}
							if(!props[9].equals("null")){
								Overflow.setStackDisplayName(props[9]);
							}
							EntityItem endidyidem=new EntityItem(world, Double.parseDouble(props[2]), Double.parseDouble(props[3]), Double.parseDouble(props[4]), Overflow);
							world.spawnEntity(endidyidem);
							if(endidyidem.lifespan>Integer.parseInt(props[11])){	//check if value is bigger than the lifespan aka over 6000
								endidyidem.lifespan=endidyidem.lifespan-Integer.parseInt(props[11]);
							}
							if(endidyidem.lifespan>Integer.parseInt(props[11])){	//check if value is bigger than the lifespan aka over 6000
								endidyidem.lifespan=endidyidem.lifespan-Integer.parseInt(props[11]);
							}
							if(props[12].equals("null")) {
								if(endidyidem.getAge()<10){
									endidyidem.setPickupDelay(10-Integer.parseInt(props[11]));
								}
							}
							else {
								if(endidyidem.getAge()<40){
									endidyidem.setPickupDelay(40-Integer.parseInt(props[11]));
								}
							}
							endidyidem.motionX=0;
							endidyidem.motionY=0;
							endidyidem.motionZ=0;
							itemcounter++; //for logging
						}
					}
				}
			}
			Buff.close();
			if(chestcounter==0&&itemcounter==0){
				DupeMod.logger.info("Nothing refilled");
			}else{
				DupeMod.logger.info("Refilled "+chestcounter+" chest(s) with "+chestitemcounter+" item(s) and spawned "+ itemcounter+ " item(s) on the ground.");
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
