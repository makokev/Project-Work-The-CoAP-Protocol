/* Generated by AN DISI Unibo */ 
package it.unibo.client_actor;
import it.unibo.qactors.PlanRepeat;
import it.unibo.qactors.QActorContext;
import it.unibo.qactors.StateExecMessage;
import it.unibo.qactors.QActorUtils;
import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.qactors.action.AsynchActionResult;
import it.unibo.qactors.action.IActorAction;
import it.unibo.qactors.action.IActorAction.ActionExecMode;
import it.unibo.qactors.action.IMsgQueue;
import it.unibo.qactors.akka.QActor;
import it.unibo.qactors.StateFun;
import java.util.Stack;
import java.util.Hashtable;
import java.util.concurrent.Callable;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import it.unibo.qactors.action.ActorTimedAction;
public abstract class AbstractClient_actor extends QActor { 
	protected AsynchActionResult aar = null;
	protected boolean actionResult = true;
	protected alice.tuprolog.SolveInfo sol;
	protected String planFilePath    = null;
	protected String terminationEvId = "default";
	protected String parg="";
	protected boolean bres=false;
	protected IActorAction action;
	 
	
		protected static IOutputEnvView setTheEnv(IOutputEnvView outEnvView ){
			return outEnvView;
		}
		public AbstractClient_actor(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/client_actor/WorldTheory.pl",
			setTheEnv( outEnvView )  , "init");
			this.planFilePath = "./srcMore/it/unibo/client_actor/plans.txt";
	  	}
		@Override
		protected void doJob() throws Exception {
			String name  = getName().replace("_ctrl", "");
			mysupport = (IMsgQueue) QActorUtils.getQActor( name ); 
			initStateTable(); 
	 		initSensorSystem();
	 		history.push(stateTab.get( "init" ));
	  	 	autoSendStateExecMsg();
	  		//QActorContext.terminateQActorSystem(this);//todo
		} 	
		/* 
		* ------------------------------------------------------------
		* PLANS
		* ------------------------------------------------------------
		*/    
	    //genAkkaMshHandleStructure
	    protected void initStateTable(){  	
	    	stateTab.put("handleToutBuiltIn",handleToutBuiltIn);
	    	stateTab.put("init",init);
	    	stateTab.put("running",running);
	    	stateTab.put("getValue",getValue);
	    	stateTab.put("putValue",putValue);
	    	stateTab.put("readValue",readValue);
	    	stateTab.put("stopping",stopping);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "client_actor tout : stops");  
	    		repeatPlanNoTransition(pr,myselfName,"application_"+myselfName,false,false);
	    	}catch(Exception e_handleToutBuiltIn){  
	    		println( getName() + " plan=handleToutBuiltIn WARNING:" + e_handleToutBuiltIn.getMessage() );
	    		QActorContext.terminateQActorSystem(this); 
	    	}
	    };//handleToutBuiltIn
	    
	    StateFun init = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("init",-1);
	    	String myselfName = "init";  
	    	temporaryStr = "\"radarCoapClient start.\"";
	    	println( temporaryStr );  
	    	it.unibo.radar.gui.radarGUIController.startGUI( myself  );
	    	//switchTo running
	        switchToPlanAsNextState(pr, myselfName, "client_actor_"+myselfName, 
	              "running",false, false, null); 
	    }catch(Exception e_init){  
	    	 println( getName() + " plan=init WARNING:" + e_init.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//init
	    
	    StateFun running = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_running",0);
	     pr.incNumIter(); 	
	    	String myselfName = "running";  
	    	//bbb
	     msgTransition( pr,myselfName,"client_actor_"+myselfName,false,
	          new StateFun[]{stateTab.get("getValue"), stateTab.get("putValue"), stateTab.get("stopping") }, 
	          new String[]{"true","E","get", "true","E","put", "true","M","stopMessage" },
	          3600000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_running){  
	    	 println( getName() + " plan=running WARNING:" + e_running.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//running
	    
	    StateFun getValue = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("getValue",-1);
	    	String myselfName = "getValue";  
	    	temporaryStr = "\"get\"";
	    	println( temporaryStr );  
	    	it.unibo.radar.coap.client.coapRadarClient.getResourceValue( myself  );
	    	//bbb
	     msgTransition( pr,myselfName,"client_actor_"+myselfName,true,
	          new StateFun[]{stateTab.get("readValue") }, 
	          new String[]{"true","M","value" },
	          2000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_getValue){  
	    	 println( getName() + " plan=getValue WARNING:" + e_getValue.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//getValue
	    
	    StateFun putValue = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("putValue",-1);
	    	String myselfName = "putValue";  
	    	temporaryStr = "\"put\"";
	    	println( temporaryStr );  
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("put(Distance,Angle)");
	    	if( currentEvent != null && currentEvent.getEventId().equals("put") && 
	    		pengine.unify(curT, Term.createTerm("put(Distance,Angle)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			{/* JavaLikeMove */ 
	    			String arg1 = "Distance" ;
	    			arg1 =  updateVars( Term.createTerm("put(Distance,Angle)"), Term.createTerm("put(Distance,Angle)"), 
	    				                Term.createTerm(currentEvent.getMsg()),  arg1 );	                
	    			//end arg1
	    			String arg2 = "Angle" ;
	    			arg2 =  updateVars( Term.createTerm("put(Distance,Angle)"), Term.createTerm("put(Distance,Angle)"), 
	    				                Term.createTerm(currentEvent.getMsg()),  arg2 );	                
	    			//end arg2
	    			it.unibo.radar.coap.client.coapRadarClient.putResourceValue(this,arg1,arg2 );
	    			}
	    	}
	    	repeatPlanNoTransition(pr,myselfName,"client_actor_"+myselfName,false,true);
	    }catch(Exception e_putValue){  
	    	 println( getName() + " plan=putValue WARNING:" + e_putValue.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//putValue
	    
	    StateFun readValue = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("readValue",-1);
	    	String myselfName = "readValue";  
	    	temporaryStr = "\"readValue\"";
	    	println( temporaryStr );  
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("value(Distance,Angle)");
	    	if( currentMessage != null && currentMessage.msgId().equals("value") && 
	    		pengine.unify(curT, Term.createTerm("value(Distance,Angle)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		{/* JavaLikeMove */ 
	    		String arg1 = "Distance" ;
	    		arg1 =  updateVars( Term.createTerm("value(Distance,Angle)"), Term.createTerm("value(Distance,Angle)"), 
	    			                Term.createTerm(currentMessage.msgContent()),  arg1 );	                
	    		//end arg1
	    		String arg2 = "Angle" ;
	    		arg2 =  updateVars( Term.createTerm("value(Distance,Angle)"), Term.createTerm("value(Distance,Angle)"), 
	    			                Term.createTerm(currentMessage.msgContent()),  arg2 );	                
	    		//end arg2
	    		it.unibo.radar.gui.radarGUIController.setValue(this,arg1,arg2 );
	    		}
	    	}
	    	//switchTo running
	        switchToPlanAsNextState(pr, myselfName, "client_actor_"+myselfName, 
	              "running",false, false, null); 
	    }catch(Exception e_readValue){  
	    	 println( getName() + " plan=readValue WARNING:" + e_readValue.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//readValue
	    
	    StateFun stopping = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("stopping",-1);
	    	String myselfName = "stopping";  
	    	temporaryStr = "\"radarCoapClient stop.\"";
	    	println( temporaryStr );  
	    	repeatPlanNoTransition(pr,myselfName,"client_actor_"+myselfName,false,false);
	    }catch(Exception e_stopping){  
	    	 println( getName() + " plan=stopping WARNING:" + e_stopping.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//stopping
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}
