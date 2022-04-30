package com.example.listofemployees;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class PersonListFragment extends Fragment{
    private RecyclerView mPersonRecyclerView;
    private PersonAdapter mAdapter;
    private int mSelectedPosition = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // обрабатываем клики меню
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_list, container, false);
        mPersonRecyclerView = (RecyclerView) view.findViewById(R.id.person_recycler_view);
        mPersonRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }

    private void updateUI() {
        PersonBank personBank = PersonBank.get(getActivity());
        List<Person> personList = personBank.getPersons();
        if (mAdapter == null) {
            mAdapter = new PersonAdapter(personList);
            mPersonRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    // Добавляем командное меню к фрагменту.
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_person_list, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean isEditingMode;
        FragmentManager manager = getFragmentManager();
        AddPersonsFragment dialog;
        switch (item.getItemId()) {
            case R.id.add_person:
                isEditingMode = false;
                dialog =  AddPersonsFragment.newInstance(isEditingMode);
                dialog.show(manager, "string");
                return true;
            case R.id.edit_person:
                isEditingMode = true;
                dialog = AddPersonsFragment.newInstance(isEditingMode);
                dialog.show(manager, "string");
                return true;
            case R.id.remove_person:


        }
        return super.onOptionsItemSelected(item);
    }

    private class PersonHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mGenderImageView;
        private TextView mFirstNameTextView;
        private TextView mSecondNameTextView;
        private TextView mDateTextView;
        private Person mPerson;


        public PersonHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_person, parent, false));

            itemView.setOnClickListener(this);
            mGenderImageView = (ImageView) itemView.findViewById(R.id.image_gender);
            mFirstNameTextView = (TextView) itemView.findViewById(R.id.tv_name);
            mSecondNameTextView = (TextView) itemView.findViewById(R.id.tv_second_name);
            mDateTextView = (TextView) itemView.findViewById(R.id.tv_date_of_birt);
        }

        public void bind(Person person) {
            mPerson = person;
            mFirstNameTextView.setText(mPerson.getFirstName());
            mSecondNameTextView.setText(mPerson.getSecondName());
            mDateTextView.setText(mPerson.getBirthDayString());

            if (person.isFemale) {
                mGenderImageView.setImageResource(R.drawable.woman);
            } else {
                mGenderImageView.setImageResource(R.drawable.man);
            }
       }

        @Override
        public void onClick(View view) {
            // действие по клику
            UUID personID = mPerson.getId();
            mSelectedPosition = getAdapterPosition();
            mAdapter.notifyDataSetChanged();

        }
    }

    private class PersonAdapter extends RecyclerView.Adapter<PersonHolder> {
        private List<Person> mPersons;

        public PersonAdapter(List<Person> persons) {
            mPersons = persons;
        }

        @NonNull
        @Override
        public PersonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new PersonHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull PersonHolder holder, int position) {
            if (mSelectedPosition == position) {
                holder.itemView.setBackgroundColor(Color.YELLOW);
            } else {
                holder.itemView.setBackgroundColor(Color.WHITE);
            }

            Person person = mPersons.get(position);
            holder.bind(person);
        }

        @Override
        public int getItemCount() {
            return mPersons.size();
        }
    }
}
