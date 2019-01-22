package warhammermod.Items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
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
import warhammermod.Entities.EntityGrenade;
import warhammermod.util.Handler.inithandler.Itemsinit;

import javax.annotation.Nullable;

public class grenadelaunchertemplate extends ItemBow {
    private int magsize;
    private int timetoreload;
    private int ammocount = 0;
    public boolean readytoFire;
    public int damage;
    private NBTTagCompound reloader;
    private NBTTagCompound ammocounter;


    public grenadelaunchertemplate(String name, int MagSize, int time, boolean enabled) {
        setUnlocalizedName(name);
        setRegistryName(name);
        if (enabled) {
            Itemsinit.ITEMS.add(this);
        }
        setCreativeTab(CreativeTabs.COMBAT);
        magsize = MagSize;
        timetoreload = time;
        this.maxStackSize = 1;
        this.setMaxDamage(384);
        this.addPropertyOverride(new ResourceLocation("reloading"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                reloader = stack.getTagCompound();
                return entityIn != null && entityIn instanceof EntityPlayer && !((EntityPlayer) entityIn).capabilities.isCreativeMode && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack && (reloader==null || reloader.getInteger("ammo")<=0) ? 1.0F : 0.0F;
            }
        });


    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (!worldIn.isRemote) {
            ammocounter = itemstack.getTagCompound();
            if (ammocounter == null) {
                ammocounter = new NBTTagCompound();
            }
            if (playerIn.capabilities.isCreativeMode) {
                readytoFire = true;
            } else if (ammocounter.getInteger("ammo") <= 0) {
                readytoFire = false;

            } else {
                readytoFire = true;
            }
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
            EntityPlayer entityplayer = (EntityPlayer) player;
            int ammoreserve = this.findAmmo(entityplayer).getCount();
            int infinitylevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack);
            if ((ammoreserve > 0) && (!entityplayer.capabilities.isCreativeMode)) {

                if (count == getMaxItemUseDuration(stack) - timetoreload) {
                    if (ammoreserve < magsize) {
                        ammocount = ammoreserve;
                        if (infinitylevel == 0) {
                            this.findAmmo(entityplayer).shrink(ammoreserve);
                        }
                    } else {
                        ammocount = magsize;
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
            if (readytoFire) {
                worldIn.playSound((EntityPlayer) null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 1.0F * 0.5F);


                if (!worldIn.isRemote) {
                    EntityGrenade entitybullet = new EntityGrenade(worldIn, entityplayer, damage);
                    float j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
                    entitybullet.shoot(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, 1.5F + 0.4F * j, 0.5F);


                    int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
                    if (k > 0) {
                        entitybullet.setknockbacklevel(k);
                    }
                    worldIn.spawnEntity(entitybullet);
                }
            }


            if (!entityplayer.capabilities.isCreativeMode && !worldIn.isRemote) {
                ammocounter = stack.getTagCompound();
                if (ammocounter == null) {
                    ammocounter = new NBTTagCompound();
                    ammocounter.setInteger("ammo", ammocount);
                    ammocount = 0;
                    stack.setTagCompound(ammocounter);
                } else if (ammocount > 0) {
                    ammocounter.setInteger("ammo", ammocount);
                    stack.setTagCompound(ammocounter);
                    ammocount = 0;
                } else {
                    ammocounter.setInteger("ammo", ammocounter.getInteger("ammo") - 1);

                    stack.setTagCompound(ammocounter);
                }
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

    public boolean canApplyAtEnchantingTable(ItemStack stack, net.minecraft.enchantment.Enchantment enchantment)
    {
        switch(Enchantment.getEnchantmentID(enchantment)){
            case 48:return true;
            case 49:return true;
            case 34:return true;
            default:return false;

        }
    }

    private boolean isAmmo(ItemStack stack) {
        return stack.getItem() instanceof Grenade;
    }
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }
    public int getItemEnchantability() { return 1; }

}
