/* Generated by AN DISI Unibo */ 
package it.unibo.server;
import it.unibo.qactors.QActorContext;
import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.qactors.akka.QActorMsgQueue;

public class MsgHandle_Server extends QActorMsgQueue{
	public MsgHandle_Server(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  {
		super(actorId, myCtx, outEnvView);
	}
}