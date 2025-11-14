package br.com.cashplus.controller;

import br.com.cashplus.util.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sessao")
public class SessionController {
    
    @Autowired
    private SessionManager sessionManager;
    
    @PostMapping("/criar")
    public ResponseEntity<Map<String, String>> criarSessao(@RequestParam(required = false, defaultValue = "user") String userId) {
        String token = sessionManager.createSession(userId);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("message", "Sessão criada com sucesso");
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/validar")
    public ResponseEntity<Map<String, String>> validarSessao(@RequestHeader("X-Session-Token") String token) {
        boolean isValid = sessionManager.isValidSession(token);
        Map<String, String> response = new HashMap<>();
        if (isValid) {
            response.put("status", "valid");
            response.put("message", "Sessão válida");
        } else {
            response.put("status", "invalid");
            response.put("message", "Sessão inválida ou expirada");
        }
        return ResponseEntity.ok(response);
    }
}

