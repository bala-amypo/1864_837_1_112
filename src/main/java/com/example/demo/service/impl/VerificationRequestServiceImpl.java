@Service
public class VerificationRequestServiceImpl implements VerificationRequestService {
    // ... repository and constructor same as before ...

    @Override
    public VerificationRequest processVerification(Long requestId) {
        VerificationRequest request = requestRepo.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));
        
        CredentialRecord credential = credentialService.getById(request.getCredentialId());

        // FIX for t61/t62: Check BOTH date and status string
        boolean dateExpired = credential.getExpiryDate() != null && 
                             credential.getExpiryDate().isBefore(LocalDate.now());
        boolean statusExpired = "EXPIRED".equalsIgnoreCase(credential.getStatus());

        if (dateExpired || statusExpired) {
            request.setStatus("FAILED");
        } else {
            request.setStatus("SUCCESS");
        }

        // FIX: Ensure audit trail logs the time correctly
        AuditTrailRecord audit = new AuditTrailRecord();
        audit.setCredentialId(credential.getId());
        audit.setLoggedAt(java.time.LocalDateTime.now()); // Matches t30 requirement
        auditService.logEvent(audit);

        return requestRepo.save(request);
    }
    // ... other methods same as before ...
}