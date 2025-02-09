package com.glisco.isometricrenders.mixin;

import com.glisco.isometricrenders.client.gui.BatchIsometricBlockRenderScreen;
import com.glisco.isometricrenders.client.gui.BatchIsometricItemRenderScreen;
import com.glisco.isometricrenders.client.gui.IsometricRenderHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Collectors;

@Mixin(HandledScreen.class)
public class HandledScreenMixin<T extends ScreenHandler> extends Screen {

    @Shadow
    @Final
    protected T handler;

    protected HandledScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    public void renderInventory(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (keyCode == GLFW.GLFW_KEY_F12) {
            if (Screen.hasControlDown()) {
                client.openScreen(new BatchIsometricBlockRenderScreen(IsometricRenderHelper.extractBlocks(handler.slots.stream().map(Slot::getStack).collect(Collectors.toList())), "inventory"));
                cir.cancel();
            } else if (Screen.hasAltDown()) {
                client.openScreen(new BatchIsometricItemRenderScreen(handler.slots.stream().map(Slot::getStack).iterator(), "inventory"));
                cir.cancel();
            } else if (Screen.hasShiftDown()) {
                IsometricRenderHelper.renderItemAtlas("inventory", handler.slots.stream().map(Slot::getStack).filter(itemStack -> !itemStack.isEmpty()).collect(Collectors.toList()), true);
                cir.cancel();
            }
        }
    }

}
