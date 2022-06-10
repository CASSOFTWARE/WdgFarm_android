package com.example.wdgfarm_android.utils;

import android.os.Handler;
import android.util.Log;

import com.example.wdgfarm_android.viewmodel.ScaleViewModel;

import java.io.ByteArrayOutputStream;
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
    ByteArrayOutputStream mRecvBuffer = new ByteArrayOutputStream();

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

        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            while (!isInterrupted()) // read the data sent from the server
            {
                try {
                    final byte[] buffer = new byte[1024]; // create receive buffer
                    inputStream = socket.getInputStream();
                    final int len = inputStream.read(buffer); // read the data and return the length of the data
                    scaleViewModel.isConnected.postValue(true);
                    for(int i = 0 ; i < len ; i++){
                        if(mRecvBuffer.size() == 0 ){
                            if(!(buffer[0] == 0x53 || buffer[0] == 0x55 || buffer[0] == 0x4F)){
                                break;
                            }
                        }

                        mRecvBuffer.write(buffer[i]);
                        if(buffer[i] == 0x0A){
                            if (mRecvBuffer.size() == 22) {
                                byte[] recvData = mRecvBuffer.toByteArray();
                                String data = new String(recvData, 0, 22);
                                cas22BytesProtocol.recvPacket(data, scaleViewModel);
                            }
                            mRecvBuffer.reset();
                            break;
                        }
                    }
                }catch (Exception e){
                    this.interrupt();
                    e.printStackTrace();
                }

            }
            if(socket != null) {
                scaleViewModel.isConnected.postValue(false);
                socket.close();
            }
        }
        catch (IOException e) {
            try {
                scaleViewModel.isConnected.postValue(false);
                socket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
