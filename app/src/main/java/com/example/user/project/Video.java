package com.example.user.project;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Video extends AppCompatActivity {
    ListView video_list;
    String[] titles = {
            "얼굴 운동",
            "팔 운동",
            "복부 운동",
            "허벅지 운동",
            "종아리 운동",
            "상체 운동",
            "하체 운동",
            "스트레칭" };

    Integer[] images = {
            R.drawable.video_0,
            R.drawable.video_1,
            R.drawable.video_2,
            R.drawable.video_4,
            R.drawable.video_5,
            R.drawable.video_6,
            R.drawable.video_7,
            R.drawable.video_3 };

    String[] froms = {
            "WqS7fSi9rR4",
            "UYHfk45Yi2c",
            "iOSYLKBk894",
            "NZpvaLfBeJE",
            "2xmNzityfKI",
            "C3TqpGgnIoI",
            "XRWP_hPH2po",
            "2LyDkE7sDec" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        ActionBar ab = getSupportActionBar();
        ab.hide();

        CustomList adapter = new CustomList(Video.this);

        video_list = (ListView) findViewById(R.id.video_list);

        video_list.setAdapter(adapter);

        video_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView video_list=(ListView)parent;
                String item=(String)video_list.getItemAtPosition(position);
                Toast.makeText(Video.this,item,Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),Video_View.class);
                intent.putExtra("youtube",froms[position]);
                startActivity(intent);
            }
        });
    }

    public class CustomList extends ArrayAdapter<String> {
        private final Activity context;
        public CustomList(Activity context) {
            super(context,R.layout.video_list_item,titles);
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.video_list_item, null, true);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.image);
            TextView title = (TextView) rowView.findViewById(R.id.title);
            TextView from = (TextView) rowView.findViewById(R.id.from);

            title.setText(titles[position]);
            imageView.setImageResource(images[position]);
            from.setText("출처 : youtube/땅끄부부");

            return rowView;
        }
    }
}
