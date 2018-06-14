package com.awen.push.core.impl.blockio.io;

import com.awen.push.core.impl.abilities.IWriter;
import com.awen.push.core.impl.exceptions.WriteException;
import com.awen.push.core.sdk.bean.IPulseSendable;
import com.awen.push.core.sdk.OkSocketOptions;
import com.awen.push.core.sdk.bean.ISendable;
import com.awen.push.core.sdk.connection.abilities.IStateSender;
import com.awen.push.core.sdk.connection.interfacies.IAction;
import com.awen.push.core.utils.BytesUtils;
import com.awen.push.core.utils.SL;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by xuhao on 2017/5/31.
 */

public class WriterImpl implements IWriter {

    private OkSocketOptions mOkOptions;

    private IStateSender mStateSender;

    private OutputStream mOutputStream;

    private LinkedBlockingQueue<ISendable> mQueue = new LinkedBlockingQueue<>();

    public WriterImpl(OutputStream outputStream, IStateSender stateSender) {
        mStateSender = stateSender;
        mOutputStream = outputStream;
    }

    @Override
    public boolean write() throws RuntimeException {
        ISendable sendable = null;
        try {
            sendable = mQueue.take();
        } catch (InterruptedException e) {
            //ignore;
        }

        if (sendable != null) {
            try {
                byte[] sendBytes = sendable.parse();
                int packageSize = mOkOptions.getWritePackageBytes();
                int remainingCount = sendBytes.length;
                ByteBuffer writeBuf = ByteBuffer.allocate(packageSize);
                writeBuf.order(mOkOptions.getWriteOrder());
                int index = 0;
                while (remainingCount > 0) {
                    int realWriteLength = Math.min(packageSize, remainingCount);
                    writeBuf.clear();
                    writeBuf.rewind();
                    writeBuf.put(sendBytes, index, realWriteLength);
                    writeBuf.flip();
                    byte[] writeArr = new byte[realWriteLength];
                    writeBuf.get(writeArr);
                    mOutputStream.write(writeArr);
                    mOutputStream.flush();

                    if (OkSocketOptions.isDebug()) {
                        byte[] forLogBytes = Arrays.copyOfRange(sendBytes, index, index + realWriteLength);
                        SL.i("write bytes: " + BytesUtils.toHexStringForLog(forLogBytes));
                        SL.i("bytes write length:" + realWriteLength);
                    }

                    index += realWriteLength;
                    remainingCount -= realWriteLength;
                }
                if (sendable instanceof IPulseSendable) {
                    mStateSender.sendBroadcast(IAction.ACTION_PULSE_REQUEST, sendable);
                } else {
                    mStateSender.sendBroadcast(IAction.ACTION_WRITE_COMPLETE, sendable);
                }
            } catch (Exception e) {
                WriteException writeException = new WriteException(e);
                throw writeException;
            }
            return true;
        }
        return false;
    }

    @Override
    public void setOption(OkSocketOptions option) {
        mOkOptions = option;
    }

    @Override
    public void offer(ISendable sendable) {
        mQueue.offer(sendable);
    }

    @Override
    public void close() {
        if (mOutputStream != null) {
            try {
                mOutputStream.close();
            } catch (IOException e) {
                //ignore
            }
        }
    }


}
