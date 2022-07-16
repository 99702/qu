package com.qu.app.service;

import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

@Service
public interface KeysService {
    Map<String, String> SaveGetRSAKeys();
    PublicKey decodePublicKey(String s);
    PrivateKey decodePrivateKey(String s);
}
