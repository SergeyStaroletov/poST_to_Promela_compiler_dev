//-----------------------------------------------------------------------------
//-----------------------------------------------------------------------------
//metadata && init
//-----------------------------------------------------------------------------
//-----------------------------------------------------------------------------
            
mtype:P__ = {
    Init__p0,
    UpperGateSim__p,
    LowerGateSim__p,
    UpperValveSim__p,
    LowerValveSim__p,
    WaterLevelSim__p,
    Init__p1,
    Waiting4Ship__p,
    UpControl__p,
    DownControl__p,
    Water2Low__p,
    Water2High__p,
    OpenLowGate__p,
    CloseLowGate__p,
    OpenHighGate__p,
    CloseHighGate__p,
    LightOFF__p,
    Low2ChmbrON__p,
    Chmbr2LowON__p,
    High2ChmbrON__p,
    Chmbr2HighON__p
}
chan __currentProcess = [1] of { mtype:P__ };

mtype:Init__S1 = {
    begin__s3,
    Stop__s14,
    Error__s11
}
mtype:Init__S1 Init__cs0 = begin__s3;

mtype:UpperGateSim__S = {
    check_open_close__s1,
    Stop__s1,
    Error__s12
}
mtype:UpperGateSim__S UpperGateSim__cs = Stop__s1;

mtype:LowerGateSim__S = {
    check_open_close__s3,
    Stop__s16,
    Error__s3
}
mtype:LowerGateSim__S LowerGateSim__cs = Stop__s16;

mtype:UpperValveSim__S = {
    check_open_close__s2,
    Stop__s10,
    Error__s6
}
mtype:UpperValveSim__S UpperValveSim__cs = Stop__s10;

mtype:LowerValveSim__S = {
    check_open_close__s0,
    Stop__s2,
    Error__s15
}
mtype:LowerValveSim__S LowerValveSim__cs = Stop__s2;

mtype:WaterLevelSim__S = {
    check_open_close__s4,
    Stop__s4,
    Error__s5
}
mtype:WaterLevelSim__S WaterLevelSim__cs = Stop__s4;

mtype:Init__S0 = {
    begin__s2,
    Stop__s15,
    Error__s0
}
mtype:Init__S0 Init__cs1 = begin__s2;

mtype:Waiting4Ship__S = {
    init__s3,
    CheckUp__s,
    CheckDown__s,
    Stop__s20,
    Error__s16
}
mtype:Waiting4Ship__S Waiting4Ship__cs = Stop__s20;

mtype:UpControl__S = {
    begin__s0,
    Wait4High__s,
    Wait4Open__s1,
    Wait4ShipGetOutChmbr__s0,
    Wait4ShipGetInChmbr__s1,
    Wait4Close__s0,
    Stop__s3,
    Error__s19
}
mtype:UpControl__S UpControl__cs = Stop__s3;

mtype:DownControl__S = {
    begin__s1,
    Wait4Low__s,
    Wait4Open__s0,
    Wait4ShipGetOutChmbr__s1,
    Wait4ShipGetInChmbr__s0,
    Wait4Close__s1,
    Stop__s19,
    Error__s2
}
mtype:DownControl__S DownControl__cs = Stop__s19;

mtype:Water2Low__S = {
    CheckConditions__s5,
    CheckLevel__s1,
    Stop__s12,
    Error__s14
}
mtype:Water2Low__S Water2Low__cs = Stop__s12;

mtype:Water2High__S = {
    CheckConditions__s3,
    CheckLevel__s0,
    Stop__s0,
    Error__s13
}
mtype:Water2High__S Water2High__cs = Stop__s0;

mtype:OpenLowGate__S = {
    CheckConditions__s4,
    CheckOpeness__s3,
    Stop__s18,
    Error__s9
}
mtype:OpenLowGate__S OpenLowGate__cs = Stop__s18;

mtype:CloseLowGate__S = {
    CheckConditions__s2,
    CheckOpeness__s1,
    Stop__s7,
    Error__s20
}
mtype:CloseLowGate__S CloseLowGate__cs = Stop__s7;

mtype:OpenHighGate__S = {
    CheckConditions__s0,
    CheckOpeness__s2,
    Stop__s17,
    Error__s17
}
mtype:OpenHighGate__S OpenHighGate__cs = Stop__s17;

