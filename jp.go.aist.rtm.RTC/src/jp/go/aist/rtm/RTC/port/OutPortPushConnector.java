package jp.go.aist.rtm.RTC.port;

import java.lang.reflect.Field;

import jp.go.aist.rtm.RTC.BufferFactory;
import jp.go.aist.rtm.RTC.InPortConsumerFactory;
import jp.go.aist.rtm.RTC.PublisherBaseFactory;
import jp.go.aist.rtm.RTC.SerializerFactory;
import jp.go.aist.rtm.RTC.buffer.BufferBase;
import jp.go.aist.rtm.RTC.log.Logbuf;
import jp.go.aist.rtm.RTC.port.publisher.PublisherBase;
import jp.go.aist.rtm.RTC.util.ORBUtil;
import jp.go.aist.rtm.RTC.util.StringUtil;
import jp.go.aist.rtm.RTC.util.DataRef;

import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

import org.omg.CORBA.ORB;

public class OutPortPushConnector extends OutPortConnector {
    /**
     * {@.ja コンストラクタ}
     * {@.en Constructor}
     *
     * <p>
     * {@.ja OutPortPushConnector のコンストラクタはオブジェクト生成時に下記
     * を引数にとる。ConnectorInfo は接続情報を含み、この情報に従いパブ
     * リッシャやバッファ等を生成する。InPort インターフェースに対する
     * コンシューマオブジェクトへのポインタを取り、所有権を持つので、
     * OutPortPushConnector は InPortConsumer の解体責任を持つ。各種イ
     * ベントに対するコールバック機構を提供する ConnectorListeners を持
     * ち、適切なタイミングでコールバックを呼び出す。データバッファがも
     * し OutPortBase から提供される場合はそのポインタを取る。}
     * {@.en OutPortPushConnector's constructor is given the following
     * arguments.  According to ConnectorInfo which includes
     * connection information, a publisher and a buffer are created.
     * It is also given a pointer to the consumer object for the
     * InPort interface.  The owner-ship of the pointer is owned by
     * this OutPortPushConnector, it has responsibility to destruct
     * the InPortConsumer.  OutPortPushConnector also has
     * ConnectorListeners to provide event callback mechanisms, and
     * they would be called at the proper timing.  If data buffer is
     * given by OutPortBase, the pointer to the buffer is also given
     * as arguments.}
     * </p>
     *
     * @param profile 
     *   {@.ja ConnectorInfo}
     *   {@.en ConnectorInfo}
     * @param consumer 
     *   {@.ja InPortConsumer}
     *   {@.en InPortConsumer}
     * @param listeners 
     *   {@.ja ConnectorListeners 型のリスナオブジェクトリスト}
     *   {@.en ConnectorListeners type lsitener object list}
     * @param buffer 
     *   {@.ja CdrBufferBase 型のバッファ}
     *   {@.en CdrBufferBase type buffer}
     */
    public OutPortPushConnector(ConnectorInfo profile,
                         InPortConsumer consumer,
                         ConnectorListeners listeners,
                         BufferBase<OutputStream> buffer) throws Exception {
        super(profile,listeners);
        try {
            _Constructor(profile,consumer,listeners,buffer);
        }
        catch(Exception e) {
            throw new Exception("bad_alloc()");
        } 
    }

    /**
     * {@.ja コンストラクタ}
     * {@.en Constructor}
     *
     * <p>
     * {@.ja OutPortPushConnector のコンストラクタはオブジェクト生成時に下記
     * を引数にとる。ConnectorInfo は接続情報を含み、この情報に従いパブ
     * リッシャやバッファ等を生成する。InPort インターフェースに対する
     * コンシューマオブジェクトへのポインタを取り、所有権を持つので、
     * OutPortPushConnector は InPortConsumer の解体責任を持つ。各種イ
     * ベントに対するコールバック機構を提供する ConnectorListeners を持
     * ち、適切なタイミングでコールバックを呼び出す。}
     * {@.en OutPortPushConnector's constructor is given the following
     * arguments.  According to ConnectorInfo which includes
     * connection information, a publisher and a buffer are created.
     * It is also given a pointer to the consumer object for the
     * InPort interface.  The owner-ship of the pointer is owned by
     * this OutPortPushConnector, it has responsibility to destruct
     * the InPortConsumer.  OutPortPushConnector also has
     * ConnectorListeners to provide event callback mechanisms, and
     * they would be called at the proper timing. }
     * </p>
     *
     * @param profile 
     *   {@.ja ConnectorInfo}
     *   {@.en ConnectorInfo}
     * @param consumer 
     *   {@.ja InPortConsumer}
     *   {@.en InPortConsumer}
     * @param listeners 
     *   {@.ja ConnectorListeners 型のリスナオブジェクトリスト}
     *   {@.en ConnectorListeners type lsitener object list}
     */
    public OutPortPushConnector(ConnectorInfo profile,
                         ConnectorListeners listeners,
                         InPortConsumer consumer )  throws Exception {
        super(profile,listeners);
        BufferBase<OutputStream> buffer = null;
        try {
            _Constructor(profile,consumer,listeners,buffer);
        }
        catch(Exception e) {
            throw new Exception("bad_alloc()");
        } 
    }

