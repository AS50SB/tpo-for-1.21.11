package eab.tpo.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel; // [必需] 导入 ServerLevel
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Relative; // [新增] 导入 Relative
import java.util.Set; // [新增] 导入 Set

public class TpoCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        // 命令结构: /tpo <玩家>
        dispatcher.register(Commands.literal("tpo")
            .requires(source -> true) // 暂时允许所有人使用，避免权限方法名不匹配问题
            .then(Commands.argument("target", EntityArgument.player())
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    ServerPlayer target = EntityArgument.getPlayer(context, "target");
                    
                    // 获取目标维度
                    ServerLevel targetLevel = (ServerLevel) target.level();

                    // [修复] 适配 1.21.11 的 8 参数写法
                    player.teleportTo(
                        targetLevel,            // 1. 目标维度
                        target.getX(),          // 2. X
                        target.getY(),          // 3. Y
                        target.getZ(),          // 4. Z
                        Set.of(),               // 5. 相对坐标集合 (Set.of() 表示全是绝对坐标)
                        target.getYRot(),       // 6. Yaw
                        target.getXRot(),       // 7. Pitch
                        false                   // 8. 是否保留速度/检查碰撞 (通常填 false)
                    );
                    return 1;
                })
            )
        );
        
        // 命令结构: /tpo <x> <y> <z>
        dispatcher.register(Commands.literal("tpo")
            .requires(source -> true)
            .then(Commands.argument("pos", Vec3Argument.vec3())
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    Vec3 pos = Vec3Argument.getVec3(context, "pos");
                    
                    ServerLevel playerLevel = (ServerLevel) player.level();

                    // [修复] 适配 8 参数写法
                    player.teleportTo(
                        playerLevel,            // 1. 目标维度
                        pos.x,                  // 2. X
                        pos.y,                  // 3. Y
                        pos.z,                  // 4. Z
                        Set.of(),               // 5. 相对坐标集合
                        player.getYRot(),       // 6. Yaw
                        player.getXRot(),       // 7. Pitch
                        false                   // 8. Boolean
                    );
                    return 1;
                })
            )
        );
    }
}