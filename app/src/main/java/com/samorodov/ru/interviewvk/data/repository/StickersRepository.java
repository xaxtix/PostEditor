package com.samorodov.ru.interviewvk.data.repository;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Observable;

public class StickersRepository {

    private volatile List<Uri> stickers = null;

    @Inject
    StickersRepository() {
    }

    public Observable<List<Uri>> getStickersUriList() {
        return Observable.fromCallable(() -> {
            List<Uri> localInstance = stickers;
            if (localInstance == null) {
                synchronized (StickersRepository.class) {
                    localInstance = stickers;
                    if (localInstance == null) {
                        int size = 24;
                        stickers = new ArrayList<>(24);
                        for (int i = 0; i < size; i++) {
                            stickers.add(Uri.parse(String.format(
                                    Locale.UK, "file:///android_asset/stickers/%d.png",
                                    i + 1)
                            ));
                        }
                        localInstance = stickers;
                    }
                }
            }

            return localInstance;
        });
    }
}
