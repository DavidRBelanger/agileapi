# Endpoints

## Auth

`POST /api/v1/auth/register` Creates a new `User` within an existing `Organization`.

* The request body must include the user’s `name`, `email`, plaintext `password`, and the `slug` of the organization they’re joining. The server hashes the password using BCrypt before saving and links the user to the specified organization.
* **_Behavior_**: Validates organization existence, ensures email uniqueness, and stores only the hashed password.
* **_Response_**: `201 Created` with the new user object (excluding password hash).

`POST /api/v1/auth/login` Authenticates a user and issues a JWT.

* The request body must include the user’s `email` and plaintext `password`.
* **_Behavior_**: Validates the password against the stored hash and returns a signed JWT containing the user’s ID and organization ID.
* **_Response_**: `200 OK` with `{ accessToken: String, tokenType: "Bearer" }`.

---

## Organization

`GET /api/v1/organizations/me` Returns the `Organization` associated with the authenticated user.

* **_Behavior_**: Reads the `organizationId` from the JWT and returns metadata such as `id`, `name`, `slug`, and `createdAt`.
* **_Response_**: `200 OK` with the `Organization` object.

---

## Users

`GET /api/v1/users/{id}` Returns a specific user’s details within the same organization.

* **_Behavior_**: Validates that the target user belongs to the authenticated user’s organization before returning.
* **_Response_**: `200 OK` with the `User` object.

`PATCH /api/v1/users/{id}` Updates fields of an existing user.

* **_Behavior_**: Accepts partial updates for fields like `name` or `email`. Only provided fields are updated.
* **_Response_**: `200 OK` with the updated `User` object.

`DELETE /api/v1/users/{id}` Deletes a user from the organization.

* **_Behavior_**: Removes the user if they belong to the same organization as the requester.
* **_Response_**: `204 No Content`.

---

## Projects

`GET /api/v1/projects` Returns all `Project` resources in the authenticated user’s organization.

* **_Behavior_**: Filters projects by `organizationId` from the JWT.
* **_Response_**: `200 OK` with a list of `Project` objects.

`POST /api/v1/projects` Creates a new `Project`.

* **_Behavior_**: Requires `name` and optional `description`. Links the project to the authenticated user’s organization.
* **_Response_**: `201 Created` with the new `Project` object.

`GET /api/v1/projects/{projectId}` Returns details for a specific project.

* **_Behavior_**: Ensures the project belongs to the user’s organization.
* **_Response_**: `200 OK` with the `Project` object.

`PATCH /api/v1/projects/{projectId}` Updates fields on a project.

* **_Behavior_**: Accepts partial updates for `name` or `description`.
* **_Response_**: `200 OK` with the updated `Project` object.

`DELETE /api/v1/projects/{projectId}` Deletes a project and all related sprints and tasks.

* **_Behavior_**: Ensures the project belongs to the organization before deletion.
* **_Response_**: `204 No Content`.

---

## Sprints

`GET /api/v1/projects/{projectId}/sprints` Returns all `Sprint` resources under a project.

* **_Behavior_**: Filters sprints by `projectId`.
* **_Response_**: `200 OK` with a list of `Sprint` objects.

`POST /api/v1/projects/{projectId}/sprints` Creates a new `Sprint`.

* **_Behavior_**: Requires `name`, `startDate`, and `endDate`. Links the sprint to the specified project.
* **_Response_**: `201 Created` with the new `Sprint` object.

`GET /api/v1/sprints/{sprintId}` Returns a specific sprint by ID.

* **_Behavior_**: Ensures the sprint belongs to a project within the user’s organization.
* **_Response_**: `200 OK` with the `Sprint` object.

`PATCH /api/v1/sprints/{sprintId}` Updates fields of a sprint.

* **_Behavior_**: Accepts partial updates for `name`, `startDate`, or `endDate`.
* **_Response_**: `200 OK` with the updated `Sprint` object.

`DELETE /api/v1/sprints/{sprintId}` Deletes a sprint and its tasks.

* **_Behavior_**: Ensures the sprint belongs to a project within the organization before deletion.
* **_Response_**: `204 No Content`.

---

## Tasks

`GET /api/v1/sprints/{sprintId}/tasks` Returns all `Task` resources under a sprint.

* **_Behavior_**: Supports optional query parameters like `status`, `priority`, and `assigneeId`.
* **_Response_**: `200 OK` with a list of `Task` objects.

`POST /api/v1/sprints/{sprintId}/tasks` Creates a new `Task`.

* **_Behavior_**: Requires `title`, `status`, `priority`, and `assigneeId`. Optionally accepts `description`.
* **_Response_**: `201 Created` with the new `Task` object.

`GET /api/v1/tasks/{taskId}` Returns a specific task by ID.

* **_Behavior_**: Ensures the task belongs to a sprint within the user’s organization.
* **_Response_**: `200 OK` with the `Task` object.

`PATCH /api/v1/tasks/{taskId}` Updates fields of a task.

* **_Behavior_**: Accepts partial updates for `status`, `priority`, `title`, `description`, or `assigneeId`.
* **_Response_**: `200 OK` with the updated `Task` object.

`DELETE /api/v1/tasks/{taskId}` Deletes a task.

* **_Behavior_**: Ensures the task belongs to a sprint within the user’s organization before deletion.
* **_Response_**: `204 No Content`.
