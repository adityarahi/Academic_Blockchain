import Includes.*;
import java.util.*;

public class MerkleTree{
	// Check the TreeNode.java file for more details
	public TreeNode rootnode;
	public int numstudents;

	public String Build(List<Pair<String,Integer>> documents){
		// Implement Code here
		CRF newobj = new CRF(64);
		int l = documents.size(); // l is no. of docs
		int d = (int)(Math.log(l) / Math.log(2)); // d is depth of tree
		TreeNode[][] TN = new TreeNode[d+1][l];
		for(int i = d; i >= 0; i--) {
			for(int j = 0; j < (int)Math.pow(2, i) ; j++) {
				TN[i][j] = new TreeNode();
				if(i == d) { // leaf
					TN[i][j].val = documents.get(j).get_first() + "_" + documents.get(j).get_second();
					TN[i][j].maxleafval = documents.get(j).get_second();
					TN[i][j].isLeaf = true;
					TN[i][j].numberLeaves = 1;
					TN[i][j].parent = null;
					TN[i][j].left = null;
					TN[i][j].right = null;
				}
				else { // not a leaf
					TN[i][j].val = newobj.Fn(TN[i+1][2*j].val + "#" + TN[i+1][2*j + 1].val);
					TN[i][j].parent =null;
					TN[i][j].left = TN[i+1][2*j];
					TN[i][j].right = TN[i+1][2*j + 1];
					TN[i][j].right.parent=TN[i][j];
					TN[i][j].left.parent=TN[i][j];
					TN[i][j].maxleafval = Math.max(TN[i][j].left.maxleafval,TN[i][j].right.maxleafval);
					TN[i][j].numberLeaves = TN[i][j].left.numberLeaves + TN[i][j].right.numberLeaves;
					TN[i][j].isLeaf = false;
				}
			}
		}
		this.rootnode = TN[0][0];
		this.numstudents = l;
		return this.rootnode.val;
	}

	/*
		Pair is a generic data structure defined in the Pair.java file
		It has two attributes - First and Second
	*/

	public String UpdateDocument(int student_id, int newScore){
		// Implement Code here
		TreeNode curr = this.rootnode;
		CRF newobj = new CRF(64);
		int n = this.numstudents;
		int k = student_id;
		while(n>=2) {
			if((n/2) <= k) {
				curr = curr.right;
				n = n/2;
				k = k - n;
			}
			else {
				curr = curr.left;
				n = n/2;
			}
		}
		curr.val = curr.val.substring(0, curr.val.length() - 2) + newScore;
		curr.maxleafval = newScore;
		curr = curr.parent;
		while(curr != null) {
			curr.val = newobj.Fn(curr.left.val + "#" + curr.right.val);
			curr.maxleafval = Math.max(curr.left.maxleafval, curr.right.maxleafval);
			curr = curr.parent;
		}
		return this.rootnode.val;
	}
}
