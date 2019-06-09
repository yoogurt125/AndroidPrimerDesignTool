package com.example.primerdesigntool;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.graphics.Typeface.BOLD;

public class MainActivity extends AppCompatActivity {
    
    TextView textViewTemperature;
    EditText editTextSequence;
    EditText editTextIndex;
    Button buttonChangeSequence;
    TextView textViewChangedSequence;
    TextView textViewPrimer;
    Button buttonAddLeft;
    Button buttonRemoveLeft;
    Button buttonAddRight;
    Button buttonRemoveRight;
    TextView textViewInfo;
    Button buttonIndex1;
    Button buttonIndex2;
    Button buttonIndex3;
    TextView textViewNumberOfCharacters;

    int idxButton1, idxButton2, idxButton3, idxTriple;
    int idx = 0;
    int idxRight = 0;
    int idxLeft = 0;
    String sequence = "";
    String changedSequence = "";
    String tempSequence = "";
    String replacement = "";
    int mismatch = 0;
    double percentageGC;
    int nNumberOfCharacters = 0;
    double mismatchPercent = 0;
    double temperature = 0;

    public int differentCharacterCounter(String str1, String str2) {
        int licznik = 0;
        if (str1 == null) {
            return -1;
        }
        if (str2 == null) {
            return -1;
        }
        for (int i = 0; i < str1.length(); i++) {

            char chA = str1.charAt(i);
            char chB = str2.charAt(i);
            if (chA != chB) {
                licznik++;
            }
        }
        return licznik;
    }

