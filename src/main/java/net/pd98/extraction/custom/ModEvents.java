package net.pd98.extraction.custom;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.pd98.extraction.Extraction;

public class ModEvents {

    private static boolean isCameraMoved = false;
    private static boolean cameraMoving = false;
    static Vec3d target;
    private static final double CAMERA_MOVE_SPEED = 0.1;

    static Entity cam;

    public static ActionResult MoveCamera(PlayerEntity player, World world, Hand hand, HitResult hitResult) {
        if (hand == Hand.MAIN_HAND) {
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                Extraction.LOGGER.info("Click!");
                BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
                Direction blockFace = ((BlockHitResult) hitResult).getSide();

                if (isCameraMoved) {
                    // If camera is already moved and player right-clicked the same block face again,
                    // move the camera back to the original position
//                    moveCameraToOriginalPosition();
                    isCameraMoved = false;
                } else {
                    // Otherwise, move camera to the clicked block face
                    cam = EntityType.ARMOR_STAND.create(MinecraftClient.getInstance().world);
                    cam.setPosition(player.getX(), player.getY(), player.getZ());
                    world.spawnEntity(cam);
                    MinecraftClient.getInstance().setCameraEntity(cam);
                    target = new Vec3d((blockPos.getX() + 0.5 + (blockFace.getOffsetX() * 0.5)), (blockPos.getY() + 0.5 + (blockFace.getOffsetY() * 0.5)),(blockPos.getZ()) + 0.5 + (blockFace.getOffsetZ() * 0.5));
                    cameraMoving = true;
                    isCameraMoved = true;
                }
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    public static void moveCamera() {
        if (cameraMoving) {
//            Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
            Entity cameraEntity = MinecraftClient.getInstance().getCameraEntity();

            if (cameraEntity != null) {

                // Calculate the distance between current camera position and target position
                double distanceX = target.x - cameraEntity.getPos().x;
                double distanceY = target.y - cameraEntity.getPos().y;
                double distanceZ = target.z - cameraEntity.getPos().z;

                Extraction.LOGGER.info(String.valueOf(distanceX <= 1));

                if (distanceX <= 1 && distanceY <= 1 && distanceZ <= 1) {
                    Extraction.LOGGER.info("stopping!");
                    cameraMoving = false;
                    cam.kill();
                    MinecraftClient.getInstance().setCameraEntity(MinecraftClient.getInstance().player);
                    return;
                }

                // Calculate the incremental step for each tick
                double stepX = distanceX * CAMERA_MOVE_SPEED;
                double stepY = distanceY * CAMERA_MOVE_SPEED;
                double stepZ = distanceZ * CAMERA_MOVE_SPEED;

                // Update camera position each tick until it reaches the target position

                MinecraftClient.getInstance().getCameraEntity().updatePosition(cameraEntity.getX() + stepX, cameraEntity.getY() + stepY, cameraEntity.getZ() + stepZ);
                cam.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, target);
    //            assert MinecraftClient.getInstance().world != null;
    //            Entity cam = getNearestEntity(MinecraftClient.getInstance().world, new BlockPos(cameraEntity.getBlockPos()));
    //            cam.addVelocity(0,1,0);
    //            MinecraftClient.getInstance().setCameraEntity(cam);
            }
        }
    }
    public static Entity getNearestEntity(World world, BlockPos pos) {
        double minDistance = Double.MAX_VALUE;
        Entity nearestEntity = null;

        for (Entity entity : world.getOtherEntities(null, new Box(pos.getX()-5, pos.getY()-5, pos.getZ()-5, pos.getX()+5, pos.getY()+5, pos.getZ()+5))) {
            double distance = entity.getPos().distanceTo(Vec3d.of(pos));
            if (distance < minDistance && !entity.isPlayer()) {
                minDistance = distance;
                nearestEntity = entity;
            }
        }

        return nearestEntity;
    }
}
