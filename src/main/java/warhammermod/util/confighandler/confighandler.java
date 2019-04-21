package warhammermod.util.confighandler;

import net.minecraftforge.common.config.Config;
import warhammermod.util.reference;

public class confighandler {

    @Config(modid = reference.modid, type = Config.Type.INSTANCE, name = "disable items")

    public static class Config_enable {
        @Config.RequiresMcRestart
        public static boolean spears_included = true;
        @Config.RequiresMcRestart
        public static boolean hammers_included = true;
        @Config.RequiresMcRestart
        public static boolean halberds_included =true;
        @Config.RequiresMcRestart
        public static boolean knife_included =true;
        @Config.RequiresMcRestart
        public static boolean musket_included =true;
        @Config.RequiresMcRestart
        public static boolean pistol_included =true;
        @Config.RequiresMcRestart
        public static boolean repeater_handgun_inlcuded =true;

        public static boolean repeater_3D_model = true;
        @Config.RequiresMcRestart
        public static boolean grenadelauncher_included = true;
        @Config.RequiresMcRestart
        public static boolean shields_included = true;
        @Config.RequiresMcRestart
        public static boolean gunsword_included = true;

    }
    @Config(modid = reference.modid, type = Config.Type.INSTANCE, name = "items values")
    public static class Config_values{
        @Config.RequiresMcRestart
        @Config.RangeDouble(min=0,max=20)
        @Config.Comment("for reference the sword has a damage of 3")
        public static double spear_damage = 2;
        @Config.RequiresMcRestart
        @Config.RangeDouble(min=0,max=20)
        public static double warhammer_damage = 8;
        @Config.RequiresMcRestart
        @Config.RangeDouble(min=0,max=20)
        public static double knife_damage = 0.5;
        @Config.RequiresMcRestart
        @Config.RangeDouble(min=0,max=20)
        public static double halberd_damage = 5.4;
        @Config.RequiresMcRestart
        @Config.RangeDouble(min=0,max=20)
        public static double pistol_damage = 8;
        @Config.RequiresMcRestart
        @Config.RangeDouble(min=0,max=20)
        @Config.Comment("affects musket and repeater handgun")
        public static double rifle_damage = 13;
        @Config.RequiresMcRestart
        @Config.RangeDouble(min=0,max=20)
        public static double gunsword_damage = 8;

        @Config.RequiresMcRestart
        @Config.RangeDouble(min=-5,max=20)
        @Config.Comment("for reference the sword has a speed of 2.4 (lower is faster)")
        public static double spear_speed = 2;
        @Config.RequiresMcRestart
        @Config.RangeDouble(min=-5,max=20)
        public static double warhammer_speed = 2.9;
        @Config.RequiresMcRestart
        @Config.RangeDouble(min=-5,max=20)
        public static double knife_speed = -5;
        @Config.RequiresMcRestart
        @Config.RangeDouble(min=-5,max=20)
        public static double halberd_speed = 2.8;
        @Config.RequiresMcRestart
        @Config.RangeDouble(min=0,max=20)
        public static double pistol_speed = 25;
        @Config.RequiresMcRestart
        @Config.RangeDouble(min=0,max=20)
        @Config.Comment("affects musket and repeater hangun")
        public static double rifle_speed = 40;
        @Config.RequiresMcRestart
        @Config.RangeDouble(min=0,max=20)
        public static double gunsword_speed = 25;

        @Config.RequiresMcRestart
        @Config.RangeInt(min=1,max=100000)
        @Config.Comment("base shield durability is 336")
        public static int shields_durability=1008;






    }
    public static class getvalues{
        public static float getspd=(float) Config_values.spear_damage;
        public static float getwhd=(float) Config_values.warhammer_damage;
        public static float getknd=(float) Config_values.knife_damage;
        public static float gethbd=(float) Config_values.halberd_damage;
        public static float getptd=(float) Config_values.pistol_damage;
        public static float getrfd=(float) Config_values.rifle_damage;
        public static float getgsd=(float) Config_values.gunsword_damage;
        public static float getsps=(float) Config_values.spear_speed;
        public static float getwhs=(float) Config_values.warhammer_speed;
        public static float getkns=(float) Config_values.knife_speed;
        public static float gethbs=(float) Config_values.halberd_speed;
        public static float getpts=(float) Config_values.pistol_speed;
        public static float getrfs=(float) Config_values.rifle_speed;
        public static float getgss=(float) Config_values.gunsword_speed;
        public static int getSD= Config_values.shields_durability;
    }
}
