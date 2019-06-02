import java.util.Scanner;
import java.io.*;

public class UnionFind {

	public static void main(String[] args) throws IOException {
		//Scanner in = new Scanner(new File("d1.txt")); // for testing
		Scanner in = new Scanner(System.in);
		UnionFind uf = null;
		
		boolean exit = false;
		while (!exit) {
			String line = in.nextLine();
			String [] tokens = line.split(" ");
			
			int key1, key2;
			boolean debug = true;
			
			switch(tokens[0]) {
				case "d":
					debug = true;
					key1 = Integer.parseInt(tokens[1]);
					uf = new UnionFind(key1);
					break;
				case "u":
					key1 = Integer.parseInt(tokens[1]);
					key2 = Integer.parseInt(tokens[2]);
					uf.union(key1, key2);
					break;
				case "f":
					key1 = Integer.parseInt(tokens[1]);
					if (debug)
						System.out.println(uf.find(key1));
					break;
				case "p":
					uf.printSets();
					break;
				case "s":
					uf.printStats();
					break;
				case "m":
					debug = false;
					key1 = Integer.parseInt(tokens[1]);
					key2 = Integer.parseInt(tokens[2]);
					
					int size = (int)(Math.pow(2, key1)); // number of rows and columns
					int numNodes = (int)(Math.pow(2, key1) * Math.pow(2, key1)); // total number of nodes
					uf = new UnionFind(numNodes);
					
					EdgeSet es = new EdgeSet(numNodes, key2); // store the maze edges
					
					while (uf.numberOfSets() != 1) { // while not one set
						int node1 = (int)(Math.random() * numNodes); // 1st node in edge
						int dir = (int)(Math.random() * 4); // direction of edges (0=up, 1=right, 2=down, 3=left)
						int node2 = generateEdge(numNodes, size, node1, dir); // 2nd node in edge
						
						if (node1 != node2) { // ensure randomly generated nodes are not the same
							if (node1 > node2) { // always make node1 < node2
								int temp = node1;
								node1 = node2;
								node2 = temp;
							}
							if (!uf.inSameSet(node1, node2)) { // check if node1 and node2 are in the same set
								uf.unionMaze(node1, node2);
								es.add(node1, node2);
							}
						}
					}
					es.print();
					break;
				case "e":
					exit = true;
					break;
				default:
					break;			
			}
		}
		
		in.close();

	}
	
	// generate the adjacent node based on the size of the maze, current node, and direction
	public static int generateEdge(int numNodes, int size, int node1, int dir) {
		if (dir == 0) { // up
			return (numNodes + (node1-size)) % numNodes;
		}
		else if (dir == 1) { // left
			if ((node1+1) % size == 0) // in the rightmost column
				return node1 - size + 1;
			else
				return node1 + 1;
		}
		else if (dir == 2) { // down
			return (node1+size) % numNodes;
		}
		else if (dir == 3) { // right
			if (node1 % size == 0) { // in the leftmost column
				return node1 + size - 1;
			}
			else {
				return node1 - 1;
			}
		}
		return -1;
	}

}

class UnionFind {
	int[] array;
	int numFinds = 0;
	int numProbes = 0;
	
	UnionFind(int n) {
		array = new int[n];
		for (int i=0; i<n; i++) // initialize to -1
			array[i] = -1;
	}
	
	// union by size function
	void union(int x, int y) {
		int xRoot = find(x);
		int xSize = Math.abs(array[xRoot]);
		int yRoot = find(y);
		int ySize = Math.abs(array[yRoot]);
		
		if (xRoot != yRoot) { // x and y are not in the same set
			if (ySize > xSize) { // make x a subtree of y
				array[yRoot] -= xSize;
				array[xRoot] = yRoot;
				System.out.println(yRoot + " " + Math.abs(array[yRoot]));
			}
			else { // make y a subtree of x
				array[xRoot] -= ySize;
				array[yRoot] = xRoot;
				System.out.println(xRoot + " " + Math.abs(array[xRoot]));
			}
		}
		else { // x and y are already in the same set
			if (ySize > xSize)
				System.out.println(yRoot + " " + Math.abs(array[yRoot]));
			else
				System.out.println(xRoot + " " + Math.abs(array[xRoot]));
		}
	}
	
