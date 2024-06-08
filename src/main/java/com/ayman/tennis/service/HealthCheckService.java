package com.ayman.tennis.service;

import com.ayman.tennis.ApplicationStatus;
import com.ayman.tennis.HealthCheck;
import com.ayman.tennis.data.HealthCheckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HealthCheckService {
    @Autowired
    private HealthCheckRepository healthCheckRepository;
    public HealthCheck healthCheck() {
        Long activeSessions = healthCheckRepository.countApplicationConnections();
        if(activeSessions > 0){
            return new HealthCheck(ApplicationStatus.OK, "Welcome to my App!");
        }
        else {
            return new HealthCheck(ApplicationStatus.KO, "The App is not fully functional, please check your configuration.!");
        }
    }
}
