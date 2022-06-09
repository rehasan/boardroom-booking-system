package com.example.boardroombookingsystem;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.boardroombookingsystem.io.FileReader;
import com.example.boardroombookingsystem.io.TxtFileReaderImpl;
import com.example.boardroombookingsystem.io.TxtFileWriterImpl;
import com.example.boardroombookingsystem.model.CommandArg;
import com.example.boardroombookingsystem.model.Result;
import com.example.boardroombookingsystem.model.Status;
import com.example.boardroombookingsystem.process.BookingProcessor;
import com.example.boardroombookingsystem.process.CommandLineProcessor;
import org.apache.commons.io.FilenameUtils;

/**
 * Your processing system must process input as text. The first line of the input text represents the company office hours,
 * in 24 hour clock format, and the remainder of the input represents individual booking requests. Each booking request is in the following format.
 * [request submission time, in the format YYYY-MM-DD HH:MM:SS] [employee id]
 * [meeting start time, in the format YYYY-MM-DD HH:MM] [meeting duration in hours]
 * <p>
 * CLI: ./[command] -f batch-01.txt
 */
public class BoardroomBookingSystem {
    public static final String STORAGE_OUTPUT_PATH = "storage/output/";
    public static final String STORAGE_OUTPUT_FILE_PREFIX = "processed-";

    public static void main(final String[] args) {
        // Extract the args
        CommandLineProcessor commandLineProcessor = new CommandLineProcessor(args);
        Result<Map<String, Object>> output = commandLineProcessor.process();
        Map<String, Object> argValues = output.getValue().orElse(new HashMap<>());

        if (output.getStatus().equals(Status.COMMAND_LINE_PARSE)
            && (args == null || args.length < 2)) {
            System.out.println("Args are required. Exception: " + output.getMessage());
        } else {
            Path inputPath = (Path) argValues.get(CommandArg.OPT_F.getValue());
            String filename = inputPath.getFileName().toString();
            String fileExt = FilenameUtils.getExtension(filename).toLowerCase();
            Path outputPath = Paths.get(STORAGE_OUTPUT_PATH + STORAGE_OUTPUT_FILE_PREFIX + filename).toAbsolutePath();

            List<FileReader> fileReaderHandlers = new ArrayList<>();
            fileReaderHandlers.add(new TxtFileReaderImpl(inputPath));
            FileReader fileReader = fileReaderHandlers.stream().filter(handler ->
                handler.getFileExtensionType().name().toLowerCase().equals(fileExt)).findFirst().orElseThrow();

            BookingProcessor bookingProcessor = new BookingProcessor(fileReader, new TxtFileWriterImpl(outputPath));

            Result<String> result = bookingProcessor.process();

            System.out.println("--------------------------------------------------");
            System.out.println("Process Status: " + result.getStatus().name());
            System.out.println("Process Value: " + (result.getValue().isPresent() ? result.getValue().get() : ""));

            if (!result.getMessage().isEmpty()) {
                System.out.println("Process Message: " + result.getMessage());
            }

            System.out.println("##################################################");
        }
    }
}
