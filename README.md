# Digilytics

API Endpoints

> /register

Registers valid users and return error csv filepath in case of any errors

Response

```
{
   "no_of_rows_parsed": "3",
   "no_of_rows_failed": "2",
   "error_file_url": "error_2019-46-1411:46:14"
}
```

> /download/{filepath}
Response - returns error csv file

> Steps to run the project
```
1) It's a spring boot application with maven, to resolve all the dependency
just open the project in any IDE like Intellij, Eclipse etc

2) application.properties contains the mysql connection proerties

3) I have provided the sql file named digilytics.sql with the schema and some dummy data, 
just run the sql to create your database in mysql.

4) I have used CSV parser and open CSV as third party dependency to read and write CSV

5) Used JUnit for the testcases.

6) User Hibernate as Entity Manager for the database connection and database querying.

7) I have provided the Postman collection which you can use to test both the endpoints 
and also provided the sample CSV file named sample.csv in the root directory.
```
[Postman Collection](https://www.getpostman.com/collections/a21a008b116fa6901f9e)
