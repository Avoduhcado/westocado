package com.avogine.westocado.io.utils;

public interface TimeListener {

	public void timePassed(TimeEvent e);
	
	// TODO isWaiting method? Add a filter when time events are fired to check if a listener is expecting an update?
	
}
