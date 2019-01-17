%====================================================================================
% Context ctxCoapSimpleClient  SYSTEM-configuration: file it.unibo.ctxCoapSimpleClient.coapSimpleClient.pl 
%====================================================================================
context(ctxcoapsimpleclient, "localhost",  "TCP", "8055" ).  		 
%%% -------------------------------------------
qactor( client_simple_actor , ctxcoapsimpleclient, "it.unibo.client_simple_actor.MsgHandle_Client_simple_actor"   ). %%store msgs 
qactor( client_simple_actor_ctrl , ctxcoapsimpleclient, "it.unibo.client_simple_actor.Client_simple_actor"   ). %%control-driven 
%%% -------------------------------------------
eventhandler(handlevalue,ctxcoapsimpleclient,"it.unibo.ctxCoapSimpleClient.Handlevalue","value_event").  
%%% -------------------------------------------

