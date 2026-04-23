Smart Campus Sensor & Room Management API

This project is a RESTful API developed for the Smart Campus initiative, designed to manage:
•	Rooms across campus
•	Sensors deployed within those rooms
•	Historical sensor readings
The system is built using JAX-RS (javax.ws.rs) and deployed on a servlet container (e.g., Apache Tomcat). It follows RESTful principles, including proper resource structuring, HTTP status codes, and error handling.

Key Design Decisions:
•	In-memory storage using ConcurrentHashMap (no database as per requirements)
•	Singleton DataStore to persist data across requests
•	Sub-resource locator pattern for sensor readings
•	Custom exception handling with mappers
•	Logging filters for request/response tracking

How to Run the Project
Prerequisites:
•	Java JDK (8 or above)
•	Apache NetBeans (or any Java IDE)
•	Apache Tomcat server
Steps:
1.	Clone the repository:
2.	git clone https://github.com/YOUR_USERNAME/smart-campus-api.git
3.	Open the project in NetBeans
4.	Ensure Tomcat server is configured
5.	Build and run the project
6.	Access the API:
7.	http://localhost:8080/smart-campus-api/api/v1/

API Endpoints

1. Discovery
GET /api/v1/

2. Rooms
GET    /api/v1/rooms
POST   /api/v1/rooms
GET    /api/v1/rooms/{roomId}
DELETE /api/v1/rooms/{roomId}

3. Sensors
GET    /api/v1/sensors
GET    /api/v1/sensors?type=CO2
POST   /api/v1/sensors
GET    /api/v1/sensors/{sensorId}

4. Sensor Readings
GET  /api/v1/sensors/{sensorId}/readings
POST /api/v1/sensors/{sensorId}/readings

Sample curl Commands

1. Get all rooms
curl -X GET http://localhost:8080/smart-campus-api/api/v1/rooms

2. Create a new room
curl -X POST http://localhost:8080/smart-campus-api/api/v1/rooms \
-H "Content-Type: application/json" \
-d '{"id":"ENG-101","name":"Engineering Room","capacity":40}'

3. Delete a room (with validation)
curl -X DELETE http://localhost:8080/smart-campus-api/api/v1/rooms/LIB-301

4. Create a sensor
curl -X POST http://localhost:8080/smart-campus-api/api/v1/sensors \
-H "Content-Type: application/json" \
-d '{"id":"TEMP-002","type":"Temperature","roomId":"LIB-301"}'

5. Add a sensor reading
curl -X POST http://localhost:8080/smart-campus-api/api/v1/sensors/TEMP-001/readings \
-H "Content-Type: application/json" \
-d '{"value":25.3}'

Error Handling

The API uses custom exception mappers to ensure clean and meaningful responses:
Scenario	Status Code
Room has sensors (cannot delete)	409 Conflict
Invalid room reference in sensor	422 Unprocessable Entity
Sensor under maintenance	403 Forbidden
Unexpected errors	500 Internal Server Error
Example response:
{
  "error": "ROOM_NOT_EMPTY",
  "message": "Room LIB-301 still has 2 sensors"
}


Conceptual Answers

1. JAX-RS Resource Lifecycle
JAX-RS creates a new instance of a resource class per request (request-scoped). This means data stored in resource classes is not persistent. To maintain shared state, a singleton DataStore is used, ensuring data consistency across requests.

2. HATEOAS (Hypermedia)
HATEOAS allows clients to dynamically navigate the API through links provided in responses. This reduces dependency on static documentation and improves flexibility, as clients can discover available actions at runtime.

3. Returning IDs vs Full Objects
Returning only IDs reduces bandwidth but requires additional requests. Returning full objects improves usability but increases response size. This API returns full objects for better client experience.

4. DELETE Idempotency
The DELETE operation is idempotent because multiple identical requests result in the same outcome. Once a room is deleted, repeated DELETE requests will return 404 but will not change the system state further.

5. @Consumes Behavior
If a client sends data in a format other than application/json, JAX-RS will reject the request with a 415 Unsupported Media Type error, as it cannot process the payload.

6. QueryParam vs PathParam
Using @QueryParam for filtering (e.g., ?type=CO2) is preferred because:
•	It is optional
•	It keeps the resource structure clean
•	It supports flexible queries

7. Sub-Resource Locator Benefits
This pattern separates logic into smaller classes, improving readability and maintainability. It avoids large, complex resource classes and allows better scalability.

8. HTTP 422 vs 404
422 is more accurate when the request is syntactically correct but semantically invalid (e.g., referencing a non-existent room). 404 is used when the resource itself is not found.

9. Security Risks of Stack Traces
Exposing stack traces can reveal:
•	Internal class names
•	File structure
•	Framework details
This information can be exploited by attackers. The API prevents this by returning generic error messages.

10. Logging Filters vs Manual Logging
Using filters centralizes logging logic, avoids duplication, and ensures consistency across all endpoints. It also simplifies maintenance compared to adding logging in every resource method.

Video Demonstration
A video demonstration showcasing API functionality and testing via Postman is included in the Blackboard submission.

Repository
This project is hosted publicly on GitHub as required.


