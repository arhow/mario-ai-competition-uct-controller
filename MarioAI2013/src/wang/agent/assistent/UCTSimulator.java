package wang.agent.assistent;

import java.util.ArrayList;


import ch.idsia.benchmark.mario.engine.sprites.Mario;

class SlotMathine
{
	int n;
	double reward;
	int index;
};
class UCTnode
{
	double reward;
	int nj;
	int j;
	int N;
	UCTnode parent;
	ArrayList<UCTnode> children;
};

public class UCTSimulator {

	    final int Jumpmode = 0;
	    final int Dangermode = 1;
	    final int Searchmode = 2;
	    final int Shootmode = 3;
	    final int Normalmode = 4;
	    final int Nomode = 100;
	    int debuglog = 6; 
	   	public LevelScene levelScene; 
	    public LevelScene backuplevelScene;
	    public ArrayList<SlotMathine> slotmathinelist;
	    ArrayList<boolean[]> remainactionlist = null;
	    public int simulationcount = 0;
		public int max = 0;
		public int min = 2000;
	    UCTnode root = CreateUCTnodeRoot();
	    int bestchildj = -1;
	    
	    public UCTnode CreateUCTnodeRoot(){
	    	
	    	UCTnode root = new UCTnode();
	    	root.parent = null;
	    	root.j = 0;
	    	root.reward = 0;
	    	root.nj = 0;
	    	root.N = 0;
	    	root.children = new ArrayList<UCTnode>();
	    	return root;
	    }
	    
	    public UCTnode CreateUCTnode(UCTnode parent, int j){
	    	
	    	UCTnode node = new UCTnode();
	    	node.parent = parent;
	    	node.j = j;
	    	node.reward = 0;
	    	node.nj = 0;
	    	if (parent.children.size() == 0)
	    		node.N = 0;
	    	else
	    		node.N = parent.children.get(0).N;
	    	node.children = new ArrayList<UCTnode>();
	    	parent.children.add(node);
	    	return node;
	    }
	    
	    public UCTnode getChildJ(UCTnode parent, int j){
	    	
	    	for(UCTnode child: parent.children)
	    	{
	    		if (child.j == j)
	    			return child;
	    	}
	    	return null;
	    }
	    
	    public void updateUCTnode(UCTnode root, int j, double reward){
	    	
	    	boolean isHasChildJ = false;
	    	for(UCTnode child: root.children){
	    		if (child.j == j){
	    			isHasChildJ = true;
	    			AddRewardIntoNode(reward, child);
	    		}
	    	}
	    	if (isHasChildJ == false){
	    		UCTnode childj = CreateUCTnode(root, j);
	    		AddRewardIntoNode(reward, childj);
	    	}
	    }
	    
	    public void update(UCTnode root, int[] randomactionlist, double reward){

	    	UCTnode backuproot = root;
	    	for (int i = 0; i < randomactionlist.length; i++){
	    		updateUCTnode(root, randomactionlist[i], reward);
	    		root = getChildJ(root, randomactionlist[i]);
	    	}
	    	root = backuproot;
	    	if (root.parent == null) return;
	    	while(root.parent.parent != null){
	    		AddRewardIntoNode(reward, root.parent);
	    		root = root.parent;
	    	}
	    }
	    
	    public void AddRewardIntoNode(double reward, UCTnode node){
	    	
	    	node.nj ++;
	    	node.reward += reward;
	    	node.N ++;
	    	for(UCTnode bros:node.parent.children)
	    		bros.N = node.N;
	    }
	    
	    public UCTnode selectBestUCBchild(UCTnode parent){
	    	
	    	double bestUCB = -99999999;
	    	UCTnode bestUCBchild = parent.children.get(0);
	    	for(UCTnode child: parent.children){
	    		double UCB = CalcUCB(child, 3); 
	    		if (UCB >= bestUCB){
	    			bestUCB = UCB;
	    			bestUCBchild = child;
	    		}
	    	}
	    	return bestUCBchild;
	    }
	    
