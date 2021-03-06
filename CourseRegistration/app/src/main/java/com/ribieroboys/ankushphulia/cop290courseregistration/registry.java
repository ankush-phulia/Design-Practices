package com.ribieroboys.ankushphulia.cop290courseregistration;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ankush.test.R;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;

public class registry extends AppCompatActivity {

    final String url = "http://agni.iitd.ernet.in/cop290/assign0/register/";

    public boolean isValid(String entry_no) {

        // Using RegEx to check validity of entry no.
        String testString = entry_no.toLowerCase();
        String Regex_Pattern = "^201[0-4](bb|cs|ce|ch|ee|mt|me|tt)[1-7][0-9]{4}$";
        Pattern p = Pattern.compile(Regex_Pattern);
        Matcher m = p.matcher(testString);
        return m.find();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);

        // get all UI elements so that they can be worked upon

        final Context context = this;
        final Button btn = (Button) findViewById(R.id.button2);
        final EditText team_name = (EditText) findViewById(R.id.editText);
        final EditText name1 = (EditText) findViewById(R.id.editText2);
        final EditText entry_no1 = (EditText) findViewById(R.id.editText3);
        final EditText name2 = (EditText) findViewById(R.id.editText4);
        final EditText entry_no2 = (EditText) findViewById(R.id.editText5);
        final EditText name3 = (EditText) findViewById(R.id.editText6);
        final EditText entry_no3 = (EditText) findViewById(R.id.editText7);
        final TextView asterisk1 = (TextView) findViewById(R.id.textView21);
        final TextView asterisk2 = (TextView) findViewById(R.id.textView31);
        final TextView asterisk3 = (TextView) findViewById(R.id.textView41);
        final TextView asterisk4 = (TextView) findViewById(R.id.textView51);

        // dealing with asterisk on changing text inside team name

