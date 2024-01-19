package CollegeHelp.College_Mantra.exceptions;

public class InvalidIdException extends Throwable {
    public InvalidIdException(String given_id_is_invalid) {
        super(given_id_is_invalid);
    }
}
