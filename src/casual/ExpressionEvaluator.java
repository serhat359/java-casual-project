package casual;
import java.util.HashMap;

public class ExpressionEvaluator{
	private static HashMap<Character, Integer> ops = null;
	private TreeNode currentNode;
	private State state;

	private static enum State{
		ExpectingNumber, ExpectingOp
	}

	public ExpressionEvaluator(){
		if(ops == null){
			ops = new HashMap<Character, Integer>();
			ops.put('+', 1);
			ops.put('-', 1);
			ops.put('*', 2);
			ops.put('/', 2);
			ops.put('^', 3);
		}

		state = State.ExpectingNumber;
		currentNode = null;
	}

	public TreeNode makeTree(String text){
		for(int i = 0; i < text.length(); i++){
			char c = text.charAt(i);
			if(c == ' ')
				continue;
			if(state == State.ExpectingNumber && !isDigitOrParanthesis(c)
					|| state == State.ExpectingOp && isDigitOrParanthesis(c))
				throw new RuntimeException("Unexpected character");
			if(c == '('){
				int index = text.indexOf(')', i);
				String subString = text.substring(i + 1, index);
				add(new ExpressionEvaluator().makeTree(subString));
				i = index;
			}
			else
				add(new TreeNode(c));
		}
		while(currentNode.parent != null)
			currentNode = currentNode.parent;
		return currentNode;
	}

	private void add(TreeNode node){
		if(currentNode == null){
			currentNode = node;
			state = State.ExpectingOp;
			return;
		}
		else if(state == State.ExpectingNumber){
			currentNode.right = node;
			currentNode.right.parent = currentNode;
			state = State.ExpectingOp;
			currentNode = currentNode.right;
			return;
		}
		else if(state == State.ExpectingOp){
			state = State.ExpectingNumber;
			while(currentNode.parent != null
					&& ops.get(currentNode.parent.self) >= ops.get(node.self)){
				currentNode = currentNode.parent;
			}
			TreeNode n = node;
			n.parent = currentNode.parent;
			currentNode.parent = n;
			n.left = currentNode;
			if(n.parent != null)
				n.parent.right = n;
			currentNode = currentNode.parent;
			return;
		}
		throw new RuntimeException("Error occurred when adding the node");
	}

	public static boolean isDigitOrParanthesis(char c){
		return isDigit(c) || isParanthesis(c);
	}

	public static boolean isDigit(char c){
		return (c >= '0' && c <= '9');
	}

	public static boolean isParanthesis(char c){
		return c == '(';
	}
	
	public static void test(){
		ExpressionEvaluator parser = new ExpressionEvaluator();

		String text = "2+3*(5-2)/7-2+8*(2/2)";
		text = "(2*2-1)^3";

		System.out.print(parser.makeTree(text).calc());
	}

	class TreeNode{
		TreeNode left;
		TreeNode right;
		TreeNode parent;
		final char self;

		TreeNode(char c){
			self = c;
			left = right = parent = null;
		}

		int calc(){
			int valLeft = left != null ? left.calc() : 0;
			int valRight = right != null ? right.calc() : 0;

			if(ExpressionEvaluator.isDigit(self)){
				return self - '0';
			}
			if(self == '+')
				return valLeft + valRight;
			if(self == '-')
				return valLeft - valRight;
			if(self == '*')
				return valLeft * valRight;
			if(self == '/')
				return valLeft / valRight;
			if(self == '^')
				return (int)Math.pow(valLeft, valRight);
			throw new RuntimeException("Operation not supported");
		}
	}
}
