

public class Term {
    String word;
    Doc doc;
    double docFrequency;
    int totalDocs;
    int totalOccurences;
    double score;//tf-idf score
    LinearProbingHashST lp;
    BST bst;
    public void Term(String word, Doc doc, double docFrequency, int totalDocs, int totalOccurences, LinearProbingHashST lp){
        this.word=word;
        this.doc=doc;
        this.docFrequency=docFrequency;
        this.totalDocs=totalDocs;
        this.totalOccurences=totalOccurences;
        this.lp=lp;
    }
    public void Term(String word, Doc doc, double docFrequency, int totalDocs, int totalOccurences, BST bst){
        this.word=word;
        this.doc=doc;
        this.docFrequency=docFrequency;
        this.totalDocs=totalDocs;
        this.totalOccurences=totalOccurences;
        this.bst=bst;
    }

    public double tfTimesIDF(){
        //System.out.println("FindTF(): "+findTF()+ " FindIDF(): "+ findIDF());
        return findTF()*findIDF();
    }
    public double tfTimesIDFBST(){
        //System.out.println("FindTF(): "+findTF()+ " FindIDF(): "+ findIDFBST());
        return findTF()*findIDFBST();

    }
    public double findTF(){
        return Math.log10(1+this.docFrequency);
    }
    public double findIDF(){
        if(this.totalOccurences>0){
            return Math.log10((double) this.lp.docNum/lp.countOfDocsWithTerm(word));

        }
        return 0;
    }
    public double findIDFBST(){
       // System.out.println(docFrequency);
       // System.out.println(bst.countOfDocsWithTerm(word));
        if(this.totalOccurences>0){
            return Math.log10((double) totalDocs/bst.countOfDocsWithTerm(word));
        }
        return 0;
    }

}
