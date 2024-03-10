package cn.ksmcbrigade.BT.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Minecraft.class)
public interface MinecraftMixin {
    @Accessor("rightClickDelay")
    int getRightClickDelay();

    @Accessor("rightClickDelay")
    void setRightClickDelay(int delay);

    @Accessor("missTime")
    int getMissTime();

    @Accessor("missTime")
    void setMissTime(int time);

    @Invoker("startUseItem")
    void startUseItem();
}
