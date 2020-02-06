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
		World world=player.getEntityWorld();
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
							if (world.getBlock(Integer.parseInt(coords[1]),Integer.parseInt(coords[2]),Integer.parseInt(coords[3]))== Blocks.chest||world.getBlock(Integer.parseInt(coords[1]),Integer.parseInt(coords[2]),Integer.parseInt(coords[3]))== Blocks.trapped_chest){
									
								foundchest= (TileEntityChest) world.getTileEntity(Integer.parseInt(coords[1]),Integer.parseInt(coords[2]),Integer.parseInt(coords[3]));
							
								if(player.getDistanceSq((double)foundchest.xCoord, (double)foundchest.yCoord, (double)foundchest.zCoord)>50.0){
										DupeMod.logger.error("Chest at "+Integer.parseInt(coords[1])+" "+Integer.parseInt(coords[2])+" "+Integer.parseInt(coords[3])+" is too far away! Distance: "+player.getDistanceSq((double)foundchest.xCoord, (double)foundchest.yCoord, (double)foundchest.zCoord));
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
										if(!items[7].equals("null")){
											enchantments=items[7].split("(\\[0+:\\{lvl:)|(s,id:)|(s,\\},[0-9]+:\\{lvl:)+|(s,\\})");
											for(int index=1;index<=(enchantments.length-2)/2;index++){
												addEnchantmentbyID(properties, Integer.parseInt(enchantments[2*index]), Integer.parseInt(enchantments[2*index-1]));
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
					double[] dupePos= {Double.parseDouble(position[1]),Double.parseDouble(position[2]),Double.parseDouble(position[3])};
					List<EntityItem> entitylist= world.getEntitiesWithinAABB(EntityItem.class, player.boundingBox.expand(10.0, 10.0, 10.0));
					
					
					if(player.getDistanceSq((double)dupePos[0],(double)dupePos[1],(double)dupePos[2])>=50.0){
						DupeMod.logger.error("Player moved too far from initial duping position. Aborting EntityDupe! DupePosition: ("+dupePos[0]+";"+dupePos[1]+";"+dupePos[2]+") Distance: "+player.getDistanceSq((double)dupePos[0],(double)dupePos[1],(double)dupePos[2]));
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
							
							if(!props[10].equals("null")){
								enchantments=props[10].split("(\\[0+:\\{lvl:)|(s,id:)|(s,\\},[0-9]+:\\{lvl:)+|(s,\\})");
								for(int index=1;index<=(enchantments.length-2)/2;index++){
									addEnchantmentbyID(Overflow, Integer.parseInt(enchantments[2*index]), Integer.parseInt(enchantments[2*index-1]));
								}
							}
							if(!props[9].equals("null")){
								Overflow.setStackDisplayName(props[9]);
							}
							EntityItem endidyidem=new EntityItem(world, Double.parseDouble(props[2]), Double.parseDouble(props[3]), Double.parseDouble(props[4]), Overflow);
							world.spawnEntityInWorld(endidyidem);
							if(endidyidem.lifespan>Integer.parseInt(props[11])){	//check if value is bigger than the lifespan aka over 6000
								endidyidem.lifespan=endidyidem.lifespan-Integer.parseInt(props[11]);
							}
							endidyidem.age=Integer.parseInt(props[11]);
							endidyidem.delayBeforeCanPickup=Integer.parseInt(props[12]);
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
	/**
	 * Adds the enchantment by ID
	 * @param Stack	ItemStack to enchant
	 * @param ID	EnchantmentID
	 * @param level	Strength of the Enchantment
	 * @return ItemStack
	 */
	private ItemStack addEnchantmentbyID(ItemStack Stack, int ID, int level){
		switch (ID) {
		case 0:
			Stack.addEnchantment(Enchantment.protection, level);
			break;
		case 1:
			Stack.addEnchantment(Enchantment.fireProtection, level);
			break;
		case 2:
			Stack.addEnchantment(Enchantment.featherFalling, level);
			break;
		case 3:
			Stack.addEnchantment(Enchantment.blastProtection, level);
			break;
		case 4:
			Stack.addEnchantment(Enchantment.projectileProtection, level);
			break;
		case 5:
			Stack.addEnchantment(Enchantment.respiration, level);
			break;
		case 6:
			Stack.addEnchantment(Enchantment.aquaAffinity,level);
			break;
		case 7:
			Stack.addEnchantment(Enchantment.thorns, level);
			break;
		case 16:
			Stack.addEnchantment(Enchantment.sharpness, level);
			break;
		case 17:
			Stack.addEnchantment(Enchantment.smite, level);
			break;
		case 18:
			Stack.addEnchantment(Enchantment.baneOfArthropods, level);
			break;
		case 19:
			Stack.addEnchantment(Enchantment.knockback, level);
			break;
		case 20:
			Stack.addEnchantment(Enchantment.fireAspect, level);
			break;
		case 21:
			Stack.addEnchantment(Enchantment.looting, level);
			break;
		case 32:
			Stack.addEnchantment(Enchantment.efficiency, level);
			break;
		case 33:
			Stack.addEnchantment(Enchantment.silkTouch, level);
			break;
		case 34:
			Stack.addEnchantment(Enchantment.unbreaking, level);
			break;
		case 35:
			Stack.addEnchantment(Enchantment.fortune, level);
			break;
		case 48:
			Stack.addEnchantment(Enchantment.power, level);
			break;
		case 49:
			Stack.addEnchantment(Enchantment.punch, level);
			break;
		case 50:
			Stack.addEnchantment(Enchantment.flame, level);
			break;
		case 51:
			Stack.addEnchantment(Enchantment.infinity, level);
			break;
		}
		return Stack;
	}
}
