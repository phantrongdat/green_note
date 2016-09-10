package vn.dat.greennote.NoteMenu;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

import vn.dat.greennote.DatabaseHandler.NoteDatabase;
import vn.dat.greennote.R;

/**
 * Created by Alone on 04/16/2016.
 */
public class Statistics extends Fragment {
    protected NoteDatabase db;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View statistics = inflater.inflate(R.layout.fragment_statistics, null);

        PieChart pieChart = (PieChart) statistics.findViewById(R.id.chart);
        Context context = getActivity().getApplicationContext();
        db = new NoteDatabase(context);
        TextView txtData=(TextView)statistics.findViewById(R.id.txtData);
        final float all=db.rawQuery("SELECT * FROM tblNote").getCount();
        db.closeDatabase();
        txtData.setText("All notes 2016: "+(int)all+ "  (100%)");

        final ArrayList<String> months = new ArrayList<String>();

        months.add("Jan");
        months.add("Feb");
        months.add("Mar");
        months.add("Apr");
        months.add("May");
        months.add("Jun");
        months.add("Jul");
        months.add("Aug");
        months.add("Sep");
        months.add("Oct");
        months.add("Nov");
        months.add("Dec");

        final float [] monthsData=new float[12];
        ArrayList<Entry> entries = new ArrayList<>();

        for (int i=0;i<months.size();i++)
        {
            float count=db.rawQuery("SELECT * FROM tblNote WHERE date LIKE '"+months.get(i)+"%'").getCount();
            monthsData[i]=((count/all)*100);
            entries.add(new Entry(monthsData[i], i));
            db.closeDatabase();
        }

        PieDataSet dataset = new PieDataSet(entries, "# of Calls");

        PieData data = new PieData(months, dataset);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);

        int[] color = { Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0), Color.rgb(106, 150, 31),
                        Color.rgb(179, 100, 53), Color.rgb(64, 89, 128), Color.rgb(149, 165, 124), Color.rgb(217, 184, 162),
                        Color.rgb(191, 134, 134), Color.rgb(179, 48, 80), Color.rgb(192, 255, 140), Color.rgb(255, 247, 140)};

        dataset.setColors(color); //
        pieChart.setDescription("Notes");
        pieChart.setData(data);
        pieChart.animateY(5000);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                Toast.makeText(getContext(), months.get(e.getXIndex())+ " you have " + (int)(monthsData[e.getXIndex()]*all/100)+" notes", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        pieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
//        pieChart.saveToGallery("/sd/mychart.jpg", 85); // 85 is the quality of the image
        return statistics;
    }
}
