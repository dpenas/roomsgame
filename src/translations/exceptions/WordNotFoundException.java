package translations.exceptions;

@SuppressWarnings("serial")
public class WordNotFoundException extends Exception {
    public WordNotFoundException(String message) {
        super(message);
    }
}
