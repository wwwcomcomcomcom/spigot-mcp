package wwwcomcomcomcom.spigotMcp;

import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import wwwcomcomcomcom.spigotMcp.mcp.McpServer;

import java.lang.reflect.Field;

public final class SpigotMcp extends JavaPlugin {

    private static SimpleCommandMap commandMap;
    private McpServer mcpServer;

    @Override
    public void onEnable() {
        try {
            // Initialize command map
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            commandMap = (SimpleCommandMap) field.get(Bukkit.getServer());
            getLogger().info("Command map initialized successfully");

            // Start MCP server
            mcpServer = new McpServer(this);
            int port = getConfig().getInt("mcp-port", 3000);
            mcpServer.start(port);

            getLogger().info("Spigot MCP Plugin enabled successfully!");
            getLogger().info("MCP Server accessible at http://localhost:" + port + "/mcp");

        } catch (Exception e) {
            getLogger().severe("Failed to enable plugin: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        if (mcpServer != null) {
            mcpServer.stop();
        }
        getLogger().info("Spigot MCP Plugin disabled");
    }

    public static SimpleCommandMap getCommandMap() {
        return commandMap;
    }
}
