package de.scribble.lp.dupemod;

import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import de.scribble.lp.dupemod.commands.DupeCommandc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = DupeMod.MODID, name=DupeMod.NAME, version = DupeMod.VERSION)
public class DupeMod
{
    public static final String MODID = "dupemod";
    public static final String VERSION = "${version}";
    public static final String NAME= "DupeMod";
    public static Logger logger= null;
    private Minecraft mc = Minecraft.getMinecraft();
    
    @Instance
    private static DupeMod instance;
    
    public static DupeMod getInstance() {
		return instance;
	}
    @SidedProxy(serverSide = "de.scribble.lp.dupemod.CommonProxy", clientSide = "de.scribble.lp.dupemod.ClientProxy")
	public static CommonProxy proxy;
	
	public static SimpleNetworkWrapper NETWORK;
  
    public static KeyBinding DupeKey = new KeyBinding("Load Chests/Items", Keyboard.KEY_I, "DupeMod");
    
    @EventHandler
    public void preinit(FMLPreInitializationEvent event){
    	logger=event.getModLog();
    	logger.info("Initializing");
    	ClientRegistry.registerKeyBinding(DupeKey);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event){
		MinecraftForge.EVENT_BUS.register(new DupeEvents());
    }
    @EventHandler
	public void serverStarting(FMLServerStartingEvent event){
		event.registerServerCommand(new DupeCommandc());
	}
    
}
