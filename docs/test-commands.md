# API Test Commands

Use these curl commands to test the AgileAPI endpoints. Make sure to replace `$TOKEN` with your actual JWT token after logging in.

## 1. Authentication

### Login
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"user1@example.com","password":"test123"}'
```

**Save the token from the response:**
```bash
TOKEN="your_access_token_here"
```

### Register a new user and create organization
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H 'Content-Type: application/json' \
  -d '{"email":"user2@example.com","password":"test456","name":"Another User","organizationName":"Demo Company","organizationSlug":"demo-co"}'
```

**Note:** Each user creates their own organization during registration. The slug must be unique and contain only lowercase letters, numbers, and hyphens.

---

## 2. Organization

### Get current organization
```bash
curl -X GET http://localhost:8080/api/v1/organizations/me \
  -H "Authorization: Bearer $TOKEN"
```

---

## 3. Users

### Get user by ID
```bash
curl -X GET http://localhost:8080/api/v1/users/1 \
  -H "Authorization: Bearer $TOKEN"
```

### Update user
```bash
curl -X PATCH http://localhost:8080/api/v1/users/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"name":"Updated Name","email":"newemail@example.com"}'
```

### Delete user
```bash
curl -X DELETE http://localhost:8080/api/v1/users/1 \
  -H "Authorization: Bearer $TOKEN"
```

---

## 4. Projects

### Get all projects
```bash
curl -X GET http://localhost:8080/api/v1/projects \
  -H "Authorization: Bearer $TOKEN"
```

### Create a project
```bash
curl -X POST http://localhost:8080/api/v1/projects \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"name":"Website Redesign","description":"Complete overhaul of company website"}'
```

### Get project by ID
```bash
curl -X GET http://localhost:8080/api/v1/projects/1 \
  -H "Authorization: Bearer $TOKEN"
```

### Update project
```bash
curl -X PATCH http://localhost:8080/api/v1/projects/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"name":"Updated Project Name","description":"Updated description"}'
```

### Delete project
```bash
curl -X DELETE http://localhost:8080/api/v1/projects/1 \
  -H "Authorization: Bearer $TOKEN"
```

---

## 5. Sprints

### Get all sprints for a project
```bash
curl -X GET http://localhost:8080/api/v1/projects/1/sprints \
  -H "Authorization: Bearer $TOKEN"
```

### Create a sprint
```bash
curl -X POST http://localhost:8080/api/v1/projects/1/sprints \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"name":"Sprint 1","startDate":"2025-10-07","endDate":"2025-10-21"}'
```

### Get sprint by ID
```bash
curl -X GET http://localhost:8080/api/v1/sprints/1 \
  -H "Authorization: Bearer $TOKEN"
```

### Update sprint
```bash
curl -X PATCH http://localhost:8080/api/v1/sprints/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"name":"Sprint 1 - Updated","endDate":"2025-10-28"}'
```

### Delete sprint
```bash
curl -X DELETE http://localhost:8080/api/v1/sprints/1 \
  -H "Authorization: Bearer $TOKEN"
```

---

## 6. Tasks

### Get all tasks for a sprint
```bash
curl -X GET http://localhost:8080/api/v1/sprints/1/tasks \
  -H "Authorization: Bearer $TOKEN"
```

### Get tasks with filters
```bash
# Filter by status
curl -X GET "http://localhost:8080/api/v1/sprints/1/tasks?status=TO_DO" \
  -H "Authorization: Bearer $TOKEN"

# Filter by priority (1-5)
curl -X GET "http://localhost:8080/api/v1/sprints/1/tasks?priority=5" \
  -H "Authorization: Bearer $TOKEN"

# Filter by assignee
curl -X GET "http://localhost:8080/api/v1/sprints/1/tasks?assigneeId=1" \
  -H "Authorization: Bearer $TOKEN"

# Multiple filters
curl -X GET "http://localhost:8080/api/v1/sprints/1/tasks?status=IN_PROGRESS&priority=3" \
  -H "Authorization: Bearer $TOKEN"
```

### Create a task
```bash
curl -X POST http://localhost:8080/api/v1/sprints/1/tasks \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"title":"Design homepage mockup","description":"Create initial design for new homepage","status":"TO_DO","priority":5,"assigneeId":1}'
```

**Available status values:** `TO_DO`, `IN_PROGRESS`, `DONE`, `BLOCKED`  
**Available priority values:** Integer from 1 (lowest) to 5 (highest)

### Get task by ID
```bash
curl -X GET http://localhost:8080/api/v1/tasks/1 \
  -H "Authorization: Bearer $TOKEN"
```

### Update task
```bash
curl -X PATCH http://localhost:8080/api/v1/tasks/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"status":"IN_PROGRESS","priority":3}'
```

### Delete task
```bash
curl -X DELETE http://localhost:8080/api/v1/tasks/1 \
  -H "Authorization: Bearer $TOKEN"
```

---

## Quick Test Workflow

Here's a complete workflow to test all endpoints:

```bash
# 1. Login and save token
LOGIN_RESPONSE=$(curl -s -X POST http://localhost:8080/api/v1/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"user1@example.com","password":"test123"}')
TOKEN=$(echo $LOGIN_RESPONSE | jq -r '.accessToken')

# 2. Get organization
curl -X GET http://localhost:8080/api/v1/organizations/me \
  -H "Authorization: Bearer $TOKEN"

# 3. Create project
PROJECT=$(curl -s -X POST http://localhost:8080/api/v1/projects \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"name":"Test Project","description":"Testing API"}')
PROJECT_ID=$(echo $PROJECT | jq -r '.id')

# 4. Create sprint
SPRINT=$(curl -s -X POST http://localhost:8080/api/v1/projects/${PROJECT_ID}/sprints \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"name":"Sprint 1","startDate":"2025-10-07","endDate":"2025-10-21"}')
SPRINT_ID=$(echo $SPRINT | jq -r '.id')

# 5. Create task
curl -X POST http://localhost:8080/api/v1/sprints/${SPRINT_ID}/tasks \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"title":"Test Task","description":"Testing","status":"TO_DO","priority":5,"assigneeId":1}'

# 6. View all tasks
curl -X GET http://localhost:8080/api/v1/sprints/${SPRINT_ID}/tasks \
  -H "Authorization: Bearer $TOKEN"
```

---

## Notes

- All authenticated endpoints require the `Authorization: Bearer $TOKEN` header
- Make sure `jq` is installed for JSON parsing in bash scripts
- The base URL is `http://localhost:8080/api/v1`
- User ID 1 belongs to organization "Acme Corporation" (ID: 1)
