package com.umg.covid19;

import scheduler.CovidScheduler;

public class Main {

    public static void main(String[] args) {
        System.out.println("Cargando espere 15 segundos");

        // Crear un nuevo hilo que espere 15 segundos antes de ejecutar el código
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    
                    Thread.sleep(15000);

                    // Llamar a la clase CovidScheduler para ejecutar la lógica principal
                    CovidScheduler.executeMainLogic();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Iniciar el hilo
        thread.start();
    }
}