mtype:CloseHighGate__S = {
    CheckConditions__s1,
    CheckOpeness__s0,
    Stop__s6,
    Error__s1
}
mtype:CloseHighGate__S CloseHighGate__cs = Stop__s6;

mtype:LightOFF__S = {
    init__s4,
    Stop__s8,
    Error__s7
}
mtype:LightOFF__S LightOFF__cs = Stop__s8;

mtype:Low2ChmbrON__S = {
    init__s0,
    Stop__s9,
    Error__s4
}
mtype:Low2ChmbrON__S Low2ChmbrON__cs = Stop__s9;

mtype:Chmbr2LowON__S = {
    init__s1,
    Stop__s13,
    Error__s8
}
mtype:Chmbr2LowON__S Chmbr2LowON__cs = Stop__s13;

mtype:High2ChmbrON__S = {
    init__s5,
    Stop__s5,
    Error__s10
}
mtype:High2ChmbrON__S High2ChmbrON__cs = Stop__s5;

mtype:Chmbr2HighON__S = {
    init__s2,
    Stop__s11,
    Error__s18
}
mtype:Chmbr2HighON__S Chmbr2HighON__cs = Stop__s11;

init {
    __currentProcess ! Init__p0;
}



//-----------------------------------------------------------------------------
//-----------------------------------------------------------------------------
//program Plant
//-----------------------------------------------------------------------------
//-----------------------------------------------------------------------------

//inout
bool shipInHigh__1;
bool shipInLow__0;
bool shipInChmbr__1;
bool atLow__1;
bool atHigh__1;
bool HighGateOpened__0;
bool LowGateOpened__1;
bool HighGateClosed__0;
bool LowGateClosed__0;
bool HighValveOpened__0;
bool LowValveOpened__0;
bool openHighGate__1;
bool openLowGate__0;
bool openHighValve__0;
bool openLowValve__1;
bool Low2ChmbrLight__1;
bool Chmbr2LowLight__1;
bool Chmbr2HighLight__0;
bool High2ChmbrLight__1;


//-----------------------------------------------------------------------------
//Init__1
//-----------------------------------------------------------------------------

active proctype Init__1() {
    do :: __currentProcess ? Init__p0 ->
        atomic {
            if
                :: begin__s3 == Init__cs0 -> {
                    shipInHigh__1 = false;
                    shipInLow__0 = false;
                    shipInChmbr__1 = false;
                    atLow__1 = true;
                    atHigh__1 = false;
                    HighGateOpened__0 = false;
                    LowGateOpened__1 = false;
                    HighValveOpened__0 = false;
                    LowValveOpened__0 = false;
                    openHighGate__1 = false;
                    openLowGate__0 = false;
                    openHighValve__0 = false;
                    openLowValve__1 = false;
                    Low2ChmbrLight__1 = false;
                    Chmbr2LowLight__1 = false;
                    Chmbr2HighLight__0 = false;
                    High2ChmbrLight__1 = false;
                    UpperGateSim__cs = check_open_close__s1;
                    LowerGateSim__cs = check_open_close__s3;
                    UpperValveSim__cs = check_open_close__s2;
                    LowerValveSim__cs = check_open_close__s0;
                    WaterLevelSim__cs = check_open_close__s4;
                    Init__cs0 = Stop__s14;
                }
                :: else -> skip;
            fi;
            __currentProcess ! UpperGateSim__p;
        }
    od;
}


//-----------------------------------------------------------------------------
//UpperGateSim
//-----------------------------------------------------------------------------

//constants
#define GATE_SPEED__0 1
#define GATE_OPEN_COORD__0 100

//vars
int coord__0 = 0;

