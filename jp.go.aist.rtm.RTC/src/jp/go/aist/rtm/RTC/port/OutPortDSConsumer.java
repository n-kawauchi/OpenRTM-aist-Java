package jp.go.aist.rtm.RTC.port;

import jp.go.aist.rtm.RTC.Manager;
import jp.go.aist.rtm.RTC.ObjectCreator;
import jp.go.aist.rtm.RTC.ObjectDestructor;
import jp.go.aist.rtm.RTC.OutPortConsumerFactory;
import jp.go.aist.rtm.RTC.buffer.BufferBase;
import jp.go.aist.rtm.RTC.log.Logbuf;
import jp.go.aist.rtm.RTC.util.DataRef;
import jp.go.aist.rtm.RTC.util.NVUtil;
import jp.go.aist.rtm.RTC.util.POAUtil;
import jp.go.aist.rtm.RTC.util.Properties;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.portable.OutputStream;

import _SDOPackage.NVListHolder;
import RTC.DataPullService;
import OpenRTM.CdrDataHolder;
//import OpenRTM.PortSharedMemory;
//import OpenRTM.PortSharedMemoryHelper;

/**
 * {@.ja OutPortDSConsumer クラス}
 * {@.en OutPortDSConsumer class}
 * <p>
 * {@.ja データ転送に CORBA の RTC::DataPullSerivce インターフェースを利用し
 * た、pull 型データフロー型を実現する OutPort コンシューマクラス。}
 * {@.en This is an implementation class of the output Consumer
 * that uses CORBA for means of communication.}
 *
 * @param DataType Data type for this port
 * @param DataType 
 *   {@.ja ポートのためのDataType}
 *   {@.en Data type for this port}
 *
 */
public class OutPortDSConsumer extends CorbaConsumer<DataPullService> implements OutPortConsumer, ObjectCreator<OutPortConsumer>, ObjectDestructor {
    /**
     * {@.ja コンストラクタ}
     * {@.en Constructor}
     *
     */
    public OutPortDSConsumer() {
        super(RTC.DataPullService.class);
        rtcout = new Logbuf("OutPortDSConsumer");
    }

    /**
     *
     *
     * {@.ja 設定初期化}
     * {@.en Initializing configuration}
     * <p>
     * {@.ja OutPortConsumerの各種設定を行う。実装クラスでは、与えられた
     * Propertiesから必要な情報を取得して各種設定を行う。この init() 関
     * 数は、OutPortProvider生成直後および、接続時にそれぞれ呼ばれる可
     * 能性がある。したがって、この関数は複数回呼ばれることを想定して記
     * 述されるべきである。}
     * {@.en This operation would be called to configure in initialization.
     * In the concrete class, configuration should be performed
     * getting appropriate information from the given Properties data.
     * This function might be called right after instantiation and
     * connection sequence respectivly.  Therefore, this function
     * should be implemented assuming multiple call.}
     *
     * @param prop 
     *   {@.ja 設定情報}
     *   {@.en Configuration information}
     *
     */
    public void init(Properties prop) {
        rtcout.println(Logbuf.TRACE, "OutPortDSConsumer.init()");
        m_properties = prop;
    }
    /**
     * <p>CORBAオブジェクトを設定します。</p>
     * 
     * @param obj CORBAオブジェクト
     * @return 設定に成功した場合はtrueを、さもなくばflaseを返します。
     */
/*
    public boolean setObject(Object obj) {
        
        if (super.setObject(obj)) {
            Object ref = getObject();
            if(ref!=null){
                PortSharedMemory outportcdr = PortSharedMemoryHelper.narrow(ref);
                //outportcdr.setInterface((PortSharedMemory)m_shmem);
                //outportcdr.setInterface(m_shmem._this(Manager.instance().getORB()));
                PortSharedMemory objref;
                try {
                    objref = OpenRTM.PortSharedMemoryHelper.narrow(
                                 POAUtil.getRef(m_shmem));
                } 
                catch (Exception e) {
                    return false; // object is null
                }
                outportcdr.setInterface(objref);

                //m_shmem.setInterface(outportcdr);
	        return true;
            }
        }
        return false; // object is null
    }
*/

