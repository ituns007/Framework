package org.ituns.framework.master.tools.storage;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class IStream {

    public static Input input(InputStream is) {
        return new Input(is);
    }

    public static Output output(OutputStream os) {
        return new Output(os);
    }

    public static class Input {
        private InputStream is;

        private Input(InputStream is) {
            this.is = is;
        }

        public byte[] readBytes() {
            try {
                int len = 0;
                byte[] bytes = new byte[8192];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((len = is.read(bytes)) != -1) {
                    baos.write(bytes, 0,  len);
                }
                is.close();
                return baos.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static class Output {
        private OutputStream os;

        private Output(OutputStream os) {
            this.os = os;
        }

        public boolean write(byte[] bytes) {
            try {
                os.write(bytes);
                os.flush();
                os.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        public boolean write(InputStream is) {
            try {
                int len = 0;
                byte[] bytes = new byte[8192];
                while ((len = is.read(bytes)) != -1) {
                    os.write(bytes, 0,  len);
                }
                os.flush();
                os.close();
                is.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
