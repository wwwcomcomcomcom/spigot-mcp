package wwwcomcomcomcom.spigotMcp.mcp.tools;

import com.google.gson.JsonObject;
import wwwcomcomcomcom.spigotMcp.mcp.protocol.Tool;
import wwwcomcomcomcom.spigotMcp.mcp.protocol.ToolResponse;

public interface McpTool {
    Tool getToolDefinition();
    ToolResponse execute(JsonObject arguments);
}
