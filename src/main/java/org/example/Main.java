package org.example;

import presentacion.controlador.ControladorPrincipal;

public class Main {
    public static void main(String[] args) {
        System.out.printf("Hello and welcome!");
        int num = 1;
        System.out.println(num);

        ControladorPrincipal controladorPrincipal = new ControladorPrincipal();
        controladorPrincipal.iniciarVentanaPrincipal();



    }
}