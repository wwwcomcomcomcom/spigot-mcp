package wwwcomcomcomcom.spigotMcp.mcp.protocol;

import java.util.Map;

public class InitializeRequest {
    public String protocolVersion;
    public ClientCapabilities capabilities;
    public ClientInfo clientInfo;

    public static class ClientCapabilities {
        public Map<String, Object> experimental;
        public Sampling sampling;
        public Roots roots;

        public static class Sampling {
        }

        public static class Roots {
            public boolean listChanged;
        }
    }

    public static class ClientInfo {
        public String name;
        public String version;
    }
}
