package br.com.cashplus.config;

import br.com.cashplus.exception.SessionException;
import br.com.cashplus.util.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionInterceptor implements HandlerInterceptor {
    
    private static final String SESSION_TOKEN_HEADER = "X-Session-Token";
    
    @Autowired
    private SessionManager sessionManager;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Permite requisições OPTIONS (CORS preflight)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        
        // Endpoints públicos que não precisam de sessão
        String path = request.getRequestURI();
        if (path.startsWith("/actuator") || path.equals("/") || path.startsWith("/sessao")) {
            return true;
        }
        
        String sessionToken = request.getHeader(SESSION_TOKEN_HEADER);
        
        if (sessionToken == null || sessionToken.isEmpty()) {
            throw new SessionException("Token de sessão é obrigatório. Envie o header X-Session-Token");
        }
        
        if (!sessionManager.isValidSession(sessionToken)) {
            throw new SessionException("Sessão inválida ou expirada");
        }
        
        return true;
    }
}

