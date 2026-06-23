package com.snhu.sslserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class SslServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SslServerApplication.class, args);
    }
}

@RestController
class ServerController {

    @RequestMapping("/hash")
    public String myHash() {

        // My name as the unique data string required for checksum verification
        // Name is hardcoded here intentionally - this is a demonstration of checksum verification
        // In a production application this would come from an authenticated session or request parameter
        String data = "Shawn Ward";

        try {
            // Initialize MessageDigest with SHA-256 - collision resistant and NIST approved
            // SHA-256 produces a unique 256-bit hash that cannot be reversed or duplicated
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Run the data string through SHA-256 using UTF-8 encoding
            // UTF-8 ensures consistent byte representation across all platforms
            byte[] hashBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));

            // Convert raw bytes to readable hex string
            // Raw byte output is not human readable so we convert to hex for verification
            String hexHash = bytesToHex(hashBytes);

            return "<p>data: " + data + "</p>"
                 + "<p>Algorithm: SHA-256</p>"
                 + "<p>Checksum: " + hexHash + "</p>";

        } catch (NoSuchAlgorithmException e) {
            // Handle the case where the algorithm is not available in this environment
            return "<p>Error generating hash: " + e.getMessage() + "</p>";
        }
    }

    // Converts each byte to a two-character hex value
    // The %02x format ensures single digit values are zero padded
    // without padding the hash output would be the wrong length
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}