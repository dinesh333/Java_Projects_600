package AVLTree;

public class AVLTreeTest {
	public static void main(String[] args) {
		AVLTree tree = new AVLTree();
		tree.insert(100);
		tree.insert(50);
		tree.insert(200);
		tree.insert(25);
		tree.insert(75);
		tree.insert(150);
		tree.insert(300);
		tree.insert(300);
		tree.preOrderTraversal();
		tree.inOrderTraversal();
		tree.delete(100);
		tree.delete(50);
		tree.delete(300);
		tree.preOrderTraversal();
		tree.inOrderTraversal();
	}
}

class AVLNode {
	int data, height, frequency;
	AVLNode left, right;

	AVLNode(int data) {
		left = null;
		this.data = data;
		height = -1;
		right = null;
		frequency = 1;
	}
}

class AVLTree{
	private AVLNode root;
	
	AVLTree(){
		root = null;
	}
	
	public void insert(int data){
		root = insert(data, root);
	}

	private AVLNode insert(int data, AVLNode current) {
		if(current == null){
			current = new AVLNode(data);
		} else if (current.data > data){
			current.left = insert(data, current.left);
		} else if(current.data < data){
			current.right = insert(data, current.right);
		} else {
			//data already exists in tree.
			current.frequency++;
			return current;
		}
		current.height = maxHeight(getHeight(current.left), getHeight(current.right)) + 1;
		
		//Check if tree unbalanced
		int currentBalance = checkBalance(current);
		
		if(currentBalance > 1 && current.left.data > data){
			current = rotateNodeToRight(current);
			System.out.println("Insertion, case 1: Left-Left => Single rotation to right.");
		}else if(currentBalance > 1 && current.left.data < data){
			current.left = rotateNodeToLeft(current.left);
			current = rotateNodeToRight(current);
			System.out.println("Insertion, case 2: Left-Right => Single rotation to left. Single rotation to right.");
		}else if(currentBalance < -1 && current.right.data > data){
			current.right = rotateNodeToRight(current.right);
			current = rotateNodeToLeft(current);
			System.out.println("Insertion, case 3: Right-left => Single rotation to right. Single rotation to left.");
		}else if(currentBalance < -1 && current.right.data < data){
			current = rotateNodeToLeft(current);
			System.out.println("Insertion, case 4: Right-Right case => Single rotation to left.");
		}
		return current;
	}
	
	//Get height of a node
	private int getHeight(AVLNode node){
		if(node == null)
			return -1;
		else
			return node.height;
	}
	
	//Get maximum height out of two heights
	private int maxHeight(int leftHeight, int rightHeight){
		if(leftHeight > rightHeight)
			return leftHeight;
		return rightHeight;
	}
	
	private int checkBalance(AVLNode node){
		if(node == null)
			return 0; 
		return getHeight(node.left) - getHeight(node.right);
	}
	
	//Rotate node a towards the right direction.
	private AVLNode rotateNodeToRight(AVLNode a){
		AVLNode b = a.left;
		AVLNode br = b.right;
		b.right = a;
		a.left = br;
		
		a.height = maxHeight(getHeight(a.left), getHeight(a.right)) + 1;
		b.height = maxHeight(getHeight(b.left), getHeight(b.right)) + 1;
		return b;
	}
	
	//Rotate node a towards the left direction.
	private AVLNode rotateNodeToLeft(AVLNode a){
		AVLNode b = a.right;
		AVLNode bl = b.left;
		b.left = a;
		a.right = bl;
		
		a.height = maxHeight(getHeight(a.left), getHeight(a.right)) + 1;
		b.height = maxHeight(getHeight(b.left), getHeight(b.right)) + 1;
		return b;
	}
	
	public void delete(int data){
		if(root == null)
			System.out.println("AVL Tree is empty.");
		else 
			root = delete(data, root);
	}
	
	private AVLNode delete(int data, AVLNode current){
		if(current == null){
			return null;
		}else if(current.data > data){
			current.left = delete(data, current.left);
		}else if(current.data < data){
			current.right = delete(data, current.right);
		}else{
			//current node has the data to be deleted
			
			//current node has no children
			if(current.left == null && current.right == null){
				return null;
			}
			//current node has right child only
			else if(current.left == null && current.right != null){
				return current.right;
			}
			//current node has left child only
			else if(current.left != null && current.right == null){
				return current.left;
			}
			//current node has left and right children
			else{
				//find node with largest data on left subtree of current node.
				AVLNode replacingNode = findNodeWithLargest(current.left);
				current.data = replacingNode.data;
				current.left = delete(replacingNode.data, current.left);
			}
		}
		current.height = maxHeight(getHeight(current.left), getHeight(current.right)) + 1;
		
		//Check if tree unbalanced
		int currentBalance = checkBalance(current);
		
		if(currentBalance > 1 && checkBalance(current.left) >= 0){
			current = rotateNodeToRight(current);
			System.out.println("Deletion, case 1: Left-Left => Single rotation to right.");
		}else if(currentBalance > 1 && checkBalance(current.left) < 0){
			current.left = rotateNodeToLeft(current.left);
			current = rotateNodeToRight(current);
			System.out.println("Deletion, case 2: Left-Right => Single rotation to left. Single rotation to right.");
		}else if(currentBalance < -1 && checkBalance(current.right) > 0){
			current.right = rotateNodeToRight(current.right);
			current = rotateNodeToLeft(current);
			System.out.println("Deletion, case 3: Right-left => Single rotation to right. Single rotation to left.");
		}else if(currentBalance < -1 && checkBalance(current.right) <= 0){
			current = rotateNodeToLeft(current);
			System.out.println("Insertion, case 4: Right-Right case => Single rotation to left.");
		}
		
		return current;
	}
	
	private AVLNode findNodeWithLargest(AVLNode current){
		while(current.right != null)
			current = current.right;
		return current;
	}
	
	public void inOrderTraversal(){
		inOrderTraversal(root);
	}
	
	private void inOrderTraversal(AVLNode current){
		if(current != null){
			inOrderTraversal(current.left);
			System.out.println(current.data + ": (" + current.frequency + ")");
			inOrderTraversal(current.right);
		}
	}
	
	public void preOrderTraversal(){
		preOrderTraversal(root, 0);
	}
	
	private void preOrderTraversal(AVLNode current, int space){
		if(current != null){
			for(int i = 0; i < space; i++){
				System.out.print("   ");
			}
			System.out.println(current.data);
			preOrderTraversal(current.left, space+1);
			preOrderTraversal(current.right, space+1);
		}
	}
}