active proctype UpperGateSim() {
    do :: __currentProcess ? UpperGateSim__p ->
        atomic {
            if
                :: check_open_close__s1 == UpperGateSim__cs -> {
                    if
                        :: openHighGate__1 -> {
                            coord__0 = coord__0 + GATE_SPEED__0;
                        }
                        :: else -> {
                            coord__0 = coord__0 - GATE_SPEED__0;
                        }
                    fi;
                    if :: coord__0 <= 0 -> {
                        coord__0 = 0;
                    } :: else -> skip; fi;
                    if :: coord__0 >= GATE_OPEN_COORD__0 -> {
                        coord__0 = GATE_OPEN_COORD__0;
                    } :: else -> skip; fi;
                    if
                        :: coord__0 == GATE_OPEN_COORD__0 -> {
                            HighGateOpened__0 = true;
                        }
                        :: else -> {
                            HighGateOpened__0 = false;
                        }
                    fi;
                    if
                        :: coord__0 == 0 -> {
                            HighGateClosed__0 = true;
                        }
                        :: else -> {
                            HighGateClosed__0 = false;
                        }
                    fi;
                }
                :: else -> skip;
            fi;
            __currentProcess ! LowerGateSim__p;
        }
    od;
}


//-----------------------------------------------------------------------------
//LowerGateSim
//-----------------------------------------------------------------------------

//constants
#define GATE_SPEED__1 1
#define GATE_OPEN_COORD__1 100

//vars
int coord__2 = 0;

active proctype LowerGateSim() {
    do :: __currentProcess ? LowerGateSim__p ->
        atomic {
            if
                :: check_open_close__s3 == LowerGateSim__cs -> {
                    if
                        :: openLowGate__0 -> {
                            coord__2 = coord__2 + GATE_SPEED__1;
                        }
                        :: else -> {
                            coord__2 = coord__2 - GATE_SPEED__1;
                        }
                    fi;
                    if :: coord__2 <= 0 -> {
                        coord__2 = 0;
                    } :: else -> skip; fi;
                    if :: coord__2 >= GATE_OPEN_COORD__1 -> {
                        coord__2 = GATE_OPEN_COORD__1;
                    } :: else -> skip; fi;
                    if
                        :: coord__2 == GATE_OPEN_COORD__1 -> {
                            LowGateOpened__1 = true;
                        }
                        :: else -> {
                            LowGateOpened__1 = false;
                        }
                    fi;
                    if
                        :: coord__2 == 0 -> {
                            LowGateClosed__0 = true;
                        }
                        :: else -> {
                            LowGateClosed__0 = false;
                        }
                    fi;
                }
                :: else -> skip;
            fi;
            __currentProcess ! UpperValveSim__p;
        }
    od;
}


//-----------------------------------------------------------------------------
//UpperValveSim
//-----------------------------------------------------------------------------

active proctype UpperValveSim() {
    do :: __currentProcess ? UpperValveSim__p ->
        atomic {
            if
                :: check_open_close__s2 == UpperValveSim__cs -> {
                    if
                        :: openHighValve__0 == true -> {
                            HighValveOpened__0 = true;
                        }
                        :: else -> {
                            HighValveOpened__0 = false;
                        }
                    fi;
                }
                :: else -> skip;
            fi;
            __currentProcess ! LowerValveSim__p;
        }
    od;
}


//-----------------------------------------------------------------------------
//LowerValveSim
//-----------------------------------------------------------------------------

active proctype LowerValveSim() {
    do :: __currentProcess ? LowerValveSim__p ->
        atomic {
            if
                :: check_open_close__s0 == LowerValveSim__cs -> {
                    if
                        :: openLowValve__1 == true -> {
                            LowValveOpened__0 = true;
                        }
                        :: else -> {
                            LowValveOpened__0 = false;
                        }
                    fi;
                }
                :: else -> skip;
            fi;
            __currentProcess ! WaterLevelSim__p;
        }
    od;
}


//-----------------------------------------------------------------------------
//WaterLevelSim
//-----------------------------------------------------------------------------

//constants
#define OUTFLOW_RATE 1
#define UPPER_LEVEL 100
#define LOWER_LEVEL 0

//vars
int coord__1 = 0;

