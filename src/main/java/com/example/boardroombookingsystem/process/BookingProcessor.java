package com.example.boardroombookingsystem.process;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.boardroombookingsystem.io.FileReader;
import com.example.boardroombookingsystem.io.TxtFileWriterImpl;
import com.example.boardroombookingsystem.model.Boardroom;
import com.example.boardroombookingsystem.model.Meeting;
import com.example.boardroombookingsystem.model.Result;
import com.example.boardroombookingsystem.model.Status;

public class BookingProcessor {
    private final FileReader fileReader;
    private final TxtFileWriterImpl txtFileWriter;

    public BookingProcessor(final FileReader fileReader, final TxtFileWriterImpl txtFileWriter) {
        this.fileReader = fileReader;
        this.txtFileWriter = txtFileWriter;
    }

    public Result<String> process() {
        Result<String> output = null;

        try {
            // File reader create input stream and read
            this.fileReader.createInputStream();
            this.fileReader.read();

            Result<Boardroom> result = this.fileReader.getBoardroom();
            if (result != null && result.getStatus() == Status.SUCCESS && result.getValue().isPresent()) {
                Map<LocalDateTime, Meeting> processedMeetings = new HashMap<>();

                LocalTime officeStartTime = result.getValue().get().getStartTime();
                LocalTime officeEndTime = result.getValue().get().getEndTime();

                // Order the bookings by request submission date time
                List<Meeting> meetings =
                    result.getValue().get().getMeetings().stream()
                        .sorted(Comparator.comparing(Meeting::getRequestSubmissionDateTime))
                        .collect(Collectors.toList());

                // Process the bookings
                for (Meeting meeting : meetings) {
                    // Skip meeting that is out of office hours
                    if (meeting.getMeetingStartDateTime().toLocalTime().isBefore(officeStartTime)
                        || meeting.getMeetingEndDateTime().toLocalTime().isAfter(officeEndTime)) {
                        continue;
                    }

                    // Skip if there is an existing meeting already
                    if (processedMeetings.containsKey(meeting.getMeetingStartDateTime())) {
                        continue;
                    }

                    processedMeetings.put(meeting.getMeetingStartDateTime(), meeting);
                }

                // Order the bookings by meeting start date time
                // THEN, Group the bookings by meeting start date
                var processedMeetingsGroupByStartDate =
                    processedMeetings.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .collect(Collectors.groupingBy(
                            e -> e.getKey().toLocalDate(),
                            Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                        ));

                // Order the bookings by meeting start date
                var processedMeetingsOrderByStartDate =
                    processedMeetingsGroupByStartDate.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));

                // File Writer set output stream writing to the a file
                // Output file path = output
                this.txtFileWriter.createOutputStream();
                output = this.txtFileWriter.write(processedMeetingsOrderByStartDate);
            }
        } catch (Exception e) {
            output = new Result<>(Status.FAIL,
                "BookingProcessor: failed, message " + e.getMessage());
        }

        return output;
    }
}
