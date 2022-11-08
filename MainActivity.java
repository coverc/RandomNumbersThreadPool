

package com.example.homework05;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;

import com.example.homework05.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {
    ExecutorService threadpool;
    static int numberTimes;
    ArrayList<Double> randomNumbers = new ArrayList<>();
    double random;
    double number;
    double average;
    double sum=0.00000;
    Handler handler;
    ArrayAdapter<Double> adapter;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        threadpool = Executors.newFixedThreadPool(2);

        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                binding.complexity.setText(String.valueOf(progress) + " Times");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberTimes = binding.seekBar.getProgress();
                randomNumbers.clear();
                binding.progressBar.setVisibility(view.VISIBLE);
                threadpool.execute(new DoWork());
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                number = Double.parseDouble(String.valueOf(message.obj));
                randomNumbers.add(number);
                Log.d("demo", "handleMessage: " + randomNumbers);
                sum = 0.00000;
                for(int j=0; j<randomNumbers.size(); j++){
                    sum = sum + randomNumbers.get(j);
                }
                average = sum/randomNumbers.size();
                Log.d("demo", "onCreate: " + average);
                if(randomNumbers.size()>0){
                    binding.average.setText("Average: " + average);
                    adapter = new ArrayAdapter<Double>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, randomNumbers);
                    binding.listview.setAdapter(adapter);
                    binding.progressBar.setMax(numberTimes);
                    binding.progressBar.setProgress(randomNumbers.size());
                    binding.progressLabel.setText(randomNumbers.size() + "/" + numberTimes);
                }

                return false;
            }
        });

    }

    class DoWork implements Runnable{
        @Override
        public void run() {
            for(int i=0; i<numberTimes; i++){
                random = HeavyWork.getNumber();
                Message message = new Message();
                message.obj = (Double)random;
                handler.sendMessage(message);

            }
        }
    }
}