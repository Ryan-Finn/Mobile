package edu.sdsmt.finn_ryan.tutorial6;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LoadDlg extends DialogFragment implements CatalogCallback {
    private final Boolean isLoad;

    public LoadDlg() {
        super();
        this.isLoad = true;
    }

    public LoadDlg(Boolean isLoad) {
        super();
        this.isLoad = isLoad;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (isLoad)
            builder.setTitle(R.string.load_fm_title);
        else
            builder.setTitle(R.string.delete_fm_title);

        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.catalog_dlg, null);
        builder.setView(view);

        // Add a cancel button
        builder.setNegativeButton(android.R.string.cancel, (dialog, id) -> dialog.dismiss());

        AlertDialog dlg = builder.create();

        RecyclerView list = view.findViewById(R.id.listHattings);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        final Cloud.CatalogAdapter adapter = new Cloud.CatalogAdapter(list, this);
        list.setAdapter(adapter);

        return dlg;
    }

    @Override
    public void callback(Cloud.Item catItem) {
        if (isLoad) {
            LoadingDlg loadDlg = new LoadingDlg();
            loadDlg.setCatId(catItem.id);
            loadDlg.show(getParentFragmentManager(), "loading");
        } else {
            DeletingDlg deleteDlg = new DeletingDlg();
            deleteDlg.setCatId(catItem.id);
            deleteDlg.setCatName(catItem.name);
            deleteDlg.show(getParentFragmentManager(), "deleting");
        }
        this.dismiss();
    }
}
