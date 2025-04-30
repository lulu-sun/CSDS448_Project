# Lilock
### A minimal notes app focused on privacy
*Version 1.0.0 is out!*
- Lilock is a pin-protected encryption-based notes application.
- Users can create or reset their pin for the app.
- We employ PBKDF2WithHMACSHA512 with 120,000 iterations for hashing the pin and creating the encryption key.
- Users can scroll through their notes and edit or delete them.
- Encrypted notes are stored via SQLite.
