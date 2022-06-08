package com.example.boardroombookingsystem.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.boardroombookingsystem.helper.Converter;
import com.example.boardroombookingsystem.model.Boardroom;
import com.example.boardroombookingsystem.model.FileExtensionType;
import com.example.boardroombookingsystem.model.Meeting;
import com.example.boardroombookingsystem.model.Result;
import com.example.boardroombookingsystem.model.Status;

public class TxtFileReaderImpl implements FileReader {
    private Result<Boardroom> boardroom;
    private InputStream inputStream;
    private Path inputFile;

    public TxtFileReaderImpl(final Path inputFile) {
        this.inputFile = inputFile;
    }

    @Override
    public FileExtensionType getFileExtensionType() {
        return FileExtensionType.TXT;
    }

    @Override
    public void createInputStream() throws FileNotFoundException {
        this.inputStream = new FileInputStream(this.inputFile.toFile());
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void read() {
        Result<Boardroom> output;
        Boardroom boardroom = null;
        Meeting meeting = null;
        List<Meeting> meetings = new ArrayList<>();

        try (Reader inputStreamReader = new InputStreamReader(this.inputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            int i = 0;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // Skip the following if line is empty
                if (line.isEmpty()) {
                    continue;
                }

                // Split the line with space
                String[] inners = this.getInners(line, " ");

                if (i == 0) {
                    // Sentences with [office hour start time, in the format HHmm] [office hour end time, in the format HHmm]
                    boardroom = new Boardroom(Converter.stringToTime("HHmm", inners[0]), Converter.stringToTime("HHmm", inners[1]));
                } else if (i % 2 != 0) {
                    // Sentences with [request submission time, in the format yyyy-MM-dd HH:mm:ss] [employee id]
                    String requestSubmissionTime = inners[0] + ' ' + inners[1];
                    meeting = new Meeting(Converter.stringToDateTime("yyyy-MM-dd HH:mm:ss", requestSubmissionTime), inners[2]);
                } else {
                    // Sentences with [meeting start time, in the format yyyy-MM-dd HH:mm:ss] [meeting duration in hours]
                    String meetingStartTime = inners[0] + ' ' + inners[1];
                    if (meeting != null) {
                        meeting.setMeetingStartDateTime(Converter.stringToDateTime("yyyy-MM-dd HH:mm", meetingStartTime));
                        meeting.setMeetingDuration(Integer.parseInt(inners[2]));

                        // Add it to boardroom meeting list
                        meetings.add(meeting);
                    }
                }

                i++;
            }

            // Add meeting list to boardroom
            if (boardroom != null) {
                boardroom.setMeetings(meetings);
            }

            output = new Result<>(Optional.of(boardroom));
        } catch (IOException e) {
            output = new Result<>(Status.IO_ERROR,
                "TxtFileReaderImpl: IO error occurred during file reading, message " + e.getMessage());
        }

        this.boardroom = output;
    }

    @Override
    public Result<Boardroom> getBoardroom() {
        return this.boardroom;
    }

    private String[] getInners(String line, String regex) {
        String[] inners = line.split(regex);
        Arrays.stream(inners).map(inner -> inner.trim());

        return inners;
    }
}
