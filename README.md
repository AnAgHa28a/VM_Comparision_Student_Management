  MySQL should run on localhost:3306
    Execute the following scripts to create and load the master tables
    01_CreateUser.sql - creates a user to use to connect to the database
    02_CreateDatabaseandTables.sql - creates the database adn requred tables
    03_student_insert_script.sql - script to insert records to student table
    04_course_insert_script.sql - script to insert records to course table
    05_enrollments_insert_script.sql - script to create records to enrollments table based on records in student and course table

List of APIs
    curl -X POST http://localhost:8080/api/marks/insert-marks : to insert 28800 records into marks table
    curl -X POST http://localhost:8080/api/marks/calculate-marks: to calcuate total marks and grade for 28800 records
    curl -X POST http://localhost:8080/api/marks/calculate-sgpa: to calculate sgpa semester wise for each student based on the grades in marks table (processes 5760 records
    curl -X DELETE http://localhost:8080/api/marks/delete-all : to delete all marks and gpa records

    
