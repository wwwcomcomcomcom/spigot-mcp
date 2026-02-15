#!/bin/bash
# Simple bash script to test MCP server using curl

MCP_URL="http://localhost:3000/mcp"

echo "======================================"
echo "  Spigot MCP Server Test (curl)"
echo "======================================"

# Test 1: Initialize
echo -e "\n🔧 Test 1: Initialize"
curl -X POST "$MCP_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 1,
    "method": "initialize",
    "params": {
      "protocolVersion": "2024-11-05",
      "capabilities": {},
      "clientInfo": {
        "name": "curl-test",
        "version": "1.0.0"
      }
    }
  }' | jq .

# Test 2: Initialized notification
echo -e "\n🔧 Test 2: Send initialized notification"
curl -X POST "$MCP_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "method": "initialized",
    "params": {}
  }'

# Test 3: List tools
echo -e "\n\n🔧 Test 3: List tools"
curl -X POST "$MCP_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 2,
    "method": "tools/list",
    "params": {}
  }' | jq .

# Test 4: Get available commands
echo -e "\n🔧 Test 4: Get available commands"
curl -X POST "$MCP_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 3,
    "method": "tools/call",
    "params": {
      "name": "get_available_commands",
      "arguments": {}
    }
  }' | jq .

# Test 5: Run a command
echo -e "\n🔧 Test 5: Run a test command"
curl -X POST "$MCP_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 4,
    "method": "tools/call",
    "params": {
      "name": "run_command",
      "arguments": {
        "command": "say Hello from MCP curl test!"
      }
    }
  }' | jq .

echo -e "\n======================================"
echo "  Test completed!"
echo "======================================"
