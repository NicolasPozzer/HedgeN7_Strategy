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
        double capitalInicial = 100;
        int cantidadDeCuentas = 100; // Usaremos menos cuentas para mejorar la visibilidad
        int cantidadDeTrades = 2500;
        double TP1 = 0.3709;  // TP1 CON 0.50% DE RIESGO ES: 0.18545. para el 1% es 0.3709.
        double TP2 = 2.132;  // TP2 CON 0.50% DE RIESGO ES: 1.0751, TP2 CON 1.00% DE RIESGO ES: 2.132
        double SL = 1;       // Porcentaje de stop loss 0.50% DE RIESGO ES: 0.50, SL CON 1.00% DE RIESGO ES: 1
        double probTP1 = 23.222; // mi acierto: probabilidadAciertosTP1 = 22.21;
        double probTP2 = 27.778; // mi acierto: probabilidadAciertosTP2 = 27.75;

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

                // Actualiza el gráfico con una selección de cada 10 puntos
                if (j % 10 == 0) {
                    series.getData().add(new XYChart.Data<>(j, capitalActual));
                }
            }
            lineChart.getData().add(series);
        }

        // Crear la escena y mostrarla
        Scene scene = new Scene(lineChart, 800, 600);
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
