package jp.go.aist.rtm.RTC.util;

import junit.framework.TestCase;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;

import RTC.ComponentProfile;
import OpenRTM.DataFlowComponentPOA;
import RTC.ExecutionContext;
import RTC.ExecutionContextService;
import RTC.FsmObjectPOA;
import RTC.FsmParticipantPOA;
import RTC.LightweightRTObject;
import RTC.Mode;
import RTC.MultiModeObjectPOA;
import RTC.PortService;
import RTC.ReturnCode_t;
import _SDOPackage.Configuration;
import _SDOPackage.DeviceProfile;
import _SDOPackage.Monitoring;
import _SDOPackage.NameValue;
import _SDOPackage.Organization;
import _SDOPackage.SDOService;
import _SDOPackage.ServiceProfile;

/**
* RTCUtilクラス　テスト
* 対象クラス：RTCUtil
*/
public class RTCUtilTests extends TestCase {

    class DataFlowComponentMock extends DataFlowComponentPOA {
        //全ての関数を記述する必要あり
        public Organization[] get_owned_organizations() { return null; }
        public String get_sdo_id() { return null; }
        public String get_sdo_type() { return null; }
        public DeviceProfile get_device_profile() { return null; }
        public ServiceProfile[] get_service_profiles() { return null; }
        public ServiceProfile get_service_profile(String id) { return null; }
        public SDOService get_sdo_service(String id) { return null; }
        public Configuration get_configuration() { return null; }
        public Monitoring get_monitoring() { return null; }
        public Organization[] get_organizations() { return null; }
        public NameValue[] get_status_list() { return null; }
        public Any get_status(String id) { return null; }
        public ReturnCode_t on_execute(int id) { return ReturnCode_t.RTC_OK; }
        public ReturnCode_t on_state_update(int id) { return ReturnCode_t.RTC_OK; }
        public ReturnCode_t on_rate_changed(int id) { return ReturnCode_t.RTC_OK; }
        public int attach_executioncontext(ExecutionContext ec) { return 0; }
        public ReturnCode_t detach_executioncontext(int id) { return ReturnCode_t.RTC_OK; }
        public ReturnCode_t on_initialize() { return ReturnCode_t.RTC_OK; }
        public ReturnCode_t on_finalize() { return ReturnCode_t.RTC_OK; }
        public ReturnCode_t on_startup(int id) { return ReturnCode_t.RTC_OK; }
        public ReturnCode_t on_shutdown(int id) { return ReturnCode_t.RTC_OK; }
        public ReturnCode_t on_activated(int id) { return ReturnCode_t.RTC_OK; }
        public ReturnCode_t on_deactivated(int id) { return ReturnCode_t.RTC_OK; }
        public ReturnCode_t on_aborting(int id) { return ReturnCode_t.RTC_OK; }
        public ReturnCode_t on_error(int id) { return ReturnCode_t.RTC_OK; }
        public ReturnCode_t on_reset(int id) { return ReturnCode_t.RTC_OK; }
        public ReturnCode_t initialize() { return ReturnCode_t.RTC_OK; }
        public ReturnCode_t _finalize() { return ReturnCode_t.RTC_OK; }
        public ReturnCode_t exit() { return ReturnCode_t.RTC_OK; }
        public boolean is_alive(ExecutionContext ec) { return true; }
        public ExecutionContext[] get_contexts() { return null; }
        public ExecutionContext get_context(int id) { return null; }
        public ComponentProfile get_component_profile() { return null; }
        public PortService[] get_ports() { return null; }
        public ExecutionContextService[] get_execution_context_services() { return null; }
        public int get_context_handle(ExecutionContext ec) { return 0; }
        public int attach_context(ExecutionContext ec) { return 0; }
        public ReturnCode_t detach_context(int ec_id) { return null; }
        public ExecutionContext[] get_owned_contexts() { return null; }
        public ExecutionContext[] get_participating_contexts() { return null; }
    };


    class FsmObjectMock extends FsmObjectPOA {
        public ReturnCode_t send_stimulus(String ids, int id) { return ReturnCode_t.RTC_OK; }
    };


