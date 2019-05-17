package com.example.websocketapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends AppCompatActivity
{
    TextView output;
    TextView EQUIP_STATUS;
    TextView LOCATION_ALERT;
    TextView MEORx;
    TextView POWER_SUPPLY;
    TextView UP_DOWN_CON;
    TextView timestamps;

    private OkHttpClient client;

    private final class EchoWebSocketListener extends WebSocketListener
    {
        private static final int NORMAL_CLOSURE_STATUS = 1000;
        @Override
        public void onOpen(WebSocket webSocket, Response response)
        {
            output("Connected to web-socket server");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            output("Receiving : " + text);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            output("Receiving bytes : " + bytes.hex());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            output("Closing : " + code + " / " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            output("Error : " + t.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output = (TextView) findViewById(R.id.output);

        client = new OkHttpClient();
        Request request = new Request.Builder().url("ws://192.168.43.10:3030").build();
        // in android emulator localhost/127.0.0.1 is equal to 10.0.2.2
        // in android mobile phone localhost/127.0.0.1 is equal to 192.168.X.X
        //Open cmd in windows
        //type "ipconfig" then press enter
        //find IPv4 Address. . . . . . . . . . . : 192.168.X.

        EchoWebSocketListener listener = new EchoWebSocketListener();
        WebSocket ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
    }

    private void output(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                output.setText(output.getText().toString() + "\n\n" + txt);
            }
        });
    }
}
