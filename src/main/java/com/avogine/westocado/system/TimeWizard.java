package com.avogine.westocado.system;

import java.util.ArrayList;
import java.util.List;

import com.avogine.westocado.io.utils.TimeEvent;
import com.avogine.westocado.io.utils.TimeListener;

public class TimeWizard {

	private static List<TimeListener> listeners = new ArrayList<>();
	
	public static List<TimeListener> getListeners() {
		return listeners;
	}
	
	public static void addListener(TimeListener l) {
		listeners.add(l);
	}
	
	public static void removeListener(TimeListener l) {
		listeners.remove(l);
	}
	
	public static void fireEvent(TimeEvent e) {
		listeners.stream()
			.forEach(t -> t.timePassed(e));
	}
	
}
