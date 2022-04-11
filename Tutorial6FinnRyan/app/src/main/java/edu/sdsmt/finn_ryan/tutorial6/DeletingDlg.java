package edu.sdsmt.finn_ryan.tutorial6;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DeletingDlg extends DialogFragment {
    private String catId;
    private String catName;
    private final static String ID = "id";
    private final static String Name = "name";

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(ID, catId);
        bundle.putString(Name, catName);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle bundle) {
        if(bundle != null) {
            catId = bundle.getString(ID);
            catName = bundle.getString(Name);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.delete_fm_title);

        String message = getString(R.string.delete_sure);
        StringBuilder newStr = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            newStr.append(message.charAt(i));
            if (i == message.length() - 2)
                newStr.append(" ").append(catName);
        }
        builder.setMessage(newStr.toString());

        builder.setNegativeButton(android.R.string.cancel, (dialog, id) -> dialog.dismiss());
        builder.setPositiveButton(android.R.string.ok, (dialog, id) -> delete(catId));

        return builder.create();
    }

    private void delete(final String catId) {
        if (!(getActivity() instanceof HatterActivity))
            return;

        HatterView view = requireActivity().findViewById(R.id.hatterView);
        Cloud cloud = new Cloud();
        cloud.deleteFromCloud(catId, view);
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }
}
