package wang.agent.assistent;

import java.util.ArrayList;


import ch.idsia.benchmark.mario.engine.sprites.Mario;



public class MCSimulator {
	

	
    public LevelScene levelScene; 
    public LevelScene backuplevelScene;
    
    //int debuglog = 1;

    
    public MCSimulator()
    {

    	this.levelScene = new LevelScene();
    	this.levelScene.init();
    	
    }
    
	public void advanceStep(boolean[] action)
	{

		levelScene.mario.setKeys(action);
		levelScene.tick();
		levelScene.getMarioState();
	}
	public void setLevelPart(byte[][] levelPart, byte[][] levelPartEx, float[] enemies, float[] realmariofloatpos)
	{
    	levelScene.setLevelScene(levelPart, levelPartEx);
    	levelScene.setEnemies(enemies, realmariofloatpos);
	}
	public void setSprite(byte[][] sprite, float[] marioFloatPos)
	{
		levelScene.setSprite(sprite, marioFloatPos);
	}
    
    //createactionlist by random the depth = 5
    
    //simulate the actionlist get the result score
    public int Simulator(LevelScene ls, ArrayList<boolean[]> actionlist)
    {
    	
    	//
    	float rightboarder = ls.mario.x + 9*16;
    	
    	for(int i = 0; i < actionlist.size(); i++)
    	{
    		ls.mario.setKeys(actionlist.get(i));
    		ls.tick();	
    		
    		if (ls.mario.getStatus() == Mario.STATUS_DEAD)
    		{
    			if (this.debuglog == 1)
    				System.out.println("simulator " + "dead when  i = " + i);
    			return 0;
    		}

    	}
    	if (debuglog == 1)
    		if(ls.mario.flowersDevoured != 0 )
    		System.out.println("gain coins " + ls.mario.coins + " flowersDevoured " + ls.mario.flowersDevoured);
    	
    	return 0;
    }
    public int Simulator(LevelScene ls,boolean[] action)
    {

    	ls.mario.setKeys(action);
    	ls.tick();	

    	return 0;
    }
    public ArrayList<boolean[]> createrandomactionlistdonothing(int depth)
    {
    	ArrayList<boolean[]> possibleActions = new ArrayList<boolean[]>();
    	int index = 0;
    	//createaction(boolean up, boolean down, boolean left, boolean right, boolean speed, boolean jump)
    	//1 left  2 speed left  3 left jump  4 left speed jump
    	//5 right 6 speed right 7 right jump 8 right speed jump  
    	//9 down  10 speed 11 jump speed
    	
    	boolean[] tmpaction = null;
    	
    	while (index < 5)
    	{
    		index++;
	    	tmpaction = createaction(false,false,false,false,true,false); 
	    	possibleActions.add(tmpaction);
    	}
    	
    	return possibleActions;
    }
    //left left right right jump
    public ArrayList<boolean[]> hardActionList_LLRRJ(int depth)
    {
    	ArrayList<boolean[]> possibleActions = new ArrayList<boolean[]>();
    	int index = 6;
    	int random = 0;
    	//createaction(boolean up, boolean down, boolean left, boolean right, boolean speed, boolean jump)
    	//1 left  2 speed left  3 left jump  4 left speed jump
    	//5 right 6 speed right 7 right jump 8 right speed jump  
    	//9 down  10 speed 11 jump speed
    	boolean[] tmpaction = null;
    	tmpaction = createaction(false,false,true,false,false,false);
    	possibleActions.add(tmpaction);
    	tmpaction = createaction(false,false,true,false,false,false);
    	possibleActions.add(tmpaction);
    	tmpaction = createaction(false,false,false,true,false,false);
    	possibleActions.add(tmpaction);
    	tmpaction = createaction(false,false,false,true,false,false);
    	possibleActions.add(tmpaction);
    	tmpaction = createaction(false,false,false,true,false,true);
    	possibleActions.add(tmpaction);
    	tmpaction = createaction(false,false,false,true,false,true);
    	possibleActions.add(tmpaction);
    	/*
    	while (index < depth)
    	{
    		index++;
    		random = (int)(Math.random()*11);
	    	switch(random)
	    	{
	    	case 0: tmpaction = createaction(false,false,true,false,false,false); break;
	    	case 1: tmpaction = createaction(false,false,true,false,true,false);  break;
	    	case 2: tmpaction = createaction(false,false,true,false,false,true);  break;
	    	case 3: tmpaction = createaction(false,false,true,false,true,true);   break;
	    	case 4: tmpaction = createaction(false,false,false,true,false,false); break;
	    	case 5: tmpaction = createaction(false,false,false,true,true,false);  break;
	    	case 6: tmpaction = createaction(false,false,false,true,false,true);  break;
	    	case 7: tmpaction = createaction(false,false,false,true,true,true);   break;
	    	case 8: tmpaction = createaction(false,true,false,false,false,false); break;
	    	case 9: tmpaction = createaction(false,false,false,false,true,false); break;
	    	case 10: tmpaction = createaction(false,false,false,false,true,true); break;
	    	}
	    	
	    	possibleActions.add(tmpaction);
    	}
    	*/
    	
    	
    	return possibleActions;
    }
    
