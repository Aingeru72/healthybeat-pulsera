package com.aingerusanchez.healthybeat.pulsera;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class Pulsera {

	//start server
    private void startServer() throws IOException{

        //Create a UUID for SPP
        UUID uuid = new UUID("1101", true);
        //Create the service url
        String connectionString = "btspp://localhost:" + uuid +";name=Sample SPP Server";

        //open server url
        StreamConnectionNotifier streamConnNotifier = (StreamConnectionNotifier)Connector.open( connectionString );

        //Wait for client connection
        System.out.println("\nServer Started. Waiting for clients to connect...");
        StreamConnection connection = streamConnNotifier.acceptAndOpen();

        RemoteDevice dev = RemoteDevice.getRemoteDevice(connection);
        System.out.println("Remote device address: "+dev.getBluetoothAddress());
        System.out.println("Remote device name: "+dev.getFriendlyName(true));

        //read string from spp client
        InputStream inStream = connection.openInputStream();
        BufferedReader bReader=new BufferedReader(new InputStreamReader(inStream));
        String lineRead=bReader.readLine();
        System.out.println(lineRead);

        //send response to spp client
        OutputStream outStream = connection.openOutputStream();
        PrintWriter pWriter = new PrintWriter(new OutputStreamWriter(outStream));
        pWriter.write("Response String from SPP Server\r\n");
        pWriter.flush();

        pWriter.close();

        streamConnNotifier.close();

    }


    public static void main(String[] args) throws IOException {

        //display local device address and name
        LocalDevice localDevice = LocalDevice.getLocalDevice();
        System.out.println("Address: " + localDevice.getBluetoothAddress());
        System.out.println("Name: " + localDevice.getFriendlyName());

        Pulsera pulseraServer = new Pulsera();
        pulseraServer.startServer();

    }

}
