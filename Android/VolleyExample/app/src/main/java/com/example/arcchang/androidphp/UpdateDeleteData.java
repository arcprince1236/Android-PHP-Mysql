package com.example.arcchang.androidphp;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

public class UpdateDeleteData extends Activity {
    private final String TAG = "KK";
    EditText item_name_et;
    String id, item_name;

    ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete_data);

        PD = new ProgressDialog(this);
        PD.setMessage("please wait.....");
        PD.setCancelable(false);

        item_name_et = (EditText) findViewById(R.id.modify_item_et);
        Intent i = getIntent();

        HashMap<String, String> item = (HashMap<String, String>) i
                .getSerializableExtra("item");

        id = item.get(ReadData.ITEM_EMP);
        item_name = item.get(ReadData.ITEM_NAME);

        item_name_et.setText(item_name);

    }

    public void update(View view) {
        //perform update
        PD.show();
        item_name = item_name_et.getText().toString();

        String update_url = "http://192.168.0.141/PHP/update.php";

        Map<String, String> params = new HashMap<String, String>();
        params.put("item_emid", id);
        params.put("item_name", item_name);

        //Log.v(TAG, id + " @@@@@@@@@@@ " + item_name);

        CustomRequest update_request = new CustomRequest(update_url,
                params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    int success = response.getInt("success");

                    if (success == 1) {
                        PD.dismiss();
                        Toast.makeText(getApplicationContext(),
                                "Updated Successfully",
                                Toast.LENGTH_SHORT).show();
                        // redirect to readdata
                        MoveToReadData();

                    } else {
                        PD.dismiss();
                        Toast.makeText(getApplicationContext(),
                                "failed to update", Toast.LENGTH_SHORT)
                                .show();
                    }

                } catch (JSONException e) {
                    Log.v(TAG, "!!!!!!!!!!!!");
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Adding request to request queue
        MyApplication.getInstance().addToReqQueue(update_request);
    }

    public void delete(View view) {
        PD.show();
        String delete_url = "http://192.168.0.141/PHP/delete.php?EmployeeID="
                + id;

        JsonObjectRequest delete_request = new JsonObjectRequest(delete_url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    int success = response.getInt("success");

                    if (success == 1) {
                        PD.dismiss();
                        Toast.makeText(getApplicationContext(),
                                "Deleted Successfully",
                                Toast.LENGTH_SHORT).show();
                        // redirect to readdata
                        MoveToReadData();
                    } else {
                        PD.dismiss();
                        Toast.makeText(getApplicationContext(),
                                "failed to delete", Toast.LENGTH_SHORT)
                                .show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Adding request to request queue
        MyApplication.getInstance().addToReqQueue(delete_request);

    }

    private void MoveToReadData() {
        Intent read_intent = new Intent(UpdateDeleteData.this, ReadData.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(read_intent);
    }
}