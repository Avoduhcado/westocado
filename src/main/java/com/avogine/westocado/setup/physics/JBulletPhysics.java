package com.avogine.westocado.setup.physics;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.avogine.westocado.Theater;
import com.avogine.westocado.entities.bodies.JBulletBody;
import com.avogine.westocado.entities.bodies.utils.JBulletBodyParams;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

public class JBulletPhysics extends PhysicsController<JBulletBody, JBulletBodyParams> {

	private BroadphaseInterface broadphase;
	private DefaultCollisionConfiguration collisionConfiguration;
	private CollisionDispatcher dispatcher;
	private SequentialImpulseConstraintSolver solver;
	
	private DiscreteDynamicsWorld world;
	
	private final int maxSubSteps = 10;
		
	public JBulletPhysics() {
		broadphase = new DbvtBroadphase();
		
		collisionConfiguration = new DefaultCollisionConfiguration();
		dispatcher = new CollisionDispatcher(collisionConfiguration);
		
		solver = new SequentialImpulseConstraintSolver();
		
		world = new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);
		world.setGravity(new Vector3f(0, -10, 0));
		
		CollisionShape groundShape = new StaticPlaneShape(new Vector3f(0, 1, 0), 1);
		Transform groundTransform = new Transform();
		groundTransform.origin.set(0, -1, 0);
		groundTransform.setRotation(new Quat4f(0, 0, 0, 1));
		DefaultMotionState groundMotionState = new DefaultMotionState(groundTransform);
		RigidBodyConstructionInfo groundRigidBodyConstructionInfo = new RigidBodyConstructionInfo(0, groundMotionState, groundShape, new Vector3f(0, 0, 0));
		RigidBody groundRigidBody = new RigidBody(groundRigidBodyConstructionInfo);
		world.addRigidBody(groundRigidBody);
		
		/*CollisionShape fallShape = new SphereShape(1);
		Transform fallTransform = new Transform();
		fallTransform.origin.set(0, 50, 0);
		fallTransform.setRotation(new Quat4f(0, 0, 0, 1));
		DefaultMotionState fallMotionState = new DefaultMotionState(fallTransform);
		float mass = 1;
		Vector3f fallInertia = new Vector3f(0, 0, 0);
		fallShape.calculateLocalInertia(mass, fallInertia);
		RigidBodyConstructionInfo fallRigidBodyConstructionInfo = new RigidBodyConstructionInfo(mass, fallMotionState, fallShape, fallInertia);
		RigidBody fallRigidBody = new RigidBody(fallRigidBodyConstructionInfo);
		world.addRigidBody(fallRigidBody);*/
	}
	
	@Override
	public JBulletBody createBody(long entity, JBulletBodyParams params) {
		CollisionShape shape = params.getShape();
		
		Transform transform = new Transform();
		transform.origin.set(params.getPosition());
		transform.setRotation(params.getRotation());
		DefaultMotionState motionState = new DefaultMotionState(transform);
		
		float mass = params.getMass();
		Vector3f inertia = params.getInertia();
		shape.calculateLocalInertia(mass, inertia);
		RigidBodyConstructionInfo rigidBodyConstructionInfo = new RigidBodyConstructionInfo(mass, motionState, shape, inertia);
		RigidBody rigidBody = new RigidBody(rigidBodyConstructionInfo);
		
		world.addRigidBody(rigidBody);
		
		return new JBulletBody(entity, rigidBody);
	}

	@Override
	public void physicsStep() {
		world.stepSimulation((float) Theater.getDelta(), maxSubSteps);
	}

	@Override
	public void cleanUp() {
		world.destroy();
	}

}
