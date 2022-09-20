# BoardRoom Booking Request
Company has an existing legacy system for employees to submit booking requests for meetings
in the boardroom

# Basic requirement

Java 11 or higher

Spring boot 2.7.3 Apache Maven 4.0.0 Postman or any other REST API Testing tool (Can use swagger documentation as well)

To run

clone https://github.com/snbiju/java-test-booking.git / download

go to java-test-booking

mvn clean install

mvn spring-boot:run

Submit Board Room Meeting Request (POST REQUEST)
http://localhost:8088/booking(POST REQUEST)

### Request body
<br>Text Format


     0900 1730 
     2020-01-18 10:17:06 EMP001
     2020-01-21 09:00 2
     2020-01-18 12:34:56 EMP002
     2020-01-21 09:00 2
     2020-01-18 09:28:23 EMP003
     2020-01-22 14:00 2
     2020-01-18 11:23:45 EMP004
     2020-01-22 16:00 1
     2020-01-15 17:29:12 EMP005
     2020-01-21 16:00 3
     2020-01-18 11:00:45 EMP006
     2020-01-23 16:00 1
     2020-01-15 11:00:45 EMP007
     2020-01-23 15:00 2  
    
    
### Response body


     [
    {
        "data": "2020-01-21",
        "bookings": [
            {
                "emp_id": "EMP001",
                "start_time": "09:00",
                "end_time": "11:00"
            }
        ]
    },
    {
        "data": "2020-01-22",
        "bookings": [
            {
                "emp_id": "EMP003",
                "start_time": "14:00",
                "end_time": "16:00"
            },
            {
                "emp_id": "EMP004",
                "start_time": "16:00",
                "end_time": "17:00"
            }
        ]
    },
    {
        "data": "2020-01-23",
        "bookings": [
            {
                "emp_id": "EMP007",
                "start_time": "15:00",
                "end_time": "17:00"
            }
        ]
    }
    ]


### Invalid Request 

    0900 1730
    2020-01-18 10:17:06 EMP001
    2020-01-21 09:00 

### Error Response

    Invalid boardroom booking request. Please enter valid input# java-test-booking
