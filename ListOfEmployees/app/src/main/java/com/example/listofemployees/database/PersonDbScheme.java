package com.example.listofemployees.database;

public class PersonDbScheme {
    public static final class PersonTable {

        public static final String NAME = "persons";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String FIRST_NAME = "first_name";
            public static final String SECOND_NAME = "second_name";
            public static final String BIRTHDAY = "birthday";
            public static final String FEMALE = "is_female";
        }
    }
}