    public double charactersGandCPercentage(String str1) {
        if (str1 == null) {
            return -1;
        }
        double licznik = 0;

        for (int i = 0; i < str1.length(); i++) {
            char znak = str1.charAt(i);
            if (znak == 'G' || znak == 'C') {
                licznik++;
            }
        }
        double wynik_procent = licznik / (double) (str1.length()) * 100;
        return wynik_procent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonChangeSequence = findViewById(R.id.button_change_sequence);
        editTextSequence = findViewById(R.id.editText_sequence);
        editTextIndex = findViewById(R.id.editText_index);
        buttonIndex1 = findViewById(R.id.button_index1);
        buttonIndex2 = findViewById(R.id.button_index2);
        buttonIndex3 = findViewById(R.id.button_index3);
        buttonChangeSequence = findViewById(R.id.button_change_sequence);
        textViewChangedSequence = findViewById(R.id.editText_changed_sequence);
        textViewPrimer = findViewById(R.id.editText_primer);
        buttonAddLeft = findViewById(R.id.button_add_left);
        buttonRemoveLeft = findViewById(R.id.button_remove_left);
        buttonAddRight = findViewById(R.id.button_add_right);
        buttonRemoveRight = findViewById(R.id.button_remove_right);
        textViewInfo = findViewById(R.id.textView_info);
        textViewTemperature = findViewById(R.id.textView_temperature);
        textViewNumberOfCharacters = findViewById(R.id.textView_numberOfCharacters);

        editTextSequence.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        updateInfo();


        buttonChangeSequence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (TextUtils.isEmpty(editTextSequence.getText().toString())) {
                    editTextSequence.setError("Cannot be empty");


                } else if (TextUtils.isEmpty(editTextIndex.getText().toString())) {
                    editTextIndex.setError("Cannot be empty");
                }else if(editTextIndex.getText().toString().equals("0")){

                    editTextIndex.setError("Cannot be 0");

                } else if (buttonIndex1.getText().toString().isEmpty() ||
                        buttonIndex2.getText().toString().isEmpty() ||
                        buttonIndex3.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this,
                            "You need to enter new amino acid sequence",
                            Toast.LENGTH_SHORT).show();
                } else {

                    sequence = editTextSequence.getText().toString();

                    int x = Integer.parseInt(editTextIndex.getText().toString());
                    int y = sequence.length();

                    idx = (Integer.parseInt(editTextIndex.getText().toString())) * 3 - 2;
                    idxTriple = (Integer.parseInt(editTextIndex.getText().toString()));
                    idxRight = idx + 2;
                    idxLeft = idx - 1;


                    if (x*3 > y) {
                        Toast.makeText(MainActivity.this, "Amino acid index is out of range", Toast.LENGTH_LONG).show();

                    } else {
                        replacement = buttonIndex1.getText().toString() + buttonIndex2.getText().toString()
                                + buttonIndex3.getText().toString();

                        changedSequence = sequence.substring(0, idx - 1) + replacement + sequence.substring(idx + 2, sequence.length());

                        SpannableStringBuilder changedSequenceSpannable = new SpannableStringBuilder(changedSequence);
                        changedSequenceSpannable.setSpan(new android.text.style.StyleSpan(BOLD), idxLeft, idxRight,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        textViewChangedSequence.setText(changedSequenceSpannable);

                        mismatch = differentCharacterCounter(sequence, changedSequence);
                        tempSequence = changedSequence.substring(idx - 1, idx + 2);
                        nNumberOfCharacters = tempSequence.length();
                        mismatchPercent = (double) mismatch / (double) nNumberOfCharacters * 100;
                        percentageGC = charactersGandCPercentage(tempSequence);
                        temperature = 81.5 + 0.41 * (percentageGC) - (double) (675 / nNumberOfCharacters) - mismatchPercent;

                        textViewChangedSequence.setEnabled(true);
                        updateInfo();
                    }
                }

            }
        });

        buttonAddLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idxLeft >= 1) {

                    tempSequence = changedSequence.charAt(idxLeft - 1) + tempSequence;
                    textViewPrimer.setText(tempSequence);
                    nNumberOfCharacters = tempSequence.length();
                    mismatchPercent = (double) mismatch / (double) nNumberOfCharacters * 100;
                    percentageGC = charactersGandCPercentage(tempSequence);
                    temperature = 81.5 + 0.41 * (percentageGC) - (double) (675 / nNumberOfCharacters) - mismatchPercent;
                    idxLeft--;
                }
                textViewPrimer.setText(tempSequence);
                updateInfo();
            }
        });
        buttonAddRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idxRight < changedSequence.length()) {

                    tempSequence = tempSequence + changedSequence.charAt(idxRight);
                    textViewPrimer.setText(tempSequence);
                    nNumberOfCharacters = tempSequence.length();
                    mismatchPercent = (double) mismatch / (double) nNumberOfCharacters * 100;
                    percentageGC = charactersGandCPercentage(tempSequence);
                    temperature = 81.5 + 0.41 * (percentageGC) - (double) (675 / nNumberOfCharacters) - mismatchPercent;
                    idxRight++;
                }
                textViewPrimer.setText(tempSequence);
                updateInfo();
            }
        });

        buttonRemoveLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idxLeft < idx - 1) {
                    tempSequence = tempSequence.substring(1, tempSequence.length());
                    mismatchPercent = (double) mismatch / (double) nNumberOfCharacters * 100;
                    nNumberOfCharacters = tempSequence.length();
                    percentageGC = charactersGandCPercentage(tempSequence);
                    temperature = 81.5 + 0.41 * (percentageGC) - (double) (675 / nNumberOfCharacters) - mismatchPercent;
                    idxLeft++;
                }
                textViewPrimer.setText(tempSequence);
                updateInfo();
            }
        });
        buttonRemoveRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idxRight > idx + 2) {
                    tempSequence = tempSequence.substring(0, tempSequence.length() - 1);
                    mismatchPercent = (double) mismatch / (double) nNumberOfCharacters * 100;
                    nNumberOfCharacters = tempSequence.length();
                    percentageGC = charactersGandCPercentage(tempSequence);
                    temperature = 81.5 + 0.41 * (percentageGC) - (double) (675 / nNumberOfCharacters) - mismatchPercent;
                    idxRight--;
                }
                textViewPrimer.setText(tempSequence);
                updateInfo();
            }
        });


        buttonIndex1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idxButton1++;
                switch (idxButton1) {
                    case 1:
                        buttonIndex1.setText("G");
                        break;
                    case 2:
                        buttonIndex1.setText("T");
                        break;
                    case 3:
                        buttonIndex1.setText("C");
                        break;
                    case 4:
                        buttonIndex1.setText("A");
                        idxButton1 = 0;
                        break;
                }
            }
        });
        buttonIndex2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idxButton2++;
                switch (idxButton2) {
                    case 1:
                        buttonIndex2.setText("G");
                        break;
                    case 2:
                        buttonIndex2.setText("T");
                        break;
                    case 3:
                        buttonIndex2.setText("C");
                        break;
                    case 4:
                        buttonIndex2.setText("A");
                        idxButton2 = 0;
                        break;
                }
            }
        });
        buttonIndex3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idxButton3++;
                switch (idxButton3) {
                    case 1:
                        buttonIndex3.setText("G");
                        break;
                    case 2:
                        buttonIndex3.setText("T");
                        break;
                    case 3:
                        buttonIndex3.setText("C");
                        break;
                    case 4:
                        buttonIndex3.setText("A");
                        idxButton3 = 0;
                        break;
                }
            }
        });

    }

    void updateInfo() {


        textViewPrimer.setText(tempSequence);
        textViewInfo.setText(" Mismatch: " + mismatch +
                "\n Mismatch %: " + mismatchPercent + "\n GC %: " + percentageGC);


        String temperatureString = " Temperature: " + temperature;
        String numberOfCharactersString = " Number of Characters: " + nNumberOfCharacters;


        if (nNumberOfCharacters >= 25 && nNumberOfCharacters <= 45) {
            SpannableStringBuilder changedSequenceSpannable = new SpannableStringBuilder(numberOfCharactersString);
            changedSequenceSpannable.setSpan(new ForegroundColorSpan(Color.GREEN), 23, numberOfCharactersString.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            textViewNumberOfCharacters.setText(changedSequenceSpannable);
            // textViewTemperature.setText(" Temperature: " + temperature);

        } else {
            SpannableStringBuilder changedSequenceSpannable = new SpannableStringBuilder(numberOfCharactersString);
            changedSequenceSpannable.setSpan(new ForegroundColorSpan(Color.RED), 23, numberOfCharactersString.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            textViewNumberOfCharacters.setText(changedSequenceSpannable);
        }

        if (temperature >= 78) {
            SpannableStringBuilder changedSequenceSpannable = new SpannableStringBuilder(temperatureString);
            changedSequenceSpannable.setSpan(new ForegroundColorSpan(Color.GREEN), 14, temperatureString.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            textViewTemperature.setText(changedSequenceSpannable);
            // textViewTemperature.setText(" Temperature: " + temperature);

        } else {
            SpannableStringBuilder changedSequenceSpannable = new SpannableStringBuilder(temperatureString);
            changedSequenceSpannable.setSpan(new ForegroundColorSpan(Color.RED), 14, temperatureString.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            textViewTemperature.setText(changedSequenceSpannable);
        }



    }
}
