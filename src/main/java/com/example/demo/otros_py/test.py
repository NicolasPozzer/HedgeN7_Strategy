import matplotlib.pyplot as plt
import numpy as np

# Parámetros del sistema
capital_inicial = 1000
riesgo_por_trade = 0.0033  # 0.5% -> 0.005
ganancia_tp1 = 0.3709
ganancia_total = 2.132  # TP1 + TP2
perdida_total = -1

# Variables del simulador
num_trades = 40000
# Tasas de acierto: probabilidad de tocar solo TP1, y de tocar TP1+TP2
tp1_rate = 0.257576
tp2_rate = 0.257576
loss_rate = 1 - (tp1_rate + tp2_rate)

# Generar resultados aleatorios de operaciones
np.random.seed(42)
resultados = np.random.choice(
    ['tp1', 'tp2', 'loss'],
    size=num_trades,
    p=[tp1_rate, tp2_rate, loss_rate]
)

capital = [capital_inicial]

# Simulación
for r in resultados:
    capital_actual = capital[-1]
    riesgo_usd = capital_actual * riesgo_por_trade

    if r == 'tp1':
        ganancia = riesgo_usd * ganancia_tp1
    elif r == 'tp2':
        ganancia = riesgo_usd * ganancia_total
    else:
        ganancia = riesgo_usd * perdida_total

    capital.append(capital_actual + ganancia)

# Gráfico
plt.figure(figsize=(12, 6))
plt.plot(capital, label='Evolución del capital')
plt.axhline(y=capital_inicial, color='gray', linestyle='--', label='Capital inicial')
plt.title('Simulación de sistema con Fixed Fractional Risk')
plt.xlabel('Número de operaciones')
plt.ylabel('Capital ($)')
plt.legend()
plt.grid(True)
plt.tight_layout()
plt.show()
