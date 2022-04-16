package com.org.messages.model.user;

import java.time.LocalDateTime;

public class User extends AbstractUser {
    private String firstName, surname;
    private LocalDateTime dateOfBirth;

    public User(int userID, String email, String password, String firstName, String surname, LocalDateTime dateOfBirth) {
        super(userID, email, password);
        setFirstName(firstName);
        setSurname(surname);
        setDateOfBirth(dateOfBirth);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "{" + getEmail() + " | " + getFirstName() + " " + getSurname() + "}";
    }
}
