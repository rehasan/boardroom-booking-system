package com.example.boardroombookingsystem.process;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.example.boardroombookingsystem.model.CommandArg;
import com.example.boardroombookingsystem.model.Result;
import com.example.boardroombookingsystem.model.Status;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandLineProcessor {
    public static final String STORAGE_INPUT_PATH = "storage/input/";
    private final String[] args;

    public CommandLineProcessor(final String[] args) {
        this.args = args;
    }

    public Result<Map<String, Object>> process() {
        Result<Map<String, Object>> output;
        CommandLine commandLine;
        Options options = new Options();
        Map<String, Object> argValues = new HashMap<>();

        Option optF = Option.builder(CommandArg.OPT_F.getValue())
            .argName("filename")
            .hasArg()
            .required()
            .desc("Please include a -f parameter for the filename to process").build();

        CommandLineParser parser = new DefaultParser();

        options.addOption(optF);

        String header = "--------------------------------------------------"
            + "\nThe above arguments may be in any order.";
        String footer = "##################################################";
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar ./build/libs/boardroom-booking-system-1.0-SNAPSHOT.jar com.example.boardroombookingsystem.BoardroomBookingSystem",
            header, options, footer, true);

        try {
            commandLine = parser.parse(options, this.args);

            if (commandLine.hasOption(CommandArg.OPT_F.getValue())) {
                String value = commandLine.getOptionValue(CommandArg.OPT_F.getValue());
                Path path = Paths.get(STORAGE_INPUT_PATH + value).toAbsolutePath();

                argValues.put(CommandArg.OPT_F.getValue(), path);
                System.out.println("Option f is present. The value is: " + path.toString());
            }

            output = new Result<>(Optional.of(argValues));
        } catch (MissingOptionException e) {
            output = new Result<>(Status.COMMAND_LINE_PARSE,
                "CommandLineProcessor: commandline args are missing, message " + e.getMessage());
        } catch (ParseException e) {
            output = new Result<>(Status.COMMAND_LINE_PARSE,
                "CommandLineProcessor: failed to parse commandline args, message " + e.getMessage());
        }

        return output;
    }
}