import cosc343.assig2.World;
import cosc343.assig2.Creature;
import java.io.*;

/**
* The MyWorld extends the cosc343 assignment 2 World.  Here you can set 
* some variables that control the simulations and override functions that
* generate populations of creatures that the World requires for its
* simulations.
*
* @author  
* @version 1.0
* @since   2017-04-05 
*/
public class MyWorld extends World {
 
  /* Here you can specify the number of turns in each simulation
   * and the number of generations that the genetic algorithm will 
   * execute.
  */
  private final int _numTurns = 100;
  private final int _numGenerations = 5000;
  private int turncount=0;
  private int maxAlive=0;
  private int maxG =0;
  private final float mutationChance;
  private final GenGraph GenG;
  
  /* Constructor.  
   
     Input: griSize - the size of the world
            windowWidth - the width (in pixels) of the visualisation window
            windowHeight - the height (in pixels) of the visualisation window
            repeatableMode - if set to true, every simulation in each
                             generation will start from the same state
            perceptFormat - format of the percepts to use: choice of 1, 2, or 3
  */
  public MyWorld(int gridSize, int windowWidth, int windowHeight, boolean repeatableMode, int perceptFormat) {   
      // Initialise the parent class - don't remove this
      super(gridSize, windowWidth,  windowHeight, repeatableMode, perceptFormat);
        this.mutationChance = 0.01f;

      // Set the number of turns and generations
      this.setNumTurns(_numTurns);
      this.setNumGenerations(_numGenerations);
      this.GenG = new GenGraph();

      
      
  }
 
  /* The main function for the MyWorld application

  */
  public static void main(String[] args) {
     // Here you can specify the grid size, window size and whether torun
     // in repeatable mode or not
     int gridSize = 50;//24
     int windowWidth =  1600;
     int windowHeight = 900;
     boolean repeatableMode = false;
     
      /* Here you can specify percept format to use - there are three to
         chose from: 1, 2, 3.  Refer to the Assignment2 instructions for
         explanation of the three percept formats.
      */
     int perceptFormat = 2;     
     
     // Instantiate MyWorld object.  The rest of the application is driven
     // from the window that will be displayed.
     MyWorld sim = new MyWorld(gridSize, windowWidth, windowHeight, repeatableMode, perceptFormat);
  }
  

  /* The MyWorld class must override this function, which is
     used to fetch a population of creatures at the beginning of the
     first simulation.  This is the place where you need to  generate
     a set of creatures with random behaviours.
  
     Input: numCreatures - this variable will tell you how many creatures
                           the world is expecting
                            
     Returns: An array of MyCreature objects - the World will expect numCreatures
              elements in that array     
  */  
  @Override
  public MyCreature[] firstGeneration(int numCreatures) {

    int numPercepts = this.expectedNumberofPercepts();
    int numActions = this.expectedNumberofActions();
      
    // This is just an example code.  You may replace this code with
    // your own that initialises an array of size numCreatures and creates
    // a population of your creatures
    MyCreature[] population = new MyCreature[numCreatures];
    for(int i=0;i<numCreatures;i++) {
        population[i] = new MyCreature(numPercepts, numActions);
        //population[i].printChromosomes();
    }
    return population;
  }
  
