package com.example.cecenet.Pengaturan.MenuHome.KotaDaerahAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cecenet.R;

import java.util.ArrayList;
import java.util.List;

public class MyAdapterDaerah extends ArrayAdapter<Daerah> implements Filterable {
    private int resourceID;
    private List<Daerah> daerahList, tempItems, suggestions;

    public MyAdapterDaerah(@NonNull Context context, int resourceID, List<Daerah> daerahList) {
        super(context, resourceID, daerahList);

        this.resourceID = resourceID;
        this.daerahList = daerahList;

        tempItems       = new ArrayList<>(daerahList);
        suggestions     = new ArrayList<>();
    }

    @NonNull
    @Override
    @SuppressLint("ViewHolder")
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view                           = LayoutInflater.from(parent.getContext()).inflate(resourceID, null, true);

        TextView txtlistNamaKota_Daerah     = view.findViewById(R.id.txtlistNamaKota_Daerah);
        TextView txtlistDetailKota_Daerah   = view.findViewById(R.id.txtlistDetailKota_Daerah);

        txtlistNamaKota_Daerah.setText(daerahList.get(position).getNama_KotaDaerah());
        txtlistDetailKota_Daerah.setText(daerahList.get(position).getDetail_KotaDaerah());

        return view;
    }

    @Nullable
    @Override
    public Daerah getItem(int position) {
        return daerahList.get(position);
    }

    @Override
    public int getCount() {
        return daerahList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return daerahFilter;
    }

    private Filter daerahFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            Daerah daerah = (Daerah)resultValue;
            return daerah.getNama_KotaDaerah();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null){
                suggestions.clear();

                for(Daerah daerah : tempItems){
                    if(daerah.getNama_KotaDaerah().toLowerCase().startsWith(constraint.toString().toLowerCase())){
                        suggestions.add(daerah);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values        = suggestions;
                filterResults.count         = suggestions.size();

                return filterResults;
            }
            else{
                return new FilterResults();
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Daerah> tempValues = (ArrayList<Daerah>)results.values;

            if(results.count > 0){
                clear();

                for(Daerah daerahObjek : tempValues){
                    add(daerahObjek);
                }
                notifyDataSetChanged();
            }
            else{
                clear();
                notifyDataSetChanged();
            }
        }
    };
}