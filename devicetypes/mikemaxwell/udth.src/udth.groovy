/* 
	Universal virtual DTH
  	Copyright 2016 Mike Maxwell
    
    2016-05-15	ignore duplicate input requests

	This software if free for Private Use. You may use and modify the software without distributing it.
 
	This software and derivatives may not be used for commercial purposes.
	You may not distribute or sublicense this software.
	You may not grant a sublicense to modify and distribute this software to third parties not included in the license.

	Software is provided without warranty and the software author/license owner cannot be held liable for damages.        

*/


metadata {
	definition (name: "uDTH", namespace: "MikeMaxwell", author: "mike maxwell") {
		//inputs
		capability "Switch"					//on, off
        capability "Lock"					//lock, unlock
        
        //outputs
        capability "Contact Sensor"			//"open","closed"
        capability "Motion Sensor"			//"active", "inactive"
        capability "Presence Sensor"		//"present", "not present"
        capability "Acceleration Sensor"	//"active", "inactive"
        
        //both
        capability "Door Control"			//listen for "open", "close" respond with "open" "closed"
        capability "Garage Door Control"	//listen for "open", "close" respond with "open" "closed"
        
        command "localOff"
        command "localOn"
	}
    
    preferences {
    	def s1
        def s2
        def d
    	input(
        	title			: "Device inputs\nRespond to these events/commands." 
            ,description	: null
            ,type			: "icon"
            ,required		: false
            ,defaultValue	: "st.illuminance.illuminance.dark"
        )
        input(
        	name			: "inSwitchOn"
            ,title			: "Switch (on, off)"
           	,type			: "bool"
            ,defaultValue	: true
        )
        input(
        	name			: "inDoorOn"
            ,title			: "Door Control (open, close)"
           	,type			: "bool"
            ,defaultValue	: false
        )
        input(
        	name			: "inGDoorOn"
            ,title			: "Garge Door Control (open, close)"
           	,type			: "bool"
            ,defaultValue	: false
        )
        input(
        	name			: "inLockOn"
            ,title			: "Lock (lock, unlock)"
           	,type			: "bool"
            ,defaultValue	: false
        )
        input( 
           	title			: "Device outputs\nSend the events listed below."
            ,description	: null
           	,type			: "icon"
            ,required		: false
            ,defaultValue	: "st.illuminance.illuminance.dark"
        )
        
        d = "Contact"
        s1 = "open"
        s2 = "closed"
        input( 
        	name			: "contactOn"
           	,title			: buildTitle(d,s1,s2)
           	,type			: "enum"
           	,options		: buildOptions(d,s1,s2)
           	,description	: contactOn ?: "Not Used, Tap to enable..."
        )        

		d = "Motion"
        s1 = "active"
        s2 = "inactive"
		input( 
        	name			: "motionOn"
            ,title			: buildTitle(d,s1,s2)
            ,type			: "enum"
            ,options		: buildOptions(d,s1,s2)
            ,description	: motionOn ?: "Not Used, Tap to enable..."            
        )
        
        d = "Presence"
        s1 = "present"
        s2 = "not present"
		input( 
        	name			: "presenceOn"
            ,title			: buildTitle(d,s1,s2)
            ,type			: "enum"
          	,options		: buildOptions(d,s1,s2)
            ,description	: presenceOn ?: "Not Used, Tap to enable..." 
        )
        
        d = "Door"
        s1 = "open"
        s2 = "closed"
        input( 
        	name			: "doorOn"
            ,title			: buildTitle(d,s1,s2)
            ,type			: "enum"
            ,options		: buildOptions(d,s1,s2)
            ,description	: doorOn ?: "Not Used, Tap to enable..." 
        )
        d = "Acceleration"
        s1 = "active"
        s2 = "inactive"
		input( 
        	name			: "accelOn"
            ,title			: buildTitle(d,s1,s2)
            ,type			: "enum"
          	,options		: buildOptions(d,s1,s2)
            ,description	: accelOn ?: "Not Used, Tap to enable..."  
        )
    }
  
  	simulator {
	}

	// tile definitions
	tiles (scale:1) {
    	multiAttributeTile(name:"switch", type: "generic", width: 6, height: 2, canChangeIcon: true){
			tileAttribute ("device.uDTH", key: "PRIMARY_CONTROL") {
				attributeState "on", label: '${name}', action: "localOff", icon: "st.switches.switch.on", backgroundColor: "#53a7c0"
				attributeState "off", label: '${name}', action: "localOn", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
			}
        }
		standardTile("contact", "device.contact", inactiveLabel: false, height:2, width:2, canChangeIcon: false) {
        	state "default", label:"contact\nnot used" //, backgroundColor: "#ffffff" 
            state "closed", label:'${name}', backgroundColor: "#53a7c0", icon:"st.contact.contact.closed" 
            state "open", label:'${name}', backgroundColor: "#e6971b", icon:"st.contact.contact.open" 
		}
        standardTile("motion", "device.motion", inactiveLabel: false, height:2, width:2, canChangeIcon: false) {
            state "default", label: "motion\nnot used" //, icon:"st.motion.motion.inactive"
            state "inactive", label:'${name}', backgroundColor: "#ffffff", icon:"st.motion.motion.inactive" 
            state "active", label:'${name}', backgroundColor: "#53a7c0", icon:"st.motion.motion.active" 
        }
           standardTile("present", "device.presence", inactiveLabel: false, height:2, width:2, canChangeIcon: false) {
            state "default", label: "presence\nnot used"
            state "not present", label:'${name}', backgroundColor: "#ffffff", icon:"st.presence.tile.presence-default" 
            state "present", label:'${name}', backgroundColor: "#53a7c0", icon:"st.presence.tile.presence-default" 
        }
        standardTile("door", "device.door", inactiveLabel: false, height:2, width:2, canChangeIcon: false) {
            state "default", label: "door\nnot used" 
            state "closed", label:'${name}', backgroundColor: "#53a7c0", icon:"st.doors.garage.garage-closed" 
            state "open", label:'${name}', backgroundColor: "#e6971b", icon:"st.doors.garage.garage-open" 
        }
        standardTile("accel", "device.acceleration", inactiveLabel: false, height:2, width:2, canChangeIcon: false) {
            state "default", label: "accelleration\nnot used"
            state "inactive", label:'${name}', backgroundColor: "#ffffff", icon:"st.motion.acceleration.inactive" 
            state "active", label:'${name}', backgroundColor: "#53a7c0", icon:"st.motion.acceleration.active" 
        }


        main(["switch"])
        details(["switch","contact","motion","present","door","accel"])
 	}
}