active proctype WaterLevelSim() {
    do :: __currentProcess ? WaterLevelSim__p ->
        atomic {
            if
                :: check_open_close__s4 == WaterLevelSim__cs -> {
                    if :: openLowValve__1 -> {
                        coord__1 = coord__1 - OUTFLOW_RATE;
                    } :: else -> skip; fi;
                    if :: openHighValve__0 -> {
                        coord__1 = coord__1 + OUTFLOW_RATE;
                    } :: else -> skip; fi;
                    if :: coord__1 >= UPPER_LEVEL -> {
                        coord__1 = UPPER_LEVEL;
                    } :: else -> skip; fi;
                    if :: coord__1 <= LOWER_LEVEL -> {
                        coord__1 = LOWER_LEVEL;
                    } :: else -> skip; fi;
                    if
                        :: coord__1 == LOWER_LEVEL -> {
                            atLow__1 = true;
                        }
                        :: else -> {
                            atLow__1 = false;
                        }
                    fi;
                    if
                        :: coord__1 == UPPER_LEVEL -> {
                            atHigh__1 = true;
                        }
                        :: else -> {
                            atHigh__1 = false;
                        }
                    fi;
                }
                :: else -> skip;
            fi;
            __currentProcess ! Init__p1;
        }
    od;
}



//-----------------------------------------------------------------------------
//-----------------------------------------------------------------------------
//program Controller
//-----------------------------------------------------------------------------
//-----------------------------------------------------------------------------

//inout
bool shipInHigh__0;
bool shipInLow__1;
bool shipInChmbr__0;
bool atLow__0;
bool atHigh__0;
bool HighGateOpened__1;
bool LowGateOpened__0;
bool HighGateClosed__1;
bool LowGateClosed__1;
bool HighValveOpened__1;
bool LowValveOpened__1;
bool openHighGate__0;
bool openLowGate__1;
bool openHighValve__1;
bool openLowValve__0;
bool Low2ChmbrLight__0;
bool Chmbr2LowLight__0;
bool Chmbr2HighLight__1;
bool High2ChmbrLight__0;


//-----------------------------------------------------------------------------
//Init__0
//-----------------------------------------------------------------------------

active proctype Init__0() {
    do :: __currentProcess ? Init__p1 ->
        atomic {
            if
                :: begin__s2 == Init__cs1 -> {
                    openHighGate__0 = false;
                    openLowGate__1 = false;
                    openHighValve__1 = false;
                    openLowValve__0 = false;
                    Low2ChmbrLight__0 = false;
                    Chmbr2LowLight__0 = false;
                    Chmbr2HighLight__1 = false;
                    High2ChmbrLight__0 = false;
                    Waiting4Ship__cs = init__s3;
                    Init__cs1 = Stop__s15;
                }
                :: else -> skip;
            fi;
            __currentProcess ! Waiting4Ship__p;
        }
    od;
}


//-----------------------------------------------------------------------------
//Waiting4Ship
//-----------------------------------------------------------------------------

active proctype Waiting4Ship() {
    do :: __currentProcess ? Waiting4Ship__p ->
        atomic {
            if
                :: init__s3 == Waiting4Ship__cs -> {
                    if
                        :: (shipInHigh__0 && shipInLow__1) -> {
                            if
                                :: (atHigh__0 || atLow__0) -> {
                                    if :: atHigh__0 -> {
                                        DownControl__cs = begin__s1;
                                        Waiting4Ship__cs = CheckDown__s;
                                    } :: else -> skip; fi;
                                    if :: atLow__0 -> {
                                        UpControl__cs = begin__s0;
                                        Waiting4Ship__cs = CheckUp__s;
                                    } :: else -> skip; fi;
                                }
                                :: else -> {
                                    DownControl__cs = begin__s1;
                                    Waiting4Ship__cs = CheckDown__s;
                                }
                            fi;
                        }
                        :: else -> {
                            if :: shipInHigh__0 -> {
                                DownControl__cs = begin__s1;
                                Waiting4Ship__cs = CheckDown__s;
                            } :: else -> skip; fi;
                            if :: shipInLow__1 -> {
                                UpControl__cs = begin__s0;
                                Waiting4Ship__cs = CheckUp__s;
                            } :: else -> skip; fi;
                        }
                    fi;
                }
                :: CheckUp__s == Waiting4Ship__cs -> {
                    if :: UpControl__p == Stop__s3 -> {
                        DownControl__cs = begin__s1;
                        Waiting4Ship__cs = CheckDown__s;
                    } :: else -> skip; fi;
                }
                :: CheckDown__s == Waiting4Ship__cs -> {
                    if :: DownControl__p == Stop__s19 -> {
                        UpControl__cs = begin__s0;
                        Waiting4Ship__cs = CheckUp__s;
                    } :: else -> skip; fi;
                }
                :: else -> skip;
            fi;
            __currentProcess ! UpControl__p;
        }
    od;
}


