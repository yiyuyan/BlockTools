package cn.ksmcbrigade.BT;

import cn.ksmcbrigade.BT.mixin.GameModeMixin;
import cn.ksmcbrigade.BT.mixin.MinecraftMixin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ForgeMod;

import java.util.Objects;

public class Manager {
    public static void runFastBreak(Player player){
        if (Minecraft.getInstance().gameMode != null) {
            ((GameModeMixin)Minecraft.getInstance().gameMode).setDestroyDelay(0);
        }
    }

    public static void runFastPlace(Player player){
        ((MinecraftMixin)Minecraft.getInstance()).setRightClickDelay(0);
    }

    public static void runAirPlace(Player player){
        //Use Event
    }

    public static void runScaffoldWalk(Player player){
        Minecraft MC = Minecraft.getInstance();
        Level level = player.getLevel();
        BlockPos pos = player.getOnPos();
        if(level.getBlockState(pos).isAir() && level.getBlockState(pos.below()).isAir() && level.getBlockState(pos.below().below()).isAir()){
            int newSlot = -1;
            for(int i = 0; i < 9; i++)
            {
                ItemStack stack = player.getInventory().getItem(i);
                if(stack.isEmpty() || !(stack.getItem() instanceof BlockItem))
                    continue;
                Block block = Block.byItem(stack.getItem());
                if(block instanceof FallingBlock)
                    continue;
                newSlot = i;
                break;
            }
            if(newSlot == -1)
                return;
            int oldSlot = player.getInventory().selected;
            player.getInventory().selected = newSlot;
            if (MC.gameMode != null) {
                LocalPlayer localPlayer = Minecraft.getInstance().player;
                if (Minecraft.getInstance().hitResult != null) {
                    if (localPlayer != null && level.getBlockState(pos).isAir()) {
                        try {
                            //Objects.requireNonNull(MC.getConnection()).getConnection().send(new ServerboundMovePlayerPacket.Rot(localPlayer.getViewXRot(0),165,localPlayer.isOnGround()));
                            if (MC.level != null) {
                                Objects.requireNonNull(MC.getConnection()).getConnection().send(new ServerboundUseItemOnPacket(player.getUsedItemHand(), (BlockHitResult) Minecraft.getInstance().hitResult));
                                MC.getConnection().getConnection().send(new ServerboundSwingPacket(player.getUsedItemHand()));
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
            player.getInventory().selected = oldSlot;
        }
    }

    public static void runReach(Player player){
        try {
            Objects.requireNonNull(player.getAttribute(ForgeMod.REACH_DISTANCE.get())).setBaseValue(BlockTools.reach);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
