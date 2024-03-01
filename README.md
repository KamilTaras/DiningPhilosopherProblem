# Dining Philosopher Problem
Dining philosopher problem, written in **Java**, going to be rewritten in **Scala**. Eventually there is going to be API which is going to visualize philosopher on **Flutter** mobile application.

Dining Philosopher Problem is a classic problem which shows problem of concurrency in programming.
At the beginning solve it for giving priority to each philosopher (I followed some variation of first dinning philosopher problem), but, following **Wikipedia**:

## Problem Statement
Five philosophers dine together at the same table. Each philosopher has his own plate at the table. There is a fork between each plate. The dish served is a kind of spaghetti which has to be eaten with two forks. Each philosopher can only alternately think and eat. Moreover, a philosopher can only eat his spaghetti when he has both a left and right fork. Thus two forks will only be available when his two nearest neighbors are thinking, not eating. After an individual philosopher finishes eating, he will put down both forks. The problem is how to design a regimen (a concurrent algorithm) such that any philosopher will not starve; i.e., each can forever continue to alternate between eating and thinking, assuming that no philosopher can know when others may want to eat or think (an issue of incomplete information).

At this moment it looks like this:

![idea64_tGZs3wsil1](https://github.com/KamilTaras/DinningPhilosopherProblem/assets/116663154/439bc5b8-05da-429f-9191-329aca968275)
