package com.sundropelectric.sundropper.app;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by seth.darr on 5/6/2014.
 */
public class MenuPage extends Fragment {
    private static final String KEY_POSITION = "position";

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
            case 0: button.setBackgroundResource(R.drawable.music); break;
            case 1: button.setBackgroundResource(R.drawable.shows); break;
            case 2: button.setBackgroundResource(R.drawable.artwork); break;
            case 3: button.setBackgroundResource(R.drawable.documents);
        }

        return result;
    }
}
