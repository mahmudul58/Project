#  Exam System (Java + MySQL terminal-based Project)

This is a terminal-based **Exam System** written in **Java**, using **JDBC** to connect to a **MySQL** database.

The system allows:
- Teachers to register/login, create exams, and view results.
- Students to register/login, take exams, and check their own or everyone's results.
- Exams are MCQ-based, timed, and saved with scores.

---

##  Features

###  For Teachers:
- Register and login
- Create exams with multiple-choice questions
- View all registered students
- View student results by exam code

###  For Students:
- Register and login
- Take exams (with live countdown timer)
- See their own result
- See everyone's result for a specific exam

---

##  Technologies Used
- **Java** (Console-based)
- **MySQL** (Database)
- **JDBC** (Java Database Connectivity)

---

##  Database Schema

Create the tables in your MySQL database:

```sql
-- Create teacher table
CREATE TABLE teacher (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    courseTitle VARCHAR(40) UNIQUE,
    courseCode VARCHAR(40) NOT NULL UNIQUE
);

-- Create student table
CREATE TABLE student (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    studentID VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

-- Create exams table
CREATE TABLE exams (
    exam_id VARCHAR(20) PRIMARY KEY,
    examTitle VARCHAR(50) NOT NULL,
    courseCode VARCHAR(40),
    marks INT NOT NULL,
    duration INT,
    FOREIGN KEY (courseCode) REFERENCES teacher(courseCode) ON DELETE CASCADE
);

-- Create questions table
CREATE TABLE questions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    qn TEXT NOT NULL,
    opt1 VARCHAR(255) NOT NULL,
    opt2 VARCHAR(255) NOT NULL,
    opt3 VARCHAR(255) NOT NULL,
    opt4 VARCHAR(255) NOT NULL,
    ans CHAR(5) NOT NULL,
    marks INT,
    exam_id VARCHAR(20),
    FOREIGN KEY (exam_id) REFERENCES exams(exam_id) ON DELETE CASCADE
);

-- Create results table
CREATE TABLE results (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    studentID VARCHAR(50) NOT NULL,
    score INT,
    exam_id VARCHAR(20),
    FOREIGN KEY (exam_id) REFERENCES exams(exam_id) ON DELETE CASCADE
);
```

---

##  Setup Instructions

### 1. Create Database
- Open MySQL .
- Create a database called `myProject`.
```sql
CREATE DATABASE myProject;
USE myProject;
```
- Then run the above table creation SQL.

### 2. Configure DB Connection
Inside `ExamSystem.java`, update:
```java
private static final String URL = "jdbc:mysql://localhost:3306/myProject";
private static final String USER = "root";
private static final String PASSWORD = "your_mysql_password";
```

### 3. Compile and Run
Make sure all `.java` files are in the same folder. Then use:
```bash
javac *.java
java ExamSystem
```

---

##  How It Works

###  Step-by-Step:
1. Program launches and asks:
   - Teacher Portal
   - Student Portal
2. Based on choice, user can login or register.
3. After login:
   - Teachers can create exams with questions.
   - Students can give exams using exam ID.
4. Exams are timed, and scores are auto-calculated.
5. Results are saved and viewable by both teacher and student.

---

- Ensure MySQL service is running.
- Use UTF-8 for special characters if needed.
- All data is stored in MySQL tables.
- Timer runs based on system clock.

---