	    public int getChildrenSize(int mode){
	    	
	    	switch(mode){
	    	case Jumpmode:return 6;
	    	case Searchmode:return 6;
	    	case Dangermode:return 6;
	    	case Normalmode:return 6;
	    	case Shootmode:return 4;
	    	case Nomode : return 10;
	    	}
	    	return -1;
	    }
	    
	    public double onePlayOut(UCTnode root, int[] randomactionlist, LevelScene ls, int mode){
	    	
	    	UCTnode backupnode = root;
	    	double reward  = 0;
	    	int uctmove = 0;
	    	while (root.children.size() == getChildrenSize(mode)){
		    	root = selectBestUCBchild(root);
	    		Simulator(ls, createaction(root.j));
	    		uctmove++;
	    		if (uctmove >= 10)
	    			break;
	    	}
	    	reward = mcsimulation(root, randomactionlist, ls);
	    	update(root, randomactionlist, reward);
	    	root = backupnode;
	    	return 0;
	    }
	    
	    public double mcsimulation(UCTnode node, int[] randomactionlist, LevelScene ls){
	    	
	    	for(int i = 0; i < randomactionlist.length; i++){
	    		Simulator(ls, createaction(randomactionlist[i]));
	    	}
	    	return ls.score;
	    }
	    

	    public double CalcUCB(double reward, int nj, int N, double c){
	    	
	    	if (nj == 0)
	    		return 0;
	    	else
	    		return reward/nj + c*(Math.sqrt((2*Math.log(N))/(nj)));
	    }
	    
	    public double CalcUCB(UCTnode node, double c){
	    	
	    	return CalcUCB(node.reward, node.nj, node.N, c);
	    }

	    
	    public void initSlotmathineList(){
	    	
	    	slotmathinelist = new ArrayList<SlotMathine>();
	    	for(int i = 0; i < 10; i++){
		    	SlotMathine slot = new SlotMathine();
		    	slot.n = 0;
		    	slot.reward = 0;
		    	slot.index = i;
		    	slotmathinelist.add(slot);
	    	}
	    }
	    
	    public boolean hasEmptyItem(){
	    	
	    	boolean res = false;
	    	for(SlotMathine slot: slotmathinelist){
	    		if (slot.n == 0)
	    			res = true;
	    	}
	    	return res;
	    }
	    
	    public int selectBestRewardSlotMathine(){
	    	
	    	double bestReward;
	    	int n = 0;
	    	int index = 0;
	    	
	    	for(SlotMathine slotmathine : slotmathinelist){
	    		n += slotmathine.n;
	    	}
	    	bestReward = -9999;
	    	for(SlotMathine slotmathine : slotmathinelist){
	    		double tmp = calSlotMathineReward(n, slotmathine);
	    		if (tmp > bestReward){
	    			bestReward = tmp;
	    			index = slotmathine.index;
	    		}
	    	}
	    	return index;
	    }
	    
	    public boolean[] getActionBySlotMathine(int index){
	    	
	    	return createaction(index);
	    }
	    
	    public boolean[] getbestactionFromslotmathinelist(){
	    	
	    	boolean[] action = null;
	    	int index = selectBestRewardSlotMathine();
	    	action = getActionBySlotMathine(index);
	    	return action;
	    }
	    
	    public int getBestActionIndexFromSlotMathineList(){
	    	
	    	return selectBestRewardSlotMathine();
	    }
	    
	    public double calSlotMathineReward(int n , SlotMathine slotmathine){
	    	
	    	double res = 0;
	    	res = slotmathine.reward/slotmathine.n + Math.sqrt((2*Math.log(n))/(slotmathine.n));
	    	return res;
	    }
	    
	    public void writeSlotMathineRewardInList(int index, double reward){
	    	
	    	slotmathinelist.get(index).n += 1;
	    	slotmathinelist.get(index).reward += reward;
	    }
	    
	    public UCTSimulator(){

	    	this.levelScene = new LevelScene();
	    	this.levelScene.init();
	    	this.initSlotmathineList();
	    }
	    
