package com.avogine.westocado.setup;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Quat4f;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.avogine.westocado.entities.Camera;
import com.avogine.westocado.entities.Entity;
import com.avogine.westocado.entities.Light;
import com.avogine.westocado.entities.bodies.JBulletBody;
import com.avogine.westocado.entities.bodies.utils.JBulletBodyParams;
import com.avogine.westocado.entities.utils.EntityContainer;
import com.avogine.westocado.io.Window;
import com.avogine.westocado.render.shaders.SimpleShader;
import com.avogine.westocado.render.utils.FBO;
import com.avogine.westocado.render.utils.PostProcessor;
import com.avogine.westocado.setup.physics.JBulletPhysics;
import com.avogine.westocado.setup.physics.PhysicsController;
import com.avogine.westocado.setup.scene.ModelRender;
import com.avogine.westocado.setup.scene.ObjectRender;
import com.avogine.westocado.setup.scene.SkyboxRender;
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
	
	private Matrix4f projectionMatrix;
	
	private ModelRender modelRender;
	private SkyboxRender skyboxRender;
	
	private SimpleShader simpleShader;
	
	private FBO entityFbo;
	private FBO outputFbo;
	
	private ObjectRender render;
	private VertexRender vRender;
	private Camera camera;
	private Light light;
	
	private PhysicsController<JBulletBody, JBulletBodyParams> physics = new JBulletPhysics();
	
	private List<Entity> entities = new ArrayList<>();
	
	public Stage(Window window) {
		physics = new JBulletPhysics();
		
		entityFbo = new FBO(1280, 720, window);
		outputFbo = new FBO(1280, 720, FBO.DEPTH_TEXTURE, window);
		PostProcessor.init(window);
		
		this.camera = new Camera(this);
		window.getInput().addInputListener(camera);
		this.light = new Light(new Vector3f(0, 25, 25), new Vector3f(1, 1, 1));
		this.render = new ObjectRender();
		this.vRender = new VertexRender();
		
		entities.add(new Entity(this));
		entities.add(new Entity(this, "robutt7.obj", "robuttUVflat"));
		JBulletBodyParams bodyParams = new JBulletBodyParams(new SphereShape(1), BTUtils.vector3f(0, 50, 0), new Quat4f(0, 0, 0, 1));
		entities.get(entities.size() - 1).setBody(physics.createBody(entities.get(entities.size() - 1), bodyParams));
		entities.add(new Entity(this, "cairn.obj", "grass"));
		entities.get(entities.size() - 1).setPosition(new Vector3f(10, 0, 10));
		entities.add(new Entity(this, "bigTree2.obj", "Dyed_Grey_Sycamore"));
		entities.get(entities.size() - 1).setScale(new Vector3f(100f));
		entities.get(entities.size() - 1).setPosition(new Vector3f(10, 0, -500));
	}

	/**
	 * Renders the scene to the screen.
	 * @param scene
	 */
	public void render() {
		prepare();
		camera.move();
		light.setPosition(camera.getPosition());
		//light.move();
		entityFbo.bindFrameBuffer();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		//GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		entities.stream().filter(e -> e.getModel() != null).forEach(e -> render.render(e, camera, light));
		entities.stream().filter(e -> e.getBody() != null).forEach(e -> vRender.render(e, camera));
		//GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
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
	
	@Override
	public PhysicsController<JBulletBody, JBulletBodyParams> getPhysics() {
		return physics;
	}
	
	@Override
	public void addEntity(Entity entity) {
		this.entities.add(entity);
	}
	
}
