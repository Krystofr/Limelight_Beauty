package com.krystofrapp.limelightbeauty;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.krystofrapp.limelightbeauty.slideradapter.ServicesAdapter;
import com.krystofrapp.limelightbeauty.slideradapter.SlidingImageAdapter;
import com.krystofrapp.limelightbeauty.slidermodel.ServicesModel;
import com.krystofrapp.limelightbeauty.slidermodel.SliderItem;
import com.krystofrapp.limelightbeauty.ui.account.AccountFragment;
import com.krystofrapp.limelightbeauty.ui.cart.CartFragment;
import com.krystofrapp.limelightbeauty.ui.services.ServicesFragment;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Homepage extends AppCompatActivity {

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    final Runnable Update = new Runnable() {
        public void run() {
            if (currentPage == NUM_PAGES) {
                currentPage = 0;
            }
            mPager.setCurrentItem(currentPage++, true);
        }
    };
    SharedPreferences mSharedPref;
    FirebaseDatabase mFirebaseDb;
    DatabaseReference mRef;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(Homepage.this, Homepage.class);
                    startActivity(intent);
                case R.id.navigation_services:
                    Intent services_intent = new Intent(Homepage.this, ServicesFragment.class);
                    startActivity(services_intent);
                    return true;
                case R.id.navigation_cart:
                    Intent cart_intent = new Intent(Homepage.this, CartFragment.class);
                    startActivity(cart_intent);
                    return true;
                case R.id.navigation_account:
                    Intent account_intent = new Intent(Homepage.this, AccountFragment.class);
                    startActivity(account_intent);
                    return true;

                default:
            }
            return false;
        }
    };
    LinearLayoutManager layoutManager;

    //Card Items List
    private ArrayList<ModelTopOrdered> itemsList;
    private RecyclerView recyclerView, recyclerView2;
    private Handler sliderHandler = new Handler();
    private ArrayList<SliderItem> imageModelArrayList;
    private int[] myImageList = new int[]{R.mipmap.slider_image1, R.mipmap.black_beauty,
            R.mipmap.blackfriday_deals, R.mipmap.facial_care
            , R.mipmap.slider_image1, R.mipmap.hair_rebonding, R.mipmap.massage, R.mipmap.mohawk_braids, R.drawable.bg2, R.drawable.bg4
            , R.drawable.bg5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);


        ActionBar actionBar = getSupportActionBar();
        actionBar.getCustomView();
        mSharedPref = getSharedPreferences("SortServices", MODE_PRIVATE);
        String mSorting = mSharedPref.getString("Sort", "newest"); //where if no setting is selected,  newest will be default

        if (mSorting.equals("newest")) {
            layoutManager = new LinearLayoutManager(this);
            layoutManager.setReverseLayout(true);
            layoutManager.setStackFromEnd(true);
            Intent intent = new Intent(Homepage.this, ItemsPreviewActivity.class);
            intent.addCategory("New");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        if (mSorting.equals("oldest")) {
            layoutManager = new LinearLayoutManager(this);
            layoutManager.setReverseLayout(false);
            layoutManager.setStackFromEnd(false);
            Intent intent = new Intent(Homepage.this, ItemsPreviewActivity.class);
            intent.addCategory("New");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        BottomNavigationView navigation = findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        recyclerView = findViewById(R.id.recycler_view_items);
        recyclerView.setHasFixedSize(true);
        recyclerView2 = findViewById(R.id.recycler_view_items2);
        recyclerView2.setHasFixedSize(true);
        itemsList = new ArrayList<>();


        //List of images for horizontal scroll view.
        itemsList.add(new ModelTopOrdered(R.drawable.bg0, "Fashion Tips", "Discount(50%OFF)"));
        itemsList.add(new ModelTopOrdered(R.drawable.bg1, "Facial Treatment", "10,000Xaf"));
        itemsList.add(new ModelTopOrdered(R.drawable.bg2, "Makeup", "15,000Xaf"));
        itemsList.add(new ModelTopOrdered(R.drawable.bg3, "Makeup Session", "9,500Xaf"));
        itemsList.add(new ModelTopOrdered(R.drawable.bg4, "Personal Facials", "5,500Xaf"));
        itemsList.add(new ModelTopOrdered(R.drawable.bg5, "Epic Nails", "5,000Xaf"));
        itemsList.add(new ModelTopOrdered(R.drawable.bg7, "Skin Care", "20,000Xaf"));
        itemsList.add(new ModelTopOrdered(R.drawable.bg8, "Hair Braiding", "25,000Xaf"));


        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        //Send query to Firebase Database
        mFirebaseDb = FirebaseDatabase.getInstance();
        mRef = mFirebaseDb.getReference("Recycler Images");
        ItemsRecyclerViewAdapter adapter = new ItemsRecyclerViewAdapter(this, itemsList);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView2.setLayoutManager(layoutManager2);

        ItemsRecyclerViewAdapter adapter2 = new ItemsRecyclerViewAdapter(this, itemsList);
        recyclerView2.setAdapter(adapter2);


        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList();

        init();
        mPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    //search data
    private void firebaseSearch(String searchText) {

        //convert string entered in SearchView to lowercase
        String query = searchText.toLowerCase();

        Query firebaseSearchQuery = mRef.orderByChild("search").startAt(query).endAt(query + "\uf8ff");

        FirebaseRecyclerAdapter<ServicesModel, ServicesAdapter> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<ServicesModel, ServicesAdapter>(
                        ServicesModel.class,
                        R.layout.row_items,
                        ServicesAdapter.class,
                        firebaseSearchQuery
                ) {
                    @Override
                    protected void populateViewHolder(ServicesAdapter servicesAdapter, ServicesModel servicesModel, int position) {
                        servicesAdapter.setDetails(getApplicationContext(), servicesModel.getTitle(), servicesModel.getPrice(), servicesModel.getImage());
                    }

                    @Override
                    public ServicesAdapter onCreateViewHolder(ViewGroup parent, int viewType) {

                        ServicesAdapter servicesAdapter = super.onCreateViewHolder(parent, viewType);

                        servicesAdapter.setOnClickListener(new ServicesAdapter.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                /*
                                //Views
                                TextView mTitleTv = view.findViewById(R.id.item_title);
                                TextView mPriceTv = view.findViewById(R.id.item_price);
                                CircleImageView mImageView = view.findViewById(R.id.item_image);
                                //get data from views
                                String mTitle = mTitleTv.getText().toString();
                                String mPrice = mPriceTv.getText().toString();
                                Drawable mDrawable = mImageView.getDrawable();
                                Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();
                                */

                                //pass this data to new activity
                                Intent intent = new Intent(view.getContext(), ItemsPreviewActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent); //start activity

                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                //TODO do your own implementaion on long item click
                            }
                        });

                        return servicesAdapter;
                    }


                };

        //set adapter to recyclerview
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        recyclerView2.setAdapter(firebaseRecyclerAdapter);
    }

    private ArrayList<SliderItem> populateList() {

        ArrayList<SliderItem> list = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            SliderItem imageModel = new SliderItem();
            imageModel.setImage(myImageList[i]);
            list.add(imageModel);
        }

        return list;
    }

    private void init() {

        mPager = findViewById(R.id.pager);

        mPager.setAdapter(new SlidingImageAdapter(Homepage.this, imageModelArrayList));

        CirclePageIndicator indicator = findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES = imageModelArrayList.size();

        // Auto start of viewpager
        final Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                sliderHandler.post(Update);
            }
        }, 2000, 2000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }


    //load data into recycler view onStart
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<ServicesModel, ServicesAdapter> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<ServicesModel, ServicesAdapter>(
                        ServicesModel.class,
                        R.layout.row_items,
                        ServicesAdapter.class,
                        mRef
                ) {
                    @Override
                    protected void populateViewHolder(ServicesAdapter servicesAdapter, ServicesModel servicesModel, int position) {
                        servicesAdapter.setDetails(getApplicationContext(), servicesModel.getTitle(), servicesModel.getPrice(), servicesModel.getImage());
                    }

                    @Override
                    public ServicesAdapter onCreateViewHolder(ViewGroup parent, int viewType) {

                        ServicesAdapter servicesAdapter = super.onCreateViewHolder(parent, viewType);

                        servicesAdapter.setOnClickListener(new ServicesAdapter.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                /*
                                //Views
                                TextView mTitleTv = view.findViewById(R.id.rTitleTv);
                                TextView mDescTv = view.findViewById(R.id.rDescriptionTv);
                                ImageView mImageView = view.findViewById(R.id.rImageView);
                                //get data from views
                                String mTitle = mTitleTv.getText().toString();
                                String mDesc = mDescTv.getText().toString();
                                Drawable mDrawable = mImageView.getDrawable();
                                Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();
                                */
                                //pass this data to new activity
                                Intent intent = new Intent(view.getContext(), ItemsPreviewActivity.class);
                                startActivity(intent); //start activity


                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                //TODO do your own implementaion on long item click
                            }
                        });

                        return servicesAdapter;
                    }

                };

        //set adapter to recyclerview
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        recyclerView2.setAdapter(firebaseRecyclerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu; this adds items to the action bar if it present
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Filter as you type
                firebaseSearch(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //handle other action bar item clicks here
        if (id == R.id.action_sort) {
            //display alert dialog to choose sorting
            showSortDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSortDialog() {
        //options to display in dialog
        String[] sortOptions = {" Newest", " Oldest"};
        //create alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort by") //set title
                .setIcon(R.drawable.ic_action_sort) //set icon
                .setItems(sortOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position of the selected item
                        // 0 means "Newest" and 1 means "oldest"
                        if (which == 0) {
                            //sort by newest
                            //Edit our shared preferences
                            SharedPreferences.Editor editor = mSharedPref.edit();
                            editor.putString("Sort", "newest"); //where 'Sort' is key & 'newest' is value
                            editor.apply(); // apply/save the value in our shared preferences
                            recreate(); //restart activity to take effect
                        } else if (which == 1) {
                            {
                                //sort by oldest
                                //Edit our shared preferences
                                SharedPreferences.Editor editor = mSharedPref.edit();
                                editor.putString("Sort", "oldest"); //where 'Sort' is key & 'oldest' is value
                                editor.apply(); // apply/save the value in our shared preferences
                                recreate(); //restart activity to take effect
                            }
                        }
                    }
                });
        builder.show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(Update);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(Update, 2000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("BackPressed: SignOut?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onBackPressed();
                    }
                });
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.searchView);

        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                Log.e("Home", " data search" + newText);

                customAdapter.getFilter().filter(newText);


                return true;
            }
        });


        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.searchView) {
            Intent intent = new Intent(Homepage.this, ItemsPreviewActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public class CustomAdapter extends BaseAdapter implements Filterable {

        private List<ItemsModel> itemsModelsl;
        private List<ItemsModel> itemsModelListFiltered;
        private Context context;

        public CustomAdapter(List<ItemsModel> itemsModelsl, Context context) {
            this.itemsModelsl = itemsModelsl;
            this.itemsModelListFiltered = itemsModelsl;
            this.context = context;
        }


        @Override
        public int getCount() {
            return itemsModelListFiltered.size();
        }

        @Override
        public Object getItem(int position) {
            return itemsModelListFiltered.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            @SuppressLint("ViewHolder")
            View view = getLayoutInflater().inflate(R.layout.row_items, null);


            TextView names = view.findViewById(R.id.name);
            TextView emails = view.findViewById(R.id.email);
            ImageView imageView = view.findViewById(R.id.images);

            names.setText(itemsModelListFiltered.get(position).getName());
            emails.setText(itemsModelListFiltered.get(position).getEmail());
            imageView.setImageResource(itemsModelListFiltered.get(position).getImages());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("homepage activity", "item clicked");
                    startActivity(new Intent(Homepage.this, ItemsPreviewActivity.class).putExtra("items", itemsModelListFiltered.get(position)));

                }
            });

            return view;
        }


        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults filterResults = new FilterResults();
                    if (constraint == null || constraint.length() == 0) {
                        filterResults.count = itemsModelsl.size();
                        filterResults.values = itemsModelsl;

                    } else {
                        List<ItemsModel> resultsModel = new ArrayList<>();
                        String searchStr = constraint.toString().toLowerCase();

                        for (ItemsModel itemsModel : itemsModelsl) {
                            if (itemsModel.getName().contains(searchStr) || itemsModel.getEmail().contains(searchStr)) {
                                resultsModel.add(itemsModel);

                            }
                            filterResults.count = resultsModel.size();
                            filterResults.values = resultsModel;
                        }


                    }

                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    itemsModelListFiltered = (List<ItemsModel>) results.values;
                    notifyDataSetChanged();

                }
            };
            return filter;
        }
    }
    */
}





/*
  viewPager2 = findViewById(R.id.viewPagerImageSlider);

        //Prepare list of images for slider from Drawable.
        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.mipmap.blackfriday_deals));
        sliderItems.add(new SliderItem(R.mipmap.black_beauty));
        sliderItems.add(new SliderItem(R.mipmap.slider_image1));
        sliderItems.add(new SliderItem(R.mipmap.facial_care));
        sliderItems.add(new SliderItem(R.mipmap.hair_rebonding));
        sliderItems.add(new SliderItem(R.mipmap.massage));
        sliderItems.add(new SliderItem(R.mipmap.mohawk_braids));
        sliderItems.add(new SliderItem(R.mipmap.spa));
        sliderItems.add(new SliderItem(R.mipmap.student_discount));


viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.SCROLL_AXIS_HORIZONTAL);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(10));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
                page.animate();
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3500); //Slide duration.
            }
        });

        }


         private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3500);
    }
 */