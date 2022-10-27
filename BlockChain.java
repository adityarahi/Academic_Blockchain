import Includes.*;
import java.util.*;

public class BlockChain{
	public static final String start_string = "LabModule5";
	public Block firstblock;
	public Block lastblock;

	public String InsertBlock(List<Pair<String,Integer>> Documents, int inputyear){
		/*
			Implement Code here
		*/
		CRF newobj = new CRF(64);
		Block newblock = new Block();
		newblock.year = inputyear;
		newblock.mtree = new MerkleTree();
		newblock.mtree.Build(Documents);
		newblock.value = newblock.mtree.rootnode.val + "_" + newblock.mtree.rootnode.maxleafval;
		if(this.lastblock == null) {
			newblock.dgst = newobj.Fn(start_string + "#" + newblock.value);
			this.firstblock = newblock;
		}
		else {
			newblock.dgst = newobj.Fn(this.lastblock.dgst + "#" + newblock.value);
			this.lastblock.next = newblock;
			newblock.previous = this.lastblock;
		}
		this.lastblock = newblock;
		return this.lastblock.dgst;
	}

	public Pair<List<Pair<String,String>>, List<Pair<String,String>>> ProofofScore(int student_id, int year){
		// Implement Code here
		ArrayList<Pair<String,String>> pathToRoot = new ArrayList<>();
		ArrayList<Pair<String,String>> proofBlocks = new ArrayList<>();
		Block curr = firstblock;
		while(curr != null) {
			if(curr.year == year) {break;}
			curr = curr.next;
		}
		if(curr != null) {
			TreeNode curr1 = curr.mtree.rootnode;
			int n = curr.mtree.numstudents;
			int k = student_id;
			boolean initial = true;
			while(n >= 2) {
				if(initial) {
					pathToRoot.add(0,new Pair<String,String>(curr1.val,null));
					initial = false;
				}
				pathToRoot.add(0,new Pair<String,String>(curr1.left.val, curr1.right.val));
				if((n/2) <= k) {
					curr1 = curr1.right;
					n = n/2;
					k = k - n;
				}
				else {
					curr1 = curr1.left;
					n = n/2;
				}
			}
			while(curr!=null) {
				proofBlocks.add(0, new Pair<String, String>(curr.value, curr.dgst));
				curr = curr.next;
			}
			return new Pair<List<Pair<String,String>>, List<Pair<String,String>>>(pathToRoot, proofBlocks);
		}
		else{	return null; }
	}
}
