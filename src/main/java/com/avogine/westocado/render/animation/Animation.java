package com.avogine.westocado.render.animation;

import java.util.List;

import org.joml.Matrix4f;

import com.avogine.westocado.Theater;

public class Animation {

	private int currentFrame;

	private List<AnimatedFrame> frames;

	private String name;

	private double duration;

	private double animationStep;

	public Animation(String name, List<AnimatedFrame> frames, double duration) {
		this.name = name;
		this.frames = frames;
		currentFrame = 0;
		this.duration = duration;
	}

	public void animate() {
		animationStep += Theater.getDelta();
		if(animationStep >= getFrameDuration()) {
			nextFrame();
			animationStep = 0;
		}
	}

	public AnimatedFrame getCurrentFrame() {
		return this.frames.get(currentFrame);
	}

	public AnimatedFrame getCurrentFrameTweened() {
		AnimatedFrame current = frames.get(currentFrame);
		AnimatedFrame next = frames.get((currentFrame + 1 >= frames.size()) ? 0 : currentFrame + 1);

		AnimatedFrame tween = new AnimatedFrame();
		float mixFactor = (float) (animationStep / getFrameDuration());
		for(int i = 0; i < AnimatedFrame.MAX_JOINTS; i++) {
			Matrix4f tweenMatrix = current.getJointMatrices()[i].lerp(next.getJointMatrices()[i], mixFactor, new Matrix4f());
			tween.setMatrix(i, tweenMatrix);
		}

		return tween;
	}

	public double getDuration() {
		return this.duration;        
	}

	public double getFrameDuration() {
		return frames.size() / duration;
	}

	public List<AnimatedFrame> getFrames() {
		return frames;
	}

	public String getName() {
		return name;
	}

	public AnimatedFrame getNextFrame() {
		nextFrame();
		return this.frames.get(currentFrame);
	}

	public void nextFrame() {
		int nextFrame = currentFrame + 1;
		if (nextFrame > frames.size() - 1) {
			currentFrame = 0;
		} else {
			currentFrame = nextFrame;
		}
	}

}
