package wwwcomcomcomcom.spigotMcp.mcp.protocol;

import java.util.Map;

public class Tool {
    public String name;
    public String description;
    public Map<String, Object> inputSchema;

    public Tool(String name, String description, Map<String, Object> inputSchema) {
        this.name = name;
        this.description = description;
        this.inputSchema = inputSchema;
    }
}
