import numpy as np
import matplotlib.pyplot as plt

# Parámetros del sistema
capital_inicial = 1000
riesgo_por_trade = 0.005  # 0.50% de riesgo -> 0.005

# Ratios y % aciertos
ganancia_tp1 = 0.3709
ganancia_total = 2.10
perdida_total = -1
tp1_rate = 0.28169
tp2_rate = 0.239437
loss_rate = 1 - (tp1_rate + tp2_rate)

#Configuraciones de casos de prueba
num_trades = 71
num_simulaciones = 40000

# Datos para el análisis
capital_final = []
drawdowns = []
equity_curves = []

for seed in range(num_simulaciones):
    np.random.seed(seed)
    resultados = np.random.choice(['tp1', 'tp2', 'loss'], size=num_trades, p=[tp1_rate, tp2_rate, loss_rate])
    capital = capital_inicial
    equity_curve = [capital]

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

    peak = capital_inicial
    max_drawdown = 0
    for equity in equity_curve:
        if equity > peak:
            peak = equity
        drawdown = (peak - equity) / peak
        max_drawdown = max(max_drawdown, drawdown)

    drawdowns.append(max_drawdown)
    capital_final.append(capital)
    if seed < 100:  # Guardamos las primeras 100 curvas para visualización
        equity_curves.append(equity_curve)

avg_drawdown_durations = []
max_drawdown_durations = []

for curve in equity_curves:
    peak = curve[0]
    duration = 0
    durations = []
    for equity in curve:
        if equity < peak:
            duration += 1
        else:
            if duration > 0:
                durations.append(duration)
            peak = equity
            duration = 0
    if duration > 0:
        durations.append(duration)
    if durations:
        avg_drawdown_durations.append(np.mean(durations))  # Promedio de DDs por curva
        max_drawdown_durations.append(np.max(durations))   # Max duración de DDs por curva



# Cálculo de estadísticas
capital_array = np.array(capital_final)
media_final = np.mean(capital_array)
mediana_final = np.median(capital_array)
desviacion_std = np.std(capital_array)

# Probabilidades de drawdowns
umbral_drawdowns = [0.02, 0.03, 0.05, 0.10, 0.15, 0.20, 0.25, 0.30, 0.33, 0.40, 0.50]
prob_drawdowns = [np.mean(np.array(drawdowns) >= thresh) * 100 for thresh in umbral_drawdowns]

# Estadísticas
capital_array = np.array(capital_final)
print(f"\n📊 RESULTADOS DE SIMULACIÓN ({num_simulaciones} repeticiones):")
print(f"Promedio final: ${np.mean(capital_array):.2f}")
print(f"Mediana final: ${np.median(capital_array):.2f}")
print(f"Desviación estándar: ${np.std(capital_array):.2f}")
print(f"% de simulaciones ganadoras: {np.mean(capital_array > capital_inicial)*100:.2f}%")

capFinal = np.mean(capital_array)
sobrante = capFinal - capital_inicial
porcentajeDeRentabilidad = (sobrante / capital_inicial) * 100
print(f"El porcentaje Promedio de Rentabilidad es: {porcentajeDeRentabilidad:.2f}%")

# Probabilidades de drawdowns
print("\n📉 Probabilidades de alcanzar drawdowns:")
for umbral in umbral_drawdowns:
    prob = np.mean(np.array(drawdowns) >= umbral) * 100
    print(f"Probabilidad de un drawdown >= {int(umbral*100)}%: {prob:.2f}%")

print(f"\n⏱️ Promedio de trades para salir de un drawdown: {np.mean(avg_drawdown_durations):.2f}")
print(f"📉 Drawdown más largo registrado (en trades): {np.max(max_drawdown_durations)}")

# Gráficos
fig, axs = plt.subplots(2, 2, figsize=(16, 10))

# Histograma del capital final
axs[0, 0].hist(capital_array, bins=50, color='skyblue', edgecolor='black')
axs[0, 0].axvline(x=capital_inicial, color='red', linestyle='--', label='Capital inicial')
axs[0, 0].set_title('Distribución de Capital Final')
axs[0, 0].set_xlabel('Capital Final ($)')
axs[0, 0].set_ylabel('Frecuencia')
axs[0, 0].legend()
axs[0, 0].grid(True)

# Curvas de equity seleccionadas
for curva in equity_curves:
    axs[0, 1].plot(curva, alpha=0.3)
axs[0, 1].set_title('Evolución de Equity en Simulaciones')
axs[0, 1].set_xlabel('Trade')
axs[0, 1].set_ylabel('Capital ($)')
axs[0, 1].grid(True)

# Boxplot del capital final
axs[1, 0].boxplot(capital_array, vert=False)
axs[1, 0].set_title('Boxplot del Capital Final')
axs[1, 0].set_xlabel('Capital Final ($)')
axs[1, 0].grid(True)

# Barras de probabilidades de drawdowns
axs[1, 1].bar([f'{int(x*100)}%' for x in umbral_drawdowns], prob_drawdowns, color='salmon')
axs[1, 1].set_title('Probabilidades de Drawdowns')
axs[1, 1].set_ylabel('Probabilidad (%)')
axs[1, 1].set_ylim(0, 110)
axs[1, 1].grid(True)

plt.tight_layout()
plt.show()
