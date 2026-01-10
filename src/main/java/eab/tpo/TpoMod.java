package eab.tpo;

import net.fabricmc.api.ModInitializer;
// 必须导入以下两个包（如果没有自动导入，请手动添加）
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import eab.tpo.command.TpoCommand;

public class TpoMod implements ModInitializer {
    @Override
    public void onInitialize() {
        System.out.println("TPO Mod initialized!");

        // [修复] 使用回调事件来注册命令
        // 当服务器准备注册命令时，Fabric 会调用这段代码，并把 dispatcher 传给我们
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            TpoCommand.register(dispatcher);
        });
    }
}