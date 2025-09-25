package com.bornfire.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class UploadProgressService {
    private final Map<String, Integer> progressMap = new ConcurrentHashMap<>();

    public void setProgress(String userId, int percent) {
        progressMap.put(userId, percent);
    }

    public int getProgress(String userId) {
        return progressMap.getOrDefault(userId, 0);
    }

    public void removeProgress(String userId) {
        progressMap.remove(userId);
    }
}
