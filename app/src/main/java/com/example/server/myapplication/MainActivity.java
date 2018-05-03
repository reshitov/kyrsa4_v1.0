package com.example.server.myapplication;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //public static final String URL = "https://ru.investing.com/currencies/streaming-forex-rates-majors";

    Button act_change, act_change2, convert, refresh;
    EditText First_editText, Second_editText;
    String sanuDollar;//переменная alert
    String sanuRybl; //переменная alert2
    double /*value*/ num1; /*evro_rybl*/ //value скорее всего это будет то значение которое мы будем парсить.
    interface RateChangeListener{
        void onChange(double rate);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // value=63.33;//ПОЛУЧИЛИ К ПРИМЕРУ ЗНАЧЕНИЕ С САЙТА РАВНЫМ 63(ДОЛЛАРА, ОТНОШЕНИЕ К РУБЛЮ)

        First_editText = findViewById(R.id.editText);
        Second_editText = findViewById(R.id.editText2);

        act_change = findViewById(R.id.act_change);//кнопка
        act_change.setOnClickListener(this);

        refresh = findViewById(R.id.refresh); // для парсинга
        refresh.setOnClickListener(this);

        act_change2 = findViewById(R.id.act_change2);//кнопка
        act_change2.setOnClickListener(this);

        convert = findViewById(R.id.convert);
        convert.setOnClickListener(this);
       // (new Newthread()).start();  //работа с потоком
    }
    public class Newthread extends Thread {  //работа с потоком
        private RateChangeListener listener;
       // public void setChangeListener(RateChangeListener listener){
          //  this.listener=listener;
       // }
        @Override
        public void run() {
            try {
                Document document = Jsoup.connect("https://ru.investing.com/currencies/streaming-forex-rates-majors").get();
               final Element content = document.getElementById("pair_2186");
                MainActivity.this.runOnUiThread(new Runnable(){

                    public  void run(){

                    Newthread.this.listener.onChange(Double.parseDouble(content.getElementsByClass("pid-2186-bid").text().replace(",", "."))); } });
                   // доллар значение котрое находится в теге pid-2186-bid закладываем в double
              //  Log.wtf("Value: ", Double.toString(value));
               // evro_rybl = Double.parseDouble(content.getElementsByClass("lastNum js-item-last pid-1691-last").text().replace(",", "."));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void setListener(RateChangeListener listener) {
            this.listener = listener;
        }
    }
            @Override
    public void onClick(View v) {
        num1 = Double.parseDouble(First_editText.getText().toString());//переводим вводное значение в первом едит тексте в дабл значение.
       switch (v.getId()) {
           case R.id.act_change:
               left(v);
               break;
           case R.id.act_change2:
               right(v);
               break;
           case R.id.convert:
               convert(v);
               break;
           case R.id.refresh:
               (new Newthread()).start();
               break;

       }

    }
    public void left(View v){
        final CharSequence[] items = {
                getText(R.string.dolar), getText(R.string.rybl), getText(R.string.evro)
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.message);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int items) {
                switch (items) {
                    case 0:
                        sanuDollar = "D";
                        Toast.makeText(MainActivity.this,  R.string.dolar, Toast.LENGTH_LONG).show();
                        break;
                    case 1: {
                        Toast.makeText(MainActivity.this, R.string.rybl, Toast.LENGTH_LONG).show();
                        sanuDollar = "rYbll";
                        break;
                    }
                    case 2: {
                        Toast.makeText(MainActivity.this, R.string.evro, Toast.LENGTH_LONG).show();
                        sanuDollar = "evroo";
                        break;
                    }
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void right(View v) {
        final CharSequence[] itemss = {
                getText(R.string.dolar), getText(R.string.rybl), getText(R.string.evro)
        };
        AlertDialog.Builder server = new AlertDialog.Builder(MainActivity.this);
        server.setTitle(R.string.message);
        //num1 = Double.parseDouble(First_editText.getText().toString());//переводим вводное значение в первом едит тексте в дабл значение.
        server.setItems(itemss, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0: {
                        sanuRybl = "doLar";
                        Toast.makeText(MainActivity.this, R.string.dolar, Toast.LENGTH_LONG).show();
                        break;
                    }
                    case 1: {
                        Toast.makeText(MainActivity.this, R.string.rybl, Toast.LENGTH_LONG).show();
                        sanuRybl = "R";       //СОЗДАЙ ДРУГОЙ СТРИНГ С ДРУГИМИ ПЕРЕМЕННЫМИ
                        break;
                    }
                    case 2: {
                        Toast.makeText(MainActivity.this, R.string.evro, Toast.LENGTH_LONG).show();
                        sanuRybl = "evrOo";     //СОЗДАЙ ДРУГОЙ СТРИНГ С ДРУГИМИ ПЕРЕМЕННЫМИ
                        break;
                    }
                }
            }
        });
        AlertDialog alert = server.create();
        alert.show();
    }
    public void convert(View v) {
        Newthread thread = new Newthread();
        thread.setListener(new RateChangeListener(){ public void onChange(double rate)
        {
            if (sanuDollar.equals("D") && sanuRybl.equals("R")) {
                First_editText = findViewById(R.id.editText);
                num1 = Double.parseDouble(First_editText.getText().toString());
               // Log.wtf("Value: ", Double.toString(value));
                //Log.wtf("num1: ", Double.toString(num1)); //
                double result = num1 * rate;
                Second_editText = findViewById(R.id.editText2);
                Second_editText.setText(Double.toString(result));
            }

        }
        });
        thread.start();

        /*if(sanuDollar.equals("evroo") && sanuRybl.equals("rybBll"))
        {
             result = num1*evro_rybl;
            Second_editText = (EditText) findViewById(R.id.editText2);
            Second_editText.setText(Double.toString(result));
        }*/
        }
    }



