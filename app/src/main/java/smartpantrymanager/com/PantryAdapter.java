package smartpantrymanager.com;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private Context context;
    private List<PantryItem> pantryItems;

    public PantryAdapter(Context context, List<PantryItem> pantryItems) {
        this.context = context;
        this.pantryItems = pantryItems;
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context).inflate(
                R.layout.activity_item_pantry,
                parent,
                false
        );

        return new PantryViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {

        PantryItem item = pantryItems.get(position);
        holder.tvIngredientName.setText(
                item.getIngredientName()
        );


        holder.tvQuantity.setText(
                "Quantity: " +
                        item.getQuantity() +
                        " " +
                        item.getUnit()
        );

        if (item.getExpiryDate() != null &&
                !item.getExpiryDate().isEmpty()) {

            holder.tvExpiryDate.setText(
                    "Expiry Date: " +
                            item.getExpiryDate()
            );

        } else {

            holder.tvExpiryDate.setText(
                    "Expiry Date: Not specified"
            );
        }

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(
                    context,
                    AddEditPantry.class
            );

            intent.putExtra(
                    "pantry_id",
                    item.getId()
            );

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return pantryItems.size();
    }

    public static class PantryViewHolder extends RecyclerView.ViewHolder {

        TextView tvIngredientName;
        TextView tvQuantity;
        TextView tvExpiryDate;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvIngredientName =
                    itemView.findViewById(
                            R.id.tvIngredientName
                    );

            tvQuantity =
                    itemView.findViewById(
                            R.id.tvQuantity
                    );

            tvExpiryDate =
                    itemView.findViewById(
                            R.id.tvExpiryDate
                    );
        }
    }
}