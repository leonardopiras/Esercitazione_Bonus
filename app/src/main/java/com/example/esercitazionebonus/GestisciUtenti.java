package com.example.esercitazionebonus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.lang.UScript;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GestisciUtenti extends AppCompatActivity {

    AppSessione appSessione;
    ListView listView;
    AdapterListViewUtenti adapterListViewUtenti;
    Button homeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestisci_utenti);
        appSessione = getIntent().getParcelableExtra("AppSessione");

        listView = findViewById(R.id.listViewUtenti);

        homeButton = findViewById(R.id.homeButton);

        adapterListViewUtenti = new AdapterListViewUtenti(appSessione, appSessione.users, this);

        listView.setAdapter(adapterListViewUtenti);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showResult = new Intent(GestisciUtenti.this, Home.class);
                showResult.putExtra("AppSessione", appSessione);
                showResult.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(showResult);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent showResult = new Intent(GestisciUtenti.this, Home.class);
        showResult.putExtra("AppSessione", appSessione);
        showResult.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(showResult);
    }


        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.searcBar);

        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterListViewUtenti.getFilter().filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.searcBar) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

class AdapterListViewUtenti extends BaseAdapter implements Filterable {
    AppSessione appSessione;
    ArrayList<User> utentiFiltrati;
    Context context;


    public AdapterListViewUtenti(AppSessione appSessione, ArrayList<User> utentiFiltrati, Context context) {
        this.appSessione = appSessione;
        this.utentiFiltrati = utentiFiltrati;
        this.context = context;
    }

    @Override
    public int getCount() {
        return utentiFiltrati.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.single_row_user, null);

        CircleImageView profilePhoto = v.findViewById(R.id.profilePhoto);
        TextView username = v.findViewById(R.id.username);
        TextView abilitaAdminButton = v.findViewById(R.id.abilitaAdminButton);

        User tempUser = utentiFiltrati.get(position);

        if (!tempUser.photoUri.isEmpty())
            profilePhoto.setImageURI(Uri.parse(tempUser.photoUri));
            username.setText(tempUser.username);

        if (tempUser.isAdmin) {
            abilitaAdminButton.setTextColor(context.getResources().getColor(android.R.color.holo_blue_dark));
            abilitaAdminButton.setText("Admin abilitato");
            abilitaAdminButton.setBackgroundResource(0);
        } else {
            abilitaAdminButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Attenzione")
                            .setMessage("Sei sicuro di voler abilitare \"" + tempUser.username +"\" come admin?" +
                                    "\nQuest'azione è irreversibile")
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    appSessione.getUser(tempUser.username).isAdmin = true;
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .create()
                            .show();
                }
            });
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                if (constraint == null || constraint.length() == 0){
                    filterResults.count = utentiFiltrati.size();
                    filterResults.values = utentiFiltrati;
                } else {
                    String searchStr = constraint.toString().toLowerCase();
                    ArrayList<User> usersResult = new ArrayList<>();
                    for (User us : appSessione.users) {
                        if (us.username.contains(searchStr)) {
                            usersResult.add(us);
                        }
                        filterResults.count = usersResult.size();
                        filterResults.values = usersResult;
                    }

                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                utentiFiltrati = (ArrayList<User>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }
}

