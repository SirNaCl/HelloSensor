package com.example.em3586ge;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import static android.content.Context.SENSOR_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompassFragment extends Fragment implements SensorEventListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final float ALPHA = 0.25f;
    protected float[] magSensorVals;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CompassFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CompassFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompassFragment newInstance() {
        CompassFragment fragment = new CompassFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    // device sensor manager
    private SensorManager sensorManager;
    // define the compass picture that will be use
    private ImageView compassImageView;
    // record the angle turned of the compass picture
    private float DegreeStart = 0f;
    TextView headingTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        // to stop the listener and save battery
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onResume() {
        super.onResume();
        // code for system's orientation sensor registered listeners
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compass, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        compassImageView = (ImageView) getView().findViewById(R.id.compassImageView);
        // TextView that will display the degree
        headingTextView = (TextView) getView().findViewById(R.id.headingTextView);
        // initialize your android device sensor capabilities
        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magSensorVals = lowPass(event.values.clone(), magSensorVals);
        }

        if(magSensorVals != null){
            // get angle around the z-axis rotated
            float degree = normDegrees(magSensorVals[0]);
            headingTextView.setText("Heading: " + String.format("%,.1f", degree) + " degrees");
            // rotation animation - reverse turn degree degrees

            if(degree <= 15f || 345f <= degree){
                compassImageView.setImageResource(R.drawable.compass_north);
            }else{
                compassImageView.setImageResource(R.drawable.pngitem_724367);
            }

            RotateAnimation ra = new RotateAnimation(
                    DegreeStart,
                    -degree,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            // set the compass animation after the end of the reservation status
            ra.setFillAfter(true);
            // set how long the animation for the compass image will take place
            ra.setDuration(210);
            // Start animation of compass image
            compassImageView.startAnimation(ra);
            DegreeStart = -degree;
        }


    }

    private float normDegrees(float val){
        val %= 360f;
        if(val < 0f){
            val += 360f;
        }

        return val;
    }

    protected float[] lowPass( float[] input, float[] output ) {
        if ( output == null ) return input;

        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}