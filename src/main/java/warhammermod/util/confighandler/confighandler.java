package warhammermod.util.confighandler;

import net.minecraftforge.common.config.Config;
import warhammermod.util.reference;

public class confighandler {

    @Config(modid = reference.modid, type = Config.Type.INSTANCE, name = reference.modid+ "_types")

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
}
