Finance Dashboard Backend
This is my first backend project. I built a REST API using Spring Boot that helps manage financial records for a dashboard. Different users have different permissions based on their role.
What I Used to Build This
I used Java 17 for coding. Spring Boot 3.2.5 made building APIs easier. MySQL is my database to store users and records. Maven helped me manage all the dependencies I needed. I used Postman to test all my APIs. I also added JWT for login and authentication.

How to Run This on Your Machine
First make sure you have Java, MySQL and IntelliJ installed on your computer.

Step 1 - Open MySQL and create the database by running this command:
CREATE DATABASE finance_db;

Step 2 - Go to src/main/resources/application.properties file and change the password to match your MySQL password. Look for spring.datasource.password and put your password there.

Step 3- Open IntelliJ and click the green play button to run FinanceApplication.java

Step 4- Wait until you see "Started FinanceApplication" in the console. That means the server is running at http://localhost:8080

Understanding the 3 Roles

I created 3 types of users in this system.

ADMIN  is the most powerful user. Admin can create records, update them, delete them and also manage other users. Basically admin can do everything.

ANALYST  is a middle level user. Analyst can view all records and also access the dashboard which shows financial summaries like total income and expenses.

VIEWER  is the most restricted user. Viewer can only see the list of records. They cannot create anything, edit anything or see the dashboard. They just look at data.

How Authentication Works

At first I was passing role in the URL like ?role=ADMIN but that was not secure. So I added proper login with JWT tokens.

Here is how it works now:

First you need to create a user account. Then you login with email and password. The system gives you back a token. This token is like an ID card. You need to send this token with every API request. The system reads your role from the token so you don't need to write ?role=ADMIN anymore.
To login:  Send a POST request to /api/auth/login with your email and password. You will get back a token.

To use the token: Add this header to all your requests - Authorization: Bearer your_token_here

The token works for 10 hours. If it expires just login again to get a new one.

List of All APIs I Built

User Management APIs

POST /api/users - Use this to create a new user. When a user is created their status is automatically set to ACTIVE.

GET /api/users - Returns the list of all users in the system. Only ADMIN can access this.

GET /api/users/{id}- Returns details of one specific user by their ID.

PATCH /api/users/{id}/deactivate - Changes the user status from ACTIVE to INACTIVE. Only ADMIN can do this.

PATCH /api/users/{id}/role?role=ANALYST - Updates the role of a user to whatever role you pass. Only ADMIN can do this.


Authentication API

POST /api/auth/login - Login with your email and password. Returns a JWT token that you need for all other APIs.


Financial Record APIs

POST /api/records - Creates a new financial record. Only ADMIN can do this. Amount, type, category and date are all required fields. If any of these are missing the API will return an error.

GET /api/records - Returns all records that have ACTIVE status. All three roles can access this.

PUT /api/records/{id} - Updates an existing record. Only ADMIN can do this.

DELETE /api/records/{id} - This does not permanently delete the record from database. It just changes the status to DELETED so it gets hidden from all listing responses. This is called soft delete. Only ADMIN can do this.


Dashboard APIs

GET /api/dashboard/summary- Returns total income, total expense and net balance. Only ADMIN and ANALYST can access this. VIEWER gets a 403 Forbidden error.

GET /api/dashboard/recent- Returns the last 5 financial records added to the system. Only ADMIN and ANALYST can access this.

GET /api/dashboard/category - Returns the total amount spent or earned per category. For example Food: 1500, Salary: 5000. Only ADMIN and ANALYST can access this.


Sample Request and Response

Let me show you a complete example of how to use the system.

Step 1 - Create a user

Send a POST request to http://localhost:8080/api/users

Body should be JSON like this:

{
    "name": "John Admin",
    "email": "john@test.com",
    "password": "mypassword",
    "role": "ADMIN",
    "status": "ACTIVE"
}


Step 2 - Login to get token

Send a POST request to http://localhost:8080/api/auth/login

Body:

{
    "email": "john@test.com",
    "password": "mypassword"
}

You will get a response like this:

{
    "token": "eyJhbGciOiJIUzI1NiIs...",
    "email": "john@test.com",
    "role": "ADMIN",
    "message": "Login successful!"
}

Copy that token value. You will need it for the next step.

Step 3 - Create an income record

Send a POST request to http://localhost:8080/api/records

But this time you need to add a header. Add Authorization header with value Bearer followed by your token. Like this:

Authorization: Bearer eyJhbGciOiJIUzI1NiIs...

Then the body:

{
    "amount": 5000,
    "type": "INCOME",
    "category": "Salary",
    "date": "2026-04-01",
    "notes": "April salary"
}


Step 4 - Check dashboard summary

Send a GET request to http://localhost:8080/api/dashboard/summary

Add the same Authorization header with your token.

You will get a response like:

{
    "totalIncome": 5000.0,
    "totalExpense": 1500.0,
    "netBalance": 3500.0
}


Filtering Records

I also added a simple filtering feature to make it easier to find specific records. Instead of looking at all data, you can filter based on type or category.


Filter by type - You can see only income or expense records:

GET /api/records?type=INCOME

GET /api/records?type=EXPENSE


Filter by category- You can filter by category like Salary or Food:

GET /api/records?category=Salary

GET /api/records?category=Food


How it works - If you don't give any filter, it shows all records. If you give type, it shows only that type. If you give category, it shows only that category.


Error Responses You Might See

Sometimes things go wrong. Here are the common errors you might see:

401 Unauthorized - This means you forgot to send the token or your token is invalid or expired. Just login again to get a new token.

403 Forbidden - This means your role does not have permission for this action. For example if you are a VIEWER and try to create a record, you will get this error.

404 Not Found - The API endpoint you are trying to reach does not exist or the record ID you passed is wrong.


Things I Learned While Building This

Before this project I did not know how REST APIs worked at all. I used to think backend development was very complicated. But after building this project, I finally understand how requests come in, how the controller handles them, how the service processes the logic and how the repository saves data to the database. It all makes sense now.
I also learned what role based access control means. It was interesting to see how just checking the role value can completely change what a user is allowed to do. One small if condition can either allow or block an entire operation.
Soft delete was something new for me. Instead of removing data permanently I just mark it as deleted. This is safer because the data can always be recovered if needed. I think this is how real applications handle deletions.
Adding JWT authentication was the hardest part honestly. I had to learn about tokens, how to generate them, how to validate them on every request, and how to extract the user role from them. I faced many errors but I kept fixing them one by one. Once it started working, I felt really good because now the system is actually secure.


Project Structure

Here is how I organized my code:


controller folder - Contains all the API endpoint files

-AuthController.java handles login and token generation

-UserController.java receives user related API requests

-RecordController.java receives record related API requests

-DashboardController.java receives dashboard related API requests

service folder - Contains the business logic
- UserService.java contains logic for user operations
- RecordService.java contains logic for records and dashboard


repository folder - Handles database queries

- UserRepository.java handles database queries for users

- RecordRepository.java handles database queries for records


model folder - Defines the data structure

- User.java defines what a user looks like in database

- Record.java defines what a record looks like in database


security folder - Handles authentication

- JwtUtil.java creates and validates JWT tokens

- JwtFilter.java checks every request for valid token

- SecurityConfig.java configures which URLs are protected




