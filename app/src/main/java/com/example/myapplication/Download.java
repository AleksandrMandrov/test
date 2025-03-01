package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Download extends AppCompatActivity {

    ImageView imageForDownload;

    TextView errorMessage;

    AutoCompleteTextView autoCompleteDiscipline;
    AutoCompleteTextView autoCompleteAuthor;
    AutoCompleteTextView autoCompleteTheme;
    AutoCompleteTextView autoCompleteTheme2;
    AutoCompleteTextView autoCompleteTheme3;
    AutoCompleteTextView autoCompleteTheme4;
    AutoCompleteTextView autoCompleteTheme5;

    TextView descriptionTheme2;
    TextView descriptionTheme3;
    TextView descriptionTheme4;
    TextView descriptionTheme5;

    ImageButton newTheme;
    ImageButton newTheme2;
    ImageButton newTheme3;
    ImageButton newTheme4;

    ImageButton deleteTheme2;
    ImageButton deleteTheme3;
    ImageButton deleteTheme4;
    ImageButton deleteTheme5;

    TextView textFile;
    TextView descriptionSection;
    TextView descriptionChapter;
    TextView descriptionParagraph;
    TextView descriptionNumberOfPublication;
    TextView descriptionNumberOfTask;
    TextView descriptionNumberOfVariant;
    EditText editTextTextSection;
    EditText editTextChapter;
    EditText editTextParagraph;
    EditText editTextNumberOfPublication;
    EditText editTextNumberOfTask;
    EditText editTextNumberOfVariant;
    TextView helpSection;
    TextView helpChapter;
    TextView helpParagraph;
    TextView helpNumberOfPublication;
    TextView helpNumberOfVariant;
    TextView helpNumberOfTask;

    EditText editTextDecision;
    Button btnCreatingSolution;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;

    Spinner typeOfWork;

    private static final int PICKFILE_RESULT_CODE = 1;

    int scenario = 0;

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        imageForDownload = (ImageView) findViewById(R.id.imageForDownload);

        errorMessage = (TextView) findViewById(R.id.errorMessage);

        autoCompleteDiscipline = (AutoCompleteTextView) findViewById(R.id.autoCompleteDiscipline);
        autoCompleteAuthor = (AutoCompleteTextView) findViewById(R.id.autoCompleteAuthor);

        autoCompleteTheme = (AutoCompleteTextView) findViewById(R.id.autoCompleteTheme);
        autoCompleteTheme2 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTheme2);
        autoCompleteTheme3 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTheme3);
        autoCompleteTheme4 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTheme4);
        autoCompleteTheme5 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTheme5);

        descriptionTheme2 = (TextView) findViewById(R.id.descriptionTheme2);
        descriptionTheme3 = (TextView) findViewById(R.id.descriptionTheme3);
        descriptionTheme4 = (TextView) findViewById(R.id.descriptionTheme4);
        descriptionTheme5 = (TextView) findViewById(R.id.descriptionTheme5);

        newTheme = (ImageButton) findViewById(R.id.newTheme);
        newTheme2 = (ImageButton) findViewById(R.id.newTheme2);
        newTheme3 = (ImageButton) findViewById(R.id.newTheme3);
        newTheme4 = (ImageButton) findViewById(R.id.newTheme4);

        deleteTheme2 = (ImageButton) findViewById(R.id.deleteTheme2);
        deleteTheme3 = (ImageButton) findViewById(R.id.deleteTheme3);
        deleteTheme4 = (ImageButton) findViewById(R.id.deleteTheme4);
        deleteTheme5 = (ImageButton) findViewById(R.id.deleteTheme5);

        btnCreatingSolution = (Button) findViewById(R.id.btnCreatingSolution);

        descriptionSection = (TextView) findViewById(R.id.descriptionSection);
        editTextTextSection = (EditText) findViewById(R.id.editTextTextSection);
        helpSection = (TextView) findViewById(R.id.helpSection);

        descriptionChapter = (TextView) findViewById(R.id.descriptionChapter);
        editTextChapter = (EditText) findViewById(R.id.editTextChapter);
        helpChapter = (TextView) findViewById(R.id.helpChapter);

        descriptionParagraph = (TextView) findViewById(R.id.descriptionParagraph);
        editTextParagraph = (EditText) findViewById(R.id.editTextParagraph);
        helpParagraph = (TextView) findViewById(R.id.helpParagraph);

        descriptionNumberOfPublication = (TextView) findViewById(R.id.descriptionNumberOfPublication);
        editTextNumberOfPublication = (EditText) findViewById(R.id.editTextNumberOfPublication);
        helpNumberOfPublication = (TextView) findViewById(R.id.helpNumberOfPublication);

        descriptionNumberOfTask = (TextView) findViewById(R.id.descriptionNumberOfTask);
        editTextNumberOfTask = (EditText) findViewById(R.id.editTextNumberOfTask);
        helpNumberOfTask = (TextView) findViewById(R.id.helpNumberOfTask);


        descriptionNumberOfVariant = (TextView) findViewById(R.id.descriptionNumberOfVariant);
        editTextNumberOfVariant = (EditText) findViewById(R.id.editTextNumberOfVariant);
        helpNumberOfVariant = (TextView) findViewById(R.id.helpNumberOfVariant);

        editTextDecision = (EditText) findViewById(R.id.editTextDecision);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        // создаем базу данных
        databaseHelper.create_db();
        db = databaseHelper.open();
        /*setContentView(R.layout.activity_main);*/




        //Код с автозаполнением дисциплины----------------------------------------------------------
        AutoCompleteTextView autoCompleteDiscipline = findViewById(R.id.autoCompleteDiscipline);

        Cursor cursor = db.query(DatabaseHelper.TABLE_DISCIPLINES, null, null, null, null, null, null);
        ArrayList<String> discipline = new ArrayList<String>();

        if (cursor.moveToFirst()) {
            int textDiscipline = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_DISCIPLINE);

            do {
                discipline.add(cursor.getString(textDiscipline));
            } while (cursor.moveToNext());
        }

        ArrayAdapter<String> userAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, discipline);

        autoCompleteDiscipline.setAdapter(userAdapter);
        cursor.close();
        //-----------------------------------------------------------------------------------------


        //Код с автозаполнением авторов-------------------------------------------------------------
        AutoCompleteTextView autoCompleteAuthor = findViewById(R.id.autoCompleteAuthor);

        cursor = db.query(DatabaseHelper.TABLE_AUTHORS, null, null, null, null, null, null);
        ArrayList<String> author = new ArrayList<String>();

        if (cursor.moveToFirst()) {
            int nameAuthor = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_AUTHOR);

            do {
                author.add(cursor.getString(nameAuthor));
            } while (cursor.moveToNext());
        }

        userAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, author);

        autoCompleteAuthor.setAdapter(userAdapter);
        cursor.close();
        //------------------------------------------------------------------------------------------

        //Код с автозаполнением тем-----------------------------------------------------------------
        AutoCompleteTextView autoCompleteTheme = findViewById(R.id.autoCompleteTheme);

        cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);
        ArrayList<String> theme = new ArrayList<String>();

        if (cursor.moveToFirst()) {
            int nameAuthor = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

            do {
                theme.add(cursor.getString(nameAuthor));
            } while (cursor.moveToNext());
        }

        userAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, theme);

        autoCompleteTheme.setAdapter(userAdapter);
        autoCompleteTheme2.setAdapter(userAdapter);
        autoCompleteTheme3.setAdapter(userAdapter);
        autoCompleteTheme4.setAdapter(userAdapter);
        autoCompleteTheme5.setAdapter(userAdapter);

        cursor.close();
        //------------------------------------------------------------------------------------------

        autoCompleteTheme2.setVisibility(View.GONE);
        autoCompleteTheme3.setVisibility(View.GONE);
        autoCompleteTheme4.setVisibility(View.GONE);
        autoCompleteTheme5.setVisibility(View.GONE);

        descriptionTheme2.setVisibility(View.GONE);
        descriptionTheme3.setVisibility(View.GONE);
        descriptionTheme4.setVisibility(View.GONE);
        descriptionTheme5.setVisibility(View.GONE);

        newTheme2.setVisibility(View.GONE);
        newTheme3.setVisibility(View.GONE);
        newTheme4.setVisibility(View.GONE);

        deleteTheme2.setVisibility(View.GONE);
        deleteTheme3.setVisibility(View.GONE);
        deleteTheme4.setVisibility(View.GONE);
        deleteTheme5.setVisibility(View.GONE);

        imageForDownload.setVisibility(View.GONE);

        //прикрепление пдф

        Button buttonPick = (Button)findViewById(R.id.btnFileAttaching);
       // textFile = (TextView)findViewById(R.id.textViewLink);

        buttonPick.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
            }});


        typeOfWork = (Spinner) findViewById(R.id.typeOfWork);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                String item = (String)parent.getItemAtPosition(position);

                newTheme.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        autoCompleteTheme2.setVisibility(View.VISIBLE);
                        descriptionTheme2.setVisibility(View.VISIBLE);
                        newTheme2.setVisibility(View.VISIBLE);
                        deleteTheme2.setVisibility(View.VISIBLE);

                    }
                });

                newTheme2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        autoCompleteTheme3.setVisibility(View.VISIBLE);
                        descriptionTheme3.setVisibility(View.VISIBLE);
                        newTheme3.setVisibility(View.VISIBLE);
                        deleteTheme3.setVisibility(View.VISIBLE);

                    }
                });

                newTheme3.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        autoCompleteTheme4.setVisibility(View.VISIBLE);
                        descriptionTheme4.setVisibility(View.VISIBLE);
                        newTheme4.setVisibility(View.VISIBLE);
                        deleteTheme4.setVisibility(View.VISIBLE);

                    }
                });

                newTheme4.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        autoCompleteTheme5.setVisibility(View.VISIBLE);
                        descriptionTheme5.setVisibility(View.VISIBLE);
                        deleteTheme5.setVisibility(View.VISIBLE);

                    }
                });

                deleteTheme5.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        autoCompleteTheme5.setVisibility(View.GONE);
                        descriptionTheme5.setVisibility(View.GONE);
                        deleteTheme5.setVisibility(View.GONE);

                    }
                });

                deleteTheme4.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        String buf = "";

                        if (autoCompleteTheme5.getVisibility() == View.VISIBLE) {
                            buf = autoCompleteTheme5.getText().toString();
                            autoCompleteTheme4.setText(buf);

                            autoCompleteTheme5.setVisibility(View.GONE);
                            autoCompleteTheme5.setText("");
                            descriptionTheme5.setVisibility(View.GONE);
                            deleteTheme5.setVisibility(View.GONE);
                        } else {
                            autoCompleteTheme4.setVisibility(View.GONE);
                            autoCompleteTheme4.setText("");
                            descriptionTheme4.setVisibility(View.GONE);
                            newTheme4.setVisibility(View.GONE);
                            deleteTheme4.setVisibility(View.GONE);
                        }

                    }
                });

                deleteTheme3.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        String buf = "";

                        if (autoCompleteTheme4.getVisibility() == View.VISIBLE) {
                            buf = autoCompleteTheme4.getText().toString();
                            autoCompleteTheme3.setText(buf);

                            if (autoCompleteTheme5.getVisibility() == View.VISIBLE) {
                                buf = autoCompleteTheme5.getText().toString();
                                autoCompleteTheme4.setText(buf);

                                autoCompleteTheme5.setVisibility(View.GONE);
                                autoCompleteTheme5.setText("");
                                descriptionTheme5.setVisibility(View.GONE);
                                deleteTheme5.setVisibility(View.GONE);
                            } else {
                                autoCompleteTheme4.setVisibility(View.GONE);
                                autoCompleteTheme4.setText("");
                                descriptionTheme4.setVisibility(View.GONE);
                                newTheme4.setVisibility(View.GONE);
                                deleteTheme4.setVisibility(View.GONE);
                            }
                        } else {
                            autoCompleteTheme3.setVisibility(View.GONE);
                            autoCompleteTheme3.setText("");
                            descriptionTheme3.setVisibility(View.GONE);
                            newTheme3.setVisibility(View.GONE);
                            deleteTheme3.setVisibility(View.GONE);
                        }

                    }
                });

                deleteTheme2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        String buf = "";

                        if (autoCompleteTheme3.getVisibility() == View.VISIBLE) {
                            buf = autoCompleteTheme3.getText().toString();
                            autoCompleteTheme2.setText(buf);

                            if (autoCompleteTheme4.getVisibility() == View.VISIBLE) {
                                buf = autoCompleteTheme4.getText().toString();
                                autoCompleteTheme3.setText(buf);

                                if (autoCompleteTheme5.getVisibility() == View.VISIBLE) {
                                    buf = autoCompleteTheme5.getText().toString();
                                    autoCompleteTheme4.setText(buf);

                                    autoCompleteTheme5.setVisibility(View.GONE);
                                    autoCompleteTheme5.setText("");
                                    descriptionTheme5.setVisibility(View.GONE);
                                    deleteTheme5.setVisibility(View.GONE);
                                } else {
                                    autoCompleteTheme4.setVisibility(View.GONE);
                                    autoCompleteTheme4.setText("");
                                    descriptionTheme4.setVisibility(View.GONE);
                                    newTheme4.setVisibility(View.GONE);
                                    deleteTheme4.setVisibility(View.GONE);
                                }
                            } else {
                                autoCompleteTheme3.setVisibility(View.GONE);
                                autoCompleteTheme3.setText("");
                                descriptionTheme3.setVisibility(View.GONE);
                                newTheme3.setVisibility(View.GONE);
                                deleteTheme3.setVisibility(View.GONE);
                            }
                        } else {
                            autoCompleteTheme2.setVisibility(View.GONE);
                            autoCompleteTheme2.setText("");
                            descriptionTheme2.setVisibility(View.GONE);
                            newTheme2.setVisibility(View.GONE);
                            deleteTheme2.setVisibility(View.GONE);
                        }

                    }
                });

