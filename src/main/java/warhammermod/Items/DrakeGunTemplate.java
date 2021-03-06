package warhammermod.Items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import warhammermod.util.Handler.inithandler.Itemsinit;

import javax.annotation.Nullable;

import static net.minecraft.init.Items.BLAZE_ROD;

public class DrakeGunTemplate extends ItemBow {
    private int magsize;
    private int timetoreload;
    private int ammocount = 0;
    private NBTTagCompound ammocounter;
    private NBTTagCompound reloader;





    public boolean needtoreload;


    public DrakeGunTemplate(String name, int MagSize, int time, boolean enabled) {
        setUnlocalizedName(name);
        setRegistryName(name);
        if(enabled){ Itemsinit.ITEMS.add(this);}
        setCreativeTab(CreativeTabs.COMBAT);
        magsize = MagSize;
        timetoreload = time;
        this.maxStackSize = 1;
        this.setMaxDamage(200);

        this.addPropertyOverride(new ResourceLocation("reloading"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                reloader=stack.getTagCompound();
                return entityIn != null && entityIn instanceof EntityPlayer && !((EntityPlayer) entityIn).capabilities.isCreativeMode && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack  && (reloader==null || reloader.getInteger("ammo")<=0) ? 1.0F : 0.0F;
            }
        });
    }
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if(!worldIn.isRemote) {
            ammocounter = itemstack.getTagCompound();
            if (ammocounter == null) {
                ammocounter = new NBTTagCompound();
                ammocounter.setInteger("ammo", ammocount);
                ammocount=0;
            }

            if (!playerIn.capabilities.isCreativeMode && ammocounter.getInteger("ammo") <= 0) {
                needtoreload = true;

            } else {
                EntitySmallFireball entitysmallfireball = new EntitySmallFireball(worldIn, playerIn, playerIn.getLookVec().x*5 /*+ playerIn.getRNG().nextGaussian()*10*/, playerIn.getLookVec().y*5, playerIn.getLookVec().z*5 /*+ playerIn.getRNG().nextGaussian()*10*/);
                entitysmallfireball.posY = playerIn.posY + (double)(playerIn.height / 2.0F +0.1F);
                playerIn.world.spawnEntity(entitysmallfireball);
                ammocounter.setInteger("ammo", ammocounter.getInteger("ammo") -1);
                playerIn.getHeldItem(handIn).setTagCompound(ammocounter);
                needtoreload=false;

            }

        }
        boolean flag = !this.findAmmo(playerIn).isEmpty();
        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, worldIn, playerIn, handIn, flag);
        if (ret != null) return ret;

        if (!needtoreload) {
            if(worldIn.isRemote){ worldIn.playSound(playerIn.posX,playerIn.posY,playerIn.posZ,SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE,SoundCategory.PLAYERS,0.1F,-1.0F,true); }
            return new ActionResult<>(EnumActionResult.PASS, itemstack);
        } else if (!flag){
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        } else {
            playerIn.setActiveHand(handIn);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS,itemstack);}
    }



    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {

        if (needtoreload && !player.world.isRemote) {
            if (count == getMaxItemUseDuration(stack) - timetoreload) {
                EntityPlayer entityplayer = (EntityPlayer) player;
                int ammoreserve = this.findAmmo(entityplayer).getCount();
                if ((ammoreserve > 0) && (!entityplayer.capabilities.isCreativeMode) ) {
                    this.findAmmo(entityplayer).shrink(1);
                }
            }
        }
        if ((count == getMaxItemUseDuration(stack) - timetoreload)) {
            player.world.playSound(player.posX, player.posY, player.posZ, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, 1.0F, true);
        }

    }


    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer && !worldIn.isRemote) {
            EntityPlayer entityplayer = (EntityPlayer) entityLiving;
                if (!entityplayer.capabilities.isCreativeMode) {
                    ammocounter = stack.getTagCompound();
                    if (ammocounter == null) {
                        ammocounter = new NBTTagCompound();
                        ammocounter.setInteger("ammo", magsize);
                        ammocount=0;
                    } else ammocounter.setInteger("ammo",magsize);
                    stack.setTagCompound(ammocounter);
                }
            stack.damageItem(1, entityplayer);

        }
    }







    private ItemStack findAmmo(EntityPlayer player) {
        if (this.isAmmo(player.getHeldItem(EnumHand.OFF_HAND))) {
            return player.getHeldItem(EnumHand.OFF_HAND);
        } else if (this.isAmmo(player.getHeldItem(EnumHand.MAIN_HAND))) {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                ItemStack itemstack = player.inventory.getStackInSlot(i);

                if (this.isAmmo(itemstack)) {
                    return itemstack;
                }
            }

            return ItemStack.EMPTY;
        }
    }
    public boolean canApplyAtEnchantingTable(ItemStack stack, net.minecraft.enchantment.Enchantment enchantment)
    {
        switch(Enchantment.getEnchantmentID(enchantment)){
            case 34:return true;
            case 70:return true;
            default:return false;

        }
    }

    private boolean isAmmo(ItemStack stack) {
        return stack.getItem() == BLAZE_ROD;
    }
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }
    public int getItemEnchantability() { return 1; }
    private int getAmmocount(ItemStack item,int ammo){
        return 0;
    }
}

