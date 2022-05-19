package edu.kau.fcit.cpit252.transactions;

import edu.kau.fcit.cpit252.utils.MissingRequiredPropertiesException;
import edu.kau.fcit.cpit252.utils.SendEmail;
import edu.kau.fcit.cpit252.utils.TOTP;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.map.PassiveExpiringMap;


public abstract class Transaction {
    private final int timeToLiveMinutes = 8;
    private final PassiveExpiringMap<String, String> cacheRegistry = new PassiveExpiringMap(TimeUnit.MINUTES.toMillis(timeToLiveMinutes));

    
    public final boolean executeProtectedTransaction(User user) {
        sendOTP(user.getEmail());
        if(validateOTP(user.getEmail())){
           
            showDialog();
            boolean result=perform(user);
            closeDialog();
            return result;
        
        }
                
            return false;

    }

    // Template method for executed non-protected transactions
    public  boolean executeTransaction(User user) {

        showDialog();
        boolean result=perform(user);
        closeDialog();
        return result;
        
    }

    // Hooks
    public abstract boolean showDialog();
    public abstract boolean perform(User u);
    public abstract void closeDialog();

    // Simulate network latency
    protected void simulateNetworkLatency() {
        try {
            System.out.println();
            for (int i = 0; i < 10; i++) {
                System.out.print(".");
                Thread.sleep(500);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private boolean sendOTP(String email) {
        try {
            System.out.println("Generating OTP and sending it to " + email + " ....");
            String otp = TOTP.generateTOTP(timeToLiveMinutes);
            cacheRegistry.put(email, otp);
            SendEmail.send("Your OTP", "Password: " + otp, email);
            return true;
        } catch (NoSuchAlgorithmException | InvalidKeyException | MissingRequiredPropertiesException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean validateOTP(String email) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter OTP that was sent to: " + email);
        String enteredPassword = scanner.nextLine().trim();
        String cachedPassword = cacheRegistry.get(email);
        if (cachedPassword!= null && cachedPassword.equals(enteredPassword)) {
            System.out.println("OTP password is matched");
            return true;
        }
        System.out.println("OTP password is invalid");
        return false;
    }

}
