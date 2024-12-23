import random

# Parámetros iniciales
probabilidad_tp1 = 23 / 100  # Probabilidad de alcanzar TP1 (23.55%)
probabilidad_tp2 = 46 / 100  # Probabilidad de alcanzar TP2 (13.00%)

# Simulador de trades
def simular_trades(n_trades):
    resultados = []  # Almacenar los resultados de cada trade

    for _ in range(n_trades):
        resultado_trade = random.random()  # Generar un número aleatorio entre 0 y 1

        if resultado_trade < probabilidad_tp1:  # Si el número es menor que la probabilidad de TP1
            resultados.append(0.38)  # Ganancia por TP1
        elif resultado_trade < probabilidad_tp2:  # Si el número está entre TP1 y TP2
            resultados.append(2.20)  # Ganancia por TP2
        else:
            resultados.append(-1)  # Pérdida por trade fallido (1R)

    return resultados

# Ejecutar simulaciones
n_trades = 10000000  # Número de trades a simular
resultados_simulacion = simular_trades(n_trades)

# Calcular el rendimiento total
rendimiento_total = sum(resultados_simulacion)
rendimiento_promedio = rendimiento_total / n_trades

print(f"\nSimulación de {n_trades} trades:")
print(f"Rendimiento total: {rendimiento_total:.2f}R")
print(f"Rendimiento promedio por trade: {rendimiento_promedio:.4f}R")

# Validación del rendimiento
if rendimiento_promedio > 0:
    print("\n✅ El sistema es rentable a largo plazo en la simulación.")
else:
    print("\n❌ El sistema NO es rentable a largo plazo en la simulación.")
