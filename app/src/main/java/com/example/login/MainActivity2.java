package com.example.login;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity2 extends AppCompatActivity {

    EditText nombre;
    EditText correo;
    EditText contrasenaR;
    EditText confirmar_contrasena;
    Button regis;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this); // Esta línea puede no ser necesaria en este contexto
        setContentView(R.layout.activity_main2);

        Button registrarButton = findViewById(R.id.regIniciar);
        nombre = findViewById(R.id.nombre);
        correo = findViewById(R.id.correo);
        contrasenaR = findViewById(R.id.contrasenaR);
        confirmar_contrasena = findViewById(R.id.confirmar_contrasena);
        regis = findViewById(R.id.Regis);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance(); // Inicializar FirebaseFirestore

        registrarButton.setOnClickListener(v -> {
            // Crear un intent para la segunda actividad
            Intent intent = new Intent(MainActivity2.this, MainActivity.class);
            // Iniciar la segunda actividad
            startActivity(intent);
            finish(); // Cierra MainActivity
        });

        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        String name = nombre.getText().toString();
        String email = correo.getText().toString();
        String password = contrasenaR.getText().toString();
        String confirmPassword = confirmar_contrasena.getText().toString();

        if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {
            if (isEmailValid(email)) {
                if (password.equals(confirmPassword)) {
                    if (password.length() >= 3) {
                        createUser(name, email, password);
                    } else {
                        Toast.makeText(MainActivity2.this, "La contraseña debe tener al menos 3 caracteres", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity2.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity2.this, "El correo no es válido", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity2.this, "Para continuar, ingrese todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void createUser(String name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String id = mAuth.getCurrentUser().getUid();
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", name);
                    map.put("email", email);
                    map.put("password", password);
                    mFirestore.collection("Users").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent i = new Intent(MainActivity2.this, MainActivity.class);
                                startActivity(i);
                                Toast.makeText(MainActivity2.this, "El usuario se guardó correctamente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity2.this, "No se pudo registrar el usuario, intente de nuevo ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

