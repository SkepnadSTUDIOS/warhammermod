package warhammermod.Entities.living;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.*;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import warhammermod.Entities.living.Emanager.EntityAIDwarfMate;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Random;

public class EntityDwarf extends EntityVillager
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final DataParameter<Integer> PROFESSION = EntityDataManager.createKey(EntityDwarf.class, DataSerializers.VARINT);
    private int randomTickDivider;
    private boolean isMating;
    private boolean isPlaying;
    Village village;
    /** This villager's current customer. */
    @Nullable
    private EntityPlayer buyingPlayer;
    /** Initialises the MerchantRecipeList.java */
    @Nullable
    private MerchantRecipeList buyingList;
    private int timeUntilReset;
    /** addDefaultEquipmentAndRecipies is called if this is true */
    private boolean needsInitilization;
    private boolean isWillingToMate;
    private int wealth;
    /** Last player to trade with this villager, used for aggressivity. */
    private java.util.UUID lastBuyingPlayer;
    private int careerId;
    /** This is the EntityVillager's career level value */
    private int careerLevel;
    private boolean isLookingForHome;
    private boolean areAdditionalTasksSet;
    private final InventoryBasic villagerInventory;
    /** A multi-dimensional array mapping the various professions, careers and career levels that a Villager may offer */
    private static final EntityDwarf.ITradeList[][][][] DEFAULT_TRADE_LIST_MAP = new EntityDwarf.ITradeList[][][][] {{{{new EntityDwarf.EmeraldForItems(Items.WHEAT, new EntityDwarf.PriceInfo(18, 22)), new EntityDwarf.EmeraldForItems(Items.POTATO, new EntityDwarf.PriceInfo(15, 19)), new EntityDwarf.EmeraldForItems(Items.CARROT, new EntityDwarf.PriceInfo(15, 19)), new EntityDwarf.ListItemForEmeralds(Items.BREAD, new EntityDwarf.PriceInfo(-4, -2))}, {new EntityDwarf.EmeraldForItems(Item.getItemFromBlock(Blocks.PUMPKIN), new EntityDwarf.PriceInfo(8, 13)), new EntityDwarf.ListItemForEmeralds(Items.PUMPKIN_PIE, new EntityDwarf.PriceInfo(-3, -2))}, {new EntityDwarf.EmeraldForItems(Item.getItemFromBlock(Blocks.MELON_BLOCK), new EntityDwarf.PriceInfo(7, 12)), new EntityDwarf.ListItemForEmeralds(Items.APPLE, new EntityDwarf.PriceInfo(-7, -5))}, {new EntityDwarf.ListItemForEmeralds(Items.COOKIE, new EntityDwarf.PriceInfo(-10, -6)), new EntityDwarf.ListItemForEmeralds(Items.CAKE, new EntityDwarf.PriceInfo(1, 1))}}, {{new EntityDwarf.EmeraldForItems(Items.STRING, new EntityDwarf.PriceInfo(15, 20)), new EntityDwarf.EmeraldForItems(Items.COAL, new EntityDwarf.PriceInfo(16, 24)), new EntityDwarf.ItemAndEmeraldToItem(Items.FISH, new EntityDwarf.PriceInfo(6, 6), Items.COOKED_FISH, new EntityDwarf.PriceInfo(6, 6))}, {new EntityDwarf.ListEnchantedItemForEmeralds(Items.FISHING_ROD, new EntityDwarf.PriceInfo(7, 8))}}, {{new EntityDwarf.EmeraldForItems(Item.getItemFromBlock(Blocks.WOOL), new EntityDwarf.PriceInfo(16, 22)), new EntityDwarf.ListItemForEmeralds(Items.SHEARS, new EntityDwarf.PriceInfo(3, 4))}, {new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL)), new EntityDwarf.PriceInfo(1, 2)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 1), new EntityDwarf.PriceInfo(1, 2)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 2), new EntityDwarf.PriceInfo(1, 2)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 3), new EntityDwarf.PriceInfo(1, 2)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 4), new EntityDwarf.PriceInfo(1, 2)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 5), new EntityDwarf.PriceInfo(1, 2)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 6), new EntityDwarf.PriceInfo(1, 2)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 7), new EntityDwarf.PriceInfo(1, 2)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 8), new EntityDwarf.PriceInfo(1, 2)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 9), new EntityDwarf.PriceInfo(1, 2)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 10), new EntityDwarf.PriceInfo(1, 2)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 11), new EntityDwarf.PriceInfo(1, 2)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 12), new EntityDwarf.PriceInfo(1, 2)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 13), new EntityDwarf.PriceInfo(1, 2)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 14), new EntityDwarf.PriceInfo(1, 2)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 15), new EntityDwarf.PriceInfo(1, 2))}}, {{new EntityDwarf.EmeraldForItems(Items.STRING, new EntityDwarf.PriceInfo(15, 20)), new EntityDwarf.ListItemForEmeralds(Items.ARROW, new EntityDwarf.PriceInfo(-12, -8))}, {new EntityDwarf.ListItemForEmeralds(Items.BOW, new EntityDwarf.PriceInfo(2, 3)), new EntityDwarf.ItemAndEmeraldToItem(Item.getItemFromBlock(Blocks.GRAVEL), new EntityDwarf.PriceInfo(10, 10), Items.FLINT, new EntityDwarf.PriceInfo(6, 10))}}}, {{{new EntityDwarf.EmeraldForItems(Items.PAPER, new EntityDwarf.PriceInfo(24, 36)), new EntityDwarf.ListEnchantedBookForEmeralds()}, {new EntityDwarf.EmeraldForItems(Items.BOOK, new EntityDwarf.PriceInfo(8, 10)), new EntityDwarf.ListItemForEmeralds(Items.COMPASS, new EntityDwarf.PriceInfo(10, 12)), new EntityDwarf.ListItemForEmeralds(Item.getItemFromBlock(Blocks.BOOKSHELF), new EntityDwarf.PriceInfo(3, 4))}, {new EntityDwarf.EmeraldForItems(Items.WRITTEN_BOOK, new EntityDwarf.PriceInfo(2, 2)), new EntityDwarf.ListItemForEmeralds(Items.CLOCK, new EntityDwarf.PriceInfo(10, 12)), new EntityDwarf.ListItemForEmeralds(Item.getItemFromBlock(Blocks.GLASS), new EntityDwarf.PriceInfo(-5, -3))}, {new EntityDwarf.ListEnchantedBookForEmeralds()}, {new EntityDwarf.ListEnchantedBookForEmeralds()}, {new EntityDwarf.ListItemForEmeralds(Items.NAME_TAG, new EntityDwarf.PriceInfo(20, 22))}}, {{new EntityDwarf.EmeraldForItems(Items.PAPER, new EntityDwarf.PriceInfo(24, 36))}, {new EntityDwarf.EmeraldForItems(Items.COMPASS, new EntityDwarf.PriceInfo(1, 1))}, {new EntityDwarf.ListItemForEmeralds(Items.MAP, new EntityDwarf.PriceInfo(7, 11))}, {new EntityDwarf.TreasureMapForEmeralds(new EntityDwarf.PriceInfo(12, 20), "Monument", MapDecoration.Type.MONUMENT), new EntityDwarf.TreasureMapForEmeralds(new EntityDwarf.PriceInfo(16, 28), "Mansion", MapDecoration.Type.MANSION)}}}, {{{new EntityDwarf.EmeraldForItems(Items.ROTTEN_FLESH, new EntityDwarf.PriceInfo(36, 40)), new EntityDwarf.EmeraldForItems(Items.GOLD_INGOT, new EntityDwarf.PriceInfo(8, 10))}, {new EntityDwarf.ListItemForEmeralds(Items.REDSTONE, new EntityDwarf.PriceInfo(-4, -1)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()), new EntityDwarf.PriceInfo(-2, -1))}, {new EntityDwarf.ListItemForEmeralds(Items.ENDER_PEARL, new EntityDwarf.PriceInfo(4, 7)), new EntityDwarf.ListItemForEmeralds(Item.getItemFromBlock(Blocks.GLOWSTONE), new EntityDwarf.PriceInfo(-3, -1))}, {new EntityDwarf.ListItemForEmeralds(Items.EXPERIENCE_BOTTLE, new EntityDwarf.PriceInfo(3, 11))}}}, {{{new EntityDwarf.EmeraldForItems(Items.COAL, new EntityDwarf.PriceInfo(16, 24)), new EntityDwarf.ListItemForEmeralds(Items.IRON_HELMET, new EntityDwarf.PriceInfo(4, 6))}, {new EntityDwarf.EmeraldForItems(Items.IRON_INGOT, new EntityDwarf.PriceInfo(7, 9)), new EntityDwarf.ListItemForEmeralds(Items.IRON_CHESTPLATE, new EntityDwarf.PriceInfo(10, 14))}, {new EntityDwarf.EmeraldForItems(Items.DIAMOND, new EntityDwarf.PriceInfo(3, 4)), new EntityDwarf.ListEnchantedItemForEmeralds(Items.DIAMOND_CHESTPLATE, new EntityDwarf.PriceInfo(16, 19))}, {new EntityDwarf.ListItemForEmeralds(Items.CHAINMAIL_BOOTS, new EntityDwarf.PriceInfo(5, 7)), new EntityDwarf.ListItemForEmeralds(Items.CHAINMAIL_LEGGINGS, new EntityDwarf.PriceInfo(9, 11)), new EntityDwarf.ListItemForEmeralds(Items.CHAINMAIL_HELMET, new EntityDwarf.PriceInfo(5, 7)), new EntityDwarf.ListItemForEmeralds(Items.CHAINMAIL_CHESTPLATE, new EntityDwarf.PriceInfo(11, 15))}}, {{new EntityDwarf.EmeraldForItems(Items.COAL, new EntityDwarf.PriceInfo(16, 24)), new EntityDwarf.ListItemForEmeralds(Items.IRON_AXE, new EntityDwarf.PriceInfo(6, 8))}, {new EntityDwarf.EmeraldForItems(Items.IRON_INGOT, new EntityDwarf.PriceInfo(7, 9)), new EntityDwarf.ListEnchantedItemForEmeralds(Items.IRON_SWORD, new EntityDwarf.PriceInfo(9, 10))}, {new EntityDwarf.EmeraldForItems(Items.DIAMOND, new EntityDwarf.PriceInfo(3, 4)), new EntityDwarf.ListEnchantedItemForEmeralds(Items.DIAMOND_SWORD, new EntityDwarf.PriceInfo(12, 15)), new EntityDwarf.ListEnchantedItemForEmeralds(Items.DIAMOND_AXE, new EntityDwarf.PriceInfo(9, 12))}}, {{new EntityDwarf.EmeraldForItems(Items.COAL, new EntityDwarf.PriceInfo(16, 24)), new EntityDwarf.ListEnchantedItemForEmeralds(Items.IRON_SHOVEL, new EntityDwarf.PriceInfo(5, 7))}, {new EntityDwarf.EmeraldForItems(Items.IRON_INGOT, new EntityDwarf.PriceInfo(7, 9)), new EntityDwarf.ListEnchantedItemForEmeralds(Items.IRON_PICKAXE, new EntityDwarf.PriceInfo(9, 11))}, {new EntityDwarf.EmeraldForItems(Items.DIAMOND, new EntityDwarf.PriceInfo(3, 4)), new EntityDwarf.ListEnchantedItemForEmeralds(Items.DIAMOND_PICKAXE, new EntityDwarf.PriceInfo(12, 15))}}}, {{{new EntityDwarf.EmeraldForItems(Items.PORKCHOP, new EntityDwarf.PriceInfo(14, 18)), new EntityDwarf.EmeraldForItems(Items.CHICKEN, new EntityDwarf.PriceInfo(14, 18))}, {new EntityDwarf.EmeraldForItems(Items.COAL, new EntityDwarf.PriceInfo(16, 24)), new EntityDwarf.ListItemForEmeralds(Items.COOKED_PORKCHOP, new EntityDwarf.PriceInfo(-7, -5)), new EntityDwarf.ListItemForEmeralds(Items.COOKED_CHICKEN, new EntityDwarf.PriceInfo(-8, -6))}}, {{new EntityDwarf.EmeraldForItems(Items.LEATHER, new EntityDwarf.PriceInfo(9, 12)), new EntityDwarf.ListItemForEmeralds(Items.LEATHER_LEGGINGS, new EntityDwarf.PriceInfo(2, 4))}, {new EntityDwarf.ListEnchantedItemForEmeralds(Items.LEATHER_CHESTPLATE, new EntityDwarf.PriceInfo(7, 12))}, {new EntityDwarf.ListItemForEmeralds(Items.SADDLE, new EntityDwarf.PriceInfo(8, 10))}}}, {new EntityDwarf.ITradeList[0][]}};

    public EntityDwarf(World worldIn)
    {
        this(worldIn, 0);
    }

    public EntityDwarf(World worldIn, int professionId)
    {
        super(worldIn,professionId);
        this.villagerInventory = new InventoryBasic("Items", false, 8);
    }

    protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityEvoker.class, 12.0F, 0.8D, 0.8D));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityVindicator.class, 8.0F, 0.8D, 0.8D));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityVex.class, 8.0F, 0.6D, 0.6D));
        this.tasks.addTask(1, new EntityAITradePlayer(this));
        this.tasks.addTask(1, new EntityAILookAtTradePlayer(this));
        this.tasks.addTask(2, new EntityAIMoveIndoors(this));
        this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6D));
        this.tasks.addTask(6, new EntityAIDwarfMate(this));
        this.tasks.addTask(7, new EntityAIFollowGolem(this));
        this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(9, new EntityAIVillagerInteract(this));
        this.tasks.addTask(9, new EntityAIWanderAvoidWater(this, 0.6D));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
    }

    private void setAdditionalAItasks()
    {
        if (!this.areAdditionalTasksSet)
        {
            this.areAdditionalTasksSet = true;

            if (this.isChild())
            {
                this.tasks.addTask(8, new EntityAIPlay(this, 0.32D));
            }
            else if (this.getProfession() == 0)
            {
                this.tasks.addTask(6, new EntityAIHarvestFarmland(this, 0.6D));
            }
        }
    }

    /**
     * This is called when Entity's growing age timer reaches 0 (negative values are considered as a child, positive as
     * an adult)
     */
    protected void onGrowingAdult()
    {
        if (this.getProfession() == 0)
        {
            this.tasks.addTask(8, new EntityAIHarvestFarmland(this, 0.6D));
        }

        super.onGrowingAdult();
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
    }

    protected void updateAITasks()
    {
        if (--this.randomTickDivider <= 0)
        {
            BlockPos blockpos = new BlockPos(this);
            this.world.getVillageCollection().addToVillagerPositionList(blockpos);
            this.randomTickDivider = 70 + this.rand.nextInt(50);
            this.village = this.world.getVillageCollection().getNearestVillage(blockpos, 32);

            if (this.village == null)
            {
                this.detachHome();
            }
            else
            {
                BlockPos blockpos1 = this.village.getCenter();
                this.setHomePosAndDistance(blockpos1, this.village.getVillageRadius());

                if (this.isLookingForHome)
                {
                    this.isLookingForHome = false;
                    this.village.setDefaultPlayerReputation(5);
                }
            }
        }

        if (!this.isTrading() && this.timeUntilReset > 0)
        {
            --this.timeUntilReset;

            if (this.timeUntilReset <= 0)
            {
                if (this.needsInitilization)
                {
                    for (MerchantRecipe merchantrecipe : this.buyingList)
                    {
                        if (merchantrecipe.isRecipeDisabled())
                        {
                            merchantrecipe.increaseMaxTradeUses(this.rand.nextInt(6) + this.rand.nextInt(6) + 2);
                        }
                    }

                    this.populateBuyingList();
                    this.needsInitilization = false;

                    if (this.village != null && this.lastBuyingPlayer != null)
                    {
                        this.world.setEntityState(this, (byte)14);
                        this.village.modifyPlayerReputation(this.lastBuyingPlayer, 1);
                    }
                }

                this.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 0));
            }
        }

        super.updateAITasks();
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(PROFESSION, Integer.valueOf(0));
    }


    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn()
    {
        return false;
    }

    protected SoundEvent getAmbientSound()
    {
        return this.isTrading() ? SoundEvents.ENTITY_VILLAGER_TRADING : SoundEvents.ENTITY_VILLAGER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_VILLAGER_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_VILLAGER_DEATH;
    }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return LootTableList.ENTITIES_VILLAGER;
    }



    private void populateBuyingList()
    {
        if (this.careerId != 0 && this.careerLevel != 0)
        {
            ++this.careerLevel;
        }
        else
        {
            this.careerId = this.getProfessionForge().getRandomCareer(this.rand) + 1;
            this.careerLevel = 1;
        }

        if (this.buyingList == null)
        {
            this.buyingList = new MerchantRecipeList();
        }

        int i = this.careerId - 1;
        int j = this.careerLevel - 1;
        java.util.List<EntityDwarf.ITradeList> trades = this.getProfessionForge().getCareer(i).getTrades(j);

        if (trades != null)
        {
            for (EntityVillager.ITradeList entityvillager$itradelist : trades)
            {
                entityvillager$itradelist.addMerchantRecipe(this, this.buyingList, this.rand);
            }
        }
    }







    @SideOnly(Side.CLIENT)
    private void spawnParticles(EnumParticleTypes particleType)
    {
        for (int i = 0; i < 5; ++i)
        {
            double d0 = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            double d2 = this.rand.nextGaussian() * 0.02D;
            this.world.spawnParticle(particleType, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 1.0D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d0, d1, d2);
        }
    }

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */


    protected void updateEquipmentIfNeeded(EntityItem itemEntity)
    {
        ItemStack itemstack = itemEntity.getItem();
        Item item = itemstack.getItem();

        if (this.canVillagerPickupItem(item))
        {
            ItemStack itemstack1 = this.villagerInventory.addItem(itemstack);

            if (itemstack1.isEmpty())
            {
                itemEntity.setDead();
            }
            else
            {
                itemstack.setCount(itemstack1.getCount());
            }
        }
    }

    private boolean canVillagerPickupItem(Item itemIn)
    {
        return itemIn == Items.BREAD || itemIn == Items.POTATO || itemIn == Items.CARROT || itemIn == Items.WHEAT || itemIn == Items.WHEAT_SEEDS || itemIn == Items.BEETROOT || itemIn == Items.BEETROOT_SEEDS;
    }



    /**
     * Returns true if villager has enough items in inventory
     */
    private boolean hasEnoughItems(int multiplier)
    {
        boolean flag = this.getProfession() == 0;

        for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i)
        {
            ItemStack itemstack = this.villagerInventory.getStackInSlot(i);

            if (!itemstack.isEmpty())
            {
                if (itemstack.getItem() == Items.BREAD && itemstack.getCount() >= 3 * multiplier || itemstack.getItem() == Items.POTATO && itemstack.getCount() >= 12 * multiplier || itemstack.getItem() == Items.CARROT && itemstack.getCount() >= 12 * multiplier || itemstack.getItem() == Items.BEETROOT && itemstack.getCount() >= 12 * multiplier)
                {
                    return true;
                }

                if (flag && itemstack.getItem() == Items.WHEAT && itemstack.getCount() >= 9 * multiplier)
                {
                    return true;
                }
            }
        }

        return false;
    }


    static class TreasureMapForEmeralds implements EntityDwarf.ITradeList
        {
            public EntityDwarf.PriceInfo value;
            public String destination;
            public MapDecoration.Type destinationType;

            public TreasureMapForEmeralds(EntityDwarf.PriceInfo p_i47340_1_, String p_i47340_2_, MapDecoration.Type p_i47340_3_)
            {
                this.value = p_i47340_1_;
                this.destination = p_i47340_2_;
                this.destinationType = p_i47340_3_;
            }

            public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
            {
                int i = this.value.getPrice(random);
                World world = merchant.getWorld();
                BlockPos blockpos = world.findNearestStructure(this.destination, merchant.getPos(), true);

                if (blockpos != null)
                {
                    ItemStack itemstack = ItemMap.setupNewMap(world, (double)blockpos.getX(), (double)blockpos.getZ(), (byte)2, true, true);
                    ItemMap.renderBiomePreviewMap(world, itemstack);
                    MapData.addTargetDecoration(itemstack, blockpos, "+", this.destinationType);
                    itemstack.setTranslatableName("filled_map." + this.destination.toLowerCase(Locale.ROOT));
                    recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, i), new ItemStack(Items.COMPASS), itemstack));
                }
            }
        }
    /*{{
        {
            {new EntityDwarf.EmeraldForItems(Items.WHEAT, new EntityDwarf.PriceInfo(18, 22)), new EntityDwarf.EmeraldForItems(Items.POTATO, new EntityDwarf.PriceInfo(15, 19)), new EntityDwarf.EmeraldForItems(Items.CARROT, new EntityDwarf.PriceInfo(15, 19)), new EntityDwarf.ListItemForEmeralds(Items.BREAD, new EntityDwarf.PriceInfo(-4, -2))},
            {new EntityDwarf.EmeraldForItems(Item.getItemFromBlock(Blocks.PUMPKIN), new EntityDwarf.PriceInfo(8, 13)), new EntityDwarf.ListItemForEmeralds(Items.PUMPKIN_PIE, new EntityDwarf.PriceInfo(-3, -2))},
            {new EntityDwarf.EmeraldForItems(Item.getItemFromBlock(Blocks.MELON_BLOCK), new EntityDwarf.PriceInfo(7, 12)), new EntityDwarf.ListItemForEmeralds(Items.APPLE, new EntityDwarf.PriceInfo(-7, -5))},
            {new EntityDwarf.ListItemForEmeralds(Items.COOKIE, new EntityDwarf.PriceInfo(-10, -6)), new EntityDwarf.ListItemForEmeralds(Items.CAKE, new EntityDwarf.PriceInfo(1, 1))}
        }    ,



        {
            {new EntityDwarf.EmeraldForItems(Items.STRING, new EntityDwarf.PriceInfo(15, 20)), new EntityDwarf.EmeraldForItems(Items.COAL, new EntityDwarf.PriceInfo(16, 24)), new EntityDwarf.ItemAndEmeraldToItem(Items.FISH, new EntityDwarf.PriceInfo(6, 6), Items.COOKED_FISH, new EntityDwarf.PriceInfo(6, 6))},
            {new EntityDwarf.ListEnchantedItemForEmeralds(Items.FISHING_ROD, new EntityDwarf.PriceInfo(7, 8))}
        },



        {
            {new EntityDwarf.EmeraldForItems(Item.getItemFromBlock(Blocks.WOOL), new EntityDwarf.PriceInfo(16, 22)), new EntityDwarf.ListItemForEmeralds(Items.SHEARS, new EntityDwarf.PriceInfo(3, 4))}, {new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL)), new EntityDwarf.PriceInfo(1, 2)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 1), new EntityDwarf.PriceInfo(1, 2)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 2), new EntityDwarf.PriceInfo(1, 2)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 3), new EntityDwarf.PriceInfo(1, 2)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 4), new EntityDwarf.PriceInfo(1, 2)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 5), new EntityDwarf.PriceInfo(1, 2)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 6), new EntityDwarf.PriceInfo(1, 2)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 7), new EntityDwarf.PriceInfo(1, 2)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 8), new EntityDwarf.PriceInfo(1, 2)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 9), new EntityDwarf.PriceInfo(1, 2)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 10), new EntityDwarf.PriceInfo(1, 2)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 11), new EntityDwarf.PriceInfo(1, 2)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 12), new EntityDwarf.PriceInfo(1, 2)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 13), new EntityDwarf.PriceInfo(1, 2)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 14), new EntityDwarf.PriceInfo(1, 2)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 15), new EntityDwarf.PriceInfo(1, 2))}
        }    ,


        {
            {new EntityDwarf.EmeraldForItems(Items.STRING, new EntityDwarf.PriceInfo(15, 20)), new EntityDwarf.ListItemForEmeralds(Items.ARROW, new EntityDwarf.PriceInfo(-12, -8))},
            {new EntityDwarf.ListItemForEmeralds(Items.BOW, new EntityDwarf.PriceInfo(2, 3)), new EntityDwarf.ItemAndEmeraldToItem(Item.getItemFromBlock(Blocks.GRAVEL), new EntityDwarf.PriceInfo(10, 10), Items.FLINT, new EntityDwarf.PriceInfo(6, 10))}
        }
    }   ,
        {
            {
                {new EntityDwarf.EmeraldForItems(Items.PAPER, new EntityDwarf.PriceInfo(24, 36)), new EntityDwarf.ListEnchantedBookForEmeralds()},
                {new EntityDwarf.EmeraldForItems(Items.BOOK, new EntityDwarf.PriceInfo(8, 10)), new EntityDwarf.ListItemForEmeralds(Items.COMPASS, new EntityDwarf.PriceInfo(10, 12)), new EntityDwarf.ListItemForEmeralds(Item.getItemFromBlock(Blocks.BOOKSHELF), new EntityDwarf.PriceInfo(3, 4))},
                {new EntityDwarf.EmeraldForItems(Items.WRITTEN_BOOK, new EntityDwarf.PriceInfo(2, 2)), new EntityDwarf.ListItemForEmeralds(Items.CLOCK, new EntityDwarf.PriceInfo(10, 12)), new EntityDwarf.ListItemForEmeralds(Item.getItemFromBlock(Blocks.GLASS), new EntityDwarf.PriceInfo(-5, -3))},
                {new EntityDwarf.ListEnchantedBookForEmeralds()},
                {new EntityDwarf.ListEnchantedBookForEmeralds()},
                {new EntityDwarf.ListItemForEmeralds(Items.NAME_TAG, new EntityDwarf.PriceInfo(20, 22))}
                },

            {{new EntityDwarf.EmeraldForItems(Items.PAPER, new EntityDwarf.PriceInfo(24, 36))}, {new EntityDwarf.EmeraldForItems(Items.COMPASS, new EntityDwarf.PriceInfo(1, 1))}, {new EntityDwarf.ListItemForEmeralds(Items.MAP, new EntityDwarf.PriceInfo(7, 11))}, {new EntityDwarf.TreasureMapForEmeralds(new EntityDwarf.PriceInfo(12, 20), "Monument", MapDecoration.Type.MONUMENT), new EntityDwarf.TreasureMapForEmeralds(new EntityDwarf.PriceInfo(16, 28), "Mansion", MapDecoration.Type.MANSION)}}
            },



        {{{new EntityDwarf.EmeraldForItems(Items.ROTTEN_FLESH, new EntityDwarf.PriceInfo(36, 40)), new EntityDwarf.EmeraldForItems(Items.GOLD_INGOT, new EntityDwarf.PriceInfo(8, 10))}, {new EntityDwarf.ListItemForEmeralds(Items.REDSTONE, new EntityDwarf.PriceInfo(-4, -1)), new EntityDwarf.ListItemForEmeralds(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()), new EntityDwarf.PriceInfo(-2, -1))}, {new EntityDwarf.ListItemForEmeralds(Items.ENDER_PEARL, new EntityDwarf.PriceInfo(4, 7)), new EntityDwarf.ListItemForEmeralds(Item.getItemFromBlock(Blocks.GLOWSTONE), new EntityDwarf.PriceInfo(-3, -1))}, {new EntityDwarf.ListItemForEmeralds(Items.EXPERIENCE_BOTTLE, new EntityDwarf.PriceInfo(3, 11))}}},



        {{{new EntityDwarf.EmeraldForItems(Items.COAL, new EntityDwarf.PriceInfo(16, 24)), new EntityDwarf.ListItemForEmeralds(Items.IRON_HELMET, new EntityDwarf.PriceInfo(4, 6))}, {new EntityDwarf.EmeraldForItems(Items.IRON_INGOT, new EntityDwarf.PriceInfo(7, 9)), new EntityDwarf.ListItemForEmeralds(Items.IRON_CHESTPLATE, new EntityDwarf.PriceInfo(10, 14))}, {new EntityDwarf.EmeraldForItems(Items.DIAMOND, new EntityDwarf.PriceInfo(3, 4)), new EntityDwarf.ListEnchantedItemForEmeralds(Items.DIAMOND_CHESTPLATE, new EntityDwarf.PriceInfo(16, 19))}, {new EntityDwarf.ListItemForEmeralds(Items.CHAINMAIL_BOOTS, new EntityDwarf.PriceInfo(5, 7)), new EntityDwarf.ListItemForEmeralds(Items.CHAINMAIL_LEGGINGS, new EntityDwarf.PriceInfo(9, 11)), new EntityDwarf.ListItemForEmeralds(Items.CHAINMAIL_HELMET, new EntityDwarf.PriceInfo(5, 7)), new EntityDwarf.ListItemForEmeralds(Items.CHAINMAIL_CHESTPLATE, new EntityDwarf.PriceInfo(11, 15))}},
            {{new EntityDwarf.EmeraldForItems(Items.COAL, new EntityDwarf.PriceInfo(16, 24)), new EntityDwarf.ListItemForEmeralds(Items.IRON_AXE, new EntityDwarf.PriceInfo(6, 8))}, {new EntityDwarf.EmeraldForItems(Items.IRON_INGOT, new EntityDwarf.PriceInfo(7, 9)), new EntityDwarf.ListEnchantedItemForEmeralds(Items.IRON_SWORD, new EntityDwarf.PriceInfo(9, 10))}, {new EntityDwarf.EmeraldForItems(Items.DIAMOND, new EntityDwarf.PriceInfo(3, 4)), new EntityDwarf.ListEnchantedItemForEmeralds(Items.DIAMOND_SWORD, new EntityDwarf.PriceInfo(12, 15)), new EntityDwarf.ListEnchantedItemForEmeralds(Items.DIAMOND_AXE, new EntityDwarf.PriceInfo(9, 12))}}, {{new EntityDwarf.EmeraldForItems(Items.COAL, new EntityDwarf.PriceInfo(16, 24)), new EntityDwarf.ListEnchantedItemForEmeralds(Items.IRON_SHOVEL, new EntityDwarf.PriceInfo(5, 7))}, {new EntityDwarf.EmeraldForItems(Items.IRON_INGOT, new EntityDwarf.PriceInfo(7, 9)), new EntityDwarf.ListEnchantedItemForEmeralds(Items.IRON_PICKAXE, new EntityDwarf.PriceInfo(9, 11))}, {new EntityDwarf.EmeraldForItems(Items.DIAMOND, new EntityDwarf.PriceInfo(3, 4)), new EntityDwarf.ListEnchantedItemForEmeralds(Items.DIAMOND_PICKAXE, new EntityDwarf.PriceInfo(12, 15))}}},



        {
            {
                {new EntityDwarf.EmeraldForItems(Items.PORKCHOP, new EntityDwarf.PriceInfo(14, 18)), new EntityDwarf.EmeraldForItems(Items.CHICKEN, new EntityDwarf.PriceInfo(14, 18))},
                {new EntityDwarf.EmeraldForItems(Items.COAL, new EntityDwarf.PriceInfo(16, 24)), new EntityDwarf.ListItemForEmeralds(Items.COOKED_PORKCHOP, new EntityDwarf.PriceInfo(-7, -5)), new EntityDwarf.ListItemForEmeralds(Items.COOKED_CHICKEN, new EntityDwarf.PriceInfo(-8, -6))}
            },
            {
                {new EntityDwarf.EmeraldForItems(Items.LEATHER, new EntityDwarf.PriceInfo(9, 12)), new EntityDwarf.ListItemForEmeralds(Items.LEATHER_LEGGINGS, new EntityDwarf.PriceInfo(2, 4))}, {new EntityDwarf.ListEnchantedItemForEmeralds(Items.LEATHER_CHESTPLATE, new EntityDwarf.PriceInfo(7, 12))}, {new EntityDwarf.ListItemForEmeralds(Items.SADDLE, new EntityDwarf.PriceInfo(8, 10))}
            }
            },


        {new EntityDwarf.ITradeList[0][]}};*/

}

/*profession > 1.miner: sell: redstone, minecart, rails, blasting charge, great pickaxe,
                        buys: coal, gold, iron, tnt
               worker: beer, mead, buys wheat, meat.
               armorer : cartridge, grenade, drakegun, shield, iron, gunpowder,
               lord : quest for ghal maraz: beer , rotten flesh, prismarine crystals ,nether star > ghal maraz
               builder: sell: quartz block, bricks, clay, quartz; concrete powder,
                        buys: andesite granite, diorite

*/