%====================================================================================
% Context ctxCoapSystemV0  SYSTEM-configuration: file it.unibo.ctxCoapSystemV0.coapSystemV0.pl 
%====================================================================================
context(ctxcoapsystemv0, "localhost",  "TCP", "8000" ).  		 
context(ctxradarbase, "localhost",  "TCP", "8033" ).  		 
%%% -------------------------------------------
qactor( coap_server , ctxcoapsystemv0, "it.unibo.coap_server.MsgHandle_Coap_server"   ). %%store msgs 
qactor( coap_server_ctrl , ctxcoapsystemv0, "it.unibo.coap_server.Coap_server"   ). %%control-driven 
qactor( coap_client , ctxcoapsystemv0, "it.unibo.coap_client.MsgHandle_Coap_client"   ). %%store msgs 
qactor( coap_client_ctrl , ctxcoapsystemv0, "it.unibo.coap_client.Coap_client"   ). %%control-driven 
%%% -------------------------------------------
%%% -------------------------------------------

