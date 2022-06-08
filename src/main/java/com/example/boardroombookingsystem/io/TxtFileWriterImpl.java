package com.example.boardroombookingsystem.io;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.boardroombookingsystem.model.Meeting;
import com.example.boardroombookingsystem.model.Result;
import com.example.boardroombookingsystem.model.Status;

public class TxtFileWriterImpl {
    private OutputStream outputStream;
    private Path outputFile;

    public TxtFileWriterImpl(final Path outputFile) {
        this.outputFile = outputFile;
    }

    public void createOutputStream() throws FileNotFoundException {
        this.outputStream = new FileOutputStream(this.outputFile.toFile());
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public Result<String> write(Map<LocalDate, List<Meeting>> meetings) {
        Result<String> output;

        try (Writer outputStreamReader = new OutputStreamWriter(this.outputStream);
             BufferedWriter bufferedWriter = new BufferedWriter(outputStreamReader)) {
            for (Map.Entry<LocalDate, List<Meeting>> entry : meetings.entrySet()) {
                bufferedWriter.write(entry.getKey().toString());
                bufferedWriter.newLine();

                for (Meeting meeting: entry.getValue()) {
                    bufferedWriter.write(
                        String.format("%s %s %s",
                            meeting.getMeetingStartDateTime().toLocalTime(),
                            meeting.getMeetingEndDateTime().toLocalTime(),
                            meeting.getEmployeeId())
                    );
                    bufferedWriter.newLine();
                }
            }

            outputStreamReader.flush();

            output = new Result<>(Optional.of(this.outputFile.toString()));
        } catch (IOException e) {
            output = new Result<>(Status.IO_ERROR,
                "TxtFileWriterImpl: IO error occurred during file writing, message " + e.getMessage());
        }

        return output;
    }
}
