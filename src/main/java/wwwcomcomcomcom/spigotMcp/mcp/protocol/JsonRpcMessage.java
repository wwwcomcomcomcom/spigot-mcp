package wwwcomcomcomcom.spigotMcp.mcp.protocol;

import com.google.gson.JsonElement;

public class JsonRpcMessage {
    public String jsonrpc = "2.0";
    public Object id;
    public String method;
    public JsonElement params;
    public JsonElement result;
    public JsonRpcError error;

    public static class JsonRpcError {
        public int code;
        public String message;
        public JsonElement data;

        public JsonRpcError(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }

    public boolean isRequest() {
        return method != null && id != null;
    }

    public boolean isNotification() {
        return method != null && id == null;
    }

    public boolean isResponse() {
        return method == null && (result != null || error != null);
    }
}
