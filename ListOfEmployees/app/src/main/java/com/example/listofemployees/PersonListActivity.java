package com.example.listofemployees;

import androidx.fragment.app.Fragment;

public class PersonListActivity extends SingleFragmentActivity{
    @Override
    protected Fragment createFragment() {
        return new PersonListFragment();
    }
}