    public ArrayList<boolean[]> hardActionList_RRRRR(int depth)
    {
    	ArrayList<boolean[]> possibleActions = new ArrayList<boolean[]>();
    	int index = 6;
    	int random = 0;
    	//createaction(boolean up, boolean down, boolean left, boolean right, boolean speed, boolean jump)
    	//1 left  2 speed left  3 left jump  4 left speed jump
    	//5 right 6 speed right 7 right jump 8 right speed jump  
    	//9 down  10 speed 11 jump speed
    	boolean[] tmpaction = null;
    	tmpaction = createaction(false,false,false,true,false,false);
    	possibleActions.add(tmpaction);
    	tmpaction = createaction(false,false,false,true,true,false);
    	possibleActions.add(tmpaction);
    	tmpaction = createaction(false,false,false,true,false,false);
    	possibleActions.add(tmpaction);
    	tmpaction = createaction(false,false,false,true,true,false);
    	possibleActions.add(tmpaction);
    	tmpaction = createaction(false,false,false,true,false,false);
    	possibleActions.add(tmpaction);
    	tmpaction = createaction(false,false,false,true,false,false);
    	possibleActions.add(tmpaction);
    	/*
    	while (index < depth)
    	{
    		index++;
    		random = (int)(Math.random()*11);
	    	switch(random)
	    	{
	    	case 0: tmpaction = createaction(false,false,true,false,false,false); break;
	    	case 1: tmpaction = createaction(false,false,true,false,true,false);  break;
	    	case 2: tmpaction = createaction(false,false,true,false,false,true);  break;
	    	case 3: tmpaction = createaction(false,false,true,false,true,true);   break;
	    	case 4: tmpaction = createaction(false,false,false,true,false,false); break;
	    	case 5: tmpaction = createaction(false,false,false,true,true,false);  break;
	    	case 6: tmpaction = createaction(false,false,false,true,false,true);  break;
	    	case 7: tmpaction = createaction(false,false,false,true,true,true);   break;
	    	case 8: tmpaction = createaction(false,true,false,false,false,false); break;
	    	case 9: tmpaction = createaction(false,false,false,false,true,false); break;
	    	case 10: tmpaction = createaction(false,false,false,false,true,true); break;
	    	}
	    	
	    	possibleActions.add(tmpaction);
    	}
    	*/
    	
    	
    	return possibleActions;
    }
    
