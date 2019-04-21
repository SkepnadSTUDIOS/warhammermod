package warhammermod.util.Handler.inithandler;

import net.minecraft.item.Item;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import warhammermod.Items.*;
import warhammermod.util.confighandler.confighandler.Config_enable;
import warhammermod.util.confighandler.confighandler.getvalues;


import java.util.ArrayList;
import java.util.List;

public class Itemsinit {
    public static final List<Item> ITEMS = new ArrayList<>();

    public static final Item flintlock_pistol = new guntemplate("flintlock pistol",1,25,8, Config_enable.pistol_included);
    public static final Item musket = new guntemplate("musket", 1, 40,13,Config_enable.musket_included);
    public static final Item repeater_handgun = new guntemplate("nuln repeater handgun",6,80,13,Config_enable.repeater_handgun_inlcuded);
    public static final Item grenadeln = new grenadelaunchertemplate("grenade launcher", 1, 40,Config_enable.grenadelauncher_included);
    public static final Item Cartridge = new Cartridge("cartridge");
    public static final Item Grenade = new Grenade("grenade",Config_enable.grenadelauncher_included);
    public static final Item diamond_gunsword = new gunswtemplate(Item.ToolMaterial.DIAMOND,"diamond gunsword",Config_enable.gunsword_included);
    public static final Item iron_gunsword = new gunswtemplate(Item.ToolMaterial.IRON,"iron gunsword",Config_enable.gunsword_included);


    public static final ItemTool stone_spear = new speartemplate("stone spear", getvalues.getspd,getvalues.getsps, Item.ToolMaterial.STONE,Config_enable.spears_included);
    public static final ItemTool wooden_spear = new speartemplate("wooden spear",getvalues.getspd,getvalues.getsps, Item.ToolMaterial.WOOD,Config_enable.spears_included);
    public static final ItemTool iron_spear = new speartemplate("iron spear",getvalues.getspd,getvalues.getsps, Item.ToolMaterial.IRON,Config_enable.spears_included);
    public static final ItemTool gold_spear = new speartemplate("gold spear",getvalues.getspd,getvalues.getsps, Item.ToolMaterial.GOLD,Config_enable.spears_included);
    public static final ItemTool diamond_spear = new speartemplate("diamond spear",getvalues.getspd,getvalues.getsps, Item.ToolMaterial.DIAMOND,Config_enable.spears_included);

    public static final ItemSword diamond_warhammer = new heavytemplate("diamond warhammer", Item.ToolMaterial.DIAMOND,getvalues.getwhd,getvalues.getwhs,Config_enable.hammers_included);
    public static final ItemSword wooden_warhammer = new heavytemplate("wooden warhammer", Item.ToolMaterial.WOOD,getvalues.getwhd,getvalues.getwhs,Config_enable.hammers_included);
    public static final ItemSword stone_warhammer = new heavytemplate("stone warhammer", Item.ToolMaterial.STONE,getvalues.getwhd,getvalues.getwhs,Config_enable.hammers_included);
    public static final ItemSword iron_warhammer = new heavytemplate("iron warhammer", Item.ToolMaterial.IRON,getvalues.getwhd,getvalues.getwhs,Config_enable.hammers_included);
    public static final ItemSword gold_warhammer = new heavytemplate("gold warhammer", Item.ToolMaterial.IRON,getvalues.getwhd+1,getvalues.getwhs,Config_enable.hammers_included);

    public static final ItemTool diamond_knife = new knifetemplate("diamond knife",getvalues.getknd,getvalues.getkns, Item.ToolMaterial.DIAMOND,Config_enable.knife_included);
    public static final ItemTool wooden_knife = new knifetemplate("wooden knife",getvalues.getknd,getvalues.getkns, Item.ToolMaterial.WOOD,Config_enable.knife_included);
    public static final ItemTool stone_knife = new knifetemplate("stone knife",getvalues.getknd,getvalues.getkns, Item.ToolMaterial.STONE,Config_enable.knife_included);
    public static final ItemTool iron_knife = new knifetemplate("iron knife",getvalues.getknd,getvalues.getkns, Item.ToolMaterial.IRON,Config_enable.knife_included);
    public static final ItemTool gold_knife = new knifetemplate("gold knife",getvalues.getknd,getvalues.getkns, Item.ToolMaterial.GOLD,Config_enable.knife_included);

    public static final ItemSword diamond_halberd = new halberdtemplate("diamond halberd", Item.ToolMaterial.DIAMOND,getvalues.gethbd,getvalues.gethbs,Config_enable.halberds_included);
    public static final ItemSword wooden_halberd = new halberdtemplate("wooden halberd", Item.ToolMaterial.WOOD,getvalues.gethbd,getvalues.gethbs,Config_enable.halberds_included);
    public static final ItemSword stone_halberd = new halberdtemplate("stone halberd", Item.ToolMaterial.STONE,getvalues.gethbd,getvalues.gethbs,Config_enable.halberds_included);
    public static final ItemSword iron_halberd = new halberdtemplate("iron halberd", Item.ToolMaterial.IRON,getvalues.gethbd,getvalues.gethbs,Config_enable.halberds_included);
    public static final ItemSword gold_halberd = new halberdtemplate("gold halberd", Item.ToolMaterial.GOLD,getvalues.gethbd,getvalues.gethbs,Config_enable.halberds_included);

    public static final ItemShield shield = new shieldtemplate("empire shield",getvalues.getSD, Config_enable.shields_included);
    public static final ItemShield shield2 = new shieldtemplate("dark elf shield",getvalues.getSD, Config_enable.shields_included);
    public static final ItemShield shield3 = new shieldtemplate("high elf shield",getvalues.getSD, Config_enable.shields_included);
    public static final ItemShield shield4 = new shieldtemplate("dwarf shield",getvalues.getSD, Config_enable.shields_included);


}