    /**
     * {@.ja バッファをセットする}
     * {@.en Setting outside buffer's pointer}
     * <p>
     * {@.ja OutPortConsumerがデータを取り出すバッファをセットする。
     * すでにセットされたバッファがある場合、以前のバッファへの
     * ポインタに対して上書きされる。
     * OutPortProviderはバッファの所有権を仮定していないので、
     * バッファの削除はユーザの責任で行わなければならない。}
     * {@.en A pointer to a buffer from which OutPortProvider retrieve data. 
     * If already buffer is set, previous buffer's pointer will be
     * overwritten by the given pointer to a buffer.  Since
     * OutPortProvider does not assume ownership of the buffer
     * pointer, destructor of the buffer should be done by user.}
     * 
     * @param buffer 
     *   {@.ja OutPortProviderがデータを取り出すバッファへのポインタ}
     *   {@.en A pointer to a data buffer to be used by OutPortProvider}
     *
     */
    public void setBuffer(BufferBase<OutputStream> buffer) {
        rtcout.println(Logbuf.TRACE, "OutPortDSConsumer.setBuffer()");
        m_buffer = buffer;
    }
    /**
     * {@.ja リスナを設定する。}
     * {@.en Set the listener.}
     * <p>
     * {@.ja InPort はデータ送信処理における各種イベントに対して特定のリスナ
     * オブジェクトをコールするコールバック機構を提供する。詳細は
     * ConnectorListener.h の ConnectorDataListener, ConnectorListener
     * 等を参照のこと。OutPortDSProvider では、以下のコールバック
     * が提供される。
     * <ol>
     * <li>- ON_BUFFER_WRITE
     * <li>- ON_BUFFER_FULL
     * <li>- ON_RECEIVED
     * <li>- ON_RECEIVER_FULL 
     * <li>- ON_SENDER_EMPTY
     * <li>- ON_SENDER_TIMEOUT
     * <li>- ON_SENDER_ERROR</ol>}
     * {@.en OutPort provides callback functionality that calls specific
     * listener objects according to the events in the data publishing
     * process. For details, see documentation of
     * ConnectorDataListener class and ConnectorListener class in
     * ConnectorListener.h. In this OutPortDSProvider provides
     * the following callbacks.
     * <ol>
     * <li>- ON_BUFFER_WRITE
     * <li>- ON_BUFFER_FULL
     * <li>- ON_RECEIVED
     * <li>- ON_RECEIVER_FULL 
     * <li>- ON_SENDER_EMPTY
     * <li>- ON_SENDER_TIMEOUT
     * <li>- ON_SENDER_ERROR</ol>}
     *
     * @param info 
     *   {@.ja 接続情報}
     *   {@.en Connector information}
     * @param listeners 
     *   {@.ja リスナオブジェクト}
     *   {@.en Listener objects}
     */
    public void setListener(ConnectorBase.ConnectorInfo info, 
                            ConnectorListeners listeners) {
        rtcout.println(Logbuf.TRACE, "OutPortDSConsumer.setListener()");
        m_listeners = listeners;
        m_profile = info;
    }


    /**
     *
     * {@.ja データを読み出す}
     * {@.en Reads data}
     * <p>
     * {@.ja 設定されたデータを読み出す。}
     * {@.en Reads data set}
     * 
     * @param data 
     *   {@.ja 読み出したデータを受け取るオブジェクト}
     *   {@.en Object to receive the read data}
     *
     * @return 
     *   {@.ja データ読み出し処理結果(読み出し成功:true、読み出し失敗:false)}
     *   {@.en Read result (Successful:true, Failed:false)}
     *
     */
    public ReturnCode get(OutputStream data) {
        rtcout.println(Logbuf.TRACE, "OutPortDSConsumer.get()");
        return ReturnCode.UNKNOWN_ERROR;
    }
    /**
     *
     */
    public ReturnCode pull(OutputStream data) {
        rtcout.println(Logbuf.TRACE, "OutPortDSConsumer.pull()");
        //OpenRTM.CdrDataHolder cdr_data = new OpenRTM.CdrDataHolder();
        RTC.OctetSeqHolder  cdr_data = new RTC.OctetSeqHolder();
        try {
            RTC.PortStatus ret = _ptr().pull(cdr_data);
            if (ret == RTC.PortStatus.PORT_OK) {
                rtcout.println(Logbuf.DEBUG, "pull() successful");
                data.write_octet_array(cdr_data.value, 0, 
                                        cdr_data.value.length);
                rtcout.println(Logbuf.PARANOID, 
                                "CDR data length: "+cdr_data.value.length);
  
                DataRef<OutputStream> dataref = new DataRef<OutputStream>(data);
                onReceived(dataref);
                data = (EncapsOutputStreamExt)dataref.v;
                //onReceived(data);

                dataref.v = data;
                onBufferWrite(dataref);
                data = (EncapsOutputStreamExt)dataref.v;

                if (m_buffer.full()) {
                    rtcout.println(Logbuf.INFO, 
                                "InPort buffer is full.");
                    onBufferFull(data);
                    onReceiverFull(data);
                }

                m_buffer.put(data);
                m_buffer.advanceWptr();
                m_buffer.advanceRptr();

                return ReturnCode.PORT_OK;
            }
            return convertReturn(ret);
        }
        catch (Exception e) {
            rtcout.println(Logbuf.WARN, 
                                "Exception caought from OutPort.get().");
            return ReturnCode.CONNECTION_LOST;
        }
    }

