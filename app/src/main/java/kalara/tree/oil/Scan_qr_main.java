package kalara.tree.oil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Scan_qr_main extends Fragment {

    private static final int ZBAR_SCANNER_REQUEST = 0;
    private static final int ZBAR_QR_SCANNER_REQUEST = 1;
    ProgressDialog pDialog;
    Bundle bundle;
    static int value;
    ArrayList<Knowlegde_item> knowlegde_items=new ArrayList<Knowlegde_item>();
    ArrayList<HashMap<String, String>> Datalist1=new ArrayList<HashMap<String,String>>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout

        View layout = inflater.inflate(R.layout.scan_qr_main, container, false);
        Navigation_Acivity.title.setText("Scan QR");
        Navigation_Acivity.sidepannel.setVisibility(View.INVISIBLE);
        bundle=new Bundle();
        value=getArguments().getInt("value");
        bundle.putInt("value", value);


        return layout;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == getActivity().RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                String format = data.getStringExtra("SCAN_RESULT_FORMAT");
                Log.i("xZing", "contents: " + contents + " format: " + format); // Handle successful scan
                getproducts(contents);
                Toast.makeText(getActivity(), contents, Toast.LENGTH_LONG).show();


            } else if (resultCode == getActivity().RESULT_CANCELED) { // Handle cancel
                Log.i("xZing", "Cancelled");
            }
        }
    }

    public void getproducts(final String content) {
        // TODO Auto-generated method stub
        class HttpGetAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog

                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
                pDialog.show();

            }

            @Override
            protected String doInBackground(String... params) {

                // As you can see, doInBackground has taken an Array of Strings
                // as the argument
                // We need to specifically get the givenUsername and
                // givenPassword
					/* code=editText.getText().toString();*/


                // Create an intermediate to connect with the Internet

                // Set up secret key spec for 128-bit AES encryption and decryption


                HttpClient httpClient = new DefaultHttpClient();

                // Sending a GET request to the web page that we want
                // Because of we are sending a GET request, we have to pass the
                // values through the URL

                String url = "http://www.support-4-pc.com/clients/kalara/subscriber.php?action=getproduct2";


                System.out.println("=======url======" + url);
                HttpGet httpGet = new HttpGet(url);

                System.out.println("=======url=1=====" + httpGet);

                try {
                    // execute(); executes a request using the default context.
                    // Then we assign the execution result to HttpResponseo
                    HttpResponse httpResponse = httpClient.execute(httpGet);

                    System.out.println("==========httpResponse====="
                            + httpResponse.toString());
                    // System.out.println("httpResponse					// getEntity() ; " +
                    // "obtains the message entity of this response
                    // getContent() ; creates a new InputStream object of the
                    // entity.
                    // Now we need a readable source to read the byte stream
                    // that comes as the httpResponse
                    InputStream inputStream = httpResponse.getEntity()
                            .getContent();

                    // We have a byte stream. Next step is to convert it to a
                    // Character stream
                    InputStreamReader inputStreamReader = new InputStreamReader(
                            inputStream);

                    // Then we have to wraps the existing reader
                    // (InputStreamReader) and buffer the input
                    BufferedReader bufferedReader = new BufferedReader(
                            inputStreamReader);

                    // InputStreamReader contains a buffer of bytes read from
                    // the source stream and converts these into characters as
                    // needed.
                    // The buffer size is 8K
                    // Therefore we need a mechanism to append the separately
                    // coming chunks in to one String element
                    // We have to use a class that can handle modifiable
                    // sequence of characters for use in creating String
                    StringBuilder stringBuilder = new StringBuilder();

                    String bufferedStrChunk = null;

                    // There may be so many buffered chunks. We have to go
                    // through each and every chunk of characters
                    // and assign a each chunk to bufferedStrChunk String
                    // variable
                    // and append that value one by one to the stringBuilder
                    while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                        stringBuilder.append(bufferedStrChunk);
                    }

                    // Now we have the whole response as a String value.
                    // We return that value then the onPostExecute() can handle
                    // the content
                    System.out.println("Returning of doInBackground :"
                            + stringBuilder.toString());

                    // If the Username and Password match, it will return
                    // "working" as response
                    // If the Username or Password wrong, it will return
                    // "invalid" as response
                    return stringBuilder.toString();

                } catch (ClientProtocolException cpe) {
                    System.out.println("Exceptionrates caz of httpResponse :"
                            + cpe);
                    cpe.printStackTrace();
                } catch (IOException ioe) {
                    System.out
                            .println("Secondption generates caz of httpResponse :"
                                    + ioe);
                    ioe.printStackTrace();
                }

                return null;
            }

            // Argument comes for this method according to the return type of
            // the doInBackground() and
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (pDialog.isShowing())
                    pDialog.dismiss();
                System.out.println("====================" + result);

                if (result != null) {

                    JSONObject json = null;
                    try {
                        json = new JSONObject(result);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (json != null) {
                        // use myJson as needed, for example
                        try {
                            String object = json.getString("result");
                            System.out.println("object" + object);
                            JSONArray array = null;
                            JSONObject object1;
                            if (object.equals("true")) {

                                String respose = json.getString("response");
                                array = new JSONArray(respose);
                                for (int i = 0; i < array.length(); i++) {
                                    object1 = array.getJSONObject(i);
                                    String product_name = object1.getString("product_name");
                                    if(product_name.equals(content)) {
                                        String id = object1.getString("id");

                                        // String report = object1.getString("report");
                                        String category = object1.getString("category");
                                        //String product = object1.getString("product");
                                        String qrcode = object1.getString("qrcode");

                                        String size = object1.getString("size");
                                        JSONArray product_image = object1.getJSONArray("product_image");
                                        String image="";
                                        for(int j=0;j<product_image.length();j++) {
                                            image = product_image.get(j).toString();
                                        }
                                        Fragment create=new Create();
                                        bundle.putString("id",id);
                                        bundle.putString("category",category);
                                        bundle.putString("qrcode",qrcode);
                                        bundle.putString("size",size);
                                        bundle.putString("product_name",product_name);
                                        bundle.putString("image",image);
                                        create.setArguments(bundle);
                                        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                        fragmentManager.beginTransaction().addToBackStack(null)
                                                .replace(R.id.container_body, create).commit();

                                    }





                                }



                            } else {
                                // Toast.makeText(getActivity(), ".", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }


                }
                // Initialize the AsyncTask class

            }
        }
        HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask();
        // Parameter we pass in the execute() method is relate to the first
        // generic type oodf the AsyncTask
        // We are passing the connectWithHttpGet() meth arguments to that
        httpGetAsyncTask.execute();
    }
    public void Getproductdetail(final String content) {
        // TODO Auto-generated method stub
        class HttpGetAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog

                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
                pDialog.show();

            }

            @Override
            protected String doInBackground(String... params) {

                // As you can see, doInBackground has taken an Array of Strings
                // as the argument
                // We need to specifically get the givenUsername and
                // givenPassword
					/* code=editText.getText().toString();*/


                // Create an intermediate to connect with the Internet

                // Set up secret key spec for 128-bit AES encryption and decryption


                HttpClient httpClient = new DefaultHttpClient();

                // Sending a GET request to the web page that we want
                // Because of we are sending a GET request, we have to pass the
                // values through the URL

                String url = "http://www.support-4-pc.com/clients/kalara/subscriber.php?action=getproduct";


                System.out.println("=======url======" + url);
                HttpGet httpGet = new HttpGet(url);

                System.out.println("=======url=1=====" + httpGet);

                try {
                    // execute(); executes a request using the default context.
                    // Then we assign the execution result to HttpResponseo
                    HttpResponse httpResponse = httpClient.execute(httpGet);

                    System.out.println("==========httpResponse====="
                            + httpResponse.toString());
                    // System.out.println("httpResponse					// getEntity() ; " +
                    // "obtains the message entity of this response
                    // getContent() ; creates a new InputStream object of the
                    // entity.
                    // Now we need a readable source to read the byte stream
                    // that comes as the httpResponse
                    InputStream inputStream = httpResponse.getEntity()
                            .getContent();

                    // We have a byte stream. Next step is to convert it to a
                    // Character stream
                    InputStreamReader inputStreamReader = new InputStreamReader(
                            inputStream);

                    // Then we have to wraps the existing reader
                    // (InputStreamReader) and buffer the input
                    BufferedReader bufferedReader = new BufferedReader(
                            inputStreamReader);

                    // InputStreamReader contains a buffer of bytes read from
                    // the source stream and converts these into characters as
                    // needed.
                    // The buffer size is 8K
                    // Therefore we need a mechanism to append the separately
                    // coming chunks in to one String element
                    // We have to use a class that can handle modifiable
                    // sequence of characters for use in creating String
                    StringBuilder stringBuilder = new StringBuilder();

                    String bufferedStrChunk = null;

                    // There may be so many buffered chunks. We have to go
                    // through each and every chunk of characters
                    // and assign a each chunk to bufferedStrChunk String
                    // variable
                    // and append that value one by one to the stringBuilder
                    while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                        stringBuilder.append(bufferedStrChunk);
                    }

                    // Now we have the whole response as a String value.
                    // We return that value then the onPostExecute() can handle
                    // the content
                    System.out.println("Returning of doInBackground :"
                            + stringBuilder.toString());

                    // If the Username and Password match, it will return
                    // "working" as response
                    // If the Username or Password wrong, it will return
                    // "invalid" as response
                    return stringBuilder.toString();

                } catch (ClientProtocolException cpe) {
                    System.out.println("Exceptionrates caz of httpResponse :"
                            + cpe);
                    cpe.printStackTrace();
                } catch (IOException ioe) {
                    System.out
                            .println("Secondption generates caz of httpResponse :"
                                    + ioe);
                    ioe.printStackTrace();
                }

                return null;
            }

            // Argument comes for this method according to the return type of
            // the doInBackground() and
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (pDialog.isShowing())
                    pDialog.dismiss();
                System.out.println("====================" + result);

                if (result != null) {

                    JSONObject json = null;
                    try {
                        json = new JSONObject(result);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (json != null) {
                        // use myJson as needed, for example
                        try {
                            String object = json.getString("result");
                            System.out.println("object" + object);
                            JSONArray array = null;
                            JSONObject object1;
                            if (object.equals("true")) {

                                String respose = json.getString("response");
                                array = new JSONArray(respose);
                                for (int i = 0; i < array.length(); i++) {
                                    object1 = array.getJSONObject(i);
                                    String product_name = object1.getString("product_name");
                                    if(product_name.equals(content)) {
                                        String id = object1.getString("id");
                                        String product_name1= object1.getString("product_name");
                                        // String report = object1.getString("report");
                                        String barcode = object1.getString("barcode");
                                        //String product = object1.getString("product");
                                        String size = object1.getString("size");

                                        String status = object1.getString("status");
                                        String product_categories = object1.getString("category");

                                        String product_video = object1.getString("product_video");
                                        String time = object1.getString("time");
                                        // String product_image = object1.getString("product_image");
                                        JSONArray product_image = object1.getJSONArray("product_image");
                                        String ratings = object1.getString("ratings");
                                        if (ratings.equals("0")) {

                                        } else {
                                            JSONArray jsonarray = new JSONArray(ratings);
                                            JSONObject ob1;
                                            for (int k = 0; k < jsonarray.length(); k++) {
                                                ob1 = jsonarray.getJSONObject(k);
                                                String ratingval = ob1.getString("rating");
                                                String username = ob1.getString("username");
                                                String image = ob1.getString("image");

                                                String comment = ob1.getString("comment");

                                                String COUNT = ob1.getString("COUNT");


                                            }
                                        }
                                        HashMap<String, String> data = new HashMap<String, String>();
                                        JSONArray avgrating = new JSONArray("AVG(rating)");
                                        String AVG=null;
                                        for (int j = 0; j < avgrating.length(); j++) {
                                            JSONObject object2 = avgrating.getJSONObject(j);
                                            AVG = object2.getString("AVG(rating)");
                                            data.put("AVG", AVG);
                                        }


                                        for (int j = 0; j < product_image.length(); j++) {
                                            String image = product_image.get(j).toString();

                                            knowlegde_items.add(new Knowlegde_item(i, image, id));
                                            System.out.println("imagesss are" + " " + knowlegde_items.size());
                                        }


                                        int j;
                                        //  *//*for( j=0;j<product_image.length();j++) {
                                        // String image = product_image.get(j).toString();
                                        //  data.put("image", product_image.get(j).toString());


                                        // }*//*
                                        data.put("id", id);

                                        data.put("barcode", barcode);
                                        // data.put("product", product);
                                        data.put("size", size);

                                        data.put("status", status);
                                        data.put("product_categories", product_categories);
                                        data.put("product_name", product_name1);
                                        data.put("product_video", product_video);

                                        Datalist1.add(data);
                                        System.out.println("vallllllll of thhhh" + id + " " + Datalist1);
                                        Bundle bundle=new Bundle();
                                        bundle.putString("productname",product_name);
                                        bundle.putString("productcategory",product_categories);
                                        bundle.putString("product_url",product_video);
                                        bundle.putString("AVG",AVG);
                                        bundle.putString("id", id);
                                        bundle.putString("position","");
                                        bundle.putInt("value", value);
                                        // bundle.putString("image",productimages);
                                        Fragment loginActivity1=new Product_detail();
                                        loginActivity1.setArguments(bundle);
                                        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                        fragmentManager.beginTransaction()
                                                .replace(R.id.container_body, loginActivity1).commit();

                                    }

                                }



                            } else {
                                //  Toast.makeText(getActivity(), "Failed to login..", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }


                }
                // Initialize the AsyncTask class

            }
        }
        HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask();
        // Parameter we pass in the execute() method is relate to the first
        // generic type oodf the AsyncTask
        // We are passing the connectWithHttpGet() meth arguments to that
        httpGetAsyncTask.execute();
    }
}