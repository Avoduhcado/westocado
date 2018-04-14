package com.avogine.westocado.entities.controllers;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.avogine.westocado.entities.Entity;
import com.avogine.westocado.entities.bodies.utils.JBulletBodyParams;
import com.avogine.westocado.io.utils.MouseButtonInputEvent;
import com.avogine.westocado.io.utils.MouseButtonInputListener;
import com.bulletphysics.collision.shapes.SphereShape;

public class ShotController extends Controller implements MouseButtonInputListener {

	private float shotCooldown = 0;
	
	public ShotController(Entity entity) {
		super(entity);
	}

	@Override
	public void mouseButtonPressed(MouseButtonInputEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseButtonReleased(MouseButtonInputEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseButtonHeld(MouseButtonInputEvent e) {
		
		entity.getContainer().getPhysics().createBody(new Entity(entity.getContainer()), new JBulletBodyParams(new SphereShape(1), new Vector3f(), new Quat4f()));
	}

	@Override
	public void control() {
		// TODO Auto-generated method stub

	}

}
