package com.example.listofemployees;

import java.util.Calendar;

public interface IAction {
    void addPerson(String firstName, String secondName, boolean isFemale, Calendar dareOfBirth);
    void editPerson(int personID, String firstName, String secondName, boolean isFemale, Calendar dareOfBirth);
}