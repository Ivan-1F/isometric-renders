package com.glisco.isometricrenders.client;

import com.glisco.isometricrenders.client.gui.AreaIsometricRenderScreen;
import com.glisco.isometricrenders.client.gui.IsometricRenderHelper;
import com.glisco.isometricrenders.client.gui.IsometricRenderPresets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

import static com.glisco.isometricrenders.Translator.tr;

public class AreaSelectionHelper {

    public static BlockPos pos1 = null;
    public static BlockPos pos2 = null;

    public static boolean shouldDraw() {
        return pos1 != null;
    }

    public static void clear() {
        AreaSelectionHelper.pos1 = null;
        AreaSelectionHelper.pos2 = null;
        MinecraftClient.getInstance().player.sendMessage(tr("message.isometric-renders.selections_cleared"), true);
    }

    public static void renderSelectionBox(MatrixStack matrices, Camera camera) {
        if (!AreaSelectionHelper.shouldDraw()) return;

        var client = MinecraftClient.getInstance();
        var player = client.player;

        BlockPos origin = AreaSelectionHelper.pos1;

        HitResult result = player.raycast(player.getAbilities().creativeMode ? 5.0F : 4.5F, 0, false);
        BlockPos size = AreaSelectionHelper.pos2 != null ? AreaSelectionHelper.pos2 : (result.getType() == HitResult.Type.BLOCK ? ((BlockHitResult) result).getBlockPos() : new BlockPos(result.getPos()));
        size = size.subtract(origin);

        origin = origin.add(size.getX() < 0 ? 1 : 0, size.getY() < 0 ? 1 : 0, size.getZ() < 0 ? 1 : 0);
        size = size.add(size.getX() >= 0 ? 1 : -1, size.getY() >= 0 ? 1 : -1, size.getZ() >= 0 ? 1 : -1);

        matrices.push();

        VertexConsumer consumer = client.getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getLines());
        matrices.translate(origin.getX() - camera.getPos().x, origin.getY() - camera.getPos().y, origin.getZ() - camera.getPos().z);

        WorldRenderer.drawBox(matrices, consumer, 0, 0, 0, size.getX(), size.getY(), size.getZ(), 1, 1, 1, 1, 0, 0, 0);

        matrices.pop();
    }

    public static void select() {
        final var client = MinecraftClient.getInstance();
        final var target = client.crosshairTarget;
        if ((target == null)) return;
        var targetPos = new BlockPos(target.getType() == HitResult.Type.BLOCK ? ((BlockHitResult) target).getBlockPos() : new BlockPos(target.getPos()));

        if (pos1 == null) {
            pos1 = targetPos;
            client.player.sendMessage(tr("message.isometric-renders.selections_started"), true);
        } else {
            client.player.sendMessage(tr("message.isometric-renders.selections_finished"), true);
            pos2 = targetPos;
        }
    }

    public static boolean tryOpenScreen() {
        if (pos1 == null || pos2 == null) return false;

        var screen = new AreaIsometricRenderScreen(false);
        IsometricRenderPresets.setupAreaRender(screen, pos1, pos2, false);
        IsometricRenderHelper.scheduleScreen(screen);
        return true;
    }
}
