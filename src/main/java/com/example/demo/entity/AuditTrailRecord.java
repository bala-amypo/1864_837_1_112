@PrePersist
public void onCreate() {
    this.loggedAt = java.time.LocalDateTime.now();
}
