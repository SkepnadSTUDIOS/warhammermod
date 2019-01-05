package warhammermod.util.Handler.inithandler;

import net.minecraft.item.Item;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemTool;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import warhammermod.Items.*;
import warhammermod.util.confighandler.confighandler.Config_enable;


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
    public static final Item diamond_gunsword = new gunswtemplate(Item.ToolMaterial.DIAMOND,"diamond gunsword");
    public static final Item iron_gunsword = new gunswtemplate(Item.ToolMaterial.IRON,"iron gunsword");


    public static final ItemTool stone_spear = new speartemplate("stone spear", Item.ToolMaterial.STONE,Config_enable.spears_included);
    public static final ItemTool wooden_spear = new speartemplate("wooden spear", Item.ToolMaterial.WOOD,Config_enable.spears_included);
    public static final ItemTool iron_spear = new speartemplate("iron spear", Item.ToolMaterial.IRON,Config_enable.spears_included);
    public static final ItemTool gold_spear = new speartemplate("gold spear", Item.ToolMaterial.GOLD,Config_enable.spears_included);
    public static final ItemTool diamond_spear = new speartemplate("diamond spear", Item.ToolMaterial.DIAMOND,Config_enable.spears_included);

    public static final ItemTool diamond_warhammer = new heavytemplate("diamond warhammer", Item.ToolMaterial.DIAMOND,6.2F,3.1F,Config_enable.hammers_included);
    public static final ItemTool wooden_warhammer = new heavytemplate("wooden warhammer", Item.ToolMaterial.WOOD,6.2F,3.1F,Config_enable.hammers_included);
    public static final ItemTool stone_warhammer = new heavytemplate("stone warhammer", Item.ToolMaterial.STONE,6.2F,3.1F,Config_enable.hammers_included);
    public static final ItemTool iron_warhammer = new heavytemplate("iron warhammer", Item.ToolMaterial.IRON,6.2F,3.1F,Config_enable.hammers_included);
    public static final ItemTool gold_warhammer = new heavytemplate("gold warhammer", Item.ToolMaterial.IRON,7.1F,2.7F,Config_enable.hammers_included);

    public static final ItemTool diamond_knife = new knifetemplate("diamond knife", Item.ToolMaterial.DIAMOND,Config_enable.knife_included);
    public static final ItemTool wooden_knife = new knifetemplate("wooden knife", Item.ToolMaterial.WOOD,Config_enable.knife_included);
    public static final ItemTool stone_knife = new knifetemplate("stone knife", Item.ToolMaterial.STONE,Config_enable.knife_included);
    public static final ItemTool iron_knife = new knifetemplate("iron knife", Item.ToolMaterial.IRON,Config_enable.knife_included);
    public static final ItemTool gold_knife = new knifetemplate("gold knife", Item.ToolMaterial.GOLD,Config_enable.knife_included);

    public static final ItemTool diamond_halberd = new halberdtemplate("diamond halberd", Item.ToolMaterial.DIAMOND,Config_enable.halberds_included);
    public static final ItemTool wooden_halberd = new halberdtemplate("wooden halberd", Item.ToolMaterial.WOOD,Config_enable.halberds_included);
    public static final ItemTool stone_halberd = new halberdtemplate("stone halberd", Item.ToolMaterial.STONE,Config_enable.halberds_included);
    public static final ItemTool iron_halberd = new halberdtemplate("iron halberd", Item.ToolMaterial.IRON,Config_enable.halberds_included);
    public static final ItemTool gold_halberd = new halberdtemplate("gold halberd", Item.ToolMaterial.GOLD,Config_enable.halberds_included);

    public static final ItemShield shield = new shieldtemplate("empire shield", Config_enable.shields_included);
    public static final ItemShield shield2 = new shieldtemplate("dark elf shield", Config_enable.shields_included);
    public static final ItemShield shield3 = new shieldtemplate("high elf shield", Config_enable.shields_included);
    public static final ItemShield shield4 = new shieldtemplate("dwarf shield", Config_enable.shields_included);


}