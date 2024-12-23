# Parámetros iniciales
probabilidad_tp1 = 23.55 / 100  # Probabilidad de alcanzar TP1 (23.55%)
probabilidad_tp2 = 23.55 / 100  # Probabilidad de alcanzar TP2 (23.55%)
probabilidad_total_ganar = probabilidad_tp1 + probabilidad_tp2  # Probabilidad total de ganar
probabilidad_perder = 1 - probabilidad_total_ganar  # Probabilidad de perder (restante)

# Valores de TP1 y TP2
tp1_ratio = 1.14  # Ganancia de TP1 en R
tp1_peso = 0.38  # Peso de TP1 en el total
tp2_ratio = 2.20  # Ganancia de TP2 en R
tp2_peso = 0.62  # Peso de TP2 en el total

# Cálculo de ganancias por TP1 y TP2
ganancia_tp1 = tp1_ratio * tp1_peso
ganancia_tp2 = tp2_ratio * tp2_peso

# Ganancia promedio ponderada (G)
ganancia_promedio = ganancia_tp1 + ganancia_tp2

# Expectativa matemática (E)
expectativa = (probabilidad_total_ganar * ganancia_promedio) - (probabilidad_perder * 1)  # Pérdida promedio es 1R

# Mostrar resultados
print(f"Ganancia por TP1: {ganancia_tp1:.4f}R")
print(f"Ganancia por TP2: {ganancia_tp2:.4f}R")
print(f"Ganancia promedio ponderada (G): {ganancia_promedio:.4f}R")
print(f"Probabilidad total de ganar: {probabilidad_total_ganar * 100:.2f}%")
print(f"Probabilidad de perder: {probabilidad_perder * 100:.2f}%")
print(f"Expectativa matemática (E): {expectativa:.4f}")

# Validación de rentabilidad
if expectativa > 0:
    print("\n✅ El sistema es rentable a largo plazo.")
else:
    print("\n❌ El sistema NO es rentable a largo plazo.")


"""
# Cálculo de balances mensuales con interés compuesto
capital_inicial = 500  # USD
meses = 24  # Período en meses
ganancia_mensual_porcentual = ganancia_promedio * probabilidad_total_ganar * 100  # Porcentaje mensual
balances = [capital_inicial]

for mes in range(1, meses + 1):
    nuevo_balance = balances[-1] * (1 + ganancia_mensual_porcentual / 100)
    balances.append(nuevo_balance)

# Mostrar resumen mensual
print("\nResumen de los 24 meses:")
print("Mes\tBalance (USD)\tGanancia (%)")
for mes, balance in enumerate(balances[1:], start=1):
    ganancia_mensual = (balance / balances[mes - 1] - 1) * 100
    print(f"{mes}\t{balance:.2f}\t{ganancia_mensual:.2f}")
    
"""