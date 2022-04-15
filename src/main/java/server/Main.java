package server;

import com.google.gson.Gson;
import work.Jinfo;
import java.net.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Base base=new Base();
        base.writeToBase();
        int PORT=23456;
        try {
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("Server started!");
            String json3;
            boolean f=true;
            while(f) {
                Socket socket = server.accept(); // accepting a new client
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                String json2 = input.readUTF(); // reading a message
                Gson gson=new Gson();
                Jinfo jinfo2 =new Jinfo();
                jinfo2=gson.fromJson(json2, Jinfo.class);
                String msg=jinfo2.type+" "+jinfo2.key+" "+ jinfo2.value;
                String[] words = msg.split(" ");
                jinfo2.key=null;
                jinfo2.value=null;
                jinfo2.type=null;
                jinfo2.reason=null;
                jinfo2.reason=null;
                if (!words[0].equals("exit")) {
                    if (words[0].equals("set")) {
                        String b = words[1];
                        int c = words.length;
                        String h = words[2];
                        for (int i = 3; i < c; i++) {
                            h = h + " " + words[i];
                        }
                        jinfo2.response=base.set(b, h);
                        base.update();
                    }
                    if (words[0].equals("get")) {
                        String b = words[1];
                        if(base.get(b).equals("ERROR")) {
                            jinfo2.response=base.get(b);
                            jinfo2.reason="No such key";
                        }
                        else {
                            jinfo2.response="OK";
                            jinfo2.value=base.get(b);
                        }
                    }
                    if (words[0].equals("delete")) {
                        String b = words[1];
                        jinfo2.response= base.delete(b);
                        if(!jinfo2.response.equals("OK")) {
                            jinfo2.reason="No such key";
                        }
                        base.update();
                    }
                    json3=gson.toJson(jinfo2);
                    output.writeUTF(json3); // resend it to the client

                } else {
                    jinfo2.response="OK";
                    json3=gson.toJson(jinfo2);
                    output.writeUTF(json3); // resend it to the client
                    server.close();
                    f = false;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