    private void _Constructor(ConnectorInfo profile,
                         InPortConsumer consumer,
                         ConnectorListeners listeners,
                         BufferBase<OutputStream> buffer) throws Exception {
        m_consumer = consumer;
        m_publisher = null;
        m_buffer = buffer;
        m_listeners = listeners;

        if (m_consumer == null) { 
            rtcout.println(Logbuf.PARANOID, "m_consumer is null");
            throw new Exception("bad_alloc()");
        }
        m_consumer.init(profile.properties);

        // publisher/buffer creation. This may throw std::bad_alloc;
        m_publisher = createPublisher(profile);
        if (m_buffer == null) {
            m_buffer = createBuffer(profile);
        }
        if (m_publisher == null || m_buffer == null) { 
            if (m_publisher == null) { 
                rtcout.println(Logbuf.PARANOID, "m_publisher is null");
            }
            if (m_buffer == null) { 
                rtcout.println(Logbuf.PARANOID, "m_buffer is null");
            }
            throw new Exception("bad_alloc()");
        }

        ReturnCode ret = m_publisher.init(profile.properties);
        if (!ret.equals(ReturnCode.PORT_OK)) {
            throw new Exception("bad_alloc()");
        }
        m_buffer.init(profile.properties.getNode("buffer"));
        m_publisher.setConsumer(m_consumer);
        m_publisher.setBuffer(m_buffer);
        m_publisher.setListener(m_profile, m_listeners);

        m_orb = ORBUtil.getOrb();
        
        String marshaling_type = profile.properties.getProperty( "marshaling_type", "corba");
        marshaling_type = profile.properties.getProperty( "out.marshaling_type", marshaling_type);
        m_marshaling_type = marshaling_type.trim();

        final SerializerFactory<CORBA_CdrSerializer,String> factory 
            = SerializerFactory.instance();
        m_serializer = factory.createObject(m_marshaling_type);

        onConnect();

    }
    /**
     * {@.ja データの書き込み}
     * {@.en Writing data}
     *
     * <p> 
     * {@.ja Publisherに対してデータを書き込み、これにより対応するInPortへデー
     * タが転送される。正常終了した場合 PORT_OK が返される。それ以外の
     * 場合、エラー値として、CONNECTION_LOST, BUFFER_FULL,
     * BUFFER_ERROR, PORT_ERROR, BUFFER_TIMEOUT, PRECONDITION_NO_MET が
     * 返される。}
     * {@.en This operation writes data into publisher and then the data
     * will be transferred to correspondent InPort. If data is written
     * properly, this function will return PORT_OK return code. Except
     * normal return, CONNECTION_LOST, BUFFER_FULL, BUFFER_ERROR,
     * PORT_ERROR, BUFFER_TIMEOUT and PRECONDITION_NO_MET will be
     * returned as error codes.}
     *
     * @param data
     *   {@.ja データ}
     *   {@.en Data}
     * @return ReturnCode
     *   {@.ja PORT_OK              正常終了
     *         CONNECTION_LOST      接続がロストした
     *         BUFFER_FULL          バッファが一杯である
     *         BUFFER_ERROR         バッファエラー
     *         BUFFER_TIMEOUT       バッファへの書き込みがタイムアウトした
     *         PRECONDITION_NOT_MET 事前条件を満たさない
     *         PORT_ERROR           その他のエラー}
     *   {@.en PORT_OK              Normal return
     *         CONNECTION_LOST      Connectin lost
     *         BUFFER_FULL          Buffer full
     *         BUFFER_ERROR         Buffer error
     *         BUFFER_TIMEOUT       Timeout
     *         PRECONDITION_NOT_MET Precondition not met
     *         PORT_ERROR           Other error}
     *
     */
    public <DataType> ReturnCode write(DataType data) {
        rtcout.println(Logbuf.TRACE, "write()");

        if (m_directInPort != null) {
            InPort inport = (InPort)m_directInPort;
            if(inport.isNew()) {
                // ON_BUFFER_OVERWRITE(In,Out), ON_RECEIVER_FULL(In,Out) callback
                m_listeners.
                  connectorData_[ConnectorDataListenerType.ON_BUFFER_OVERWRITE].notify(m_profile, data);
                m_inPortListeners.
                  connectorData_[ConnectorDataListenerType.ON_BUFFER_OVERWRITE].notify(m_profile, data);
                m_listeners.
                  connectorData_[ConnectorDataListenerType.ON_RECEIVER_FULL].notify(m_profile, data);
                m_inPortListeners.
                  connectorData_[ConnectorDataListenerType.ON_RECEIVER_FULL].notify(m_profile, data);
                rtcout.println(Logbuf.PARANOID, 
                    "ON_BUFFER_OVERWRITE(InPort,OutPort), "
                    + "ON_RECEIVER_FULL(InPort,OutPort) "
                    + "callback called in direct mode.");

            }
            // ON_BUFFER_WRITE(In,Out) callback
            m_listeners.
                connectorData_[ConnectorDataListenerType.ON_BUFFER_WRITE].notify(m_profile, data);
            m_inPortListeners .
                connectorData_[ConnectorDataListenerType.ON_BUFFER_WRITE].notify(m_profile, data);
            rtcout.println(Logbuf.PARANOID, 
                    "ON_BUFFER_WRITE(InPort,OutPort), "
                    + "callback called in direct mode.");
            DataRef<DataType> dataref 
                    = new DataRef<DataType>(data);
            inport.write(dataref); // write to InPort variable!!
            // ON_RECEIVED(In,Out) callback
            m_listeners.
                connectorData_[ConnectorDataListenerType.ON_RECEIVED].notify(m_profile, data);
            m_inPortListeners.
                connectorData_[ConnectorDataListenerType.ON_RECEIVED].notify(m_profile, data);
            rtcout.println(Logbuf.PARANOID, 
                    "ON_RECEIVED(InPort,OutPort), "
                    + "callback called in direct mode.");
            return ReturnCode.PORT_OK;

/*
            DataRef<DataType> dataref 
                    = new DataRef<DataType>(data);
            //static_cast<InPort<DataType>*>(m_directInPort).write(data);
            ((InPort)m_directInPort).write(dataref);
            return ReturnCode.PORT_OK;
*/
        }

        // normal case
        //OutPort out = (OutPort)m_outport;
        OutputStream cdr 
            = new EncapsOutputStreamExt(m_orb,m_isLittleEndian);
        //out.write_stream(data,cdr); 

	m_serializer.isLittleEndian(m_isLittleEndian);
        SerializeReturnCode ser_ret = m_serializer.serialize(data,cdr);
        if(ser_ret.equals(SerializeReturnCode.SERIALIZE_NOT_SUPPORT_ENDIAN)){
            rtcout.println(Logbuf.ERROR,
                "write(): endian %s is not support. "+ m_isLittleEndian);
            return ReturnCode.UNKNOWN_ERROR;
        }
	else if(ser_ret.equals(SerializeReturnCode.SERIALIZE_ERROR)){
            rtcout.println(Logbuf.ERROR,"unkown error." );
            return ReturnCode.UNKNOWN_ERROR;
        }
	else if(ser_ret.equals(SerializeReturnCode.SERIALIZE_NOTFOUND)){
            rtcout.println(Logbuf.ERROR,
                "write(): serializer %s is not support. "+
                m_marshaling_type);
            return ReturnCode.UNKNOWN_ERROR;
        }
        return m_publisher.write(cdr,0,0);
    }

