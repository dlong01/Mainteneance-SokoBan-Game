package game;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Class to handle the logger for the game.
 */
public class GameLogger extends Logger {

    private static Logger logger = Logger.getLogger("GameLogger");
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private Calendar calendar = Calendar.getInstance();

    /**
     * Constructs a new Logger called GameLogger. Creates a directory in the same directory as the user is
     * loading the program from and creates a new .log file in that directory.
     * @throws  IOException Caught in {@link StartMeUp#StartMeUp(InputStream, boolean)}
     */
    public GameLogger() throws IOException {
        super("GameLogger", null);

        File directory = new File(System.getProperty("user.dir") + "/" + "logs");
        directory.mkdirs();

        FileHandler fh = new FileHandler(directory + "/" + StartMeUp.GAME_NAME + ".log");
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
    }

    /**
     * Formats a message into Time -- Message
     * @param   message The message to be formatted
     * @return  The formatted message
     */
    private String createFormattedMessage(String message) {
        return dateFormat.format(calendar.getTime()) + " -- " + message;
    }

    /**
     * Adds the message to the log as info.
     * Passes the message to {@link #createFormattedMessage(String)} before adding to the log.
     * @param   message The message to be added.
     */
    public void info(String message) {
        logger.info(createFormattedMessage(message));
    }

    /**
     * Adds the message to the log as a warning.
     * Passes the message to {@link #createFormattedMessage(String)} before adding to the log.
     * @param   message The message to be added.
     */
    public void warning(String message) {
        logger.warning(createFormattedMessage(message));
    }

    /**
     * Adds the message to the log as a severe message.
     * Passes the message to {@link #createFormattedMessage(String)} before adding to the log.
     * @param   message The message to be added.
     */
    public void severe(String message) {
        logger.severe(createFormattedMessage(message));
    }
}