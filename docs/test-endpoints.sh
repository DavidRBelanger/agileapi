#!/bin/bash
# API Testing Script for AgileAPI
# This script tests the entire workflow using the registered user

BASE_URL="http://localhost:8080/api/v1"

echo "=========================================="
echo "AgileAPI Endpoint Testing"
echo "=========================================="
echo ""

# ==========================================
# 0. AUTH - Register (creates user + organization)
# ==========================================
echo "0. Registering user and creating organization..."
REGISTER_RESPONSE=$(curl -s -X POST ${BASE_URL}/auth/register \
  -H 'Content-Type: application/json' \
  -d '{"email":"user1@example.com","password":"test123","name":"Test User","organizationName":"Acme Corporation","organizationSlug":"acme"}')

# Check if registration was successful or user already exists
if echo "$REGISTER_RESPONSE" | jq -e '.id' > /dev/null 2>&1; then
    echo "✓ User and organization created successfully"
elif echo "$REGISTER_RESPONSE" | grep -q "already exists\|already registered\|already taken"; then
    echo "✓ User or organization already exists (expected on subsequent runs)"
else
    echo "⚠ Registration response: $REGISTER_RESPONSE" | jq '.' 2>/dev/null || echo "$REGISTER_RESPONSE"
fi
echo ""

# ==========================================
# 1. AUTH - Login
# ==========================================
echo "1. Logging in with existing user..."
LOGIN_RESPONSE=$(curl -s -X POST ${BASE_URL}/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"user1@example.com","password":"test123"}')

echo "$LOGIN_RESPONSE" | jq '.'

# Extract the access token
TOKEN=$(echo $LOGIN_RESPONSE | jq -r '.accessToken')

if [ "$TOKEN" == "null" ] || [ -z "$TOKEN" ]; then
    echo "ERROR: Failed to get access token. Please register a user first."
    exit 1
fi

echo ""
echo "Access Token obtained: ${TOKEN:0:20}..."
echo ""

# ==========================================
# 2. ORGANIZATION - Get current org
# ==========================================
echo "2. Getting organization info..."
curl -s -X GET ${BASE_URL}/organizations/me \
  -H "Authorization: Bearer $TOKEN" | jq '.'
echo ""

# ==========================================
# 3. USERS - Get user details
# ==========================================
echo "3. Getting user details (ID: 1)..."
curl -s -X GET ${BASE_URL}/users/1 \
  -H "Authorization: Bearer $TOKEN" | jq '.'
echo ""

echo "4. Updating user..."
curl -s -X PATCH ${BASE_URL}/users/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"name":"Test User Updated"}' | jq '.'
echo ""

# ==========================================
# 5. PROJECTS - Create and manage
# ==========================================
echo "5. Creating a new project..."
PROJECT_RESPONSE=$(curl -s -X POST ${BASE_URL}/projects \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"name":"Website Redesign","description":"Complete overhaul of company website"}')

echo "$PROJECT_RESPONSE" | jq '.'
PROJECT_ID=$(echo $PROJECT_RESPONSE | jq -r '.id')
echo ""

echo "6. Getting all projects..."
curl -s -X GET ${BASE_URL}/projects \
  -H "Authorization: Bearer $TOKEN" | jq '.'
echo ""

echo "7. Getting project details (ID: $PROJECT_ID)..."
curl -s -X GET ${BASE_URL}/projects/${PROJECT_ID} \
  -H "Authorization: Bearer $TOKEN" | jq '.'
echo ""

echo "8. Updating project..."
curl -s -X PATCH ${BASE_URL}/projects/${PROJECT_ID} \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"description":"Complete overhaul of company website - Phase 1"}' | jq '.'
echo ""

# ==========================================
# 9. SPRINTS - Create and manage
# ==========================================
echo "9. Creating a new sprint..."
SPRINT_RESPONSE=$(curl -s -X POST ${BASE_URL}/projects/${PROJECT_ID}/sprints \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"name":"Sprint 1","startDate":"2025-10-07","endDate":"2025-10-21"}')

