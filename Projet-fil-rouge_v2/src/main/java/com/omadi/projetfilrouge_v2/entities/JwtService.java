package com.omadi.projetfilrouge_v2.entities;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final String accessSecret;
    private final String refreshSecret;
    private final long accessExpiration;
    private final long refreshExpiration;

    public JwtService(Dotenv dotenv) {
        this.accessSecret = dotenv.get("JWT_ACCESS_SECRET");
        this.refreshSecret = dotenv.get("JWT_REFRESH_SECRET");
        this.accessExpiration = Long.parseLong(dotenv.get("JWT_ACCESS_EXPIRATION"));
        this.refreshExpiration = Long.parseLong(dotenv.get("JWT_REFRESH_EXPIRATION"));
    }
    
}
