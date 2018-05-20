package com.avogine.westocado.utils.math;

public class TweenUtils {

	public enum Tween {
		IN, OUT, IN_OUT, LINEAR;
		
		public float getTweenedValue(float time, float start, float change, float duration) {
			switch(this) {
			case IN:
				return easeIn(time, start, change, duration);
			case OUT:
				return easeOut(time, start, change, duration);
			case IN_OUT:
				return easeInOut(time, start, change, duration);
			case LINEAR:
				return linearTween(time, start, change, duration);
			default:
				return -1;
			}
		}
	}
	
	/**
	 * Change over time at linear rate
	 */
	public static float linearTween(float time, float begin, float change, float duration) {
		return change * time / duration + begin;
	}
	
	/**
	 * Start slow then move quickly
	 */
	public static float easeIn(float t, float b, float c, float d) {
		return c*(t/=d)*t + b;
	}
	
	/**
	 * Start fast then slow down
	 */
	public static float easeOut(float t, float b, float c, float d) {
		return -c *(t/=d)*(t-2) + b;
	}
	
	public static float easeInOut(float t, float b, float c, float d) {
		if ((t/=d/2) < 1) return c/2*t*t + b;
		return -c/2 * ((--t)*(t-2) - 1) + b;
	}
	
}