echo "$SPRINT_RESPONSE" | jq '.'
SPRINT_ID=$(echo $SPRINT_RESPONSE | jq -r '.id')
echo ""

echo "10. Getting all sprints for project..."
curl -s -X GET ${BASE_URL}/projects/${PROJECT_ID}/sprints \
  -H "Authorization: Bearer $TOKEN" | jq '.'
echo ""

echo "11. Getting sprint details (ID: $SPRINT_ID)..."
curl -s -X GET ${BASE_URL}/sprints/${SPRINT_ID} \
  -H "Authorization: Bearer $TOKEN" | jq '.'
echo ""

echo "12. Updating sprint..."
curl -s -X PATCH ${BASE_URL}/sprints/${SPRINT_ID} \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"name":"Sprint 1 - Design Phase"}' | jq '.'
echo ""

# ==========================================
# 13. TASKS - Create and manage
# ==========================================
echo "13. Creating a new task..."
TASK_RESPONSE=$(curl -s -X POST ${BASE_URL}/sprints/${SPRINT_ID}/tasks \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"title":"Design homepage mockup","description":"Create initial design for new homepage","status":"TO_DO","priority":5,"assigneeId":1}')

echo "$TASK_RESPONSE" | jq '.'
TASK_ID=$(echo $TASK_RESPONSE | jq -r '.id')
echo ""

echo "14. Creating another task..."
curl -s -X POST ${BASE_URL}/sprints/${SPRINT_ID}/tasks \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"title":"Set up development environment","description":"Configure local dev environment","status":"IN_PROGRESS","priority":3,"assigneeId":1}' | jq '.'
echo ""

echo "15. Getting all tasks for sprint..."
curl -s -X GET ${BASE_URL}/sprints/${SPRINT_ID}/tasks \
  -H "Authorization: Bearer $TOKEN" | jq '.'
echo ""

echo "16. Getting tasks filtered by status (TO_DO)..."
curl -s -X GET "${BASE_URL}/sprints/${SPRINT_ID}/tasks?status=TO_DO" \
  -H "Authorization: Bearer $TOKEN" | jq '.'
echo ""

echo "17. Getting task details (ID: $TASK_ID)..."
curl -s -X GET ${BASE_URL}/tasks/${TASK_ID} \
  -H "Authorization: Bearer $TOKEN" | jq '.'
echo ""

echo "18. Updating task status..."
curl -s -X PATCH ${BASE_URL}/tasks/${TASK_ID} \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"status":"IN_PROGRESS"}' | jq '.'
echo ""

# ==========================================
# CLEANUP OPTIONS (commented out by default)
# ==========================================
echo "=========================================="
echo "Testing Complete!"
echo "=========================================="
echo ""
echo "Created resources:"
echo "  - Project ID: $PROJECT_ID"
echo "  - Sprint ID: $SPRINT_ID"
echo "  - Task ID: $TASK_ID"
echo ""
echo "To clean up, uncomment and run the delete commands below:"
echo ""

# Uncomment these lines to clean up test data:
# echo "Deleting task..."
# curl -s -X DELETE ${BASE_URL}/tasks/${TASK_ID} \
#   -H "Authorization: Bearer $TOKEN"
# echo ""
# 
# echo "Deleting sprint..."
# curl -s -X DELETE ${BASE_URL}/sprints/${SPRINT_ID} \
#   -H "Authorization: Bearer $TOKEN"
# echo ""
# 
# echo "Deleting project..."
# curl -s -X DELETE ${BASE_URL}/projects/${PROJECT_ID} \
#   -H "Authorization: Bearer $TOKEN"
# echo ""
# 
# echo "Deleting user..."
# curl -s -X DELETE ${BASE_URL}/users/1 \
#   -H "Authorization: Bearer $TOKEN"
# echo ""
