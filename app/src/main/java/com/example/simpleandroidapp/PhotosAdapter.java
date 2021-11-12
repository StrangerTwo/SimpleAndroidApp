package com.example.simpleandroidapp;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {

    private final Context context;
    private final LayoutInflater layoutInflater;
    private List<Photo> photos;

    public PhotosAdapter(Context context, LayoutInflater layoutInflater) {
        this.context = context;
        this.layoutInflater = layoutInflater;
    }

    public void setPhotos(List<Photo> photos) {
        if (this.photos != null) this.notifyItemRangeRemoved(0, this.photos.size());
        this.photos = photos;
        this.notifyItemRangeInserted(0, photos.size());
    }

    @NonNull
    @Override
    public PhotosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.view_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotosAdapter.ViewHolder viewHolder, int position) {
        Photo photo = photos.get(position);

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();

        theme.resolveAttribute(R.attr.colorOnPrimary, typedValue, true);
        @ColorInt int colorPrimary = typedValue.data;

        theme.resolveAttribute(R.attr.colorOnSecondary, typedValue, true);
        @ColorInt int colorSecondary = typedValue.data;

        if (position % 2 == 1)
            viewHolder.layout.setBackgroundColor(colorPrimary);
        else
            viewHolder.layout.setBackgroundColor(colorSecondary);

        viewHolder.textViewTitle.setText(context.getString(R.string.photo_title_short, photo.getTitle()));
        viewHolder.textViewId.setText(context.getString(R.string.photo_id, photo.getId()));

        Picasso.get().load(photo.getThumbnailUrl()).into(viewHolder.imageViewPhoto);

        viewHolder.layout.setOnClickListener(v -> {
            Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);

            dialog.setContentView(R.layout.view_photo_dialog);

            TextView textViewTitle = dialog.findViewById(R.id.textViewPhotoDialogTitle);
            TextView textViewId = dialog.findViewById(R.id.textViewPhotoDialogId);
            TextView textViewAlbumId = dialog.findViewById(R.id.textViewPhotoDialogAlbumId);
            TextView textViewUrl = dialog.findViewById(R.id.textViewPhotoDialogUrl);
            TextView textViewThumbnailUrl = dialog.findViewById(R.id.textViewPhotoDialogThumbnailUrl);
            ImageView imageView = dialog.findViewById(R.id.imageViewPhotoDialog);

            textViewTitle.setText(context.getString(R.string.photo_title, photo.getTitle()));
            textViewId.setText(context.getString(R.string.photo_id, photo.getId()));
            textViewAlbumId.setText(context.getString(R.string.photo_album_id, photo.getAlbumId()));
            textViewUrl.setText(context.getString(R.string.photo_url, photo.getUrl()));
            textViewThumbnailUrl.setText(context.getString(R.string.photo_thumbnail_url, photo.getThumbnailUrl()));
            Picasso.get().load(photo.getUrl()).into(imageView);

            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return photos != null ? photos.size() : 0;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout layout;
        TextView textViewTitle, textViewId;
        ImageView imageViewPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layoutViewPhoto);
            textViewTitle = itemView.findViewById(R.id.textViewPhotoTitle);
            textViewId = itemView.findViewById(R.id.textViewPhotoId);
            imageViewPhoto = itemView.findViewById(R.id.imageViewPhoto);
        }
    }
}
