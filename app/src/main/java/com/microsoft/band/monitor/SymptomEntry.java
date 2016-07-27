package com.microsoft.band.monitor;

public class SymptomEntry {

    String date;
    boolean acne, cramps, tired;

    public SymptomEntry(String date, boolean acne, boolean cramps,
                               boolean tired) {
        this.date = new String(date);
        this.acne = acne;
        this.cramps = cramps;
        this.tired = tired;
    }

    @Override
    public String toString() {
        return "Date = " + date +
                "\nAcne = " + acne +
                "\nCramps= " + cramps +
                "\nTired= " + tired;
    }
}
