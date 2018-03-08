import java.util.ArrayList;

/**
 * Created by Erfan Rahnemoon on 6/27/17.
 * 
 * detect deadlock Exactly in the same form and algorithm that the book
 * (Database System Concepts by Avi Silberschatz, Henry F. Korth, and 
 * S. Sudarshan) explains with no Optimization.
 */
public class DetectDeadlock {
    private ArrayList<String> listOfAction ;
    private int numberOfTransaction;
    private String [][] transactionTable;
    private int [][] graphDeadlock;
    private boolean isDeadlock;
    public DetectDeadlock(ArrayList<String> listOfAction, int numberOfTransaction){
        this.numberOfTransaction = numberOfTransaction;
        this.listOfAction = listOfAction;
        transactionTable = new String[listOfAction.size()][numberOfTransaction];
        graphDeadlock = new int[numberOfTransaction][numberOfTransaction];
        createTableTransaction(listOfAction);
        checkDeadlock();
        findloop(graphDeadlock);
    }
    private void createTableTransaction(ArrayList list) {
        for(int i=0; i<numberOfTransaction;i++){
            for (int j=0;j<listOfAction.size();j++) {
                transactionTable[j][i] = "null";
            }
        }
        for(int i=0; i<numberOfTransaction;i++){
            for (int j=0;j<listOfAction.size();j++) {
                if(listOfAction.get(j).toUpperCase().contains("T"+(i+1))){
                    transactionTable[j][i] = listOfAction.get(j).split(":")[1].toUpperCase();
                }
            }
        }
    }
    private void checkDeadlock(){
        for (int i = 0; i < transactionTable.length; i++) {
            for (int j = 0; j < transactionTable[0].length; j++) {
                if (transactionTable[i][j].contains("LOCK_X")){
                    findvortex(j,i,transactionTable);
                }
            }
        }
    }
    private void findvortex(int transactionNum,int lineofPlan, String[][] transactionTable){
        for (int i = lineofPlan; i < transactionTable.length; i++) {
            for (int j = 0; j < transactionTable[0].length; j++) {
                if (transactionTable[i][transactionNum].contains(transactionTable[lineofPlan][transactionNum].replace("LOCK_X","UNLOCK"))){
                    return;
                }
                if (transactionTable[i][j].contains(transactionTable[lineofPlan][transactionNum]) ||
                        transactionTable[i][j].contains(transactionTable[lineofPlan][transactionNum].replace("LOCK_X","LOCK_S")) ){
                    graphDeadlock[j][transactionNum] = 1;
                }
                if (transactionTable[i][j].contains("LOCK_X") && !transactionTable[i][j].contains(transactionTable[lineofPlan][transactionNum])){
                    findvortex(j,i,transactionTable);
                }
            }
        }
    }
    private void  findloop(int[][]matrix){
        KosarajoAlgoritm graph = new KosarajoAlgoritm(numberOfTransaction);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j]==1){
                    graph.addEdge(i,j);
                }
            }
        }
        graph.findSCCs();
        setDeadlock(graph.getNoLoop());
    }

    public void setDeadlock(boolean deadlock) {
        isDeadlock = deadlock;
    }

    public boolean isDeadlock() {
        return isDeadlock;
    }
}
