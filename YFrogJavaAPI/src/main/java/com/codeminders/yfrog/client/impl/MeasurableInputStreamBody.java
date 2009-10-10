/**
 * 
 */
package com.codeminders.yfrog.client.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.content.AbstractContentBody;

/**
 * @author idemydenko
 *
 */
public class MeasurableInputStreamBody extends AbstractContentBody {
	private final InputStream in;
	private final String filename;
	private long length;
	
	public MeasurableInputStreamBody(final InputStream in, final String mimeType, final String filename) throws IOException {
		super(mimeType);
		if (in == null) {
			throw new IllegalArgumentException("Input stream may not be null");
		}
		this.in = measure(in);
		this.filename = filename;
	}
	
	public MeasurableInputStreamBody(final InputStream in, final String filename) throws IOException {
		this(in, "application/octet-stream", filename);
	}

	private InputStream measure(InputStream in) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		writeFromTo(in, baos);
		this.length = baos.size();
		return new ByteArrayInputStream(baos.toByteArray());
	}
	
	public void writeTo(final OutputStream out) throws IOException {
		writeFromTo(in, out);
    }
	
	private void writeFromTo(InputStream in, OutputStream out) throws IOException {
		if (in == null) {
			throw new IllegalArgumentException("Input stream may not be null");
		}

		if (out == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }

        try {
            byte[] tmp = new byte[4096];
            int l;
            while ((l = in.read(tmp)) != -1) {
                out.write(tmp, 0, l);
            }
            out.flush();
        } finally {
            in.close();
        }
		
	}
	
    public String getTransferEncoding() {
        return MIME.ENC_BINARY;
    }

    public String getCharset() {
        return null;
    }

    public long getContentLength() {
        return length;
    }

	public String getFilename() {
		return filename;
	}
}