def open(){
	if (inDoorOn || inGDoorOn) localOn()
}

def close(){
	if (inDoorOn || inGDoorOn) localOff()
}

def lock(){
	if (inLockOn) localOn()
}

def unlock(){
	if (inLockOn) localOff()
}

def on(){
	if (inSwitchOn) localOn()
}

def off(){
	if (inSwitchOn) localOff()
}

def buildTitle(d,s1,s2){
	return "${d} (${s1}, ${s2})"
}

def buildOptions(d,s1,s2){
	def sOn = "on"
    def sOff = "off"
	def options = []
    options.add(["1":"when ${sOn} set ${d} to '${s1}'\nwhen ${sOff} set ${d} to '${s2}'"])
    options.add(["0":"when ${sOn} set ${d} to '${s2}'\nwhen ${sOff} set ${d} to '${s1}'"])
	return options
}

def syncDevices(cmd){
	if (cmd == null) cmd = device.currentValue("uDTH") == "on" ? "1" : "0"
    //log.debug "cmd: ${cmd}"
	if (contactOn != null){
		if (contactOn == cmd) sendEvent(name: "contact", value: "open")			//"when on send 'open'\nwhen off send 'closed'"
        else sendEvent(name: "contact", value: "closed")						//"when on send 'closed'\nwhen off send 'open'"
    } else sendEvent(name: "contact", value: null, displayed	: false)
	if (motionOn != null){
		if (motionOn == cmd) sendEvent(name: "motion", value: "active")			//"when on send 'active'\nwhen off send 'inactive'"
        else sendEvent(name: "motion", value: "inactive")						//"when on send 'inactive'\nwhen off send 'active'"
    } else sendEvent(name: "motion", value: null, displayed	: false)
	if (presenceOn != null){
		if (presenceOn == cmd) sendEvent(name: "presence", value: "present")		//"when on send 'present'\nwhen off send 'not present'"
        else sendEvent(name: "presence", value: "not present")					//"when on send 'not present'\nwhen off send 'present'"
    } else sendEvent(name: "presence", value: null, displayed	: false)
	if (doorOn != null){
		if (doorOn == cmd) sendEvent(name: "door", value: "open")			//"when on send 'active'\nwhen off send 'inactive'"
        else sendEvent(name: "door", value: "closed")						//"when on send 'inactive'\nwhen off send 'active'"
    } else sendEvent(name: "door", value: null, displayed	: false)
	if (accelOn != null){
		if (accelOn == cmd) sendEvent(name: "acceleration", value: "active")
        else sendEvent(name: "acceleration", value: "inactive")				
    } else sendEvent(name: "acceleration", value: null, displayed	: false)
    
}

def localOn() {
	if (device.currentValue("uDTH") != "on"){
    	log.info "on request: OK"
		sendEvent(name: "uDTH", value: "on" ,displayed: false)
    	syncDevices("1")
    } else {
    	log.info "on request: duplicate, ignored"
    }
}

def localOff() {
	if (device.currentValue("uDTH") != "off"){
    	log.info "off request: OK"
		sendEvent(name: "uDTH", value: "off" ,displayed: false)
    	syncDevices("0")
    } else {
    	log.info "off request: duplicate, ignored"
    }
}

//capture preference changes
def updated() {
    //log.debug "syncDevices"
    syncDevices(null)
}

def configure() {

}
