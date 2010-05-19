package jp.go.aist.rtm.RTC.port;

import jp.go.aist.rtm.RTC.InPortProviderFactory;
import jp.go.aist.rtm.RTC.ObjectCreator;
import jp.go.aist.rtm.RTC.ObjectDestructor;
import jp.go.aist.rtm.RTC.buffer.BufferBase;
import jp.go.aist.rtm.RTC.log.Logbuf;
import jp.go.aist.rtm.RTC.util.CORBA_SeqUtil;
import jp.go.aist.rtm.RTC.util.NVListHolderFactory;
import jp.go.aist.rtm.RTC.util.NVUtil;
import jp.go.aist.rtm.RTC.util.ORBUtil;
import jp.go.aist.rtm.RTC.util.POAUtil;
import jp.go.aist.rtm.RTC.util.Properties;

import org.omg.CORBA.ORB;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.OutputStream;

import OpenRTM.InPortCdrPOA;
import _SDOPackage.NVListHolder;

import com.sun.corba.se.impl.encoding.EncapsOutputStream; 

/**
 * <p> InPortCorbaCdrProvider </p>
 * <p> InPortCorbaCdrProvider class </p>
 *
 * <p>  This is an implementation class of the input port Provider  </p>
 * <p>  that uses CORBA for means of communication. </p>
 *
 *
 */
public class InPortCorbaCdrProvider extends InPortCdrPOA implements InPortProvider, ObjectCreator<InPortProvider>, ObjectDestructor {
    /**
     * <p> Constructor </p>
     *
     * <p>  Set the following items to port properties </p>
     * <p>   - Interface type : CORBA_Any </p>
     * <p>   - Data flow type : Push, Pull </p>
     * <p>   - Subscription type : Any </p>
     *
     *
     */
    public InPortCorbaCdrProvider() {
        m_buffer = null; 
        rtcout = new Logbuf("InPortCorbaCdrProvider");
        // PortProfile setting
        setInterfaceType("corba_cdr");
    
        // ConnectorProfile setting
        m_objref = this._this();
    
        // set InPort's reference
        ORB orb = ORBUtil.getOrb();
        CORBA_SeqUtil.push_back(m_properties,
                NVUtil.newNVString("dataport.corba_cdr.inport_ior",
                              orb.object_to_string(m_objref)));
        CORBA_SeqUtil.push_back(m_properties,
                NVUtil.newNV("dataport.corba_cdr.inport_ref",
                              m_objref, OpenRTM.InPortCdr.class ));

        m_spi_orb = (com.sun.corba.se.spi.orb.ORB)ORBUtil.getOrb();

    }
    /**
     * 
     */
    public OpenRTM.InPortCdr _this() {
        
        if (this.m_objref == null) {
            try {
                this.m_objref = 
                        OpenRTM.InPortCdrHelper.narrow(POAUtil.getRef(this));
            } catch (Exception e) {
                rtcout.println(rtcout.WARN, "The exception was caught.");
                throw new IllegalStateException(e);
            }
        }
        
        return this.m_objref;
    }
    /**
     * <p> init </p>
     *
     * @param prop
     */
    public void init(Properties prop){
    }
    /**
     * <p> setBuffer </p>
     *
     * @param buffer 
     */
    public void setBuffer(BufferBase<OutputStream> buffer) {
        m_buffer = buffer;
    }

    /**
     * {@.ja [CORBA interface] バッファにデータを書き込む}
     * {@.en [CORBA interface] Write data into the buffer}
     *
     * <p>
     * {@.ja 設定されたバッファにデータを書き込む。}
     * {@.en Write data into the specified buffer.}
     * </p>
     *
     * @param data 
     *   {@.ja 書込対象データ}
     *   {@.en The target data for writing}
     *
     */
    public OpenRTM.PortStatus put(byte[] data)
      throws SystemException {

        rtcout.println(rtcout.PARANOID, "InPortCorbaCdrProvider.put()");

        if (m_buffer == null) {
            EncapsOutputStream cdr 
            = new EncapsOutputStream(m_spi_orb,m_connector.isLittleEndian());
            cdr.write_octet_array(data, 0, data.length);
            onReceiverError(cdr);
            return OpenRTM.PortStatus.PORT_ERROR;
        }


        rtcout.println(rtcout.PARANOID, "received data size: "+data.length);


        EncapsOutputStream cdr 
            = new EncapsOutputStream(m_spi_orb,m_connector.isLittleEndian());
        cdr.write_octet_array(data, 0, data.length);

        int len = cdr.toByteArray().length;
        rtcout.println(rtcout.PARANOID, "converted CDR data size: "+len);
        onReceived(cdr);
        jp.go.aist.rtm.RTC.buffer.ReturnCode ret = m_buffer.write(cdr);
        return convertReturn(ret,cdr);
    }

