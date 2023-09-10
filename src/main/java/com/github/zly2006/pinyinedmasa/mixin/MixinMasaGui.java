package com.github.zly2006.pinyinedmasa.mixin;

import com.github.zly2006.pinyinedmasa.PinyinDict;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.interfaces.IConfigInfoProvider;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GuiConfigsBase.class, remap = false)
public class MixinMasaGui {
    // Debug, not used
    @Shadow protected IConfigInfoProvider hoverInfoProvider;

    @Inject(
            method = "setHoverInfoProvider",
            at = @At("RETURN")
    )
    private void setHoverInfoProvider(IConfigInfoProvider provider, CallbackInfoReturnable<GuiConfigsBase> cir) {
        this.hoverInfoProvider = config -> {
            var sb = new StringBuilder(provider.getHoverInfo(config));
            sb.append("\nPinyin info: ")
                    .append(PinyinDict.getDisplayName(config))
                    .append("\n");
            PinyinDict.getFirstAndFullPinyin(PinyinDict.getDisplayName(config)).forEach(x -> sb.append(x).append("\n"));
            return sb.toString();
        };
    }

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void init(int listX, int listY, String modId, Screen parent, String titleKey, Object[] args, CallbackInfo ci) {
        this.hoverInfoProvider = config -> {
            var comment = config.getComment();
            var sb = new StringBuilder(comment != null ? comment : "");
            sb.append("\nPinyin info: ")
                    .append(PinyinDict.getDisplayName(config))
                    .append("\n");
            PinyinDict.getFirstAndFullPinyin(PinyinDict.getDisplayName(config)).forEach(x -> sb.append(x).append("\n"));
            return sb.toString();
        };
    }
}
