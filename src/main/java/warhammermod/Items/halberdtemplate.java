package warhammermod.Items;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import warhammermod.util.Handler.inithandler.Itemsinit;

import java.util.Set;

public class halberdtemplate extends ItemTool {

    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet();


    public halberdtemplate(String name, Item.ToolMaterial material,boolean enabled){
        super(5F, -2.7F, material, EFFECTIVE_ON);
        setUnlocalizedName(name);
        setCreativeTab(CreativeTabs.COMBAT);
        setRegistryName(name);

        if(enabled){Itemsinit.ITEMS.add(this);}
    }
    public boolean canApplyAtEnchantingTable(ItemStack stack, net.minecraft.enchantment.Enchantment enchantment)
    {
        switch(enchantment.type){
            case WEAPON:return true;
            case BREAKABLE:return true;
            default: return false;
        }
    }

}