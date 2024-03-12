import matplotlib.pyplot as plt
import numpy as np
import matplotlib.ticker as mticker

def lire_fichier(chemin_fichier):
    with open(chemin_fichier, 'r') as fichier:
        ligne = fichier.readline()
        moyenne = float(ligne.strip().split()[0].replace(',', '.'))
    return moyenne

def tracer_graphique_baton_temps(moyenne_naif, moyenne_bmd):
    algorithmes = ['Naïf', 'BMD']
    moyennes = [moyenne_naif, moyenne_bmd]
    
    plt.figure(figsize=(8, 6))
    plt.bar(algorithmes, moyennes, color=['blue', 'green'])
    for i, v in enumerate(moyennes):
        plt.text(i, v + 0.01, f"{v:.2f}", ha='center', va='bottom', color='black')
    plt.xlabel('Algorithmes')
    plt.ylabel('Temps moyen (ms)')
    plt.title('Comparaison du temps CPU moyen: Naïf vs BMD')
    plt.show()

def tracer_graphique_baton_temps_ecart(ecart_naif, ecart_bmd):
    algorithmes = ['Naïf', 'BMD']
    ecarts = [ecart_naif, ecart_bmd]
    
    plt.figure(figsize=(8, 6))
    plt.bar(algorithmes, ecarts, color=['blue', 'green'])
    for i, v in enumerate(ecarts):
        plt.text(i, v + 0.01, f"{v:.2f}", ha='center', va='bottom', color='black')	
    plt.xlabel('Algorithmes')
    plt.ylabel('Ecart-Type (ms)')

    plt.title('Comparaison de l écart-type du temps CPU moyen: Naïf vs BMD')
    plt.show()
    
def tracer_graphique_baton_memory(moyenne_naif, moyenne_bmd):
    algorithmes = ['Naïf', 'BMD']
    moyennes = [moyenne_naif, moyenne_bmd]
    plt.figure(figsize=(8, 6))
    plt.bar(algorithmes, moyennes, color=['blue', 'green'])
    plt.xlabel('Algorithmes')
    plt.ylabel('Memoire (bytes)')
    plt.title('Comparaison de la consommation de mémoire moyen: Naïf vs BMD')
    plt.show()
    

def lire_frequences_et_tracer_graphique(chemin_fichier):
    # Lire les données du fichier
    frequences = {}
    with open(chemin_fichier, 'r') as fichier:
        for ligne in fichier:
            temps, count = ligne.strip().split(': ')
            temps = float(temps.replace(' ms', ''))
            # Arrondir chaque temps à la tranche de 0.2 la plus proche
            tranche = round(temps * 5) / 5
            if tranche in frequences:
                frequences[tranche] += int(count)
            else:
                frequences[tranche] = int(count)

    # Trier les données par temps
    tranches_triees = sorted(frequences.keys())
    counts_triees = [frequences[tranche] for tranche in tranches_triees]

    # Tracer le graphique
    plt.figure(figsize=(10, 6))
    plt.bar(tranches_triees, counts_triees, width=0.2, align='center')
    plt.xlabel('Temps (ms)')
    plt.ylabel('Fréquence')
    plt.title('Diagramme de fréquence des temps d\'exécution')

    # Ajuster les graduations de l'axe des abscisses
    # Par exemple, afficher une graduation tous les 1 ms
    plt.xticks(np.arange(min(tranches_triees), max(tranches_triees) + 1, 5.0))
    
    # Rotation des labels de l'axe des abscisses pour une meilleure lisibilité
    plt.xticks(rotation=0)

    plt.grid(axis='y')
    plt.tight_layout()  # Ajuster automatiquement les paramètres de la figure
    plt.show()


# Chemins vers les fichiers de moyenne
fichier_temps_naif_mean = 'temps_cpu_naif_mean.txt'
fichier_temps_bmd_mean = 'temps_cpu_bmd_mean.txt'

fichier_temps_naif_freq = 'temps_cpu_naif_freq.txt'
fichier_temps_bmd_freq = 'temps_cpu_bmd_freq.txt'

fichier_temps_naif_ecart= 'temps_cpu_naif_ecart.txt'
fichier_temps_bmd_ecart = 'temps_cpu_bmd_ecart.txt'

fichier_memory_naif_mean = 'memory_naif_mean.txt'
fichier_memory_bmd_mean = 'memory_bmd_mean.txt'

# Lire les moyennes depuis les fichiers
moyenne_tmp_naif = lire_fichier(fichier_temps_naif_mean)
moyenne_tmp_bmd = lire_fichier(fichier_temps_bmd_mean)

ecart_tmp_naif = lire_fichier(fichier_temps_naif_ecart)
ecart_tmp_bmd = lire_fichier(fichier_temps_bmd_ecart)

moyenne_mem_naif = lire_fichier(fichier_memory_naif_mean)
moyenne_mem_bmd = lire_fichier(fichier_memory_bmd_mean)

# Tracer le graphique à barres
#tracer_graphique_baton_temps(moyenne_tmp_naif, moyenne_tmp_bmd)
#tracer_graphique_baton_memory(moyenne_mem_naif, moyenne_mem_bmd)
#tracer_graphique_baton_temps_ecart(ecart_tmp_naif, ecart_tmp_bmd)
#lire_frequences_et_tracer_graphique(fichier_temps_naif_freq)
lire_frequences_et_tracer_graphique(fichier_temps_bmd_freq)












