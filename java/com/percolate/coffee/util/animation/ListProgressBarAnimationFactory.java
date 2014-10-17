package com.percolate.coffee.util.animation;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Created by AlexanderEmmanuel on 2014-10-16.
 */
public class ListProgressBarAnimationFactory extends AnimationFactory {

	private ObjectAnimator moveAnim;

	private boolean mShown = false;

	private static float sOvershootTension  = 5f;
	private static float sDecelerateTension = 2f;

	public static final int PROGRESS_BAR_START_POSITION = 25;
	public static final int PROGRESS_BAR_END_POSITION   = -100;

	private static final OvershootInterpolator  sOvershootInterpolator  = new OvershootInterpolator(sOvershootTension);
	private static final DecelerateInterpolator sDecelerateInterpolator = new DecelerateInterpolator(sDecelerateTension);


	public ListProgressBarAnimationFactory(View view) {
		super(view);
	}

	public AnimationFactory shown(boolean shown) {
		mShown = shown;
		return this;
	}

	@Override
	public void animate() {

		moveAnim = ObjectAnimator.ofFloat(
				mView,
				View.TRANSLATION_Y,
				mShown ? PROGRESS_BAR_START_POSITION : PROGRESS_BAR_END_POSITION,
				mShown ? PROGRESS_BAR_END_POSITION : PROGRESS_BAR_START_POSITION);

		moveAnim.setInterpolator(mShown ? sDecelerateInterpolator : sOvershootInterpolator);
		moveAnim.setDuration(mDuration);
		animSet.play(moveAnim);

		super.animate();
	}
}
