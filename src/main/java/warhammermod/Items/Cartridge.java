package warhammermod.Items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import warhammermod.util.Handler.inithandler.Itemsinit;

public class Cartridge extends Item {

        public Cartridge(String cartridge){
            setUnlocalizedName(cartridge);
            setCreativeTab(CreativeTabs.COMBAT);
            setRegistryName(cartridge);

            Itemsinit.ITEMS.add(this);
        }


}

