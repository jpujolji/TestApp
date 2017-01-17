package com.jpujolji.testapp.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpujolji.testapp.model.ItemList;
import com.jpujolji.testapp.R;
import com.squareup.picasso.Picasso;

import io.realm.Realm;

public class DetailItemActivity extends AppCompatActivity {

    public static final String ID_ITEM = "detail:id";
    public static final String VIEW_CITY = "detail:city";
    ImageView ivPhoto;
    ItemList item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString(ID_ITEM, "");

        Realm realm = Realm.getDefaultInstance();

        item = realm.where(ItemList.class).equalTo("id", id).findFirst();

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setTitle(item.title.replaceAll("(/r/|r/|/u/)", ""));

        ivPhoto = (ImageView) collapsingToolbarLayout.findViewById(R.id.ivPhoto);

        ViewCompat.setTransitionName(ivPhoto, VIEW_CITY);
        loadImage();
        loadContent();

    }

    private void loadImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && addTransitionListener()) {
            loadThumbnail();
        } else {
            loadThumbnail();
        }
    }

    public void loadContent() {
        ImageView ivHeader = (ImageView) findViewById(R.id.ivHeader);
        TextView tvHeader = (TextView) findViewById(R.id.tvHeader);
        TextView tvContent = (TextView) findViewById(R.id.tvContent);

        if (item.id != null && !item.id.isEmpty()) {
            tvHeader.setText(item.id);
        } else {
            tvHeader.setText("");
        }

        if (item.description != null && !item.description.isEmpty()) {
            tvContent.setText(item.description);
        } else {
            tvContent.setText("");
        }

        if (item.header_img != null && !item.header_img.isEmpty()) {
            Picasso.with(DetailItemActivity.this).load(item.header_img).into(ivHeader);
        } else {
            ivHeader.setImageDrawable(null);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadThumbnail() {
        if (item.icon_img != null && !item.icon_img.isEmpty()) {
            Picasso.with(DetailItemActivity.this)
                    .load(item.icon_img)
                    .noFade()
                    .into(ivPhoto);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private boolean addTransitionListener() {
        final Transition transition = getWindow().getSharedElementEnterTransition();

        if (transition != null) {
            // There is an entering shared element transition so add a listener to it
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    // As the transition has ended, we can now load the full-size image
                    loadImage();

                    // Make sure we remove ourselves as a listener
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionStart(Transition transition) {
                    // No-op
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    // Make sure we remove ourselves as a listener
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionPause(Transition transition) {
                    // No-op
                }

                @Override
                public void onTransitionResume(Transition transition) {
                    // No-op
                }
            });
            return true;
        }

        // If we reach here then we have not added a listener
        return false;
    }
}
