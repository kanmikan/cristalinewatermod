# Cristaline Water - mod experimental 
Mi intento de hacer el agua mas transparente y similar a 1.13+ en minecraft 1.7.10 usando mixins.

- injecta la textura del agua por defecto y la reemplaza por la version gris semitransparente que usa minecraft moderno, y aplica un tinte suave predefinido en base a algunos biomas disponibles en 1.7.10.
- cancela el horrible overlay de la pantalla que sale cuando el jugador esta bajo agua
- hace el fog mas translucido
- hace el ambiente de bajo el agua mas brillante
- hace que el agua no se vea cuando toca cristales, haciendo que las estructuras bajo el agua tengan mejor visibilidad, pero a cambio, el agua se ve invisible y rara en estructuras fuera del agua, depende del caso. (se puede desactivar con TransparentWaterSides=false)
- tambien se aplica a capsulas de aire bajo el agua
- opcionalmente, añade algunos sonidos (se desactiva con UnderwaterSounds=false)


Muy util para poder ver mejor en los submarinos del archimedes ships plus:

![seagrass y peces son de un mod aparte](https://i.ibb.co/YLtYnR1/image.png)

### Errores conocidos:

- los fogs (sky y underwater) se aplican fuera del agua al mirar en tercera persona bajo el agua, lo mismo cuando el jugador se coloca bajo una piscina de agua de mas de 4 bloques de profundidad
- la transicion del ciclo de 24hs no es completamente correcta
- el fog bajo el agua esta roto en angelica/beddium 

## Unimixins example mod License
This example mod is available under the [CC0 license](LICENSE).
