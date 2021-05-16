package adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practicaltest_bookstoremobile.BookDetails;
import com.example.practicaltest_bookstoremobile.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import model.Items;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private final Items dataList;

    public BookAdapter(Items dataList) {
        this.dataList = dataList;

    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_book, parent, false);


        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        if (dataList.getItems().get(position).getVolumeInfo().getImageLinks() != null)
            Picasso.get()
                    .load(dataList.getItems().get(position).getVolumeInfo().getImageLinks().getThumbnail())
                    .resize(256, 414).into(holder.imbCover);
        else
            Picasso.get()
                    .load("https://books.google.pt/googlebooks/images/no_cover_thumb.gif")
                    .resize(256, 414).into(holder.imbCover);

        holder.imbCover.setOnClickListener(v -> {


            Intent intent = new Intent(v.getContext(), BookDetails.class);
            Gson gson = new Gson();
            String json = gson.toJson(dataList.getItems().get(position));
            intent.putExtra("Book",json);

            v.getContext().startActivity(intent);
        });

    }


    @Override
    public int getItemCount() {
        return dataList.getItems().size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {

        ImageButton imbCover;


        BookViewHolder(View itemView) {
            super(itemView);
            imbCover = itemView.findViewById(R.id.imb_book_Cover);
        }
    }


}
