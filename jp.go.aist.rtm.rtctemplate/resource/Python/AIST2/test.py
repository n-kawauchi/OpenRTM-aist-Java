#!/usr/bin/env python
# -*- Python -*-

import sys
import time
sys.path.append(".")

# Import RTM module
import OpenRTM
import RTC

# Import Service implementation class
# <rtc-template block="service_impl">

# </rtc-template>

# Import Service stub modules
# <rtc-template block="consumer_import">
import _GlobalIDL, _GlobalIDL__POA

# </rtc-template>


# This module's spesification
# <rtc-template block="module_spec">
test_spec = ["implementation_id", "test", 
		 "type_name",         "test", 
		 "description",       "test component", 
		 "version",           "1.0.0", 
		 "vendor",            "S.Kurihara", 
		 "category",          "example", 
		 "activity_type",     "STATIC", 
		 "max_instance",      "1", 
		 "language",          "Python", 
		 "lang_type",         "SCRIPT",
		 ""]
# </rtc-template>

class test(OpenRTM.DataFlowComponentBase):
	def __init__(self, manager):
		OpenRTM.DataFlowComponentBase.__init__(self, manager)

		self._d_in = RTC.TimedFloatSeq(RTC.Time(0,0),[])
		self._inIn = OpenRTM.InPort("in", self._d_in, OpenRTM.RingBuffer(8))
		self._d_out = RTC.TimedFloatSeq(RTC.Time(0,0),[])
		self._outOut = OpenRTM.OutPort("out", self._d_out, OpenRTM.RingBuffer(8))
		

		# Set InPort buffers
		self.registerInPort("in",self._inIn)
		
		# Set OutPort buffers
		self.registerOutPort("out",self._outOut)
		

		self._MySVConPort = OpenRTM.CorbaPort("MySVCon")
		

		

		self._myservice = OpenRTM.CorbaConsumer(interfaceType=_GlobalIDL.MyService)
		
		# Set service provider to Ports
		
		# Set service consumers to Ports
		self._MySVConPort.registerConsumer("myservice", "MyService", self._myservice)
		
		# Set CORBA Service Ports
		self.registerPort(self._MySVConPort)
		

		# initialize of configuration-data.
		# <rtc-template block="init_conf_param">
		
		# </rtc-template>


		 
	def onInitialize(self):
		# Bind variables and configuration variable
		
		return RTC.RTC_OK


	
	#def onFinalize(self, ec_id):
	#
	#	return RTC.RTC_OK
	
	#def onStartup(self, ec_id):
	#
	#	return RTC.RTC_OK
	
	#def onShutdown(self, ec_id):
	#
	#	return RTC.RTC_OK
	
	#def onActivated(self, ec_id):
	#
	#	return RTC.RTC_OK
	
	#def onDeactivated(self, ec_id):
	#
	#	return RTC.RTC_OK
	
	#def onExecute(self, ec_id):
	#
	#	return RTC.RTC_OK
	
	#def onAborting(self, ec_id):
	#
	#	return RTC.RTC_OK
	
	#def onError(self, ec_id):
	#
	#	return RTC.RTC_OK
	
	#def onReset(self, ec_id):
	#
	#	return RTC.RTC_OK
	
	#def onStateUpdate(self, ec_id):
	#
	#	return RTC.RTC_OK
	
	#def onRateChanged(self, ec_id):
	#
	#	return RTC.RTC_OK
	



def MyModuleInit(manager):
    profile = OpenRTM.Properties(defaults_str=test_spec)
    manager.registerFactory(profile,
                            test,
                            OpenRTM.Delete)

    # Create a component
    comp = manager.createComponent("test")



def main():
	mgr = OpenRTM.Manager.init(len(sys.argv), sys.argv)
	mgr.setModuleInitProc(MyModuleInit)
	mgr.activateManager()
	mgr.runManager()

if __name__ == "__main__":
	main()

