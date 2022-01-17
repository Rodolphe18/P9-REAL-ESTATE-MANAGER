package com.francotte.realestatemanager.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.francotte.realestatemanager.R;
import com.francotte.realestatemanager.activities.AddActivity;

public class GalleryPictureDialog extends AppCompatDialogFragment implements View.OnClickListener {

    private Button takePicture;
    private Button selectPicture;
    private EditText description;
    private DialogListener dialogListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_gallery_picture_dialog, null);

        builder.setView(view);
        builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String pictureDescription = description.getText().toString();
                dialogListener.applyDescription(pictureDescription);
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        takePicture = view.findViewById(R.id.bt_gallery_picture_dialog_take_picture);
        selectPicture = view.findViewById(R.id.bt_gallery_picture_dialog_select_picture);
        description = view.findViewById(R.id.et_gallery_picture_dialog_description);

        takePicture.setOnClickListener(this);
        selectPicture.setOnClickListener(this);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            dialogListener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DialogListener");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_gallery_picture_dialog_take_picture:
                ((AddActivity) getActivity()).takePicture();
                break;

            case R.id.bt_gallery_picture_dialog_select_picture:
                ((AddActivity) getActivity()).addPictureFromDevice();
                break;
        }
    }

    public interface DialogListener {
        void applyDescription(String pictureDescription);
    }
}
