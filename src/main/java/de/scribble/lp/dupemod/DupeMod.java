package de.scribble.lp.dupemod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import de.scribble.lp.dupemod.commands.DupeCommandc;
import net.minecraft.client.Minecraft;

@Mod(modid = DupeMod.MODID, name=DupeMod.NAME, version = DupeMod.VERSION)
public class DupeMod
{
    public static final String MODID = "dupemod";
    public static final String VERSION = "${version}";
    public static final String NAME= "DupeMod";
    public static Logger logger= LogManager.getLogger("DupeMod");
    private Minecraft mc = Minecraft.getMinecraft();
    
    @Instance
    private static DupeMod instance;
    
    public static DupeMod getInstance() {
		return instance;
	}
    @SidedProxy(serverSide = "de.scribble.lp.dupemod.CommonProxy", clientSide = "de.scribble.lp.dupemod.ClientProxy")
	public static CommonProxy proxy;
	
	public static SimpleNetworkWrapper NETWORK;
    
	@EventHandler
    public void preinit(FMLPreInitializationEvent event){
    	logger.info("Initializing");
    	proxy.preInit(event);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event){
		proxy.Init(event);
    }
    @EventHandler
	public void serverStarting(FMLServerStartingEvent event){
		event.registerServerCommand(new DupeCommandc());
	}
    
}