		public void advanceStep(boolean[] action){
			
			levelScene.mario.setKeys(action);
			levelScene.tick();
		}
		
		public void setLevelPart(byte[][] levelPart, byte[][] levelPartEx, float[] enemies, float[] realmariofloatpos){
			
	    	levelScene.setLevelScene(levelPart, levelPartEx);
	    	levelScene.setEnemies(enemies, realmariofloatpos);
		}
		
		public void setSprite(byte[][] sprite, float[] marioFloatPos){
			
			levelScene.setSprite(sprite, marioFloatPos);
		}
		
	    public int Simulator(LevelScene ls, ArrayList<boolean[]> actionlist){
	    	
	    	ls.mario.resetmariostate();
	    	for(int i = 0; i < actionlist.size(); i++){
	    		ls.mario.setKeys(actionlist.get(i));
	    		ls.tick();
	    		if (ls.mario.getStatus() == Mario.STATUS_DEAD) return 0;
	    	}
	    	return 0;
	    }
	    
	    public ArrayList<boolean[]> createrandomactionlistdonothing(int depth){
	    	
	    	ArrayList<boolean[]> possibleActions = new ArrayList<boolean[]>();
	    	int index = 0;
	    	//createaction(boolean up, boolean down, boolean left, boolean right, boolean speed, boolean jump)
	    	//1 left  2 speed left  3 left jump  4 left speed jump
	    	//5 right 6 speed right 7 right jump 8 right speed jump  
	    	//9 down  10 speed 11 jump speed
	    	boolean[] tmpaction = null;
	    	while (index < 5){
	    		index++;
		    	tmpaction = createaction(false,false,false,false,true,false); 
		    	possibleActions.add(tmpaction);
	    	}
	    	return possibleActions;
	    }
	    
	    public ArrayList<boolean[]> createrandomactionlistgoright(int depth){
	    	
	    	ArrayList<boolean[]> possibleActions = new ArrayList<boolean[]>();
	    	int index = 0;
	    	//createaction(boolean up, boolean down, boolean left, boolean right, boolean speed, boolean jump)
	    	//1 left  2 speed left  3 left jump  4 left speed jump
	    	//5 right 6 speed right 7 right jump 8 right speed jump  
	    	//9 down  10 speed 11 jump speed
	    	//boolean up, boolean down, boolean left, boolean right, boolean speed, boolean jump
	    	boolean[] tmpaction = null;
	    	while (index < depth){
	    		index++;
		    	tmpaction = createaction(false,false,false,true,false,false); 
		    	possibleActions.add(tmpaction);
	    	}
	    	
	    	return possibleActions;
	    }
	    
	    public ArrayList<boolean[]> createrandomactionlistjumpinggoright(int depth){
	    	
	    	ArrayList<boolean[]> possibleActions = new ArrayList<boolean[]>();
	    	int index = 0;
	    	//createaction(boolean up, boolean down, boolean left, boolean right, boolean speed, boolean jump)
	    	//1 left  2 speed left  3 left jump  4 left speed jump
	    	//5 right 6 speed right 7 right jump 8 right speed jump  
	    	//9 down  10 speed 11 jump speed
	    	//boolean up, boolean down, boolean left, boolean right, boolean speed, boolean jump
	    	boolean[] tmpaction = null;
	    	while (index < depth){
	    		index++;
		    	tmpaction = createaction(false,false,false,true,false,true); 
		    	possibleActions.add(tmpaction);
	    	}
	    	return possibleActions;
	    }
	    
	    public boolean[] createaction(int index){
	    	
	    	boolean[] action = null;
	    	switch(index){
	    	case 0: action = createaction(false,false,true,false,false,false); break;
	    	case 1: action = createaction(false,false,true,false,true,false);  break;
	    	case 2: action = createaction(false,false,true,false,false,true);  break;
	    	case 3: action = createaction(false,false,true,false,true,true);   break;
	    	case 4: action = createaction(false,false,false,true,false,false); break;
	    	case 5: action = createaction(false,false,false,true,true,false);  break;
	    	case 6: action = createaction(false,false,false,true,false,true);  break;
	    	case 7: action = createaction(false,false,false,true,true,true);   break;
	    	case 8: action = createaction(false,false,false,false,true,false); break;
	    	case 9: action = createaction(false,false,false,false,false,true); break;
	    	}
	    	return action;
	    }
	    
