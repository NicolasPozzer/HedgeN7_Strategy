import numpy as np
import matplotlib.pyplot as plt

# Parámetros del sistema
capital_inicial = 1000
riesgo_por_trade = 0.005
ganancia_tp1 = 0.3709
ganancia_total = 2.10
perdida_total = -1

# Configuración de simulación
num_trades = 792
num_simulaciones = 10000

tp1_rate = 0.25
tp2_rate = 0.25
loss_rate = 1 - (tp1_rate + tp2_rate)

capital_final = []
drawdowns = []
equity_total = np.zeros(num_trades + 1)
drawdown_curves = []

print("Ejecutando simulaciones...")

for seed in range(num_simulaciones):
    np.random.seed(seed)
    resultados = np.random.choice(['tp1', 'tp2', 'loss'], size=num_trades, p=[tp1_rate, tp2_rate, loss_rate])

    capital = capital_inicial
    equity_curve = [capital]
    peak = capital
    drawdown_curve = []

    for r in resultados:
        riesgo_usd = capital * riesgo_por_trade
        if r == 'tp1':
            ganancia = riesgo_usd * ganancia_tp1
        elif r == 'tp2':
            ganancia = riesgo_usd * ganancia_total
        else:
            ganancia = riesgo_usd * perdida_total
        capital += ganancia
        equity_curve.append(capital)

        if capital > peak:
            peak = capital
        drawdown_curve.append((peak - capital) / peak)

    capital_final.append(capital)
    drawdowns.append(max(drawdown_curve))
    equity_total += np.array(equity_curve)
    drawdown_curves.append(drawdown_curve)

# Promedios
equity_promedio = equity_total / num_simulaciones
drawdown_matrix = np.array([d + [0]*(num_trades - len(d)) for d in drawdown_curves])
drawdown_promedio = np.mean(drawdown_matrix, axis=0)

# Estadísticas
capital_array = np.array(capital_final)
print(f"\n📊 RESULTADOS DE SIMULACIÓN ({num_simulaciones} repeticiones):")
print(f"Promedio final: ${np.mean(capital_array):.2f}")
print(f"Mediana final: ${np.median(capital_array):.2f}")
print(f"Desviación estándar: ${np.std(capital_array):.2f}")
print(f"% de simulaciones ganadoras: {np.mean(capital_array > capital_inicial)*100:.2f}%")

# Probabilidades de drawdowns
print("\n📉 Probabilidades de alcanzar drawdowns:")
for umbral in [0.05, 0.10, 0.15, 0.20, 0.25, 0.33, 0.40, 0.50]:
    prob = np.mean(np.array(drawdowns) >= umbral) * 100
    print(f"Probabilidad de un drawdown >= {int(umbral*100)}%: {prob:.2f}%")

# Gráfico distribución capital final
plt.figure(figsize=(12, 6))
plt.hist(capital_array, bins=50, color='skyblue', edgecolor='black')
plt.axvline(x=capital_inicial, color='red', linestyle='--', label='Capital inicial')
plt.title('Distribución de Capital Final')
plt.xlabel('Capital Final ($)')
plt.ylabel('Frecuencia')
plt.legend()
plt.grid(True)
plt.tight_layout()
plt.show()

# Curva de equity promedio
plt.figure(figsize=(12, 6))
plt.plot(equity_promedio, color='green', label='Equity promedio')
plt.axhline(y=capital_inicial, color='red', linestyle='--', label='Capital inicial')
plt.title('Curva de Equity Promedio')
plt.xlabel('Número de Trades')
plt.ylabel('Capital ($)')
plt.legend()
plt.grid(True)
plt.tight_layout()
plt.show()

# Curva de drawdown promedio
plt.figure(figsize=(12, 6))
plt.plot(drawdown_promedio * 100, color='orange', label='Drawdown promedio')
plt.title('Drawdown Promedio por Trade')
plt.xlabel('Número de Trades')
plt.ylabel('Drawdown (%)')
plt.legend()
plt.grid(True)
plt.tight_layout()
plt.show()
