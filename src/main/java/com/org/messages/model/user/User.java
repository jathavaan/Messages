package com.org.messages.model.user;

import javafx.fxml.Initializable;

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
        checkName(firstName, "First name");
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        checkName(surname, "Surname");
        this.surname = surname;
    }

    public LocalDateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDateTime dateOfBirth) {
        checkDateOfBirth(dateOfBirth);
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "{" + getEmail() + " | " + getFirstName() + " " + getSurname() + "}";
    }

    private void checkName(String name, String nameType) {
        String regex = "/^[A-Za-z\\x{00C0}-\\x{00FF}][A-Za-z\\x{00C0}-\\x{00FF}\\'\\-]+([\\ A-Za-z\\x{00C0}-\\x{00FF}][A-Za-z\\x{00C0}-\\x{00FF}\\'\\-]+)*/u";

        if (name == null || name.isBlank()) throw new IllegalArgumentException(nameType + " cannot be empty");
        if (!name.matches(regex)) throw new IllegalArgumentException("Invalid " + nameType.toLowerCase() +".");
    }

    private void checkDateOfBirth(LocalDateTime dateOfBirth) {
        if (dateOfBirth == null) throw new IllegalArgumentException("Date of birth cannot be null");
        if (dateOfBirth.isAfter(LocalDateTime.now())) throw new IllegalStateException("Date of birth cannot be in the future");
    }
}
