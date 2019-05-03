package warhammermod.Items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.SideOnly;
import warhammermod.Entities.render.RenderShieldDW;
import warhammermod.Entities.render.RenderShieldEM;
import warhammermod.Entities.render.RenderShieldHE;
import warhammermod.Entities.render.RendershieldDE;
import warhammermod.util.Handler.inithandler.Itemsinit;

import javax.annotation.Nullable;
import java.util.List;

import static net.minecraftforge.fml.relauncher.Side.CLIENT;


public class shieldtemplate extends ItemShield {
    private String shield_name;
    public shieldtemplate(String name,int durability,boolean enabled){
        super();

        setMaxDamage(durability);
        setUnlocalizedName(name);
        setRegistryName(name);
        shield_name=name;
        String wside=FMLCommonHandler.instance().getSide().toString();
        if(wside=="CLIENT"){
            settheteisr(shield_name);}
        if(enabled){Itemsinit.ITEMS.add(this);}

    }

    public String getItemStackDisplayName(ItemStack stack)
    {
        return shield_name;
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @SideOnly(CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
    }
    public boolean isShield(ItemStack stack, @Nullable EntityLivingBase entity)
    {
        return(stack.getItem() == Items.SHIELD || stack.getItem()== shieldtemplate.this);
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }
    @SideOnly(CLIENT)
    private void settheteisr(String name){
        switch (name) {
            case "high elf shield":setTileEntityItemStackRenderer(new RenderShieldHE());break;
            case "dark elf shield":setTileEntityItemStackRenderer(new RendershieldDE());break;
            case "dwarf shield":setTileEntityItemStackRenderer(new RenderShieldDW());break;
            default:setTileEntityItemStackRenderer(new RenderShieldEM());break;
        }
    }




}
