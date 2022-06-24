package com.example.cecenet.Pengaturan.MenuHome.BackgroundAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.cecenet.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AktifBackgroundFragment extends Fragment {
    private SwipeRefreshLayout SRLBackground;
    private ListView LVBackground;

    private Background background;
    private MyAdapterBackground myAdapterBackground;
    private SharedPreferences SPModeBackground;
    private static ArrayList<Background> BackgroundArrayList    = new ArrayList<>();
    private ArrayList<String> BackgroundAktifAction             = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.fragment_tab_background, container, false);

        SRLBackground   = view.findViewById(R.id.SRLBackground);
        LVBackground    = view.findViewById(R.id.LVBackground);

        if(getContext() != null){
            SPModeBackground    = getContext().getSharedPreferences("ModeBackground", Context.MODE_PRIVATE);

            myAdapterBackground = new MyAdapterBackground(getContext(), BackgroundArrayList);
            LVBackground.setAdapter(myAdapterBackground);
        }

        LVBackground.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        LVBackground.setMultiChoiceModeListener(modeListener);
        LVBackground.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);

        SRLBackground.setRefreshing(true);
        cekModeBackground();
        tampilBackground();

        SRLBackground.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tampilBackground();
            }
        });

        return view;
    }

    private void cekModeBackground(){
        final Handler handler   = new Handler();
        Runnable runnable       = new Runnable() {
            @Override
            public void run() {
                if(SPModeBackground.getBoolean("Refresh", true)){
                    SRLBackground.setRefreshing(true);
                    tampilBackground();
                }

                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);
    }

    private void tampilBackground(){
        AndroidNetworking.post(getResources().getString(R.string.URL) + "Beranda.php")
                .addBodyParameter("Aksi", "DapatkanBackgroundAktif")
                .addBodyParameter("Jenis", SPModeBackground.getString("Jenis", "Gambar"))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        BackgroundArrayList.clear();
                        try{
                            JSONObject jsonObject   = new JSONObject(response);
                            String Sukses           = jsonObject.getString("Sukses");
                            JSONArray jsonArray     = jsonObject.getJSONArray("Data");

                            if(Sukses.equals("1")){
                                for(int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object       = jsonArray.getJSONObject(i);

                                    String ID_Background    = object.getString("ID_Background");
                                    String Nama_Background  = object.getString("Nama_Background");
                                    String Tampil           = object.getString("Tampil");

                                    background              = new Background(ID_Background, Nama_Background, Tampil);
                                    BackgroundArrayList.add(background);
                                    LVBackground.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                                    myAdapterBackground.notifyDataSetChanged();
                                }
                                SPModeBackground.edit().putBoolean("Refresh", false).apply();
                                SRLBackground.setRefreshing(false);
                            }
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        SRLBackground.setRefreshing(false);
                        Toast.makeText(getContext(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private AbsListView.MultiChoiceModeListener modeListener = new AbsListView.MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            switch(LVBackground.getCheckedItemCount()){
                case 0  :
                    mode.setTitle(null);
                    break;
                case 1  :
                    mode.setTitle(R.string.SATU_BACKGROUND);
                    break;
                default :
                    mode.setTitle(LVBackground.getCheckedItemCount() + " " + getString(R.string.BACKGROUND));
                    break;
            }

            if(checked){
                BackgroundAktifAction.add(BackgroundArrayList.get(position).getID_Background());
            }
            else{
                BackgroundAktifAction.remove(BackgroundArrayList.get(position).getID_Background());
            }
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_background, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            BackgroundAktifAction.clear();
            menu.findItem(R.id.action_aktif).setVisible(false);
            LVBackground.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);

            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch(item.getItemId()){
                case R.id.action_arsip :
                    BackgroundArsip(mode);
                    break;
                case R.id.action_hapus :
                    KonfirmasiHapus(mode);
                    break;
            }

            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) { }
    };

    private void BackgroundArsip(final ActionMode mode){
        AndroidNetworking.post(getString(R.string.URL) + "Beranda.php")
                .addBodyParameter("Aksi", "BackgroundArsip")
                .addBodyParameter("Jenis", "Gambar")
                .addBodyParameter("BackgroundAktifAction", BackgroundAktifAction.toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            if(response.equalsIgnoreCase("Background di Arsipkan")){
                                mode.finish();

                                SPModeBackground.edit().putBoolean("Refresh", true).apply();
                                Toast.makeText(getContext(), BackgroundAktifAction.size() + " " + getString(R.string.Background_di_Arsipkan), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                mode.finish();
                                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        mode.finish();
                        Toast.makeText(getContext(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void KonfirmasiHapus(final ActionMode mode){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialog);
        builder.setTitle(getString(R.string.Konfirmasi))
                .setMessage(getString(R.string.Apakah_Yakin_Ingin_Menghapus) + " " + BackgroundAktifAction.size() + " " + getString(R.string.Background_ini_))
                .setIcon(R.drawable.hapus_black)
                .setCancelable(false)
                .setNegativeButton(getString(R.string.TIDAK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mode.finish();
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.YA), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        HapusAction(mode);
                    }
                })
                .create().show();
    }

    private void HapusAction(final ActionMode mode){
        AndroidNetworking.post(getResources().getString(R.string.URL) + "Beranda.php")
                .addBodyParameter("Aksi", "HapusBackground")
                .addBodyParameter("Jenis", "Gambar")
                .addBodyParameter("BackgroundAktifAction", BackgroundAktifAction.toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            if(response.equalsIgnoreCase("Background Terhapus")){
                                mode.finish();
                                tampilBackground();
                                Toast.makeText(getContext(), BackgroundAktifAction.size() + " " + getString(R.string.Background_Terhapus), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                mode.finish();
                                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        mode.finish();
                        Toast.makeText(getContext(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}