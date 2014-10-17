package com.percolate.coffee.util.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.view.View;

/**
 * Created by AlexanderEmmanuel on 2014-10-16.
 */
public abstract class AnimationFactory {
	protected final View mView;


	protected AnimatorSet animSet = new AnimatorSet();
	protected int mDuration = 500;

	private Runnable mOnStartAnimationRunnable;
	private Runnable mOnEndAnimationRunnable;
	private Runnable mOnCancelAnimationRunnable;
	private Runnable mOnRepeatAnimationRunnable;

	public AnimationFactory(View view) {
		mView = view;
	}

	public AnimationFactory onStartAnimationRunnable(Runnable onStartAnimationRunnable) {
		mOnStartAnimationRunnable = onStartAnimationRunnable;
		return this;
	}

	public AnimationFactory onEndAnimationRunnable(Runnable onEndAnimationRunnable) {
		mOnEndAnimationRunnable = onEndAnimationRunnable;
		return this;
	}

	public AnimationFactory onCancelAnimationRunnable(Runnable onCancelAnimationRunnable) {
		mOnCancelAnimationRunnable = onCancelAnimationRunnable;
		return this;
	}

	public AnimationFactory onRepeatAnimationRunnable(Runnable onRepeatAnimationRunnable) {
		mOnRepeatAnimationRunnable = onRepeatAnimationRunnable;
		return this;
	}

	public AnimationFactory duration(int duration) {
		mDuration = duration;
		return this;
	}

	public void animate() {
		animSet.addListener(new Animator.AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animator) {

				if (mOnStartAnimationRunnable != null) {
					mOnStartAnimationRunnable.run();
				}
			}

			@Override
			public void onAnimationEnd(Animator animator) {
				if (mOnEndAnimationRunnable != null) {
					mOnEndAnimationRunnable.run();
				}

			}

			@Override
			public void onAnimationCancel(Animator animator) {
				if (mOnCancelAnimationRunnable != null) {
					mOnCancelAnimationRunnable.run();
				}

			}

			@Override
			public void onAnimationRepeat(Animator animator) {

				if (mOnRepeatAnimationRunnable != null) {
					mOnRepeatAnimationRunnable.run();
				}
			}
		});

		animSet.setDuration(mDuration);
		animSet.start();

	}
}
