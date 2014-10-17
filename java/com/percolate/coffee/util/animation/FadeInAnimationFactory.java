package com.percolate.coffee.util.animation;

import android.animation.ObjectAnimator;
import android.transition.Fade;
import android.transition.Transition;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by AlexanderEmmanuel on 2014-10-17.
 */
public class FadeInAnimationFactory extends AnimationFactory  {

	private static float sDecelerateTension = 10f;
	private float mFadeStartValue = 0f;
	private float mFadeEndValue = 1f;

	private ObjectAnimator mfadeAnim;
	private static DecelerateInterpolator sDecelerateInterpolator = new DecelerateInterpolator(sDecelerateTension);

	public FadeInAnimationFactory(View view) {
		super(view);
	}

	public FadeInAnimationFactory decelerateTension(float decelerateTension){
		sDecelerateTension = decelerateTension;
		sDecelerateInterpolator = new DecelerateInterpolator(sDecelerateTension);

		return this;
	}

	public FadeInAnimationFactory fadeStartValue(float fadeStartValue){
		mFadeStartValue = fadeStartValue;
		return this;
	}

	public FadeInAnimationFactory fadeEndValue(float fadeEndValue){
		mFadeEndValue = fadeEndValue;
		return this;
	}

	@Override
	public void animate() {
		mfadeAnim = ObjectAnimator.ofFloat(
				mView, View.ALPHA, mFadeStartValue, mFadeEndValue);

		mfadeAnim.setInterpolator(sDecelerateInterpolator);
		mfadeAnim.setDuration(mDuration);

		animSet.play(mfadeAnim);

		super.animate();
	}
}