    /**
     * {@.ja 接続解除}
     * {@.en disconnect}
     *
     * <p>
     * {@.ja consumer, publisher, buffer が解体・削除される。}
     * {@.en This operation destruct and delete the consumer, the publisher
     * and the buffer.}
     */
    public ReturnCode disconnect() {
        rtcout.println(Logbuf.TRACE, "disconnect()");
        onDisconnect();
        // delete publisher
        if (m_publisher != null) {
            rtcout.println(Logbuf.DEBUG, "delete publisher");
            PublisherBaseFactory<PublisherBase,String> pfactory 
                = PublisherBaseFactory.instance();
            pfactory.deleteObject(m_publisher.getName(),m_publisher);
        }
        m_publisher = null;
    
        // delete consumer
        if (m_consumer != null) {
            rtcout.println(Logbuf.DEBUG, "delete consumer");
            InPortConsumerFactory<InPortConsumer,String> cfactory 
                = InPortConsumerFactory.instance();
            cfactory.deleteObject(m_consumer);
        }
        m_consumer = null;

        // delete buffer
        if (m_buffer != null) {
            rtcout.println(Logbuf.DEBUG, "delete buffer");
            BufferFactory<BufferBase<OutputStream>,String> bfactory 
                = BufferFactory.instance();
            bfactory.deleteObject(m_buffer);
        }
        m_buffer = null;

        if(m_serializer != null){
            rtcout.println(Logbuf.DEBUG, "delete serializer");
        }
        m_serializer = null;

        rtcout.println(Logbuf.TRACE, "disconnect() done");
        return ReturnCode.PORT_OK;
    
    }
    /**
     *
     * {@.ja アクティブ化}
     * {@.en Connector activation}
     *
     * <p> 
     * {@.ja このコネクタをアクティブ化する}
     * {@.en This operation activates this connector}
     *
     */
    public void activate() {
        m_publisher.activate();
    }
    /**
     * {@.ja Buffer を取得する}
     * {@.en Getting Buffer}
     *
     * <p>
     * {@.ja Connector が保持している Buffer を返す}
     * {@.en This operation returns this connector's buffer}
     *
     * @return
     *   {@.ja Connector が保持している Buffer}
     *   {@.en Connector's buffer}
     */
    public BufferBase<OutputStream> getBuffer() {
        return m_buffer;
    }
    /**
     * {@.ja OutPortBaseを格納する。}
     * {@.en Stores OutPortBase.}
     *
     * @param outportbase
     *   {@.ja OutPortBase}
     *   {@.en OutPortBase}
     *
     */
    public void setOutPortBase(OutPortBase outportbase) {
        m_outport = outportbase;
    }