//-----------------------------------------------------------------------------
//UpControl
//-----------------------------------------------------------------------------

active proctype UpControl() {
    do :: __currentProcess ? UpControl__p ->
        atomic {
            if
                :: begin__s0 == UpControl__cs -> {
                    LightOFF__cs = init__s4;
                    if
                        :: shipInHigh__0 || shipInChmbr__0 -> {
                            Water2High__cs = CheckConditions__s3;
                            UpControl__cs = Wait4High__s;
                        }
                        :: else -> {
                            UpControl__cs = Stop__s3;
                        }
                    fi;
                }
                :: Wait4High__s == UpControl__cs -> {
                    if :: (Water2High__p == Stop__s0) -> {
                        OpenHighGate__cs = CheckConditions__s0;
                        UpControl__cs = Wait4Open__s1;
                    } :: else -> skip; fi;
                }
                :: Wait4Open__s1 == UpControl__cs -> {
                    if :: (OpenHighGate__p == Stop__s17) -> {
                        if
                            :: shipInChmbr__0 -> {
                                Chmbr2HighON__cs = init__s2;
                                UpControl__cs = Wait4ShipGetOutChmbr__s0;
                            }
                            :: else -> {
                                High2ChmbrON__cs = init__s5;
                                UpControl__cs = Wait4ShipGetInChmbr__s1;
                            }
                        fi;
                    } :: else -> skip; fi;
                }
                :: Wait4ShipGetOutChmbr__s0 == UpControl__cs -> {
                    if :: (!shipInChmbr__0) -> {
                        if
                            :: (!shipInHigh__0) -> {
                                CloseHighGate__cs = CheckConditions__s1;
                                UpControl__cs = Wait4ShipGetInChmbr__s1;
                            }
                            :: else -> {
                                High2ChmbrON__cs = init__s5;
                                UpControl__cs = Wait4ShipGetInChmbr__s1;
                            }
                        fi;
                    } :: else -> skip; fi;
                }
                :: Wait4ShipGetInChmbr__s1 == UpControl__cs -> {
                    if :: (shipInChmbr__0) -> {
                        CloseHighGate__cs = CheckConditions__s1;
                        UpControl__cs = Wait4Close__s0;
                    } :: else -> skip; fi;
                }
                :: Wait4Close__s0 == UpControl__cs -> {
                    if :: (CloseHighGate__p == Stop__s6) -> {
                        UpControl__cs = Stop__s3;
                    } :: else -> skip; fi;
                }
                :: else -> skip;
            fi;
            __currentProcess ! DownControl__p;
        }
    od;
}


//-----------------------------------------------------------------------------
//DownControl
//-----------------------------------------------------------------------------

active proctype DownControl() {
    do :: __currentProcess ? DownControl__p ->
        atomic {
            if
                :: begin__s1 == DownControl__cs -> {
                    LightOFF__cs = init__s4;
                    if
                        :: shipInLow__1 || shipInChmbr__0 -> {
                            Water2Low__cs = CheckConditions__s5;
                            DownControl__cs = Wait4Low__s;
                        }
                        :: else -> {
                            DownControl__cs = Stop__s19;
                        }
                    fi;
                }
                :: Wait4Low__s == DownControl__cs -> {
                    if :: (Water2Low__p == Stop__s12) -> {
                        OpenLowGate__cs = CheckConditions__s4;
                        DownControl__cs = Wait4Open__s0;
                    } :: else -> skip; fi;
                }
                :: Wait4Open__s0 == DownControl__cs -> {
                    if :: (OpenLowGate__p == Stop__s18) -> {
                        if
                            :: shipInChmbr__0 -> {
                                Chmbr2LowON__cs = init__s1;
                                DownControl__cs = Wait4ShipGetOutChmbr__s1;
                            }
                            :: else -> {
                                Low2ChmbrON__cs = init__s0;
                                DownControl__cs = Wait4ShipGetInChmbr__s0;
                            }
                        fi;
                    } :: else -> skip; fi;
                }
                :: Wait4ShipGetOutChmbr__s1 == DownControl__cs -> {
                    if :: (!shipInChmbr__0) -> {
                        if
                            :: (!shipInLow__1) -> {
                                CloseLowGate__cs = CheckConditions__s2;
                                DownControl__cs = Wait4Close__s1;
                            }
                            :: else -> {
                                Low2ChmbrON__cs = init__s0;
                                DownControl__cs = Wait4ShipGetInChmbr__s0;
                            }
                        fi;
                    } :: else -> skip; fi;
                }
                :: Wait4ShipGetInChmbr__s0 == DownControl__cs -> {
                    if :: (shipInChmbr__0) -> {
                        CloseLowGate__cs = CheckConditions__s2;
                        DownControl__cs = Wait4Close__s1;
                    } :: else -> skip; fi;
                }
                :: Wait4Close__s1 == DownControl__cs -> {
                    if :: (CloseLowGate__p == Stop__s7) -> {
                        DownControl__cs = Stop__s19;
                    } :: else -> skip; fi;
                }
                :: else -> skip;
            fi;
            __currentProcess ! Water2Low__p;
        }
    od;
}


