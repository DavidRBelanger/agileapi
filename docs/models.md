# Models

1. Organization
2. User
3. Project
4. Sprint
5. Task
---

## ORGANIZATION

An `Organization` is a model representing any organization that uses this API. All the following models are dependent on the organization.
The listed Organization is provided in the form of its `slug` in the URI, or its ID but the organization access permission is still defined by the JWT header validation.

Organizations have the following members:

`id: Long` an identifier for the organization, required in the URI header for access.

`name: String` string name for the organization, in the format of "Example Company".

`createdAt: LocalDateTime` a datetime object for when the organization was originally created.

`slug: String` slug representation of the organization's name, in the format of "example-company"

---

## USER

A user is a model representing a person user. Users are per `Organization`, so one individual person may have multiple user accounts. 

`id: Long` an identifier for the user.

`email: String` a user's email.

`passwordHash: String` a user's encrypted password.

`name: String` a user's name.

`organization: Organization` a reference to the `Organization` object.

---

## PROJECT

A project is a model representing an Agile Project. Projects are per organization.

`id: Long` an identifier for the project.

`name: String` the name of the project.

`description: String (not required)` the description of the project, optional.

`organization: Organization` a reference to the `Organization` object.

---

## SPRINT

`id: Long` an identifier for the sprint.

`name: String` the name of the sprint.

`startDate: LocalDateTime` a datetime object for when the sprint started or when it will start.

`endDate: LocalDateTime` a datetime object for when the sprint ended or when it will end.

`project: Project` a reference to the `Project` object.

---

## TASK

`id: Long` an identifier for the task.

`title: String` the title of the task.

`description: String (not required)` the description of the task, optional.

`status: Enum: TODO, IN_PROGRESS, DONE, BLOCKED` the status of the task.

`priority: int` the priority of the task, bounded to 1-5

`user: User` reference to the `User` object for this task.

`sprint: Sprint` reference to the `Sprint` object for this task.



