package com.github.zly2006.pinyinedmasa.mixin;

import com.github.zly2006.pinyinedmasa.PinyinDict;
import fi.dy.masa.malilib.gui.widgets.WidgetListBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = WidgetListBase.class, remap = false)
public abstract class MixinWidgetListBase {
    @Inject(
            method = "matchesFilter(Ljava/lang/String;Ljava/lang/String;)Z",
            at = @At("RETURN"),
            cancellable = true
    )
    private void matchesFilter(String entryString, String filterText, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            var pinyin = PinyinDict.getFirstAndFullPinyin(entryString);
            for (String filter : filterText.split("\\|")) {
                if (pinyin.stream().anyMatch(x -> x.contains(filter))) {
                    cir.setReturnValue(true);
                }
            }
        }
    }
}
