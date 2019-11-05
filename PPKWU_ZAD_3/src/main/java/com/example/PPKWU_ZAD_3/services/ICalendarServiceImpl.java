package com.example.PPKWU_ZAD_3.services;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import com.example.PPKWU_ZAD_3.models.CalendarEvent;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ICalendarServiceImpl implements ICalendarService {

    public static final String HTTP_NOVEMBER = "http://www.weeia.p.lodz.pl/pliki_strony_kontroler/kalendarz.php?rok=2019&miesiac=11&lang=1";
    public static final String HTTP_DECEMBER = "http://www.weeia.p.lodz.pl/pliki_strony_kontroler/kalendarz.php?rok=2019&miesiac=12&lang=1";

    private static ICalendar iCalendar = new ICalendar();

    @Override
    public File generateThisMonth() throws IOException {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        String url = "http://www.weeia.p.lodz.pl/pliki_strony_kontroler/kalendarz.php?rok=" + date.getYear() + "&miesiac=" + date.getMonth() + "&lang=1";
        generateCalendar(getCalendarEvent(getWebsiteHTML(HTTP_NOVEMBER)), Calendar.NOVEMBER, 2019);
        File calendarFile = new File("calendar.ics");
        Biweekly.write(iCalendar).go(calendarFile);
        return calendarFile;
    }

    @Override
    public File generateNextMonth() throws IOException {
        generateCalendar(getCalendarEvent(getWebsiteHTML(HTTP_DECEMBER)), Calendar.DECEMBER, 2019);
        File calendarFile = new File("calendar.ics");
        Biweekly.write(iCalendar).go(calendarFile);
        return calendarFile;
    }

    private static void generateCalendar(List<CalendarEvent> calendarEventList, int month, int year) {
        for (CalendarEvent  calendarEvent: calendarEventList) {
            VEvent event = new VEvent();
            event.setSummary(calendarEvent.getEventName());
            Date eventDate = new GregorianCalendar(year, month, Integer.valueOf(calendarEvent.getEventDay())).getTime();
            event.setDateStart(eventDate);
            event.setDateEnd(eventDate);
            iCalendar.addEvent(event);
        }
    }

    private static List<CalendarEvent> getCalendarEvent(String websiteContent) {
        String[] splitted = websiteContent.split("href=\"javascript:void\\(\\);\">");
        List<String> eventsCalendar = new ArrayList<String>();
        for (String s : splitted) {
            if (s.contains("<div class=\"calendar-text\"><div class=\"InnerBox\"><p>")) {
                eventsCalendar.add(s);
            }
        }
        List<CalendarEvent> readyEvents = new ArrayList<CalendarEvent>();
        for (String event : eventsCalendar) {
            String[] splittedEvent = event.split("</a>|<p>|</p>");
            readyEvents.add(new CalendarEvent(splittedEvent[2], splittedEvent[0]));
        }
        return readyEvents;
    }

    private static String getWebsiteHTML(String website) {
        try {
            URL url = new URL(website);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            String returnString = "";
            while ((inputLine = in.readLine()) != null)
                returnString += inputLine;
            in.close();
            return returnString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
