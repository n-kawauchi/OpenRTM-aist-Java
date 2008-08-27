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
# </rtc-template>


# This module's spesification
# <rtc-template block="module_spec">
foo_spec = ["implementation_id", "foo", 
		 "type_name",         "foo", 
		 "description",       "test component", 
		 "version",           "1.0.1", 
		 "vendor",            "TA", 
		 "category",          "sample", 
		 "activity_type",     "STATIC", 
		 "max_instance",      "2", 
		 "language",          "Python", 
		 "lang_type",         "SCRIPT",
		 ""]
# </rtc-template>

class foo(OpenRTM.DataFlowComponentBase):
	def __init__(self, manager):
		OpenRTM.DataFlowComponentBase.__init__(self, manager)

		self._d_in1 = RTC.TimedShort(RTC.Time(0,0),0)
		self._in1In = OpenRTM.InPort("in1", self._d_in1, OpenRTM.RingBuffer(8))
		self._d_ = RTC.TimedLong(RTC.Time(0,0),0)
		self._Out = OpenRTM.OutPort("", self._d_, OpenRTM.RingBuffer(8))
		

		# Set InPort buffers
		self.registerInPort("in1",self._in1In)
		
		# Set OutPort buffers
		self.registerOutPort("",self._Out)
		

		

		

		
		# Set service provider to Ports
		
		# Set service consumers to Ports
		
		# Set CORBA Service Ports
		

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
    profile = OpenRTM.Properties(defaults_str=foo_spec)
    manager.registerFactory(profile,
                            foo,
                            OpenRTM.Delete)

    # Create a component
    comp = manager.createComponent("foo")



def main():
	mgr = OpenRTM.Manager.init(len(sys.argv), sys.argv)
	mgr.setModuleInitProc(MyModuleInit)
	mgr.activateManager()
	mgr.runManager()

if __name__ == "__main__":
	main()

