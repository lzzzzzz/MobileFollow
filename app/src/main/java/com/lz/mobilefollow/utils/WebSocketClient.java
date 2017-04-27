//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.lz.mobilefollow.utils;

import com.lz.mobilefollow.lzcore.base.BaseApplication;
import com.lz.mobilefollow.lzcore.util.SharedPreferenceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketAdapter;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.exceptions.InvalidHandshakeException;
import org.java_websocket.framing.Framedata;
import org.java_websocket.framing.Framedata.Opcode;
import org.java_websocket.handshake.HandshakeImpl1Client;
import org.java_websocket.handshake.Handshakedata;
import org.java_websocket.handshake.ServerHandshake;

public abstract class WebSocketClient extends WebSocketAdapter implements Runnable, WebSocket {
    protected URI uri;
    private WebSocketImpl engine;
    private Socket socket;
    private InputStream istream;
    private OutputStream ostream;
    private Proxy proxy;
    private Thread writeThread;
    private Draft draft;
    private Map<String, String> headers;
    private CountDownLatch connectLatch;
    private CountDownLatch closeLatch;
    private int connectTimeout;

    public WebSocketClient(URI serverURI) {
        this(serverURI, new Draft_17());
    }

    public WebSocketClient(URI serverUri, Draft draft) {
        this(serverUri, draft, (Map)null, 0);
    }

    public WebSocketClient(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders, int connectTimeout) {
        this.uri = null;
        this.engine = null;
        this.socket = null;
        this.proxy = Proxy.NO_PROXY;
        this.connectLatch = new CountDownLatch(1);
        this.closeLatch = new CountDownLatch(1);
        this.connectTimeout = 0;
        if(serverUri == null) {
            throw new IllegalArgumentException();
        } else if(protocolDraft == null) {
            throw new IllegalArgumentException("null as draft is permitted for `WebSocketServer` only!");
        } else {
            this.uri = serverUri;
            this.draft = protocolDraft;
            this.headers = httpHeaders;
            this.connectTimeout = connectTimeout;
            this.engine = new WebSocketImpl(this, protocolDraft);
        }
    }

    public URI getURI() {
        return this.uri;
    }

    public Draft getDraft() {
        return this.draft;
    }

    public void connect() {
        if(this.writeThread != null) {
            throw new IllegalStateException("WebSocketClient objects are not reuseable");
        } else {
            this.writeThread = new Thread(this);
            this.writeThread.start();
        }
    }

    public boolean connectBlocking() throws InterruptedException {
        this.connect();
        this.connectLatch.await();
        return this.engine.isOpen();
    }

    public void close() {
        if(this.writeThread != null) {
            this.engine.close(1000);
        }

    }

    public void closeBlocking() throws InterruptedException {
        this.close();
        this.closeLatch.await();
    }

    public void send(String text) throws NotYetConnectedException {
        this.engine.send(text);
    }

    public void send(byte[] data) throws NotYetConnectedException {
        this.engine.send(data);
    }

    public void run() {
        try {
            if(this.socket == null) {
                this.socket = new Socket(this.proxy);
            } else if(this.socket.isClosed()) {
                throw new IOException();
            }

            if(!this.socket.isBound()) {
                this.socket.connect(new InetSocketAddress(this.uri.getHost(), this.getPort()), this.connectTimeout);
            }

            this.istream = this.socket.getInputStream();
            this.ostream = this.socket.getOutputStream();
            this.sendHandshake();
        } catch (Exception var6) {
            this.onWebsocketError(this.engine, var6);
            this.engine.closeConnection(-1, var6.getMessage());
            return;
        }

        this.writeThread = new Thread(new WebSocketClient.WebsocketWriteThread());
        this.writeThread.start();
        byte[] rawbuffer = new byte[WebSocketImpl.RCVBUF];

        try {
            int readBytes;
            while(!this.isClosed() && (readBytes = this.istream.read(rawbuffer)) != -1) {
                this.engine.decode(ByteBuffer.wrap(rawbuffer, 0, readBytes));
            }

            this.engine.eot();
        } catch (IOException var4) {
            this.engine.eot();
        } catch (RuntimeException var5) {
            this.onError(var5);
            this.engine.closeConnection(1006, var5.getMessage());
        }

        assert this.socket.isClosed();

    }

    private int getPort() {
        int port = this.uri.getPort();
        if(port == -1) {
            String scheme = this.uri.getScheme();
            if(scheme.equals("wss")) {
                return 443;
            } else if(scheme.equals("ws")) {
                return 80;
            } else {
                throw new RuntimeException("unkonow scheme" + scheme);
            }
        } else {
            return port;
        }
    }

    private void sendHandshake() throws InvalidHandshakeException {
        String part1 = this.uri.getPath();
        String part2 = this.uri.getQuery();
        String path;
        if(part1 != null && part1.length() != 0) {
            path = part1;
        } else {
            path = "/";
        }

        if(part2 != null) {
            path = path + "?" + part2;
        }

        int port = this.getPort();
        String host = this.uri.getHost() + (port != 80?":" + port:"");
        HandshakeImpl1Client handshake = new HandshakeImpl1Client();
        handshake.setResourceDescriptor(path);
        handshake.put("Host", host);
        String user_id=String.valueOf(SharedPreferenceUtils.getSPInt(BaseApplication.getCurrentContext(),"userId"));
        handshake.put("Origin", String.valueOf(user_id));
        if(this.headers != null) {
            Iterator i$ = this.headers.entrySet().iterator();

            while(i$.hasNext()) {
                Entry kv = (Entry)i$.next();
                handshake.put((String)kv.getKey(), (String)kv.getValue());
            }
        }

        this.engine.startHandshake(handshake);
    }

