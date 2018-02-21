package com.samorodov.ru.interviewvk.domain;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.samorodov.ru.interviewvk.data.repository.StickersRepository;
import com.samorodov.ru.interviewvk.domain.base.BaseInteractor;
import com.samorodov.ru.interviewvk.presentation.ui.view.PhotoEditorView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class PhotoEditorInteractor extends BaseInteractor {

    private final StickersRepository repository;

    @Inject
    public PhotoEditorInteractor(StickersRepository repository) {
        this.repository = repository;
    }

    public Observable<List<Uri>> loadStickers() {
        return repository.getStickersUriList()
                .compose(applySchedulers());
    }

    public Observable<String> saveAndAddToGallery(PhotoEditorView editorView) {
        return Observable.just(editorView)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(view -> editorView.preDrawIntoBitmap())
                .observeOn(Schedulers.computation())
                .map(view -> {
                    Bitmap bitmap = editorView.prepareBitmap();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd_HH:mm:ss", Locale.ENGLISH);
                    return MediaStore.Images.Media.insertImage(
                            editorView.getContext().getContentResolver(),
                            bitmap,
                            "interviewVK" + formatter.format(new Date()),
                            "Сделано с помощью interviewVK.\nС любовью \n<3 мур мур мур");
                })
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
