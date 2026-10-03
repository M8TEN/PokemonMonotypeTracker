module Client.GUI {
    requires javafx.controls;
    requires java.sql;
    requires com.google.gson;
    requires java.net.http;
    requires java.desktop;

    opens Client.GUI;
    opens Client.GUI.View;
    opens Client.GUI.Controller to java.sql;
    opens Shared.DTOs to com.google.gson;
    opens Shared to com.google.gson;
    opens Battle.Moves to com.google.gson;
    opens Battle to com.google.gson;
    opens RunManagement to com.google.gson;
}