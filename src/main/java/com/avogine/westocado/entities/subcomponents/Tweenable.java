package com.avogine.westocado.entities.subcomponents;

import com.avogine.westocado.entities.components.EntityComponent;
import com.avogine.westocado.io.utils.TimeListener;
import com.avogine.westocado.system.AvoEventQueue;
import com.avogine.westocado.system.TimeWizard;
import com.avogine.westocado.utils.math.MathUtils;
import com.avogine.westocado.utils.math.TweenUtils.Tween;

public abstract class Tweenable extends SubComponent implements TimeListener {

	private float currentTime;
	private float value;
	private float targetValue;
	private float duration;
	
	private Tween tween;
	
	public Tweenable(EntityComponent parent, float value, float targetValue, float duration, Tween tween) {
		super(parent);
		TimeWizard.addListener(this);
		this.currentTime = 0;
		this.value = value;
		this.targetValue = targetValue;
		this.duration = duration;
		this.tween = tween;
	}
	
	public float tween(float timeChange) {
		// Prevent overtween
		currentTime = MathUtils.clamp(currentTime + timeChange, 0, duration);
		if(currentTime >= duration) {
			finish();
		}
		float val = tween.getTweenedValue(currentTime, value, targetValue, duration);
		return val;
	}
	
	public void finish() {
		AvoEventQueue.doLater(() -> {
			TimeWizard.removeListener(this);
		});
	}
	
}
