package com.silvager.testPlugin;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.silvager.testPlugin.randomEvents.NewWorldJumpscare;
import com.silvager.testPlugin.randomEvents.SkullJumpscare;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class PluginCommands {
    private static HashMap<String, Consumer<Player>> triggerFxnsMap = null;
    private static void initializeTriggerFxnsMap() {
        PluginCommands.triggerFxnsMap = new HashMap<>();
        PluginCommands.triggerFxnsMap.put("newWorldJumpscare", NewWorldJumpscare::triggerNewWorldJumpscare);
        PluginCommands.triggerFxnsMap.put("scarySkull", SkullJumpscare::showScary);
    }
    public static void registerCommands() {
        PluginCommands.initializeTriggerFxnsMap();
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("vegan");
        LiteralArgumentBuilder<CommandSourceStack> triggerNode = Commands.literal("trigger");
        // Automatically create a new command branch off of "trigger" for every function in triggerFxnsMap
        for (Map.Entry<String, Consumer<Player>> entry : triggerFxnsMap.entrySet()) {
            triggerNode.then(Commands.literal(entry.getKey()).then(Commands.argument("player", ArgumentTypes.player())
                    .executes(ctx -> {
                        final PlayerSelectorArgumentResolver plrResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                        final Player target = plrResolver.resolve(ctx.getSource()).getFirst();
                        entry.getValue().accept(target);
                        return Command.SINGLE_SUCCESS;
                    })));
        }
        root.then(triggerNode);

        VeganHorror.getInstance().getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(root.build());
        });
    }
}
