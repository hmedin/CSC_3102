import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Route {
	public static class Vertex {
		char[] airport_code = new char[3];
		int heap_pos;
		int hash_pos;
		boolean visited;
		int dvalue;
		List<Edge> adjList = new LinkedList<Edge>();
		char[] parent = new char[3];

		Vertex(char[] AC) {
			visited = false;
			airport_code = AC;
			dvalue = Integer.MAX_VALUE;
		}

		public void addEdge(Edge e) {
			adjList.add(e);
		}

		public void printlist() {
			for (Edge e : adjList) {
				System.out.println(e.dest);
			}
		}
	}

	public static class Edge {
		char[] origin = new char[3];
		char[] dest = new char[3];
		String airlines;
		int fltno;
		int deptime;
		int arrtime;
		Edge next;

		Edge(char[] ori, char[] desti, String AL, int fltnum, int leave, int arrive) {
			origin = ori;
			dest = desti;
			airlines = AL;
			fltno = fltnum;
			deptime = leave;
			arrtime = arrive;
		}
	}

	static int myhash(char[] c) {
		int p0 = (int) c[0] - 'A' + 1;
		int p1 = (int) c[1] - 'A' + 1;
		int p2 = (int) c[2] - 'A' + 1;
		int p3 = p0 * 467 * 467 + p1 * 467 + p2;
		int p4 = p3 % 7193;
		int p5 = p4 % 1000;
		return p5;

	}

	public static class heap {
		private Vertex[] array;
		private int arraySize;
		private int heapSize;
		hashTable ht;

		public heap(int arraySze, hashTable hashTable) {
			arraySize = arraySze;
			heapSize = 0;
			array = new Vertex[arraySize];
			ht = hashTable;
		}

		void insertHeap(Vertex heapEle) {
			array[heapSize] = heapEle;
			floatUp(heapSize);
			heapSize++;
		}

		Vertex extractmin() {
			heapSize--;
			Vertex min = array[0];
			array[0] = array[heapSize];
			array[0].heap_pos = 0;
			sinkDown(0, heapSize);
			return min;

		}

		void swap(int v1, int v2) {
			Vertex temp = array[v1];
			array[v1] = array[v2];
			array[v2] = temp;

			Vertex ele1 = array[v1];
			Vertex ele2 = array[v2];

			ele1.heap_pos = v1;
			ele2.heap_pos = v2;

		}

		void floatUp(int index) {
			if (index < 0) {
				return;
			}
			int currentIndex = index;
			int parentIndex;
			Vertex currentEle;
			Vertex parentEle;
			while (currentIndex != 0) {
				parentIndex = (currentIndex - 1) / 2;
				parentEle = array[parentIndex];
				currentEle = array[currentIndex];

				if (currentEle.dvalue < parentEle.dvalue) {
					swap(parentIndex, currentIndex);
					currentIndex = parentIndex;
				} else {
					break;
				}
			}

		}

		void decreasekey(Vertex v) {
			int heapIndex = v.heap_pos;
			floatUp(heapIndex);

		}

		void sinkDown(int root, int size) {
			Vertex minkid, currentEle;
			int currentIndex = root;
			int minkidIndex, otherkidIndex;
			currentEle = array[currentIndex];
			minkidIndex = currentIndex * 2 + 1;
			if (minkidIndex > size) {
				return;
			}
			if (minkidIndex < size) {
				otherkidIndex = minkidIndex + 1;
				if (array[minkidIndex].dvalue < array[otherkidIndex].dvalue) {
					minkid = array[minkidIndex];
				} else {
					minkid = array[otherkidIndex];

				}
				if (currentEle.dvalue < minkid.dvalue) {
					return;
				}
				swap(currentIndex, minkidIndex);
			}

		}
	}

	public static class hashTable {
		int tableSize;
		Vertex[] hasharray;

		public hashTable(int tblSze) {
			tableSize = tblSze;
			hasharray = new Vertex[tblSze];

		}

		public void insertHash(Vertex v) {

			int hash = myhash(v.airport_code);
			if (hasharray[hash] == null) {
				hasharray[hash] = v;
				hasharray[hash].hash_pos = hash;
			} else
				while (hasharray[hash] != null) {
					hash = hash + 1;
				}
			hasharray[hash] = v;
			hasharray[hash].hash_pos = hash;
		}

		 Vertex getVertex(char[] code) {
			int p = myhash(code);

			if (hasharray[p] == null) {
				return null;
			} else if (Arrays.equals(hasharray[p].airport_code, code)) {
				return hasharray[p];
			}
			for (int i = p + 1; !(i == p); i++) {

				if (i >= 1000)
					i = i % 1000;// rolling over

				if (Arrays.equals(hasharray[i].airport_code, code)) {
					return hasharray[i];
				}
			}
			return null;
		}

	}

	public static void dijks(hashTable ht, Vertex start) {

		heap heap = new heap(1000, ht);

		for (int i = 0; i < 1000; i++) {
			if (ht.hasharray[i] != null) {
				Vertex v = ht.hasharray[i];
				if (v.equals(start)) {
					v.dvalue = 0;
					v.parent=null;
				}
				heap.insertHeap(v);

			}

		}

		while (heap.heapSize != 0) {
			Vertex v = heap.extractmin();
			for (int j = 0; j < v.adjList.size(); j++) {
				Edge e = v.adjList.get(j);
				relax(ht, heap, e);
			}
		}

	}

	static void relax(hashTable ht, heap h, Edge e) {
		Vertex v = ht.getVertex(e.origin);
		if (v.dvalue >= e.deptime) {
			return;
		}
		v = ht.getVertex(e.dest);
		if (v.dvalue > e.arrtime) {
			v.dvalue = e.arrtime;
			h.decreasekey(v);
			v.parent = e.origin;
		}

	}
	static void getPath(Vertex start, Vertex end,hashTable ht) {
		String[] flightpath = new String[10];
		flightpath[0] = String.valueOf(end.airport_code);
		int time = end.dvalue;
		int i = 1;
		while (!Arrays.equals(end.airport_code, start.airport_code)) {
			flightpath[i] = String.valueOf(end.parent);
			end = ht.getVertex(end.parent);
			i++;
		}
		for (int j = i - 1; j >= 0; j--) {
			System.out.print(flightpath[j]);
			System.out.print("-");
		}
		System.out.println(time);
		
	}

	public static void main(String[] args) throws FileNotFoundException {

		hashTable ht = new hashTable(1000);

		int numAirports = 0;
		File input = new File("airports.txt");
		Scanner in = new Scanner(input);
		if (in.hasNextLine()) {
			numAirports = Integer.parseInt(in.nextLine());
		}

		while (numAirports >= 1 && in.hasNextLine()) {
			String airport = in.nextLine();
			char[] ch = airport.toCharArray();
			Vertex air = new Vertex(ch);
			ht.insertHash(air);
			numAirports--;
		}

		int numFlights = 0;
		File inputv2 = new File("flights.txt");
		Scanner inv2 = new Scanner(inputv2);
		if (inv2.hasNextLine()) {
			numFlights = Integer.parseInt(inv2.nextLine());
		}
		inv2.nextLine();// used to skip header line
		while (numFlights >= 1 && inv2.hasNextLine()) {
			String flight = inv2.nextLine();
			String[] fields = flight.split("	");
			Vertex v = ht.getVertex(fields[2].toCharArray());
			Edge es = new Edge(fields[2].toCharArray(), fields[3].toCharArray(), fields[0], Integer.parseInt(fields[1]),
					Integer.parseInt(fields[4]), Integer.parseInt(fields[5]));
			v.addEdge(es);
			numFlights--;
		}
		Vertex start = ht.getVertex(args[0].toUpperCase().toCharArray());

		dijks(ht, start);

		Vertex end = ht.getVertex(args[1].toUpperCase().toCharArray());

		getPath(start, end, ht);
		
		in.close();
		inv2.close();


	}

}
