package com.zmide.lit.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class Slide {
	public static void slideToUp(View view) {
		Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		
		slide.setDuration(300);
		slide.setFillAfter(true);
		slide.setFillEnabled(true);
		view.startAnimation(slide);
		
		slide.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
			
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			
			}
		});
		
	}
}

