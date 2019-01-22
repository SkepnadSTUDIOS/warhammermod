package warhammermod.Items;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import warhammermod.util.Handler.inithandler.Itemsinit;

import java.util.Set;

public class heavytemplate extends ItemSword {

    private float attackSpeed;
    private float attackdamage;



    public heavytemplate(String name, Item.ToolMaterial material,float damage,float attspeed,boolean enabled){
        super(material);
        setUnlocalizedName(name);
        setCreativeTab(CreativeTabs.COMBAT);
        setRegistryName(name);
        attackSpeed=-attspeed;
        this.attackdamage = damage + material.getAttackDamage();;

        if(enabled){Itemsinit.ITEMS.add(this);};
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();;

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)this.attackdamage, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (double)this.attackSpeed, 0));
        }

        return multimap;
    }



}
