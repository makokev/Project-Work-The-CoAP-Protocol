%====================================================================================
% Context ctxCoapServer  SYSTEM-configuration: file it.unibo.ctxCoapServer.coapServer.pl 
%====================================================================================
context(ctxcoapserver, "localhost",  "TCP", "8044" ).  		 
context(ctxradarbase, "localhost",  "TCP", "8033" ).  		 
%%% -------------------------------------------
qactor( server_actor , ctxcoapserver, "it.unibo.server_actor.MsgHandle_Server_actor"   ). %%store msgs 
qactor( server_actor_ctrl , ctxcoapserver, "it.unibo.server_actor.Server_actor"   ). %%control-driven 
%%% -------------------------------------------
%%% -------------------------------------------

