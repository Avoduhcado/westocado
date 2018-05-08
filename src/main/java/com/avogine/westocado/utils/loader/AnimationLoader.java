package com.avogine.westocado.utils.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIAnimation;
import org.lwjgl.assimp.AINode;
import org.lwjgl.assimp.AINodeAnim;
import org.lwjgl.assimp.AIQuatKey;
import org.lwjgl.assimp.AIQuaternion;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.AIVectorKey;

import com.avogine.westocado.render.animation.AnimatedFrame;
import com.avogine.westocado.render.animation.Animation;
import com.avogine.westocado.render.animation.Bone;
import com.avogine.westocado.render.animation.Node;
import com.avogine.westocado.utils.AssimpUtils;

public class AnimationLoader {

	public static Map<String, Animation> loadAnimations(AIScene aiScene, List<Bone> boneList) {
		AINode aiRootNode = aiScene.mRootNode();
		Matrix4f rootTransfromation = AssimpUtils.toMatrix(aiRootNode.mTransformation());
		Node rootNode = processNodesHierarchy(aiRootNode, null);
		Map<String, Animation> animations = processAnimations(aiScene, boneList, rootNode, rootTransfromation);
		
		return animations;
	}

	private static Map<String, Animation> processAnimations(AIScene aiScene, List<Bone> boneList, Node rootNode, Matrix4f rootTransformation) {
		Map<String, Animation> animations = new HashMap<>();

		// Process all animations
		int numAnimations = aiScene.mNumAnimations();
		PointerBuffer aiAnimations = aiScene.mAnimations();
		for (int i = 0; i < numAnimations; i++) {
			AIAnimation aiAnimation = AIAnimation.create(aiAnimations.get(i));

			// Calculate transformation matrices for each node
			int numChanels = aiAnimation.mNumChannels();
			PointerBuffer aiChannels = aiAnimation.mChannels();
			for (int j = 0; j < numChanels; j++) {
				AINodeAnim aiNodeAnim = AINodeAnim.create(aiChannels.get(j));
				String nodeName = aiNodeAnim.mNodeName().dataString();
				Node node = rootNode.findByName(nodeName);
				buildTransformationMatrices(aiNodeAnim, node);
			}
			
			List<AnimatedFrame> frames = buildAnimationFrames(boneList, rootNode, rootTransformation);
			Animation animation = new Animation(aiAnimation.mName().dataString(), frames, aiAnimation.mDuration());
			animations.put(animation.getName(), animation);
		}
		return animations;
	}
	
	private static Node processNodesHierarchy(AINode aiNode, Node parentNode) {
		String nodeName = aiNode.mName().dataString();
		Node node = new Node(nodeName, parentNode);

		int numChildren = aiNode.mNumChildren();
		PointerBuffer aiChildren = aiNode.mChildren();
		for (int i = 0; i < numChildren; i++) {
			AINode aiChildNode = AINode.create(aiChildren.get(i));
			Node childNode = processNodesHierarchy(aiChildNode, node);
			node.addChild(childNode);
		}

		return node;
	}

	private static void buildTransformationMatrices(AINodeAnim aiNodeAnim, Node node) {
		int numFrames = aiNodeAnim.mNumPositionKeys();
		AIVectorKey.Buffer positionKeys = aiNodeAnim.mPositionKeys();
		AIVectorKey.Buffer scalingKeys = aiNodeAnim.mScalingKeys();
		AIQuatKey.Buffer rotationKeys = aiNodeAnim.mRotationKeys();

		for (int i = 0; i < numFrames; i++) {
			AIVectorKey aiVecKey = positionKeys.get(i);
			AIVector3D vec = aiVecKey.mValue();

			Matrix4f transfMat = new Matrix4f().translate(vec.x(), vec.y(), vec.z());

			AIQuatKey quatKey = rotationKeys.get(i);
			AIQuaternion aiQuat = quatKey.mValue();
			Quaternionf quat = new Quaternionf(aiQuat.x(), aiQuat.y(), aiQuat.z(), aiQuat.w());
			transfMat.rotate(quat);

			if (i < aiNodeAnim.mNumScalingKeys()) {
				aiVecKey = scalingKeys.get(i);
				vec = aiVecKey.mValue();
				transfMat.scale(vec.x(), vec.y(), vec.z());
			}

			node.addTransformation(transfMat);
			
			int animBehavior = aiNodeAnim.mPreState();
			
			node.addAnimBehavior(animBehavior);
		}
	}

	private static List<AnimatedFrame> buildAnimationFrames(List<Bone> boneList, Node rootNode, Matrix4f rootTransformation) {
		int numFrames = rootNode.getAnimationFrames();
		List<AnimatedFrame> frameList = new ArrayList<>();
		for (int i = 0; i < numFrames; i++) {
			AnimatedFrame frame = new AnimatedFrame();
			frameList.add(frame);

			int numBones = boneList.size();
			for (int j = 0; j < numBones; j++) {
				Bone bone = boneList.get(j);
				Node node = rootNode.findByName(bone.getBoneName());
				Matrix4f boneMatrix = Node.getParentTransforms(node, i);
				boneMatrix.mul(bone.getOffsetMatrix());
				boneMatrix = new Matrix4f(rootTransformation).mul(boneMatrix);
				frame.setMatrix(j, boneMatrix);
			}
		}

		return frameList;
	}

}
