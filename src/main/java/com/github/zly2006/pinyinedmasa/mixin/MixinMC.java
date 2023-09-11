package com.github.zly2006.pinyinedmasa.mixin;

import com.github.zly2006.pinyinedmasa.PinyinDict;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Mixin(MinecraftClient.class)
public abstract class MixinMC {
    @Shadow public abstract void loadBlockList();

    @Inject(
            method = "method_1485",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void addPinYin4creative(ItemStack stack, CallbackInfoReturnable<Stream<String>> cir) {
        List<String> list = new ArrayList<>(stack.getTooltip(null, TooltipContext.Default.BASIC.withCreative()).stream()
                .map((tooltip) -> Formatting.strip(tooltip.getString()).trim())
                .filter((string) -> !string.isEmpty())
                .toList());
        list.addAll(PinyinDict.getFirstAndFullPinyin(String.join(" ", list)));
        cir.setReturnValue(list.stream());
    }

    @Inject(
            method = "method_43761",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void addPinYin4craft(RecipeResultCollection resultCollection, CallbackInfoReturnable<Stream<String>> cir) {
        List<String> list = new ArrayList<>(resultCollection.getAllRecipes().stream()
                .flatMap(recipe -> recipe.getOutput(resultCollection.getRegistryManager()).getTooltip(null, TooltipContext.Default.BASIC).stream())
                .map(text -> Formatting.strip(text.getString()).trim())
                .filter(text -> !text.isEmpty()).toList());
        list.addAll(PinyinDict.getFirstAndFullPinyin(String.join(" ", list)));
        cir.setReturnValue(list.stream());
    }
}
