# Questions

Here we have 3 questions related to the code base for you to answer. It is not about right or wrong, but more about what's the reasoning behind your decisions.

1. In this code base, we have some different implementation strategies when it comes to database access layer and manipulation. If you would maintain this code base, would you refactor any of those? Why?

Currently there are mixed database access implementations like direct entity manipulation, repository patterns. For maintainability and consistency, I would recommend refactoring to use a single pattern, such as the repository pattern 
Reasons for refactoring:

Consistency: A unified approach makes the codebase easier to understand and maintain.
Testability: Repository patterns are easier to mock and test in isolation.
Separation of concerns: Keeps business logic separate from data access logic.
Scalability: A consistent pattern makes it easier to extend or optimize data access in the future.
I would rather refactor all to use repositories, ensuring all database operations go through a clear abstraction layer. This reduces technical debt and improves long-term maintainability.

```
----
2. When it comes to API spec and endpoints handlers, we have an Open API yaml file for the `Warehouse` API from which we generate code, but for the other endpoints - `Product` and `Store` - we just coded directly everything. What would be your thoughts about what are the pros and cons of each approach and what would be your choice?

**Answer:**
Pros:
Easier integration with Swagger UI and testing
Ensures API contract consistency and documentation.
Enables automated client/server code generation, reducing manual errors.
Facilitates collaboration between frontend/backend teams.
Cons:
Requires maintaining the spec file.
Generated code can be verbose or less flexible.
Custom logic may need manual extension.


Pros:
Maximum flexibility for custom business logic.
No need to maintain a spec file.
Direct control over implementation details.
Cons:
Risk of undocumented or inconsistent APIs.
Manual documentation and testing required.

```
----
3. Given the need to balance thorough testing with time and resource constraints, how would you prioritize and implement tests for this project? Which types of tests would you focus on, and how would you ensure test coverage remains effective over time?

**Answer:**
To balance thorough testing with time and resource constraints, I would:

Focus on unit tests for core business logic and REST endpoints, covering critical workflows and validation.
Add tests for edge cases and error handling, especially for database and external service interactions.
Implement integration tests for modules interacting with the database or other services, ensuring end-to-end correctness.
Cover regression tests for previously reported bugs and high-risk areas.

To keep coverage effective in future:

Use code coverage tools (like JaCoCo) and use SonarQube to monitor and identify gaps.
Automate test runs in CI/CD pipelines for every commit and pull request.
Regularly review and update tests as business logic evolves.
