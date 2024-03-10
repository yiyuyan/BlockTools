package cn.ksmcbrigade.BT;

import cn.ksmcbrigade.BT.mixin.MinecraftMixin;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.Commands;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Mod("bt")
@Mod.EventBusSubscriber
public class BlockTools {

    public static int reach = 10;
    public static final Path path = Paths.get("config/bt-config.json");

    public BlockTools() throws IOException {
        MinecraftForge.EVENT_BUS.register(this);
        init();
    }

    public static void init() throws IOException {
        if(!new File("config/vm/mods").exists()){
            new File("config/vm/mods").mkdirs();
        }
        if(!new File("config/vm/mods/FastBreak.json").exists()){
            JsonObject object = new JsonObject();
            object.addProperty("name","FastBreak");
            object.addProperty("id","bt");
            object.addProperty("main","cn.ksmcbrigade.BT.Manager");
            object.addProperty("function","NONE");
            object.addProperty("function_2","runFastBreak");
            object.addProperty("gui_main","NONE");
            object.addProperty("gui_function","NONE");
            Files.write(Paths.get("config/vm/mods/FastBreak.json"),object.toString().getBytes());
        }
        if(!new File("config/vm/mods/FastPlace.json").exists()){
            JsonObject object = new JsonObject();
            object.addProperty("name","FastPlace");
            object.addProperty("id","bt");
            object.addProperty("main","cn.ksmcbrigade.BT.Manager");
            object.addProperty("function","NONE");
            object.addProperty("function_2","runFastPlace");
            object.addProperty("gui_main","NONE");
            object.addProperty("gui_function","NONE");
            Files.write(Paths.get("config/vm/mods/FastPlace.json"),object.toString().getBytes());
        }
        if(!new File("config/vm/mods/AirPlace.json").exists()){
            JsonObject object = new JsonObject();
            object.addProperty("name","AirPlace");
            object.addProperty("id","bt");
            object.addProperty("main","cn.ksmcbrigade.BT.Manager");
            object.addProperty("function","NONE");
            object.addProperty("function_2","runAirPlace");
            object.addProperty("gui_main","NONE");
            object.addProperty("gui_function","NONE");
            Files.write(Paths.get("config/vm/mods/AirPlace.json"),object.toString().getBytes());
        }
        if(!new File("config/vm/mods/ScaffoldWalk.json").exists()){
            JsonObject object = new JsonObject();
            object.addProperty("name","ScaffoldWalk");
            object.addProperty("id","bt");
            object.addProperty("main","cn.ksmcbrigade.BT.Manager");
            object.addProperty("function","NONE");
            object.addProperty("function_2","runScaffoldWalk");
            object.addProperty("gui_main","NONE");
            object.addProperty("gui_function","NONE");
            Files.write(Paths.get("config/vm/mods/ScaffoldWalk.json"),object.toString().getBytes());
        }
        if(!new File("config/vm/mods/Reach.json").exists()){
            JsonObject object = new JsonObject();
            object.addProperty("name","Reach");
            object.addProperty("id","bt");
            object.addProperty("main","cn.ksmcbrigade.BT.Manager");
            object.addProperty("function","NONE");
            object.addProperty("function_2","runReach");
            object.addProperty("gui_main","NONE");
            object.addProperty("gui_function","NONE");
            Files.write(Paths.get("config/vm/mods/Reach.json"),object.toString().getBytes());
        }

        if(!new File("config/bt-config.json").exists()){
            JsonObject object = new JsonObject();
            object.addProperty("reach",10);
            Files.write(path,object.toString().getBytes());
        }
        reach = JsonParser.parseString(Files.readString(path)).getAsJsonObject().get("reach").getAsInt();
    }

    @SubscribeEvent
    public static void onInputLeft(PlayerInteractEvent.LeftClickBlock event) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            if(is("FastBreak")) {
                Minecraft MC =Minecraft.getInstance();
                Player player = MC.player;
                if (Minecraft.getInstance().gameMode != null) {
                    if (player != null) {
                        if (Minecraft.getInstance().hitResult != null) {
                            Objects.requireNonNull(MC.getConnection()).getConnection().send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK,event.getPos(),player.getDirection()));
                        }
                    }
                }
            }
    }

    @SubscribeEvent
    public static void onInputRight(PlayerInteractEvent.RightClickEmpty event) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if(Minecraft.getInstance().options.keyUse.isDown()){
            if(is("AirPlace")) {
                Minecraft MC =Minecraft.getInstance();
                Player player = MC.player;
                if (Minecraft.getInstance().gameMode != null) {
                    if (player != null) {
                        if (Minecraft.getInstance().hitResult != null) {
                            Objects.requireNonNull(MC.getConnection()).getConnection().send(new ServerboundUseItemOnPacket(player.getUsedItemHand(), (BlockHitResult) Minecraft.getInstance().hitResult));
                            MC.getConnection().getConnection().send(new ServerboundSwingPacket(player.getUsedItemHand()));
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void tick(TickEvent.PlayerTickEvent event) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if(!is("Reach")){
            try {
                Objects.requireNonNull(event.player.getAttribute(ForgeMod.REACH_DISTANCE.get())).setBaseValue(5);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public static void command(RegisterClientCommandsEvent event){
        event.getDispatcher().register(Commands.literal("reach").then(Commands.argument("value", IntegerArgumentType.integer(0,1024)).executes(context -> {
            reach=IntegerArgumentType.getInteger(context,"value");
            JsonObject object = new JsonObject();
            object.addProperty("reach",10);
            try {
                Files.write(path,object.toString().getBytes());
                return 0;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        })));
    }

    public static boolean is(String name) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> clazz = Class.forName("cn.ksmcbrigade.VM.Utils");
        Class<?>[] parameterTypes = new Class[]{String.class};
        Method method = clazz.getDeclaredMethod("isEnabledMod", parameterTypes);
        method.setAccessible(true);
        Object instance = clazz.getDeclaredConstructor().newInstance();
        Object obj = method.invoke(instance, name);
        if(obj==null){
            return false;
        }
        else{
            return (boolean)obj;
        }
    }
}
