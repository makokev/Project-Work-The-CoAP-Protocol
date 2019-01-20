%====================================================================================
% Context ctxCoapClient  SYSTEM-configuration: file it.unibo.ctxCoapClient.coapClient.pl 
%====================================================================================
context(ctxcoapclient, "localhost",  "TCP", "8055" ).  		 
%%% -------------------------------------------
qactor( client_actor , ctxcoapclient, "it.unibo.client_actor.MsgHandle_Client_actor"   ). %%store msgs 
qactor( client_actor_ctrl , ctxcoapclient, "it.unibo.client_actor.Client_actor"   ). %%control-driven 
%%% -------------------------------------------
eventhandler(handlevalue,ctxcoapclient,"it.unibo.ctxCoapClient.Handlevalue","value_event").  
%%% -------------------------------------------

