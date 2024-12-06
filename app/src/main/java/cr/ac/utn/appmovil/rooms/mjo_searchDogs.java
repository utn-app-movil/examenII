package cr.ac.utn.appmovil.rooms;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class mjo_searchDogs extends Activity {

    private EditText searchEditText;
    private Button searchButton;
    private ListView resultsListView;
    private List<String> dogList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mjo_activity_search_dogs);

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        resultsListView = findViewById(R.id.resultsListView);

        dogList = new ArrayList<>();
        dogList.add("Labrador");
        dogList.add("Beagle");
        dogList.add("Bulldog");
        dogList.add("Poodle");

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchEditText.getText().toString().trim();
                if (!query.isEmpty()) {
                    searchDogs(query);
                } else {
                    Toast.makeText(mjo_searchDogs.this, "Please enter a search term", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void searchDogs(String query) {
        List<String> results = new ArrayList<>();
        for (String dog : dogList) {
            if (dog.toLowerCase().contains(query.toLowerCase())) {
                results.add(dog);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, results);
        resultsListView.setAdapter(adapter);
    }
}
