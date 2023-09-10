package com.github.zly2006.pinyinedmasa;

import net.fabricmc.api.ModInitializer;

public class PinyinedMasa implements ModInitializer {
    @Override
    public void onInitialize() {
        PinyinDict.init();
    }
}
