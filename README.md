# Projet Algo 2: Le nombre de Bacon

# Téléchargement des datasets
Afin de lancer le projet, vous aurez besoin de téléchager les datasets de IMDB [ici](https://datasets.imdbws.com/)
Vous n'aurez besoin uniquement de `name.basics.tsv` ainsi que `title.akas.tsv`, après les avoirs téléchargé, mettez les dans le dossier `datasets`

# Ajouts de la librairie Algs4.jar
## IntelliJ
Si vous utilisez IntelliJ, appuyez sur `ctrl + shift + alt + S`, si le dossier lib n'est pas présent,
appuyez sur Librairies -> + -> Java, et séléctionnez le dossier lib

# Compilation
## IntelliJ
Sur IntelliJ, si la configuration n'est pas présente, appuyez sur `ADD CONFIGURATION..` en haut a droite -> + -> Application.
Comme `Main class` mettez `Main`. Cliquez sur `Apply` et vous pouvez maintenant lancer le programme !

## Compilation manuelle
Vous allez avoir besoin du compilateur `Java`, le programme a été ecrit pour fonctionner avec le SDK 1.8.
```bash
javac -cp "lib/*" -sourcepath src/ src/Main.java -d .
java Main
```
