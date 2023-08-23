package com.webknot.assignment.otpService;

import com.webknot.assignment.model.otp.Otp;
import com.webknot.assignment.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@Configuration
@EnableScheduling
public class OtpService {
    @Autowired
    OtpRepository otpRepository;
    public Long createAndReturnOTP(String email){
        Long otp = new Random().nextLong(899999)+100000;
        otpRepository.save(new Otp(email, otp));
        return otp;
    }
    public Boolean checkOtp(String email, Long otp){
        Otp fetchedOtp = otpRepository.findByEmail(email);
        return Objects.equals(fetchedOtp.getOtp(), otp);
    }

    @Scheduled(fixedDelay = 1000*60)
    public void checkOtpValidation(){
        System.out.println("Deleting all the expired otp.");
//        otpRepository.deleteExpiredOtps();
        List<Otp> otps = otpRepository.findAll();
        List<Long> otpIds = new ArrayList<>();
        for(Otp otp: otps){
            long mins = Duration.between(otp.getCreationTime(), LocalDateTime.now()).toMinutes();
            if(mins>1){
                otpIds.add(otp.getOtpId());
                otpRepository.delete(otp);
            }
        }
        System.out.println(otpIds);
        System.out.println("Deleted all expired otp.");
    }

    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(otpRepository.findAll());
    }
}
