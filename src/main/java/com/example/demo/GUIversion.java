package com.example.demo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class GUIversion extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Simulación de Rentabilidad con Monte Carlo");

        // Ejes del gráfico
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Número de Trades");
        yAxis.setLabel("Capital");

        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Evolución del Capital por Cuenta");

        // Configuración inicial
        int tamanioLineas = 100;
        double capitalInicial = 500;
        int cantidadDeCuentas = 1000; // Usaremos menos cuentas para mejorar la visibilidad
        int cantidadDeTrades = 10000;
        double TP1 = 0.3709;  // TP1 CON 0.50% DE RIESGO ES: 0.18545. para el 1% es 0.3709.
        double TP2 = 2.132;  // TP2 CON 0.50% DE RIESGO ES: 1.0751, TP2 CON 1.00% DE RIESGO ES: 2.132
        double SL = 1;       // Porcentaje de stop loss 0.50% DE RIESGO ES: 0.50, SL CON 1.00% DE RIESGO ES: 1
        double probTP1 = 18.1818; // mi acierto: probabilidadAciertosTP1 = 23.222;
        double probTP2 = 29.8182; // mi acierto: probabilidadAciertosTP2 = 27.778;

        int cuentasRentables = 0;  // Contador de cuentas rentables
        double totalCapitalFinal = 0;  // Para medir el rendimiento total

        // Agregar las cuentas al gráfico con una actualización más eficiente
        for (int i = 1; i <= cantidadDeCuentas; i++) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName("Cuenta " + i);
            double capitalActual = capitalInicial;

            // Usar un Timeline para actualizar de forma más suave los puntos
            for (int j = 0; j < cantidadDeTrades; j++) {
                int resultado = realizarTrade(probTP1, probTP2);
                if (resultado == 1) {
                    capitalActual += (capitalActual * TP1 / 100);
                } else if (resultado == 2) {
                    capitalActual += (capitalActual * TP2 / 100);
                } else {
                    capitalActual -= (capitalActual * SL / 100);
                }

                // Actualiza el gráfico con una selección de cada 50 puntos
                if (j % tamanioLineas == 0) {
                    series.getData().add(new XYChart.Data<>(j, capitalActual));
                }
            }

            // Verificar si la cuenta fue rentable
            if (capitalActual > capitalInicial) {
                cuentasRentables++;
            }
            totalCapitalFinal += capitalActual;

            lineChart.getData().add(series);
        }

        // Calcular la probabilidad de rentabilidad
        double probabilidadRentabilidad = (double) cuentasRentables / cantidadDeCuentas * 100;
        double rendimientoPromedio = totalCapitalFinal / cantidadDeCuentas;

        double porcentajePromedio = (rendimientoPromedio / capitalInicial) * 100;
        // Mostrar probabilidad de rentabilidad en la consola
        System.out.println("Probabilidad de rentabilidad: " + probabilidadRentabilidad + "%");
        System.out.printf("Rendimiento promedio por cuenta en USD: $ %.2f", rendimientoPromedio);
        System.out.print("\nRendimiento promedio por cuenta en Porcentaje: %");
        System.out.printf("%.2f\n", porcentajePromedio);

        // Crear la escena y mostrarla
        Scene scene = new Scene(lineChart, 1600, 800);
        stage.setScene(scene);
        stage.show();
    }

    private int realizarTrade(double probTP1, double probTP2) {
        Random random = new Random();
        double numRandom = random.nextDouble() * 100;
        if (numRandom <= probTP1) {
            return 1; // TP1
        } else if (numRandom <= probTP1 + probTP2) {
            return 2; // TP2
        } else {
            return 3; // SL
        }
    }
}
