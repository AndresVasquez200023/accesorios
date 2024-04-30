package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Inicio extends AppCompatActivity {

    private TextView nombreUsuarioTextView;
    private TextView FechaActual;

    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio);

        // Cacheo de la vista del nombre de usuario
        nombreUsuarioTextView = findViewById(R.id.NombreUsuario);
        FechaActual = findViewById(R.id.fecha);
        btnNext = findViewById(R.id.btnNext);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(Inicio.this, MainActivity3.class);
            startActivity(intent);
        });

        // Mostrar el nombre de usuario
        mostrarNombreUsuario();
        mostrarFechaActual();
    }

    private void mostrarNombreUsuario() {
        String nombreUsuario = obtenerNombreUsuario();
        // Uso de recursos optimizado
        nombreUsuarioTextView.setText(getString(R.string.welcome_message, nombreUsuario));
    }
    private void mostrarFechaActual() {
        // Obtiene la fecha actual
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String fechaActual = dateFormat.format(new Date());

        // Muestra la fecha actual en el TextView
        FechaActual.setText(fechaActual);
    }

    private String obtenerNombreUsuario() {
        return getIntent().getStringExtra("nombreUsuario");
    }
}

