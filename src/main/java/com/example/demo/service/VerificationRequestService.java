@Override
public VerificationRequest processVerification(Long requestId) {
    VerificationRequest request = repository.findById(requestId)
            .orElseThrow(() -> new ResourceNotFoundException("Request not found"));

    CredentialRecord credential =
            credentialRecordService.getCredentialById(request.getCredentialId());

    if (credential.getExpiryDate().isBefore(LocalDate.now())) {
        request.setStatus("FAILED");
        request.setResultMessage("Credential expired");
    } else {
        request.setStatus("SUCCESS");
        request.setResultMessage("Credential valid");
    }

    request.setVerifiedAt(LocalDateTime.now());

    AuditTrailRecord audit = new AuditTrailRecord();
    audit.setCredentialId(credential.getId());
    audit.setEventType("VERIFICATION");
    audit.setDetails(request.getStatus());

    auditTrailService.logEvent(audit);

    return repository.save(request);
}
