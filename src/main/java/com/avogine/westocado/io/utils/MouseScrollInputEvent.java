package com.avogine.westocado.io.utils;

public class MouseScrollInputEvent {

	public static final int MOUSE_SCROLL = 1;
	
	private final int eventType;
	private final double dx;
	private final double dy;
	
	public MouseScrollInputEvent(int eventType, double dx, double dy) {
		this.eventType = eventType;
		this.dx = dx;
		this.dy = dy;
	}
	
	public int getEventType() {
		return eventType;
	}
	
	public double getDx() {
		return dx;
	}
	
	public double getDy() {
		return dy;
	}
}
