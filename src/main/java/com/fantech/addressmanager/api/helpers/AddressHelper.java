package com.fantech.addressmanager.api.helpers;

import com.fantech.addressmanager.api.entity.common.Coordinates;
import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

@Component
public class AddressHelper {

    static private final Geocoder geocoder = new Geocoder();
    @Value("${GOOGLE_API_KEY}")
    private String apiGKey;

    public Coordinates getCoordinates(String address) throws IOException {

        URL url = new URL(String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s", address.replaceAll("[\\s+]","+"),apiGKey));
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");


        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(String.valueOf(content));
        JsonObject geo = jsonObject.getAsJsonArray("results").get(0).getAsJsonObject().getAsJsonObject("geometry").getAsJsonObject("location");
        double lat = geo.get("lat").getAsDouble();
        double lng = geo.get("lng").getAsDouble();

        return  new Coordinates(lat,lng);
    }
}
