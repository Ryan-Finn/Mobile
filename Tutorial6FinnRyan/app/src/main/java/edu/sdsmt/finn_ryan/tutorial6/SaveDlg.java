package edu.sdsmt.finn_ryan.tutorial6;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SaveDlg extends DialogFragment {
    private AlertDialog dlg;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());// Set the title
        builder.setTitle(R.string.saveTitle);

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.save_item, null);
        builder.setView(view);

        builder.setPositiveButton(android.R.string.ok, (dialog, id) -> {
            EditText editName = dlg.findViewById(R.id.editName);
            save(editName.getText().toString());
        });

        builder.setNegativeButton(android.R.string.cancel, (dialog, id) -> dialog.dismiss());

        dlg = builder.create();

        return dlg;
    }

    private void save(final String name) {
        if (!(getActivity() instanceof HatterActivity))
            return;
        HatterActivity activity = (HatterActivity) getActivity();
        HatterView view = activity.findViewById(R.id.hatterView);

        Cloud cloud = new Cloud();
        cloud.saveToCloud(name, view);
    }
}
