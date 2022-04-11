package edu.sdsmt.finn_ryan.tutorial5;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class LoadingDlg extends DialogFragment {
    private String catId;
    private final static String ID = "id";

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(ID, catId);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle bundle) {
        if(bundle != null)
            catId = bundle.getString(ID);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.loading);

        builder.setNegativeButton(android.R.string.cancel, (dialog, id) -> dialog.dismiss());

        // Create the dialog box
        final AlertDialog dlg = builder.create();

        final HatterView view = requireActivity().findViewById(R.id.hatterView);
        Cloud cloud = new Cloud();
        cloud.loadFromCloud(view, catId, dlg);

        return dlg;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }
}
