%====================================================================================
% Context ctxRadarBase  SYSTEM-configuration: file it.unibo.ctxRadarBase.radargui.pl 
%====================================================================================
context(ctxradarbase, "localhost",  "TCP", "8033" ).  		 
%%% -------------------------------------------
qactor( radarguibase , ctxradarbase, "it.unibo.radarguibase.MsgHandle_Radarguibase"   ). %%store msgs 
qactor( radarguibase_ctrl , ctxradarbase, "it.unibo.radarguibase.Radarguibase"   ). %%control-driven 
qactor( tester , ctxradarbase, "it.unibo.tester.MsgHandle_Tester"   ). %%store msgs 
qactor( tester_ctrl , ctxradarbase, "it.unibo.tester.Tester"   ). %%control-driven 
%%% -------------------------------------------
eventhandler(evh,ctxradarbase,"it.unibo.ctxRadarBase.Evh","alarm").  
%%% -------------------------------------------

