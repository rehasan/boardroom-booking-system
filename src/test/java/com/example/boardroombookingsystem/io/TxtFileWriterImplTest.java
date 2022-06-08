package com.example.boardroombookingsystem.io;

import java.io.ByteArrayOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.boardroombookingsystem.model.Meeting;
import com.example.boardroombookingsystem.model.Result;
import com.example.boardroombookingsystem.model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TxtFileWriterImplTest {
    private ByteArrayOutputStream byteArrayOutputStream;
    private TxtFileWriterImpl txtFileWriter;
    private Path outputFile;

    @BeforeEach
    public void setUp() {
        this.outputFile = Paths.get("/test/processed-batch-01.txt");
        this.txtFileWriter = new TxtFileWriterImpl(this.outputFile);
        this.byteArrayOutputStream = new ByteArrayOutputStream();
        this.txtFileWriter.setOutputStream(this.byteArrayOutputStream);
    }

    @Test
    public void shouldBeAbleToWriteData() {
        LinkedHashMap<LocalDate, List<Meeting>> meetings = new LinkedHashMap<>();
        meetings.put(
            LocalDate.of(2011, 3, 21),
            List.of(
                new Meeting(LocalDateTime.of(2011,3,17,10,17,6), "EMP001",
                    LocalDateTime.of(2011,3,21,9,0), 2),
                new Meeting(LocalDateTime.of(2011,3,16,12,34,56), "EMP002",
                    LocalDateTime.of(2011,3,21,9,0), 2)
            ));
        Result<String> output = this.txtFileWriter.write(meetings);

        assertNotNull(output);
        assertEquals(Status.SUCCESS, output.getStatus());
        assertEquals("", output.getMessage());
        assertTrue(output.getValue().isPresent());
        assertEquals(this.outputFile.toString(), output.getValue().get());
    }
}
