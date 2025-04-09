package com.example.demo;

import java.util.Random;

public class RentabilityCalculator2TPs {
    public static void main(String[] args) {    /* main */

        /* ⬇️CONFIGURAR⬇️ */
        // config estrategia
        double capitalInicial = 1000;
        final double TP1 = 0.18545; // TP1 CON 0.50% DE RIESGO ES: 0.18545. para el 1% es 0.3709.
        final double TP2 = 1.0751; // TP2 CON 0.50% DE RIESGO ES: 1.0751, TP2 CON 1.00% DE RIESGO ES: 2.132
        final double SL = 0.50; // Porcentaje de stop loss 0.50% DE RIESGO ES: 0.50, SL CON 1.00% DE RIESGO ES: 1
        final double probabilidadAciertosTP1 = 27.1186;
        final double probabilidadAciertosTP2 = 27.1186;
        final double comisiones = 0.0;


        /* (ACIERTO MINIMO NICO ALCANZADO!!)
        final double probabilidadAciertosTP1 = 29.0909;
        final double probabilidadAciertosTP2 = 23.6364;
        * */

        /* ACIERTO PERFECTO NICO(uno de mis aciertos alcanzados en racha alta)
        final double probabilidadAciertosTP1 = 27.0270;
        final double probabilidadAciertosTP2 = 29.7297;
        acierto total -> %56.75
        * */


        // config pruebas
        int cantidadDeCuentasParaTestear = 10000;
        int cantidadDeTradesPorCuenta = 10000;      //422 mes  //10000   //2500


        // mi acierto: probabilidadAciertosTP1 = 29.0323;
        // mi acierto: probabilidadAciertosTP2 = 25.8065;






        //llamado a funciones
        header();
        testearRentabilidadDeCuentas(cantidadDeCuentasParaTestear, capitalInicial, cantidadDeTradesPorCuenta,
                probabilidadAciertosTP1, probabilidadAciertosTP2, TP1, TP2, SL, comisiones);
    }


    /* =========FUNCIONES============== */
    public static void testearRentabilidadDeCuentas(int cantidadDeCuentasParaTestear,
                                                    double capitalInicial, int cantidadDeTradesPorCuenta,
                                                    double probabilidadAciertosTP1, double probabilidadAciertosTP2, double TP1,
                                                    double TP2, double SL, double comisiones) {
        double promedioEfectividadEstrategia = 0.0;
        double acumDineroFinal = 0.0;

        boolean detenerEjecucion = false; //por sl gestion de riesgo, osea cueta quemada
        int cantCuentas8Alcanzadas = 0;
        int cantCuentas10Alcanzadas = 0;
        int cantCuentas20Alcanzadas = 0; // cantidad de cuentas quemadas
        int cantCuentas30Alcanzadas = 0; // cantidad de cuentas quemadas
        int cantCuentas40Alcanzadas = 0; // cantidad de cuentas quemadas
        int cantCuentas50Alcanzadas = 0; // cantidad de cuentas quemadas

        int cantidadTradesParaSuperarDrawDown = 0;

        for (int i = 1; i <= cantidadDeCuentasParaTestear; i++) { //Recorre Cuentas
            double capitalActual = capitalInicial;

            double ochoPorciento = ((8.0 * capitalInicial) / 100); //calculo SL por Gestion
            ochoPorciento = capitalInicial - ochoPorciento;

            double diezPorciento = ((10.0 * capitalInicial) / 100); //calculo SL por Gestion
            diezPorciento = capitalInicial - diezPorciento;

            double veintePorciento = ((20.0 * capitalInicial) / 100); //calculo SL por Gestion
            veintePorciento = capitalInicial - veintePorciento;

            double treintaPorciento = ((30.0 * capitalInicial) / 100); //calculo SL por Gestion
            treintaPorciento = capitalInicial - treintaPorciento;

            double cuarentaPorciento = ((40.0 * capitalInicial) / 100); //calculo SL por Gestion
            cuarentaPorciento = capitalInicial - cuarentaPorciento;

            double cincuentaPorciento = ((50.0 * capitalInicial) / 100); //calculo SL por Gestion
            cincuentaPorciento = capitalInicial - cincuentaPorciento;

            boolean drawdownAlcanzado8 = false;
            boolean drawdownAlcanzado10 = false;
            boolean drawdownAlcanzado20 = false;// variable local que se resetea y contamos las cuentas quemadas
            boolean drawdownAlcanzado30 = false;
            boolean drawdownAlcanzado40 = false;
            boolean drawdownAlcanzado50 = false;

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
                        //decrementar capital con stop
                        capitalActual = capitalActual - ((SL * capitalActual) / 100);
                        break;
                }

                //CALCULAR CUANTOS TRADES NECESITO COMO MINO PARA SALIR DE DRAWDOWN
                if(capitalActual <= capitalInicial){

                    if(j > cantidadTradesParaSuperarDrawDown){
                        cantidadTradesParaSuperarDrawDown = j;
                    }
                }

                //preguntar si el capital actual esta por debajo del drawdown
                if(capitalActual <= ochoPorciento){

                    if(j > cantidadTradesParaSuperarDrawDown){
                        cantidadTradesParaSuperarDrawDown = j;
                    }

                    detenerEjecucion = true;
                    drawdownAlcanzado8 = true;
                    //System.out.println("Drawdown alcanzado del 8% en el trade: "+ j+" de la cuenta: "+ i);
                    //System.out.printf("\tcapital actual: %.2f\n",capitalActual);
                    if(capitalActual <= diezPorciento){
                        drawdownAlcanzado8 = false;
                    }
                }

                //preguntar si el capital actual esta por debajo del drawdown
                if(capitalActual <= diezPorciento){
                    detenerEjecucion = true;
                    drawdownAlcanzado10 = true;
                    //System.out.println("Drawdown alcanzado del 20% en el trade: "+ j+" de la cuenta: "+ i);
                    //System.out.printf("\tcapital actual: %.2f\n",capitalActual);
                    if(capitalActual <= veintePorciento){
                        drawdownAlcanzado10 = false;
                    }
                }

                //preguntar si el capital actual esta por debajo del drawdown
                if(capitalActual <= veintePorciento){
                    detenerEjecucion = true;
                    drawdownAlcanzado20 = true;
                    //System.out.println("Drawdown alcanzado del 20% en el trade: "+ j+" de la cuenta: "+ i);
                    //System.out.printf("\tcapital actual: %.2f\n",capitalActual);
                    if(capitalActual <= treintaPorciento){
                        drawdownAlcanzado20 = false;
                    }
                }
                if (capitalActual <= treintaPorciento){
                    detenerEjecucion = true;
                    drawdownAlcanzado30 = true;
                    //System.out.println("Drawdown alcanzado del 30% en el trade: "+ j+" de la cuenta: "+ i);
                    //System.out.printf("\tcapital actual: %.2f\n",capitalActual);
                    if(capitalActual <= cuarentaPorciento){
                        drawdownAlcanzado30 = false;
                    }
                }
                if (capitalActual <= cuarentaPorciento){
                    detenerEjecucion = true;
                    drawdownAlcanzado40 = true;
                    //System.out.println("Drawdown alcanzado del 40% en el trade: "+ j+" de la cuenta: "+ i);
                    //System.out.printf("\tcapital actual: %.2f\n",capitalActual);
                    if(capitalActual <= cincuentaPorciento){
                        drawdownAlcanzado40 = false;
                    }
                }
                if (capitalActual <= cincuentaPorciento){
                    detenerEjecucion = true;
                    drawdownAlcanzado50 = true;
                    //System.out.println("Drawdown alcanzado del 50% en el trade: "+ j+" de la cuenta: "+ i);
                    //System.out.printf("\tcapital actual: %.2f\n",capitalActual);
                }
            }

