import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;



public class AVL {
	public static class Node {
		int key, height, data, mindata;
		Node left, right;

		Node(int k, int d) {
			key = k;
			data = d;
			mindata = d;
			height = 1;
		}
	}

	public static class AVLTree {

		Node root;

		// get the height of the tree
		private int height(Node N) {
			if (N == null)
				return 0;

			return N.height;
		}

		// get max of two integers
		private int max(int a, int b) {
			if (a > b) {
				return a;
			} else {
				return b;
			}
		}

		// right rotate
		private Node rightRotate(Node y) {
			Node x = y.left;
			Node z = x.right;

			// perform rotation
			x.right = y;
			y.left = z;

			// update heights
			y.height = max(height(y.left), height(y.right)) + 1;
			x.height = max(height(x.left), height(x.right)) + 1;

			// update mindatas
			reminData(y);
			reminData(x);

			// return new root
			return x;
		}

		// left rotate
		private Node leftRotate(Node y) {
			Node x = y.right;
			Node z = x.left;

			// perform rotation
			x.left = y;
			y.right = z;

			// update heights
			y.height = max(height(y.left), height(y.right)) + 1;
			x.height = max(height(x.left), height(x.right)) + 1;

			// update mindatas
			reminData(y);
			reminData(x);

			// Return new root
			return x;
		}

		// get balance factor of node
		private int getBalance(Node N) {
			if (N == null)
				return 0;

			return height(N.left) - height(N.right);
		}

		public Node insert(Node node, int key, int data) {

			// normal BST insertion
			if (node == null)
				return (new Node(key, data));

			if (key < node.key)
				node.left = insert(node.left, key, data);
			else if (key > node.key)
				node.right = insert(node.right, key, data);
			else
				return node;

			// Update height of this ancestor node
			node.height = 1 + max(height(node.left), height(node.right));

			// get the balance factor of this ancestor node
			int balance = getBalance(node);

			// re-balancing node
			if (balance > 1 && key < node.left.key)
				return rightRotate(node);

			if (balance < -1 && key > node.right.key)
				return leftRotate(node);

			if (balance > 1 && key > node.left.key) {
				node.left = leftRotate(node.left);
				return rightRotate(node);
			}

			if (balance < -1 && key < node.right.key) {
				node.right = rightRotate(node.right);
				return leftRotate(node);
			}
			reminData(node);
			return node;
		}

		void reminData(Node node) {
			if ((node.left == null) && (node.right == null)) {
				node.mindata = node.data;
			} else if (node.left == null) {
				node.mindata = (node.right.mindata < node.data) ? node.right.mindata : node.data;
			} else if (node.right == null) {
				node.mindata = (node.left.mindata < node.data) ? node.left.mindata : node.data;
			} else {
				int min = node.data;
				if (node.left.mindata < min) {
					min = node.left.mindata;
				}
				if (node.right.mindata < min) {
					min = node.right.mindata;
				}
				node.mindata = min;
			}
		}

		public int rangeMinData(Node node, int key1, int key2) {
			int min = Integer.MAX_VALUE;
			if (node == null || key1>key2) {
				return min;
			}
			while((node.key<key1)||(node.key>key2)) {
				if(node.key<key1) {
					node=node.right;
				}
				if(node.key>key2) {
					node=node.left;
				}
			}
			List<Integer> minis = new ArrayList<Integer>(); 
			minis.add(node.data);
			Node y = node.left;
			while((y!=null)&&(y.key!=key1)) {
				if(key1<=y.key) {
					minis.add(y.data);
					if(y.right!=null) {
						minis.add(y.right.mindata);
					}
					y=y.left;
				}else {
					y=y.right;
				}		
			}
			y=node.right;
			while((y!=null)&&(y.key!=key2)) {
				if(key2>=y.key) {
					minis.add(y.data);
					if(y.left!=null) {
						minis.add(y.left.mindata);
					}
					y=y.right;
				}else {
					y=y.left;
				}
			}
			min=Collections.min(minis);
			return min;
			

		}

	}

	public static void main(String[] args) throws FileNotFoundException {
		int numInstructions = 0;
		int min = 0;
		File input = new File(args[0]);
		Scanner in = new Scanner(input);
		if (in.hasNextLine()) {
			numInstructions = Integer.parseInt(in.nextLine());
		}
		AVLTree tree = new AVLTree();

		while (numInstructions >= 1 && in.hasNextLine()) {
			String instructions = in.nextLine();
			String[] commands = instructions.split(" ");
			if (commands[0].equals("IN")) {
				tree.root = tree.insert(tree.root, Integer.parseInt(commands[1]), Integer.parseInt(commands[2]));
			} else if (commands[0].equals("RM")) {
				min = tree.rangeMinData(tree.root, Integer.parseInt(commands[1]), Integer.parseInt(commands[2]));
			}
			numInstructions--;
		}
		System.out.println(min);

		in.close();
	}

}