    public ArrayList<boolean[]> hardActionList_JJRRRR(int depth)
    {
    	ArrayList<boolean[]> possibleActions = new ArrayList<boolean[]>();
    	int index = 6;
    	int random = 0;
    	//createaction(boolean up, boolean down, boolean left, boolean right, boolean speed, boolean jump)
    	//1 left  2 speed left  3 left jump  4 left speed jump
    	//5 right 6 speed right 7 right jump 8 right speed jump  
    	//9 down  10 speed 11 jump speed
    	boolean[] tmpaction = null;
    	tmpaction = createaction(false,false,false,false,false,true);
    	possibleActions.add(tmpaction);
    	tmpaction = createaction(false,false,false,false,false,true);
    	possibleActions.add(tmpaction);
    	tmpaction = createaction(false,false,false,true,false,false);
    	possibleActions.add(tmpaction);
    	tmpaction = createaction(false,false,false,true,true,false);
    	possibleActions.add(tmpaction);
    	tmpaction = createaction(false,false,false,true,false,false);
    	possibleActions.add(tmpaction);
    	tmpaction = createaction(false,false,false,true,false,false);
    	possibleActions.add(tmpaction);
    	/*
    	while (index < depth)
    	{
    		index++;
    		random = (int)(Math.random()*11);
	    	switch(random)
	    	{
	    	case 0: tmpaction = createaction(false,false,true,false,false,false); break;
	    	case 1: tmpaction = createaction(false,false,true,false,true,false);  break;
	    	case 2: tmpaction = createaction(false,false,true,false,false,true);  break;
	    	case 3: tmpaction = createaction(false,false,true,false,true,true);   break;
	    	case 4: tmpaction = createaction(false,false,false,true,false,false); break;
	    	case 5: tmpaction = createaction(false,false,false,true,true,false);  break;
	    	case 6: tmpaction = createaction(false,false,false,true,false,true);  break;
	    	case 7: tmpaction = createaction(false,false,false,true,true,true);   break;
	    	case 8: tmpaction = createaction(false,true,false,false,false,false); break;
	    	case 9: tmpaction = createaction(false,false,false,false,true,false); break;
	    	case 10: tmpaction = createaction(false,false,false,false,true,true); break;
	    	}
	    	
	    	possibleActions.add(tmpaction);
    	}
    	*/
    	
    	
    	return possibleActions;
    }
    
