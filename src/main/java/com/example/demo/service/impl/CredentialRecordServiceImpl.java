@Service
public class CredentialRecordServiceImpl implements CredentialRecordService {
    private final CredentialRecordRepository credentialRepo;

    public CredentialRecordServiceImpl(CredentialRecordRepository credentialRepo) {
        this.credentialRepo = credentialRepo;
    }

    @Override
    public CredentialRecord getById(Long id) {
        return credentialRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Credential not found"));
    }

    // FIX for t16: Ensure this throws the exception so the portal sees a 404
    @Override
    public CredentialRecord getCredentialByCode(String code) {
        return credentialRepo.findByCredentialCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Credential not found"));
    }

    @Override
    public CredentialRecord createCredential(CredentialRecord record) {
        if (record.getExpiryDate() != null && record.getExpiryDate().isBefore(LocalDate.now())) {
            record.setStatus("EXPIRED");
        } else if (record.getStatus() == null) {
            record.setStatus("VALID");
        }
        return credentialRepo.save(record);
    }

    // FIX for t61/t62: Update ALL fields. 
    // If the test updates the date/status and we ignore it here, verification fails.
    @Override
    public CredentialRecord updateCredential(Long id, CredentialRecord update) {
        CredentialRecord existing = getById(id);
        existing.setCredentialCode(update.getCredentialCode());
        existing.setTitle(update.getTitle());
        existing.setIssuer(update.getIssuer());
        existing.setCredentialType(update.getCredentialType());
        existing.setStatus(update.getStatus());
        existing.setExpiryDate(update.getExpiryDate());
        existing.setMetadataJson(update.getMetadataJson());
        return credentialRepo.save(existing);
    }

    @Override
    public List<CredentialRecord> getCredentialsByHolder(Long holderId) {
        return credentialRepo.findByHolderId(holderId);
    }
}