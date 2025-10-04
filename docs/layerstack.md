# Layer Stack

1. Security
2. Controller
3. Service
4. Repository
5. Entity
6. Response

---

## Security
Runs before the controllers, authenticates the request. Validates JWT, verifies signature, and parses it. 
Loads the authenticated user and their organization into the security context.
Rejects the request with 401 or 403 if invalid. Ensures only valid, known users reach the controllers.

---

## Controller

Initial logic handling, parses request body, paramas, and variables. Performs basic validation of if required fields are present.
Pulls the user from the security context, calls service layer to perform bulk of logic.

---

## Service Layer

Core logic functionality, enforces domain rules, orchestrates between multiple repositories. Handles complex validation and decision making, prepares saved or returned data.

---

## Repository Layer

Talks directly to the database. Saves, retrieves, updates, and deletes entities. 
Exposes simple methods like save(), findById(), and findByOrganizationId().
Contains only data operations.

---

## Entity Layer


Defines how java objects map to database tables, holding relationship flags. Represents the authoritative data structure. 

---

## Responsfe Layer

Converts entities into clean response objects to be sent back to the client, hiding internal fields. As conformed to docs/endpoints.md