    class FsmParticipantObjectMock extends FsmParticipantPOA {
        public ReturnCode_t initialize() { return null; }
        public ReturnCode_t _finalize() { return null; }
        public boolean is_alive(ExecutionContext ec) { return true; }
        public ReturnCode_t exit() { return null; }
        public int attach_context(ExecutionContext ec) { return 0; }
        public ReturnCode_t detach_context(int ec_id) { return null; }
        public ExecutionContext get_context(int ec_id) { return null; }
        public ExecutionContext[] get_owned_contexts() { return null; }
        public ExecutionContext[] get_participating_contexts() { return null; }
        public int get_context_handle(ExecutionContext ec) { return 0; }
        public ReturnCode_t on_initialize() { return null; }
        public ReturnCode_t on_finalize() { return null; }
        public ReturnCode_t on_startup(int ec_id) { return null; }
        public ReturnCode_t on_shutdown(int ec_id) { return null; }
        public ReturnCode_t on_activated(int ec_id) { return null; }
        public ReturnCode_t on_deactivated(int ec_id) { return null; }
        public ReturnCode_t on_aborting(int ec_id) { return null; }
        public ReturnCode_t on_error(int ec_id) { return null; }
        public ReturnCode_t on_reset(int ec_id) { return null; }
        public ReturnCode_t on_action(int ec_id) { return null; }
    };


    class MultiModeObjectMock extends MultiModeObjectPOA {
        public ReturnCode_t initialize() { return null; }
        public ReturnCode_t _finalize() { return null; }
        public boolean is_alive(ExecutionContext ec) { return false; }
        public ReturnCode_t exit() { return null; }
        public int attach_context(ExecutionContext ec) { return 0; }
        public ReturnCode_t detach_context(int ec_id) { return null; }
        public ExecutionContext get_context(int ec_id) { return null; }
        public ExecutionContext[] get_owned_contexts() { return null; }
        public ExecutionContext[] get_participating_contexts() { return null; }
        public int get_context_handle(ExecutionContext ec) { return 0; }
        public ReturnCode_t on_initialize() { return null; }
        public ReturnCode_t on_finalize() { return null; }
        public ReturnCode_t on_startup(int ec_id) { return null; }
        public ReturnCode_t on_shutdown(int ec_id) { return null; }
        public ReturnCode_t on_activated(int ec_id) { return null; }
        public ReturnCode_t on_deactivated(int ec_id) { return null; }
        public ReturnCode_t on_aborting(int ec_id) { return null; }
        public ReturnCode_t on_error(int ec_id) { return null; }
        public ReturnCode_t on_reset(int ec_id) { return null; }
        public Mode get_default_mode() { return null; }
        public Mode get_current_mode() { return null; }
        public Mode get_current_mode_in_context(ExecutionContext ec) { return null; }
        public Mode get_pending_mode() { return null; }
        public Mode get_pending_mode_in_context(ExecutionContext ec) { return null; }
        public ReturnCode_t set_mode(Mode mode , boolean flag) { return ReturnCode_t.RTC_OK; }
        public ReturnCode_t on_mode_changed(int ec_id) { return ReturnCode_t.RTC_OK; }
    };


    private ORB m_pORB;
    private POA m_pPOA;

    protected void setUp() throws Exception {
        super.setUp();

        // (1-1) ORBの初期化
        java.util.Properties props = new java.util.Properties();
        props.put("org.omg.CORBA.ORBInitialPort", "2809");
        props.put("org.omg.CORBA.ORBInitialHost", "localhost");
        this.m_pORB = ORB.init(new String[0], props);

        // (1-2) POAManagerのactivate
        this.m_pPOA = org.omg.PortableServer.POAHelper.narrow(
                this.m_pORB.resolve_initial_references("RootPOA"));
        this.m_pPOA.the_POAManager().activate();
    }
    protected void tearDown() throws Exception {
        super.tearDown();
        
        this.m_pORB.destroy();
    }

    public void test_isDataFlowComponent_DataFlowComponent() throws Exception{
        DataFlowComponentMock obj = new DataFlowComponentMock();
        this.m_pPOA.activate_object(obj);
        org.omg.CORBA.Object ref = obj._this();
        assertNotNull(ref);
        
        assertTrue(RTCUtil.isDataFlowComponent(ref));
        assertFalse(RTCUtil.isFsmObject(ref));
        assertFalse(RTCUtil.isFsmParticipant(ref));
    }
    
    public void test_isDataFlowComponent_FsmObject() throws Exception{
        DataFlowComponentMock obj = new DataFlowComponentMock();
        this.m_pPOA.activate_object(obj);
        org.omg.CORBA.Object ref = obj._this();
        assertNotNull(ref);
        
        assertFalse(RTCUtil.isFsmObject(ref));
    }

    public void test_isDataFlowComponent_FsmParticipant() throws Exception{
        DataFlowComponentMock obj = new DataFlowComponentMock();
        this.m_pPOA.activate_object(obj);
        org.omg.CORBA.Object ref = obj._this();
        assertNotNull(ref);
        
        assertFalse(RTCUtil.isFsmParticipant(ref));
    }

