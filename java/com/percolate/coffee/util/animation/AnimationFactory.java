package com.percolate.coffee.util.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.view.View;

/**
 * Basic builder which makes generating, managing and standardizing animations much simpler
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

	/**
	 * @param onStartAnimationRunnable A runnable which is desired to be run when the animation starts
	 * @return
	 */
	public AnimationFactory onStartAnimationRunnable(Runnable onStartAnimationRunnable) {
		mOnStartAnimationRunnable = onStartAnimationRunnable;
		return this;
	}

	/**
	 * @param onEndAnimationRunnable A runnable which is desired to be run when the animation ends
	 * @return
	 */
	public AnimationFactory onEndAnimationRunnable(Runnable onEndAnimationRunnable) {
		mOnEndAnimationRunnable = onEndAnimationRunnable;
		return this;
	}

	/**
	 * @param onCancelAnimationRunnable A runnable which is desired to be run when the animation is canceled
	 * @return
	 */
	public AnimationFactory onCancelAnimationRunnable(Runnable onCancelAnimationRunnable) {
		mOnCancelAnimationRunnable = onCancelAnimationRunnable;
		return this;
	}

	/**
	 * @param onRepeatAnimationRunnable A runnable which is desired to be run everytime the animation repeats
	 * @return
	 */
	public AnimationFactory onRepeatAnimationRunnable(Runnable onRepeatAnimationRunnable) {
		mOnRepeatAnimationRunnable = onRepeatAnimationRunnable;
		return this;
	}

	/**
	 *
	 * @param duration the duration of this animation when {@link AnimationFactory#animate()} is called
	 * @return
	 */
	public AnimationFactory duration(int duration) {
		mDuration = duration;
		return this;
	}

	/**
	 * builds and runs the animation on the specified view
	 */
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
