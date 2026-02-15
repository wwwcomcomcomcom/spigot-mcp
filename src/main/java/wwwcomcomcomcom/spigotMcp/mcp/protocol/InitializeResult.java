package wwwcomcomcomcom.spigotMcp.mcp.protocol;

import java.util.Map;

public class InitializeResult {
    public String protocolVersion;
    public ServerCapabilities capabilities;
    public ServerInfo serverInfo;

    public InitializeResult(String protocolVersion) {
        this.protocolVersion = protocolVersion;
        this.capabilities = new ServerCapabilities();
        this.serverInfo = new ServerInfo();
    }

    public static class ServerCapabilities {
        public Map<String, Object> experimental;
        public ToolsCapability tools;

        public ServerCapabilities() {
            this.tools = new ToolsCapability();
        }

        public static class ToolsCapability {
            public boolean listChanged = true;
        }
    }

    public static class ServerInfo {
        public String name = "spigot-mcp";
        public String version = "1.0.0";
    }
}
