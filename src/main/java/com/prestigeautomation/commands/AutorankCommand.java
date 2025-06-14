package com.prestigeautomation.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import com.prestigeautomation.PrestigeAutomationClient;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;

public class AutorankCommand {
    
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("autorank")
            .then(literal("prestige")
                .then(literal("toggle")
                    .executes(context -> {
                        PrestigeAutomationClient.config.toggle("prestige");
                        boolean enabled = PrestigeAutomationClient.config.prestigeEnabled;
                        context.getSource().sendFeedback(Text.literal(
                            "§7[§6AutoRank§7] Prestige automation " + (enabled ? "§aenabled" : "§cdisabled")
                        ));
                        return 1;
                    }))
                .then(literal("interval")
                    .then(argument("ticks", IntegerArgumentType.integer(20))
                        .executes(context -> {
                            int ticks = IntegerArgumentType.getInteger(context, "ticks");
                            PrestigeAutomationClient.config.setInterval("prestige", ticks);
                            context.getSource().sendFeedback(Text.literal(
                                "§7[§6AutoRank§7] Prestige interval set to §b" + ticks + " ticks §7(" + (ticks / 20.0) + " seconds)"
                            ));
                            return 1;
                        })))
                .then(literal("status")
                    .executes(context -> {
                        var config = PrestigeAutomationClient.config;
                        context.getSource().sendFeedback(Text.literal(
                            "§7[§6AutoRank§7] §ePrestige Status:\n" +
                            "§7Enabled: " + (config.prestigeEnabled ? "§aYes" : "§cNo") + "\n" +
                            "§7Interval: §b" + config.prestigeInterval + " ticks §7(" + (config.prestigeInterval / 20.0) + "s)\n" +
                            "§7Slot: §b" + config.inventorySlot + "\n" +
                            "§7Debug: " + (config.debugMode ? "§aOn" : "§cOff")
                        ));
                        return 1;
                    }))
                .then(literal("slot")
                    .then(argument("slot", IntegerArgumentType.integer(0, 8))
                        .executes(context -> {
                            int slot = IntegerArgumentType.getInteger(context, "slot");
                            PrestigeAutomationClient.config.inventorySlot = slot;
                            PrestigeAutomationClient.config.save();
                            context.getSource().sendFeedback(Text.literal(
                                "§7[§6AutoRank§7] Inventory slot set to §b" + slot
                            ));
                            return 1;
                        })))
                .then(literal("debug")
                    .executes(context -> {
                        PrestigeAutomationClient.config.toggle("debug");
                        boolean enabled = PrestigeAutomationClient.config.debugMode;
                        context.getSource().sendFeedback(Text.literal(
                            "§7[§6AutoRank§7] Debug mode " + (enabled ? "§aenabled" : "§cdisabled")
                        ));
                        return 1;
                    })))
            .then(literal("ascension")
                .then(literal("toggle")
                    .executes(context -> {
                        PrestigeAutomationClient.config.toggle("ascension");
                        boolean enabled = PrestigeAutomationClient.config.ascensionEnabled;
                        context.getSource().sendFeedback(Text.literal(
                            "§7[§6AutoRank§7] Ascension automation " + (enabled ? "§aenabled" : "§cdisabled")
                        ));
                        return 1;
                    }))
                .then(literal("interval")
                    .then(argument("ticks", IntegerArgumentType.integer(20))
                        .executes(context -> {
                            int ticks = IntegerArgumentType.getInteger(context, "ticks");
                            PrestigeAutomationClient.config.setInterval("ascension", ticks);
                            context.getSource().sendFeedback(Text.literal(
                                "§7[§6AutoRank§7] Ascension interval set to §b" + ticks + " ticks §7(" + (ticks / 20.0) + " seconds)"
                            ));
                            return 1;
                        })))
                .then(literal("status")
                    .executes(context -> {
                        var config = PrestigeAutomationClient.config;
                        context.getSource().sendFeedback(Text.literal(
                            "§7[§6AutoRank§7] §dAscension Status:\n" +
                            "§7Enabled: " + (config.ascensionEnabled ? "§aYes" : "§cNo") + "\n" +
                            "§7Interval: §b" + config.ascensionInterval + " ticks §7(" + (config.ascensionInterval / 20.0) + "s)\n" +
                            "§7Slot: §b" + config.inventorySlot + "\n" +
                            "§7Debug: " + (config.debugMode ? "§aOn" : "§cOff")
                        ));
                        return 1;
                    })))
            .then(literal("help")
                .executes(context -> {
                    context.getSource().sendFeedback(Text.literal(
                        "§7[§6AutoRank§7] §eAvailable Commands:\n" +
                        "§b/autorank prestige toggle §7- Enable/disable prestige automation\n" +
                        "§b/autorank prestige interval <ticks> §7- Set prestige check interval\n" +
                        "§b/autorank prestige status §7- Show prestige settings\n" +
                        "§b/autorank prestige slot <0-8> §7- Set inventory slot to monitor\n" +
                        "§b/autorank prestige debug §7- Toggle debug mode\n" +
                        "§d/autorank ascension toggle §7- Enable/disable ascension automation\n" +
                        "§d/autorank ascension interval <ticks> §7- Set ascension check interval\n" +
                        "§d/autorank ascension status §7- Show ascension settings"
                    ));
                    return 1;
                })));
    }
}