  /* The MyWorld class must override this function, which is
     used to fetch the next generation of the creatures.  This World will
     proivde you with the old_generation of creatures, from which you can
     extract information relating to how they did in the previous simulation...
     and use them as parents for the new generation.
  
     Input: old_population_btc - the generation of old creatures before type casting. 
                              The World doesn't know about MyCreature type, only
                              its parent type Creature, so you will have to
                              typecast to MyCreatures.  These creatures 
                              have been simulated over and their state
                              can be queried to compute their fitness
            numCreatures - the number of elements in the old_population_btc
                           array
                        
                            
  Returns: An array of MyCreature objects - the World will expect numCreatures
           elements in that array.  This is the new population that will be
           use for the next simulation.  
  */  
  @Override
  public MyCreature[] nextGeneration(Creature[] old_population_btc, int numCreatures) {
    // Typcast old_population of Creatures to array of MyCreatures
     MyCreature[] old_population = (MyCreature[]) old_population_btc;
     // Create a new array for the new population
     MyCreature[] new_population = new MyCreature[numCreatures];
     
     // Here is how you can get information about old creatures and how
     // well they did in the simulation
     
     
     float avgLifeTime=0f;
     int nSurvivors = 0;
     turncount++;
     int fitness=0;
     for(MyCreature creature : old_population) {
        // The energy of the creature.  This is zero if creature starved to
        // death, non-negative oterhwise.  If this number is zero, but the 
        // creature is dead, then this number gives the enrgy of the creature
        // at the time of death.
        int energy = creature.getEnergy();
        
        //counter for eater and starver
        // This querry can tell you if the creature died during simulation
        // or not.  
        boolean dead = creature.isDead();
        
        if(dead) {
           // If the creature died during simulation, you can determine
           // its time of death (in turns)
           int timeOfDeath = creature.timeOfDeath();
           fitness += timeOfDeath+energy/5;
           avgLifeTime += (float) timeOfDeath;
        } else {
           nSurvivors += 1;
           fitness += 100+energy/5;
           avgLifeTime += (float) _numTurns;
           //creature.printChromosomes();
        }
     }
     GenG.addPoint(turncount, nSurvivors);
     if(maxAlive<nSurvivors){
         maxAlive = nSurvivors;
         maxG = turncount;
     }
     
        int na=0;
        MyCreature[] alive = new MyCreature[nSurvivors];
        for(MyCreature creat : old_population) {
        
        //counter for eater and starver
        // This querry can tell you if the creature died during simulation
        // or not.  
            boolean ded = creat.isDead();
        
            if(ded) {
                int timeOfD = creat.timeOfDeath();
            }else{
                alive[na]=creat;
                na++;
            }
            
        }
        
        
     // Right now the information is used to print some stats...but you should
     // use this information to access creatures fitness.  It's up to you how
     // you define your fitness function.  You should add a print out or
     // some visual display of average fitness over generations.
     avgLifeTime /= (float) numCreatures;
     System.out.println("Simulation stats:");
     System.out.println("  Survivors    : " + nSurvivors + " out of " + numCreatures);
     System.out.println("Current max survivors: "+ maxAlive+" from Generation "+(maxG));
     System.out.println("  Avg life time: " + avgLifeTime + " turns");

     
      // Having some way of measuring the fitness, you should implement a proper
      // parent selection method here and create a set of new creatures.  You need
      // to create numCreatures of the new creatures.  If you'd like to have
      // some elitism, you can use old creatures in the next generation.  This
      // example code uses all the creatures from the old generation in the
      // new generation.
      /* elitism, survivors got to next round
      refer to 3.1 Elitism, page 6
       */
      System.arraycopy(alive, 0, new_population, 0, nSurvivors);
     
     for(int i=nSurvivors;i<numCreatures; i++) {
        new_population[i] = babyC(arenaS(old_population)
                ,arenaS(old_population));
     }
     
     
     // Return new population of cratures.
     return new_population;
  }
  /**
   * Arena selection, refer to 3.0 Arena Selections, page 6
   * @param pool the gene pool
   * @return a fit parent
   */
  private MyCreature arenaS(MyCreature[] pool){
      int poolsize = pool.length;
      int maxID =0;
      int maxScore = 0;
      int minID = 0;
      int minScore = 150;
      int timeScore;
      int iD;
      int scoreDecider = rand.nextInt(5 - 2 + 1) + 2;
      for(int i = 0; i < poolsize/15;i++){
          iD = rand.nextInt((poolsize-1) - 0 + 1) + 0;
          if(pool[iD].isDead()==true){
              timeScore = pool[iD].timeOfDeath();
          }else{
              timeScore = 100;
          }
          if(minScore>(pool[iD].getEnergy()/scoreDecider+timeScore)){
              minScore = (pool[iD].getEnergy()/scoreDecider+timeScore);
              minID = iD;
          }
          if(maxScore<(pool[iD].getEnergy()/scoreDecider+timeScore)){
              maxScore = (pool[iD].getEnergy()/scoreDecider+timeScore);
              maxID = iD;
          }
      }
      if(rand.nextFloat()<0.05f){
          maxID=minID;//rand.nextInt((poolsize-1) - 0 + 1) + 0;
      }
      return pool[maxID];
  }
  /**
   * Baby Chromosome, this mix the two parent's chromosomes
   * refer to 3.2 Chromosomes splicing, page 8 
   * @param p1 first parent
   * @param p2 second parent
   * @return a baby chromosome
   */
  
