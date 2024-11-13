package com.example.agencedevoyage.Dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.agencedevoyage.Database.DatabaseHelper;
import com.example.agencedevoyage.Entity.Reservation;

import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public ReservationDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Insert a new reservation
    // Insert a new reservation
    public long insertReservation(Reservation reservation) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_FULL_NAME, reservation.getFirstName());
        values.put(DatabaseHelper.COLUMN_LAST_NAME, reservation.getLastName());
        values.put(DatabaseHelper.COLUMN_PHONE, reservation.getPhone());
        values.put(DatabaseHelper.COLUMN_PASSPORT, reservation.getPassport());
        values.put(DatabaseHelper.COLUMN_REQUIREMENTS, reservation.getRequirements());

        return database.insert(DatabaseHelper.TABLE_RESERVATION, null, values);
    }


    // Retrieve all reservations
    @SuppressLint("Range")
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_RESERVATION,
                null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Reservation reservation = new Reservation();
                reservation.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
                reservation.setFirstName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FULL_NAME)));
                reservation.setLastName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_LAST_NAME)));
                reservation.setPhone(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE)));
                reservation.setPassport(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PASSPORT)));
                reservation.setRequirements(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_REQUIREMENTS)));

                reservations.add(reservation);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return reservations;
    }

    // Update a reservation
    public int updateReservation(Reservation reservation) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_FULL_NAME, reservation.getFirstName());
        values.put(DatabaseHelper.COLUMN_LAST_NAME, reservation.getLastName());
        values.put(DatabaseHelper.COLUMN_PHONE, reservation.getPhone());
        values.put(DatabaseHelper.COLUMN_PASSPORT, reservation.getPassport());
        values.put(DatabaseHelper.COLUMN_REQUIREMENTS, reservation.getRequirements());

        return database.update(DatabaseHelper.TABLE_RESERVATION, values,
                DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(reservation.getId())});
    }

    // Delete a reservation
    public int deleteReservation(int reservationId) {
        return database.delete(DatabaseHelper.TABLE_RESERVATION,
                DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(reservationId)});
    }

    @SuppressLint("Range")
    public Reservation getReservationById(int id) {
        Reservation reservation = null;
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_RESERVATION,
                null,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            reservation = new Reservation();
            reservation.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
            reservation.setFirstName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FULL_NAME)));
            reservation.setLastName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_LAST_NAME)));
            reservation.setPhone(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE)));
            reservation.setPassport(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PASSPORT)));
            reservation.setRequirements(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_REQUIREMENTS)));
            cursor.close();
        }
        return reservation;
    }

}
