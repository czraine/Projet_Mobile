package com.example.agencedevoyage.Entity;

public class Reservation {

    private int id;           // Corresponds to COLUMN_ID, INTEGER PRIMARY KEY AUTOINCREMENT
    private String firstName;   // Corresponds to COLUMN_FULL_NAME, TEXT
    private String lastName;   // Corresponds to COLUMN_LAST_NAME, TEXT
    private String phone;      // Corresponds to COLUMN_PHONE, TEXT
    private String passport;   // Corresponds to COLUMN_PASSPORT, TEXT
    private String requirements; // Corresponds to COLUMN_REQUIREMENTS, TEXT

    // Empty constructor
    public Reservation() {}

    // Constructor without ID for new entries
    public Reservation(String firstName, String lastName, String phone, String passport, String requirements) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.passport = passport;
        this.requirements = requirements;
    }

    // Constructor with ID (for entries with known ID, such as when retrieved from DB)
    public Reservation(int id, String firstName, String lastName, String phone, String passport, String requirements) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.passport = passport;
        this.requirements = requirements;
    }

    // Getters and setters for all fields
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassport() { return passport; }
    public void setPassport(String passport) { this.passport = passport; }

    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }
}
