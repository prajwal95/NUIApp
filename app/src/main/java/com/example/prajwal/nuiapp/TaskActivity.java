package com.example.prajwal.nuiapp;

/**
 * Created by Prajwal on 20-02-2017.
 */

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.os.Build;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Calendar;

public class TaskActivity extends Activity {

    NotificationActivity nt;
    DatabaseHelp db;
    EditText t, d, dl, n;
    Button btn1, btn2, btn3;
    Spinner sp;
    static String[] type = {"Personal", "Work", "Others"};
    DatePickerDialog datePickerDialog;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_main);
        db = new DatabaseHelp(this);

        btn1 = (Button) findViewById(R.id.button1);
        // btn2=(Button)findViewById(R.id.button2);
        //btn3=(Button)findViewById(R.id.button3);
        t = (EditText) findViewById(R.id.editText1);


        n = (EditText) findViewById(R.id.editText5);
        sp = (Spinner) findViewById(R.id.spinner);


        btn3 = (Button) findViewById(R.id.button3);
        // initiate the date picker and a button
        d = (EditText) findViewById(R.id.editText6);
        // perform click event on edit text
        d.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(TaskActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                d.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        dl = (EditText) findViewById(R.id.editText4);
        // perform click event listener on edit text
        dl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(TaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        dl.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        btn3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent m=new Intent(TaskActivity.this, ReminderActivity.class);
                startActivity(m);
            }
        });
        ArrayAdapter<String> aa = new ArrayAdapter<String>(TaskActivity.this,
                android.R.layout.simple_list_item_1, type);
        sp.setAdapter(aa);
        //String text = sp.getSelectedItem().toString();
        AddData();

        // viewAll();
        //UpdateData();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

//    public void UpdateData()
//    {
//        btn3.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//                boolean isUpdate=db.updateData(ed4.getText().toString(),ed1.getText().toString(),ed2.getText().toString(),ed3.getText().toString());
//                if(isUpdate==true)
//                {
//                    Toast.makeText(MainActivity.this, "Data has been updated ", Toast.LENGTH_LONG).show();
//                }
//                else
//                {
//                    Toast.makeText(MainActivity.this, "Data has not  been updated ", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//    }

    private void addNotification(final String str, final String str1) {
        final Notification.Builder builder = new Notification.Builder(this);
        builder.setStyle(new Notification.BigTextStyle(builder)
                .bigText(str)
                .setBigContentTitle("New Task Created")
                .setSummaryText(str1))

                .setSmallIcon(android.R.drawable.sym_def_app_icon);

        Intent notificationIntent = new Intent(this,
                NotificationActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    public void AddData() {
        btn1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                boolean isInserted = db.insertData(t.getText().toString(), d.getText().toString(), dl.getText().toString(), sp.getSelectedItem().toString(), n.getText().toString());

                if (isInserted == true) {


                    Toast.makeText(TaskActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                    addNotification(n.getText().toString(), t.getText().toString());
                    d.setText("");
                    dl.setText("");
                    n.setText("");
                    t.setText("");
                    Intent i = new Intent(getApplicationContext(), Welcome.class);
                    startActivity(i);

                } else
                    Toast.makeText(TaskActivity.this, "Data Not Inserted", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void viewAll() {

        btn2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Cursor r = db.getAllData();

                if (r.getCount() == 0) {
                    showMsg("Error !!", "Nothing found ...");
                }

                StringBuffer buff = new StringBuffer();
                while (r.moveToNext()) {
                    buff.append("TaskId: " + r.getString(0) + "\n");
                    buff.append("Task Name: " + r.getString(1) + "\n");
                    buff.append("Date: " + r.getString(2) + "\n");
                    buff.append("Time: " + r.getString(3) + "\n\n");
                    buff.append("Type: " + r.getString(4) + "\n\n");
                    buff.append("Notes: " + r.getString(5) + "\n\n");
                }

                showMsg("Data Entered !!", buff.toString());
            }
        });
    }

    public void showMsg(String title, String msg) {
        Builder build = new Builder(this);
        build.setCancelable(true);
        build.setTitle(title);
        build.setMessage(msg);
        build.show();

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Task Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
