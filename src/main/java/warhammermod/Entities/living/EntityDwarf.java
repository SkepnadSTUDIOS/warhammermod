package warhammermod.Entities.living;

import com.google.common.collect.Lists;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockStone;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import warhammermod.Entities.living.EntityDwarf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.datafix.*;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.village.Village;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import warhammermod.Entities.living.Emanager.*;
import warhammermod.util.Handler.inithandler.Itemsinit;
import warhammermod.util.utils;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class EntityDwarf extends EntityAgeable implements INpc, IMerchant
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final DataParameter<Integer> DWARF_PROFESSION = EntityDataManager.createKey(EntityDwarf.class, DataSerializers.VARINT);
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
    /** This is the EntityDwarf's career level value */
    private int careerLevel;
    private boolean isLookingForHome;
    private boolean areAdditionalTasksSet;
    private final InventoryBasic villagerInventory;
    private net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession prof;
    /** A multi-dimensional array mapping the various professions, careers and career levels that a Villager may offer */
    public static final EntityDwarf.ITradeList[][][][] DWARF_TRADE_LIST_MAP = new EntityDwarf.ITradeList[][][][] {{{{new EntityDwarf.EmeraldForItems(Items.WHEAT, new EntityDwarf.PriceInfo(18, 22)), new EntityDwarf.EmeraldForItems(Items.POTATO, new EntityDwarf.PriceInfo(15, 19)), new EntityDwarf.EmeraldForItems(Items.CARROT, new EntityDwarf.PriceInfo(15, 19)), new EntityDwarf.ListItemForEmeralds(Itemsinit.beer, new EntityDwarf.PriceInfo(2, 6))}, {new EntityDwarf.EmeraldForItems(Item.getItemFromBlock(Blocks.PUMPKIN), new EntityDwarf.PriceInfo(8, 13)), new EntityDwarf.ListItemForEmeralds(Items.PUMPKIN_PIE, new EntityDwarf.PriceInfo(-3, -2))}, {new EntityDwarf.EmeraldForItems(Item.getItemFromBlock(Blocks.MELON_BLOCK), new EntityDwarf.PriceInfo(7, 12)), new EntityDwarf.ListItemForEmeralds(Items.APPLE, new EntityDwarf.PriceInfo(-7, -5))}, {new EntityDwarf.ListItemForEmeralds(Items.COOKIE, new EntityDwarf.PriceInfo(-10, -6)), new EntityDwarf.ListItemForEmeralds(Items.CAKE, new EntityDwarf.PriceInfo(1, 1))}}},
            {{{new EmeraldForItems(Items.COAL,new PriceInfo(10,16)),new EntityDwarf.ListItemForEmeralds(Items.REDSTONE,new EntityDwarf.PriceInfo(-6,-2)),new ListItemForEmeralds(Items.MINECART, new PriceInfo(7,15)),new EmeraldForItems(Item.getItemFromBlock(Blocks.RAIL), new PriceInfo(-3,1)),new EmeraldForItems(Itemsinit.beer,new PriceInfo(4,8))},{new ListItemForEmeralds(Item.getItemFromBlock(Blocks.GOLDEN_RAIL),new PriceInfo(2,6)),new ListItemForEmeralds(new ItemStack(Items.DYE,1,EnumDyeColor.BLUE.getDyeDamage()), new PriceInfo(4, 2)), new EmeraldForItems(Items.GOLD_INGOT,new PriceInfo(5,7)),new ListItemForEmeralds(Item.getItemFromBlock(Blocks.TNT),new PriceInfo(11,16))},{new ListItemForEmeralds(Items.QUARTZ,new PriceInfo(1,4)),new ListEnchantedItemForEmeralds(Itemsinit.GREAT_PICKAXE,new PriceInfo(45,64))}}},
            {{{new ListItemForEmeralds(Itemsinit.Cartridge,new PriceInfo(1,4)),new EmeraldForItems(Items.GUNPOWDER,new PriceInfo(1,3)),new ListItemForEmeralds(Itemsinit.Grenade,new PriceInfo(3,7)),new EmeraldForItems(Itemsinit.beer,new PriceInfo(4,8)),},{new ListItemForEmeralds(utils.getRandomShield(),new PriceInfo(12,23)),new EmeraldForItems(Itemsinit.thunderer_hangun,new PriceInfo(21,36))},{new ListEnchantedItemForEmeralds(utils.getRandomarmor(1),new PriceInfo(16,19)),new ListEnchantedItemForEmeralds(utils.getRandomarmor(0),new PriceInfo(16,19))},{new ListItemForEmeralds(Itemsinit.Drakegun,new PriceInfo(43,60))}}},
            {{{new ListItemForEmeralds(Item.getItemFromBlock(Blocks.CLAY),new PriceInfo(1,4)),new EmeraldForItems(new ItemStack(Item.getItemFromBlock(Blocks.NETHER_BRICK), 1, 0),new PriceInfo(16,8)),new ListItemForEmeralds(Item.getItemFromBlock(Blocks.RED_SANDSTONE),new PriceInfo(2,7))},{new ListItemForEmeralds(Item.getItemFromBlock(Blocks.CONCRETE_POWDER),new PriceInfo(2,7)),new EmeraldForItems(Itemsinit.beer,new PriceInfo(4,8))},{new ListItemForEmeralds(Item.getItemFromBlock(Blocks.QUARTZ_BLOCK),new PriceInfo(2,4)),new ListItemForEmeralds(Item.getItemFromBlock(Blocks.PURPUR_BLOCK),new PriceInfo(3,7))}}},
            {{{new EmeraldForItems(Itemsinit.beer,new PriceInfo(6,8))},{new EmeraldForItems(Items.ROTTEN_FLESH,new PriceInfo(58,64))},{new EmeraldForItems(Items.PRISMARINE_CRYSTALS,new PriceInfo(2,5))},{new EmeraldForItems(Items.BLAZE_ROD,new PriceInfo(2,4))},{new ListEnchantedItemForItemAndEmeralds(Items.NETHER_STAR,new PriceInfo(1,1),Itemsinit.GHAL_MARAZ,new PriceInfo(1,1))}}},
            {new EntityDwarf.ITradeList[0][]}};




    public EntityDwarf(World worldIn)
    {
        this(worldIn, 0);
    }

    public EntityDwarf(World worldIn, int professionId)
    {
        super(worldIn);
        this.villagerInventory = new InventoryBasic("Items", false, 8);
        this.setProfession(professionId);
        this.setSize(0.6F, 1.95F);
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        this.setCanPickUpLoot(true);
    }

    protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityEvoker.class, 12.0F, 0.8D, 0.8D));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityVindicator.class, 8.0F, 0.8D, 0.8D));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityVex.class, 8.0F, 0.6D, 0.6D));
        this.tasks.addTask(1, new EntityAIDTradePlayer(this));
        this.tasks.addTask(1, new EntityAIDLookAtTradePlayer(this));
        this.tasks.addTask(2, new EntityAIMoveIndoors(this));
        this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6D));
        this.tasks.addTask(6, new EntityAIDwarfMate(this));
        this.tasks.addTask(7, new EntityAIDFollowGolem(this));
        this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(9, new EntityAIDVillagerInteract(this));
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
                this.tasks.addTask(8, new EntityAIDPlay(this, 0.32D));
            }
            else if (this.getProfession() == 0)
            {
                this.tasks.addTask(6, new EntityAIDHarvestFarmland(this, 0.6D));
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
            this.tasks.addTask(8, new EntityAIDHarvestFarmland(this, 0.6D));
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
        this.dataManager.register(DWARF_PROFESSION, Integer.valueOf(0));
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


    @Override
    public void notifyDataManagerChange(DataParameter<?> key)
    {
        super.notifyDataManagerChange(key);
    }

    public boolean isMating()
    {
        return this.isMating;
    }

    public void setMating(boolean mating)
    {
        this.isMating = mating;
    }

    public void setPlaying(boolean playing)
    {
        this.isPlaying = playing;
    }

    public boolean isPlaying()
    {
        return this.isPlaying;
    }

    /**
     * Hint to AI tasks that we were attacked by the passed EntityLivingBase and should retaliate. Is not guaranteed to
     * change our actual active target (for example if we are currently busy attacking someone else)
     */
    public void setRevengeTarget(@Nullable EntityLivingBase livingBase)
    {
        super.setRevengeTarget(livingBase);

        if (this.village != null && livingBase != null)
        {
            this.village.addOrRenewAgressor(livingBase);

            if (livingBase instanceof EntityPlayer)
            {
                int i = -1;

                if (this.isChild())
                {
                    i = -3;
                }

                this.village.modifyPlayerReputation(livingBase.getUniqueID(), i);

                if (this.isEntityAlive())
                {
                    this.world.setEntityState(this, (byte)13);
                }
            }
        }
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource cause)
    {
        if (this.village != null)
        {
            Entity entity = cause.getTrueSource();

            if (entity != null)
            {
                if (entity instanceof EntityPlayer)
                {
                    this.village.modifyPlayerReputation(entity.getUniqueID(), -2);
                }
                else if (entity instanceof IMob)
                {
                    this.village.endMatingSeason();
                }
            }
            else
            {
                EntityPlayer entityplayer = this.world.getClosestPlayerToEntity(this, 16.0D);

                if (entityplayer != null)
                {
                    this.village.endMatingSeason();
                }
            }
        }

        super.onDeath(cause);
    }

    public void setCustomer(@Nullable EntityPlayer player)
    {
        this.buyingPlayer = player;
    }

    @Nullable
    public EntityPlayer getCustomer()
    {
        return this.buyingPlayer;
    }

    public boolean isTrading()
    {
        return this.buyingPlayer != null;
    }

    /**
     * Returns current or updated value of {@link #isWillingToMate}
     */
    public boolean getIsWillingToMate(boolean updateFirst)
    {
        if (!this.isWillingToMate && updateFirst && this.hasEnoughFoodToBreed())
        {
            boolean flag = false;

            for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i)
            {
                ItemStack itemstack = this.villagerInventory.getStackInSlot(i);

                if (!itemstack.isEmpty())
                {
                    if ((itemstack.getItem() == Items.BREAD||itemstack.getItem() == Itemsinit.beer) && itemstack.getCount() >= 3)
                    {
                        flag = true;
                        this.villagerInventory.decrStackSize(i, 3);
                    }
                    else if ((itemstack.getItem() == Items.POTATO || itemstack.getItem() == Items.CARROT) && itemstack.getCount() >= 12)
                    {
                        flag = true;
                        this.villagerInventory.decrStackSize(i, 12);
                    }
                }

                if (flag)
                {
                    this.world.setEntityState(this, (byte)18);
                    this.isWillingToMate = true;
                    break;
                }
            }
        }

        return this.isWillingToMate;
    }

    public void setIsWillingToMate(boolean isWillingToMate)
    {
        this.isWillingToMate = isWillingToMate;
    }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("Profession", this.getProfession());
        compound.setString("ProfessionName", getprofessionname(getProfession(),careerId));
        compound.setInteger("Riches", this.wealth);
        compound.setInteger("Career", this.careerId);
        compound.setInteger("CareerLevel", this.careerLevel);
        compound.setBoolean("Willing", this.isWillingToMate);

        if (this.buyingList != null)
        {
            compound.setTag("Offers", this.buyingList.getRecipiesAsTags());
        }

        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i)
        {
            ItemStack itemstack = this.villagerInventory.getStackInSlot(i);

            if (!itemstack.isEmpty())
            {
                nbttaglist.appendTag(itemstack.writeToNBT(new NBTTagCompound()));
            }
        }

        compound.setTag("Inventory", nbttaglist);
    }

    @Nullable
    public MerchantRecipeList getRecipes(EntityPlayer player)
    {
        if (this.buyingList == null)
        {
            this.populateBuyingList();
        }

        return this.buyingList;
    }



    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);
        boolean flag = itemstack.getItem() == Items.NAME_TAG;

        if (flag)
        {
            itemstack.interactWithEntity(player, this, hand);
            return true;
        }
        else if (!this.holdingSpawnEggOfClass(itemstack, this.getClass()) && this.isEntityAlive() && !this.isTrading() && !this.isChild() && !player.isSneaking())
        {
            if (this.buyingList == null)
            {
                this.populateBuyingList();
            }

            if (hand == EnumHand.MAIN_HAND)
            {
                player.addStat(StatList.TALKED_TO_VILLAGER);
            }

            if (!this.world.isRemote && !this.buyingList.isEmpty())
            {
                this.setCustomer(player);
                player.displayVillagerTradeGui(this);
            }
            else if (this.buyingList.isEmpty())
            {
                return super.processInteract(player, hand);
            }

            return true;
        }
        else
        {
            return super.processInteract(player, hand);
        }
    }

    public static void registerFixesVillager(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntityDwarf.class);
        fixer.registerWalker(FixTypes.ENTITY, new ItemStackDataLists(EntityDwarf.class, new String[] {"Inventory"}));
        fixer.registerWalker(FixTypes.ENTITY, new IDataWalker()
        {
            public NBTTagCompound process(IDataFixer fixer, NBTTagCompound compound, int versionIn)
            {
                if (EntityList.getKey(EntityDwarf.class).equals(new ResourceLocation(compound.getString("id"))) && compound.hasKey("Offers", 10))
                {
                    NBTTagCompound nbttagcompound = compound.getCompoundTag("Offers");

                    if (nbttagcompound.hasKey("Recipes", 9))
                    {
                        NBTTagList nbttaglist = nbttagcompound.getTagList("Recipes", 10);

                        for (int i = 0; i < nbttaglist.tagCount(); ++i)
                        {
                            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
                            DataFixesManager.processItemStack(fixer, nbttagcompound1, versionIn, "buy");
                            DataFixesManager.processItemStack(fixer, nbttagcompound1, versionIn, "buyB");
                            DataFixesManager.processItemStack(fixer, nbttagcompound1, versionIn, "sell");
                            nbttaglist.set(i, nbttagcompound1);
                        }
                    }
                }

                return compound;
            }
        });
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setProfession(compound.getInteger("Profession"));
        if (compound.hasKey("ProfessionName"))
        {

            this.setProfession(getProfession());
        }
        this.wealth = compound.getInteger("Riches");
        this.careerId = compound.getInteger("Career");
        this.careerLevel = compound.getInteger("CareerLevel");
        this.isWillingToMate = compound.getBoolean("Willing");

        if (compound.hasKey("Offers", 10))
        {
            NBTTagCompound nbttagcompound = compound.getCompoundTag("Offers");
            this.buyingList = new MerchantRecipeList(nbttagcompound);
        }

        NBTTagList nbttaglist = compound.getTagList("Inventory", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            ItemStack itemstack = new ItemStack(nbttaglist.getCompoundTagAt(i));

            if (!itemstack.isEmpty())
            {
                this.villagerInventory.addItem(itemstack);
            }
        }

        this.setCanPickUpLoot(true);
        this.setAdditionalAItasks();
    }

    public void useRecipe(MerchantRecipe recipe)
    {
        recipe.incrementToolUses();
        this.livingSoundTime = -this.getTalkInterval();
        this.playSound(SoundEvents.ENTITY_VILLAGER_YES, this.getSoundVolume(), this.getSoundPitch());
        int i = 3 + this.rand.nextInt(4);

        if (recipe.getToolUses() == 1 || this.rand.nextInt(5) == 0)
        {
            this.timeUntilReset = 40;
            this.needsInitilization = true;
            this.isWillingToMate = true;

            if (this.buyingPlayer != null)
            {
                this.lastBuyingPlayer = this.buyingPlayer.getUniqueID();
            }
            else
            {
                this.lastBuyingPlayer = null;
            }

            i += 5;
        }

        if (recipe.getItemToBuy().getItem() == Items.EMERALD)
        {
            this.wealth += recipe.getItemToBuy().getCount();
        }

        if (recipe.getRewardsExp())
        {
            this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY + 0.5D, this.posZ, i));
        }

    }



    private void populateBuyingList()
    {
        if (this.careerLevel != 0)
        {
            ++this.careerLevel;
        }
        else
        {
            this.careerId = this.getRandomCareer();
            this.careerLevel = 1;
        }

        if (this.buyingList == null)
        {
            this.buyingList = new MerchantRecipeList();
        }

        int i = this.careerId;
        int j = this.careerLevel - 1;
        java.util.List<EntityDwarf.ITradeList> trades = ProfessionHelper.careers.get(getProfession()).getTrades(j) ;


        if (trades != null)
        {
            for (EntityDwarf.ITradeList EntityDwarf$itradelist : trades)
            {
                EntityDwarf$itradelist.addMerchantRecipe(this, this.buyingList, this.rand);
            }
        }
    }

    /**
     * Notifies the merchant of a possible merchantrecipe being fulfilled or not. Usually, this is just a sound byte
     * being played depending if the suggested itemstack is not null.
     */
    public void verifySellingItem(ItemStack stack)
    {
        if (!this.world.isRemote && this.livingSoundTime > -this.getTalkInterval() + 20)
        {
            this.livingSoundTime = -this.getTalkInterval();
            this.playSound(stack.isEmpty() ? SoundEvents.ENTITY_VILLAGER_NO : SoundEvents.ENTITY_VILLAGER_YES, this.getSoundVolume(), this.getSoundPitch());
        }
    }

    @SideOnly(Side.CLIENT)
    public void setRecipes(@Nullable MerchantRecipeList recipeList)
    {
    }

    public World getWorld()
    {
        return this.world;
    }

    public BlockPos getPos()
    {
        return new BlockPos(this);
    }

    public ITextComponent getDisplayName()
    {
        Team team = this.getTeam();
        String s = this.getCustomNameTag();

        if (s != null && !s.isEmpty())
        {
            TextComponentString textcomponentstring = new TextComponentString(ScorePlayerTeam.formatPlayerName(team, s));
            textcomponentstring.getStyle().setHoverEvent(this.getHoverEvent());
            textcomponentstring.getStyle().setInsertion(this.getCachedUniqueIdString());
            return textcomponentstring;
        }
        else
        {
            if (this.buyingList == null)
            {
                this.populateBuyingList();
            }

            String s1 = null;




            s1 = this.getprofessionname(getProfession(),getcareer());
            {
                ITextComponent itextcomponent = new TextComponentTranslation("" + s1, new Object[0]);
                itextcomponent.getStyle().setHoverEvent(this.getHoverEvent());
                itextcomponent.getStyle().setInsertion(this.getCachedUniqueIdString());

                if (team != null)
                {
                    itextcomponent.getStyle().setColor(team.getColor());
                }

                return itextcomponent;
            }
        }
    }

    public float getEyeHeight()
    {
        return this.isChild() ? 0.81F : 1.62F;
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id)
    {
        if (id == 12)
        {
            this.spawnParticles(EnumParticleTypes.HEART);
        }
        else if (id == 13)
        {
            this.spawnParticles(EnumParticleTypes.VILLAGER_ANGRY);
        }
        else if (id == 14)
        {
            this.spawnParticles(EnumParticleTypes.VILLAGER_HAPPY);
        }
        else
        {
            super.handleStatusUpdate(id);
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
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        return this.finalizeMobSpawn(difficulty, livingdata, true);
    }

    public IEntityLivingData finalizeMobSpawn(DifficultyInstance p_190672_1_, @Nullable IEntityLivingData p_190672_2_, boolean p_190672_3_)
    {
        p_190672_2_ = super.onInitialSpawn(p_190672_1_, p_190672_2_);

        if (p_190672_3_)
        {
            setRandomProfession();
        }

        this.setAdditionalAItasks();
        this.populateBuyingList();
        return p_190672_2_;
    }

    public void setLookingForHome()
    {
        this.isLookingForHome = true;
    }

    public EntityDwarf createChild(EntityAgeable ageable)
    {
        EntityDwarf entityDwarf = new EntityDwarf(this.world);
        entityDwarf.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entityDwarf)), (IEntityLivingData)null);
        return entityDwarf;
    }

    public boolean canBeLeashedTo(EntityPlayer player)
    {
        return false;
    }

    /**
     * Called when a lightning bolt hits the entity.
     */
    public void onStruckByLightning(EntityLightningBolt lightningBolt)
    {
        if (!this.world.isRemote && !this.isDead)
        {
            EntityWitch entitywitch = new EntityWitch(this.world);
            entitywitch.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            entitywitch.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitywitch)), (IEntityLivingData)null);
            entitywitch.setNoAI(this.isAIDisabled());

            if (this.hasCustomName())
            {
                entitywitch.setCustomNameTag(this.getCustomNameTag());
                entitywitch.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
            }

            this.world.spawnEntity(entitywitch);
            this.setDead();
        }
    }

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    public InventoryBasic getVillagerInventory()
    {
        return this.villagerInventory;
    }

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
        return itemIn == Items.BREAD ||itemIn == Itemsinit.beer || itemIn == Items.POTATO || itemIn == Items.CARROT || itemIn == Items.WHEAT || itemIn == Items.WHEAT_SEEDS || itemIn == Items.BEETROOT || itemIn == Items.BEETROOT_SEEDS;
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
                if (itemstack.getItem() == Items.BREAD && itemstack.getCount() >= 3 * multiplier ||itemstack.getItem() == Itemsinit.beer && itemstack.getCount() >= 3 * multiplier || itemstack.getItem() == Items.POTATO && itemstack.getCount() >= 12 * multiplier || itemstack.getItem() == Items.CARROT && itemstack.getCount() >= 12 * multiplier || itemstack.getItem() == Items.BEETROOT && itemstack.getCount() >= 12 * multiplier)
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

    /**
     * Returns true if villager has seeds, potatoes or carrots in inventory
     */
    public boolean isFarmItemInInventory()
    {
        for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i)
        {
            ItemStack itemstack = this.villagerInventory.getStackInSlot(i);

            if (!itemstack.isEmpty() && (itemstack.getItem() == Items.WHEAT_SEEDS || itemstack.getItem() == Items.POTATO || itemstack.getItem() == Items.CARROT || itemstack.getItem() == Items.BEETROOT_SEEDS))
            {
                return true;
            }
        }

        return false;
    }

    public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn)
    {
        if (super.replaceItemInInventory(inventorySlot, itemStackIn))
        {
            return true;
        }
        else
        {
            int i = inventorySlot - 300;

            if (i >= 0 && i < this.villagerInventory.getSizeInventory())
            {
                this.villagerInventory.setInventorySlotContents(i, itemStackIn);
                return true;
            }
            else
            {
                return false;
            }
        }
    }
    public boolean hasEnoughFoodToBreed()
    {
        return this.hasEnoughItems(1);
    }

    /**
     * Used by {@link net.minecraft.entity.ai.EntityAIVillagerInteract EntityAIVillagerInteract} to check if the
     * villager can give some items from an inventory to another villager.
     */
    public boolean canAbondonItems()
    {
        return this.hasEnoughItems(2);
    }

    public boolean wantsMoreFood()
    {
        boolean flag = this.getProfession() == 0;

        if (flag)
        {
            return !this.hasEnoughItems(5);
        }
        else
        {
            return !this.hasEnoughItems(1);
        }
    }


    public static class EmeraldForItems implements EntityDwarf.ITradeList
    {
        public Item buyingItem;
        public EntityDwarf.PriceInfo price;

        public EmeraldForItems(Item itemIn, EntityDwarf.PriceInfo priceIn)
        {
            this.buyingItem = itemIn;
            this.price = priceIn;
        }

        public EmeraldForItems(ItemStack stack, PriceInfo priceInfo)
        {
            this.buyingItem = stack.getItem();
            this.price = priceInfo;
        }

        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
        {
            int i = 1;

            if (this.price != null)
            {
                i = this.price.getPrice(random);
            }

            recipeList.add(new MerchantRecipe(new ItemStack(this.buyingItem, i, 0), Items.EMERALD));
        }
    }

    public interface ITradeList
    {
        void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random);
    }

    public static class ItemAndEmeraldToItem implements EntityDwarf.ITradeList
    {
        /**
         * The itemstack to buy with an emerald. The Item and damage value is used only, any tag data is not
         * retained.
         */
        public ItemStack buyingItemStack;
        /** The price info defining the amount of the buying item required with 1 emerald to match the selling item. */
        public EntityDwarf.PriceInfo buyingPriceInfo;
        /** The itemstack to sell. The item and damage value are used only, any tag data is not retained. */
        public ItemStack sellingItemstack;
        public EntityDwarf.PriceInfo sellingPriceInfo;

        public ItemAndEmeraldToItem(Item p_i45813_1_, EntityDwarf.PriceInfo p_i45813_2_, Item p_i45813_3_, EntityDwarf.PriceInfo p_i45813_4_)
        {
            this.buyingItemStack = new ItemStack(p_i45813_1_);
            this.buyingPriceInfo = p_i45813_2_;
            this.sellingItemstack = new ItemStack(p_i45813_3_);
            this.sellingPriceInfo = p_i45813_4_;
        }


        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
        {
            int i = this.buyingPriceInfo.getPrice(random);
            int j = this.sellingPriceInfo.getPrice(random);
            recipeList.add(new MerchantRecipe(new ItemStack(this.buyingItemStack.getItem(), i, this.buyingItemStack.getMetadata()), new ItemStack(Items.EMERALD), new ItemStack(this.sellingItemstack.getItem(), j, this.sellingItemstack.getMetadata())));
        }
    }

    public static class ListEnchantedItemForItemAndEmeralds implements EntityDwarf.ITradeList
    {
        public ItemStack buyingItemStack;
        /** The price info defining the amount of the buying item required with 1 emerald to match the selling item. */
        public EntityDwarf.PriceInfo buyingPriceInfo;
        /** The itemstack to sell. The item and damage value are used only, any tag data is not retained. */
        public ItemStack sellingItemstack;
        public EntityDwarf.PriceInfo sellingPriceInfo;
        /** The enchanted item stack to sell */
        public ItemStack enchantedItemStack;
        /** The price info determining the amount of emeralds to trade in for the enchanted item */
        public EntityDwarf.PriceInfo priceInfo;

        public ListEnchantedItemForItemAndEmeralds(Item p_i45813_1_, EntityDwarf.PriceInfo p_i45813_2_, Item p_i45813_3_, EntityDwarf.PriceInfo p_i45813_4_)
        {


            this.buyingItemStack = new ItemStack(p_i45813_1_);
            this.buyingPriceInfo = p_i45813_2_;
            this.enchantedItemStack = new ItemStack(p_i45813_3_);
            this.sellingPriceInfo = p_i45813_4_;
        }

        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
        {

            ItemStack itemstack1 = EnchantmentHelper.addRandomEnchantment(random, new ItemStack(this.enchantedItemStack.getItem(), 1, this.enchantedItemStack.getMetadata()), 5 + random.nextInt(15), false);
            int i = this.buyingPriceInfo.getPrice(random);
            int j = this.sellingPriceInfo.getPrice(random);
            recipeList.add(new MerchantRecipe(new ItemStack(this.buyingItemStack.getItem(), i, this.buyingItemStack.getMetadata()), new ItemStack(Items.EMERALD), itemstack1, j, this.enchantedItemStack.getMetadata()));

        }
    }

    public static class ListEnchantedBookForEmeralds implements EntityDwarf.ITradeList
    {
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
        {
            Enchantment enchantment = (Enchantment)Enchantment.REGISTRY.getRandomObject(random);
            int i = MathHelper.getInt(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
            ItemStack itemstack = ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(enchantment, i));
            int j = 2 + random.nextInt(5 + i * 10) + 3 * i;

            if (enchantment.isTreasureEnchantment())
            {
                j *= 2;
            }

            if (j > 64)
            {
                j = 64;
            }

            recipeList.add(new MerchantRecipe(new ItemStack(Items.BOOK), new ItemStack(Items.EMERALD, j), itemstack));
        }
    }

    public static class ListEnchantedItemForEmeralds implements EntityDwarf.ITradeList
    {
        /** The enchanted item stack to sell */
        public ItemStack enchantedItemStack;
        /** The price info determining the amount of emeralds to trade in for the enchanted item */
        public EntityDwarf.PriceInfo priceInfo;

        public ListEnchantedItemForEmeralds(Item p_i45814_1_, EntityDwarf.PriceInfo p_i45814_2_)
        {
            this.enchantedItemStack = new ItemStack(p_i45814_1_);
            this.priceInfo = p_i45814_2_;
        }

        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
        {
            int i = 1;

            if (this.priceInfo != null)
            {
                i = this.priceInfo.getPrice(random);
            }

            ItemStack itemstack = new ItemStack(Items.EMERALD, i, 0);
            ItemStack itemstack1 = EnchantmentHelper.addRandomEnchantment(random, new ItemStack(this.enchantedItemStack.getItem(), 1, this.enchantedItemStack.getMetadata()), 5 + random.nextInt(15), false);
            recipeList.add(new MerchantRecipe(itemstack, itemstack1));
        }
    }

    public static class ListItemForEmeralds implements EntityDwarf.ITradeList
    {
        /** The item that is being bought for emeralds */
        public ItemStack itemToBuy;
        /**
         * The price info for the amount of emeralds to sell for, or if negative, the amount of the item to buy for
         * an emerald.
         */
        public EntityDwarf.PriceInfo priceInfo;

        public ListItemForEmeralds(Item par1Item, EntityDwarf.PriceInfo priceInfo)
        {
            this.itemToBuy = new ItemStack(par1Item);
            this.priceInfo = priceInfo;
        }

        public ListItemForEmeralds(ItemStack stack, EntityDwarf.PriceInfo priceInfo)
        {
            this.itemToBuy = stack;
            this.priceInfo = priceInfo;
        }

        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
        {
            int i = 1;

            if (this.priceInfo != null)
            {
                i = this.priceInfo.getPrice(random);
            }

            ItemStack itemstack;
            ItemStack itemstack1;

            if (i < 0)
            {
                itemstack = new ItemStack(Items.EMERALD);
                itemstack1 = new ItemStack(this.itemToBuy.getItem(), -i, this.itemToBuy.getMetadata());
            }
            else
            {
                itemstack = new ItemStack(Items.EMERALD, i, 0);
                itemstack1 = new ItemStack(this.itemToBuy.getItem(), 1, this.itemToBuy.getMetadata());
            }

            recipeList.add(new MerchantRecipe(itemstack, itemstack1));
        }
    }

    public static class PriceInfo extends Tuple<Integer, Integer>
    {
        public PriceInfo(int p_i45810_1_, int p_i45810_2_)
        {
            super(Integer.valueOf(p_i45810_1_), Integer.valueOf(p_i45810_2_));

            if (p_i45810_2_ < p_i45810_1_)
            {
                EntityDwarf.LOGGER.warn("PriceRange({}, {}) invalid, {} smaller than {}", Integer.valueOf(p_i45810_1_), Integer.valueOf(p_i45810_2_), Integer.valueOf(p_i45810_2_), Integer.valueOf(p_i45810_1_));
            }
        }

        public int getPrice(Random rand)
        {
            return ((Integer)this.getFirst()).intValue() >= ((Integer)this.getSecond()).intValue() ? ((Integer)this.getFirst()).intValue() : ((Integer)this.getFirst()).intValue() + rand.nextInt(((Integer)this.getSecond()).intValue() - ((Integer)this.getFirst()).intValue() + 1);
        }
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

    public int getProfession()
    {
        return Math.max(((Integer)this.dataManager.get(DWARF_PROFESSION)).intValue(), 0);
    }

    public void setProfession(int professionId)
    {
        this.dataManager.set(DWARF_PROFESSION, Integer.valueOf(professionId));
    }
    public void setRandomProfession(){
        int random = new Random().nextInt(5);
        int lordrate;
        System.out.println(random + " profession");
        if(random==4){
            if((lordrate = new Random().nextInt(3))==2){dataManager.set(DWARF_PROFESSION,Integer.valueOf(random));}
            else {System.out.println(lordrate+" lordrate");setRandomProfession();}
        }
        else dataManager.set(DWARF_PROFESSION,Integer.valueOf(random));
    }
    public int getRandomCareer(){
        int prof=getProfession();
        if(prof==2){
            int random = new Random().nextInt(2);
            return random;}
        else return 0;
    }
    public int getcareer(){return careerId;}

    public static EntityDwarf.ITradeList[][][][] GET_TRADES(){ return DWARF_TRADE_LIST_MAP; }

    public String getprofessionname(int prof,int career){
        if(prof==0)return "farmer";
        else if(prof==1)return "miner";
        else if (prof==2)return "weapon engineer";
        else if (prof==3)return"builder";
        else return "lord";
    }


    /*{{{new EntityDwarf.EmeraldForItems(Items.COAL,new EntityDwarf.PriceInfo(10,16)),new EntityDwarf.ListItemForEmeralds(Items.REDSTONE,new EntityDwarf.PriceInfo(-6,-2)), new ListItemForEmeralds(Items.MINECART, new PriceInfo(7,15)), new EmeraldForItems(Item.getItemFromBlock(Blocks.RAIL), new PriceInfo(1,3)), new EmeraldForItems(Itemsinit.beer,new PriceInfo(2,3))},
        {new ListItemForEmeralds(Item.getItemFromBlock(Blocks.GOLDEN_RAIL),new PriceInfo(2,6)),new ListItemForEmeralds(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()), new EntityDwarf.PriceInfo(-4, -2))), new EmeraldForItems(Items.GOLD_INGOT,new PriceInfo(5,7)), new ListItemForEmeralds(Item.getItemFromBlock(Blocks.TNT),new PriceInfo(11,16))},
        {new ListItemForEmeralds(Items.QUARTZ,new PriceInfo(1,5)),new ListEnchantedItemForEmeralds(Itemsinit.GREAT_PICKAXE,new PriceInfo(45,64))}},*/




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
    }  ,
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