    public OpenRTM.PortStatus put(final OpenRTM.CdrDataHolder data)
      throws SystemException {
        return put(data.value);

    }
    /**
     * {@.ja リターンコード変換}
     * {p.en Return codes conversion}
     */
    protected OpenRTM.PortStatus 
    convertReturn(jp.go.aist.rtm.RTC.buffer.ReturnCode status,
                  final EncapsOutputStream data) {
        switch (status) {
            case BUFFER_OK:
                onBufferWrite(data);
                return OpenRTM.PortStatus.from_int(OpenRTM.PortStatus._PORT_OK);
            case BUFFER_ERROR:
                onReceiverError(data);
                return OpenRTM.PortStatus.from_int(
                                            OpenRTM.PortStatus._PORT_ERROR);

            case BUFFER_FULL:
                onBufferFull(data);
                onReceiverFull(data);
                return OpenRTM.PortStatus.from_int(
                                            OpenRTM.PortStatus._BUFFER_FULL);

            case BUFFER_EMPTY:
                // never come here
                return OpenRTM.PortStatus.from_int(
                                            OpenRTM.PortStatus._BUFFER_EMPTY);
            case TIMEOUT:
                onBufferWriteTimeout(data);
                onReceiverTimeout(data);
                return OpenRTM.PortStatus.from_int(
                                            OpenRTM.PortStatus._BUFFER_TIMEOUT);
            case PRECONDITION_NOT_MET:
                onReceiverError(data);
                return OpenRTM.PortStatus.from_int(
                                            OpenRTM.PortStatus._PORT_ERROR);
            default:
                onReceiverError(data);
                return OpenRTM.PortStatus.from_int(
                                            OpenRTM.PortStatus._UNKNOWN_ERROR);
        }
    }

    private Logbuf rtcout;

    /**
     * <p> creator_ </p>
     * 
     * @return Object Created instances
     *
     */
    public InPortProvider creator_() {
        return new InPortCorbaCdrProvider();
    }
    /**
     * <p> destructor_ </p>
     * 
     * @param obj    The target instances for destruction
     *
     */
    public void destructor_(Object obj) {
        try{
            byte[] oid 
                = _default_POA().servant_to_id((InPortCorbaCdrProvider)obj);
            _default_POA().deactivate_object(oid);
        }
        catch(Exception e){
            e.printStackTrace();
        } 
        obj = null;
    }

    /**
     * <p> InPortCorbaCdrProviderInit </p>
     *
     */
    public static void InPortCorbaCdrProviderInit() {
        final InPortProviderFactory<InPortProvider,String> factory 
            = InPortProviderFactory.instance();

        factory.addFactory("corba_cdr",
                    new InPortCorbaCdrProvider(),
                    new InPortCorbaCdrProvider());
    
    }
    /**
     * <p>InterfaceProfile情報を公開します。</p>
     * 
     * @param properties InterfaceProfile情報を受け取るホルダオブジェクト
     */
    public void publishInterfaceProfile(NVListHolder properties) {

        NVUtil.appendStringValue(properties, "dataport.interface_type",
                this.m_interfaceType);
        NVUtil.append(properties, this.m_properties);
    }
    
    /**
     * <p>Interface情報を公開します。</p>
     * 
     * @param properties Interface情報を受け取るホルダオブジェクト
     */
    public boolean publishInterface(NVListHolder properties) {

        rtcout.println(rtcout.TRACE, "publishInterface()");
        rtcout.println(rtcout.DEBUG, NVUtil.toString(properties));


        if (! NVUtil.isStringValue(properties,
                "dataport.interface_type",
                this.m_interfaceType)) {
            return false;
        }

        NVUtil.append(properties, this.m_properties);
        return true;

    }

    public void setListener(ConnectorBase.ConnectorInfo info, 
                            ConnectorListeners listeners) {
        m_profile = info;
        m_listeners = listeners;
    }
    /**
     * <p> setConnecotor </p>
     * @param connector
     */
    public void setConnector(InPortConnector connector) {
        m_connector = connector;
    }
    /**
     * <p>データタイプを設定します。</p>
     * 
     * @param dataType データタイプ
     */
    protected void setDataType(final String dataType) {
        this.m_dataType = dataType;
    }
    
