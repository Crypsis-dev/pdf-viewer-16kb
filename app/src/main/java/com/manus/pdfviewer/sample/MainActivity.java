package com.manus.pdfviewer.sample;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.shahnexus.pdfviewer.PDFView;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PDFView pdfView = findViewById(R.id.pdfView);
        
        // In a real app, you would have a PDF in assets or storage
        // For this sample, we just initialize the view
        // try {
        //     pdfView.fromAsset("sample.pdf");
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    }
}
