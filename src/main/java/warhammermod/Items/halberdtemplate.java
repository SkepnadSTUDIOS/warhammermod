package warhammermod.Items;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.Sys;
import warhammermod.Entities.EntityHalberd;
import warhammermod.Entities.entitybullet;
import warhammermod.util.Handler.inithandler.Itemsinit;

import javax.annotation.Nullable;

public class halberdtemplate extends ItemSword {

    private float attackSpeed;
    private float attackdamage;
    private int cooldown=0;
    private int itemUseDuration = 72000;
    private ItemStack stack;

    public halberdtemplate(String name, Item.ToolMaterial material,float damage,float attspeed,boolean enabled){
        super(material);
        setUnlocalizedName(name);
        setCreativeTab(CreativeTabs.COMBAT);
        setRegistryName(name);
        attackSpeed=-attspeed;
        this.attackdamage = damage + material.getAttackDamage();

        if(enabled){Itemsinit.ITEMS.add(this);}

        /*this.addPropertyOverride(new ResourceLocation("powerhit"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                return  entityIn instanceof EntityPlayer && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });*/
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        if(!worldIn.isRemote){
            stack=playerIn.getHeldItem(handIn);
            EntityHalberd halberdstrike = new EntityHalberd(worldIn, playerIn,attackdamage*1.3F);
            halberdstrike.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 4F, 0.2F);
            int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack);
            if (j > 0) {
                halberdstrike.setpowerDamage(j);
            }
            int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.KNOCKBACK, stack) + 1;
            if (k > 0) {
                halberdstrike.setknockbacklevel(k);
            }
            worldIn.spawnEntity(halberdstrike);
            stack.damageItem(3, playerIn);
            playerIn.getCooldownTracker().setCooldown(this, 20);
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
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