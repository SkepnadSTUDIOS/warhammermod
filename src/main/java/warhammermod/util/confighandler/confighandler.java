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
        @Config.RequiresMcRestart
        @Config.Comment("put on true to add compatibility with with headshot mods \n on right click attack of the halberd")
        public static boolean headshotmod_compatibility = false;

    }
    @Config(modid = reference.modid, type = Config.Type.INSTANCE, name = "items values")
    public static class Config_values {
        @Config.RequiresMcRestart
        @Config.RangeDouble(min = 0, max = 20)
        @Config.Comment("for reference the sword has a damage of 3")
        public static double spear_damage = 2;
        @Config.RequiresMcRestart
        @Config.RangeDouble(min = 0, max = 20)
        public static double warhammer_damage = 8;
        @Config.RequiresMcRestart
        @Config.RangeDouble(min = 0, max = 20)
        public static double knife_damage = 0.5;
        @Config.RequiresMcRestart
        @Config.RangeDouble(min = 0, max = 20)
        public static double halberd_damage = 5.4;
        @Config.RequiresMcRestart
        @Config.RangeInt(min = 0, max = 40)
        public static int pistol_damage = 8;
        @Config.RequiresMcRestart
        @Config.RangeInt(min = 0, max = 50)
        @Config.Comment("affects musket and repeater handgun")
        public static int rifle_damage = 13;
        @Config.RequiresMcRestart
        @Config.RangeDouble(min = 0, max = 50)
        public static int gunsword_damage = 8;

        @Config.RequiresMcRestart
        @Config.RangeInt(min = 0, max = 50)
        public static int blunderbusses_damage = 21;

        @Config.RequiresMcRestart
        @Config.RangeDouble(min = -5, max = 20)
        @Config.Comment("for reference the sword has a speed of 2.4 (lower is faster)")
        public static double spear_speed = 2;
        @Config.RequiresMcRestart
        @Config.RangeDouble(min = -5, max = 20)
        public static double warhammer_speed = 2.9;
        @Config.RequiresMcRestart
        @Config.RangeDouble(min = -5, max = 20)
        public static double knife_speed = -5;
        @Config.RequiresMcRestart
        @Config.RangeDouble(min = -5, max = 20)
        public static double halberd_speed = 2.8;
        @Config.RequiresMcRestart
        @Config.RangeInt(min = 0, max = 1000)
        public static int pistol_speed = 25;
        @Config.RequiresMcRestart
        @Config.RangeInt(min = 0, max = 1000)
        @Config.Comment("affects musket and repeater hangun")
        public static int rifle_reload_speed = 40;
        @Config.RequiresMcRestart
        @Config.RangeInt(min = 0, max = 1000)
        public static int gunsword_reload_speed = 25;
        @Config.RequiresMcRestart
        @Config.RangeInt(min = 0, max = 1000)
        public static int grenadelauncher_reload_speed = 40;
        @Config.RequiresMcRestart
        @Config.RangeInt(min = 0, max = 1000)
        public static int drakegun_reload_speed = 40;

        @Config.RequiresMcRestart
        @Config.RangeInt(min = 0, max = 100)
        public static int blunderbusses_speed = 40;

        @Config.RequiresMcRestart
        @Config.RangeInt(min = 1, max = 100000)
        @Config.Comment("base shield durability is 336")
        public static int shields_durability = 1008;

        @Config.RequiresMcRestart
        @Config.RangeInt(min = 0, max = 2000)
        public static int drakegun_ammo = 45;

        @Config.RequiresMcRestart
        @Config.RangeDouble(min = 0, max = 100)
        public static double Ghal_maraz_speed = 2.35;

        @Config.RequiresMcRestart
        @Config.RangeDouble(min = 0, max = 100)
        public static double Ghal_maraz_damage = 9;
    }
}
