package com.example.prajwal.nuiapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * Created by Prajwal on 20-02-2017.
 */

public class Welcome extends ActionBarActivity implements GestureOverlayView.OnGesturePerformedListener

    {
        DatabaseHelp db;
        String date,deadline,type,notes, deletedstring,code;
        int selectedIndex= -1;
        private GestureLibrary gestureLib;
        private ArrayList<String> arrayListToDo;
        private ArrayAdapter<String> arrayAdapterToDo;

    /*Called when the activity is first  created */
        @Override
        public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
            db=new DatabaseHelp(this);
        // Create a UI component to grab gesture input
        GestureOverlayView gestureOverlayView = new GestureOverlayView(this);
        // Here we are adding gestureOverlay to or UI
        View inflate = getLayoutInflater().inflate(R.layout.activity_main,null);
        gestureOverlayView.addView(inflate);
        gestureOverlayView.addOnGesturePerformedListener(this);

        // Initialise gesture lib
        gestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if(!gestureLib.load()){
            finish();
        }

        setContentView(gestureOverlayView);

//        setContentView(R.layout.activity_main);
//
        arrayListToDo= new ArrayList<String>();
        arrayAdapterToDo= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayListToDo);

        ListView listViewToDo=(ListView)findViewById(R.id.lv1);
        listViewToDo.setAdapter(arrayAdapterToDo);

        registerForContextMenu(listViewToDo);




//        try{
//            Log.i("ON CREATE HAS OCCURED","Hi, it has occured");
//
//            Scanner scanner=new Scanner(openFileInput("ToDo.txt"));
//
//            while(scanner.hasNextLine())
//            {
//                String toDo=scanner.nextLine();
//                arrayAdapterToDo.add(toDo);
//            }
//            scanner.close();
//        }catch(Exception e)
//        {
//            Log.i("ON CREATE",e.getMessage());
//        }
    }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater=getMenuInflater();
            inflater.inflate(R.menu.more_tab_menu, menu);
            return true;


        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch(item.getItemId())
            {
                case R.id.hand:
                    Intent i=new Intent(getApplicationContext(),ReminderActivity.class);
                    startActivity(i);
                    break;
                case R.id.viewl:
                    arrayListToDo.clear();
                    Cursor r=db.getAllData();
                    while(r.moveToNext())
                    {
                        arrayListToDo.add(r.getString(1));
                        arrayAdapterToDo.notifyDataSetChanged();
                    }
                    break;
                case R.id.help:
                    Intent in=new Intent(getApplicationContext(),Help.class);
                    startActivity(in);
                    break;
                default:
                    return super.onOptionsItemSelected(item);
            }
               return true;
        }

        @Override
        public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture){
        ArrayList<Prediction> predictions = gestureLib.recognize(gesture);
        for(Prediction prediction : predictions){
            if(prediction.score > 3.0){
            //    Toast.makeText(this, prediction.name+" : "+prediction.score, Toast.LENGTH_SHORT)
              //          .show();
                if(prediction.name.equals("view")){
                    arrayListToDo.clear();
                    Cursor r=db.getAllData();
                    while(r.moveToNext())
                    {
                        arrayListToDo.add(r.getString(1));
                        arrayAdapterToDo.notifyDataSetChanged();
                    }
                }
               else if(prediction.name.equals("new"))
                {
                    Intent i=new Intent(Welcome.this,TaskActivity.class);
                    startActivity(i);
                }
               else if(prediction.name.equals("clear"))
                {
                    deletedstring =arrayListToDo.get(selectedIndex);
                    // db.deleteData(arrayListToDo.get(selectedIndex));
                    arrayListToDo.remove(selectedIndex);
                    arrayAdapterToDo.notifyDataSetChanged();
                    Cursor r=db.getAllData();
                    while(r.moveToNext())
                    {
                        if(r.getString(1).equals(deletedstring)) {
                            date = r.getString(2);
                            deadline = r.getString(3);
                            type = r.getString(4);
                            notes = r.getString(5);
                            code= r.getString(0);
                        }
                    }

                    db.deleteData(deletedstring);


                }
                else if(prediction.name.equals("others"))											//code for undo
                {
                 Intent k=new Intent(getApplicationContext(),ReminderActivity.class);
                    startActivity(k);

                }
                else if(prediction.name.equals("undo"))											//code for undo
                {

                    db.insertData(deletedstring,date,deadline,type,notes);
                    arrayAdapterToDo.notifyDataSetChanged();
                }
                else if(prediction.name.equals("list")) {

                    Cursor r=db.getAllData();

                StringBuffer buff=new StringBuffer();
                while(r.moveToNext())
                {
                    buff.append("TaskId: "+r.getString(0)+"\n");
                    buff.append("Task Name: "+r.getString(1)+"\n");
                    buff.append("Date: "+r.getString(2)+"\n");
                    buff.append("Time: "+r.getString(3)+"\n");
                    buff.append("Type: "+r.getString(4)+"\n");
                    buff.append("Notes: "+r.getString(5)+"\n\n");
                }

                showMsg("Details Of Tasks !!", buff.toString());

//                    if(selectedIndex != -1) {
//                        arrayListToDo.get(selectedIndex)
//                        arrayAdapterToDo.notifyDataSetChanged();
//                    }
              }

                else if(prediction.name.equals("personal")){
                    arrayListToDo.clear();
                    Cursor r=db.getAllData();
                    while(r.moveToNext())
                    {
                        if(r.getString(4).equals("Personal"))
                        {
                            arrayListToDo.add(r.getString(1));
                            arrayAdapterToDo.notifyDataSetChanged();
                        }
                    }
                }
                else if(prediction.name.equals("work")){
                    arrayListToDo.clear();
                    Cursor r=db.getAllData();
                    while(r.moveToNext())
                    {
                        if(r.getString(4).equals("Work"))
                        {
                            arrayListToDo.add(r.getString(1));
                            arrayAdapterToDo.notifyDataSetChanged();
                        }
                    }
                }
//                else {
//                    Toast.makeText(this,"Invalid Gesture!",Toast.LENGTH_LONG).show();
//                }
            }
        }
    }
