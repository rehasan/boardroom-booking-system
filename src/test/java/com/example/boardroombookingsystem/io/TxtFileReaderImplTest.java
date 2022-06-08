package com.example.boardroombookingsystem.io;

import java.io.ByteArrayInputStream;

import com.example.boardroombookingsystem.model.Boardroom;
import com.example.boardroombookingsystem.model.FileExtensionType;
import com.example.boardroombookingsystem.model.Result;
import com.example.boardroombookingsystem.model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

public class TxtFileReaderImplTest {
    private ByteArrayInputStream byteArrayInputStream;
    private FileReader fileReader;

    @BeforeEach
    public void setUp() {
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

        this.fileReader = new TxtFileReaderImpl(any());
        this.byteArrayInputStream = new ByteArrayInputStream(inputFileContent.getBytes());
        this.fileReader.setInputStream(this.byteArrayInputStream);
    }

    @Test
    public void shouldBeAbleToGetBoardroomData() {
        this.fileReader.read();
        Result<Boardroom> output = this.fileReader.getBoardroom();

        assertNotNull(output);
        assertEquals(FileExtensionType.TXT, this.fileReader.getFileExtensionType());
        assertEquals(Status.SUCCESS, output.getStatus());
        assertEquals("", output.getMessage());
        assertTrue(output.getValue().isPresent());
        assertEquals(5, output.getValue().get().getMeetings().size());
    }
}
