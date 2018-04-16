package com.avogine.westocado.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.avogine.westocado.entities.bodies.Body;
import com.avogine.westocado.entities.components.EntityComponent;
import com.avogine.westocado.entities.components.LightEmitter;
import com.avogine.westocado.entities.controllers.Controller;
import com.avogine.westocado.entities.models.Model;

public class Entities {

	private static long entityCount = 0;
	private static List<Long> entityList = new ArrayList<>();
	
	public static Map<Long, Body> bodyComponentMap = new HashMap<>();
	public static Map<Long, Model> modelComponentMap = new HashMap<>();
	public static Map<Long, Controller> controllerComponentMap = new HashMap<>();
	public static Map<Long, LightEmitter> lightComponentMap = new HashMap<>();
	
	public static long getNewEntity() {
		return entityCount++;
	}
	
	public static long reserveNewEntity() {
		entityList.add(entityCount++);
		return entityCount;
	}
	
	public static void addEntity(long entity) {
		entityList.add(entity);
	}
	
	public static boolean removeEntity(long entity) {
		return entityList.remove(entity);
	}
	
	public static void registerComponent(long entity, EntityComponent component) {
		if(component instanceof Body) {
			bodyComponentMap.put(entity, (Body) component);
		} else if(component instanceof Model) {
			modelComponentMap.put(entity, (Model) component);
		} else if(component instanceof Controller) {
			controllerComponentMap.put(entity, (Controller) component);
		} else if(component instanceof LightEmitter) {
			lightComponentMap.put(entity, (LightEmitter) component);
		} else {
			System.err.println("Trying to register a component of type: '" + component.getClass() + "' for entity: '" + entity + "' but there's no available component map.");
		}
	}
	
}
