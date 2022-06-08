package com.example.boardroombookingsystem.process;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.boardroombookingsystem.model.CommandArg;
import com.example.boardroombookingsystem.model.Result;
import com.example.boardroombookingsystem.model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandLineProcessorTest {
    private CommandLineProcessor commandLineProcessor;
    private String[] args;

    @BeforeEach
    public void setUp() {
        this.args = new String[] {"-".concat(CommandArg.OPT_F.getValue()), "batch-01.txt"};
    }

    @Test
    public void shouldBeAbleToProcess() {
        this.commandLineProcessor = new CommandLineProcessor(this.args);
        Result<Map<String, Object>> output = this.commandLineProcessor.process();
        Map<String, Object> argValues = output.getValue().orElse(new HashMap<>());

        assertNotNull(output);
        assertEquals(Status.SUCCESS, output.getStatus());
        assertEquals("", output.getMessage());
        assertTrue(argValues.get(this.args[0].replace("-", "")).toString().contains(this.args[1]));
    }

    @Test
    public void shouldNotBeAbleToProcessWhenRequiredArgsNotProvided() {
        this.commandLineProcessor = new CommandLineProcessor(new String[]{});
        Result<Map<String, Object>> output = this.commandLineProcessor.process();

        assertNotNull(output);
        assertEquals(Status.COMMAND_LINE_PARSE, output.getStatus());
        assertTrue(output.getMessage().contains("CommandLineProcessor: commandline args are missing"));
    }
}
