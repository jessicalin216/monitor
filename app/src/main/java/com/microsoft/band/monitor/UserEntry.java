package com.microsoft.band.monitor;

/**
 * Created by Angie on 8/22/2016.
 */
public class UserEntry {
    String username;
    double avg_sep, avg_len;
    int len_sample, sep_sample, days_until;

    public UserEntry(String username,
            double avg_sep, int len_sample, int sep_sample, int days_until, double avg_len) {
        this.username = username;
        this.avg_sep = avg_sep;
        this.len_sample = len_sample;
        this.sep_sample = sep_sample;
        this.days_until = days_until;
        this.avg_len = avg_len;
    }

    @Override
    public String toString() {
        return "Username = " + username +
                "\nAvg Sep = " + avg_sep +
                "\nLen Sample = " + len_sample +
                "\nSep Sample = " + sep_sample +
                "\nDays Until = " + days_until +
                "\nAvg Length = " + avg_len;
    }
}
