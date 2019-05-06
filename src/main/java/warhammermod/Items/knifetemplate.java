package warhammermod.Items;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import warhammermod.util.Handler.inithandler.Itemsinit;
import warhammermod.util.confighandler.confighandler;
import warhammermod.util.confighandler.confighandler.getvalues;

import java.util.Set;

public class knifetemplate extends ItemTool {

    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet();

    public knifetemplate(String name, Item.ToolMaterial material){
        super(getvalues.getknd, -getvalues.getkns, material, EFFECTIVE_ON);
        setUnlocalizedName(name);
        setCreativeTab(CreativeTabs.COMBAT);
        setRegistryName(name);


        if(confighandler.Config_enable.knife_included){Itemsinit.ITEMS.add(this);}
    }
    public boolean canApplyAtEnchantingTable(ItemStack stack,float damage,float attspeed, net.minecraft.enchantment.Enchantment enchantment)
    {
        switch(enchantment.type){
            case WEAPON:return true;
            case BREAKABLE:return true;
            default: return false;
        }
    }



}