    /**
     * <p>インタフェースタイプを設定します。</p>
     * 
     * @param interfaceType インタフェースタイプ
     */
     protected void setInterfaceType(final String interfaceType) {
        rtcout.println(rtcout.TRACE, "setInterfaceType("+interfaceType+")");
        this.m_interfaceType = interfaceType;
    }
    
    /**
     * <p>データフロータイプを設定します。</p>
     * 
     * @param dataflowType データフロータイプ
     */
    protected void setDataFlowType(final String dataflowType) {
        rtcout.println(rtcout.TRACE, "setDataFlowType("+dataflowType+")");
        this.m_dataflowType = dataflowType;
    }
    
    /**
     * <p>サブスクリプションタイプを設定します。</p>
     * 
     * @param subscriptionType サブスクリプションタイプ
     */
    protected void setSubscriptionType(final String subscriptionType) {
        rtcout.println(rtcout.TRACE,
                       "setSubscriptionType("+subscriptionType+")");
        this.m_subscriptionType = subscriptionType;
    }

    /**
     * <p> Connector data listener functions </p>
     */
    private void onBufferWrite(final OutputStream data) {
        m_listeners.connectorData_[ConnectorDataListenerType.ON_BUFFER_WRITE].notify(m_profile, data);
    }

    private void onBufferFull(final OutputStream data) {
      m_listeners.connectorData_[ConnectorDataListenerType.ON_BUFFER_FULL].notify(m_profile, data);
    }

    private void onBufferWriteTimeout(final OutputStream data) {
      m_listeners.connectorData_[ConnectorDataListenerType.ON_BUFFER_WRITE_TIMEOUT].notify(m_profile, data);
    }

    private void onBufferWriteOverwrite(final OutputStream data) {
      m_listeners.connectorData_[ConnectorDataListenerType.ON_BUFFER_OVERWRITE].notify(m_profile, data);
    }

//    private void onBufferRead(final OutputStream data) {
//      m_listeners.connectorData_[ConnectorDataListenerType.ON_BUFFER_READ].notify(m_profile, data);
//    }

//    private void onSend(final OutputStream data) {
//      m_listeners.connectorData_[ConnectorDataListenerType.ON_SEND].notify(m_profile, data);
//    }

    private void onReceived(final OutputStream data) {
      m_listeners.connectorData_[ConnectorDataListenerType.ON_RECEIVED].notify(m_profile, data);
    }

    private void onReceiverFull(final OutputStream data) {
      m_listeners.connectorData_[ConnectorDataListenerType.ON_RECEIVER_FULL].notify(m_profile, data);
    }

    private void onReceiverTimeout(final OutputStream data) {
      m_listeners.connectorData_[ConnectorDataListenerType.ON_RECEIVER_TIMEOUT].notify(m_profile, data);
    }

    private void onReceiverError(final OutputStream data) {
      m_listeners.connectorData_[ConnectorDataListenerType.ON_RECEIVER_ERROR].notify(m_profile, data);
    }

    /**
     * <p> Connector listener functions </p>
     */
//    private void onBufferEmpty() {
//      m_listeners.connector_[ConnectorDataListenerType.ON_BUFFER_EMPTY].notify(m_profile);
//    }

//    privaet void onBufferReadTimeout(){
//      m_listeners.connector_[ConnectorDataListenerType.ON_BUFFER_READ_TIMEOUT].notify(m_profile);
//    }

//    privaet void onSenderEmpty() {
//      m_listeners.connector_[ConnectorDataListenerType.ON_SENDER_EMPTY].notify(m_profile);
//    }

//    privaet void onSenderTimeout() {
//      m_listeners.connector_[ConnectorDataListenerType.ON_SENDER_TIMEOUT].notify(m_profile);
//    }

//    private void onSenderError(){
//      m_listeners.connector_[ConnectorDataListenerType.ON_SENDER_ERROR].notify(m_profile);
//    }



    /**
     * <p>インタフェース情報を保持するオブジェクトです。</p>
     */
    protected NVListHolder m_properties = NVListHolderFactory.create();

    private String m_dataType = new String();
    private String m_interfaceType = new String();
    private String m_dataflowType = new String();
    private String m_subscriptionType = new String();

    private BufferBase<OutputStream> m_buffer;
    private OpenRTM.InPortCdr m_objref;

    private com.sun.corba.se.spi.orb.ORB m_spi_orb;
    private InPortConnector m_connector;
    private ConnectorListeners m_listeners;
    private ConnectorBase.ConnectorInfo m_profile; 
}
