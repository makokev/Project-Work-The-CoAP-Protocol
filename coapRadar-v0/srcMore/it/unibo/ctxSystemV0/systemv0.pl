%====================================================================================
% Context ctxSystemV0  SYSTEM-configuration: file it.unibo.ctxSystemV0.systemV0.pl 
%====================================================================================
context(ctxsystemv0, "localhost",  "TCP", "8000" ).  		 
context(ctxradarbase, "localhost",  "TCP", "8033" ).  		 
%%% -------------------------------------------
qactor( server , ctxsystemv0, "it.unibo.server.MsgHandle_Server"   ). %%store msgs 
qactor( server_ctrl , ctxsystemv0, "it.unibo.server.Server"   ). %%control-driven 
qactor( client , ctxsystemv0, "it.unibo.client.MsgHandle_Client"   ). %%store msgs 
qactor( client_ctrl , ctxsystemv0, "it.unibo.client.Client"   ). %%control-driven 
%%% -------------------------------------------
%%% -------------------------------------------

