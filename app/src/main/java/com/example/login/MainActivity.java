package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login.Inicio;
import com.example.login.MainActivity2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    EditText correo;
    EditText contrasena;
    Button ing;
    Button registrarButton;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        correo = findViewById(R.id.correo);
        contrasena = findViewById(R.id.contrasena);
        ing = findViewById(R.id.ing);
        registrarButton = findViewById(R.id.reg);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance(); // Inicializar FirebaseFirestore

        ing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                login();
            }
        });

        registrarButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            startActivity(intent);
            finish(); // Cierra MainActivity
        });
    }

    void login() {
        String emailLogin = correo.getText().toString().trim();
        String password = contrasena.getText().toString().trim();

        if (emailLogin.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(emailLogin, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Obtener el usuario actualmente autenticado
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Obtener el nombre de usuario del usuario autenticado desde Firestore
                            String userId = user.getUid();
                            DocumentReference docRef = mFirestore.collection("Users").document(userId);
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            String nombreUsuario = document.getString("name");
                                            if (nombreUsuario == null) {
                                                nombreUsuario = user.getEmail();
                                            }
                                            Intent i = new Intent(MainActivity.this, Inicio.class);
                                            i.putExtra("nombreUsuario", nombreUsuario);
                                            startActivity(i);
                                        } else {
                                            Toast.makeText(MainActivity.this, "No se encontró el nombre de usuario", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(MainActivity.this, "Error al obtener el nombre de usuario", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            // Si el usuario es nulo, mostrar un mensaje de error
                            Toast.makeText(MainActivity.this, "No se pudo obtener el usuario autenticado", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "El correo o contraseña son incorrectos", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}


















