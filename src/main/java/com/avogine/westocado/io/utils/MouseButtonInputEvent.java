package com.avogine.westocado.io.utils;

public class MouseButtonInputEvent {

	public static final int MOUSE_PRESS = 1;
	
	public static final int MOUSE_RELEASE = 2;
	
	public static final int MOUSE_HELD = 3;
	
	private final int eventType;
	private final int mouseButton;
	
	public MouseButtonInputEvent(int eventType, int mouseButton) {
		this.eventType = eventType;
		this.mouseButton = mouseButton;
	}
	
	public int getEventType() {
		return eventType;
	}
	
	public int getMouseButton() {
		return mouseButton;
	}
}