        team_name.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (team_name.getText().toString().length() != 0) {
                            asterisk1.setText("");
                        } else {
                            asterisk1.setText("*");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                });

        // dealing with asterisk on changing text inside member1

        name1.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (name1.getText().toString().length() != 0
                                && entry_no1.getText().toString().length() != 0) {
                            asterisk2.setText("");
                        } else {
                            asterisk2.setText("*");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        entry_no1.addTextChangedListener(
                                new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(
                                            CharSequence s, int start, int count, int after) {}

                                    @Override
                                    public void onTextChanged(
                                            CharSequence s, int start, int before, int count) {
                                        if (entry_no1.getText().toString().length() == 0
                                                || name1.getText().toString().length() == 0) {
                                            asterisk2.setText("*");
                                        } else {
                                            asterisk2.setText("");
                                        }
                                        // soft check for length of valid entry number
                                        if (entry_no1.getText().toString().length() == 11) {
                                            name2.requestFocus();
                                        }
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {}
                                });
                    }
                });

        // dealing with asterisk on changing text inside member2

        name2.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (name2.getText().toString().length() != 0
                                && entry_no2.getText().toString().length() != 0) {
                            asterisk3.setText("");
                        } else {
                            asterisk3.setText("*");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        entry_no2.addTextChangedListener(
                                new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(
                                            CharSequence s, int start, int count, int after) {}

                                    @Override
                                    public void onTextChanged(
                                            CharSequence s, int start, int before, int count) {
                                        if (entry_no2.getText().toString().length() == 0
                                                || name2.getText().toString().length() == 0) {
                                            asterisk3.setText("*");
                                        } else {
                                            asterisk3.setText("");
                                        }
                                        // soft check for length of valid entry number
                                        if (entry_no2.getText().toString().length() == 11) {
                                            name3.requestFocus();
                                        }
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {}
                                });
                    }
                });

        // dealing with asterisk on changing text inside member3

        name3.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (name3.getText().toString().length() != 0
                                && entry_no3.getText().toString().length() != 0) {
                            asterisk4.setText("");
                        } else {
                            asterisk4.setText("*");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        entry_no3.addTextChangedListener(
                                new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(
                                            CharSequence s, int start, int count, int after) {}

                                    @Override
                                    public void onTextChanged(
                                            CharSequence s, int start, int before, int count) {
                                        if (entry_no3.getText().toString().length() == 0
                                                || name3.getText().toString().length() == 0) {
                                            asterisk4.setText("*");
                                        } else {
                                            asterisk4.setText("");
                                        }
                                        // soft check for length of valid entry number
                                        if (entry_no3.getText().toString().length() == 11) {
                                            btn.requestFocus();
                                        }
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {}
                                });
                    }
                });

        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // checking if entries are filled properly
                        if (team_name.getText().toString().length() == 0) {
                            // No team name!
                            team_name.setHint("This is a required field");
                            team_name.setText("");
                            team_name.setHintTextColor(Integer.parseInt(String.valueOf(Color.RED)));
                            team_name.requestFocus();
                        } else if (name1.getText().toString().length() == 0) {
                            // Name of member1 absent!
                            name1.setHint("This is a required field");
                            name1.setText("");
                            name1.setHintTextColor(Integer.parseInt(String.valueOf(Color.RED)));
                            name1.requestFocus();
                        } else if (entry_no1.getText().toString().length() == 0) {
                            // Entry no. of member1 absent!
                            entry_no1.setHint("This is a required field");
                            entry_no1.setText("");
                            entry_no1.setHintTextColor(Integer.parseInt(String.valueOf(Color.RED)));
                            entry_no1.requestFocus();
                        } else if (!isValid(entry_no1.getText().toString())) {
                            // checking validity of entry no. of member1
                            new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_DARK)
                                    .setTitle("Invalid Entry Number")
                                    .setMessage("Please enter a valid Entry Number for Member 1")
                                    .setPositiveButton(
                                            "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog, int which) {
                                                    entry_no1.setText("");
                                                    entry_no1.setHint(
                                                            "Entry Number of First Member");
                                                    entry_no1.setHintTextColor(
                                                            Integer.parseInt(
                                                                    String.valueOf(Color.WHITE)));
                                                    entry_no1.requestFocus();
                                                }
                                            })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        } else if (name2.getText().toString().length() == 0) {
                            // Name of member2 absent!
                            name2.setHint("This is a required field");
                            name2.setText("");
                            name2.setHintTextColor(Integer.parseInt(String.valueOf(Color.RED)));
                            name2.requestFocus();
                        } else if (entry_no2.getText().toString().length() == 0) {
                            // Entry no. of member2 absent!
                            entry_no2.setHint("This is a required field");
                            entry_no2.setText("");
                            entry_no2.setHintTextColor(Integer.parseInt(String.valueOf(Color.RED)));
                            entry_no2.requestFocus();
                        } else if (!isValid(entry_no2.getText().toString())) {
                            // checking validity of entry no. of member2
                            new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_DARK)
                                    .setTitle("Invalid Entry Number")
                                    .setMessage("Please enter a valid Entry Number for Member 2")
                                    .setPositiveButton(
                                            "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog, int which) {
                                                    entry_no2.setText("");
                                                    entry_no2.setHint(
                                                            "Entry Number of First Member");
                                                    entry_no2.setHintTextColor(
                                                            Integer.parseInt(
                                                                    String.valueOf(Color.WHITE)));
                                                    entry_no2.requestFocus();
                                                }
                                            })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        } else if (name3.getText().toString().length() == 0) {
                            // Name of member3 absent!
                            name3.setHint("This is a required field");
                            name3.setText("");
                            name3.setHintTextColor(Integer.parseInt(String.valueOf(Color.RED)));
                            name3.requestFocus();
                        } else if (entry_no3.getText().toString().length() == 0) {
                            // Entry no. of member3 absent!
                            entry_no3.setHint("This is a required field");
                            entry_no3.setText("");
                            entry_no3.setHintTextColor(Integer.parseInt(String.valueOf(Color.RED)));
                            entry_no3.requestFocus();
                        } else if (!isValid(entry_no3.getText().toString())) {
                            // checking validity of entry no. of member3
                            new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_DARK)
                                    .setTitle("Invalid Entry Number")
                                    .setMessage("Please enter a valid Entry Number for Member 3")
                                    .setPositiveButton(
                                            "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog, int which) {
                                                    entry_no3.setText("");
                                                    entry_no3.setHint(
                                                            "Entry Number of First Member");
                                                    entry_no3.setHintTextColor(
                                                            Integer.parseInt(
                                                                    String.valueOf(Color.WHITE)));
                                                    entry_no3.requestFocus();
                                                }
                                            })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        } else {
                            /* everything is perfect...
                             * way to go!
                             */

                            // JsonObjectRequest code inspired by AndroidHive.com

                            StringRequest postReq =
                                    new StringRequest(
                                            Request.Method.POST,
                                            url,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    String success = null;
                                                    // receive reply from server and display it to
                                                    // the user as appropriate
                                                    try {
                                                        JSONObject jsonResponse =
                                                                new JSONObject(response);
                                                        success =
                                                                jsonResponse
                                                                        .get("RESPONSE_MESSAGE")
                                                                        .toString();
                                                        Toast.makeText(
                                                                        registry.this,
                                                                        success,
                                                                        Toast.LENGTH_LONG)
                                                                .show();
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    } finally {
                                                        if (!success.equals("Data not posted!")) {
                                                            Intent intent =
                                                                    new Intent(
                                                                            registry.this,
                                                                            Welcome.class);
                                                            startActivity(intent);
                                                        }
                                                    }
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(
                                                                    registry.this,
                                                                    "Connection Error",
                                                                    Toast.LENGTH_LONG)
                                                            .show();
                                                }
                                            }) {

                                        // putting the parameters as key-value pairs to pass
                                        @Override
                                        public Map<String, String> getParams() {
                                            Map<String, String> params =
                                                    new HashMap<String, String>();
                                            params.put("teamname", team_name.getText().toString());
                                            params.put("entry1", entry_no1.getText().toString());
                                            params.put("name1", name1.getText().toString());
                                            params.put("entry2", entry_no2.getText().toString());
                                            params.put("name2", name2.getText().toString());
                                            params.put("entry3", entry_no3.getText().toString());
                                            params.put("name3", name3.getText().toString());
                                            return params;
                                        }
                                    };
                            // Add the team details to global request queue
                            RequestQ.getInstance().addToRequestQ(postReq);
                        }
                    }
                });
    }
}