    /**
     *
     * {@.ja データ受信通知への登録}
     * {@.en Subscribe the data receive notification}
     * <p>
     * {@.ja 指定されたプロパティに基づいて、データ受信通知の受け取りに
     * 登録する。}
     * {@.en Subscribe the data receive notification based on specified
     * property information}
     *
     * @param properties 
     *   {@.ja 登録情報}
     *   {@.en Subscription information}
     *
     * @return 
     *   {@.ja 登録処理結果(登録成功:true、登録失敗:false)}
     *   {@.en Subscription result (Successful:true, Failed:false)}
     *
     */
    public boolean subscribeInterface(final NVListHolder properties) {

        rtcout.println(Logbuf.TRACE, 
                            "OutPortDSConsumer.subscribeInterface()");
        int index;
        index = NVUtil.find_index(properties,
                                   "dataport.corba_cdr.outport_ior");
        if (index < 0) {
            rtcout.println(Logbuf.DEBUG, 
                            "dataport.corba_cdr.outport_ior not found.");
            return false;
        }
    
        if (NVUtil.isString(properties,
                             "dataport.corba_cdr.outport_ior")) {
            rtcout.println(Logbuf.DEBUG, 
                            "dataport.corba_cdr.outport_ior found.");
            final String ior;
            try {
                if( properties.value[index].value.type().kind() == 
                    TCKind.tk_wstring ) {
                    ior = properties.value[index].value.extract_wstring();
                } else {
                    ior = properties.value[index].value.extract_string();
                }
            }
            catch(BAD_OPERATION e) {
                rtcout.println(Logbuf.ERROR, "outport_ior has no string");
                return false;
            }

            ORB orb = Manager.instance().getORB();
            Object var = orb.string_to_object(ior);
            if (var==null) {
                rtcout.println(Logbuf.ERROR, 
                                    "invalid IOR string has been passed");
                return false;
            }
    
            if (!super.setObject(var)) {
                rtcout.println(Logbuf.ERROR, 
                                    "Invalid object reference.");
                return false;
            }
            rtcout.println(Logbuf.DEBUG, 
                                "CorbaConsumer was set successfully.");
            return true;
        }
        
        return false;
    }
    
    /**
     * {@.ja データ受信通知からの登録解除}
     * {@.en Unsubscribe the data receive notification}
     * <p>
     * {@.ja データ受信通知の受け取りから登録を解除する。}
     * {@.en Unsubscribe the data receive notification.}
     *
     * @param properties 
     *   {@.ja 登録解除情報}
     *   {@.en Unsubscription information}
     *
     */
    public void unsubscribeInterface(final NVListHolder properties) {
        rtcout.println(Logbuf.TRACE, 
                            "OutPortDSConsumer.unsubscribeInterface()");
        int index;
        index = NVUtil.find_index(properties,
                                  "dataport.corba_cdr.outport_ior");
        if (index < 0) {
            rtcout.println(Logbuf.DEBUG, 
                            "dataport.corba_cdr.outport_ior not found.");
            return;
        }
    
        final String ior;
        try {
            if( properties.value[index].value.type().kind() == 
                TCKind.tk_wstring ) {
                ior = properties.value[index].value.extract_wstring();
            } else {
                ior = properties.value[index].value.extract_string();
            }
        }
        catch(BAD_OPERATION e) {
            rtcout.println(Logbuf.ERROR, "inport_ior has no string");
            return;
        }
        rtcout.println(Logbuf.DEBUG, 
                            "dataport.corba_cdr.outport_ior found.");
        ORB orb = Manager.instance().getORB();
        Object var = orb.string_to_object(ior);
        if (_ptr()._is_equivalent(var)) {
            releaseObject();
            rtcout.println(Logbuf.DEBUG, 
                            "CorbaConsumer's reference was released.");
            return;
        }
        rtcout.println(Logbuf.ERROR, 
                            "hmm. Inconsistent object reference.");
    }
    
