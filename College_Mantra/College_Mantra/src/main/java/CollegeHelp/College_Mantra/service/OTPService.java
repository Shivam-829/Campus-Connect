package CollegeHelp.College_Mantra.service;

import org.springframework.stereotype.Service;

@Service
public class OTPService {

    public static String generateOTP()
    {  //int randomPin declared to store the otp
        //since we using Math.random() hence we have to type cast it int
        //because Math.random() returns decimal value
        int randomPin   =(int) (Math.random()*9000)+1000;
        String otp  = String.valueOf(randomPin);
        return otp; //returning value of otp
    }

    public static String generateTransactionId(){
        int randomPin   =(int) (Math.random()*900000)+1000;
        String otp  = String.valueOf(randomPin);
        return otp; //returning value of otp
    }

}
