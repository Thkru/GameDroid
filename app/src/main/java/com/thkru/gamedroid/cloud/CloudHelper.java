package com.thkru.gamedroid.cloud;

import android.support.annotation.NonNull;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.thkru.gamedroid.data.Game;

public class CloudHelper {

    public static final String BASE = "http://thegamesdb.net";
    public static final String GAME = "/api/GetGame.php?id=";
    public static final String COVER = "/banners/_gameviewcache/boxart/original/front/";

    private static final String ID = "id";
    private static final String NAME = "GameTitle";
    private static final String RELEASE_DATE = "ReleaseDate";
    private static final String TEXT = "Overview";
    private static final String DEV = "Developer";

    private static final Set<Integer> game_ids = new HashSet<>();

    static {    //TODO read from textfile or sth
        game_ids.add(1);
        game_ids.add(2);
        game_ids.add(146);
        game_ids.add(149);
        game_ids.add(770);
        game_ids.add(800);
        game_ids.add(33631);
        game_ids.add(33641);
    }

    public static String getCoverUrlForId(int id) {
        return BASE + COVER + id + "-1.jpg";
    }

    public List<Game> getGamesFromServer() throws IOException {

        List<Game> games = new ArrayList<>();
        for (int id : game_ids) {
            games.add(doRequest(BASE + GAME + id));
        }
        return games;
    }

    private String getStringFromSteam(InputStream inputStream) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder total = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            total.append(line);
        }
        reader.close();

        return total.toString();
    }

    private Game getGameFromXmlString(InputStream inStream) throws IOException, XmlPullParserException {

        String id = null, name = null, date = null, text = null, developer = null;

        XmlPullParser parser = initParser(inStream);
        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {   //TODO refactor n make it smaller
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals(ID) && id == null) {
                    parser.next();
                    id = parser.getText();
                } else if (parser.getName().equals(NAME) && name == null) {
                    parser.next();
                    name = parser.getText();
                } else if (parser.getName().equals(RELEASE_DATE) && date == null) {
                    parser.next();
                    date = parser.getText();
                } else if (parser.getName().equals(TEXT) && text == null) {
                    parser.next();
                    text = parser.getText();
                } else if (parser.getName().equals(DEV) && developer == null) {
                    parser.next();
                    developer = parser.getText();
                }
            }
            eventType = parser.next();
        }
        return new Game(Integer.parseInt(id), name, date, text, developer);
    }

    @NonNull
    private XmlPullParser initParser(InputStream inStream) throws XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(inStream, null);
        return parser;
    }

    private Game doRequest(String url) throws IOException {

        Log.i("", "DoRequest: " + url);
        InputStream in = null;
        try {
            URLConnection urlConnection = new URL(url).openConnection();
            urlConnection.setReadTimeout(30 * 1000);
            urlConnection.setConnectTimeout(30 * 1000);
            return getGameFromXmlString(urlConnection.getInputStream());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {

            }
        }//end finally
        return null;
    }//end doRequest

}