//                textFile.setHint("");

                if (item.equals("Контрольная работа") || item.equals("Тест") || item.equals("Сессия") || item.equals("Лабораторные работы")) {

                    errorMessage.setVisibility(View.GONE);

                    editTextDecision.setHint("Текст вашего решения / Описание решения / комментарий к решению");
                    descriptionSection.setVisibility(View.GONE);
                    helpSection.setVisibility(View.GONE);
                    editTextTextSection.setVisibility(View.GONE);
                    editTextTextSection.setEnabled(false);

                    descriptionChapter.setVisibility(View.GONE);
                    helpChapter.setVisibility(View.GONE);
                    editTextChapter.setVisibility(View.GONE);
                    editTextChapter.setEnabled(false);

                    descriptionParagraph.setVisibility(View.GONE);
                    helpParagraph.setVisibility(View.GONE);
                    editTextParagraph.setVisibility(View.GONE);
                    editTextParagraph.setEnabled(false);

                    descriptionNumberOfPublication.setVisibility(View.GONE);
                    helpNumberOfPublication.setVisibility(View.GONE);
                    editTextNumberOfPublication.setVisibility(View.GONE);
                    editTextNumberOfPublication.setEnabled(false);

                    descriptionNumberOfTask.setVisibility(View.VISIBLE);
                    helpNumberOfTask.setVisibility(View.VISIBLE);
                    editTextNumberOfTask.setVisibility(View.VISIBLE);
                    editTextNumberOfTask.setEnabled(true);

                    descriptionNumberOfVariant.setVisibility(View.VISIBLE);
                    helpNumberOfVariant.setVisibility(View.VISIBLE);
                    editTextNumberOfVariant.setVisibility(View.VISIBLE);
                    editTextNumberOfVariant.setEnabled(true);

                    btnCreatingSolution.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            String autoCompleteDisciplineValue = autoCompleteDiscipline.getText().toString();
                            String autoCompleteAuthorValue = autoCompleteAuthor.getText().toString();
                            String autoCompleteThemeValue = autoCompleteTheme.getText().toString();
                            String autoCompleteThemeValue2 = autoCompleteTheme2.getText().toString();
                            String autoCompleteThemeValue3 = autoCompleteTheme3.getText().toString();
                            String autoCompleteThemeValue4 = autoCompleteTheme4.getText().toString();
                            String autoCompleteThemeValue5 = autoCompleteTheme5.getText().toString();

                            String error = "";
                            boolean errorFlag = true;

                            if (autoCompleteDisciplineValue.equals("")) {
                                error += "\n- нужно ввести название дисциплины";
                                errorFlag = false;
                            }

                            if (autoCompleteAuthorValue.equals("")) {
                                error += "\n- нужно ввести имя автора / преподователя";
                                errorFlag = false;
                            }

                            if (autoCompleteThemeValue.equals("")) {
                                error += "\n- нужно ввести название темы";
                                errorFlag = false;
                            }

                            if (autoCompleteThemeValue2.equals("") && (autoCompleteTheme2.getVisibility() == View.VISIBLE)) {
                                error += "\n- нужно ввести название темы 2";
                                errorFlag = false;
                            }

                            if (autoCompleteThemeValue3.equals("") && (autoCompleteTheme3.getVisibility() == View.VISIBLE)) {
                                error += "\n- нужно ввести название темы 3";
                                errorFlag = false;
                            }

                            if (autoCompleteThemeValue4.equals("") && (autoCompleteTheme4.getVisibility() == View.VISIBLE)) {
                                error += "\n- нужно ввести название темы 4";
                                errorFlag = false;
                            }

                            if (autoCompleteThemeValue5.equals("") && (autoCompleteTheme5.getVisibility() == View.VISIBLE)) {
                                error += "\n- нужно ввести название темы 5";
                                errorFlag = false;
                            }

                            boolean repeatingError = true;

                            if (!(autoCompleteThemeValue2.equals("")) && ( autoCompleteThemeValue2.equals(autoCompleteThemeValue)
                             || autoCompleteThemeValue2.equals(autoCompleteThemeValue3) || autoCompleteThemeValue2.equals(autoCompleteThemeValue4)
                                    || autoCompleteThemeValue2.equals(autoCompleteThemeValue5))) {
                                error += "\n- темы не могут иметь одинаковое название";
                                errorFlag = false;
                                repeatingError = false;
                            }

                            if (repeatingError && !(autoCompleteThemeValue3.equals("")) && ( autoCompleteThemeValue3.equals(autoCompleteThemeValue)
                                    || autoCompleteThemeValue3.equals(autoCompleteThemeValue4)
                                    || autoCompleteThemeValue3.equals(autoCompleteThemeValue5))) {
                                error += "\n- темы не могут иметь одинаковое название";
                                errorFlag = false;
                                repeatingError = false;
                            }

                            if (repeatingError && !(autoCompleteThemeValue4.equals("")) && ( autoCompleteThemeValue4.equals(autoCompleteThemeValue)
                                    || autoCompleteThemeValue4.equals(autoCompleteThemeValue5))) {
                                error += "\n- темы не могут иметь одинаковое название";
                                errorFlag = false;
                                repeatingError = false;
                            }

                            if (repeatingError && !(autoCompleteThemeValue4.equals("")) && autoCompleteThemeValue5.equals(autoCompleteThemeValue)) {
                                error += "\n- темы не могут иметь одинаковое название";
                                errorFlag = false;
                                repeatingError = false;
                            }

                            if (errorFlag) {

                                String regex = "^[а-яёА-ЯЁ\\t\\n\\f\\r\\p{Z}]*$";

                                Pattern pattern = Pattern.compile(regex);
                                Matcher m = pattern.matcher(autoCompleteDisciplineValue);
                                if (!m.matches()){
                                    error += "\n- введены недопустимые символы в названии дисциплины";
                                    errorFlag = false;
                                }

                                m = pattern.matcher(autoCompleteThemeValue);
                                if (!m.matches()){
                                    error += "\n- введены недопустимые символы в названии темы";
                                    errorFlag = false;
                                }

                                m = pattern.matcher(autoCompleteThemeValue2);
                                if (!(m.matches()) && (autoCompleteTheme2.getVisibility() == View.VISIBLE)){
                                    error += "\n- введены недопустимые символы в названии темы 2";
                                    errorFlag = false;
                                }

                                m = pattern.matcher(autoCompleteThemeValue3);
                                if (!(m.matches()) && (autoCompleteTheme3.getVisibility() == View.VISIBLE)){
                                    error += "\n- введены недопустимые символы в названии темы 3";
                                    errorFlag = false;
                                }

                                m = pattern.matcher(autoCompleteThemeValue4);
                                if (!(m.matches()) && (autoCompleteTheme4.getVisibility() == View.VISIBLE)){
                                    error += "\n- введены недопустимые символы в названии темы 4";
                                    errorFlag = false;
                                }

                                m = pattern.matcher(autoCompleteThemeValue5);
                                if (!(m.matches()) && (autoCompleteTheme5.getVisibility() == View.VISIBLE)){
                                    error += "\n- введены недопустимые символы в названии темы 5";
                                    errorFlag = false;
                                }

                                regex = "^[А-Я][а-я]+\\s?[А-Я]\\.\\s?[А-Я]\\.$";
                                pattern = Pattern.compile(regex);
                                m = pattern.matcher(autoCompleteAuthorValue);
                                if (!m.matches()){
                                    error += "\n- имя должно иметь формат: Маяковский В. В.";
                                    errorFlag = false;
                                }

                                if (errorFlag) {

                                    if (autoCompleteDisciplineValue.length() > 50) {
                                        error += "\n- название дисциплины слишком длинное";
                                        errorFlag = false;
                                    }

                                    if (autoCompleteAuthorValue.length() > 33) {
                                        error += "\n- введенное имя автора / преподователя слишком длинное";
                                        errorFlag = false;
                                    }

                                    if (autoCompleteThemeValue.length() > 50) {
                                        error += "\n- название темы слишком длинное";
                                        errorFlag = false;
                                    }

                                    if (autoCompleteThemeValue2.length() > 50) {
                                        error += "\n- название темы 2 слишком длинное";
                                        errorFlag = false;
                                    }

                                    if (autoCompleteThemeValue3.length() > 50) {
                                        error += "\n- название темы 3 слишком длинное";
                                        errorFlag = false;
                                    }

                                    if (autoCompleteThemeValue4.length() > 50) {
                                        error += "\n- название темы 4 слишком длинное";
                                        errorFlag = false;
                                    }

                                    if (autoCompleteThemeValue5.length() > 50) {
                                        error += "\n- название темы 5 слишком длинное";
                                        errorFlag = false;
                                    }

                                    if (errorFlag) {

                                        if (editTextNumberOfTask.getText().toString().equals("")) {
                                            error += "\n- нужно ввести номер задания";
                                            errorFlag = false;
                                        }

                                        if (editTextNumberOfVariant.getText().toString().equals("")) {
                                            error += "\n- нужно ввести номер варианта";
                                            errorFlag = false;
                                        }

                                        if (errorFlag) {

                                            regex = "[0-9]+";
                                            pattern = Pattern.compile(regex);

                                            m = pattern.matcher(editTextNumberOfTask.getText());
                                            if (!m.matches()){
                                                error += "\n- в номере задания можно использовать только цифры";
                                                errorFlag = false;
                                            }

                                            m = pattern.matcher(editTextNumberOfVariant.getText());
                                            if (!m.matches()){
                                                error += "\n- в номере варианта можно использовать только цифры";
                                                errorFlag = false;
                                            }

                                            if (errorFlag) {

                                                if (editTextNumberOfTask.getText().length() > 4) {
                                                    error += "\n- номер задания слишком большой";
                                                    errorFlag = false;
                                                }

                                                if (editTextNumberOfVariant.getText().length() > 3) {
                                                    error += "\n- номер варианта слишком большой";
                                                    errorFlag = false;
                                                }

                                                if (errorFlag) {

                                                    String editTextDecisionValue = editTextDecision.getText().toString();

                                                    if (editTextDecisionValue.equals("")) {
                                                        error += "\n- нужно ввести текст в поле решения";
                                                        errorFlag = false;
                                                    }

                                                    if (editTextDecisionValue.length() > 3000) {
                                                        error += "\n- текст решения слишком большой";
                                                        errorFlag = false;

                                                    }

                                                        if (errorFlag) {

                                                            //---------------------------------------------------
                                                            boolean noAppend = false;

                                                            Cursor cursor = db.query(DatabaseHelper.TABLE_DISCIPLINES, null, null, null, null, null, null);

                                                            if (cursor.moveToFirst()) {
                                                                int idNameDiscipline = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_DISCIPLINE);

                                                                do {
                                                                    if (cursor.getString(idNameDiscipline).equals(autoCompleteDisciplineValue)) {
                                                                        noAppend = true;
                                                                        break;
                                                                    }
                                                                } while (cursor.moveToNext());
                                                            }
                                                            cursor.close();

                                                            if (!noAppend) {
                                                                ContentValues cv = new ContentValues();
                                                                cv.put(DatabaseHelper.COLUMN_NAME_DISCIPLINE, autoCompleteDisciplineValue);
                                                                db.insert(DatabaseHelper.TABLE_DISCIPLINES, null, cv);
                                                            }
                                                            //---------------------------------------------------

                                                            //---------------------------------------------------
                                                            noAppend = false;

                                                            cursor = db.query(DatabaseHelper.TABLE_AUTHORS, null, null, null, null, null, null);

                                                            if (cursor.moveToFirst()) {
                                                                int idNameAuthor = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_AUTHOR);

                                                                do {
                                                                    if (cursor.getString(idNameAuthor).equals(autoCompleteAuthorValue)) {
                                                                        noAppend = true;
                                                                        break;
                                                                    }
                                                                } while (cursor.moveToNext());
                                                            }
                                                            cursor.close();

                                                            if (!noAppend) {
                                                                ContentValues cv = new ContentValues();
                                                                cv.put(DatabaseHelper.COLUMN_NAME_AUTHOR, autoCompleteAuthorValue);
                                                                db.insert(DatabaseHelper.TABLE_AUTHORS, null, cv);
                                                            }
                                                            //---------------------------------------------------

                                                            //---------------------------------------------------
                                                            noAppend = false;

                                                            cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                            if (cursor.moveToFirst()) {
                                                                int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                                do {
                                                                    if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue)) {
                                                                        noAppend = true;
                                                                        break;
                                                                    }
                                                                } while (cursor.moveToNext());
                                                            }
                                                            cursor.close();

                                                            if (!noAppend) {
                                                                ContentValues cv = new ContentValues();
                                                                cv.put(DatabaseHelper.COLUMN_THEME, autoCompleteThemeValue);
                                                                db.insert(DatabaseHelper.TABLE_THEMES, null, cv);
                                                            }
                                                            //---------------------------------------------------

                                                            //---------------------------------------------------

                                                            if (!autoCompleteThemeValue2.equals("")) {

                                                                noAppend = false;

                                                                cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                                if (cursor.moveToFirst()) {
                                                                    int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                                    do {
                                                                        if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue2)) {
                                                                            noAppend = true;
                                                                            break;
                                                                        }
                                                                    } while (cursor.moveToNext());
                                                                }
                                                                cursor.close();

                                                                if (!noAppend) {
                                                                    ContentValues cv = new ContentValues();
                                                                    cv.put(DatabaseHelper.COLUMN_THEME, autoCompleteThemeValue2);
                                                                    db.insert(DatabaseHelper.TABLE_THEMES, null, cv);
                                                                }

                                                            }
                                                            //---------------------------------------------------

                                                            //---------------------------------------------------

                                                            if (!autoCompleteThemeValue3.equals("")) {

                                                                noAppend = false;

                                                                cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                                if (cursor.moveToFirst()) {
                                                                    int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                                    do {
                                                                        if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue3)) {
                                                                            noAppend = true;
                                                                            break;
                                                                        }
                                                                    } while (cursor.moveToNext());
                                                                }
                                                                cursor.close();

                                                                if (!noAppend) {
                                                                    ContentValues cv = new ContentValues();
                                                                    cv.put(DatabaseHelper.COLUMN_THEME, autoCompleteThemeValue3);
                                                                    db.insert(DatabaseHelper.TABLE_THEMES, null, cv);
                                                                }

                                                            }
                                                            //---------------------------------------------------

                                                            //---------------------------------------------------

                                                            if (!autoCompleteThemeValue4.equals("")) {

                                                                noAppend = false;

                                                                cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                                if (cursor.moveToFirst()) {
                                                                    int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                                    do {
                                                                        if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue4)) {
                                                                            noAppend = true;
                                                                            break;
                                                                        }
                                                                    } while (cursor.moveToNext());
                                                                }
                                                                cursor.close();

                                                                if (!noAppend) {
                                                                    ContentValues cv = new ContentValues();
                                                                    cv.put(DatabaseHelper.COLUMN_THEME, autoCompleteThemeValue4);
                                                                    db.insert(DatabaseHelper.TABLE_THEMES, null, cv);
                                                                }

                                                            }
                                                            //---------------------------------------------------

                                                            //---------------------------------------------------

                                                            if (!autoCompleteThemeValue5.equals("")) {

                                                                noAppend = false;

                                                                cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                                if (cursor.moveToFirst()) {
                                                                    int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                                    do {
                                                                        if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue5)) {
                                                                            noAppend = true;
                                                                            break;
                                                                        }
                                                                    } while (cursor.moveToNext());
                                                                }
                                                                cursor.close();

                                                                if (!noAppend) {
                                                                    ContentValues cv = new ContentValues();
                                                                    cv.put(DatabaseHelper.COLUMN_THEME, autoCompleteThemeValue5);
                                                                    db.insert(DatabaseHelper.TABLE_THEMES, null, cv);
                                                                }

                                                            }
                                                            //---------------------------------------------------

                                                            //---------------------------------------------------


                                                            cursor = db.query(DatabaseHelper.TABLE_DISCIPLINES, null, null, null, null, null, null);

                                                            int id_discipline = 0;

                                                            if (cursor.moveToFirst()) {
                                                                id_discipline = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_DISCIPLINE);
                                                                int idName_discipline = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_DISCIPLINE);

                                                                do {
                                                                    if (cursor.getString(idName_discipline).equals(autoCompleteDisciplineValue)) {
                                                                        id_discipline = cursor.getInt(id_discipline);
                                                                        break;
                                                                    }
                                                                } while (cursor.moveToNext());
                                                            }
                                                            cursor.close();


                                                            cursor = db.query(DatabaseHelper.TABLE_AUTHORS, null, null, null, null, null, null);

                                                            int id_author = 0;

                                                            if (cursor.moveToFirst()) {
                                                                id_author = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_AUTHOR);
                                                                int idName_author = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_AUTHOR);

                                                                do {
                                                                    if (cursor.getString(idName_author).equals(autoCompleteAuthorValue)) {
                                                                        id_author = cursor.getInt(id_author);
                                                                        break;
                                                                    }
                                                                } while (cursor.moveToNext());
                                                            }
                                                            cursor.close();

                                                            int[] masIdThemes = new int[5];
                                                            int lenMasIdThemes = 0;

                                                            cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                            if (cursor.moveToFirst()) {
                                                                int id_theme = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_THEME);
                                                                int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                                do {

                                                                    if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue)) {
                                                                        masIdThemes[lenMasIdThemes] = cursor.getInt(id_theme);
                                                                        lenMasIdThemes++;
                                                                    }

                                                                    if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue2)) {
                                                                        masIdThemes[lenMasIdThemes] = cursor.getInt(id_theme);
                                                                        lenMasIdThemes++;
                                                                    }

                                                                    if ( cursor.getString(idNameTheme).equals(autoCompleteThemeValue3)) {
                                                                        masIdThemes[lenMasIdThemes] = cursor.getInt(id_theme);
                                                                        lenMasIdThemes++;
                                                                    }

                                                                    if ( cursor.getString(idNameTheme).equals(autoCompleteThemeValue4)) {
                                                                        masIdThemes[lenMasIdThemes] = cursor.getInt(id_theme);
                                                                        lenMasIdThemes++;
                                                                    }

                                                                    if ( cursor.getString(idNameTheme).equals(autoCompleteThemeValue5)) {
                                                                        masIdThemes[lenMasIdThemes] = cursor.getInt(id_theme);
                                                                        lenMasIdThemes++;
                                                                    }

                                                                } while (cursor.moveToNext());
                                                            }
                                                            cursor.close();


                                                            noAppend = false;

                                                            cursor = db.query(DatabaseHelper.TABLE_WORK_DESCRIPTION, null, null, null, null, null, null);

                                                            int id_work_description = 0;

                                                            if (cursor.moveToFirst()) {
                                                                id_work_description = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_WORK_DESCRIPTION);
                                                                int idType_of_work = cursor.getColumnIndex(DatabaseHelper.COLUMN_TYPE_OF_WORK);
                                                                int id_discipline2 = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_DISCIPLINE);
                                                                int id_author2 = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_AUTHOR);
                                                                int idTask_number = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_NUMBER);
                                                                int idOption_number = cursor.getColumnIndex(DatabaseHelper.COLUMN_OPTION_NUMBER);

                                                                do {
                                                                    if (cursor.getString(idType_of_work).equals(item) && cursor.getInt(id_discipline2) == id_discipline
                                                                            &&  cursor.getInt(id_author2) == id_author
                                                                            &&  cursor.getInt(idTask_number) == Integer.parseInt(editTextNumberOfTask.getText().toString())
                                                                            &&  cursor.getInt(idOption_number) == Integer.parseInt(editTextNumberOfVariant.getText().toString())) {

                                                                        int[] masIdThemes2 = new int[5];
                                                                        int lenMasIdThemes2 = 0;

                                                                        userCursor = db.query(DatabaseHelper.TABLE_THEME_OF_WORK, null, null, null, null, null, null);

                                                                        if (userCursor.moveToFirst()) {
                                                                            int id_theme = userCursor.getColumnIndex(DatabaseHelper.COLUMN_ID_THEME);
                                                                            int id_workDescription = userCursor.getColumnIndex(DatabaseHelper.COLUMN_ID_WORK_DESCRIPTION);

                                                                            do {
                                                                                if (userCursor.getInt(id_workDescription) == cursor.getInt(id_work_description)) {
                                                                                    masIdThemes2[lenMasIdThemes2] = userCursor.getInt(id_theme);
                                                                                    lenMasIdThemes2++;
                                                                                }
                                                                            } while (userCursor.moveToNext());
                                                                        }
                                                                        userCursor.close();

                                                                        boolean coincide = false;
                                                                        int partCoincide = 0;

                                                                        if (lenMasIdThemes2 == lenMasIdThemes) {
                                                                            for (int i =0; i < lenMasIdThemes2; i++) {
                                                                                for (int j = 0; j < lenMasIdThemes; j++) {
                                                                                    if (masIdThemes2[i] == masIdThemes[j]) {
                                                                                        partCoincide++;
                                                                                        break;
                                                                                    }
                                                                                }
                                                                            }
                                                                        }


                                                                        if (partCoincide == lenMasIdThemes2) {
                                                                            coincide = true;
                                                                        }

                                                                        if (coincide) {
                                                                            id_work_description = cursor.getInt(id_work_description);
                                                                            noAppend = true;
                                                                            break;
                                                                        }
                                                                    }
                                                                } while (cursor.moveToNext());
                                                            }
                                                            cursor.close();

                                                            int id_WDescription = 0;

                                                            if (!noAppend) {


                                                                ContentValues cv = new ContentValues();
                                                                cv.put(DatabaseHelper.COLUMN_TYPE_OF_WORK, item);
                                                                cv.put(DatabaseHelper.COLUMN_ID_DISCIPLINE, id_discipline);
                                                                cv.put(DatabaseHelper.COLUMN_ID_AUTHOR, id_author);
                                                                cv.put(DatabaseHelper.COLUMN_TASK_NUMBER, Integer.parseInt(editTextNumberOfTask.getText().toString()));
                                                                cv.put(DatabaseHelper.COLUMN_OPTION_NUMBER, Integer.parseInt(editTextNumberOfVariant.getText().toString()));
                                                                cv.put(DatabaseHelper.COLUMN_SECTION_NUMBER, -1);
                                                                cv.put(DatabaseHelper.COLUMN_CHAPTER_NUMBER, -1);
                                                                cv.put(DatabaseHelper.COLUMN_PARAGRAPH_NUMBER, -1);
                                                                cv.put(DatabaseHelper.COLUMN_PUBLICATION_NUMBER, -1);

                                                                db.insert(DatabaseHelper.TABLE_WORK_DESCRIPTION, null, cv);

                                                                userCursor = db.query(DatabaseHelper.TABLE_WORK_DESCRIPTION, null, null, null, null, null, null);

                                                                if (userCursor.moveToLast()) {
                                                                    int idNew_work_description = userCursor.getColumnIndex(DatabaseHelper.COLUMN_ID_WORK_DESCRIPTION);
                                                                    idNew_work_description = userCursor.getInt(idNew_work_description);

                                                                    for (int i = 0; i < lenMasIdThemes; i++) {
                                                                        cv = new ContentValues();
                                                                        cv.put(DatabaseHelper.COLUMN_ID_WORK_DESCRIPTION, idNew_work_description);
                                                                        cv.put(DatabaseHelper.COLUMN_ID_THEME, masIdThemes[i]);
                                                                        db.insert(DatabaseHelper.TABLE_THEME_OF_WORK, null, cv);
                                                                    }

                                                                    id_WDescription = idNew_work_description;

                                                                }

                                                                userCursor.close();

                                                            } else {

                                                                id_WDescription = id_work_description;

                                                            }

                                                            Bundle arguments = getIntent().getExtras();
                                                            int id_user = arguments.getInt("id_user");

                                                            Date currentDate = new Date();
                                                            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
                                                            String dateText = df.format(Calendar.getInstance().getTime());


                                                            ContentValues cv = new ContentValues();
                                                            cv.put(DatabaseHelper.COLUMN_ID_WORK_DESCRIPTION, id_WDescription);
                                                            cv.put(DatabaseHelper.COLUMN_ID_USER, id_user);
                                                            cv.put(DatabaseHelper.COLUMN_TEXT_WORK, editTextDecisionValue);
                                                            cv.put(DatabaseHelper.COLUMN_TIME_OF_CREATION, dateText);

                                                            if(imageForDownload.getVisibility() == View.VISIBLE) {

                                                                BitmapDrawable drawable = (BitmapDrawable) imageForDownload.getDrawable();
                                                                Bitmap bitmap = drawable.getBitmap();

                                                                // Записываем изображение в поток байтов.
                                                                // При этом изображение можно сжать и / или перекодировать в другой формат.
                                                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

                                                                // Получаем изображение из потока в виде байтов
                                                                byte[] bytes = byteArrayOutputStream.toByteArray();

                                                                // Кодируем байты в строку Base64 и возвращаем
                                                                String fileForDownload =  Base64.encodeToString(bytes, Base64.DEFAULT);

                                                                cv.put(DatabaseHelper.COLUMN_FILE_FROM_WORK, fileForDownload);
                                                            } else {
                                                                cv.put(DatabaseHelper.COLUMN_FILE_FROM_WORK, "nine");
                                                            }


                                                            db.insert(DatabaseHelper.TABLE_USER_WORK, null, cv);

                                                            userCursor = db.query(DatabaseHelper.TABLE_USER_WORK, null, null, null, null, null, null);

                                                            int id_user_work = 0;

                                                            if (userCursor.moveToLast()) {
                                                                id_user_work = userCursor.getColumnIndex(DatabaseHelper.COLUMN_ID_USER_WORK);
                                                                id_user_work = userCursor.getInt(id_user_work);
                                                            }

                                                            userCursor.close();

                                                            Intent intent = new Intent(getApplicationContext(), InformationAboutTheSolution.class);
                                                            intent.putExtra("id_user", id_user);
                                                            intent.putExtra("id_user_work", id_user_work);
                                                            startActivity(intent);

                                                        }

                                                }

                                            }

                                        }

                                    }

                                }

                            }

                            if (!errorFlag) {
                                error = "Что-то пошло не по плану:" + error;
                                errorMessage.setText(error);
                                errorMessage.setPaddingRelative(20, 20,20,20);
                                errorMessage.setVisibility(View.VISIBLE);
                            }

                        }
                    });

                }

                if(item.equals("Домашняя работа")) {

                    errorMessage.setVisibility(View.GONE);

                    editTextDecision.setHint("Текст вашего решения / Описание решения / комментарий к решению");
                    descriptionNumberOfPublication.setVisibility(View.GONE);
                    helpNumberOfPublication.setVisibility(View.GONE);
                    editTextNumberOfPublication.setVisibility(View.GONE);
                    editTextNumberOfPublication.setEnabled(false);

                    descriptionSection.setVisibility(View.GONE);
                    helpSection.setVisibility(View.GONE);
                    editTextTextSection.setVisibility(View.GONE);
                    editTextTextSection.setEnabled(false);

                    descriptionChapter.setVisibility(View.VISIBLE);
                    helpChapter.setVisibility(View.VISIBLE);
                    editTextChapter.setVisibility(View.VISIBLE);
                    editTextChapter.setEnabled(true);

                    descriptionParagraph.setVisibility(View.VISIBLE);
                    helpParagraph.setVisibility(View.VISIBLE);
                    editTextParagraph.setVisibility(View.VISIBLE);
                    editTextParagraph.setEnabled(true);

                    descriptionNumberOfTask.setVisibility(View.VISIBLE);
                    helpNumberOfTask.setVisibility(View.VISIBLE);
                    editTextNumberOfTask.setVisibility(View.VISIBLE);
                    editTextNumberOfTask.setEnabled(true);

                    descriptionNumberOfVariant.setVisibility(View.GONE);
                    helpNumberOfVariant.setVisibility(View.GONE);
                    editTextNumberOfVariant.setVisibility(View.GONE);
                    editTextNumberOfVariant.setEnabled(false);

                    btnCreatingSolution.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            String autoCompleteDisciplineValue = autoCompleteDiscipline.getText().toString();
                            String autoCompleteAuthorValue = autoCompleteAuthor.getText().toString();
                            String autoCompleteThemeValue = autoCompleteTheme.getText().toString();
                            String autoCompleteThemeValue2 = autoCompleteTheme2.getText().toString();
                            String autoCompleteThemeValue3 = autoCompleteTheme3.getText().toString();
                            String autoCompleteThemeValue4 = autoCompleteTheme4.getText().toString();
                            String autoCompleteThemeValue5 = autoCompleteTheme5.getText().toString();

                            String error = "";
                            boolean errorFlag = true;

                            if (autoCompleteDisciplineValue.equals("")) {
                                error += "\n- нужно ввести название дисциплины";
                                errorFlag = false;
                            }

                            if (autoCompleteAuthorValue.equals("")) {
                                error += "\n- нужно ввести имя автора / преподователя";
                                errorFlag = false;
                            }

                            if (autoCompleteThemeValue.equals("")) {
                                error += "\n- нужно ввести название темы";
                                errorFlag = false;
                            }

                            if (autoCompleteThemeValue2.equals("") && (autoCompleteTheme2.getVisibility() == View.VISIBLE)) {
                                error += "\n- нужно ввести название темы 2";
                                errorFlag = false;
                            }

                            if (autoCompleteThemeValue3.equals("") && (autoCompleteTheme3.getVisibility() == View.VISIBLE)) {
                                error += "\n- нужно ввести название темы 3";
                                errorFlag = false;
                            }

                            if (autoCompleteThemeValue4.equals("") && (autoCompleteTheme4.getVisibility() == View.VISIBLE)) {
                                error += "\n- нужно ввести название темы 4";
                                errorFlag = false;
                            }

                            if (autoCompleteThemeValue5.equals("") && (autoCompleteTheme5.getVisibility() == View.VISIBLE)) {
                                error += "\n- нужно ввести название темы 5";
                                errorFlag = false;
                            }

                            boolean repeatingError = true;

                            if (!(autoCompleteThemeValue2.equals("")) && ( autoCompleteThemeValue2.equals(autoCompleteThemeValue)
                                    || autoCompleteThemeValue2.equals(autoCompleteThemeValue3) || autoCompleteThemeValue2.equals(autoCompleteThemeValue4)
                                    || autoCompleteThemeValue2.equals(autoCompleteThemeValue5))) {
                                error += "\n- темы не могут иметь одинаковое название";
                                errorFlag = false;
                                repeatingError = false;
                            }

                            if (repeatingError && !(autoCompleteThemeValue3.equals("")) && ( autoCompleteThemeValue3.equals(autoCompleteThemeValue)
                                    || autoCompleteThemeValue3.equals(autoCompleteThemeValue4)
                                    || autoCompleteThemeValue3.equals(autoCompleteThemeValue5))) {
                                error += "\n- темы не могут иметь одинаковое название";
                                errorFlag = false;
                                repeatingError = false;
                            }

                            if (repeatingError && !(autoCompleteThemeValue4.equals("")) && ( autoCompleteThemeValue4.equals(autoCompleteThemeValue)
                                    || autoCompleteThemeValue4.equals(autoCompleteThemeValue5))) {
                                error += "\n- темы не могут иметь одинаковое название";
                                errorFlag = false;
                                repeatingError = false;
                            }

                            if (repeatingError && !(autoCompleteThemeValue4.equals("")) && autoCompleteThemeValue5.equals(autoCompleteThemeValue)) {
                                error += "\n- темы не могут иметь одинаковое название";
                                errorFlag = false;
                                repeatingError = false;
                            }

                            if (errorFlag) {

                                String regex = "^[а-яёА-ЯЁ\\t\\n\\f\\r\\p{Z}]*$";

                                Pattern pattern = Pattern.compile(regex);
                                Matcher m = pattern.matcher(autoCompleteDisciplineValue);
                                if (!m.matches()){
                                    error += "\n- введены недопустимые символы в названии дисциплины";
                                    errorFlag = false;
                                }

                                m = pattern.matcher(autoCompleteThemeValue);
                                if (!m.matches()){
                                    error += "\n- введены недопустимые символы в названии темы";
                                    errorFlag = false;
                                }

                                m = pattern.matcher(autoCompleteThemeValue2);
                                if (!(m.matches()) && (autoCompleteTheme2.getVisibility() == View.VISIBLE)){
                                    error += "\n- введены недопустимые символы в названии темы 2";
                                    errorFlag = false;
                                }

                                m = pattern.matcher(autoCompleteThemeValue3);
                                if (!(m.matches()) && (autoCompleteTheme3.getVisibility() == View.VISIBLE)){
                                    error += "\n- введены недопустимые символы в названии темы 3";
                                    errorFlag = false;
                                }

                                m = pattern.matcher(autoCompleteThemeValue4);
                                if (!(m.matches()) && (autoCompleteTheme4.getVisibility() == View.VISIBLE)){
                                    error += "\n- введены недопустимые символы в названии темы 4";
                                    errorFlag = false;
                                }

                                m = pattern.matcher(autoCompleteThemeValue5);
                                if (!(m.matches()) && (autoCompleteTheme5.getVisibility() == View.VISIBLE)){
                                    error += "\n- введены недопустимые символы в названии темы 5";
                                    errorFlag = false;
                                }

                                regex = "^[А-Я][а-я]+\\s?[А-Я]\\.\\s?[А-Я]\\.$";
                                pattern = Pattern.compile(regex);
                                m = pattern.matcher(autoCompleteAuthorValue);
                                if (!m.matches()){
                                    error += "\n- имя должно иметь формат: Маяковский В. В.";
                                    errorFlag = false;
                                }

                                if (errorFlag) {

                                    if (autoCompleteDisciplineValue.length() > 50) {
                                        error += "\n- название дисциплины слишком длинное";
                                        errorFlag = false;
                                    }

                                    if (autoCompleteAuthorValue.length() > 33) {
                                        error += "\n- введенное имя автора / преподователя слишком длинное";
                                        errorFlag = false;
                                    }

                                    if (autoCompleteThemeValue.length() > 50) {
                                        error += "\n- название темы слишком длинное";
                                        errorFlag = false;
                                    }

                                    if (errorFlag) {

                                        if (editTextNumberOfTask.getText().toString().equals("")) {
                                            error += "\n- нужно ввести номер задания";
                                            errorFlag = false;
                                        }

                                        if (errorFlag) {

                                            regex = "[0-9]+";
                                            pattern = Pattern.compile(regex);

                                            m = pattern.matcher(editTextNumberOfTask.getText());
                                            if (!m.matches()){
                                                error += "\n- в номере задания можно использовать только цифры";
                                                errorFlag = false;
                                            }

                                            m = pattern.matcher(editTextParagraph.getText());
                                            if (!m.matches() && !(editTextParagraph.getText().toString().equals(""))){
                                                error += "\n- в номере параграфа можно использовать только цифры";
                                                errorFlag = false;
                                            }
                                            m = pattern.matcher(editTextChapter.getText());

                                            if (!m.matches() && !(editTextChapter.getText().toString().equals(""))){
                                                error += "\n- в номере главы можно использовать только цифры";
                                                errorFlag = false;
                                            }

                                            if (errorFlag) {

                                                if (editTextNumberOfTask.getText().length() > 4) {
                                                    error += "\n- номер задания слишком большой";
                                                    errorFlag = false;
                                                }

                                                if (editTextParagraph.getText().length() > 3) {
                                                    error += "\n- номер параграфа слишком большой";
                                                    errorFlag = false;
                                                }

                                                if (editTextChapter.getText().length() > 3) {
                                                    error += "\n- номер главы слишком большой";
                                                    errorFlag = false;
                                                }

                                                if (errorFlag) {

                                                    String editTextDecisionValue = editTextDecision.getText().toString();

                                                    if (editTextDecisionValue.equals("")) {
                                                        error += "\n- нужно ввести текст в поле решения";
                                                        errorFlag = false;
                                                    }

                                                    if (editTextDecisionValue.length() > 2000) {
                                                        error += "\n- текст решения слишком большой";
                                                        errorFlag = false;
                                                    }

                                                    if (errorFlag) {

                                                        //---------------------------------------------------
                                                        boolean noAppend = false;

                                                        Cursor cursor = db.query(DatabaseHelper.TABLE_DISCIPLINES, null, null, null, null, null, null);

                                                        if (cursor.moveToFirst()) {
                                                            int idNameDiscipline = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_DISCIPLINE);

                                                            do {
                                                                if (cursor.getString(idNameDiscipline).equals(autoCompleteDisciplineValue)) {
                                                                    noAppend = true;
                                                                    break;
                                                                }
                                                            } while (cursor.moveToNext());
                                                        }
                                                        cursor.close();

                                                        if (!noAppend) {
                                                            ContentValues cv = new ContentValues();
                                                            cv.put(DatabaseHelper.COLUMN_NAME_DISCIPLINE, autoCompleteDisciplineValue);
                                                            db.insert(DatabaseHelper.TABLE_DISCIPLINES, null, cv);
                                                        }
                                                        //---------------------------------------------------

                                                        //---------------------------------------------------
                                                        noAppend = false;

                                                        cursor = db.query(DatabaseHelper.TABLE_AUTHORS, null, null, null, null, null, null);

                                                        if (cursor.moveToFirst()) {
                                                            int idNameAuthor = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_AUTHOR);

                                                            do {
                                                                if (cursor.getString(idNameAuthor).equals(autoCompleteAuthorValue)) {
                                                                    noAppend = true;
                                                                    break;
                                                                }
                                                            } while (cursor.moveToNext());
                                                        }
                                                        cursor.close();

                                                        if (!noAppend) {
                                                            ContentValues cv = new ContentValues();
                                                            cv.put(DatabaseHelper.COLUMN_NAME_AUTHOR, autoCompleteAuthorValue);
                                                            db.insert(DatabaseHelper.TABLE_AUTHORS, null, cv);
                                                        }
                                                        //---------------------------------------------------

                                                        //---------------------------------------------------
                                                        noAppend = false;

                                                        cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                        if (cursor.moveToFirst()) {
                                                            int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                            do {
                                                                if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue)) {
                                                                    noAppend = true;
                                                                    break;
                                                                }
                                                            } while (cursor.moveToNext());
                                                        }
                                                        cursor.close();

                                                        if (!noAppend) {
                                                            ContentValues cv = new ContentValues();
                                                            cv.put(DatabaseHelper.COLUMN_THEME, autoCompleteThemeValue);
                                                            db.insert(DatabaseHelper.TABLE_THEMES, null, cv);
                                                        }
                                                        //---------------------------------------------------


                                                        //---------------------------------------------------

                                                        if (!autoCompleteThemeValue2.equals("")) {

                                                            noAppend = false;

                                                            cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                            if (cursor.moveToFirst()) {
                                                                int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                                do {
                                                                    if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue2)) {
                                                                        noAppend = true;
                                                                        break;
                                                                    }
                                                                } while (cursor.moveToNext());
                                                            }
                                                            cursor.close();

                                                            if (!noAppend) {
                                                                ContentValues cv = new ContentValues();
                                                                cv.put(DatabaseHelper.COLUMN_THEME, autoCompleteThemeValue2);
                                                                db.insert(DatabaseHelper.TABLE_THEMES, null, cv);
                                                            }

                                                        }
                                                        //---------------------------------------------------

                                                        //---------------------------------------------------

                                                        if (!autoCompleteThemeValue3.equals("")) {

                                                            noAppend = false;

                                                            cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                            if (cursor.moveToFirst()) {
                                                                int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                                do {
                                                                    if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue3)) {
                                                                        noAppend = true;
                                                                        break;
                                                                    }
                                                                } while (cursor.moveToNext());
                                                            }
                                                            cursor.close();

                                                            if (!noAppend) {
                                                                ContentValues cv = new ContentValues();
                                                                cv.put(DatabaseHelper.COLUMN_THEME, autoCompleteThemeValue3);
                                                                db.insert(DatabaseHelper.TABLE_THEMES, null, cv);
                                                            }

                                                        }
                                                        //---------------------------------------------------

                                                        //---------------------------------------------------

                                                        if (!autoCompleteThemeValue4.equals("")) {

                                                            noAppend = false;

                                                            cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                            if (cursor.moveToFirst()) {
                                                                int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                                do {
                                                                    if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue4)) {
                                                                        noAppend = true;
                                                                        break;
                                                                    }
                                                                } while (cursor.moveToNext());
                                                            }
                                                            cursor.close();

                                                            if (!noAppend) {
                                                                ContentValues cv = new ContentValues();
                                                                cv.put(DatabaseHelper.COLUMN_THEME, autoCompleteThemeValue4);
                                                                db.insert(DatabaseHelper.TABLE_THEMES, null, cv);
                                                            }

                                                        }
                                                        //---------------------------------------------------

                                                        //---------------------------------------------------

                                                        if (!autoCompleteThemeValue5.equals("")) {

                                                            noAppend = false;

                                                            cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                            if (cursor.moveToFirst()) {
                                                                int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                                do {
                                                                    if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue5)) {
                                                                        noAppend = true;
                                                                        break;
                                                                    }
                                                                } while (cursor.moveToNext());
                                                            }
                                                            cursor.close();

                                                            if (!noAppend) {
                                                                ContentValues cv = new ContentValues();
                                                                cv.put(DatabaseHelper.COLUMN_THEME, autoCompleteThemeValue5);
                                                                db.insert(DatabaseHelper.TABLE_THEMES, null, cv);
                                                            }

                                                        }
                                                        //---------------------------------------------------

                                                        //---------------------------------------------------


                                                        cursor = db.query(DatabaseHelper.TABLE_DISCIPLINES, null, null, null, null, null, null);

                                                        int id_discipline = 0;

                                                        if (cursor.moveToFirst()) {
                                                            id_discipline = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_DISCIPLINE);
                                                            int idName_discipline = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_DISCIPLINE);

                                                            do {
                                                                if (cursor.getString(idName_discipline).equals(autoCompleteDisciplineValue)) {
                                                                    id_discipline = cursor.getInt(id_discipline);
                                                                    break;
                                                                }
                                                            } while (cursor.moveToNext());
                                                        }
                                                        cursor.close();


                                                        cursor = db.query(DatabaseHelper.TABLE_AUTHORS, null, null, null, null, null, null);

                                                        int id_author = 0;

                                                        if (cursor.moveToFirst()) {
                                                            id_author = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_AUTHOR);
                                                            int idName_author = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_AUTHOR);

                                                            do {
                                                                if (cursor.getString(idName_author).equals(autoCompleteAuthorValue)) {
                                                                    id_author = cursor.getInt(id_author);
                                                                    break;
                                                                }
                                                            } while (cursor.moveToNext());
                                                        }
                                                        cursor.close();

                                                        int[] masIdThemes = new int[5];
                                                        int lenMasIdThemes = 0;

                                                        cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                        if (cursor.moveToFirst()) {
                                                            int id_theme = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_THEME);
                                                            int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                            do {

                                                                if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue)) {
                                                                    masIdThemes[lenMasIdThemes] = cursor.getInt(id_theme);
                                                                    lenMasIdThemes++;
                                                                }

                                                                if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue2)) {
                                                                    masIdThemes[lenMasIdThemes] = cursor.getInt(id_theme);
                                                                    lenMasIdThemes++;
                                                                }

                                                                if ( cursor.getString(idNameTheme).equals(autoCompleteThemeValue3)) {
                                                                    masIdThemes[lenMasIdThemes] = cursor.getInt(id_theme);
                                                                    lenMasIdThemes++;
                                                                }

                                                                if ( cursor.getString(idNameTheme).equals(autoCompleteThemeValue4)) {
                                                                    masIdThemes[lenMasIdThemes] = cursor.getInt(id_theme);
                                                                    lenMasIdThemes++;
                                                                }

                                                                if ( !(autoCompleteThemeValue5.equals("")) && cursor.getString(idNameTheme).equals(autoCompleteThemeValue5)) {
                                                                    masIdThemes[lenMasIdThemes] = cursor.getInt(id_theme);
                                                                    lenMasIdThemes++;
                                                                }

                                                            } while (cursor.moveToNext());
                                                        }
                                                        cursor.close();


                                                        noAppend = false;

                                                        cursor = db.query(DatabaseHelper.TABLE_WORK_DESCRIPTION, null, null, null, null, null, null);

                                                        int id_work_description = 0;

                                                        if (cursor.moveToFirst()) {
                                                            id_work_description = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_WORK_DESCRIPTION);
                                                            int idType_of_work = cursor.getColumnIndex(DatabaseHelper.COLUMN_TYPE_OF_WORK);
                                                            int id_discipline2 = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_DISCIPLINE);
                                                            int id_author2 = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_AUTHOR);
                                                            int idTask_number = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_NUMBER);
                                                            int paragraph_number = cursor.getColumnIndex(DatabaseHelper.COLUMN_PARAGRAPH_NUMBER);
                                                            int chapter_number = cursor.getColumnIndex(DatabaseHelper.COLUMN_CHAPTER_NUMBER);

                                                            do {
                                                                if (cursor.getString(idType_of_work).equals(item) && cursor.getInt(id_discipline2) == id_discipline
                                                                        &&  cursor.getInt(id_author2) == id_author
                                                                        &&  cursor.getInt(idTask_number) == Integer.parseInt(editTextNumberOfTask.getText().toString())
                                                                        &&  cursor.getInt(paragraph_number) == Integer.parseInt(editTextParagraph.getText().toString())
                                                                        &&  cursor.getInt(chapter_number) == Integer.parseInt(editTextChapter.getText().toString())) {

                                                                    int[] masIdThemes2 = new int[5];
                                                                    int lenMasIdThemes2 = 0;

                                                                    userCursor = db.query(DatabaseHelper.TABLE_THEME_OF_WORK, null, null, null, null, null, null);

                                                                    if (userCursor.moveToFirst()) {
                                                                        int id_theme = userCursor.getColumnIndex(DatabaseHelper.COLUMN_ID_THEME);
                                                                        int id_workDescription = userCursor.getColumnIndex(DatabaseHelper.COLUMN_ID_WORK_DESCRIPTION);

                                                                        do {
                                                                            if (userCursor.getInt(id_workDescription) == cursor.getInt(id_work_description)) {
                                                                                masIdThemes2[lenMasIdThemes2] = userCursor.getInt(id_theme);
                                                                                lenMasIdThemes2++;
                                                                            }
                                                                        } while (userCursor.moveToNext());
                                                                    }
                                                                    userCursor.close();

                                                                    boolean coincide = false;
                                                                    int partCoincide = 0;

                                                                    if (lenMasIdThemes2 == lenMasIdThemes) {
                                                                        for (int i =0; i < lenMasIdThemes2; i++) {
                                                                            for (int j = 0; j < lenMasIdThemes; j++) {
                                                                                if (masIdThemes2[i] == masIdThemes[j]) {
                                                                                    partCoincide++;
                                                                                    break;
                                                                                }
                                                                            }
                                                                        }
                                                                    }

                                                                    if (partCoincide == lenMasIdThemes2) {
                                                                        coincide = true;
                                                                    }

                                                                    if (coincide) {
                                                                        id_work_description = cursor.getInt(id_work_description);
                                                                        noAppend = true;
                                                                        break;
                                                                    }
                                                                }
                                                            } while (cursor.moveToNext());
                                                        }
                                                        cursor.close();

                                                        int id_WDescription = 0;

                                                        if (!noAppend) {

                                                            ContentValues cv = new ContentValues();
                                                            cv.put(DatabaseHelper.COLUMN_TYPE_OF_WORK, item);
                                                            cv.put(DatabaseHelper.COLUMN_ID_DISCIPLINE, id_discipline);
                                                            cv.put(DatabaseHelper.COLUMN_ID_AUTHOR, id_author);
                                                            cv.put(DatabaseHelper.COLUMN_TASK_NUMBER, Integer.parseInt(editTextNumberOfTask.getText().toString()));
                                                            cv.put(DatabaseHelper.COLUMN_OPTION_NUMBER, -1);
                                                            cv.put(DatabaseHelper.COLUMN_SECTION_NUMBER, -1);
                                                            cv.put(DatabaseHelper.COLUMN_CHAPTER_NUMBER, Integer.parseInt(editTextChapter.getText().toString()));
                                                            cv.put(DatabaseHelper.COLUMN_PARAGRAPH_NUMBER, Integer.parseInt(editTextParagraph.getText().toString()));
                                                            cv.put(DatabaseHelper.COLUMN_PUBLICATION_NUMBER, -1);
                                                            db.insert(DatabaseHelper.TABLE_WORK_DESCRIPTION, null, cv);

                                                            userCursor = db.query(DatabaseHelper.TABLE_WORK_DESCRIPTION, null, null, null, null, null, null);

                                                            if (userCursor.moveToLast()) {
                                                                int idNew_work_description = userCursor.getColumnIndex(DatabaseHelper.COLUMN_ID_WORK_DESCRIPTION);
                                                                idNew_work_description = userCursor.getInt(idNew_work_description);

                                                                for (int i = 0; i < lenMasIdThemes; i++) {
                                                                    cv = new ContentValues();
                                                                    cv.put(DatabaseHelper.COLUMN_ID_WORK_DESCRIPTION, idNew_work_description);
                                                                    cv.put(DatabaseHelper.COLUMN_ID_THEME, masIdThemes[i]);
                                                                    db.insert(DatabaseHelper.TABLE_THEME_OF_WORK, null, cv);
                                                                }

                                                                id_WDescription = idNew_work_description;

                                                            }

                                                            userCursor.close();

                                                        } else {

                                                            id_WDescription = id_work_description;

                                                        }

                                                        Bundle arguments = getIntent().getExtras();
                                                        int id_user = arguments.getInt("id_user");

                                                        Date currentDate = new Date();
                                                        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                                                        String dateText = dateFormat.format(currentDate);

                                                        ContentValues cv = new ContentValues();
                                                        cv.put(DatabaseHelper.COLUMN_ID_WORK_DESCRIPTION, id_WDescription);
                                                        cv.put(DatabaseHelper.COLUMN_ID_USER, id_user);
                                                        cv.put(DatabaseHelper.COLUMN_TEXT_WORK, editTextDecisionValue);
                                                        cv.put(DatabaseHelper.COLUMN_TIME_OF_CREATION, dateText);

                                                        if(imageForDownload.getVisibility() == View.VISIBLE) {

                                                            BitmapDrawable drawable = (BitmapDrawable) imageForDownload.getDrawable();
                                                            Bitmap bitmap = drawable.getBitmap();

                                                            // Записываем изображение в поток байтов.
                                                            // При этом изображение можно сжать и / или перекодировать в другой формат.
                                                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

                                                            // Получаем изображение из потока в виде байтов
                                                            byte[] bytes = byteArrayOutputStream.toByteArray();

                                                            // Кодируем байты в строку Base64 и возвращаем
                                                            String fileForDownload =  Base64.encodeToString(bytes, Base64.DEFAULT);

                                                            cv.put(DatabaseHelper.COLUMN_FILE_FROM_WORK, fileForDownload);
                                                        } else {
                                                            cv.put(DatabaseHelper.COLUMN_FILE_FROM_WORK, "nine");
                                                        }

                                                        db.insert(DatabaseHelper.TABLE_USER_WORK, null, cv);

                                                        userCursor = db.query(DatabaseHelper.TABLE_USER_WORK, null, null, null, null, null, null);

                                                        int id_user_work = 0;

                                                        if (userCursor.moveToLast()) {
                                                            id_user_work = userCursor.getColumnIndex(DatabaseHelper.COLUMN_ID_USER_WORK);
                                                            id_user_work = userCursor.getInt(id_user_work);
                                                        }

                                                        userCursor.close();


                                                        Intent intent = new Intent(getApplicationContext(), InformationAboutTheSolution.class);
                                                        intent.putExtra("id_user", id_user);
                                                        intent.putExtra("id_user_work", id_user_work);
                                                        startActivity(intent);



                                                    }

                                                }

                                            }

                                        }

                                    }

                                }

                            }

                            if (!errorFlag) {
                                error = "Что-то пошло не по плану:" + error;
                                errorMessage.setText(error);
                                errorMessage.setPaddingRelative(20, 20,20,20);
                                errorMessage.setVisibility(View.VISIBLE);
                            }

                        }
                    });

                }

                if (item.equals("Доклад")) {

                    errorMessage.setVisibility(View.GONE);

                    editTextDecision.setHint("Текст вашего доклада / Описание доклада / комментарий к докладу");

                    descriptionSection.setVisibility(View.GONE);
                    descriptionSection.setVisibility(View.GONE);
                    editTextTextSection.setVisibility(View.GONE);
                    editTextTextSection.setEnabled(false);

                    descriptionChapter.setVisibility(View.GONE);
                    helpChapter.setVisibility(View.GONE);
                    editTextChapter.setVisibility(View.GONE);
                    editTextChapter.setEnabled(false);

                    descriptionParagraph.setVisibility(View.GONE);
                    helpParagraph.setVisibility(View.GONE);
                    editTextParagraph.setVisibility(View.GONE);
                    editTextParagraph.setEnabled(false);

                    descriptionNumberOfPublication.setVisibility(View.GONE);
                    helpNumberOfPublication.setVisibility(View.GONE);
                    editTextNumberOfPublication.setVisibility(View.GONE);
                    editTextNumberOfPublication.setEnabled(false);

                    descriptionNumberOfTask.setVisibility(View.GONE);
                    helpNumberOfTask.setVisibility(View.GONE);
                    editTextNumberOfTask.setVisibility(View.GONE);
                    editTextNumberOfTask.setEnabled(false);

                    descriptionNumberOfVariant.setVisibility(View.GONE);
                    helpNumberOfVariant.setVisibility(View.GONE);
                    editTextNumberOfVariant.setVisibility(View.GONE);
                    editTextNumberOfVariant.setEnabled(false);

                    btnCreatingSolution.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            String autoCompleteDisciplineValue = autoCompleteDiscipline.getText().toString();
                            String autoCompleteAuthorValue = autoCompleteAuthor.getText().toString();
                            String autoCompleteThemeValue = autoCompleteTheme.getText().toString();
                            String autoCompleteThemeValue2 = autoCompleteTheme2.getText().toString();
                            String autoCompleteThemeValue3 = autoCompleteTheme3.getText().toString();
                            String autoCompleteThemeValue4 = autoCompleteTheme4.getText().toString();
                            String autoCompleteThemeValue5 = autoCompleteTheme5.getText().toString();

                            String error = "";
                            boolean errorFlag = true;

                            if (autoCompleteDisciplineValue.equals("")) {
                                error += "\n- нужно ввести название дисциплины";
                                errorFlag = false;
                            }

                            if (autoCompleteAuthorValue.equals("")) {
                                error += "\n- нужно ввести имя автора / преподователя";
                                errorFlag = false;
                            }

                            if (autoCompleteThemeValue.equals("")) {
                                error += "\n- нужно ввести название темы";
                                errorFlag = false;
                            }

                            if (autoCompleteThemeValue2.equals("") && (autoCompleteTheme2.getVisibility() == View.VISIBLE)) {
                                error += "\n- нужно ввести название темы 2";
                                errorFlag = false;
                            }

                            if (autoCompleteThemeValue3.equals("") && (autoCompleteTheme3.getVisibility() == View.VISIBLE)) {
                                error += "\n- нужно ввести название темы 3";
                                errorFlag = false;
                            }

                            if (autoCompleteThemeValue4.equals("") && (autoCompleteTheme4.getVisibility() == View.VISIBLE)) {
                                error += "\n- нужно ввести название темы 4";
                                errorFlag = false;
                            }

                            if (autoCompleteThemeValue5.equals("") && (autoCompleteTheme5.getVisibility() == View.VISIBLE)) {
                                error += "\n- нужно ввести название темы 5";
                                errorFlag = false;
                            }

                            boolean repeatingError = true;

                            if (!(autoCompleteThemeValue2.equals("")) && ( autoCompleteThemeValue2.equals(autoCompleteThemeValue)
                                    || autoCompleteThemeValue2.equals(autoCompleteThemeValue3) || autoCompleteThemeValue2.equals(autoCompleteThemeValue4)
                                    || autoCompleteThemeValue2.equals(autoCompleteThemeValue5))) {
                                error += "\n- темы не могут иметь одинаковое название";
                                errorFlag = false;
                                repeatingError = false;
                            }

                            if (repeatingError && !(autoCompleteThemeValue3.equals("")) && ( autoCompleteThemeValue3.equals(autoCompleteThemeValue)
                                    || autoCompleteThemeValue3.equals(autoCompleteThemeValue4)
                                    || autoCompleteThemeValue3.equals(autoCompleteThemeValue5))) {
                                error += "\n- темы не могут иметь одинаковое название";
                                errorFlag = false;
                                repeatingError = false;
                            }

                            if (repeatingError && !(autoCompleteThemeValue4.equals("")) && ( autoCompleteThemeValue4.equals(autoCompleteThemeValue)
                                    || autoCompleteThemeValue4.equals(autoCompleteThemeValue5))) {
                                error += "\n- темы не могут иметь одинаковое название";
                                errorFlag = false;
                                repeatingError = false;
                            }

                            if (repeatingError && !(autoCompleteThemeValue4.equals("")) && autoCompleteThemeValue5.equals(autoCompleteThemeValue)) {
                                error += "\n- темы не могут иметь одинаковое название";
                                errorFlag = false;
                                repeatingError = false;
                            }

                            if (errorFlag) {

                                String regex = "^[а-яёА-ЯЁ\\t\\n\\f\\r\\p{Z}]*$";

                                Pattern pattern = Pattern.compile(regex);
                                Matcher m = pattern.matcher(autoCompleteDisciplineValue);
                                if (!m.matches()){
                                    error += "\n- введены недопустимые символы в названии дисциплины";
                                    errorFlag = false;
                                }

                                m = pattern.matcher(autoCompleteThemeValue);
                                if (!m.matches()){
                                    error += "\n- введены недопустимые символы в названии темы";
                                    errorFlag = false;
                                }

                                m = pattern.matcher(autoCompleteThemeValue2);
                                if (!(m.matches()) && (autoCompleteTheme2.getVisibility() == View.VISIBLE)){
                                    error += "\n- введены недопустимые символы в названии темы 2";
                                    errorFlag = false;
                                }

                                m = pattern.matcher(autoCompleteThemeValue3);
                                if (!(m.matches()) && (autoCompleteTheme3.getVisibility() == View.VISIBLE)){
                                    error += "\n- введены недопустимые символы в названии темы 3";
                                    errorFlag = false;
                                }

                                m = pattern.matcher(autoCompleteThemeValue4);
                                if (!(m.matches()) && (autoCompleteTheme4.getVisibility() == View.VISIBLE)){
                                    error += "\n- введены недопустимые символы в названии темы 4";
                                    errorFlag = false;
                                }

                                m = pattern.matcher(autoCompleteThemeValue5);
                                if (!(m.matches()) && (autoCompleteTheme5.getVisibility() == View.VISIBLE)){
                                    error += "\n- введены недопустимые символы в названии темы 5";
                                    errorFlag = false;
                                }

                                regex = "^[А-Я][а-я]+\\s?[А-Я]\\.\\s?[А-Я]\\.$";
                                pattern = Pattern.compile(regex);
                                m = pattern.matcher(autoCompleteAuthorValue);
                                if (!m.matches()){
                                    error += "\n- имя должно иметь формат: Маяковский В. В.";
                                    errorFlag = false;
                                }

                                if (errorFlag) {

                                    if (autoCompleteDisciplineValue.length() > 50) {
                                        error += "\n- название дисциплины слишком длинное";
                                        errorFlag = false;
                                    }

                                    if (autoCompleteAuthorValue.length() > 33) {
                                        error += "\n- введенное имя автора / преподователя слишком длинное";
                                        errorFlag = false;
                                    }

                                    if (autoCompleteThemeValue.length() > 50) {
                                        error += "\n- название темы слишком длинное";
                                        errorFlag = false;
                                    }

                                    if (errorFlag) {

                                        String editTextDecisionValue = editTextDecision.getText().toString();

                                        if (editTextDecisionValue.equals("")) {
                                            error += "\n- нужно ввести текст в поле доклада";
                                            errorFlag = false;
                                        }

                                        if (editTextDecisionValue.length() > 3000) {
                                            error += "\n- текст решения слишком большой";
                                            errorFlag = false;
                                        }

                                        if (errorFlag) {

                                            //---------------------------------------------------
                                            boolean noAppend = false;

                                            Cursor cursor = db.query(DatabaseHelper.TABLE_DISCIPLINES, null, null, null, null, null, null);

                                            if (cursor.moveToFirst()) {
                                                int idNameDiscipline = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_DISCIPLINE);

                                                do {
                                                    if (cursor.getString(idNameDiscipline).equals(autoCompleteDisciplineValue)) {
                                                        noAppend = true;
                                                        break;
                                                    }
                                                } while (cursor.moveToNext());
                                            }
                                            cursor.close();

                                            if (!noAppend) {
                                                ContentValues cv = new ContentValues();
                                                cv.put(DatabaseHelper.COLUMN_NAME_DISCIPLINE, autoCompleteDisciplineValue);
                                                db.insert(DatabaseHelper.TABLE_DISCIPLINES, null, cv);
                                            }
                                            //---------------------------------------------------

                                            //---------------------------------------------------
                                            noAppend = false;

                                            cursor = db.query(DatabaseHelper.TABLE_AUTHORS, null, null, null, null, null, null);

                                            if (cursor.moveToFirst()) {
                                                int idNameAuthor = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_AUTHOR);

                                                do {
                                                    if (cursor.getString(idNameAuthor).equals(autoCompleteAuthorValue)) {
                                                        noAppend = true;
                                                        break;
                                                    }
                                                } while (cursor.moveToNext());
                                            }
                                            cursor.close();

                                            if (!noAppend) {
                                                ContentValues cv = new ContentValues();
                                                cv.put(DatabaseHelper.COLUMN_NAME_AUTHOR, autoCompleteAuthorValue);
                                                db.insert(DatabaseHelper.TABLE_AUTHORS, null, cv);
                                            }
                                            //---------------------------------------------------

                                            //---------------------------------------------------
                                            noAppend = false;

                                            cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                            if (cursor.moveToFirst()) {
                                                int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                do {
                                                    if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue)) {
                                                        noAppend = true;
                                                        break;
                                                    }
                                                } while (cursor.moveToNext());
                                            }
                                            cursor.close();

                                            if (!noAppend) {
                                                ContentValues cv = new ContentValues();
                                                cv.put(DatabaseHelper.COLUMN_THEME, autoCompleteThemeValue);
                                                db.insert(DatabaseHelper.TABLE_THEMES, null, cv);
                                            }
                                            //---------------------------------------------------

                                            //---------------------------------------------------

                                            if (!autoCompleteThemeValue2.equals("")) {

                                                noAppend = false;

                                                cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                if (cursor.moveToFirst()) {
                                                    int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                    do {
                                                        if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue2)) {
                                                            noAppend = true;
                                                            break;
                                                        }
                                                    } while (cursor.moveToNext());
                                                }
                                                cursor.close();

                                                if (!noAppend) {
                                                    ContentValues cv = new ContentValues();
                                                    cv.put(DatabaseHelper.COLUMN_THEME, autoCompleteThemeValue2);
                                                    db.insert(DatabaseHelper.TABLE_THEMES, null, cv);
                                                }

                                            }
                                            //---------------------------------------------------

                                            //---------------------------------------------------

                                            if (!autoCompleteThemeValue3.equals("")) {

                                                noAppend = false;

                                                cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                if (cursor.moveToFirst()) {
                                                    int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                    do {
                                                        if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue3)) {
                                                            noAppend = true;
                                                            break;
                                                        }
                                                    } while (cursor.moveToNext());
                                                }
                                                cursor.close();

                                                if (!noAppend) {
                                                    ContentValues cv = new ContentValues();
                                                    cv.put(DatabaseHelper.COLUMN_THEME, autoCompleteThemeValue3);
                                                    db.insert(DatabaseHelper.TABLE_THEMES, null, cv);
                                                }

                                            }
                                            //---------------------------------------------------

                                            //---------------------------------------------------

                                            if (!autoCompleteThemeValue4.equals("")) {

                                                noAppend = false;

                                                cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                if (cursor.moveToFirst()) {
                                                    int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                    do {
                                                        if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue4)) {
                                                            noAppend = true;
                                                            break;
                                                        }
                                                    } while (cursor.moveToNext());
                                                }
                                                cursor.close();

                                                if (!noAppend) {
                                                    ContentValues cv = new ContentValues();
                                                    cv.put(DatabaseHelper.COLUMN_THEME, autoCompleteThemeValue4);
                                                    db.insert(DatabaseHelper.TABLE_THEMES, null, cv);
                                                }

                                            }
                                            //---------------------------------------------------

                                            //---------------------------------------------------

                                            if (!autoCompleteThemeValue5.equals("")) {

                                                noAppend = false;

                                                cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                if (cursor.moveToFirst()) {
                                                    int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                    do {
                                                        if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue5)) {
                                                            noAppend = true;
                                                            break;
                                                        }
                                                    } while (cursor.moveToNext());
                                                }
                                                cursor.close();

                                                if (!noAppend) {
                                                    ContentValues cv = new ContentValues();
                                                    cv.put(DatabaseHelper.COLUMN_THEME, autoCompleteThemeValue5);
                                                    db.insert(DatabaseHelper.TABLE_THEMES, null, cv);
                                                }

                                            }
                                            //---------------------------------------------------

                                            //---------------------------------------------------


                                            cursor = db.query(DatabaseHelper.TABLE_DISCIPLINES, null, null, null, null, null, null);

                                            int id_discipline = 0;

                                            if (cursor.moveToFirst()) {
                                                id_discipline = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_DISCIPLINE);
                                                int idName_discipline = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_DISCIPLINE);

                                                do {
                                                    if (cursor.getString(idName_discipline).equals(autoCompleteDisciplineValue)) {
                                                        id_discipline = cursor.getInt(id_discipline);
                                                        break;
                                                    }
                                                } while (cursor.moveToNext());
                                            }
                                            cursor.close();


                                            cursor = db.query(DatabaseHelper.TABLE_AUTHORS, null, null, null, null, null, null);

                                            int id_author = 0;

                                            if (cursor.moveToFirst()) {
                                                id_author = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_AUTHOR);
                                                int idName_author = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_AUTHOR);

                                                do {
                                                    if (cursor.getString(idName_author).equals(autoCompleteAuthorValue)) {
                                                        id_author = cursor.getInt(id_author);
                                                        break;
                                                    }
                                                } while (cursor.moveToNext());
                                            }
                                            cursor.close();

                                            int[] masIdThemes = new int[5];
                                            int lenMasIdThemes = 0;

                                            cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                            if (cursor.moveToFirst()) {
                                                int id_theme = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_THEME);
                                                int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                do {

                                                    if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue)) {
                                                        masIdThemes[lenMasIdThemes] = cursor.getInt(id_theme);
                                                        lenMasIdThemes++;
                                                    }

                                                    if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue2)) {
                                                        masIdThemes[lenMasIdThemes] = cursor.getInt(id_theme);
                                                        lenMasIdThemes++;
                                                    }

                                                    if ( cursor.getString(idNameTheme).equals(autoCompleteThemeValue3)) {
                                                        masIdThemes[lenMasIdThemes] = cursor.getInt(id_theme);
                                                        lenMasIdThemes++;
                                                    }

                                                    if ( cursor.getString(idNameTheme).equals(autoCompleteThemeValue4)) {
                                                        masIdThemes[lenMasIdThemes] = cursor.getInt(id_theme);
                                                        lenMasIdThemes++;
                                                    }

                                                    if ( cursor.getString(idNameTheme).equals(autoCompleteThemeValue5)) {
                                                        masIdThemes[lenMasIdThemes] = cursor.getInt(id_theme);
                                                        lenMasIdThemes++;
                                                    }

                                                } while (cursor.moveToNext());
                                            }
                                            cursor.close();


                                            noAppend = false;

                                            cursor = db.query(DatabaseHelper.TABLE_WORK_DESCRIPTION, null, null, null, null, null, null);

                                            int id_work_description = 0;

                                            if (cursor.moveToFirst()) {
                                                id_work_description = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_WORK_DESCRIPTION);
                                                int idType_of_work = cursor.getColumnIndex(DatabaseHelper.COLUMN_TYPE_OF_WORK);
                                                int id_discipline2 = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_DISCIPLINE);
                                                int id_author2 = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_AUTHOR);

                                                do {
                                                    if (cursor.getString(idType_of_work).equals(item) && cursor.getInt(id_discipline2) == id_discipline
                                                            &&  cursor.getInt(id_author2) == id_author) {

                                                        int[] masIdThemes2 = new int[5];
                                                        int lenMasIdThemes2 = 0;

                                                        userCursor = db.query(DatabaseHelper.TABLE_THEME_OF_WORK, null, null, null, null, null, null);

                                                        if (userCursor.moveToFirst()) {
                                                            int id_theme = userCursor.getColumnIndex(DatabaseHelper.COLUMN_ID_THEME);
                                                            int id_workDescription = userCursor.getColumnIndex(DatabaseHelper.COLUMN_ID_WORK_DESCRIPTION);

                                                            do {
                                                                if (userCursor.getInt(id_workDescription) == cursor.getInt(id_work_description)) {
                                                                    masIdThemes2[lenMasIdThemes2] = userCursor.getInt(id_theme);
                                                                    lenMasIdThemes2++;
                                                                }
                                                            } while (userCursor.moveToNext());
                                                        }
                                                        userCursor.close();

                                                        boolean coincide = false;
                                                        int partCoincide = 0;

                                                        if (lenMasIdThemes2 == lenMasIdThemes) {
                                                            for (int i =0; i < lenMasIdThemes2; i++) {
                                                                for (int j = 0; j < lenMasIdThemes; j++) {
                                                                    if (masIdThemes2[i] == masIdThemes[j]) {
                                                                        partCoincide++;
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                        }


                                                        if (partCoincide == lenMasIdThemes2) {
                                                            coincide = true;
                                                        }

                                                        if (coincide) {
                                                            id_work_description = cursor.getInt(id_work_description);
                                                            noAppend = true;
                                                            break;
                                                        }
                                                    }
                                                } while (cursor.moveToNext());
                                            }
                                            cursor.close();

                                            int id_WDescription = 0;

                                            if (!noAppend) {


                                                ContentValues cv = new ContentValues();
                                                cv.put(DatabaseHelper.COLUMN_TYPE_OF_WORK, item);
                                                cv.put(DatabaseHelper.COLUMN_ID_DISCIPLINE, id_discipline);
                                                cv.put(DatabaseHelper.COLUMN_ID_AUTHOR, id_author);
                                                cv.put(DatabaseHelper.COLUMN_TASK_NUMBER, -1);
                                                cv.put(DatabaseHelper.COLUMN_OPTION_NUMBER, -1);
                                                cv.put(DatabaseHelper.COLUMN_SECTION_NUMBER, -1);
                                                cv.put(DatabaseHelper.COLUMN_CHAPTER_NUMBER, -1);
                                                cv.put(DatabaseHelper.COLUMN_PARAGRAPH_NUMBER, -1);
                                                cv.put(DatabaseHelper.COLUMN_PUBLICATION_NUMBER, -1);
                                                db.insert(DatabaseHelper.TABLE_WORK_DESCRIPTION, null, cv);

                                                userCursor = db.query(DatabaseHelper.TABLE_WORK_DESCRIPTION, null, null, null, null, null, null);

                                                if (userCursor.moveToLast()) {
                                                    int idNew_work_description = userCursor.getColumnIndex(DatabaseHelper.COLUMN_ID_WORK_DESCRIPTION);
                                                    idNew_work_description = userCursor.getInt(idNew_work_description);

                                                    for (int i = 0; i < lenMasIdThemes; i++) {
                                                        cv = new ContentValues();
                                                        cv.put(DatabaseHelper.COLUMN_ID_WORK_DESCRIPTION, idNew_work_description);
                                                        cv.put(DatabaseHelper.COLUMN_ID_THEME, masIdThemes[i]);
                                                        db.insert(DatabaseHelper.TABLE_THEME_OF_WORK, null, cv);
                                                    }

                                                    id_WDescription = idNew_work_description;

                                                }

                                                userCursor.close();

                                            } else {

                                                id_WDescription = id_work_description;

                                            }

                                            Bundle arguments = getIntent().getExtras();
                                            int id_user = arguments.getInt("id_user");

                                            Date currentDate = new Date();
                                            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                                            String dateText = dateFormat.format(currentDate);

                                            ContentValues cv = new ContentValues();
                                            cv.put(DatabaseHelper.COLUMN_ID_WORK_DESCRIPTION, id_WDescription);
                                            cv.put(DatabaseHelper.COLUMN_ID_USER, id_user);
                                            cv.put(DatabaseHelper.COLUMN_TEXT_WORK, editTextDecisionValue);
                                            cv.put(DatabaseHelper.COLUMN_TIME_OF_CREATION, dateText);

                                            if(imageForDownload.getVisibility() == View.VISIBLE) {

                                                BitmapDrawable drawable = (BitmapDrawable) imageForDownload.getDrawable();
                                                Bitmap bitmap = drawable.getBitmap();

                                                // Записываем изображение в поток байтов.
                                                // При этом изображение можно сжать и / или перекодировать в другой формат.
                                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

                                                // Получаем изображение из потока в виде байтов
                                                byte[] bytes = byteArrayOutputStream.toByteArray();

                                                // Кодируем байты в строку Base64 и возвращаем
                                                String fileForDownload =  Base64.encodeToString(bytes, Base64.DEFAULT);

                                                cv.put(DatabaseHelper.COLUMN_FILE_FROM_WORK, fileForDownload);
                                            } else {
                                                cv.put(DatabaseHelper.COLUMN_FILE_FROM_WORK, "nine");
                                            }

                                            db.insert(DatabaseHelper.TABLE_USER_WORK, null, cv);

                                            userCursor = db.query(DatabaseHelper.TABLE_USER_WORK, null, null, null, null, null, null);

                                            int id_user_work = 0;

                                            if (userCursor.moveToLast()) {
                                                id_user_work = userCursor.getColumnIndex(DatabaseHelper.COLUMN_ID_USER_WORK);
                                                id_user_work = userCursor.getInt(id_user_work);
                                            }

                                            userCursor.close();


                                            Intent intent = new Intent(getApplicationContext(), InformationAboutTheSolution.class);
                                            intent.putExtra("id_user", id_user);
                                            intent.putExtra("id_user_work", id_user_work);
                                            startActivity(intent);

                                        }

                                    }




                                    }

                                }



                            if (!errorFlag) {
                                error = "Что-то пошло не по плану:" + error;
                                errorMessage.setText(error);
                                errorMessage.setPaddingRelative(20, 20,20,20);
                                errorMessage.setVisibility(View.VISIBLE);
                            }

                        }
                    });
                }

                if (item.equals("Лекции")) {

                    errorMessage.setVisibility(View.GONE);

                    editTextDecision.setHint("Текст лекции / Описание лекции / комментарий к тексту лекции");
                    descriptionSection.setVisibility(View.VISIBLE);
                    helpSection.setVisibility(View.VISIBLE);
                    editTextTextSection.setVisibility(View.VISIBLE);
                    editTextTextSection.setEnabled(true);

                    descriptionChapter.setVisibility(View.VISIBLE);
                    helpChapter.setVisibility(View.VISIBLE);
                    editTextChapter.setVisibility(View.VISIBLE);
                    editTextChapter.setEnabled(true);

                    descriptionParagraph.setVisibility(View.VISIBLE);
                    helpParagraph.setVisibility(View.VISIBLE);
                    editTextParagraph.setVisibility(View.VISIBLE);
                    editTextParagraph.setEnabled(true);

                    descriptionNumberOfPublication.setVisibility(View.GONE);
                    helpNumberOfPublication.setVisibility(View.GONE);
                    editTextNumberOfPublication.setVisibility(View.GONE);
                    editTextNumberOfPublication.setEnabled(false);

                    descriptionNumberOfTask.setVisibility(View.GONE);
                    helpNumberOfTask.setVisibility(View.GONE);
                    editTextNumberOfTask.setVisibility(View.GONE);
                    editTextNumberOfTask.setEnabled(false);

                    descriptionNumberOfVariant.setVisibility(View.GONE);
                    helpNumberOfVariant.setVisibility(View.GONE);
                    editTextNumberOfVariant.setVisibility(View.GONE);
                    editTextNumberOfVariant.setEnabled(false);

                    btnCreatingSolution.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            String autoCompleteDisciplineValue = autoCompleteDiscipline.getText().toString();
                            String autoCompleteAuthorValue = autoCompleteAuthor.getText().toString();
                            String autoCompleteThemeValue = autoCompleteTheme.getText().toString();
                            String autoCompleteThemeValue2 = autoCompleteTheme2.getText().toString();
                            String autoCompleteThemeValue3 = autoCompleteTheme3.getText().toString();
                            String autoCompleteThemeValue4 = autoCompleteTheme4.getText().toString();
                            String autoCompleteThemeValue5 = autoCompleteTheme5.getText().toString();

                            String error = "";
                            boolean errorFlag = true;

                            if (autoCompleteDisciplineValue.equals("")) {
                                error += "\n- нужно ввести название дисциплины";
                                errorFlag = false;
                            }

                            if (autoCompleteAuthorValue.equals("")) {
                                error += "\n- нужно ввести имя автора / преподователя";
                                errorFlag = false;
                            }

                            if (autoCompleteThemeValue.equals("")) {
                                error += "\n- нужно ввести название темы";
                                errorFlag = false;
                            }

                            if (autoCompleteThemeValue2.equals("") && (autoCompleteTheme2.getVisibility() == View.VISIBLE)) {
                                error += "\n- нужно ввести название темы 2";
                                errorFlag = false;
                            }

                            if (autoCompleteThemeValue3.equals("") && (autoCompleteTheme3.getVisibility() == View.VISIBLE)) {
                                error += "\n- нужно ввести название темы 3";
                                errorFlag = false;
                            }

                            if (autoCompleteThemeValue4.equals("") && (autoCompleteTheme4.getVisibility() == View.VISIBLE)) {
                                error += "\n- нужно ввести название темы 4";
                                errorFlag = false;
                            }

                            if (autoCompleteThemeValue5.equals("") && (autoCompleteTheme5.getVisibility() == View.VISIBLE)) {
                                error += "\n- нужно ввести название темы 5";
                                errorFlag = false;
                            }

                            boolean repeatingError = true;

                            if (!(autoCompleteThemeValue2.equals("")) && ( autoCompleteThemeValue2.equals(autoCompleteThemeValue)
                                    || autoCompleteThemeValue2.equals(autoCompleteThemeValue3) || autoCompleteThemeValue2.equals(autoCompleteThemeValue4)
                                    || autoCompleteThemeValue2.equals(autoCompleteThemeValue5))) {
                                error += "\n- темы не могут иметь одинаковое название";
                                errorFlag = false;
                                repeatingError = false;
                            }

                            if (repeatingError && !(autoCompleteThemeValue3.equals("")) && ( autoCompleteThemeValue3.equals(autoCompleteThemeValue)
                                    || autoCompleteThemeValue3.equals(autoCompleteThemeValue4)
                                    || autoCompleteThemeValue3.equals(autoCompleteThemeValue5))) {
                                error += "\n- темы не могут иметь одинаковое название";
                                errorFlag = false;
                                repeatingError = false;
                            }

                            if (repeatingError && !(autoCompleteThemeValue4.equals("")) && ( autoCompleteThemeValue4.equals(autoCompleteThemeValue)
                                    || autoCompleteThemeValue4.equals(autoCompleteThemeValue5))) {
                                error += "\n- темы не могут иметь одинаковое название";
                                errorFlag = false;
                                repeatingError = false;
                            }

                            if (repeatingError && !(autoCompleteThemeValue4.equals("")) && autoCompleteThemeValue5.equals(autoCompleteThemeValue)) {
                                error += "\n- темы не могут иметь одинаковое название";
                                errorFlag = false;
                                repeatingError = false;
                            }

                            if (errorFlag) {

                                String regex = "^[а-яёА-ЯЁ\\t\\n\\f\\r\\p{Z}]*$";

                                Pattern pattern = Pattern.compile(regex);
                                Matcher m = pattern.matcher(autoCompleteDisciplineValue);
                                if (!m.matches()){
                                    error += "\n- введены недопустимые символы в названии дисциплины";
                                    errorFlag = false;
                                }

                                m = pattern.matcher(autoCompleteThemeValue);
                                if (!m.matches()){
                                    error += "\n- введены недопустимые символы в названии темы";
                                    errorFlag = false;
                                }

                                m = pattern.matcher(autoCompleteThemeValue2);
                                if (!(m.matches()) && (autoCompleteTheme2.getVisibility() == View.VISIBLE)){
                                    error += "\n- введены недопустимые символы в названии темы 2";
                                    errorFlag = false;
                                }

                                m = pattern.matcher(autoCompleteThemeValue3);
                                if (!(m.matches()) && (autoCompleteTheme3.getVisibility() == View.VISIBLE)){
                                    error += "\n- введены недопустимые символы в названии темы 3";
                                    errorFlag = false;
                                }

                                m = pattern.matcher(autoCompleteThemeValue4);
                                if (!(m.matches()) && (autoCompleteTheme4.getVisibility() == View.VISIBLE)){
                                    error += "\n- введены недопустимые символы в названии темы 4";
                                    errorFlag = false;
                                }

                                m = pattern.matcher(autoCompleteThemeValue5);
                                if (!(m.matches()) && (autoCompleteTheme5.getVisibility() == View.VISIBLE)){
                                    error += "\n- введены недопустимые символы в названии темы 5";
                                    errorFlag = false;
                                }

                                regex = "^[А-Я][а-я]+\\s?[А-Я]\\.\\s?[А-Я]\\.$";
                                pattern = Pattern.compile(regex);
                                m = pattern.matcher(autoCompleteAuthorValue);
                                if (!m.matches()){
                                    error += "\n- имя должно иметь формат: Маяковский В. В.";
                                    errorFlag = false;
                                }

                                if (errorFlag) {

                                    if (autoCompleteDisciplineValue.length() > 50) {
                                        error += "\n- название дисциплины слишком длинное";
                                        errorFlag = false;
                                    }

                                    if (autoCompleteAuthorValue.length() > 33) {
                                        error += "\n- введенное имя автора / преподователя слишком длинное";
                                        errorFlag = false;
                                    }

                                    if (autoCompleteThemeValue.length() > 50) {
                                        error += "\n- название темы слишком длинное";
                                        errorFlag = false;
                                    }

                                    if (errorFlag) {

                                        regex = "[0-9]+";
                                        pattern = Pattern.compile(regex);

                                        m = pattern.matcher(editTextTextSection.getText());
                                        if (!m.matches() && !(editTextTextSection.getText().toString().equals(""))){
                                            error += "\n- в номере раздела можно использовать только цифры";
                                            errorFlag = false;
                                        }

                                        m = pattern.matcher(editTextChapter.getText());
                                        if (!m.matches() && !(editTextChapter.getText().toString().equals(""))){
                                            error += "\n- в номере главы можно использовать только цифры";
                                            errorFlag = false;
                                        }

                                        m = pattern.matcher(editTextParagraph.getText());
                                        if (!m.matches() && !(editTextParagraph.getText().toString().equals(""))){
                                            error += "\n- в номере параграфа можно использовать только цифры";
                                            errorFlag = false;
                                        }

                                        if (errorFlag) {

                                            if (editTextTextSection.getText().length() > 3) {
                                                error += "\n- номер раздела слишком большой";
                                                errorFlag = false;
                                            }

                                            if (editTextChapter.getText().length() > 3) {
                                                error += "\n- номер главы слишком большой";
                                                errorFlag = false;
                                            }

                                            if (editTextParagraph.getText().length() > 3) {
                                                error += "\n- номер параграфа слишком большой";
                                                errorFlag = false;
                                            }

                                            if (errorFlag) {

                                                String editTextDecisionValue = editTextDecision.getText().toString();

                                                if (editTextDecisionValue.equals("")) {
                                                    error += "\n- нужно ввести текст лекции";
                                                    errorFlag = false;
                                                }

                                                if (editTextDecisionValue.length() > 2000) {
                                                    error += "\n- текст лекции слишком большой";
                                                    errorFlag = false;
                                                }

                                                if (errorFlag) {

                                                    //---------------------------------------------------
                                                    boolean noAppend = false;

                                                    Cursor cursor = db.query(DatabaseHelper.TABLE_DISCIPLINES, null, null, null, null, null, null);

                                                    if (cursor.moveToFirst()) {
                                                        int idNameDiscipline = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_DISCIPLINE);

                                                        do {
                                                            if (cursor.getString(idNameDiscipline).equals(autoCompleteDisciplineValue)) {
                                                                noAppend = true;
                                                                break;
                                                            }
                                                        } while (cursor.moveToNext());
                                                    }
                                                    cursor.close();

                                                    if (!noAppend) {
                                                        ContentValues cv = new ContentValues();
                                                        cv.put(DatabaseHelper.COLUMN_NAME_DISCIPLINE, autoCompleteDisciplineValue);
                                                        db.insert(DatabaseHelper.TABLE_DISCIPLINES, null, cv);
                                                    }
                                                    //---------------------------------------------------

                                                    //---------------------------------------------------
                                                    noAppend = false;

                                                    cursor = db.query(DatabaseHelper.TABLE_AUTHORS, null, null, null, null, null, null);

                                                    if (cursor.moveToFirst()) {
                                                        int idNameAuthor = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_AUTHOR);

                                                        do {
                                                            if (cursor.getString(idNameAuthor).equals(autoCompleteAuthorValue)) {
                                                                noAppend = true;
                                                                break;
                                                            }
                                                        } while (cursor.moveToNext());
                                                    }
                                                    cursor.close();

                                                    if (!noAppend) {
                                                        ContentValues cv = new ContentValues();
                                                        cv.put(DatabaseHelper.COLUMN_NAME_AUTHOR, autoCompleteAuthorValue);
                                                        db.insert(DatabaseHelper.TABLE_AUTHORS, null, cv);
                                                    }
                                                    //---------------------------------------------------

                                                    //---------------------------------------------------
                                                    noAppend = false;

                                                    cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                    if (cursor.moveToFirst()) {
                                                        int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                        do {
                                                            if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue)) {
                                                                noAppend = true;
                                                                break;
                                                            }
                                                        } while (cursor.moveToNext());
                                                    }
                                                    cursor.close();

                                                    if (!noAppend) {
                                                        ContentValues cv = new ContentValues();
                                                        cv.put(DatabaseHelper.COLUMN_THEME, autoCompleteThemeValue);
                                                        db.insert(DatabaseHelper.TABLE_THEMES, null, cv);
                                                    }
                                                    //---------------------------------------------------

                                                    //---------------------------------------------------

                                                    if (!autoCompleteThemeValue2.equals("")) {

                                                        noAppend = false;

                                                        cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                        if (cursor.moveToFirst()) {
                                                            int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                            do {
                                                                if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue2)) {
                                                                    noAppend = true;
                                                                    break;
                                                                }
                                                            } while (cursor.moveToNext());
                                                        }
                                                        cursor.close();

                                                        if (!noAppend) {
                                                            ContentValues cv = new ContentValues();
                                                            cv.put(DatabaseHelper.COLUMN_THEME, autoCompleteThemeValue2);
                                                            db.insert(DatabaseHelper.TABLE_THEMES, null, cv);
                                                        }

                                                    }
                                                    //---------------------------------------------------

                                                    //---------------------------------------------------

                                                    if (!autoCompleteThemeValue3.equals("")) {

                                                        noAppend = false;

                                                        cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                        if (cursor.moveToFirst()) {
                                                            int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                            do {
                                                                if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue3)) {
                                                                    noAppend = true;
                                                                    break;
                                                                }
                                                            } while (cursor.moveToNext());
                                                        }
                                                        cursor.close();

                                                        if (!noAppend) {
                                                            ContentValues cv = new ContentValues();
                                                            cv.put(DatabaseHelper.COLUMN_THEME, autoCompleteThemeValue3);
                                                            db.insert(DatabaseHelper.TABLE_THEMES, null, cv);
                                                        }

                                                    }
                                                    //---------------------------------------------------

                                                    //---------------------------------------------------

                                                    if (!autoCompleteThemeValue4.equals("")) {

                                                        noAppend = false;

                                                        cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                        if (cursor.moveToFirst()) {
                                                            int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                            do {
                                                                if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue4)) {
                                                                    noAppend = true;
                                                                    break;
                                                                }
                                                            } while (cursor.moveToNext());
                                                        }
                                                        cursor.close();

                                                        if (!noAppend) {
                                                            ContentValues cv = new ContentValues();
                                                            cv.put(DatabaseHelper.COLUMN_THEME, autoCompleteThemeValue4);
                                                            db.insert(DatabaseHelper.TABLE_THEMES, null, cv);
                                                        }

                                                    }
                                                    //---------------------------------------------------

                                                    //---------------------------------------------------

                                                    if (!autoCompleteThemeValue5.equals("")) {

                                                        noAppend = false;

                                                        cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                        if (cursor.moveToFirst()) {
                                                            int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                            do {
                                                                if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue5)) {
                                                                    noAppend = true;
                                                                    break;
                                                                }
                                                            } while (cursor.moveToNext());
                                                        }
                                                        cursor.close();

                                                        if (!noAppend) {
                                                            ContentValues cv = new ContentValues();
                                                            cv.put(DatabaseHelper.COLUMN_THEME, autoCompleteThemeValue5);
                                                            db.insert(DatabaseHelper.TABLE_THEMES, null, cv);
                                                        }

                                                    }
                                                    //---------------------------------------------------

                                                    //---------------------------------------------------


                                                    cursor = db.query(DatabaseHelper.TABLE_DISCIPLINES, null, null, null, null, null, null);

                                                    int id_discipline = 0;

                                                    if (cursor.moveToFirst()) {
                                                        id_discipline = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_DISCIPLINE);
                                                        int idName_discipline = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_DISCIPLINE);

                                                        do {
                                                            if (cursor.getString(idName_discipline).equals(autoCompleteDisciplineValue)) {
                                                                id_discipline = cursor.getInt(id_discipline);
                                                                break;
                                                            }
                                                        } while (cursor.moveToNext());
                                                    }
                                                    cursor.close();


                                                    cursor = db.query(DatabaseHelper.TABLE_AUTHORS, null, null, null, null, null, null);

                                                    int id_author = 0;

                                                    if (cursor.moveToFirst()) {
                                                        id_author = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_AUTHOR);
                                                        int idName_author = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_AUTHOR);

                                                        do {
                                                            if (cursor.getString(idName_author).equals(autoCompleteAuthorValue)) {
                                                                id_author = cursor.getInt(id_author);
                                                                break;
                                                            }
                                                        } while (cursor.moveToNext());
                                                    }
                                                    cursor.close();

                                                    int[] masIdThemes = new int[5];
                                                    int lenMasIdThemes = 0;

                                                    cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                    if (cursor.moveToFirst()) {
                                                        int id_theme = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_THEME);
                                                        int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                        do {

                                                            if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue)) {
                                                                masIdThemes[lenMasIdThemes] = cursor.getInt(id_theme);
                                                                lenMasIdThemes++;
                                                            }

                                                            if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue2)) {
                                                                masIdThemes[lenMasIdThemes] = cursor.getInt(id_theme);
                                                                lenMasIdThemes++;
                                                            }

                                                            if ( cursor.getString(idNameTheme).equals(autoCompleteThemeValue3)) {
                                                                masIdThemes[lenMasIdThemes] = cursor.getInt(id_theme);
                                                                lenMasIdThemes++;
                                                            }

                                                            if ( cursor.getString(idNameTheme).equals(autoCompleteThemeValue4)) {
                                                                masIdThemes[lenMasIdThemes] = cursor.getInt(id_theme);
                                                                lenMasIdThemes++;
                                                            }

                                                            if ( cursor.getString(idNameTheme).equals(autoCompleteThemeValue5)) {
                                                                masIdThemes[lenMasIdThemes] = cursor.getInt(id_theme);
                                                                lenMasIdThemes++;
                                                            }

                                                        } while (cursor.moveToNext());
                                                    }
                                                    cursor.close();


                                                    noAppend = false;

                                                    cursor = db.query(DatabaseHelper.TABLE_WORK_DESCRIPTION, null, null, null, null, null, null);

                                                    int id_work_description = 0;

                                                    if (cursor.moveToFirst()) {
                                                        id_work_description = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_WORK_DESCRIPTION);
                                                        int idType_of_work = cursor.getColumnIndex(DatabaseHelper.COLUMN_TYPE_OF_WORK);
                                                        int id_discipline2 = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_DISCIPLINE);
                                                        int id_author2 = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_AUTHOR);
                                                        int section_number = cursor.getColumnIndex(DatabaseHelper.COLUMN_SECTION_NUMBER);
                                                        int chapter_number = cursor.getColumnIndex(DatabaseHelper.COLUMN_CHAPTER_NUMBER);
                                                        int paragraph_number = cursor.getColumnIndex(DatabaseHelper.COLUMN_PARAGRAPH_NUMBER);

                                                        do {

                                                            if (cursor.getString(idType_of_work).equals(item) && cursor.getInt(id_discipline2) == id_discipline
                                                                    &&  cursor.getInt(id_author2) == id_author
                                                                    &&  cursor.getInt(section_number) == Integer.parseInt(editTextTextSection.getText().toString())
                                                                    &&  cursor.getInt(chapter_number) == Integer.parseInt(editTextChapter.getText().toString())
                                                                    &&  cursor.getInt(paragraph_number) == Integer.parseInt(editTextParagraph.getText().toString())) {

                                                                int[] masIdThemes2 = new int[5];
                                                                int lenMasIdThemes2 = 0;

                                                                userCursor = db.query(DatabaseHelper.TABLE_THEME_OF_WORK, null, null, null, null, null, null);

                                                                if (userCursor.moveToFirst()) {
                                                                    int id_theme = userCursor.getColumnIndex(DatabaseHelper.COLUMN_ID_THEME);
                                                                    int id_workDescription = userCursor.getColumnIndex(DatabaseHelper.COLUMN_ID_WORK_DESCRIPTION);

                                                                    do {
                                                                        if (userCursor.getInt(id_workDescription) == cursor.getInt(id_work_description)) {
                                                                            masIdThemes2[lenMasIdThemes2] = userCursor.getInt(id_theme);
                                                                            lenMasIdThemes2++;
                                                                        }
                                                                    } while (userCursor.moveToNext());
                                                                }
                                                                userCursor.close();

                                                                boolean coincide = false;
                                                                int partCoincide = 0;

                                                                if (lenMasIdThemes2 == lenMasIdThemes) {
                                                                    for (int i =0; i < lenMasIdThemes2; i++) {
                                                                        for (int j = 0; j < lenMasIdThemes; j++) {
                                                                            if (masIdThemes2[i] == masIdThemes[j]) {
                                                                                partCoincide++;
                                                                                break;
                                                                            }
                                                                        }
                                                                    }
                                                                }


                                                                if (partCoincide == lenMasIdThemes2) {
                                                                    coincide = true;
                                                                }

                                                                if (coincide) {
                                                                    id_work_description = cursor.getInt(id_work_description);
                                                                    noAppend = true;
                                                                    break;
                                                                }
                                                            }
                                                        } while (cursor.moveToNext());
                                                    }
                                                    cursor.close();

                                                    int id_WDescription = 0;

                                                    if (!noAppend) {

                                                        ContentValues cv = new ContentValues();
                                                        cv.put(DatabaseHelper.COLUMN_TYPE_OF_WORK, item);
                                                        cv.put(DatabaseHelper.COLUMN_ID_DISCIPLINE, id_discipline);
                                                        cv.put(DatabaseHelper.COLUMN_ID_AUTHOR, id_author);
                                                        cv.put(DatabaseHelper.COLUMN_TASK_NUMBER, -1);
                                                        cv.put(DatabaseHelper.COLUMN_OPTION_NUMBER, -1);
                                                        cv.put(DatabaseHelper.COLUMN_SECTION_NUMBER, Integer.parseInt(editTextTextSection.getText().toString()));
                                                        cv.put(DatabaseHelper.COLUMN_CHAPTER_NUMBER, Integer.parseInt(editTextChapter.getText().toString()));
                                                        cv.put(DatabaseHelper.COLUMN_PARAGRAPH_NUMBER, Integer.parseInt(editTextParagraph.getText().toString()));
                                                        cv.put(DatabaseHelper.COLUMN_PUBLICATION_NUMBER, -1);
                                                        db.insert(DatabaseHelper.TABLE_WORK_DESCRIPTION, null, cv);

                                                        userCursor = db.query(DatabaseHelper.TABLE_WORK_DESCRIPTION, null, null, null, null, null, null);

                                                        if (userCursor.moveToLast()) {
                                                            int idNew_work_description = userCursor.getColumnIndex(DatabaseHelper.COLUMN_ID_WORK_DESCRIPTION);
                                                            idNew_work_description = userCursor.getInt(idNew_work_description);

                                                            for (int i = 0; i < lenMasIdThemes; i++) {
                                                                cv = new ContentValues();
                                                                cv.put(DatabaseHelper.COLUMN_ID_WORK_DESCRIPTION, idNew_work_description);
                                                                cv.put(DatabaseHelper.COLUMN_ID_THEME, masIdThemes[i]);
                                                                db.insert(DatabaseHelper.TABLE_THEME_OF_WORK, null, cv);
                                                            }

                                                            id_WDescription = idNew_work_description;

                                                        }

                                                        userCursor.close();

                                                    } else {

                                                        id_WDescription = id_work_description;

                                                    }

                                                    Bundle arguments = getIntent().getExtras();
                                                    int id_user = arguments.getInt("id_user");

                                                    Date currentDate = new Date();
                                                    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                                                    String dateText = dateFormat.format(currentDate);

                                                    ContentValues cv = new ContentValues();
                                                    cv.put(DatabaseHelper.COLUMN_ID_WORK_DESCRIPTION, id_WDescription);
                                                    cv.put(DatabaseHelper.COLUMN_ID_USER, id_user);
                                                    cv.put(DatabaseHelper.COLUMN_TEXT_WORK, editTextDecisionValue);
                                                    cv.put(DatabaseHelper.COLUMN_TIME_OF_CREATION, dateText);

                                                    if(imageForDownload.getVisibility() == View.VISIBLE) {

                                                        BitmapDrawable drawable = (BitmapDrawable) imageForDownload.getDrawable();
                                                        Bitmap bitmap = drawable.getBitmap();

                                                        // Записываем изображение в поток байтов.
                                                        // При этом изображение можно сжать и / или перекодировать в другой формат.
                                                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

                                                        // Получаем изображение из потока в виде байтов
                                                        byte[] bytes = byteArrayOutputStream.toByteArray();

                                                        // Кодируем байты в строку Base64 и возвращаем
                                                        String fileForDownload =  Base64.encodeToString(bytes, Base64.DEFAULT);

                                                        cv.put(DatabaseHelper.COLUMN_FILE_FROM_WORK, fileForDownload);
                                                    } else {
                                                        cv.put(DatabaseHelper.COLUMN_FILE_FROM_WORK, "nine");
                                                    }

                                                    db.insert(DatabaseHelper.TABLE_USER_WORK, null, cv);

                                                    userCursor = db.query(DatabaseHelper.TABLE_USER_WORK, null, null, null, null, null, null);

                                                    int id_user_work = 0;

                                                    if (userCursor.moveToLast()) {
                                                        id_user_work = userCursor.getColumnIndex(DatabaseHelper.COLUMN_ID_USER_WORK);
                                                        id_user_work = userCursor.getInt(id_user_work);
                                                    }

                                                    userCursor.close();

                                                    Intent intent = new Intent(getApplicationContext(), InformationAboutTheSolution.class);
                                                    intent.putExtra("id_user", id_user);
                                                    intent.putExtra("id_user_work", id_user_work);
                                                    startActivity(intent);

                                                }

                                            }




                                            }

                                        }

                                    }

                                }



                            if (!errorFlag) {
                                error = "Что-то пошло не по плану:" + error;
                                errorMessage.setText(error);
                                errorMessage.setPaddingRelative(20, 20,20,20);
                                errorMessage.setVisibility(View.VISIBLE);
                            }

                        }
                    });

                }

                if (item.equals("Семинары")) {

                    errorMessage.setVisibility(View.GONE);

                    editTextDecision.setHint("Задачи с семинара и их решение / текст аудиторной работа");
                    descriptionSection.setVisibility(View.VISIBLE);
                    helpSection.setVisibility(View.VISIBLE);
                    editTextTextSection.setVisibility(View.VISIBLE);
                    editTextTextSection.setEnabled(true);

                    descriptionChapter.setVisibility(View.VISIBLE);
                    helpChapter.setVisibility(View.VISIBLE);
                    editTextChapter.setVisibility(View.VISIBLE);
                    editTextChapter.setEnabled(true);

                    descriptionParagraph.setVisibility(View.VISIBLE);
                    helpParagraph.setVisibility(View.VISIBLE);
                    editTextParagraph.setVisibility(View.VISIBLE);
                    editTextParagraph.setEnabled(true);

                    descriptionNumberOfPublication.setVisibility(View.GONE);
                    helpNumberOfPublication.setVisibility(View.GONE);
                    editTextNumberOfPublication.setVisibility(View.GONE);
                    editTextNumberOfPublication.setEnabled(false);

                    descriptionSection.setVisibility(View.VISIBLE);
                    helpSection.setVisibility(View.VISIBLE);
                    editTextTextSection.setVisibility(View.VISIBLE);
                    editTextTextSection.setEnabled(true);

                    descriptionNumberOfTask.setVisibility(View.VISIBLE);
                    helpNumberOfTask.setVisibility(View.VISIBLE);
                    editTextNumberOfTask.setVisibility(View.VISIBLE);
                    editTextNumberOfTask.setEnabled(true);

                    descriptionNumberOfVariant.setVisibility(View.GONE);
                    helpNumberOfVariant.setVisibility(View.GONE);
                    editTextNumberOfVariant.setVisibility(View.GONE);
                    editTextNumberOfVariant.setEnabled(false);

                    btnCreatingSolution.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            String autoCompleteDisciplineValue = autoCompleteDiscipline.getText().toString();
                            String autoCompleteAuthorValue = autoCompleteAuthor.getText().toString();
                            String autoCompleteThemeValue = autoCompleteTheme.getText().toString();
                            String autoCompleteThemeValue2 = autoCompleteTheme2.getText().toString();
                            String autoCompleteThemeValue3 = autoCompleteTheme3.getText().toString();
                            String autoCompleteThemeValue4 = autoCompleteTheme4.getText().toString();
                            String autoCompleteThemeValue5 = autoCompleteTheme5.getText().toString();

                            String error = "";
                            boolean errorFlag = true;

                            if (autoCompleteDisciplineValue.equals("")) {
                                error += "\n- нужно ввести название дисциплины";
                                errorFlag = false;
                            }

                            if (autoCompleteAuthorValue.equals("")) {
                                error += "\n- нужно ввести имя автора / преподователя";
                                errorFlag = false;
                            }

                            if (autoCompleteThemeValue.equals("")) {
                                error += "\n- нужно ввести название темы";
                                errorFlag = false;
                            }

                            if (autoCompleteThemeValue2.equals("") && (autoCompleteTheme2.getVisibility() == View.VISIBLE)) {
                                error += "\n- нужно ввести название темы 2";
                                errorFlag = false;
                            }

                            if (autoCompleteThemeValue3.equals("") && (autoCompleteTheme3.getVisibility() == View.VISIBLE)) {
                                error += "\n- нужно ввести название темы 3";
                                errorFlag = false;
                            }

                            if (autoCompleteThemeValue4.equals("") && (autoCompleteTheme4.getVisibility() == View.VISIBLE)) {
                                error += "\n- нужно ввести название темы 4";
                                errorFlag = false;
                            }

                            if (autoCompleteThemeValue5.equals("") && (autoCompleteTheme5.getVisibility() == View.VISIBLE)) {
                                error += "\n- нужно ввести название темы 5";
                                errorFlag = false;
                            }

                            boolean repeatingError = true;

                            if (!(autoCompleteThemeValue2.equals("")) && ( autoCompleteThemeValue2.equals(autoCompleteThemeValue)
                                    || autoCompleteThemeValue2.equals(autoCompleteThemeValue3) || autoCompleteThemeValue2.equals(autoCompleteThemeValue4)
                                    || autoCompleteThemeValue2.equals(autoCompleteThemeValue5))) {
                                error += "\n- темы не могут иметь одинаковое название";
                                errorFlag = false;
                                repeatingError = false;
                            }

                            if (repeatingError && !(autoCompleteThemeValue3.equals("")) && ( autoCompleteThemeValue3.equals(autoCompleteThemeValue)
                                    || autoCompleteThemeValue3.equals(autoCompleteThemeValue4)
                                    || autoCompleteThemeValue3.equals(autoCompleteThemeValue5))) {
                                error += "\n- темы не могут иметь одинаковое название";
                                errorFlag = false;
                                repeatingError = false;
                            }

                            if (repeatingError && !(autoCompleteThemeValue4.equals("")) && ( autoCompleteThemeValue4.equals(autoCompleteThemeValue)
                                    || autoCompleteThemeValue4.equals(autoCompleteThemeValue5))) {
                                error += "\n- темы не могут иметь одинаковое название";
                                errorFlag = false;
                                repeatingError = false;
                            }

                            if (repeatingError && !(autoCompleteThemeValue4.equals("")) && autoCompleteThemeValue5.equals(autoCompleteThemeValue)) {
                                error += "\n- темы не могут иметь одинаковое название";
                                errorFlag = false;
                                repeatingError = false;
                            }

                            if (errorFlag) {

                                String regex = "^[а-яёА-ЯЁ\\t\\n\\f\\r\\p{Z}]*$";

                                Pattern pattern = Pattern.compile(regex);
                                Matcher m = pattern.matcher(autoCompleteDisciplineValue);
                                if (!m.matches()){
                                    error += "\n- введены недопустимые символы в названии дисциплины";
                                    errorFlag = false;
                                }

                                m = pattern.matcher(autoCompleteThemeValue);
                                if (!m.matches()){
                                    error += "\n- введены недопустимые символы в названии темы";
                                    errorFlag = false;
                                }

                                m = pattern.matcher(autoCompleteThemeValue2);
                                if (!(m.matches()) && (autoCompleteTheme2.getVisibility() == View.VISIBLE)){
                                    error += "\n- введены недопустимые символы в названии темы 2";
                                    errorFlag = false;
                                }

                                m = pattern.matcher(autoCompleteThemeValue3);
                                if (!(m.matches()) && (autoCompleteTheme3.getVisibility() == View.VISIBLE)){
                                    error += "\n- введены недопустимые символы в названии темы 3";
                                    errorFlag = false;
                                }

                                m = pattern.matcher(autoCompleteThemeValue4);
                                if (!(m.matches()) && (autoCompleteTheme4.getVisibility() == View.VISIBLE)){
                                    error += "\n- введены недопустимые символы в названии темы 4";
                                    errorFlag = false;
                                }

                                m = pattern.matcher(autoCompleteThemeValue5);
                                if (!(m.matches()) && (autoCompleteTheme5.getVisibility() == View.VISIBLE)){
                                    error += "\n- введены недопустимые символы в названии темы 5";
                                    errorFlag = false;
                                }

                                regex = "^[А-Я][а-я]+\\s?[А-Я]\\.\\s?[А-Я]\\.$";
                                pattern = Pattern.compile(regex);
                                m = pattern.matcher(autoCompleteAuthorValue);
                                if (!m.matches()){
                                    error += "\n- имя должно иметь формат: Маяковский В. В.";
                                    errorFlag = false;
                                }

                                if (errorFlag) {

                                    if (autoCompleteDisciplineValue.length() > 50) {
                                        error += "\n- название дисциплины слишком длинное";
                                        errorFlag = false;
                                    }

                                    if (autoCompleteAuthorValue.length() > 33) {
                                        error += "\n- введенное имя автора / преподователя слишком длинное";
                                        errorFlag = false;
                                    }

                                    if (autoCompleteThemeValue.length() > 50) {
                                        error += "\n- название темы слишком длинное";
                                        errorFlag = false;
                                    }

                                    if (errorFlag) {

                                        if (editTextNumberOfTask.getText().toString().equals("")) {
                                            error += "\n- нужно ввести номер задания";
                                            errorFlag = false;
                                        }

                                        if (errorFlag) {

                                            regex = "[0-9]+";
                                            pattern = Pattern.compile(regex);

                                            m = pattern.matcher(editTextTextSection.getText());
                                            if (!m.matches() && !(editTextTextSection.getText().toString().equals(""))){
                                                error += "\n- в номере раздела можно использовать только цифры";
                                                errorFlag = false;
                                            }

                                            m = pattern.matcher(editTextChapter.getText());
                                            if (!m.matches() && !(editTextChapter.getText().toString().equals(""))){
                                                error += "\n- в номере главы можно использовать только цифры";
                                                errorFlag = false;
                                            }

                                            m = pattern.matcher(editTextParagraph.getText());
                                            if (!m.matches() && !(editTextParagraph.getText().toString().equals(""))){
                                                error += "\n- в номере параграфа можно использовать только цифры";
                                                errorFlag = false;
                                            }


                                            m = pattern.matcher(editTextNumberOfTask.getText());
                                            if (!m.matches() && !(editTextNumberOfTask.getText().toString().equals(""))){
                                                error += "\n- в номере задания можно использовать только цифры";
                                                errorFlag = false;
                                            }

                                            if (errorFlag) {

                                                if (editTextNumberOfTask.getText().length() > 4) {
                                                    error += "\n- номер задания слишком большой";
                                                    errorFlag = false;
                                                }

                                                if (editTextTextSection.getText().length() > 3) {
                                                    error += "\n- номер раздела слишком большой";
                                                    errorFlag = false;
                                                }

                                                if (editTextParagraph.getText().length() > 3) {
                                                    error += "\n- номер параграфа слишком большой";
                                                    errorFlag = false;
                                                }

                                                if (editTextChapter.getText().length() > 3) {
                                                    error += "\n- номер главы слишком большой";
                                                    errorFlag = false;
                                                }

                                                if (errorFlag) {

                                                    String editTextDecisionValue = editTextDecision.getText().toString();

                                                    if (editTextDecisionValue.equals("")) {
                                                        error += "\n- введите текст семинара";
                                                        errorFlag = false;
                                                    }

                                                    if (editTextDecisionValue.length() > 2000) {
                                                        error += "\n- текст решения слишком большой";
                                                        errorFlag = false;
                                                    }

                                                    if (errorFlag) {

                                                        //---------------------------------------------------
                                                        boolean noAppend = false;

                                                        Cursor cursor = db.query(DatabaseHelper.TABLE_DISCIPLINES, null, null, null, null, null, null);

                                                        if (cursor.moveToFirst()) {
                                                            int idNameDiscipline = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_DISCIPLINE);

                                                            do {
                                                                if (cursor.getString(idNameDiscipline).equals(autoCompleteDisciplineValue)) {
                                                                    noAppend = true;
                                                                    break;
                                                                }
                                                            } while (cursor.moveToNext());
                                                        }
                                                        cursor.close();

                                                        if (!noAppend) {
                                                            ContentValues cv = new ContentValues();
                                                            cv.put(DatabaseHelper.COLUMN_NAME_DISCIPLINE, autoCompleteDisciplineValue);
                                                            db.insert(DatabaseHelper.TABLE_DISCIPLINES, null, cv);
                                                        }
                                                        //---------------------------------------------------

                                                        //---------------------------------------------------
                                                        noAppend = false;

                                                        cursor = db.query(DatabaseHelper.TABLE_AUTHORS, null, null, null, null, null, null);

                                                        if (cursor.moveToFirst()) {
                                                            int idNameAuthor = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_AUTHOR);

                                                            do {
                                                                if (cursor.getString(idNameAuthor).equals(autoCompleteAuthorValue)) {
                                                                    noAppend = true;
                                                                    break;
                                                                }
                                                            } while (cursor.moveToNext());
                                                        }
                                                        cursor.close();

                                                        if (!noAppend) {
                                                            ContentValues cv = new ContentValues();
                                                            cv.put(DatabaseHelper.COLUMN_NAME_AUTHOR, autoCompleteAuthorValue);
                                                            db.insert(DatabaseHelper.TABLE_AUTHORS, null, cv);

                                                        }
                                                        //---------------------------------------------------

                                                        //---------------------------------------------------
                                                        noAppend = false;

                                                        cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                        if (cursor.moveToFirst()) {
                                                            int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                            do {
                                                                if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue)) {
                                                                    noAppend = true;
                                                                    break;
                                                                }
                                                            } while (cursor.moveToNext());
                                                        }
                                                        cursor.close();

                                                        if (!noAppend) {
                                                            ContentValues cv = new ContentValues();
                                                            cv.put(DatabaseHelper.COLUMN_THEME, autoCompleteThemeValue);
                                                            db.insert(DatabaseHelper.TABLE_THEMES, null, cv);
                                                        }
                                                        //---------------------------------------------------

                                                        //---------------------------------------------------

                                                        if (!autoCompleteThemeValue2.equals("")) {

                                                            noAppend = false;

                                                            cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                            if (cursor.moveToFirst()) {
                                                                int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                                do {
                                                                    if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue2)) {
                                                                        noAppend = true;
                                                                        break;
                                                                    }
                                                                } while (cursor.moveToNext());
                                                            }
                                                            cursor.close();

                                                            if (!noAppend) {
                                                                ContentValues cv = new ContentValues();
                                                                cv.put(DatabaseHelper.COLUMN_THEME, autoCompleteThemeValue2);
                                                                db.insert(DatabaseHelper.TABLE_THEMES, null, cv);
                                                            }

                                                        }
                                                        //---------------------------------------------------

                                                        //---------------------------------------------------

                                                        if (!autoCompleteThemeValue3.equals("")) {

                                                            noAppend = false;

                                                            cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                            if (cursor.moveToFirst()) {
                                                                int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                                do {
                                                                    if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue3)) {
                                                                        noAppend = true;
                                                                        break;
                                                                    }
                                                                } while (cursor.moveToNext());
                                                            }
                                                            cursor.close();

                                                            if (!noAppend) {
                                                                ContentValues cv = new ContentValues();
                                                                cv.put(DatabaseHelper.COLUMN_THEME, autoCompleteThemeValue3);
                                                                db.insert(DatabaseHelper.TABLE_THEMES, null, cv);
                                                            }

                                                        }
                                                        //---------------------------------------------------

                                                        //---------------------------------------------------

                                                        if (!autoCompleteThemeValue4.equals("")) {

                                                            noAppend = false;

                                                            cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                            if (cursor.moveToFirst()) {
                                                                int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                                do {
                                                                    if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue4)) {
                                                                        noAppend = true;
                                                                        break;
                                                                    }
                                                                } while (cursor.moveToNext());
                                                            }
                                                            cursor.close();

                                                            if (!noAppend) {
                                                                ContentValues cv = new ContentValues();
                                                                cv.put(DatabaseHelper.COLUMN_THEME, autoCompleteThemeValue4);
                                                                db.insert(DatabaseHelper.TABLE_THEMES, null, cv);
                                                            }

                                                        }
                                                        //---------------------------------------------------

                                                        //---------------------------------------------------

                                                        if (!autoCompleteThemeValue5.equals("")) {

                                                            noAppend = false;

                                                            cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                            if (cursor.moveToFirst()) {
                                                                int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                                do {
                                                                    if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue5)) {
                                                                        noAppend = true;
                                                                        break;
                                                                    }
                                                                } while (cursor.moveToNext());
                                                            }
                                                            cursor.close();

                                                            if (!noAppend) {
                                                                ContentValues cv = new ContentValues();
                                                                cv.put(DatabaseHelper.COLUMN_THEME, autoCompleteThemeValue5);
                                                                db.insert(DatabaseHelper.TABLE_THEMES, null, cv);
                                                            }

                                                        }
                                                        //---------------------------------------------------

                                                        //---------------------------------------------------


                                                        cursor = db.query(DatabaseHelper.TABLE_DISCIPLINES, null, null, null, null, null, null);

                                                        int id_discipline = 0;

                                                        if (cursor.moveToFirst()) {
                                                            id_discipline = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_DISCIPLINE);
                                                            int idName_discipline = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_DISCIPLINE);

                                                            do {
                                                                if (cursor.getString(idName_discipline).equals(autoCompleteDisciplineValue)) {
                                                                    id_discipline = cursor.getInt(id_discipline);
                                                                    break;
                                                                }
                                                            } while (cursor.moveToNext());
                                                        }
                                                        cursor.close();


                                                        cursor = db.query(DatabaseHelper.TABLE_AUTHORS, null, null, null, null, null, null);

                                                        int id_author = 0;

                                                        if (cursor.moveToFirst()) {
                                                            id_author = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_AUTHOR);
                                                            int idName_author = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_AUTHOR);

                                                            do {
                                                                if (cursor.getString(idName_author).equals(autoCompleteAuthorValue)) {
                                                                    id_author = cursor.getInt(id_author);
                                                                    break;
                                                                }
                                                            } while (cursor.moveToNext());
                                                        }
                                                        cursor.close();

                                                        int[] masIdThemes = new int[5];
                                                        int lenMasIdThemes = 0;

                                                        cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                        if (cursor.moveToFirst()) {
                                                            int id_theme = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_THEME);
                                                            int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                            do {

                                                                if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue)) {
                                                                    masIdThemes[lenMasIdThemes] = cursor.getInt(id_theme);
                                                                    lenMasIdThemes++;
                                                                }

                                                                if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue2)) {
                                                                    masIdThemes[lenMasIdThemes] = cursor.getInt(id_theme);
                                                                    lenMasIdThemes++;
                                                                }

                                                                if ( cursor.getString(idNameTheme).equals(autoCompleteThemeValue3)) {
                                                                    masIdThemes[lenMasIdThemes] = cursor.getInt(id_theme);
                                                                    lenMasIdThemes++;
                                                                }

                                                                if ( cursor.getString(idNameTheme).equals(autoCompleteThemeValue4)) {
                                                                    masIdThemes[lenMasIdThemes] = cursor.getInt(id_theme);
                                                                    lenMasIdThemes++;
                                                                }

                                                                if ( cursor.getString(idNameTheme).equals(autoCompleteThemeValue5)) {
                                                                    masIdThemes[lenMasIdThemes] = cursor.getInt(id_theme);
                                                                    lenMasIdThemes++;
                                                                }

                                                            } while (cursor.moveToNext());
                                                        }
                                                        cursor.close();


                                                        noAppend = false;

                                                        cursor = db.query(DatabaseHelper.TABLE_WORK_DESCRIPTION, null, null, null, null, null, null);

                                                        int id_work_description = 0;

                                                        if (cursor.moveToFirst()) {
                                                            id_work_description = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_WORK_DESCRIPTION);
                                                            int idType_of_work = cursor.getColumnIndex(DatabaseHelper.COLUMN_TYPE_OF_WORK);
                                                            int id_discipline2 = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_DISCIPLINE);
                                                            int id_author2 = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_AUTHOR);
                                                            int idTask_number = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_NUMBER);
                                                            int section_number = cursor.getColumnIndex(DatabaseHelper.COLUMN_SECTION_NUMBER);
                                                            int paragraph_number = cursor.getColumnIndex(DatabaseHelper.COLUMN_PARAGRAPH_NUMBER);
                                                            int chapter_number = cursor.getColumnIndex(DatabaseHelper.COLUMN_CHAPTER_NUMBER);

                                                            do {

                                                                if (cursor.getString(idType_of_work).equals(item) && cursor.getInt(id_discipline2) == id_discipline
                                                                        &&  cursor.getInt(id_author2) == id_author
                                                                        &&  cursor.getInt(idTask_number) == Integer.parseInt(editTextNumberOfTask.getText().toString())
                                                                        &&  cursor.getInt(section_number) == Integer.parseInt(editTextTextSection.getText().toString())
                                                                        &&  cursor.getInt(paragraph_number) == Integer.parseInt(editTextParagraph.getText().toString())
                                                                        &&  cursor.getInt(chapter_number) == Integer.parseInt(editTextChapter.getText().toString())) {

                                                                    int[] masIdThemes2 = new int[5];
                                                                    int lenMasIdThemes2 = 0;

                                                                    userCursor = db.query(DatabaseHelper.TABLE_THEME_OF_WORK, null, null, null, null, null, null);

                                                                    if (userCursor.moveToFirst()) {
                                                                        int id_theme = userCursor.getColumnIndex(DatabaseHelper.COLUMN_ID_THEME);
                                                                        int id_workDescription = userCursor.getColumnIndex(DatabaseHelper.COLUMN_ID_WORK_DESCRIPTION);

                                                                        do {
                                                                            if (userCursor.getInt(id_workDescription) == cursor.getInt(id_work_description)) {
                                                                                masIdThemes2[lenMasIdThemes2] = userCursor.getInt(id_theme);
                                                                                lenMasIdThemes2++;
                                                                            }
                                                                        } while (userCursor.moveToNext());
                                                                    }
                                                                    userCursor.close();

                                                                    boolean coincide = false;
                                                                    int partCoincide = 0;

                                                                    if (lenMasIdThemes2 == lenMasIdThemes) {
                                                                        for (int i =0; i < lenMasIdThemes2; i++) {
                                                                            for (int j = 0; j < lenMasIdThemes; j++) {
                                                                                if (masIdThemes2[i] == masIdThemes[j]) {
                                                                                    partCoincide++;
                                                                                    break;
                                                                                }
                                                                            }
                                                                        }
                                                                    }


                                                                    if (partCoincide == lenMasIdThemes2) {
                                                                        coincide = true;
                                                                    }

                                                                    if (coincide) {
                                                                        id_work_description = cursor.getInt(id_work_description);
                                                                        noAppend = true;
                                                                        break;
                                                                    }
                                                                }
                                                            } while (cursor.moveToNext());
                                                        }
                                                        cursor.close();

                                                        int id_WDescription = 0;

                                                        if (!noAppend) {

                                                            ContentValues cv = new ContentValues();
                                                            cv.put(DatabaseHelper.COLUMN_TYPE_OF_WORK, item);
                                                            cv.put(DatabaseHelper.COLUMN_ID_DISCIPLINE, id_discipline);
                                                            cv.put(DatabaseHelper.COLUMN_ID_AUTHOR, id_author);
                                                            cv.put(DatabaseHelper.COLUMN_TASK_NUMBER, Integer.parseInt(editTextNumberOfTask.getText().toString()));
                                                            cv.put(DatabaseHelper.COLUMN_OPTION_NUMBER, -1);
                                                            cv.put(DatabaseHelper.COLUMN_SECTION_NUMBER, Integer.parseInt(editTextTextSection.getText().toString()));
                                                            cv.put(DatabaseHelper.COLUMN_CHAPTER_NUMBER, Integer.parseInt(editTextChapter.getText().toString()));
                                                            cv.put(DatabaseHelper.COLUMN_PARAGRAPH_NUMBER, Integer.parseInt(editTextParagraph.getText().toString()));
                                                            cv.put(DatabaseHelper.COLUMN_PUBLICATION_NUMBER, -1);
                                                            db.insert(DatabaseHelper.TABLE_WORK_DESCRIPTION, null, cv);

                                                            userCursor = db.query(DatabaseHelper.TABLE_WORK_DESCRIPTION, null, null, null, null, null, null);

                                                            if (userCursor.moveToLast()) {
                                                                int idNew_work_description = userCursor.getColumnIndex(DatabaseHelper.COLUMN_ID_WORK_DESCRIPTION);
                                                                idNew_work_description = userCursor.getInt(idNew_work_description);

                                                                for (int i = 0; i < lenMasIdThemes; i++) {
                                                                    cv = new ContentValues();
                                                                    cv.put(DatabaseHelper.COLUMN_ID_WORK_DESCRIPTION, idNew_work_description);
                                                                    cv.put(DatabaseHelper.COLUMN_ID_THEME, masIdThemes[i]);
                                                                    db.insert(DatabaseHelper.TABLE_THEME_OF_WORK, null, cv);
                                                                }

                                                                id_WDescription = idNew_work_description;

                                                            }

                                                            userCursor.close();

                                                        } else {

                                                            id_WDescription = id_work_description;

                                                        }

                                                        Bundle arguments = getIntent().getExtras();
                                                        int id_user = arguments.getInt("id_user");

                                                        Date currentDate = new Date();
                                                        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                                                        String dateText = dateFormat.format(currentDate);

                                                        ContentValues cv = new ContentValues();
                                                        cv.put(DatabaseHelper.COLUMN_ID_WORK_DESCRIPTION, id_WDescription);
                                                        cv.put(DatabaseHelper.COLUMN_ID_USER, id_user);
                                                        cv.put(DatabaseHelper.COLUMN_TEXT_WORK, editTextDecisionValue);
                                                        cv.put(DatabaseHelper.COLUMN_TIME_OF_CREATION, dateText);

                                                        if(imageForDownload.getVisibility() == View.VISIBLE) {

                                                            BitmapDrawable drawable = (BitmapDrawable) imageForDownload.getDrawable();
                                                            Bitmap bitmap = drawable.getBitmap();

                                                            // Записываем изображение в поток байтов.
                                                            // При этом изображение можно сжать и / или перекодировать в другой формат.
                                                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

                                                            // Получаем изображение из потока в виде байтов
                                                            byte[] bytes = byteArrayOutputStream.toByteArray();

                                                            // Кодируем байты в строку Base64 и возвращаем
                                                            String fileForDownload =  Base64.encodeToString(bytes, Base64.DEFAULT);

                                                            cv.put(DatabaseHelper.COLUMN_FILE_FROM_WORK, fileForDownload);
                                                        } else {
                                                            cv.put(DatabaseHelper.COLUMN_FILE_FROM_WORK, "nine");
                                                        }

                                                        db.insert(DatabaseHelper.TABLE_USER_WORK, null, cv);

                                                        userCursor = db.query(DatabaseHelper.TABLE_USER_WORK, null, null, null, null, null, null);

                                                        int id_user_work = 0;

                                                        if (userCursor.moveToLast()) {
                                                            id_user_work = userCursor.getColumnIndex(DatabaseHelper.COLUMN_ID_USER_WORK);
                                                            id_user_work = userCursor.getInt(id_user_work);
                                                        }

                                                        userCursor.close();


                                                        Intent intent = new Intent(getApplicationContext(), InformationAboutTheSolution.class);
                                                        intent.putExtra("id_user", id_user);
                                                        intent.putExtra("id_user_work", id_user_work);
                                                        startActivity(intent);


                                                    }

                                                }

                                            }

                                        }

                                    }

                                }

                            }

                            if (!errorFlag) {
                                error = "Что-то пошло не по плану:" + error;
                                errorMessage.setText(error);
                                errorMessage.setPaddingRelative(20, 20,20,20);
                                errorMessage.setVisibility(View.VISIBLE);
                            }

                        }
                    });

                }

                if (item.equals("Учебная литература")) {

                    errorMessage.setVisibility(View.GONE);

                    editTextDecision.setHint("Описание или комментарий");

                    descriptionSection.setVisibility(View.VISIBLE);
                    helpSection.setVisibility(View.VISIBLE);
                    editTextTextSection.setVisibility(View.VISIBLE);
                    editTextTextSection.setEnabled(true);

                    descriptionChapter.setVisibility(View.VISIBLE);
                    helpChapter.setVisibility(View.VISIBLE);
                    editTextChapter.setVisibility(View.VISIBLE);
                    editTextChapter.setEnabled(true);

                    descriptionParagraph.setVisibility(View.VISIBLE);
                    helpParagraph.setVisibility(View.VISIBLE);
                    editTextParagraph.setVisibility(View.VISIBLE);
                    editTextParagraph.setEnabled(true);

                    descriptionNumberOfPublication.setVisibility(View.VISIBLE);
                    helpNumberOfPublication.setVisibility(View.VISIBLE);
                    editTextNumberOfPublication.setVisibility(View.VISIBLE);
                    editTextNumberOfPublication.setEnabled(true);

                    descriptionNumberOfTask.setVisibility(View.GONE);
                    helpNumberOfTask.setVisibility(View.GONE);
                    editTextNumberOfTask.setVisibility(View.GONE);
                    editTextNumberOfTask.setEnabled(false);

                    descriptionNumberOfVariant.setVisibility(View.GONE);
                    helpNumberOfVariant.setVisibility(View.GONE);
                    editTextNumberOfVariant.setVisibility(View.GONE);
                    editTextNumberOfVariant.setEnabled(false);

                    btnCreatingSolution.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            String autoCompleteDisciplineValue = autoCompleteDiscipline.getText().toString();
                            String autoCompleteAuthorValue = autoCompleteAuthor.getText().toString();
                            String autoCompleteThemeValue = autoCompleteTheme.getText().toString();
                            String autoCompleteThemeValue2 = autoCompleteTheme2.getText().toString();
                            String autoCompleteThemeValue3 = autoCompleteTheme3.getText().toString();
                            String autoCompleteThemeValue4 = autoCompleteTheme4.getText().toString();
                            String autoCompleteThemeValue5 = autoCompleteTheme5.getText().toString();

                            String error = "";
                            boolean errorFlag = true;

                            if (autoCompleteDisciplineValue.equals("")) {
                                error += "\n- нужно ввести название дисциплины";
                                errorFlag = false;
                            }

                            if (autoCompleteAuthorValue.equals("")) {
                                error += "\n- нужно ввести имя автора / преподователя";
                                errorFlag = false;
                            }

                            if (autoCompleteThemeValue.equals("")) {
                                error += "\n- нужно ввести название темы";
                                errorFlag = false;
                            }

                            if (autoCompleteThemeValue2.equals("") && (autoCompleteTheme2.getVisibility() == View.VISIBLE)) {
                                error += "\n- нужно ввести название темы 2";
                                errorFlag = false;
                            }

                            if (autoCompleteThemeValue3.equals("") && (autoCompleteTheme3.getVisibility() == View.VISIBLE)) {
                                error += "\n- нужно ввести название темы 3";
                                errorFlag = false;
                            }

                            if (autoCompleteThemeValue4.equals("") && (autoCompleteTheme4.getVisibility() == View.VISIBLE)) {
                                error += "\n- нужно ввести название темы 4";
                                errorFlag = false;
                            }

                            if (autoCompleteThemeValue5.equals("") && (autoCompleteTheme5.getVisibility() == View.VISIBLE)) {
                                error += "\n- нужно ввести название темы 5";
                                errorFlag = false;
                            }

                            boolean repeatingError = true;

                            if (!(autoCompleteThemeValue2.equals("")) && ( autoCompleteThemeValue2.equals(autoCompleteThemeValue)
                                    || autoCompleteThemeValue2.equals(autoCompleteThemeValue3) || autoCompleteThemeValue2.equals(autoCompleteThemeValue4)
                                    || autoCompleteThemeValue2.equals(autoCompleteThemeValue5))) {
                                error += "\n- темы не могут иметь одинаковое название";
                                errorFlag = false;
                                repeatingError = false;
                            }

                            if (repeatingError && !(autoCompleteThemeValue3.equals("")) && ( autoCompleteThemeValue3.equals(autoCompleteThemeValue)
                                    || autoCompleteThemeValue3.equals(autoCompleteThemeValue4)
                                    || autoCompleteThemeValue3.equals(autoCompleteThemeValue5))) {
                                error += "\n- темы не могут иметь одинаковое название";
                                errorFlag = false;
                                repeatingError = false;
                            }

                            if (repeatingError && !(autoCompleteThemeValue4.equals("")) && ( autoCompleteThemeValue4.equals(autoCompleteThemeValue)
                                    || autoCompleteThemeValue4.equals(autoCompleteThemeValue5))) {
                                error += "\n- темы не могут иметь одинаковое название";
                                errorFlag = false;
                                repeatingError = false;
                            }

                            if (repeatingError && !(autoCompleteThemeValue4.equals("")) && autoCompleteThemeValue5.equals(autoCompleteThemeValue)) {
                                error += "\n- темы не могут иметь одинаковое название";
                                errorFlag = false;
                                repeatingError = false;
                            }

                            if (errorFlag) {

                                String regex = "^[а-яёА-ЯЁ\\t\\n\\f\\r\\p{Z}]*$";

                                Pattern pattern = Pattern.compile(regex);
                                Matcher m = pattern.matcher(autoCompleteDisciplineValue);
                                if (!m.matches()){
                                    error += "\n- введены недопустимые символы в названии дисциплины";
                                    errorFlag = false;
                                }

                                m = pattern.matcher(autoCompleteThemeValue);
                                if (!m.matches()){
                                    error += "\n- введены недопустимые символы в названии темы";
                                    errorFlag = false;
                                }

                                m = pattern.matcher(autoCompleteThemeValue2);
                                if (!(m.matches()) && (autoCompleteTheme2.getVisibility() == View.VISIBLE)){
                                    error += "\n- введены недопустимые символы в названии темы 2";
                                    errorFlag = false;
                                }

                                m = pattern.matcher(autoCompleteThemeValue3);
                                if (!(m.matches()) && (autoCompleteTheme3.getVisibility() == View.VISIBLE)){
                                    error += "\n- введены недопустимые символы в названии темы 3";
                                    errorFlag = false;
                                }

                                m = pattern.matcher(autoCompleteThemeValue4);
                                if (!(m.matches()) && (autoCompleteTheme4.getVisibility() == View.VISIBLE)){
                                    error += "\n- введены недопустимые символы в названии темы 4";
                                    errorFlag = false;
                                }

                                m = pattern.matcher(autoCompleteThemeValue5);
                                if (!(m.matches()) && (autoCompleteTheme5.getVisibility() == View.VISIBLE)){
                                    error += "\n- введены недопустимые символы в названии темы 5";
                                    errorFlag = false;
                                }

                                regex = "^[А-Я][а-я]+\\s?[А-Я]\\.\\s?[А-Я]\\.$";
                                pattern = Pattern.compile(regex);
                                m = pattern.matcher(autoCompleteAuthorValue);
                                if (!m.matches()){
                                    error += "\n- имя должно иметь формат: Маяковский В. В.";
                                    errorFlag = false;
                                }

                                if (errorFlag) {

                                    if (autoCompleteDisciplineValue.length() > 50) {
                                        error += "\n- название дисциплины слишком длинное";
                                        errorFlag = false;
                                    }

                                    if (autoCompleteAuthorValue.length() > 33) {
                                        error += "\n- введенное имя автора / преподователя слишком длинное";
                                        errorFlag = false;
                                    }

                                    if (autoCompleteThemeValue.length() > 50) {
                                        error += "\n- название темы слишком длинное";
                                        errorFlag = false;
                                    }

                                    if (errorFlag) {

                                        regex = "[0-9]+";
                                        pattern = Pattern.compile(regex);

                                        m = pattern.matcher(editTextTextSection.getText());
                                        if (!m.matches() && !(editTextTextSection.getText().toString().equals(""))){
                                            error += "\n- в номере раздела можно использовать только цифры";
                                            errorFlag = false;
                                        }

                                        m = pattern.matcher(editTextChapter.getText());
                                        if (!m.matches() && !(editTextChapter.getText().toString().equals(""))){
                                            error += "\n- в номере главы можно использовать только цифры";
                                            errorFlag = false;
                                        }

                                        m = pattern.matcher(editTextParagraph.getText());
                                        if (!m.matches() && !(editTextParagraph.getText().toString().equals(""))){
                                            error += "\n- в номере параграфа можно использовать только цифры";
                                            errorFlag = false;
                                        }

                                        m = pattern.matcher(editTextNumberOfPublication.getText());
                                        if (!m.matches() && !(editTextNumberOfPublication.getText().toString().equals(""))){
                                            error += "\n- в номере издания можно использовать только цифры";
                                            errorFlag = false;
                                        }

                                        if (errorFlag) {

                                            if (editTextTextSection.getText().length() > 3) {
                                                error += "\n- номер раздела слишком большой";
                                                errorFlag = false;
                                            }

                                            if (editTextChapter.getText().length() > 3) {
                                                error += "\n- номер главы слишком большой";
                                                errorFlag = false;
                                            }

                                            if (editTextParagraph.getText().length() > 3) {
                                                error += "\n- номер параграфа слишком большой";
                                                errorFlag = false;
                                            }

                                            if (editTextNumberOfPublication.getText().length() > 3) {
                                                error += "\n- номер издания слишком большой";
                                                errorFlag = false;
                                            }

                                            if (errorFlag) {

                                                String editTextDecisionValue = editTextDecision.getText().toString();

                                                if (editTextDecisionValue.equals("")) {
                                                    error += "\n- описание или сам текст из литературы";
                                                    errorFlag = false;
                                                }

                                                if (editTextDecisionValue.length() > 2000) {
                                                    error += "\n- текст из учебной дисциплины слишком большой";
                                                    errorFlag = false;
                                                }

                                                if (errorFlag) {

                                                    //---------------------------------------------------
                                                    boolean noAppend = false;

                                                    Cursor cursor = db.query(DatabaseHelper.TABLE_DISCIPLINES, null, null, null, null, null, null);

                                                    if (cursor.moveToFirst()) {
                                                        int idNameDiscipline = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_DISCIPLINE);

                                                        do {
                                                            if (cursor.getString(idNameDiscipline).equals(autoCompleteDisciplineValue)) {
                                                                noAppend = true;
                                                                break;
                                                            }
                                                        } while (cursor.moveToNext());
                                                    }
                                                    cursor.close();

                                                    if (!noAppend) {
                                                        ContentValues cv = new ContentValues();
                                                        cv.put(DatabaseHelper.COLUMN_NAME_DISCIPLINE, autoCompleteDisciplineValue);
                                                        db.insert(DatabaseHelper.TABLE_DISCIPLINES, null, cv);
                                                    }
                                                    //---------------------------------------------------

                                                    //---------------------------------------------------
                                                    noAppend = false;

                                                    cursor = db.query(DatabaseHelper.TABLE_AUTHORS, null, null, null, null, null, null);

                                                    if (cursor.moveToFirst()) {
                                                        int idNameAuthor = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_AUTHOR);

                                                        do {
                                                            if (cursor.getString(idNameAuthor).equals(autoCompleteAuthorValue)) {
                                                                noAppend = true;
                                                                break;
                                                            }
                                                        } while (cursor.moveToNext());
                                                    }
                                                    cursor.close();

                                                    if (!noAppend) {
                                                        ContentValues cv = new ContentValues();
                                                        cv.put(DatabaseHelper.COLUMN_NAME_AUTHOR, autoCompleteAuthorValue);
                                                        db.insert(DatabaseHelper.TABLE_AUTHORS, null, cv);
                                                    }
                                                    //---------------------------------------------------

                                                    //---------------------------------------------------
                                                    noAppend = false;

                                                    cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                    if (cursor.moveToFirst()) {
                                                        int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                        do {
                                                            if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue)) {
                                                                noAppend = true;
                                                                break;
                                                            }
                                                        } while (cursor.moveToNext());
                                                    }
                                                    cursor.close();

                                                    if (!noAppend) {
                                                        ContentValues cv = new ContentValues();
                                                        cv.put(DatabaseHelper.COLUMN_THEME, autoCompleteThemeValue);
                                                        db.insert(DatabaseHelper.TABLE_THEMES, null, cv);
                                                    }
                                                    //---------------------------------------------------

                                                    //---------------------------------------------------

                                                    if (!autoCompleteThemeValue2.equals("")) {

                                                        noAppend = false;

                                                        cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                        if (cursor.moveToFirst()) {
                                                            int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                            do {
                                                                if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue2)) {
                                                                    noAppend = true;
                                                                    break;
                                                                }
                                                            } while (cursor.moveToNext());
                                                        }
                                                        cursor.close();

                                                        if (!noAppend) {
                                                            ContentValues cv = new ContentValues();
                                                            cv.put(DatabaseHelper.COLUMN_THEME, autoCompleteThemeValue2);
                                                            db.insert(DatabaseHelper.TABLE_THEMES, null, cv);
                                                        }

                                                    }
                                                    //---------------------------------------------------

                                                    //---------------------------------------------------

                                                    if (!autoCompleteThemeValue3.equals("")) {

                                                        noAppend = false;

                                                        cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                        if (cursor.moveToFirst()) {
                                                            int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                            do {
                                                                if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue3)) {
                                                                    noAppend = true;
                                                                    break;
                                                                }
                                                            } while (cursor.moveToNext());
                                                        }
                                                        cursor.close();

                                                        if (!noAppend) {
                                                            ContentValues cv = new ContentValues();
                                                            cv.put(DatabaseHelper.COLUMN_THEME, autoCompleteThemeValue3);
                                                            db.insert(DatabaseHelper.TABLE_THEMES, null, cv);
                                                        }

                                                    }
                                                    //---------------------------------------------------

                                                    //---------------------------------------------------

                                                    if (!autoCompleteThemeValue4.equals("")) {

                                                        noAppend = false;

                                                        cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                        if (cursor.moveToFirst()) {
                                                            int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                            do {
                                                                if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue4)) {
                                                                    noAppend = true;
                                                                    break;
                                                                }
                                                            } while (cursor.moveToNext());
                                                        }
                                                        cursor.close();

                                                        if (!noAppend) {
                                                            ContentValues cv = new ContentValues();
                                                            cv.put(DatabaseHelper.COLUMN_THEME, autoCompleteThemeValue4);
                                                            db.insert(DatabaseHelper.TABLE_THEMES, null, cv);
                                                        }

                                                    }
                                                    //---------------------------------------------------

                                                    //---------------------------------------------------

                                                    if (!autoCompleteThemeValue5.equals("")) {

                                                        noAppend = false;

                                                        cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                        if (cursor.moveToFirst()) {
                                                            int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                            do {
                                                                if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue5)) {
                                                                    noAppend = true;
                                                                    break;
                                                                }
                                                            } while (cursor.moveToNext());
                                                        }
                                                        cursor.close();

                                                        if (!noAppend) {
                                                            ContentValues cv = new ContentValues();
                                                            cv.put(DatabaseHelper.COLUMN_THEME, autoCompleteThemeValue5);
                                                            db.insert(DatabaseHelper.TABLE_THEMES, null, cv);
                                                        }

                                                    }
                                                    //---------------------------------------------------

                                                    //---------------------------------------------------


                                                    cursor = db.query(DatabaseHelper.TABLE_DISCIPLINES, null, null, null, null, null, null);

                                                    int id_discipline = 0;

                                                    if (cursor.moveToFirst()) {
                                                        id_discipline = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_DISCIPLINE);
                                                        int idName_discipline = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_DISCIPLINE);

                                                        do {
                                                            if (cursor.getString(idName_discipline).equals(autoCompleteDisciplineValue)) {
                                                                id_discipline = cursor.getInt(id_discipline);
                                                                break;
                                                            }
                                                        } while (cursor.moveToNext());
                                                    }
                                                    cursor.close();


                                                    cursor = db.query(DatabaseHelper.TABLE_AUTHORS, null, null, null, null, null, null);

                                                    int id_author = 0;

                                                    if (cursor.moveToFirst()) {
                                                        id_author = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_AUTHOR);
                                                        int idName_author = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_AUTHOR);

                                                        do {
                                                            if (cursor.getString(idName_author).equals(autoCompleteAuthorValue)) {
                                                                id_author = cursor.getInt(id_author);
                                                                break;
                                                            }
                                                        } while (cursor.moveToNext());
                                                    }
                                                    cursor.close();

                                                    int[] masIdThemes = new int[5];
                                                    int lenMasIdThemes = 0;

                                                    cursor = db.query(DatabaseHelper.TABLE_THEMES, null, null, null, null, null, null);

                                                    if (cursor.moveToFirst()) {
                                                        int id_theme = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_THEME);
                                                        int idNameTheme = cursor.getColumnIndex(DatabaseHelper.COLUMN_THEME);

                                                        do {

                                                            if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue)) {
                                                                masIdThemes[lenMasIdThemes] = cursor.getInt(id_theme);
                                                                lenMasIdThemes++;
                                                            }

                                                            if (cursor.getString(idNameTheme).equals(autoCompleteThemeValue2)) {
                                                                masIdThemes[lenMasIdThemes] = cursor.getInt(id_theme);
                                                                lenMasIdThemes++;
                                                            }

                                                            if ( cursor.getString(idNameTheme).equals(autoCompleteThemeValue3)) {
                                                                masIdThemes[lenMasIdThemes] = cursor.getInt(id_theme);
                                                                lenMasIdThemes++;
                                                            }

                                                            if ( cursor.getString(idNameTheme).equals(autoCompleteThemeValue4)) {
                                                                masIdThemes[lenMasIdThemes] = cursor.getInt(id_theme);
                                                                lenMasIdThemes++;
                                                            }

                                                            if ( cursor.getString(idNameTheme).equals(autoCompleteThemeValue5)) {
                                                                masIdThemes[lenMasIdThemes] = cursor.getInt(id_theme);
                                                                lenMasIdThemes++;
                                                            }

                                                        } while (cursor.moveToNext());
                                                    }
                                                    cursor.close();


                                                    noAppend = false;

                                                    cursor = db.query(DatabaseHelper.TABLE_WORK_DESCRIPTION, null, null, null, null, null, null);

                                                    int id_work_description = 0;

                                                    if (cursor.moveToFirst()) {
                                                        id_work_description = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_WORK_DESCRIPTION);
                                                        int idType_of_work = cursor.getColumnIndex(DatabaseHelper.COLUMN_TYPE_OF_WORK);
                                                        int id_discipline2 = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_DISCIPLINE);
                                                        int id_author2 = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_AUTHOR);
                                                        int section_number = cursor.getColumnIndex(DatabaseHelper.COLUMN_SECTION_NUMBER);
                                                        int chapter_number = cursor.getColumnIndex(DatabaseHelper.COLUMN_CHAPTER_NUMBER);
                                                        int paragraph_number = cursor.getColumnIndex(DatabaseHelper.COLUMN_PARAGRAPH_NUMBER);
                                                        int publication_number = cursor.getColumnIndex(DatabaseHelper.COLUMN_PUBLICATION_NUMBER);

                                                        do {

                                                            if (cursor.getString(idType_of_work).equals(item) && cursor.getInt(id_discipline2) == id_discipline
                                                                    &&  cursor.getInt(id_author2) == id_author
                                                                    &&  cursor.getInt(section_number) == Integer.parseInt(editTextTextSection.getText().toString())
                                                                    &&  cursor.getInt(chapter_number) == Integer.parseInt(editTextChapter.getText().toString())
                                                                    &&  cursor.getInt(paragraph_number) == Integer.parseInt(editTextParagraph.getText().toString())
                                                                    &&  cursor.getInt(publication_number) == Integer.parseInt(editTextNumberOfPublication.getText().toString())) {

                                                                int[] masIdThemes2 = new int[5];
                                                                int lenMasIdThemes2 = 0;

                                                                userCursor = db.query(DatabaseHelper.TABLE_THEME_OF_WORK, null, null, null, null, null, null);

                                                                if (userCursor.moveToFirst()) {
                                                                    int id_theme = userCursor.getColumnIndex(DatabaseHelper.COLUMN_ID_THEME);
                                                                    int id_workDescription = userCursor.getColumnIndex(DatabaseHelper.COLUMN_ID_WORK_DESCRIPTION);

                                                                    do {
                                                                        if (userCursor.getInt(id_workDescription) == cursor.getInt(id_work_description)) {
                                                                            masIdThemes2[lenMasIdThemes2] = userCursor.getInt(id_theme);
                                                                            lenMasIdThemes2++;
                                                                        }
                                                                    } while (userCursor.moveToNext());
                                                                }
                                                                userCursor.close();

                                                                boolean coincide = false;
                                                                int partCoincide = 0;

                                                                if (lenMasIdThemes2 == lenMasIdThemes) {
                                                                    for (int i =0; i < lenMasIdThemes2; i++) {
                                                                        for (int j = 0; j < lenMasIdThemes; j++) {
                                                                            if (masIdThemes2[i] == masIdThemes[j]) {
                                                                                partCoincide++;
                                                                                break;
                                                                            }
                                                                        }
                                                                    }
                                                                }


                                                                if (partCoincide == lenMasIdThemes2) {
                                                                    coincide = true;
                                                                }

                                                                if (coincide) {
                                                                    id_work_description = cursor.getInt(id_work_description);
                                                                    noAppend = true;
                                                                    break;
                                                                }
                                                            }
                                                        } while (cursor.moveToNext());
                                                    }
                                                    cursor.close();

                                                    int id_WDescription = 0;

                                                    if (!noAppend) {

                                                        ContentValues cv = new ContentValues();
                                                        cv.put(DatabaseHelper.COLUMN_TYPE_OF_WORK, item);
                                                        cv.put(DatabaseHelper.COLUMN_ID_DISCIPLINE, id_discipline);
                                                        cv.put(DatabaseHelper.COLUMN_ID_AUTHOR, id_author);
                                                        cv.put(DatabaseHelper.COLUMN_TASK_NUMBER, -1);
                                                        cv.put(DatabaseHelper.COLUMN_OPTION_NUMBER, -1);
                                                        cv.put(DatabaseHelper.COLUMN_SECTION_NUMBER, Integer.parseInt(editTextTextSection.getText().toString()));
                                                        cv.put(DatabaseHelper.COLUMN_CHAPTER_NUMBER, Integer.parseInt(editTextChapter.getText().toString()));
                                                        cv.put(DatabaseHelper.COLUMN_PARAGRAPH_NUMBER, Integer.parseInt(editTextParagraph.getText().toString()));
                                                        cv.put(DatabaseHelper.COLUMN_PUBLICATION_NUMBER, Integer.parseInt(editTextNumberOfPublication.getText().toString()));
                                                        db.insert(DatabaseHelper.TABLE_WORK_DESCRIPTION, null, cv);

                                                        userCursor = db.query(DatabaseHelper.TABLE_WORK_DESCRIPTION, null, null, null, null, null, null);

                                                        if (userCursor.moveToLast()) {
                                                            int idNew_work_description = userCursor.getColumnIndex(DatabaseHelper.COLUMN_ID_WORK_DESCRIPTION);
                                                            idNew_work_description = userCursor.getInt(idNew_work_description);

                                                            for (int i = 0; i < lenMasIdThemes; i++) {
                                                                cv = new ContentValues();
                                                                cv.put(DatabaseHelper.COLUMN_ID_WORK_DESCRIPTION, idNew_work_description);
                                                                cv.put(DatabaseHelper.COLUMN_ID_THEME, masIdThemes[i]);
                                                                db.insert(DatabaseHelper.TABLE_THEME_OF_WORK, null, cv);
                                                            }

                                                            id_WDescription = idNew_work_description;

                                                        }

                                                        userCursor.close();

                                                    } else {

                                                        id_WDescription = id_work_description;

                                                    }

                                                    Bundle arguments = getIntent().getExtras();
                                                    int id_user = arguments.getInt("id_user");

                                                    Date currentDate = new Date();
                                                    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                                                    String dateText = dateFormat.format(currentDate);

                                                    ContentValues cv = new ContentValues();
                                                    cv.put(DatabaseHelper.COLUMN_ID_WORK_DESCRIPTION, id_WDescription);
                                                    cv.put(DatabaseHelper.COLUMN_ID_USER, id_user);
                                                    cv.put(DatabaseHelper.COLUMN_TEXT_WORK, editTextDecisionValue);
                                                    cv.put(DatabaseHelper.COLUMN_TIME_OF_CREATION, dateText);

                                                    if(imageForDownload.getVisibility() == View.VISIBLE) {

                                                        BitmapDrawable drawable = (BitmapDrawable) imageForDownload.getDrawable();
                                                        Bitmap bitmap = drawable.getBitmap();

                                                        // Записываем изображение в поток байтов.
                                                        // При этом изображение можно сжать и / или перекодировать в другой формат.
                                                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

                                                        // Получаем изображение из потока в виде байтов
                                                        byte[] bytes = byteArrayOutputStream.toByteArray();

                                                        // Кодируем байты в строку Base64 и возвращаем
                                                        String fileForDownload =  Base64.encodeToString(bytes, Base64.DEFAULT);

                                                        cv.put(DatabaseHelper.COLUMN_FILE_FROM_WORK, fileForDownload);
                                                    } else {
                                                        cv.put(DatabaseHelper.COLUMN_FILE_FROM_WORK, "nine");
                                                    }

                                                    db.insert(DatabaseHelper.TABLE_USER_WORK, null, cv);

                                                    userCursor = db.query(DatabaseHelper.TABLE_USER_WORK, null, null, null, null, null, null);

                                                    int id_user_work = 0;

                                                    if (userCursor.moveToLast()) {
                                                        id_user_work = userCursor.getColumnIndex(DatabaseHelper.COLUMN_ID_USER_WORK);
                                                        id_user_work = userCursor.getInt(id_user_work);
                                                    }

                                                    userCursor.close();


                                                    Intent intent = new Intent(getApplicationContext(), InformationAboutTheSolution.class);
                                                    intent.putExtra("id_user", id_user);
                                                    intent.putExtra("id_user_work", id_user_work);
                                                    startActivity(intent);


                                                }

                                            }

                                        }

                                    }

                                }

                            }

                            if (!errorFlag) {
                                error = "Что-то пошло не по плану:" + error;
                                errorMessage.setText(error);
                                errorMessage.setPaddingRelative(20, 20,20,20);
                                errorMessage.setVisibility(View.VISIBLE);
                            }

                        }
                    });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        typeOfWork.setOnItemSelectedListener(itemSelectedListener);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    imageForDownload.setImageURI(selectedImage);
                    imageForDownload.setMaxHeight(200);
                    imageForDownload.setMaxWidth(200);
                    imageForDownload.setVisibility(View.VISIBLE);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    imageForDownload.setImageURI(selectedImage);
                    imageForDownload.setMaxHeight(200);
                    imageForDownload.setMaxWidth(200);
                    imageForDownload.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Закрываем подключение
        db.close();
    }

}