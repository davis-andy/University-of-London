# Group 9/J Submission Notes

## Algorithms

The explore function used a version of the fringe/A-Star algorithms.  With the restriction of having to move the player to be able to see the neighbor nodes, the algorithms could not be used in their full potential.  This is why they are a "version" of the algorithm.  There were times where we had the funciton working 40% of the time because it would get stuck in a corner and see that the next node to get to would be a node already visited, but not a neighbor.
Thus, the priority had to change a bit and focus on those that were just neighbors.

The escape function is using a greedy version of Dijkstra's algorithm, where the algorithm is used to calculate the shortest path but reruns each time the player gets to a piece of gold.  To start, we search for the gold with the highest value within the time allotted to get there and to the exit,
then repeat the same step to search for the next highest value of gold so that we can maximize the score.  Luckily, this phase allows us to do all the calculations first before actually moving so that we do not lose any steps and can escape in time.
There were some helper functions and a helper class added to make the escape rounded out and perform optimally.

## Classes and Files Added or Modified

### `.gitignore`

Used `gitignore.io` to search for `Java`, `Gradle`, `Intellij`, `JetBrains`, `VisualStudio`, `VisualStudioCode`.  We did start with some members using both IntelliJ and some using Visual Studio Code, but in the end we all switched to IntelliJ but the `gitignore` still holds the VSCode files.
We did notice that the IDE ignore files from `gitignore.io` is not complete and still allowed in some folders, so there was an overall `.IDEfolder` included (e.g. `.idea`).

### `temple/src/main/java/student/Explorer.java`

The `explore` function was filled out, and a helper method called `determineNextMove` was added. `determineNextMove` determines the next move based on the current state and the set of visited nodes. It selects the unvisited neighboring node with the shortest distance to the target.
The `escape` function was also filled out, with `calculateRichestPath` and `calculateShortestPath` methods added.  `calculateRichestPath` finds the path to the largest piles of gold which can be reached while still leaving enough steps to reach the exit
`calculateShortestPath` calculates the shortest path from a start node to an end node using Dijkstra's algorithm.

### `temple/src/main/java/student/Path.java`

This class created to construct the path once the algorithm has done the calculations to figure out which path to take.  It also a function to calculate the score to help aid the algorithm in finding the highest scoring path to take.

### `.github/workflows/test.yml`

* GitHub Actions workflow used in pull requests to run `gradle test` as well as the headless solver.
* Uses official Gradle and GitHub actions and a `run` command for the application.
* Runs on every push to a branch with an open PR as well as every push to `main`.
* The headless solver is run 100 times in every workflow but only if the unit tests pass.