//-----------------------------------------------------------------------------
//Water2Low
//-----------------------------------------------------------------------------

active proctype Water2Low() {
    do :: __currentProcess ? Water2Low__p ->
        atomic {
            if
                :: CheckConditions__s5 == Water2Low__cs -> {
                    if
                        :: (atLow__0) -> {
                            Water2Low__cs = Stop__s12;
                        }
                        :: else -> {
                            if
                                :: (!HighGateClosed__1) -> {
                                    Water2Low__cs = Error__s14;
                                }
                                :: else -> {
                                    openLowValve__0 = true;
                                    openHighValve__1 = false;
                                    Water2Low__cs = CheckLevel__s1;
                                }
                            fi;
                        }
                    fi;
                }
                :: CheckLevel__s1 == Water2Low__cs -> {
                    if :: (atLow__0) -> {
                        openLowValve__0 = false;
                        Water2Low__cs = Stop__s12;
                    } :: else -> skip; fi;
                }
                :: else -> skip;
            fi;
            __currentProcess ! Water2High__p;
        }
    od;
}


//-----------------------------------------------------------------------------
//Water2High
//-----------------------------------------------------------------------------

active proctype Water2High() {
    do :: __currentProcess ? Water2High__p ->
        atomic {
            if
                :: CheckConditions__s3 == Water2High__cs -> {
                    if
                        :: (atHigh__0) -> {
                            Water2High__cs = Stop__s0;
                        }
                        :: else -> {
                            if
                                :: (!LowGateClosed__1) -> {
                                    Water2High__cs = Error__s13;
                                }
                                :: else -> {
                                    openLowValve__0 = false;
                                    openHighValve__1 = true;
                                    Water2High__cs = CheckLevel__s0;
                                }
                            fi;
                        }
                    fi;
                }
                :: CheckLevel__s0 == Water2High__cs -> {
                    if :: (atHigh__0) -> {
                        openHighValve__1 = false;
                        Water2High__cs = Stop__s0;
                    } :: else -> skip; fi;
                }
                :: else -> skip;
            fi;
            __currentProcess ! OpenLowGate__p;
        }
    od;
}


//-----------------------------------------------------------------------------
//OpenLowGate
//-----------------------------------------------------------------------------

active proctype OpenLowGate() {
    do :: __currentProcess ? OpenLowGate__p ->
        atomic {
            if
                :: CheckConditions__s4 == OpenLowGate__cs -> {
                    if
                        :: (LowGateOpened__0) -> {
                            OpenLowGate__cs = Stop__s18;
                        }
                        :: else -> {
                            if
                                :: (!atLow__0) -> {
                                    OpenLowGate__cs = Error__s9;
                                }
                                :: else -> {
                                    openLowGate__1 = true;
                                    OpenLowGate__cs = CheckOpeness__s3;
                                }
                            fi;
                        }
                    fi;
                }
                :: CheckOpeness__s3 == OpenLowGate__cs -> {
                    if :: (LowGateOpened__0) -> {
                        OpenLowGate__cs = Stop__s18;
                    } :: else -> skip; fi;
                }
                :: else -> skip;
            fi;
            __currentProcess ! CloseLowGate__p;
        }
    od;
}


