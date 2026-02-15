#!/usr/bin/env python3
"""
Simple MCP client for testing the Spigot MCP server
"""

import requests
import json

class McpClient:
    def __init__(self, url="http://localhost:3000/mcp"):
        self.url = url
        self.request_id = 0

    def _send_request(self, method, params=None):
        """Send a JSON-RPC request to the MCP server"""
        self.request_id += 1

        payload = {
            "jsonrpc": "2.0",
            "id": self.request_id,
            "method": method,
            "params": params or {}
        }

        print(f"\n📤 Sending request: {method}")
        print(f"   Payload: {json.dumps(payload, indent=2)}")

        try:
            response = requests.post(self.url, json=payload)
            response.raise_for_status()

            result = response.json()
            print(f"✅ Response received:")
            print(f"   {json.dumps(result, indent=2)}")

            return result
        except requests.exceptions.RequestException as e:
            print(f"❌ Error: {e}")
            return None

    def initialize(self):
        """Initialize the MCP connection"""
        params = {
            "protocolVersion": "2024-11-05",
            "capabilities": {},
            "clientInfo": {
                "name": "test-client",
                "version": "1.0.0"
            }
        }
        return self._send_request("initialize", params)

    def initialized(self):
        """Send initialized notification"""
        print(f"\n📤 Sending notification: initialized")
        payload = {
            "jsonrpc": "2.0",
            "method": "initialized",
            "params": {}
        }
        try:
            response = requests.post(self.url, json=payload)
            print(f"✅ Notification sent (status: {response.status_code})")
        except requests.exceptions.RequestException as e:
            print(f"❌ Error: {e}")

    def list_tools(self):
        """List available tools"""
        return self._send_request("tools/list")

    def call_tool(self, tool_name, arguments):
        """Call a tool"""
        params = {
            "name": tool_name,
            "arguments": arguments
        }
        return self._send_request("tools/call", params)

    def get_available_commands(self):
        """Get available Minecraft commands"""
        return self.call_tool("get_available_commands", {})

    def run_command(self, command):
        """Run a Minecraft command"""
        return self.call_tool("run_command", {"command": command})


def main():
    print("=" * 60)
    print("  Spigot MCP Server Test Client")
    print("=" * 60)

    client = McpClient()

    # Step 1: Initialize
    print("\n🔧 Step 1: Initialize connection")
    client.initialize()

    # Step 2: Send initialized notification
    print("\n🔧 Step 2: Send initialized notification")
    client.initialized()

    # Step 3: List tools
    print("\n🔧 Step 3: List available tools")
    client.list_tools()

    # Step 4: Get available commands
    print("\n🔧 Step 4: Get available Minecraft commands")
    client.get_available_commands()

    # Step 5: Run a test command
    print("\n🔧 Step 5: Run a test command")
    client.run_command("say Hello from MCP test client!")

    print("\n" + "=" * 60)
    print("  Test completed!")
    print("=" * 60)


if __name__ == "__main__":
    main()
