@Override
public CredentialHolderProfile updateStatus(Long id, boolean active) {
    // 1. Load existing profile
    CredentialHolderProfile profile = holderRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
    // 2. Update active status
    profile.setActive(active);
    // 3. Save and return (Returns 200 OK via Controller)
    return holderRepo.save(profile);
}