    public ArrayList<boolean[]> createrandomactionlistgoright(int depth)
    {
    	ArrayList<boolean[]> possibleActions = new ArrayList<boolean[]>();
    	int index = 0;
    	//createaction(boolean up, boolean down, boolean left, boolean right, boolean speed, boolean jump)
    	//1 left  2 speed left  3 left jump  4 left speed jump
    	//5 right 6 speed right 7 right jump 8 right speed jump  
    	//9 down  10 speed 11 jump speed
    	//boolean up, boolean down, boolean left, boolean right, boolean speed, boolean jump
    	boolean[] tmpaction = null;
    	
    	while (index < depth)
    	{
    		index++;
	    	tmpaction = createaction(false,false,false,true,false,false); 
	    	possibleActions.add(tmpaction);
    	}
    	
    	return possibleActions;
    }
    public ArrayList<boolean[]> createrandomactionlistjumpinggoright(int depth)
    {
    	ArrayList<boolean[]> possibleActions = new ArrayList<boolean[]>();
    	int index = 0;
    	//createaction(boolean up, boolean down, boolean left, boolean right, boolean speed, boolean jump)
    	//1 left  2 speed left  3 left jump  4 left speed jump
    	//5 right 6 speed right 7 right jump 8 right speed jump  
    	//9 down  10 speed 11 jump speed
    	//boolean up, boolean down, boolean left, boolean right, boolean speed, boolean jump
    	boolean[] tmpaction = null;
    	
    	while (index < depth)
    	{
    		index++;
	    	tmpaction = createaction(false,false,false,true,false,true); 
	    	possibleActions.add(tmpaction);
    	}
    	
    	return possibleActions;
    }
    public boolean[] createrandomaction()
    {
    	boolean[] action = null;
		int random = (int)(Math.random()*13);
    	switch(random)
    	{
    	case 0: action = createaction(false,false,true,false,false,false); break;
    	case 1: action = createaction(false,false,true,false,true,false);  break;
    	case 2: action = createaction(false,false,true,false,false,true);  break;
    	case 3: action = createaction(false,false,true,false,true,true);   break;
    	case 4: action = createaction(false,false,false,true,false,false); break;
    	case 5: action = createaction(false,false,false,true,true,false);  break;
    	case 6: action = createaction(false,false,false,true,false,true);  break;
    	case 7: action = createaction(false,false,false,true,true,true);   break;
    	case 8: action = createaction(false,true,false,false,false,false); break;
    	case 9: action = createaction(false,false,false,false,true,false); break;
    	case 10: action = createaction(false,false,false,false,true,true); break;
    	case 11: action = createaction(false,false,false,false,false,false); break;
    	case 12: action = createaction(false,false,false,false,false,true); break;
    	}
    	return action;
    }
    

    
    public ArrayList<boolean[]> createrandomactionlist(int depth)
    {
    	ArrayList<boolean[]> possibleActions = new ArrayList<boolean[]>();
    	int random = 0;
    	int index = 0;
    	//createaction(boolean up, boolean down, boolean left, boolean right, boolean speed, boolean jump)
    	//1 left  2 speed left  3 left jump  4 left speed jump
    	//5 right 6 speed right 7 right jump 8 right speed jump  
    	//9 down  10 speed 11 jump speed
    	
    	boolean[] tmpaction = null;
    	//random = (int)(Math.random()*2);
    	
    	
    	
    	while (index < depth)
    	{
    		index++;
    		random = (int)(Math.random()*10);
	    	switch(random)
	    	{
	    	case 0: tmpaction = createaction(false,false,true,false,false,false); break;
	    	case 1: tmpaction = createaction(false,false,true,false,true,false);  break;
	    	case 2: tmpaction = createaction(false,false,true,false,false,true);  break;
	    	case 3: tmpaction = createaction(false,false,true,false,true,true);   break;
	    	
	    	case 4: tmpaction = createaction(false,false,false,true,false,false); break;	
	    	case 5: tmpaction = createaction(false,false,false,true,true,false);  break;
	    	case 6: tmpaction = createaction(false,false,false,true,false,true);  break;
	    	case 7: tmpaction = createaction(false,false,false,true,true,true);   break;
	    	
	    	//case 8: tmpaction = createaction(false,true,false,false,false,false); break;
	    	case 8: tmpaction = createaction(false,false,false,false,true,false); break;
	    	case 9: tmpaction = createaction(false,false,false,false,true,true); break;
	    	}
	    	
	    	possibleActions.add(tmpaction);
    	}
    	
    	return possibleActions;
    }
    public boolean[] createaction(boolean up, boolean down, boolean left, boolean right, boolean speed, boolean jump)
    {
    	//boolean[] result = new boolean[6];
    	boolean[] action = new boolean[6];
    	action[Mario.KEY_DOWN] = down;
    	action[Mario.KEY_JUMP] = jump;
    	action[Mario.KEY_LEFT] = left;
    	action[Mario.KEY_RIGHT] = right;
    	action[Mario.KEY_SPEED] = speed;
    	action[Mario.KEY_UP] = up;
    	return action;
    }

    int debuglog = 4; 
    ArrayList<boolean[]> remainactionlist = null;
    
    public boolean[] search(long startTime)
    {
    	int depth = 25;
    	int searchcase;
    	int ticks = 0 + 1;
    	boolean[] resultaction = new boolean[]{false,false,false,false,false,false};
    	ArrayList<boolean[]> bestactionlist = null;
    	LevelScene futurelevelscene = null;
    	LevelScene bestfuturelevelscene = null;
    	ArrayList<boolean[]> tmpactionlist = null;
    	//MarioState bestmariostate = levelScene.mario.getMarioState();
    	//MarioState nowmariostate = levelScene.mario.getMarioState();
    	double bestscore = 0;
    	double startpos = this.levelScene.mario.x;
    	


    	while( System.currentTimeMillis() - startTime < 30) //40
    	{
    		
    		ticks++;
    		if (debuglog == 0)
    			System.out.println("ticks " + ticks);
    		
        	try { futurelevelscene = (LevelScene) levelScene.clone(); } catch (CloneNotSupportedException e){}
        	
    	    //createactionlist by random the depth = 5
        	tmpactionlist = createrandomactionlist(depth);
        	
        	futurelevelscene.mario.resetmariostate();
        	futurelevelscene.resetScore();
    		
    	    //simulate the actionlist get the result score
    		Simulator(futurelevelscene, tmpactionlist);
    		
    		
    		
    		//if (futurelevelscene.mario.betterthan(nowmariostate, bestmariostate, searchcase) == 1)
    		if (futurelevelscene.score > bestscore)
    		{
    			if (debuglog == 1)
    				System.out.println("future mario x " + (futurelevelscene.mario.x - levelScene.mario.x));
    			
    			resultaction = tmpactionlist.get(0);
    			bestfuturelevelscene = futurelevelscene;
    			bestscore = futurelevelscene.score;
    			
    			if (debuglog == 0) 
    				System.out.println("search " + resultaction[0] + " " + resultaction[1] + " " + resultaction[2] + " " + resultaction[3] + " " + resultaction[4] + " " + resultaction[5]);
    			
    		}

    	}
    	
    	if (debuglog == 2)
    		System.out.println("distance " + (bestfuturelevelscene.mario.x - startpos));
    	
    	if (debuglog == 2)
    		System.out.println();
		if (debuglog == 2)
			System.out.println("ticks " + ticks);
		 
		
		//return thebest action
		/*
		if (remainactionlist == null || remainactionlist.size() <= (int)depth/2)
				remainactionlist = bestactionlist;
				
				
    	*/
		
    	//if(debuglog == 4)
    		//System.out.println("last " + (bestfutureleveScene.mario.x - levelScene.mario.x));
    	
    	
    	return resultaction;
    	

    }
    
