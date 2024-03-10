package cn.ksmcbrigade.BT.mixin;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MultiPlayerGameMode.class)
public interface GameModeMixin {
    @Accessor("destroyDelay")
    int getDestroyDelay();

    @Accessor("destroyDelay")
    void setDestroyDelay(int delay);
}
