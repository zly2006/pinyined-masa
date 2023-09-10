package com.github.zly2006.pinyinedmasa.mixin;

import net.minecraft.client.search.TextSearchProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Function;
import java.util.stream.Stream;

@Mixin(TextSearchProvider.class)
public interface AMixinTextSearchProvider {
    @Accessor("textsGetter")
    void textsGetter(Function<Object, Stream<String>> textsGetter);
}
