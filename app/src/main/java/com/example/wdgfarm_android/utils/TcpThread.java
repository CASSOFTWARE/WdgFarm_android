package com.example.wdgfarm_android.utils;

import android.os.Handler;
import android.util.Log;

import com.example.wdgfarm_android.viewmodel.ScaleViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class TcpThread extends Thread {
    Socket socket;
    String ip;
    int port;
    OutputStream outputStream;
    InputStream inputStream;
    ScaleViewModel scaleViewModel;
    String data;

    private Cas22BytesProtocol cas22BytesProtocol = new Cas22BytesProtocol();


    public void TcpThread(String ip, int port, ScaleViewModel scaleViewModel){
        this.ip = ip;
        this.port = port;
        this.scaleViewModel = scaleViewModel;
    }


    public void run(){

        try {
            socket = new Socket (ip, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            outputStream = socket.getOutputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            while (!isInterrupted()) // read the data sent from the server
            {
                final byte [] buffer = new byte [1024]; // create receive buffer
                inputStream = socket.getInputStream();
                final int len = inputStream.read(buffer); // read the data and return the length of the data
                if(len>0)
                {
                    if(new String(buffer, 0, len).startsWith("ST") || new String(buffer, 0, len).startsWith("US")){
                        data = new String(buffer, 0, len);
                    }else{
                        data += new String(buffer, 0, len);
                        cas22BytesProtocol.recvPacket(data, scaleViewModel);
                        data = "";
                    }
                }
            }
            socket.close();
        }
        catch (IOException e) {
            try {
                socket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
