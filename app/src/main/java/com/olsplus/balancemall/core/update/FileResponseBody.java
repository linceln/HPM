package com.olsplus.balancemall.core.update;

import com.olsplus.balancemall.core.util.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;


public class FileResponseBody extends ResponseBody {

    private ResponseBody responseBody;

    private BufferedSource bufferedSource;

    public FileResponseBody(ResponseBody responseBody) {
        this.responseBody = responseBody;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {

            long bytesRead = 0;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                this.bytesRead += bytesRead == -1 ? 0 : bytesRead;
                //实时发送当前已读取的字节和总字节
//                RxBus.getInstance().post(new FileLoadEvent(contentLength(), this.bytesRead));
//                LogUtil.e("Send233", this.bytesRead + "/" + contentLength());
                EventBus.getDefault().post(new FileSizeEvent(this.bytesRead, contentLength()));
                return bytesRead;
            }
        };
    }
}
