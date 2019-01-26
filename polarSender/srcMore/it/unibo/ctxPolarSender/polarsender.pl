%====================================================================================
% Context ctxPolarSender  SYSTEM-configuration: file it.unibo.ctxPolarSender.polarsender.pl 
%====================================================================================
context(ctxpolarsender, "localhost",  "TCP", "8009" ).  		 
context(ctxradarbase, "localhost",  "TCP", "8033" ).  		 
%%% -------------------------------------------
qactor( sender_actor , ctxpolarsender, "it.unibo.sender_actor.MsgHandle_Sender_actor"   ). %%store msgs 
qactor( sender_actor_ctrl , ctxpolarsender, "it.unibo.sender_actor.Sender_actor"   ). %%control-driven 
%%% -------------------------------------------
%%% -------------------------------------------

