package CollegeHelp.College_Mantra.exceptions;

public class WrongOTP extends Throwable {
    public WrongOTP(String incorrect_otp) {
        super(incorrect_otp);
    }
}
