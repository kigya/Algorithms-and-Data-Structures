# Algorithms And Data Structures

BSU Course

## Term 5

---

<aside>
1️⃣ 
**Task 1.1.** Implement one of the hybrid algorithms combining quick sort and insertion sort as follows: in the quick sort algorithm, sections of an array of length less than some parameter k are sorted by insertion sort, without using fast sort recursion for them.

Do a computational experiment. Find the optimal k for sorting R arrays of length N, whose elements are random integers in the range from 0 to M.

Enable the user to set the parameters R,N and M.

**Task 1.2.** Implement one of the hybrid algorithms combining merge sorting and insertion sorting as follows: in the merge sorting algorithm, sections of an array of length less than some parameter k are sorted by insertion sorting, without using merge sorting recursion for them.

Do a computational experiment. Find the optimal k for sorting R arrays of length N, whose elements are random integers in the range from 0 to M.

Enable the user to set the parameters R,N and M.

**Task 1.3.** Count the number of elementary operations in your implementation of sorting by inserts.

</aside>

<aside>
2️⃣ 
**Task 2.1.** Implement algorithms for binary and interpolation search for the number x in an array of length N, the elements of which are random integers in the range from 0 to M. Print the number of comparison operations performed by the algorithm for the given N, M and x.

**Task 2.2.** Implement algorithms for constructing, traversing and balancing the binary search tree (BST). A sequence of positive integers a_1,a_2,...,a_n is fed to the input of the algorithm. The program should build a BST by adding nodes in the order of the sequence. Implement tree traversals in ascending nodes and descending nodes. Implement an algorithm for finding the k-th minimum key in the tree; on its basis, balance the constructed tree (rotations to the right and left n/2-the minimum element is placed at the root, then this procedure is recursively repeated for subtrees with roots in child nodes).

**Task 2.3.** Implement a hashing algorithm by multiplication with collision resolution by overflow chains, linear probing and double hashing. In a computational experiment, choose your own constant for the multiplication method, compare it with Knuth's constant for the longest length of collision chains (sets of keys with an equal hash value) for P sets of N random keys from 1 to R, with the length of the hash table M. 

</aside>

<aside>
3️⃣ 
**Task 3.1.** Graph G is defined by lists of vertex adjacencies. Find the connectivity components of the graph G. Determine whether the graph G is Eulerian; if the graph G is Eulerian, construct an Eulerian cycle. Determine whether the graph G is bipartite; if G is bipartite, find the partition into fractions.

**Task 3.2.** There are N intersections and M roads in the city (each road begins and ends with an intersection, the roads have a direction). The travel time of each road is known (the travel time of roads i->j and j->i may be different). Determine the intersection for the location of the fire station on it with the condition: the fire truck must get to the intersection farthest from the station in the shortest possible time (the fire truck may violate traffic regulations and go in the opposite direction). The task is implemented by 2 algorithms.

**Task 3.3.** There are N nodes that need to be combined into a network. The cost of laying a fiber-optic cable between any pair of nodes is known. It is required to design a connected network (a network between any nodes of which a signal can be transmitted) of minimal cost. The task is implemented by 2 algorithms.

**Task 3.4.** There are K employees and K tasks. For each employee i, tasks N(i) are defined that he can perform. Assign tasks to employees so that each employee only works on one task and all tasks are completed. If such an appointment is not possible, specify which employee needs to be trained in which task in order to be able to get the desired appointment.

**Task 3.5.** The company has N employees and M tasks to perform. Each employee x has a list of interest N(x) in working on tasks that he knows how to perform (in descending order of interest). For each task y, a list of the effectiveness of employees S(y) who are able to perform this task is known (in descending order of efficiency). No more than one employee can work on each task, and each employee can work on no more than one task. Conduct 2 different distributions of the maximum number of tasks for employees in the company in accordance with the principles:

1. Employee x could be assigned to perform task t only if all the more interesting tasks for him were assigned to other employees more effective for their performance than x.
2. Task t is assigned to employee x only if all employees who are more effective at completing task t have been assigned to other tasks that are more interesting to them.
</aside>
