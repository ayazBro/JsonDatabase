package client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import work.Jinfo;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    @Parameter(names={"-t"})
    String prikaz;
    @Parameter(names={"-k"})
    String number;
    @Parameter(names={"-v"})
    String text;
    @Parameter(names={"-in"})
    String fileJson;

    public static void main(String[] args) {
        Main main = new Main();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(args);

        main.run();
    }
    public void run () {
        String address = "127.0.0.1";
        int port = 23456;
        try {
            System.out.println("Client started!");
            Socket socket = new Socket(address, port);
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            String json1;
            Jinfo jinfo=new Jinfo();
            Gson gson1 = new Gson();
            if (text != null) {
                jinfo.type=prikaz;
                jinfo.key=number;
                jinfo.value=text;
                json1=gson1.toJson(jinfo);
                output.writeUTF(json1);
                System.out.println("Sent: "+json1);
            }
            else {
                if(prikaz!=null) {
                    jinfo.type = prikaz;
                    jinfo.key = String.valueOf(number);
                    json1 = gson1.toJson(jinfo);
                    output.writeUTF(json1);
                    System.out.println("Sent: " + json1);
                }

                if(fileJson!=null) {
                    Path path = Paths.get("src/main/java/client/data/"+fileJson);
                    byte[] bytes = null;
                    try {
                        bytes = Files.readAllBytes(path);
                    } catch (IOException ex) {
                        System.out.println("Can not find file");
                    }
                    json1=new String(bytes, StandardCharsets.UTF_8);
                    output.writeUTF(json1);
                    System.out.println("Sent: " + json1);
                }
            }
            String receivedMsg = input.readUTF(); // response message
            System.out.println("Received: " + receivedMsg);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}