    public READYSTATE getReadyState() {
        return this.engine.getReadyState();
    }

    public final void onWebsocketMessage(WebSocket conn, String message) {
        this.onMessage(message);
    }

    public final void onWebsocketMessage(WebSocket conn, ByteBuffer blob) {
        this.onMessage(blob);
    }

    public void onWebsocketMessageFragment(WebSocket conn, Framedata frame) {
        this.onFragment(frame);
    }

    public final void onWebsocketOpen(WebSocket conn, Handshakedata handshake) {
        this.connectLatch.countDown();
        this.onOpen((ServerHandshake)handshake);
    }

    public final void onWebsocketClose(WebSocket conn, int code, String reason, boolean remote) {
        this.connectLatch.countDown();
        this.closeLatch.countDown();
        if(this.writeThread != null) {
            this.writeThread.interrupt();
        }

        try {
            if(this.socket != null) {
                this.socket.close();
            }
        } catch (IOException var6) {
            this.onWebsocketError(this, var6);
        }

        this.onClose(code, reason, remote);
    }

    public final void onWebsocketError(WebSocket conn, Exception ex) {
        this.onError(ex);
    }

    public final void onWriteDemand(WebSocket conn) {
    }

    public void onWebsocketCloseInitiated(WebSocket conn, int code, String reason) {
        this.onCloseInitiated(code, reason);
    }

    public void onWebsocketClosing(WebSocket conn, int code, String reason, boolean remote) {
        this.onClosing(code, reason, remote);
    }

    public void onCloseInitiated(int code, String reason) {
    }

    public void onClosing(int code, String reason, boolean remote) {
    }

    public WebSocket getConnection() {
        return this.engine;
    }

    public InetSocketAddress getLocalSocketAddress(WebSocket conn) {
        return this.socket != null?(InetSocketAddress)this.socket.getLocalSocketAddress():null;
    }

    public InetSocketAddress getRemoteSocketAddress(WebSocket conn) {
        return this.socket != null?(InetSocketAddress)this.socket.getRemoteSocketAddress():null;
    }

    public abstract void onOpen(ServerHandshake var1);

    public abstract void onMessage(String var1);

    public abstract void onClose(int var1, String var2, boolean var3);

    public abstract void onError(Exception var1);

    public void onMessage(ByteBuffer bytes) {
    }

    public void onFragment(Framedata frame) {
    }

    public void setProxy(Proxy proxy) {
        if(proxy == null) {
            throw new IllegalArgumentException();
        } else {
            this.proxy = proxy;
        }
    }

    public void setSocket(Socket socket) {
        if(this.socket != null) {
            throw new IllegalStateException("socket has already been set");
        } else {
            this.socket = socket;
        }
    }

    public void sendFragmentedFrame(Opcode op, ByteBuffer buffer, boolean fin) {
        this.engine.sendFragmentedFrame(op, buffer, fin);
    }

    public boolean isOpen() {
        return this.engine.isOpen();
    }

    public boolean isFlushAndClose() {
        return this.engine.isFlushAndClose();
    }

    public boolean isClosed() {
        return this.engine.isClosed();
    }

    public boolean isClosing() {
        return this.engine.isClosing();
    }

    public boolean isConnecting() {
        return this.engine.isConnecting();
    }

    public boolean hasBufferedData() {
        return this.engine.hasBufferedData();
    }

    public void close(int code) {
        this.engine.close();
    }

    public void close(int code, String message) {
        this.engine.close(code, message);
    }

    public void closeConnection(int code, String message) {
        this.engine.closeConnection(code, message);
    }

    public void send(ByteBuffer bytes) throws IllegalArgumentException, NotYetConnectedException {
        this.engine.send(bytes);
    }

    public void sendFrame(Framedata framedata) {
        this.engine.sendFrame(framedata);
    }

    public InetSocketAddress getLocalSocketAddress() {
        return this.engine.getLocalSocketAddress();
    }

    public InetSocketAddress getRemoteSocketAddress() {
        return this.engine.getRemoteSocketAddress();
    }

    public String getResourceDescriptor() {
        return this.uri.getPath();
    }

    private class WebsocketWriteThread implements Runnable {
        private WebsocketWriteThread() {
        }

        public void run() {
            Thread.currentThread().setName("WebsocketWriteThread");

            try {
                while(!Thread.interrupted()) {
                    ByteBuffer e = (ByteBuffer)WebSocketClient.this.engine.outQueue.take();
                    WebSocketClient.this.ostream.write(e.array(), 0, e.limit());
                    WebSocketClient.this.ostream.flush();
                }
            } catch (IOException var2) {
                WebSocketClient.this.engine.eot();
            } catch (InterruptedException var3) {
                ;
            }

        }
    }
}
