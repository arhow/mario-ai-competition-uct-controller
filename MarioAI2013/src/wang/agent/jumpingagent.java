package wang.agent;


import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;


public class jumpingagent implements Agent
{
	private String name;
	private boolean[] action = new boolean[Environment.numberOfKeys];;
	protected byte[][] levelScene;
	protected byte[][] enemies;
	protected byte[][] mergedObservation;
	protected float[] marioFloatPos = null;
	protected float[] enemiesFloatPos = null;
	protected int[] marioState = null;
	protected int marioStatus;
	protected int marioMode;
	protected boolean isMarioOnGround;
	protected boolean isMarioAbleToJump;
	protected boolean isMarioAbleToShoot;
	protected boolean isMarioCarrying;
	protected int getKillsTotal;
	protected int getKillsByFire;
	protected int getKillsByStomp;
	protected int getKillsByShell;
	int zLevelScene = 1;
	int zLevelEnemies = 0;
	int index = 0;
	float highestY = 0;
	float x = 32;
	float ax = 0;

	public boolean[] getAction(){
		
		if (marioFloatPos[0] != x){
			x = marioFloatPos[0];
		}
	    if (index == 20 || index == 21 || index == 22 || index == 23 || 
	    		index == 24 || index == 25|| index == 26 || index == 27 || 
	    		index == 28 || index == 29 || index == 30){
	    	
	    	action[Mario.KEY_RIGHT] = true;
	    	action[Mario.KEY_SPEED] = true;
	    }
	    else{
	    	action[Mario.KEY_RIGHT] = false;
	    	action[Mario.KEY_SPEED] = false;
	    }
	    index ++;
	    return action;
	}
	
	public void integrateObservation(Environment environment)
	{
	    levelScene = environment.getLevelSceneObservationZ(zLevelScene);
	    enemies = environment.getEnemiesObservationZ(zLevelEnemies);
	    mergedObservation = environment.getMergedObservationZZ(1, 0);
	    this.marioFloatPos = environment.getMarioFloatPos();
	    this.enemiesFloatPos = environment.getEnemiesFloatPos();
	    this.marioState = environment.getMarioState();
	    marioStatus = marioState[0];
	    marioMode = marioState[1];
	    isMarioOnGround = marioState[2] == 1;
	    isMarioAbleToJump = marioState[3] == 1;
	    isMarioAbleToShoot = marioState[4] == 1;
	    isMarioCarrying = marioState[5] == 1;
	    getKillsTotal = marioState[6];
	    getKillsByFire = marioState[7];
	    getKillsByStomp = marioState[8];
	    getKillsByShell = marioState[9];
	}
	
	public void giveIntermediateReward(float intermediateReward)
	{
	
	}
	
	public void reset()
	{
	    for (int i = 0; i < action.length; ++i)
	        action[i] = false;
	}
	
	public void setObservationDetails(final int rfWidth, final int rfHeight, final int egoRow, final int egoCol){}
	public String getName() { return name; }
	public void setName(String Name) { this.name = Name; }
	public boolean[] getAction(Environment observation){ return null; }
	public void integrateObservation(int[] serializedLevelSceneObservationZ, int[] serializedEnemiesObservationZ, float[] marioFloatPos, float[] enemiesFloatPos, int[] marioState){}
}
