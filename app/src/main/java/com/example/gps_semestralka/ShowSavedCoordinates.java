package com.example.gps_semestralka;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ShowSavedCoordinates extends AppCompatActivity {
    DBHelper dbHelper;
    ListView lv_result;
    ArrayList<Item_GPS> listItems = new ArrayList<>();
    ArrayAdapter<Item_GPS> adapter;
    int clickCounter = 0;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_saved_coordinates);

        String str_lat = getString(R.string.latitude) + " ";
        String str_lon = getString(R.string.longitude) + " ";
        String str_alt = getString(R.string.altitude) + " ";
        String str_desc = getString(R.string.nameLocation) + " ";

        String str_del = getString(R.string.delete) + " ";
        String str_delMsg = getString(R.string.deleteMessage) + " ";
        String str_cancel = getString(R.string.cancel) + " ";
        String str_ok = getString(R.string.ok) + " ";

        lv_result = (ListView) findViewById(R.id.lv_coordinates);

        adapter = new ArrayAdapter<Item_GPS>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        lv_result.setAdapter(adapter);

        dbHelper = new DBHelper(this);

        Cursor cursor = dbHelper.getUserData();

        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext()){
            Item_GPS item_gps = new Item_GPS(str_lat + cursor.getString(1)+"\n",
                    str_lon + cursor.getString(2)+"\n",
                    str_alt + cursor.getString(3)+"\n",
                    str_desc + cursor.getString(4)+"\n");

            buffer.append(str_lat + cursor.getString(1)+"\n");
            buffer.append(str_lon + cursor.getString(2)+"\n");
            //String alt = String.format("%.02f", Float.parseFloat(cursor.getString(3)));
            buffer.append(str_alt + cursor.getString(3)+"\n");
            buffer.append(str_desc + cursor.getString(4)+"\n");

            listItems.add(item_gps);
            buffer.delete(0, buffer.length());
            adapter.notifyDataSetChanged();
        }

        lv_result.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("Item",String.valueOf(id));
                String sirka = ((TextView) view).getText().toString().split("\n")[0];
                sirka = sirka.split(": ")[1];
                String delka = ((TextView) view).getText().toString().split("\n")[1];
                delka = delka.split(": ")[1];
                String vyska = ((TextView) view).getText().toString().split("\n")[2];
                vyska = vyska.split(": ")[1];
                String popis = ((TextView) view).getText().toString().split("\n")[3];
                popis = popis.split(": ")[1];

                AlertDialog.Builder adb=new AlertDialog.Builder(ShowSavedCoordinates.this);
                adb.setTitle(str_del);
                adb.setMessage(str_delMsg + (position+1) + "?");
                adb.setNegativeButton(str_cancel, null);
                String finalSirka = sirka;
                String finalDelka = delka;
                String finalVyska = vyska;
                String finalPopis = popis;
                adb.setPositiveButton(str_ok, new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Boolean removedItem = dbHelper.deleteUserData(finalSirka, finalDelka, finalVyska, finalPopis);

                        listItems.remove(position);
                        //Log.v("Item", String.valueOf(removedItem));
                        adapter.notifyDataSetChanged();
                    }});
                adb.show();

                return true;
            }
        });
        lv_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("Item", "edit item");
                Log.v("Item",String.valueOf(id));
                String sirka = ((TextView) view).getText().toString().split("\n")[0];
                sirka = sirka.split(": ")[1];
                String delka = ((TextView) view).getText().toString().split("\n")[1];
                delka = delka.split(": ")[1];
                String vyska = ((TextView) view).getText().toString().split("\n")[2];
                vyska = vyska.split(": ")[1];
                String popis = ((TextView) view).getText().toString().split("\n")[3];
                popis = popis.split(": ")[1];
                String finalSirka = sirka;
                String finalDelka = delka;
                String finalVyska = vyska;
                String finalPopis = popis;

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShowSavedCoordinates.this);
                alertDialog.setTitle(getString(R.string.editName));
                alertDialog.setMessage(getString(R.string.enterNewName));

                final EditText input = new EditText(ShowSavedCoordinates.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String name = input.getText().toString();
                        Log.v("Item", name);
                        if (TextUtils.isEmpty(input.getText())){
                            Boolean updatedItem = dbHelper.updateUserData(finalSirka, finalDelka, finalVyska, finalPopis, getString(R.string.locationName_default));
                            listItems.get(position).setDesc(str_desc + getString(R.string.locationName_default) + "\n");
                        }else{
                            Boolean updatedItem = dbHelper.updateUserData(finalSirka, finalDelka, finalVyska, finalPopis, name);
                            listItems.get(position).setDesc(str_desc + name + "\n");
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
                alertDialog.setNegativeButton(getString(R.string.cancel),null);
                alertDialog.show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}