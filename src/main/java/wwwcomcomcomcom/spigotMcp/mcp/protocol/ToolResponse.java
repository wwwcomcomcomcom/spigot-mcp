package wwwcomcomcomcom.spigotMcp.mcp.protocol;

import java.util.ArrayList;
import java.util.List;

public class ToolResponse {
    public List<Content> content = new ArrayList<>();
    public boolean isError = false;

    public static class Content {
        public String type = "text";
        public String text;

        public Content(String text) {
            this.text = text;
        }
    }

    public void addText(String text) {
        content.add(new Content(text));
    }
}
