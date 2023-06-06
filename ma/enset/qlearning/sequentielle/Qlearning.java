package ma.enset.qlearning.sequentielle;

import java.util.Random;

public class Qlearning {
    private  final  double ALPHA=0.1;
    private final double GAMMA=0.9;
    private final  int MAX_EPOCH = 10000;
    private final int GRID_SIZE=3;
   // private final int GRID_SIZE=6;
    private final int ACTION_SIZE=4;
    private int [][] grid;
    //table d'apprentissage
    private double[][] qTable = new double[GRID_SIZE*GRID_SIZE][ACTION_SIZE];
    //table des actions => deux dimension
    private  int [][] actions;

    private int state_x;
    private int state_y;


    public Qlearning(){
        actions = new int[][]{
                {0,-1},//gauche
                {0,1},//droit
                {1,0},//bas
                {-1,0},//haut
        };

        // Les recompenses
        grid = new int[][]{

                {0,0,0},
                {0,-1,0},
                {0,0,1},

                /*
                {0,0,0,0,0,0},
                {-1,-1,-1,-1,-1,0},
                {0,0,0,0,0,0},
                {0,0,0,-1,0,0},
                {0,0,0,0,0,0},
                {-1,0,0,0,0,1},
                 */
                /*
                {0,0,1},
                {0,-1,0},
                {0,0,0},
                 */
        };
    }

    //pour retourne au l'état initial
    private void resetState(){
        //State est definie par => x et y
        state_x = 0;
        state_y=0;

    }

    private int chooseAction(double eps){
        Random rnd = new Random();
        double bestQ=0;
        int act=0;
        //Choisir une action d'une maniere aleatoire => Exploration
        if(rnd.nextDouble()<eps){
            act = rnd.nextInt(ACTION_SIZE);
        }else
        {
            //Exploitation => choisir la mellieure action (maximale) que je peut faire à partie du mon état actuelle => on doit utilisée QTable
            int st = state_x*GRID_SIZE+state_y;
            for(int i=0;i<ACTION_SIZE;i++){
                if(qTable[st][i]>bestQ){
                    bestQ=qTable[st][i];
                    act=i;
                }
            }

        }

        return  act;
    }

    public int executeAction(int actIndex){
        //pour rester dans l'etat actuelle au niveau (X) => (3,2) => on doit choisir valeur minimum qui est 2 pour rester à meme position
        state_x=Math.max(0,Math.min(actions[actIndex][0]+state_x,GRID_SIZE-1));
        //
        state_y = Math.max(0,Math.min(actions[actIndex][1]+state_y,GRID_SIZE-1));

        return state_x*GRID_SIZE+state_y;

    }

    private boolean finished(){
        return grid[state_x][state_y]==1;
    }

    private void showResult(){
        System.out.println("******  q table ******");
        for (double [] line:qTable){
            System.out.printf("[");
            for(double qvalue:line){
                System.out.printf(qvalue+" ");
            }
            System.out.println("]");
        }

        System.out.println("");
        resetState();
        while (!finished()){
            int act = chooseAction(0);
            System.out.println("State : "+(state_x*GRID_SIZE+state_y)+" action : "+act);
            executeAction(act);
        }
        System.out.println("Final state : "+(state_x*GRID_SIZE+state_y));
    }
    public void runQlearning(){
        int iter = 0;
        resetState();
        int currentState;
        int nextState;
        int act1,act;
        while (iter<MAX_EPOCH){
        resetState();

            while (!finished()){
                currentState = state_x*GRID_SIZE+state_y;
                act = chooseAction(0.4);
                nextState = executeAction(act);
                act1 =chooseAction(0);
                qTable[currentState][act] = qTable[currentState][act] + ALPHA*(grid[state_x][state_y]+ GAMMA*qTable[nextState][act1]-qTable[currentState][act]);

            }
            iter++;

        }

        showResult();

    }
}

