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
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import warhammermod.Entities.EntityHalberd;
import warhammermod.util.Handler.inithandler.Itemsinit;

public class halberdtemplate extends ItemSword {

    private float attackSpeed;
    private float attackdamage;
    private int cooldown=20;
    private int itemUseDuration = 72000;
    private ItemStack stack;
    private boolean canhit;

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
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {

            if (count == getMaxItemUseDuration(stack) - cooldown &&!player.world.isRemote){
                canhit=true;
                player.world.playSound(player.posX, player.posY, player.posZ, SoundEvents.ENTITY_HORSE_BREATHE, SoundCategory.PLAYERS, 1.0F, 1.0F, true);
            }
    }

    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
            if(canhit && !worldIn.isRemote && entityLiving instanceof EntityPlayer){

                EntityPlayer playerIn = (EntityPlayer) entityLiving;
                EntityHalberd halberdstrike = new EntityHalberd(worldIn, playerIn,attackdamage*1.3F);
                halberdstrike.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 3F, 0.2F);
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
                playerIn.getCooldownTracker().setCooldown(this, 40);

            }
    }





    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap = HashMultimap.create();

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)this.attackdamage, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (double)this.attackSpeed, 0));
        }

        return multimap;
    }

    public int getMaxItemUseDuration(ItemStack stack) { return 72000; }
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }


}
