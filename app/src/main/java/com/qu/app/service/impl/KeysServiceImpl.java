package com.qu.app.service.impl;

import com.qu.app.entity.Keys;
import com.qu.app.error.QuException;
import com.qu.app.repository.KeysRepository;
import com.qu.app.service.KeysService;
import com.qu.app.utils.RSA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


@Service
public class KeysServiceImpl implements KeysService {
    @Autowired
    private RSA rsa;

    @Autowired
    private KeysRepository keysRepository;

    @Override
    public Map<String, String > SaveGetRSAKeys() {
        // check and if public, save public private key in database
        if(keysRepository.findByName("PUBLIC") == null){
            // Initialization of key pair for encryption and decryption
            KeyPair keyPair = rsa.getKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // save public key
            Keys keysPub = new Keys();
            keysPub.setName("PUBLIC");
            keysPub.setRSAKey(this.encodeKey(publicKey));
            keysRepository.save(keysPub);
            // save private key
            Keys keysPri = new Keys();
            keysPri.setName("PRIVATE");
            keysPri.setRSAKey(this.encodeKey(privateKey));
            keysRepository.save(keysPri);
        }

        // else we get key from database
        Map<String, String> publicPrivateKeys = new HashMap<>();
        publicPrivateKeys.put("PUBLIC", this.encodeKey(this.decodePublicKey(keysRepository.findByName("PUBLIC").getRSAKey())));
        publicPrivateKeys.put("PRIVATE", this.encodeKey(this.decodePrivateKey(keysRepository.findByName("PRIVATE").getRSAKey())));
        return publicPrivateKeys;
    }

    public String encodeKey(Key key) {
        byte[] keyBytes = key.getEncoded();
        String encodedKeyStr = Base64.getEncoder().encodeToString(keyBytes);
        return encodedKeyStr;
    }
    @Override
    public PublicKey decodePublicKey(String keyStr){
        try{
            byte[] keyBytes = Base64.getDecoder().decode(keyStr);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey key = keyFactory.generatePublic(spec);
            return key;
        }catch (Exception e){
            throw new QuException(e.getMessage());
        }
    }

    @Override
    public PrivateKey decodePrivateKey(String keyStr){
        try{
            byte[] keyBytes = Base64.getDecoder().decode(keyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey key = keyFactory.generatePrivate(keySpec);
            return key;
        }catch (Exception e){
            throw new QuException(e.getMessage());
        }
    }


}