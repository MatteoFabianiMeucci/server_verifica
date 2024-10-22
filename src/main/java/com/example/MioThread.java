package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Random;

import javax.print.DocFlavor.STRING;

public class MioThread extends Thread{
    Socket s;
    int numero;
    int contatore;

    public MioThread(Socket s, int numero){
        this.s = s;
        this.numero = numero;
        this.contatore = 0;
    }

    public int generaNuovoNumero(){
        Random r = new Random();
        return r.nextInt(100);
    }

    public void run(){
        try (BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()))) {
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            String stringRicevuta;
            boolean acceso = true;

            do{
                stringRicevuta = in.readLine();
                try{
                    int num = Integer.parseInt(stringRicevuta);

                    if(num >= 0 && num<= 100){
                        if (num < numero){
                            out.writeBytes("<\n");
                            contatore++;
                        }

                        if (num > numero) {
                            out.writeBytes(">\n");
                            contatore++;
                        }

                        if (num == numero) {
                            contatore++;
                            out.writeBytes("=\n");
                            out.writeBytes(contatore + "\n");
                        }
                    } 

                    if (stringRicevuta.equals("y")) {
                        System.out.println("entra");
                        numero = generaNuovoNumero();
                        contatore = 0;
                    }

                    if (stringRicevuta.equals("n")) {
                        acceso = false;
                    }

                } catch (Exception e){
                    out.writeBytes("!\n");
                }
            } while(acceso);
            s.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}