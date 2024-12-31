package DebugLogger;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class DebugFormatter extends Formatter {
    // ANSI escape codes for colors


    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String GREEN = "\u001B[32m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREY = "\u001B[37m";
    private static final String WHITE = "\u001B[97m";

    @Override
    public String format(LogRecord record)
    {
        String levelColor = getLevelColor(record.getLevel().getName());
        String levelName = record.getLevel().getLocalizedName();
        String fullClassName = record.getSourceClassName();
        String methodName = record.getSourceMethodName();

        // Extract the simple class name (without package)
        String className = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);


        String message = formatMessage(record);
        // Return formatted log message with color and level
        return String.format
                ("%s[%s.%s] - %-13s: %s %s%n",
                levelColor,
                className,
                methodName,
                levelName,
                WHITE,
                message
                );
    }


    private String getLevelColor(String level) {
        return switch (level) {
            case "SEVERE" -> RED;           // Red for severe errors
            case "WARNING" -> YELLOW;       // Yellow for warnings
            case "INFO" -> GREEN;           // Green for informational messages
            case "CONFIG" -> CYAN;          // Cyan for configuration messages
            case "FINE", "FINER", "FINEST" -> BLUE; // Blue for fine-grained logs
            default -> RESET;               // Default terminal color
        };
    }



}



