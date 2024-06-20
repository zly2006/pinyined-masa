package com.github.zly2006.pinyinedmasa.mixin;

import com.github.zly2006.pinyinedmasa.PinyinDict;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.client.search.SearchManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Mixin(SearchManager.class)
public abstract class MixinSearchManager {
    @Inject(
            method = "method_60349",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void addPinYin4creative(Item.TooltipContext tooltipContext, TooltipType tooltipType, ItemStack stack, CallbackInfoReturnable<Stream<String>> cir) {
        List<String> list = new ArrayList<>(stack.getTooltip(Item.TooltipContext.DEFAULT, null, TooltipType.Default.BASIC.withCreative()).stream()
                .map((tooltip) -> Formatting.strip(tooltip.getString()).trim())
                .filter((string) -> !string.isEmpty())
                .toList());
        list.addAll(PinyinDict.getFirstAndFullPinyin(String.join(" ", list)));
        cir.setReturnValue(list.stream());
    }

    @Inject(
            method = "method_60360",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void addPinYin4craft(DynamicRegistryManager.Immutable immutable, Item.TooltipContext tooltipContext, TooltipType tooltipType, RecipeResultCollection resultCollection, CallbackInfoReturnable<Stream<String>> cir) {
        List<String> list = new ArrayList<>(resultCollection.getAllRecipes().stream()
                .flatMap(recipe -> recipe.value().getResult(resultCollection.getRegistryManager()).getTooltip(Item.TooltipContext.DEFAULT, null, TooltipType.Default.BASIC).stream())
                .map(text -> Formatting.strip(text.getString()).trim())
                .filter(text -> !text.isEmpty()).toList());
        list.addAll(PinyinDict.getFirstAndFullPinyin(String.join(" ", list)));
        cir.setReturnValue(list.stream());
    }
}