            //condicional para contar cuentas quemadas
            if(drawdownAlcanzado8){
                cantCuentas8Alcanzadas = cantCuentas8Alcanzadas + 1;
            }

            if(drawdownAlcanzado10){
                cantCuentas10Alcanzadas = cantCuentas10Alcanzadas + 1;
            }

            if(drawdownAlcanzado20){
                cantCuentas20Alcanzadas = cantCuentas20Alcanzadas + 1;
            }

            if(drawdownAlcanzado30){
                cantCuentas30Alcanzadas = cantCuentas30Alcanzadas + 1;
            }

            if(drawdownAlcanzado40){
                cantCuentas40Alcanzadas = cantCuentas40Alcanzadas + 1;
            }

            if(drawdownAlcanzado50){
                cantCuentas50Alcanzadas = cantCuentas50Alcanzadas + 1;
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

        System.out.println("\nDespues de esta cantidad de trades en la cuenta deberia terminar el Drawdown: "+ cantidadTradesParaSuperarDrawDown);

        //Funcion con resultados finales de la strategia
        resultadoFinal(promedioEfectividadEstrategia, capitalInicial, cantidadDeTradesPorCuenta,
                detenerEjecucion, cantCuentas8Alcanzadas, cantCuentas10Alcanzadas, cantCuentas20Alcanzadas,cantCuentas30Alcanzadas,cantCuentas40Alcanzadas,cantCuentas50Alcanzadas, cantidadDeCuentasParaTestear);
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

    public static void resultadoFinal(double promedioEfectividadEstrategia, double capitalInicial, int cantTrades,
                                      boolean detenerEjecucion, int cantCuentas8Alcanzadas, int cantCuentas10Alcanzadas, int cantCuentas20Alcanzadas, int cantCuentas30Alcanzadas,
                                      int cantCuentas40Alcanzadas, int cantCuentas50Alcanzadas, int cantidadDeCuentasParaTestear) {

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

        System.out.println("⚡ Cantidad de Cuentas Que alcanzaron el 8% de drawdown -> " + cantCuentas8Alcanzadas);
        System.out.println("⚡ Cantidad de Cuentas Que alcanzaron el 10% de drawdown -> " + cantCuentas10Alcanzadas);
        System.out.println("⚡ Cantidad de Cuentas Que alcanzaron el 20% de drawdown -> " + cantCuentas20Alcanzadas);
        System.out.println("⚡ Cantidad de Cuentas Que alcanzaron el 30% de drawdown -> " + cantCuentas30Alcanzadas);
        System.out.println("⚡ Cantidad de Cuentas Que alcanzaron el 40% de drawdown -> " + cantCuentas40Alcanzadas);
        System.out.println("⚡ Cantidad de Cuentas Que alcanzaron mas del 50% de drawdown -> " + cantCuentas50Alcanzadas);
        double diff = 0.0; // sobrante
        double probabilidadDeEstrategia = 0.0;
        diff = cantidadDeCuentasParaTestear - (cantCuentas20Alcanzadas +cantCuentas30Alcanzadas+cantCuentas40Alcanzadas+cantCuentas50Alcanzadas);// obtener sobrante
        probabilidadDeEstrategia = (((diff / cantidadDeCuentasParaTestear) * 100));

        System.out.println(" Probabilidad de Estrategia : %"+ probabilidadDeEstrategia);

    }

    public static void header(){
        System.out.println("\n\t=========================================");
        System.out.println("\t=========== TEST RENTABILIDAD ===========");
        System.out.println("\t=========================================\n");
    }
}