//        @Override
//        public void onBackPressed()
//        {
//            try{
//                Log.i("ON BACK PRESSED","Hi, it has occured");
//                PrintWriter pw=new PrintWriter(openFileOutput("ToDo.txt", Context.MODE_PRIVATE));
//
//                for(String toDO : arrayListToDo)
//                {
//                    pw.println(toDO);
//                }
//                pw.close();
//            }
//            catch(Exception e)
//            {
//                Log.i("ON BACK PRESSED",e.getMessage());
//            }
//            super.onBackPressed();
//        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo)
        {
            if(view.getId() != R.id.lv1){
                return;
            }
            menu.setHeaderTitle("Choose Actions");
            String []options={"Select"};

            for(String option:options)
            {
                menu.add(option);
            }
        }

        @Override
        public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        selectedIndex=info.position;

//        if(item.getTitle().equals("Details"))
//        {
//            String s=arrayListToDo.get(selectedIndex);
//            Cursor r=db.details(s);
//
//                StringBuffer buff=new StringBuffer();
//                while(r.moveToNext())
//                {
//                    buff.append("TaskId: "+r.getString(0)+"\n");
//                    buff.append("Task Name: "+r.getString(1)+"\n");
//                    buff.append("Date: "+r.getString(2)+"\n");
//                    buff.append("Time: "+r.getString(3)+"\n\n");
//                    buff.append("Type: "+r.getString(4)+"\n\n");
//                    buff.append("Notes: "+r.getString(5)+"\n\n");
//                }
//
//                showMsg("Details Of Task !!", buff.toString());
//            }


        return true;
    }


        public void showMsg(String title,String msg)
        {
            AlertDialog.Builder build=new AlertDialog.Builder(this);
            build.setCancelable(true);
            build.setTitle(title);
            build.setMessage(msg);
            build.show();

        }


//    public void buttonClick(View v)
//    {
//        EditText editText1=(EditText) findViewById(R.id.editText);
//        String todo=editText1.getText().toString();
//        if(todo.isEmpty())
//        {
//            return;
//        }
//        arrayAdapterToDo.add(todo);
//        editText1.setText("");
//    }


}


