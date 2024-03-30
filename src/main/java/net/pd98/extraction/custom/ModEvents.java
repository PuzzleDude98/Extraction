package net.pd98.extraction.custom;

import net.minecraft.client.MinecraftClient;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
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
    private static Vec3d target;
    private static BlockPos blockPos;
    private static final double CAMERA_MOVE_SPEED = 0.1;
    private static byte cooldown = 0;

    static Entity cam;

    public static ActionResult MoveCamera(PlayerEntity player, World world, Hand hand, HitResult hitResult) {
        if (hand == Hand.MAIN_HAND && !cameraMoving) {
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                Extraction.LOGGER.info("Click!");
                cooldown = 10;
                blockPos = ((BlockHitResult) hitResult).getBlockPos();
                Direction blockFace = ((BlockHitResult) hitResult).getSide();

                if (isCameraMoved && cooldown == 0) {
                    // If camera is already moved and player right-clicked the same block face again,
                    // move the camera back to the original position
//                    moveCameraToOriginalPosition();
                    isCameraMoved = false;
                    MinecraftClient.getInstance().setCameraEntity(MinecraftClient.getInstance().player);
                    cam.kill();
                } else {
                    // Otherwise, move camera to the clicked block face
                    cam = EntityType.MARKER.create(MinecraftClient.getInstance().world);
//                    cam.setPosition(player.getEyePos());
//                    cam.setYaw(player.getYaw());
//                    cam.setPitch(player.getPitch());
                    world.spawnEntity(cam);
                    cam.updatePosition(player.getEyePos().getX(), player.getEyePos().getY(), player.getEyePos().getZ());
                    cam.setYaw(player.getYaw());
                    cam.setPitch(player.getPitch());
                    MinecraftClient.getInstance().setCameraEntity(cam);
//                    target = new Vec3d((blockPos.getX() + 0.5 + (blockFace.getOffsetX() * 1.5)), (blockPos.getY() + 0.5 + (blockFace.getOffsetY() * 1.5)),(blockPos.getZ()) + 0.5 + (blockFace.getOffsetZ() * 1.5));
//                    cameraMoving = true;
                    isCameraMoved = true;
                    MinecraftClient.getInstance().options.hudHidden = true;
                }
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    public static void tick() {
        if (cooldown > 0) {
            cooldown--;
            Extraction.LOGGER.info(String.valueOf(cooldown));
        }
        if (cameraMoving) {
//            Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
            Entity cameraEntity = MinecraftClient.getInstance().getCameraEntity();

            if (cameraEntity != null) {

                // Calculate the distance between current camera position and target position
                double distanceX = target.x - cameraEntity.getPos().x;
                double distanceY = target.y - cameraEntity.getPos().y;
                double distanceZ = target.z - cameraEntity.getPos().z;

                Extraction.LOGGER.info(String.valueOf(distanceX <= 1));

                if (distanceX <= 0.001 && distanceY <= 0.001 && distanceZ <= 0.001) {
                    Extraction.LOGGER.info("stopping!");
                    cameraMoving = false;
                    MinecraftClient.getInstance().setCameraEntity(MinecraftClient.getInstance().player);
                    cam.kill();
                    MinecraftClient.getInstance().options.hudHidden = false;
                    return;
                }

                // Calculate the incremental step for each tick
                double stepX = distanceX * CAMERA_MOVE_SPEED;
                double stepY = distanceY * CAMERA_MOVE_SPEED;
                double stepZ = distanceZ * CAMERA_MOVE_SPEED;

                // Update camera position each tick until it reaches the target position

                cam.updatePosition(cameraEntity.getX() + stepX, cameraEntity.getY() + stepY, cameraEntity.getZ() + stepZ);
                cam.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, blockPos.toCenterPos());
    //            assert MinecraftClient.getInstance().world != null;
    //            Entity cam = getNearestEntity(MinecraftClient.getInstance().world, new BlockPos(cameraEntity.getBlockPos()));
    //            cam.addVelocity(0,1,0);
    //            MinecraftClient.getInstance().setCameraEntity(cam);
            }
        }
    }

    public static void kick() {
        Extraction.LOGGER.info("Kicking");
        MinecraftClient.getInstance().setCameraEntity(MinecraftClient.getInstance().player);
        cam.kill();
        MinecraftClient.getInstance().options.hudHidden = false;
    }

    public static void debug() {
        kick();
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
