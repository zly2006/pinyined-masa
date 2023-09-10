package com.github.zly2006.pinyinedmasa;

import fi.dy.masa.malilib.config.IConfigBase;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.resource.language.I18n;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PinyinDict {
    public static String getDisplayName(IConfigBase config) {
        return I18n.translate(config.getPrettyName());
    }
    public static List<String> getFirstAndFullPinyin(String entryString) {
        List<String> fullPin = List.of(entryString);
        List<String> fullPinNoTone = List.of(entryString);
        List<String> firstPin = List.of(entryString);
        for (int i = 0; i < entryString.length(); i++) {
            var c = entryString.charAt(i);
            var charAsStr = String.valueOf(c);
            var pronounces = PinyinDict.getString(c);
            if (pronounces != null) {
                fullPin = fullPin.stream().mapMulti((s, consumer) -> {
                    for (var pronounce : pronounces) {
                        consumer.accept(s.replace(charAsStr, pronounce));
                    }
                }).map(x -> (String) x).toList();
                fullPinNoTone = fullPinNoTone.stream().mapMulti((s, consumer) -> {
                    for (var pronounce : pronounces) {
                        consumer.accept(s.replace(charAsStr, pronounce.substring(0, pronounce.length() - 1)));
                    }
                }).map(x -> (String) x).toList();
                firstPin = firstPin.stream().mapMulti((s, consumer) -> {
                    for (var pronounce : Arrays.stream(pronounces).map(x -> x.charAt(0)).collect(Collectors.toSet())) {
                        consumer.accept(s.replace(c, pronounce));
                    }
                }).map(x -> (String) x).toList();
            }
        }
        var all = new ArrayList<String>();
        all.addAll(fullPin);
        all.addAll(fullPinNoTone);
        all.addAll(firstPin);
        return all;
    }
    public static @Nullable String[] getString(int charCode) {
        return dict.get(charCode);
    }
    static Int2ObjectOpenHashMap<String[]> dict = new Int2ObjectOpenHashMap<>();
    static void init() {
        try (InputStream stream = PinyinDict.class.getClassLoader().getResourceAsStream("assets/pinyinedmasa/mandarin.txt")) {
            if (stream != null) {
                var buffered = new BufferedReader(new InputStreamReader(stream));
                while (buffered.ready()) {
                    var line = buffered.readLine();
                    if (line.isEmpty()) break;
                    var split = line.split(" ");
                    if (split[0].startsWith("U+")) {
                        var charCode = Integer.parseInt(split[0].substring(2), 16);
                        dict.put(charCode, Arrays.copyOfRange(split, 1, split.length));
                    }
                }
            }
            else {
                throw new RuntimeException("Failed to load pinyin dict");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
