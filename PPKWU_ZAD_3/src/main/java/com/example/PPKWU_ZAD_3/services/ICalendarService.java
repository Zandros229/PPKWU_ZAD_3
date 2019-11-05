package com.example.PPKWU_ZAD_3.services;

import java.io.File;
import java.io.IOException;

public interface ICalendarService {
    File generateThisMonth() throws IOException;

    File generateNextMonth() throws IOException;
}
