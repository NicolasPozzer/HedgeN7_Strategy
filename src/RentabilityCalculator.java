import java.util.Random;

public class RentabilityCalculator {
    public static void main(String[] args) {    /* main */

        /* ⬇️CONFIGURAR⬇️ */
        // config estrategia
        double capitalInicial = 10000.0;
        final double TP = 2.00; // Porcentaje de Take Profit de la cuenta
        final double SL = 1.00; // Porcentaje de stop loss de la cuenta
        final double probabilidadAciertos = 35.33; // numero entero entre 0 y 100 (Ej. 50 seria el 50% de acierto).
        final double comisiones = 0.07;
        final double stopGestionDeRiesgo = 20.0; // si el capital inicial llega a un porcentaje menor a este se deja de operar

        // config pruebas
        int cantidadDeCuentasParaTestear = 10;
        int cantidadDeTradesPorCuenta = 100;

        //llamado a funciones
        header();
        testearRentabilidadDeCuentas(cantidadDeCuentasParaTestear, capitalInicial,
                cantidadDeTradesPorCuenta, probabilidadAciertos, TP, SL, comisiones, stopGestionDeRiesgo);
    }


    /* =========FUNCIONES============== */
    public static void testearRentabilidadDeCuentas(int cantidadDeCuentasParaTestear,
                                                    double capitalInicial, int cantidadDeTradesPorCuenta,
                                                    double probabilidadAciertos, double TP, double SL, double comisiones, double stopGestionDeRiesgo) {
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
                Boolean resultadoTrade = tradeRealizado(probabilidadAciertos);

                if (resultadoTrade) {
                    capitalActual = capitalActual + ((TP * capitalActual) / 100);
                }else {
                    capitalActual = capitalActual - ((SL * capitalActual) / 100);
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

            //Descontar comisiones
            capitalActual = capitalActual - ((comisiones * capitalActual) / 100);

            if(capitalActual >= capitalInicial){
                System.out.printf("\t✅Capital: $ %.2f",capitalActual);
                System.out.println("  Cuenta Nro. "+ i);
            }else{
                System.out.printf("\t❌Capital: $ %.2f",capitalActual);
                System.out.println("  Cuenta Nro. "+ i);
            }
            acumDineroFinal = acumDineroFinal + capitalActual;
        }


        promedioEfectividadEstrategia = acumDineroFinal / cantidadDeCuentasParaTestear;
        System.out.printf("\t Promedio: $ %.2f\n",promedioEfectividadEstrategia);
        //Funcion con resultados finales de la strategia
        resultadoFinal(promedioEfectividadEstrategia, capitalInicial, cantidadDeTradesPorCuenta, detenerEjecucion, cantCuentasQuedamas, cantidadDeCuentasParaTestear);
    }

    //funcion para correr bucle con pruebas de acierto
    public static Boolean tradeRealizado(double probabilidadAciertos){
        Random random = new Random();
        Boolean tradeAcertado = null;
        double numRandom = 0;

        numRandom = random.nextDouble(0,100); // numeros aleatorios desde el 0 al 100

        if(numRandom >= probabilidadAciertos){
            tradeAcertado = false;
        }else{
            tradeAcertado = true;
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
