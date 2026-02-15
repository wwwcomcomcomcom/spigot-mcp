package wwwcomcomcomcom.spigotMcp.mcp.tools;

import com.google.gson.JsonObject;
import org.bukkit.command.Command;
import wwwcomcomcomcom.spigotMcp.SpigotMcp;
import wwwcomcomcomcom.spigotMcp.mcp.protocol.Tool;
import wwwcomcomcomcom.spigotMcp.mcp.protocol.ToolResponse;

import java.util.HashMap;
import java.util.Map;

public class GetAvailableCommandsTool implements McpTool {

    @Override
    public Tool getToolDefinition() {
        Map<String, Object> inputSchema = new HashMap<>();
        inputSchema.put("type", "object");
        inputSchema.put("properties", new HashMap<>());

        return new Tool(
            "get_available_commands",
            "Get a list of all available Minecraft server commands with their descriptions and usage",
            inputSchema
        );
    }

    @Override
    public ToolResponse execute(JsonObject arguments) {
        ToolResponse response = new ToolResponse();

        try {
            StringBuilder commandList = new StringBuilder();
            commandList.append("Available Minecraft Server Commands:\n\n");

            for (Command command : SpigotMcp.getCommandMap().getCommands()) {
                commandList.append("Command: ").append(command.getName()).append("\n");

                if (command.getDescription() != null && !command.getDescription().isEmpty()) {
                    commandList.append("  Description: ").append(command.getDescription()).append("\n");
                }

                if (command.getUsage() != null && !command.getUsage().isEmpty()) {
                    commandList.append("  Usage: ").append(command.getUsage()).append("\n");
                }

                if (command.getAliases() != null && !command.getAliases().isEmpty()) {
                    commandList.append("  Aliases: ").append(String.join(", ", command.getAliases())).append("\n");
                }

                commandList.append("\n");
            }

            response.addText(commandList.toString());

        } catch (Exception e) {
            response.isError = true;
            response.addText("Error retrieving commands: " + e.getMessage());
        }

        return response;
    }
}
