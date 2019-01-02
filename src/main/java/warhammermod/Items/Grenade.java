package warhammermod.Items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import warhammermod.util.Handler.inithandler.Itemsinit;
import warhammermod.util.confighandler.confighandler;

public class Grenade extends Item {

    public Grenade(String cartridge,boolean enabled){
        setUnlocalizedName(cartridge);
        setCreativeTab(CreativeTabs.COMBAT);
        setRegistryName(cartridge);
        if(enabled){
        Itemsinit.ITEMS.add(this);}
    }


}