	    public ArrayList<boolean[]> createrandomactionlist(int depth){
	    	
	    	ArrayList<boolean[]> possibleActions = new ArrayList<boolean[]>();
	    	int random = 0;
	    	int index = 0;
	    	//createaction(boolean up, boolean down, boolean left, boolean right, boolean speed, boolean jump)
	    	//1 left  2 speed left  3 left jump  4 left speed jump
	    	//5 right 6 speed right 7 right jump 8 right speed jump  
	    	//9 down  10 speed 11 jump speed
	    	boolean[] tmpaction = null;
	    	random = (int)(Math.random()*2);
	    	while (index < depth){
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
	    	
	    	return possibleActions;
	    }
	    
	    public boolean[] createaction(boolean up, boolean down, boolean left, boolean right, boolean speed, boolean jump){
	    	
	    	boolean[] action = new boolean[6];
	    	action[Mario.KEY_DOWN] = down;
	    	action[Mario.KEY_JUMP] = jump;
	    	action[Mario.KEY_LEFT] = left;
	    	action[Mario.KEY_RIGHT] = right;
	    	action[Mario.KEY_SPEED] = speed;
	    	action[Mario.KEY_UP] = up;
	    	return action;
	    }

	    public int createRandomIndex(){
	    	
	    	int random = (int)(Math.random()*10);
	    	return random;
	    }
	    
	    public int[] createRandomActionList(int mode, double c){
	    	
	    	int[] actionlist = null;
	    	switch(mode){
		    	case Jumpmode: actionlist = new int[(int)(23*c)]; break;
		    	case Dangermode: actionlist = new int[(int)(15*c)]; break;
		    	case Searchmode: actionlist = new int[(int)(15*c)]; break;
		    	case Normalmode: actionlist = new int[(int)(23*c)]; break;
		    	case Shootmode: actionlist = new int[(int)(20*c)]; break;
		    	case Nomode: actionlist = new int[(int)(20*c)];break;
	    	}
	    	for(int i = 0; i < actionlist.length; i++){
	    		actionlist[i] = createRandomIndex(mode);
	    	}
	    	return actionlist;
	    }
	    
	    public int[] createRandomActionList(int c){
	    	
	    	int[] actionlist = new int[20*c]; 
	    	for(int i = 0; i < actionlist.length; i++){
	    		actionlist[i] = createRandomIndex();
	    	}
	    	return actionlist;
	    }
	    
	    public int createRandomIndex(int mode){
	    	
	    	int res = 0;
	    	int random = 0;
	    	switch(mode){
	    	case Jumpmode:
	    		random = (int)(Math.random()*6);
	    		switch (random){
	    		case 0: res = 4; break;
	    		case 1: res = 5; break;
	    		case 2: res = 6; break;
	    		case 3: res = 7; break;
	    		case 4: res = 8; break;
	    		case 5: res = 9; break;
	    		}
	    		break;
	    	case Dangermode:
	    		random = (int)(Math.random()*6);
	    		switch (random){
	    		case 0: res = 0; break;
	    		case 1: res = 2; break;
	    		case 2: res = 4; break;
	    		case 3: res = 6; break;
	    		case 4: res = 8; break;
	    		case 5: res = 9; break;
	    		}
	    		break;
	    	case Searchmode:
	    		random = (int)(Math.random()*6);
	    		switch (random){
	    		case 0: res = 0; break;
	    		case 1: res = 1; break;
	    		case 2: res = 4; break;
	    		case 3: res = 5; break;
	    		case 4: res = 8; break;
	    		case 5: res = 9; break;
	    		}
	    		break;
	    	case Shootmode:
	    		random = (int)(Math.random()*4);
	    		switch (random){
	    		case 0: res = 0; break;
	    		case 1: res = 4; break;
	    		case 2: res = 8; break;
	    		case 3: res = 9; break;
	    		}
	    		break;
	    	case Normalmode:
	    		random = (int)(Math.random()*6);
	    		switch (random){
	    		case 0: res = 4; break;
	    		case 1: res = 5; break;
	    		case 2: res = 6; break;
	    		case 3: res = 7; break;
	    		case 4: res = 8; break;
	    		case 5: res = 9; break;
	    		}
	    		break;
	    	case Nomode:
	    		random = (int)(Math.random()*10);
	    		switch (random){
	    		case 0: res = 0; break;
	    		case 1: res = 1; break;
	    		case 2: res = 2; break;
	    		case 3: res = 3; break;
	    		case 4: res = 4; break;
	    		case 5: res = 5; break;
	    		case 6: res = 6; break;
	    		case 7: res = 7; break;
	    		case 8: res = 8; break;
	    		case 9: res = 9; break;
	    		}
	    		break;
	    	}
	    	return res;
	    }
	    
	    public boolean[] createrandomaction(){
	    	
	    	boolean[] action = null;
			int random = (int)(Math.random()*10);
	    	switch(random){
	    	case 0: action = createaction(false,false,true,false,false,false); break;
	    	case 1: action = createaction(false,false,true,false,true,false);  break;
	    	case 2: action = createaction(false,false,true,false,false,true);  break;
	    	case 3: action = createaction(false,false,true,false,true,true);   break;
	    	case 4: action = createaction(false,false,false,true,false,false); break;
	    	case 5: action = createaction(false,false,false,true,true,false);  break;
	    	case 6: action = createaction(false,false,false,true,false,true);  break;
	    	case 7: action = createaction(false,false,false,true,true,true);   break;
	    	case 8: action = createaction(false,false,false,false,true,false); break;
	    	case 9: action = createaction(false,false,false,false,false,true); break;
	    	}
	    	return action;
	    }
	    
	    //mario go right border or dead
	    public int IsGoal(LevelScene now, LevelScene furture){
	    	
	    	if (furture.mario.getStatus() == Mario.STATUS_DEAD)
	    		return -1;
	    	if (furture.mario.getStatus() == Mario.STATUS_WIN)
	    		return 1;
	    	if (furture.mario.x - now.mario.x > 9*16)
	    		return 2;
	    	return 0;
	    }
	    
	    public int Simulator(LevelScene ls,boolean[] action){
	    	ls.mario.setKeys(action);
	    	ls.tick();
	    	simulationcount++;
	    	return 0;
	    }
	    
	    void setReward(int strategy){
	    	
	    	switch(strategy){
	    	
	    	case Jumpmode:
	        	LevelScene.DISTANCE = 1;
	        	LevelScene.MODE = 32;
	        	LevelScene.COINS = 16;
	        	LevelScene.FLOWERFIRE = 100;
	        	LevelScene.KILLS = 42;
	        	LevelScene.KILLEDBYFIRE = 20;
	        	LevelScene.KILLEDBYSHELL = 17;
	        	LevelScene.KILLEDBYSTOMP = 12;
	        	LevelScene.MUSHROOM = 100;
	        	LevelScene.HIDDENBLOCK = 0;
	        	LevelScene.GREENMUSHROOM = 0;
	        	LevelScene.BRICKS = 0;
	        	LevelScene.DAMAGE = -10000;
	        	LevelScene.BUMP = 0;
	        	LevelScene.KICKS = 20;

	    		break;
	    	case Dangermode:
	        	LevelScene.DISTANCE = 1;
	        	LevelScene.MODE = 32;
	        	LevelScene.COINS = 16;
	        	LevelScene.FLOWERFIRE = 100;
	        	LevelScene.KILLS = 42;
	        	LevelScene.KILLEDBYFIRE = 4;
	        	LevelScene.KILLEDBYSHELL = 17;
	        	LevelScene.KILLEDBYSTOMP = 12;
	        	LevelScene.MUSHROOM = 100;
	        	LevelScene.HIDDENBLOCK = 0;
	        	LevelScene.GREENMUSHROOM = 0;
	        	LevelScene.BRICKS = 0;
	        	LevelScene.DAMAGE = -1000;
	        	LevelScene.BUMP = 0;
	        	LevelScene.KICKS = 20;
	    		break;
	    	case Normalmode:
	        	LevelScene.DISTANCE = 1;
	        	LevelScene.MODE = 32;
	        	LevelScene.COINS = 16;
	        	LevelScene.FLOWERFIRE = 100;
	        	LevelScene.KILLS = 42;
	        	LevelScene.KILLEDBYFIRE = 20;
	        	LevelScene.KILLEDBYSHELL = 17;
	        	LevelScene.KILLEDBYSTOMP = 12;
	        	LevelScene.MUSHROOM = 100;
	        	LevelScene.HIDDENBLOCK = 0;
	        	LevelScene.GREENMUSHROOM = 0;
	        	LevelScene.BRICKS = 10;
	        	LevelScene.DAMAGE = -1000;
	        	LevelScene.BUMP = 0;
	        	LevelScene.KICKS = 20;
	    		break;

	    	case Searchmode:
	        	LevelScene.DISTANCE = 1;
	        	LevelScene.MODE = 32;
	        	LevelScene.COINS = 16;
	        	LevelScene.FLOWERFIRE = 100;
	        	LevelScene.KILLS = 42;
	        	LevelScene.KILLEDBYFIRE = 20;
	        	LevelScene.KILLEDBYSHELL = 17;
	        	LevelScene.KILLEDBYSTOMP = 12;
	        	LevelScene.MUSHROOM = 100;
	        	LevelScene.HIDDENBLOCK = 0;
	        	LevelScene.GREENMUSHROOM = 0;
	        	LevelScene.BRICKS = 0;
	        	LevelScene.DAMAGE = -1000;
	        	LevelScene.BUMP = 0;
	        	LevelScene.KICKS = 20;
	    		break;
	    	case Shootmode:
	        	LevelScene.DISTANCE = 1;
	        	LevelScene.MODE = 32;
	        	LevelScene.COINS = 16;
	        	LevelScene.FLOWERFIRE = 100;
	        	LevelScene.KILLS = 42;
	        	LevelScene.KILLEDBYFIRE = 20;
	        	LevelScene.KILLEDBYSHELL = 17;
	        	LevelScene.KILLEDBYSTOMP = 12;
	        	LevelScene.MUSHROOM = 100;
	        	LevelScene.HIDDENBLOCK = 0;
	        	LevelScene.GREENMUSHROOM = 0;
	        	LevelScene.BRICKS = 0;
	        	LevelScene.DAMAGE = -1000;
	        	LevelScene.BUMP = 0;
	        	LevelScene.KICKS = 20;
	    		break;
	    	}
	    }
	    
	    // right uct with mode
	    public boolean[] optimise_with_mode(long starttime){
	    	
	    	if (levelScene.level.isGap[(int)levelScene.mario.x/16] == true 
	    			|| levelScene.level.isGap[(int)levelScene.mario.x/16 + 1] == true	
	    			|| levelScene.level.isGap[(int)levelScene.mario.x/16 + 2] == true )
	    	{
	    		this.setReward(Jumpmode);
	    		return uct(starttime, Jumpmode, 23);	
	    		
	    	}
	    	else if (this.levelScene.getEnemiesFloatPosinMarioZone(3).length/3 > 2 )
	    	{	
	    		this.setReward(Dangermode);
	    		return uct(starttime, Dangermode, 15);
	    	}
	    	else if (this.levelScene.getFireFlowerorMushroomFloatPosinMarioZone(3).length > 0)
	    	{
	    		this.setReward(Searchmode);
	    		return uct(starttime, Searchmode, 15);
	    	}
	    	else if (this.levelScene.getEnemiesFloatPosFrontMario(3*16).length/3 > 3 && this.levelScene.mario.getMode() == 2) 
	    	{
		    		this.setReward(Shootmode);
		    		return uct(starttime, Shootmode, 20);		
	    	}
	    	else
	    	{
	    		this.setReward(Normalmode);
	    		return uct(starttime, Normalmode, 23);	
	    	}
	    }
	    
	    public boolean[] optimise_without_mode(long starttime) // right uct without mode
	    {
    		this.setReward(Normalmode);
    		return search4_nomode(starttime, Normalmode, 20);
	    }
	    
	    void resetRoot(){
	    	
	    	UCTnode root = CreateUCTnodeRoot();
	    	bestchildj = -1;
	    }
	    
	    //right uct
	    public boolean[] uct(long startTime, int mode, int depth){
	    	
	    	boolean[] resaction = null;
	    	LevelScene futurelevelscene = null;
	    	simulationcount = 0;
	    	
	    	int[] randomActionList = null;
	    	
	    	if (bestchildj == -1)
	    		root = CreateUCTnodeRoot();
	    	else
	    	{
	    		root = getChildJ(root, bestchildj);
	    		root.parent = null;
		    	root.j = 0;
		    	root.reward = 0;
		    	root.nj = 0;
		    	root.N = 0;
	    	}
	    	double reward= 0;
	    	int bestj = -1;
	    	int ticks = 0;
	    	while(simulationcount < 3000){
        		try { futurelevelscene = (LevelScene) levelScene.clone(); } catch (CloneNotSupportedException e){}
        		futurelevelscene.resetScore();
        		futurelevelscene.mario.resetmariostate();
        		randomActionList = createRandomActionList(mode, 1);
        		ticks++;
	    	}
	    	if (root == null){
	    		resetRoot();
	    		return new boolean[]{false, false, false, false, false, false };
	    	}
	    	if (root.children.size() == 0){
	    		resetRoot();
	    		return new boolean[]{false, false, false, false, false, false };
	    	}
	    	bestj = selectBestUCBchild(root).j;
	    	if (bestj == -1){
	    		resetRoot();
	    		return new boolean[]{false, false, false, false, false, false };
	    	}
	    	resaction = createaction(bestj);
	    	if (resaction == null){
	    		resetRoot();
	    		return new boolean[]{false, false, false, false, false, false };
	    	}
			if (simulationcount > max) 
			{
				max = simulationcount;
			}
			if (simulationcount < min)
			{
				min = simulationcount;
			}
			bestchildj = bestj;
	    	return resaction;
	    }
	    
	    // no mode
	    public boolean[] search4_nomode(long startTime, int mode, int depth){
	    	
	    	boolean[] resaction = null;
	    	LevelScene futurelevelscene = null;
	    	simulationcount = 0;
	    	int[] randomActionList = null;
	    	UCTnode root = CreateUCTnodeRoot();
	    	double reward= 0;
	    	int bestj = -1;
	    	while( System.currentTimeMillis() - startTime < 35){
        		try { futurelevelscene = (LevelScene) levelScene.clone(); } catch (CloneNotSupportedException e){}
        		futurelevelscene.resetScore();
        		futurelevelscene.mario.resetmariostate();
        		randomActionList = createRandomActionList(mode, 1);
        		onePlayOut(root, randomActionList, futurelevelscene, mode);
        	}
	    	bestj = selectBestUCBchild(root).j;
	    	if (root == null)
	    		return new boolean[]{false, false, false, false, false, false };
	    	if (selectBestUCBchild(root) == null)
	    		return new boolean[]{false, false, false, false, false, false };
	    	if (bestj == -1)
	    		return new boolean[]{false, false, false, false, false, false };
	    	resaction = createaction(bestj);
	    	if (resaction == null)
	    		return new boolean[]{false, false, false, false, false, false };
			if (simulationcount > max){
				max = simulationcount;
			}
			if (simulationcount < min){
				min = simulationcount;
			}
			return resaction;
	    }    
}
