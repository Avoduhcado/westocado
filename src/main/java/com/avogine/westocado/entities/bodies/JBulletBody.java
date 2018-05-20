package com.avogine.westocado.entities.bodies;

import org.joml.Matrix3f;
import org.joml.Vector3f;

import com.avogine.westocado.entities.Entities;
import com.avogine.westocado.entities.bodies.utils.BodyListener;
import com.avogine.westocado.entities.bodies.utils.MovementEvent;
import com.avogine.westocado.entities.bodies.utils.RotationEvent;
import com.avogine.westocado.entities.bodies.utils.SpeedChangeEvent;
import com.avogine.westocado.entities.states.utils.EntityState;
import com.avogine.westocado.entities.states.utils.StateChangeEvent;
import com.avogine.westocado.render.data.Mesh;
import com.avogine.westocado.utils.loader.MeshLoader;
import com.avogine.westocado.utils.math.BTUtils;
import com.avogine.westocado.utils.math.ConversionUtils;
import com.avogine.westocado.utils.system.AvoEvent;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.Transform;

public class JBulletBody extends Body implements BodyListener {

	private RigidBody rigidBody;
	private final Transform transform = new Transform();
	
	private Vector3f velocity = new Vector3f();
	private Vector3f linearVelocity = new Vector3f();
	
	private float speed = 15f;
	
	private Mesh debugMesh;
	
	public JBulletBody(long entity, RigidBody rigidBody) {
		super(entity);
		this.rigidBody = rigidBody;
		try {
			debugMesh = MeshLoader.load("testsphere.nff").getFirst()[0];
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Vector3f getPosition() {
		transform.setIdentity();
		rigidBody.getMotionState().getWorldTransform(transform);
		return ConversionUtils.convertVecmathToJoml(transform.origin);
	}
	
	@Override
	public Mesh getDebugMesh() {
		return debugMesh;
	}
	
	@Override
	public void applyForce(Vector3f force) {
		rigidBody.applyCentralImpulse(ConversionUtils.convertJomlToVecmath(force));
	}
	
	public RigidBody getRigidBody() {
		return rigidBody;
	}

	private Matrix3f getInvertedYaw() {
		return new Matrix3f().rotateY((float) Math.toRadians(getYaw()));
	}
	
	@Override
	public void fireEvent(AvoEvent e) {
		if(e instanceof MovementEvent) {
			move((MovementEvent) e);
		} else if(e instanceof RotationEvent) {
			rotate((RotationEvent) e);
		} else if(e instanceof SpeedChangeEvent) {
			speedChange((SpeedChangeEvent) e);
		}
	}

	@Override
	public void move(MovementEvent e) {
		velocity.add(e.getVelocity());
		// If the length is 0, the body should be stopped, no need to normalize (other wise it'll divide by 0 in the normalize)
		if(velocity.length() != 0) {
			velocity.normalize();
		}
		
		linearVelocity.set(velocity);
		switch(e.getMoveType()) {
		case MovementEvent.FIXED:
			break;
		case MovementEvent.RELATIVE:
			linearVelocity.mul(getInvertedYaw());
			break;
		}
		
		linearVelocity.mul(speed);
		// If notOnGround maybe?
		linearVelocity.add(0, rigidBody.getLinearVelocity(BTUtils.vector3f(0, 0, 0)).y, 0);
		
		rigidBody.setLinearVelocity(ConversionUtils.convertJomlToVecmath(linearVelocity));
		Entities.stateComponentMap.fireEventAt(entity, new StateChangeEvent(EntityState.WALK));
	}

	@Override
	public void rotate(RotationEvent e) {
		setYaw((getYaw() - e.getRotation().y) % 360);
	}

	@Override
	public void speedChange(SpeedChangeEvent e) {
		this.speed += (float) e.getValue();
	}

}
