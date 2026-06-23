# CS-305 Software Security Portfolio

This repository contains my completed Project Two artifact for CS-305 (Software Security) at SNHU: the **Artemis Financial Practices for Secure Software Report**, along with the Module Eight journal reflection below.

## Artifact

- **Practices for Secure Software Report** (Project Two) — documents the security refactor of Artemis Financial's Java/Spring Boot application, including HTTPS implementation, a SHA-256 data integrity endpoint, and OWASP Dependency-Check vulnerability remediation.

## Module Eight Journal — Reflection

### Client and software requirements

Artemis Financial is a fictional financial consulting firm that needed a security review of its Java/Spring Boot web application before it could be trusted with client financial data. The company's core concern was straightforward: clients hand over sensitive financial and personal information, and a vulnerability anywhere in the stack (outdated dependencies, unencrypted communication, weak data integrity checks) could expose that data or violate regulations like GLBA. Artemis Financial needed a real vulnerability assessment, not a checkbox exercise, followed by actual code changes that closed the gaps the assessment identified.

### What I did well, and why secure coding matters

The strongest part of my work was treating the vulnerability assessment and the refactor as one continuous process instead of two disconnected assignments. Project One's manual review and dependency scan surfaced specific, named problems: no encryption in transit, no data integrity verification, and a Spring Boot dependency tree carrying 218 known CVEs. Project Two existed to fix exactly those problems, not generic "best practices." I added a SHA-256 checksum endpoint for data integrity, configured HTTPS with a proper keystore for encryption in transit, and used OWASP Dependency-Check's suppression mechanism to document and clear the dependency vulnerabilities rather than just silencing the noise.

Secure coding matters because a financial company's entire business model runs on trust. One breach doesn't just cost money in the moment, it costs the credibility that got clients to hand over their financial information in the first place. Security isn't a separate feature bolted onto working software. It's part of whether the software actually does its job.

### Challenging or helpful parts

The dependency suppression work was the most genuinely difficult part. The OWASP tool's UI only surfaced one CVE at a time instead of showing the full list, so building a suppression file that actually covered all 13 vulnerable dependency groups took several passes, catching dependencies the first version missed each time. The keystore setup for HTTPS was its own fight: a broken keytool.exe from a JDK update, a pre-existing keystore with no documented password, and permission errors generating a new one. Working through that taught me more about how Java actually handles certificates and trust stores than any reading would have. The most helpful part was running the dependency check again after the refactor and confirming all 218 vulnerabilities traced back to the pre-existing Spring Boot version, not to any code I added. That gave me a clean, evidence-based answer to "did I make this worse" instead of just assuming I hadn't.

### Increasing layers of security, and future tools

I followed the same order as the vulnerability assessment process flow diagram: architecture review first, then cryptography (the certificate and the SHA-256 hash function), then the client/server HTTPS configuration, then a final code quality and dependency re-check. Each layer assumed the others might fail, which is the same logic I used for years doing pre-trip inspections and route clearance: no single check is allowed to be the only thing standing between you and a problem. Going forward, I'd lean on OWASP Dependency-Check as a standard part of any build process, not a one-time assignment tool, along with NIST-standardized algorithms (AES-256-GCM, SHA-256, RSA-2048) instead of anything custom or unproven.

### Verifying functionality and security after refactoring

After making the code changes, I ran the application and confirmed the new `/hash` endpoint returned the correct SHA-256 checksum and that the HTTPS listener was actually serving traffic over the configured certificate, not silently falling back to HTTP. Then I ran OWASP Dependency-Check again to get a fresh report. Comparing the before and after results showed the vulnerability count was unchanged and fully attributable to the existing Spring Boot dependency tree, which confirmed the refactor added functionality without expanding the attack surface.

### Resources and practices for future use

OWASP Dependency-Check, Java's `MessageDigest` class for integrity verification, and Spring Boot's keystore-based HTTPS configuration are all things I expect to use again. Beyond the tools themselves, the habit that will carry forward is treating a dependency scan as a baseline to compare against, not a one-time pass/fail gate. I also picked up a hard lesson about credential handling early in Project One (catching a hardcoded API key in `pom.xml` before it became a real exposure) that I now treat as a standing check on every project, not a one-off mistake to fix and forget.

### What I'd show a future employer

I'd show this report specifically because it documents a complete security lifecycle: identify real vulnerabilities, justify a specific mitigation with named algorithms and standards, implement the fix, and verify the fix didn't introduce new problems. That's the actual job, not just one slice of it. It also shows I can troubleshoot real environment problems (broken tooling, undocumented credentials) instead of only working when everything is already set up correctly.
