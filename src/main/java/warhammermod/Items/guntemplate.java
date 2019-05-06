package warhammermod.Items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
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
import org.lwjgl.Sys;
import warhammermod.Entities.entitybullet;
import warhammermod.util.Handler.inithandler.Itemsinit;

import javax.annotation.Nullable;

public class guntemplate extends ItemBow {
    int magsize;
    int timetoreload;
    protected int ammocount = 0;
    protected NBTTagCompound ammocounter;
    private NBTTagCompound reloader;





    public boolean readytoFire;
    public int damage;


    public guntemplate(String name, int MagSize, int time, int damagein,int durability, boolean enabled) {
        setUnlocalizedName(name);
        setRegistryName(name);
        if(enabled){ Itemsinit.ITEMS.add(this);}
        setCreativeTab(CreativeTabs.COMBAT);
        magsize = MagSize;
        timetoreload = time;
        this.maxStackSize = 1;
        this.setMaxDamage(durability);
        damage = damagein;
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
            }
            if (playerIn.capabilities.isCreativeMode) {
                readytoFire = true;
            } else readytoFire = ammocounter.getInteger("ammo") > 0;
        }


        boolean flag = !this.findAmmo(playerIn).isEmpty();
        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, worldIn, playerIn, handIn, flag);
        if (ret != null) return ret;

        if (!playerIn.capabilities.isCreativeMode && !flag && !readytoFire) {
            return flag ? new ActionResult<>(EnumActionResult.PASS, itemstack) : new ActionResult<>(EnumActionResult.FAIL, itemstack);
        } else {
            playerIn.setActiveHand(handIn);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
    }



    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {

        if (!readytoFire && !player.world.isRemote) {
            if (count == getMaxItemUseDuration(stack) - timetoreload) {
                EntityPlayer entityplayer = (EntityPlayer) player;
                int ammoreserve = this.findAmmo(entityplayer).getCount();
                if ((ammoreserve > 0) && (!entityplayer.capabilities.isCreativeMode) ) {
                    int infinitylevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack);
                    if (ammoreserve < magsize) {
                        ammocount=ammoreserve;

                        if (infinitylevel == 0) {
                            this.findAmmo(entityplayer).shrink(ammoreserve);
                        }
                    } else {
                        ammocount=magsize;

                        if (infinitylevel == 0) {
                            this.findAmmo(entityplayer).shrink(magsize);
                        }
                    }
                }
            }
        }
        if ((count == getMaxItemUseDuration(stack) - timetoreload) && !readytoFire) {
            player.world.playSound(player.posX, player.posY, player.posZ, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, 1.0F, true);
        }

    }


    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer) entityLiving;
            System.out.println(getUnlocalizedName(stack));
            if(readytoFire) {
                worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1.0F, 1.35F / (itemRand.nextFloat() * 0.4F + 1.2F) + 1.0F * 0.5F);


                if (!worldIn.isRemote) {
                    entitybullet entitybullet = new entitybullet(worldIn, entityplayer, damage);
                    entitybullet.shoot(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, 4F, 0.2F);

                    int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
                    if (j > 0) {
                        entitybullet.setpowerDamage(j);
                    }
                    int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack) + 1;
                    if (k > 0) {
                        entitybullet.setknockbacklevel(k);
                    }

                    worldIn.spawnEntity(entitybullet);
                    if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
                        entitybullet.setFire(100);
                    }
                    stack.damageItem(1, entityplayer);

                }
            }


                if (!entityplayer.capabilities.isCreativeMode && !worldIn.isRemote) {
                    ammocounter = stack.getTagCompound();
                    if (ammocounter == null) {
                        ammocounter = new NBTTagCompound();
                        ammocounter.setInteger("ammo", ammocount);
                        ammocount=0;
                    }else if(ammocount>0){
                        ammocounter.setInteger("ammo",ammocount);
                    ammocount=0;}
                    else{
                        ammocounter.setInteger("ammo", ammocounter.getInteger("ammo") -1);
                    }
                    stack.setTagCompound(ammocounter);
                }
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

    private boolean isAmmo(ItemStack stack) {
        return stack.getItem() instanceof Cartridge;
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

