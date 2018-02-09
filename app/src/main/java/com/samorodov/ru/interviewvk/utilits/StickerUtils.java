package com.samorodov.ru.interviewvk.utilits;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StickerUtils {

    public static List<Uri> getStickersUriList() {
        int size = 24;
        List<Uri> uriList = new ArrayList<>(24);
        for (int i = 0; i < size; i++) {
            uriList.add(Uri.parse(String.format(
                    Locale.UK, "file:///android_asset/stickers/%d.png", i + 1)
            ));
        }

        return uriList;
    }
}
