package com.example.listofemployees;

import androidx.fragment.app.Fragment;

import com.example.listofemployees.database.entity.Person;

import java.util.Calendar;
import java.util.UUID;

public class PersonListActivity extends SingleFragmentActivity implements IAction {
    @Override
    protected Fragment createFragment() {
        return new PersonListFragment();
    }

    @Override
    public void addPerson(String firstName, String secondName, boolean isFemale, Calendar dareOfBirth) {
        Person person = new Person(firstName, secondName, dareOfBirth, isFemale);
        PersonBank.get(this).addPerson(person);
    }

    @Override
    public void editPerson(String firstName, String secondName, boolean isFemale, UUID mSelectedPersonUUID) {
        PersonBank.get(this).editPerson(mSelectedPersonUUID, firstName, secondName, isFemale);
    }
}
