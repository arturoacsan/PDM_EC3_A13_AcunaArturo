package com.example.tareassqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tareasqlite.database.DBAdapter;

import java.nio.channels.IllegalBlockingModeException;
import java.util.ArrayList;
import java.util.List;

public class AgregarTareas extends AppCompatActivity {

	Button btnFin, btnVer;
	TextView lblID;
	EditText txtCategory, txtSummary, txtDescription;
	long id = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agregar_tareas);

		btnFin = (Button) findViewById(R.id.btnFin);
		btnVer = (Button) findViewById(R.id.btnVer);
		txtCategory = (EditText) findViewById(R.id.txtCategory);
		txtSummary = (EditText) findViewById(R.id.txtSummary);
		txtDescription = (EditText) findViewById(R.id.txtDescription);
		lblID = (TextView) findViewById(R.id.lblID);
		DBAdapter dbHelper = new DBAdapter(this);
		DBAdapter db = dbHelper.open();
		Bundle extras = getIntent().getExtras();
		if (extras!=null) {
			cargarActualizar(extras,db);
		}

		//btnFin on Click
		btnFin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DBAdapter dbHelper = new DBAdapter(getApplicationContext());
				DBAdapter db = dbHelper.open();

				if (btnFin.getText() == "ACTUALIZAR") {
					if (db.updateTodo(id, txtCategory.getText().toString(), txtSummary.getText().toString(), txtDescription.getText().toString()) == false) {
						mensajeError("No se pudo actualizar");
					} else {
						desplegarTareas();
					}
				} else {
					if (db.createTodo(txtCategory.getText().toString(), txtSummary.getText().toString(), txtDescription.getText().toString()) == -1) {
						mensajeError("No se pudo agregar");
					} else {
						desplegarTareas();
					}
				}
			}
		});
		//btnVer on Click
		btnVer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),DesplegarTareas.class);
				startActivity(i);
			}
		});
	}

	public void desplegarTareas() {
		Intent i = new Intent(getApplicationContext(), DesplegarTareas.class);
		startActivity(i);
	}

	public void mensajeError(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);

	}
	public void cargarActualizar(Bundle extras, DBAdapter db){
		btnFin.setText("ACTUALIZAR");
		id = extras.getLong("ID");
		Cursor c = db.fetchTodo(id);
		if (c.moveToFirst()) {
			do {
				try {
					lblID.setText("ID: " + c.getString(0));
					txtCategory.setText(c.getString(1));
					txtSummary.setText(c.getString(2));
					txtDescription.setText(c.getString(3));
				} catch (Exception ex) {
					ex = ex;
				}
			} while (c.moveToNext());
		}
		c.close();
		db.close();
	}
}
