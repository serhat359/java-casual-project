package casual;

import java.util.ArrayList;
import java.util.List;

public class TreeNode{

	private static final int maxDepth = 100;
	private List<TreeNode> nodeList;

	public TreeNode(List<TreeNode> nodeList){
		this.nodeList = nodeList;
	}

	public void print(){
		print(1, true, new char[maxDepth], 0);
	}

	private void print(int degree, boolean amILast, char[] fillerArray, int charCount){

		for(int i = 0; i < charCount; i++){
			System.out.print(fillerArray[i]);
		}

		System.out.print(amILast ? '└' : '├');
		System.out.println("░ " + degree);

		for(int i = 0; i < nodeList.size(); i++){
			char newFiller = amILast ? ' ' : '│';

			boolean newAmILast = i == nodeList.size() - 1;

			fillerArray[charCount] = newFiller;

			nodeList.get(i).print(degree + 1, newAmILast, fillerArray, charCount + 1);
		}
	}
	
	public static void test(){
		TreeNode root = new TreeNode(new ArrayList<TreeNode>(){{
			add(new TreeNode(new ArrayList<TreeNode>(){{
				add(new TreeNode(new ArrayList<TreeNode>()));
				add(new TreeNode(new ArrayList<TreeNode>(){{
					add(new TreeNode(new ArrayList<TreeNode>(){{
						add(new TreeNode(new ArrayList<TreeNode>()));
						add(new TreeNode(new ArrayList<TreeNode>()));
					}}));
					add(new TreeNode(new ArrayList<TreeNode>(){{
						add(new TreeNode(new ArrayList<TreeNode>(){{
							add(new TreeNode(new ArrayList<TreeNode>()));
						}}));
						add(new TreeNode(new ArrayList<TreeNode>(){{
							add(new TreeNode(new ArrayList<TreeNode>(){{
							}}));
							add(new TreeNode(new ArrayList<TreeNode>(){{
								add(new TreeNode(new ArrayList<TreeNode>()));
							}}));
						}}));
					}}));
					add(new TreeNode(new ArrayList<TreeNode>()));
				}}));
			}}));
			add(new TreeNode(new ArrayList<TreeNode>(){{
				add(new TreeNode(new ArrayList<TreeNode>(){{
					add(new TreeNode(new ArrayList<TreeNode>(){{
						add(new TreeNode(new ArrayList<TreeNode>()));
						add(new TreeNode(new ArrayList<TreeNode>()));
					}}));
					add(new TreeNode(new ArrayList<TreeNode>(){{
						add(new TreeNode(new ArrayList<TreeNode>(){{
							add(new TreeNode(new ArrayList<TreeNode>()));
						}}));
						add(new TreeNode(new ArrayList<TreeNode>(){{
							add(new TreeNode(new ArrayList<TreeNode>(){{
							}}));
							add(new TreeNode(new ArrayList<TreeNode>(){{
								add(new TreeNode(new ArrayList<TreeNode>()));
							}}));
						}}));
					}}));
				}}));
				add(new TreeNode(new ArrayList<TreeNode>()));
			}}));
		}});

		root.print();
	}
}