    public void test_isDataFlowComponent_MultiModeObject() throws Exception{
        DataFlowComponentMock obj = new DataFlowComponentMock();
        this.m_pPOA.activate_object(obj);
        org.omg.CORBA.Object ref = obj._this();
        assertNotNull(ref);
        
        assertFalse(RTCUtil.isMultiModeObject(ref));
    }

    //
    public void test_isFsmObject_DataFlowComponent() throws Exception{
        FsmObjectMock obj = new FsmObjectMock();
        this.m_pPOA.activate_object(obj);
        org.omg.CORBA.Object ref = obj._this();
        assertNotNull(ref);
        
        assertFalse(RTCUtil.isDataFlowComponent(ref));
    }

    public void test_isFsmObject_FsmObject() throws Exception{
        FsmObjectMock obj = new FsmObjectMock();
        this.m_pPOA.activate_object(obj);
        org.omg.CORBA.Object ref = obj._this();
        assertNotNull(ref);
        
        assertTrue(RTCUtil.isFsmObject(ref));
    }

    public void test_isFsmObject_FsmParticipant() throws Exception{
        FsmObjectMock obj = new FsmObjectMock();
        this.m_pPOA.activate_object(obj);
        org.omg.CORBA.Object ref = obj._this();
        assertNotNull(ref);
        
        assertFalse(RTCUtil.isFsmParticipant(ref));
    }

    public void test_isFsmObject_MultiModeObject() throws Exception{
        FsmObjectMock obj = new FsmObjectMock();
        this.m_pPOA.activate_object(obj);
        org.omg.CORBA.Object ref = obj._this();
        assertNotNull(ref);
        
        assertFalse(RTCUtil.isMultiModeObject(ref));
    }

    //
    public void test_isFsmParticipant_DataFlowComponent() throws Exception{
        FsmParticipantObjectMock obj = new FsmParticipantObjectMock();
        this.m_pPOA.activate_object(obj);
        org.omg.CORBA.Object ref = obj._this();
        assertNotNull(ref);
        
        assertFalse(RTCUtil.isDataFlowComponent(ref));
    }

    public void test_isFsmParticipant_FsmObject() throws Exception{
        FsmParticipantObjectMock obj = new FsmParticipantObjectMock();
        this.m_pPOA.activate_object(obj);
        org.omg.CORBA.Object ref = obj._this();
        assertNotNull(ref);
        
        assertFalse(RTCUtil.isFsmObject(ref));
    }

    public void test_isFsmParticipant_FsmParticipant() throws Exception{
        FsmParticipantObjectMock obj = new FsmParticipantObjectMock();
        this.m_pPOA.activate_object(obj);
        org.omg.CORBA.Object ref = obj._this();
        assertNotNull(ref);
        
        assertTrue(RTCUtil.isFsmParticipant(ref));
    }

    public void test_isFsmParticipant_MultiModeObject() throws Exception{
        FsmParticipantObjectMock obj = new FsmParticipantObjectMock();
        this.m_pPOA.activate_object(obj);
        org.omg.CORBA.Object ref = obj._this();
        assertNotNull(ref);
        
        assertFalse(RTCUtil.isMultiModeObject(ref));
    }
    
    //
    public void test_isMultiModeObject_DataFlowComponent() throws Exception{
        MultiModeObjectMock obj = new MultiModeObjectMock();
        this.m_pPOA.activate_object(obj);
        org.omg.CORBA.Object ref = obj._this();
        assertNotNull(ref);
        
        assertFalse(RTCUtil.isDataFlowComponent(ref));
    }

    public void test_isMultiModeObject_FsmObject() throws Exception{
        MultiModeObjectMock obj = new MultiModeObjectMock();
        this.m_pPOA.activate_object(obj);
        org.omg.CORBA.Object ref = obj._this();
        assertNotNull(ref);
        
        assertFalse(RTCUtil.isFsmObject(ref));
    }

    public void test_isMultiModeObject_FsmParticipant() throws Exception{
        MultiModeObjectMock obj = new MultiModeObjectMock();
        this.m_pPOA.activate_object(obj);
        org.omg.CORBA.Object ref = obj._this();
        assertNotNull(ref);
        
        assertFalse(RTCUtil.isFsmParticipant(ref));
    }

    public void test_isMultiModeObject_MultiModeObject() throws Exception{
        MultiModeObjectMock obj = new MultiModeObjectMock();
        this.m_pPOA.activate_object(obj);
        org.omg.CORBA.Object ref = obj._this();
        assertNotNull(ref);
        
        assertTrue(RTCUtil.isMultiModeObject(ref));
    }
}
