package com.example.criminalintent.database;

public class CrimeDbScheme {
    public static final class CrimeTable {
        public static final String NAME = "crimes";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String Title = "title";
            public static final String Date = "date";
            public static final String Solved = "solved";
        }
    }
}
