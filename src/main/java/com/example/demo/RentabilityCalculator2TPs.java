package com.example.demo;

import java.util.Random;

public class RentabilityCalculator2TPs {
    public static void main(String[] args) {    /* main */

        /* ⬇️CONFIGURAR⬇️ */
        // config estrategia
        double capitalInicial = 500.0;
        final double TP1 = 0.3709; // TP1 CON 0.50% DE RIESGO ES: 0.18545. para el 1% es 0.3709.
        final double TP2 = 2.132; // TP2 CON 0.50% DE RIESGO ES: 1.0751, TP2 CON 1.00% DE RIESGO ES: 2.132
        final double SL = 1; // Porcentaje de stop loss 0.50% DE RIESGO ES: 0.50, SL CON 1.00% DE RIESGO ES: 1
        final double probabilidadAciertosTP1 = 23.222; // mi acierto: probabilidadAciertosTP1 = 22.21;
        final double probabilidadAciertosTP2 = 27.778;    // mi acierto: probabilidadAciertosTP2 = 27.75;
        final double comisiones = 0.0;
        final double stopGestionDeRiesgo = 50; // si el capital inicial llega a un porcentaje menor a este se deja de operar


        // config pruebas
        int cantidadDeCuentasParaTestear = 10;
        int cantidadDeTradesPorCuenta = 500;//500  //10000   //2500


        // mi acierto: probabilidadAciertosTP1 = 22.21;
        // mi acierto: probabilidadAciertosTP2 = 27.75;






        //llamado a funciones
        header();
        testearRentabilidadDeCuentas(cantidadDeCuentasParaTestear, capitalInicial,
                cantidadDeTradesPorCuenta, probabilidadAciertosTP1, probabilidadAciertosTP2, TP1, TP2, SL, comisiones, stopGestionDeRiesgo);
    }


    /* =========FUNCIONES============== */
    public static void testearRentabilidadDeCuentas(int cantidadDeCuentasParaTestear,
                                                    double capitalInicial, int cantidadDeTradesPorCuenta,
                                                    double probabilidadAciertosTP1, double probabilidadAciertosTP2,
                                                    double TP1, double TP2, double SL, double comisiones, double stopGestionDeRiesgo) {
        double promedioEfectividadEstrategia = 0.0;
        double acumDineroFinal = 0.0;

        boolean detenerEjecucion = false; //por sl gestion de riesgo, osea cueta quemada
        int cantCuentasQuedamas = 0; // cantidad de cuentas quemadas


        for (int i = 1; i <= cantidadDeCuentasParaTestear; i++) { //Recorre Cuentas
            double capitalActual = capitalInicial;
            double porcentajeSLgestion = ((stopGestionDeRiesgo * capitalInicial) / 100); //calculo SL por Gestion
            porcentajeSLgestion = capitalInicial - porcentajeSLgestion;
            boolean cuentaQuemada = false;// variable local que se resetea y contamos las cuentas quemadas

            for (int j = 0; j < cantidadDeTradesPorCuenta; j++) { //Recorre Trades por cuenta
                int resultadoTrade = tradeRealizado(probabilidadAciertosTP1, probabilidadAciertosTP2);

                //Descontar comisiones antes del trade
                capitalActual = capitalActual - ((comisiones * capitalActual) / 100);

                switch (resultadoTrade){
                    case 1:
                        //incrementar capital con beneficio de tp1
                        capitalActual = capitalActual + ((TP1 * capitalActual) / 100);
                        break;
                    case 2:
                        //incrementar capital con beneficio de tp2
                        capitalActual = capitalActual + ((TP2 * capitalActual) / 100);
                        break;
                    case 3:
                        //incrementar capital con stop
                        capitalActual = capitalActual - ((SL * capitalActual) / 100);
                        break;
                }

                //preguntar si el capital actual esta por debajo del stopGestionDeRiesgo
                if(capitalActual <= porcentajeSLgestion){
                    detenerEjecucion = true;
                    cuentaQuemada = true;
                }
            }

            //condicional para contar cuentas quemadas
            if(cuentaQuemada){
                cantCuentasQuedamas = cantCuentasQuedamas + 1;
            }

            if(capitalActual >= capitalInicial){
                //System.out.printf("\t✅Capital: $ %.2f",capitalActual);
                //System.out.println("  Cuenta Nro. "+ i);
            }else{
                //System.out.printf("\t❌Capital: $ %.2f",capitalActual);
                //System.out.println("  Cuenta Nro. "+ i);
            }
            acumDineroFinal = acumDineroFinal + capitalActual;
        }


        promedioEfectividadEstrategia = acumDineroFinal / cantidadDeCuentasParaTestear;
        System.out.printf("\t Promedio: $ %.2f\n",promedioEfectividadEstrategia);
        //Funcion con resultados finales de la strategia
        resultadoFinal(promedioEfectividadEstrategia, capitalInicial, cantidadDeTradesPorCuenta, detenerEjecucion, cantCuentasQuedamas, cantidadDeCuentasParaTestear);
    }

