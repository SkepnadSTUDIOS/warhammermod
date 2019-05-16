package warhammermod.util.Handler.inithandler;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import warhammermod.Items.*;
import static warhammermod.util.confighandler.confighandler.Config_values.*;
import warhammermod.util.confighandler.confighandler.Config_enable;

import java.util.ArrayList;
import java.util.List;



public class Itemsinit {
    public static final List<Item> ITEMS = new ArrayList<>();

    public static final Item flintlock_pistol = new guntemplate("flintlock pistol",1,pistol_speed,pistol_damage,384, Config_enable.pistol_included);
    public static final Item musket = new guntemplate("musket", 1, rifle_reload_speed,rifle_damage,420,Config_enable.musket_included);
    public static final Item repeater_handgun = new guntemplate("nuln repeater handgun",6,(int)(rifle_reload_speed*2.3),rifle_damage,500,Config_enable.repeater_handgun_inlcuded);
    public static final Item grenadeln = new grenadelaunchertemplate("grenade launcher", 1, grenadelauncher_reload_speed,Config_enable.grenadelauncher_included);
    public static final Item diamond_gunsword = new gunswtemplate(Item.ToolMaterial.DIAMOND,"diamond gunsword",Config_enable.gunsword_included,gunsword_reload_speed);
    public static final Item iron_gunsword = new gunswtemplate(Item.ToolMaterial.IRON,"iron gunsword",Config_enable.gunsword_included,gunsword_reload_speed);
    public static final ItemBow Drakegun = new DrakeGunTemplate("Drakegun",drakegun_ammo,drakegun_reload_speed,true);
    public static final guntemplate thunderer_hangun = new guntemplate("thunderer handgun",1,rifle_reload_speed,rifle_damage+3,540,true);
    public static final Item blunderbuss = new shotguntemplate("blunderbuss",1,blunderbusses_speed,300,true);
    public static final Item GrudgeRaker = new shotguntemplate("GrudgeRaker",2,blunderbusses_speed+5,450,true);

    public static final GreatPickaxe GREAT_PICKAXE = new GreatPickaxe("war pick",2.6F,2F,Item.ToolMaterial.DIAMOND,true);
    public static final GhalMaraz GHAL_MARAZ = new GhalMaraz("Ghal Maraz", Item.ToolMaterial.DIAMOND,(float)Ghal_maraz_damage,(float)Ghal_maraz_speed,true);
    public static final Item Cartridge = new Cartridge("cartridge");
    public static final Item Cartridge2 = new Cartridge("shotgun shells");
    public static final Item Grenade = new Grenade("grenade",Config_enable.grenadelauncher_included);

    public static final beertemplate beer = new beertemplate("beer");

    public static final ItemArmor Diamond_Chainmail_helmet = new Armortemplate("diamond chainmail helmet", EntityEquipmentSlot.HEAD);
    public static final ItemArmor Diamond_Chainmail_plate = new Armortemplate("diamond chainmail", EntityEquipmentSlot.CHEST);
    public static final ItemArmor Diamond_Chainmail_legging = new Armortemplate("diamond chainmail leggings", EntityEquipmentSlot.LEGS);
    public static final ItemArmor Diamond_Chainmail_boots = new Armortemplate("diamond chainmail boots", EntityEquipmentSlot.FEET);

    public static final ItemTool stone_spear = new speartemplate("stone spear", Item.ToolMaterial.STONE);
    public static final ItemTool wooden_spear = new speartemplate("wooden spear", Item.ToolMaterial.WOOD);
    public static final ItemTool iron_spear = new speartemplate("iron spear", Item.ToolMaterial.IRON);
    public static final ItemTool gold_spear = new speartemplate("gold spear",Item.ToolMaterial.GOLD);
    public static final ItemTool diamond_spear = new speartemplate("diamond spear", Item.ToolMaterial.DIAMOND);

    public static final ItemSword diamond_warhammer = new heavytemplate("diamond warhammer", Item.ToolMaterial.DIAMOND);
    public static final ItemSword wooden_warhammer = new heavytemplate("wooden warhammer", Item.ToolMaterial.WOOD);
    public static final ItemSword stone_warhammer = new heavytemplate("stone warhammer", Item.ToolMaterial.STONE);
    public static final ItemSword iron_warhammer = new heavytemplate("iron warhammer", Item.ToolMaterial.IRON);
    public static final ItemSword gold_warhammer = new heavytemplate("gold warhammer", Item.ToolMaterial.IRON);

    public static final ItemTool diamond_knife = new knifetemplate("diamond knife", Item.ToolMaterial.DIAMOND);
    public static final ItemTool wooden_knife = new knifetemplate("wooden knife", Item.ToolMaterial.WOOD);
    public static final ItemTool stone_knife = new knifetemplate("stone knife", Item.ToolMaterial.STONE);
    public static final ItemTool iron_knife = new knifetemplate("iron knife", Item.ToolMaterial.IRON);
    public static final ItemTool gold_knife = new knifetemplate("gold knife", Item.ToolMaterial.GOLD);

    public static final ItemSword diamond_halberd = new halberdtemplate("diamond halberd", Item.ToolMaterial.DIAMOND);
    public static final ItemSword wooden_halberd = new halberdtemplate("wooden halberd", Item.ToolMaterial.WOOD);
    public static final ItemSword stone_halberd = new halberdtemplate("stone halberd", Item.ToolMaterial.STONE);
    public static final ItemSword iron_halberd = new halberdtemplate("iron halberd", Item.ToolMaterial.IRON);
    public static final ItemSword gold_halberd = new halberdtemplate("gold halberd", Item.ToolMaterial.GOLD);

    public static final ItemShield shield = new shieldtemplate("empire shield", Config_enable.shields_included);
    public static final ItemShield shield2 = new shieldtemplate("dark elf shield", Config_enable.shields_included);
    public static final ItemShield shield3 = new shieldtemplate("high elf shield", Config_enable.shields_included);
    public static final ItemShield shield4 = new shieldtemplate("dwarf shield", Config_enable.shields_included);


}
