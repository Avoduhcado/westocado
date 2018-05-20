package com.avogine.westocado.entities.utils;

import java.util.HashMap;

import com.avogine.westocado.entities.components.EntityComponent;
import com.avogine.westocado.utils.system.AvoEvent;

public class EntityComponentMap<T extends EntityComponent> extends HashMap<Long, T> {
	private static final long serialVersionUID = 1L;
	
	public void fireEventAt(Long entity, AvoEvent event) {
		if(!containsKey(entity)) {
			return;
		}
		
		get(entity).fireEvent(event);
	}
	
}