    //funcion para correr bucle con pruebas de acierto
    public static int tradeRealizado(double probabilidadAciertosTP1, double probabilidadAciertosTP2) {
        Random random = new Random();
        int tradeAcertado = 0;
        double numRandom = 0.0;

        numRandom = random.nextDouble(0,100); // numeros aleatorios desde el 0 al 100

        if(numRandom <= probabilidadAciertosTP1){
            tradeAcertado = 1;
        } else if (numRandom <= (probabilidadAciertosTP1 + probabilidadAciertosTP2)) {
            tradeAcertado = 2;
        } else{
            tradeAcertado = 3;
        }

        // Si retorna True es decir que el trade fue exitoso, sino lo contrario
        return tradeAcertado;
    }

    public static void resultadoFinal(double promedioEfectividadEstrategia, double capitalInicial,
                                      int cantTrades,boolean detenerEjecucion, int cantCuentasQuedamas, int cantidadDeCuentasParaTestear) {

        //Variables para obtener el porcentaje de rentabilidad
        double sobrante = promedioEfectividadEstrategia - capitalInicial;
        double porcentajeDeRentabilidad = (sobrante / capitalInicial) * 100;

        if(promedioEfectividadEstrategia > capitalInicial){
            System.out.print("\n\t✅✅✅ La estrategia es Rentable! ✅✅✅ con un:\n\t % ");
            System.out.printf("%.2f", porcentajeDeRentabilidad);
            System.out.print(" Porciento de Rentabilidad en "+cantTrades+" Trades\n");
            System.out.printf("\tPromedio de Ganancia: $ %.2f\n", sobrante);
        }
        else if (promedioEfectividadEstrategia == capitalInicial) {
            System.out.println("\n\tLa estrategia DA Exactamente una Rentabilidad IGUAL");
        }
        else {
            System.out.print("\n\t❌❌❌ La estrategia No es Rentable ❌❌❌ con un:\n\t % ");
            System.out.printf("%.2f", porcentajeDeRentabilidad);
            System.out.print(" Porciento de Rentabilidad Negativa en "+cantTrades+" Trades\n");
            System.out.printf("\tPromedio de Perdida: $ %.2f\n", sobrante);
        }

        if(detenerEjecucion){
            System.out.println("\n\t❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌");
            System.out.println("\t❌❌ STOP POR GESTION ALCANZADO ❌❌");
            System.out.println("\t❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌\n");
        }else{
            System.out.println("\n\t✅✅✅✅✅✅✅✅✅✅✅✅✅✅✅✅");
            System.out.println("\t✅✅ ESTRATEGIA 100% EFECTIVA ✅✅");
            System.out.println("\t✅✅✅✅✅✅✅✅✅✅✅✅✅✅✅✅\n");
        }

        System.out.println("⚡ Cantidad de Cuentas Quemadas -> " + cantCuentasQuedamas);
        double diff = 0.0; // sobrante
        double probabilidadDeEstrategia = 0.0;
        diff = cantidadDeCuentasParaTestear - cantCuentasQuedamas;// obtener sobrante
        probabilidadDeEstrategia = (((diff / cantidadDeCuentasParaTestear) * 100));

        System.out.println(" Probabilidad de Estrategia : %"+ probabilidadDeEstrategia);

    }

    public static void header(){
        System.out.println("\n\t=========================================");
        System.out.println("\t=========== TEST RENTABILIDAD ===========");
        System.out.println("\t=========================================\n");
    }
}
