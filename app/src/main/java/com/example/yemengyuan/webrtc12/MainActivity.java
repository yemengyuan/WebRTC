package com.example.yemengyuan.webrtc12;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoCapturerAndroid;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoRendererGui;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;


public class MainActivity extends AppCompatActivity {
    private GLSurfaceView glSurfaceView;
    private VideoSource localVideoSource;
    private VideoTrack localVideoTrack;
    private AudioSource localAudioSource;
    private AudioTrack localAudioTrack;
    private MediaConstraints defaultMediaConstrains;
    private VideoRenderer renderer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        defaultMediaConstrains = new MediaConstraints();
        defaultMediaConstrains.mandatory.add(new MediaConstraints.KeyValuePair("maxWidth", "1280"));
        defaultMediaConstrains.mandatory.add(new MediaConstraints.KeyValuePair("maxHeight", "720"));
        defaultMediaConstrains.mandatory.add(new MediaConstraints.KeyValuePair("minWidth", "640"));
        defaultMediaConstrains.mandatory.add(new MediaConstraints.KeyValuePair("minHeight", "480"));

        /* Create core pcFactory */
        PeerConnectionFactory.initializeAndroidGlobals(
                this,  // Context
                true,  // Audio Enabled
                true,  // Video Enabled
                true,  // Hardware Acceleration Enabled
                false); // Render Context
        PeerConnectionFactory pcFactory = new PeerConnectionFactory();

        /* get camera and audio resource */
        String frontCam = VideoCapturerAndroid.getNameOfFrontFacingDevice();
        //回傳前置相機名稱
        VideoCapturer videoCapturer = VideoCapturer.create(frontCam);
        //建立VideoCapture用以生成VideoSource
        localVideoSource = pcFactory.createVideoSource(
                videoCapturer, // VideoCapturer we created last step
                defaultMediaConstrains); // MediaConstrain
        localVideoTrack = pcFactory.createVideoTrack(
                "VidoTrack", // ID of this VideoTrack
                localVideoSource); // VideoSource we want
        localAudioSource = pcFactory.createAudioSource(
                defaultMediaConstrains);
        localAudioTrack = pcFactory.createAudioTrack(
                "AudioTrack", //ID of this AudioTrack
                localAudioSource); // AudioSource we created


        glSurfaceView =(GLSurfaceView)findViewById(R.id.gl_surface);
        VideoRendererGui.setView(glSurfaceView, null);
        try{renderer = VideoRendererGui.createGui(
                72, // renderer左上角x座標 >=0 <=100
                65, // renderer左上角y座標 >=0 <=100
                25, // width >=0 <=100 +x座標 <=100
                25, // height >=0 <=100 +y座標 <=100
                VideoRendererGui.ScalingType.SCALE_ASPECT_FILL,
                true);}
        catch (Exception e){}
        localVideoTrack.addRenderer(renderer);

        MediaStream mediaStream = pcFactory.createLocalMediaStream("localMediaStream");
        mediaStream.addTrack(localVideoTrack);
        mediaStream.addTrack(localAudioTrack);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