    /**
     * {@.ja リターンコード変換 (DataPortStatus -> BufferStatus)}
     * {@.en Return codes conversion}
     * @param status
     *   {@.ja PortStatus}
     *   {@.en PortStatus}
     * @return
     *   {@.ja ReturnCode}
     *   {@.en ReturnCode}
     */
    protected ReturnCode convertReturn(RTC.PortStatus status) {
        switch (status.value()) {
            case RTC.PortStatus._PORT_OK:
                // never comes here
                return ReturnCode.PORT_OK;

            case RTC.PortStatus._PORT_ERROR:
                onSenderError();
                return ReturnCode.PORT_ERROR;

            case RTC.PortStatus._BUFFER_FULL:
                // never comes here
                return ReturnCode.BUFFER_FULL;

            case RTC.PortStatus._BUFFER_EMPTY:
                onSenderEmpty();
                return ReturnCode.BUFFER_EMPTY;

            case RTC.PortStatus._BUFFER_TIMEOUT:
                onSenderTimeout();
                return ReturnCode.BUFFER_TIMEOUT;
 
            case RTC.PortStatus._UNKNOWN_ERROR:
                onSenderError();
                return ReturnCode.UNKNOWN_ERROR;

            default:
                onSenderError();
                return ReturnCode.UNKNOWN_ERROR;
        }
    }
    /**
     * {@.ja OutPortDSConsumer を生成する}
     * {@.en Creats OutPortDSConsumer}
     * 
     * @return 
     *   {@.ja 生成されたOutPortConsumer}
     *   {@.en Object Created instances}
     *
     *
     */
    public OutPortConsumer creator_() {
        return new OutPortDSConsumer();
    }
    /**
     * {@.ja Object を破棄する}
     * {@.en Destructs Object}
     * 
     * @param obj
     *   {@.ja 破棄するインタスタンス}
     *   {@.en The target instances for destruction}
     *
     */
    public void destructor_(java.lang.Object obj) {
        obj = null;
    }
    /**
     * <p> OutPortDSConsumerInit </p>
     *
     */
    /**
     * {@.ja モジュール初期化関数}
     * {@.en Module initialization}
     * <p>
     * {@.ja OutPortDSConsumer のファクトリを登録する初期化関数。}
     * {@.en This initialization function registers OutPortDSConsumer's
     * factory.}
     */
    public static void OutPortDSConsumerInit() {
        final OutPortConsumerFactory<OutPortConsumer,String> factory 
            = OutPortConsumerFactory.instance();

        factory.addFactory("data_service",
                    new OutPortDSConsumer(),
                    new OutPortDSConsumer());
    
    }
    /**
     * {@.ja Connectorを設定する。}
     * {@.en set Connector}
     *
     * @param connector 
     *   {@.ja InPortConnector}
     *   {@.en InPortConnector}
     */
    public void setConnector(InPortConnector connector) {
        m_connector = connector;
    }

    /**
     * <p> Connector data listener functions </p>
     */
    /**
     * {@.ja ON_BUFFER_WRITE のリスナへ通知する。}
     * {@.en Notify an ON_BUFFER_WRITE event to listeners}
     * @param data 
     *   {@.ja OutputStream}
     *   {@.en OutputStream}
     */
    private void onBufferWrite(DataRef<OutputStream> data) {
        m_listeners.connectorData_[ConnectorDataListenerType.ON_BUFFER_WRITE].notify(m_profile, data);
    }

