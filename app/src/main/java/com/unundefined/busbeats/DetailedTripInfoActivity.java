package com.unundefined.busbeats;


import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class DetailedTripInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_trip_info);
//
//        Serializable serializable = getIntent().getSerializableExtra("TRIP");
//        Trip trip;
//        if (serializable instanceof Trip) {
//            trip = (Trip) serializable;
//        } else {
//            throw new RuntimeException("Serializable extra is not an instance of Trip");
//        }
//
//        ((TextView) findViewById(R.id.trip_date)).setText(trip.getDate());
//        ((TextView) findViewById(R.id.starting_location)).setText(trip.getStartingLocationInfo());
//        ((TextView) findViewById(R.id.destination_location)).setText(trip.getDestinationLocationInfo());
//        ((TextView) findViewById(R.id.trip_duration)).setText(getString(R.string.trip_duration_with_placeholder, String.valueOf(trip.getDuration())));
//        ((TextView) findViewById(R.id.bus_fee)).setText(getString(R.string.trip_fee_with_placeholder, String.valueOf(trip.getFees())));
//        ((TextView) findViewById(R.id.trip_distance)).setText(getString(R.string.trip_distance_with_placeholder, String.valueOf(trip.getDistance())));
//
//        ((ListView) findViewById(R.id.trip_buses_info)).setAdapter(new BusInfoAdapter(getApplicationContext(), R.layout.buses_ticket, trip.getLinkedBuses()));
   }
}
