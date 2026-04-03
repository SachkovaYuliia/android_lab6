package com.example.lab6;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private EditText inputText;
    private Button analyzeButton;
    private TextView resultTextView;

    // Простий словник для оцінки емоційності
    private String[] positiveWords = {"гарний","чудовий","радість","щастя","успіх","красивий"};
    private String[] negativeWords = {"поганий","сумний","проблема","невдача","страх","жаль"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText = findViewById(R.id.inputText);
        analyzeButton = findViewById(R.id.analyzeButton);
        resultTextView = findViewById(R.id.resultTextView);

        analyzeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analyzeText();
            }
        });
    }

    private void analyzeText() {
        String text = inputText.getText().toString().toLowerCase().trim();
        if(text.isEmpty()) {
            resultTextView.setText("Будь ласка, введіть текст для аналізу.");
            return;
        }

        // 1. Підрахунок слів
        String[] words = text.split("\\W+");
        int wordCount = words.length;

        // 2. Підрахунок частоти кожного слова
        HashMap<String, Integer> freqMap = new HashMap<>();
        for(String word : words) {
            if(freqMap.containsKey(word)) {
                freqMap.put(word, freqMap.get(word)+1);
            } else {
                freqMap.put(word, 1);
            }
        }

        // 3. Визначення топ-5 слів
        List<Map.Entry<String, Integer>> list = new ArrayList<>(freqMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String,Integer>>() {
            @Override
            public int compare(Map.Entry<String,Integer> o1, Map.Entry<String,Integer> o2) {
                return o2.getValue() - o1.getValue(); // сортування за спаданням
            }
        });

        StringBuilder topWords = new StringBuilder();
        int topCount = Math.min(5, list.size());
        for(int i=0; i<topCount; i++) {
            topWords.append(list.get(i).getKey()).append(" (").append(list.get(i).getValue()).append(") ");
        }

        // 4. Оцінка емоційності
        int positiveScore = 0;
        int negativeScore = 0;
        for(String word : words) {
            for(String p : positiveWords) {
                if(word.equals(p)) positiveScore++;
            }
            for(String n : negativeWords) {
                if(word.equals(n)) negativeScore++;
            }
        }

        String sentiment = "Нейтральний";
        if(positiveScore > negativeScore) sentiment = "Позитивний";
        else if(negativeScore > positiveScore) sentiment = "Негативний";

        // 5. Вивід результатів
        String result = "Кількість слів: " + wordCount + "\n" +
                "Топ-5 слів: " + topWords.toString() + "\n" +
                "Емоційність тексту: " + sentiment;
        resultTextView.setText(result);
    }
}