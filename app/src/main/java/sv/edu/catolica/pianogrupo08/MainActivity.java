package sv.edu.catolica.pianogrupo08;

// ----- IMPORTACIONES ACTUALIZADAS -----
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    // ----- PASO 1: REEMPLAZAR MEDIAPLAYER CON SOUNDPOOL -----
    private SoundPool soundPool;
    private SparseIntArray soundMap;
    private boolean soundsLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupPianoKeys();

        // ----- PASO 2: INICIALIZAR SOUNDPOOL Y CARGAR LOS SONIDOS -----
        initializeSoundPool();
    }

    private void initializeSoundPool() {
        // Configura los atributos de audio para baja latencia (ideal para juegos y apps interactivas)
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        // Inicializa el SoundPool
        // setMaxStreams(7) permite que las 7 notas suenen simultáneamente si se tocan muy rápido
        soundPool = new SoundPool.Builder()
                .setMaxStreams(7)
                .setAudioAttributes(audioAttributes)
                .build();

        // Espera a que los sonidos se carguen para evitar errores
        soundPool.setOnLoadCompleteListener((sp, sampleId, status) -> {
            if (status == 0) {
                // Si el sonido se cargó correctamente, marcamos que estamos listos
                // Para una app simple, podemos asumir que todos se cargarán bien.
                soundsLoaded = true;
            } else {
                Toast.makeText(MainActivity.this, "Error al cargar un sonido", Toast.LENGTH_SHORT).show();
            }
        });

        // Crea un mapa para asociar los recursos (R.raw.nota_do) con un ID de SoundPool
        soundMap = new SparseIntArray();
        soundMap.put(R.raw.nota_do, soundPool.load(this, R.raw.nota_do, 1));
        soundMap.put(R.raw.nota_re, soundPool.load(this, R.raw.nota_re, 1));
        soundMap.put(R.raw.nota_mi, soundPool.load(this, R.raw.nota_mi, 1));
        soundMap.put(R.raw.nota_fa, soundPool.load(this, R.raw.nota_fa, 1));
        soundMap.put(R.raw.nota_sol, soundPool.load(this, R.raw.nota_sol, 1));
        soundMap.put(R.raw.nota_la, soundPool.load(this, R.raw.nota_la, 1));
        soundMap.put(R.raw.nota_si, soundPool.load(this, R.raw.nota_si, 1));
    }


    private void setupPianoKeys() {
        // Tecla Do
        MaterialCardView teclaDo = findViewById(R.id.tecla_Do);
        teclaDo.setOnClickListener(v -> {
            animateKeyPress(v);
            playSound(R.raw.nota_do, "Do");
        });

        // Tecla Re
        MaterialCardView teclaRe = findViewById(R.id.tecla_Re);
        teclaRe.setOnClickListener(v -> {
            animateKeyPress(v);
            playSound(R.raw.nota_re, "Re");
        });

        // Tecla Mi
        MaterialCardView teclaMi = findViewById(R.id.tecla_Mi);
        teclaMi.setOnClickListener(v -> {
            animateKeyPress(v);
            playSound(R.raw.nota_mi, "Mi");
        });

        // Tecla Fa
        MaterialCardView teclaFa = findViewById(R.id.tecla_Fa);
        teclaFa.setOnClickListener(v -> {
            animateKeyPress(v);
            playSound(R.raw.nota_fa, "Fa");
        });

        // Tecla Sol
        MaterialCardView teclaSol = findViewById(R.id.tecla_Sol);
        teclaSol.setOnClickListener(v -> {
            animateKeyPress(v);
            playSound(R.raw.nota_sol, "Sol");
        });

        // Tecla La
        MaterialCardView teclaLa = findViewById(R.id.tecla_La);
        teclaLa.setOnClickListener(v -> {
            animateKeyPress(v);
            playSound(R.raw.nota_la, "La");
        });

        // Tecla Si
        MaterialCardView teclaSi = findViewById(R.id.tecla_Si);
        teclaSi.setOnClickListener(v -> {
            animateKeyPress(v);
            playSound(R.raw.nota_si, "Si");
        });
    }

    private void animateKeyPress(View v) {
        v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(50).withEndAction(() -> {
            v.animate().scaleX(1f).scaleY(1f).setDuration(50);
        });
    }

    // ----- PASO 3: REEMPLAZAR EL MÉTODO playSound -----
    private void playSound(int soundResource, String noteName) {
        if (soundsLoaded) {
            int soundId = soundMap.get(soundResource);
            // Parámetros: soundID, leftVolume, rightVolume, priority, loop, rate
            soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
            Toast.makeText(this, "Nota: " + noteName, Toast.LENGTH_SHORT).show();
        } else {
            // Este mensaje solo debería aparecer si se toca una tecla antes de que los sonidos carguen
            Toast.makeText(this, "Sonidos aún no cargados", Toast.LENGTH_SHORT).show();
        }
    }

    // ----- PASO 4: ACTUALIZAR onDestroy y exitApp PARA LIBERAR SOUNDPOOL -----
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }

    // (El resto de tus métodos de menú permanecen igual)

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_change_piano) {
            showChangePianoDialog();
            return true;
        } else if (itemId == R.id.action_about) {
            // Intent intent = new Intent(this, AboutActivity.class);
            // startActivity(intent);
            return true;
        } else if (itemId == R.id.action_exit) {
            exitApp();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showChangePianoDialog() {
        final String[] pianoTypes = {"Piano Tradicional", "Piano Infantil de la Selva", "Piano de Instrumentos Musicales"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona un tipo de piano");
        builder.setItems(pianoTypes, (dialog, which) -> {
            String selectedPiano = pianoTypes[which];
            Toast.makeText(MainActivity.this, "Cambiando a: " + selectedPiano, Toast.LENGTH_SHORT).show();
            // Aquí iría la lógica para cambiar los sonidos,
            // por ejemplo, podrías volver a llamar a initializeSoundPool con otros archivos de sonido.
        });
        builder.show();
    }

    private void exitApp() {
        // Libera los recursos de SoundPool antes de cerrar
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
        finishAffinity();
    }
}