    public int IsGoal(LevelScene now, LevelScene furture)//mario go right border or dead
    {
    	if (furture.mario.getStatus() == Mario.STATUS_DEAD)
    	{
    		
    		return -1;
    	}
    	if (furture.mario.getMode() < now.mario.getMode())
    		return -2;
    	if (furture.mario.getStatus() == Mario.STATUS_WIN)
    		return 1;
    	if (furture.mario.x - now.mario.x > 3*16)
    		return 2;
    	return 0;
    }
    
    int index = 0;
    public boolean[] searchEx(long startTime)
    {
    	
    	
    	boolean isGoal = false;
    	ArrayList<boolean[]> actionlist = new ArrayList<boolean[]>();
    	ArrayList<boolean[]> bestactionlist = null;
    	boolean[] randomaction = null;
    	boolean[] resaction = null;
    	
    	LevelScene futurelevelscene = null;
    	LevelScene bestfutureleveScene = null;
    	
    	int ticks;
    	int bestticks = 99999;
    	int depth = 20;
    	int isgood = 0;
    	

    	
    	
    	while(System.currentTimeMillis() - startTime < 30)
    	{
    	
    		try { futurelevelscene = (LevelScene) levelScene.clone(); } catch (CloneNotSupportedException e){}
    		actionlist.clear();
    		
    		isGoal = false;
    		isgood = 0;
    		ticks = 0;
    		
	    	while(isGoal == false && ticks < depth && System.currentTimeMillis() - startTime < 30)
	    	{ 	
		    	randomaction = createrandomaction();
		    	actionlist.add(randomaction);
		    	Simulator(futurelevelscene, randomaction);
		    	ticks++;
		    	isgood = IsGoal(levelScene, futurelevelscene);
		    	if (isgood != 0)
		    	{
		    		isGoal = true;
		    	}
	    	}
	    	if(debuglog == 3)
	    		System.out.println("ticks " + ticks + " mariox " + (futurelevelscene.mario.x - levelScene.mario.x));
	    	
	    	if ((bestfutureleveScene == null) || isgood > 0 && futurelevelscene.mario.betterthan(levelScene.mario.getMarioState(), bestfutureleveScene.mario.getMarioState(), 0) == 1)
	    	{
	    		bestfutureleveScene = futurelevelscene;
	    		bestactionlist = actionlist;
	    		
	    		resaction = bestactionlist.get(0);
		    	if(debuglog == 3)
		    		System.out.println("bestticks " + bestticks + " mariox " + (futurelevelscene.mario.x - levelScene.mario.x));
	    	}
    	}
    	if(debuglog == 3)
    		System.out.println("last " + bestticks  + " " + (bestfutureleveScene.mario.x - levelScene.mario.x));
    	//index = 0;
    	return resaction;
    }
    

    

    
}