    /**
     * <p> Connector deactivation </p>
     *
     * <p> This operation deactivates this connector </p>
     *
     */
     public void deactivate() {
         m_publisher.deactivate();
     }
    
    /**
     * <p> create publisher </p>
     */
    protected PublisherBase createPublisher(ConnectorInfo profile) {
        String pub_type="";
        pub_type = profile.properties.getProperty("io_mode");
        if(pub_type.isEmpty()) {
            pub_type = profile.properties.getProperty("subscription_type",
                                                  "flush");
         
            if(pub_type.equals("flush")) {
                profile.properties.setProperty("io_mode","block");
            }
            else if(pub_type.equals("new")) {
                profile.properties.setProperty("io_mode","nonblock");
            }
            else {
                profile.properties.setProperty("io_mode",pub_type);
            }
            
        }
        pub_type = StringUtil.normalize(pub_type);
        PublisherBaseFactory<PublisherBase,String> factory  
                = PublisherBaseFactory.instance();
        return factory.createObject(pub_type);
    }

    /**
     * <p> create buffer </p>
     */
    protected BufferBase<OutputStream> createBuffer(ConnectorInfo profile) {
        String buf_type;
        buf_type = profile.properties.getProperty("buffer_type",
                                              "ring_buffer");
        BufferFactory<BufferBase<OutputStream>,String> factory 
                = BufferFactory.instance();
        return factory.createObject(buf_type);
    }
    /**
     * <p> Invoke callback when connection is established </p>
     */
    protected void onConnect() {
        m_listeners.connector_[ConnectorListenerType.ON_CONNECT].notify(m_profile);
    }

    /**
     * {@.ja 接続切断時にコールバックを呼ぶ}
     * {@.en Invoke callback when connection is destroied}
     */
    protected void onDisconnect() {
        m_listeners.connector_[ConnectorListenerType.ON_DISCONNECT].notify(
                                                                    m_profile);
    }

    /**
     * <p> InPortConsumer </p>
     */
    private InPortConsumer m_consumer;

    /**
     * <p> publisher </p>
     */
    private PublisherBase m_publisher;

    /**
     * <p> the buffer </p>
     */
    private BufferBase<OutputStream> m_buffer;
    private Streamable m_streamable = null;
    private Field m_field = null;
    private ORB m_orb;
    private OutPortBase m_outport;
    /**
     * <p> A reference to a ConnectorListener </p>
     */
    private ConnectorListeners m_listeners;

    private String m_marshaling_type;
    private CORBA_CdrSerializer m_serializer;

}

