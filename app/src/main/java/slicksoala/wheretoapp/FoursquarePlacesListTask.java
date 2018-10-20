package slicksoala.wheretoapp;

import android.os.AsyncTask;

import java.util.ArrayList;

public class FoursquarePlacesListTask extends AsyncTask<String, Void, ArrayList<Place>> {

    //https://api.foursquare.com/v2/venues/explore?client_id=SN4XROTU4SNIPGE2B35U2FS34ZE00WESSTX2WO1SHJ3PMOBJ&client_secret=ZPO3E2HYNFKACKF1SCTVZ52ZPDG2H4NWB1AET5YS4M1FYVRD&v=20180323&limit=1&ll=40.7243,-74.0018&query=food
    final String FOURSQ_API_BASE = "https://api.foursquare.com/v2/venues/explore?";
    final String CLIENT_ID = "SN4XROTU4SNIPGE2B35U2FS34ZE00WESSTX2WO1SHJ3PMOBJ";
    final String CLIENT_SECRET = "ZPO3E2HYNFKACKF1SCTVZ52ZPDG2H4NWB1AET5YS4M1FYVRD";
    final String LOC_PAR = "&ll=";
    final String RAD_PAR = "&radius=";
    final String TYP_PAR = "&section=";
    final String CID_PAR = "&key=";


    @Override
    protected ArrayList<Place> doInBackground(String... strings) {
        return null;
    }
}