//-----------------------------------------------------------------------------
//CloseLowGate
//-----------------------------------------------------------------------------

active proctype CloseLowGate() {
    do :: __currentProcess ? CloseLowGate__p ->
        atomic {
            if
                :: CheckConditions__s2 == CloseLowGate__cs -> {
                    if
                        :: (LowGateClosed__1) -> {
                            CloseLowGate__cs = Stop__s7;
                        }
                        :: else -> {
                            openLowGate__1 = false;
                            CloseLowGate__cs = CheckOpeness__s1;
                        }
                    fi;
                }
                :: CheckOpeness__s1 == CloseLowGate__cs -> {
                    if :: (LowGateClosed__1) -> {
                        CloseLowGate__cs = Stop__s7;
                    } :: else -> skip; fi;
                }
                :: else -> skip;
            fi;
            __currentProcess ! OpenHighGate__p;
        }
    od;
}


//-----------------------------------------------------------------------------
//OpenHighGate
//-----------------------------------------------------------------------------

active proctype OpenHighGate() {
    do :: __currentProcess ? OpenHighGate__p ->
        atomic {
            if
                :: CheckConditions__s0 == OpenHighGate__cs -> {
                    if
                        :: (HighGateOpened__1) -> {
                            OpenHighGate__cs = Stop__s17;
                        }
                        :: else -> {
                            if
                                :: (!atHigh__0) -> {
                                    OpenHighGate__cs = Error__s17;
                                }
                                :: else -> {
                                    openHighGate__0 = true;
                                    OpenHighGate__cs = CheckOpeness__s2;
                                }
                            fi;
                        }
                    fi;
                }
                :: CheckOpeness__s2 == OpenHighGate__cs -> {
                    if :: (HighGateOpened__1) -> {
                        OpenHighGate__cs = Stop__s17;
                    } :: else -> skip; fi;
                }
                :: else -> skip;
            fi;
            __currentProcess ! CloseHighGate__p;
        }
    od;
}


//-----------------------------------------------------------------------------
//CloseHighGate
//-----------------------------------------------------------------------------

active proctype CloseHighGate() {
    do :: __currentProcess ? CloseHighGate__p ->
        atomic {
            if
                :: CheckConditions__s1 == CloseHighGate__cs -> {
                    if
                        :: (HighGateClosed__1) -> {
                            CloseHighGate__cs = Stop__s6;
                        }
                        :: else -> {
                            openHighGate__0 = false;
                            CloseHighGate__cs = CheckOpeness__s0;
                        }
                    fi;
                }
                :: CheckOpeness__s0 == CloseHighGate__cs -> {
                    if :: (HighGateClosed__1) -> {
                        CloseHighGate__cs = Stop__s6;
                    } :: else -> skip; fi;
                }
                :: else -> skip;
            fi;
            __currentProcess ! LightOFF__p;
        }
    od;
}


//-----------------------------------------------------------------------------
//LightOFF
//-----------------------------------------------------------------------------

active proctype LightOFF() {
    do :: __currentProcess ? LightOFF__p ->
        atomic {
            if
                :: init__s4 == LightOFF__cs -> {
                    Low2ChmbrLight__0 = false;
                    Chmbr2LowLight__0 = false;
                    Chmbr2HighLight__1 = false;
                    High2ChmbrLight__0 = false;
                    LightOFF__cs = Stop__s8;
                }
                :: else -> skip;
            fi;
            __currentProcess ! Low2ChmbrON__p;
        }
    od;
}


//-----------------------------------------------------------------------------
//Low2ChmbrON
//-----------------------------------------------------------------------------

active proctype Low2ChmbrON() {
    do :: __currentProcess ? Low2ChmbrON__p ->
        atomic {
            if
                :: init__s0 == Low2ChmbrON__cs -> {
                    Low2ChmbrLight__0 = true;
                    Chmbr2LowLight__0 = false;
                    Chmbr2HighLight__1 = false;
                    High2ChmbrLight__0 = false;
                    Low2ChmbrON__cs = Stop__s9;
                }
                :: else -> skip;
            fi;
            __currentProcess ! Chmbr2LowON__p;
        }
    od;
}


//-----------------------------------------------------------------------------
//Chmbr2LowON
//-----------------------------------------------------------------------------

