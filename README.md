# Grands-Reseaux-Interaction - Large interaction networks
Theorie des graphes - tous les algorithmes pratiques.
Graph Theory - All practical Algorithms

## Contribution :
***Work in progress, feel free to participate and create Merge requests. Many issues are open.***
make sure that / Assurez-vous que :
- Tous les fichiers qui contiennent nos graphes sont en format ***Stanford***
- Il n y a pas de limite, le programme est capable de parser meme les gros graphes (testé sur un graphe de 2 Millions de noeuds et 6 Millions d'aretes parsé en 20 secondes sur un processeur i5 2.6Ghz et 6Go de Ram. La limite est la RAM alloué par votre JVM.  
- All the parsed data that contains our graphs are in ***Stanford*** format
- There is no limit, the program is able to parse even large graphs (tested on a graph of 2 Million nodes and 6 Million edges and was parsed in 21 seconds on a i5 cpu 2.6Ghz with 6Go of RAM. The limit is the RAM allocated by your JVM.

# TP1/ :
- Gaphe construction.
- Node's maximum degre.
- Number of Edges.
- Optimised way to loop over the Adjacency list.
- Degree distribution.

# TP2/ :
- A more optimised graph construction.
- Distance min between two nodes
- NPCC(S, T) (number of shortest paths between S and T)
- NPCC(S, V, T) (nnumber of shortest paths between S and T through V)
- Betweenness Centrality

# TP3/-TP4/
- Newman's Modularity
- Modularity Increment
- Louvain algorithm to build clusters with the best modularity possible.
- Clustring
- ...etc

# 3D Simulation screenshots :
Here, if you click on a node (Box) you can see the adjacency list (Arcs = Edges) rendered on screen and all data related to the selected node.
![Imgur](https://raw.githubusercontent.com/Aizen93/Grands-Reseaux-Interaction/master/TP4/Louvain/src/assets/images/3DSimulation.PNG)

# 2D Visualisation of the graph :
There are multiple algorithms and ways to render the graph on scene. On this screenshot you can see the clusters in which their nodes are randomly rendere in a certain range depending on how big the graph is.
![Imgur](https://imgur.com/q5HAPyo.png)
![Graphe](http://sna433.weebly.com/uploads/1/3/9/3/13930242/7562313_orig.png)

# Credits :
Some parts of the programm are taken from different gitHub repositories, All theses classes have there author's original name marked. But they are slightly modified and have more content and features made by me.
- [SimpleFPSCamera.java](https://github.com/FXyz/FXyz/blob/master/FXyz-Core/src/main/java/org/fxyz3d/scene/SimpleFPSCamera.java) : developed by Jason Pollastrini aka jdub1581 it also uses the MathUtils.java Class

