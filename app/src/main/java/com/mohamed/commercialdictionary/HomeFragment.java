package com.mohamed.commercialdictionary;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.mohamed.commercialdictionary.databases.ExternalDatabaseHelper;
import com.mohamed.commercialdictionary.databases.HistoryDatabaseHelper;


public class HomeFragment extends Fragment {


    AutoCompleteTextView fromEditText;
    EditText toEditText, descEditText;
    Button translateBtn;
    ListView historyListView;
    TextView historyTextView;
    HistoryDatabaseHelper historyDatabaseHelper;
    ExternalDatabaseHelper databaseHelper;
    public HomeFragment() {
        // Required empty public
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Define to edit text and description edit Text
        final LinearLayout liner = (LinearLayout) getActivity().findViewById(R.id.historyLayoutList);
        fromEditText = (AutoCompleteTextView) getActivity().findViewById(R.id.fromEditText);
        toEditText = (EditText) getActivity().findViewById(R.id.toEditText);
        descEditText = (EditText) getActivity().findViewById(R.id.DescEditText);
        translateBtn = (Button) getActivity().findViewById(R.id.translateButton);
        historyListView = (ListView) getActivity().findViewById(R.id.history_listView);
        historyTextView = (TextView) getActivity().findViewById(R.id.historyTextView);
        databaseHelper=new ExternalDatabaseHelper(getActivity());
        historyDatabaseHelper=new HistoryDatabaseHelper(getActivity());

        historyListView.setTextFilterEnabled(true);
        historyListView.setItemsCanFocus(true);
        historyListView.setClickable(true);
        historyListView.setLongClickable(true);




        historyListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
                Log.e("onInter", "Toast OnItemLongClickListener called  ... done ... #### ");
                final Dialog dialog = new Dialog(getActivity());
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.delete_dialog);
                Button deleteButton = (Button) dialog.findViewById(R.id.deleteButton);
                Button cancelButton = (Button) dialog.findViewById(R.id.cancel);
                dialog.show();
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] FromArgs = {((TextView) view.findViewById(R.id.FROM_textView)).getText().toString()};
                        historyDatabaseHelper.deleteRaw(FromArgs);
                        refreshHistoryList();
                        dialog.cancel();
                    }
                });

                return true;
            }
        });

        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("onInter", "Toast OnItemClickListener called  ... done ... #### ");
                String FromText = ((TextView) view.findViewById(R.id.FROM_textView)).getText().toString();
                String ToText = ((TextView) view.findViewById(R.id.TO_textView)).getText().toString();
                String descText = ((TextView) view.findViewById(R.id.description_textView)).getText().toString();
                Intent intent = new Intent(getActivity(), ViewWord.class);
                intent.putExtra("FromText", FromText);
                intent.putExtra("ToText", ToText);
                intent.putExtra("descText", descText);
                startActivity(intent);
            }
        });

        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.getReadableDatabase();
                String from = fromEditText.getText().toString();
                Boolean isArabic = isProbablyArabic(from);
                String[] args = {fromEditText.getText().toString().toLowerCase()};
                if (isArabic == true){
                    Cursor c = databaseHelper.getEnglishData(args);
                    if (c.moveToFirst()) {
                        toEditText.setText(c.getString(0));
                        descEditText.setText(c.getString(2));
                    } else {
                        toEditText.setText("not found!");
                        descEditText.setText("not found!");
                    }
                }else{
                    Cursor c = databaseHelper.getArabicData(args);
                    if (c.moveToFirst()) {
                        toEditText.setText(c.getString(1));
                        descEditText.setText(c.getString(2));
                    } else {
                        toEditText.setText("not found!");
                        descEditText.setText("not found!");
                    }

                }
                String fText = fromEditText.getText().toString();
                String tText = toEditText.getText().toString();
                String dText = descEditText.getText().toString();
                String[] FromArgs = {fText};
                historyDatabaseHelper.deleteRaw(FromArgs);
                historyDatabaseHelper.addHistoryRaw(fText, tText, dText);
                refreshHistoryList();
                toEditText.setVisibility(View.VISIBLE);
                descEditText.setVisibility(View.VISIBLE);
                hideKeyboard();
                liner.setVisibility(View.INVISIBLE);
            }
        });

        refreshHistoryList();
        fromEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toEditText.setVisibility(View.GONE);
                descEditText.setVisibility(View.GONE);
                liner.setVisibility(View.VISIBLE);
            }
        });

        databaseHelper.openDB();
        autoComplete();

    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void refreshHistoryList(){
        String[] from = {historyDatabaseHelper.KEY_FROM , historyDatabaseHelper.KEY_TO , historyDatabaseHelper.KEY_DESC};
        int[] to = {R.id.FROM_textView, R.id.TO_textView, R.id.description_textView};
        Cursor cursor = historyDatabaseHelper.getData();
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.history_raw,cursor,from,to);
        historyListView.setAdapter(cursorAdapter);
    }


    public static boolean isProbablyArabic(String s) {
        for (int i = 0; i < Character.codePointCount(s, 0, s.length()); i++) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <=0x06E0)
                return true;
        }
        return false;
    }

    private void autoComplete(){
        String[] enColumn = databaseHelper.getAllEnglishColumn();
        for(int i = 0; i < enColumn.length; i++)
        {
            Log.i(this.toString(), enColumn[i]);
        }
        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<String>(getActivity(),R.layout.auto_complete,enColumn);
        fromEditText.setAdapter(autoCompleteAdapter);
    }





}