active proctype Chmbr2LowON() {
    do :: __currentProcess ? Chmbr2LowON__p ->
        atomic {
            if
                :: init__s1 == Chmbr2LowON__cs -> {
                    Low2ChmbrLight__0 = false;
                    Chmbr2LowLight__0 = true;
                    Chmbr2HighLight__1 = false;
                    High2ChmbrLight__0 = false;
                    Chmbr2LowON__cs = Stop__s13;
                }
                :: else -> skip;
            fi;
            __currentProcess ! High2ChmbrON__p;
        }
    od;
}


//-----------------------------------------------------------------------------
//High2ChmbrON
//-----------------------------------------------------------------------------

active proctype High2ChmbrON() {
    do :: __currentProcess ? High2ChmbrON__p ->
        atomic {
            if
                :: init__s5 == High2ChmbrON__cs -> {
                    Low2ChmbrLight__0 = false;
                    Chmbr2LowLight__0 = false;
                    Chmbr2HighLight__1 = false;
                    High2ChmbrLight__0 = true;
                    High2ChmbrON__cs = Stop__s5;
                }
                :: else -> skip;
            fi;
            __currentProcess ! Chmbr2HighON__p;
        }
    od;
}


//-----------------------------------------------------------------------------
//Chmbr2HighON
//-----------------------------------------------------------------------------

active proctype Chmbr2HighON() {
    do :: __currentProcess ? Chmbr2HighON__p ->
        atomic {
            if
                :: init__s2 == Chmbr2HighON__cs -> {
                    Low2ChmbrLight__0 = false;
                    Chmbr2LowLight__0 = false;
                    Chmbr2HighLight__1 = true;
                    High2ChmbrLight__0 = false;
                    Chmbr2HighON__cs = Stop__s11;
                }
                :: else -> skip;
            fi;
            __currentProcess ! Init__p0;
        }
    od;
}


//-----------------------------------------------------------------------------
//-----------------------------------------------------------------------------
//ltl
//-----------------------------------------------------------------------------
//-----------------------------------------------------------------------------

#define apply1__ltl(f, arg) f(arg)
#define apply2__ltl(f, arg) f(apply1__ltl(f, arg))
#define apply3__ltl(f, arg) f(apply2__ltl(f, arg))
#define apply4__ltl(f, arg) f(apply3__ltl(f, arg))
#define apply5__ltl(f, arg) f(apply4__ltl(f, arg))
#define apply6__ltl(f, arg) f(apply5__ltl(f, arg))
#define apply7__ltl(f, arg) f(apply6__ltl(f, arg))
#define apply8__ltl(f, arg) f(apply7__ltl(f, arg))
#define apply9__ltl(f, arg) f(apply8__ltl(f, arg))
#define apply10__ltl(f, arg) f(apply9__ltl(f, arg))
#define apply__ltl(n, f, arg) apply##n##__ltl(f, arg)

#define afterCycle__ltl(expr) (cycle__u U (!cycle__u W (cycle__u && (expr))))
#define afterNCyclesWith__ltl(n, cond, expr) (apply__ltl(n, (cond) -> afterCycle__ltl, expr))
#define afterNCyclesOrSoonerWith__ltl(n, cond, expr) afterNCyclesWith__ltl(n, (cond) && !(expr), expr)

//-----------------------------------------------------------------------------
//ltl between cycles
//-----------------------------------------------------------------------------

#define cltl(expr) (cycle__u -> (expr))
#define G__cltl(expr) [](cycle__u -> (expr))
#define F__cltl(expr) <>(cycle__u && (expr))
#define U__cltl(expr1, expr2) (cycle__u -> (expr1)) U (cycle__u && (expr2))
#define W__cltl(expr1, expr2) (cycle__u -> (expr1)) W (cycle__u && (expr2))
#define V__cltl(expr1, expr2) (cycle__u && (expr1)) V (cycle__u -> (expr2))

#define next__cltl(expr) (cycle__u -> afterCycle__ltl(expr))
#define afterNWith__cltl(n, cond, expr) (cycle__u -> afterNCyclesWith__ltl(n, cond, expr))
#define afterNOrSoonerWith__cltl(n, cond, expr) (cycle__u -> afterNCyclesOrSoonerWith__ltl(n, cond, expr))
