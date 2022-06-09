# boardroom-booking-system

Console application that provide a successful booking calendar as output, with bookings being grouped chronologically by
day.

## PRE-REQUISITES

- Requires Java `16`

### Run locally

To clean and build,

```
./gradlew clean build
```

To start the console application run the following command:

```
java -jar ./build/libs/boardroom-booking-system-1.0.0-SNAPSHOT.jar com.example.boardroombookingsystem.BoardroomBookingSystem -f <filename>
```

File directories are located under root of this project, and the root directory is named `storage`.

- [input](./storage/input) directory
- [output](./storage/output) directory

You can upload a file under the [input](./storage/input) directory and a file with `processed-` prefix will be exported
in the [output](./storage/output) directory. There is an existing file named `batch-01.txt` to try out the
functionality.

An example command,

```
java -jar ./build/libs/boardroom-booking-system-1.0.0-SNAPSHOT.jar com.example.boardroombookingsystem.BoardroomBookingSystem -f batch-01.txt
```

### Test

To run tests:

```
./gradlew clean test
```