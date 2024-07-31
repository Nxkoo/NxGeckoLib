package com.github.nxkoo.nxgeckolib.core.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.*;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;

public class NxRenderUtils {

    public static void translateRotateGeckolib(GeoBone bone, MatrixStack matrixStackIn) {
        matrixStackIn.translate((double) (bone.rotationPointX / 16.0F), (double) (bone.rotationPointY / 16.0F), (double) (bone.rotationPointZ / 16.0F));
        if (bone.getRotationZ() != 0.0F) {
            rotate(matrixStackIn, Vector3f.ZP.rotation(bone.getRotationZ()));
        }

        if (bone.getRotationY() != 0.0F) {
            rotate(matrixStackIn, Vector3f.YP.rotation(bone.getRotationY()));
        }

        if (bone.getRotationX() != 0.0F) {
            rotate(matrixStackIn, Vector3f.XP.rotation(bone.getRotationX()));
        }

        matrixStackIn.scale(bone.getScaleX(), bone.getScaleY(), bone.getScaleZ());
    }

    public static void matrixStackFromModel(MatrixStack matrixStack, GeoBone geoBone) {
        GeoBone parent = geoBone.parent;
        if (parent != null) matrixStackFromModel(matrixStack, parent);
        translateRotateGeckolib(geoBone, matrixStack);
    }

    public static Vector3d getWorldPosFromModel(Entity entity, float entityYaw, GeoBone geoBone) {
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.translate(entity.getX(), entity.getY(), entity.getZ());
        rotate(matrixStack, new Quaternion(0, -entityYaw + 180, 0, true));
        matrixStack.scale(-1, -1, 1);
        matrixStack.translate(0, -1.5f, 0);
        NxRenderUtils.matrixStackFromModel(matrixStack, geoBone);
        MatrixStack.Entry matrixEntry = matrixStack.last();
        Matrix4f matrix4f = matrixEntry.pose();

        Vector4f vec = new Vector4f(0, 0, 0, 1);
        vec.transform(matrix4f);
        return new Vector3d(vec.x(), vec.y(), vec.z());
    }

    // Mirrored render utils
    public static void moveToPivotMirror(GeoCube cube, MatrixStack stack) {
        Vector3f pivot = cube.pivot;
        stack.translate((double) (-pivot.x() / 16.0F), (double) (pivot.y() / 16.0F), (double) (pivot.z() / 16.0F));
    }

    public static void moveBackFromPivotMirror(GeoCube cube, MatrixStack stack) {
        Vector3f pivot = cube.pivot;
        stack.translate((double) (pivot.x() / 16.0F), (double) (-pivot.y() / 16.0F), (double) (-pivot.z() / 16.0F));
    }

    public static void moveToPivotMirror(GeoBone bone, MatrixStack stack) {
        stack.translate((double) (-bone.rotationPointX / 16.0F), (double) (bone.rotationPointY / 16.0F), (double) (bone.rotationPointZ / 16.0F));
    }

    public static void moveBackFromPivotMirror(GeoBone bone, MatrixStack stack) {
        stack.translate((double) (bone.rotationPointX / 16.0F), (double) (-bone.rotationPointY / 16.0F), (double) (-bone.rotationPointZ / 16.0F));
    }

    public static void translateMirror(GeoBone bone, MatrixStack stack) {
        stack.translate((double) (bone.getPositionX() / 16.0F), (double) (bone.getPositionY() / 16.0F), (double) (bone.getPositionZ() / 16.0F));
    }

    public static void rotateMirror(GeoBone bone, MatrixStack stack) {
        if (bone.getRotationZ() != 0.0F) {
            rotate(stack, Vector3f.ZP.rotation(-bone.getRotationZ()));
        }

        if (bone.getRotationY() != 0.0F) {
            rotate(stack, Vector3f.YP.rotation(-bone.getRotationY()));
        }

        if (bone.getRotationX() != 0.0F) {
            rotate(stack, Vector3f.XP.rotation(bone.getRotationX()));
        }

    }

    public static void rotateMirror(GeoCube bone, MatrixStack stack) {
        Vector3f rotation = bone.rotation;
        rotate(stack, new Quaternion(0.0F, 0.0F, -rotation.z(), false));
        rotate(stack, new Quaternion(0.0F, -rotation.y(), 0.0F, false));
        rotate(stack, new Quaternion(rotation.x(), 0.0F, 0.0F, false));
    }

    public static void rotate(MatrixStack stack, Quaternion quaternion) {
        MatrixStack.Entry matrixstack$entry = stack.last();
        matrixstack$entry.pose().multiply(quaternion);
        matrixstack$entry.normal().mul(quaternion);
    }
}
