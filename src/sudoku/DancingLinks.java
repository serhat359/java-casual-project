package sudoku;

public class DancingLinks<T>{

	private class Node<E>{
		E val;

		Node<E> up;
		Node<E> down;
		Node<E> left;
		Node<E> right;

		public Node(E t){
			val = t;
			this.up = this.down = this.left = this.right = this;
		}
	}

	private Node<T> start;

	public DancingLinks(int width){
		start = new Node<T>(null);

		for(int i = width; i > 0; i--){
			Node<T> node = new Node<>(null);

			addToRight(start.left, node);
		}
	}

	private void addToRight(Node<T> node, Node<T> newNode){
		newNode.right = node.right;
		node.right.left = newNode;
		node.right = newNode;
		newNode.left = node;
	}

	private void removeH(Node<T> node){
		node.right.left = node.left;
		node.left.right = node.right;
	}

	private void removeV(Node<T> node){
		node.up.down = node.down;
		node.down.up = node.up;
	}

	private void print(){
		Node<T> pointer = start;

		while(pointer.right != pointer){
			System.out.print(pointer.val);
			removeH(pointer);
			pointer = pointer.right;
		}
	}

	public static void test(){
		DancingLinks<Integer> links = new DancingLinks<>(3);

		links.print();

	}
}
