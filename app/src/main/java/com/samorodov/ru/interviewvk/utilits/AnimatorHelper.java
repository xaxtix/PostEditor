package com.samorodov.ru.interviewvk.utilits;

import android.animation.Animator;
import android.support.annotation.NonNull;
import android.view.animation.Animation;

import com.annimon.stream.Optional;
import com.annimon.stream.function.Consumer;
import com.transitionseverywhere.Transition;

/**
 * Created by loskin on 08.12.2017.
 */
public class AnimatorHelper<T> implements
        Animator.AnimatorListener,
        Animation.AnimationListener,
        Transition.TransitionListener,
        android.support.transition.Transition.TransitionListener {
    private Optional<Consumer<Adapter>> onCancel = Optional.empty();
    private Optional<Consumer<Adapter>> onRepeat = Optional.empty();
    private Optional<Consumer<Adapter>> onResume = Optional.empty();
    private Optional<Consumer<Adapter>> onPause = Optional.empty();
    private Optional<Consumer<Adapter>> onStart = Optional.empty();
    private Optional<Consumer<Adapter>> onEnd = Optional.empty();

    private AnimatorHelper() {
    }

    private static AnimatorHelper build() {
        return new AnimatorHelper<>();
    }

    public static <T> AnimatorHelper<T> onEnd(Consumer<T> end) {
        return build().withEnd(end);
    }

    public static <T> AnimatorHelper<T> onStart(Consumer<T> start) {
        return build().withStart(start);
    }

    public static <T> AnimatorHelper<T> onRepeat(Consumer<T> repeat) {
        return build().withRepeat(repeat);
    }

    public static <T> AnimatorHelper<T> apply() {
        return build();
    }

    public AnimatorHelper<T> withEnd(Consumer<T> end) {
        this.onEnd = buildAdapter(end);
        return this;
    }

    private Optional<Consumer<Adapter>> buildAdapter(Consumer<T> end) {
        return Optional.ofNullable(end).map(anim -> (adapter -> adapter.resolve(anim)));
    }

    public AnimatorHelper<T> withStart(Consumer<T> start) {
        this.onStart = buildAdapter(start);
        return this;
    }

    public AnimatorHelper<T> withCancel(Consumer<T> cancel) {
        this.onCancel = buildAdapter(cancel);
        return this;
    }

    public AnimatorHelper<T> withRepeat(Consumer<T> repeat) {
        this.onRepeat = buildAdapter(repeat);
        return this;
    }

    @Override
    public void onAnimationEnd(Animator animator) {
        doAnimation(onEnd, Adapter.create(animator));
    }

    @Override
    public void onAnimationCancel(Animator animator) {
        doAnimation(onCancel, Adapter.create(animator));
    }

    @Override
    public void onAnimationStart(Animator animator, boolean isReverse) {
        onAnimationStart(animator);
    }

    @Override
    public void onAnimationEnd(Animator animator, boolean isReverse) {
        onAnimationEnd(animator);
    }

    @Override
    public void onAnimationStart(Animator animation) {
        doAnimation(onStart, Adapter.create(animation));
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
        doAnimation(onRepeat, Adapter.create(animation));
    }

    @Override
    public void onAnimationStart(Animation animation) {
        doAnimation(onStart, Adapter.create(animation));
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        doAnimation(onEnd, Adapter.create(animation));
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        doAnimation(onRepeat, Adapter.create(animation));
    }

    private void doAnimation(Optional<Consumer<Adapter>> animation, Adapter animator) {
        animation.ifPresent(animatorConsumer -> animatorConsumer.accept(animator));
    }

    @Override
    public void onTransitionStart(Transition transition) {
        doAnimation(onStart, Adapter.create(transition));
    }

    @Override
    public void onTransitionEnd(Transition transition) {
        doAnimation(onEnd, Adapter.create(transition));
    }

    @Override
    public void onTransitionCancel(Transition transition) {
        doAnimation(onCancel, Adapter.create(transition));
    }

    @Override
    public void onTransitionPause(Transition transition) {
        doAnimation(onPause, Adapter.create(transition));
    }

    @Override
    public void onTransitionResume(Transition transition) {
        doAnimation(onResume, Adapter.create(transition));
    }

    @Override
    public void onTransitionStart(@NonNull android.support.transition.Transition transition) {
        doAnimation(onStart, Adapter.create(transition));
    }

    @Override
    public void onTransitionEnd(@NonNull android.support.transition.Transition transition) {
        doAnimation(onEnd, Adapter.create(transition));
    }

    @Override
    public void onTransitionCancel(@NonNull android.support.transition.Transition transition) {
        doAnimation(onCancel, Adapter.create(transition));
    }

    @Override
    public void onTransitionPause(@NonNull android.support.transition.Transition transition) {
        doAnimation(onPause, Adapter.create(transition));
    }

    @Override
    public void onTransitionResume(@NonNull android.support.transition.Transition transition) {
        doAnimation(onResume, Adapter.create(transition));
    }

    public static class Adapter<T> {
        public final Optional<T> adapter;

        private Adapter(T animation) {
            this.adapter = Optional.ofNullable(animation);
        }

        public static Adapter create(Animation animation) {
            return new Adapter<>(animation);
        }

        public static Adapter create(Animator animator) {
            return new Adapter<>(animator);
        }

        public static Adapter create(Transition transition) {
            return new Adapter<>(transition);
        }

        public static Adapter create(android.support.transition.Transition transition) {
            return new Adapter<>(transition);
        }

        public void resolve(Consumer<T> end) {
            adapter.ifPresent(end);
        }
    }
}

