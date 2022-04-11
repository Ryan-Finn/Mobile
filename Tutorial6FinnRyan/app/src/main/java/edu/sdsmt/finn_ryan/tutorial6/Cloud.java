package edu.sdsmt.finn_ryan.tutorial6;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class Cloud {
    private final static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference hattingsList = database.getReference("hattings").child(MonitorCloud.INSTANCE.getUserUid());

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public static class Item {
        public final String name;
        public final String id;

        public Item(String name, String id) {
            this.name = name;
            this.id = id;
        }
    }

    public static class CatalogAdapter extends RecyclerView.Adapter<ViewHolder> {
        private final ArrayList<Item> items;
        public final CatalogCallback clickEvent;

        public CatalogAdapter(final View view,  CatalogCallback click) {
            items = getCatalog(view);
            clickEvent = click;
        }

        public ArrayList<Item> getCatalog(final View view) {
            ArrayList<Item> newItems = new ArrayList<>();

            database.goOnline();

            // Read from the database
            hattingsList.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Item tempItem = new Item(Objects.requireNonNull(child.child("name").getValue()).toString(), child.getKey());
                        newItems.add(tempItem);
                    }

                    view.post(CatalogAdapter.this::notifyDataSetChanged);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    view.post(() -> Toast.makeText(view.getContext(), R.string.catalog_fail, Toast.LENGTH_SHORT).show());
                }
            });

            return newItems;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.catalog_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.view.setOnClickListener(v -> {
                Item catItem = getItem(position);
                clickEvent.callback(catItem);
            });

            TextView tv = holder.view.findViewById(R.id.textItem);
            String text = " " + items.get(position).name;
            tv.setText(text);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public Item getItem(int position) {
            return items.get(position);
        }

        public long getItemId(int position) {
            return position;
        }
    }

    public void loadFromCloud(final HatterView view, String catId, final Dialog dlg) {
        // Read from the database
        hattingsList.child(catId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                view.loadJSON(dataSnapshot);
                dlg.dismiss();
                if (view.getContext() instanceof HatterActivity)
                    ((HatterActivity) view.getContext()).updateUI();
                view.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                view.post(() -> {
                    Toast.makeText(view.getContext(), R.string.loading_fail, Toast.LENGTH_SHORT).show();
                    dlg.dismiss();
                });
            }
        });
    }

    public void saveToCloud(String name, HatterView view) {
        name = name.trim();
        if (name.length() == 0)
            view.post(() -> Toast.makeText(view.getContext(), R.string.save_fail, Toast.LENGTH_SHORT).show());

        String key = hattingsList.push().getKey();
        assert key != null;
        DatabaseReference myRef = hattingsList.child(key);
        myRef.child("name").setValue(name, (databaseError, databaseReference) -> {
            if (databaseError != null)
                view.post(() -> Toast.makeText(view.getContext(), R.string.server_fail, Toast.LENGTH_SHORT).show());
        });
        view.saveJSON(myRef);
    }

    public void deleteFromCloud(String catId, HatterView view) {
        if (catId.length() == 0)
            view.post(() -> Toast.makeText(view.getContext(), R.string.delete_fail, Toast.LENGTH_SHORT).show());

        DatabaseReference myRef = hattingsList.child(catId);
        myRef.removeValue();
        view.deleteJSON(myRef);
    }
}
