package com.vedansh.smssync.filter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class BankSmsFilter {

    private static final Set<String> BANK_SENDERS = new HashSet<>(Arrays.asList(

            "HDFCBK",
            "SBIINB",
            "ICICIB",
            "AXISBK",
            "KOTAKB",
            "PNBSMS",
            "CANBNK",
            "BOIIND",
            "UNION",
            "IDFCFB",
            "YESBNK",
            "AUBANK",
            "INDBNK"

    ));

    private static final String[] KEYWORDS = {

            "credited",
            "debited",
            "withdrawn",
            "balance",
            "available balance",
            "upi",
            "imps",
            "neft",
            "rtgs",
            "txn",
            "transaction",
            "a/c",
            "account",
            "spent",
            "received"

    };

    public static boolean isBankTransaction(String sender,
                                            String message) {

        if (sender == null || message == null)
            return false;

        sender = sender.toUpperCase(Locale.ROOT);

        message = message.toLowerCase(Locale.ROOT);

        for (String bank : BANK_SENDERS) {

            if (sender.contains(bank))
                return true;

        }

        for (String keyword : KEYWORDS) {

            if (message.contains(keyword))
                return true;

        }

        return false;
    }

}