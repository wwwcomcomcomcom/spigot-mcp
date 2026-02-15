# Spigot MCP Server

Minecraft Spigot 서버 관리를 자동화하기 위한 MCP (Model Context Protocol) 서버 플러그인입니다.

## 기능

이 플러그인은 AI 모델이 Minecraft 서버를 관리할 수 있도록 다음 기능을 제공합니다:

### Tools

1. **run_command**
   - Minecraft 서버 명령어를 ConsoleSender로 실행합니다
   - 파라미터:
     - `command` (string): 실행할 명령어 (슬래시 없이)
   - 예시: `{"command": "say Hello, world!"}`

2. **get_available_commands**
   - 서버에서 사용 가능한 모든 명령어 목록을 반환합니다
   - 각 명령어의 이름, 설명, 사용법, 별칭을 포함합니다
   - 파라미터: 없음

## 설치

1. 플러그인 빌드:
   ```bash
   ./gradlew build
   ```

2. 생성된 JAR 파일을 Spigot 서버의 `plugins` 폴더에 복사:
   ```bash
   cp build/libs/spigot-mcp-1.0-SNAPSHOT.jar <서버경로>/plugins/
   ```

3. 서버를 시작하거나 재시작합니다

## 설정

`plugins/spigot-mcp/config.yml`:

```yaml
# MCP 서버가 수신할 포트
mcp-port: 3000
```

## 사용법

### MCP 클라이언트 연결

플러그인이 활성화되면 MCP 서버는 구성된 포트(기본값: 3000)에서 실행됩니다.

**엔드포인트**: `http://localhost:3000/mcp`

### 프로토콜

이 서버는 MCP Streamable HTTP transport를 구현합니다:
- **프로토콜 버전**: 2024-11-05
- **Transport**: HTTP POST (JSON-RPC)
- **메시지 형식**: JSON-RPC 2.0

### 초기화 예시

```json
POST http://localhost:3000/mcp

{
  "jsonrpc": "2.0",
  "id": 1,
  "method": "initialize",
  "params": {
    "protocolVersion": "2024-11-05",
    "capabilities": {},
    "clientInfo": {
      "name": "my-client",
      "version": "1.0.0"
    }
  }
}
```

### Tool 목록 조회

```json
POST http://localhost:3000/mcp

{
  "jsonrpc": "2.0",
  "id": 2,
  "method": "tools/list",
  "params": {}
}
```

### 명령어 실행

```json
POST http://localhost:3000/mcp

{
  "jsonrpc": "2.0",
  "id": 3,
  "method": "tools/call",
  "params": {
    "name": "run_command",
    "arguments": {
      "command": "say Hello from MCP!"
    }
  }
}
```

## 개발

### 요구사항

- Java 8+
- Gradle
- Spigot API 1.13+

### 프로젝트 구조

```
src/main/java/wwwcomcomcomcom/spigotMcp/
├── SpigotMcp.java              # 메인 플러그인 클래스
├── mcp/
│   ├── McpServer.java          # MCP HTTP 서버
│   ├── protocol/               # MCP 프로토콜 메시지 클래스
│   │   ├── JsonRpcMessage.java
│   │   ├── InitializeRequest.java
│   │   ├── InitializeResult.java
│   │   ├── Tool.java
│   │   ├── CallToolRequest.java
│   │   └── ToolResponse.java
│   └── tools/                  # MCP Tool 구현
│       ├── McpTool.java
│       ├── RunCommandTool.java
│       └── GetAvailableCommandsTool.java
```

### 새 Tool 추가하기

1. `McpTool` 인터페이스를 구현하는 새 클래스를 만듭니다:

```java
public class MyCustomTool implements McpTool {
    @Override
    public Tool getToolDefinition() {
        // Tool 정의 반환
    }

    @Override
    public ToolResponse execute(JsonObject arguments) {
        // Tool 로직 구현
    }
}
```

2. `McpServer.registerTools()`에 등록합니다:

```java
registerTool(new MyCustomTool());
```

## 라이선스

이 프로젝트는 오픈 소스 프로젝트입니다.

## 참고 자료

- [Model Context Protocol](https://modelcontextprotocol.io/)
- [Spigot API Documentation](https://hub.spigotmc.org/javadocs/spigot/)
