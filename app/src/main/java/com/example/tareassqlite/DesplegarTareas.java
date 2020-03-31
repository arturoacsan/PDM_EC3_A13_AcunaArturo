package com.example.tareassqlite;

import com.example.*;
import com.example.tareasqlite.database.DBAdapter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class DesplegarTareas extends AppCompatActivity {

	ListView list;
	private Button btnAgregar;
	String prueba = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_desplegar_tareas);
		DBAdapter dbHelper = new DBAdapter(this);
		DBAdapter db = dbHelper.open();

		list = (ListView) findViewById(R.id.listVTareas);
		btnAgregar = (Button) findViewById(R.id.btnAgregar);
		cargarDatos(db);

		//listView on ItemClick
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String value = (String) list.getItemAtPosition(position);
				for (int i = 0; i < value.length(); i++) {
					if (value.charAt(i) == '-') {
						value = value.substring(0, i);
						Toast.makeText(getApplicationContext(), "ID: " + value.substring(0, i), Toast.LENGTH_SHORT).show();
						MessageBox(Long.parseLong(value.trim()));
						break;
					}
				}
			}
		});

		btnAgregar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), AgregarTareas.class);
				startActivity(i);
			}
		});
	}

	public boolean MessageBox(final Long value) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		DBAdapter dbHelper = new DBAdapter(this);
		final DBAdapter db = dbHelper.open();
		builder.setMessage("¿Qué deseas hacer con esta tarea?");
		builder.setNeutralButton("Nada", null);
		builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				Intent u = new Intent(getApplicationContext(), AgregarTareas.class);
				u.putExtra("ID", value);
				startActivity(u);
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("Eliminarlo", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				db.deleteTodo(value);
				cargarDatos(db);
				dialog.dismiss();
			}
		});
		final AlertDialog alert = builder.create();
		alert.show();
		return false;
	}

	public void cargarDatos(DBAdapter db) {

		final List<String> tareas = new ArrayList<String>();
		tareas.clear();
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
				(this, android.R.layout.simple_list_item_1, tareas);
		Cursor c = db.fetchAllTodos();
		if (c.moveToFirst()) {
			do {
				// Passing values
				try {
					tareas.add(c.getString(0) + " - " + c.getString(1) + " - " + c.getString(2) + " - " + c.getString(3));
				} catch (Exception ex) {
					ex = ex;
				}
			} while (c.moveToNext());
		}
		c.close();
		db.close();
		list.setAdapter(arrayAdapter);
		arrayAdapter.notifyDataSetChanged();
	}
}
