package sfami.softwares.waitfordownload;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

// TODO: Permissions for Internet, and Read and Write to Storage.

public class MainActivity extends AppCompatActivity {

    Button downloadButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadButton = findViewById(R.id.downloadBtn);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call download task
                new DownloadFileTask().execute();
            }
        });
    }


    private class DownloadFileTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.R)
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("https://www.cs.cmu.edu/afs/cs/academic/class/15780-s16/www/slides/prob_models.pdf");
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);

                // Connect
                urlConnection.connect();

                // Choose where to save the file
                File internalStorageRoot = Environment.getExternalStorageDirectory();

                // Create new file to be saved
                File file = new File(internalStorageRoot, "textbook.pdf");
                FileOutputStream fileOutput = new FileOutputStream(file);

                // Stream for reading the data from the internet
                InputStream inputStream = urlConnection.getInputStream();

                // Create a buffer
                byte[] buffer = new byte[124];
                int bufferLength = 0;

                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutput.write(buffer, 0 , bufferLength);
                }

                fileOutput.close();
            }
            catch (final MalformedURLException e) {
                showError(e.getMessage());
                e.printStackTrace();
            }
            catch (final IOException e) {
                showError(e.getMessage());
                e.printStackTrace();
            }
            catch (final Exception e) {
                showError("Error: Please check your internet connection " + e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Toast.makeText(MainActivity.this, "Done Executing....\nDownload Completed.", Toast.LENGTH_SHORT).show();
            // Toast.makeText(MainActivity.this, "Download Successful", Toast.LENGTH_SHORT).show();
        }

        void showError(final String err) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, err, Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

}