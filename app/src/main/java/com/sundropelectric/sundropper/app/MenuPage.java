package com.sundropelectric.sundropper.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by seth.darr on 5/6/2014.
 */
public class MenuPage extends Fragment {
    private static final String KEY_POSITION = "position";
    private SharedPreferences prefs = null;

    static MenuPage newInstance(int position) {
        MenuPage frag = new MenuPage();
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_menu_page, container, false);
        ImageButton button = (ImageButton)result.findViewById(R.id.menu_button);
        int position = getArguments().getInt(KEY_POSITION, -1);

        switch (position) {
//            case 0: button.setBackground(getResources().getDrawable(R.drawable.Music));
            case 0: button.setBackgroundResource(R.drawable.music);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(v.getContext(), Music.class));
                        }
                    });
                break;
            case 1: button.setBackgroundResource(R.drawable.shows);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), "Shows!", Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case 2: button.setBackgroundResource(R.drawable.artwork);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), "Artwork!", Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case 3: button.setBackgroundResource(R.drawable.documents);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), "Documents!", Toast.LENGTH_LONG).show();
                    }
                });

        }

        return result;
    }
}
