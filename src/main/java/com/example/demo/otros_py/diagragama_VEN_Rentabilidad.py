import numpy as np
import matplotlib.pyplot as plt

# Parámetros del sistema
capital_inicial = 1000
riesgo_por_trade = 0.005  # 0.50% -> 0.005
ganancia_tp1 = 0.3709
ganancia_total = 2.10
perdida_total = -1

#cant trades
num_trades = 528 # cantidad de trades

# Tasas de acierto
tp1_rate = 0.25
tp2_rate = 0.25
loss_rate = 1 - (tp1_rate + tp2_rate)

# Simulaciones
num_simulaciones = 10001
capital_final = []

print("Ejecutando simulaciones...")

# Ejecutar múltiples simulaciones
for seed in range(num_simulaciones):
    np.random.seed(seed)
    resultados = np.random.choice(
        ['tp1', 'tp2', 'loss'],
        size=num_trades,
        p=[tp1_rate, tp2_rate, loss_rate]
    )

    capital = capital_inicial
    for r in resultados:
        riesgo_usd = capital * riesgo_por_trade
        if r == 'tp1':
            ganancia = riesgo_usd * ganancia_tp1
        elif r == 'tp2':
            ganancia = riesgo_usd * ganancia_total
        else:
            ganancia = riesgo_usd * perdida_total
        capital += ganancia

    capital_final.append(capital)

# Estadísticas
capital_array = np.array(capital_final)
media_final = np.mean(capital_array)
mediana_final = np.median(capital_array)
desviacion_std = np.std(capital_array)
porcentaje_ganadoras = np.mean(capital_array > capital_inicial) * 100

# Resultados
print(f"\n📊 RESULTADOS DE SIMULACIÓN ({num_simulaciones} repeticiones):")
print(f"Promedio final: ${media_final:.2f}")
print(f"Mediana final: ${mediana_final:.2f}")
print(f"Desviación estándar: ${desviacion_std:.2f}")
print(f"% de simulaciones ganadoras: {porcentaje_ganadoras:.2f}%")

# Gráfico de distribución
plt.figure(figsize=(12, 6))
plt.hist(capital_array, bins=50, color='skyblue', edgecolor='black')
plt.axvline(x=capital_inicial, color='red', linestyle='--', label='Capital inicial')
plt.title('Distribución de Capital Final - Simulación Fixed Fractional')
plt.xlabel('Capital Final ($)')
plt.ylabel('Frecuencia')
plt.legend()
plt.grid(True)
plt.tight_layout()
plt.show()
