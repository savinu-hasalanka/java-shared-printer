package Utility;

import java.time.LocalDate;
import java.time.Period;

public class NIC {
    private String nic;
    private LocalDate birthday;
    private int age;
    private Gender gender;

    private final int[] DAYS_IN_MONTHS = new int[] {
            31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
    };

    public NIC(String nic) {
        if (nic == null)
            throw new IllegalArgumentException();

        this.nic = nic;
        birthday = extractBirthday();
        age = calculateAge();
        gender = extractGender();
    }

    private LocalDate extractBirthday() {
        int birthYear;
        int birthDayOfTheYear;

        if (nic.length() == 12) {
            birthYear = Integer.parseInt(nic.substring(0, 4));
            birthDayOfTheYear = Integer.parseInt(nic.substring(4, 7));
        } else {
            birthYear = Integer.parseInt(nic.substring(0, 2)) + 1900;
            birthDayOfTheYear = Integer.parseInt(nic.substring(2, 5));
        }

        if (birthDayOfTheYear > 366)
            birthDayOfTheYear -= 500;

        int month = extractBirthMonthAndDay(birthDayOfTheYear)[0];
        int day = extractBirthMonthAndDay(birthDayOfTheYear)[1];
        return LocalDate.of(birthYear, month, day);
    }

    private int[] extractBirthMonthAndDay(int birthDayOfTheYear) {
        int count = 0;
        int total = 0;
        int month;
        int day;
        while (true) {
            total += DAYS_IN_MONTHS[count];

            if (birthDayOfTheYear <= total) {
                month = count + 1;
                if (count > 0)
                    day = birthDayOfTheYear - (total - DAYS_IN_MONTHS[count]);
                else
                    day = birthDayOfTheYear;
                break;
            }
            count ++;
        }

        return new int[] {month, day};
    }

    private int calculateAge() {
        return Period.between(birthday, LocalDate.now()).getYears();
    }

    private Gender extractGender() {
        int birthDayOfTheYear;
        if (nic.length() == 12)
            birthDayOfTheYear = Integer.parseInt(nic.substring(4, 7));
        else
            birthDayOfTheYear = Integer.parseInt(nic.substring(2, 5));
        return birthDayOfTheYear > 500 ? Gender.FEMALE : Gender.MALE;
    }

    public int getAge() {
        return age;
    }

    public Gender getGender() {
        return gender;
    }

    @Override
    public String toString() {
        return nic;
    }
}
