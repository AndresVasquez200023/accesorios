// En el archivo MainActivityTest.java
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class MainActivityTest {

    private MainActivity mainActivity;

    @Before
    public void setup() {
        mainActivity = new MainActivity();
    }

    @Test
    public void testLoginButtonClicked() {
        // Verificar que los elementos no sean nulos
        assertNotNull(mainActivity.correo);
        assertNotNull(mainActivity.contrasena);
        assertNotNull(mainActivity.ing);

        // Simular que se ingresan un correo y una contraseña
        mainActivity.correo.setText("correo@example.com");
        mainActivity.contrasena.setText("contraseña");

        // Llamar al método login directamente
        mainActivity.login();
    }
}



