package wwwcomcomcomcom.spigotMcp.mcp.tools;

import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import wwwcomcomcomcom.spigotMcp.mcp.protocol.Tool;
import wwwcomcomcomcom.spigotMcp.mcp.protocol.ToolResponse;

import java.util.HashMap;
import java.util.Map;

public class RunCommandTool implements McpTool {

    @Override
    public Tool getToolDefinition() {
        Map<String, Object> inputSchema = new HashMap<>();
        inputSchema.put("type", "object");

        Map<String, Object> properties = new HashMap<>();
        Map<String, Object> commandProp = new HashMap<>();
        commandProp.put("type", "string");
        commandProp.put("description", "The Minecraft command to execute (without leading slash)");
        properties.put("command", commandProp);

        inputSchema.put("properties", properties);
        inputSchema.put("required", new String[]{"command"});

        return new Tool(
            "run_command",
            "Execute a Minecraft server command through the console",
            inputSchema
        );
    }

    @Override
    public ToolResponse execute(JsonObject arguments) {
        ToolResponse response = new ToolResponse();

        try {
            if (!arguments.has("command")) {
                response.isError = true;
                response.addText("Missing required parameter: command");
                return response;
            }

            String command = arguments.get("command").getAsString();

            // Execute command on the main server thread
            Bukkit.getScheduler().runTask(
                Bukkit.getPluginManager().getPlugin("spigot-mcp"),
                () -> {
                    ConsoleCommandSender console = Bukkit.getConsoleSender();
                    Bukkit.dispatchCommand(console, command);
                }
            );

            response.addText("Command executed successfully: " + command);

        } catch (Exception e) {
            response.isError = true;
            response.addText("Error executing command: " + e.getMessage());
        }

        return response;
    }
}
