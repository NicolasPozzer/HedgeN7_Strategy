package com.example.demo;

import java.util.Random;

public class HedgeN7_Strategy {


    public static void main(String[] args) {
        int acum = 0;
        Double promedio = 0.0;

        for(int y = 0; y < 100; y++) {


        /* CONFIGURAR */
        Double porcentajeSL = 0.005; // Stop Loss 0.01 = 1.00%
        Double porcentajeTP = 0.01; // Take Profit
        Double cierreTotal = 0.05; // Cierre por Gestion de Riesgo %5.0
        int cantTrades = 10000;
        int tasaDePerdidas = 47; //por ciento
        int probabilidad_Cierre_por_gestion = 4;//por ciento de probabilidad de PERDIDA DEL 5% POR GESTION
        /* CONFIGURAR */





        // Generar un número aleatorio entre 0 y 100
        Random random = new Random();
        Double capitalInicial = 10000.0;
        int cant_cuentas_de_prueba = 100;
        int cuentasRentables = 0;
        int restaProbGestionConTasaGanancia = 100 - probabilidad_Cierre_por_gestion;

        //100 cuentas de prueba con 10mil operaciones cada una
        for (int x = 0; x < cant_cuentas_de_prueba; x++) {
            Double capInicial = capitalInicial;

            for(int i = 0; i < cantTrades; i++){
                int aleatorio = random.nextInt(100) + 1; // Número aleatorio entre 1 y 100

                if (aleatorio <= tasaDePerdidas) { // probabilidad de perder
                    capInicial = capInicial - (capInicial * porcentajeSL);
                }
                else if (aleatorio <= restaProbGestionConTasaGanancia) { // probabilidad de ganar
                    capInicial = capInicial + (capInicial * porcentajeTP);
                }
                else { // probabilidad de 5%
                    capInicial = capInicial - (capInicial * cierreTotal);
                }
            }


            if(capInicial > capitalInicial){
                //System.out.println("GANASTE: " + capInicial);
                cuentasRentables = cuentasRentables + 1;
            }else{
                //System.out.println("-----PERDISTE-----: " + capInicial);
            }
        }

        acum = acum + cuentasRentables;
        }

        promedio = acum / 100.0;

        System.out.println("%"+promedio);

    }
}