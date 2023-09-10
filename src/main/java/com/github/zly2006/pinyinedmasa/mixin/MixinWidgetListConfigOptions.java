package com.github.zly2006.pinyinedmasa.mixin;

import com.github.zly2006.pinyinedmasa.PinyinDict;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.widgets.WidgetListConfigOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = WidgetListConfigOptions.class, remap = false)
public class MixinWidgetListConfigOptions {
    @Inject(
            method = "getEntryStringsForFilter(Lfi/dy/masa/malilib/gui/GuiConfigsBase$ConfigOptionWrapper;)Ljava/util/List;",
            at = @At("RETURN")
    )
    private void addTranslatedStrings(GuiConfigsBase.ConfigOptionWrapper entry, CallbackInfoReturnable<List<String>> cir) {
        if (entry.getConfig() == null) return;
        var translated = PinyinDict.getDisplayName(entry.getConfig());
        if (translated != null && !cir.getReturnValue().contains(translated)) {
            cir.getReturnValue().add(translated);
        }
    }
}
