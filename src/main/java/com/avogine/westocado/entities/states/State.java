package com.avogine.westocado.entities.states;

import com.avogine.westocado.entities.components.EntityComponent;
import com.avogine.westocado.entities.states.utils.EntityState;
import com.avogine.westocado.utils.system.AvoEvent;

public class State extends EntityComponent {

	private EntityState state;
	
	public State(long entity) {
		super(entity);
	}

	@Override
	public void fireEvent(AvoEvent e) {
		// TODO Auto-generated method stub

	}

}
