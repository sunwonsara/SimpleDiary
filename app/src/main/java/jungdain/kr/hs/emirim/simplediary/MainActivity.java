package jungdain.kr.hs.emirim.simplediary;

import android.content.Context;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    DatePicker date;
    EditText edit;
    Button but;
    String fileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        date = (DatePicker)findViewById(R.id.date_pick);
        edit = (EditText)findViewById(R.id.edit);
        but=(Button)findViewById(R.id.but);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileOutputStream fOut=openFileOutput(fileName, Context.MODE_PRIVATE);
                    String str=edit.getText().toString();
                    fOut.write(str.getBytes());
                    fOut.close();
                    Toast.makeText(MainActivity.this, "정상적으로 "+fileName+"파일이 저장되었습니다", Toast.LENGTH_LONG).show(); //Toast로 저장이 잘 되었다는 걸 표시
                }  catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Calendar cal=Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DATE);

        date.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                fileName=year+"_"+(month+1)+"_"+day+".txt";
                String readDate=readDiary(fileName);
                edit.setText(readDate);
                but.setEnabled(true); //수정이나 새로작성가능하게 활성화시켜줌
            }
        });//날짜가 변경되었을때 일처리를 하는 핸들러를 null에다 넣으면 됨
    }
    public String readDiary(String fileName){
        String diaryStr=null;
        FileInputStream fIn =null;
        try {
            fIn = openFileInput(fileName);
            byte[] buf=new byte[500];
            fIn.read(buf); //500byte을 읽어와라
            diaryStr=new String(buf).trim();//byte값을 string으로 바꾸는 방법 , trim():앞,뒤에 있는 공백을 제거하라, 중간공백은 제거 못함
            but.setText("수정 하기");
            fIn.close();
        }catch(FileNotFoundException e){
            edit.setHint("일기가 존재하지 않습니다."); //파일이 없을때
            but.setText("새로 저장");
        } catch (IOException e) {

        }


        return diaryStr;
    }
}
