package ch.hevs.fbonvin.disasterassistance.views.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ch.hevs.fbonvin.disasterassistance.R;
import ch.hevs.fbonvin.disasterassistance.models.Message;
import ch.hevs.fbonvin.disasterassistance.utils.MessagesManagement;
import ch.hevs.fbonvin.disasterassistance.views.activities.ActivityMessageDetails;

import static ch.hevs.fbonvin.disasterassistance.Constant.CURRENT_DEVICE_LOCATION;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGES_DISPLAYED;
import static ch.hevs.fbonvin.disasterassistance.Constant.TAG;
import static ch.hevs.fbonvin.disasterassistance.Constant.VALUE_PREF_RADIUS_GEO_FENCING;

public class FragMap extends Fragment implements GoogleMap.OnMarkerClickListener{

    private MapView mMapView;
    private GoogleMap mMap;


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

        MessagesManagement.updateDisplayedMessagesList();
        if(MESSAGES_DISPLAYED.size() > 0){

            for (Message m : MESSAGES_DISPLAYED){
                LatLng latLng = new LatLng(
                        m.getMessageLatitude(),
                        m.getMessageLongitude());


                MarkerOptions options = new MarkerOptions()
                        .position(latLng);

                if(m.getCategory().equals(catVictim)){
                    options.icon(iconVictim);
                }else if(m.getCategory().equals(catDanger)){
                    options.icon(iconDanger);
                }else if(m.getCategory().equals(catResource)){
                    options.icon(iconResource);
                }else if(m.getCategory().equals(cateCaretaker)){
                    options.icon(iconCaretaker);
                }

                Marker marker = mMap.addMarker(options);
                marker.setTag(m.getDateCreatedMillis() + "_" + m.getTitle());
            }


            mMap.setOnMarkerClickListener(FragMap.this);
        }
    }

    private void moveCamera(){

        if(CURRENT_DEVICE_LOCATION != null){

            double earthRadius = 6371;

            double radius = Double.valueOf(VALUE_PREF_RADIUS_GEO_FENCING);
            radius = (radius + radius/3)/1000;

            double currentLat = CURRENT_DEVICE_LOCATION.getLatitude();
            double currentLng = CURRENT_DEVICE_LOCATION.getLongitude();


            double maxLat = currentLat + (radius / earthRadius) * (180 / Math.PI);
            double maxLng = currentLng + (radius / earthRadius) * (180 / Math.PI) / Math.cos(maxLat * Math.PI/180);

            double minLat = currentLat - (radius / earthRadius) * (180 / Math.PI);
            double minLng = currentLng - (radius / earthRadius) * (180 / Math.PI) / Math.cos(minLat * Math.PI/180);


            Log.i(TAG, "moveCamera: radius " + radius + " maxLat " + maxLat + " minLat " + minLat + " maxLng " + maxLng + " minLng  " + minLng);

            LatLng maxLatLng = new LatLng(maxLat, maxLng);
            LatLng minLatLng = new LatLng(minLat, minLng);


            LatLngBounds bounds = new LatLngBounds(minLatLng, maxLatLng);

            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));

        }


    }

    private void initMap(){

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressWarnings("unused")
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap googleMap) {

                mMap = googleMap;
                mMap.setMyLocationEnabled(true);

                try{
                    final double lat = CURRENT_DEVICE_LOCATION.getLatitude();
                    double lng = CURRENT_DEVICE_LOCATION.getLongitude();

                    CircleOptions circleOptions = new CircleOptions()
                            .center(new LatLng(lat, lng))
                            .radius(Integer.valueOf(VALUE_PREF_RADIUS_GEO_FENCING))
                            .fillColor(Color.argb(40, 71, 100, 100))
                            .strokeColor(R.color.secondaryColor)
                            .strokeWidth(5);

                    mMap.addCircle(circleOptions);

                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {

                            ConstraintLayout layout = FragMap.this.getActivity().findViewById(R.id.layout_map_details);

                            if(layout.getVisibility() == View.VISIBLE){
                                layout.setVisibility(View.GONE);
                            }
                        }
                    });
                    moveCamera();
                    addMessagesMarkers();
                } catch (Exception e){
                    Log.e(TAG, "onMapReady: ", e);
                }

            }
        });

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

    @Override
    public boolean onMarkerClick(Marker marker) {

        Log.i(TAG, "onMarkerClick: TAG " + marker.getTag());

        Message display = new Message();

        String timestamp = ((String) marker.getTag()).split("_")[0];
        String title = ((String) marker.getTag()).split("_")[1];

        for(Message m : MESSAGES_DISPLAYED){
            if(m.getDateCreatedMillis().equals(timestamp) && m.getTitle().equals(title)){
                display = m;
            }
        }

        if(!display.getTitle().equals("")){
            final Message m = display;
            ConstraintLayout layout = FragMap.this.getActivity().findViewById(R.id.layout_map_details);

            TextView tvTitle = layout.findViewById(R.id.tv_map_detail_title);
            TextView tvSender = layout.findViewById(R.id.tv_map_detail_sender);
            TextView tvDate = layout.findViewById(R.id.tv_map_details_date);
            TextView tvDistance = layout.findViewById(R.id.tv_map_details_distance);
            Button btDetails = layout.findViewById(R.id.bt_map_detail);

            tvTitle.setText(display.getTitle());
            tvTitle.setTextColor(getColorForCategory(display));

            String sender = getString(R.string.activity_message_detail_send_by, display.getCreatorUserName());
            tvSender.setText(sender);

            Long dateLong = Long.parseLong(display.getDateCreatedMillis());

            String dateString = DateUtils.getRelativeTimeSpanString(dateLong).toString();

            String dateDisplay = getString(R.string.activity_message_detail_send_time, dateString);
            tvDate.setText(dateDisplay);

            String distance = getString(R.string.activity_message_detail_distance, String.valueOf(display.getDistance()));
            tvDistance.setText(distance);


            btDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(FragMap.this.getActivity(), ActivityMessageDetails.class);
                    intent.putExtra("message", m);
                    startActivity(intent);
                }
            });

            layout.setVisibility(View.VISIBLE);
        }

        return false;
    }

    /**
     * Return the integer of the color corresponding to the category of the message
     * @return integer of the color stored int R.Color
     */
    private int getColorForCategory(Message message) {
        if (this.getString(R.string.category_Victims).equals(message.getCategory())) {
            return this.getResources().getColor(R.color.category_victim);
        } else if (this.getString(R.string.category_Danger).equals(message.getCategory())) {
            return this.getResources().getColor(R.color.category_danger);
        } else if (this.getString(R.string.category_Resources).equals(message.getCategory())) {
            return this.getResources().getColor(R.color.category_resource);
        } else if (this.getString(R.string.category_Caretaker).equals(message.getCategory())) {
            return this.getResources().getColor(R.color.category_caretaker);
        }
        return 0;
    }
}