    /**
     * {@.ja ON_BUFFER_FULL のリスナへ通知する。}
     * {@.en Notify an ON_BUFFER_FULL event to listeners}
     * @param data 
     *   {@.ja OutputStream}
     *   {@.en OutputStream}
     */
    private void onBufferFull(final OutputStream data) {
        m_listeners.connectorData_[ConnectorDataListenerType.ON_BUFFER_FULL].notify(m_profile, data);
    }

//    private void onBufferWriteTimeout(final OutputStream data) {
//        m_listeners.connectorData_[ConnectorDataListenerType.ON_BUFFER_WRITE_TIMEOUT].notify(m_profile, data);
//    }

//    private void onBufferWriteOverwrite(final OutputStream data) {
//        m_listeners.connectorData_[ConnectorDataListenerType.ON_BUFFER_OVERWRITE].notify(m_profile, data);
//    }

//    private void onBufferRead(final OutputStream data) {
//      m_listeners.connectorData_[ConnectorDataListenerType.ON_BUFFER_READ].notify(m_profile, data);
//    }

//    private void onSend(final OutputStream data) {
//        m_listeners.connectorData_[ConnectorDataListenerType.ON_SEND].notify(m_profile, data);
//    }

    /**
     * {@.ja ON_RECEIVED のリスナへ通知する。}
     * {@.en Notify an ON_RECEIVED event to listeners}
     * @param data 
     *   {@.ja OutputStream}
     *   {@.en OutputStream}
     */
    private void onReceived(DataRef<OutputStream> data) {
        m_listeners.connectorData_[ConnectorDataListenerType.ON_RECEIVED].notify(m_profile, data);
    }

    /**
     * {@.ja ON_RECEIVER_FULL のリスナへ通知する。}
     * {@.en Notify an ON_RECEIVER_FULL event to listeners}
     * @param data 
     *   {@.ja OutputStream}
     *   {@.en OutputStream}
     */
    private void onReceiverFull(final OutputStream data) {
        m_listeners.connectorData_[ConnectorDataListenerType.ON_RECEIVER_FULL].notify(m_profile, data);
    }

//    private void onReceiverTimeout(final OutputStream data) {
//        m_listeners.connectorData_[ConnectorDataListenerType.ON_RECEIVER_TIMEOUT].notify(m_profile, data);
//    }

//    private void onReceiverError(final OutputStream data) {
//        m_listeners.connectorData_[ConnectorDataListenerType.ON_RECEIVER_ERROR].notify(m_profile, data);
//    }

    /**
     * <p> Connector listener functions </p>
     */
//    private void onBufferEmpty() {
//        m_listeners.connector_[ConnectorDataListenerType.ON_BUFFER_EMPTY].notify(m_profile);
//    }

//    private void onBufferReadTimeout() {
//        m_listeners.connector_[ConnectorDataListenerType.ON_BUFFER_READ_TIMEOUT].notify(m_profile);
//    }

    /**
     * {@.ja ON_SENDER_EMPTYのリスナへ通知する。}
     * {@.en Notify an ON_SENDER_EMPTY event to listeners}
     */
    private void onSenderEmpty() {
        m_listeners.connector_[ConnectorListenerType.ON_SENDER_EMPTY].notify(m_profile);
    }

    /**
     * {@.ja ON_SENDER_TIMEOUT のリスナへ通知する。} 
     * {@.en Notify an ON_SENDER_TIMEOUT event to listeners}
     */
    private void onSenderTimeout() {
        m_listeners.connector_[ConnectorListenerType.ON_SENDER_TIMEOUT].notify(m_profile);
    }

    /**
     * {@.ja ON_SENDER_ERRORのリスナへ通知する。}
     * {@.en Notify an ON_SENDER_ERROR event to listeners}
     */
    private void onSenderError() {
        m_listeners.connector_[ConnectorListenerType.ON_SENDER_ERROR].notify(m_profile);
    }

    //    RTC::PortSharedMemory_var m_outport;
    private BufferBase<OutputStream> m_buffer;

    private Logbuf rtcout;
    private InPortConnector m_connector;
    private ConnectorListeners m_listeners;
    private ConnectorBase.ConnectorInfo m_profile;
    //private SharedMemory m_shmem = new SharedMemory();
    //private PortSharedMemory m_outportcdr;
    private DataPullService m_outportcdr;
    private Properties m_properties = new Properties();
    private static String m_mutex = new String();
}