	// union function for maze - no output to screen
	void unionMaze(int x, int y) {
		int xRoot = find(x);
		int xSize = Math.abs(array[xRoot]);
		int yRoot = find(y);
		int ySize = Math.abs(array[yRoot]);
		
		if (xRoot != yRoot) { // x and y are not in the same set
			if (ySize > xSize) { // make x a subtree of y
				array[yRoot] -= xSize;
				array[xRoot] = yRoot;
			}
			else { // make y a subtree of x
				array[xRoot] -= ySize;
				array[yRoot] = xRoot;
			}
		}
	}
	
	// find helper function
	int find(int y) {
		numFinds++; // keep track of how many times find is called
		return recursiveFind(y);
	}
	
	// recursively find the root of the tree containing node y
	int recursiveFind(int y) {
		numProbes++; // keep track of how many probes are made
		if (array[y] < 0)
			return y;
		else {
			int index = recursiveFind(array[y]);
			array[y] = index; // path compression
			return index;
		}
	}
	
	// return the number of sets left in the union-find structure
	int numberOfSets() {
		int count = 0;
		for (int i=0; i<array.length; i++) {
			if (array[i] < 0)
				count++;
		}
		return count;
	}
	
	// print statistics
	void printStats()  {
		System.out.printf("Number of sets remaining = %4d\n", numberOfSets());
		System.out.printf("Mean path length in find = %6.2f\n", (double)numProbes/(double)numFinds);
	}
	
	// print the array containing the set on one line, separated by spaces
	void printSets() {
		for (int i=0; i<array.length; i++) {
			if (i == 0)
				System.out.print(array[i]);
			else
				System.out.print(" " + array[i]);
		}
		System.out.println();
	}
	
	// return true if node1 and node2 are in the same set
	boolean inSameSet(int node1, int node2) {
		int root1 = find(node1);
		int root2 = find(node2);
		
		if (root1 == -1 && root2 == -1)
			return false;
		else if (root1 == -1 && root2 == node1)
			return true;
		else if (root2 == -1 && root1 == node2)
			return true;
		else if (root1 == root2)
			return true;
		return false;
	}
}

// class that keeps track of maze edges
class EdgeSet {
	
	int size;
	int maxWeight;
	int[][] edges;
	
	public EdgeSet (int s, int w) {
		size = s;
		maxWeight = w;
		edges = new int[size][5]; // 5th column contains the number of edges off of the node
		for (int i=0; i<size; i++) {
			for (int j=0; j<4; j++) {
				edges[i][j] = -1;
			}
			edges[i][4] = 0;
		}
	}
	
	// add edge <node1, node2> to data structure
	public void add(int node1, int node2) {
		if (edges[node1][0] == -1) // add first edge
			edges[node1][0] = node2;
		else if (edges[node1][1] == -1) { // add second edge
			if (edges[node1][0] > node2) {
				edges[node1][1] = edges[node1][0];
				edges[node1][0] = node2;
			}
			else
				edges[node1][1] = node2;
		}
		else if (edges[node1][2] == -1) { // add third edge
			if (edges[node1][0] > node2) {
				edges[node1][2] = edges[node1][1];
				edges[node1][1] = edges[node1][0];
				edges[node1][0] = node2;
			}
			else if (edges[node1][1] > node2) { 
				edges[node1][2] = edges[node1][1];
				edges[node1][1] = node2;
			}
			else
				edges[node1][2] = node2;
		}
			
		else if (edges[node1][3] == -1) { // add fourth edge
			if (edges[node1][0] > node2) {
				edges[node1][3] = edges[node1][2];
				edges[node1][2] = edges[node1][1];
				edges[node1][1] = edges[node1][0];
				edges[node1][0] = node2;
			}
			else if (edges[node1][1] > node2) {
				edges[node1][3] = edges[node1][2];
				edges[node1][2] = edges[node1][1];
				edges[node1][1] = node2;
			}
			else if (edges[node1][2] > node2) {
				edges[node1][3] = edges[node1][2];
				edges[node1][2] = node2;
			}
			else
				edges[node1][3] = node2;
		}
		else
			System.out.println("Edge is already in the Edge Set.");
		edges[node1][4]++; // increment number of edges in the row
	}
	
	// print the edges
	public void print() {
		for (int i=0; i<size; i++) { // iterate through rows
			System.out.print(edges[i][4]);
			if (edges[i][4] > 0) { // if numEdges > 0
				for (int j=0; j<4; j++) { // iterate through columns
					if (edges[i][j] != -1)
						System.out.print(" " + edges[i][j]);
				}
				
				// generate and print weights
				for (int k=0; k<edges[i][4]; k++) {
					int weight = (int)(Math.random() * maxWeight) + 1;
					System.out.print(" " + weight);
				}
			}
			System.out.println();
		}
	}
	
}
