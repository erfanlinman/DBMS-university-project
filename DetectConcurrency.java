import java.util.ArrayList;

/**
 * Created by Erfan Rahnemoon on 6/26/17.
 *
 * detect deadlock Exactly in the same form and algorithm that the book explains with no Optimization
 * In fact, code with awful performance and scalability is worse
 */
public class DetectConcurrency {
    ArrayList<String> listOfAction ;
    int numberOfTransaction;
    int [][] matrixDetectConcurrency ;
    String [][] transactionTable;
    private static boolean isConcurrent = true;
    public DetectConcurrency (ArrayList listOfAction, int numberOfTransaction) {
        this.listOfAction = listOfAction;
        this.numberOfTransaction = numberOfTransaction;
        matrixDetectConcurrency = new int [numberOfTransaction][numberOfTransaction];
        transactionTable = new String[listOfAction.size()][numberOfTransaction];
        createTableTransaction(listOfAction);
        findloop(matrixDetectConcurrency);
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
        for(int i=0; i<transactionTable.length;i++){
            for (int j=0;j<transactionTable[0].length;j++) {
                if (!transactionTable [i][j].equals("null")) {
                    findVortex(transactionTable[i][j],j,i);
                }
            }
        }
    }

    private void findVortex(String regex,int indexTransaction,int lineOfPlan) {
        if (regex.contains("R") || regex.contains("LOCK_S")) {
            for (int i = lineOfPlan; i < transactionTable.length; i++) {
                for (int j = 0; j < transactionTable[0].length; j++) {
                    if (!transactionTable[i][j].equals("null") && (transactionTable[i][j].equals(regex.replace("R","W")) ||
                            transactionTable[i][j].equals(regex.replace("LOCK_S","LOCK_X")))) {
                        matrixDetectConcurrency[indexTransaction][j] = 1;
                    }
                }
            }
        }else if(regex.contains("W") || regex.contains("LOCK_X")){
            for (int i = lineOfPlan; i < transactionTable.length; i++) {
                for (int j = 0; j < transactionTable[0].length; j++) {
                    if (!transactionTable[i][j].equals("null") && transactionTable[i][j].equals(regex)) {
                        matrixDetectConcurrency[indexTransaction][j] = 1;
                    }else if (!transactionTable[i][j].equals("null") && (transactionTable[i][j].equals(regex.replace("R","W")) ||
                            transactionTable[i][j].equals(regex.replace("LOCK_X","LOCK_S")))){
                        matrixDetectConcurrency[indexTransaction][j] = 1;
                    }
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
        setIsConcurrent(graph.getNoLoop());
    }


    public void setIsConcurrent(boolean isConcurrenct) {
        DetectConcurrency.isConcurrent = isConcurrenct;
    }

    public boolean getIsConcurrent (){
        return DetectConcurrency.isConcurrent;
    }
}
