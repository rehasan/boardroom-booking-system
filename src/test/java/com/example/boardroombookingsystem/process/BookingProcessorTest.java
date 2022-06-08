package com.example.boardroombookingsystem.process;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

import com.example.boardroombookingsystem.io.FileReader;
import com.example.boardroombookingsystem.io.TxtFileReaderImpl;
import com.example.boardroombookingsystem.io.TxtFileWriterImpl;
import com.example.boardroombookingsystem.model.Result;
import com.example.boardroombookingsystem.model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class BookingProcessorTest {
    private BookingProcessor bookingProcessor;
    private FileReader fileReader;
    private TxtFileWriterImpl txtFileWriter;
    private Path inputFile;
    private Path outputFile;

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        String inputFileContent = "0900 1730\n" +
            "2011-03-17 10:17:06 EMP001\n" +
            "2011-03-21 09:00 2\n" +
            "2011-03-16 12:34:56 EMP002\n" +
            "2011-03-21 09:00 2\n"+
            "2011-03-16 09:28:23 EMP003\n" +
            "2011-03-22 14:00 2\n" +
            "2011-03-17 10:17:06 EMP004\n" +
            "2011-03-22 16:00 1\n"+
            "2011-03-15 17:29:12 EMP005\n" +
            "2011-03-21 16:00 3";
        this.inputFile = Paths.get("/test/batch-01.txt");
        this.outputFile = Paths.get("/test/processed-batch-01.txt");
        FileReader fileReader = new TxtFileReaderImpl(this.inputFile);
        TxtFileWriterImpl txtFileWriter = new TxtFileWriterImpl(this.outputFile);

        // Spy TxtFileReaderImpl: stub createInputStream
        this.fileReader = spy(fileReader);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputFileContent.getBytes());
        this.fileReader.setInputStream(byteArrayInputStream);
        doNothing().when(this.fileReader).createInputStream();

        // Spy TxtFileWriterImpl: stub createOutputStream
        this.txtFileWriter = spy(txtFileWriter);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        this.txtFileWriter.setOutputStream(byteArrayOutputStream);
        doNothing().when(this.txtFileWriter).createOutputStream();
    }

    @Test
    public void shouldBeAbleToProcess() {
        this.bookingProcessor = new BookingProcessor(this.fileReader, this.txtFileWriter);
        Result<String> output = this.bookingProcessor.process();

        assertNotNull(output);
        assertEquals(Status.SUCCESS, output.getStatus());
        assertEquals("", output.getMessage());
        assertTrue(output.getValue().isPresent());
        assertEquals(this.outputFile.toString(), output.getValue().get());
    }

    @Test
    public void shouldNotBeAbleToProcessWhenFailsWithException() throws FileNotFoundException {
        doThrow(FileNotFoundException.class).when(this.txtFileWriter).createOutputStream();
        this.bookingProcessor = new BookingProcessor(this.fileReader, this.txtFileWriter);
        Result<String> output = this.bookingProcessor.process();

        assertNotNull(output);
        assertEquals(Status.FAIL, output.getStatus());
        assertTrue(output.getMessage().contains("BookingProcessor: failed, message"));
        assertEquals(Optional.empty(), output.getValue());
    }
}