  private MyCreature babyC(MyCreature p1, MyCreature p2){
      int p1Score = /*p1.getEnergy()/5+*/p1.timeOfDeath();
      int p2Score = /*p2.getEnergy()/5+*/p2.timeOfDeath();
      MyCreature daddy;
      MyCreature mommy;
      MyCreature baby =new MyCreature(27, 11);
      if(p1Score>p2Score/*(p1Score/(p1Score+p2Score))>rand.nextFloat()*/){
          daddy = p1;
          mommy = p2;
      }else{
          daddy = p2;
          mommy = p1;
      }
      for(int i = 0; i<8;i+=2){
          baby.chromosomes[i]=mutator(mommy.chromosomes[i]);
          baby.chromosomes[i+1]=mutator(daddy.chromosomes[i+1]);
      }
      baby.chromosomes[8]=mutator(mommy.chromosomes[8]);
      for(int i = 9; i<11; i++){
          baby.chromosomes[i]=mutatorTrait(daddy.chromosomes[i]);
      }
      baby.chromosomes[11]=mutatorTrait(mommy.chromosomes[11]);
      if(rand.nextFloat()<0.6f){
          baby.chromosomes[12]=mutatorTrait(daddy.chromosomes[12]);
      }else{
          baby.chromosomes[12]=mutatorTrait(mommy.chromosomes[12]);
      }
      //for(int i = 13; i<18; i++){
      //    baby.chromosomes[i]=imutator(daddy.chromosomes[i]);
      //}
      //for(int i =18; i<21;i++){
      //    baby.chromosomes[i]=imutator(daddy.chromosomes[i]);
      //}
      if(rand.nextFloat()<0.6f){
          baby.chromosomes[13]=mutatorTrait(daddy.chromosomes[13]);
      }else{
          baby.chromosomes[13]=mutatorTrait(mommy.chromosomes[13]);
      }
      if(rand.nextFloat()<0.6f){
          baby.chromosomes[14]=mutatorTrait(daddy.chromosomes[14]);
      }else{
          baby.chromosomes[14]=mutatorTrait(mommy.chromosomes[14]);
      }
      baby.chromosomes[15]=mutatorTrait(daddy.chromosomes[15]);
      if(rand.nextFloat()<0.5f){
          baby.chromosomes[16]=imutator(daddy.chromosomes[16]);
      }else{
          baby.chromosomes[16]=imutator(mommy.chromosomes[16]);
      }
      if(turncount%500==0){
        baby.printChromosomes(); 
      }
      return baby;
  }
  /* mutator methods
     refer to 3.3 Mutation, page 8
  */
  private float mutator(float strand){
      if(rand.nextFloat()<mutationChance){
          return rand.nextInt(8 - 0 + 1) + 0;
      }
      return strand;
  }
  private float imutator(float strand){
      if(rand.nextFloat()<mutationChance){
          return rand.nextInt(7 - 0 + 1) + 0;
      }
      return strand;
  }
  private float mutatorTrait(float strand){
      if(rand.nextFloat()<mutationChance){
          return rand.nextFloat()*((((Math.random()<0.5)?0:1)*-2)+1);
      }
      return strand;
  }
  
}