package com.hotellister;

import android.content.Context;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Database;
import com.couchbase.lite.Expression;
import com.couchbase.lite.Meta;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.Result;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.SelectResult;

import com.couchbase.lite.DatabaseConfiguration;
import com.couchbase.lite.MutableDocument;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HotelLister extends CordovaPlugin {

    private Database database;
	private Database userDB;

    // tag::initialize[]
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        Context context = cordova.getContext();
        DatabaseManager.getSharedInstance(context);
        this.database = DatabaseManager.getDatabase();
		this.userDB = DatabaseManager.getUserDatabase();
    }
    // end::initialize[]

    // tag::execute[]
    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) {
        if ("queryHotels".equals(action)) {
            List<Result> resultList = queryHotels();
            ArrayList<Map<String,Object>> arrayList = new ArrayList<>();
            assert resultList != null;
            for (Result result : resultList) {
                Map<String,Object> map = result.toMap();
                arrayList.add(map);
            }
            JSONArray jsonArray = new JSONArray(arrayList);
            callbackContext.success(jsonArray);
            return true;
			
        } else if ("addUser".equals(action)) {
			MutableDocument mutableDoc = new MutableDocument()
			.setString("name", "Amer")
			.setString("email", "amer@gmail.com")
			.setString("state", "Johor");

			try {
				// Save it to the database.
				this.userDB.save(mutableDoc);
				callbackContext.success();
				return true;
			} catch(CouchbaseLiteException e) {
				e.printStackTrace();
			}
			
        } else if ("getUser".equals(action)) {
			List<Result> resultList = queryUser();
            ArrayList<Map<String,Object>> arrayList = new ArrayList<>();
            assert resultList != null;
            for (Result result : resultList) {
                Map<String,Object> map = result.toMap();
                arrayList.add(map);
            }
            JSONArray jsonArray = new JSONArray(arrayList);
            callbackContext.success(jsonArray);
            return true;
			
		}
        return false;
    }
    // end::execute[]

    // tag::query-hotels[]
    private List<Result> queryHotels() {
        String DOC_TYPE = "hotel";

        Query query = QueryBuilder
            .select(
                SelectResult.expression(Meta.id),
                SelectResult.property("address"),
                SelectResult.property("phone"),
                SelectResult.property("name")
            )
            .from(DataSource.database(database))
            .where(
                Expression.property("type")
                    .equalTo(Expression.string(DOC_TYPE))
            );

        try {
            ResultSet resultSet = query.execute();
            return resultSet.allResults();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            return null;
        }
    }
    // end::query-hotels[]
	
	private List<Result> queryUser() {

        Query query = QueryBuilder
            .select(
                SelectResult.expression(Meta.id),
                SelectResult.property("name"),
                SelectResult.property("email"),
                SelectResult.property("state")
            )
            .from(DataSource.database(userDB));

        try {
            ResultSet resultSet = query.execute();
            return resultSet.allResults();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            return null;
        }
    }

}
