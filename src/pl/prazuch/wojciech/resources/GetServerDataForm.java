package pl.prazuch.wojciech.resources;

import javafx.fxml.FXML;

import java.awt.*;

/**
 * Created by wojciechprazuch on 17.12.2017.
 */
public class GetServerDataForm {

    @FXML
    TextField server;

    @FXML
    TextField port;


    private String server;
    private String port;

    public void getServerInfo()
    {



    }


    public String getLocalhost() {
        return server;
    }

    public void setLocalhost(String server) {
        this.server = server;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
