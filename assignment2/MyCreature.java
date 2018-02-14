import cosc343.assig2.Creature;
import java.util.*;

/**
* The MyCreate extends the cosc343 assignment 2 Creature.  Here you implement
* creatures chromosome and the agent function that maps creature percepts to
* actions.  
*
* @author  
* @version 1.0
* @since   2017-04-05 
*/
public class MyCreature extends Creature {

  // Random number generator
  int energy = getEnergy();
  Random rand = new Random();
  boolean plantMode = false;
  private int[] percepts;
  //private String[] directions={"up :","down :","left :","right :","top left :","top right :","bleft :","bright :","stop :","eat :","random :"};
  float[] chromosomes = new float[17];
  //directional key reformat

  /* Empty constructor - might be a good idea here to put the code that 
   initialises the chromosome to some random state   
  
   Input: numPercept - number of percepts that creature will be receiving
          numAction - number of action output vector that creature will need
                      to produce on every turn
  */
  /*initialize the chromosome by randomizing everything
    refer to 2.2 Chromosomes at page 2 
  */
  public MyCreature(int numPercepts, int numActions) {
      //Integer[] dformat = {0,5,3,7,1,6,2,4};
      //Integer[] c1 = {1,2,3,4,5,6,7,8,9};
      //Collections.shuffle(Arrays.asList(dformat));
      //Collections.shuffle(Arrays.asList(c1));
      for(int i = 0; i<9; i++){
        chromosomes[i]=rand.nextInt(8 - 0 + 1) + 0;//c1[i];//
      }
      float[] c2 = new float[4];
      for(int i =0; i<4; i++){
          c2[i] = rand.nextFloat()*((((Math.random()<0.5)?0:1)*-2)+1);
      }
      System.arraycopy(c2,0,chromosomes,9,4);
      chromosomes[13] = rand.nextFloat()*((((Math.random()<0.5)?0:1)*-2)+1);
      chromosomes[14] = rand.nextFloat()*((((Math.random()<0.5)?0:1)*-2)+1);
      chromosomes[15] =((Math.random()<0.5)?0:1);//rand.nextFloat()*((((Math.random()<0.5)?0:1)*-2)+1);
      chromosomes[16] = rand.nextInt(7 - 0 + 1) + 0;
      if(plantMode){
          for(int i =0; i<17; i++){
              chromosomes[i]=1;
          }
          chromosomes[14]=9;
      }
  }
  
  /* This function must be overridden by MyCreature, because it implements
     the AgentFunction which controls creature behavoiur.  This behaviour
     should be governed by a model (that you need to come up with) that is
     parameterise by the chromosome.  
  
     Input: percepts - an array of percepts
            numPercepts - the size of the array of percepts depend on the percept
                          chosen
            numExpectedAction - this number tells you what the expected size
                                of the returned array of percepts should bes
     Returns: an array of actions 
  */
  
  /* Used to map out the actions
     refer to 2.3 Action, page 3
  */
  @Override
  public float[] AgentFunction(int[] percept, int numPercepts, int numExpectedActions) {
      // This is where your chromosome gives rise to the model that maps
      // percepts to actions.  This function governs your creature's behaviour.
      // You need to figure out what model you want to use, and how you're going
      // to encode its parameters in a chromosome.
      this.percepts = percept;
      float actions[] = new float[numExpectedActions];
      //Which way does it want to move
      for(int i=0;i<8;i++) {
         actions[i]= oDirect(percepts,i);
      } 
      //eat
      int eatPos;
      eatPos=(int)chromosomes[8];
      actions[9]=(float)(percepts[eatPos]*(chromosomes[9])+
                percepts[eatPos+9]*(chromosomes[10]));
        
      if(percepts[eatPos+18]<=1){
          actions[9]+=(float)(percepts[eatPos+18]*(chromosomes[12]));
      }else{
          actions[9]+=(float)((chromosomes[11]));
      } 
      actions[9]*=(chromosomes[15]+(50-energy)/50);
      actions[4]=chromosomes[14];
      if(plantMode){
          actions[4]=chromosomes[14];
      }
      actions[10]=chromosomes[13];
      if(!plantMode){
         actions[(int)chromosomes[16]]+=0.001f;
      }
      return actions;
  }
  /* directional calculations
     refer to 2.3.1 cDirect, page 3
  */
  private float cDirect(int[] percepts,int i){
      int pos = i;
      return directional(percepts,(int)chromosomes[pos])+
              (0.25f)*directional(percepts,(int)chromosomes[(pos-1)==-1?7:pos])+
              (0.25f)*directional(percepts,(int)chromosomes[(pos+1)==8?0:pos]);
  }
  /* directional calculations
     refer to 2.3.2 oDirect, page 4
  */
  private float oDirect(int[] percepts, int i){
      int pos = i;
      float returnV=cDirect(percepts,i);
      pos+=1;
      for(int j = 0; j<4; j++){
          pos++;
          if(pos>=8){
              pos = 0;
          }
          returnV -=
              (0.25f)*directional(percepts,(int)chromosomes[pos]);
      }
      return returnV;
      
  }
  /* calculations for spatial awareness
     refer to 2.3 action, page 3
  */
  private float directional(int[] percepts,int i){
      int pos;
      float food;
      pos = (int)chromosomes[i];
      if(percepts[pos+18]<=1){
          food=percepts[pos+18]*chromosomes[12];
      }else{
          food=chromosomes[11];
      }
      return percepts[pos]*(chromosomes[9])+percepts[pos+9]
                 *(chromosomes[10])+food;
  
  }
  /* methond used to print out chromosomes for debugging */
  public void printChromosomes(){
      for(int i =0; i < chromosomes.length; i++){
          System.out.print(chromosomes[i]+" ");
      }
      System.out.print("\n");
  }
}