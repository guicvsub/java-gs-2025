package br.com.cashplus.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {
    
    private static final long SESSION_TIMEOUT_MINUTES = 30;
    
    private final Map<String, SessionInfo> sessions = new ConcurrentHashMap<>();
    
    public String createSession(String userId) {
        String token = UUID.randomUUID().toString();
        SessionInfo sessionInfo = new SessionInfo(userId, LocalDateTime.now());
        sessions.put(token, sessionInfo);
        return token;
    }
    
    public boolean isValidSession(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        
        SessionInfo sessionInfo = sessions.get(token);
        if (sessionInfo == null) {
            return false;
        }
        
        // Verifica se a sessão expirou
        if (sessionInfo.getLastAccess().plusMinutes(SESSION_TIMEOUT_MINUTES).isBefore(LocalDateTime.now())) {
            sessions.remove(token);
            return false;
        }
        
        // Atualiza último acesso
        sessionInfo.setLastAccess(LocalDateTime.now());
        return true;
    }
    
    public void invalidateSession(String token) {
        sessions.remove(token);
    }
    
    public void cleanupExpiredSessions() {
        LocalDateTime now = LocalDateTime.now();
        sessions.entrySet().removeIf(entry -> 
            entry.getValue().getLastAccess().plusMinutes(SESSION_TIMEOUT_MINUTES).isBefore(now)
        );
    }
    
    private static class SessionInfo {
        private String userId;
        private LocalDateTime lastAccess;
        
        public SessionInfo(String userId, LocalDateTime lastAccess) {
            this.userId = userId;
            this.lastAccess = lastAccess;
        }
        
        public String getUserId() {
            return userId;
        }
        
        public LocalDateTime getLastAccess() {
            return lastAccess;
        }
        
        public void setLastAccess(LocalDateTime lastAccess) {
            this.lastAccess = lastAccess;
        }
    }
}

