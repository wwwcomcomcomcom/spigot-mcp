package wwwcomcomcomcom.spigotMcp.mcp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.bukkit.plugin.Plugin;
import wwwcomcomcomcom.spigotMcp.mcp.protocol.*;
import wwwcomcomcomcom.spigotMcp.mcp.tools.GetAvailableCommandsTool;
import wwwcomcomcomcom.spigotMcp.mcp.tools.McpTool;
import wwwcomcomcomcom.spigotMcp.mcp.tools.RunCommandTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class McpServer {
    private static final String PROTOCOL_VERSION = "2024-11-05";
    private static final int DEFAULT_PORT = 3000;

    private final Plugin plugin;
    private final Logger logger;
    private final Gson gson;
    private Javalin app;
    private final Map<String, McpTool> tools;
    private boolean initialized = false;

    public McpServer(Plugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.tools = new HashMap<>();

        registerTools();
    }

    private void registerTools() {
        registerTool(new RunCommandTool());
        registerTool(new GetAvailableCommandsTool());
    }

    private void registerTool(McpTool tool) {
        tools.put(tool.getToolDefinition().name, tool);
        logger.info("Registered MCP tool: " + tool.getToolDefinition().name);
    }

    public void start(int port) {
        try {
            app = Javalin.create(config -> {
                config.showJavalinBanner = false;
            }).start(port);

            app.post("/mcp", this::handleRequest);
            app.get("/mcp", ctx -> ctx.result("MCP Server is running"));

            logger.info("MCP Server started on port " + port);
        } catch (Exception e) {
            logger.severe("Failed to start MCP server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void start() {
        start(DEFAULT_PORT);
    }

    public void stop() {
        if (app != null) {
            app.stop();
            logger.info("MCP Server stopped");
        }
    }

    private void handleRequest(Context ctx) {
        try {
            String body = ctx.body();
            JsonRpcMessage request = gson.fromJson(body, JsonRpcMessage.class);

            if (request == null || !request.isRequest()) {
                sendError(ctx, null, -32600, "Invalid Request");
                return;
            }

            JsonRpcMessage response = new JsonRpcMessage();
            response.id = request.id;

            try {
                switch (request.method) {
                    case "initialize":
                        response.result = gson.toJsonTree(handleInitialize(request.params));
                        break;
                    case "initialized":
                        handleInitialized();
                        ctx.status(202);
                        return;
                    case "tools/list":
                        response.result = gson.toJsonTree(handleToolsList());
                        break;
                    case "tools/call":
                        response.result = gson.toJsonTree(handleToolsCall(request.params));
                        break;
                    default:
                        sendError(ctx, request.id, -32601, "Method not found: " + request.method);
                        return;
                }

                ctx.contentType("application/json");
                ctx.result(gson.toJson(response));

            } catch (Exception e) {
                logger.warning("Error handling method " + request.method + ": " + e.getMessage());
                sendError(ctx, request.id, -32603, "Internal error: " + e.getMessage());
            }

        } catch (Exception e) {
            logger.warning("Error parsing request: " + e.getMessage());
            sendError(ctx, null, -32700, "Parse error");
        }
    }

    private InitializeResult handleInitialize(com.google.gson.JsonElement params) {
        InitializeRequest initRequest = gson.fromJson(params, InitializeRequest.class);
        logger.info("Initializing MCP connection from client: " +
                (initRequest.clientInfo != null ? initRequest.clientInfo.name : "unknown"));

        InitializeResult result = new InitializeResult(PROTOCOL_VERSION);
        return result;
    }

    private void handleInitialized() {
        initialized = true;
        logger.info("MCP connection initialized successfully");
    }

    private JsonObject handleToolsList() {
        List<Tool> toolList = new ArrayList<>();
        for (McpTool tool : tools.values()) {
            toolList.add(tool.getToolDefinition());
        }

        JsonObject result = new JsonObject();
        result.add("tools", gson.toJsonTree(toolList));
        return result;
    }

    private JsonObject handleToolsCall(com.google.gson.JsonElement params) {
        CallToolRequest callRequest = gson.fromJson(params, CallToolRequest.class);

        if (!tools.containsKey(callRequest.name)) {
            throw new RuntimeException("Tool not found: " + callRequest.name);
        }

        McpTool tool = tools.get(callRequest.name);
        ToolResponse response = tool.execute(callRequest.arguments);

        return gson.toJsonTree(response).getAsJsonObject();
    }

    private void sendError(Context ctx, Object id, int code, String message) {
        JsonRpcMessage error = new JsonRpcMessage();
        error.id = id;
        error.error = new JsonRpcMessage.JsonRpcError(code, message);

        ctx.contentType("application/json");
        ctx.status(code == -32600 ? 400 : 200);
        ctx.result(gson.toJson(error));
    }

    public boolean isInitialized() {
        return initialized;
    }
}
