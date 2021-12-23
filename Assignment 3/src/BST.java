import edu.princeton.cs.algs4.Queue;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class BST<Key extends Comparable<Key>, Value> {
    private Node root; // root of BST
    LinkedList<Doc> docList=new LinkedList<Doc>(); //List of all docs in the tree
    int docNum=0; //Number of docs in the tree

    private class Node {
        private Key key; // key
        private Value val; // associated value
        private Node left, right; // links to subtrees
        private int N; // # nodes in subtree rooted here

        public Node(Key key, Value val, int N) {
            this.key = key;
            this.val = val;
            this.N = N;
        }
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0; else return x.N;
    }

    public boolean contains(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return get(key) != null;
    }

    public Value get(Key key) {
        return get(root, key);
    }

    private Value get(Node x, Key key) {
        // Return value associated with key in the subtree rooted at x;
        // return null if key not present in subtree rooted at x.
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            return get(x.left, key);
        } else if (cmp > 0) {
            return get(x.right, key);
        } else {
            return x.val;
        }
    }

    public String getRandomKey(){//Problem
        Iterable<String> keys = (Iterable<String>) this.levelOrder();
        List<String> result = StreamSupport.stream(keys.spliterator(), false)
                .collect(Collectors.toList());
        int index= ThreadLocalRandom.current().nextInt(0, result.size());
        return result.get(index);
    }

    public void put(Key key, Value val) {
        // Search for key. Update value if found; grow table if new.
        root = put(root, key, val);
    }

    private Node put(Node x, Key key, Value val) {
        // Change key’s value to val if key in subtree rooted at x.
        // Otherwise, add new node to subtree associating key with val.
        if (x == null) {
            return new Node(key, val, 1);
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = put(x.left, key, val);
        } else if (cmp > 0) {
            x.right = put(x.right, key, val);
        } else {
            x.val = val;
        }
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    public Key min() {
        return min(root).key;
    }

    private Node min(Node x) {
        if (x.left == null) {
            return x;
        }
        return min(x.left);
    }

    public Key max() {
        if (isEmpty()) throw new NoSuchElementException("calls max() with empty symbol table");
        return max(root).key;
    }

    private Node max(Node x) {
        if (x.right == null) return x;
        else                 return max(x.right);
    }
    /*
    public Key floor(Key key) {
        Node x = floor(root, key);
        if (x == null) {
            return null;
        }
        return x.key;
    }

    private Node floor(Node x, Key key) {
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp == 0) {
            return x;
        }
        if (cmp < 0) {
            return floor(x.left, key);
        }
        Node t = floor(x.right, key);
        if (t != null) {
            return t;
        } else {
            return x;
        }
    }
    */
    public Key select(int k) { return select(root, k).key; }

    private Node select(Node x, int k) {
        // Return Node containing key of rank k.
        if (x == null) {
            return null;
        }
        int t = size(x.left);
        if (t > k) {
            return select(x.left, k);
        } else if (t < k) {
            return select(x.right, k-t-1);
        } else {
            return x;
        }
    }

    public int rank(Key key) { return rank(key, root); }

    private int rank(Key key, Node x) {
        // Return number of keys less than x.key in the subtree rooted at x.
        if (x == null) return 0;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            return rank(key, x.left);
        }
        else if (cmp > 0) {
            return 1 + size(x.left) + rank(key, x.right);
        } else {
            return size(x.left);
        }
    }
    /*
    public void deleteMax() {
        if (isEmpty()) throw new NoSuchElementException("Symbol table underflow");
        root = deleteMax(root);
        assert check();
    }

    private Node deleteMax(Node x) {
        if (x.right == null) return x.left;
        x.right = deleteMax(x.right);
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    public void deleteMin() {
        root = deleteMin(root);
    }

    private Node deleteMin(Node x) {
        if (x.left == null) {
            return x.right;
        }
        x.left = deleteMin(x.left);
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    public void delete(Key key) {
        root = delete(root, key);
    }

    private Node delete(Node x, Key key) {
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = delete(x.left, key);
        } else if (cmp > 0) {
            x.right = delete(x.right, key);
        } else {
            if (x.right == null) {
                return x.left;
            }
            if (x.left == null) {
                return x.right;
            }
            Node t = x;
            x = min(t.right);
            x.right = deleteMin(t.right);
            x.left = t.left;
        }
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }*/

    public Queue<Key> keys() {
        return keys(min(), max());
    }

    public Queue<Key> keys(Key lo, Key hi) {
        Queue<Key> queue = new Queue<Key>();
        keys(root, queue, lo, hi);
        return queue;
    }

    private void keys(Node x, Queue<Key> queue, Key lo, Key hi) {
        if (x == null) {
            return;
        }
        int cmplo = lo.compareTo(x.key);
        int cmphi = hi.compareTo(x.key);
        if (cmplo < 0) {
            keys(x.left, queue, lo, hi);
        } if (cmplo <= 0 && cmphi >= 0) {
            queue.enqueue(x.key);
        } if (cmphi > 0) {
            keys(x.right, queue, lo, hi);
        }
    }

    public void loadDocs(String allDocs, BST<String, Doc> bst){
        String stream=null;
        try {
            stream = new String(Files.readAllBytes(Paths.get(allDocs)));
            String[] lines = stream.split("\\r?\\n");
            for(String line :lines){
                String[] arr=line.split(" ");
                Doc doc=new Doc();
                doc.doc(arr[0],arr[1]);
                bst.put(arr[0],doc);
                doc.readDoc();
                docList.add(doc);
                docNum++;
            }
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public int countOccurences(String term, String[] doc){
        int result=0;
        for(Key word:(Key[]) doc){
            if(word!=null){
                if(term.equals(word)){
                    result++;
                }
            }
        }
        return result;
    }

    public int countTotalOccurences(String word){
        int total=0;
        for(int i=0;i< docList.size();i++) {
            Doc doc=docList.get(i);
            if (doc != null) {
                if(doc.termTree.get(word)!=null){
                    Term term=(Term)doc.termTree.get(word);
                    total+=term.docFrequency;
                }
            }
        }
        //System.out.println("Total entries of '"+word+ "': " + total);
        return total;
    }

    public void buildTermTable(BST DocFreq){
        for(Doc doc: docList) {
            if (doc != null) {
                //System.out.println(doc.fileName);
                Queue<Key> terms=doc.termTree.keys();
                for(Key word:terms){
                    Term w = ((Term) doc.termTree.get(word));
                    w.totalDocs = docNum;
                    w.doc = doc;
                    w.totalOccurences = this.countTotalOccurences(w.word);
                    w.bst=this;
                    w.score=w.tfTimesIDFBST();
                    DocFreq.put(word,get(word));
                }
            }
        }
        scoreTerms(DocFreq);
    }

    public void scoreTerms(BST DocFreq){
        for(Doc doc: docList) {
            if (doc != null) {
                Iterable<String> keys = (Iterable<String>) doc.termTree.levelOrder();
                for (String word : keys) {
                    if(word!=null && doc.termTree.get(word)!=null ) {
                      //  System.out.println("SCORING!!!");
                        Term term = (Term) doc.termTree.get(word);
                        term.totalDocs = docNum;
                        term.bst=this;
                        //System.out.println(term.tfTimesIDFBST());
                        term.score = term.tfTimesIDFBST();
                        DocFreq.put(term.word, term);
                        doc.termTree.put(term.word, term);
                    }
                }
            }
        }
    }

    public double countOfDocsWithTerm(Key word){
        double result=0;
        for(Doc doc :docList){
            Iterable<String> keys = (Iterable<String>) doc.termTree.levelOrder();
            for(String term: keys){
                if(term!= null && ((Term)doc.termTable.get(term)).word.equals(word)){
                    result++;
                    break;
                }
            }
        }
        return result;
    }

    public Queue<Key> levelOrder() {
        Queue<Key> keys = new Queue<Key>();
        Queue<Node> queue = new Queue<Node>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            Node x = queue.dequeue();
            if (x == null) continue;
            keys.enqueue(x.key);
            queue.enqueue(x.left);
            queue.enqueue(x.right);
        }
        return keys;
    }

    public LinkedList<Doc> search(Key word){
        LinkedList<Doc> list=new LinkedList<Doc>();
        // System.out.println(docList.size());
        System.out.println("    '"+word +"' appeared ");
        for(Doc doc: docList) {

            //System.out.println(doc.fileName);
            if (doc != null) {
                // System.out.println(doc.fileName);

                BST terms = doc.termTree;
                if(word!=null && terms.get(word)!=null){
                    //System.out.println("        "+((Term) terms.get(word)).docFrequency+" time(s) in "+doc.fileName );
                    /*System.out.println("estTF: "+((Term) terms.get(word)).findTF());
                    System.out.println("estIDF: "+((Term) terms.get(word)).findIDFBST());
                    System.out.println("TF: "+ Math.log10(4)+" IDF "+Math.log10(14));
                    System.out.println("Score: "+Math.log10(4)*Math.log10(14));*/
                    System.out.println("        "+((Term) terms.get(word)).docFrequency+" time(s) (Score: "+ ((Term) terms.get(word)).tfTimesIDFBST()+") in "+doc.fileName );
                    list.add(doc);
                }

            }
        }
        return list;
    }

    //Tests for the tree
    private boolean check() {
        if (!isBST())            System.out.println("Not in symmetric order");
        if (!isSizeConsistent()) System.out.println("Subtree counts not consistent");
        if (!isRankConsistent()) System.out.println("Ranks not consistent");
        return isBST() && isSizeConsistent() && isRankConsistent();
    }

    // does this binary tree satisfy symmetric order?
    // Note: this test also ensures that data structure is a binary tree since order is strict
    private boolean isBST() {
        return isBST(root, null, null);
    }

    // is the tree rooted at x a BST with all keys strictly between min and max
    // (if min or max is null, treat as empty constraint)
    // Credit: Bob Dondero's elegant solution
    private boolean isBST(Node x, Key min, Key max) {
        if (x == null) return true;
        if (min != null && x.key.compareTo(min) <= 0) return false;
        if (max != null && x.key.compareTo(max) >= 0) return false;
        return isBST(x.left, min, x.key) && isBST(x.right, x.key, max);
    }

    // are the size fields correct?
    private boolean isSizeConsistent() { return isSizeConsistent(root); }

    private boolean isSizeConsistent(Node x) {
        if (x == null) return true;
        if (x.N != size(x.left) + size(x.right) + 1) return false;
        return isSizeConsistent(x.left) && isSizeConsistent(x.right);
    }

    // check that ranks are consistent
    private boolean isRankConsistent() {
        for (int i = 0; i < size(); i++)
            if (i != rank(select(i))) return false;
        for (Key key : keys())
            if (key.compareTo(select(rank(key))) != 0) return false;
        return true;
    }
    


}

