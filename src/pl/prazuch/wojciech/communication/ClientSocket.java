/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.prazuch.wojciech.communication;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class ClientSocket {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    DataFromClient dataFromClient;

    DataToClient dataToClient;

    public ClientSocket() throws IOException {
        socket = new Socket("localhost", 4444);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
        dataFromClient = new DataFromClient();
        dataToClient = new DataToClient();
    }

    public static void main(String[] args)
    {

        ClientSocket cs = null;
        try {
            cs = new ClientSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cs.run();


    }


    public void run() {
        String text;

        try {
            while((text = in.readLine()) != null) {

                dataToClient.setDataToClient(text);


                out.println(dataFromClient.getDataFromClient());

            }
        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {

            }
        }
    }

    public void setDataFromClient(DataFromClient dataFromClient) {
        this.dataFromClient = dataFromClient;
        out.println(this.dataFromClient.getDataFromClient());

    }

    public DataToClient receiveDataToClient() {
        String text;
        System.out.println("receiveDataToClient outside try");
        try {
            if((text = in.readLine()) != null)
            {
                System.out.println("receiveDataToClient");
                dataToClient.setDataToClient(text);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataToClient;

    }
}
