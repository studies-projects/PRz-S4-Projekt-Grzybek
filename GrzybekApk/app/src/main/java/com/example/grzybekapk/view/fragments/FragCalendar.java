package com.example.grzybekapk.view.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.grzybekapk.R;
import com.example.grzybekapk.view.DataForEvents;
import com.example.grzybekapk.view.activities.EventDetailsActivity;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.*;

public class FragCalendar extends Fragment {

    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    private CompactCalendarView compactCalendarView;
    private ActionBar toolbar;
    private TextView showMonthYear;
    private List<Event> bookingsFromMap = new ArrayList<>();
    private static List<Event> events = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar,container,false);

        final List<String> mutableBookings = new ArrayList<>();


        final ListView bookingsListView = view.findViewById(R.id.bookings_listview);
        final ImageButton showPreviousMonthBut = view.findViewById(R.id.prev_button);
        final ImageButton showNextMonthBut = view.findViewById(R.id.next_button);
        showMonthYear = view.findViewById(R.id.month_view);
        final ArrayAdapter adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mutableBookings);

        bookingsListView.setAdapter(adapter);
        compactCalendarView = view.findViewById(R.id.compactcalendar_view);

        compactCalendarView.setUseThreeLetterAbbreviation(true);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        compactCalendarView.setIsRtl(false);
        compactCalendarView.displayOtherMonthDays(true);

        compactCalendarView.addEvents(events);

        compactCalendarView.invalidate();


        toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        toolbar.setTitle("Kalendarz");

        showMonthYear.setText(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));

        bookingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event evClicked = bookingsFromMap.get(position);
                DataForEvents data = (DataForEvents) evClicked.getData();

                Intent i = new Intent(getContext(), EventDetailsActivity.class)
                        .putExtra("name", data.getNameOfEvent())
                        .putExtra("description", data.getDescriptionOfEvent())
                        .putExtra("organizer", data.getOrganizer())
                        .putExtra("date", data.getDateOfEventTxt() + " o godz. " + data.getTimeOfEventTxt());
                startActivity(i);
            }
        });

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                bookingsFromMap = compactCalendarView.getEvents(dateClicked);
                if (bookingsFromMap != null) {
                    mutableBookings.clear();
                    for (Event booking : bookingsFromMap) {                                         //pętla foreach
                        DataForEvents dt = (DataForEvents) booking.getData();                       // pobranie danych naszego eventu
                        String message = dt.getTimeOfEventTxt() + " " + dt.getNameOfEvent();
                        mutableBookings.add(message);
                    }
                    adapter.notifyDataSetChanged();
                } // Wyświetlenie wydarzeń w danym dniu
            }



            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                showMonthYear.setText(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));
            }
        });

        showPreviousMonthBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.scrollLeft();
            }
        });
        showNextMonthBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.scrollRight();
            }
        });


        compactCalendarView.setAnimationListener(new CompactCalendarView.CompactCalendarAnimationListener() {
            @Override
            public void onOpened() {
            }

            @Override
            public void onClosed() {
            }
        });
        return view;
    }


    //Dwie statyczne funkcje do dodawania wydarzeń w kalendarzu, bierzcie jak swoje
    public static void createEvent(Calendar date, DataForEvents object){
        events.add(new Event(Color.rgb(255, 0, 0), date.getTimeInMillis(), object));
    }

    //Ta się różni tym, że kolor tej kropki można ustawić według uznania
    public static void createEvent(Calendar date, DataForEvents object, int red, int green, int blue){
        events.add(new Event(Color.rgb(red, green, blue), date.getTimeInMillis(), object));
    }

    @Override
    public void onResume() {
        super.onResume();
        showMonthYear.setText(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));
    }

}
