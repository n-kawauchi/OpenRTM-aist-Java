package jp.go.aist.rtm.RTC;

import jp.go.aist.rtm.RTC.util.Properties;
import RTC.ComponentProfile;
import RTC.ExecutionContextListHolder;
import RTC.ExecutionKind;
import RTC.LifeCycleState;
//import RTC.PortListHolder;
import RTC.ReturnCode_t;
import RTMExamples.SimpleIO.ConsoleOut;
import RTMExamples.SimpleIO.ConsoleOutComp;
import _SDOPackage.NVListHolder;

/**
* ConsoleOut　テスト(21)
* 対象クラス：ConsoleOut, ConsoleOutComp
*/
public class ConsoleOutTest extends SampleTest {
    private String configPath;
    private RTObject_impl comp;
    private  Manager manager;

    protected void setUp() throws Exception {
        super.setUp();
        java.io.File fileCurrent = new java.io.File(".");
        String rootPath = fileCurrent.getAbsolutePath();
        rootPath = rootPath.substring(0,rootPath.length()-1);
        configPath = rootPath + "src\\RTMExamples\\SimpleIO\\rtc.conf";
        //
        String args[] = {"-f", configPath };
        try {
            manager = new Manager();
            manager.initManager(args);
            manager.initLogger();
            manager.initORB();
            manager.initNaming();
            manager.initExecContext();
            manager.initTimer();
        } catch (Exception e) {
            manager = null;
        }
        ConsoleOutComp init = new ConsoleOutComp();
        manager.setModuleInitProc(init);
        manager.activateManager();
        String component_conf[] = {
                "implementation_id", "ConsoleOut",
                "type_name",         "ConsoleOut",
                "description",       "Console output component",
                "version",           "1.0",
                "vendor",            "Noriaki Ando, AIST",
                "category",          "example",
                "activity_type",     "DataFlowComponent",
                "max_instance",      "10",
                "language",          "C++",
                "lang_type",         "compile",
                ""
                };
      Properties prop = new Properties(component_conf);
      manager.registerFactory(prop, new ConsoleOut(), new ConsoleOut());
      comp = manager.createComponent("ConsoleOut");
    }
    protected void tearDown() throws Exception {
        for(int intIdx=0;intIdx<manager.m_ecs.size();intIdx++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        manager.shutdownComponents();
        manager.shutdownNaming();
        manager = null;
        super.tearDown();
    }

    /**
     *<pre>
     * ComponentProfileのチェック
     *　・設定したComponentProfileが取得できるか？
     *　・設定したPort情報を取得できるか？
     *　・設定したPortProfileを取得できるか？
     *</pre>
     */
    public void test_profile() {
/*
        ComponentProfile prof = comp.get_component_profile();
        assertTrue(prof.instance_name.startsWith("ConsoleOut"));
        assertEquals("ConsoleOut", prof.type_name);
        assertEquals("Console output component", prof.description);
        assertEquals("1.0", prof.version);
        assertEquals("Noriaki Ando, AIST", prof.vendor);
        assertEquals("example", prof.category);
        //
        PortListHolder portlist = new PortListHolder(comp.get_ports());
        assertEquals( 1, portlist.value.length);
        assertEquals( "in", portlist.value[0].get_port_profile().name);
        //
        Properties prop = new Properties();
        this.copyToProperties(prop, new NVListHolder(portlist.value[0].get_port_profile().properties));
        assertEquals( "DataInPort", prop.getProperty("port.port_type"));
        assertEquals( "TimedLong", prop.getProperty("dataport.data_type"));
        assertEquals( "CORBA_Any,TCP_Any", prop.getProperty("dataport.interface_type"));
        assertEquals( "Push, Pull", prop.getProperty("dataport.dataflow_type"));
        assertEquals( "Any", prop.getProperty("dataport.subscription_type"));
        //
*/
    }
    
    /**
     *<pre>
     * ExecutionContextのチェック
     *  ・RTCのalive状態を取得できるか？
     *　・ExecutionContextの実行状態を取得できるか？
     *　・ExecutionContextの種類を取得できるか？
     *　・ExecutionContextの更新周期を設定できるか？
     *　・ExecutionContextの更新周期を取得できるか？
     *　・ExecutionContextを停止できるか？
     *　・停止したExecutionContextを再度停止した場合にエラーが返ってくるか？
     *　・ExecutionContextを開始できるか？
     *　・開始したExecutionContextを再度開始した場合にエラーが返ってくるか？
     *</pre>
     */
    public void test_EC() {
/*
        assertEquals(true, comp.is_alive());
        ExecutionContextListHolder execlist = new ExecutionContextListHolder();
        execlist.value = comp.get_contexts();
        assertEquals(true, execlist.value[0].is_running());
        assertEquals(ExecutionKind.PERIODIC, execlist.value[0].get_kind());
        assertEquals(1000.0, execlist.value[0].get_rate());
        //
        ReturnCode_t result = execlist.value[0].stop();
        assertEquals(ReturnCode_t.RTC_OK, result);
        assertEquals(false, execlist.value[0].is_running());
        result = execlist.value[0].stop();
        assertEquals(ReturnCode_t.PRECONDITION_NOT_MET, result);
        //
        result = execlist.value[0].start();
        assertEquals(ReturnCode_t.RTC_OK, result);
        assertEquals(true, execlist.value[0].is_running());
        result = execlist.value[0].start();
        assertEquals(ReturnCode_t.PRECONDITION_NOT_MET, result);
        //
*/
    }

    /**
     *<pre>
     * RTCのチェック
     *  ・RTCの状態を取得できるか？
     *　・Inactive状態でdeactivateした場合にエラーが返ってくるか？
     *　・RTCをactivateできるか？
     *　・Active状態でactivateした場合にエラーが返ってくるか？
     *　・Active状態でresetした場合にエラーが返ってくるか？
     *　・Active状態でfinalizeした場合にエラーが返ってくるか？
     *　・RTCをfinalizeできるか？
     *　・RTCをfinalizeしてもalive状態か？
     *　・RTCをexitしたらalive状態から抜けるか？
     *</pre>
     */
    public void test_State() {
/*
        ExecutionContextListHolder execlist = new ExecutionContextListHolder();
        execlist.value = comp.get_contexts();
        assertEquals(LifeCycleState.INACTIVE_STATE, execlist.value[0].get_component_state(comp.getObjRef()));
        ReturnCode_t result = execlist.value[0].deactivate_component(comp.getObjRef());
        assertEquals(ReturnCode_t.PRECONDITION_NOT_MET, result);
        result = execlist.value[0].start();
        //
        result = execlist.value[0].activate_component(comp.getObjRef());
        assertEquals(ReturnCode_t.RTC_OK, result);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
//        assertEquals(LifeCycleState.ACTIVE_STATE, execlist.value[0].get_component_state(comp.getObjRef()));
        result = execlist.value[0].activate_component(comp.getObjRef());
//        assertEquals(ReturnCode_t.PRECONDITION_NOT_MET, result);
        assertEquals(LifeCycleState.ACTIVE_STATE, execlist.value[0].get_component_state(comp.getObjRef()));
        //
        result = execlist.value[0].reset_component(comp.getObjRef());
        assertEquals(ReturnCode_t.PRECONDITION_NOT_MET, result);
        //
        result = comp._finalize();
        assertEquals(ReturnCode_t.PRECONDITION_NOT_MET,  result);
        //
        result = execlist.value[0].stop();
        result = comp._finalize();
        assertEquals(ReturnCode_t.RTC_OK,  result);
//        assertEquals(true, comp.is_alive());
//        result = comp.exit();
        assertEquals(false, comp.is_alive());
*/
    }

}
