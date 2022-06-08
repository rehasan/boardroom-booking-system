# boardroom-booking-system

Console application that provide a successful booking calendar as output, with bookings being grouped chronologically by day.

## PRE-REQUISITES

- Requires Java `16`

### Run locally

To clean and build,

```
./gradlew clean build
```

To start the console application run the following command:

File directory is located under root of this project, and the directory is named `storage`.

```
java -jar ./build/libs/boardroom-booking-system-1.0-SNAPSHOT.jar com.example.boardroombookingsystem.BoardroomBookingSystem -f batch-01.txt
```

### Test

To run tests:

```
./gradlew clean test
```