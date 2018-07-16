package ch.hevs.fbonvin.disasterassistance.views;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ch.hevs.fbonvin.disasterassistance.R;
import ch.hevs.fbonvin.disasterassistance.models.Message;

import static ch.hevs.fbonvin.disasterassistance.Constant.CURRENT_DEVICE_LOCATION;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGES_RECEIVED;
import static ch.hevs.fbonvin.disasterassistance.Constant.TAG;

public class FragMap extends Fragment {

    private MapView mMapView;
    private GoogleMap mMap;

    //TODO default zoom
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        initMap();


        return view;
    }

    private void addMessagesMarkers(){

        BitmapDescriptor iconVictim = drawIcon(R.drawable.ic__category_victim, R.color.category_victim);
        BitmapDescriptor iconDanger = drawIcon(R.drawable.ic__category_danger, R.color.category_danger);
        BitmapDescriptor iconResource = drawIcon(R.drawable.ic__category_resource, R.color.category_resource);
        BitmapDescriptor iconCaretaker = drawIcon(R.drawable.ic__category_caretaker, R.color.category_caretaker);

        final String catVictim = getResources().getString(R.string.category_Victims);
        final String catDanger = getResources().getString(R.string.category_Danger);
        final String catResource = getResources().getString(R.string.category_Resources);
        final String cateCaretaker = getResources().getString(R.string.category_Caretaker);

        if(MESSAGES_RECEIVED.size() > 0){
            for (Message m : MESSAGES_RECEIVED){
                LatLng latLng = new LatLng(
                        m.getMessageLatitude(),
                        m.getMessageLongitude());

                //TODO zoom to see the radius defined in settings
                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title(m.getTitle());

                if(m.getCategory().equals(catVictim)){
                    options.icon(iconVictim);
                }else if(m.getCategory().equals(catDanger)){
                    options.icon(iconDanger);
                }else if(m.getCategory().equals(catResource)){
                    options.icon(iconResource);
                }else if(m.getCategory().equals(cateCaretaker)){
                    options.icon(iconCaretaker);
                }

                mMap.addMarker(options);
            }
        }
    }


    private BitmapDescriptor drawIcon(int idRes, int idColor){

        Drawable drawable = getResources().getDrawable(idRes);

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);


        Canvas canvas = new Canvas();
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        Paint paint = new Paint();
        ColorFilter filter = new PorterDuffColorFilter(ContextCompat.getColor(getContext(), idColor), PorterDuff.Mode.SRC_IN);
        paint.setColorFilter(filter);


        canvas.drawBitmap(bitmap, 0,0, paint);


        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void moveCamera(LatLng latLng, float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void initMap(){

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //TODO is a toast needed?
                Log.i(TAG, "onMapReady: map ready");

                mMap = googleMap;
                mMap.setMyLocationEnabled(true);

                try{
                    double lat = CURRENT_DEVICE_LOCATION.getLatitude();
                    double lng = CURRENT_DEVICE_LOCATION.getLongitude();

                    moveCamera(new LatLng(lat, lng), 15);

                    addMessagesMarkers();
                } catch (Exception e){
                    Log.e(TAG, "onMapReady: ", e);
                }

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}