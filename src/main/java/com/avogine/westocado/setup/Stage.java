package com.avogine.westocado.setup;

import javax.vecmath.Quat4f;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.avogine.westocado.Theater;
import com.avogine.westocado.entities.Entities;
import com.avogine.westocado.entities.bodies.CameraBody;
import com.avogine.westocado.entities.bodies.JBulletBody;
import com.avogine.westocado.entities.bodies.PlainBody;
import com.avogine.westocado.entities.bodies.utils.JBulletBodyParams;
import com.avogine.westocado.entities.components.LightEmitter;
import com.avogine.westocado.entities.controllers.CameraController;
import com.avogine.westocado.entities.models.PlainModel;
import com.avogine.westocado.entities.utils.EntityContainer;
import com.avogine.westocado.io.Window;
import com.avogine.westocado.render.utils.FBO;
import com.avogine.westocado.render.utils.PostProcessor;
import com.avogine.westocado.setup.physics.JBulletPhysics;
import com.avogine.westocado.setup.physics.PhysicsController;
import com.avogine.westocado.setup.scene.ObjectRender;
import com.avogine.westocado.setup.scene.VertexRender;
import com.avogine.westocado.utils.math.BTUtils;
import com.bulletphysics.collision.shapes.SphereShape;

public class Stage implements EntityContainer {
	
	//CORNFLOUR BLUE
	private static final float RED = 101f / 255f;
	private static final float GREEN = 156f / 255f;
	private static final float BLUE = 239f / 255f;
	
	private static final Vector3f CORNFLOUR_BLUE = new Vector3f(101f / 255f, 156f / 255f, 239f / 255f);
	private static final Vector3f BLACK = new Vector3f(0, 0, 0);
	private static final Vector3f WHITE = new Vector3f(1f, 1f, 1f);
	
	/*public static final float RED = 0.5444f;
	public static final float GREEN = 0.62f;
	public static final float BLUE = 0.69f;*/
	
	private Window window;
	
	private FBO entityFbo;
	private FBO outputFbo;
	
	private ObjectRender render;
	private VertexRender vRender;
	
	private PhysicsController<JBulletBody, JBulletBodyParams> physics = new JBulletPhysics();
	
	public Stage(Window window) {
		this.window = window;
		physics = new JBulletPhysics();
		
		entityFbo = new FBO(1280, 720, window);
		outputFbo = new FBO(1280, 720, FBO.DEPTH_TEXTURE, window);
		PostProcessor.init(window);
		
		long cameraEntity = Entities.reserveNewEntity();
		new CameraController(cameraEntity, window);
		new LightEmitter(cameraEntity, new Vector3f(0.9f, 0.3f, 0.1f));
		CameraBody mainCamera = new CameraBody(cameraEntity);

		// TODO Could probably wrap these up inside some sort of full renderer to cut down on code duplication
		this.render = new ObjectRender(mainCamera);
		this.vRender = new VertexRender(mainCamera);
		
		// This is all garbo entity creation and should DEFO be hid behind a level loader
		long entity = Entities.reserveNewEntity();
		JBulletBodyParams bodyParams = new JBulletBodyParams(new SphereShape(1), BTUtils.vector3f(0, 50, 0), new Quat4f(0, 0, 0, 1));
		physics.createBody(entity, bodyParams);
		new PlainModel(entity, "robutt7.obj", "robuttUVflat");
		
		entity = Entities.reserveNewEntity();
		new PlainModel(entity, "cairn.obj", "grass");
		PlainBody body = new PlainBody(entity);
		body.setPosition(new Vector3f(10, 0, 10));
		
		entity = Entities.reserveNewEntity();
		new PlainModel(entity, "bigTree2.obj", "Dyed_Grey_Sycamore");
		body = new PlainBody(entity);
		body.setPosition(new Vector3f(10, 0, -500));
		body.setScale(new Vector3f(100f));
		
		entity = Entities.reserveNewEntity();
		body = new PlainBody(entity);
		body.setPosition(new Vector3f(0, 25, 25));
		new LightEmitter(entity, new Vector3f(0.1f, 0.8f, 0f));
	}

	/**
	 * Renders the scene to the screen.
	 */
	public void render() {
		prepare();
		entityFbo.bindFrameBuffer();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		if(Theater.wireFrame) {
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		}
		render.renderScene();
		vRender.renderScene();
		if(Theater.wireFrame) {
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		}
		entityFbo.unbindFrameBuffer();
		entityFbo.resolveToFbo(GL30.GL_COLOR_ATTACHMENT0, outputFbo);
		entityFbo.resolveToFbo(GL30.GL_DEPTH_ATTACHMENT, outputFbo);

		PostProcessor.doPostProcessing(outputFbo.getColorTexture(), outputFbo.getDepthTexture());
	}
	
	public void simulateWorld() {
		physics.physicsStep();
	}
	
	/**
	 * Clean up when the game is closed.
	 */
	public void cleanUp() {
		render.cleanUp();
		vRender.cleanUp();
		entityFbo.cleanUp();
		outputFbo.cleanUp();
		PostProcessor.cleanUp();
		physics.cleanUp();
	}

	/**
	 * Prepare to render the current frame by clearing the framebuffer.
	 */
	private void prepare() {
		GL11.glClearColor(WHITE.x, WHITE.y, WHITE.z, 1);
		//GL11.glClearColor(CORNFLOUR_BLUE.x, CORNFLOUR_BLUE.y, CORNFLOUR_BLUE.z, 1);
		//GL11.glClearColor(BLACK.x, BLACK.y, BLACK.z, 1);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	public Window getWindow() {
		return window;
	}
	
	@Override
	public PhysicsController<JBulletBody, JBulletBodyParams> getPhysics() {
		return physics;
	}
	
}
